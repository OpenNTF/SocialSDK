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
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.io.StreamUtil;
import com.ibm.sbt.core.configuration.Configuration;
import com.ibm.sbt.security.authentication.oauth.OAuthException;
import com.ibm.sbt.security.encryption.HMACEncryptionUtility;
import com.ibm.sbt.services.util.SSLUtil;

/**
 * HMAC OAuth1 Handler
 * 
 * @author Vimal Dhupar
 * @author Manish Kataria
 */

public class HMACOAuth1Handler extends OAuth1Handler implements Serializable {

	private static final long	serialVersionUID	= 1L;

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.security.authentication.oauth.consumer.OAuth1Handler#getRequestTokenFromServer()
	 */
	@Override
	public void getRequestTokenFromServer() throws OAuthException {

		int responseCode = HttpStatus.SC_OK;
		Context context = Context.get();
		OAProvider oaProvider = getOAProvider();
		String responseBody = "";
		try {
			HttpClient client = new DefaultHttpClient();
			if (oaProvider.getForceTrustSSLCertificate()) {
				client = SSLUtil.wrapHttpClient((DefaultHttpClient) client);
			}

			// In case of Twitter, this callback URL registered can be different from the URL specified below.
			String callbackUrl = oaProvider.getCallbackUrl(context);
			String consumerKey = oaProvider.getConsumerKey();
			String nonce = getNonce();
			String timeStamp = getTimestamp();

			// HMAC requires parameter to be alphabetically sorted, using LinkedHashMap below to preserve
			// ordering
			LinkedHashMap<String, String> signatureParamsMap = new LinkedHashMap<String, String>();
			signatureParamsMap.put(Configuration.CALLBACK, callbackUrl);
			signatureParamsMap.put(Configuration.CONSUMER_KEY, consumerKey);
			signatureParamsMap.put(Configuration.NONCE, nonce);
			signatureParamsMap.put(Configuration.SIGNATURE_METHOD, oaProvider.getSignatureMethod());
			signatureParamsMap.put(Configuration.TIMESTAMP, timeStamp);
			signatureParamsMap.put(Configuration.VERSION, Configuration.OAUTH_VERSION1);

			String consumerSecret = oaProvider.getConsumerSecret();
			String requestPostUrl = getRequestTokenURL();
			HttpPost method = new HttpPost(requestPostUrl);
			String signature = HMACEncryptionUtility.generateHMACSignature(requestPostUrl,
					method.getMethod(), consumerSecret, "", signatureParamsMap);

			StringBuilder headerStr = new StringBuilder();
			headerStr.append("OAuth ").append(Configuration.CALLBACK).append("=\"").append(callbackUrl)
					.append("\"");
			headerStr.append(",").append(Configuration.CONSUMER_KEY).append("=\"").append(consumerKey)
					.append("\"");
			headerStr.append(",").append(Configuration.SIGNATURE_METHOD).append("=\"")
					.append(oaProvider.getSignatureMethod()).append("\"");
			headerStr.append(",").append(Configuration.TIMESTAMP).append("=\"").append(timeStamp)
					.append("\"");
			headerStr.append(",").append(Configuration.NONCE).append("=\"").append(nonce).append("\"");
			headerStr.append(",").append(Configuration.VERSION).append("=\"")
					.append(Configuration.OAUTH_VERSION1).append("\"");
			headerStr.append(",").append(Configuration.SIGNATURE).append("=\"")
					.append(URLEncoder.encode(signature, "UTF-8")).append("\"");
			method.setHeader("Authorization", headerStr.toString());

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
			throw new OAuthException(e, "Internal error - getRequestToken failed Exception: ");
		}
		if (responseCode != HttpStatus.SC_OK) {
			Exception exception = createException(responseCode, responseBody);
			if (exception != null) {
				throw new OAuthException(exception,
						"HMACOAuth1Handler.java : getRequestToken caused exception");
			}
		} else {
			/*
			 * The Response from Twitter contains OAuth request token, OAuth request token secret, and a
			 * boolean oauth_callback_confirmed with value set as true or false.
			 */
			setRequestToken(getTokenValue(responseBody, Configuration.OAUTH_TOKEN));
			setRequestTokenSecret(getTokenValue(responseBody, Configuration.OAUTH_TOKEN_SECRET));
			/*
			 * OAUTH_CALLBACK_CONFIRMED : This property can be used for debugging applications which have not
			 * provided the Callback URL while registering the Application. If OAUTH_CALLBACK_CONFIRMED is
			 * returned as false, then the application needs to be modified to set a callback url. However,
			 * when the Application has specified the Callback Url, and is different from the callback Url we
			 * are passing, this property value is returned as true.
			 */
			setOAuthCallbackConfirmed(getTokenValue(responseBody, Configuration.OAUTH_CALLBACK_CONFIRMED));
		}
	}

	private Exception createException(int responseCode, String responseBody) {
		Exception exception = null;
		if (responseCode == HttpStatus.SC_NOT_IMPLEMENTED) {
			exception = new Exception(
					"getRequestToken failed with Response Code: Not implemented (501), Msg: " + responseBody);
		} else if (responseCode == HttpStatus.SC_UNAUTHORIZED) {
			exception = new Exception("getRequestToken failed with Response Code: Unauthorized (401), Msg: "
					+ responseBody);
		} else if (responseCode == HttpStatus.SC_BAD_REQUEST) {
			exception = new Exception("getRequestToken failed with Response Code: Bad Request (400), Msg: "
					+ responseBody);
		} else if (responseCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
			exception = new Exception(
					"getRequestToken failed with Response Code: Internal Server error (500), Msg: "
							+ responseBody);
		} else {
			exception = new Exception("getRequestToken failed with Response Code (" + responseCode
					+ "), Msg: " + responseBody);
		}
		return exception;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.security.authentication.oauth.consumer.OAuth1Handler#getAccessTokenFromServer()
	 */
	@Override
	public void getAccessTokenFromServer() throws OAuthException {

		int responseCode = HttpStatus.SC_OK;
		String responseBody = null;
		OAProvider oaProvider = getOAProvider();
		try {
			HttpClient client = new DefaultHttpClient();
			if (oaProvider.getForceTrustSSLCertificate()) {
				client = SSLUtil.wrapHttpClient((DefaultHttpClient) client);
			}

			StringBuilder requestPostUrl = new StringBuilder(getAccessTokenURL());
			// adding the oauth_verifier to the request.
			requestPostUrl.append("?");
			requestPostUrl.append(Configuration.OAUTH_VERIFIER).append('=')
					.append(URLEncoder.encode(verifierCode, "UTF-8"));
			HttpPost method = new HttpPost(requestPostUrl.toString());
			// Collecting parameters for preparing the Signature
			String consumerKey = oaProvider.getConsumerKey();
			String requestToken = getRequestToken();
			String nonce = getNonce();
			String timeStamp = getTimestamp();
			/*
			 * Generate a map of parameters which are required for creating signature. We are using a Linked
			 * HashMap to preserver the order in which parameters are added to the Map, as the parameters need
			 * to be sorted for Twitter Signature generation.
			 */
			LinkedHashMap<String, String> signatureParamsMap = new LinkedHashMap<String, String>();
			signatureParamsMap.put(Configuration.CONSUMER_KEY, consumerKey);
			signatureParamsMap.put(Configuration.NONCE, nonce);
			signatureParamsMap.put(Configuration.OAUTH_TOKEN, requestToken);
			signatureParamsMap.put(Configuration.SIGNATURE_METHOD, oaProvider.getSignatureMethod());
			signatureParamsMap.put(Configuration.TIMESTAMP, timeStamp);
			signatureParamsMap.put(Configuration.VERSION, Configuration.OAUTH_VERSION1);

			String requestTokenSecret = getRequestTokenSecret();
			String consumerSecret = oaProvider.getConsumerSecret();
			String signature = HMACEncryptionUtility.generateHMACSignature(requestPostUrl.toString(),
					method.getMethod(), consumerSecret, requestTokenSecret, signatureParamsMap);

			// Preparing the Header for getting access token
			StringBuilder headerStr = new StringBuilder();

			headerStr.append("OAuth ").append(Configuration.CONSUMER_KEY).append("=\"").append(consumerKey)
					.append("\"");
			headerStr.append(",").append(Configuration.SIGNATURE_METHOD).append("=\"")
					.append(oaProvider.getSignatureMethod()).append("\"");
			headerStr.append(",").append(Configuration.TIMESTAMP).append("=\"").append(timeStamp)
					.append("\"");
			headerStr.append(",").append(Configuration.NONCE).append("=\"").append(nonce).append("\"");
			headerStr.append(",").append(Configuration.VERSION).append("=\"")
					.append(Configuration.OAUTH_VERSION1).append("\"");
			// This is the request token which is obtained from getRequestTokenFromServer() method.
			headerStr.append(",").append(Configuration.OAUTH_TOKEN).append("=\"").append(requestToken)
					.append("\"");
			headerStr.append(",").append(Configuration.SIGNATURE).append("=\"")
					.append(URLEncoder.encode(signature, "UTF-8")).append("\"");
			method.setHeader("Authorization", headerStr.toString());

			method.setHeader("Authorization", headerStr.toString());

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
			throw new OAuthException(e, "Internal error - getAccessToken failed Exception: ");
		}
		if (responseCode != HttpStatus.SC_OK) {
			Exception exception = createException(responseCode, responseBody);
			if (exception != null) {
				throw new OAuthException(exception,
						"HMACOAuth1Handler.java : getAccessToken caused exception");
			}
		} else {
			/*
			 * Response from twitter contains Access Token, Access Token Secret, User Id and Screen Name of
			 * the Application.
			 */
			setAccessToken(getTokenValue(responseBody, Configuration.OAUTH_TOKEN));
			setAccessTokenSecret(getTokenValue(responseBody, Configuration.OAUTH_TOKEN_SECRET));
		}
	}

	/**
	 * createAuthorizationHeader
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public String createAuthorizationHeader(String url, Map<String, String> params) throws OAuthException {

		OAProvider oaProvider = getOAProvider();
		Context context = Context.get();

		String nonce = getNonce();
		String timeStamp = getTimestamp();
		String consumerKey = oaProvider.getConsumerKey();
		String consumerSecret = oaProvider.getConsumerSecret();
		String method = context.getHttpRequest().getMethod();
		/*
		 * This is the Access Token which is obtained from the Application, while registering the App. User
		 * will have to create these tokens, in the application, if not generated already. This Access Token
		 * if required for executing APIs on Twitter.
		 */
		String applicationAccessToken = oaProvider.getApplicationAccessToken();
		/* This is the Access Token Secret which is obtained from the getAccessTokenFromServer() method. */
		String tokenSecret = getAccessTokenSecret();
		/*
		 * Generate a map of parameters which are required for creating signature. We are using a TreeMap here
		 * instead of Linked HashMap, since the authorization header creates a signature using the parameters
		 * passed by the User for API Execution. And all these parameters need to be sorted for Twitter
		 * Signature generation.
		 */
		TreeMap<String, String> treeMap = new TreeMap<String, String>();
		treeMap.put(Configuration.CONSUMER_KEY, consumerKey);
		treeMap.put(Configuration.NONCE, nonce);
		treeMap.put(Configuration.SIGNATURE_METHOD, oaProvider.getSignatureMethod());
		treeMap.put(Configuration.VERSION, Configuration.OAUTH_VERSION1);
		treeMap.put(Configuration.TIMESTAMP, timeStamp);
		treeMap.put(Configuration.OAUTH_TOKEN, applicationAccessToken);

		for (Map.Entry<String, String> entry : params.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			treeMap.put(key, value);
		}
		String signature = "";
		try {
			// generate the complete URL first
			signature = HMACEncryptionUtility.generateHMACSignature(url, method, consumerSecret, tokenSecret,
					treeMap);
		} catch (Exception e) {
			throw new OAuthException(new Exception(e), "createAuthorizationHeader failed with Exception");
		}

		StringBuilder headerStr = new StringBuilder();
		headerStr.append("OAuth ").append(Configuration.CONSUMER_KEY).append("=\"").append(consumerKey)
				.append("\"");
		headerStr.append(",").append(Configuration.NONCE).append("=\"").append(nonce).append("\"");
		try {
			headerStr.append(",").append(Configuration.SIGNATURE).append("=\"")
					.append(URLEncoder.encode(signature, "UTF-8")).append("\"");
		} catch (UnsupportedEncodingException e1) {
			throw new OAuthException(new Exception(e1),
					"createAuthorizationHeader failed with UnsupportedEncodingException");
		}
		headerStr.append(",").append(Configuration.SIGNATURE_METHOD).append("=\"")
				.append(oaProvider.getSignatureMethod()).append("\"");
		headerStr.append(",").append(Configuration.TIMESTAMP).append("=\"").append(timeStamp).append("\"");
		headerStr.append(",").append(Configuration.OAUTH_TOKEN).append("=\"").append(applicationAccessToken)
				.append("\"");
		headerStr.append(",").append(Configuration.VERSION).append("=\"")
				.append(Configuration.OAUTH_VERSION1).append("\"");

		return headerStr.toString();
	}
}
