(ns blog.content-helpers-tests
  (:require
   [babashka.fs :as fs]
   [blog.content-helpers :as ch]
   [clojure.pprint]
   [clojure.test :refer [deftest is]])
  (:import
   [java.time LocalDate]))

(deftest get-posts-test
  (let [post-list [{:file-name "2026-05-01-first-post.md"
                    :content "---\ntitle: First Post\ndescription: Desc 1\ntags: [first tag, second tag]\n---\nHello!"}
                   {:file-name "2026-05-02-second-post.md"
                    :content "---\ntitle: Second Post\ndescription: Desc 2\ntags: [second tag, third tag]\n---\nBye!"}]
        parsed-posts (ch/get-posts {:list-md-files (constantly post-list)})]
    (is (= 2 (count parsed-posts)))
    (is (= (-> parsed-posts first keys set)
           #{:file-name :content :info}))))

(deftest asset-request?-test
  (is (ch/asset-request? "/posts/2026-05-30-things/file.png"))
  (is (ch/asset-request? "/projects/2026-05-30-things/file.webp"))
  (is (not (ch/asset-request? "/weird/2026-05-30-things/file.webp")))
  (is (not (ch/asset-request? "/posts/2026-05-30-things/file"))))

(deftest asset-response
  (let [response (ch/asset-response "/posts/2026-05-30-test/smallest.png" (fs/absolutize "test/resources"))]
    (is (= 200 (:status response)))
    (is (= 200 (:status response)))
    (is (instance? java.io.InputStream (:body response)))
    (is (< 10 (.available (:body response)))))
  (is (= 400 (:status (ch/asset-response "/posts/2026-05-30-test/none.png")))))
