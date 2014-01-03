/*
 * © Copyright IBM Corp. 2013
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
package com.ibm.sbt.services.client.connections.cmisfiles;

import com.ibm.commons.util.StringUtil;

/**
 * Class for construction of service URL for CMIS Files Service <br>
 * @author Vimal Dhupar
 */

public enum CMISFilesUrlBuilder {
	
	FILES("/files"),
    GET_SERVICE_DOCUMENT("{files}/basic/cmis/my/servicedoc"),
    GET_MY_FILES("{files}/basic/cmis/repository/{repositoryId}/folderc/snx:files"),
    GET_FILES_SHARED_WITH_ME("{files}/basic/cmis/repository/{repositoryId}/folderc/snx:virtual!.!filessharedwith"),
    GET_MY_COLLECTIONS("{files}/basic/cmis/repository/{repositoryId}/folderc/snx:collections"),
    GET_COLLECTIONS_SHARED_WITH_ME("{files}/basic/cmis/repository/{repositoryId}/folderc/snx:virtual!.!collectionssharedwith"),
    GET_MY_SHARES("{files}/basic/api/myshares/feed"),
    
    ATOM_GET_USER_ID("/connections/opensocial/basic/rest/people/@me/"),
    ATOM_GET_SUBSCRIBER_ID("/connections/opensocial/basic/rest/people/@me/");
    
    private CMISFilesUrlBuilder(String baseUrl) {
    	requestUrl = baseUrl;
    }
    String requestUrl ;
    
    public static String populateURL(String requestUrl, String repositoryId) {
	    if (StringUtil.isEmpty(repositoryId)) {
	    	//TODO
	    } 
	    requestUrl = requestUrl.replace("{repositoryId}", repositoryId);
	    return requestUrl;
    }
    
    public String populateURL(String requestUrl, String repositoryId, String fileId) {
	    if (StringUtil.isEmpty(repositoryId)) {
	    	//TODO
	    } 
	    if (StringUtil.isEmpty(fileId)) {
	    	//TODO
	    } 
	    requestUrl = requestUrl.replace("{repositoryId}", repositoryId);
	    requestUrl = requestUrl.replace("{fileId}", fileId);
	    return requestUrl;
    }
    
    public String getUrl() {
    	return requestUrl.replace("{files}", FILES.requestUrl);
    }
}
    
