(ns malachite-migrations.adapters.postgres
  (:use [malachite-migrations.sqlgen])
    (:require [clojure.string :refer [join]]))

(def type-map 
   {
    :string  "VARCHAR(64)"
    :integer "INTEGER"
    :bigint  "BIGINT"
   })

(defn- remove-from-end
  "chops the string end off of the string s if end is at the end of s"
  [s end]
  (if (.endsWith s end)
    (.substring s 0 (- (count s)
                       (count end)))))

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

(defn- add-index-sql
  [table-name columns {:keys [unique concurrently index-name method]
                                       :or {method "BTREE"}
                                       :as index-config}]
    (let [column-names (map #(name %1) columns)]
      (str "CREATE " unique
           " INDEX " concurrently index-name
           " ON " table-name
           " USING " method
           " ("  (clojure.string/join ", "  column-names)
           ");")))


(defn- remove-index-sql
  [table-name columns index-name]
  (let [columns (map #(name %1) columns)
        index-name (or index-name (str table-name "_" (clojure.string/join "_" columns)))]
    (str "DROP INDEX " index-name ";"))
  [table-name columns]
  (remove-index-sql table-name columns false))

(deftype PostgresGenerator []

SQLGenerator

  (create-table
    [this table-name columns]
    (let [base-sql-str (str "CREATE TABLE IF NOT EXISTS " table-name " (")]
      ; loop over all the columns, accumulating the SQL string as we go
      (add-cols-to-sql columns base-sql-str)))

  (add-column
    [this table-name [col-name col-type]]
    (str "ALTER TABLE " table-name
         " ADD COLUMN " (name  col-name)
         " " (col-type type-map) ";"))

  (drop-table [this table-name] (str "DROP TABLE IF EXISTS " table-name ";"))

  (remove-column [this table-name column-name]
    (str "ALTER TABLE " table-name " DROP COLUMN " (name column-name)))

  (add-index [this table-name columns index-config]
    (add-index-sql table-name columns index-config))
  (add-index [this table-name columns]
    (add-index-sql table-name columns {}))

  (remove-index [this table-name columns index-name]
    (remove-index-sql table-name columns index-name))

  (remove-index [this table-name columns]
    (remove-index-sql table-name columns)))

(defn postgres
  []
  (PostgresGenerator.))
