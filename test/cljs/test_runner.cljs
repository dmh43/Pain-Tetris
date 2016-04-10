(ns pain-tetris.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [pain-tetris.core-test]
   [pain-tetris.grid-test]
   [pain-tetris.movement-test]))

(enable-console-print!)

(doo-tests 'pain-tetris.core-test
           'pain-tetris.grid-test
           'pain-tetris.movement-test)

#_(doo.runner/doo-all-tests)
