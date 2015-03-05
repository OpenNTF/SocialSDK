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

import java.util.logging.Logger;

import com.ibm.sbt.provisioning.sample.app.services.Authentication;
import com.ibm.sbt.provisioning.sample.app.util.BSSEndpoints;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.WeightedBSSCall;
import com.ibm.sbt.services.client.smartcloud.bss.UserCredentialJsonBuilder;

public class ChangePassword extends WeightedBSSCall<Boolean> {

	private static final Logger logger = Logger.getLogger(ChangePassword.class.getName());
	
	private String loginName;
	private String oldPassword;
	private String newPassword;
	
	public ChangePassword( String loginName, String oldPassword, String newPassword ){
		this.loginName = loginName ;
		this.oldPassword = oldPassword ;
		this.newPassword = newPassword ;
	}
	/**
	 * Method used for triggering the proper HTTP call for changing the password of an
	 * organization member, towards the BSS endpoint designed for 
	 * managing an organization member credential
	 */
	@Override
	protected Boolean doCall() throws Exception {
		boolean passwordChanged = false ;
		UserCredentialJsonBuilder userCredential = new UserCredentialJsonBuilder();
		userCredential.setLoginName(loginName)
					  .setOldPassword(oldPassword)
					  .setNewPassword(newPassword)
					  .setConfirmPassword(newPassword);
		logger.fine("triggering call : "+ this.getKey() );
		Authentication.getInstance().getService().changePassword(userCredential);
		passwordChanged = true ;
		logger.info("password changed for the user "+ this.loginName);
		return passwordChanged ;
	}

	@Override
	public String getKey() {
		return BSSEndpoints.SER_AUTHENTICATION_CHPWD.getEndpointString();
	}
}

