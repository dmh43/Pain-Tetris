(ns pain-tetris.core
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [pain-tetris.movement :as mo]
            [pain-tetris.pieces :as p]
            [pain-tetris.util :as util]
            [pain-tetris.movement :as mo]
            [pain-tetris.grid :as g]
            [pain-tetris.draw :as d]
            [pain-tetris.turns :as t]
            [pain-tetris.components.root :as c.r]
            [om.dom :as dom]
            [om.core :as om]))

(.log js/console "hiii")

(set! (.-onload js/window)
      #(om/root c.r/root-component t/points
                {:target (. js/document (getElementById "painTetris"))}))

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 5)
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  (t/start-game @t/block-speed)
  {:color 0
   :game (apply q/create-graphics (d/game-size t/grid-dims))
   :border (apply q/create-graphics (d/canvas-size t/grid-dims))})

(defn update-state [state]
  state)

(defn draw-state [state]
  (let [game (get state :game)
        border (get state :border)]
    (.beginDraw border)
    (q/background 255 255 255 0)
    (.endDraw border)
    (.beginDraw game)
    (q/fill 255)
    (d/clear-game t/grid-dims)
    (q/stroke-weight 0)
    (q/stroke-join :round)
    (d/draw-grid @t/grid)
    (.endDraw game)
    ))

(defn on-js-reload
  []
  (swap! t/timer js/clearInterval))

;; FIXME: This method is here due to a quil bug not calling handler if
;; in another ns
(defn mouse-pressed
  [state event]
  (let [x (q/mouse-x)
        y (q/mouse-y)
        piece-num (g/get-current-piece)]
    (if (and (> y 200) (< y 500))
      (if (> x 150)
        (when (mo/can-move? @t/grid piece-num :right)
          (swap! t/grid #(mo/move % piece-num :right)))
        (when (mo/can-move? @t/grid piece-num :left)
          (swap! t/grid #(mo/move % piece-num :left))))
      (if (> y 350)
        (swap! t/grid #(mo/slam-piece % piece-num @t/gravity-direction))
        (swap! t/grid #(mo/rotate % (g/piece-num-to-coords @t/grid piece-num))))))
  state)

(q/defsketch canvas
  :host "canvas"
  :size (d/canvas-size t/grid-dims)
  :setup setup
  :update update-state
  :draw draw-state
  :key-pressed t/key-pressed
  :mouse-pressed mouse-pressed
  :middleware [m/fun-mode]
  :features [:global-key-events]
  ;; :settings q/smooth
  )
