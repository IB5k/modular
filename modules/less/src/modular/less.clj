;; Copyright © 2014 JUXT LTD.

(ns modular.less
  (:require
   [com.stuartsierra.component :refer (Lifecycle)]
   [schema.core :as s]
   [lein-less.less :refer (run-compiler)]))

(defrecord LessCompiler [engine less-config]
  Lifecycle
  (start [this] (run-compiler engine less-config))
  (stop [this] this))

(defn new-less-compiler [& {:as opts}]
  (->> opts
       (merge {:engine :javascript})
       (s/validate {:engine (s/enum :javascript :rhino :nashorn)
                    :less-config {:project-root s/Str
                                  :source-path s/Str
                                  :target-path s/Str}})
       map->LessCompiler))
