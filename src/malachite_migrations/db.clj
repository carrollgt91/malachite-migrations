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

(def type-map 
   {
    :string  "VARCHAR(64)"
    :integer "INTEGER"
    :bigint  "BIGINT"
   })

(defn- parse-col
  "Takes a vector of column information and generates the line of SQL needed
   for adding that column to the table during a create table query"
  [base-sql-str col]
  (let [col-name (name (first col))
        col-type (second col)
        sql-str (str base-sql-str "\n\t" col-name)]
    (str sql-str " " (col-type type-map) ",")))

(defn- add-cols-to-sql
  "Adds the columns to the given CREATE TABLE sql string"
  [columns base-sql-str]
  (let [naive-sql-str (reduce parse-col
                        base-sql-str
                        columns)
        sql-str (remove-from-end naive-sql-str ",")]
    (str sql-str ");")))

(defn column-exists?
  "Checks if a table with a given tablename exists"
  [table-name col-name]
  (not
   (nil?
    (first
     (db/query
      (:url db-config)
      [#spy/p (str "SELECT column_name " 
                   "FROM information_schema.columns " 
                   "WHERE table_name='" table-name
                   "' and column_name='" col-name "';")])))))

(defn create-table-sql 
  "Generates the sql string to create a table"
  [table-name columns]
  (let [base-sql-str (str "CREATE TABLE IF NOT EXISTS " table-name " (")]
    ; loop over all the columns, accumulating the SQL string as we go
    (add-cols-to-sql columns base-sql-str)))

(defn add-column-sql
  "Generates the sql string to add a column"
  [table-name [col-name col-type]]
  (str "ALTER TABLE " table-name " ADD COLUMN " (name  col-name) " " (col-type type-map) ";"))

(defn drop-table! 
  "Drops table with a given table name"
  [table-name]
  (db/execute!
    (:url db-config)
    [(str "DROP TABLE IF EXISTS " table-name ";")]))

(defn create-table!
  "Creates a table on the DB specified in the config hash"
  [table-name columns]
   (db/execute!
     (:url db-config)
     [(create-table-sql table-name columns)]))

(defn create-table
  "Generates a function which chooses between the creation
   and destruction of a table depending on whether :up or :down
   is passed to it"
  [table-name columns]
  (fn [symbol]
    (cond
     (= :up symbol)
       (create-table! table-name columns)
     (= :down symbol)
       (drop-table! table-name))))
(defn remove-column!
  "Removes a column from the table with table-name on the
   DB specified in the config hash"
  [table-name column-name]
  (db/execute!
   (:url db-config)
   [(str "ALTER TABLE " table-name " DROP COLUMN " (name column-name))]))

(defn add-column!
  "Adds a column to the table with table-name on the DB
   specified in the config hash"
  [table-name column]
  (db/execute!
   (:url db-config)
    [(add-column-sql table-name column)]))

(defn add-column 
  "Generates a function which chooses between the addition 
   and removal of a table depending on whether :up or :down
   is passed to it"
  [table-name column]
  (fn [symbol]
    (cond
     (= :up symbol)
       (add-column! table-name column)
     (= :down symbol)
       (remove-column! table-name (first column)))))
