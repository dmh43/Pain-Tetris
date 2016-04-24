(ns pain-tetris.turns
  (:require [pain-tetris.grid :as g]
            [pain-tetris.movement :as m]
            [pain-tetris.pieces :as p]
            [quil.core :as q :include-macros :true]
            [cljs.core.async :refer [chan close!]])
  (:require-macros
   [cljs.core.async.macros :as m :refer [go]]))

(def grid-dims {:width 10
                :height 20})

(defonce block-speed (atom 1))
(defonce drop-rate (atom 5))

(defonce timer (atom nil))
(defonce turn-counter (atom 0))
(defonce drop-side (atom :top))
(defonce gravity-direction (atom :down))

(defonce grid (atom (g/build-grid (:width grid-dims) (:height grid-dims))))

(defn timeout [ms]
  (let [c (chan)]
    (reset! timer (js/setTimeout (fn [] (close! c)) ms))
    c))

(defn toggle-gravity
  [dir]
  (if (= :up dir) :down :up))

(defn toggle-drop-side
  [dir]
  (if (= :top dir) :bottom :top))

(defn game-over
  []
  (js/console.log "game over!!"))

(declare start-game)

(defn flip-game
  []
  (swap! timer js/clearInterval)
  (swap! gravity-direction toggle-gravity)
  (swap! drop-side toggle-drop-side)
  (go
    (while (not (m/pieces-stopped? @grid @gravity-direction))
      (swap! grid #(m/gravity % @gravity-direction))
      (<! (timeout 100)))
    (js/clearInterval timer)
    (start-game)))

(defn next-turn
  []
  (when (m/pieces-stopped? @grid @gravity-direction)
    (let [piece (p/random-piece)]
      (if (g/can-insert? @grid piece (g/side-to-coords @grid @drop-side))
        (swap! grid #(g/drop-piece % piece @drop-side))
        (game-over))))
  (when (= 0 (rand-nth (range 10)))
    (flip-game))
  (swap! grid g/clear-full-rows)
  (swap! grid #(m/gravity % @gravity-direction))
  (swap! turn-counter inc))

(defn start-game
  []
  (reset! timer (js/setInterval next-turn (/ 1000 @block-speed))))

(defn key-pressed
  [state]
  (let [key (q/key-as-keyword)]
    (cond
      (some #(= % key) [:left :right])
      (when (m/can-move? @grid (g/get-current-piece) key)
        (swap! grid #(m/move % (g/get-current-piece) key)))
      (= key :down)
      (when (m/can-move? @grid (g/get-current-piece) @gravity-direction)
        (swap! grid #(m/move % (g/get-current-piece) @gravity-direction)))
      (= key :up)
      (swap! grid #(m/rotate % (g/piece-num-to-coords @grid (g/get-current-piece))))
      (= key (keyword " "))
      (swap! grid #(m/slam-piece % (g/get-current-piece) @gravity-direction)))
    state))