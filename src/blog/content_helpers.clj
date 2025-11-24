(ns blog.content-helpers
  (:require
    [markdown.core :as md]
    [babashka.fs :as fs]
    [clojure.string :as str]
    [clojure.java.io :as io])
  (:import
    [java.time LocalDate]))

(defn- remove-file-extension
  [path]
  (str/replace path #"\.[^.]+$" ""))

(defn- root-path
  []
  (-> (io/resource "public")))

(defn get-post-info
  [path]
  (let [post-meta (md/md-to-meta (slurp (str path)))
        file-name (fs/file-name path)
        [year month date] (->> (re-find #"^\d{4}-\d{2}-\d{2}" file-name)
                               (re-seq #"\d+")
                               (map #(Integer/parseInt %)))
        additional-fields {:type :post
                           :year year
                           :month month
                           :published-at (LocalDate/of year month date)
                           :href (str "/posts/" (remove-file-extension file-name))}]
    (merge post-meta additional-fields)))

(defn get-project-info
  [path]
  (let [project-meta (md/md-to-meta (slurp (str path)))
        file-name (fs/file-name path)
        id (->> (re-find #"^\d+" file-name) (Integer/parseInt ,,,))
        additional-fields {:type :project
                           :id id
                           :href (str "/projects/" (remove-file-extension file-name))}]
    (merge project-meta additional-fields)))

(defn get-post-list
  []
  (let [file-list (fs/glob (root-path) "posts/*/*.md")]
    (map get-post-info file-list)))

(defn get-project-list
  []
  (let [file-list (fs/glob (root-path) "projects/*.md")]
    (map get-project-info file-list)))

(defn entity-root-path
  [group entity-id]
  (case group
    "posts" (str (root-path) "/" group "/" (subs entity-id 0 4) "/" entity-id)
    "projects" (str (root-path) "/" group "/" entity-id)
    "about" (str (root-path) "/" group "/" entity-id)))

(defn prepare-entity-content
  [group entity-id]
  (let [file-path (str (entity-root-path group entity-id) ".md")
        file-contents (slurp file-path)]
    (md/md-to-html-string-with-meta file-contents)))

(comment
  (get-project-list)
  (fs/glob (io/resource "public") "posts/*/*.md")
  (str/replace "hello.world.md" #"\.[^.]+$" "")
  (def files (fs/glob (-> *file* fs/absolutize fs/parent fs/parent fs/parent) "posts/*/*.md"))
  (fs/file-name (first files))
  (get-post-list)
  (java.time.LocalDate/parse "2025-10-11")
  ((slurp "/home/madis/temp/learning-babashka/mad.is/posts/2025/2025-11-23-the-future-is-here.md"))
  (get-post-info "/home/madis/temp/learning-babashka/mad.is/posts/2025/2025-11-23-the-future-is-here.md"))
