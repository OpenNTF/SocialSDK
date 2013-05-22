/* ***************************************************************** */
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


import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;

/**
 * @author priand
 *
 */
public class SmartCloudBasicEndpoint extends BasicEndpoint {
    
    private ConnectionsEndpointAdapter endpointAdapter;

    private static final int FORBIDDEN = 403;
    
    public SmartCloudBasicEndpoint() {
        endpointAdapter = new ConnectionsEndpointAdapter(this);
    }

    @Override
	public boolean isHeaderAllowed(String headerName, String serviceUrl){
		return endpointAdapter.isHeaderAllowed(headerName, serviceUrl);
    }
    
    @Override
    public void updateHeaders(DefaultHttpClient client, HttpRequestBase method) {
        endpointAdapter.updateHeaders(client, method);
    }

    @Override
	public ClientService getClientService() throws ClientServicesException {
    	return endpointAdapter.getClientService();
    }
    
    @Override
    public int getAuthenticationErrorCode() {
    	return FORBIDDEN;
    }
}
