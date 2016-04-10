(ns pain-tetris.movement-test
  (:require-macros [cljs.test :refer (is deftest testing)])
  (:require [pain-tetris.movement :as mov]
            [cljs.test :as t]))

#_(deftest can-move
  (testing "check that a block can move"
    (let [p])
    (is (= 1 1))
    (is (mov/can-move piece))))

(deftest default
  (is (= 10 10)))
