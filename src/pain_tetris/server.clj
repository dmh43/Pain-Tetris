(ns pain-tetris.server
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as resp]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn wrap-dir-index [handler]
  (fn [req]
    (handler
     (update-in req [:uri]
                #(if (= "/" %) "/index.html" %)))))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (wrap-dir-index (wrap-defaults app-routes site-defaults)))
