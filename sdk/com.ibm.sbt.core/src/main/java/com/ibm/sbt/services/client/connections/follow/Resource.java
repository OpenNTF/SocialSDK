package com.ibm.sbt.services.client.connections.follow;

import com.ibm.sbt.services.client.base.NamedUrlPart;

public enum Resource {
	;
	
	public static NamedUrlPart get(String resourceId){
		return new NamedUrlPart("resource", resourceId);
	}

}
