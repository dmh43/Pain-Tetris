(ns pain-tetris.core
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [pain-tetris.frame :as f]
            [pain-tetris.shapes :as sh]
            [pain-tetris.util :as util]
            [pain-tetris.movement :as mo]
            [devtools.core :as devtools]))

(devtools/enable-feature! :sanity-hints :dirac)
(devtools/install!)

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  ; setup function returns initial state. It contains
  ; circle color and position.
  {:color 0
   :block-speed 5
   :pieces []})

(defn update-state [state]
  ; Update sketch state by changing circle color and position.
  {:color (mod (+ (:color state) 0.7) 255)
   :shapes (f/keep-in-frame (mo/gravity :right (:shapes state) 10))})

(defn draw-state [state]
  ; Clear the sketch by filling it with light-grey color.
  (q/background 240)
  ; Set circle color.
  (q/fill (:color state) 255 255)
  (q/no-stroke)
  ; Calculate x and y coordinates of the circle.
  (q/with-translation [(/ (q/width) 2)
                       (/ (q/height) 2)]
                                        ; Draw the circle.
    (doseq [piece (:pieces state)]
      (sh/draw-piece piece))))

(q/defsketch pain-tetris
  :host "pain-tetris"
  :size [300 500]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
