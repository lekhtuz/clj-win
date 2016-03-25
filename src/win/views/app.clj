(ns win.views.app
  (:require
    [clojure.tools.logging :as log :refer [info]]
    [hiccup.element :refer [image link-to unordered-list]]
    [win.views.layout :as layout]
  )
)

(defn home [{current-user :current-user :as current-authentication}]
  (log/info "app/home: current-user =" current-user)
  (layout/common current-user)
)
