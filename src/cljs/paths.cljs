(ns hello-quil.paths)

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
