(ns pain-tetris.shapes
  (:require [quil.core :as q :include-macros true]
            [pain-tetris.movement :as m]))

(def block-size 10)

(defn build-poly
  [x-pos y-pos path]
  {:type :poly :top-left {:x-pos x-pos :y-pos y-pos} :path path})

(defn build-path
  [dirs dists]
  (mapv (fn [dir dist]
          (assoc {} :dir dir :dist dist))
        dirs
        dists))

(defn L
  [scale]
  [{:dir :right :dist scale}
   {:dir :down :dist scale}
   {:dir :right :dist (* 2 scale)}
   {:dir :down :dist scale}
   {:dir :left :dist (* 3 scale)}
   {:dir :up :dist (* 2 scale)}])

(defn square
  [scale]
  (build-path
   [:right :down :left :up]
   [scale scale scale scale]))

(def L-blocks
  [{:x-pos 0 :y-pos 0} {:x-pos 0 :y-pos 10} {:x-pos 10 :y-pos 10}
   {:x-pos 20 :y-pos 10}])

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

(defn draw-block
  [{:keys [x-pos y-pos]}]
  (q/rect x-pos y-pos block-size block-size))

(defn draw-piece
  [piece]
  (doseq [block piece]
    (draw-block block)))
