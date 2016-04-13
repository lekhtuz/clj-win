(ns win.views.orgsettings
  (:require
    [clojure.tools.logging :as log :refer [info]]
    [hiccup.element :refer [image javascript-tag link-to unordered-list]]
    [hiccup.form :refer [check-box drop-down email-field file-upload form-to submit-button text-field]]
    [hiccup.page :refer [include-js]]
    [win.views.layout :as layout]
  )
)

(def order-bol-mapping
  [
   ["Order#" "ORDER_NUM"]
   ["Purchase Order#" "PO_NUMBER"]
   ["Sales Order#" "SALES_ORDER_NUMBER"]
   ["Pickup Number" "PICKUP_NUMBER"]
  ]
)

(def timeout-units
  [
   ["minuite(s)" "minuite(s)"]
   ["hours(s)" "hours(s)"]
   ["days(s)" "days(s)"]
  ]
)

(defn home [current-user orgsettings]
  (log/info "orgsettings/home: current-user =" current-user)
  (log/info "orgsettings/home: orgsettings =" orgsettings)
  (layout/common current-user
    (include-js "/js/orgsettings.js")
    (form-to {:enctype "multipart/form-data"} [:post ""]
      [:center
       [:table#orgsettings
        [:tr
         [:th {:colspan 2} "Organization settings - " (:organization-name current-user)]
        ]
        [:tr
         [:td {:width "50%"} "The \"E-Mail From\" address which is used to send Tender Requests"]
         [:td {:width "50%"} (email-field :booking-email-from (:booking-email-from orgsettings))]
        ]
        [:tr
         [:td {:colspan 2} (check-box :copy-user-info (:copy-user-info orgsettings)) "Copy user's contact information on Order creation"]
        ]
        [:tr
         [:td {:colspan 2} (check-box :fill-freight-cost (:fill-freight-cost orgsettings)) "Send Freight Cost information to Carriers in Tender Requests"]
        ]
        [:tr
         [:td {:colspan 2} (check-box :external-update-ui (:external-update-ui orgsettings)) "Enable UI-components for handling External Updates (Restart is needed)"]
        ]
        [:tr
         [:td {:colspan 2} (check-box :unique-bol-number (:unique-bol-number orgsettings)) "Enable duplicate BOL# warnings"]
        ]
        [:tr
         [:td {:colspan 2}
          (check-box :order-bol-mapping-cb (:order-bol-mapping orgsettings))
          "Enable automatic creation of BOL# using "
          (drop-down :order-bol-mapping order-bol-mapping (:order-bol-mapping orgsettings))
         ]
        ]
        [:tr
         [:td {:colspan 2} (check-box :customer-order-section (:customer-order-section orgsettings)) "Enable UI for Customer Order Section"]
        ]
        [:tr
         [:td {:colspan 2}
          [:fieldset
           [:legend "Auto Tendering"]
            (check-box :start-auto-tendering (:start-auto-tendering orgsettings)) "Start Auto Tendering using Timeout " 
            (text-field {:type "number" :size "3" :maxlength "5" :min "0"} :ast-wait-timeout-amount (:ast-wait-timeout-amount orgsettings)) 
            (drop-down :ast-wait-timeout-uom timeout-units (:ast-wait-timeout-uom orgsettings)) [:br]
            (check-box :ast-need-review (:ast-need-review orgsettings)) "Confirmation Needed" 
          ]
         ]
        ]
        [:tr
         [:td {:colspan 2} (check-box :use-logo-in-bol) "Use Logo in BOL"]
        ]
        [:tr#logo-groupbox
         [:td {:colspan 2}
          [:fieldset
           [:legend "Logo"]
           (file-upload {:accept "image/*"} :logo-upload)
           (image {:onclick "javascript:removeFileToUpload();"} "/img/Red-Cross-20px.png")
           [:fieldset {:style "margin-top: 5"}
            "Current logo is displayed here"
           ]
          ]
         ]
        ]
        [:tr
         [:td {:colspan 2 :align "right"} (submit-button {:onclick "javascript:validateForm();"}"Save Settings")]
        ]
       ]
      ]
    )
  )
)
