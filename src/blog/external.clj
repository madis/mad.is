(ns blog.external
  (:require
   [babashka.fs :as fs]
   [clojure.java.io :as io]
   [clojure.string :as str])
  (:import
   [java.nio.file.attribute FileTime]
   [java.time ZoneId ZonedDateTime]
   [java.time.format DateTimeFormatter]))

(defn root-path
  []
  (-> (io/resource "public")))

(defn- file-time->local-date
  [ft]
  (-> (.toInstant ft)
      (ZonedDateTime/ofInstant (ZoneId/systemDefault))
      (.toLocalDate)))

(defn- filetime->yyyy-mm-dd
  "Converts FileTime to YYYY-MM-DD string (local date)"
  [^FileTime ft]
  (when ft
    (-> ft
        .toInstant
        (.atZone (ZoneId/systemDefault))   ; or (ZoneId/of "UTC")
        .toLocalDate
        (.format DateTimeFormatter/ISO_LOCAL_DATE))))

(defn remove-default-file-suffix
  [path]
  (let [s (str path)]
    (cond
      (str/ends-with? s "/README.md") (str/replace s #"/README\.md$" "")
      (str/ends-with? s ".md")        (str/replace s #"\.md$" "")
      :else s)))

(defn list-md-files
  [folder & [root]]
  (let [root-path (or root (root-path))]
    (->> (fs/glob root-path (str folder "/**.md"))
         (map (fn [p]
               {:file-name (fs/file-name p)
                :path (remove-default-file-suffix (fs/relativize root-path p))
                :created-at (filetime->yyyy-mm-dd (fs/creation-time p))
                :content (slurp (str p))}))
        (sort-by :created-at #(compare %2 %1) ,,,))))

(defn get-md-file
  [uri & [root]]
  (let [root-path (or root (.getPath (root-path)))
        uri-without-trailing-slash (str/replace uri #"/$" "")
        contents-directly (str root-path uri-without-trailing-slash ".md")
        contents-readme (str root-path uri "README.md")
        final-path (if (fs/exists? contents-directly)
                     contents-directly
                     (if (fs/exists? contents-readme)
                       contents-readme
                       (throw (ex-info (str "No content found: " contents-readme) {:uri uri}))))]
    {:path final-path
     :created-at (filetime->yyyy-mm-dd (fs/creation-time final-path))
     :content (slurp final-path)}))
