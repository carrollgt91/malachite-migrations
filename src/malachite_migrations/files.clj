(ns malachite-migrations.files
  (:use clojure.java.io)
  (:require [clj-time.core :as time]
            [clojure.string :as str]
            [clj-time.coerce :as coerce]))

(defn- current-timestamp
  "Generates a timestamp of the current millisecond"
  []
  (coerce/to-long (time/now)))

(defn grab-timestamp
  "Strips the timestamp from a migration file"
  [fpath]
  (let [fname (last (str/split fpath #"/"))
        timestamp (first (str/split fname #"_"))]
        (BigInteger. timestamp)))

(defn write-to-file
  "Writes the scaffolding of the migrations file to disc"
  [fpath contents]
  (with-open [writer (writer fpath)]
    (.write writer contents))
  fpath)

(defn- write-file
  "Writes the scaffolding of the migrations file to disc"
  [name]
    (let [fpath (str "migrations/" (current-timestamp) "_" name ".clj")]
      (write-to-file fpath "")))

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