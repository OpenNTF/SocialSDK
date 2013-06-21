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
package com.ibm.sbt.config;

import com.ibm.sbt.services.endpoints.OAuthEndpoint;

/**
 * @author mwallace
 *
 */
public class MockOAuthEndpoint extends OAuthEndpoint {

	/**
	 * Constructor
	 */
	public MockOAuthEndpoint() {
		setUrl("https://apps.lotuslive.com");
		setForceTrustSSLCertificate(true);
		setRequestTokenURL("https://apps.lotuslive.com/manage/oauth/getRequestToken");
		setAuthorizationURL("https://apps.lotuslive.com/manage/oauth/authorizeToken");
		setAccessTokenURL("https://apps.lotuslive.com/manage/oauth/getAccessToken");
		setSignatureMethod("PLAINTEXT");
		setCredentialStore("SmartCloudStore");
		setServiceName("serviceName");
		setConsumerKey("consumer_key");
		setConsumerSecret("consumer_secret");
	}

}
