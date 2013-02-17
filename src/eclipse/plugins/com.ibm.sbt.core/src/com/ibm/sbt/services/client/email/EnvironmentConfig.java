package com.ibm.sbt.services.client.email;

public enum EnvironmentConfig {
	
	INSTANCE;
	
	private final String SBT_SESSION_RESOURCE = "java:comp/env/mail/ibmsbt-session";
	
	public String getSbtSessionResource(){
		return SBT_SESSION_RESOURCE;
	}
}
