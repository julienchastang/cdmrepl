(ns examples.core
  (:import [ucar.nc2.constants FeatureType])
  (:require [cdmrepl.core :as  cr]
            [clojure.java.io :as io] ))

(def upper-air (cr/open-dataset 
                (str (io/file 
                      (io/resource "20140110_upa.gem")))
                FeatureType/STATION_PROFILE cr/log))

(def pfc (first  (.getPointFeatureCollectionList upper-air)))

(defn get-profile[profs p]
  (first (filter #(= (.getName %) p) profs)))

(filter #(> (count (cr/ncj-seq %)) 0) (cr/ncj-seq pfc))

(map #(.getName %) (filter #(> (count (cr/ncj-seq %)) 0) 
                           (cr/ncj-seq pfc)))

(def profile (get-profile (cr/ncj-seq pfc) "2185"))
;;(def profile (get-profile (ncj-seq pfc) "72214"))

(def all-levels (cr/ncj-seq (first (cr/ncj-seq profile))))

(defn get-level-int-data[lvl field] 
  (let [lvl-data (.getData lvl)
        m (.findMember lvl-data field)]
    [(.getScalarFloat lvl-data
                      m) (.getUnitsString m)]))

(defn get-level-seq-data[lvl uafield field] 
  (let [lvl-data (.getData lvl)
        data (.getArraySequence lvl-data
                                (.findMember lvl-data uafield))
        m (.findMember data field)
        d (.extractMemberArray data m)]
    [(for [i (range (.getSize d))]
       (.getFloat d i)) (.getUnitsString m)]))

(def pressures (map #(get-level-int-data % "PRES") all-levels))
(def temperatures (map #(get-level-int-data % "TEMP") all-levels))
(def dewpoints (map #(get-level-int-data % "DWPT") all-levels))
(def speeds (map #(get-level-int-data % "SPED") all-levels))
(def dirs (map #(get-level-int-data % "DRCT") all-levels))
(def heights (map #(get-level-int-data % "HGHT") all-levels))

(def trpa-pressure (get-level-seq-data (first all-levels) 
                                       "TRPA" "PRES"))
(def trpa-temperatures (get-level-seq-data (first all-levels) 
                                           "TRPA" "TEMP"))
(def trpa-dewpoints (get-level-seq-data (first all-levels) 
                                        "TRPA" "DWPT"))
(def trpa-speeds (get-level-seq-data (first all-levels) 
                                     "TRPA" "SPED"))
(def trpa-dirs (get-level-seq-data (first all-levels) 
                                   "TRPA" "DRCT"))

(def ttbb-pressure (get-level-seq-data (first all-levels) 
                                       "TTBB" "PRES"))
(def ttbb-temperatures (get-level-seq-data (first all-levels) 
                                           "TTBB" "TEMP"))
(def ttbb-dewpoints (get-level-seq-data (first all-levels) 
                                        "TTBB" "DWPT"))

(def ppbb-heights (get-level-seq-data (first all-levels) 
                                     "PPBB" "HGHT"))
(def ppbb-speeds (get-level-seq-data (first all-levels) 
                                     "PPBB" "SPED"))
(def ppbb-dirs (get-level-seq-data (first all-levels) 
                                   "PPBB" "DRCT"))

(def ttcc-pressure (get-level-seq-data (first all-levels) 
                                       "TTCC" "PRES"))
(def ttcc-temperatures (get-level-seq-data (first all-levels) 
                                           "TTCC" "TEMP"))
(def ttcc-dewpoints (get-level-seq-data (first all-levels) 
                                        "TTCC" "DWPT"))
(def ttcc-speeds (get-level-seq-data (first all-levels) 
                                     "TTCC" "SPED"))
(def ttcc-dirs (get-level-seq-data (first all-levels) 
                                   "TTCC" "DRCT"))

(def mxwc-pressure (get-level-seq-data (first all-levels) 
                                       "MXWC" "PRES"))
(def mxwc-speeds (get-level-seq-data (first all-levels) 
                                     "MXWC" "SPED"))
(def mxwc-dirs (get-level-seq-data (first all-levels) 
                                   "MXWC" "DRCT"))
