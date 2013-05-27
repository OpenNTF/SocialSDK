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
import com.ibm.sbt.core.configuration.Configuration;
import com.ibm.sbt.security.authentication.oauth.OAuthException;
import com.ibm.sbt.security.authentication.oauth.consumer.AccessToken;
import com.ibm.sbt.security.authentication.oauth.consumer.HMACOAuth1Handler;
import com.ibm.sbt.security.authentication.oauth.consumer.OAProvider;
import com.ibm.sbt.security.authentication.oauth.consumer.OAuthHandler;
import com.ibm.sbt.security.authentication.oauth.consumer.oauth_10a.servlet.OAClientAuthentication;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.js.JSReference;
import com.ibm.sbt.util.SBTException;

/**
 * Bean that provides authentication via OAuth.
 * <p>
 * </p>
 * 
 * @author Philippe Riand
 */
public class OAuthEndpoint extends AbstractEndpoint {

	protected final OAProvider	oaProvider	= new OAProvider();

	public OAuthEndpoint() {
	}

	@Override
	public void checkValid() throws SBTException {
		super.checkValid();
		if (StringUtil.isEmpty(oaProvider.getConsumerKey())) {
			throw new SBTException(null, "The Endpoint consumer key is empty, class {0}", getClass());
		}
		if (StringUtil.isEmpty(oaProvider.getConsumerSecret())) {
			throw new SBTException(null, "The Endpoint consumer secret is empty, class {0}", getClass());
		}
		if (StringUtil.isEmpty(oaProvider.getAuthorizationURL())) {
			throw new SBTException(null, "The Endpoint authorization URL is empty, class {0}", getClass());
		}
		if (StringUtil.isEmpty(oaProvider.getRequestTokenURL())) {
			throw new SBTException(null, "The Endpoint request token URL is empty, class {0}", getClass());
		}
		if (StringUtil.isEmpty(oaProvider.getAccessTokenURL())) {
			throw new SBTException(null, "The Endpoint access token URL is empty, class {0}", getClass());
		}
	}

	public OAProvider getOAuthProvider() {
		return oaProvider;
	}

	@Override
	public void setUrl(String url) {
		super.setUrl(url);
		// Make the URL the service name if not already set
		if (StringUtil.isEmpty(oaProvider.getServiceName())) {
			oaProvider.setServiceName(url);
		}
	}

	public String getConsumerKey() {
		return oaProvider.getConsumerKey();
	}

	public void setConsumerKey(String consumerKey) {
		oaProvider.setConsumerKey(consumerKey);
	}

	public String getConsumerSecret() {
		return oaProvider.getConsumerSecret();
	}

	public void setConsumerSecret(String consumerSecret) {
		oaProvider.setConsumerSecret(consumerSecret);
	}

	public String getTokenStore() {
		return oaProvider.getTokenStore();
	}

	public void setTokenStore(String tokenStore) {
		oaProvider.setTokenStore(tokenStore);
	}

	public String getAppId() {
		return oaProvider.getAppId();
	}

	public void setAppId(String appId) {
		oaProvider.setAppId(appId);
	}

	public String getServiceName() {
		return oaProvider.getServiceName();
	}

	public void setServiceName(String serviceName) {
		oaProvider.setServiceName(serviceName);
	}

	public String getRequestTokenURL() {
		return oaProvider.getRequestTokenURL();
	}

	public void setRequestTokenURL(String requestTokenURL) {
		oaProvider.setRequestTokenURL(requestTokenURL);
	}

	public String getAuthorizationURL() {
		return oaProvider.getAuthorizationURL();
	}

	public void setAuthorizationURL(String authorizationURL) {
		oaProvider.setAuthorizationURL(authorizationURL);
	}

	public String getAccessTokenURL() {
		return oaProvider.getAccessTokenURL();
	}

	public void setAccessTokenURL(String accessTokenURL) {
		oaProvider.setAccessTokenURL(accessTokenURL);
	}

	public String getSignatureMethod() {
		return oaProvider.getSignatureMethod();
	}

	public void setSignatureMethod(String signatureMethod) {
		oaProvider.setSignatureMethod(signatureMethod);
	}

	@Override
	public String getAuthType() {
		return "oauth1.0a";
	}

	@Override
	public boolean isForceTrustSSLCertificate() {
		return oaProvider.getForceTrustSSLCertificate();
	}

	@Override
	public void setForceTrustSSLCertificate(boolean forceTrustSSLCertificate) {
		oaProvider.setForceTrustSSLCertificate(forceTrustSSLCertificate);
	}

	public String getApplicationAccessToken() {
		return oaProvider.applicationAccessToken;
	}

	public void setApplicationAccessToken(String applicationAccessToken) {
		oaProvider.applicationAccessToken = applicationAccessToken;
	}

	@Override
	public JSReference getAuthenticator(String endpointName) {
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
		return reference;
	}

	@Override
	public boolean isAuthenticated() throws ClientServicesException {
		try {
			return oaProvider.acquireToken() != null;
		} catch (OAuthException ex) {
			throw new ClientServicesException(ex);
		}
	}

	@Override
	public void authenticate(boolean force) throws ClientServicesException {
		try {
			oaProvider.acquireToken(true, force);
		} catch (OAuthException ex) {
			throw new ClientServicesException(ex);
		}
	}

	@Override
	public void logout() throws OAuthException {
		oaProvider.deleteToken();
	}

	@Override
	public void initialize(DefaultHttpClient httpClient) throws ClientServicesException {
		try {
			AccessToken token = oaProvider.acquireToken(false);
			if (token != null) {
				HttpRequestInterceptor oauthInterceptor = new OAuthInterceptor(token, super.getUrl());
				httpClient.addRequestInterceptor(oauthInterceptor, 0);
			}
		} catch (OAuthException ex) {
			throw new ClientServicesException(ex);
		}
	}

	private static class OAuthInterceptor implements HttpRequestInterceptor {

		private final AccessToken	token;
		private final String		baseUrl;

		public OAuthInterceptor(AccessToken token, String baseUrl) {
			this.token = token;
			this.baseUrl = baseUrl;
		}

		@Override
		public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {

			Context contextForHandler = Context.get();
			OAuthHandler oaHandler = (OAuthHandler) contextForHandler.getSessionMap().get(
					Configuration.OAUTH_HANDLER);
			contextForHandler.getSessionMap().get("oaProvider");
			String authorizationheader = null;
			if(oaHandler != null) {
				if (oaHandler.getClass().equals(HMACOAuth1Handler.class)) {
					String requestUri = request.getRequestLine().getUri().toString();
					// Using UrlUtil's getParamsMap for creating the ParamsMap from the query String Request Uri.
					Map<String, String> mapOfParams = UrlUtil.getParamsMap(requestUri);
					try {
						requestUri = requestUri.substring(0, requestUri.indexOf("?"));
						requestUri = PathUtil.concat(baseUrl, requestUri, '/');
	
						// creating authorization header
						authorizationheader = ((HMACOAuth1Handler) oaHandler).createAuthorizationHeader(
								requestUri, mapOfParams);
					} catch (OAuthException e) {
						throw new HttpException(
								"OAuthException thrown while creating Authorization header in OAuthInterceptor",
								e);
					}
				} else {
					authorizationheader = oaHandler.createAuthorizationHeader();
				}
			}
			else {
				throw new HttpException("Error retrieving OAuth Handler. OAuth Handler is null");
			}
			request.addHeader("Authorization", authorizationheader);
		}
	}
}
