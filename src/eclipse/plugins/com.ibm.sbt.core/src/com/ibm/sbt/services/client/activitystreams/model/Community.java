package com.ibm.sbt.services.client.activitystreams.model;

/**
 * Actor class for persisting Community information from Activity Stream Entry 
 * @author Manish Kataria
 */

public class Community {
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public String getCommunityId() {
		return communityId;
	}
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	private String communityName = "";
	private String communityId = "";

}
