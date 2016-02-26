(ns win.routes.home
  (:require
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
  (GET "/Application" [] (app/home))
  (POST "/login" [username password] (resp/redirect "Application"))
  (GET "/ResetPassword" [] (login/reset-password))
)
