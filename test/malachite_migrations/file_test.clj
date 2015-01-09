(ns malachite-migrations.file-test
  (:use clojure.java.io
        clojure.string)
  (:require [expectations :refer :all]
            [malachite-migrations.files :refer :all :as files]
            [clj-time.core :as time]
            [clj-time.coerce :as coerce]))

(defn- current-timestamp
  "Generates a timestamp of the current millisecond"
  []
  (coerce/to-long (time/now)))

(let [fpath (files/make-file "test_blah")]
  ; Should create a file that contains the name
  (expect (.contains fpath "_test_blah.clj"))
  ; Filename should have timestamp that is before the current timestamp
  (expect (> (current-timestamp) (files/grab-timestamp fpath)))
  (delete-file fpath))