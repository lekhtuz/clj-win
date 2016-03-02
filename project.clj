(defproject win "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :min-lein-version "2.0.0"
  
  :dependencies [
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [base64-clj "0.1.1"]
                 [com.cemerick/friend "0.2.1"]
                 [compojure "1.4.0"]
                 [digest "1.4.4"]
                 [hiccup "1.0.5"]
                 [korma "0.4.2"]
                 [oracle/ojdbc "11.2.0.4"]
                 [ring-server "0.3.1"]
                 [sonian/carica "1.2.1" :exclusions [cheshire]]
                 [log4j "1.2.17" :exclusions [javax.mail/mail
                                           javax.jms/jms
                                           com.sun.jdmk/jmxtools
                                           com.sun.jmx/jmxri]]
                ]

  :jvm-opts ^:replace ["-Xmx1g" "-server"]

  :plugins [[lein-ring "0.9.7"]]

  :ring {
         :handler win.handler/app
         :init    win.handler/init
         :destroy win.handler/destroy
         :nrepl {:start? true :port 3001}
        }
  :profiles {
             :uberjar {:aot :all}
             :production {
                          :ring {
                                 :open-browser? false
                                 :stacktraces? false
                                 :auto-reload? false
                                }
                         }
             :dev {
                   :dependencies [
                                  [ring/ring-mock "0.3.0"]
                                  [ring/ring-devel "1.4.0"]
                                 ]
                   :ring {
                          :open-browser? false
                          :stacktraces? true
                          :auto-reload? true
                         }
                   }
             }
)
