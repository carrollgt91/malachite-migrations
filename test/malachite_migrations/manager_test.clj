(ns malachite-migrations.manager-test
  (:require [expectations :refer :all]
            [malachite-migrations.manager :refer :all]
            [malachite-migrations.core :refer :all]))


;; Test reading and writing of timestamps to the migrations table
(write-timestamp! 100)
(write-timestamp! 101)

(let [ct (current-timestamp)]
  (expect 101 ct))
(delete-timestamp! 101)

(let [ct (current-timestamp)]
  (expect 100 ct))

(delete-timestamp! 100)
