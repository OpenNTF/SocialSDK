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

package com.ibm.sbt.security.authentication.oauth.consumer;

import com.ibm.commons.util.StringUtil;




/**
 * Holds a consumer token.
 * <p> 
 * This holds both the consumerKey and the consumerSecret needed by an application.
 * </p>
 * @author Philippe Riand
 */
public class ConsumerToken {

    private String requestTokenUri;
    private String authorizationUri; 
    private String accessTokenUri;
    private String verifierId;
    private String consumerKey;
    private String signatureMethod;
    private String consumerSecret;
    
    public ConsumerToken() {
    }

    public ConsumerToken(String requestTokenUri, String authorizationUri, String accessTokenUri, String verifierId, String consumerKey, String signatureMethod, String consumerSecret) {
        this.requestTokenUri = requestTokenUri;
        this.authorizationUri = authorizationUri;
        this.accessTokenUri = accessTokenUri;
        this.verifierId = verifierId;
        this.consumerKey = consumerKey;
        this.signatureMethod = signatureMethod;
        this.consumerSecret = consumerSecret;
    }

    public String getRequestTokenUri() {
        return requestTokenUri;
    }

    public String getAuthorizationUri() {
        return authorizationUri;
    }

    public String getAccessTokenUri() {
        return accessTokenUri;
    }

    public String getVerifierId() {
        return verifierId;
    }
    
    public String getConsumerKey() {
        return consumerKey;
    }
    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }
    
    public String getSignatureMethod() {
		return signatureMethod;
	}

	public void setSignatureMethod(String signatureMethod) {
		this.signatureMethod = signatureMethod;
	}

	public String getConsumerSecret() {
        return consumerSecret;
    }
    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }
    
    @Override
    public String toString()
    {
    	return StringUtil.format( "[ConsumerToken: requestTokenUri = {0}, authorizationUri = {1}, accessTokenUri = {2}]", 
    			                     new Object[] { requestTokenUri, authorizationUri, accessTokenUri } );

    }
}
