package com.ibm.sbt.services.client.connections.profiles;

public enum ProfileEntity {
	
	/*
	 *  Root for Url Construction
	 *  Community for referring to single community
	 *  communities for referring to multiple community
	 */

	ADMIN("admin"),
	NONADMIN("");
	
	String ProfileEntity;
	
	private ProfileEntity(String ProfileEntity) {
		this.ProfileEntity = ProfileEntity;
	}
	
	public String getProfileEntityType(){
		return ProfileEntity;
	}

}
