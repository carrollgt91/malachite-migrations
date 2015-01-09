(ns malachite-migrations.core-test
  (:use clojure.java.io
        clojure.string)
  (:require [expectations :refer :all]
            [malachite-migrations.core :refer :all]
            [clj-time.core :as time]
            [clj-time.coerce :as coerce]))

(defn- current-timestamp
  "Generates a timestamp of the current millisecond"
  []
  (coerce/to-long (time/now)))
