/*
 * © Copyright IBM Corp. 2013
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
package com.ibm.sbt.security.encryption;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.core.configuration.Configuration;
import com.ibm.sbt.security.authentication.oauth.OAuthException;

/**
 * HMACEncryptionUtility can be used to generate HMAC based signature for OAuth1 Authentication
 * <p>
 * 
 * @author Manish Kataria
 * @author Vimal Dhupar
 */

public class HMACEncryptionUtility {

	public static String generateHMACSignature(String apiUrl, String method, String consumerSecret,
			String tokenSecret, Map<String, String> paramsSortedMap) throws OAuthException {
		try {
			String parameterString = generateParameterString(paramsSortedMap);
			String signature_base_string = generateSignatureBaseString(method, apiUrl, parameterString);
			String signingKey = null;
			if (StringUtil.isEmpty(tokenSecret)) {
				// No token secret is available when call is made from getRequestToken, tokensecret is fetched
				// later in OADance
				signingKey = consumerSecret + "&";
			} else {
				signingKey = consumerSecret + "&" + tokenSecret;
			}
			SecretKey secretKey = null;
			byte[] keyBytes = signingKey.getBytes();
			secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(secretKey);
			byte[] text = signature_base_string.getBytes();
			String signature = new String(Base64.encodeBase64(mac.doFinal(text))).trim();
			return signature;
		} catch (NoSuchAlgorithmException e) {
			throw new OAuthException(e,
					"HMACEncryptionUtility : generateHMACSignature caused NoSuchAlgorithmException exception");
		} catch (InvalidKeyException e) {
			throw new OAuthException(e,
					"HMACEncryptionUtility : generateHMACSignature caused InvalidKeyException exception");
		}
	}

	public static String percentEncode(String str) {
		String encodedStr = null;
		try {
			encodedStr = URLEncoder.encode(str, Configuration.ENCODING).replace("+", "%20")
					.replace("*", "%2A").replace("%7E", "~");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage());
		}
		return encodedStr;
	}

	public static String generateParameterString(Map<String, String> paramsMap) {

		StringBuilder parameterString = new StringBuilder();
		for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			parameterString.append(key).append("=").append(percentEncode(value)).append("&");
		}
		// Now remove the last & and return the string.
		return parameterString.deleteCharAt(parameterString.length() - 1).toString();
	}

	public static String generateSignatureBaseString(String method, String url, String parameterString)
			throws OAuthException {
		StringBuilder signatureBaseString = new StringBuilder();

		try {
			signatureBaseString.append(method.toUpperCase()).append("&").append(percentEncode(url))
					.append("&").append(URLEncoder.encode(parameterString, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new OAuthException(e,
					"HMACEncryptionUtility : generateSignatureBaseString caused UnsupportedEncodingException exception");
		}
		return signatureBaseString.toString();
	}
}
