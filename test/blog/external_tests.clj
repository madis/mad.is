(ns blog.external-tests
  (:require
    [babashka.fs :as fs]
    [clojure.pprint :as pp]
    [clojure.test :refer [deftest is]]
    [blog.external :as ex]))

(deftest get-md-files-test
  (let [md-files (ex/list-md-files "posts" (fs/absolutize "test/resources"))]
    (is (= 2 (count md-files)))))

(comment
  (clojure.pprint/pprint {:hello "World"})
  (ex/root-path)
  (fs/absolutize "test/resources"))
