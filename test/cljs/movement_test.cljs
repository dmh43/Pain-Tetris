(ns pain-tetris.movement-test
  (:require-macros [cljs.test :refer (is deftest testing)])
  (:require [pain-tetris.movement :as mov]
            [pain-tetris.grid :as g]
            [cljs.test :as t]))

(deftest movement
  (testing "check that a block or piece can move"
    (let [grid (g/build-grid)
          {:keys [one-piece-grid piece-num]} (g/insert-piece grid :square :top)
          block-coords g/top-center]
      (is (mov/can-move grid block-coords :down))
      (is (not (mov/can-move grid block-coords :up)))
      (is (mov/can-move grid piece-num :down))
      (is (not (mov/can-move grid piece-num :up))))))

(deftest default
  (is (= 10 10)))
