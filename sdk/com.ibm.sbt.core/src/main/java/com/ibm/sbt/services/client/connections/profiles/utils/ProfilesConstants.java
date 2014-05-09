/*
 * © Copyright IBM Corp. 2013
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

package com.ibm.sbt.services.client.connections.profiles.utils;



public final class ProfilesConstants {

	// Request Parameters
	public static final String EMAIL				= "email";
	public static final String CONNECTIONID			= "connectionId";
	public static final String CONNECTIONTYPE		= "connectionType";	
	public static final String OUTPUT				= "output";
	public static final String FORMAT				= "format";
	public static final String SOURCEUSERID			= "sourceUserid";
	public static final String SOURCEEMAIL			= "sourceEmail";
	public static final String SOURCEKEY			= "sourceKey";
	public static final String TARGETUSERID			= "targetUserid";
	public static final String TARGETKEY			= "targetKey";
	public static final String TARGETEMAIL			= "targetEmail";
	public static final String PROFILES				= "profiles";
	public static final String CONNECTION_UNIQUE_IDENTIFIER = "connectionId";
	public static final String VCARD = "vcard";
	public static final String FULL = "full";
	public static final String CONNECTION_TYPE = "connectionType";
	public static final String OUTPUT_TYPE = "outputType";
	public static final String CONNECTION = "connection";
	public static final String COLLEAGUE = "colleague";
	public static final String PENDING = "pending";
	public static final String ACCEPTED = "accepted";

	/**
	 * Profile ATOM
	 */
	public static final String PROFILE = "profile";
	public static final String CATEGORIES = "categories";
	public static final String SNX_ATTRIB = "com.ibm.snx_profiles.attrib";
	public static final String BEGIN_VCARD = "BEGIN:VCARD";
	public static final String END_VCARD = "END:VCARD";
	public static final String VCARD_V21 = "VERSION:2.1";
	public static final String JOBTITLE = "jobTitle";
	public static final String ADDRESS = "address";
	public static final String STREETADRESS = "streetAddress";
	public static final String EXTENDEDADDRESS = "extendedAddress";
	public static final String LOCALITY = "locality";
	public static final String REGION = "region";
	public static final String POSTALCODE = "postalCode";
	public static final String COUNTRYNAME = "countryName";
	public static final String VCARD_ADDR = "ADR;WORK:;;";

	public enum VCardField {
		ALTERNATE_LAST_NAME("alternateLastname", "X_ALTERNATE_LAST_NAME"),
		BUILDING("bldgId", "X_BUILDING"),
		BLOG_URL("blogUrl", "X_BLOG_URL;VALUE"),
		COUNTRY_CODE("countryCode", "X_COUNTRY_CODE"),
		COURTESY_TITLE("courtesyTitle", "HONORIFIC_PREFIX"),
		DEPT_NUMBER("deptNumber", "X_DEPARTMENT_NUMBER"),
		DESCRIPTION("description", "X_DESCRIPTION"),
		DISPLAY_NAME("displayName", "FN"),
		EMAIL("email", "EMAIL;INTERNET"),
		EMPLOYEE_NUMBER("employeeNumber", "X_EMPLOYEE_NUMBER"),
		EMPLOYEE_TYPE_CODE("employeeTypeCode", "X_EMPTYPE"),
		EMPLOYEE_TYPE_DESC("employeeTypeDesc", "ROLE"),
		EXPERIENCE("experience", "X_EXPERIENCE"),
		FAX_NUMBER("faxNumber", "TEL;FAX"),
		FLOOR("floor", "X_FLOOR"),
		GROUPWARE_EMAIL("groupwareMail", "EMAIL;X_GROUPWARE_MAIL"),
		GUID("guid", "UID"),
		IP_TELEPHONE_NUMBER("ipTelephoneNumber", "TEL;X_IP"),
		IS_MANAGER("isManager", "X_IS_MANAGER"),
		JOBRESP("jobResp", "TITLE"),
		LAST_UPDATE("lastUpdate", "REV"),
		MANAGER_UID("managerUid", "X_MANAGER_UID"),
		MOBILE_NUMBER("mobileNumber", "TEL;CELL"),
		NATIVE_FIRST_NAME("nativeFirstName", "X_NATIVE_FIRST_NAME"),
		NATIVE_LAST_NAME("nativeLastName", "X_NATIVE_LATS_NAME"),
		OFFICENAME("officeName", "X_OFFICE_NUMBER"),
		ORGANIZATION_TITLE("organizationTitle", "ORG"),
		ORG_ID("orgId", "X_ORGANIZATION_CODE"),
		PAGER_ID("pagerId", "X_PAGER_ID"),
		PAGER_SERVICE_PROVIDER("pagerServiceProvider", "X_PAGER_PROVIDER"),
		PAGER_TYPE("pagerType", "X_PAGER_TYPE"),
		PREFERRED_FIRST_NAME("preferredFirstName", "NICKNAME"),
		PREFERRED_LANGUAGE("preferredLanguage", "X_PREFERRED_LANGUAGE"),
		PREFERRED_LAST_NAME("preferredLastName", "X_PREFERRED_LAST_NAME"),
		PHOTO("photo","PHOTO;VALUE=URL"),
		TELEPHONE_NUMBER("telephoneNumber", "TEL;WORK"),
		TIMEZONE("timezone", "TZ"),
		UID("uid", "X_PROFILE_UID"),
		URL("fnUrl", "URL"),
		WORK_LOCATION("workLocation", "ADR;WORK"),
		WORK_LOCATION_CODE("workLocationCode", "X_WORKLOCATION_CODE");

		private final String vCardValue;
		private final String entityValue;
		
		VCardField(String entityValue, String vCardValue){
			this.entityValue = entityValue;
			this.vCardValue = vCardValue;
		}

		public String getVCardValue(){
			return vCardValue;
		}

		public String getEntityValue(){
			return entityValue;
		}
	}
	
	public enum ProfileAttribute {
			GUID("guid", "com.ibm.snx_profiles.base.guid"),
			EMAIL("email", "com.ibm.snx_profiles.base.email"),
			UID("uid", "com.ibm.snx_profiles.base.uid"),
			DISTINGUISHED_NAME("distinguishedName", "com.ibm.snx_profiles.base.distinguishedName"),
			DISPLAY_NAME("displayName", "com.ibm.snx_profiles.base.displayName"),
			GIVEN_NAMES("givenNames", "com.ibm.snx_profiles.base.givenNames"),
			SURNAME("surname", "com.ibm.snx_profiles.base.surname"),
			USER_STATE("userState",	"com.ibm.snx_profiles.base.userState"),
		;
		
		private final String entityName;
		private final String atomName;
		
		ProfileAttribute(String entityName, String atomName){
			this.entityName = entityName;
			this.atomName = atomName;
		}
		
		public String getEntityName(){
			return entityName;
		}
		
		public String getAtomName(){
			return atomName;
		}
	}
}

