(ns boot-lein
  {:boot/export-tasks true}
  (:require [boot.core :as core :refer :all]
            [boot.pod :as pod]
            [boot.util :as util]
            [clojure.edn :as edn]))

(defn- read-project [] (edn/read-string (slurp "project.clj")))

(def ^:private lein-defaults
  {:resource-paths ["src/main/resource"]
   :target-path    "target"
   :source-paths   ["src" "src/main/clojure"]})

(defn- lein->map
  [data]
  (reduce (fn [a [k v]] (assoc a k v))
          lein-defaults
          (partition 2 2 data)))

(defn- lein->data
  ([[_ project version & data :as lein-edn]]
   (assoc (lein->map data)
     :project project
     :version version))

  ([] (lein->data (read-project))))

(def ^:private project-mappings
  {:dependencies   {}
   :resource-paths {:mapping set}
   :target-path    {}
   :source-paths   {:mapping set}
   :mirrors        {}
   :repositories   {}
   :local-repo     {}
   :offline?       {}})

(defn- env     [lp] (get-in project-mappings [lp :env] lp))
(defn- mapping [lp] (get-in project-mappings [lp :mapping] identity))

(defn- project->env
  ([lp lv]
   (when (project-mappings lp)
     [(env lp) ((mapping lp) lv)]))

  ([[lp lv]] (project->env lp lv)))

(defn- properties->env
  []
  (->> (lein->data)
       (keep project->env)
       (reduce (partial apply assoc) {})))

(deftask merge-properties
  []
  (doseq [[k v] (properties->env)]
    (merge-env! k v)))

(def ^:private lein-pom-mappings
  {:project     identity
   :version     identity
   :description identity
   :url         identity
   :scm         identity
   :license   #(into {} (for [x %] (vec (map name x))))})

(defn merge-pom [])

(defn merge-lein [])
