(ns examples.trajectory
  (:import [ucar.nc2.constants FeatureType])
  (:require [cdmrepl.core :as  cr]
            [clojure.java.io :as io] ))

;; FeatureType/TRAJECTORY example

(def feature-ds (cr/open-dataset
                 (str (io/file 
                       (io/resource "cfs_ew_850hpa_1993_AFR.nc")))
                 FeatureType/TRAJECTORY cr/log))

;; Could also open with
;;
;; (def trajectory (cr/open-dataset
;;                 "https://motherlode.ucar.edu/repository/opendap/fa78457d-bb85-473f-ae54-bccdf49f27bd/entry.das"
;;                 FeatureType/TRAJECTORY cr/log))

(def fcseq (-> feature-ds cr/feature-collection cr/fc-seq))

(def fc-names (cr/fc-seq-names fcseq))

(def feature (first (cr/get-feature-by-name fcseq "11")))

(def features (cr/ncj-seq feature))

(def members (cr/list-members (first features)))

(def var-names (cr/list-vars feature-ds))

(def times (map #(cr/scalar-float-data % "time") features))

(def maxrv (map #(cr/scalar-double-data % "maxrv") features))
