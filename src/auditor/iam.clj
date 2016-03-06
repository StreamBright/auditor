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

(ns ^{  :doc "com.sb.auditor :: iam"
      :author "Istvan Szukacs"  }
auditor.iam
  (:require
    [clojure.tools.logging  :as   log   ]
    [clojure.walk           :as   walk  ])
  (:import
    [com.amazonaws.auth
     BasicAWSCredentials ]
    [com.amazonaws.services.identitymanagement
     AmazonIdentityManagementAsyncClient ]
    [com.amazonaws.services.identitymanagement.model
     User]

    )
  (:gen-class))

;AmazonIdentityManagement
;;AbstractAmazonIdentityManagement, AbstractAmazonIdentityManagementAsync,
;;AmazonIdentityManagementAsyncClient, AmazonIdentityManagementClient
(defn create-iam-async-client
  ^AmazonIdentityManagementAsyncClient [^BasicAWSCredentials creds]
  (AmazonIdentityManagementAsyncClient. creds))

(defn get-account-summary
  [^AmazonIdentityManagementAsyncClient iam-client]
  (walk/keywordize-keys
    (into {} (.getSummaryMap @(.getAccountSummaryAsync iam-client)))))


(defn get-user-details [^User user]
  { :arn                          (.getArn              user)
    :create-date                  (.getCreateDate       user)
    :get-password-last-updated    (.getPasswordLastUsed user)
    :get-path                     (.getPath             user)
    :get-user-id                  (.getUserId           user)
   }
  )

(defn get-all-users
  [iam-client]
  (.getUsers @(.listUsersAsync iam-client)))

