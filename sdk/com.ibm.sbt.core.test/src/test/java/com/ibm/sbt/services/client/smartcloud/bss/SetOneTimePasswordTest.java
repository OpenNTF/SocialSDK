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

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.base.JsonEntity;

/**
 * @author mwallace
 *
 */
public class SetOneTimePasswordTest extends BaseBssTest {

    @Test
    public void testSetOneTimePasswordBadRequest() {
    	try {
    		BigInteger subscriberId = addSubscriber();
    		JsonEntity subscriber = getSubscriberById(subscriberId);
    		String loginName = subscriber.getAsString("Subscriber/Person/EmailAddress");
    		System.out.println(loginName);
    		
    		UserCredentialJsonBuilder userCredential = new UserCredentialJsonBuilder();
    		userCredential.setStrict(false);
    		userCredential.setLoginName("")
    					  .setNewPassword("new_password");
    		System.out.println(userCredential.toJson());
    		
    		AuthenticationService authenticationService = getAuthenticationService();
    		authenticationService.setOneTimePassword(userCredential);

    		Assert.fail("Expected bad request response");
    		
    	} catch (BssException be) {
    		// expecting this exception
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.err.println(jsonObject);
    		Assert.assertNotNull("Expected JSON response", jsonObject);
    		Assert.assertEquals("400", be.getResponseCode());
    		Assert.assertNotNull(be.getResponseMessage());
    		Assert.assertEquals("Error", be.getSeverity());
    		Assert.assertNotNull(be.getUserAction());
    		Assert.assertNotNull(be.getMessageId());
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error changing password caused by: "+e.getMessage());    		
    	}
    }
	
    @Test
    public void testSetOneTimePassword() {
    	try {
    		BigInteger subscriberId = addSubscriber();
    		JsonEntity subscriber = getSubscriberById(subscriberId);
    		String loginName = subscriber.getAsString("Subscriber/Person/EmailAddress");
    		System.out.println(loginName);
    		
    		UserCredentialJsonBuilder userCredential = new UserCredentialJsonBuilder();
    		userCredential.setLoginName(loginName)
    					  .setNewPassword("one_time_passw0rd");
    		System.out.println(userCredential.toJson());
    		
    		AuthenticationService authenticationService = getAuthenticationService();
    		authenticationService.setOneTimePassword(userCredential);
    		
    	} catch (BssException be) {
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.err.println(jsonObject);
    		Assert.fail("Error setting one time password because: "+jsonObject);
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error setting one time password caused by: "+e.getMessage());    		
    	}
    }
	
}
