(ns malachite-migrations.db-test
  (:use malachite-migrations.db)
  (:require [expectations :refer :all]
            [clojure.java.jdbc :as db]))

; Helpers
(defn- table-exists?
  "Uses a sql query to check if a table exists"
  [table-name]
  (:exists (first (db/query
              (:url db-config)
              [(str "SELECT EXISTS(
                  SELECT * 
                  FROM information_schema.tables 
                  WHERE 
                    table_name = '" table-name "'
                );")]))))

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
                      [:id :integer]
                      [:name :string]))

(expect (table-exists? "users") true)

(expect (drop-table "users"))

(expect (table-exists? "users") false)
