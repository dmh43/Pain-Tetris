(ns pain-tetris.movement)

(defn shift-right
  [{:keys [x-pos y-pos] :as vertex} dist]
  (update vertex :x-pos #(+ % dist)))

(defn shift-left
  [{:keys [x-pos y-pos] :as vertex} dist]
  (update vertex :x-pos #(- % dist)))

(defn shift-up
  [{:keys [x-pos y-pos] :as vertex} dist]
  (update vertex :y-pos #(- % dist)))

(defn shift-down
  [{:keys [x-pos y-pos] :as vertex} dist]
  (update vertex :y-pos #(+ % dist)))

(defn shift
  [dir vertex dist]
  ((case dir
     :right shift-right
     :left shift-left
     :up shift-up
     :down shift-down) vertex dist))

(defn translate
  [shape dir dist]
  (update shape :top-left #(shift dir % dist)))

(defn gravity
  [shapes dir dist]
  (map #(translate % dir dist) shapes))
