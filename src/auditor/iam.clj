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

(ns ^{:doc    "com.sb.auditor :: iam"
      :author "Istvan Szukacs"}
auditor.iam
  (:require
    [auditor.auth :as auth]
    [clojure.tools.logging :as log]
    [clojure.walk :as walk])
  (:import
    [com.amazonaws.auth
     BasicAWSCredentials]
    [com.amazonaws.internal
     SdkInternalList]
    [com.amazonaws.services.identitymanagement
     AmazonIdentityManagementAsyncClient]
    [com.amazonaws.services.identitymanagement.model
      User
      Group
      AttachedPolicy
      ListUsersRequest                 ListUsersResult
      ListGroupsRequest                ListGroupsResult
      ListUserPoliciesRequest          ListUserPoliciesResult
      ListAttachedUserPoliciesRequest  ListAttachedUserPoliciesResult
      ListGroupPoliciesRequest         ListGroupPoliciesResult
      ListAttachedGroupPoliciesRequest ListAttachedGroupPoliciesResult
      ]

    )
  (:gen-class))

; http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/identitymanagement/AmazonIdentityManagementClient.html

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


(defn get-user-details
  [^User user]
  {:arn                   (.getArn user)
   :user-id               (.getUserId user)
   :user-name             (.getUserName user)
   :path                  (.getPath user)
   :create-date           (.getCreateDate user)
   :password-last-updated (.getPasswordLastUsed user)})

(defn get-group-details
  [^Group group]
  {:arn         (.getArn group)
   :group-id    (.getGroupId group)
   :group-name  (.getGroupName group)
   :path        (.getPath group)
   :create-date (.getCreateDate group)})

(defn get-managed-policy-details
  [^AttachedPolicy policy]
  {:arn           (.getPolicyArn  policy)
   :policy-name   (.getPolicyName policy)})

;; List entities
;;
;; first try to page through requests
;;

(defn list-users
  (^SdkInternalList [^AmazonIdentityManagementAsyncClient iam-client]
   (let [^ListUsersResult result @(.listUsersAsync iam-client)]
     (if-not (.isTruncated result)
       ;return
       (.getUsers result)
       ;call the other signature
       (list-users iam-client (.getUsers result) (.getMarker result)))))
  ; paging
  (^SdkInternalList [^AmazonIdentityManagementAsyncClient iam-client acc marker]
   (let [ ^ListUsersRequest request (doto (ListUsersRequest.) (.setMarker marker))
          ^ListUsersResult  result  @(.listUsersAsync iam-client request) ]
     (if-not (.isTruncated result)
       ;return
       (.addAll acc (.getUsers result))
       ;recur
       (recur iam-client (.addAll acc (.getUsers result)) (.getMarker result))))))

(defn list-groups
  (^SdkInternalList [^AmazonIdentityManagementAsyncClient iam-client]
   (let [^ListGroupsResult result @(.listGroupsAsync iam-client) ]
     (if-not (.isTruncated result)
       ;return
       (.getGroups result)
       ;call the other signature
       (list-groups iam-client (.getGroups result) (.getMarker result)))))
  ; paging
  (^SdkInternalList [^AmazonIdentityManagementAsyncClient iam-client acc marker]
   (let [ ^ListGroupsRequest  request (.setMarker (ListGroupsRequest.) marker)
          ^ListGroupsResult   result  @(.listGroupsAsync iam-client request) ]
     (if-not (.isTruncated result)
       ;return
       (.addAll acc (.getGroups result))
       ;recur
       (recur iam-client (.addAll acc (.getGroups result)) (.getMarker result))))))

(defn list-user-inline-policies
  "Lists the names of the inline policies embedded in the specified user."
  (^SdkInternalList [^AmazonIdentityManagementAsyncClient iam-client ^String user-name]
   (let [ ^ListUserPoliciesRequest  request (doto (ListUserPoliciesRequest.) (.setUserName user-name))
          ^ListUserPoliciesResult   result  @(.listUserPoliciesAsync iam-client request) ]
     (if-not (.isTruncated result)
       ;return
       (.getPolicyNames result)
       ;call the other signature
       (list-user-inline-policies iam-client user-name (.getPolicyNames result) (.getMarker result)))))
  ; paging
  (^SdkInternalList [^AmazonIdentityManagementAsyncClient iam-client ^String user-name acc marker]
   (let [ ^ListUserPoliciesRequest  request (doto (ListUserPoliciesRequest.)
                                                    (.setMarker marker)
                                                    (.setUserName user-name))
          ^ListUserPoliciesResult   result  @(.listUserPoliciesAsync iam-client request) ]
     (if-not (.isTruncated result)
       ;return
       (.addAll acc (.getPolicyNames result))
       ;recur
       (recur iam-client user-name (.addAll acc (.getPolicyNames result)) (.getMarker result))))))


(defn list-user-managed-policies
  "Lists the names of the inline policies embedded in the specified user."
  (^SdkInternalList [^AmazonIdentityManagementAsyncClient iam-client ^String user-name]
   (let [ ^ListAttachedUserPoliciesRequest  request (doto (ListAttachedUserPoliciesRequest.) (.setUserName user-name))
          ^ListAttachedUserPoliciesResult   result  @(.listAttachedUserPoliciesAsync iam-client request) ]
     (if-not (.isTruncated result)
       ;return
       (.getAttachedPolicies result)
       ;call the other signature
       (list-user-managed-policies iam-client user-name (.getAttachedPolicies result) (.getMarker result)))))
  ; paging
  (^SdkInternalList [^AmazonIdentityManagementAsyncClient iam-client ^String user-name acc marker]
   (let [ ^ListAttachedUserPoliciesRequest  request (doto (ListAttachedUserPoliciesRequest.)
                                                            (.setMarker marker)
                                                            (.setUserName user-name))
         ^ListAttachedUserPoliciesResult    result  @(.listAttachedUserPoliciesAsync iam-client request) ]
     (if-not (.isTruncated result)
       ;return
       (.addAll acc (.getAttachedPolicies result))
       ;recur
       (recur iam-client user-name (.addAll acc (.getAttachedPolicies result)) (.getMarker result))))))


(defn list-group-inline-policies
  (^SdkInternalList [^AmazonIdentityManagementAsyncClient iam-client ^String group-name]
   (let [^ListGroupPoliciesRequest request (ListGroupPoliciesRequest.)
         _ (.setGroupName request group-name)
         ^ListGroupPoliciesResult result @(.listGroupPoliciesAsync iam-client request)]
     (if-not (.isTruncated result)
       ;return
       (.getPolicyNames result)
       ;call the other signature
       (list-group-inline-policies iam-client group-name (.getPolicyNames result) (.getMarker result)))))
  ; paging
  (^SdkInternalList [^AmazonIdentityManagementAsyncClient iam-client ^String group-name acc marker]
   (let [^ListUserPoliciesRequest request (doto (ListUserPoliciesRequest.)
                                                (.setMarker marker)
                                                (.setUserName group-name))
         ^ListUserPoliciesResult result @(.listUserPoliciesAsync iam-client request) ]
     (if-not (.isTruncated result)
       ;return
       (.addAll acc (.getPolicyNames result))
       ;recur
       (recur iam-client group-name (.addAll acc (.getPolicyNames result)) (.getMarker result))))))

(defn list-group-managed-policies
  (^SdkInternalList [^AmazonIdentityManagementAsyncClient iam-client ^String group-name]
   (let [^ListAttachedGroupPoliciesRequest request  (doto (ListAttachedGroupPoliciesRequest.)
                                                          (.setGroupName group-name))
         ^ListAttachedGroupPoliciesResult  result   @(.listAttachedGroupPoliciesAsync iam-client request) ]
     (if-not (.isTruncated result)
       ;return
       (.getAttachedPolicies result)
       ;call the other signature
       (list-group-managed-policies iam-client group-name (.getAttachedPolicies result) (.getMarker result)))))
  ; paging
  (^SdkInternalList [^AmazonIdentityManagementAsyncClient iam-client ^String group-name acc marker]
   (let [^ListAttachedGroupPoliciesRequest request (doto  (ListAttachedGroupPoliciesRequest.)
                                                          (.setMarker marker)
                                                          (.setGroupName group-name))
         ^ListAttachedGroupPoliciesResult  result   @(.listUserPoliciesAsync iam-client request)  ]
     (if-not (.isTruncated result)
       ;return
       (.addAll acc (.getAttachedPolicies result))
       ;recur
       (recur iam-client group-name (.addAll acc (.getAttachedPolicies result)) (.getMarker result))))))

;; Java -> Clojure

(defn users->clj
  [^SdkInternalList users]
  (vec (map get-user-details users)))

(defn groups->clj
  [^SdkInternalList groups]
  (vec (map get-group-details groups)))

(defn user-policies->clj
  [^ListUserPoliciesResult policies]
  (vec policies))

(defn group-managed-policies->clj
  [^ListAttachedGroupPoliciesResult policies]
  (vec (map get-managed-policy-details policies)))

;; Synthetic niceties

(defn get-user-managed-policies
  [iam-client users]
  (into {} (map #(hash-map
                  (keyword %)
                  (user-policies->clj (list-user-managed-policies iam-client %)))
                (map :user-name users))))

(defn get-group-managed-policies
  [iam-client groups]
  (into {} (map #(hash-map
                (keyword %)
                (group-managed-policies->clj (list-group-managed-policies iam-client %)))
              (map :group-name groups))))