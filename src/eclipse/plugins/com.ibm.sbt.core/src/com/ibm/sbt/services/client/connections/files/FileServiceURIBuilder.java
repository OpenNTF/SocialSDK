/*
 * © Copyright IBM Corp. 2012
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
package com.ibm.sbt.services.client.connections.files;

import java.util.HashMap;
import com.ibm.commons.util.StringUtil;

/**
 * Files Base URL <br>
 * Enumeration which determines the BaseUrl to be used for Executing FileService API
 * 
 * @author Vimal Dhupar
 */
public enum FileServiceURIBuilder {

    FILES("/files"),
    
    GET_SERVICE_DOCUMENT("files/{auth-type}{access-type}/introspection"),
    
    POST_COMMENT_TO_FILE("files/{auth-type}{access-type}/userlibrary/{user-id}/document/{document-id}/feed"),
    
    GET_COMMENTS_FEED_FROM_USER_FILE("files/{auth-type}{access-type}/userlibrary/{user-id}/document/{document-id}/feed", new HashMap<String, String>() {{
       put("category", "comment");
     }}),
     GET_COMMENTS_FEED_FROM_MY_FILE("files/{auth-type}{access-type}/myuserlibrary/document/{document-id}/feed", new HashMap<String, String>() {{
         put("category", "comment");
       }}),
        
    GET_SINGLE_COMMENT_FROM_USER_FILE("files/{auth-type}{access-type}/userlibrary/{user-id}/document/{document-id}/comment/{comment-id}/entry"),
    GET_SINGLE_COMMENT_FROM_MY_FILE("files/{auth-type}{access-type}/myuserlibrary/document/{document-id}/comment/{comment-id}/entry");

    final String baseUrl;
    private final HashMap<String, String> parameters;

    private FileServiceURIBuilder(String baseUrl) {
        this(baseUrl,null);
    }
    private FileServiceURIBuilder(String baseUrl, HashMap<String,String> fixedParameters) {
        this.parameters = fixedParameters == null? fixedParameters  : new HashMap<String, String>();
        this.baseUrl = baseUrl;
    }

    public String populateURL(String accessType, String category, String view,
            String filter, SubFilters subFilters, String resultType, String authType) {
        String ret = baseUrl;
        if (StringUtil.isEmpty(accessType) && StringUtil.isEmpty(category) && StringUtil.isEmpty(view)
                && StringUtil.isEmpty(filter) && subFilters.isEmpty()) {
            accessType = AccessType.AUTHENTICATED.getAccessType();
            category = Categories.MYLIBRARY.getCategory();
            view = Views.FILES.getViews();
            filter = Filters.NULL.getFilters();
        }

        if (accessType != null)
            ret = ret.replace("{access-type}", accessType);
        if (authType != null)
            ret = ret.replace("{auth-type}", authType);
        if (subFilters!=null) {
        if (subFilters.getUserId() != null)
            ret = ret.replace("{user-id}", subFilters.getUserId());
        if (subFilters.getFileId() != null)
            ret = ret.replace("{document-id}", subFilters.getFileId());
        if (subFilters.getCommentId() != null)
            ret = ret.replace("{comment-id}", subFilters.getCommentId());
        }
        return ret;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
    public HashMap<String, String> getParameters() {
        return parameters;
    }

}
