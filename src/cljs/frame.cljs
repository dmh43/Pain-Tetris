(ns hello-quil.frame
  (:require [quil.core :as q :include-macros true]))

(defn right-bounds []
  (/ (q/width) 2))

(defn left-bounds []
  (- (right-bounds)))

(defn bottom-bounds []
  (/ (q/height) 2))

(defn top-bounds []
  (- (bottom-bounds)))

(defn beyond-right-bounds?
  [vertex]
  (let [x-pos (:x-pos vertex)]
    (> x-pos (right-bounds))))

(defn beyond-left-bounds?
  [vertex]
  (let [x-pos (:x-pos vertex)]
    (< x-pos (left-bounds))))

(defn beyond-top-bounds?
  [vertex]
  (let [y-pos (:y-pos vertex)]
    (< y-pos (top-bounds))))

(defn beyond-bottom-bounds?
  [vertex]
  (let [y-pos (:y-pos vertex)]
    (> y-pos (bottom-bounds))))

(defn max-width
  [poly]
  (reduce (fn [acc step]
            (+ acc (if (= (:dir step) :right)
                     (+ (:dist step))
                     0)))
          0
          (:path poly)))

(defn max-height
  [poly]
  (reduce (fn [acc step]
            (+ acc (if (= (:dir step) :up)
                     (+ (:dist step))
                     0)))
          0
          (:path poly)))

(defn offscreen-right?
  [shape]
  (> (+ (get-in shape [:top-left :x-pos]) (max-width shape))
     (right-bounds)))

(defn offscreen-left?
  [shape]
  (beyond-left-bounds? (:top-left shape)))

(defn offscreen-top?
  [shape]
  (beyond-top-bounds? (:top-left shape)))

(defn offscreen-bottom?
  [shape]
  (> (+ (get-in shape [:top-left :y-pos]) (max-height shape))
     (bottom-bounds)))

(defn coerce-into-frame
  [shape]
  (cond-> shape
    (offscreen-right? shape) (assoc-in [:top-left :x-pos]
                                       (- (right-bounds) (max-width shape)))
    (offscreen-left? shape) (assoc-in [:top-left :x-pos]
                                      (left-bounds))
    (offscreen-bottom? shape) (assoc-in [:top-left :y-pos]
                                        (- (bottom-bounds) (max-height shape)))
    (offscreen-top? shape) (assoc-in [:top-left :y-pos]
                                     (top-bounds))))

(defn keep-in-frame
  [shapes]
  (map coerce-into-frame shapes))
