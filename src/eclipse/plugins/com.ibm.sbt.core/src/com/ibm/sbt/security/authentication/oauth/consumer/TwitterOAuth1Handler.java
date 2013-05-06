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
import com.ibm.sbt.core.configuration.Configuration;
import com.ibm.sbt.security.encryption.HMACEncryptionUtility;
import com.ibm.sbt.services.util.SSLUtil;

/**
 * Twitter OAuth1 Handler
 * 
 * @author Vimal Dhupar
 */

public class TwitterOAuth1Handler extends OAuth1Handler implements Serializable {

	private static final long	serialVersionUID	= 1L;
	private Map<String, String>	apiRequestParameters;
	private String				apiTwitterUri;

	public Map<String, String> getApiRequestParameters() {
		return apiRequestParameters;
	}

	public void setApiRequestParameters(Map<String, String> requestParameters) {
		this.apiRequestParameters = requestParameters;
	}

	public String getApiTwitterUri() {
		return apiTwitterUri;
	}

	public void setApiTwitterUri(String requestUri) {
		this.apiTwitterUri = requestUri;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.security.authentication.oauth.consumer.OAuth1Handler#getRequestTokenFromServer()
	 */
	@Override
	public void getRequestTokenFromServer() throws Exception {

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

			// Preparing Signature for Twitter.

			/*
			 * A callback URL is registered while creating an application(Mandatory. Should be provided).
			 * However, in case of Twitter, this callback URL registered can be different from the URL
			 * specified below.
			 */
			String callbackUrl = oaProvider.getCallbackUrl(context);
			String consumerKey = oaProvider.getConsumerKey();
			String nonce = getNonce();
			String timeStamp = getTimestamp();
			/*
			 * Generate a map of parameters which are required for creating signature. We are using a Linked
			 * HashMap to preserver the order in which parameters are added to the Map, as the parameters need
			 * to be sorted for Twitter Signature generation.
			 */
			LinkedHashMap<String, String> treeMap = new LinkedHashMap<String, String>();
			treeMap.put(Configuration.CALLBACK, callbackUrl);
			treeMap.put(Configuration.CONSUMER_KEY, consumerKey);
			treeMap.put(Configuration.NONCE, nonce);
			treeMap.put(Configuration.SIGNATURE_METHOD, Configuration.HMAC_SIGNATURE);
			treeMap.put(Configuration.TIMESTAMP, timeStamp);
			treeMap.put(Configuration.VERSION, Configuration.OAUTH_VERSION1);

			String consumerSecret = oaProvider.getConsumerSecret();
			String twitterPostUrl = getRequestTokenURL();
			HttpPost method = new HttpPost(twitterPostUrl);
			String signature = HMACEncryptionUtility.generateHMACSignature(twitterPostUrl,
					method.getMethod(), consumerSecret, "", treeMap);

			// Preparing Auth Header for Request Token
			StringBuilder headerStr = new StringBuilder();
			headerStr.append("OAuth " + Configuration.CALLBACK + "=\"" + callbackUrl + "\"");
			headerStr.append("," + Configuration.CONSUMER_KEY + "=\"" + consumerKey + "\"");
			headerStr.append("," + Configuration.SIGNATURE_METHOD + "=\"" + Configuration.HMAC_SIGNATURE
					+ "\"");
			headerStr.append("," + Configuration.TIMESTAMP + "=\"" + timeStamp + "\"");
			headerStr.append("," + Configuration.NONCE + "=\"" + nonce + "\"");
			headerStr.append("," + Configuration.VERSION + "=\"" + Configuration.OAUTH_VERSION1 + "\"");
			headerStr.append("," + Configuration.SIGNATURE + "=\"" + URLEncoder.encode(signature, "UTF-8")
					+ "\"");
			method.setHeader("Authorization", headerStr.toString());

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

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.security.authentication.oauth.consumer.OAuth1Handler#getAccessTokenFromServer()
	 */
	@Override
	public void getAccessTokenFromServer() throws Exception {

		int responseCode = HttpStatus.SC_OK;
		String responseBody = null;
		InputStream content = null;
		OAProvider oaProvider = getOAProvider();
		try {
			HttpClient client = new DefaultHttpClient();
			if (oaProvider.getForceTrustSSLCertificate()) {
				client = SSLUtil.wrapHttpClient((DefaultHttpClient) client);
			}

			StringBuilder twitterPostUrl = new StringBuilder(getAccessTokenURL());
			// adding the oauth_verifier to the request.
			twitterPostUrl.append("?");
			twitterPostUrl.append(Configuration.OAUTH_VERIFIER).append('=')
					.append(URLEncoder.encode(verifierCode, "UTF-8"));
			HttpPost method = new HttpPost(twitterPostUrl.toString());
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
			LinkedHashMap<String, String> treeMap = new LinkedHashMap<String, String>();
			treeMap.put(Configuration.CONSUMER_KEY, consumerKey);
			treeMap.put(Configuration.NONCE, nonce);
			treeMap.put(Configuration.OAUTH_TOKEN, requestToken);
			treeMap.put(Configuration.SIGNATURE_METHOD, Configuration.HMAC_SIGNATURE);
			treeMap.put(Configuration.TIMESTAMP, timeStamp);
			treeMap.put(Configuration.VERSION, Configuration.OAUTH_VERSION1);

			String requestTokenSecret = getRequestTokenSecret();
			String consumerSecret = oaProvider.getConsumerSecret();
			String signature = HMACEncryptionUtility.generateHMACSignature(twitterPostUrl.toString(),
					method.getMethod(), consumerSecret, requestTokenSecret, treeMap);

			// Preparing the Header for getting access token
			StringBuilder headerStr = new StringBuilder();
			headerStr.append("OAuth " + Configuration.CONSUMER_KEY + "=\"" + consumerKey + "\"");
			headerStr.append("," + Configuration.SIGNATURE_METHOD + "=\"" + Configuration.HMAC_SIGNATURE
					+ "\"");
			headerStr.append("," + Configuration.TIMESTAMP + "=\"" + timeStamp + "\"");
			headerStr.append("," + Configuration.NONCE + "=\"" + nonce + "\"");
			headerStr.append("," + Configuration.VERSION + "=\"" + "1.0" + "\"");
			// This is the request token which is obtained from getRequestTokenFromServer() method.
			headerStr.append("," + Configuration.OAUTH_TOKEN + "=\"" + requestToken + "\"");
			headerStr.append("," + Configuration.SIGNATURE + "=\"" + URLEncoder.encode(signature, "UTF-8")
					+ "\"");
			method.setHeader("Authorization", headerStr.toString());

			HttpResponse httpResponse = client.execute(method);
			responseCode = httpResponse.getStatusLine().getStatusCode();
			content = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			responseBody = reader.readLine();
		} catch (Exception e) {
			throw new Exception("Internal error - getAccessToken failed Exception: <br>" + e);
		} finally {
			content.close();
		}
		if (responseCode != HttpStatus.SC_OK) {
			if (responseCode == HttpStatus.SC_NOT_IMPLEMENTED) {
				throw new Exception(
						"getAccessToken failed with Response Code: Not implemented (501),<br>Msg: "
								+ responseBody);
			} else if (responseCode == HttpStatus.SC_UNAUTHORIZED) {
				throw new Exception("getAccessToken failed with Response Code: Unauthorized (401),<br>Msg: "
						+ responseBody);
			} else if (responseCode == HttpStatus.SC_BAD_REQUEST) {
				throw new Exception("getAccessToken failed with Response Code: Bad Request (400),<br>Msg: "
						+ responseBody.toString());
			} else if (responseCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				throw new Exception(
						"getAccessToken failed with Response Code: Internal Server error (500),<br>Msg: "
								+ responseBody);
			} else {
				throw new Exception("getAccessToken failed with Response Code (" + responseCode
						+ "),<br>Msg: " + responseBody);
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
	public String createAuthorizationHeader(String url, Map<String, String> params) {
		StringBuilder headerStr = new StringBuilder(1024);
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
		treeMap.put(Configuration.SIGNATURE_METHOD, Configuration.HMAC_SIGNATURE);
		treeMap.put(Configuration.VERSION, Configuration.OAUTH_VERSION1);
		treeMap.put(Configuration.TIMESTAMP, timeStamp);
		treeMap.put(Configuration.OAUTH_TOKEN, applicationAccessToken);

		for (Map.Entry<String, String> entry : params.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			treeMap.put(key, value);
		}
		String signature = HMACEncryptionUtility.generateHMACSignature(url, method, consumerSecret,
				tokenSecret, treeMap);
		headerStr.append("OAuth " + Configuration.CONSUMER_KEY + "=\"" + consumerKey + "\"");
		headerStr.append("," + Configuration.NONCE + "=\"" + nonce + "\"");
		try {
			headerStr.append("," + Configuration.SIGNATURE + "=\"" + URLEncoder.encode(signature, "UTF-8")
					+ "\"");
		} catch (UnsupportedEncodingException e) {
			System.err.println();
		}
		headerStr.append("," + Configuration.SIGNATURE_METHOD + "=\"" + Configuration.HMAC_SIGNATURE + "\"");
		headerStr.append("," + Configuration.TIMESTAMP + "=\"" + timeStamp + "\"");
		headerStr.append("," + Configuration.OAUTH_TOKEN + "=\"" + applicationAccessToken + "\"");
		headerStr.append("," + Configuration.VERSION + "=\"" + Configuration.OAUTH_VERSION1 + "\"");

		return headerStr.toString();
	}
}
