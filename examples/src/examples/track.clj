(ns examples.track
  (:import [ucar.nc2.constants FeatureType])
  (:require [cdmrepl.core :as  cr]
            [clojure.java.io :as io] ))

(def track (cr/open-dataset 
                (str (io/file 
                      (io/resource "track.nc")))
                FeatureType/TRAJECTORY cr/log))
