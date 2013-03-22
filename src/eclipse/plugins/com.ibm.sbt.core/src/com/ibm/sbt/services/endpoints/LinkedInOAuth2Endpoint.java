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

import com.ibm.sbt.security.authentication.oauth.consumer.LinkedInOauth2Handler;
import com.ibm.sbt.security.authentication.oauth.consumer.OAuth2Handler;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.OAuth2Endpoint;



/**
 * LinkedIn OAuth 2 Endpoint.
 * @author mkataria
 *
 */
public class LinkedInOAuth2Endpoint extends OAuth2Endpoint {
	
    public LinkedInOAuth2Endpoint() {
    	super.oAuthHandler = null;
    	super.setHandler(new LinkedInOauth2Handler());
    }

   
	public void setScope(String scope) {
		((LinkedInOauth2Handler)oAuthHandler).setScope(scope);
	}

	public String getScope() {
		return ((LinkedInOauth2Handler)oAuthHandler).getScope();
	}
	
	public void setState(String state) {
		((LinkedInOauth2Handler)oAuthHandler).setState(state);
	}

	public String getState() {
		return ((LinkedInOauth2Handler)oAuthHandler).getState();
	}
	
	
	
	@Override
	public void initialize(DefaultHttpClient httpClient) throws ClientServicesException {
		// do nothing, interceptor can not add parameters, it can only add headers
	}
}
