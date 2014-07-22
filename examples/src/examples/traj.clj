(ns examples.traj
  (:import [ucar.nc2.constants FeatureType])
  (:require [cdmrepl.core :as  cr]
            [clojure.java.io :as io] ))

(def trajectory (cr/open-dataset 
                (str (io/file 
                      (io/resource "traj.nc")))
                FeatureType/TRAJECTORY cr/log))

(def pfc (first  (.getPointFeatureCollectionList trajectory)))

(defn get-traj[trajs t]
  (first (filter #(= (.getName %) t) trajs)))


(def traj (get-traj (cr/ncj-seq pfc) "17"))

(def all-points (cr/ncj-seq traj))

(defn get-point-int-data[lvl field] 
  (let [lvl-data (.getData lvl)
        m (.findMember lvl-data field)]
    [(.getScalarFloat lvl-data
                      m) (.getUnitsString m)]))

(def pressures (map #(get-point-int-data % "mslp") all-points))
(def z (map #(get-point-int-data % "z") all-points))
