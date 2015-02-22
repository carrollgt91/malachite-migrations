(ns malachite-migrations.helpers
  (:require [malachite-migrations.db :refer :all]
            [clojure.java.jdbc :as db]))

(defn table-exists?
  "Checks if a table with a given tablename exists"
  [table-name]
  (:exists (first (db/query
              (:url db-config)
              [(str "SELECT EXISTS(
                  SELECT * 
                  FROM information_schema.tables 
                  WHERE 
                    table_name = '" table-name "'
                );")]))))

(defn write-migrations-table!
  "Creates the malachite-migrations table on the database if it doesn't exist"
  []
  (when-not (table-exists? "malachite_migrations")
    (do (create-table! "malachite_migrations" [[:timestamp :bigint]])
        nil)))

(defn delete-all-migrations!
  "Deletes all records in the malachite-migrations table"
  []
  (db/execute!
   (:url db-config)
   ["DELETE FROM malachite_migrations"]))
