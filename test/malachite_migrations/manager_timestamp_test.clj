(ns malachite-migrations.manager-timestamp-test
  (:use clojure.java.io)
  (:require [expectations :refer :all]
            [malachite-migrations.helpers :refer :all]
            [malachite-migrations.manager :refer :all]
            [malachite-migrations.db :refer :all]
            [malachite-migrations.core :refer :all]))

(def db-config 
  {
   :url "jdbc:postgresql://localhost/test-migrations"
   })

(delete-all-migrations! db-config)
;; Test reading and writing of timestamps to the migrations table
(write-timestamp! db-config 100)
(write-timestamp! db-config 101)

(let [ct (current-timestamp db-config)]
  (expect 101 ct))
(delete-timestamp! db-config 101)

(let [ct (current-timestamp db-config)]
  (expect 100 ct))

(delete-timestamp! db-config 100)

(expect (= 0 #spy/p (current-timestamp db-config)))
