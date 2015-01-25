(ns malachite-migrations.manager-test
  (:use clojure.java.io)
  (:require [expectations :refer :all]
            [malachite-migrations.manager :refer :all]
            [malachite-migrations.db :refer :all]
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

(expect (nil? (current-timestamp)))

;; Test migrations
(let [fpath (generate-migration "create_users"
                                "users_mng"
                                :create-table
                                [:id :integer]
                                [:name :string])] 
  (migrate!)
  (expect (table-exists? "users_mng"))
  (delete-file fpath))

(let [ct (current-timestamp)]
;  the current timestamp should be a number
  (expect (number? ct))
  (delete-timestamp! ct))

(defn clean-up-mng-test
  {:expectations-options :after-run}
  []
  (drop-table "malachite_migrations")
  (drop-table "users_mng"))
