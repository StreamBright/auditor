;; Copyright 2016 StreamBright LLC and contributors

;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at

;;     http://www.apache.org/licenses/LICENSE-2.0

;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.
(defproject auditor "0.1.0"
  :description "AWS IAM auditing (managed and inline policies)"
  :url "https://github.com/StreamBright/auditor"
  :license {:name "Apache License 2.0"
            :url  "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [
                 [org.clojure/clojure "1.8.0"               ]
                 [cheshire "5.5.0"                          ]
                 [org.clojure/tools.cli "0.3.3"             ]
                 [org.clojure/tools.logging "0.3.1"         ]
                 [org.slf4j/slf4j-log4j12 "1.7.12"          ]
                 [log4j/log4j "1.2.17"                      ]
                 [com.amazonaws/aws-java-sdk-core "1.10.58" ]
                 [com.amazonaws/aws-java-sdk-iam  "1.10.58" ]
                 [com.amazonaws/aws-java-sdk-s3   "1.10.58" ]
                 [org.clojure/test.check "0.9.0"            ]
                 ]
  :exclusions [
               javax.mail/mail
               javax.jms/jms
               com.sun.jdmk/jmxtools
               com.sun.jmx/jmxri
               jline/jline
               ]
  :profiles {
             :uberjar {
                       :aot :all
                       }
             }

  :jvm-opts [
             "-Xms128m" "-Xmx256m"
             "-server" "-XX:+UseConcMarkSweepGC"
             "-XX:+TieredCompilation" "-XX:+AggressiveOpts"
             ;"-Dcom.sun.management.jmxremote"
             ;"-Dcom.sun.management.jmxremote.port=8888"
             ;"-Dcom.sun.management.jmxremote.local.only=false"
             ;"-Dcom.sun.management.jmxremote.authenticate=false"
             ;"-Dcom.sun.management.jmxremote.ssl=false"
             ;"-XX:+UnlockCommercialFeatures" "-XX:+FlightRecorder"
             ;"-XX:StartFlightRecording=duration=60s,filename=myrecording.jfr"
             ;"-Xprof" "-Xrunhprof"
             ]
  :repl-options {:init-ns auditor.audit}
  :main auditor.core)
