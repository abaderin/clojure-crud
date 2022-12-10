(ns clojure-crud.core
  (:require [clojure.data.json :as json]
            [ring.adapter.jetty :as jetty])
  (:gen-class))

(def simple-response
  {:message "hello world"})

(defn handler
  [request]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (json/write-str simple-response)})

(def server (atom nil))

(defn start-server
  [args]
  (let [default-args {:port 8000
                      :join? false}
        server-args (merge default-args args)
        instance (jetty/run-jetty #'handler server-args)]
    (reset! server instance)))

(defn start-server
  ([] (start-server {}))
  ([server-args]
   (let [default-args {:port 8000
                       :join? false}
         args (merge default-args server-args)
         jetty-inst (jetty/run-jetty #'handler args)]
     (reset! server jetty-inst))))

(defn stop-server
  []
  (.stop @server)
  (reset! server nil))

(defn -main
  [& args]
  (start-server {:join? true}))
