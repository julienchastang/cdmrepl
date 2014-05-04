(ns cdmrepl.core
  (:import 
   [ucar.nc2.ft FeatureDatasetFactoryManager]
   [java.util Formatter Locale]))

(def log (Formatter. (StringBuilder.) Locale/US))

(defn clear-log [] (.setLength (.out log) 0))

(defn reset-iterator[x] (.resetIteration x))

(defn ncj-seq[it]
  (do (reset-iterator it)
      (loop [i it r '()]
        (if (.hasNext i)
          (recur i (conj r (.next i)))
          r))))

(defn open-dataset [source ft formatter]
  (try (FeatureDatasetFactoryManager/open
        ft
        source nil formatter)
       (catch Exception e
	 (.format formatter "Could not open %s\n"
                  (to-array [source])))))
