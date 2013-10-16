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
package com.ibm.sbt.services.client.smartcloud;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.SmartCloudFormEndpoint;

/**
 * SmartCloud service.
 * @author Vimal Dhupar
 */
public class SmartCloudService extends ClientService {

	public SmartCloudService() {
	}
	public SmartCloudService(Endpoint endpoint) {
		super(endpoint);
	}
	public SmartCloudService(String endpointName) {
		super(endpointName);
	}
	@Override
	protected boolean isResponseRequireAuthentication(HttpResponse httpResponse){
		// Smartcloud platform returns 200 with a login page when authentication is required particularly in case of form based authentication
		// To handle this we check the content type of response from Smartcloud. It should never be text/html except in case it is returning a login form
		if(httpResponse.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK && endpoint instanceof SmartCloudFormEndpoint){
			Header[] value = httpResponse.getHeaders("Content-Type");
			String headerValue = value[0].getValue();
			if(StringUtil.equals(headerValue, "text/html")){
				return true;
			}
		}
		return false;
		
	}
}