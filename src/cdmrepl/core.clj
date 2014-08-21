(ns cdmrepl.core
  (:import 
   [ucar.nc2.ft FeatureDatasetFactoryManager]
   [java.util Formatter Locale]))

(def log (Formatter. (StringBuilder.) Locale/US))

(defn clear-log
  "Clear the message log."
  []
  (.setLength (.out log) 0))

(defn reset-iterator
  "Reset iterator idiom common in netCDF-Java"
  [x]
  (.resetIteration x))

(defn ncj-seq
  "Return a seq from an netCDF-Java adhoc 'iterator'"
  [it]
  (do (reset-iterator it)
      (loop [i it r '()]
        (if (.hasNext i)
          (recur i (conj r (.next i)))
          r))))

(defn open-dataset
  "Wrapper around FeatureDatasetFactoryManager/open"
  [source ft error-log]
  (try (FeatureDatasetFactoryManager/open
        ft
        source nil error-log)
       (catch Exception e
         (.format error-log "Could not open %s\n"
                  (to-array [source])))))

;; some useful idioms derived empirically

(defn get-feature-collection
  "Get the FeatureCollection from the FeatureDataset. Empirically these seem to
   be of length one so return the first."
  [feature-dataset]
  (first  (.getPointFeatureCollectionList feature-dataset)))

(defn get-fc-seq
  "Get a netcdf-Java 'sequence' from the feature collection"
  [feature-collection]
  (ncj-seq feature-collection))

(defn get-feature-by-name
  "Given a name (identifier), get features from a feature collection sequence"
  [fc-seq name]
  (filter #(= (.getName %) name) fc-seq))

(defn list-members
  "List data members associated with feature"
  [feature] 
  (let [f-data (.getData feature)]
    [(map str (.getMembers f-data))]))

(defn str-invoke [instance method-str & args]
  (clojure.lang.Reflector/invokeInstanceMethod 
   instance 
   method-str 
   (to-array args)))

(defn get-scalar-data
  "Get scalar data for a scalar-type, feature and field"
  [scalar-type feature field] 
  (let [data (.getData feature)
        m (.findMember data field)]
    [(apply str-invoke
            data
            (str "getScalar" scalar-type)
            [m])
     (.getUnitsString m)]))

(defn get-scalar-float-data
  "Get scalar double data for a feature and field"
  [feature field] 
  (get-scalar-data 'Float feature field))

(defn get-scalar-double-data
  "Get scalar double data for a feature and field"
  [feature field] 
  (get-scalar-data 'Double feature field))
