(ns pain-tetris.grid
  (:require [pain-tetris.pieces :as p]))

(defonce piece-counter (atom 0))

(def y-hat [0 1])
(def x-hat [1 0])

(defn get-piece-counter
  []
  @piece-counter)

(defn increment-piece-counter
  []
  (swap! piece-counter inc))

(defn add
  [p1 p2]
  (mapv + p1 p2))

(defn sub
  [p1 p2]
  (mapv - p1 p2))

(defn neg
  [p]
  (sub (repeat 0) p))

(defn build-grid
  [width height]
  (vec (take width
             (repeat (vec (take height
                                (repeat :_)))))))

(defn is-occupied?
  [grid coords]
  (not (= (get-in grid coords) :_)))

(defn place-block
  [grid coords elem]
  (assoc-in grid coords elem))

(defn insert-block
  [grid coords]
  (place-block grid coords (get-piece-counter)))

(defn remove-block
  [grid coords]
  (place-block grid coords :_))

(defn can-insert?
  [grid shape coords]
  (every? false? (map #(is-occupied? grid %)
                  (p/get-shape-block-coords shape coords))))

(defn dims
  [grid]
  {:width (count grid)
   :height (count (first grid))})

(defn side-to-coords
  [grid side]
  (let [{:keys [width height]} (dims grid)
        center (dec (int (/ width 2)))]
    (case side
      :top-center [center 0]
      :bottom-center [center (- height 2)]
      :top (side-to-coords grid :top-center)
      :bottom (side-to-coords grid :bottom-center))))

(defn insert-piece
  [grid shape side]
  (if (can-insert? grid shape (side-to-coords grid side))
    (reduce insert-block grid (p/get-shape-block-coords shape (side-to-coords grid side)))
    grid))

(defn drop-piece
  [grid piece side]
  (let [new-grid (insert-piece grid piece side)]
    (increment-piece-counter)
    new-grid))

(defn block-below
  [grid coords]
  (let [coords-below (add coords y-hat)]
    (is-occupied? grid coords-below)))

(defn block-above
  [grid coords]
  (let [coords-above (sub coords y-hat)]
    (is-occupied? grid coords-above)))

(defn index-to-coords
  [grid index]
  (let [{:keys [width height]} (dims grid)
        y (int (/ index width))
        x (int (- index (* y width)))]
    [x y]))

(defn all-coords
  [grid]
  (map (partial index-to-coords grid)
       (range (apply * ((juxt :width :height) (dims grid))))))

(defn full-row?
  [grid row-num]
  (let [row (mapv #(get % row-num) grid)]
    (every? (partial not= :_) row)))

(defn full-grid
  [grid]
  (mapv #(mapv (partial identity 3) %) grid))

(defn full-grid?
  [grid]
  (every? (partial is-occupied? grid)
          (all-coords grid)))

(defn piece-num-to-coords
  [grid piece-num]
  (let [{:keys [width height]} (dims grid)]
    (reduce (fn [acc coords]
              (if (= piece-num (get-in grid coords))
                (conj acc coords)
                acc))
            []
            (map (partial index-to-coords grid)
                 (range (* width height))))))

(defn get-column
  [grid x-index]
  (get grid x-index))

(def origin-corner (atom :tl))

(def piece-origin (atom {}))

(defn get-points-center
  [points]
  (let [origin (get @piece-origin (get-piece-counter))
        new-origin (mapv #(Math/ceil (/ % (count points)))
                         (reduce (fn [acc coords]
                                   (add acc coords))
                                 [0 0]
                                 points))]
    (do (reset! piece-origin (assoc {} (get-piece-counter) new-origin))
        new-origin)))

(defn clear-row
  [grid row-num]
  (reduce (fn [grid x]
            (remove-block grid [x row-num]))
          grid
          (range (:width (dims grid)))))

(defn full-rows
  [grid]
  (reduce (fn [acc row-num]
            (if (full-row? grid row-num)
              (conj acc row-num)
              acc))
          []
          (range (:height (dims grid)))))

(defn clear-grid
  [grid]
  (apply build-grid ((juxt :width :height) (dims grid))))

                                        ; Transducers??
(defn clear-full-rows
  [grid]
  (reduce clear-row grid (full-rows grid)))

(defn all-pieces
  [grid]
  (range (inc (get-piece-counter))))

(defn get-current-piece
  []
  (dec (get-piece-counter)))

(defn empty-locs
  [grid]
  (reduce (fn [acc coords]
            (if (not (is-occupied? grid coords))
              (conj acc coords)
              acc))
          []
          (all-coords grid)))
