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

import com.ibm.sbt.services.client.ClientServicesException;


/**
 * Bean that provides an empty authentication.
 * <p>
 * </p>
 * @author Philippe Riand
 */
public class AnonymousEndpoint extends AbstractEndpoint {

    public AnonymousEndpoint() {
    }
    
    @Override
	public boolean isAuthenticated() throws ClientServicesException {
        return true;
    }
    
    @Override
	public void authenticate(boolean force) throws ClientServicesException {
        throw new ClientServicesException(null,"Empty authorization bean cannot authenticate");
    }
    
    @Override
	public void initialize(DefaultHttpClient httpClient) {
        // nothing
    }
}
