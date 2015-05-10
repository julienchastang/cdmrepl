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

(defn feature-collection
  "Get the FeatureCollection from the FeatureDataset. Empirically these seem to
   be of length one so return the first."
  [feature-dataset]
  (first  (.getPointFeatureCollectionList feature-dataset)))

(defn list-vars [feature-dataset]
  "List vars associated with feature"
  (map (memfn getName) (.getDataVariables feature-dataset)))

(defn fc-seq
  "Get a netcdf-Java 'sequence' from the feature collection"
  [feature-coll]
  (ncj-seq feature-coll))

(defn fc-seq-names[fcseq]
  "Call getName on each item in the feature collection sequence"
  (map (memfn getName) fcseq))

(defn get-feature-by-name
  "Given a name (identifier), get features from a feature collection sequence"
  [fc-sq name]
  (filter #(= (.getName %) name) fc-sq))

(defn list-members
  "List data member names associated with feature"
  [feature] 
  (let [f-data (.getFeatureData feature)]
    [(map str (.getMembers f-data))]))

(defn scalar-data
  "Get scalar data for a scalar-type, feature and field"
  [scalar-type feature field] 
  (let [data (.getFeatureData feature)
        m (.findMember data field)]
    [(apply str-invoke
            data
            (str "getScalar" scalar-type)
            [m])
     (.getUnitsString m)]))

(defn scalar-float-data
  "Get scalar double data for a feature and field"
  [feature field] 
  (scalar-data 'Float feature field))

(defn scalar-double-data
  "Get scalar double data for a feature and field"
  [feature field] 
  (scalar-data 'Double feature field))

;; General Utilities

(defn str-invoke [instance method-str & args]
  "Invoke method-str via reflection"
  (clojure.lang.Reflector/invokeInstanceMethod 
   instance 
   method-str 
   (to-array args)))
