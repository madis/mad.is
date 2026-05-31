(ns blog.content-helpers
  (:require
    [markdown.core :as md]
    [babashka.fs :as fs]
    [clojure.string :as str]
    [clojure.java.io :as io]
    [ring.util.mime-type :as mime-type]
    [blog.external :as be])
  (:import
    [java.time LocalDate]))

(defn root-path
  []
  (-> (io/resource "public")))

(defn extract-publication-date
  [p]
  (let [pattern #"(?i)(\d{4})(?:[-/_](\d{1,2}))?(?:[-/_](\d{1,2}))?"
        match   (re-find pattern (:file-name p))]
      (when match
        (let [[_ year month day] match]
          [(when year (Integer/parseInt year))
           (when month (Integer/parseInt month))
           (when day (Integer/parseInt day))]))))

(defn get-publication-info
  [p]
  (let [p-meta (md/md-to-meta (:content p))
        [year month date] (extract-publication-date p)
        additional-fields {:year year
                           :month month
                           :published-at (if (every? int? [year month date])
                                           (LocalDate/of year month date)
                                           (:created-at p))
                           :href (str (:path p) "/")}]
    (merge p-meta additional-fields)))

(defn get-posts
  ([] (get-posts {}))
  ([{:keys [list-md-files] :or {list-md-files (partial be/list-md-files "posts")}}]
   (->> (list-md-files)
        (map (fn [p] (assoc p :info (get-publication-info p))) ,,,)
        (sort-by :published-at #(compare %2 %1) ,,,))))

(defn get-projects
  ([] (get-projects {}))
  ([{:keys [list-md-files] :or {list-md-files (partial be/list-md-files "projects")}}]
   (->> (list-md-files)
        (map (fn [p] (assoc p :info (get-publication-info p))) ,,,)
        (sort-by :published-at #(compare %2 %1) ,,,))))

(def allowed-groups #{"posts" "projects" "about"})
(def supported-extensions #{"jpg" "jpeg" "png" "gif" "bmp" "webp" "tiff" "tif" "svg"})

(defn publication-data
  [uri]
  (let [file-contents (be/get-md-file uri)
        {:keys [metadata html]} (md/md-to-html-string-with-meta file-contents)]
    {:title (:title metadata)
     :published-at "2026 was the year and May was the month"
     :content html}))

(defn asset-request?
  [uri]
  (let [[_ group _ asset] (str/split uri #"/")
        extension (when asset
                    (-> asset
                        str/lower-case
                        (str/split #"\." -1)
                        last))]
    (and
      (not (nil? extension))
      (contains? allowed-groups group)
      (contains? supported-extensions extension))))

(defn asset-response
  ([path] (asset-response path (root-path)))
  ([path root-path]
   (let [full-path (str root-path path)
         content-type (mime-type/ext-mime-type path)
         headers {"Content-Type" content-type "Cache-Control" "public, max-age=31536000"}]
     (if (fs/exists? full-path)
       {:status 200 :body (io/input-stream full-path) :headers headers}
       {:status 400 :body ""}))))
