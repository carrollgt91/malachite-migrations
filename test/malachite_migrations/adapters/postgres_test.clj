(ns malachite-migrations.adapters.postgres-test
  (:require [malachite-migrations.adapters.postgres :refer :all]
            [malachite-migrations.sqlgen :refer :all])
  (:require [expectations :refer :all]
            [malachite-migrations.helpers :refer :all]))

(def pg (postgres))

(expect (.contains (create-table pg "users_db" [[:id :integer] [:name :string]])
                   "id INTEGER"))

(expect (.contains (create-table pg "users_db" [[:id :integer] [:name :string]])
                   "CREATE TABLE IF NOT EXISTS users"))

(expect (.contains (create-table pg "users_db" [[:id :integer] [:name :string]])
                   "name VARCHAR(64)"))

(expect (.contains (drop-table pg "users_db")
                   "DROP TABLE IF EXISTS users_db"))

(expect (.contains (add-column pg "users_db" [:email :string])
                   "ALTER TABLE users_db"))

(expect (.contains (add-column pg "users_db" [:email :string])
                   "ADD COLUMN email VARCHAR(64)"))

(expect (.contains (remove-column pg "users_db" :email)
                   "ALTER TABLE users_db"))

(expect (.contains (remove-column pg "users_db" :email)
                   "DROP COLUMN email"))

