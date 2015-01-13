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
package com.ibm.sbt.provisioning.sample.app.weightedBSSCall.customer;

import java.util.logging.Logger;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.provisioning.sample.app.services.Customer;
import com.ibm.sbt.provisioning.sample.app.util.BSSEndpoints;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.WeightedBSSCall;

public class RegisterCustomer extends WeightedBSSCall<String>{

	private static final Logger logger = Logger.getLogger(RegisterCustomer.class.getName());
	private String customerJson;
	
	public  RegisterCustomer( String customerJson ){
		this.customerJson = customerJson ;
	}
	/**
	 * Method used for triggering the proper HTTP call for registering a 
	 * customer/organization, towards the BSS endpoint designed for 
	 * managing customers
	 */
	@Override
	protected String doCall() throws Exception {
		logger.fine("triggering call : "+ this.getKey() );
    	JsonJavaObject response = Customer.getInstance().getService().registerCustomer(this.customerJson);
    	String customerId = String.valueOf(response.getAsLong("Long"));
    	logger.info("Customer registered !!!");
    	return customerId;
	}

	@Override
	public String getKey() {
		return BSSEndpoints.RES_CUSTOMER.getEndpointString()+":POST";
	}
}
