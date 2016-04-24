(ns pain-tetris.movement-test
  (:require-macros [cljs.test :refer (is deftest testing)])
  (:require [pain-tetris.movement :as mov]
            [pain-tetris.grid :as g]
            [cljs.test :as t]))

(deftest can-move?
  (testing "check that a block or piece can move"
    (let [grid (g/build-grid 10 10)
          one-piece-grid (g/insert-piece grid :square :top)
          block-coords (g/side-to-coords grid :top-center)]
      (is (not (@#'mov/block-can-move? one-piece-grid block-coords :down)))
      (is (not (@#'mov/block-can-move? one-piece-grid block-coords :up)))
      (is (@#'mov/block-can-move? one-piece-grid [5 1] :down))
      (is (mov/can-move? one-piece-grid 0 :down))
      (is (not (mov/can-move? one-piece-grid 0 :up))))))

(deftest move
  (let [grid (g/build-grid 10 10)
        one-piece-grid (g/insert-piece grid :square :top)
        one-piece-grid-bottom (g/insert-piece grid :square :bottom)
        block-coords (g/side-to-coords grid :top-center)]
    (is (not= one-piece-grid (mov/move one-piece-grid 0 :down)))
    (is (= one-piece-grid-bottom (nth (iterate #(mov/move % 0 :down) one-piece-grid) 8)))))

(deftest can-rotate?
  (let [grid (g/build-grid 10 10)
        one-piece-grid (g/insert-piece grid :square :top)
        two-piece-grid (g/insert-piece one-piece-grid :L :bottom)
        block-coords (g/side-to-coords grid :top-center)]
    (is (mov/can-rotate? one-piece-grid (g/piece-num-to-coords grid 0)))
    (is (mov/can-rotate? one-piece-grid (g/piece-num-to-coords grid 1)))))

(deftest rotate
  (let [grid (g/build-grid 10 10)
        one-piece-grid (g/insert-piece grid :square :top)
        _ (g/increment-piece-counter)
        two-piece-grid (g/insert-piece one-piece-grid :L :bottom)
        block-coords (g/side-to-coords grid :top-center)]
    (is (= two-piece-grid
           (nth (iterate #(mov/rotate % (g/piece-num-to-coords two-piece-grid 1)) two-piece-grid) 4)))))

(deftest default
  (is (= 10 10)))
