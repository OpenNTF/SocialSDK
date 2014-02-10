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
public class CustomerJsonBuilder extends BaseJsonBuilder {
	
	private JsonField CustomerNumber = new JsonField("CustomerNumber", false, 50);
	private JsonField OrgName = new JsonField("Organization/OrgName", true, 255);
	private JsonField Phone = new JsonField("Organization/Phone", false, 30);
	private JsonField ContactFamilyName = new JsonField("Organization/Contact/FamilyName", true, 255);
	private JsonField ContactGivenName = new JsonField("Organization/Contact/GivenName", true, 255);
	private JsonField ContactEmailAddress = new JsonField("Organization/Contact/EmailAddress", true, 255);
	private JsonField ContactNamePrefix = new JsonField("Organization/Contact/NamePrefix", false, 10);
	private JsonField ContactNameSuffix = new JsonField("Organization/Contact/NameSuffix", false, 10);
	private JsonField ContactEmployeeNumber = new JsonField("Organization/Contact/EmployeeNumber", false, 10);
	private JsonField ContactLanguagePreference = new JsonField("Organization/Contact/LanguagePreference", false, 15);
	private JsonField ContactWorkPhone = new JsonField("Organization/Contact/WorkPhone", true, 30);
	private JsonField ContactHomePhone = new JsonField("Organization/Contact/HomePhone", false, 30);
	private JsonField ContactMobilePhone = new JsonField("Organization/Contact/MobilePhone", false, 30);
	private JsonField ContactFax = new JsonField("Organization/Contact/Fax", false, 30);
	private JsonField ContactJobTitle = new JsonField("Organization/Contact/JobTitle", false, 50);
	private JsonField ContactWebSiteAddress = new JsonField("Organization/Contact/WebSiteAddress", false, 255);
	private JsonField ContactTimeZone = new JsonField("Organization/Contact/TimeZone", false, 255);
	private JsonField ContactPhoto = new JsonField("Organization/Contact/Photo", false, 255);
	private JsonField CustomerAccountNumber = new JsonField("CustomerAccountSet[1]/AccountNumber", true, 255);
	private JsonField CustomerAccountLocationName = new JsonField("CustomerAccountSet[1]/LocationName", true, 255);
	private JsonField CustomerAccountPaymentMethodType = new JsonField("CustomerAccountSet[1]/PaymentMethodType", true, 25);
	private JsonField CustomerAccountCurrencyType = new JsonField("CustomerAccountSet[1]/CurrencyType", false, 25);
	private JsonField OrganizationAddressLine1 = new JsonField("Organization/AddressSet[1]/AddressLine1", true, 255);
	private JsonField OrganizationAddressLine2  = new JsonField("Organization/AddressSet[1]/AddressLine2", false, 100);
	private JsonField OrganizationAddressType = new JsonField("Organization/AddressSet[1]/AddressType", true, 15);
	private JsonField OrganizationCity = new JsonField("Organization/AddressSet[1]/City", true, 50);
	private JsonField OrganizationCountry = new JsonField("Organization/AddressSet[1]/Country", true, 50);
	private JsonField OrganizationState = new JsonField("Organization/AddressSet[1]/State", true, 50);
	private JsonField OrganizationPostalCode = new JsonField("Organization/AddressSet[1]/PostalCode", true, 20);
	private JsonField CustomerIdentifierType = new JsonField("CustomerIdentifierSet[1]/CustomerIdType", true, 25);
	private JsonField CustomerIdentifierValue = new JsonField("CustomerIdentifierSet[1]/Value", true, 50);
	
	public static final String CUSTOMER =
			  "{ \"Customer\" : {" +
			    "\"Organization\" : {" +
			      "\"OrgName\" : \"%{getOrgName}\"," +
			      "\"Phone\" : \"%{getPhone}\"," +
			      "\"AddressSet\" :  [{" +
			          "\"AddressLine1\" : \"%{getOrganizationAddressLine1}\"," +
			          "\"AddressLine2\" : \"%{getOrganizationAddressLine2}\"," +
			          "\"AddressType\" : \"%{getOrganizationAddressType}\"," +
			          "\"City\" : \"%{getOrganizationCity}\"," +
			          "\"Country\" : \"%{getOrganizationCountry}\"," +
			          "\"PostalCode\" : \"%{getOrganizationPostalCode}\"," +
			          "\"State\" : \"%{getOrganizationState}\"" +
			        "}" +
			      "]," +
			      "\"Contact\" : {" +
			        "\"FamilyName\" : \"%{getContactFamilyName}\"," +
			        "\"GivenName\" : \"%{getContactGivenName}\"," +
			        "\"EmailAddress\" : \"%{getContactEmailAddress}\"," +
			        "\"NamePrefix\" : \"%{getContactNamePrefix}\"," +
			        "\"EmployeeNumber\" : \"%{getContactEmployeeNumber}\"," +
			        "\"LanguagePreference\" : \"%{getContactLanguagePreference}\"," +
			        "\"WorkPhone\" : \"%{getContactWorkPhone}\"," +
			        "\"MobilePhone\" : \"%{getContactMobilePhone}\"," +
			        "\"HomePhone\" : \"%{getContactHomePhone}\"," +
			        "\"Fax\" : \"%{getContactFax}\"," +
			        "\"JobTitle\" : \"%{getContactJobTitle}\"," +
			        "\"WebSiteAddress\" : \"%{getContactWebSiteAddress}\"," +
			        "\"TimeZone\" : \"%{getContactTimeZone}\"," +
			        "\"Photo\" : \"%{getContactPhoto}\"" +
			      "}" +
			    "}," +
			    "\"CustomerAccountSet\" :  [{" +
			        "\"AccountNumber\" : \"%{getCustomerAccountNumber}\"," +
			        "\"LocationName\" : \"%{getCustomerAccountLocationName}\"," +
			        "\"PaymentMethodType\" : \"%{getCustomerAccountPaymentMethodType}\"," +
			        "\"CurrencyType\" : \"%{getCustomerAccountCurrencyType}\"" +
			      "}" +
			    "]," +
			    "\"CustomerIdentifierSet\" :  [{" +
			        "\"Value\" : \"%{getCustomerIdentifierValue}\"," +
			        "\"CustomerIdType\" : \"%{getCustomerIdentifierType}\"" +
			      "}" +
			    "]" +
			  "} }";


	/**
	 * Default constructor
	 */
	public CustomerJsonBuilder() {
		template = CUSTOMER;
	}
	
	/**
	 * @return the customerNumber
	 */
	public String getCustomerNumber() {
		return (String)CustomerNumber.getValue();
	}

	/**
	 * @param customerNumber the customerNumber to set
	 */
	public CustomerJsonBuilder setCustomerNumber(String customerNumber) {
		CustomerNumber.setValue(customerNumber);
		return this;
	}

	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return (String)OrgName.getValue();
	}

	/**
	 * @param orgName the orgName to set
	 */
	public CustomerJsonBuilder setOrgName(String orgName) {
		OrgName.setValue(orgName);
		return this;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return (String)Phone.getValue();
	}

	/**
	 * @param phone the phone to set
	 */
	public CustomerJsonBuilder setPhone(String phone) {
		Phone.setValue(phone);
		return this;
	}

	/**
	 * @return the familyName
	 */
	public String getContactFamilyName() {
		return (String)ContactFamilyName.getValue();
	}

	/**
	 * @param familyName the familyName to set
	 */
	public CustomerJsonBuilder setContactFamilyName(String familyName) {
		ContactFamilyName.setValue(familyName);
		return this;
	}

	/**
	 * @return the givenName
	 */
	public String getContactGivenName() {
		return (String)ContactGivenName.getValue();
	}

	/**
	 * @param givenName the givenName to set
	 */
	public CustomerJsonBuilder setContactGivenName(String givenName) {
		ContactGivenName.setValue(givenName);
		return this;
	}

	/**
	 * @return the emailAddress
	 */
	public String getContactEmailAddress() {
		return (String)ContactEmailAddress.getValue();
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public CustomerJsonBuilder setContactEmailAddress(String emailAddress) {
		ContactEmailAddress.setValue(emailAddress);
		return this;
	}

	/**
	 * @return the namePrefix
	 */
	public String getContactNamePrefix() {
		return (String)ContactNamePrefix.getValue();
	}

	/**
	 * @param namePrefix the namePrefix to set
	 */
	public CustomerJsonBuilder setContactNamePrefix(String namePrefix) {
		ContactNamePrefix.setValue(namePrefix);
		return this;
	}

	/**
	 * @return the nameSuffix
	 */
	public String getContactNameSuffix() {
		return (String)ContactNameSuffix.getValue();
	}

	/**
	 * @param nameSuffix the nameSuffix to set
	 */
	public CustomerJsonBuilder setContactNameSuffix(String nameSuffix) {
		ContactNameSuffix.setValue(nameSuffix);
		return this;
	}

	/**
	 * @return the employeeNumber
	 */
	public String getContactEmployeeNumber() {
		return (String)ContactEmployeeNumber.getValue();
	}

	/**
	 * @param employeeNumber the employeeNumber to set
	 */
	public CustomerJsonBuilder setContactEmployeeNumber(String employeeNumber) {
		ContactEmployeeNumber.setValue(employeeNumber);
		return this;
	}

	/**
	 * @return the languagePreference
	 */
	public String getContactLanguagePreference() {
		return (String)ContactLanguagePreference.getValue();
	}

	/**
	 * @param languagePreference the languagePreference to set
	 */
	public CustomerJsonBuilder setContactLanguagePreference(String languagePreference) {
		ContactLanguagePreference.setValue(languagePreference);
		return this;
	}

	/**
	 * @return the workPhone
	 */
	public String getContactWorkPhone() {
		return (String)ContactWorkPhone.getValue();
	}

	/**
	 * @param workPhone the workPhone to set
	 */
	public CustomerJsonBuilder setContactWorkPhone(String workPhone) {
		ContactWorkPhone.setValue(workPhone);
		return this;
	}

	/**
	 * @return the homePhone
	 */
	public String getContactHomePhone() {
		return (String)ContactHomePhone.getValue();
	}

	/**
	 * @param homePhone the homePhone to set
	 */
	public CustomerJsonBuilder setContactHomePhone(String homePhone) {
		ContactHomePhone.setValue(homePhone);
		return this;
	}

	/**
	 * @return the mobilePhone
	 */
	public String getContactMobilePhone() {
		return (String)ContactMobilePhone.getValue();
	}

	/**
	 * @param mobilePhone the mobilePhone to set
	 */
	public CustomerJsonBuilder setContactMobilePhone(String mobilePhone) {
		ContactMobilePhone.setValue(mobilePhone);
		return this;
	}

	/**
	 * @return the fax
	 */
	public String getContactFax() {
		return (String)ContactFax.getValue();
	}

	/**
	 * @param fax the fax to set
	 */
	public CustomerJsonBuilder setContactFax(String fax) {
		ContactFax.setValue(fax);
		return this;
	}

	/**
	 * @return the jobTitle
	 */
	public String getContactJobTitle() {
		return (String)ContactJobTitle.getValue();
	}

	/**
	 * @param jobTitle the jobTitle to set
	 */
	public CustomerJsonBuilder setContactJobTitle(String jobTitle) {
		ContactJobTitle.setValue(jobTitle);
		return this;
	}

	/**
	 * @return the webSiteAddress
	 */
	public String getContactWebSiteAddress() {
		return (String)ContactWebSiteAddress.getValue();
	}

	/**
	 * @param webSiteAddress the webSiteAddress to set
	 */
	public CustomerJsonBuilder setContactWebSiteAddress(String webSiteAddress) {
		ContactWebSiteAddress.setValue(webSiteAddress);
		return this;
	}

	/**
	 * @return the timeZone
	 */
	public String getContactTimeZone() {
		return (String)ContactTimeZone.getValue();
	}

	/**
	 * @param timeZone the timeZone to set
	 */
	public CustomerJsonBuilder setContactTimeZone(String timeZone) {
		ContactTimeZone.setValue(timeZone);
		return this;
	}

	/**
	 * @return the photo
	 */
	public String getContactPhoto() {
		return (String)ContactPhoto.getValue();
	}

	/**
	 * @param photo the photo to set
	 */
	public CustomerJsonBuilder setContactPhoto(String photo) {
		ContactPhoto.setValue(photo);
		return this;
	}

	/**
	 * @return the customerAccountNumber
	 */
	public String getCustomerAccountNumber() {
		return (String)CustomerAccountNumber.getValue();
	}

	/**
	 * @param customerAccountNumber the customerAccountNumber to set
	 */
	public CustomerJsonBuilder setCustomerAccountNumber(String customerAccountNumber) {
		CustomerAccountNumber.setValue(customerAccountNumber);
		return this;
	}

	/**
	 * @return the customerAccountLocationName
	 */
	public String getCustomerAccountLocationName() {
		return (String)CustomerAccountLocationName.getValue();
	}

	/**
	 * @param customerAccountLocationName the customerAccountLocationName to set
	 */
	public CustomerJsonBuilder setCustomerAccountLocationName(String customerAccountLocationName) {
		CustomerAccountLocationName.setValue(customerAccountLocationName);
		return this;
	}

	/**
	 * @return the customerAccountPaymentMethodType
	 */
	public String getCustomerAccountPaymentMethodType() {
		return (String)CustomerAccountPaymentMethodType.getValue();
	}

	/**
	 * @param customerAccountPaymentMethodType the customerAccountPaymentMethodType to set
	 */
	public CustomerJsonBuilder setCustomerAccountPaymentMethodType(CustomerManagementService.PaymentMethodType paymentMethodType) {
		CustomerAccountPaymentMethodType.setValue(paymentMethodType.name());
		return this;
	}

	/**
	 * @return the customerAccountCurrencyType
	 */
	public String getCustomerAccountCurrencyType() {
		return (String)CustomerAccountCurrencyType.getValue();
	}

	/**
	 * @param customerAccountCurrencyType the customerAccountCurrencyType to set
	 */
	public CustomerJsonBuilder setCustomerAccountCurrencyType(String customerAccountCurrencyType) {
		CustomerAccountCurrencyType.setValue(customerAccountCurrencyType);
		return this;
	}

	/**
	 * @return the organizationAddressLine1
	 */
	public String getOrganizationAddressLine1() {
		return (String)OrganizationAddressLine1.getValue();
	}

	/**
	 * @param organizationAddressLine1 the organizationAddressLine1 to set
	 */
	public CustomerJsonBuilder setOrganizationAddressLine1(String organizationAddressLine1) {
		OrganizationAddressLine1.setValue(organizationAddressLine1);
		return this;
	}

	/**
	 * @return the organizationAddressLine2
	 */
	public String getOrganizationAddressLine2() {
		return (String)OrganizationAddressLine2.getValue();
	}

	/**
	 * @param organizationAddressLine2 the organizationAddressLine2 to set
	 */
	public CustomerJsonBuilder setOrganizationAddressLine2(String organizationAddressLine2) {
		OrganizationAddressLine2.setValue(organizationAddressLine2);
		return this;
	}

	/**
	 * @return the organizationAddressType
	 */
	public String getOrganizationAddressType() {
		return (String)OrganizationAddressType.getValue();
	}

	/**
	 * @param organizationAddressType the organizationAddressType to set
	 */
	public CustomerJsonBuilder setOrganizationAddressType(CustomerManagementService.AddressType addressType) {
		OrganizationAddressType.setValue(addressType.name());
		return this;
	}

	/**
	 * @return the organizationCity
	 */
	public String getOrganizationCity() {
		return (String)OrganizationCity.getValue();
	}

	/**
	 * @param organizationCity the organizationCity to set
	 */
	public CustomerJsonBuilder setOrganizationCity(String organizationCity) {
		OrganizationCity.setValue(organizationCity);
		return this;
	}

	/**
	 * @return the organizationCountry
	 */
	public String getOrganizationCountry() {
		return (String)OrganizationCountry.getValue();
	}

	/**
	 * @param organizationCountry the organizationCountry to set
	 */
	public CustomerJsonBuilder setOrganizationCountry(String organizationCountry) {
		OrganizationCountry.setValue(organizationCountry);
		return this;
	}

	/**
	 * @return the organizationState
	 */
	public String getOrganizationState() {
		return (String)OrganizationState.getValue();
	}

	/**
	 * @param organizationState the organizationState to set
	 */
	public CustomerJsonBuilder setOrganizationState(String organizationState) {
		OrganizationState.setValue(organizationState);
		return this;
	}

	/**
	 * @return the organizationPostalCode
	 */
	public String getOrganizationPostalCode() {
		return (String)OrganizationPostalCode.getValue();
	}

	/**
	 * @param organizationPostalCode the organizationPostalCode to set
	 */
	public CustomerJsonBuilder setOrganizationPostalCode(String organizationPostalCode) {
		OrganizationPostalCode.setValue(organizationPostalCode);
		return this;
	}

	/**
	 * @return the customerIdentifierType
	 */
	public String getCustomerIdentifierType() {
		return (String)CustomerIdentifierType.getValue();
	}

	/**
	 * @param customerIdentifierType the customerIdentifierType to set
	 */
	public CustomerJsonBuilder setCustomerIdentifierType(CustomerManagementService.CustomerIdType customerIdType) {
		CustomerIdentifierType.setValue(customerIdType.name());
		return this;
	}

	/**
	 * @return the customerIdentifierValue
	 */
	public String getCustomerIdentifierValue() {
		return (String)CustomerIdentifierValue.getValue();
	}

	/**
	 * @param customerIdentifierValue the customerIdentifierValue to set
	 */
	public CustomerJsonBuilder setCustomerIdentifierValue(String customerIdentifierValue) {
		CustomerIdentifierValue.setValue(customerIdentifierValue);
		return this;
	}

}
