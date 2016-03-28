(ns pain-tetris.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [pain-tetris.core-test]))

(enable-console-print!)

(doo-tests 'pain-tetris.core-test)
