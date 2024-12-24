(ns is.mad.browser-app.pages
  (:require
    [is.mad.browser-app.pages.readings :as pages.readings]))

(defn home []
  [:h1 "Hello!"])

(def readings pages.readings/page)

(defn writings []
  [:div
   [:h1.title "Some writings here"]])

(defn about []
  [:div
   [:h1.title "About me"]])
