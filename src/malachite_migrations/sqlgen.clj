(ns malachite-migrations.sqlgen)

(defprotocol SQLGenerator
  (create-table [this table-name columns])
  (drop-table [this table-name])
  (add-column [this table-name [col-name col-type]])
  (remove-column [this table-name column-name])
  (add-index [this table-name columns index-config] [this table-name columns])
  (remove-index [this table-name columns index-name] [this table-name columns]))
