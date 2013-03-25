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
package com.ibm.sbt.services.client;

import java.util.Map;

import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.LinkedInOAuth2Endpoint;

/**
 * Base class for a REST service client for Linkedin
 * 
 * @author Manish Kataria
 */
public class LinkedInClientService extends ClientService {
	
	public LinkedInClientService(Endpoint endpoint) {
		super(endpoint);
	} 
	
	@Override
	protected Args createArgs(String serviceUrl, Map<String, String> parameters)
			throws ClientServicesException {
		Args args = new Args();
		args.setServiceUrl(serviceUrl);
		// linkedin requires security token as a param instead of header
		parameters.put("oauth2_access_token", ((LinkedInOAuth2Endpoint)super.endpoint).getHandler().getAccessToken());
		args.setParameters(parameters);
		return args;
	}
	
}
