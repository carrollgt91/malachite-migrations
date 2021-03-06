(ns malachite-migrations.core
  (:require [clj-time.core :as time]
            [clj-time.coerce :as coerce]
            [malachite-migrations.files :refer :all :as files]
            [malachite-migrations.db :refer :all]
            [malachite-migrations.manager :refer :all]))

(defn write-create-migration!
  "Generates a migration file for creating a table"
  [fname table-name columns]
  (let [migration-code (str "(create-table \"" table-name "\" " columns ")")]
    (println migration-code)
    (files/write-to-file fname migration-code)))

(defn generate-migration!
  "Generates a migration file with a given name. It adds a timestamp to the 
   front of the name, then writes some clojure code which will operate on 
   the table with table-name. For example,
    (generate-migration 'create_users'
                        'users' 
                        :create-table [:id :integer]
                                      [:name :string])
   will generate a migration which creates a user table with two fields on it"
  [name table-name & info]
  {:pre [(keyword? (first info))]}
  (let [fpath (files/make-file name)
        type (first info)]
    (cond 
      (= type :create-table)
      (write-create-migration! fpath table-name (vec (rest info))))
    fpath))
