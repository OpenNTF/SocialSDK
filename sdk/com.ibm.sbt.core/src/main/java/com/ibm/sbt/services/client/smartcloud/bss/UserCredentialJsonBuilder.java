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


/**
 * @author mwallace
 *
 */
public class UserCredentialJsonBuilder extends BaseJsonBuilder {
	
	private JsonField LoginName = new JsonField("LoginName", true, -1);
	private JsonField OldPassword = new JsonField("OldPassword", false, -1);
	private JsonField NewPassword = new JsonField("NewPassword", true, -1);
	private JsonField ConfirmPassword = new JsonField("ConfirmPassword", false, -1);
	
	public static final String USER_CREDENTIAL = 
				"{" +
				    "\"UserCredential\": {" +
				        "\"LoginName\": \"%{getLoginName}\"," +
				        "\"OldPassword\": \"%{getOldPassword}\"," +
				        "\"NewPassword\": \"%{getNewPassword}\"," +
				        "\"ConfirmPassword\": \"%{getConfirmPassword}\"" +
				    "}" +
				"}"; 

	/**
	 * Default constructor
	 */
	public UserCredentialJsonBuilder() {
		template = USER_CREDENTIAL;
	}

	/**
	 * @return the loginName
	 */
	public String getLoginName() {
		return (String)LoginName.getValue();
	}

	/**
	 * @param loginName the loginName to set
	 */
	public UserCredentialJsonBuilder setLoginName(String  loginName) {
		LoginName.setValue(loginName);
		return this;
	}

	/**
	 * @return the oldPassword
	 */
	public String getOldPassword() {
		return (String)OldPassword.getValue();
	}

	/**
	 * @param oldPassword the oldPassword to set
	 */
	public UserCredentialJsonBuilder setOldPassword(String  oldPassword) {
		OldPassword.setValue(oldPassword);
		return this;
	}

	/**
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return (String)NewPassword.getValue();
	}

	/**
	 * @param newPassword the newPassword to set
	 */
	public UserCredentialJsonBuilder setNewPassword(String  newPassword) {
		NewPassword.setValue(newPassword);
		return this;
	}

	/**
	 * @return the confirmPassword
	 */
	public String getConfirmPassword() {
		return (String)ConfirmPassword.getValue();
	}

	/**
	 * @param confirmPassword the confirmPassword to set
	 */
	public UserCredentialJsonBuilder setConfirmPassword(String  confirmPassword) {
		ConfirmPassword.setValue(confirmPassword);
		return this;
	}
	
	
}
