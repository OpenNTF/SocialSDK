/****************************************************************************************
 * Copyright 2012 IBM Corp.                                                                   *
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.ibm.commons.Platform;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.StreamUtil;
import com.ibm.commons.util.profiler.Profiler;
import com.ibm.commons.util.profiler.ProfilerAggregator;
import com.ibm.commons.util.profiler.ProfilerType;
import com.ibm.sbt.core.configuration.Configuration;
import com.ibm.sbt.plugin.SbtCoreLogger;
import com.ibm.sbt.security.authentication.oauth.OAuthException;
import com.ibm.sbt.security.credential.store.CredentialStore;
import com.ibm.sbt.security.credential.store.CredentialStoreException;
import com.ibm.sbt.security.credential.store.CredentialStoreFactory;
import com.ibm.sbt.services.util.AnonymousCredentialStore;
import com.ibm.sbt.services.util.SSLUtil;

/**
 * @author Vimal Dhupar
 * @author Manish Kataria
 */

public class OAuth1Handler extends OAuthHandler implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final ProfilerType profilerLoadCredentialStore = new ProfilerType("OAuth: Load a token from the store"); //$NON-NLS-1$
	private static final ProfilerType profilerAcquireToken = new ProfilerType("OAuth: Acquire a token from the service"); //$NON-NLS-1$
	private static final ProfilerType profilerRenewToken = new ProfilerType("OAuth: Renew a token from the provider"); //$NON-NLS-1$
	private static final ProfilerType profilerDeleteToken = new ProfilerType("OAuth: Delete a token from the store"); //$NON-NLS-1$
	private static final ProfilerType profilerReadTempToken = new ProfilerType("OAuth: Acquire a temporary token from the provider"); //$NON-NLS-1$
	private static final ProfilerType profilerReadToken = new ProfilerType("OAuth: Read a token with verifier from the provider"); //$NON-NLS-1$

	private static final int 				BYTE_ARRAY_SIZE				= ((23 * 5) / 8) + 1;
	private static final SecureRandom		SECURE_RANDOM				= new SecureRandom();
	public static final int 				EXPIRE_THRESHOLD 			= 60; // 60sec = 1min
	public static final String 				ACCESS_TOKEN_STORE_TYPE 	= "OAUTH1_ACCESS_TOKEN_STORE";
	public static final String 				CONSUMER_TOKEN_STORE_TYPE 	= "OAUTH1_CONSUMER_TOKEN_STORE";
	
	protected String		OAuthCallbackConfirmed;
	protected String		requestToken;
	private String			requestTokenSecret;
	protected String		verifierCode;
	private String			accessTokenSecret;
	private String			accessToken;
	private String			expiresIn;
	private String			authorizationExpiresIn;
	private String			oauth_session_handle;
	private int 			expireThreshold;
	private String 			applicationPage;
	private boolean 		storeRead;
	protected String 			appId;
	protected String 			serviceName;
	protected String 			credentialStore;
	protected String 			consumerKey;
	protected String 			consumerSecret;
	protected String 			requestTokenURL;
	protected String 			authorizationURL;
	protected String 			accessTokenURL;
	protected String 			signatureMethod;
	protected boolean			forceTrustSSLCertificate;
	protected AccessToken 		accessTokenObject;
	
	public OAuth1Handler() {
		this.expireThreshold = EXPIRE_THRESHOLD;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getAuthorizationExpiresIn() {
		return authorizationExpiresIn;
	}

	public void setAuthorizationExpiresIn(String authorizationExpiresIn) {
		this.authorizationExpiresIn = authorizationExpiresIn;
	}

	/**
	 * Get a Request token from the resource server by sending a HTTP Post call
	 * 
	 * Http call has the following data format:
	 * POST /manage/oauth/getRequestToken HTTP/1.1
	 * Host: lotuslive
	 * Authorization: OAuth
	 *                oauth_consumer_key="<con key>",
	 *                oauth_signature="<signature>"
	 *                oauth_signature_method="PLAINTEXT",
	 *                oauth_timestamp="<timestamp>",
	 *                oauth_nonce="<nonce>",
	 *                oauth_version="1.0",
	 *                oauth_callback="<callback url>"
	 *   
	 * @throws Exception
	 */
	public void getRequestTokenFromServer() throws Exception {
		HttpGet method = null;
		int responseCode = HttpStatus.SC_OK;
		String responseBody = null;
		InputStream content = null;
		Context context = Context.get();
		try {
			HttpClient client = new DefaultHttpClient();
			if (getForceTrustSSLCertificate()) {
				client = SSLUtil.wrapHttpClient((DefaultHttpClient) client);
			}
			StringBuilder url = new StringBuilder();
			url.append(getRequestTokenURL()).append("?");
			url.append(Configuration.CONSUMER_KEY).append('=')
			.append(URLEncoder.encode(getConsumerKey(), "UTF-8")).append('&');
			url.append(Configuration.SIGNATURE).append('=')
			.append(URLEncoder.encode(buildSignature(getConsumerSecret(), ""), "UTF-8"))
			.append('&');
			url.append(Configuration.SIGNATURE_METHOD).append('=')
			.append(URLEncoder.encode(Configuration.PLAINTEXT_SIGNATURE, "UTF-8")).append('&');
			url.append(Configuration.TIMESTAMP).append('=')
			.append(URLEncoder.encode(getTimestamp(), "UTF-8")).append('&');
			url.append(Configuration.NONCE).append('=').append(URLEncoder.encode(getNonce(), "UTF-8"))
			.append('&');
			url.append(Configuration.VERSION).append('=')
			.append(URLEncoder.encode(Configuration.OAUTH_VERSION1, "UTF-8")).append('&');
			url.append(Configuration.CALLBACK).append('=')
			.append(URLEncoder.encode(getCallbackUrl(context), "UTF-8"));
			method = new HttpGet(url.toString());

			HttpResponse httpResponse = client.execute(method);
			responseCode = httpResponse.getStatusLine().getStatusCode();
			content = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			try {
				responseBody = StreamUtil.readString(reader);
			} finally {
				StreamUtil.close(reader);
			}
		} catch (Exception e) {
			throw new OAuthException(e, "Internal error - getRequestToken failed Exception: <br>");
		} finally {
			if(content!=null) {
				content.close();
			}
		}
		if (responseCode != HttpStatus.SC_OK) {
			String exceptionDetail = buildErrorMessage(responseCode, responseBody);
			if (exceptionDetail != null) {
				throw new OAuthException(null,
						"OAuth1Handler.java : getRequestToken failed. " + exceptionDetail);
			}
		} else {
			setRequestToken(getTokenValue(responseBody, Configuration.OAUTH_TOKEN));
			setRequestTokenSecret(getTokenValue(responseBody, Configuration.OAUTH_TOKEN_SECRET));
			getTokenValue(responseBody, Configuration.OAUTH1_ISSUEDON);
			setExpiresIn(getTokenValue(responseBody, Configuration.OAUTH1_EXPIRESIN));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.security.authentication.oauth.consumer.OAuthHandler#getAccessTokenFromServer()
	 */
	public void getAccessTokenFromServer() throws Exception {
		HttpGet method = null;
		int responseCode = HttpStatus.SC_OK;
		String responseBody = null;
		try {
			HttpClient client = new DefaultHttpClient();
			if (getForceTrustSSLCertificate()) {
				client = SSLUtil.wrapHttpClient((DefaultHttpClient) client);
			}
			StringBuilder url = new StringBuilder();
			url.append(getAccessTokenURL()).append("?");
			url.append(Configuration.CONSUMER_KEY).append('=')
			.append(URLEncoder.encode(getConsumerKey(), "UTF-8")).append('&');
			url.append(Configuration.OAUTH_TOKEN).append('=')
			.append(URLEncoder.encode(requestToken, "UTF-8")).append('&');
			// services like dropbox do not use verifier code, adding a check
			if(verifierCode!=null){
				url.append(Configuration.OAUTH_VERIFIER).append('=')
				.append(URLEncoder.encode(verifierCode, "UTF-8")).append('&');
			}
			url.append(Configuration.SIGNATURE)
			.append('=')
			.append(URLEncoder.encode(
					buildSignature(getConsumerSecret(), requestTokenSecret), "UTF-8"))
					.append('&');
			url.append(Configuration.SIGNATURE_METHOD).append('=')
			.append(URLEncoder.encode(Configuration.PLAINTEXT_SIGNATURE, "UTF-8")).append('&');
			url.append(Configuration.TIMESTAMP).append('=')
			.append(URLEncoder.encode(getTimestamp(), "UTF-8")).append('&');
			url.append(Configuration.NONCE).append('=').append(URLEncoder.encode(getNonce(), "UTF-8"))
			.append('&');
			url.append(Configuration.VERSION).append('=')
			.append(URLEncoder.encode(Configuration.OAUTH_VERSION1, "UTF-8"));
			method = new HttpGet(url.toString());

			HttpResponse httpResponse = client.execute(method);
			responseCode = httpResponse.getStatusLine().getStatusCode();
			InputStream content = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			try {
				responseBody = StreamUtil.readString(reader);
			} finally {
				StreamUtil.close(reader);
			}
		} catch (Exception e) {
			Platform.getInstance().log(e);
			throw new OAuthException(e, "Internal error - getAccessToken failed Exception: <br>" + e);
		}
		if (responseCode != HttpStatus.SC_OK) {
			String exceptionDetail = buildErrorMessage(responseCode, responseBody);
			if (exceptionDetail != null) {
				throw new OAuthException(null,
						"OAuth1Handler.java : getAccessToken failed. " + exceptionDetail);
			}
		} else {
			setAccessToken(getTokenValue(responseBody, Configuration.OAUTH_TOKEN));
			setAccessTokenSecret(getTokenValue(responseBody, Configuration.OAUTH_TOKEN_SECRET));
			getTokenValue(responseBody, Configuration.OAUTH1_ISSUEDON);
			setExpiresIn(getTokenValue(responseBody, Configuration.OAUTH1_EXPIRESIN));
		}
	}

	public String getAccessTokenURL() {
		if (getServerUrl() != null) {
			return getServerUrl() + accessTokenURL;
		} else {
			return accessTokenURL;
		}
	}

	public String getRequestTokenURL() {
		if (getServerUrl() != null) {
			return getServerUrl() + requestTokenURL;
		} else {
			return requestTokenURL;
		}

	}

	public String getNonce() {
		byte[] genNonceBytes = new byte[BYTE_ARRAY_SIZE];
		SECURE_RANDOM.nextBytes(genNonceBytes);
		// use URL-friendly Base64 encoding
		return (Base64Url.encode(genNonceBytes));
	}

	private String buildSignature(String consumerSecret, String tokenSecret) {
		return consumerSecret + "&" + tokenSecret;
	}

	public String getTimestamp() {
		long ts = System.currentTimeMillis() / 1000;
		return (String.valueOf(ts));
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.security.authentication.oauth.consumer.OAuthHandler#createAuthorizationHeader()
	 */
	@Override
	public String createAuthorizationHeader() {
		StringBuilder authHdr = new StringBuilder(1024);
		authHdr.append("OAuth ");
		authHdr.append(Configuration.CONSUMER_KEY).append("=\"").append(percentEncode(getConsumerKey())).append("\",");
		authHdr.append(Configuration.OAUTH_TOKEN).append("=\"").append(percentEncode(accessToken)).append("\",");
		authHdr.append(Configuration.SIGNATURE_METHOD).append("=\"").append(Configuration.PLAINTEXT_SIGNATURE).append("\",");
		authHdr.append(Configuration.SIGNATURE).append("=\"").append(percentEncode(buildSignature(getConsumerSecret(), getAccessTokenSecret()))).append("\",");
		authHdr.append(Configuration.TIMESTAMP).append("=\"").append(percentEncode(getTimestamp())).append("\",");
		authHdr.append(Configuration.NONCE).append("=\"").append(percentEncode(getNonce())).append("\",");
		authHdr.append(Configuration.VERSION).append("=\"").append(Configuration.OAUTH_VERSION1).append("\"");
		return authHdr.toString();
	}

	public String getRequestTokenSecret() {
		return requestTokenSecret;
	}

	public String getVerifierCode() {
		return verifierCode;
	}

	public void setVerifierCode(String verifierCode) {
		this.verifierCode = verifierCode;
	}

	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}

	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}

	public void setRequestTokenSecret(String requestTokenSecret) {
		this.requestTokenSecret = requestTokenSecret;
	}

	public String getOAuthCallbackConfirmed() {
		return OAuthCallbackConfirmed;
	}

	public void setOAuthCallbackConfirmed(String oAuthCallbackConfirmed) {
		OAuthCallbackConfirmed = oAuthCallbackConfirmed;
	}

	public void setAccessTokenSecret(String accessTokenSecret) {
		this.accessTokenSecret = accessTokenSecret;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getOauth_session_handle() {
		return oauth_session_handle;
	}

	public void setOauth_session_handle(String oauth_session_handle) {
		this.oauth_session_handle = oauth_session_handle;
	}

	@Override
	public String getAccessToken() {
		return accessToken;
	}

	public String getRequestToken() {
		return requestToken;
	}
	
	public AccessToken getAccessTokenObject() {
		return accessTokenObject;
	}

	public void setAccessTokenObject(AccessToken accessTokenObject) {
		this.accessTokenObject = accessTokenObject;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.security.authentication.oauth.consumer.OAuthHandler#doPreAuthorizationFlow()
	 */
	@Override
	public void doPreAuthorizationFlow() throws Exception {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.security.authentication.oauth.consumer.OAuthHandler#doPostAuthorizationFlow()
	 */
	@Override
	public void doPostAuthorizationFlow() throws Exception {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.security.authentication.oauth.consumer.OAuthHandler#isInitialized()
	 */
	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.security.authentication.oauth.consumer.OAuthHandler#getAuthType()
	 */
	@Override
	public String getAuthType() {
		return Configuration.AUTH_TYPE_OAUTH1;
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

	public void setRequestTokenURL(String requestTokenURL) {
		this.requestTokenURL = requestTokenURL;
	}

	@Override
	public String getAuthorizationURL() {
		return authorizationURL;
	}

	public void setAuthorizationURL(String authorizationURL) {
		this.authorizationURL = authorizationURL;
	}

	public void setAccessTokenURL(String accessTokenURL) {
		this.accessTokenURL = accessTokenURL;
	}

	public String getSignatureMethod() {
		return signatureMethod;
	}

	public void setSignatureMethod(String signatureMethod) {
		this.signatureMethod = signatureMethod;
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
    	AccessToken tk;

		// If force is used, then login must be requested
		if (force) {
			login = true;
		}

		String userId = getUserId();

		// Look for a token in the store
		// If the user is anonymous, then the token might had been stored in the session
		if (!force) {
			if (getAccessTokenObject() != null) {
            	// if cred store is not defined in end point return from bean
            	tk = getAccessTokenObject();
            }else{
			 tk = context.isCurrentUserAnonymous() ? (AccessToken) AnonymousCredentialStore
					.loadCredentials(context, getAppId(), getServiceName()) : findTokenFromStore(context,
							userId);
            }
					
			// check if token needs to be renewed
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
			setAccessTokenObject(null);
			setApplicationPage(getApplicationPage(context));
			try {
				// This sends a signal
				performOAuth1Dance();
			} catch (Exception ex) {
				throw new OAuthException(ex, "Error while acquiring OAuth token");
			}
		}

		return null;
	}

	/**
	 * Request a temporary token for the application and redirect to a callback
	 * page.
	 */
	public synchronized void performOAuth1Dance() throws OAuthException {
		if (Profiler.isEnabled()) {
			ProfilerAggregator agg = Profiler.startProfileBlock(
					profilerReadTempToken, "");
			long ts = Profiler.getCurrentTime();
			try {
				_performOAuth1Dance();
			} finally {
				Profiler.endProfileBlock(agg, ts);
			}
		} else {
			_performOAuth1Dance();
		}
	}
	protected synchronized void _performOAuth1Dance() throws OAuthException {
		try {
			// Call the OAuth1Handler's method to get the Request token by
			// making network call.
			getRequestTokenFromServer();
			String redirectUrl = getAuthorizationURL() + "?" + OAConstants.OAUTH_TOKEN + "=" + getRequestToken();
			// tbd: is there a better way to handle this DropboxFiles specific parameter?
			if (redirectUrl.contains("dropbox")) {
				redirectUrl = redirectUrl + "&" + OAConstants.OAUTH_CALLBACK
				+ "=" + getCallbackUrl(Context.get());
			}

			// Look if there are paththough parameters
			String pass = (String) Context.get().getRequestParameterMap().get("oaredirect");
			if (StringUtil.isNotEmpty(pass)) {
				// Commenting this logic of modification of URL, as this creates
				// a URL for OAuth1.0 which includes unsupported parameters, and
				// error is thrown by Smartcloud.
				// Testing this removal of code, to check if it breaks anything.
				// Preliminary testing results in OK status(things work without
				// this code).

				// redirectUrl = redirectUrl + "&oaredirect=" +
				// URLEncoder.encode(pass,"utf-8");
			}
			Context.get().getSessionMap().put(Configuration.OAUTH1_HANDLER, this);
			// URL redirection
			Context.get().sendRedirect(redirectUrl);
			// throw new RedirectSignal();
			// throw new RedirectSignal(redirectUrl);
		} catch (IOException e) {
			throwOAuthException(e, "_performOAuth1Dance", "Failed to get request token.");
		} catch (OAuthException e) {
			throwOAuthException(e, "_performOAuth1Dance", "Failed to get request token.");
		} catch (URISyntaxException e) {
			throwOAuthException(e, "_performOAuth1Dance", "Failed to get request token.");
		} catch (Exception e) {
			throwOAuthException(e, "_performOAuth1Dance", "Failed to get request token.");
		}
	}

	private void throwOAuthException(Exception e, String method, String message) throws OAuthException {
		String callback = null;
		String secret = null;
		String key = null;
		String requestUrl = null;
		String authorizeUrl = null;
		String accessUrl = null;
		boolean shortKey = false;
		boolean shortSecret = false;
		Context context = Context.get();
		//		if (oaProvider != null) {
		callback = getCallbackUrl(context);
		secret = getConsumerSecret();
		if (StringUtil.isNotEmpty(secret)) {
			int pre = 0;
			if (secret.length() > 12) {
				pre = 4;
			} else if (secret.length() > 9) {
				pre = 3;
			} else {
				shortSecret = true;
				if (secret.length() > 6) {
					pre = 2;
				} else {
					secret = StringUtil
					.format("secret is too short to display, {0} characters long",
							secret.length());
				}
			}
			if (pre >= 2) {
				String tmp = secret.substring(0, pre);
				tmp = tmp + "....";
				tmp = tmp + secret.substring(secret.length() - pre);
				secret = tmp;
			}
		}
		key = getConsumerKey();
		if (StringUtil.isNotEmpty(key)) {
			int pre = 0;
			if (key.length() > 12) {
				pre = 4;
			} else if (key.length() > 9) {
				pre = 3;
			} else {
				shortKey = true;
				if (key.length() > 6) {
					pre = 2;
				} else {
					key = StringUtil
					.format("key is too short to display, {0} characters long",
							key.length());
				}
			}
			if (pre >= 2) {
				String tmp = key.substring(0, pre);
				tmp = tmp + "....";
				tmp = tmp + key.substring(key.length() - pre);
				key = tmp;
			}
		}
		requestUrl = getRequestTokenURL();
		authorizeUrl = getAuthorizationURL();
		accessUrl = getAccessTokenURL();
		//		}

		String formattedString = StringUtil
		.format(" requestUrl:{0}, authorizeUrl: {1}, accessUrl: {2}, callback: {3}, truncated key:{4}, truncated secret:{5}.",
				requestUrl, authorizeUrl, accessUrl, callback, key,
				secret);
		StringBuffer extraInfo = new StringBuffer(" ");
		if (StringUtil.isEmpty(requestUrl)) {
			extraInfo
			.append("OAuth requestUrl is empty, please check your application definition in the the web security store is correct.\n");
		}
		if (StringUtil.isEmpty(authorizeUrl)) {
			extraInfo
			.append("OAuth authorizeUrl is empty, please check your application definition in the the web security store is correct.\n");
		}
		if (StringUtil.isEmpty(accessUrl)) {
			extraInfo
			.append("OAuth accessUrl is empty, please check your application definition in the the web security store is correct.\n");
		}
		if (StringUtil.isEmpty(callback)) {
			extraInfo
			.append("OAuth callback is empty, please check with your application vendor to ensure a callback is not required.\n");
		}
		if (shortKey) {
			extraInfo
			.append("The value supplied for the OAuth user key is short (less than 9 characters), please ensure it has been entered correctly.\n");
		}
		if (shortSecret) {
			extraInfo
			.append("The value supplied for the OAuth user secret is short (less than 9 characters) please ensure it has been entered correctly.\n");
		}
		if (SbtCoreLogger.SBT.isErrorEnabled()) {
			SbtCoreLogger.SBT.errorp(this, method, e, message + formattedString
					+ extraInfo.toString());
		}
		throw new OAuthException(e, message + formattedString
				+ extraInfo.toString());
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
			userId = getUserId();
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
		setAccessToken(token.getAccessToken());
		setAccessTokenSecret(token.getTokenSecret());
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
			getAccessTokenFromServer();

			token = createToken(getAppId(), getServiceName(), this, token.getUserId());
			setAccessTokenObject(token);
			if (!Context.get().isCurrentUserAnonymous()) {
				CredentialStore credStore = findCredentialStore();
				if (credStore != null) {
					try {
						// if the token is already present, and was expired due to which we have fetched a new 
						// token, then we remove the token from the store first and then add this new token.
						deleteToken();
						credStore.store(getServiceName(), ACCESS_TOKEN_STORE_TYPE, getUserId(), token);
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
			userId = getUserId();
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
					credStore.remove(getServiceName(), ACCESS_TOKEN_STORE_TYPE, getUserId());
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

	/**
	 * Read the OAuth token from the verifier.
	 */
	public AccessToken readToken(String token, String verifier)
	throws OAuthException {
		if (Profiler.isEnabled()) {
			ProfilerAggregator agg = Profiler.startProfileBlock(
					profilerReadToken, "");
			long ts = Profiler.getCurrentTime();
			try {
				return _readToken(token, verifier);
			} finally {
				Profiler.endProfileBlock(agg, ts);
			}
		} else {
			return _readToken(token, verifier);
		}
	}
	
	protected AccessToken _readToken(String token, String verifier) throws OAuthException {
		// first we set the Verifier which will be used to get the Access Token
		//		setVerifierCode(verifier);
		try {
			getAccessTokenFromServer();
		} catch (IOException e) {
			throwOAuthException(e, "_readToken", "Failed to get access token.");
		} catch (OAuthException e) {
			throwOAuthException(e, "_readToken", "Failed to get access token.");
		} catch (URISyntaxException e) {
			throwOAuthException(e, "_readToken", "Failed to get access token.");
		} catch (Exception e) {
			throwOAuthException(e, "_readToken", "Failed to get access token.");
		}

		return createToken(getAppId(), getServiceName(), this, getUserId());
	}

	public String getApplicationPage() {
		return applicationPage;
	}

	public void setApplicationPage(String applicationPage) {
		this.applicationPage = applicationPage;
	}
}
