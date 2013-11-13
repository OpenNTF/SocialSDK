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

	// Headers
	public static final String REQ_HEADER_CONTENT_TYPE_ATOM = "application/atom+xml";
	public static final String REQ_HEADER_CONTENT_TYPE_PARAM = "Content-Type";

	// Request Parameters
	public static final String	EMAIL					= "email";
	public static final String	USERID					= "userid";
	public static final String	CONNECTIONID			= "connectionId";
	public static final String	CONNECTIONTYPE			= "connectionType";	
	public static final String	OUTPUT					= "output";
	public static final String	FORMAT					= "format";
	public static final String  SOURCEUSERID			= "sourceUserid";
	public static final String  SOURCEEMAIL				= "sourceEmail";
	public static final String  TARGETUSERID			= "targetUserid";
	public static final String  TARGETEMAIL				= "targetEmail";
	
	// field maps for add/update profile
	public static final HashMap<String, String> createFieldsIdentifierMap;
	public static final HashMap<String, String> updateFieldsIdentifierMap;

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

