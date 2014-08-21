(ns examples.traj
  (:import [ucar.nc2.constants FeatureType])
  (:require [cdmrepl.core :as  cr]
            [clojure.java.io :as io] ))

(def trajectory (cr/open-dataset
                "https://motherlode.ucar.edu/repository/opendap/fa78457d-bb85-473f-ae54-bccdf49f27bd/entry.das"
                FeatureType/TRAJECTORY cr/log))

(defn get-traj[trajs t]
  (first (filter #(= (.getName %) t) trajs)))

(defn get-traj-names[trajs]
  (map #(.getName %) trajs))

(def traj-names (get-traj-names fc))

(def traj (get-traj fc "11"))

(def all-points (cr/ncj-seq traj))

(def members (cr/list-members (first all-points)))

(def times (map #(cr/get-scalar-float-data % "time") all-points))

(def maxrv (map #(cr/get-scalar-float-data % "maxrv") all-points))
