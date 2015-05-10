(ns examples.station
  (:import [ucar.nc2.constants FeatureType])
  (:require [cdmrepl.core :as  cr]
            [clojure.java.io :as io] ))

(def feature-ds (cr/open-dataset 
                (str (io/file 
                      (io/resource "new_dataset4.nc")))
                FeatureType/STATION cr/log))

(def fcseq (-> feature-ds cr/feature-collection cr/fc-seq))

(def fc-names (cr/fc-seq-names fcseq))

(def feature (first (cr/get-feature-by-name fcseq "C")))

(def features (cr/ncj-seq feature))

(def members (cr/list-members (first features)))

(def var-names (cr/list-vars feature-ds))

(def times (map #(cr/scalar-float-data % "time") features))

(def humidity (map #(cr/scalar-float-data % "humidity") features))
