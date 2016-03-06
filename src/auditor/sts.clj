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

(ns ^{  :doc "com.sb.auditor :: sts"
      :author "Istvan Szukacs"  }
auditor.sts
  (:require
    [clojure.tools.logging  :as   log   ]
    )
  (:gen-class))

(defn assume-role
  [^String role]
  ; returns the assumed-role object that
  ; has the access key and secret key
  ; addigned for this session as well
  ; as the security token
  ; STS -> AWS Security Token Service
  :role)

; assumed_role = sts.assume_role(args.role, "SecAudit")
; access_key_id = assumed_role.credentials.access_key
; secret_access_key = assumed_role.credentials.secret_key
; security_token = assumed_role.credentials.session_token

(defn run [] :ok)
