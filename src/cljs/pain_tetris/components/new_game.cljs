(ns pain-tetris.components.new-game
  (:require [pain-tetris.turns :as t]
            [pain-tetris.grid :as g]
            [om-bootstrap.random :as r]
            [om.dom :as dom]
            [om.core :as om]))

(defn new-game-button [app-state owner]
  (reify
    om/IRender
    (render [this]
      (dom/div #js {:id "new-game"
                    :onClick (fn []
                               (swap! t/grid g/clear-grid)
                               (reset! g/piece-counter 0)
                               (t/start-game @t/block-speed))}
               (r/glyphicon {:glyph "refresh"})))))
