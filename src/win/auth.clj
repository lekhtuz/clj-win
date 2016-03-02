(ns win.auth
  (:require
    [clojure.pprint :refer [pprint]]
    [clojure.string :as str :refer [trim]]
    [clojure.tools.logging :as log :refer [info]]
    [cemerick.friend.workflows :as workflows]
    [base64-clj.core :as b64 :refer [decode]]
    [digest :as digest :refer [md5]]
    [win.schema :as schema :refer [load-user]]
;    [win.views.login :as login :refer [record-failed-login]]
  )
)

;(def all-roles (vals schema/usertype-role))

(defn printarrhex [ba] (areduce ba i ret "" (str ret (format "%02x" (aget ba i)))))
(defn printarrdec [ba] (str (reduce #(str %1 " " (format "%d" %2)) ba)))

(defn signed-byte [b]
  (byte (if (> b 127) (- b 256) b))
)

(defn password-match [raw enc]
  (log/info "password-match: raw =" raw ", enc =" enc)
  (let [md5hash (digest/md5 raw) enc-bytes (.getBytes enc) enc-bytes-decoded (b64/decode-bytes enc-bytes)]
    (log/info "password-match: md5hash =" md5hash)
    (log/info "password-match: enc-bytes =" (printarrhex enc-bytes) (printarrdec enc-bytes))
    (log/info "password-match: enc-bytes-decoded =" (printarrhex enc-bytes-decoded) (printarrdec enc-bytes-decoded))
    (= md5hash (printarrhex enc-bytes-decoded))
  )
)

(defn authenticate [{username :username password :password :as credentials}]
  (log/info "authenticate: username =" username ", password =" password ", credentials =" credentials)
  (if-let [ user (schema/load-user username) ]
    (if (password-match password (:password user))
      { :identity user :roles nil }
    )
  )
)

(defn login-failure-handler [request]
  (log/info "login-failure-handler: request =" (with-out-str (pprint request)))
;  (login/record-failed-login (-> request :params :username))
  (workflows/interactive-login-redirect request)
)
 