(ns {{name}}.website
  (:require
   [clojure.pprint :refer (pprint)]
   [modular.ring :refer (WebRequestHandler)]
   [modular.bidi :refer (WebService as-request-handler)]
   [modular.template :refer (render-template template-model)]
   [plumbing.core :refer :all :exclude [update]]
   [bidi.bidi :refer (path-for)]
   [bidi.ring :refer (->Redirect)]
   [hiccup.core :refer (html)]
   [hiccup.page :refer (include-css include-js)]
   [clojure.tools.logging :refer :all]))

(defnk head
  [title
   {stylesheets []}]
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:http-equiv "X-UA-Compatible"
           :content "IE=edge, chrome=1"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   [:title title]
   (for [url stylesheets]
     (include-css url))])

(defnk body
  [{content ""}
   {javascripts []}]
  [:body
   content
   (when (seq javascripts)
     (apply include-js javascripts))])

(defrecord Website [aggregate-template-model]
  WebService
  (request-handlers [this]
    {::index (fn [req]
               (let [model (template-model aggregate-template-model nil)]
                 {:status 200
                  :body
                  (html
                   (head model)
                   (body model))}))})

  (routes [_] ["/" {"index.html" ::index
                    "" (->Redirect 307 ::index)}])

  (uri-context [_] "")

  WebRequestHandler
  (request-handler [this] (as-request-handler this)))

(defn new-website []
  (map->Website {}))
