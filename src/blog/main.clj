(ns blog.main
  (:require
    [blog.handlers :as handlers]
    [ring.middleware.resource :as resource]
    [ring.middleware.content-type :as content-type]
    [clj-simple-router.core :as router]
    [org.httpkit.server :as hk-server]
    [ring.logger :as logger]))

(def routes
  {"GET /" handlers/home
   "GET /posts" handlers/list-posts
   "GET /projects" handlers/list-projects
   "GET /projects/*" handlers/show-project
   "GET /about" handlers/about
   "GET /posts/*" handlers/show-post
   "GET /assets/*" handlers/serve-assets})

(defonce server-instance (atom nil))

(defn start!
  [& [{:keys [port] :or {port 3001}}]]
  (let [handler (router/router routes)]
    (println "Starting server on port" port)
    (reset! server-instance
            (hk-server/run-server
              (-> handler
                  (resource/wrap-resource ,,, "public" {:prefer-handler? false})
                  (content-type/wrap-content-type ,,, )
                  (logger/wrap-with-logger ,,,))
              {:port port :join false :legacy-return-value? false}))))

(defn stop!
  ([]
   (stop! @server-instance))
  ([instance]
   (deref (hk-server/server-stop! instance))))

(defn before-ns-unload []
  (stop!))

(defn after-ns-reload []
  (start!))

(defn -main [& args]
  (start!)
  @(promise))
