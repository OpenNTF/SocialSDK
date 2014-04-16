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

import java.util.HashMap;


public class ProfilesConstants {

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
	public static final String SNX_GUID = "com.ibm.snx_profiles.base.guid";
	public static final String SNX_EMAIL = "com.ibm.snx_profiles.base.email";
	public static final String SNX_UID = "com.ibm.snx_profiles.base.uid";
	public static final String SNX_DISTINGUISHEDNAME = "com.ibm.snx_profiles.base.distinguishedName";
	public static final String SNX_DISPLAYNAME = "com.ibm.snx_profiles.base.displayName";
	public static final String SNX_GIVENNAMES = "com.ibm.snx_profiles.base.givenNames";
	public static final String SNX_SURNAME = "com.ibm.snx_profiles.base.surname";
	public static final String SNX_USERSTATE = "com.ibm.snx_profiles.base.userState";
	public static final String GUID = "guid";
	public static final String DISTINGUISHEDNAME = "distinguishedName";
	public static final String DISPLAYNAME = "displayName";
	public static final String GIVENNAMES = "givenNames";
	public static final String SURNAME = "surname";
	public static final String USERSTATE = "userState";
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

	
	// field maps for add/update profile
	public static final HashMap<String, String> createFieldsIdentifierMap;
	public static final HashMap<String, String> updateFieldsIdentifierMap;
	
	public enum VCardFields {
		BUILDING("building", "X_BUILDING"),
		FLOOR("floor", "X_FLOOR"),
		OFFICENAME("officeName", "X_FLOOR"),
		JOBRESP("jobResp", "TITLE"),
		TELEPHONE_NUMBER("telephoneNumber", "TEL;WORK"),
		WORK_LOCATION("workLocation", "ADR;WORK");

		private final String vCardValue;
		private final String entityValue;
		
		VCardFields(String entityValue, String vCardValue){
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

	static 
	{
		createFieldsIdentifierMap = new HashMap<String, String>();
		String[][] createPairs = {

				{"guid",				"com.ibm.snx_profiles.base.guid"},
				{"email",				"com.ibm.snx_profiles.base.email"},
				{"uid",					"com.ibm.snx_profiles.base.uid"},
				{"distinguishedName", 	"com.ibm.snx_profiles.base.distinguishedName"},
				{"displayName", 		"com.ibm.snx_profiles.base.displayName"},
				{"givenNames", 			"com.ibm.snx_profiles.base.givenNames"},
				{"surname", 			"com.ibm.snx_profiles.base.surname"},
				{"userState",			"com.ibm.snx_profiles.base.userState"},
		};
		for (String[] pair : createPairs) {
			createFieldsIdentifierMap.put(pair[0], pair[1]);
		}

		updateFieldsIdentifierMap = new HashMap<String, String>();
		String[][] updatePairs = {

				{"building" , 		 "X_BUILDING"},
				{"floor" ,			 "X_FLOOR"},
				{"officeName",		 "X_OFFICE_NUMBER"},
				{"jobResp",          "TITLE"},
				{"telephoneNumber",  "TEL;WORK"},
				{"workLocation",	 "ADR;WORK"}
		};
		for (String[] pair : updatePairs) {
			updateFieldsIdentifierMap.put(pair[0], pair[1]);
		}
	}
	
}

