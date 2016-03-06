(ns win.schema
  (:require
    [carica.core :as cc]
    [korma.core :refer [database defentity entity-fields where select select* as-sql sql-only dry-run exec table has-many belongs-to fields with sqlfn transform]]
    [korma.db :refer [defdb oracle]]
    [clojure.pprint :refer [pprint]]
    [clojure.string :as str]
    [clojure.tools.logging :as log :refer [info]]
  )
)

(def db-profile ((cc/config :db-key) (cc/config :db-profiles)))

(defdb nn-db
  (oracle {
           :subname (str "@//" (:host db-profile) ":" (:port db-profile) "/" (:sid db-profile))
           :user (:user db-profile)
           :password (:password db-profile)
           :naming {:keys str/lower-case :fields str/upper-case}
          }
  )
)

(declare address_book_entry user organization)

(defentity user
  (database nn-db)
  (table :bouser)
  (belongs-to address_book_entry {:fk :addrbookentryid})
  (belongs-to organization {:fk :organizationid})
  (transform (fn [{cwa-accepted :cwa-accepted last-login-date :last-login-date :as li}]
               (log/info "login-info transform: li =" li)
               (merge li
                      { :cwa-accepted (= cwa-accepted 1M) }
                      { :account-locked (nil? last-login-date) }
                      { :account-expired false, :credentials-expired false, :account-enabled true }
               )
             )
  )
)

(defentity organization
  (database nn-db)
  (table :boorganization)
  (has-many user {:fk :organizationid})
)

(defentity address_book_entry
  (database nn-db)
  (table :boaddrbookentry)
  (has-many user {:fk :addrbookentryid})
)

(defn login-info [username]
  (-> (select* user)
    (with address_book_entry)
    (with organization
      (where (> :transactionnum 0))
    )
    (fields [:id :userid] :username :password :organizationid [:lastLoginDate :last-login-date]
            :boaddrbookentry.firstname :boaddrbookentry.lastname :boaddrbookentry.email :boaddrbookentry.phoneno :boaddrbookentry.faxno
            [:boaddrbookentry.win_cw_agr_accepted :cwa-accepted]
            :boorganization.orgtype :boorganization.organizationname)
    (where (and (= (sqlfn :lower :username) (str/lower-case username)) (> :transactionnum 0)))
  )
)

(defn start []
  (log/info "Starting db ...")
  (log/info "db-profile =" db-profile)
  (log/info "nn-db =" nn-db)
  (log/info "user =" user)
  (log/info "organization =" organization)
  (log/info "address_book_entry =" address_book_entry)
  (log/info "login-info =" (login-info "ey@acme.com"))
  (log/info "login-info(SQL) =" (as-sql (login-info "ey@acme.com")))
  (log/info "login-info(EXEC) =" (exec (login-info "ey@acme.com")))
  (log/info "DB started!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
)

(defn stop []
  (log/info "Stopping db ...")
)

(defn load-user [username]
  (log/info "load-user: username =" username)
  (if-let [li (select (login-info username))]
    (do 
      (log/info "load-user: li =" li)
      (first li)
    )
  )
)
