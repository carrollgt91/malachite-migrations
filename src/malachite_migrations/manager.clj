(ns malachite-migrations.manager
  (:require [malachite-migrations.db :refer :all]))

(defn- current-timestamp
  "Grabs the latest timestamp in the database"
  []
  nil)

(defn migrate
  "Runs all migrations created after the latest timestamp in the database"
  []
  nil)

(defn rollback
  "Undoes the latest migration and removes that timestamp from the database"
  []
  nil)

(defn migrate-to
  "Runs all migrations between the latest timestamp in the database and the
  timestamp specified by time-stamp"
  [time-stamp]
  nil)