(ns pain-tetris.grid-test
  (:require-macros [cljs.test :refer (is deftest testing)])
  (:require [cljs.test :as t]
            [pain-tetris.grid :as g]))

(deftest grid
  (let [grid (g/build-grid 100 100)
        one-block-grid (g/insert-piece grid :square :bottom)
        full-grid (g/full-grid grid)]
    (testing "insert piece"
      (testing "can be inserted?"
        (is (g/can-insert? grid :square :top))
        (is (g/can-insert? grid :square :bottom))
        (is (g/can-insert? one-block-grid :square :top))
        (is (not (g/can-insert? one-block-grid :square :bottom)))))
    (testing "Block below or above?"
      (is (not (g/block-below grid g/top-center)))
      (is (g/block-above one-block-grid g/bottom-center)))
    (testing "full row?"
      (is (not (g/full-row? grid 0)))
      (is (not (g/full-row? one-block-grid 0)))
      (is (g/full-row? full-grid 0))
      (is (g/full-row? full-grid 1))
      (is (g/full-row? full-grid 5)))))
