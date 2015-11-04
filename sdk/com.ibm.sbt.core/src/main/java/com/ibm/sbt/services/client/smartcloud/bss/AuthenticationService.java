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

import java.io.IOException;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * Use authentication services to change and reset passwords.
 * 
 * @author mwallace
 */
public class AuthenticationService extends BssService {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 */
	public AuthenticationService() {
	}

	/**
	 * Constructor
	 * 
	 * @param endpointName
	 */
	public AuthenticationService(String endpointName) {
		super(endpointName);
	}

	/**
     * Constructor
     * 
     * @param endpointName
     * @param cacheSize
     */
    public AuthenticationService(String endpointName, int cacheSize) {
       super(endpointName, cacheSize);
    }

	/**
     * Constructor
     * 
     * @param endpoint
     */
    public AuthenticationService(Endpoint endpoint) {
        super(endpoint);
    }

	/**
     * Constructor
     * 
     * @param endpoint
     * @param cacheSize
     */
    public AuthenticationService(Endpoint endpoint, int cacheSize) {
    	super(endpoint, cacheSize);
    }
   
    /**
     * Change the password of a subscriber. 
     * The current password must be available to perform this action.
     * 
     * @param userCredential
     * @throws BssException
     * @throws JsonException
     * @throws IOException 
     */
    public void changePassword(UserCredentialJsonBuilder userCredential) throws BssException, IOException, JsonException {
    	changePassword(userCredential.toJson());
    }

    /**
     * Change the password of a subscriber. 
     * The current password must be available to perform this action.
     * 
     * @param userCredentialJson
     * @throws BssException
     * @throws JsonException
     * @throws IOException 
     */
    public void changePassword(String userCredentialJson) throws BssException, JsonException, IOException {
    	JsonJavaObject jsonObject = (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, userCredentialJson);
    	changePassword(jsonObject);
    }

    /**
     * Change the password of a subscriber. 
     * The current password must be available to perform this action.
     * 
     * @param customerObject
     * @return JSON object containing 
     * @throws BssException
     * @throws IOException 
     */
    public void changePassword(JsonJavaObject userCredentialObject) throws BssException {
		try {
			String serviceUrl = BssUrls.API_AUTHENTICATION_CHANGEPASSWORD.format(this);
			Response response = createData(serviceUrl, null, JsonHeader, userCredentialObject, ClientService.FORMAT_JSON);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error changing password {0}", userCredentialObject);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error changing password {0} caused by {1]", userCredentialObject, e.getMessage());			
		} 
    }
    
    /**
     * Reset the password for a subscriber when the current password is not available. 
     * Subscribers are forced to change their reset passwords the next time they log in.
     * 
     * @param loginName
     * @throws BssException
     */
    public void resetPassword(String loginName) throws BssException {
		try {
			String serviceUrl = BssUrls.API_AUTHENTICATION_RESETPASSWORD.format(this, new NamedUrlPart("loginName", loginName));
			Response response = createData(serviceUrl, null, null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error resetting password {0}", loginName);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error resetting password {0} caused by {1}", loginName, e.getMessage());			
		} 
    }
    
    /**
     * Set a one-time password for users after performing checks against the password policy. 
     * Users can log in with this password only once and must change the password the next time they log in.
     * 
     * @param userCredential
     * @throws BssException
     * @throws IOException
     * @throws JsonException
     */
    public void setOneTimePassword(UserCredentialJsonBuilder userCredential) throws BssException, IOException, JsonException {
    	setOneTimePassword(userCredential.toJson());
    }

    /**
     * Set a one-time password for users after performing checks against the password policy. 
     * Users can log in with this password only once and must change the password the next time they log in.
     * 
     * @param userCredentialJson
     * @throws BssException
     * @throws JsonException
     * @throws IOException
     */
    public void setOneTimePassword(String userCredentialJson) throws BssException, JsonException, IOException {
    	JsonJavaObject jsonObject = (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, userCredentialJson);
    	setOneTimePassword(jsonObject);
    }

    /**
     * Set a one-time password for users after performing checks against the password policy. 
     * Users can log in with this password only once and must change the password the next time they log in.
     * 
     * @param userCredentialObject
     * @throws BssException
     */
    public void setOneTimePassword(JsonJavaObject userCredentialObject) throws BssException {
		try {
			String serviceUrl = BssUrls.API_AUTHENTICATION_SETONETIMEPASSWORD.format(this);
			Response response = createData(serviceUrl, null, JsonHeader, userCredentialObject, ClientService.FORMAT_JSON);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error setting one time password {0}", userCredentialObject);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error setting one time password {0} caused by {1}", userCredentialObject, e.getMessage());			
		} 
    }
	
    /**
     * This Service operation sets the password for a user in TAM after conditionally doing checks against the Password policy. 
     * The service does the following:
     *     Checks if the user's account is valid and ACTIVE in TAM. The login operation fails if the user is not a valid TAM user or if the user is SUSPENDED.
     *     If the bypassPolicy parameter is 'false' or not specified, service operation Validates the NewPassword against BSS password policy. The Operation 
     *     fails if the password does not conform to the policy.
     *     If the bypassPolicy parameter is 'true', then service operation will set the new password for the user irrespective of the policy. Please note 
     *     that if the caller chooses to bypass our password policy restrictions then they're responsible for security aspects of the passwords should they be weak or reused.
     * 
     * @param userCredential
     * @param bypassPolicy
     * @throws BssException
     * @throws IOException
     * @throws JsonException
     */
    public void setUserPassword(UserCredentialJsonBuilder userCredential, boolean bypassPolicy) throws BssException, IOException, JsonException {
    	setUserPassword(userCredential.toJson(), bypassPolicy);
    }

    /**
     * This Service operation sets the password for a user in TAM after conditionally doing checks against the Password policy. 
     * The service does the following:
     *     Checks if the user's account is valid and ACTIVE in TAM. The login operation fails if the user is not a valid TAM user or if the user is SUSPENDED.
     *     If the bypassPolicy parameter is 'false' or not specified, service operation Validates the NewPassword against BSS password policy. The Operation 
     *     fails if the password does not conform to the policy.
     *     If the bypassPolicy parameter is 'true', then service operation will set the new password for the user irrespective of the policy. Please note 
     *     that if the caller chooses to bypass our password policy restrictions then they're responsible for security aspects of the passwords should they be weak or reused.
     * 
     * @param userCredentialJson
     * @param bypassPolicy
     * @throws BssException
     * @throws JsonException
     * @throws IOException
     */
    public void setUserPassword(String userCredentialJson, boolean bypassPolicy) throws BssException, JsonException, IOException {
    	JsonJavaObject jsonObject = (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, userCredentialJson);
    	setUserPassword(jsonObject, bypassPolicy);
    }

    /**
     * This Service operation sets the password for a user in TAM after conditionally doing checks against the Password policy. 
     * The service does the following:
     *     Checks if the user's account is valid and ACTIVE in TAM. The login operation fails if the user is not a valid TAM user or if the user is SUSPENDED.
     *     If the bypassPolicy parameter is 'false' or not specified, service operation Validates the NewPassword against BSS password policy. The Operation 
     *     fails if the password does not conform to the policy.
     *     If the bypassPolicy parameter is 'true', then service operation will set the new password for the user irrespective of the policy. Please note 
     *     that if the caller chooses to bypass our password policy restrictions then they're responsible for security aspects of the passwords should they be weak or reused.
     *     
     * @param userCredentialObject
     * @param bypassPolicy
     * @throws BssException
     */
    public void setUserPassword(JsonJavaObject userCredentialObject, boolean bypassPolicy) throws BssException {
		try {
			String serviceUrl = BssUrls.API_AUTHENTICATION_SETUSERPASSWORD.format(this, new NamedUrlPart("bypassPolicy", bypassPolicy ? "true" : "false"));
    		Response response = createData(serviceUrl, null, JsonHeader, userCredentialObject, ClientService.FORMAT_JSON);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error setting one time password {0}", userCredentialObject);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error setting one time password {0} caused by {1}", userCredentialObject, e.getMessage());			
		} 
    }
	
}
