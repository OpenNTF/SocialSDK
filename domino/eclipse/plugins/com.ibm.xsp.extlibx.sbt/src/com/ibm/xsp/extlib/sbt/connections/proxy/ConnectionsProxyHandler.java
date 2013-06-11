/*
 * © Copyright IBM Corp. 2010
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
package com.ibm.xsp.extlib.sbt.connections.proxy;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.sbt.service.basic.ProxyEndpointService;
import com.ibm.xsp.extlib.proxy.handlers.AbstractProxyHandler;
/**
 * Proxy Handler for Connections.
 * <p>
 * This proxy handler is based on an implementation using an endpoint bean for 
 * security reasons. 
 * </p>
 * @author priand
 */
public class ConnectionsProxyHandler extends AbstractProxyHandler {

    public static final String URL_PATH = "connections";

    public ConnectionsProxyHandler() {
    }
    
    public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        ProxyEndpointService p = new ProxyEndpointService() {
            @Override
            protected String getProxyUrlPath() {
                return URL_PATH;
            }
            @Override
            protected boolean isMethodAllowed(String method) {
                if(method.equalsIgnoreCase("get")) {
                    return true;
                }
                return false;
            }
        };
        p.service(request, response);
    }    
}
