;; Copyright © 2014 JUXT LTD.

(defproject ib5k.modular/maker "0.5.1"
  :description "A utility library that makes it easier to blend config with kv-based component constructors."
  :url "https://github.com/juxt/modular/tree/master/modules/maker"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies []
  :profiles {:dev {:plugins [[com.keminglabs/cljx "0.5.0"]]}}
  :prep-tasks [["cljx" "once"]]
  :cljx {:builds [{:source-paths ["src"]
                   :output-path "target/classes"
                   :rules :clj}
                  {:source-paths ["src"]
                   :output-path "target/classes"
                   :rules :cljs}]})
