(ns malachite-migrations.manager-migrations-test
  (:use clojure.java.io)
  (:require [expectations :refer :all]
            [malachite-migrations.helpers :refer :all]
            [malachite-migrations.manager :refer :all]
            [malachite-migrations.db :refer :all]
            [malachite-migrations.core :refer :all]))


(write-migrations-table!)

;; When there are no migrations, migrate should return nil and not fail
(expect nil (migrate!))

(let [fpath (generate-migration "create_users"
                                "users_mng"
                                :create-table
                                [:id :integer]
                                [:name :string])] 
  (migrate!)
  ;; (expect (table-exists? "users_mng"))
  (delete-file fpath))

(let [ct (current-timestamp)]
;  the current timestamp should be a number
  (expect (number? ct))
  (delete-timestamp! ct))

(let [fpath (generate-migration "create_users_mig_mng"
                                "users_mig_mng"
                                :create-table
                                [:id :integer]
                                [:name :string])
      fpath1 (generate-migration "create_users_mig_mng1"
                                "users_mig_mng1"
                                :create-table
                                [:id :integer]
                                [:name :string])]
  (migrate!)
  ;; migrate! should create both tables
  ;(expect (table-exists? "users_mig_mng"))
  ;(expect (table-exists? "users_mig_mng1"))
  ;; clean up the files generated 
  (delete-file fpath)
  (delete-file fpath1))

(defn clean-up-mng-test
  {:expectations-options :after-run}
  []
  (drop-table! "users_mng")
  (drop-table! "users_mig_mng")
  (drop-table! "users_mig_mng1"))
