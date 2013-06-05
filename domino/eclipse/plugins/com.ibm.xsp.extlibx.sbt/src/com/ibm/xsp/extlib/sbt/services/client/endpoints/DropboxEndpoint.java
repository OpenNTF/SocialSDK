/* ***************************************************************** */
/*
 * © Copyright IBM Corp. 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.xsp.extlib.sbt.services.client.endpoints;

import com.ibm.sbt.services.endpoints.OAuthEndpoint;

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
