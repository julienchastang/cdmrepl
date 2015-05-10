(ns examples.station-profile
  (:import [ucar.nc2.constants FeatureType])
  (:require [cdmrepl.core :as  cr]
            [clojure.java.io :as io]))

;; FeatureType/STATION_PROFILE example

(def feature-ds (cr/open-dataset 
                (str (io/file 
                      (io/resource "19990503_upa.gem")))
                FeatureType/STATION_PROFILE cr/log))

(def fcseq (-> feature-ds cr/feature-collection cr/fc-seq))

(def feature (first (cr/get-feature-by-name fcseq "SLC")))

(def features (cr/ncj-seq feature))

(def members (cr/list-members (first features)))

(def var-names (cr/list-vars feature-ds))

;; Utility method
(defn level-seq-data[lvl uafield field] 
  (let [lvl-data (.getData lvl)]
    (when-let [fmb (.findMember lvl-data uafield)]
      (let [data (.getArraySequence lvl-data fmb)
            m (.findMember data field)
            d (.extractMemberArray data m)]
        [(for [i (range (.getSize d))]
           (.getFloat d i)) (.getUnitsString m)]))))

(def all-levels (cr/ncj-seq (first (cr/ncj-seq feature))))

(def pressures (map #(cr/scalar-float-data % "PRES") all-levels))
(def temperatures (map #(cr/scalar-float-data % "TEMP") all-levels))
(def dewpoints (map #(cr/scalar-float-data % "DWPT") all-levels))
(def speeds (map #(cr/scalar-float-data % "SPED") all-levels))
(def dirs (map #(cr/scalar-float-data % "DRCT") all-levels))
(def heights (map #(cr/scalar-float-data % "HGHT") all-levels))

(def trpa-pressure (level-seq-data (first all-levels) 
                                       "TRPA" "PRES"))
(def trpa-temperatures (level-seq-data (first all-levels) 
                                           "TRPA" "TEMP"))
(def trpa-dewpoints (level-seq-data (first all-levels) 
                                        "TRPA" "DWPT"))
(def trpa-speeds (level-seq-data (first all-levels) 
                                     "TRPA" "SPED"))
(def trpa-dirs (level-seq-data (first all-levels) 
                                   "TRPA" "DRCT"))
(def ttbb-pressure (level-seq-data (first all-levels) 
                                       "TTBB" "PRES"))
(def ttbb-temperatures (level-seq-data (first all-levels) 
                                           "TTBB" "TEMP"))
(def ttbb-dewpoints (level-seq-data (first all-levels) 
                                        "TTBB" "DWPT"))
(def ppbb-heights (level-seq-data (first all-levels) 
                                     "PPBB" "HGHT"))
(def ppbb-speeds (level-seq-data (first all-levels) 
                                     "PPBB" "SPED"))
(def ppbb-dirs (level-seq-data (first all-levels) 
                                   "PPBB" "DRCT"))
(def ttcc-pressure (level-seq-data (first all-levels) 
                                       "TTCC" "PRES"))
(def ttcc-temperatures (level-seq-data (first all-levels) 
                                           "TTCC" "TEMP"))
(def ttcc-dewpoints (level-seq-data (first all-levels) 
                                        "TTCC" "DWPT"))
(def ttcc-speeds (level-seq-data (first all-levels) 
                                     "TTCC" "SPED"))
(def ttcc-dirs (level-seq-data (first all-levels) 
                                   "TTCC" "DRCT"))
(def mxwc-pressure (level-seq-data (first all-levels) 
                                       "MXWC" "PRES"))
(def mxwc-speeds (level-seq-data (first all-levels) 
                                     "MXWC" "SPED"))
(def mxwc-dirs (level-seq-data (first all-levels) 
                                   "MXWC" "DRCT"))
