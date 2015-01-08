# malachite-migrations

A Clojure library for handling database migrations, heavily inspired by the fantastic Ruby library ActiveRecord.

## Initial Design Goals 


Since Clojure is driven by it’s REPL, I want the library to be usable entirely in the REPL in the same way ActiveRecord migrations can be (mostly) handled in the terminal. Down the line, I’d like to make it a lein plugin and provide a CLI, but for now, I’m focusing on the REPL workflow.

Migrations should reside in files of Clojure code, again similar to ActiveRecord’s migration files. That way, you can generate a migration using the library, but you have full control over what actually happens in that migration — including running any arbitrary Clojure code to manipulate data to fit the new schema.

A  timestamp protocol for each migration will allow the library to easily keep track of which migrations are currently applied to the database; we’ll prepend each migration with the time it was generated and store that in a migrations table in the database to manage the state.

The only changes to the database that I’m focused on for now are as follows:

add/remove-index
add/remove-column
create/drop-table

## Usage

Sorry, you can't use it just yet.

## License

Copyright © 2015 Grayson Carroll 

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
