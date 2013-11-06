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
    
    GET_SERVICE_DOCUMENT("{files}/{auth-type}{access-type}/introspection"),
    
    POST_COMMENT_TO_FILE("{files}/{auth-type}{access-type}/userlibrary/{user-id}/document/{document-id}/feed"),
    
    GET_COMMENTS_FEED_FROM_USER_FILE("{files}/{auth-type}{access-type}/userlibrary/{user-id}/document/{document-id}/feed", new HashMap<String, String>() {{
       put("category", "comment");
     }}),
     GET_COMMENTS_FEED_FROM_MY_FILE("{files}/{auth-type}{access-type}/myuserlibrary/document/{document-id}/feed", new HashMap<String, String>() {{
         put("category", "comment");
       }}),
        
    GET_SINGLE_COMMENT_FROM_USER_FILE("{files}/{auth-type}{access-type}/userlibrary/{user-id}/document/{document-id}/comment/{comment-id}/entry"),
    GET_SINGLE_COMMENT_FROM_MY_FILE("{files}/{auth-type}{access-type}/myuserlibrary/document/{document-id}/comment/{comment-id}/entry");

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
            String filter, SubFilters subFilters, String resultType) {
        String ret = baseUrl;
        String authType = "basic";
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

    /**
     * constructUrl
     * <p>
     * This method is used to construct the URL for the API execution. The General Pattern of the URL is :: <br>
     * baseUrl {@link FileServiceURIBuilder} + authType(basic or oauth) + AccessType {@link AccessType} + Category
     * {@link Categories} + View {@link Views}+ Filter {@link Filters}+ {SubFilterKey + SubFilters}*
     * {@link SubFilters}+ resultType {@link ResultType}
     * 
     * @param baseUrl
     * @param accessType
     * @param category
     * @param view
     * @param filter
     * @param subFilters
     * @param resultType
     * @return String
     */
    public static String constructUrl(String baseUrl, String accessType, String category, String view,
            String filter, SubFilters subFilters, String resultType) {
        // here we will set the value in API after constructing the url
        // if the user has set these values then ok. otherwise, we set the default to GetMyFiles :
        // /files/basic/api/myuserlibrary/feed
        StringBuilder url = new StringBuilder(baseUrl);
        url.append(FileConstants.SEPARATOR).append("basic");
        // if none of the values have been set, then we set default values here.
        // by default here we are giving the feed of My Files
        if (StringUtil.isEmpty(accessType) && StringUtil.isEmpty(category) && StringUtil.isEmpty(view)
                && StringUtil.isEmpty(filter) && subFilters.isEmpty()) {
            accessType = AccessType.AUTHENTICATED.getAccessType();
            category = Categories.MYLIBRARY.getCategory();
            view = Views.FILES.getViews();
            filter = Filters.NULL.getFilters();
        }

        if (!StringUtil.isEmpty(accessType)) {
            url = url.append(accessType);
        }

        if (!StringUtil.isEmpty(category)) {
            url = url.append(category);
        }
        if (!StringUtil.isEmpty(view)) {
            url = url.append(view);
        }
        if (!StringUtil.isEmpty(filter)) {
            url = url.append(filter);
        }

        if (subFilters != null) {
        	
        	if (!StringUtil.isEmpty(subFilters.getCommunityCollectionId())) {
                url = url.append(subFilters.COMMUNITYCOLLECTION).append(FileConstants.SEPARATOR).append(subFilters.getCommunityCollectionId());
            }
            if (!StringUtil.isEmpty(subFilters.getCommunityLibraryId())) {
                url = url.append(subFilters.COMMUNITYLIBRARY).append(FileConstants.SEPARATOR).append(subFilters.getCommunityLibraryId());
            }
            if (!StringUtil.isEmpty(subFilters.getCollectionId())) {
                url = url.append(subFilters.COLLECTION).append(FileConstants.SEPARATOR)
                        .append(subFilters.getCollectionId());
            }
            if (!StringUtil.isEmpty(subFilters.getUserId())) {
                url = url.append(subFilters.USERLIBRARY).append(FileConstants.SEPARATOR).append(subFilters.getUserId());
            }
            if (!StringUtil.isEmpty(subFilters.getLibraryId())) {
                url = url.append(subFilters.LIBRARY).append(FileConstants.SEPARATOR).append(subFilters.getLibraryId());
            }
            if (!StringUtil.isEmpty(subFilters.getFileId())) {
                url = url.append(subFilters.FILE).append(FileConstants.SEPARATOR).append(subFilters.getFileId());
            }
            if (!StringUtil.isEmpty(subFilters.getCommentId())) {
                url = url.append(subFilters.COMMENT).append(FileConstants.SEPARATOR).append(subFilters.getCommentId());
            }
            if (!StringUtil.isEmpty(subFilters.getRecycleBinDocumentId())) {
                url = url.append(subFilters.RECYCLEBIN).append(FileConstants.SEPARATOR)
                        .append(subFilters.getRecycleBinDocumentId());
            }
            if (!StringUtil.isEmpty(subFilters.getVersionId())) {
                url = url.append(subFilters.VERSION).append(FileConstants.SEPARATOR).append(subFilters.getVersionId());
            }
            if (!StringUtil.isEmpty(subFilters.getDocumentsId())) {
                url = url.append(subFilters.DOCUMENTS).append(FileConstants.SEPARATOR).append(subFilters.getDocumentsId());
            }
        }

        if (!StringUtil.isEmpty(resultType)) {
            url.append(resultType);
        }
        return url.toString();
    }

    
}
