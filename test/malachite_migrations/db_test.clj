(ns malachite-migrations.db-test
  (:use malachite-migrations.db)
  (:require [expectations :refer :all]
            [clojure.java.jdbc :as db]))

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
(expect (create-table "users_db" 
                      [[:id :integer]
                      [:name :string]]))

(expect (table-exists? "users_db") true)

(expect (drop-table "users_db"))

(expect (table-exists? "users_db") false)
