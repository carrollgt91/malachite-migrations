(ns malachite-migrations.core-test
  (:use clojure.java.io)
  (:require [expectations :refer :all]
            [malachite-migrations.core :refer :all]
            [clj-time.core :as time]
            [clj-time.coerce :as coerce]))

; generating a migration should create a clojure file with some valid clojure
; code within
(let [fpath (generate-migration "create_users"
                    "users" 
                    :create-table [:id :integer]
                                  [:name :string])
      contents (slurp fpath)]
  (expect (.contains contents "(create-table "))
  (expect (.contains contents "[:id :integer]"))
  (expect (.contains contents "[:name :string]"))
  (delete-file fpath)
)