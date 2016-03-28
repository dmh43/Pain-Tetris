(ns demom.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [demom.core-test]))

(enable-console-print!)

(doo-tests 'demom.core-test)
