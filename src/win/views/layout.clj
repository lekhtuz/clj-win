(ns win.views.layout
  (:require 
    [hiccup.page :refer [html5 include-css]]
    [hiccup.element :refer [image link-to unordered-list]]
  )
)

(defn menu []
  [:div.menu
   (unordered-list
     (list
       [:span
        (link-to { :class "hide" } "/" "Home")
        (unordered-list
          (list
            (link-to { :class "hide" } "/" "Menu 1 1")
            (link-to { :class "hide" } "/" "Menu 1 2")
          )
        )
       ]
       [:span
        (link-to { :class "hide" } "/" "Orders")
        (unordered-list
          (list
            (link-to { :class "hide" } "/" "New Blank Order")
            [:span
             (link-to { :class "hide" } "/" "New from Template")
             (unordered-list
               (list
                 (link-to { :class "hide" } "/" "Template 1")
                 (link-to { :class "hide" } "/" "Template 2")
               )
             )
            ]
          )
        )
       ]
       (link-to { :class "hide" } "/" "Templates")
       (link-to { :class "hide" } "/" "Spot Quotes")
       (link-to { :class "hide" } "/" "Tools")
       [:span
        (link-to { :class "hide" } "/" "Master Data")
        (unordered-list
          (list
            (link-to { :class "hide" } "/" "Carriers")
            (link-to { :class "hide" } "/" "Equipment Types")
            (link-to { :class "hide" } "/" "Special Services")
            (link-to { :class "hide" } "/" "SQ Configuration")
            (link-to { :class "hide" } "/" "Routing Guide Sources")
            (link-to { :class "hide" } "/" "Consignee Configuration")
          )
        )
       ]
       [:span
        (link-to { :class "hide" } "/" "Administration")
        (unordered-list
          (list
            (link-to { :class "hide" } "/" "Users")
            (link-to { :class "hide" } "/" "Roles")
            (link-to { :class "hide" } "/" "Data Filters")
          )
        )
       ]
       [:span
        (link-to { :class "hide" } "/" "Settings")
        (unordered-list
          (list
            (link-to { :class "hide" } "/" "User")
            (link-to { :class "hide" } "/" "Notifications")
            (link-to { :class "hide" } "/Application/orgsettings" "Organization")
          )
        )
       ]
       [:span
        (link-to { :class "hide" } "/" "Help")
        (unordered-list
          (list
            (link-to { :class "hide" } "/" "Log Issue")
          )
        )
       ]
     )
   )
  ]
)

(defn common [current-user & body]
  (html5
    [:head
     [:title "Web Integrated Network"]
     (include-css "/css/screen.css")
     (include-css "/css/dropdown.css")
    ]
    [:body#app 
     [:div#head
       [:table { :width "100%", :cellpadding "0", :cellspacing "0" }
        [:tr
         [:td#logo { :valign "center" } (image "/img/title_logo.png")]
         [:td#company-name { :valign "center" } (:organizationname current-user) ]
         [:td#welcome { :valign "center" } "Welcome " (:firstname current-user) " " (:lastname current-user) " (role)"]
         [:td#change-role { :valign "center" } (link-to "" "Change role")]
         [:td#logout { :valign "center" } (link-to "" "Logout")]
         [:td#comet-indicator { :valign "center" } (image "/img/bullet-red.png")]
         ]
        ]
     ]
     (menu)
     body
    ]
  )
)
