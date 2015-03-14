(ns malachite-migrations.helpers
  (:require [malachite-migrations.db :refer :all]
            [clojure.java.jdbc :as db]))

(defn table-exists?
  "Checks if a table with a given tablename exists"
  [db-config table-name]
  (:exists (first (db/query
              (:url db-config)
              [(str "SELECT EXISTS(
                  SELECT * 
                  FROM information_schema.tables 
                  WHERE 
                    table_name = '" table-name "'
                );")]))))

(defn index-exists?
  "Checks whether a given index exists"
  [db-config index-name]
  (:exists (first (db/query
                   (:url db-config)
                   [(str "SELECT EXISTS (
SELECT 1
FROM pg_class c
JOIN pg_namespace n ON n.oid = c.relnamespace
WHERE c.relname = '" index-name "');")]))))

(defn write-migrations-table!
  "Creates the malachite-migrations table on the database if it doesn't exist"
  [db-config]
  (when-not (table-exists? db-config "malachite_migrations")
    (do (create-table! db-config "malachite_migrations" [[:timestamp :bigint]])
        nil)))

(defn delete-all-migrations!
  "Deletes all records in the malachite-migrations table"
  [db-config]
  (db/execute!
   (:url db-config)
   ["DELETE FROM malachite_migrations"]))
