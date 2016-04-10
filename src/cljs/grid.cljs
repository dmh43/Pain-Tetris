(ns pain-tetris.grid)

(def top-center)
(def bottom-center)

(defn build-grid
  [width height])

(defn insert-piece
  [grid shape side])

(defn can-insert?
  [grid shape side])

(defn block-below
  [grid coords])

(defn full-row?
  [grid row-num])

(defn full-grid
  [grid])
