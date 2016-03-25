(ns win.views.orgsettings
  (:require
    [clojure.tools.logging :as log :refer [info]]
    [hiccup.element :refer [image link-to unordered-list]]
    [hiccup.form :refer [check-box drop-down file-upload form-to text-field]]
    [hiccup.page :refer [include-js]]
    [win.views.layout :as layout]
  )
)

(def enable-bol-using
  [
   ["Order#" :order-number]
   ["Purchase Order#" :purchase-order-number]
   ["Sales Order#" :sales-order-number]
   ["Pickup Number" :pickup-number]
  ]
)

(def timeout-units
  [
   ["minuite(s)" :ms]
   ["hours(s)" :hs]
   ["days(s)" :ds]
  ]
)

(defn home [{current-user :current-user :as current-authentication}]
  (log/info "orgsettings/home: current-user =" current-user)
  (layout/common current-user
    (include-js "/js/orgsettings.js")
    (form-to [:post ""]
      [:center
       [:table#orgsettings
        [:tr
         [:th {:colspan 2} "Organization settings"]
        ]
        [:tr
         [:td {:width "50%"} "The \"E-Mail From\" address which is used to send Tender Requests"]
         [:td {:width "50%"} (text-field :from-email "")]
        ]
        [:tr
         [:td {:colspan 2} (check-box :copy-contact) "Copy user's contact information on Order creation"]
        ]
        [:tr
         [:td {:colspan 2} (check-box :send-cost-info) "Send Freight Cost information to Carriers in Tender Requests"]
        ]
        [:tr
         [:td {:colspan 2} (check-box :enable-ui-external) "Enable UI-components for handling External Updates (Restart is needed)"]
        ]
        [:tr
         [:td {:colspan 2} (check-box :enable-duplicate-warning) "Enable duplicate BOL# warnings"]
        ]
        [:tr
         [:td {:colspan 2}
          (check-box :enable-bol) "Enable automatic creation of BOL# using " (drop-down :enable-bol-using enable-bol-using)
         ]
        ]
        [:tr
         [:td {:colspan 2} (check-box :enable-duplicate-warning) "Enable UI for Customer Order Section"]
        ]
        [:tr
         [:td {:colspan 2}
          [:fieldset
           [:legend "Auto Tendering"]
            (check-box :start-auto-tendering) "Start Auto Tendering using Timeout " 
            (text-field {:type "number" :size "3" :maxlength "5"}:timeout) 
            (drop-down :timeout-unit timeout-units) [:br]
            (check-box :confirmation-needed) "Confirmation Needed" 
          ]
         ]
        ]
        [:tr
         [:td {:colspan 2} (check-box :use-logo-in-bol) "Use Logo in BOL"]
        ]
        [:tr
         [:td {:colspan 2}
          [:fieldset
           [:legend "Logo"]
           (file-upload {:accept "image/*"} :logo-upload)
           (image {:onclick "javascript:removeFileToUpload();"} "/img/Red-Cross-20px.png")
          ]
         ]
        ]
       ]
      ]
    )
  )
)
