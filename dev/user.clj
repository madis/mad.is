(ns user
  (:require
    [clj-reload.core :as reload]
    [blog.main]))

(alter-var-root #'*warn-on-reflection* (constantly true))

(reload/init
  {:no-reload '#{user}})

(defn go! [] (reload/reload))

(comment
  (blog.main/start!)
  (go!))
