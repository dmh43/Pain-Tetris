(ns pain-tetris.shapes
  (:require [quil.core :as q :include-macros true]))

(def block-size 10)

(def L
  [{:x-pos 0 :y-pos 0} {:x-pos 0 :y-pos 10} {:x-pos 10 :y-pos 10}
   {:x-pos 20 :y-pos 10}])

(defn draw-block
  [{:keys [x-pos y-pos]}]
  (q/rect x-pos y-pos block-size block-size))

(defn draw-piece
  [piece]
  (doseq [block piece]
    (draw-block block)))