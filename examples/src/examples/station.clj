(ns examples.station
  (:import [ucar.nc2.constants FeatureType])
  (:require [cdmrepl.core :as  cr]
            [clojure.java.io :as io] ))

(def station (cr/open-dataset 
                (str (io/file 
                      (io/resource "new_dataset2.nc")))
                FeatureType/STATION cr/log))

(def fc (-> station cr/get-feature-collection cr/get-fc-seq))

(map (memfn getName) fc)

(def f (first fc))

(def data (cr/get-fc-seq f))

(def data1 (first data))

(def b (.getData data1))

(defn list-members [lvl] 
  (let [lvl-data (.getData lvl)]
    [(map str (.getMembers lvl-data))]))



