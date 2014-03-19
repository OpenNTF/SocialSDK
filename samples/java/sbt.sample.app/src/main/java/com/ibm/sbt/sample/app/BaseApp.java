/*
 * © Copyright IBM Corp. 2014
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
package com.ibm.sbt.sample.app;

import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.SmartCloudBasicEndpoint;

/**
 * @author mwallace
 *
 */
public class BaseApp {

	private String url;
	private String user;
	private String password;
	
	private BasicEndpoint basicEndpoint;
	
	public BaseApp(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
	}

	/**
	 * @return the basicEndpoint
	 */
	public BasicEndpoint getBasicEndpoint() {
    	if (basicEndpoint == null) {
    		if ("true".equals(System.getProperty("isSmartCloud"))) {
    			basicEndpoint = new SmartCloudBasicEndpoint();    			
    		} else {
    			basicEndpoint = new BasicEndpoint();
    		}
			basicEndpoint.setUrl(url);
			basicEndpoint.setForceTrustSSLCertificate(true);
			basicEndpoint.setUser(user);
			basicEndpoint.setPassword(password);
    	}
    	return basicEndpoint;
	}
    		
}
