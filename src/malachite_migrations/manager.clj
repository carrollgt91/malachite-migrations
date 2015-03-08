(ns malachite-migrations.manager
  (:require [malachite-migrations.db :refer :all]
            [malachite-migrations.files :refer :all]
            [clojure.java.io :refer :all]
            [clojure.java.jdbc :as db]))

(defn- migrations
  "Grabs all migration files"
  []
  (map #(.getPath %) (rest (file-seq (as-file "migrations/")))))

(defn- migration
  [timestamp]
  (first (filter #(.startsWith % (str "migrations/" timestamp)) (migrations))))

;; TODO: Implement grabbing latest timestamp from db, right now hardcoded
(defn- pending-migrations
  "Grabs all migration file paths which have not yet been run by checking
   for the most recently ran migration's timestamp in the migrations table
   and returning all migrations which have a timestamp which is greater"
  [migration-files]
  (filter #(> (get-timestamp %) 1345) migration-files))

(defn delete-timestamp!
  "Removes a given timestamp from the migrations database table"
  [db-config timestamp]
  (db/execute!
   (:url db-config)
   ["DELETE FROM malachite_migrations WHERE malachite_migrations.timestamp = ?;" timestamp]))

(defn write-timestamp!
  "Writes the timestamp to the database when the migration has been handled"
  [db-config timestamp]
  (db/execute!
   (:url db-config)
   ["INSERT INTO malachite_migrations (timestamp) VALUES(?);" timestamp]))

(defn current-timestamp
  "Grabs the latest timestamp in the database"
  [db-config]
  (:timestamp
    (first
     (db/query
      (:url db-config)
      ["SELECT * FROM malachite_migrations
        ORDER BY timestamp DESC LIMIT 1;"]))))

(defn migrate!
  "Runs all migrations created after the latest timestamp in the database"
  [db-config]
  (let [ct (or (current-timestamp db-config) 0)
        pending-migrations (pending-migrations (migrations))]
    (if-not (empty? pending-migrations)
      ; run all migrations; if they succeed, write the timestamp to the database
      (doseq [mig pending-migrations]
        ((load-file mig) db-config :up)
        (write-timestamp! db-config (get-timestamp mig)))
      (println "No pending migrations."))))

(defn rollback!
  "Undoes the latest migration and removes that timestamp from the database"
  [db-config]
  (let [ct (current-timestamp db-config)]
    (cond
     (nil? ct)
     nil
     (not (nil? ct))
     (do ((load-file (migration ct)) :down)
         (delete-timestamp! db-config ct)))))

(defn migrate-to!
  "Runs all migrations between the latest timestamp in the d
  timestamp specified by time-stamp"
  [time-stamp]
  nil)
