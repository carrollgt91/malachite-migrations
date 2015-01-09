(ns malachite-migrations.files
  (:use clojure.java.io
        clojure.string)
  (:require [clj-time.core :as time]
            [clj-time.coerce :as coerce]))

(defn- current-timestamp
  "Generates a timestamp of the current millisecond"
  []
  (coerce/to-long (time/now)))

(defn grab-timestamp
  "Strips the timestamp from a migration file"
  [fpath]
  (let [fname (last (split fpath #"/"))
        timestamp (first (split fname #"_"))]
        (BigInteger. timestamp)))

(defn- write-file
  "Writes the scaffolding of the migrations file to disc"
  [name]
    (let [fpath (str "migrations/" (current-timestamp) "_" name ".clj")]
        (with-open [writer (writer fpath)]
          (.write writer "Testing the writer yo"))
        fpath))

(defn- create-migrations-dir
  "Creates the migrations directory"
  []
  (.mkdir (java.io.File. "migrations")))

(defn make-file
  "Writes the scaffolding of a migration file to disc, creating the migrations directory if necessary"
  [name]
    (if (.exists (file "migrations/"))
        (write-file name)
        (create-migrations-dir)))