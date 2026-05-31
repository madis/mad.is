(ns blog.handlers
  (:require
   [blog.content-helpers :as ch :refer [get-posts get-projects publication-data]]
   [blog.views :as views :refer [layout]]
   [hiccup2.core :as h]))

(defn- render-html
  [hiccup]
  (-> (h/html (hiccup2.core/raw "<!DOCTYPE html>\n") hiccup) str))

(defn- response-ok
  [body]
  (assoc {:status 200 :headers {"Content-Type" "text/html"}} :body body))

(defn home
  [req]
  (let [posts (take 3 (get-posts))
        projects (take 3 (get-projects))
        model {:title "Madis Nõmme - Home"
               :posts posts
               :projects projects
               :menu-selection :home}]
    (response-ok (-> model views/show-landing ((partial layout model) ,,,) render-html))))

(defn list-posts
  [req]
  (let [model {:title "Posts"
               :menu-selection :posts
               :publications (get-posts)}]
    (response-ok (-> model views/entity-list ((partial layout model) ,,,) render-html))))

(defn list-projects
  [req]
  (let [model {:title "Projects"
               :menu-selection :projects
               :publications (get-projects)}]
    (response-ok (-> model views/entity-list ((partial layout model) ,,,) render-html))))

(defn make-show-handler
  [menu-selection view-fn]
  (fn [req]
    (if (ch/asset-request? (:uri req))
      (ch/asset-response (:uri req))
      (let [publication (publication-data (:uri req))
            layout-model {:title (:title publication) :menu-selection menu-selection}
            view-model (select-keys publication [:content :published-at])]
        (response-ok (render-html (layout layout-model (view-fn view-model))))))))

(def show-project (make-show-handler :projects views/entity-content))
(def show-post (make-show-handler :posts views/entity-content))
(def show-about (make-show-handler :about views/show-about))
