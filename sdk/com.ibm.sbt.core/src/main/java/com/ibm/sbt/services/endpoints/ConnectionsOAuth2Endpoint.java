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

import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.ConnectionsService;

/**
 * @author priand
 *
 */
public class ConnectionsOAuth2Endpoint extends OAuth2Endpoint {

	private static final int authenticationErrorCode = 403;
	
	
	
    public ConnectionsOAuth2Endpoint() {
    }
    
    /**
	 * The method blocks the header x-requested-with only for calls to IBM Connections Activities Service.
	 * The Activities Service does not return the correct feed when this header is present in the request.
	 * @param headerName Header name
	 * @param serviceUrl HTTP Request
	 * @return boolean true depicting header is passed
	 * 
	 */
    @Override
	public boolean isHeaderAllowed(String headerName, String serviceUrl){
    	if (headerName.equalsIgnoreCase("x-requested-with"))
		{
			if(serviceUrl.indexOf("activities/service") != -1){
				return false;
			}
		}
		return true;

    }

    @Override
    public String getPlatform() {
    	return PLATFORM_CONNECTIONS;
    }

    @Override
	public ClientService getClientService() throws ClientServicesException {
    	return new ConnectionsService(this);
    }
    
    @Override
	public int getAuthenticationErrorCode(){
    	return authenticationErrorCode;
    }
    
    @Override
    public void setApiVersion(String apiVersion) {
    	if (apiVersion!=null) {
    		if (apiVersion.matches("4[.]?5([.]?0)?")){
    			oAuthHandler.setUsePost(true);
    		}
    	}
    }
    
}
