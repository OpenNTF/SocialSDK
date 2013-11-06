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

import java.util.Date;

import com.ibm.sbt.security.authentication.oauth.OAuthException;
import com.ibm.sbt.security.authentication.oauth.consumer.AccessToken;
import com.ibm.sbt.security.authentication.oauth.consumer.ConsumerToken;


/**
 * Static OAuth store.
 * 
 * @author Philippe Riand
 */
public class StaticTokenStore10 extends MemoryTokenStore {

	// Consumer token
	private String consumerKey;
	private String consumerSecret;
	private String requestTokenURL;
	private String authorizationURL;
	private String accessTokenURL;
	private String signatureMethod;
	private String verifierId;
	
	// Access token
	private String accessKey;
	private String accessSecret;
//	private Date expiresIn;
//	private Date authorizationExpiresIn;
//	private String sessionHandle;
	
    public StaticTokenStore10() {
    }

    public String getConsumerKey() throws OAuthException {
        return consumerKey;
    }
    public void setConsumerKey(String consumerKey) throws OAuthException {
    	this.consumerKey = consumerKey;
    }
    
    public String getConsumerSecret() throws OAuthException {
        return consumerSecret;
    }
    public void setConsumerSecret(String consumerSecret) throws OAuthException {
        this.consumerSecret = consumerSecret;
    }

    public String getRequestTokenURL() {
        return requestTokenURL;
    }
    public void setRequestTokenURL(String requestTokenURL) {
    	this.requestTokenURL = requestTokenURL;
    }

    public String getAuthorizationURL() {
        return authorizationURL;
    }
    public void setAuthorizationURL(String authorizationURL) {
        this.authorizationURL = authorizationURL;
    }

    public String getAccessTokenURL() {
        return accessTokenURL;
    }
    public void setAccessTokenURL(String accessTokenURL) {
        this.accessTokenURL = accessTokenURL;
    }

    public String getSignatureMethod() {
    	return signatureMethod;
    }
    public void setSignatureMethod(String signatureMethod) {
        this.signatureMethod = signatureMethod;
    }

    public String getVerifierId() {
    	return verifierId;
    }
    public void setVerifierId(String verifierId) {
        this.verifierId = verifierId;
    }

    public String getAccessKey() throws OAuthException {
        return accessKey;
    }
    public void setAccessKey(String accessKey) throws OAuthException {
    	this.accessKey = accessKey;
    }

    public String getAccessSecret() throws OAuthException {
        return accessSecret;
    }
    public void setAccessSecret(String accessSecret) throws OAuthException {
    	this.accessSecret = accessSecret;
    }
    
    
    //
    // Create the default tokens
    //
    
    @Override
	protected ConsumerToken createDefaultConsumerToken(String application, String provider) throws OAuthException {
    	return new ConsumerToken(getRequestTokenURL(), getAuthorizationURL(), getAccessTokenURL(), getVerifierId(), getConsumerKey(), getSignatureMethod(), getConsumerSecret());
    }
    @Override
    protected AccessToken createDefaultAccessToken(String appId, String serviceName, String consumerKey, String moduleId, String tokenName, String userId) throws OAuthException {
    	Date expiresIn = new Date(System.currentTimeMillis()+10*24*60*60*1000); // 10 days...
    	Date authorizationExpiresIn = new Date(System.currentTimeMillis()+10*24*60*60*1000); // 10 days...
    	String sessionHandle = ""; // None...
    	return new AccessToken(appId, serviceName, consumerKey, getAccessKey(), getAccessSecret(), userId, expiresIn, authorizationExpiresIn, sessionHandle);
    }
}
