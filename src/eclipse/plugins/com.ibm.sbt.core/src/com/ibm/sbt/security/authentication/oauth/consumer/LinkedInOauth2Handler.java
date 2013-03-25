/****************************************************************************************
 * Copyright 2013 IBM Corp.                                                                   *
 *                                                                                      *
 * Licensed under the Apache License, Version 2.0 (the "License");                      *
 * you may not use this file except in compliance with the License.                     *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0   *
 *                                                                                      *
 * Unless required by applicable law or agreed to in writing, software                  *
 * distributed under the License is distributed on an "AS IS" BASIS,                    *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.             *
 * See the License for the specific language governing permissions and                  *
 * limitations under the License.                                                       *
 ****************************************************************************************/


package com.ibm.sbt.security.authentication.oauth.consumer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ibm.sbt.core.configuration.Configuration;
import com.ibm.sbt.services.util.SSLUtil;

public class LinkedInOauth2Handler extends OAuth2Handler {
	//for logging 
	private static final String sourceClass = OAuth1Handler.class.getName();
    private static final Logger logger = Logger.getLogger(sourceClass);
    private String scope;
    private String state;
	
	/**
	 * Generates the authorization url for fetching the authorization tokens
	 * 
	 * @return URL
	 * 
	 * @author Manish Kataria
	 */
	@Override
	public String getAuthorizationNetworkUrl() {
		StringBuilder url = new StringBuilder();
		try {
			url.append(getAuthorizationURL());
			url.append('?');
			url.append(Configuration.OAUTH2_RESPONSE_TYPE);
			url.append('=');
			url.append(Configuration.OAUTH2_CODE);
			url.append('&');
			url.append(Configuration.OAUTH2_CLIENT_ID);
			url.append('=');
			url.append(URLEncoder.encode(getConsumerKey(), "UTF-8"));
			url.append('&');
			url.append(Configuration.OAUTH_SCOPE);
			url.append('=');
			url.append(URLEncoder.encode(getScope(),"UTF-8"));
			url.append('&');
			url.append(Configuration.OAUTH_STATE);
			url.append('=');
			url.append(URLEncoder.encode(getState(),"UTF-8"));
			url.append('&');
			url.append(Configuration.OAUTH2_REDIRECT_URI);
			url.append('=');
			url.append(URLEncoder.encode(getClient_uri(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
		}
		return url.toString();	
	}
	
	
	
	/**
	 ** Authorization: OAuth 
	 *                callback_uri="<callback_uri>", 
	 *                client_secret="<client_secret>",
	 *                client_id="<client_id>", 
	 *                grant_type="authorization_code", 
	 *                code="<authorization_code>"
	 * 
	 * Calls the server URL with Authorization header and gets back following values:
	 *    access_token, refresh_token, issued_on expires_in, token_type.
	 * 
	 * @throws Exception
	 */
	@Override
	public void getAccessTokenForAuthorizedUser() throws Exception {
		HttpPost method = null;
		int responseCode = HttpStatus.SC_OK;
		String responseBody = null;
		InputStream content = null;
		try {
			HttpClient client = new DefaultHttpClient();
			if(forceTrustSSLCertificate)
				client = (DefaultHttpClient)SSLUtil.wrapHttpClient((DefaultHttpClient)client);
			String url = getAccessTokenUrl();
			method = new HttpPost(url.toString());
			
			HttpResponse httpResponse =client.execute(method);
			responseCode = httpResponse.getStatusLine().getStatusCode();
			
			content = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			responseBody = reader.readLine();
		} catch (Exception e) {
			throw new Exception("getAccessToken failed with Exception: <br>" + e);
		} finally {
			content.close();
		}
		if (responseCode != HttpStatus.SC_OK) {
			if (responseCode == HttpStatus.SC_UNAUTHORIZED) {
				throw new Exception("getAccessToken failed with Response Code: Unauthorized (401),<br>Msg: " + responseBody);
			} else if (responseCode == HttpStatus.SC_BAD_REQUEST) {
				throw new Exception("getAccessToken failed with Response Code: Bad Request (400),<br>Msg: " + responseBody);
			} else if (responseCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				throw new Exception("getAccessToken failed with Response Code: Internal Server error (500),<br>Msg: " + responseBody);
			} else { 
				throw new Exception("getAccessToken failed with Response Code: (" + responseCode + "),<br>Msg: " + responseBody);
			}
		} else {
			setOAuthData(responseBody); //save the returned data
		}
	}
	
	
	public String getAccessTokenUrl() throws Exception {
		try {
			StringBuffer url = new StringBuffer(2048);
			url.append(getAccessTokenURL()).append("?");
			url.append(Configuration.OAUTH2_REDIRECT_URI).append('=').append(URLEncoder.encode(getClient_uri(), "UTF-8"));
			url.append('&');
			url.append(Configuration.OAUTH2_CLIENT_ID).append('=').append(URLEncoder.encode(getConsumerKey(), "UTF-8"));
			url.append('&');
			url.append(Configuration.OAUTH2_CLIENT_SECRET).append('=').append(URLEncoder.encode(getConsumerSecret(), "UTF-8"));
			url.append('&');
			url.append(Configuration.OAUTH2_GRANT_TYPE).append('=').append(Configuration.OAUTH2_AUTHORIZATION_CODE);
			url.append('&');
			url.append(Configuration.OAUTH2_CODE).append('=').append(URLEncoder.encode(getAuthorization_code(), "UTF-8"));
			return url.toString();
		} catch (Exception e) {
			throw new Exception(
					"getAccessTokenUrl  : Exception occured in generating url for fetching Access Token");
		}

	}
	
	
	@Override
	protected void setOAuthData(String responseBody) {
		super.setAccessToken(getTokenValue(responseBody, Configuration.OAUTH2_ACCESS_TOKEN));
		super.setExpiresIn(getTokenValue(responseBody, Configuration.OAUTH2_EXPIRESIN));
	}
	
	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getScope() {
		System.err.println("returning scope as "+scope);
		return scope;
	}
	
	
	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}
}
