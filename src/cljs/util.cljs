(ns pain-tetris.util)

(defn bounded-by? [val up-bound low-bound]
  (and  (> val low-bound) (< val up-bound)))
