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
package com.ibm.sbt.provisioning.sample.app.weightedBSSCall.authentication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import com.ibm.sbt.provisioning.sample.app.services.Authentication;
import com.ibm.sbt.provisioning.sample.app.task.BSSProvisioning;
import com.ibm.sbt.provisioning.sample.app.util.BSSEndpoints;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.WeightedBSSCall;
import com.ibm.sbt.services.client.smartcloud.bss.UserCredentialJsonBuilder;

public class SetOneTimePassword extends WeightedBSSCall<Boolean> {

	private static final Logger logger = Logger.getLogger(SetOneTimePassword.class.getName());
	private String loginName;
	private String password;
	
	public SetOneTimePassword( String loginName, String password ){
		this.loginName = loginName ;
		this.password = password ;
	}
	/**
	 * Method used for triggering the proper HTTP call for setting the one time password of an
	 * organization member, towards the BSS endpoint designed for 
	 * managing an organization member credential
	 */
	@Override
	protected Boolean doCall() throws Exception {
		boolean oneTimePasswordSet = false ;    
		UserCredentialJsonBuilder userCredential = new UserCredentialJsonBuilder();
		userCredential.setLoginName(this.loginName).setNewPassword(this.password);
		logger.fine("triggering call : "+ this.getKey() );
		Authentication.getInstance().getService().setOneTimePassword(userCredential);
		oneTimePasswordSet = true ;
		String[] subscriberReport = BSSProvisioning.getStateTransitionReport().get(this.loginName);
		if( subscriberReport!= null ){
			subscriberReport[3] = new SimpleDateFormat(BSSProvisioning.DATE_FORMAT).format(new Date());
			BSSProvisioning.getSubscriberWeightReport().get(this.loginName)[1]++ ;
		}
		
		logger.info("one time password set for the user "+ this.loginName);
		return oneTimePasswordSet ;
	}

	@Override
	public String getKey() {
		return BSSEndpoints.SER_AUTHENTICATION.getEndpointString();
	}
}
