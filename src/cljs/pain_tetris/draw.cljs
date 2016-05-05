(ns pain-tetris.draw
  (:require [quil.core :as q :include-macros true]
            [pain-tetris.grid :as g]))

(def block-size 30)
(def border-size 100)

(defn border-width
  []
  (/ border-size 2))

(defn draw-block
  [x-pos y-pos c-target]
  (let [x (+ x-pos (/ border-size 2))
        y (+ y-pos (/ border-size 2))]
    (q/fill c-target)
    (q/rect x y block-size block-size)))

(defn piece-num-to-color
  [num]
  (q/color (* 255 (/ num (g/get-piece-counter))) 200 255 100))

(defn- draw-column
  [grid x-index]
  (let [{:keys [width height]} (g/dims grid)
        column (g/get-column grid x-index)
        x-pos (* x-index block-size)]
    (doseq [block column
            y-index (range height)]
      (let [y-pos (* y-index block-size)
            piece-num (g/get-block grid [x-index y-index])]
        (when (g/is-occupied? grid [x-index y-index])
          (draw-block x-pos y-pos (piece-num-to-color piece-num)))))))

(defn draw-grid
  [grid]
  (let [{:keys [width height]} (g/dims grid)]
    (doseq [x-index (range width)]
      (draw-column grid x-index))))

(defn game-size
  [grid-dims]
  (mapv #(* block-size (% grid-dims))
        [:width :height]))

(defn canvas-size
  [grid-dims]
  (mapv #(+ border-size (* block-size (% grid-dims)))
        [:width :height]))

(defn clear-game
  [grid-dims]
  (apply q/rect
         (border-width)
         (border-width)
         (game-size grid-dims)))
