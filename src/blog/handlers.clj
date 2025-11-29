(ns blog.handlers
  (:require
    [hiccup2.core :as h]
    [clojure.java.io :as io]
    [blog.content-helpers :as ch :refer [prepare-entity-content get-post-list get-project-list]]
    [ring.util.mime-type :as mime-type]
    [lambdaisland.uri :refer [uri]]
    [clojure.string :as str]
    [blog.views :as views :refer [layout]]))

(defn- render-html
  [hiccup]
  (-> (h/html (hiccup2.core/raw "<!DOCTYPE html>\n") hiccup) str))

(defn- response-ok
  [body]
  (assoc {:status 200 :headers {"Content-Type" "text/html"}} :body body))

(defn home
  [req]
  (let [posts (get-post-list)
        projects (get-project-list)
        model {:title "Madis Nõmme - Home"
               :posts posts
               :projects projects
               :menu-selection :home}]
    (response-ok (-> model views/show-landing ((partial layout model) ,,,) render-html))))

(defn list-posts
  [req]
  (let [model {:title "Posts"
               :menu-selection :posts
               :entities (get-post-list)}]
    (response-ok (-> model views/entity-list ((partial layout model) ,,,) render-html))))

(defn list-projects
  [req]
  (let [projects (get-project-list)
        model {:title "Projects"
               :menu-selection :projects
               :entities projects}]
    (response-ok (-> model views/entity-list ((partial layout model) ,,,) render-html))))

(defn about
  [req]
  (let [about (prepare-entity-content "about" "madis-bio")
        _ (println ">>> ABOUT" (keys about))
        model {:metadata (:metadata about)
               :menu-selection :about
               :content (:html about)}]
    (response-ok (-> model views/show-about ((partial layout model) ,,,) render-html))))

(defn show-project
  [req]
  (let [post-id (-> req :path-params first)
        post (prepare-entity-content "projects" post-id)
        model {:menu-selection :projects
               :metadata (:metadata post)
               :content (:html post)}]
    (response-ok (-> model views/entity-content ((partial layout model) ,,,) render-html))))

(defn show-post
  [req]
  (let [post-id (-> req :path-params first)
        post (prepare-entity-content "posts" post-id)
        model {:menu-selection :posts
               :metadata (:metadata post)
               :content (:html post)}]
    (response-ok (-> model views/entity-content ((partial layout model) ,,,) render-html))))

(defn serve-assets
  [req]
  (let [[_ group entity-id] (str/split (:path (uri (get-in req [:headers "referer"]))) #"/")
        asset-filename (last (str/split (:path (uri (get req :uri))) #"/assets/"))
        full-path (str (ch/entity-root-path group entity-id) "/" asset-filename)
        content-type (mime-type/ext-mime-type asset-filename)]
    {:status 200
     :body (io/input-stream full-path)
     :headers {"Content-Type" content-type
               "Cache-Control" "public, max-age=31536000"}}))
