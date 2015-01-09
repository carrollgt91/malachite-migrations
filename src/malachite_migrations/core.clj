(ns malachite-migrations.core
  (:require [clj-time.core :as time]
            [clj-time.coerce :as coerce]
            [malachite-migrations.files :refer :all :as files]))

(defn generate-migration
  "Generates a migration file "
  [name]
  (let [fpath (files/make-file name)]
    fpath))