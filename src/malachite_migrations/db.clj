(ns malachite-migrations.db
    (:require [clojure.java.jdbc :as db]))
(def db-config 
  {
    :url "jdbc:postgresql://localhost/test-migrations"
  })

(defn- remove-from-end
  "chops the string end off of the string s if end is at the end of s"
  [s end]
  (if (.endsWith s end)
    (.substring s 0 (- (count s)
                       (count end)))))

(defn- parse-create-col
  "Takes a vector of column information and generates the line of SQL needed
   for adding that column to the table during a create table query"
  [base-sql-str col]
  (let [col-name (name (first col))
        col-type (second col)
        sql-str (str base-sql-str "\n\t" col-name)]
    (cond
      (= col-type :string)
        (str sql-str " VARCHAR(64),")
      (= col-type :integer)
        (str sql-str " INTEGER,"))))

(defn- add-cols-to-sql
  "Adds the columns to the given CREATE TABLE sql string"
  [columns base-sql-str]
  (let [naive-sql-str (reduce parse-create-col
                        base-sql-str
                        columns)
        sql-str (remove-from-end naive-sql-str ",")]
    (str sql-str ");")))

(defn create-table-sql 
  "Generates the sql string to create a table"
  [table-name columns]
  (let [base-sql-str (str "CREATE TABLE IF NOT EXISTS " table-name " (")]
    ; loop over all the columns, accumulating the SQL string as we go
    (add-cols-to-sql columns base-sql-str)))

(defn drop-table 
  "Drops table with a given table name"
  [table-name]
  (db/execute!
    (:url db-config)
    [(str "DROP TABLE IF EXISTS " table-name ";")]))

(defn create-table
  "Creates a table on the DB specified in the config hash"
  [table-name columns]
   (db/execute!
     (:url db-config)
     [(create-table-sql table-name columns)]))