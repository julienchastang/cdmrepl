(defproject cdmrepl "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["sonatype" "https://artifacts.unidata.ucar.edu/content/repositories/unidata-releases"]]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [edu.ucar/netcdf "4.3.20"]
                 [edu.ucar/opendap "4.3.20"]
                 [edu.ucar/visadCdm "4.3.20"]]
  :plugins [[cider/cider-nrepl "0.7.0-SNAPSHOT"]])
