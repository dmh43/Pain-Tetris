(ns pain-tetris.pieces
  (:require [quil.core :as q :include-macros true]))

(def pieces [:square :backL :S :L :T :Z :|])

(defn base-coords
  [shape]
  (case shape
    :backL [[0 0] [1 0] [2 0] [2 1]]
    :S [[1 0] [0 1]
        [1 1] [2 0]]
    :square [[0 0] [0 1]
             [1 0] [1 1]]
    :L [[0 0] [1 0] [2 0]
        [0 1]]
    :T [[0 0] [1 0] [2 0]
        [1 1]]
    :Z [[0 0] [1 0]
        [1 1] [2 1]]
    :| [[0 0] [1 0] [2 0] [3 0]]))

(defn rotate-coords
  [shape]
  (case shape
    :backL [[0 0] [1 0] [2 0] [2 1]]
    :S [[1 0] [0 1]
        [1 1] [2 0]]
    :square [[0 0] [0 1]
             [1 0] [1 1]]
    :L [[0 0] [1 0] [2 0]
        [0 1]]
    :T [[0 0] [1 0] [2 0]
        [1 1]]
    :Z [[0 0] [1 0]
        [1 1] [2 1]]
    :| [[0 0] [1 0] [2 0] [3 0]]))

(defn get-shape-block-coords
  [shape top-left-coords]
  (let [blocks-coords (base-coords shape)
        num-blocks (count blocks-coords)]
    (mapv #(mapv + %1 %2)
          blocks-coords
          (take num-blocks (repeat top-left-coords)))))

(defn random-piece
  []
  (rand-nth pieces))
