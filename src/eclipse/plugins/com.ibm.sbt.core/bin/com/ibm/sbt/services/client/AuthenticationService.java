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

import com.ibm.commons.Platform;
import com.ibm.sbt.services.endpoints.Endpoint;




/**
 * Service that verifies that the authentication is valid.
 * @author Philippe Riand
 */
public class AuthenticationService extends ClientService {

    private boolean validAuthentication;
    
    public AuthenticationService() {
    }
    public AuthenticationService(Endpoint endpoint) {
        super(endpoint);
    }
    public AuthenticationService(String endpointName) {
        super(endpointName);
    }
    
    public boolean isValidAuthentication(String serviceUrl) {
        try {
            this.validAuthentication = true;
            get(serviceUrl,ClientService.FORMAT_NULL);
            return validAuthentication;
        } catch (ClientServicesException e) {
        	Platform.getInstance().log(e);
        }
        return false;
    }

    @Override
    protected void forceAuthentication(Args args) throws ClientServicesException {
        // ok, we are not authenticated...
        this.validAuthentication = false;
    }
}
