/*
 * © Copyright IBM Corp. 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package com.ibm.sbt.security.authentication.oauth.consumer.store;

import com.ibm.sbt.security.authentication.oauth.OAuthException;
import com.ibm.sbt.security.authentication.oauth.consumer.AccessToken;
import com.ibm.sbt.security.authentication.oauth.consumer.ConsumerToken;




/**
 * Gives access to a user token for a particular application.
 * <p>
 * A particular implementation of the store should get some user credential to get access to the token.
 * For example, it can use the current XPages web user session to grab the identity of the user, and
 * then return the token only if authorized.
 * </p>
 * @author Philippe Riand
 */
public interface TokenStore {
    
    //
    // App tokens
    //

    /**
     * Load a token for a particular application.
     * The token contains the consumerKey and the consumerSecret
     * @param consumerKey
     * @param user
     * @return
     */
    public ConsumerToken loadConsumerToken(String appId, String serviceName) throws OAuthException;

    /**
     * Save a token to the store.
     * @param consumerKey
     * @param user
     * @param token
     * @return
     */
    public void saveConsumerToken(String appId, String serviceName, ConsumerToken token) throws OAuthException;

    
    //
    // User tokens
    //
    
    /**
     * Load a token from the store.
     * @param appId Application URI
     * @param serviceName Service associated with the application
     * @param consumerKey Consumer key for the token
     * @param userId User submitting the request
     * @return
     */
    public AccessToken loadAccessToken(String appId, String serviceName, String consumerKey, String userId) throws OAuthException;

    /**
     * This method retrieves an access token, such as was created by an OpenSocial container,  from the database.  
     * The combination of the five parameters is considered to be a unique key
     * @param appId Application URI
     * @param serviceName Service associated with the application
     * @param consumerKey Consumer key for the token
     * @param moduleId Sub identifier
     * @param tokenName Token name
     * @param userId User submitting the request
     * @throws OAuthException
     */
    
    public AccessToken loadAccessToken(String appId, String serviceName, String consumerKey, String moduleId, String tokenName, String userId ) throws OAuthException;    

    /**
     * Save a token to the store.
     * @param consumerKey
     * @param user
     * @param token
     * @return
     */
    public void saveAccessToken(AccessToken token) throws OAuthException;
    
    /**
     * This method removes an access token, such as provided by an OpenSocial container into the database.  The combination of the five
     * parameters is considered to be a unique key
     * @param appId Application URI
     * @param serviceName Service associated with the application
     * @param consumerKey The consumer secret
     * @param userId User submitting the request
     * @throws OAuthException
     */
   	public void deleteAccessToken(String appId, String serviceName, String consumerKey, String userId ) throws OAuthException;
    
    /**
     * This method removes an access token, such as provided by an OpenSocial container into the database.  The combination of the five
     * parameters is considered to be a unique key
     * @param appId Application URI
     * @param serviceName Service associated with the application
     * @param consumerKey The consumer secret
     * @param moduleId Sub identifier
     * @param tokenName Token name
     * @param userId User submitting the request
     * @throws OAuthException
     */
    public void deleteAccessToken(String appId, String serviceName, String consumerKey, String moduleId, String tokenName, String userId ) throws OAuthException;
    
}
