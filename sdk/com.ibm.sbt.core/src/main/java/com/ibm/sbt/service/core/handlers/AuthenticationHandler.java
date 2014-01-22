/*
R *  Copyright IBM Corp. 2012
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
package com.ibm.sbt.service.core.handlers;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.security.authentication.password.PasswordException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;


public class AuthenticationHandler extends AbstractServiceHandler {
	public static final String URL_PATH                 = "authHandler";
    public static final String LOG_OUT					= "logout";
    public static final String IS_AUTHENTICATED			= "isAuth";
    public static final String IS_AUTHENTICATION_VALID	= "isAuthValid";
    private static final String APPLICATION_JSON        = "application/json";
	
	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
    	Endpoint endpoint = EndpointFactory.getEndpoint(getEndpointName(pathInfo));  //remove hardcoded name ..get second token from path info
    	PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "utf-8"));
		JsonObject jsonResponse = new JsonJavaObject();
    	if(getAuthAction(pathInfo).equals(LOG_OUT)){
    		boolean logoutSuccessful = true;
    		try {
				endpoint.logout();
			} catch (AuthenticationException e) {
				logoutSuccessful = false;
			}
			try {
				if(endpoint.isAuthenticated()){
					logoutSuccessful = false;
				}
			} catch (ClientServicesException e) {
				logoutSuccessful = false;
			}
			if(logoutSuccessful){
				jsonResponse.putJsonProperty("success", true);
				jsonResponse.putJsonProperty("status", 200);
			}else{
				jsonResponse.putJsonProperty("success", false);
				jsonResponse.putJsonProperty("status", 500);
			}
    	}
    	if(getAuthAction(pathInfo).equals(IS_AUTHENTICATED)){
				try {
					jsonResponse.putJsonProperty("result", endpoint.isAuthenticated());
					jsonResponse.putJsonProperty("status", 200);
				} catch (ClientServicesException e) {
					jsonResponse.putJsonProperty("result", false);
					jsonResponse.putJsonProperty("status", 500);
				}
    	}
    	if(getAuthAction(pathInfo).equals(IS_AUTHENTICATION_VALID)){
			try {
				jsonResponse.putJsonProperty("result", endpoint.isAuthenticationValid());
				jsonResponse.putJsonProperty("status", 200);
			} catch (ClientServicesException e) {
				jsonResponse.putJsonProperty("result", false);
				jsonResponse.putJsonProperty("status", 500);
			}
	}
        response.setContentType(APPLICATION_JSON);
        writer.write(jsonResponse.toString());
    	writer.flush();
	}
	
	public String getAuthAction(String pathInfo){//returns string JSApp or JavaApp
		String authAction = ""; 
			if(pathInfo != null){
				String[] tokens = pathInfo.split("/");
				if(tokens.length > 3){
					authAction = tokens[3];
				}
			}
		return authAction;
	}
	
	public String getEndpointName(String pathInfo){
		String endPointName = ""; 
			if(pathInfo != null){
				String[] tokens = pathInfo.split("/");
				if(tokens.length > 2){
					endPointName = tokens[2];
				}
			}
		return endPointName;
	}
    
}

