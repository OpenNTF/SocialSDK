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

import org.apache.http.impl.client.DefaultHttpClient;
import com.ibm.sbt.security.authentication.oauth.consumer.TwitterOAuth1Handler;
import com.ibm.sbt.services.client.ClientServicesException;

/**
 * Twitter OAuth1 Endpoint
 * 
 * @author Vimal Dhupar
 */
public class TwitterOAuth1Endpoint extends OAuthEndpoint {

	public TwitterOAuth1Endpoint() {
		super();
		super.setOaHandler(new TwitterOAuth1Handler());
	}

	public String getApplicationAccessToken() {
		return oaProvider.applicationAccessToken;
	}

	public void setApplicationAccessToken(String applicationAccessToken) {
		oaProvider.applicationAccessToken = applicationAccessToken;
	}

	@Override
	public void initialize(DefaultHttpClient httpClient) throws ClientServicesException {
		// Do nothing here.
		// We are not using interceptor for Twitter.
	}

}
