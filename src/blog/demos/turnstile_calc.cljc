(ns blog.demos.turnstile-calc)

(defn add-to-arrivals
  [old-val & [new-val]]
  (if (nil? old-val)
    [new-val]
    (conj old-val new-val)))

(def direction-symbols
  {0 :enter 1 :exit})

(defn pass-next
  [last-action entering exiting]
  (let [preferred (condp contains? last-action
                    #{:idle :exit} (if (seq exiting) exiting entering)
                    #{:enter} (if (seq entering) entering exiting))]
    (if (empty? preferred)
      [:idle nil entering exiting]
      (let [passing (first preferred)
            dir (:direction passing)]
        [dir passing
         (if (= dir :enter) (disj entering passing) entering)
         (if (= dir :exit) (disj exiting passing) exiting)]))))

(defn group-arrivals-by-time
  [arrivals directions]
  (reduce
    (fn [acc [person-id [arrival direction]]]
      (update-in acc
                 [arrival]
                 add-to-arrivals
                 {:direction (get direction-symbols direction)
                  :person person-id}))
    {}
    (map-indexed vector (map vector arrivals directions))))

(defn calculate-times
  [arrivals directions & [notify]]
  (let [grouped-arrivals (group-arrivals-by-time arrivals directions)
        last-arrival (last arrivals)
        comparator (fn [a b] (compare (:person a) (:person b)))
        empty-queue (sorted-set-by comparator)]
    (loop [moment 0
           last-action :idle
           entering empty-queue
           exiting empty-queue
           passings []]
      (let [new-arrivals (get grouped-arrivals moment [])
            entering (into entering (filter #(= (:direction %) :enter) new-arrivals))
            exiting (into exiting (filter #(= (:direction %) :exit) new-arrivals))
            [this-action this-passing enter-q exit-q] (pass-next last-action entering exiting)
            updated-passings (conj passings
                                   (when (not (nil? this-passing))
                                     (assoc this-passing :moment moment)))]
        (if (and (empty? enter-q) (empty? exit-q) (>= moment last-arrival))
          (reduce (fn [acc p]
                       (assoc acc (:person p) (:moment p)))
                  (vec (repeat (count (remove nil? updated-passings)) nil)) ; vector with nil-s to be filled
                  (vec (remove nil? updated-passings)))
          (recur (inc moment) this-action enter-q exit-q updated-passings))))))

#?(:scittle
   (let [api {}]
     (set! (.-turnstileCalculateTimes js/window) calculate-times)
     (set! (.-turnstileStart js/window)
           (fn [times directions]
             (js/console.log ">>> turnstileStart" (clj->js {:times times
                                                    :directions directions
                                                    :times-type (type times)
                                                    :directions-type (type directions)}))))
     (js/console.log "turnstile-api evaluated")))
