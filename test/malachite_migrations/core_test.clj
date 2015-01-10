(ns malachite-migrations.core-test
  (:use clojure.java.io)
  (:require [expectations :refer :all]
            [malachite-migrations.core :refer :all]
            [clj-time.core :as time]
            [clj-time.coerce :as coerce]))

