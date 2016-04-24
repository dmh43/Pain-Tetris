(ns pain-tetris.core
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [pain-tetris.pieces :as p]
            [pain-tetris.util :as util]
            [pain-tetris.movement :as mo]
            [pain-tetris.grid :as g]
            [pain-tetris.draw :as d]
            [pain-tetris.turns :as t]
            [om.core :as om]
            [om.dom :as dom]
            [devtools.core :as devtools]))

(devtools/enable-feature! :sanity-hints :dirac)
(devtools/install!)

(defn clear-button [data owner]
  (reify
    om/IRender
    (render [this]
      (dom/button #js {:onClick #(swap! t/grid g/clear-grid)} "Clear"))))

(set! (.-onload js/window)
      #(om/root clear-button "data"
                {:target (. js/document (getElementById "painTetris"))}))

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  (t/start-game)
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

(defn fig-reload-hook
  []
  (swap! t/timer js/clearInterval))

(q/defsketch canvas
  :host "canvas"
  :size (d/canvas-size t/grid-dims)
  :setup setup
  :update update-state
  :draw draw-state
  :key-pressed t/key-pressed
  :middleware [m/fun-mode]
  :features [:global-key-events]
  :settings q/smooth)
