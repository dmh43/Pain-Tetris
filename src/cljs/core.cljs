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
            [devtools.core :as devtools]
            [om-bootstrap.random :as r]))

(devtools/enable-feature! :sanity-hints :dirac)
(devtools/install!)

(defn new-game-button [app-state owner]
  (reify
    om/IRender
    (render [this]
      (dom/div #js {:id "new-game"
                       :onClick (fn []
                                  (swap! t/grid g/clear-grid)
                                  (reset! g/piece-counter 0)
                                  (t/start-game))}
                  (r/glyphicon {:glyph "refresh"})))))

(defn points [app-state owner]
  (reify
    om/IRender
    (render [this]
      (js/console.log app-state)
      (dom/div #js {:id "points"} app-state))))

(defn root-component [app-state owner]
  (reify
    om/IRender
    (render [this]
      (js/console.log "app" app-state)
      (dom/div
       #js {:id "root"}
       (om/build new-game-button nil)
       (om/build points app-state)))))

(set! (.-onload js/window)
      #(om/root root-component t/points
                {:target (. js/document (getElementById "painTetris"))}))

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 10)
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
