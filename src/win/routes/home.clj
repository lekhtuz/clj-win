(ns win.routes.home
  (:require
    [clojure.tools.logging :as log :refer [info]]
    [compojure.core :refer :all]
    [ring.util.response :as resp]
    [win.views.layout :as layout]
    [win.views.login :as login]
    [win.views.app :as app]
  )
)

(defroutes home-routes
  (GET "/" [] (login/home))
  (GET "/login" [username login_failed] (login/home username login_failed))
  (POST "/login" [username password] (constantly ""))
  
  (GET "/Application" {session :session}
       (do
         (log/info "/Application: checking session =" session)
         (if-let [identity (:cemerick.friend/identity session)]
           (app/home ((:authentications identity) (:current identity)))
           (resp/redirect "/")
         )
       )
  )

  (GET "/ResetPassword" [] (login/reset-password))
)
