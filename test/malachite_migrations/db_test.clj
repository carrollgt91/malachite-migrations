(ns malachite-migrations.db-test
  (:use malachite-migrations.db)
  (:require [expectations :refer :all]
            [clojure.java.jdbc :as db]
            [malachite-migrations.helpers :refer :all]))

(def db-config 
  {
   :url "jdbc:postgresql://localhost/test-migrations"
   })

; SQL Generation Tests
(expect (.contains (create-table-sql "users_db" [[:id :integer] [:name :string]]) 
  "id INTEGER"))

(expect (.contains (create-table-sql "users_db" [[:id :string] [:name :string]])
  "CREATE TABLE IF NOT EXISTS users"))

(expect (.contains (create-table-sql "users_db" [[:id :string] [:name :string]])
  "id VARCHAR(64)"))

(expect (.contains (create-table-sql "users_db" [[:id :string] [:name :string]]) 
  "name VARCHAR(64)"))

(expect (table-exists? db-config "users_db") false)

; DB Integration Tests
(expect (create-table! db-config "users_db" 
                      [[:id :integer]
                      [:name :string]]))

(expect (table-exists? db-config "users_db") true)
(expect (column-exists? db-config "users_db" "id") true)
(expect (column-exists? db-config "users_db" "name") true)

(expect (add-column! db-config "users_db" [:email :string]))
(expect (column-exists? db-config "users_db" "email") true)

(expect (add-index! db-config "users_db" [:email]))
(expect (index-exists? db-config "users_db_email_idx") true)
(expect (remove-index! db-config "users_db" [:email]))
(expect (index-exists? db-config "users_db_email_idx") false)

;; index functionality using the :up :down wrapper
(let [add-users-db-email-idx (add-index "users_db" [:email])]
  (expect (add-users-db-email-idx db-config :up))
  (expect (index-exists? db-config "users_db_email_idx") true)
  (expect (add-users-db-email-idx db-config :down))
  (expect (index-exists? db-config "users_db_email_idx") false))


(expect (remove-column! db-config "users_db" :email))
(expect (column-exists? db-config "users_db" "email") false)

(expect (drop-table! db-config "users_db"))

(expect (table-exists? db-config "users_db") false)
