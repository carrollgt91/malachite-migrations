(ns malachite-migrations.manager
  (:require [malachite-migrations.db :refer :all]
            [clojure.java.jdbc :as db]))

(defn- write-migrations-table!
  "Creates the malachite-migrations table on the database if it doesn't exist"
  []
  (when-not (table-exists? "malachite_migrations")
    (do (create-table "malachite_migrations" [[:timestamp :integer]])
        nil)))

(defn delete-timestamp!
  "Removes a given timestamp from the migrations database table"
  [timestamp]
  (write-migrations-table!)
  (db/execute!
   (:url db-config)
   ["DELETE FROM malachite_migrations WHERE malachite_migrations.timestamp = ?;" timestamp]))

(defn write-timestamp!
  "Writes the timestamp to the database when the migration has been handled"
  [timestamp]
  (write-migrations-table!)
  (db/execute!
   (:url db-config)
   ["INSERT INTO malachite_migrations (timestamp) VALUES(?);" timestamp]))

(defn current-timestamp
  "Grabs the latest timestamp in the database"
  []
  (write-migrations-table!)
  (:timestamp
    (first
     (db/query
      (:url db-config)
      ["SELECT * FROM malachite_migrations
        ORDER BY timestamp DESC LIMIT 1;"]))))

(defn migrate!
  "Runs all migrations created after the latest timestamp in the database"
  []
  (write-migrations-table!)
  (let []))

(defn rollback!
  "Undoes the latest migration and removes that timestamp from the database"
  []
  nil)

(defn migrate-to!
  "Runs all migrations between the latest timestamp in the d
  timestamp specified by time-stamp"
  [time-stamp]
  nil)
