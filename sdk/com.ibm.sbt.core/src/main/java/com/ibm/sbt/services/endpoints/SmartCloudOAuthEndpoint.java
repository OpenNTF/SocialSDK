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
package com.ibm.sbt.services.endpoints;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.smartcloud.SmartCloudService;

/**
 * @author priand
 *
 */
public class SmartCloudOAuthEndpoint extends OAuthEndpoint {

	private static String REQUEST_TOKEN_PATH = "/manage/oauth/getRequestToken";
	private static String AUTHORIZATION_PATH = "/manage/oauth/authorizeToken";
	private static String ACCESS_TOKEN_PATH = "/manage/oauth/getAccessToken";
	private static String AUTHENTICATION_SERVICE = "communities/service/atom/communities/my";
	private static String SIGNATURE_METHOD = "PLAINTEXT";

	
    public SmartCloudOAuthEndpoint() {
    	setAuthenticationErrorCode(403);
    	clientParams.put("isSmartCloud", true);
    	
    	initDefaults();
    }
    
    /**
     * Set default values for authentication service and signature method
     */
    protected void initDefaults() {
    	if (StringUtil.isEmpty(getAuthenticationService())) {
    		setAuthenticationService(AUTHENTICATION_SERVICE);
    	}
    	if (StringUtil.isEmpty(getSignatureMethod())) {
    		setSignatureMethod(SIGNATURE_METHOD);
    	}
    }

    @Override
    public String getPlatform() {
    	return PLATFORM_SMARTCLOUD;
    }

    @Override
	public ClientService getClientService() throws ClientServicesException {
    	return new SmartCloudService(this);
    }
    
    @Override
	public boolean isHeaderAllowed(String headerName, String serviceUrl){
    	if (headerName.equalsIgnoreCase("Origin"))
		{
			return false;
		}
		return true;

    }


    @Override
    public void setUrl(String url) {
    	super.setUrl(url);

    	// set defaults for other url's if not already set
    	if (StringUtil.isEmpty(getRequestTokenURL())) {
    		setRequestTokenURL(url + REQUEST_TOKEN_PATH);
    	}
    	if (StringUtil.isEmpty(getAuthorizationURL())) {
    		setAuthorizationURL(url + AUTHORIZATION_PATH);
    	}
    	if (StringUtil.isEmpty(getAccessTokenURL())) {
    		setAccessTokenURL(url + ACCESS_TOKEN_PATH);
    	}
    }
    
    /**
     * Convenience method to set the serviceName and appId properties.
     * 
     * @param appName
     */
    public void setAppName(String appName) {
    	setAppId(appName);
    }
    
    /**
     * Convenience method to set the consumerKey property.
     * 
     * @param clientID
     */
    public void setOAuthKey(String oAuthKey) {
    	setConsumerKey(oAuthKey);
    }
    
    /**
     * Convenience method to set the consumerSecret property.
     * 
     * @param clientSecret
     */
    public void setOAuthSecret(String oAuthSecret) {
    	setConsumerSecret(oAuthSecret);
    }
}
