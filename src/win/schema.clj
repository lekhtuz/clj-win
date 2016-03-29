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
(def nn-db-profile (:nn db-profile))
(def ng-db-profile (:ng db-profile))

(defdb nn-db
  (oracle {
           :subname (str "@//" (:host nn-db-profile) ":" (:port nn-db-profile) "/" (:sid nn-db-profile))
           :user (:user nn-db-profile)
           :password (:password nn-db-profile)
           :naming {:keys str/lower-case :fields str/upper-case}
          }
  )
)

(defdb ng-db
  (oracle {
           :subname (str "@//" (:host ng-db-profile) ":" (:port ng-db-profile) "/" (:sid ng-db-profile))
           :user (:user ng-db-profile)
           :password (:password ng-db-profile)
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
  (transform (fn [{cwa-accepted :cwa-accepted last-login-date :last-login-date nn-org-id :nn-org-id userid :userid :as li}]
               (log/info "login-info transform: li =" li)
               (assoc li
                      :cwa-accepted (= cwa-accepted 1M)
                      :account-locked (nil? last-login-date)
                      :account-expired false
                      :credentials-expired false
                      :account-enabled true
                      :nn-org-id (int nn-org-id)
                      :userid (int userid)
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
    (fields [:id :userid] :username :password [:organizationid :nn-org-id] [:lastLoginDate :last-login-date]
            :boaddrbookentry.firstname :boaddrbookentry.lastname :boaddrbookentry.email :boaddrbookentry.phoneno :boaddrbookentry.faxno
            [:boaddrbookentry.win_cw_agr_accepted :cwa-accepted]
            :boorganization.orgtype [:boorganization.organizationname :organization-name])
    (where (and (= (sqlfn :lower :username) (str/lower-case username)) (> :transactionnum 0)))
  )
)

(declare organization-settings)

(defentity organization-settings
  (database ng-db)
  (table :md_saas_org_settings)
  (transform (fn [{copy-user-info :copy-user-info fill-freight-cost :fill-freight-cost :as os}]
               (log/info "organization-settings transform: os =" os)
               (merge os
                      { :copy-user-info (= (:copy-user-info os) 1M) }
                      { :fill-freight-cost (= (:fill-freight-cost os) 1M) }
                      { :external-update-ui (= (:external-update-ui os) 1M) }
                      { :unique-bol-number (= (:unique-bol-number os) 1M) }
                      { :customer-order-section (= (:customer-order-section os) 1M) }
                      { :start-auto-tendering (= (:start-auto-tendering os) 1M) }
                      { :ast-need-review (= (:ast-need-review os) 1M) }
               )
             )
  )
)

(defn orgsettings-info [org-id]
  (-> (select* organization-settings)
    (fields :id [:org_id :org-id] [:booking_email_from :booking-email-from] [:copy_userinfo :copy-user-info]
            [:fill_freight_cost :fill-freight-cost] [:external_update_ui :external-update-ui] 
            [:unique_bol_number :unique-bol-number] [:order_bol_mapping :order-bol-mapping]
            [:customer_order_section :customer-order-section] [:start_auto_tendering :start-auto-tendering]
            [:ast_wait_timeout_amount :ast-wait-timeout-amount] [:ast_wait_timeout_uom :ast-wait-timeout-uom]
            [:ast_need_review :ast-need-review])
    (where (= :org_id org-id))
  )
)
  
(defentity md-organization
  (database ng-db)
  (table :md_organization)
  (transform (fn [{id :id :as mo}]
               (log/info "md-organization transform: mo =" mo)
               (assoc mo :id (int id))
             )
  )
)

(defn md-organization-info [nn-org-id]
  (-> (select* md-organization)
    (fields :id)
    (where (and (= :md_ref_value nn-org-id) (= :md_ref_type "NN")))
  )
)

(defn- load-first-record [sqlfn param]
  (if-let [result (exec (sqlfn param))]
    (do
      (log/info "load-first-record: result =" result)
      (first result)
    )
  )
)

(defn load-user [username]
  (log/info "load-user: username =" username)
  (load-first-record login-info username)
)

(defn load-organization [id]
  (log/info "load-organization: id =" id)
  (load-first-record orgsettings-info id)
)

(defn get-organization-ngid [nn-id]
  (log/info "get-organization-ngid: nn-id =" nn-id)
  (:id (load-first-record md-organization-info nn-id))
)

(defn start []
  (log/info "Starting db ...")
  (log/info "db-profile =" db-profile)
  (log/info "nn-db =" nn-db)
  (log/info "ng-db =" ng-db)
  (log/info "DB started!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
)

(defn stop []
  (log/info "Stopping db ...")
)

