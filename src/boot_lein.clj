(ns boot-lein
  {:boot/export-tasks true}
  (:require [boot.core :as core :refer :all]
            [boot.pod :as pod]
            [boot.util :as util]
            [clojure.edn :as edn]))

(defn- read-project [] (edn/read-string (slurp "project.clj")))

(defn- lein->map
  [data]
  (reduce (fn [a [k v]] (assoc a k v))
          {}
          (partition 2 2 data)))

(defn- lein->data
  ([[_ project version & data :as lein-edn]]
   (assoc (lein->map data)
     :project project
     :version version))

  ([] (lein->data (read-project))))

(deftask merge-deps
  []
  (->> (lein->data)
       (:dependencies)
       (merge-env! :dependencies)))

(def ^:private pom-options
  {:project     identity
   :version     identity
   :description identity
   :url         identity
   :scm         identity
   :license   #(into {} (for [x %] (vec (map name x))))})

(defn merge-pom
  []
  (let [opts (lein->data)]
    (doseq [[opt f] pom-options
            :when (opt opts)]
      (prn 'pom {opt (f (opt opts))}))))

(defn merge-lein
  [])
