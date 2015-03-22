(ns malachite-migrations.adapters.postgres
  (:use [malachite-migrations.sqlgen]))

(deftype PostgresGenerator []
  SQLGenerator
  (add-column [this table-name [col-name col-type]] table-name)
  (drop-table [this table-name] table-name)
  (remove-column [this table-name column-name] table-name)
  (add-index [this table-name columns index-config] table-name)
  (add-index [this table-name columns] table-name)
  (remove-index [this table-name columns index-name] table-name)
  (remove-index [this table-name columns] table-name))
