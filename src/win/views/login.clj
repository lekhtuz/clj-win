(ns win.views.login
  (:require
    [carica.core :as cc]
    [hiccup.element :refer [image link-to unordered-list]]
    [hiccup.form :refer [label form-to text-field password-field submit-button]]
    [hiccup.page :refer [html5 include-css]]
    [win.views.layout :as layout]
  )
)

(def message-login-error (cc/config :message.login-error))

(defn home 
  ([] (home nil nil))
  ([username login_failed]    
    (html5
      [:head
       [:title "Web Integrated Network: Login Page"]
       (include-css "/css/screen.css")]
       [:body#login
        [:div#top (image "img/win_logo.png")]
        (form-to [ :post "/login" ]
          [:table#login-box { :cellspacing 0, :cellpadding 0 }
           [:tr
            [:td.box-title { :colspan 2 } "WIN Login" ]
           ]
           [:tr
            [:td { :colspan 2 }
             (if (= login_failed "Y") 
               [:div.error-message message-login-error] "&nbsp;")
            ]
           ]
           [:tr
            [:td.info-label "User Name:" ]
            [:td.info-field  (text-field :username)]
           ]
           [:tr
            [:td.info-label "Password:" ]
            [:td.info-field  (password-field :password)]
           ]
           [:tr
            [:td { :colspan 2 } "&nbsp;"]
           ]
           [:tr
            [:td.info-label.forgot-password (link-to "/ResetPassword" "Forgot&nbsp;Password?") ]
            [:td.info-field (submit-button "Login") ]
           ]
          ]
        )
        [:div#bottom "&copy; Odyssey Logistics &amp; Technology Corporation"]
       ]
    )
  )
)

(defn reset-password []
  (html5
    [:head
     [:title "Web Integrated Network: Reset Password"]
     (include-css "/css/screen.css")
    ]
    [:body#login
     [:div#top (image "img/win_logo.png")]
     (form-to [ :post "/ResetPassword" ]
       [:table#reset-password-box { :cellspacing 0, :cellpadding 0 }
        [:tr
         [:td.box-title { :colspan 2 } "Reset Password" ]
        ]
        [:tr
         [:td.info-label { :colspan 2 } "Please enter your username below and we will email you a link to reset your password."]
        ]
        [:tr
         [:td.info-label "User&nbsp;Name:" ]
         [:td.info-field  (text-field :username)]
        ]
        [:tr
         [:td { :colspan 2 } "&nbsp;"]
        ]
        [:tr
         [:td.info-field { :colspan 2 } (submit-button "Reset Password") ]
        ]
       ]
     )
     [:div#bottom "&copy; Odyssey Logistics &amp; Technology Corporation"]
    ]
  )
)
