(ns is.mad.browser-app.pages.readings)

(defn page
  []
  [:article {:class [:message :is-info]}
    [:div {:class [:message-header]}
     [:p "Info"]
     [:button {:class [:delete]
               :aria-label "delete"}]]
    [:div {:class [:message-body]}
      "This is message body"]])
