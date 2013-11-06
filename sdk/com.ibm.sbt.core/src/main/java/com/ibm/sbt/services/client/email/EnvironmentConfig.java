package com.ibm.sbt.services.client.email;

public enum EnvironmentConfig {
	
	INSTANCE;
	
	private final String SBT_SESSION_RESOURCE = "java:comp/env/mail/ibmsbt-session";

	private static final String	SBT_SESSION_FACTORY_RESOURCE = "ibmsbt-sessionfactory";
	
	public String getSbtSessionResource(){
		return SBT_SESSION_RESOURCE;
	}
	
	public String getSbtSessionFactoryResource(){
		return SBT_SESSION_FACTORY_RESOURCE;
	}
}
