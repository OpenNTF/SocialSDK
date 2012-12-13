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

package com.ibm.sbt.services.util;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.endpoints.Endpoint;

public enum AuthUtil {
	INSTANCE;
	
	public final String OAUTH 		= "oauth";
	public final String BASIC 		= "basic";
	

	/*
	 * @param endpoint
	 * @return authentication type to be used in API construction based on Endpoint AuthType
	 */
	
	public String getAuthValue(Endpoint endpoint) {

		if (null == endpoint) {
			return null;
		}

		String authType = endpoint.getAuthType();
		if (StringUtil.isEmpty(authType)) {
			return null;
		}

		String authValue = null;
		if (authType.equalsIgnoreCase("oauth2.0")) {
			authValue = OAUTH;
		} else if (authType.equalsIgnoreCase("oauth1.0a")) {
			authValue = OAUTH;
		} else if (authType.equalsIgnoreCase("basic")) {
			authValue = BASIC;
		}
		return authValue;

	}

}
