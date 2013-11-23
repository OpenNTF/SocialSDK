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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.core.configuration.Configuration;
import com.ibm.sbt.security.authentication.oauth.OAuthException;
import com.ibm.sbt.security.authentication.oauth.consumer.servlet.OACallback;
import com.ibm.sbt.service.util.ServiceUtil;

public abstract class OAuthHandler {

	private boolean	showOAuthFlow;
	private String	version;
	private String	serverUrl;
	private String userId;

	/**
	 * Do all steps in the OAuth flow before the Authorization step.
	 * 
	 * @throws Exception
	 */
	abstract public void doPreAuthorizationFlow() throws Exception;

	/**
	 * Do all steps in the OAuth flow after the Authorization step.
	 * 
	 * @throws Exception
	 */
	abstract public void doPostAuthorizationFlow() throws Exception;

	/**
	 * Gets the Server Authorization Endpoint URL
	 * 
	 * @return
	 */
	abstract public String getAuthorizationURL();

	/**
	 * Returns the OAuth header string for each API request.
	 * 
	 * @return
	 */
	abstract public String createAuthorizationHeader();

	abstract public String getAccessToken();

	abstract public boolean isInitialized();

	abstract public String getAuthType();

	public void setCallBackUrl(HttpServletRequest request) {
	};

	/**
	 * OAuth data is passed to other applications (e.g. API Explorer) to convey OAuth info that has already
	 * been collected from the user, so that the other application does not have to collect it again
	 * ("poor man's" SSO).
	 */
	public String createAuthData() {
		StringBuilder authData = new StringBuilder(1024);
		authData.append(createAuthorizationHeader());
		authData.append(",").append(Configuration.SERVER_URL).append("=").append(this.getServerUrl());
		authData.append(",").append(Configuration.AUTH_TYPE).append("=").append(this.getAuthType());

		return authData.toString();

	}

	public String buildErrorMessage(int responseCode, String responseBody) {
		String errorDetail = null;
		if (responseCode == HttpStatus.SC_NOT_IMPLEMENTED) {
			errorDetail = "Response Code: Not implemented (501), Msg: " + responseBody;
		} else if (responseCode == HttpStatus.SC_UNAUTHORIZED) {
			errorDetail = "Response Code: Unauthorized (401), Msg: "
					+ responseBody;
		} else if (responseCode == HttpStatus.SC_BAD_REQUEST) {
			errorDetail = "Response Code: Bad Request (400), Msg: "
					+ responseBody;
		} else if (responseCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
			errorDetail = "Response Code: Internal Server error (500), Msg: "
							+ responseBody;
		} else {
			errorDetail = "Response Code (" + responseCode
					+ "), Msg: " + responseBody;
		}
		return errorDetail;
	}
	public boolean isShowOAuthFlow() {
		return showOAuthFlow;
	}

	public void setShowOAuthFlow(boolean showOAuthFlow) {
		this.showOAuthFlow = showOAuthFlow;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Looks for a token name and returns it's value.
	 * 
	 * @param input
	 * @param tokenName
	 * @return
	 */
	public static String getTokenValue(String input, String tokenName) {

		if (input.startsWith("{")) {
			input = input.substring(1, input.length());
		}
		if (input.endsWith("}")) {
			input = input.substring(0, input.length() - 1);
		}

		String splitterkey = input.contains("&") ? "&" : ","; // Connections and Smartcloud have different
																// response types
		String altTokenName = "\"" + tokenName + "\"";

		String[] tokens = input.split(splitterkey);
		String tokenValue = null;
		for (String token : tokens) {
			if (token.startsWith(tokenName + "=")) {
				tokenValue = token.substring(tokenName.length() + 1);
				break;
			} else if (token.startsWith(altTokenName + ":")) {
				tokenValue = token.substring(altTokenName.length() + 1);
				tokenValue.substring(0, tokenValue.length());
				break;
			}
		}

		if (null != tokenValue && !tokenValue.equals("")) {
			if (tokenValue.startsWith("\"")) {
				tokenValue = tokenValue.substring(1, tokenValue.length());
			}
			if (tokenValue.endsWith("\"")) {
				tokenValue = tokenValue.substring(0, tokenValue.length() - 1);
			}
		}

		return tokenValue;
	}

	public static String percentEncode(String str) {
		String encodedStr = null;
		if (!StringUtil.isEmpty(str)) {
			try {
				encodedStr = URLEncoder.encode(str, Configuration.ENCODING).replace("+", "%20")
						.replace("*", "%2A").replace("%7E", "~");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return encodedStr;
	}

	public static String convertToSafeHtml(String sourceText) {
		// escape HTML by escaping the < character
		if (sourceText == null) {
			return null;
		}
		return sourceText.replaceAll("<", "&lt;");
	}

	public String getApplicationPage(Context context) throws OAuthException {
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

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		if (this.userId != null) {
			return this.userId;
		}
		Context context = Context.getUnchecked();
		return (context == null) ? null : context.getCurrentUserId();
	}
		
}
