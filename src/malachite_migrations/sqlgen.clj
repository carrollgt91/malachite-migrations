(ns malachite-migrations.sqlgen)

(defprotocol SQLGenerator
  (create-table 
    [this table-name columns]
    "Generates SQL to create a table.")
  (drop-table 
    [this table-name]
    "Generates SQL to drop a table.")
  (add-column 
    [this table-name [col-name col-type]]
    "Generates SQL to add a column to a table.")
  (remove-column 
    [this table-name column-name]
    "Genertes SQL to remove a column from a table.")
  (add-index
    [this table-name columns index-config] [this table-name columns]
    "Generates SQL to add an index to one or more columns on a table.")
  (remove-index  [this table-name columns index-name] [this table-name columns]
    "Generates SQL to remove an index from one or more columns on a table."))
