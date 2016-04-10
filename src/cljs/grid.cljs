(ns pain-tetris.grid
  (:require [pain-tetris.pieces :as p]))

(def piece-counter (atom 0))

(defn build-grid
  [width height]
  (vec (take height
             (repeat (vec (take width
                                (repeat :_)))))))

(defn is-occupied?
  [grid coords]
  (= (get-in grid coords) :_))

(defn place-block
  [grid coords]
  (assoc-in grid coords @piece-counter))

(defn can-insert?
  [grid shape side])

(defn side-to-coords
  [grid side]
  (let [width (count (first grid))
        height (count grid)]
    (case side
      :top-center [0 (int (/ width 2))]
      :bottom-center [(int (/ height 2)) 0])))

(defn insert-piece
  [grid shape side]
  (if (can-insert? grid shape side)
    (reduce place-block grid (p/block-coords shape (side-to-coords grid side)))
    grid))

(defn block-below
  [grid coords])

(defn block-above
  [grid coords])

(defn full-row?
  [grid row-num])

(defn full-grid
  [grid]
  (mapv #(mapv (partial identity 3) %) grid))

(defn full-grid?
  [grid])
