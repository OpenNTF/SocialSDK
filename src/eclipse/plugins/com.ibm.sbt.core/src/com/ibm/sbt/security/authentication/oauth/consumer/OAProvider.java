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

package com.ibm.sbt.security.authentication.oauth.consumer;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import com.ibm.commons.Platform;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.profiler.Profiler;
import com.ibm.commons.util.profiler.ProfilerAggregator;
import com.ibm.commons.util.profiler.ProfilerType;
import com.ibm.sbt.core.configuration.Configuration;
import com.ibm.sbt.security.authentication.oauth.OAuthException;
import com.ibm.sbt.security.authentication.oauth.consumer.servlet.OACallback;
import com.ibm.sbt.security.authentication.oauth.consumer.util.OADance;
import com.ibm.sbt.security.authentication.oauth.consumer.store.OATokenStoreFactory;
import com.ibm.sbt.security.authentication.oauth.consumer.store.TokenStore;
import com.ibm.sbt.service.util.ServiceUtil;
import com.ibm.sbt.services.util.AnonymousCredentialStore;
import com.ibm.sbt.util.SBTException;

/**
 * Encapsulate an OAuth service.
 * <p>
 * This holds both the application identifier and the URL to reach the service. Not that it does not hold the
 * token specific to a user.<br>
 * Such a service should be added as a bean, which can be application specific or shared between all the
 * applications.
 * </p>
 * 
 * @author Philippe Riand /////// TEMP //////// Sample values for Greenhouse
 *         (https://greenhouse.lotus.com/vulcan/security/provider/register.jsp?serviceProvider=vulcanToolkit)
 *         consumerKey: eecaba0d-136c-4677-a614-0b41612a2430 consumerSecret:
 *         IaL348_VDilHbgWSKm5Z64gvI0AIPWwObY0aVh1xKUx7m7VMrH65nnlxXfSU0vRzGqIjg8lHgP8etdlp8DuHGA
 *         requestTokenURL: https://greenhouse.lotus.com:443/vulcan/security/provider/requestToken
 *         authorizationURL: https://greenhouse.lotus.com:443/vulcan/security/provider/accessToken
 *         accessTokenURL: https://greenhouse.lotus.com:443/vulcan/security/provider/authorize
 */
public class OAProvider implements Serializable {

	private static final ProfilerType	profilerLoadTokenStore	= new ProfilerType(
																		"OAuth: Load a token from the store");		//$NON-NLS-1$
	private static final ProfilerType	profilerAcquireToken	= new ProfilerType(
																		"OAuth: Acquire a token from the service"); //$NON-NLS-1$
	private static final ProfilerType	profilerRenewToken		= new ProfilerType(
																		"OAuth: Renew a token from the provider");	//$NON-NLS-1$
	private static final ProfilerType	profilerDeleteToken		= new ProfilerType(
																		"OAuth: Delete a token from the store");	//$NON-NLS-1$

	public static final int				EXPIRE_THRESHOLD		= 60;												// 60sec
																													// =
																													// 1min
	private static final long			serialVersionUID		= 1L;

	private boolean						storeRead;
	private int							expireThreshold;

	private String						appId;
	private String						serviceName;
	private String						tokenStore;

	private String						consumerKey;
	private String						consumerSecret;
	private String						requestTokenURL;
	private String						authorizationURL;
	private String						accessTokenURL;
	private String						signatureMethod;
	private boolean						forceTrustSSLCertificate;
	public String						applicationAccessToken;

	public OAuthHandler					oauthHandler			= new OAuth1Handler();

	public OAProvider() {
		this.expireThreshold = EXPIRE_THRESHOLD;
	}

	public String getTokenStore() {
		return tokenStore;
	}

	public void setTokenStore(String tokenStore) {
		this.tokenStore = tokenStore;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	public String getRequestTokenURL() {
		return requestTokenURL;
	}

	public void setRequestTokenURL(String requestTokenURL) {
		this.requestTokenURL = requestTokenURL;
	}

	public String getAuthorizationURL() {
		return authorizationURL;
	}

	public void setAuthorizationURL(String authorizationURL) {
		this.authorizationURL = authorizationURL;
	}

	public String getAccessTokenURL() {
		return accessTokenURL;
	}

	public void setAccessTokenURL(String accessTokenURL) {
		this.accessTokenURL = accessTokenURL;
	}

	public OAuthHandler getOauthHandler() {
		return oauthHandler;
	}

	public void setOauthHandler(OAuthHandler oauthHandler) {
		this.oauthHandler = oauthHandler;
	}

	public String getSignatureMethod() {
		return signatureMethod;
	}

	public void setSignatureMethod(String signatureMethod) {
		this.signatureMethod = signatureMethod;

		// If HMAC is used, change the handler
		if (StringUtil.equalsIgnoreCase(Configuration.HMAC_SIGNATURE, signatureMethod)) {
			this.setOauthHandler(new HMACOAuth1Handler());
		}
	}

	public int getExpireThreshold() {
		return expireThreshold;
	}

	public void setExpireThreshold(int expireThreshold) {
		this.expireThreshold = expireThreshold;
	}

	public boolean getForceTrustSSLCertificate() {
		return forceTrustSSLCertificate;
	}

	public void setForceTrustSSLCertificate(boolean forceTrustSSLCertificate) {
		this.forceTrustSSLCertificate = forceTrustSSLCertificate;
	}

	public String getApplicationAccessToken() {
		return applicationAccessToken;
	}

	public void setApplicationAccessToken(String applicationAccessToken) {
		this.applicationAccessToken = applicationAccessToken;
	}

	private void readConsumerToken() throws OAuthException {
		if (!storeRead) {
			TokenStore factory = OATokenStoreFactory.getTokenStore(getTokenStore());
			if (factory != null) {
				ConsumerToken consumerToken = factory.loadConsumerToken(getAppId(), getServiceName());
				if (consumerToken != null) {
					storeRead = true;
					if (StringUtil.isNotEmpty(consumerToken.getConsumerKey())) {
						setConsumerKey(consumerToken.getConsumerKey());
					}
					if (StringUtil.isNotEmpty(consumerToken.getConsumerSecret())) {
						setConsumerSecret(consumerToken.getConsumerSecret());
					}
					if (StringUtil.isNotEmpty(consumerToken.getRequestTokenUri())) {
						setRequestTokenURL(consumerToken.getRequestTokenUri());
					}
					if (StringUtil.isNotEmpty(consumerToken.getAuthorizationUri())) {
						setAuthorizationURL(consumerToken.getAuthorizationUri());
					}
					if (StringUtil.isNotEmpty(consumerToken.getAccessTokenUri())) {
						setAccessTokenURL(consumerToken.getAccessTokenUri());
					}
					if (StringUtil.isNotEmpty(consumerToken.getSignatureMethod())) {
						setSignatureMethod(consumerToken.getSignatureMethod());
					}
				}
			}
		}
	}

	// ==========================================================
	// Token management
	// ==========================================================

	public boolean isTokenExpired() throws OAuthException {
		return isTokenExpired(null);
	}

	public boolean isTokenExpired(AccessToken token) throws OAuthException {
		// We do not automatically renew/acquire it - we just get it from the store
		if (token == null) {
			token = _findTokenFromStore(Context.get(), null);
			if (token == null) {
				throw new OAuthException(null, "No user token is available");
			}
		}
		return token.isExpired();
	}

	public boolean shouldRenewToken() throws OAuthException {
		return shouldRenewToken(null);
	}

	public boolean shouldRenewToken(AccessToken token) throws OAuthException {
		// We do not automatically renew/acquire it - we just get it from the store
		if (token == null) {
			token = _findTokenFromStore(Context.get(), null);
			if (token == null) {
				throw new OAuthException(null, "No user token is available");
			}
		}
		return token.isExpired(getExpireThreshold());
	}

	public AccessToken acquireToken() throws OAuthException {
		return acquireToken(false);
	}

	public AccessToken acquireToken(boolean login) throws OAuthException {
		return acquireToken(login, false);
	}

	public AccessToken acquireToken(boolean login, boolean force) throws OAuthException {
		if (Profiler.isEnabled()) {
			ProfilerAggregator agg = Profiler.startProfileBlock(profilerAcquireToken, "");
			long ts = Profiler.getCurrentTime();
			try {
				return _acquireToken(login, force);
			} finally {
				Profiler.endProfileBlock(agg, ts);
			}
		} else {
			return _acquireToken(login, force);
		}
	}

	public AccessToken _acquireToken(boolean login, boolean force) throws OAuthException {
		Context context = Context.get();

		// If force is used, then login must be requested
		if (force) {
			login = true;
		}

		String userId = context.getCurrentUserId();

		// Look for a token in the store
		// If the user is anonymous, then the token might had been stored in the session
		if (!force) {
			AccessToken tk = context.isCurrentUserAnonymous() ? (AccessToken) AnonymousCredentialStore
					.loadCredentials(context, getAppId(), getServiceName()) : findTokenFromStore(context,
					userId);
			if (tk != null) {
				if (shouldRenewToken(tk)) {
					return renewToken(tk);
				}
				return tk;
			}
		}

		// Ok, we should then play the OAuth dance if requested
		if (login) {
			OADance oauth = createOAuthDance(context, userId);
			try {
				// This sends a signal
				oauth.perform3LegsDance(context);
			} catch (Exception ex) {
				throw new SBTException(ex, "Error while acquiring OAuth token");
			}
		}

		return null;
	}

	protected String getApplicationPage(Context context) throws OAuthException {
		// We just return to the same page
		Object _req = context.getHttpRequest();
		if (_req instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) _req;
			String url = UrlUtil.getRequestUrl(request);
			return url;
		}
		return null;
	}

	public String getCallbackUrl(Context context) throws OAuthException {
		Object _req = context.getHttpRequest();
		if (_req instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) _req;
			String proxyBaseUrl = PathUtil.concat(ServiceUtil.getProxyUrl(request), OACallback.URL_PATH, '/');
			return proxyBaseUrl;
		}
		return null;
	}

	protected OADance createOAuthDance(Context context, String userId) throws OAuthException {
		String callback = getCallbackUrl(context);
		String initialPage = getApplicationPage(context);

		// OAuthHandler oauthHandler = new OAuth1Handler();

		// Store the Oauth handler in session object
		context.getSessionMap().put(Configuration.OAUTH_HANDLER, oauthHandler);
		context.getSessionMap().put("oaProvider", this);
		return new OADance(this, getAppId(), getServiceName(), userId, callback, initialPage);
	}

	protected AccessToken findTokenFromStore(Context context, String userId) throws OAuthException {
		if (Profiler.isEnabled()) {
			ProfilerAggregator agg = Profiler.startProfileBlock(profilerLoadTokenStore, "");
			long ts = Profiler.getCurrentTime();
			try {
				return _findTokenFromStore(context, userId);
			} finally {
				Profiler.endProfileBlock(agg, ts);
			}
		} else {
			return _findTokenFromStore(context, userId);
		}
	}

	protected AccessToken _findTokenFromStore(Context context, String userId) throws OAuthException {
		readConsumerToken();

		if (StringUtil.isEmpty(userId)) {
			userId = context.getCurrentUserId();
			// Anonymous is not valid
			if (StringUtil.isEmpty(userId)) {
				return null;
			}
		}
		TokenStore ts = OATokenStoreFactory.getTokenStore(getTokenStore());
		if (ts != null) {
			// Find the token for this user
			AccessToken tk = ts.loadAccessToken(getAppId(), getServiceName(), getConsumerKey(), userId);
			if (tk != null) {
				return tk;
			}
		}

		return null;
	}

	public TokenStore findTokenStore() throws OAuthException {
		TokenStore ts = OATokenStoreFactory.getTokenStore(getTokenStore());
		return ts;
	}

	// ==========================================================
	// Renew the token
	// ==========================================================

	public AccessToken renewToken() throws OAuthException {
		return renewToken(null);
	}

	public AccessToken renewToken(AccessToken token) throws OAuthException {
		if (Profiler.isEnabled()) {
			ProfilerAggregator agg = Profiler.startProfileBlock(profilerRenewToken, "");
			long ts = Profiler.getCurrentTime();
			try {
				return _renewToken(token);
			} finally {
				Profiler.endProfileBlock(agg, ts);
			}
		} else {
			return _renewToken(token);
		}
	}

	protected AccessToken _renewToken(AccessToken token) throws OAuthException {
		readConsumerToken();
		if (token == null) {
			token = acquireToken();
			if (token == null) {
				throw new OAuthException(null, "No user token is available");
			}
		}
		Context context = Context.get();
		OAuth1Handler oAuthHandler = (OAuth1Handler) context.getSessionMap().get(Configuration.OAUTH_HANDLER);
		try {
			oAuthHandler.getAccessTokenFromServer();

			token = createToken(getAppId(), getServiceName(), oAuthHandler, token.getUserId());
			if (!Context.get().isCurrentUserAnonymous()) {
				TokenStore ts = findTokenStore();
				if (ts != null) {
					ts.saveAccessToken(token);
				}
			} else {
				AnonymousCredentialStore.storeCredentials(Context.get(), token, getAppId(), getServiceName());
			}
			//
		} catch (IOException e) {
			throw new OAuthException(e);
		} catch (URISyntaxException e) {
			throw new OAuthException(e);
		} catch (OAuthException e) {
			// We cannot renew the token, so we ask for a brand new one...
			Platform.getInstance().log(e);
			acquireToken(true, true);
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return token;
	}

	// ==========================================================
	// Delete the token
	// ==========================================================

	public void deleteToken() throws OAuthException {
		if (Profiler.isEnabled()) {
			ProfilerAggregator agg = Profiler.startProfileBlock(profilerDeleteToken, "");
			long ts = Profiler.getCurrentTime();
			try {
				_deleteToken(Context.get(), null);
			} finally {
				Profiler.endProfileBlock(agg, ts);
			}
		} else {
			_deleteToken(Context.get(), null);
		}
	}

	protected void _deleteToken(Context context, String userId) throws OAuthException {
		readConsumerToken();

		if (StringUtil.isEmpty(userId)) {
			userId = context.getCurrentUserId();
			// Anonymous is not valid
			if (StringUtil.isEmpty(userId)) {
				return;
			}

		}
		if (StringUtil.equals(userId, "anonymous")) {
			AnonymousCredentialStore.deleteCredentials(context, getAppId(), getServiceName());
		} else {
			TokenStore ts = OATokenStoreFactory.getTokenStore(getTokenStore());
			if (ts != null) {
				// Find the token for this user
				ts.deleteAccessToken(getAppId(), getServiceName(), getConsumerKey(), null, null, userId);
			}
		}
	}

	// ==========================================================
	// Utilities
	// ==========================================================
	/**
	 * @param appId
	 * @param serviceName
	 * @param handler
	 * @param userId
	 * @return
	 * @throws OAuthException
	 */
	public AccessToken createToken(String appId, String serviceName, OAuth1Handler handler, String userId)
			throws OAuthException {
		long now = System.currentTimeMillis();
		String expirationinterval = handler.getExpiresIn();
		Date expiresIn = null;
		if (StringUtil.isNotEmpty(expirationinterval)) {
			expiresIn = new Date(now + Long.parseLong(expirationinterval) * 1000);
		}
		Date authorizationExpiresIn = null;
		String oauth_authorization_expires_in = handler.getAuthorizationExpiresIn();
		if (StringUtil.isNotEmpty(oauth_authorization_expires_in)) {
			authorizationExpiresIn = new Date(now + Long.parseLong(oauth_authorization_expires_in) * 1000);
		}

		return new AccessToken(appId, serviceName, getConsumerKey(), handler.getAccessToken(),
				handler.getAccessTokenSecret(), userId, expiresIn, authorizationExpiresIn,
				handler.getOauth_session_handle()

		);
	}
}
