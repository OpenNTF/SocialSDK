package com.ibm.sbt.services.client.base;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.endpoints.Endpoint;

public enum AuthType {
	oauth, basic, form;

    /**
     * Get authentication type for the endpoint. like basicAuth, oauth etc.
     * @return
     */
	public static String getAuthTypePart(Endpoint endpoint){
		//TODO: Add support for SSO and Form authentication
    	
		if (null == endpoint) {
			return basic.name(); // default should be basic as per defect 48438
		}

		String authType = endpoint.getAuthType();
		if (StringUtil.isEmpty(authType)) {
			return basic.name();
		}

		String authValue = null;
		if (authType.equalsIgnoreCase("oauth2.0")) {
			authValue = oauth.name();
		} else if (authType.equalsIgnoreCase("oauth1.0a")) {
			authValue = oauth.name();
		} else if (authType.equalsIgnoreCase("basic")) {
			authValue = basic.name();
		} else if (authType.equalsIgnoreCase("form")) {
			authValue = form.name();
		}
		return authValue;
    }
}
