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

import java.util.HashMap;
import java.util.Map;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.PathUtil;
import com.ibm.sbt.core.configuration.Configuration;
import com.ibm.sbt.security.authentication.oauth.consumer.TwitterOAuth1Handler;
import com.ibm.sbt.services.endpoints.Endpoint;

public class TwitterClientService extends ClientService {

	private Args	args;

	public TwitterClientService(Endpoint endpoint) {
		super(endpoint);
	}

	public void setArgs(Args args) {
		this.args = args;
	}

	public Args getArgs() {
		return args;
	}

	@Override
	protected Args createArgs(String serviceUrl, Map<String, String> parameters)
			throws ClientServicesException {

		Args args = super.createArgs(serviceUrl, parameters);
		Map<String, String> headers = new HashMap<String, String>();
		String url = PathUtil.concat(getBaseUrl(), serviceUrl, '/');
		String authorizationheader = ((TwitterOAuth1Handler) Context.get().getSessionMap()
				.get(Configuration.OAUTH_HANDLER)).createAuthorizationHeader(url, parameters);
		headers.put("Authorization", authorizationheader);
		args.setHeaders(headers);
		return args;
	}
}
