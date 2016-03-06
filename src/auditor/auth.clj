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

(ns ^{  :doc "com.sb.auditor :: auth"
      :author "Istvan Szukacs"  }
auditor.auth
  (:require
    [clojure.tools.logging  :as   log   ]
    [auditor.sts            :as   sts   ]
    )
  (:import
    [com.amazonaws.auth
      BasicAWSCredentials
      BasicSessionCredentials ]
    [com.amazonaws.auth.profile
     ProfileCredentialsProvider]
    )
  (:gen-class))

; AWSCredentialsProvider
;; AWSCredentialsProviderChain, ClasspathPropertiesFileCredentialsProvider,
;; DefaultAWSCredentialsProviderChain, EnvironmentVariableCredentialsProvider,
;; InstanceProfileCredentialsProvider, ProfileCredentialsProvider,
;; PropertiesFileCredentialsProvider, STSAssumeRoleSessionCredentialsProvider,
;; STSSessionCredentialsProvider, SystemPropertiesCredentialsProvider,
;; WebIdentityFederationSessionCredentialsProvider

(defn create-basic-aws-credentials-file
  "Creates BasicAWSCredentials using credential file and profile name"
  (^BasicAWSCredentials [^String credentials-file]
   (create-basic-aws-credentials-file credentials-file "default"))
  (^BasicAWSCredentials [^String credentials-file ^String profile-name]
   (.getCredentials (ProfileCredentialsProvider. credentials-file profile-name))))

; AWSCredentials
;; AnonymousAWSCredentials, BasicAWSCredentials, 
;; BasicSessionCredentials, PropertiesCredentials, 
;; STSSessionCredentials

(defn create-basic-aws-credentials-keys
  "Creates BasicAWSCredentials using 
    access key and secret key"
  ^BasicAWSCredentials [  ^String aws_access_key_id 
                          ^String aws_secret_access_key     ]
    (BasicAWSCredentials. aws_access_key_id 
                          aws_secret_access_key))

(defn create-basic-session-credentials
  "Creates BasicSessionCredentials using 
    access key and secret key and security token provided by STS" 
  ^BasicSessionCredentials [  ^String aws_access_key_id 
                              ^String aws_secret_access_key 
                              ^String aws_security_token    ]
    (BasicSessionCredentials. aws_access_key_id 
                              aws_secret_access_key 
                              aws_security_token))