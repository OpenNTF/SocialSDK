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

import java.io.Serializable;
import java.util.Date;
import com.ibm.commons.util.StringUtil;



/**
 * Holds a token to connect to a service, for a dedicated user.
 * <p> 
 * This holds both the application identifier and the URL to reach the service. Not that it
 * does not hold the token specific to a user.
 * </p>
 * @author Philippe Riand
 */
public class AccessToken implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String appId;
    private String serviceName;
    private String consumerKey;
    private String userId;
    private String moduleId;
    private String tokenName;
    private String accessToken;
    private String tokenSecret;
    private String refreshToken;
    
    private Date expiresIn;
    private Date authorizationExpiresIn;

    // Do we need to store that?
    private String sessionHandle;
    
	public AccessToken(String appId, String serviceName, String consumerKey, String accessToken, String tokenSecret, String userId, String moduleId, String tokenName, Date expiresIn, Date authorizationExpiresIn, String sessionHandle) {
        this.appId = appId;
        this.serviceName = serviceName;
        this.consumerKey = consumerKey;
	    this.accessToken = accessToken;
	    this.tokenSecret = tokenSecret;
        this.userId = userId;
        this.moduleId = moduleId;
        this.tokenName = tokenName;
	    this.expiresIn = expiresIn;
	    this.authorizationExpiresIn = authorizationExpiresIn;
	    this.sessionHandle = sessionHandle;
    }
	
    public AccessToken(String appId, String serviceName, String consumerKey, String accessToken, String tokenSecret, String userId, Date expiresIn, Date authorizationExpiresIn, String sessionHandle) {
        this(appId, serviceName, consumerKey, accessToken, tokenSecret, userId, null, null, expiresIn, authorizationExpiresIn, sessionHandle);
    }
    
    // This is consumed with OAuth2.0
    public AccessToken(String appId, String serviceName, String consumerKey, String accessToken, String tokenSecret, String userId, Date expiresIn, String refreshToken) {
        this.appId = appId;
        this.serviceName = serviceName;
        this.consumerKey = consumerKey;
	    this.accessToken = accessToken;
	    this.tokenSecret = tokenSecret;
        this.userId = userId;
	    this.expiresIn = expiresIn;
	    this.refreshToken = refreshToken;
    }
    
	public boolean isExpired() {
	    return isExpired(0);
	}
    
    public boolean isExpired(int threshold) {
        if(expiresIn!=null) {
            long exp = expiresIn.getTime()-threshold*1000;
            long now = System.currentTimeMillis();
            return exp<=now; 
        }
        return false;
    }
    
    public boolean isAuthorizationExpired() {
        if(authorizationExpiresIn!=null) {
            return authorizationExpiresIn.getTime()<System.currentTimeMillis(); 
        }
        return false;
    }

    public String getAppId() {
        return appId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public String getUserId() {
        return userId;
    }

    public Date getExpiresIn() {
        return expiresIn;
    }

    public Date getAuthorizationExpiresIn() {
        return authorizationExpiresIn;
    }

    public String getSessionHandle() {
        return sessionHandle;
    }

	public String getModuleId() {
		return moduleId == null ? "" : moduleId;
	}

	public String getTokenName() {
		return tokenName == null ? "" : tokenName;
	}
	
	/**
	 * @return the refreshToken
	 */
	public String getRefreshToken() {
		return refreshToken;
	}
	
	@Override
	public String toString() 
	{
		return StringUtil.format( "[AccessToken: appId = {0}, serviceName = {1}, userId = {2}, moduleId = {3}, tokenName = {4}, expiresIn = {5}, authorizationExpiresIn = {6}, sessionHandle = {7}]",
				                     new Object[] { appId, serviceName, userId, moduleId, tokenName, expiresIn, authorizationExpiresIn, sessionHandle } );
	}
}
