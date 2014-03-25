package com.ibm.sbt.services.client.connections.follow;

import com.ibm.sbt.services.client.base.NamedUrlPart;

public enum FollowServiceType {
	ACTIVITIES, BLOGS, COMMUNITIES, FORUMS, PROFILES, WIKIS, TAGS;
	
	public NamedUrlPart get(){
		return new NamedUrlPart("serviceType", name().toLowerCase());
	}

	public static NamedUrlPart getByName(String name){
		return valueOf(name).get();
	}
}
