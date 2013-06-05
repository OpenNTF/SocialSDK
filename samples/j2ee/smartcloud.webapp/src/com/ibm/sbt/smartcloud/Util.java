package com.ibm.sbt.smartcloud;

import javax.servlet.http.HttpServletRequest;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;

public class Util {

    public static String getThemeUrl(HttpServletRequest request) {
    	Endpoint endpoint = EndpointFactory.getEndpoint(getEndpointName(request));
    	return endpoint.getUrl() + "/theming/theme/css/3";
    	
    }
	
    public static String getNavBarUrl(HttpServletRequest request) {
    	Endpoint endpoint = EndpointFactory.getEndpoint(getEndpointName(request));
    	return endpoint.getUrl() + "/manage/navbar/banner/smartcloudExt?oneui=3";
    }
    
    public static String getEndpointName(HttpServletRequest request) {
    	String name = request.getParameter("endpoint");
    	if (StringUtil.isEmpty(name)) {
    		name = "smartcloud";
    	}
    	return name;
    }
	
}
