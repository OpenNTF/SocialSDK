package com.ibm.commons.runtime.beans;

public enum EnvironmentConfig {
	
	INSTANCE;
	
	private final String configfile = "managed-beans.xml";
	private final String configUrl = "url/ibmsbt-managedbeansxml";
	
	public String getEnvironmentConfigFile(){
		return configfile;
	}
	
	public String getEnvironmentConfigUrl(){
		return configUrl;
	}

}
