(ns pain-tetris.components.points
  (:require [om.dom :as dom]
            [om.core :as om]))

(defn points [app-state owner]
  (reify
    om/IRender
    (render [this]
      (dom/div #js {:id "points"} app-state))))
