(ns hello-quil.shapes
  (:require [quil.core :as q :include-macros true]
            [hello-quil.movement :as m]))

(defn draw-poly
  [{{:keys [x-pos y-pos]} :top-left path :path}]
  (q/begin-shape)
  (q/vertex x-pos y-pos)
  (loop [path path
         vert {:x-pos x-pos :y-pos y-pos}]
    (let [{:keys [dir dist]} (first path)
          shifted-vertex (m/shift dir vert dist)]
      (q/vertex (:x-pos shifted-vertex) (:y-pos shifted-vertex))
      (when (> (count path) 1)
        (recur (rest path) shifted-vertex))))
  (q/end-shape))

(defn draw-piece
  [piece]
  ())
