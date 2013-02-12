package com.ibm.commons.runtime.properties;

public enum EnvironmentConfig {
	
	INSTANCE;
	
	private final String sbtPropertiesFileConfig = "url/ibmsbt-sbtproperties";
	
	public String getSbtPropertiesFileConfig(){
		return sbtPropertiesFileConfig;
	}

}
