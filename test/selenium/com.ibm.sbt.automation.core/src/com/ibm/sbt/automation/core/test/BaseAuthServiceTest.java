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
package com.ibm.sbt.automation.core.test;

import com.ibm.sbt.automation.core.environment.TestEnvironment;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.security.authentication.password.PasswordException;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;

/**
 * @author mwallace
 *  
 * @date 5 Mar 2013
 */
public class BaseAuthServiceTest extends BaseServiceTest {
    
    /**
     * Default constructor
     */
    public BaseAuthServiceTest() {
        setAuthType(AuthType.AUTO_DETECT);
    }

    /**
     * @throws PasswordException
     */
    protected void loginConnections() throws AuthenticationException {
        BasicEndpoint endpoint = (BasicEndpoint)EndpointFactory.getEndpoint(CommunityService.DEFAULT_ENDPOINT_NAME);
        String username = getProperty(TestEnvironment.PROP_BASIC_USERNAME);
        String password = getProperty(TestEnvironment.PROP_BASIC_PASSWORD);
        endpoint.login(username, password, true);
    }
    
}
