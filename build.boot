(set-env!
 :source-paths #{"src"}
 :dependencies '[[adzerk/bootlaces "0.1.11" :scope "test"]])

(require '[adzerk.bootlaces :refer :all])

(def +version+ "0.0.1-SNAPSHOT")
(bootlaces! +version+)

(task-options!
 pom {:project 'boot-lein
      :version +version+
      :description "Boot task for using project.clj for dependencies"
      :url "https://github.com/carelogistics/boot-lein"
      :license {"EPL" "http://www.eclipse.org/legal/epl-v10.html"}})
