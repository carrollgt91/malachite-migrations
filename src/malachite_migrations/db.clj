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
  [col]
  (let [col-name (name (first col))
        col-type (second col)
        sql-str (str "\n\t" col-name)]
    (cond
      (= col-type :string)
        (str sql-str " VARCHAR(64),"))))

(defn- parse-create-cols
  [columns base-sql-str]
  (loop [cols columns
         sql-str base-sql-str] 
    (if-not (first cols)
      (remove-from-end sql-str ",")
      (recur (rest cols) 
             (str sql-str (parse-create-col (first cols)))))))

(defn- add-cols-to-sql
  "Adds the columns to the given CREATE TABLE sql string"
  [columns base-sql-str]
  (let [sql-str (parse-create-cols columns base-sql-str)]
    (str sql-str ");")))

(defn create-table-sql 
  "Generates the sql string to create a table"
  [table-name columns]
  (let [base-sql-str (str "CREATE TABLE IF NOT EXISTS " table-name " (")]
    ; loop over all the columns, accumulating the SQL string as we go
    (add-cols-to-sql columns base-sql-str)))

(defn create-table
  "Creates a table on the DB specified in the config hash"
  [table-name & columns]
   (db/execute!
     (:url db-config)
     [(create-table-sql table-name columns)]))