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

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeConstants;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.security.authentication.oauth.OAuthException;
import com.ibm.sbt.security.authentication.oauth.consumer.AccessToken;
import com.ibm.sbt.security.authentication.oauth.consumer.HMACOAuth1Handler;
import com.ibm.sbt.security.authentication.oauth.consumer.OAuth1Handler;
import com.ibm.sbt.security.authentication.oauth.consumer.OAuthHandler;
import com.ibm.sbt.security.authentication.oauth.consumer.servlet.OAClientAuthentication;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.js.JSReference;
import com.ibm.sbt.util.SBTException;

import org.apache.http.Header;

/**
 * Bean that provides authentication via OAuth.
 * <p>
 * </p>
 * 
 * @author Philippe Riand
 * @author Vimal Dhupar
 */
public class OAuthEndpoint extends AbstractEndpoint {

	protected OAuth1Handler	oAuthHandler;
	
	public OAuthEndpoint(){
		this.oAuthHandler = new OAuth1Handler();
	}
	
	protected OAuthEndpoint(OAuth1Handler handler) {
		this.oAuthHandler = handler;
	}
	
	/**
	 * Force login of the specified user using the cached credentials if available.
	 * If not cached credential are available or these are invalid the login will fail.
	 * 
	 * @param user
	 * @return true if the specified user was logged in using their cached credentials
	 * @throws ClientServicesException
	 */
    public boolean login(String user) throws ClientServicesException {
    	this.oAuthHandler.setUserId(user);
    	if (isAuthenticated()) {
    		return isAuthenticationValid();
    	}
    	return false;
    }	
	
	@Override
	public void checkValid() throws SBTException {
		super.checkValid();
		if (StringUtil.isEmpty(oAuthHandler.getConsumerKey())) {
			throw new SBTException(null, "The Endpoint consumer key is empty, class {0}", getClass());
		}
		if (StringUtil.isEmpty(oAuthHandler.getConsumerSecret())) {
			throw new SBTException(null, "The Endpoint consumer secret is empty, class {0}", getClass());
		}
		if (StringUtil.isEmpty(oAuthHandler.getAuthorizationURL())) {
			throw new SBTException(null, "The Endpoint authorization URL is empty, class {0}", getClass());
		}
		if (StringUtil.isEmpty(oAuthHandler.getRequestTokenURL())) {
			throw new SBTException(null, "The Endpoint request token URL is empty, class {0}", getClass());
		}
		if (StringUtil.isEmpty(oAuthHandler.getAccessTokenURL())) {
			throw new SBTException(null, "The Endpoint access token URL is empty, class {0}", getClass());
		}
	}

	@Override
	public void setUrl(String url) {
		super.setUrl(url);
		// Make the URL the service name if not already set
		if (StringUtil.isEmpty(oAuthHandler.getServiceName())) {
			oAuthHandler.setServiceName(url);
		}
	}

	public String getConsumerKey() {
		return oAuthHandler.getConsumerKey();
	}

	public void setConsumerKey(String consumerKey) {
		oAuthHandler.setConsumerKey(consumerKey);
	}

	public String getConsumerSecret() {
		return oAuthHandler.getConsumerSecret();
	}

	public void setConsumerSecret(String consumerSecret) {
		oAuthHandler.setConsumerSecret(consumerSecret);
	}

	@Override
	public String getCredentialStore() {
		return oAuthHandler.getCredentialStore();
	}

	@Override
	public void setCredentialStore(String credentialStore) {
		oAuthHandler.setCredentialStore(credentialStore);
	}

	public String getAppId() {
		return oAuthHandler.getAppId();
	}

	public void setAppId(String appId) {
		oAuthHandler.setAppId(appId);
	}

	public String getServiceName() {
		return oAuthHandler.getServiceName();
	}

	public void setServiceName(String serviceName) {
		oAuthHandler.setServiceName(serviceName);
	}

	public String getRequestTokenURL() {
		return oAuthHandler.getRequestTokenURL();
	}

	public void setRequestTokenURL(String requestTokenURL) {
		oAuthHandler.setRequestTokenURL(requestTokenURL);
	}

	public String getAuthorizationURL() {
		return oAuthHandler.getAuthorizationURL();
	}

	public void setAuthorizationURL(String authorizationURL) {
		oAuthHandler.setAuthorizationURL(authorizationURL);
	}

	public String getAccessTokenURL() {
		return oAuthHandler.getAccessTokenURL();
	}

	public void setAccessTokenURL(String accessTokenURL) {
		oAuthHandler.setAccessTokenURL(accessTokenURL);
	}

	public String getSignatureMethod() {
		return oAuthHandler.getSignatureMethod();
	}

	public void setSignatureMethod(String signatureMethod) {
		oAuthHandler.setSignatureMethod(signatureMethod);
	}

	protected void setOauthHandler(HMACOAuth1Handler hmacoAuth1Handler) {
		this.oAuthHandler = hmacoAuth1Handler;
	}

	@Override
	public String getAuthType() {
		return "oauth1.0a";
	}

	@Override
	public boolean isForceTrustSSLCertificate() {
		return oAuthHandler.getForceTrustSSLCertificate();
	}

	@Override
	public void setForceTrustSSLCertificate(boolean forceTrustSSLCertificate) {
		oAuthHandler.setForceTrustSSLCertificate(forceTrustSSLCertificate);


	}

	@Override
	public JSReference getAuthenticator(String endpointName, String sbtUrl) {
		Context ctx = Context.get();
		JSReference reference = new JSReference("sbt/authenticator/OAuth");
		StringBuilder b = new StringBuilder();
		RuntimeConstants.get().appendBaseProxyUrl(b, ctx);
		b.append("/");
		b.append(OAClientAuthentication.URL_PATH);
		b.append('/');
		b.append(endpointName);
		String url = b.toString();
		reference.getProperties().put("url", url);
		reference.getProperties().put("loginUi", ctx.getProperty("loginUi"));
		return reference;
	}

	@Override
	public boolean isAuthenticated() throws ClientServicesException {
		try {
			return oAuthHandler.acquireToken() != null;
		} catch (OAuthException ex) {
			throw new ClientServicesException(ex);
		}
	}

	@Override
	public void authenticate(boolean force) throws ClientServicesException {
		try {
			oAuthHandler.acquireToken(true, force);
		} catch (OAuthException ex) {
			throw new ClientServicesException(ex);
		}
	}

	@Override
	public void logout() throws OAuthException {
		oAuthHandler.deleteToken();
	}

	@Override
	public void initialize(DefaultHttpClient httpClient) throws ClientServicesException {
		try {
			AccessToken token = oAuthHandler.acquireToken(false);
			if ((token != null) && (oAuthHandler != null)) {
				HttpRequestInterceptor oauthInterceptor = new OAuthInterceptor(token, super.getUrl(),oAuthHandler);
				httpClient.addRequestInterceptor(oauthInterceptor, 0);
			}
		} catch (OAuthException ex) {
			throw new ClientServicesException(ex);
		}
	}

	private static class OAuthInterceptor implements HttpRequestInterceptor {

		private final AccessToken token;
		private final String baseUrl;
		private final OAuthHandler oaHandler;

		public OAuthInterceptor(AccessToken token, String baseUrl, OAuthHandler oaHandler) {
			this.token = token;
			this.baseUrl = baseUrl;
			this.oaHandler = oaHandler;
		}

		@Override
		public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {

			String authorizationheader = null;
			if (oaHandler != null) {
				if (oaHandler.getClass().equals(HMACOAuth1Handler.class)) {
					String requestUri = request.getRequestLine().getUri().toString();
					// Using UrlUtil's getParamsMap for creating the ParamsMap
					// from the query String Request Uri.
					Map<String, String> mapOfParams = UrlUtil.getParamsMap(requestUri);
					try {
						if(StringUtil.isNotEmpty(requestUri)&& requestUri.indexOf("?")!=-1)
							requestUri = requestUri.substring(0, requestUri.indexOf("?"));
						requestUri = PathUtil.concat(baseUrl, requestUri, '/');

						// creating authorization header
						authorizationheader = ((HMACOAuth1Handler) oaHandler).createAuthorizationHeader(requestUri,
								mapOfParams);
					} catch (OAuthException e) {
						throw new HttpException(
								"OAuthException thrown while creating Authorization header in OAuthInterceptor", e);
					}
				} else {
					authorizationheader = oaHandler.createAuthorizationHeader();
				}
			} else {
				throw new HttpException("Error retrieving OAuth Handler. OAuth Handler is null");
			}
			
			// Remove any existing authorization headers which can cause oauth requests to fail
			if(request.containsHeader("authorization")){
				Header[] header = request.getHeaders("authorization");
				for (int i = 0; i < header.length; i++) {
					request.removeHeader(header[i]);
				}
			} 
			
			request.addHeader("Authorization", authorizationheader);
		}
	}
}
