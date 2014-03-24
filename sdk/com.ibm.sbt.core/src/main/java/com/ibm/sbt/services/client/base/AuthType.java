package com.ibm.sbt.services.client.base;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.endpoints.Endpoint;

public enum AuthType {
	OAUTH, BASIC, FORM, SSO;
	
	public String get(){
		return name().toLowerCase();
	}

    /**
     * Get authentication type for the endpoint. like basicAuth, oauth etc.
     * @return
     */
	public static String getAuthTypePart(Endpoint endpoint){
		//TODO: Add support for SSO authentication
    	
		if (null == endpoint) {
			return BASIC.get(); // default should be basic as per defect 48438
		}

		String authType = endpoint.getAuthType();
		if (StringUtil.isEmpty(authType)) {
			return BASIC.get();
		}

		String authValue = null;
		if (authType.equalsIgnoreCase("oauth2.0")) {
			authValue = OAUTH.get();
		} else if (authType.equalsIgnoreCase("oauth1.0a")) {
			authValue = OAUTH.get();
		} else if (authType.equalsIgnoreCase("basic")) {
			authValue = BASIC.get();
		} else if (authType.equalsIgnoreCase("form")) {
			authValue = FORM.get();
		}
		return authValue;
    }

}
