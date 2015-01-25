(ns malachite-migrations.manager-migrations-test
  (:use clojure.java.io)
  (:require [expectations :refer :all]
            [malachite-migrations.manager :refer :all]
            [malachite-migrations.db :refer :all]
            [malachite-migrations.core :refer :all]))

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
