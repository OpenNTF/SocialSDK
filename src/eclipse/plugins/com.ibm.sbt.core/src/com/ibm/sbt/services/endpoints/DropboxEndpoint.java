package com.ibm.sbt.services.endpoints;


/**
 * @author doconnor
 *
 */
public class DropboxEndpoint extends OAuthEndpoint {
    public static final String DEFAULT_API_VERSION = "-1";
    private String apiVersion = DEFAULT_API_VERSION;

    /**
     * 
     */
    public DropboxEndpoint() {
    }

    @Override
    /**
     * @return the apiVersion
     */
    public String getApiVersion() {
        return apiVersion;
    }

    @Override
    /**
     * @param apiVersion the apiVersion to set
     */
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
    

}