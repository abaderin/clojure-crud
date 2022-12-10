(ns clojure-crud.core
  (:require [clojure.data.json :as json]))

(def simple-response
  {:message "hello world"})

(defn handler
  [request]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (json/write-str simple-response)})
