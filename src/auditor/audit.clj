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

(ns ^{  :doc "com.sb.auditor :: audit"
      :author "Istvan Szukacs"  }
auditor.audit
  (:require
    [clojure.tools.logging  :as   log   ]
    [auditor.iam            :as   iam   ]
    [auditor.sts            :as   sts   ]
    [auditor.auth           :as   auth  ]
    )
  (:import
    [clojure.lang
      PersistentArrayMap
      PersistentList      ]
    [com.amazonaws
      AmazonServiceException  
      ClientConfiguration ]
    [com.amazonaws.auth   
      AWSCredentials
      BasicAWSCredentials ]
    [com.amazonaws.auth.profile
      ProfileCredentialsProvider]
    [com.amazonaws.services.identitymanagement    
      AmazonIdentityManagementAsyncClient]
    )
  (:gen-class))

;; this part can work two ways: either using a 
;; credential (access key + secret key) or 
;; using sts and getting a session that way

(def audits [:get-account-summary iam/get-account-summary])

(defn run-with-creds
  "Runs audit with the supplied credentials"
  [creds-file profile]
  (let [^BasicAWSCredentials                  creds       (auth/create-basic-aws-credentials-file creds-file profile)
        ^AmazonIdentityManagementAsyncClient  iam-client  (iam/create-iam-async-client creds)
        ;; audit first hm
        account-summary (iam/get-account-summary iam-client)
        get-all-users (map iam/get-user-details (iam/get-all-users iam-client))
        ]
    {:ok { :account-summary account-summary :szop get-all-users} }))

(defn run-with-sts [] :ok)
