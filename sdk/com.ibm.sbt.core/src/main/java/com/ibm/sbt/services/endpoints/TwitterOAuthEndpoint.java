/*
 * © Copyright IBM Corp. 2013
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
import com.ibm.sbt.core.configuration.Configuration;
import com.ibm.sbt.security.authentication.oauth.consumer.HMACOAuth1Handler;
/**
 * @author Vimal Dhupar
 *
 */

public class TwitterOAuthEndpoint extends OAuthEndpoint {
	
    public TwitterOAuthEndpoint() {
    	super(new HMACOAuth1Handler());
    }
    public String getApplicationAccessToken() {
		return ((HMACOAuth1Handler)oAuthHandler).getApplicationAccessToken();
	}

	public void setApplicationAccessToken(String applicationAccessToken) {
		((HMACOAuth1Handler)oAuthHandler).setApplicationAccessToken(applicationAccessToken);
	}
    @Override
	public void setSignatureMethod(String signatureMethod) {
		oAuthHandler.setSignatureMethod(signatureMethod);
		if (StringUtil.equalsIgnoreCase(Configuration.HMAC_SIGNATURE, signatureMethod)) {
			this.setOauthHandler(new HMACOAuth1Handler(getConsumerKey(), getConsumerSecret(), getCredentialStore(), getAppId(), 
					getServiceName(), getRequestTokenURL(), getAuthorizationURL(), getAccessTokenURL(), getSignatureMethod(), 
					isForceTrustSSLCertificate(), getApplicationAccessToken()));
    }
	}
	@Override
    public String getPlatform() {
    	return PLATFORM_TWITTER;
    }
}
