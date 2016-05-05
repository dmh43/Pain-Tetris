(ns pain-tetris.movement
  (:require [pain-tetris.grid :as g]))

(defn- move-block-destination
  [grid coords dir]
  (let [offset (case dir
                 :up (g/neg g/y-hat)
                 :down g/y-hat
                 :left (g/neg g/x-hat)
                 :right g/x-hat)
        destination (g/add coords offset)]
    destination))

(defn- block-can-move?
  [grid coords dir]
  (let [destination (move-block-destination grid coords dir)]
    (and
     (every? #(>= % 0) destination)
     (not (g/is-occupied? grid destination)))))

(defn can-move?
  [grid piece-num dir]
  (let [blocks (g/piece-num-to-coords grid piece-num)]
    (when ((complement empty?) blocks)
      (every? (fn [block]
                (let [destination-blocks (move-block-destination grid block dir)]
                  (or (some #(= % destination-blocks) blocks)
                      (block-can-move? grid block dir))))
              blocks))))

(defn move
  [grid piece-num dir]
  (let [old-coords (g/piece-num-to-coords grid piece-num)
        unit-vec (case dir
                   :up (g/neg g/y-hat)
                   :down g/y-hat
                   :left (g/neg g/x-hat)
                   :right g/x-hat)
        new-coords (mapv g/add old-coords (repeat unit-vec))
        cleared-grid (reduce (fn [grid coords]
                               (g/remove-block grid coords))
                             grid
                             old-coords)
        redrawn-grid (reduce (fn [grid coords]
                               (g/place-block grid coords piece-num))
                             cleared-grid
                             new-coords)]
    redrawn-grid))

(defn gravity
  [grid dir]
  (reduce (fn [grid piece-num]
            (if (can-move? grid piece-num dir)
              (move grid piece-num dir)
              grid))
          grid
          (g/all-pieces grid)))

(defn rotate-about-origin
  [coords]
  (mapv * [1 -1] (reverse coords)))

(defn rotation-destination
  [grid piece-coords]
  (let [origin (g/get-points-center piece-coords)
        shifted-coords (mapv #(g/sub % origin) piece-coords)
        rotated-coords (mapv rotate-about-origin shifted-coords)
        restored-coords (mapv #(g/add % origin) rotated-coords)]
    restored-coords))

(defn can-rotate?
  [grid piece-coords]
  (every? (fn [coords]  (or (some #(= % coords) (rotation-destination grid piece-coords))
                            (not (g/is-occupied? grid coords))))
          restored-coords))

(defn rotate
  [grid piece-coords]
  (if (every? (fn [coords] (or (some #(= % coords) piece-coords)
                               (not (g/is-occupied? grid coords))))
              (rotation-destination grid piece-coords))
    (as-> grid new-grid
      (reduce #(g/remove-block %1 %2) new-grid piece-coords)
      (reduce #(g/place-block %1 %2 elem) new-grid restored-coords))
    grid))

(defn slam-piece
  [grid piece-num dir]
  (if (can-move? grid piece-num dir)
    (recur (move grid piece-num dir) piece-num dir)
    grid))

(defn pieces-stopped?
  [grid gravity-direction]
  (not (some #(can-move? grid % gravity-direction)
             (g/all-pieces grid))))
