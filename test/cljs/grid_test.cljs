(ns pain-tetris.grid-test
  (:require-macros [cljs.test :refer (is deftest testing use-fixtures)])
  (:require [cljs.test :as t]
            [pain-tetris.grid :as g]))

(def grid (g/build-grid 5 8))
(def one-block-grid (g/insert-piece grid :square :bottom))
(def full-grid (g/full-grid grid))
(def coords [0 0])

(deftest can-insert?
  (is (g/can-insert? grid :square (g/side-to-coords grid :top)))
  (is (g/can-insert? grid :square (g/side-to-coords grid :bottom)))
  (is (g/can-insert? one-block-grid :square (g/side-to-coords grid :top)))
  (is (not (g/can-insert? one-block-grid :square (g/side-to-coords grid :bottom)))))

(deftest insert-piece
  (is (g/is-occupied? (g/insert-piece grid :square :top)
                      (g/side-to-coords grid :top))))

(deftest insert-block
  (is (g/is-occupied? (g/insert-block grid coords) coords)))

(deftest remove-block
  (is (= grid
         (g/remove-block (g/insert-block grid coords) coords)))
  (is (not= one-block-grid
            (g/remove-block one-block-grid (g/side-to-coords grid :bottom))))
  (is (not= grid
            (g/remove-block one-block-grid (g/side-to-coords grid :bottom)))))

(deftest block-below-or-above?
  (is (not (g/block-below grid (g/side-to-coords grid :top-center))))
  (is (not (g/block-above one-block-grid (g/side-to-coords grid :bottom-center))))
  (is (not (g/block-above one-block-grid (g/sub (g/side-to-coords grid :bottom-center) [0 1])))))

(deftest full-row?
  (is (not (g/full-row? grid 0)))
  (is (not (g/full-row? one-block-grid 0)))
  (is (g/full-row? full-grid 0))
  (is (g/full-row? full-grid 1))
  (is (g/full-row? full-grid 2)))

(deftest full-grid?
  (is (not (g/full-grid? grid)))
  (is (not (g/full-grid? one-block-grid)))
  (is (g/full-grid? full-grid)))

(deftest coord-arith
  (is (= [0 0] (g/add [0 0] [0 0])))
  (is (= [1 2] (g/add [1 1] [0 1])))
  (is (= [1 2] (g/add [0 1] [1 1])))
  (is (= [0 0] (g/sub [0 0] [0 0])))
  (is (= [1 0] (g/sub [1 1] [0 1])))
  (is (= [-1 0] (g/sub [0 1] [1 1]))))

(deftest index-to-coords
  (is (= (g/index-to-coords grid 2) [2 0]))
  (is (= (g/index-to-coords grid 0) [0 0]))
  (is (= (g/index-to-coords grid 6) [1 1]))
  (is (= (g/index-to-coords grid 15) [0 3])))

(deftest piece-num-to-coords
  (let [full-grid-coords [[0 0] [1 0] [2 0] [3 0] [4 0]
                          [0 1] [1 1] [2 1] [3 1] [4 1]
                          [0 2] [1 2] [2 2] [3 2] [4 2]
                          [0 3] [1 3] [2 3] [3 3] [4 3]
                          [0 4] [1 4] [2 4] [3 4] [4 4]
                          [0 5] [1 5] [2 5] [3 5] [4 5]
                          [0 6] [1 6] [2 6] [3 6] [4 6]
                          [0 7] [1 7] [2 7] [3 7] [4 7]]]
    (is (= [[1 6] [2 6] [1 7] [2 7]] (g/piece-num-to-coords one-block-grid 0)))
    (is (= full-grid-coords (g/piece-num-to-coords full-grid 3)))))

(deftest get-points-center
  (is (= [0 0] (g/get-points-center [[0 0]])))
  (is (= [0 0] (g/get-points-center [[0 0] [1 1]])))
  (is (= [1 1] (g/get-points-center [[0 0] [1 1] [2 2]]))))
