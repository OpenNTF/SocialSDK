package com.ibm.commons.runtime.beans;

public enum EnvironmentConfig {
	
	INSTANCE;
	
	private final String configfile = "managed-beans.xml";
	
	public String getEnvironmentConfig(){
		return configfile;
	}

}
