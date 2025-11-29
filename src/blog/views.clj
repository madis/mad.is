(ns blog.views
  (:require
    [clojure.string :as str]
    [cheshire.core :as json]
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

(def google-analytics-init
  (str/join
    ["window.dataLayer = window.dataLayer || [];"
     "function gtag(){dataLayer.push(arguments);}"
     "gtag('js', new Date());"
     "gtag('config', 'G-0K088F0CZX');"]))

(def ld-json
  (json/generate-string
    {"@context" "https://schema.org"
     "@type" "Person"
     "name" "Madis Nõmme"
     "jobTitle" "Software developer, consultant and speaker"
     "url" "https://mad.is"
     "image" "https://mad.is/images/circular-profile.webp"
     "sameAs" [
       "http://x.com/madisIT"
       "http://github.com/madis"
     ]
     "email" "me@mad.is"
     "description" "Madis Nõmme is an experienced back-end developer specializing in Clojure(Script), Ruby, Bitcoin & Lightning development"}
    {:pretty true}))

(defn layout
  [{:keys [menu-selection metadata title]} body-content]
  (let [title (get metadata :title title)
        final-title (if title (str title " | mad.is") "mad.is")]
    [:html
     [:head
      [:script {:async true :src "https://www.googletagmanager.com/gtag/js?id=G-0K088F0CZX"}]
      [:script (hiccup2.core/raw google-analytics-init)]

      [:meta {:charset "utf-8"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
      [:meta {:name "description" :content "Madis Nõmme - software developer and researcher. Learn how to technology can bring us more freedom and prosperity"}]
      [:link {:rel "icon" :href "/web-icons/favicon.ico" :sizes "any"}]
      [:link {:rel "icon" :href "/web-icons/icon.svg" :type "image/svg+xml"}]
      [:link {:rel "apple-touch-icon" :href "/web-icons/apple-touch-icon.png"}]
      [:link {:rel "manifest" :href "/web-icons/manifest.webmanifest"}]
      [:title final-title]
      [:link {:rel "stylesheet" :href "https://cdn.jsdelivr.net/npm/bulma@1.0.4/css/bulma.min.css"}]
      [:link {:rel "stylesheet" :href "/css/style.css"}]
      [:script {:type "application/ld+json"} (hiccup2.core/raw ld-json)]
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
    [:div.column {:class [:is-one-fifth-desktop :is-half-mobile :is-one-third-tablet]}
     [:figure.image.mx-auto.is-square
      [:img {:src "/images/circular-profile.webp" :alt "Madis Nõmme"}]]]]
   [:h1.title.is-2.has-text-centered "Madis Nõmme"]
   [:a {:href "mailto:services@mad.is"} [:h4.title.is-4.has-text-centered "services@mad.is"]]
   (entity-content model)])
