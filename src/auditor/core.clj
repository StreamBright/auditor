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

(ns auditor.core)

(ns ^{  :doc "com.sb.auditor :: Core"
        :author "Istvan Szukacs"      }
  auditor.core
  (:require
    [auditor.cli              :as     cli                   ]
    [auditor.audit            :as     audit                 ]
    [clojure.tools.logging    :as     log                   ]
    [clojure.edn              :as     edn                   ]
    [clojure.string           :as     string                ]
    )
  (:import
    [clojure.lang         PersistentArrayMap PersistentList ]
    )
  (:gen-class))

(defn -main
  [& args]
  (let [
        ; CLI & Config
        cli-options-parsed                          (cli/process-cli args cli/cli-options)
        {:keys [options arguments errors summary]}  cli-options-parsed
        config                                      (cli/process-config (:config options))

        env         (keyword (:env options))

        ]

    ; main entry point for execution
    (log/info (str ":ok" " env " env))
    (audit/run) ;running the audit
    (log/info "init :: stop")))