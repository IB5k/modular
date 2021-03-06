;; Copyright © 2014 JUXT LTD.

(ns modular.datomic
  (:require
   [clojure.java.io :as io]
   [com.stuartsierra.component :as component]
   [clojure.tools.reader.reader-types :refer (indexing-push-back-reader)]
   [datomic.api :as d]
   [schema.core :as s]
   clojure.tools.reader))

(defn conn [connection-comp]
      (:connection connection-comp))

(defrecord EphemeralDatabase [uri]
  component/Lifecycle
  (start [this]
    (d/create-database uri)
    this)

  (stop [this]
    (d/delete-database uri)
    (d/shutdown false)
    this))

(defrecord DurableDatabase [uri]
  component/Lifecycle
  (start [this] this)
  (stop [this]
    (d/shutdown false)
    this))

(def new-datomic-database-schema
  {:uri s/Str
   :ephemeral? s/Bool})

(defn new-datomic-database [& {:as opts}]
  (let [{:keys [uri ephemeral?]} (->> opts
                           (merge {:ephemeral? false})
                           (s/validate new-datomic-database-schema))]
    (if ephemeral?
      (->EphemeralDatabase uri)
      (->DurableDatabase uri))))

(defrecord DatomicConnection []
  component/Lifecycle
  (start [this] (assoc this :connection (d/connect (get-in this [:database :uri]))))
  (stop [this] (d/release (:connection this)) (dissoc this :connection)))

(defn new-datomic-connection []
  (component/using
   (->DatomicConnection)
   [:database]))

(defrecord DatomicSchema [res]
  component/Lifecycle
  (start [this]
    (with-open [rdr (java.io.PushbackReader. (io/reader res))]
      @(d/transact (conn (this :connection))
                   (binding [clojure.tools.reader/*data-readers*
                             {'db/id datomic.db/id-literal
                              'db/fn datomic.function/construct
                              'base64 datomic.codec/base-64-literal}]
                     (clojure.tools.reader/read (indexing-push-back-reader rdr)))))
    this)
  (stop [this] this))

(defn new-datomic-schema [res]
  (component/using
   (->DatomicSchema res)
   [:connection]))


(defn create-functions [functions]
  (vec
   (for [[ident {:keys [doc params path]}] functions]
     {:db/id (d/tempid :db.part/user)
      :db/ident ident
      :db/doc doc
      :db/fn (d/function {:lang "clojure" :params params :code (slurp (io/resource path))})})))

(defrecord DatomicFunctions [functions]
  component/Lifecycle
  (start [this]
    @(d/transact (conn (this :connection)) (create-functions functions))
    this)
  (stop [this] this))

(defn new-datomic-functions [functions]
  (component/using
   (->DatomicFunctions functions)
   [:connection]))
