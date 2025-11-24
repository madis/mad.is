(ns blog.views
  (:require
    [clojure.string :as str]
    [hiccup2.core]))

(defn navbar-items
  [menu-selection]
  (let [items [{:id :home :href "/"}
               {:id :posts :href "/posts"}
               {:id :projects :href "/projects"}
               {:id :about :href "/about"}]
        new-menu-item (fn [i] [:a.navbar-item.is-size-5.has-text-weight-semibold
                               {:href (:href i)
                                :class (when (= menu-selection (:id i)) [:is-active])}
                               (str/capitalize (name (:id i)))])]
    (map new-menu-item items)))

(defn navbar
  [menu-selection]
  [:nav.navbar {:data-init "$navbar-open = false"}
   [:div.container
    [:div.navbar-brand
     [:a.navbar-item {:href "../"}
      [:img {:src "/images/mad.is-logo.svg" :alt "Logo"}]]
     [:span.navbar-burger.burger {:data-class:is-active "$navbar-open"
                                  :data-on:click "$navbar-open = !$navbar-open"}
      [:span] [:span] [:span]]]
    [:div.navbar-menu {:data-class:is-active "$navbar-open"}
     [:div.navbar-end
      #_[:div.navbar-item
       [:div.control.has-icons-left
        [:input.input.is-rounded {:type "email" :placeholder "Search"}]
        [:span.icon.is-left
         [:i.fa.fa-search]]]]
      (navbar-items menu-selection)]]]])

(defn layout
  [{:keys [menu-selection metadata title]} body-content]
  (let [title (get metadata :title title)
        final-title (if title (str title " | mad.is") "mad.is")]
    [:html
     [:head
      [:meta {:charset "utf-8"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
      [:title final-title]
      [:link {:rel "stylesheet" :href "https://cdn.jsdelivr.net/npm/bulma@1.0.4/css/bulma.min.css"}]
      [:link {:rel "stylesheet" :href "/css/style.css"}]
      [:script {:type :module
                :src "https://cdn.jsdelivr.net/gh/starfederation/datastar@v1.0.0-RC.6/bundles/datastar.js"}]
      [:script {:src "https://kit.fontawesome.com/cb70718952.js" :crossorigin "anonymous"}]]
     [:body
      (navbar menu-selection)
      [:div.section
       [:div.container
        [:div.columns.is-desktop
         [:div.column.is-10.is-offset-1
          body-content]]]]]]))

(defn post-summary
  [post]
  [:div.box
   [:div.content.is-medium
    [:h2.subtitle.is-4 (:published-at post)]
    [:a {:href (:href post)}
     [:h1.title (:title post)]]
    [:p (:description post)]]])

(defn entity-content
  [model]
  [:div.content (hiccup2.core/raw (:content model))])

(defn entity-list
  [model]
  (map post-summary (take 2 (:entities model))))

(defn show-landing
  [model]
  [:div
   [:div.mb-4
    [:h1.title.is-3 "Latest posts"]
    (entity-list {:entities (:posts model)})]
   [:div.mb-4
    [:h1.title.is-3 "Projects"]
    (entity-list {:entities (:projects model)})]])

(defn show-about
  [model]
  [:div
   [:div.columns.is-centered.is-mobile
    [:div.column.is-one-fifth-desktop.is-half-mobile
     [:figure.image.mx-auto.is-square
      [:img {:src "/images/circular-profile.webp"}]]]]
   [:h1.title.is-2.has-text-centered "Madis Nõmme"]
   (entity-content model)])
