(ns blog.turnstile-tests
  (:require
   [clojure.pprint]
   [blog.demos.turnstile-calc :refer [pass-next calculate-times]]
   [clojure.test :refer [deftest is testing]]))

(deftest pass-next-test
  (let [comparator (fn [a b] (compare (:person a) (:person b)))
        ss (partial sorted-set-by comparator)
        enter (fn [person-idx] {:direction :enter :person person-idx})
        exit (fn [person-idx] {:direction :exit :person person-idx})]
    (is (= [:enter (enter 2) (ss (enter 3)) (ss)] (pass-next :exit (ss (enter 2) (enter 3)) (ss))))
    (is (= [:enter (enter 4) (ss) (ss  (exit 5))] (pass-next :enter (ss (enter 4)) (ss (exit 5)))))))

(deftest turnstile-test
  (testing "example-1"
    (is (= [2 0 1 5] (calculate-times [0 0 1 5] [0 1 1 0]))))

  (testing "example-2"
    (is (= [0 2 1 4 3] (calculate-times [0 1 1 3 3] [0 1 0 0 1]))))

  (testing "more examples"
    (is (= (calculate-times [0 0 1 5] [0 1 1 0]) [2 0 1 5]))
    (is (= (calculate-times [0 0 5 5] [0 1 1 0]) [1 0 5 6]))
    (is (= (calculate-times [0 0 1 1] [0 1 1 0]) [2 0 1 3]))
    (is (= (calculate-times [0 0 0 0] [0 1 1 0]) [2 0 1 3]))
    (is (= (calculate-times [0 0 0 0] [0 0 0 0]) [0 1 2 3]))
    (is (= (calculate-times [0 0 1 3 10] [0 1 1 1 0])  [2 0 1 3 10]))
    (is (= (calculate-times [0 1 1 3 3]  [0 1 0 0 1]) [0 2 1 4 3]))
    (is (= (calculate-times [1 1 3 3 4 5 6 7 7] [1 1 0 0 0 1 1 1 1]) [1 2 3 4 5 6 7 8 9]))))
