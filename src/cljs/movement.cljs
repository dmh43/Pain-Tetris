(ns pain-tetris.movement)

(defn shift-right
  [{:keys [x-pos y-pos] :as center} dist]
  (update center :x-pos #(+ % dist)))

(defn shift-left
  [{:keys [x-pos y-pos] :as center} dist]
  (update center :x-pos #(- % dist)))

(defn shift-up
  [{:keys [x-pos y-pos] :as center} dist]
  (update center :y-pos #(- % dist)))

(defn shift-down
  [{:keys [x-pos y-pos] :as center} dist]
  (update center :y-pos #(+ % dist)))

(defn shift
  [dir center dist]
  ((case dir
     :right shift-right
     :left shift-left
     :up shift-up
     :down shift-down) center dist))

(defn translate
  [dir piece dist]
  (mapv #(shift dir % dist) piece))

(defn gravity
  [dir pieces dist]
  (mapv #(translate dir % dist) pieces))

(defn can-move?
  [grid block-coords dir])
