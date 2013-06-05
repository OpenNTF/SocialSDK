package com.ibm.sbt.smartcloud;

import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;

public class Util {

    public static String getThemeUrl() {
    	Endpoint endpoint = EndpointFactory.getEndpoint("smartcloud");
    	return endpoint.getUrl() + "/theming/theme/css/3";
    	
    }
	
    public static String getNavBarUrl() {
    	Endpoint endpoint = EndpointFactory.getEndpoint("smartcloud");
    	return endpoint.getUrl() + "/manage/navbar/banner/smartcloudExt?oneui=3";
    }
    
}
