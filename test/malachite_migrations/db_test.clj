(ns malachite-migrations.db-test
  (:use malachite-migrations.db)
  (:require [expectations :refer :all]
            [clojure.java.jdbc :as db]))

; SQL Generation Tests
(expect (.contains (create-table-sql "users" [[:id :integer] [:name :string]]) 
  "id INTEGER"))

(expect (.contains (create-table-sql "users" [[:id :string] [:name :string]])
  "CREATE TABLE IF NOT EXISTS users"))

(expect (.contains (create-table-sql "users" [[:id :string] [:name :string]])
  "id VARCHAR(64)"))

(expect (.contains (create-table-sql "users" [[:id :string] [:name :string]]) 
  "name VARCHAR(64)"))

(expect (table-exists? "users") false)

; DB Integration Tests
(expect (create-table "users" 
                      [[:id :integer]
                      [:name :string]]))

(expect (table-exists? "users") true)

(expect (drop-table "users"))

(expect (table-exists? "users") false)
