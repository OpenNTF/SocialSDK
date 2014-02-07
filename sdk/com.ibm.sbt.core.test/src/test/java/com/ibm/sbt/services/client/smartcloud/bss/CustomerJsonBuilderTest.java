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

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;

/**
 * @author mwallace
 *
 */
public class CustomerJsonBuilderTest extends BaseBssTest {
   
	@Test
    public void testCustomer() {
    	try {
    		CustomerJsonBuilder customer = new CustomerJsonBuilder();
    		
        	customer.setOrgName("Test Enterprise");
        	customer.setPhone("999-999-9999");
    		
        	String customerJson = customer.toJson();
        	System.out.println(customerJson);
        	
        	Assert.assertTrue("Error populating customer JSON", customerJson.indexOf("Error") == -1);

        	// validate it's a valid json string
        	JsonJavaObject jsonObject = (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, customerJson);
    		System.out.println(jsonObject);
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error creating customer JSON caused by: "+e.getMessage());
    	}
    }
	
}
