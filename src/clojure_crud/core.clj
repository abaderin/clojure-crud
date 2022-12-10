(ns clojure-crud.core
  (:require [clojure.data.json :as json]
            [ring.adapter.jetty :as jetty]
            [clojure.tools.cli :refer [parse-opts]])
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

(def args-specs
  [["-p" "--port PORT" :default 8000 :parse-fn #(Integer. %)]
   ["-P" "--db-port DBPORT" :default 27017 :parse-fn #(Integer. %)]
   ["-H" "--db-host DBHOST" :default "localhost"]])

(defn -main
  [& args]
  (let [parsed-opts (parse-opts args args-specs)]
    (if (:errors parsed-opts)
      (println (:errors parsed-opts))
      (let [{:keys [port db-host db-port]} (:options parsed-opts)]
        (println parsed-opts)
        (println (str "db-host=" db-host))
        (println (str "db-port=" db-port))
        (start-server {:join? true
                       :port port})))))
