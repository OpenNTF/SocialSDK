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
public class SubscriberJsonBuilder extends BaseJsonBuilder {
	
	private JsonField CustomerId = new JsonField("", true, -1);
	private JsonField Role = new JsonField("", true, 255);
	private JsonField FamilyName = new JsonField("", true, 255);
	private JsonField GivenName = new JsonField("", true, 255);
	private JsonField EmailAddress = new JsonField("", true, 255);
	private JsonField NamePrefix = new JsonField("", false, 10);
	private JsonField NameSuffix = new JsonField("", false, 10);
	private JsonField EmployeeNumber = new JsonField("", false, 10);
	private JsonField LanguagePreference = new JsonField("", false, 15);
	private JsonField WorkPhone = new JsonField("", false, 30);
	private JsonField HomePhone = new JsonField("", false, 30);
	private JsonField MobilePhone = new JsonField("", false, 30);
	private JsonField Fax = new JsonField("", false, 30);
	private JsonField JobTitle = new JsonField("", false, 50);
	private JsonField WebSiteAddress = new JsonField("", false, 255);
	private JsonField TimeZone = new JsonField("", false, 255);
	private JsonField Photo = new JsonField("", false, 255);
	
	public static final String SUBSCRIBER = 
				"{" +
				    "\"Subscriber\": {" +
				        "\"CustomerId\": \"%{getCustomerId}\"," +
				        "\"Person\": {" +
				            "\"RoleSet\":  [" +
				                "\"%{getRole}\"" +
				            "]," +
				            "\"FamilyName\": \"%{getFamilyName}\"," +
				            "\"GivenName\": \"%{getGivenName}\"," +
				            "\"EmailAddress\": \"%{getEmailAddress}\"," +
				            "\"NamePrefix\": \"%{getNamePrefix}\"," +
				            "\"NameSuffix\": \"%{getNameSuffix}\"," +
				            "\"EmployeeNumber\": \"%{getEmployeeNumber}\"," +
				            "\"LanguagePreference\": \"%{getLanguagePreference}\"," +
				            "\"WorkPhone\": \"%{getWorkPhone}\"," +
				            "\"MobilePhone\": \"%{getMobilePhone}\"," +
				            "\"HomePhone\": \"%{getHomePhone}\"," +
				            "\"Fax\": \"%{getFax}\"," +
				            "\"JobTitle\": \"%{getJobTitle}\"," +
				            "\"WebSiteAddress\": \"%{getWebSiteAddress}\"," +
				            "\"TimeZone\": \"%{getTimeZone}\"," +
				            "\"Photo\": \"%{getPhoto}\"" +
				        "}" +
				    "}" +
				"}";

	/**
	 * Default constructor
	 */
	public SubscriberJsonBuilder() {
		template = SUBSCRIBER;
	}
	
	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		Object value = CustomerId.getValue();
		return (value == null) ? null : (String)value;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public SubscriberJsonBuilder setCustomerId(String customerId) {
		CustomerId.setValue(customerId);
		return this;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return (String)Role.getValue();
	}

	/**
	 * @param role the role to set
	 */
	public SubscriberJsonBuilder setRole(SubscriberManagementService.Role role) {
		Role.setValue(role.name());
		return this;
	}

	/**
	 * @return the familyName
	 */
	public String getFamilyName() {
		return (String)FamilyName.getValue();
	}

	/**
	 * @param familyName the familyName to set
	 */
	public SubscriberJsonBuilder setFamilyName(String familyName) {
		FamilyName.setValue(familyName);
		return this;
	}

	/**
	 * @return t		return  (String)nName
	 */
	public String getGivenName() {
		return (String)GivenName.getValue();
	}

	/**
	 * @param givenName the givenName to set
	 */
	public SubscriberJsonBuilder setGivenName(String givenName) {
		GivenName.setValue(givenName);
		return this;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return  (String)EmailAddress.getValue();
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public SubscriberJsonBuilder setEmailAddress(String emailAddress) {
		EmailAddress.setValue(emailAddress);
		return this;
	}

	/**
	 * @return the namePrefix
	 */
	public String getNamePrefix() {
		return  (String)NamePrefix.getValue();
	}

	/**
	 * @param namePrefix the namePrefix to set
	 */
	public SubscriberJsonBuilder setNamePrefix(String namePrefix) {
		NamePrefix.setValue(namePrefix);
		return this;
	}

	/**
	 * @return the nameSuffix
	 */
	public String getNameSuffix() {
		return  (String)NameSuffix.getValue();
	}

	/**
	 * @param nameSuffix the nameSuffix to set
	 */
	public SubscriberJsonBuilder setNameSuffix(String nameSuffix) {
		NameSuffix.setValue(nameSuffix);
		return this;
	}

	/**
	 * @return the employeeNumber
	 */
	public String getEmployeeNumber() {
		return  (String)EmployeeNumber.getValue();
	}

	/**
	 * @param employeeNumber the employeeNumber to set
	 */
	public SubscriberJsonBuilder setEmployeeNumber(String employeeNumber) {
		EmployeeNumber.setValue(employeeNumber);
		return this;
	}

	/**
	 * @return the languagePreference
	 */
	public String getLanguagePreference() {
		return  (String)LanguagePreference.getValue();
	}

	/**
	 * @param languagePreference the languagePreference to set
	 */
	public SubscriberJsonBuilder setLanguagePreference(String languagePreference) {
		LanguagePreference.setValue(languagePreference);
		return this;
	}

	/**
	 * @return the workPhone
	 */
	public String getWorkPhone() {
		return  (String)WorkPhone.getValue();
	}

	/**
	 * @param workPhone the workPhone to set
	 */
	public SubscriberJsonBuilder setWorkPhone(String workPhone) {
		WorkPhone.setValue(workPhone);
		return this;
	}

	/**
	 * @return the homePhone
	 */
	public String getHomePhone() {
		return  (String)HomePhone.getValue();
	}

	/**
	 * @param homePhone the homePhone to set
	 */
	public SubscriberJsonBuilder setHomePhone(String homePhone) {
		HomePhone.setValue(homePhone);
		return this;
	}

	/**
	 * @return the mobilePhone
	 */
	public String getMobilePhone() {
		return  (String)MobilePhone.getValue();
	}

	/**
	 * @param mobilePhone the mobilePhone to set
	 */
	public SubscriberJsonBuilder setMobilePhone(String mobilePhone) {
		MobilePhone.setValue(mobilePhone);
		return this;
	}

	/**
	 * @return the fax
	 */
	public String getFax() {
		return  (String)Fax.getValue();
	}

	/**
	 * @param fax the fax to set
	 */
	public SubscriberJsonBuilder setFax(String fax) {
		Fax.setValue(fax);
		return this;
	}

	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return  (String)JobTitle.getValue();
	}

	/**
	 * @param jobTitle the jobTitle to set
	 */
	public SubscriberJsonBuilder setJobTitle(String jobTitle) {
		JobTitle.setValue(jobTitle);
		return this;
	}

	/**
	 * @return the webSiteAddress
	 */
	public String getWebSiteAddress() {
		return  (String)WebSiteAddress.getValue();
	}

	/**
	 * @param webSiteAddress the webSiteAddress to set
	 */
	public SubscriberJsonBuilder setWebSiteAddress(String webSiteAddress) {
		WebSiteAddress.setValue(webSiteAddress);
		return this;
	}

	/**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return  (String)TimeZone.getValue();
	}

	/**
	 * @param timeZone the timeZone to set
	 */
	public SubscriberJsonBuilder setTimeZone(String timeZone) {
		TimeZone.setValue(timeZone);
		return this;
	}

	/**
	 * @return the photo
	 */
	public String getPhoto() {
		return  (String)(String)Photo.getValue();
	}

	/**
	 * @param photo the photo to set
	 */
	public SubscriberJsonBuilder setPhoto(String photo) {
		Photo.setValue(photo);
		return this;
	} 
	
}
