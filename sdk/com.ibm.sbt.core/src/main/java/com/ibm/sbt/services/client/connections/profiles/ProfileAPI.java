package com.ibm.sbt.services.client.connections.profiles;

public enum ProfileAPI {
	
	/*
	 *  Root for Url Construction
	 *  ADMIN for referring to admin API's
	 *  NONADMIN for referring to non-admin API's
	 */

	ADMIN("/admin"),
	NONADMIN("");
	
	String ProfileApi;
	
	private ProfileAPI(String ProfileApi) {
		this.ProfileApi = ProfileApi;
	}
	
	public String getProfileEntityType(){
		return ProfileApi;
	}

}
