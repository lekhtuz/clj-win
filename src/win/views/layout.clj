(ns win.views.layout
  (:require [hiccup.page :refer [html5 include-css]]))

(defn common [& body]
  (html5
    [:head
     [:title "Web Integrated Network"]
     (include-css "/css/screen.css")
     (include-css "/css/dropdown.css")
    ]
    [:body#app body]))
