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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.ibm.commons.Platform;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.io.StreamUtil;
import com.ibm.sbt.core.configuration.Configuration;
import com.ibm.sbt.services.util.SSLUtil;

/**
 * @author Vimal Dhupar
 * @author Manish Kataria
 */

public class OAuth1Handler extends OAuthHandler {
	private static final int			BYTE_ARRAY_SIZE	= ((23 * 5) / 8) + 1;
	private static final SecureRandom	SECURE_RANDOM	= new SecureRandom();

	protected String					OAuthCallbackConfirmed;
	protected String					requestToken;
	private String						requestTokenSecret;
	protected String					verifierCode;
	private String						accessTokenSecret;
	private String						accessToken;
	private String						expiresIn;
	private String						authorizationExpiresIn;
	private String						oauth_session_handle;

	// for logging
	private static final String			sourceClass		= OAuth1Handler.class.getName();
	private static final Logger			logger			= Logger.getLogger(sourceClass);

	public OAuth1Handler() {
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
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getRequestTokenFromServer", new Object[] {});
		}

		HttpGet method = null;
		int responseCode = HttpStatus.SC_OK;
		String responseBody = null;
		InputStream content = null;
		Context context = Context.get();
		OAProvider oaProvider = getOAProvider();
		try {
			HttpClient client = new DefaultHttpClient();
			if (oaProvider.getForceTrustSSLCertificate()) {
				client = SSLUtil.wrapHttpClient((DefaultHttpClient) client);
			}
			StringBuilder url = new StringBuilder();
			url.append(getRequestTokenURL()).append("?");
			url.append(Configuration.CONSUMER_KEY).append('=')
					.append(URLEncoder.encode(oaProvider.getConsumerKey(), "UTF-8")).append('&');
			url.append(Configuration.SIGNATURE).append('=')
					.append(URLEncoder.encode(buildSignature(oaProvider.getConsumerSecret(), ""), "UTF-8"))
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
					.append(URLEncoder.encode(oaProvider.getCallbackUrl(context), "UTF-8"));
			method = new HttpGet(url.toString());
			if (logger.isLoggable(Level.FINEST)) {
				logger.log(Level.FINEST, "OAuth1.0 making network call to get Request Token from Server", url);
			}

			HttpResponse httpResponse = client.execute(method);
			responseCode = httpResponse.getStatusLine().getStatusCode();
			content = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			responseBody = reader.readLine();
		} catch (Exception e) {
			throw new Exception("Internal error - getRequestToken failed Exception: <br>" + e);
		} finally {
			content.close();
		}
		if (responseCode != HttpStatus.SC_OK) {
			if (responseCode == HttpStatus.SC_NOT_IMPLEMENTED) {
				throw new Exception(
						"getRequestToken failed with Response Code: Not implemented (501),<br>Msg: "
								+ responseBody);
			} else if (responseCode == HttpStatus.SC_UNAUTHORIZED) {
				throw new Exception("getRequestToken failed with Response Code: Unauthorized (401),<br>Msg: "
						+ responseBody);
			} else if (responseCode == HttpStatus.SC_BAD_REQUEST) {
				throw new Exception("getRequestToken failed with Response Code: Bad Request (400),<br>Msg: "
						+ responseBody.toString());
			} else if (responseCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				throw new Exception(
						"getRequestToken failed with Response Code: Internal Server error (500),<br>Msg: "
								+ responseBody);
			} else {
				throw new Exception("getRequestToken failed with Response Code (" + responseCode
						+ "),<br>Msg: " + responseBody);
			}
		} else {
			setRequestToken(getTokenValue(responseBody, Configuration.OAUTH_TOKEN));
			setRequestTokenSecret(getTokenValue(responseBody, Configuration.OAUTH_TOKEN_SECRET));
			getTokenValue(responseBody, Configuration.OAUTH1_ISSUEDON);
			// Date issuedOn = new Date(Long.valueOf(issuedOnDate));
			setExpiresIn(getTokenValue(responseBody, Configuration.OAUTH1_EXPIRESIN));
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getRequestTokenFromServer", new Object[] { getRequestToken(),
					getRequestTokenSecret(), responseBody, responseCode });
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.security.authentication.oauth.consumer.OAuthHandler#getAccessTokenFromServer()
	 */
	public void getAccessTokenFromServer() throws Exception {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getAccessTokenFromServer", new Object[] {});
		}

		HttpGet method = null;
		int responseCode = HttpStatus.SC_OK;
		String responseBody = null;
		OAProvider oaProvider = getOAProvider();
		try {
			HttpClient client = new DefaultHttpClient();
			if (oaProvider.getForceTrustSSLCertificate()) {
				client = SSLUtil.wrapHttpClient((DefaultHttpClient) client);
			}
			StringBuilder url = new StringBuilder();
			url.append(getAccessTokenURL()).append("?");
			url.append(Configuration.CONSUMER_KEY).append('=')
					.append(URLEncoder.encode(oaProvider.getConsumerKey(), "UTF-8")).append('&');
			url.append(Configuration.OAUTH_TOKEN).append('=')
					.append(URLEncoder.encode(requestToken, "UTF-8")).append('&');
			url.append(Configuration.OAUTH_VERIFIER).append('=')
					.append(URLEncoder.encode(verifierCode, "UTF-8")).append('&');
			url.append(Configuration.SIGNATURE)
					.append('=')
					.append(URLEncoder.encode(
							buildSignature(oaProvider.getConsumerSecret(), requestTokenSecret), "UTF-8"))
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

			if (logger.isLoggable(Level.FINEST)) {
				logger.log(Level.FINEST, "OAuth1.0 making network call to get Access Token from Server", url);
			}

			HttpResponse httpResponse = client.execute(method);
			responseCode = httpResponse.getStatusLine().getStatusCode();
			InputStream content = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			try {
				responseBody = reader.readLine();
			} finally {
				StreamUtil.close(reader);
			}
		} catch (Exception e) {
			Platform.getInstance().log(e);
			throw new Exception("Internal error - getRequestToken failed Exception: <br>" + e);
		}
		if (responseCode != HttpStatus.SC_OK) {
			if (responseCode == HttpStatus.SC_NOT_IMPLEMENTED) {
				throw new Exception(
						"getRequestToken failed with Response Code: Not implemented (501),<br>Msg: "
								+ responseBody);
			} else if (responseCode == HttpStatus.SC_UNAUTHORIZED) {
				throw new Exception("getRequestToken failed with Response Code: Unauthorized (401),<br>Msg: "
						+ responseBody);
			} else if (responseCode == HttpStatus.SC_BAD_REQUEST) {
				throw new Exception("getRequestToken failed with Response Code: Bad Request (400),<br>Msg: "
						+ responseBody);
			} else if (responseCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				throw new Exception(
						"getRequestToken failed with Response Code: Internal Server error (500),<br>Msg: "
								+ responseBody);
			} else {
				throw new Exception("getRequestToken failed with Response Code (" + responseCode
						+ "),<br>Msg: " + responseBody);
			}
		} else {
			setAccessToken(getTokenValue(responseBody, Configuration.OAUTH_TOKEN));
			setAccessTokenSecret(getTokenValue(responseBody, Configuration.OAUTH_TOKEN_SECRET));
			getTokenValue(responseBody, Configuration.OAUTH1_ISSUEDON);
			// Date issuedOn = new Date(Long.valueOf(issuedOnDate));
			setExpiresIn(getTokenValue(responseBody, Configuration.OAUTH1_EXPIRESIN));
			// setOauth_session_handle(getTokenValue(responseBody, Configuration.))
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getAccessTokenFromServer", new Object[] { getAccessToken(),
					getAccessTokenSecret(), responseBody, responseCode });
		}
	}

	protected String getAccessTokenURL() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getAccessTokenURL", new Object[] {});
		}
		OAProvider oap = getOAProvider();
		if (getServerUrl() != null) {
			return getServerUrl() + oap.getAccessTokenURL();
		} else {
			return oap.getAccessTokenURL();
		}
	}

	protected String getRequestTokenURL() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getRequestTokenURL", new Object[] {});
		}
		OAProvider oap = getOAProvider();
		if (getServerUrl() != null) {
			return getServerUrl() + oap.getRequestTokenURL();
		} else {
			return oap.getRequestTokenURL();
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
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "createAuthorizationHeader", new Object[] {});
		}
		OAProvider oaprovider = getOAProvider();
		StringBuilder authHdr = new StringBuilder(1024);
		authHdr.append("OAuth ");
		authHdr.append(Configuration.CONSUMER_KEY).append("=\"").append(percentEncode(oaprovider.getConsumerKey())).append("\",");
		authHdr.append(Configuration.OAUTH_TOKEN).append("=\"").append(percentEncode(accessToken)).append("\",");
		authHdr.append(Configuration.SIGNATURE_METHOD).append("=\"").append(Configuration.PLAINTEXT_SIGNATURE).append("\",");
		authHdr.append(Configuration.SIGNATURE).append("=\"").append(percentEncode(buildSignature(oaprovider.getConsumerSecret(), getAccessTokenSecret()))).append("\",");
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

	public OAProvider getOAProvider() {
		Context context = Context.get();
		return (OAProvider) context.getSessionMap().get("oaProvider");
	}

	@Override
	public String getAccessToken() {
		return accessToken;
	}

	public String getRequestToken() {
		return requestToken;
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

	@Override
	public String getAuthorizationURL() {
		// TODO Auto-generated method stub
		return null;
	}

}
