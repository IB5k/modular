(set-env!
 :dependencies (vec
                (concat
                 '[{{#library-dependencies}}
                   {{{.}}}
                   {{/library-dependencies}}]
                 (mapv #(conj % :scope "test")
                       '[[adzerk/bootlaces "0.1.6-SNAPSHOT"]
                         [adzerk/boot-cljs "0.0-2411-4"]
                         [adzerk/boot-cljs-repl "0.1.8"]
                         [adzerk/boot-reload "0.2.0"]
                         [boot-garden "1.2.5-1"]
                         [deraen/boot-cljx "0.2.0"]
                         [jeluard/boot-notify "0.1.1"]
                         [pleasetrythisathome/boot-component "0.1.0-SNAPSHOT"]])))
 :source-paths #{"src"}
 :resource-paths #(conj % "resources"))

(require
 '[adzerk.bootlaces      :refer :all]
 '[adzerk.boot-cljs      :refer :all]
 '[adzerk.boot-cljs-repl :refer :all]
 '[adzerk.boot-reload    :refer :all]
 '[boot-garden.core      :refer :all]
 '[boot-component.repl   :refer :all]
 '[deraen.boot-cljx      :refer :all]
 '[jeluard.boot-notify   :refer :all])

(def +version+ "0.1.0-SNAPSHOT")
(bootlaces! +version+)

(task-options!
 pom {:project '{{name}}
      :version +version+
      :description "A modular project created with lein new modular {{template}}"
      :license {"The MIT License" "http://opensource.org/licenses/MIT"}
      :url "http://github.com/{{user}}/{{name}}"
      :scm {:url "http://github.com/{{user}}/{{name}}"}}
 aot {:namespace #{'{{name}}.main}}
 jar {:main '{{name}}.main
      :file "{{name}}.jar"}
 cljs {:output-to "public/js/main.js"
       :source-map true}
 garden {:styles-var '{{name}}.style/style
         :output-to "public/css/style.css"
         :vendors ["webkit" "moz" "o"]})

(deftask dev
  "watch and compile cljx, css, cljs, init cljs-repl and push changes to browser"
  []
  (comp
   (watch)
   (notify)
   (cljx)
   (reload :port 3449)
   (reload-system :system-var '{{name}}.system/new-production-system)
   (cljs-repl)
   (garden :pretty-print true)
   (cljs :optimizations :none
         :source-map true
         :pretty-print true)))

(deftask package
  "compile cljx, garden, cljs, and build a jar"
  []
  (comp
   (garden)
   (cljx)
   (cljs :optimizations :advanced)
   (aot)
   (pom)
   (uber)
   (jar)))
