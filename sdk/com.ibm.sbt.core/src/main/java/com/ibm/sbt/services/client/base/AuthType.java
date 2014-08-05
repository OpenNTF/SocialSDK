package com.ibm.sbt.services.client.base;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.endpoints.Endpoint;

public enum AuthType {
    OAUTH10("oauth1.0a", "oauth"),
    OAUTH20("oauth2.0", "oauth"),
    BASIC("basic", "basic"),
    FORM("form", "form"),
    SSO("sso", "basic");

    private String typeTag;
    private String urlPart;

    public String get(){
        return urlPart;
    }

    public NamedUrlPart getPart() {
        return new NamedUrlPart("authType", get());
    }

    private AuthType(String typeTag, String urlPart) {
        this.urlPart = urlPart;
        this.typeTag = typeTag;
    }

    boolean matchesType(String type) {
        return typeTag.equals(type);
    }

    /**
     * Get authentication type for the endpoint. like basicAuth, oauth etc.
     * 
     * @return
     */
    public static String getAuthTypePart(Endpoint endpoint) {
        //TODO: Add support for SSO authentication

        if (null == endpoint) {
            return BASIC.get(); // default should be basic as per defect 48438
        }

        String authType = endpoint.getAuthType();
        if (StringUtil.isEmpty(authType)) {
            return BASIC.get();
        }

        for (AuthType t : AuthType.values()) {
            if (t.matchesType(authType))
                return t.get();
        }

        return BASIC.get();

    }

}
