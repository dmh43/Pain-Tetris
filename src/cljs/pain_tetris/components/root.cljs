(ns pain-tetris.components.root
  (:require [pain-tetris.components.new-game :as c.ng]
            [pain-tetris.components.points :as c.p]
            [om.dom :as dom]
            [om.core :as om]))

(defn root-component [app-state owner]
  (reify
    om/IRender
    (render [this]
      (js/console.log "app" app-state)
      (dom/div
       #js {:id "root"}
       (om/build c.ng/new-game-button nil)
       (om/build c.p/points app-state)))))
