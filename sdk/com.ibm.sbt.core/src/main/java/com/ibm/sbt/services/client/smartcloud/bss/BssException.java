/*
 * © Copyright IBM Corp. 2014
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
package com.ibm.sbt.services.client.smartcloud.bss;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.commons.util.AbstractException;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;

/**
 * @author mwallace
 *
 */
public class BssException extends AbstractException {

	static final String sourceClass = BssException.class.getName();
	static final Logger logger = Logger.getLogger(sourceClass);
	
	private static final long serialVersionUID = 1L;

	/**
	 * @param nextException
	 */
	public BssException(Exception nextException) {
		super(nextException);
	}
	
	/**
	 * @param nextException
	 */
	public BssException(Throwable nextException, String msg, Object...params) {
		super(nextException, msg, params);
	}
	
	/**
	 * 
	 * @param response
	 * @param msg
	 * @param params
	 */
	public BssException(Response response, String msg, Object...params) {
		super(new ClientServicesException(response.getResponse(), response.getRequest()), msg, params);
	}
	
	/**
	 * Return JSON object returned by the server if available and otherwise returns null.
	 *  
	 * @return
	 */
	public JsonJavaObject getResponseJson() {
		Throwable cause = getCause();
		if (cause instanceof ClientServicesException) {
			String responseBody = ((ClientServicesException)cause).getResponseBody();
			if (StringUtil.isNotEmpty(responseBody)) {
				try {
					return (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, responseBody);
				} catch (JsonException je) {
					logger.log(Level.WARNING, "Unable to parse JSON response: "+responseBody, je);
				}
			}
		}
		return null;
	}
	
	/**
	 * Helper method to extract the response code from the JSON response (if any).
	 * 
	 * @return Response code or null if none exists or if no valid JSON response is available.
	 */
	public String getResponseCode() {
		JsonJavaObject responseJson = getResponseJson();
		if (responseJson != null) {
			try {
				JsonJavaObject bssResponse = responseJson.getAsObject("BSSResponse");
				String responseCode = bssResponse.getAsString("ResponseCode");
				if (StringUtil.isEmpty(responseCode)) {
					responseCode = bssResponse.getAsString("responseCode");
				}
				return responseCode;
			} catch (Exception e) {
				logger.log(Level.WARNING, "Unable to extract response code: "+responseJson, e);
			}
		}
		return null;
	}
	
	/**
	 * Helper method to return the response status code if the cause is a ClientServicesException.
	 * 
	 * @return
	 */
	public int getResponseStatusCode() {
		Throwable cause = getCause();
		if (cause instanceof ClientServicesException) {
			return ((ClientServicesException)cause).getResponseStatusCode();
		}
		return 0;
	}
	
	/**
	 * Helper method to extract the user action from the JSON response (if any).
	 * 
	 * @return User action or null if none exists or if no valid JSON response is available.
	 */
	public String getUserAction() {
		JsonJavaObject responseJson = getResponseJson();
		if (responseJson != null) {
			try {
				JsonJavaObject bssResponse = responseJson.getAsObject("BSSResponse");
				return bssResponse.getAsString("userAction");
			} catch (Exception e) {
				logger.log(Level.WARNING, "Unable to extract user action: "+responseJson, e);
			}
		}
		return null;
	}
	
	/**
	 * Helper method to extract the severity from the JSON response (if any).
	 * 
	 * @return Severity or null if none exists or if no valid JSON response is available.
	 */
	public String getSeverity() {
		JsonJavaObject responseJson = getResponseJson();
		if (responseJson != null) {
			try {
				JsonJavaObject bssResponse = responseJson.getAsObject("BSSResponse");
				return bssResponse.getAsString("severity");
			} catch (Exception e) {
				logger.log(Level.WARNING, "Unable to extract severity: "+responseJson, e);
			}
		}
		return null;
	}
	
	/**
	 * Helper method to extract the message id from the JSON response (if any).
	 * 
	 * @return Message id or null if none exists or if no valid JSON response is available.
	 */
	public String getMessageId() {
		JsonJavaObject responseJson = getResponseJson();
		if (responseJson != null) {
			try {
				JsonJavaObject bssResponse = responseJson.getAsObject("BSSResponse");
				return bssResponse.getAsString("messageId");
			} catch (Exception e) {
				logger.log(Level.WARNING, "Unable to extract message id: "+responseJson, e);
			}
		}
		return null;
	}
	
	/**
	 * Helper method to extract the response message from the JSON response (if any).
	 * 
	 * @return Response message or null if none exists or if no valid JSON response is available.
	 */
	public String getResponseMessage() {
		JsonJavaObject responseJson = getResponseJson();
		if (responseJson != null) {
			try {
				JsonJavaObject bssResponse = responseJson.getAsObject("BSSResponse");
				return bssResponse.getAsString("ResponseMessage");
			} catch (Exception e) {
				logger.log(Level.WARNING, "Unable to extract response message: "+responseJson, e);
			}
		}
		return null;
	}
	
	/**
	 * Helper method to extract the exception message from the JSON response (if any).
	 * 
	 * @return Exception message or null if none exists or if no valid JSON response is available.
	 */
	public String getExceptionMessage() {
		JsonJavaObject responseJson = getResponseJson();
		if (responseJson != null) {
			try {
				JsonJavaObject bssResponse = responseJson.getAsObject("BSSResponse");
				return bssResponse.getAsString("ExceptionMessage");
			} catch (Exception e) {
				logger.log(Level.WARNING, "Unable to extract exception message: "+responseJson, e);
			}
		}
		return null;
	}
	
}
