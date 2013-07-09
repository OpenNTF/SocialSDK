/*
 * � Copyright IBM Corp. 2012
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
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import com.ibm.sbt.security.credential.store.CredentialStore;
import com.ibm.sbt.security.credential.store.CredentialStoreException;
import com.ibm.sbt.security.credential.store.CredentialStoreFactory;
import com.ibm.sbt.security.authentication.oauth.consumer.servlet.OACallback;
import com.ibm.sbt.security.authentication.oauth.consumer.util.OADance;
import com.ibm.sbt.service.util.ServiceUtil;
import com.ibm.sbt.services.util.AnonymousCredentialStore;
import com.ibm.sbt.util.SBTException;

/**
 * Encapsulate an OAuth service.
 * <p>
 * This holds both the application identifier and the URL to reach the service.
 * Not that it does not hold the token specific to a user.<br>
 * Such a service should be added as a bean, which can be application specific
 * or shared between all the applications.
 * </p>
 * 
 * @author Philippe Riand /////// TEMP //////// Sample values for Greenhouse
 *         (https://greenhouse.lotus.com/vulcan/security/provider/register.jsp?
 *         serviceProvider=vulcanToolkit) consumerKey:
 *         eecaba0d-136c-4677-a614-0b41612a2430 consumerSecret:
 *         IaL348_VDilHbgWSKm5Z64gvI0AIPWwObY0aVh1xKUx7m7VMrH65nnlxXfSU0vRzGqIjg8lHgP8etdlp8DuHGA
 *         requestTokenURL:
 *         https://greenhouse.lotus.com:443/vulcan/security/provider
 *         /requestToken authorizationURL:
 *         https://greenhouse.lotus.com:443/vulcan/security/provider/accessToken
 *         accessTokenURL:
 *         https://greenhouse.lotus.com:443/vulcan/security/provider/authorize
 */
public class OAProvider implements Serializable {

	private static final ProfilerType profilerLoadCredentialStore = new ProfilerType("OAuth: Load a token from the store"); //$NON-NLS-1$
	private static final ProfilerType profilerAcquireToken = new ProfilerType("OAuth: Acquire a token from the service"); //$NON-NLS-1$
	private static final ProfilerType profilerRenewToken = new ProfilerType("OAuth: Renew a token from the provider"); //$NON-NLS-1$
	private static final ProfilerType profilerDeleteToken = new ProfilerType("OAuth: Delete a token from the store"); //$NON-NLS-1$

	public static final int EXPIRE_THRESHOLD = 60; // 60sec = 1min
	// Store Type used to store the Tokens in the Credential Store
	public static final String ACCESS_TOKEN_STORE_TYPE = "OAUTH1_ACCESS_TOKEN_STORE";
	public static final String CONSUMER_TOKEN_STORE_TYPE = "OAUTH1_CONSUMER_TOKEN_STORE";
	
	// for logging
	private static final String sourceClass = OAProvider.class.getName();
	private static final Logger logger = Logger.getLogger(sourceClass);

	private static final long serialVersionUID = 1L;

	private boolean storeRead;
	private int expireThreshold;

	private String appId;
	private String serviceName;
	private String credentialStore;

	private String consumerKey;
	private String consumerSecret;
	private String requestTokenURL;
	private String authorizationURL;
	private String accessTokenURL;
	private String signatureMethod;
	private boolean forceTrustSSLCertificate;
	public String applicationAccessToken;

	
	public OAuthHandler oauthHandler = new OAuth1Handler(this);

	public OAProvider() {
		this.expireThreshold = EXPIRE_THRESHOLD;
	}

	public String getCredentialStore() {
		return credentialStore;
	}

	public void setCredentialStore(String credentialStore) {
		this.credentialStore = credentialStore;
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
			this.setOauthHandler(new HMACOAuth1Handler(this));
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
			try {
				CredentialStore factory = CredentialStoreFactory.getCredentialStore(getCredentialStore());
				if (factory != null) {
					ConsumerToken consumerToken = (ConsumerToken) factory.load(getServiceName(), CONSUMER_TOKEN_STORE_TYPE, null);
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
			} catch (CredentialStoreException cse) {
					throw new OAuthException(cse, cse.getMessage());
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
		// We do not automatically renew/acquire it - we just get it from the
		// store
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
		// We do not automatically renew/acquire it - we just get it from the
		// store
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
				setAccessToken(tk);
				if (shouldRenewToken(tk)) {
					return renewToken(tk);
				}
				return tk;
			}
		}

		// Ok, we should then play the OAuth dance if requested
		if (login) {
			// Here if we are forced to start an OAuth dance, we clear the Store from any existing tokens for this application and fetch new tokens.
			deleteToken();
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

		// Store the Oauth handler in session object
		context.getSessionMap().put(Configuration.OAUTH1_HANDLER, oauthHandler);
		context.getSessionMap().put("oaProvider", this);
		return new OADance(this, getAppId(), getServiceName(), userId, callback, initialPage);
	}

	protected AccessToken findTokenFromStore(Context context, String userId) throws OAuthException {
		if (Profiler.isEnabled()) {
			ProfilerAggregator agg = Profiler.startProfileBlock(profilerLoadCredentialStore, "");
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
		try {
			CredentialStore credStore = CredentialStoreFactory.getCredentialStore(getCredentialStore());
			if (credStore != null) {
				// Find the token for this user
	            AccessToken token = (AccessToken) credStore.load(getServiceName(), ACCESS_TOKEN_STORE_TYPE, userId);
	            if(token!=null) {
	                return token;
	            }
			}
		} catch (CredentialStoreException cse) {
				throw new OAuthException(cse, "Error finding credentials from the store");
		}
		return null;
	}

	private void setAccessToken(AccessToken token) {
		//Setting the access token and access token secret in Handler, when token is fetched from the store. 
		((OAuth1Handler)oauthHandler).setAccessToken(token.getAccessToken());
		((OAuth1Handler)oauthHandler).setAccessTokenSecret(token.getTokenSecret());
	}

	public CredentialStore findCredentialStore() throws OAuthException {
		CredentialStore credStore = null;
		try {
			credStore = CredentialStoreFactory.getCredentialStore(getCredentialStore());
		} catch (CredentialStoreException cse) {
			throw new OAuthException(cse, "Error finding credentials from the store");
		}
		return credStore;
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
		try {
			((OAuth1Handler) getOauthHandler()).getAccessTokenFromServer();

			token = createToken(getAppId(), getServiceName(), (OAuth1Handler) getOauthHandler(), token.getUserId());
			if (!Context.get().isCurrentUserAnonymous()) {
				CredentialStore credStore = findCredentialStore();
				if (credStore != null) {
					try {
						// if the token is already present, and was expired due to which we have fetched a new 
						// token, then we remove the token from the store first and then add this new token.
						deleteToken();
						credStore.store(getServiceName(), ACCESS_TOKEN_STORE_TYPE, context.getCurrentUserId(), token);
					} catch (CredentialStoreException cse) {
						throw new OAuthException(cse);
					}
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
		} catch (CredentialStoreException cse) {
			throw new OAuthException(cse, "Error trying to renew Token.");
		} catch (Exception e) {
			throw new OAuthException(e);
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
			try {
				CredentialStore credStore = CredentialStoreFactory.getCredentialStore(getCredentialStore());
				if (credStore != null) {
					// Find the token for this user
					credStore.remove(getServiceName(), ACCESS_TOKEN_STORE_TYPE, context.getCurrentUserId());
				}
			} catch (CredentialStoreException cse) {
				throw new OAuthException(cse, "Error trying to delete Token.");
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
