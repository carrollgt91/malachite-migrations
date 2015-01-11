(ns malachite-migrations.db-test
  (:use malachite-migrations.db)
  (:require [expectations :refer :all]))


(expect (.contains (create-table-sql "users" [:id :string] [:name :string])
  "CREATE TABLE IF NOT EXISTS users"))

(expect (.contains (create-table-sql "users" [:id :string] [:name :string])
  "id VARCHAR(64)"))

(expect (.contains (create-table-sql "users" [:id :string] [:name :string])
  "name VARCHAR(64)"))