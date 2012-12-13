package com.ibm.sbt.services.client.activitystreams;
/**
 * Activity streams ASUser class, allows user to choose User type
 * @author Manish Kataria
 */
public enum ASUser {
	ME("@me"),
	PUBLIC("@public"),
	COMMUNITY("urn:lsid:lconn.ibm.com:communities.community:");
	
	String userType;
	ASUser(String userType){
		this.userType = userType;
	}
	
	public String getUserType(){return userType;}

}
