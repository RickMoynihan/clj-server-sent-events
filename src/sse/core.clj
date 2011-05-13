(ns sse.core
  (:use 
   [ring.middleware.file :only [wrap-file]]
   [ring.middleware.resource :only [wrap-resource]])
  (:use lamina.core
        aleph.http))

(def ch (channel))

(def eid (atom 0))

(defn send-event [s]
  (let [ev-id (swap! eid inc)]
    (enqueue ch (str "id: " ev-id "\ndata: " s "\n\n"))
    ev-id))

(defn handler [request]
  (println request)
  (when (= (:uri request) "/event-source")
    {:status 200
     :headers {"Content-Type" "text/event-stream"}
     :body ch}))

(def app
  (-> #'handler
      (wrap-file "./files")
      (wrap-resource "./resources")))

(defonce server (start-http-server (wrap-ring-handler #'app) {:port 8080} ))


