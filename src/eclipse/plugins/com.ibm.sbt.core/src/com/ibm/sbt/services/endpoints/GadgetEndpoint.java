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

import org.apache.http.impl.client.DefaultHttpClient;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.js.JSReference;

/**
 * Bean that provides authentication via OAuth.
 * 
 * @author mwallace
 */
public class GadgetEndpoint extends AbstractEndpoint {
	
	/**
	 * Default constructor for GadgetEndpoint
	 */
	public GadgetEndpoint() {
		setUseProxy(false);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.services.endpoints.AbstractEndpoint#getTransport(java.lang.String, java.lang.String)
	 */
	@Override
	public JSReference getTransport(String endpointName, String moduleId) {
		JSReference reference = new JSReference("sbt/GadgetTransport");
		return reference;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.services.endpoints.Endpoint#isAuthenticated()
	 */
	@Override
	public boolean isAuthenticated() throws ClientServicesException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.services.endpoints.Endpoint#authenticate(boolean)
	 */
	@Override
	public void authenticate(boolean force) throws ClientServicesException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.services.endpoints.Endpoint#initialize(org.apache.http.impl.client.DefaultHttpClient)
	 */
	@Override
	public void initialize(DefaultHttpClient httpClient) throws ClientServicesException {
		// TODO Auto-generated method stub

	}

	@Override
	public void logout() throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}

}
