package com.ibm.sbt.services.endpoints;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import com.ibm.commons.Platform;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeConstants;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.security.authentication.oauth.OAuthException;
import com.ibm.sbt.security.authentication.oauth.consumer.AccessToken;
import com.ibm.sbt.security.authentication.oauth.consumer.OAuth1Handler;
import com.ibm.sbt.security.authentication.oauth.consumer.OAuth2Handler;
import com.ibm.sbt.security.authentication.oauth.consumer.servlet.OAClientAuthentication;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.js.JSReference;
import com.ibm.sbt.util.SBTException;

/**
 * Bean that provides authentication via OAuth2.
 * <p>
 * </p>
 * 
 * @author Manish Kataria
 */

public class OAuth2Endpoint extends AbstractEndpoint {

	protected OAuth2Handler	oAuthHandler;

	public OAuth2Endpoint(){
		this.oAuthHandler = new OAuth2Handler();
	}
	
	protected OAuth2Endpoint(OAuth2Handler handler) {
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
		// Looks OK with C4?
		if (StringUtil.isEmpty(oAuthHandler.getConsumerKey())) {
			throw new SBTException(null, "The Endpoint consumer key is empty, class {0}", getClass());
		}
		if (StringUtil.isEmpty(oAuthHandler.getConsumerSecret())) {
			throw new SBTException(null, "The Endpoint consumer secret is empty, class {0}", getClass());
		}
		if (StringUtil.isEmpty(oAuthHandler.getAccessTokenURL())) {
			throw new SBTException(null, "The Endpoint access token URL is empty, class {0}", getClass());
		}
		if (StringUtil.isEmpty(oAuthHandler.getAuthorizationURL())) {
			throw new SBTException(null, "The Endpoint authorization URL is empty, class {0}", getClass());
		}
	}

	/*
	 * * @see com.ibm.sbt.services.endpoints.Endpoint#isAuthenticated() Checks in token store if we already
	 * have a token for this application id.
	 */
	@Override
	public boolean isAuthenticated() throws ClientServicesException {
		try {
			return oAuthHandler.acquireToken(false) != null;
		} catch (OAuthException ex) {
			throw new ClientServicesException(ex);
		}
	}

	@Override
	public void authenticate(boolean force) throws ClientServicesException {
		try {
			oAuthHandler.acquireToken(true, false);
		} catch (OAuthException ex) {
			throw new ClientServicesException(ex);
		}

	}

	@Override
	/**
	 * This gets called from Basic Proxy and decorates the HttpClient object with required security headers for OAuth2.0 implementation.
	 */
	public void initialize(DefaultHttpClient httpClient) throws ClientServicesException {
		try {
			AccessToken accesstoken = oAuthHandler.acquireToken(false);
			if (accesstoken != null) {
				HttpRequestInterceptor oauthInterceptor = new OAuth2Interceptor(accesstoken);
				httpClient.addRequestInterceptor(oauthInterceptor, 0);
			}
		} catch (OAuthException e) {}
	}

	private static class OAuth2Interceptor implements HttpRequestInterceptor {

		private final AccessToken	token;

		public OAuth2Interceptor(AccessToken token) {
			this.token = token;
		}

		@Override
		public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
			// Add Security headers
			try {
				if (null == token.getAccessToken()) {
					return;
				}
			} catch (Exception e) {
				Platform.getInstance().log(e);
				return;
			}
			
			// Remove any existing authorization headers which can cause oauth requests to fail
			if(request.containsHeader("authorization")){
				Header[] header = request.getHeaders("authorization");
				for (int i = 0; i < header.length; i++) {
					request.removeHeader(header[i]);
				}
			} 
			
			
			String authorizationheader = "Bearer " + token.getAccessToken();
			request.addHeader("Authorization", authorizationheader);
		}
	}

	// Methods to populate properties from endpoint definition in managedbean

	@Override
	public void setUrl(String url) {
		super.setUrl(url);
		// Make the URL the service name if not already set
		if (StringUtil.isEmpty(oAuthHandler.getServerUrl())) {
			oAuthHandler.setServerUrl(url);
		}
	}

	public void setAppId(String appId) {
		oAuthHandler.setAppId(appId);
	}

	public void setAuthorizationURL(String authorizationURL) {
		oAuthHandler.setAuthorizationURL(authorizationURL);
	}

	public void setAccessTokenURL(String accessTokenURL) {
		oAuthHandler.setAccessTokenURL(accessTokenURL);
	}

	public void setConsumerKey(String consumerKey) {
		oAuthHandler.setConsumerKey(consumerKey);
	}

	public void setConsumerSecret(String consumerSecret) {
		oAuthHandler.setConsumerSecret(consumerSecret);
	}

	public String getServiceName() {
		return oAuthHandler.getServiceName();
	}

	public void setServiceName(String serviceName) {
		oAuthHandler.setServiceName(serviceName);
	}

	public String getAppId() {
		return oAuthHandler.getAppId();
	}

	public String getAccessTokenURL() {
		return oAuthHandler.getAccessTokenURL();
	}

	public String getAuthorizationURL() {
		return oAuthHandler.getAuthorizationURL();
	}

	public String getConsumerKey() {
		return oAuthHandler.getConsumerKey();
	}

	public String getConsumerSecret() {
		return oAuthHandler.getConsumerSecret();
	}
	
	@Override
	public String getCredentialStore() {
		return oAuthHandler.getCredentialStore();
	}

	@Override
	public void setCredentialStore(String credentialStore) {
		oAuthHandler.setCredentialStore(credentialStore);
	}

	@Override
	public String getAuthType() {
		return "oauth2.0";
	}

	@Override
	public boolean isForceTrustSSLCertificate() {
		return oAuthHandler.isForceTrustSSLCertificate();
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
		return reference;
	}
	
	@Override
	public void logout() throws OAuthException {
		oAuthHandler.deleteToken();
	}
	
	public OAuth2Handler getHandler(){
		return oAuthHandler;
	}
	
	protected void setHandler(OAuth2Handler handler){
		this.oAuthHandler = handler;
	}

	
}
