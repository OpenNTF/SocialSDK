package com.ibm.sbt.services.client.connections.profiles;

import java.util.HashMap;

public class ProfileContentMap {
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

				{"bldgId" , 		 "X_BUILDING"},
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
