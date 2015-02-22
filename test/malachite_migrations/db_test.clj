(ns malachite-migrations.db-test
  (:use malachite-migrations.db)
  (:require [expectations :refer :all]
            [clojure.java.jdbc :as db]
            [malachite-migrations.helpers :refer :all]))

; SQL Generation Tests
(expect (.contains (create-table-sql "users_db" [[:id :integer] [:name :string]]) 
  "id INTEGER"))

(expect (.contains (create-table-sql "users_db" [[:id :string] [:name :string]])
  "CREATE TABLE IF NOT EXISTS users"))

(expect (.contains (create-table-sql "users_db" [[:id :string] [:name :string]])
  "id VARCHAR(64)"))

(expect (.contains (create-table-sql "users_db" [[:id :string] [:name :string]]) 
  "name VARCHAR(64)"))

(expect (table-exists? "users_db") false)

; DB Integration Tests
(expect (create-table! "users_db" 
                      [[:id :integer]
                      [:name :string]]))

(expect (table-exists? "users_db") true)
(expect (column-exists? "users_db" "id") true)
(expect (column-exists? "users_db" "name") true)

(expect (add-column! "users_db" [:email :string]))
(expect (column-exists? "users_db" "email") true)


(expect (remove-column! "users_db" :email))
(expect (column-exists? "users_db" "email") false)

(expect (drop-table! "users_db"))

(expect (table-exists? "users_db") false)
