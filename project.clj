(defproject malachite-migrations "0.1.0-SNAPSHOT"
  :description "A Clojure database migration library inspired by Ruby's ActiveRecord"
  :url "https://github.com/carrollgt91/malachite-migrations"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [expectations "2.0.9"]
                 [clj-time "0.9.0"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [org.postgresql/postgresql "9.3-1102-jdbc4"]])
