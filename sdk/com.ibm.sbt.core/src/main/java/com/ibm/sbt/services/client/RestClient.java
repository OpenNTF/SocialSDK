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

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.HttpContext;

import com.ibm.commons.util.StringUtil;


/**
 * REST service client that simply uses a base URL.
 * 
 * @author Philippe Riand
 */
public class RestClient extends ClientService {

	private String baseUrl;
	private Authenticator authenticator;
	
	public RestClient() {
	}

	public RestClient(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	@Override
	protected String getBaseUrl() {
		return baseUrl;
	}
	
	public Authenticator getAuthenticator() {
		return authenticator;
	}
	
	public void setAuthenticator(Authenticator authenticator) {
		this.authenticator = authenticator;
	}

	@Override
	protected void initialize(DefaultHttpClient httpClient) throws ClientServicesException {
		if (authenticator != null) {
			authenticator.initialize(httpClient);
		}
	}
	
	public static abstract class Authenticator {
		public abstract void initialize(DefaultHttpClient httpClient) throws ClientServicesException;
	}

	
	//
	// Basic authentication
	//

	public static class BasicAuthenticator extends Authenticator {
		private String userIdentity;
		private String password;
		public BasicAuthenticator(String userIdentity, String password) {
			this.userIdentity = userIdentity;
			this.password = password;
		}
	    public String getUserIdentity() {
			return userIdentity;
		}
		public void setUserIdentity(String userIdentity) {
			this.userIdentity = userIdentity;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		@Override
		public void initialize(DefaultHttpClient httpClient) throws ClientServicesException {
	        String usr = getUserIdentity();
	        if(StringUtil.isNotEmpty(usr)) {
	            String pwd = getPassword();
	            
	            UsernamePasswordCredentials creds = new UsernamePasswordCredentials(usr,pwd);
	            
	            HttpRequestInterceptor basicInterceptor = new BasicInterceptor(creds);
	            httpClient.addRequestInterceptor(basicInterceptor, 0);
	        }
	    }

	    private static class BasicInterceptor implements HttpRequestInterceptor {
	        
	        private UsernamePasswordCredentials credentials;
	
	        public BasicInterceptor(UsernamePasswordCredentials credentials) {
	            this.credentials = credentials;
	        }
	
	        @Override
			public void process(HttpRequest request, HttpContext context)throws HttpException, IOException {
	            AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
	            if (authState != null && authState.getAuthScheme() == null) {
	                AuthScheme scheme = new BasicSchemeFactory().newInstance(new BasicHttpParams());
	                authState.setAuthScheme(scheme);
	                authState.setCredentials(credentials);
	            }
	        }
	    }
	}
	
}
