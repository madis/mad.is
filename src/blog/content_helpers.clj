(ns blog.content-helpers
  (:require
   [babashka.fs :as fs]
   [blog.external :as be]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [markdown.core :as md]
   [ring.util.mime-type :as mime-type])
  (:import
   [java.time LocalDate ZoneOffset]
   [java.util Date]))

(defn root-path
  []
  (-> (io/resource "public")))

(defn safe-parse-int
  [s]
  (when (seq s) (Integer/parseInt s)))

(defn string->local-date
  [s]
  (let [pattern #"(?i)(\d{4})(?:[-/_](\d{1,2}))?(?:[-/_](\d{1,2}))?"
        match   (re-find pattern s)
        [_ year-str month-str day-str] match
        year (safe-parse-int year-str)
        month (safe-parse-int month-str)
        day (safe-parse-int day-str)]
    (when (every? int? [year month day]) (LocalDate/of year month day))))

(defn date->yyyy-mm-dd
  [date]
  (when (and (not (nil? date)) (instance? Date date))
    (-> date
      (.toInstant)
      (.atZone ZoneOffset/UTC)
      (.toLocalDate)
      (str))))

(defn calculate-published-at
  [date-strings]
  (let [dates (remove nil? (map string->local-date date-strings))]
    (first (sort #(compare %2 %1) dates))))

(defn get-publication-info
  [p]
  (let [p-meta (md/md-to-meta (:content p))
        published-at-candidates (remove nil? [(:path p) (date->yyyy-mm-dd (:publish-at p-meta))])
        additional-fields {:published-at (calculate-published-at published-at-candidates)
                           :href (str (:path p) "/")}]
    (merge p-meta additional-fields)))

(defn get-posts
  ([] (get-posts {}))
  ([{:keys [list-md-files] :or {list-md-files (partial be/list-md-files "posts")}}]
   (->> (list-md-files)
        (map (fn [p] (assoc p :info (get-publication-info p))) ,,,)
        (filter (fn [p] (not (get-in p [:info :draft]))) ,,,)
        (sort-by #(get-in % [:info :published-at]) #(compare %2 %1) ,,,))))

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
  (let [md-file (be/get-md-file uri)
        file-content (:content md-file)
        {:keys [metadata html]} (md/md-to-html-string-with-meta file-content)
        published-at-candidates (remove nil? [(:path md-file)
                                              (:created-at md-file)
                                              (date->yyyy-mm-dd (:publish-at metadata))])
        ]
    {:title (:title metadata)
     :published-at (calculate-published-at published-at-candidates)
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
