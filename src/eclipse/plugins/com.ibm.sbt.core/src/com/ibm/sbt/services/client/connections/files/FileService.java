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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.connections.files.FileServiceException;
import com.ibm.sbt.services.client.connections.files.model.CommentEntry;
import com.ibm.sbt.services.client.connections.files.model.FileCommentParameterBuilder;
import com.ibm.sbt.services.client.connections.files.model.FileCommentsFeedParameterBuilder;
import com.ibm.sbt.services.client.connections.files.model.FileEntry;
import com.ibm.sbt.services.client.connections.files.model.FileRequestParams;
import com.ibm.sbt.services.client.connections.files.model.FileRequestPayload;
import com.ibm.sbt.services.client.connections.files.model.FilesModerationDocumentEntry;
import com.ibm.sbt.services.client.connections.files.model.Headers;
import com.ibm.sbt.services.client.connections.files.util.ContentMapFiles;
import com.ibm.sbt.services.client.connections.files.util.Messages;
import com.ibm.sbt.services.client.connections.files.util.NamespacesConnections;
import com.ibm.sbt.services.client.ClientService.ContentStream;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.ClientService;

/**
 * FileService can be used to perform File related operations.
 * <p>
 * Relies on the ID's provided by the user to perform the task.
 * 
 * @Represents Connections FileService
 * @author Vimal Dhupar
 * @see http 
 *      ://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation
 *      #action=openDocument&res_title=Files_API_ic40a&content=pdcontent
 */
public class FileService extends BaseService {

    private static char SEPARATOR   = '/';

    static final String sourceClass = FileService.class.getName();
    static final Logger logger      = Logger.getLogger(sourceClass);


    /**
     * Default Constructor - 0 argument constructor Calls the Constructor of BaseService Class.
     * <p>
     * Creates FileService with Default Endpoint.
     */
    public FileService() {
        this(DEFAULT_ENDPOINT_NAME, DEFAULT_CACHE_SIZE);
    }

    /**
     * Constructor
     * 
     * @param endpoint Creates fileservice with specified endpoint.
     */
    public FileService(String endpoint) {
        this(endpoint, DEFAULT_CACHE_SIZE);
    }

    /**
     * Constructor - Creates a service object with specified endpoint and cache size
     * 
     * @param endpoint
     * @param cacheSize Creates CommunityService with specified endpoint and CacheSize
     */
    public FileService(String endpoint, int cacheSize) {
        super(endpoint, cacheSize);
    }

    public void actOnCommentAwaitingApproval(String commentId, String action, String actionReason)
            throws FileServiceException {
        // get thr uri from here ::
        // In the service document, locate the workspace with the <category term="comments-moderation" .../>
        // child element, and then find the collection with the <atom:category term="approval-action" .../>
        // child element, and make a note of the web address in its href attribute.

        FilesModerationDocumentEntry fileModDocEntry = new FilesModerationDocumentEntry(null);
        if (FilesModerationDocumentEntry.data == null) {
            FilesModerationDocumentEntry.setData(this.getFilesModerationServiceDocument());
        }
        // get the request URI from the servide document.
        String requestUri = fileModDocEntry.get(ContentMapFiles.moderationMap.get("commentApprovalUrl")); // TODO
        // If not obtained due to some reason, then construct the url in regular fashion.
        // URI : /files/basic/api/approval/actions/comments
        if (StringUtil.isEmpty(requestUri)) {
            SubFilters subFilters = new SubFilters();
            subFilters.setCommentId("");
            requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(),
                    AccessType.AUTHENTICATED.getAccessType(),
                    Categories.APPROVAL.getCategory(), null, Filters.ACTIONS.getFilters(), subFilters, null);
        }
        Object payload = this.constructPayloadForModeration(commentId, action, actionReason, "comment");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        try {
            super.updateData(requestUri, null, headers, payload, commentId);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInStatusChange);
        }
    }

    public void actOnFileAwaitingApproval(String fileId, String action, String actionReason)
            throws FileServiceException {
        // get the uri
        // In the service document, locate the workspace with the <category term="documents-moderation" .../>
        // child element, and then find the collection with the <atom:category term="approval-action" .../>
        // child element, and make a note of the web address in its href attribute.
        FilesModerationDocumentEntry fileModDocEntry = new FilesModerationDocumentEntry(null);
        if (FilesModerationDocumentEntry.data == null) {
            FilesModerationDocumentEntry.setData(this.getFilesModerationServiceDocument());
        }
        // get the request URI from the servide document.
        String requestUri = fileModDocEntry.get(ContentMapFiles.moderationMap.get("fileApprovalUrl")); // TODO
        // If not obtained due to some reason, then construct the url in regular fashion.
        // URI : /files/basic/api/approval/actions/documents
        if (StringUtil.isEmpty(requestUri)) {
            SubFilters subFilters = new SubFilters();
            subFilters.setDocumentsId("");
            requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(),
                    AccessType.AUTHENTICATED.getAccessType(),
                    Categories.APPROVAL.getCategory(), null, Filters.ACTIONS.getFilters(), subFilters, null);
        }
        Object payload = this.constructPayloadForModeration(fileId, action, actionReason, "file");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        try {
            super.updateData(requestUri, null,  headers, payload, fileId);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInStatusChange);
        }
    }

    public void actOnFlaggedComment(String commentId, String action, String actionReason)
            throws FileServiceException {
        // get thr uri from here ::
        // In the service document, locate the workspace with the <category term="comments-moderation" .../>
        // child element, and then find the collection with the <atom:category term="approval-action" .../>
        // child element, and make a note of the web address in its href attribute.

        FilesModerationDocumentEntry fileModDocEntry = new FilesModerationDocumentEntry(null);
        if (FilesModerationDocumentEntry.data == null) {
            FilesModerationDocumentEntry.setData(this.getFilesModerationServiceDocument());
        }
        // get the request URI from the servide document.
        String requestUri = fileModDocEntry.get(ContentMapFiles.moderationMap.get("commentReviewUrl")); // TODO
        // If not obtained due to some reason, then construct the url in regular fashion.
        // URI : /files/basic/api/review/actions/comments
        if (StringUtil.isEmpty(requestUri)) {
            SubFilters subFilters = new SubFilters();
            subFilters.setCommentId("");
            requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(),
                    AccessType.AUTHENTICATED.getAccessType(),
                    Categories.REVIEW.getCategory(), null, Filters.ACTIONS.getFilters(), subFilters, null);
        }
        Object payload = this.constructPayloadForModeration(commentId, action, actionReason, "comment");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        try {
            super.updateData(requestUri, null, headers, payload, commentId);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInFlaggingComment);
        }
    }

    public void actOnFlaggedFile(String fileId, String action, String actionReason)
            throws FileServiceException {
        // get the uri
        // In the service document, locate the workspace with the <category term="documents-moderation" .../>
        // child element, and then find the collection with the <atom:category term="approval-action" .../>
        // child element, and make a note of the web address in its href attribute.
        FilesModerationDocumentEntry fileModDocEntry = new FilesModerationDocumentEntry(null);
        if (FilesModerationDocumentEntry.data == null) {
            FilesModerationDocumentEntry.setData(this.getFilesModerationServiceDocument());
        }
        // get the request URI from the servide document.
        String requestUri = fileModDocEntry.get(ContentMapFiles.moderationMap.get("fileReviewUrl")); // TODO
        // If not obtained due to some reason, then construct the url in regular fashion.
        // URI : /files/basic/api/review/actions/documents
        if (StringUtil.isEmpty(requestUri)) {
            SubFilters subFilters = new SubFilters();
            subFilters.setDocumentsId("");
            requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(),
                    AccessType.AUTHENTICATED.getAccessType(),
                    Categories.REVIEW.getCategory(), null, Filters.ACTIONS.getFilters(), subFilters, null);
        }
        Object payload = this.constructPayloadForModeration(fileId, action, actionReason, "file");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        try {
            super.updateData(requestUri, null, headers, payload, fileId);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MesssageExceptionInFlaggingFile);
        }
    }

    public CommentEntry addCommentToFile(FileEntry fe, String comment, Map<String, String> parameters) throws FileServiceException {
        return addCommentToFile(fe.getFileId(), comment, fe.getAuthorEntry().getUserUuid(), parameters);
    }


    /**
     * addCommentToFile
     * <p>
     * Rest API used : /files/basic/api/userlibrary/{userid}/document/{document-id}/feed <br>
     * 
     * @param fileId - ID of the file
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @param comment - Comment to be added to the File
     * @param userId - user Id of the person in whose library the file is present
     * @return CommentEntry
     * @throws FileServiceException
     */
    public CommentEntry addCommentToFile(String fileId, String comment, String userId,
            Map<String, String> params) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "addCommentToFile");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        SubFilters subFilters = new SubFilters();
        String category = null;
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        if (StringUtil.isEmpty(userId) || StringUtil.equalsIgnoreCase(userId, null)) {
            throw new FileServiceException(null, Messages.Invalid_UserId);

        } else {
            subFilters.setUserId(userId);
        } 
        String authValue = "basic";
        subFilters.setFileId(fileId);
        subFilters.setUserId(userId);
        String resultType = ResultType.FEED.getResultType();
        String requestUri  =FileServiceURIBuilder.POST_COMMENT_TO_FILE.populateURL(accessType, category, null,
                null,
                subFilters, resultType);
                

        Document payload = this.constructPayloadForComments(comment);
        
        try {
            Response result = (Response) super.createData(requestUri, null, new ClientService.ContentXml(payload,"application/atom+xml"));
            return (CommentEntry)new CommentEntryFeedHandler().createEntity(result);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInCreatingComment);
        }

    }

    /**
     * addFilesToFolder
     * <p>
     * Rest API used : /files/basic/api/collection/{collection-id}/feed <br>
     * 
     * @param folderId ID of the Collection / Folder to which File(s) need to be added.
     * @param listOfFileIds A list of file Ids, which need to be added to the collection.
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileEntryList
     * @throws FileServiceException
     */
    public FileEntryList addFilesToFolder(String folderId, List<String> listOfFileIds,
            Map<String, String> params) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "addFilesToFolder");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        if (StringUtil.isEmpty(folderId)) {
            throw new FileServiceException(null, Messages.Invalid_CollectionId);
        }
        for (String fileId : listOfFileIds) {
            if (StringUtil.isEmpty(fileId)) {
                throw new FileServiceException(null, Messages.Invalid_FileId);
            }
        }
        SubFilters subFilters = new SubFilters();
        subFilters.setCollectionId(folderId);
        String resultType = ResultType.FEED.getResultType();
        if (null == params) {
            params = new HashMap<String, String>();
        }
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, null,
                null,
                subFilters, resultType);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        Object payload = this.constructPayloadForMultipleEntries(listOfFileIds,
                FileRequestParams.ITEMID.getFileRequestParams());

        try {
            Response result;
            result = (Response) super.createData(requestUri, params, headers, payload);
            return new FileEntryFeedHandler().createEntityList(result);
            
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageGenericException);
        }
    }

    /**
     * addFileToFolders
     * <p>
     * Add a file to a folder or multiple folders. <br>
     * Rest API used : /files/basic/api/userlibrary/{user-id}/document/{document-id-or-label}/feed <br>
     * 
     * @param fileId
     * @param folderIds - list of folder Ids to which the file needs to be added.
     * @throws FileServiceException
     */
    public void addFileToFolders(String fileId, List<String> folderIds) throws FileServiceException {
        this.addFileToFolders(fileId, folderIds, null, null);
    }

    /**
     * addFileToFolders
     * <p>
     * Add a file to a folder or multiple folders. <br>
     * Rest API used : /files/basic/api/userlibrary/{user-id}/document/{document-id-or-label}/feed <br>
     * 
     * @param fileId
     * @param folderIds - list of folder Ids to which the file needs to be added.
     * @param params
     * @throws FileServiceException
     */
    public void addFileToFolders(String fileId, List<String> folderIds, Map<String, String> params)
            throws FileServiceException {
        this.addFileToFolders(fileId, folderIds, null, params);
    }

    /**
     * addFileToFolders
     * <p>
     * Add a file to a folder or multiple folders. <br>
     * Rest API used : /files/basic/api/userlibrary/{user-id}/document/{document-id-or-label}/feed <br>
     * 
     * @param fileId
     * @param folderIds - list of folder Ids to which the file needs to be added.
     * @param userId
     * @throws FileServiceException
     */
    public void addFileToFolders(String fileId, List<String> folderIds, String userId)
            throws FileServiceException {
        this.addFileToFolders(fileId, folderIds, userId, null);
    }

    /**
     * addFileToFolders
     * <p>
     * Add a file to a folder or multiple folders. <br>
     * Rest API used : /files/basic/api/userlibrary/{user-id}/document/{document-id-or-label}/feed <br>
     * 
     * @param fileId
     * @param folderIds - list of folder Ids to which the file needs to be added.
     * @param userId
     * @param params
     * @throws FileServiceException
     */
    public void addFileToFolders(String fileId, List<String> folderIds, String userId,
            Map<String, String> params) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "addFileToFolders");
        }
        if (fileId == null) {
            throw new FileServiceException(null, Messages.Invalid_FileEntry);
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = null;
        SubFilters subFilters = new SubFilters();
        subFilters.setFileId(fileId);

        if (StringUtil.isEmpty(userId) || StringUtil.equalsIgnoreCase(userId, null)) {
            logger.log(Level.WARNING, Messages.Invalid_UserId);
            category = Categories.MYLIBRARY.getCategory();
        } else {
            subFilters.setUserId(userId);
        }
        String resultType = ResultType.FEED.getResultType();
        if (null == params) {
            params = new HashMap<String, String>();
        }
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, null,
                null,
                subFilters, resultType);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        headers.put(Headers.ContentLanguage, Headers.UTF);

        Object payload = this.constructPayloadForMultipleEntries(folderIds,
                FileRequestParams.ITEMID.getFileRequestParams(), "collection");
        try {
            super.createData(requestUri, params, headers, payload);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageGenericException);
        }

        // executePost(requestUri, params, headers, payload);
    }

    public CommentEntry createComment(String fileId, String comment) throws FileServiceException {
        return this.createComment(fileId, comment, null, null);
    }

    /**
     * createComment
     * <p>
     * Create a comment programmatically. <br>
     * Rest API Used : /files/basic/api/userlibrary/{userid}/document/{document-id}/feed
     * 
     * @param fileId
     * @param comment
     * @param userId
     * @return
     * @throws FileServiceException
     */
    public CommentEntry createComment(String fileId, String comment, String userId)
            throws FileServiceException {
        return this.createComment(fileId, comment, userId, null);
    }

    /**
     * createComment
     * <p>
     * Create a comment programmatically. <br>
     * Rest API Used : /files/basic/api/userlibrary/{userid}/document/{document-id}/feed
     * 
     * @param fileId
     * @param comment
     * @param userId
     * @param params
     * @return CommentEntry
     * @throws FileServiceException
     */
    public CommentEntry createComment(String fileId, String comment, String userId, Map<String, String> params)
            throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "createComment");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = null;
        SubFilters subFilters = new SubFilters();
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        if (StringUtil.isEmpty(userId)) {
            logger.log(Level.WARNING, Messages.Invalid_UserId);
            category = Categories.MYLIBRARY.getCategory();
        } else {
            subFilters.setUserId(userId);
        }
        subFilters.setFileId(fileId);
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, null,
                null,
                subFilters, resultType);
       
        Document payload = this.constructPayloadForComments(comment);
        Map<String, String> headers = new HashMap<String, String>();
      
        try {
            Response result = (Response) super.createData(requestUri, params, headers, new ClientService.ContentXml(
                    payload, "application/atom+xml"));
            return (CommentEntry) new CommentEntryFeedHandler().createEntity(result);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInCreatingComment);
        }
    }

    public FileEntry createFolder(String name) throws FileServiceException {
        return this.createFolder(name, null, null);
    }

    /**
     * createFolder
     * <p>
     * Rest API used : /files/basic/api/collections/feed
     * 
     * @param name name of the folder to be created
     * @param description description of the folder
     * @param shareWith If the folder needs to be shared, specify the details in this parameter. <br>
     *            Pass Coma separated List of id, (person/community/group) or role(reader/Contributor/owner)
     *            in order
     * @return FileEntry
     * @throws FileServiceException
     */
    public FileEntry createFolder(String name, String description) throws FileServiceException {
        return this.createFolder(name, description, null);
    }

    public FileEntry createFolder(String name, String description, String shareWith)
            throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "createFolder");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String view = Views.FOLDERS.getViews();
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, view,
                null, null,
                resultType);
        Document payload = this.constructPayloadFolder(name, description, shareWith, "create");

        try {
            Response result = (Response) super.createData(requestUri, null,
                    new ClientService.ContentXml(
                            payload, "application/atom+xml"));
            return (FileEntry) new FileEntryFeedHandler().createEntity(result);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInCreatingFolder);
        }

    }

    /**
     * delete
     * <p>
     * Rest API used : /files/basic/api/myuserlibrary/document/{document-id}/entry <br>
     * 
     * @param fileId - id of the file to be deleted
     * @throws FileServiceException
     */
    public void deleteFile(String fileId) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "delete");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = Categories.MYLIBRARY.getCategory();
        SubFilters subFilters = new SubFilters();
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        subFilters.setFileId(fileId);
        String resultType = ResultType.ENTRY.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, null,
                null,
                subFilters, resultType);

        try {
            super.deleteData(requestUri, null, null);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInDeletingFile);
        }

    }

    /**
     * deleteFileFromRecycleBin
     * <p>
     * Permanently deletes a file from the logged in person's recycle bin. <br>
     * Rest API Used : /files/basic/api/myuserlibrary/view/recyclebin/{document-id}/entry
     * 
     * @param fileId
     * @throws FileServiceException
     */
    public void deleteAllFilesFromRecycleBin() throws FileServiceException {
        this.deleteAllFilesFromRecycleBin(null);
    }

    /**
     * deleteAllFilesFromRecycleBin
     * <p>
     * Permanently deletes a file from the specified person's recycle bin. <br>
     * Rest API Used : /files/basic/api/userlibrary/{userid}/view/recyclebin/feed
     * 
     * @param fileId
     * @param userId
     * @throws FileServiceException
     */
    public void deleteAllFilesFromRecycleBin(String userId) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "deleteAllFilesFromRecycleBin");
        }
        // String accessType = AccessType.AUTHENTICATED.getAccessType();
        // String category = null;
        // SubFilters subFilters = new SubFilters();
        // if (StringUtil.isEmpty(userId) || StringUtil.equalsIgnoreCase(userId, null)) {
        // logger.log(Level.WARNING, Messages.InvalidArgument_3);
        // category = Categories.MYLIBRARY.getCategory();
        // } else {
        // subFilters.setUserId(userId);
        // }
        // String view = Views.RECYCLEBIN.getViews();
        // String resultType = ResultType.FEED.getResultType();
        String requestUri = "/files/basic/api/";
        if (StringUtil.isEmpty(userId) || StringUtil.equalsIgnoreCase(userId, null)) {
            requestUri += "myuserlibrary/view/recyclebin/feed";
        } else {
            requestUri += "userlibrary/" + userId + "/view/recyclebin/feed";
        }

        try {
            super.deleteData(requestUri, null, null);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInDeletingFile);
        }

    }

    /**
     * deleteAllVersionsOfFile
     * <p>
     * Removes one or more versions of a file from your library. You cannot delete the current (most recent)
     * version of a file using this request. <br>
     * Rest API Used : /files/basic/api/myuserlibrary/document/{document-id}/feed
     * 
     * @param fileId
     * @param versionLabel - Specifies the latest version to delete. This version and all earlier versions are
     *            deleted. This should not be the current version of the file.
     * @param params > identifier : Indicates how the document is identified in the {document-id} variable
     *            segment of the web address. By default, the lookup operation is performed with the
     *            expectation that the URL contains the value from the <td:uuid>element of a file Atom entry,
     *            so the value uuid is used. Specify label if the URL instead contains the value from the <td:label>
     *            element of the file Atom entry.
     * @throws FileServiceException
     */
    public void deleteAllVersionsOfFile(String fileId, String versionLabel, Map<String, String> params)
            throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "deleteAllVersionsOfFile");
        }
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        if (StringUtil.isEmpty(versionLabel)) {
            throw new FileServiceException(null, Messages.InvalidArgument_VersionLabel);
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = Categories.MYLIBRARY.getCategory();
        SubFilters subFilters = new SubFilters();
        subFilters.setFileId(fileId);
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, null,
                null,
                subFilters, resultType);
        if (null == params) {
            params = new HashMap<String, String>();
        }
        params.put(FileRequestParams.CATEGORY.getFileRequestParams(), "version");
        params.put(FileRequestParams.DELETEFROM.getFileRequestParams(), versionLabel);

        try {
            super.deleteData(requestUri, params, fileId);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInDeletingFile);
        }

    }

    /**
     * deleteComment
     * <p>
     * 
     * @param fileId
     * @param commentId
     * @return
     * @throws FileServiceException
     */
    public void deleteComment(String fileId, String commentId) throws FileServiceException {
        this.deleteComment(fileId, commentId, "");
    }

    /**
     * deleteComment
     * <p>
     * Rest API used : /files/basic/api/userlibrary/{userid}/document/{document-id}/comment/{comment-id}/entry
     * 
     * @param FileEntry specifies the file for which the comment needs to be deleted.
     * @param commentId Id of the comment to be deleted.
     * @throws FileServiceException
     */
    public void deleteComment(String fileId, String commentId, String userId) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "deleteComment");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        SubFilters subFilters = new SubFilters();
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        if (StringUtil.isEmpty(userId) || StringUtil.equalsIgnoreCase(userId, "null")) {
            logger.log(Level.WARNING, Messages.Invalid_UserId);
            Categories.MYLIBRARY.getCategory();
        } else {
            subFilters.setUserId(userId);
        }
        if (StringUtil.isEmpty(commentId)) {
            throw new FileServiceException(null, Messages.Invalid_CommentId);
        }
        subFilters.setFileId(fileId);
        subFilters.setCommentId(commentId);
        String resultType = ResultType.ENTRY.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, null,
                null,
                subFilters, resultType);

        try {
            super.deleteData(requestUri, null, commentId);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInDeletingComment);
        }
    }

    public void deleteFileAwaitingApproval(String fileId) throws FileServiceException {
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        String requestUri = this.getModerationUri(fileId, "approval", "file");
        try {
            super.deleteData(requestUri, null, fileId);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInDeletingFile);
        }
    }

    /**
     * deleteFileFromRecycleBin
     * <p>
     * Permanently deletes a file from the logged in person's recycle bin. <br>
     * Rest API Used : /files/basic/api/myuserlibrary/view/recyclebin/{document-id}/entry
     * 
     * @param fileId
     * @throws FileServiceException
     */
    public void deleteFileFromRecycleBin(String fileId) throws FileServiceException {
        this.deleteFileFromRecycleBin(fileId, null);
    }

    /**
     * deleteFileFromRecycleBin
     * <p>
     * Permanently deletes a file from the specified person's recycle bin. <br>
     * Rest API Used : /files/basic/api/userlibrary/{userid}/view/recyclebin/{document-id}/entry
     * 
     * @param fileId
     * @param userId
     * @throws FileServiceException
     */
    public void deleteFileFromRecycleBin(String fileId, String userId) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "deleteFileFromRecycleBin");
        }
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = null;
        SubFilters subFilters = new SubFilters();
        if (StringUtil.isEmpty(userId) || StringUtil.equalsIgnoreCase(userId, null)) {
            logger.log(Level.WARNING, Messages.Invalid_UserId);
            category = Categories.MYLIBRARY.getCategory();
        } else {
            subFilters.setUserId(userId);
        }
        subFilters.setRecycleBinDocumentId(fileId);
        String resultType = ResultType.ENTRY.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, null,
                null,
                subFilters, resultType);
        try {
            super.deleteData(requestUri, null, fileId);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInDeletingFile);
        }
    }

    /**
     * deleteFileShare
     * <p>
     * Delete a file share programmatically. Only the owner of a file can delete shares from that file.<br>
     * Rest API Used : /files/basic/api/shares/feed?sharedWhat={document-id}
     * 
     * @param fileId - sharedWhat : This is a required parameter. Document uuid. Delete a set of share
     *            resources for the specified document.<br>
     * @throws FileServiceException
     */
    public void deleteFileShare(String fileId) throws FileServiceException {
        this.deleteFileShare(fileId, null);
    }

    /**
     * deleteFileShare
     * <p>
     * Delete a file share programmatically. Only the owner of a file can delete shares from that file.<br>
     * Rest API Used : /files/basic/api/shares/feed?sharedWhat={document-id}
     * 
     * @param fileId - sharedWhat : This is a required parameter. Document uuid. Delete a set of share
     *            resources for the specified document.<br>
     *            - sharedWith : User ID of the user with whom the document has been shared, but you would
     *            like to prevent from having access to it. You can specify more than one person. Separate
     *            multiple user IDs with a comma. Any share resources for the document that have the specified
     *            users as targets of the share will be deleted. The default is to delete the document's
     *            shares with all users.
     * @param userId
     * @throws FileServiceException
     */
    public void deleteFileShare(String fileId, String userId) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "deleteFileShare");
        }
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = Categories.SHARES.getCategory();
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, null,
                null,
                null,
                resultType);
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.SHAREDWHAT.getFileRequestParams(), fileId);
        if (!StringUtil.isEmpty(userId)) {
            params.put(FileRequestParams.SHAREDWITH.getFileRequestParams(), userId);
        }
        try {
            super.deleteData(requestUri, params, fileId);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInDeletingFileShare);
        }
    }

    public void deleteFlaggedComment(String commentId) throws FileServiceException {
        if (StringUtil.isEmpty(commentId)) {
            throw new FileServiceException(null, Messages.Invalid_CommentId);
        }
        String requestUri = this.getModerationUri(commentId, "review", "comment");
        if (StringUtil.isEmpty(requestUri)) {
            logger.log(Level.SEVERE, Messages.Invalid_CommentId);
            return;
        }
        try {
            super.deleteData(requestUri, null, commentId);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInDeletingComment);
        }
    }

    public void deleteFlaggedFiles(String fileId) throws FileServiceException {
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        String requestUri = this.getModerationUri(fileId, "review", "file");
        if (StringUtil.isEmpty(requestUri)) {
            logger.log(Level.SEVERE, Messages.Invalid_FileId);
            return;
        }
        try {
            super.deleteData(requestUri, null, fileId);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInDeletingFile);
        }
    }

    /**
     * deleteFolder
     * <p>
     * Deletes the folder corresponding to the folderId mentioned as input. <br>
     * Rest API Used : /basic/api/collection/{collection-id}/entry
     * 
     * @param folderId
     * @throws FileServiceException
     */
    public void deleteFolder(String folderId) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "deleteFolder");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        SubFilters subFilters = new SubFilters();
        subFilters.setCollectionId(folderId);
        String resultType = ResultType.ENTRY.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, null,
                null,
                subFilters, resultType);
        try {
            super.deleteData(requestUri, null, null);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInDeleteFolder);
        }
    }

    /**
     * flagAsInappropriate
     * <p>
     * To flag a file/comment as inappropriate. <br>
     * Rest API Used : /files/basic/api/reports
     * 
     * @param id - id of the file/comment which needs to be flagged as inappropriate.
     * @param flagReason - reason , why the file/comment is being flagged as inappropriate.
     * @param flagWhat - If flagging file as inappropriate, flagWhat should be the string "file". If flagging
     *            a comment, then flagWhat should be the String "comment".
     * @throws FileServiceException
     */
    public void flagAsInappropriate(String id, String flagReason, String flagWhat)
            throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "flagFile");
        }
        if (StringUtil.isEmpty(id)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        if (StringUtil.isEmpty(flagWhat)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String resultType = ResultType.REPORTS.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, null,
                null, null,
                resultType);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);

        Object payload = this.constructPayloadForFlagging(id, flagReason, flagWhat);
        try {
            super.updateData(requestUri, null, headers, payload, id);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInFlaggingInappropriate);
        }
    }

    public FileEntryList getAllUserFiles(String userId) throws FileServiceException {
        return this.getAllUserFiles(userId, null);
    }

    /**
     * getAllUserFiles
     * <p>
     * Rest API used : /files/basic/anonymous/api/userlibrary/{userid}/feed <br>
     * Public method. No Auth required.
     * 
     * @param userId
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileEntryList
     * @throws FileServiceException
     */

    public FileEntryList getAllUserFiles(String userId, Map<String, String> params)
            throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getPersonLibrary");
        }
        String accessType = AccessType.PUBLIC.getAccessType();
        SubFilters subFilters = new SubFilters();
        if (StringUtil.isEmpty(userId)) {
            throw new FileServiceException(null, Messages.Invalid_UserId);
        }
        subFilters.setUserId(userId);
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, null,
                null,
                subFilters, resultType);

        FileEntryList fileEntries = null;
        try {
            fileEntries = (FileEntryList) getEntities(requestUri, params, new FileEntryFeedHandler());
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }
        return fileEntries;
    }

    public CommentEntryList getCommentsAwaitingApproval(Map<String, String> params)
            throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getCommentsAwaitingApproval");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = Categories.APPROVAL.getCategory();
        String view = Views.COMMENTS.getViews();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, view,
                null,
                null,
                null);

        CommentEntryList commentEntries = null;
        try {
            commentEntries = (CommentEntryList) getEntities(requestUri, params, new CommentEntryFeedHandler());
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }
        
        return commentEntries;
    }

    /**
     * getFile
     * 
     * @param fileId - ID of the file to be fetched from the Connections Server
     * @return FileEntry
     * @throws FileServiceException
     */
    public FileEntry getFile(String fileId) throws FileServiceException {
        return this.getFile(fileId, true);
    }

    /**
     * getFile
     * 
     * @param fileId - ID of the file to be fetched from the Connections Server
     * @param load - a flag to determine whether the network call should be made or an empty placeholder of
     *            the fileEntry object should be returned. load - true : network call is made to fetch the
     *            file load - false : an empty fileEntry object is returned, and then updations can be made on
     *            this object.
     * @return FileEntry
     * @throws FileServiceException
     */
    public FileEntry getFile(String fileId, boolean load) throws FileServiceException {
        return this.getFile(fileId, null, load);
    }

    /**
     * getFile
     * <p>
     * Rest API for getting files :- /files/basic/api/myuserlibrary/document/{document-id}/entry
     * 
     * @param fileId - ID of the file to be fetched from the Connections Server
     * @param parameters - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @param load - a flag to determine whether the network call should be made or an empty placeholder of
     *            the fileEntry object should be returned. load - true : network call is made to fetch the
     *            file load - false : an empty fileEntry object is returned, and then updations can be made on
     *            this object.
     * @return FileEntry
     * @throws FileServiceException
     */

    public FileEntry getFile(String fileId, Map<String, String> parameters, boolean load)
            throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getFile");
        }
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        String requestUri = null;
        FileEntry file = new FileEntry(fileId);
        if (load) {
            SubFilters subFilters = new SubFilters();
            subFilters.setFileId(fileId);
            requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(),
                    AccessType.AUTHENTICATED.getAccessType(),
                    Categories.MYLIBRARY.getCategory(), null, null, subFilters,
                    ResultType.ENTRY.getResultType());

            try {
                return (FileEntry) super.getEntity(requestUri, parameters, new FileEntryFeedHandler());
            } catch (Exception e) {
               throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
            }
        }
        return file;
    }

    public FileEntry getFileAwaitingAction(String fileId) throws FileServiceException {
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        String requestUri = this.getModerationUri(fileId, "approval", "file");
        try {
            return (FileEntry) super.getEntity(requestUri, null, new FileEntryFeedHandler());
        } catch (Exception e) {
           throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }
    }

    /**
     * 
     * @param fileId
     * @param userId
     * @param commentId
     * @param anonymousAccess
     * @param parameters a map of paramters; can be generated using the {@link FileCommentParameterBuilder}
     * @return
     * @throws FileServiceException
     */
            
    public CommentEntryList getUserFileComment(String fileId, String userId, String commentId, boolean anonymousAccess, Map<String, String> parameters, Map<String, String> headers) throws FileServiceException {
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        if (StringUtil.isEmpty(userId)) {
            throw new FileServiceException(null, Messages.Invalid_UserId);
        }
        if (StringUtil.isEmpty(commentId)) {
            throw new FileServiceException(null, Messages.Invalid_CommentId);
        }
        
        return getFileComments(fileId, anonymousAccess, userId, commentId, parameters,headers);
    }
    
    /**
     * retrieve a single comment from a file of the authenticated user
     * @param fileId
     * @param commentId
     * @param parameters a map of paramters; can be generated using the {@link FileCommentsParameterBuilder}
     * @return
     * @throws FileServiceException
     */
    public CommentEntryList getFileComment(String fileId, String commentId, Map<String, String> parameters, Map<String, String> headers) throws FileServiceException {
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        if (StringUtil.isEmpty(commentId)) {
            throw new FileServiceException(null, Messages.Invalid_CommentId);
        }
        
        return getFileComments(fileId, false, null, commentId, parameters, headers);
    }
    /**
     * retrieve all comments from a file of any user
     * @param fileId
     * @param userId
     * @param anonymousAccess try anonymous access - will only work if the file visibility is public
     * @param parameters a map of paramters; can be generated using the {@link FileCommentsFeedParameterBuilder}
     * @return
     * @throws FileServiceException
     */
    public CommentEntryList getAllUserFileComments(String fileId, String userId, boolean anonymousAccess, Map<String,String> parameters) throws FileServiceException {
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        if (StringUtil.isEmpty(userId)) {
            throw new FileServiceException(null, Messages.Invalid_UserId);
        }
        
        return getFileComments(fileId, anonymousAccess, userId, null, parameters,null);
    }
    /**
     * retrieve all comments from a file of the authenticated user
     * @param fileId
     * @param parameters a map of paramters; can be generated using the {@link FileCommentsFeedParameterBuilder}
     * @return
     * @throws FileServiceException
     */
    public CommentEntryList getAllFileComments(String fileId, Map<String, String> parameters) throws FileServiceException {
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        return getFileComments(fileId, false, null, null, parameters,null);
    }
    
    private CommentEntryList getFileComments(String fileId, boolean anonymousAccess, String userId, String commentId, Map<String, String> parameters, Map<String,String> headers) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getFilesComments");
        }
        
        String accessType = null;
        if (anonymousAccess) {
            accessType = AccessType.PUBLIC.getAccessType();
        } else {
            accessType = AccessType.AUTHENTICATED.getAccessType();
        }
        
        SubFilters subFilters = new SubFilters();

        subFilters.setFileId(fileId);
        
        subFilters.setUserId(userId);
        FileServiceURIBuilder uriBuilder = null;
        
        if (StringUtil.isEmpty(commentId)) {
            if (StringUtil.isEmpty(userId)) {
                uriBuilder = FileServiceURIBuilder.GET_COMMENTS_FEED_FROM_MY_FILE;
            } else {
                uriBuilder = FileServiceURIBuilder.GET_COMMENTS_FEED_FROM_USER_FILE;
            }
        } else {
            if (StringUtil.isEmpty(userId)) {
                uriBuilder = FileServiceURIBuilder.GET_SINGLE_COMMENT_FROM_MY_FILE;
            } else {
                uriBuilder = FileServiceURIBuilder.GET_SINGLE_COMMENT_FROM_USER_FILE;
            }
        }
        
        CommentEntryList commentEntries = null;
        try {
            if (parameters == null)
                parameters = uriBuilder.getParameters();
            else
                parameters.putAll(uriBuilder.getParameters());
            //TODO: pass in headers
            commentEntries = (CommentEntryList) getEntities(uriBuilder.populateURL(accessType,   null, null, null, subFilters, null), parameters, headers, new CommentEntryFeedHandler());
            return commentEntries;
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }
    }

    /**
     * getFileFromRecycleBin
     * <p>
     * 
     * @param fileId
     * @return
     * @throws FileServiceException
     */
    public FileEntry getFileFromRecycleBin(String fileId) throws FileServiceException {
        return this.getFileFromRecycleBin(fileId, null, null);
    }

    /**
     * getFileFromRecycleBin
     * <p>
     * 
     * @param fileId
     * @param userId
     * @return
     * @throws FileServiceException
     */
    public FileEntry getFileFromRecycleBin(String fileId, String userId) throws FileServiceException {
        return this.getFileFromRecycleBin(fileId, userId, null);
    }

    /**
     * getFileFromRecycleBin
     * <p>
     * Retrieve a file from the recycle bin. This method returns the Atom document of the file. <br>
     * Rest API Used : /files/basic/api/userlibrary/{userid}/view/recyclebin/{document-id}/entry
     * 
     * @param fileId Id of the file in recycle bin.
     * @param userId
     * @param params
     * @return FileEntry
     * @throws FileServiceException
     */
    public FileEntry getFileFromRecycleBin(String fileId, String userId, Map<String, String> params)
            throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getFileFromRecycleBin");
        }
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = null;
        SubFilters subFilters = new SubFilters();
        if (StringUtil.isEmpty(userId) || StringUtil.equalsIgnoreCase(userId, null)) {
            logger.log(Level.WARNING, Messages.Invalid_UserId);
            category = Categories.MYLIBRARY.getCategory();
        } else {
            subFilters.setUserId(userId);
        }
        subFilters.setRecycleBinDocumentId(fileId);
        String resultType = ResultType.ENTRY.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, null,
                null,
                subFilters, resultType);
        try {
            return (FileEntry) super.getEntity(requestUri, params, new FileEntryFeedHandler());
        } catch (Exception e) {
           throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }
    }

    // /files/basic/api/approval/documents
    /**
     * getFilesAwaitingApproval
     * 
     * @param params
     * @return
     * @throws FileServiceException
     */
    public FileEntryList getFilesAwaitingApproval(Map<String, String> params) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getFilesAwaitingApproval");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = Categories.APPROVAL.getCategory();
        String view = Views.FILES.getViews();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, view,
                null,
                null,
                null);

        FileEntryList fileEntries = null;
        try {
            fileEntries = (FileEntryList) getEntities(requestUri, params, new FileEntryFeedHandler());
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }

        return fileEntries;
    }

    /**
     * getFileShares
     * <p>
     * Get a feed that lists the share entries. A share entry describes an instance in which access that has
     * been given to a file. <br>
     * Rest API used : /files/basic/api/documents/shared/feed
     * 
     * @return
     * @throws FileServiceException
     */
    public FileEntryList getFileShares() throws FileServiceException {
        return this.getFileShares(null);
    }

    /**
     * getFileShares
     * <p>
     * Get a feed that lists the share entries. A share entry describes an instance in which access that has
     * been given to a file. <br>
     * Rest API used : /files/basic/api/documents/shared/feed
     * 
     * @param params
     * @return
     * @throws FileServiceException
     */
    public FileEntryList getFileShares(Map<String, String> params) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getFileShares");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String view = Views.FILES.getViews();
        String filter = Filters.SHARED.getFilters();
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, view,
                filter,
                null,
                resultType);

        FileEntryList fileEntries = null;
        try {
            fileEntries = (FileEntryList) getEntities(requestUri, params, new FileEntryFeedHandler());
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }

        return fileEntries;
    }

    public FileEntryList getFilesInFolder(String folderId) throws FileServiceException {
        return this.getFilesInFolder(folderId, null);
    }

    /**
     * getFilesInFolder
     * <p>
     * Rest API used : /files/basic/api/collection/{collection-id}/feed
     * 
     * @param folderId - uuid of the folder/collection.
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileEntryList
     * @throws FileServiceException
     */

    public FileEntryList getFilesInFolder(String folderId, Map<String, String> params)
            throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getFilesInFolder");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        SubFilters subFilters = new SubFilters();
        if (StringUtil.isEmpty(folderId)) {
            throw new FileServiceException(null, Messages.Invalid_CollectionId);
        }
        subFilters.setCollectionId(folderId);
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, null,
                null,
                subFilters, resultType);

        FileEntryList fileEntries = null;
        try {
            fileEntries = (FileEntryList) getEntities(requestUri, params, new FileEntryFeedHandler());
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }

        return fileEntries;
    }

    /**
     * getFilesInMyRecycleBin
     * 
     * @return
     * @throws FileServiceException
     */
    public FileEntryList getFilesInMyRecycleBin() throws FileServiceException {
        return this.getFilesInMyRecycleBin(null);
    }

    // content type must be specified as application/atom+xml.
    // /basic/api/collection/{collection-id}/entry
    // public FileEntry updateFolder(String folderId, Document updatedFolder) throws FileServiceException {
    // if (logger.isLoggable(Level.FINEST)) {
    // logger.entering(sourceClass, "getFolder");
    // }
    // String accessType = AccessType.AUTHENTICATED.getAccessType();
    // SubFilters subFilters = new SubFilters();
    // subFilters.setCollectionId(folderId);
    // String resultType = ResultType.ENTRY.getResultType();
    // String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, null, null,
    // subFilters, resultType);
    // return (FileEntry) executeGet(requestUri, null, ClientService.FORMAT_XML, FileEntry.class).get(0);
    // }

    /**
     * getFilesInMyRecycleBin
     * <p>
     * Rest API used : /files/basic/api/myuserlibrary/view/recyclebin/feed
     * 
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileEntryList
     * @throws FileServiceException
     */

    public FileEntryList getFilesInMyRecycleBin(Map<String, String> params) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getFilesInMyRecycleBin");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = Categories.MYLIBRARY.getCategory();
        String view = Views.RECYCLEBIN.getViews();
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, view,
                null,
                null,
                resultType);

        FileEntryList fileEntries = null;
        try {
            fileEntries = (FileEntryList) getEntities(requestUri, params, new FileEntryFeedHandler());
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }

        return fileEntries;
    }

    /**
     * getFilesModerationServiceDocument
     * <p>
     * Rest API Used : /files/basic/api/moderation/atomsvc
     * 
     * @return
     */
    protected Document getFilesModerationServiceDocument() {

        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getFilesModerationServiceDocument");
        }

        String requestUri = FileServiceURIBuilder.GET_SERVICE_DOCUMENT.populateURL(AccessType.AUTHENTICATED.toString(), null, null, null, null, null);
        Response result = null;
        try {
            result = super.retrieveData(requestUri, null);
        } catch (Exception e) {
            logger.log(Level.SEVERE, Messages.MessageGenericException + "getFilesModerationServiceDocument()", e);
        }
        return (Document) result.getData();
    }

    /**
     * getFilesServiceDocument
     * <p>
     * Rest API Used : /files/basic/api/introspection
     * 
     * @return
     * @throws FileServiceException 
     */
    public Document getFilesServiceDocument() throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getFilesServiceDocument");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String resultType = ResultType.INTROSPECTION.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, null,
                null, null,
                resultType);
        Object result = null;
        try {
            result = this.getClientService().get(requestUri, null, ClientService.FORMAT_XML);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInFetchingServiceDocument);
        }
        return (Document) result;
    }

    /**
     * getFilesSharedByMe
     * <p>
     * This method calls getFilesSharedByMe(Map<String, String> params) with null params
     * 
     * @return FileEntryList
     * @throws FileServiceException
     */
    public FileEntryList getFilesSharedByMe() throws FileServiceException {
        return this.getFilesSharedByMe(null);
    }

    /**
     * getFilesSharedByMe
     * <p>
     * Rest API used : /files/basic/api/documents/shared/feed <br>
     * This method is used to get Files Shared By the person.
     * 
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileEntryList
     * @throws FileServiceException
     */

    public FileEntryList getFilesSharedByMe(Map<String, String> params) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getFilesSharedWithMe");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String view = Views.FILES.getViews();
        String filter = Filters.SHARED.getFilters();
        String resultType = ResultType.FEED.getResultType();
        if (null == params) {
            params = new HashMap<String, String>();
        }
        params.put(FileRequestParams.DIRECTION.getFileRequestParams(), "outbound");
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, view,
                filter,
                null,
                resultType);

        FileEntryList fileEntries = null;
        try {
            fileEntries = (FileEntryList) getEntities(requestUri, params, new FileEntryFeedHandler());
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }

        return fileEntries;
    }

    /**
     * getFilesSharedWithMe
     * <p>
     * calls getFilesSharedWithMe(Map<String, String> params) with null params
     * 
     * @return FileEntryList
     * @throws FileServiceException
     */

    public FileEntryList getFilesSharedWithMe() throws FileServiceException {
        return this.getFilesSharedWithMe(null);
    }

    /**
     * getFilesSharedWithMe
     * <p>
     * Rest API used : /files/basic/api/documents/shared/feed <br>
     * This method is used to get Files Shared With the person.
     * 
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileEntryList
     * @throws FileServiceException
     */

    public FileEntryList getFilesSharedWithMe(Map<String, String> params) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getFilesSharedWithMe");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String view = Views.FILES.getViews();
        String filter = Filters.SHARED.getFilters();
        String resultType = ResultType.FEED.getResultType();
        if (null == params) {
            params = new HashMap<String, String>();
        }
        params.put(FileRequestParams.DIRECTION.getFileRequestParams(), "inbound");
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, view,
                filter,
                null,
                resultType);

        FileEntryList fileEntries = null;
        try {
            fileEntries = (FileEntryList) getEntities(requestUri, params, new FileEntryFeedHandler());
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }

        return fileEntries;
    }

    /**
     * getFileWithGivenVersion
     * <p>
     * Retrieve an Atom document representation of a version of a file from your library.<br>
     * Rest API Used : /files/basic/api/myuserlibrary/document/{document-id}/version/{version-id}/entry
     * 
     * @param fileId
     * @param versionId
     * @return FileEntry
     * @throws FileServiceException
     */
    public FileEntry getFileWithGivenVersion(String fileId, String versionId) throws FileServiceException {
        return this.getFileWithGivenVersion(fileId, versionId, null, null);
    }

    /**
     * getFileWithGivenVersion
     * <p>
     * Retrieve an Atom document representation of a version of a file from your library.<br>
     * Rest API Used : /files/basic/api/myuserlibrary/document/{document-id}/version/{version-id}/entry
     * 
     * @param fileId
     * @param versionId
     * @param params > acls : Indicates whether permissions should be retrieved and returned with the version
     *            entry. Options are true or false. The default value is false.<br>
     *            > identifier : Indicates how the document is identified in the {document-id} variable
     *            segment of the web address. By default, the lookup operation is performed with the
     *            expectation that the URL contains the value from the <td:uuid>element of a document Atom
     *            entry, so the value uuid is used. Specify label if the URL instead contains the value from
     *            the <td:label>element of a document Atom entry.<br>
     *            > inline : Specifies whether the version content should be included in the content element
     *            of the returned Atom document. Options are true or false. The default value is false.
     * @return FileEntry
     * @throws FileServiceException
     */
    public FileEntry getFileWithGivenVersion(String fileId, String versionId, Map<String, String> params)
            throws FileServiceException {
        return this.getFileWithGivenVersion(fileId, versionId, params, null);
    }

    /**
     * getFileWithGivenVersion
     * <p>
     * Retrieve an Atom document representation of a version of a file from your library.<br>
     * Rest API Used : /files/basic/api/myuserlibrary/document/{document-id}/version/{version-id}/entry
     * 
     * @param fileId
     * @param versionId
     * @param params > acls : Indicates whether permissions should be retrieved and returned with the version
     *            entry. Options are true or false. The default value is false.<br>
     *            > identifier : Indicates how the document is identified in the {document-id} variable
     *            segment of the web address. By default, the lookup operation is performed with the
     *            expectation that the URL contains the value from the <td:uuid>element of a document Atom
     *            entry, so the value uuid is used. Specify label if the URL instead contains the value from
     *            the <td:label>element of a document Atom entry.<br>
     *            > inline : Specifies whether the version content should be included in the content element
     *            of the returned Atom document. Options are true or false. The default value is false.
     * @param headers {@link FileReuestHeaders}<br>
     *            > If-Modified-Since : Used to validate the local cache of the feed and entry documents
     *            retrieved previously. If the feed or entry has not been modified since the specified date,
     *            HTTP response code 304 (Not Modified) is returned. <br>
     *            > If-None-Match : Contains an ETag response header sent by the server in a previous request
     *            to the same URL. If the ETag is still valid for the specified resource, HTTP response code
     *            304 (Not Modified) is returned.
     * @return FileEntry
     * @throws FileServiceException
     */
    public FileEntry getFileWithGivenVersion(String fileId, String versionId, Map<String, String> params,
            Map<String, String> headers) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getFileWithGivenVersion");
        }
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        if (StringUtil.isEmpty(versionId)) {
            logger.log(Level.WARNING, Messages.Invalid_VersionId + Messages.Message_RetrievalError);
            return this.getFile(fileId, params, true);
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = Categories.MYLIBRARY.getCategory();
        SubFilters subFilters = new SubFilters();
        subFilters.setFileId(fileId);
        subFilters.setVersionId(versionId);
        String resultType = ResultType.ENTRY.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, null,
                null,
                subFilters, resultType);

        FileEntry fileEntry;
        try {
            fileEntry = (FileEntry) getEntity(requestUri, params, new FileEntryFeedHandler());
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }

        return fileEntry;
    }

    public CommentEntry getFlaggedComment(String commentId) throws FileServiceException {
        if (StringUtil.isEmpty(commentId)) {
            throw new FileServiceException(null, Messages.Invalid_CommentId);
        }
        String requestUri = this.getModerationUri(commentId, "review", "comment");
        CommentEntry commentEntry;
        try {
            commentEntry = (CommentEntry) getEntity(requestUri, null, new CommentEntryFeedHandler());
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }

        return commentEntry;
    }

    // /files/basic/api/review/comments
    public CommentEntryList getFlaggedComments(Map<String, String> params) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getFlaggedComments");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = Categories.REVIEW.getCategory();
        String view = Views.COMMENTS.getViews();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, view,
                null,
                null,
                null);

        CommentEntryList commentEntries = null;
        try {
            commentEntries = (CommentEntryList) getEntities(requestUri, params, new FileEntryFeedHandler());
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }

        return commentEntries;
    }

    public FileEntry getFlaggedFile(String fileId) throws FileServiceException {
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        String requestUri = this.getModerationUri(fileId, "review", "file");
        FileEntry fileEntry;
        try {
            fileEntry = (FileEntry) getEntity(requestUri, null, new FileEntryFeedHandler());
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }

        return fileEntry;
    }

    // /files/basic/api/review/actions/documents/{document-id}
    public Document getFlaggedFileHistory(String fileId, Map<String, String> params)
            throws FileServiceException {
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = Categories.REVIEW.getCategory();
        String filter = Filters.ACTIONS.getFilters();
        SubFilters subFilters = new SubFilters();
        subFilters.setDocumentsId(fileId);
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, null,
                filter,
                subFilters, null);
        try {
            return (Document) super.retrieveData(requestUri, params).getData();
        } catch (Exception e) {
           throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }

    }

    // /files/basic/api/review/documents
    public FileEntryList getFlaggedFiles(Map<String, String> params) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getFlaggedFiles");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = Categories.REVIEW.getCategory();
        String view = Views.FILES.getViews();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, view,
                null,
                null,
                null);

        FileEntryList fileEntries = null;
        try {
            fileEntries = (FileEntryList) getEntities(requestUri, params, new FileEntryFeedHandler());
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }

        return fileEntries;
    }

    /**
     * getFolder
     * <p>
     * retrieves the atom entry document of the folder, whose folder id is mentioned as input. <br>
     * Rest API used : /basic/api/collection/{collection-id}/entry
     * 
     * @param folderId
     * @return
     * @throws FileServiceException
     */
    public FileEntry getFolder(String folderId) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getFolder");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        SubFilters subFilters = new SubFilters();
        subFilters.setCollectionId(folderId);
        String resultType = ResultType.ENTRY.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, null,
                null,
                subFilters, resultType);
        FileEntry fileEntry;
        try {
            fileEntry = (FileEntry) getEntity(requestUri, null, new FileEntryFeedHandler());
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }

        return fileEntry;
    }

    public FileEntryList getFoldersWithRecentlyAddedFiles() throws FileServiceException {
        return this.getFoldersWithRecentlyAddedFiles(null);
    }

    /**
     * getFoldersWithRecentlyAddedFiles
     * <p>
     * Rest API used : /files/basic/api/collections/addedto/feed
     * 
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileEntryList
     * @throws FileServiceException
     */
    public FileEntryList getFoldersWithRecentlyAddedFiles(Map<String, String> params)
            throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getFoldersWithRecentlyAddedFiles");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String view = Views.FOLDERS.getViews();
        String filter = Filters.ADDEDTO.getFilters();
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, view,
                filter,
                null,
                resultType);

        FileEntryList fileEntries = null;
        try {
            fileEntries = (FileEntryList) getEntities(requestUri, params, new FileEntryFeedHandler());
        } catch (Exception e) {
            throw new FileServiceException(e,
                    Messages.MessageExceptionInReadingObject);
        }

        return fileEntries;
    }

    /**
     * getMyFiles
     * <p>
     * calls getMyFiles(Map<String, String> params) internally with null parameters, if user has not specific
     * any params
     * 
     * @return FileEntryList
     * @throws FileServiceException
     */

    public FileEntryList getMyFiles() throws FileServiceException {
        return this.getMyFiles(null);
    }

    /**
     * getMyFiles
     * <p>
     * Rest API used : /files/basic/api/myuserlibrary/feed <br>
     * This method is used to get Files of the person.
     * 
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileEntryList
     * @throws FileServiceException
     */

    public FileEntryList getMyFiles(Map<String, String> params) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getMyFiles");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = Categories.MYLIBRARY.getCategory();
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, null,
                null,
                null,
                resultType);

        FileEntryList fileEntries = null;
        try {
            fileEntries = (FileEntryList) getEntities(requestUri, params, new FileEntryFeedHandler());
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }

        return fileEntries;
    }

    public FileEntryList getMyFolders() throws FileServiceException {
        return this.getMyFolders(null);
    }

    /**
     * getMyFolders
     * <p>
     * Rest API used : /files/basic/api/collections/feed Required Parameters : creator={snx:userid}
     * 
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileEntryList
     * @throws FileServiceException
     */
    public FileEntryList getMyFolders(Map<String, String> params) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getMyFolders");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String view = Views.FOLDERS.getViews();
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, view,
                null, null,
                resultType);
        FileEntryList fileEntries = null;
        try {
            fileEntries = (FileEntryList) getEntities(requestUri, params, new FileEntryFeedHandler());
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }

        return fileEntries;
    }

    /**
     * getNonce
     * <p>
     * Returns the Cryptographic Key - Nonce value obtained from Connections Server <br>
     * Rest API used : /files/basic/api/nonce
     * 
     * @return String - nonce value
     * @throws FileServiceException 
     */
    public String getNonce() throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getNonce");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String resultType = ResultType.NONCE.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, null,
                null, null,
                resultType);
        Object result = null;
        try {
            result = this.getClientService().get(requestUri, null, ClientService.FORMAT_TEXT);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInGettingNonce);
        }
        if (result == null) {
            logger.log(Level.SEVERE, Messages.MessageNonceValue + result);
        }
        return (String)((Response) result).getData();
    }

    public FileEntryList getPinnedFiles() throws FileServiceException {
        return this.getPinnedFiles(null);
    }

    /**
     * getPinnedFiles
     * <p>
     * Rest API used : /files/basic/api/myfavorites/documents/feed
     * 
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileEntryList
     * @throws FileServiceException
     */
    public FileEntryList getPinnedFiles(Map<String, String> params) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getPinnedFiles");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = Categories.PINNED.getCategory();
        String view = Views.FILES.getViews();
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, view,
                null,
                null,
                resultType);

        FileEntryList fileEntries = null;
        try {
            fileEntries = (FileEntryList) getEntities(requestUri, params, new FileEntryFeedHandler());
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }

        return fileEntries;
    }

    public FileEntryList getPinnedFolders() throws FileServiceException {
        return this.getPinnedFolders(null);
    }

    /**
     * getMyPinnedFolders
     * <p>
     * Rest API used : /files/basic/api/myfavorites/collections/feed
     * 
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileEntryList
     * @throws FileServiceException
     */
    public FileEntryList getPinnedFolders(Map<String, String> params) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getMyPinnedFolders");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = Categories.PINNED.getCategory();
        String view = Views.FOLDERS.getViews();
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, view,
                null,
                null,
                resultType);
        FileEntryList fileEntries = null;
        try {
            fileEntries = (FileEntryList) getEntities(requestUri, params, new FileEntryFeedHandler());
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }

        return fileEntries;
    }

    public FileEntryList getPublicFiles() throws FileServiceException {
        return this.getPublicFiles(null);
    }

    /**
     * getPublicFiles
     * <p>
     * Rest API used : /files/basic/anonymous/api/documents/feed <br>
     * This method returns a list of Public Files.
     * 
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileEntryList
     * @throws FileServiceException
     */
    public FileEntryList getPublicFiles(Map<String, String> params) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getPublicFiles");
        }
        String accessType = AccessType.PUBLIC.getAccessType();
        String view = Views.FILES.getViews();
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, view,
                null, null,
                resultType);
        if (params == null) {
            params = new HashMap<String, String>();
        }
        params.put(FileRequestParams.VISIBILITY.getFileRequestParams(), "public");

        FileEntryList fileEntries = null;
        try {
            fileEntries = (FileEntryList) getEntities(requestUri, params, new FileEntryFeedHandler());
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }

        return fileEntries;
    }

    public FileEntryList getPublicFolders() throws FileServiceException {
        return this.getPublicFolders(null);
    }

    /**
     * getPublicFileFolders
     * <p>
     * Rest API used : /files/basic/anonymous/api/collections/feed <br>
     * Public method. No Auth required.
     * 
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileEntryList
     * @throws FileServiceException
     */

    public FileEntryList getPublicFolders(Map<String, String> params) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "getPublicFileFolders");
        }
        String accessType = AccessType.PUBLIC.getAccessType();
        String view = Views.FOLDERS.getViews();
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, view,
                null, null,
                resultType);
        FileEntryList fileEntries = null;
        try {
            fileEntries = (FileEntryList) getEntities(requestUri, params, new FileEntryFeedHandler());
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        }

        return fileEntries;
    }

    /**
     * lock
     * <p>
     * This method can be used to set a lock on File. <br>
     * Rest API used : /files/basic/api/document/{document-id}/lock <br>
     * 
     * @param fileId - fileId of the file to be locked.
     * @throws FileServiceException
     */
    public void lock(String fileId) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "lock");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        SubFilters subFilters = new SubFilters();
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        subFilters.setFileId(fileId);
        String resultType = ResultType.LOCK.getResultType();
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.LOCK.getFileRequestParams(), "HARD");
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, null,
                null,
                subFilters, resultType);

        try {
            super.createData(requestUri, params, null);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInLockingFile);
        }

        // executePost(requestUri, params, null, null);
    }

    /**
     * pinFile
     * <p>
     * Pin a file. <br>
     * Rest API Used : /files/basic/api/myfavorites/documents/feed
     * 
     * @param fileId
     * @throws FileServiceException
     */
    public void pinFile(String fileId) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "pinFile");
        }
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = Categories.PINNED.getCategory();
        String view = Views.FILES.getViews();
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, view,
                null,
                null,
                resultType);
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.ITEMID.getFileRequestParams(), fileId);
        try {
            super.createData(requestUri, params, null);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInPinningFile);
        }

        // executePost(requestUri, params, null, null);
    }

    /**
     * * pinFolder
     * <p>
     * To pin a folder.<br>
     * Rest API Used : /files/basic/api/myfavorites/collections/feed
     * 
     * @param folderId
     * @throws FileServiceException
     */
    public void pinFolder(String folderId) throws FileServiceException {
        this.pinFolder(folderId, null);
    }

    // // /files/basic/api/myuserlibrary/document/{document-id}/version/{version-id}/media
    // public FileEntry downloadFileWithGivenVersion(String fileId, String versionId,
    // Map<String, String> params, Map<String, String> headers) throws FileServiceException {
    // if (logger.isLoggable(Level.FINEST)) {
    // logger.entering(sourceClass, "downloadFileWithGivenVersion");
    // }
    // if (StringUtil.isEmpty(fileId)) {
    // throw new FileServiceException(null, Messages.InvalidArgument_2);
    // }
    // if (StringUtil.isEmpty(versionId)) {
    // logger.log(Level.WARNING, Messages.InvalidArgument_9);
    // return getFile(fileId, params, true);
    // }
    // String accessType = AccessType.AUTHENTICATED.getAccessType();
    // String category = Categories.MYLIBRARY.getCategory();
    // SubFilters subFilters = new SubFilters();
    // subFilters.setDocumentId(fileId);
    // subFilters.setVersionId(versionId);
    // String resultType = ResultType.MEDIA.getResultType();
    // String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, null, null,
    // subFilters, resultType);
    //
    // return (FileEntry) executeGet(requestUri, params, headers, ClientService.FORMAT_XML, FileEntry.class)
    // .get(0);
    // }

    /**
     * pinFolder
     * <p>
     * To pin a folder.<br>
     * Rest API Used : /files/basic/api/myfavorites/collections/feed
     * 
     * @param folderId
     * @param params filesAddedNotification - String. Indicates whether the collection should be added to
     *            notification at the same time. Options are on and off. The default value is on.
     * @throws FileServiceException
     */
    public void pinFolder(String folderId, Map<String, String> params) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "pinFolder");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = Categories.PINNED.getCategory();
        String view = Views.FOLDERS.getViews();
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, view,
                null,
                null,
                resultType);
        if (null == params) {
            params = new HashMap<String, String>();
        }
        HashMap<String, String> headers = new HashMap<String, String>();
        params.put(FileRequestParams.ITEMID.getFileRequestParams(), folderId);
        try {
            super.updateData(requestUri, params, headers, null, folderId);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInPinningFolder);
        }
    }

    /**
     * removeFileFromFolder
     * <p>
     * Removes a file from a folder. This action does not delete the file entirely; it only removes its
     * association with the folder.<br>
     * Rest API used : /files/basic/api/collection/{collection-id}/feed
     * 
     * @param folderId ID of the Collection / Folder from which the File needs to be removed.
     * @param fileId file Id of the file which need to be removed from the collection.
     * @throws FileServiceException
     */
    public void removeFileFromFolder(String folderId, String fileId) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "removeFileFromFolder");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        if (StringUtil.isEmpty(folderId)) {
            throw new FileServiceException(null, Messages.Invalid_CollectionId);
        }
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        SubFilters subFilters = new SubFilters();
        subFilters.setCollectionId(folderId);
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, null,
                null,
                subFilters, resultType);
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.ITEMID.getFileRequestParams(), fileId);
        try {
            super.deleteData(requestUri, null, fileId);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInDeletingFile);
        }
    }

    /**
     * restoreFileFromRecycleBin
     * <p>
     * Restore a file to a document library from the trash. Restore a file from the logged in person's recycle
     * bin. <br>
     * Rest API Used : /files/basic/api/myuserlibrary/view/recyclebin/{document-id}/entry
     * 
     * @param fileId
     * @return
     * @throws FileServiceException
     */
    public FileEntry restoreFileFromRecycleBin(String fileId) throws FileServiceException {
        return this.restoreFileFromRecycleBin(fileId, null);
    }

    /**
     * restoreFileFromRecycleBin
     * <p>
     * Restore a file to a document library from the trash. Restore a file from the specified person's recycle
     * bin. <br>
     * Rest API Used : /files/basic/api/userlibrary/{userid}/view/recyclebin/{document-id}/entry
     * 
     * @param fileId
     * @param userId
     * @return
     * @throws FileServiceException
     */
    public FileEntry restoreFileFromRecycleBin(String fileId, String userId) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "restoreFileFromRecycleBin");
        }
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = null;
        SubFilters subFilters = new SubFilters();
        if (StringUtil.isEmpty(userId) || StringUtil.equalsIgnoreCase(userId, null)) {
            logger.log(Level.WARNING, Messages.Invalid_UserId);
            category = Categories.MYLIBRARY.getCategory();
        } else {
            subFilters.setUserId(userId);
        }
        subFilters.setRecycleBinDocumentId(fileId);
        String resultType = ResultType.ENTRY.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, null,
                null,
                subFilters, resultType);
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.UNDELETE.getFileRequestParams(), "true");
        Map<String, String> headers = new HashMap<String, String>();
        try {
            Response data = (Response) this.updateData(requestUri, params, headers, null, null).getData();
            return (FileEntry)new FileEntryFeedHandler().createEntity(data);
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInRestoreFile);
        }
       
    }

    /**
     * shareFileWithCommunities
     * <p>
     * Share a file with a community or multiple communities programmatically.<br>
     * Rest API Used : /basic/api/myuserlibrary/document/{document-id}/feed
     * 
     * @param fileId Id of the file to be shared
     * @param communityIds Id/Ids of the communities with which the file needs to be shared
     * @param params
     * @return
     * @throws FileServiceException
     */
    public void shareFileWithCommunities(String fileId, List<String> communityIds, Map<String, String> params)
            throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "shareFileWithCommunities");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        for (String communityId : communityIds) {
            if (StringUtil.isEmpty(communityId)) {
                throw new FileServiceException(null, Messages.Invalid_CommunityId);
            }
        }
        String category = Categories.MYLIBRARY.getCategory();
        SubFilters subFilters = new SubFilters();
        subFilters.setFileId(fileId);
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, null,
                null,
                subFilters, resultType);
        if (null == params) {
            params = new HashMap<String, String>();
        }
        Object payload = this.constructPayloadForMultipleEntries(communityIds,
                FileRequestParams.ITEMID.getFileRequestParams(), "community");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        headers.put(Headers.ContentLanguage, Headers.UTF);
        try {
            super.updateData(requestUri, params, headers, payload, fileId);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, "Error sharing the file");
        }
    }

    /**
     * unlock
     * <p>
     * This method can be used to unlock a File. <br>
     * Rest API used : /files/basic/api/document/{document-id}/lock <br>
     * 
     * @param fileId - fileId of the file to be unlocked.
     * @throws FileServiceException
     */
    public void unlock(String fileId) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "unlock");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        SubFilters subFilters = new SubFilters();
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        subFilters.setFileId(fileId);
        String resultType = ResultType.LOCK.getResultType();
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.LOCK.getFileRequestParams(), "NONE");
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, null,
                null,
                subFilters, resultType);
        try {
            super.createData(requestUri, params, null);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, "Error unlocking the file");
        }
    }

    /**
     * unPinFile
     * <p>
     * Removes the file from the myfavorites feed. <br>
     * Rest API Used : /files/basic/api/myfavorites/documents/feed
     * 
     * @param fileId
     * @throws FileServiceException
     */
    public void unPinFile(String fileId) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "unPinFile");
        }
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = Categories.PINNED.getCategory();
        String view = Views.FILES.getViews();
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, view,
                null,
                null,
                resultType);
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.ITEMID.getFileRequestParams(), fileId);
        try {
            super.deleteData(requestUri, params ,null);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, "Error unpinning the file");
        }
}

    public void unPinFolder(String folderId) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "unPinFolder");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = Categories.PINNED.getCategory();
        String view = Views.FOLDERS.getViews();
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, view,
                null,
                null,
                resultType);
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.ITEMID.getFileRequestParams(), folderId);
        try {
            super.deleteData(requestUri, null, folderId);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, "Error unpinning the folder");
        }
    }

    /**
     * updateComment
     * <p>
     * 
     * @param fileId
     * @param commentId
     * @param params
     * @param comment
     * @return
     * @throws FileServiceException
     */
    public CommentEntry updateComment(String fileId, String commentId, Map<String, String> params,
            String comment) throws FileServiceException {
        return this.updateComment(fileId, commentId, comment, "", params);
    }

    /**
     * updateComment
     * <p>
     * 
     * @param fileId
     * @param commentId
     * @param comment
     * @return
     * @throws FileServiceException
     */
    public CommentEntry updateComment(String fileId, String commentId, String comment)
            throws FileServiceException {
        return this.updateComment(fileId, commentId, comment, "", null);
    }

    /**
     * updateComment
     * <p>
     * 
     * @param fileId
     * @param commentId
     * @param comment
     * @param userId
     * @return
     * @throws FileServiceException
     */
    public CommentEntry updateComment(String fileId, String commentId, String comment, String userId)
            throws FileServiceException {
        return this.updateComment(fileId, commentId, comment, userId, null);
    }

    /**
     * updateComment
     * <p>
     * Rest API used : /files/basic/api/userlibrary/{userid}/document/{document-id}/comment/{comment-id}/entry
     * <br>
     * Updates comment from someone else's file whose userid is specified
     * 
     * @param FileEntry File for which the comment needs to be updated.
     * @param commentId Id of the comment to be updated.
     * @param params
     * @param comment New comment String.
     * @return CommentEntry
     * @throws FileServiceException
     */
    public CommentEntry updateComment(String fileId, String commentId, String comment, String userId,
            Map<String, String> params) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "updateComment");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        SubFilters subFilters = new SubFilters();
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        if (StringUtil.isEmpty(userId)) {
            logger.log(Level.WARNING, Messages.Invalid_UserId);
            Categories.MYLIBRARY.getCategory();
        } else {
            subFilters.setUserId(userId);
        }
        if (StringUtil.isEmpty(commentId)) {
            throw new FileServiceException(null, Messages.Invalid_CommentId);
        }
        subFilters.setFileId(fileId);
        subFilters.setCommentId(commentId);
        String resultType = ResultType.ENTRY.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, null,
                null,
                subFilters, resultType);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        Object payload = this.constructPayloadForComments(comment);


        try {
            Response result = (Response) this.updateData(requestUri, params, headers, payload, null).getData();
            return (CommentEntry) new CommentEntryFeedHandler().createEntity(result);
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInRestoreFile);
        }
       
    }

    /**
     * updateFile
     * 
     * @param fileId
     * @param params
     * @param content
     * @return
     * @throws FileServiceException
     */
    public FileEntry updateFile(String fileId, Map<String, String> params, Object content)
            throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "updateFile");
        }
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = Categories.MYLIBRARY.getCategory();
        SubFilters subFilters = new SubFilters();
        if (StringUtil.isEmpty(fileId)) {
            logger.log(Level.SEVERE, Messages.Invalid_FileId);
            return new FileEntry();
        }
        subFilters.setFileId(fileId);
        String resultType = ResultType.ENTRY.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, null,
                null,
                subFilters, resultType); // we pass null value for non applicable types.
        try {
            //TODO: check get data wrapping
            Response result = (Response) this.updateData(requestUri, params, content, null).getData();
            return (FileEntry) new FileEntryFeedHandler().createEntity(result);
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInRestoreFile);
        }
      

    }

    /**
     * updateFileInformation
     * 
     * @param fileEntry
     * @param params
     * @param requestBody
     * @return
     * @throws FileServiceException
     */
    public FileEntry updateFileInformation(FileEntry fileEntry, Map<String, String> params,
            Document requestBody) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "updateFileInformation");
        }
        if (fileEntry == null) {
            throw new FileServiceException(null, Messages.Invalid_FileEntry);
        }
        if (StringUtil.isEmpty(fileEntry.getFileId())) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        return this.updateFileInformation(fileEntry.getFileId(), params, requestBody);
    }

    /**
     * updateFileInformation
     * 
     * @param fileEntry
     * @param params
     * @param payloadMap
     * @return
     * @throws FileServiceException
     */
    public FileEntry updateFileInformation(FileEntry fileEntry, Map<String, String> params,
            Map<String, String> payloadMap) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "updateFileInformation");
        }
        if (fileEntry == null) {
            throw new FileServiceException(null, Messages.Invalid_FileEntry);
        }
        if (StringUtil.isEmpty(fileEntry.getFileId())) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        return this.updateFileInformation(fileEntry.getFileId(), params, payloadMap);
    }

    /**
     * updateFileInformation
     * 
     * @param fileId
     * @param updationsMap a Map of updations which need to be done to the file.
     * @return
     * @throws FileServiceException
     */
    public FileEntry updateFileInformation(String fileId, Map<String, String> updationsMap)
            throws FileServiceException {
        Map<String, String> payloadMap = new HashMap<String, String>();
        Map<String, String> paramsMap = new HashMap<String, String>();
        this.parseUpdationsMap(updationsMap, payloadMap, paramsMap);
        return this.updateFileInformation(fileId, paramsMap, payloadMap);
    }

    /**
     * updateFileInformation
     * <p>
     * This method is used to update the metadata/content of File in Connections. <br>
     * Rest API used : /files/basic/api/myuserlibrary/document/{document-id}/entry. <br>
     * User should get the specific file before calling this API, by using getFile method.
     * 
     * @param FileId - pass the fileID of the file to be updated
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.possible values.
     * @param requestBody - Document which is passed directly as requestBody to the execute request. This
     *            method is used to update the metadata/content of File in Connections.
     * @return FileEntry
     * @throws FileServiceException
     */
    public FileEntry updateFileInformation(String fileId, Map<String, String> params, Document requestBody)
            throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "updateFileInformation");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = Categories.MYLIBRARY.getCategory();
        SubFilters subFilters = new SubFilters();
        if (StringUtil.isEmpty(fileId)) {
            logger.log(Level.SEVERE, Messages.Invalid_FileId);
            return new FileEntry();
        }
        subFilters.setFileId(fileId);
        String resultType = ResultType.ENTRY.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, null,
                null,
                subFilters, resultType); // we pass null value for non applicable types.
        try {
            Response result = (Response) super.updateData(requestUri, params, new ClientService.ContentXml(
                    requestBody, "application/atom+xml"), null);
            return (FileEntry) new FileEntryFeedHandler().createEntity(result);
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInRestoreFile);
        }
    }

    /**
     * updateFileInformation
     * <p>
     * This method is used to update the metadata/content of File in Connections. <br>
     * Rest API used : /files/basic/api/myuserlibrary/document/{document-id}/entry. <br>
     * User should get the specific file before calling this API, by using getFile method.
     * 
     * @param FileId - pass the fileID of the file to be updated
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @param payloadMap - Map of entries for which we will construct a Request Body. See
     *            {@link FileRequestPayload} for possible values.
     * @return FileEntry
     * @throws FileServiceException
     */
    public FileEntry updateFileInformation(String fileId, Map<String, String> params,
            Map<String, String> payloadMap) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "updateFileInformation");
        }
        if (StringUtil.isEmpty(fileId)) {
            logger.log(Level.SEVERE, Messages.Invalid_FileId);
            return new FileEntry();
        }
        Document updateFilePayload = null;
        if (payloadMap != null && !payloadMap.isEmpty()) {
            updateFilePayload = this.constructPayload(fileId, payloadMap);
        }
        return this.updateFileInformation(fileId, params, updateFilePayload);
    }

    // Need to figure out what should be done with the label updation of comment. Connection Doc states that
    // comment updations here can be done on comment content and on label. But what is the label of the
    // comment ? Need to check this.
    public void updateFlaggedComment(String commentId, String updatedComment) throws FileServiceException {
        if (StringUtil.isEmpty(commentId)) {
            throw new FileServiceException(null, Messages.Invalid_CommentId);
        }
        String requestUri = this.getModerationUri(commentId, "review", "comment");
        // FileEntry fileEntry = (FileEntry) executeGet(requestUri, null, ClientService.FORMAT_XML,
        // null).get(0);

        // Map<String, String> payloadMap = new HashMap<String, String>();
        // Map<String, String> paramsMap = new HashMap<String, String>();
        Map<String, String> headers = new HashMap<String, String>();
        // parseUpdationsMap(updationsMap, payloadMap, paramsMap);
        // if (payloadMap != null && !payloadMap.isEmpty()) {
        headers.put(Headers.ContentType, Headers.ATOM);
        // }
        Document payload = this.constructPayloadForComments(updatedComment); // TODO
        try {
            this.updateData(requestUri, null, headers, payload, null).getData();
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInRestoreFile);
        }
    }

    public void updateFlaggedFile(String fileId, Map<String, String> updationsMap/* to title, tag and content */)
            throws FileServiceException {
        if (StringUtil.isEmpty(fileId)) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        String requestUri = this.getModerationUri(fileId, "review", "file");
        // FileEntry fileEntry = (FileEntry) executeGet(requestUri, null, ClientService.FORMAT_XML,
        // null).get(0);

        Map<String, String> payloadMap = new HashMap<String, String>();
        Map<String, String> paramsMap = new HashMap<String, String>();
        Map<String, String> headers = new HashMap<String, String>();
        this.parseUpdationsMap(updationsMap, payloadMap, paramsMap);
        if (payloadMap != null && !payloadMap.isEmpty()) {
            headers.put(Headers.ContentType, Headers.ATOM);
        }
        Document payload = this.constructPayload(fileId, payloadMap);

        try {
            this.updateData(requestUri, null, headers, payload, null).getData();
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInRestoreFile);
        }
    }

    public FileEntry updateFolder(String folderId, String name, String description, String shareWith)
            throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "updateFolder");
        }
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        SubFilters subFilters = new SubFilters();
        subFilters.setCollectionId(folderId);
        String resultType = ResultType.ENTRY.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, null,
                null,
                subFilters, resultType);
        Document payload = this.constructPayloadFolder(name, description, shareWith, "update");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        
        try {
            Response result = (Response) this.updateData(requestUri, null, headers, payload, null).getData();
            return (FileEntry) new FileEntryFeedHandler().createEntity(result);
        } catch (Exception e) {
            throw new FileServiceException(e, Messages.MessageExceptionInRestoreFile);
        }
        
    }

    /**
     * Upload a new file; cannot be used to update an existing file
     * <p>
     * Rest API Used : /files/basic/api/myuserlibrary/feed <br>
     * 
     * @param file - a readable file on the server
     * @return FileEntry
     * @throws FileServiceException
     */
    public FileEntry upload(File file) throws FileServiceException {
        return this.upload(file, null);
    }

    /**
     * Upload a new file; cannot be used to update an existing file
     * <p>
     * Rest API Used : /files/basic/api/myuserlibrary/feed <br>
     * 
     * @param file - a readable file on the server
     * @param parameters - file creation parameters, can be null
     * @return FileEntry
     * @throws FileServiceException
     */
    public FileEntry upload(File file, Map<String, String> parameters) throws FileServiceException {
        if (file == null) {
            throw new FileServiceException(null, Messages.Invalid_FileId);
        }
        if (!file.canRead()) {
            throw new FileServiceException(null, Messages.MessageCannotReadFile,
                    file.getAbsolutePath());
        }

        try {
            return this.upload(new FileInputStream(file), file.getName(), file.length(), parameters);
        } catch (FileNotFoundException e) {
            throw new FileServiceException(null, Messages.MessageCannotReadFile,
                    file.getAbsolutePath());
        }
    }

    /**
     * Upload a new file; cannot be used to update an existing file
     * <p>
     * Rest API Used : /files/basic/api/myuserlibrary/feed <br>
     * 
     * @param file - a readable file on the server
     * @param parameters - file creation parameters, can be null
     * @return FileEntry
     * @throws FileServiceException
     */
    public FileEntry upload(java.io.InputStream stream, final String title, long length,
            Map<String, String> p) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "uploading File");
        }
        if (stream == null) {
            throw new FileServiceException(null, Messages.Invalid_Stream);
        }
        if (title == null) {
            throw new FileServiceException(null, Messages.Invalid_Name);
        }
        ContentStream contentFile = new ContentStream(stream, length, title);
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = Categories.MYLIBRARY.getCategory();
        String resultType = ResultType.FEED.getResultType();
        String requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, category, null,
                null,
                null, resultType);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/atom+xml");
        try {
            Response data = (Response) super.createData(requestUri, p, headers, contentFile);
           
            return (FileEntry)new FileEntryFeedHandler().createEntity(data);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileServiceException(e, Messages.MessageExceptionInUpload);
        }
}



    private String constructEntry(String fileId, String content, String entity) {
        StringBuilder payload = new StringBuilder(
                "xmlns=\"http://www.w3.org/2005/Atom\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\"> <snx:in-ref-to rel=\"http://www.ibm.com/xmlns/prod/sn/report-item\" ref=\""
                        + fileId
                        + "\" ref-item-type=\""
                        + entity
                        + "\" /snx:in-ref-to><content type=\"text\">" + content + "</content>");
        return payload.toString();
    }

    /**
     * constructPayload
     * <p>
     * This method constructs the Atom entry document for the APIs requiring payload input. Currently this
     * method constructs payload for updating Label, Summary, Visibility, Title of the file.
     * 
     * @param FileEntry - pass the fileEntry object to be updated
     * @param payloadMap - Map of entries for which we will construct a Request Body. See
     *            {@link FileRequestPayload} for possible values.
     * @return Document
     */

    private Document constructPayload(String fileId, Map<String, String> payloadMap) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "constructPayload");
        }
        if (payloadMap == null || payloadMap.isEmpty()) {
            logger.log(Level.ALL, Messages.MessageEmptyPayload);
            return null;
            // throw new FileServiceException(null, Messages.PayloadInfo_1);
        }
        StringBuilder requestBody = new StringBuilder("<entry xmlns=\"http://www.w3.org/2005/Atom\">");
        requestBody
                .append("<category term=\"document\" label=\"document\" scheme=\"tag:ibm.com,2006:td/type\"></category>");
        requestBody.append("<id>urn:lsid:ibm.com:td:" + fileId + "</id>");
        requestBody.append("<uuid xmlns=\""
                            + ConnectionsConstants.nameSpaceCtx.getNamespaceURI("td") + "\">" + fileId + "</uuid>");
        Iterator<Map.Entry<String, String>> entries = payloadMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> fieldMapPairs = entries.next();
            String key = fieldMapPairs.getKey();
            String value = fieldMapPairs.getValue();
            if (!StringUtil.isEmpty(key) && !StringUtil.isEmpty(value)) {
                // here we handle the cases of setting label/title/summary/visibility
                if (key.equals("label")) {
                    requestBody.append("<label xmlns=\""
                            + ConnectionsConstants.nameSpaceCtx.getNamespaceURI("td") + "\">" + value
                            + "</label>");
                    requestBody.append("<title>" + value + "</title>");
                } else if (key.equals("summary")) {
                    requestBody.append("<summary type=\"text\">" + value + "</summary>");
                } else if (key.equals("visibility")) {
                    requestBody.append("<visibility xmlns=\""
                            + ConnectionsConstants.nameSpaceCtx.getNamespaceURI("td") + "\">" + value
                            + "</visibility>");
                }
            }
            entries.remove();
        }
        requestBody.append("</entry>");
        return this.convertToXML(requestBody.toString());
    }

    /**
     * constructPayloadFolder
     * 
     * @param name
     * @param description
     * @param shareWith
     * @param operation
     * @return
     */
    private Document constructPayloadFolder(String name, String description, String shareWith,
            String operation) {
        return this.constructPayloadFolder(name, description, shareWith, operation, null);
    }

    private Document constructPayloadFolder(String name, String description, String shareWith,
            String operation, String entityId) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "constructPayloadFolder");
        }
        String visibility = null, shareWithId = null, shareWithWhat = null, shareWithRole = null;
        StringBuilder payload = new StringBuilder("<entry xmlns=\"http://www.w3.org/2005/Atom\">");
        payload.append("<category term=\"collection\"  label=\"collection\" scheme=\"tag:ibm.com,2006:td/type\"/>");

        if (!StringUtil.isEmpty(operation)) {
            if (operation.equals("update")) {
                payload.append("<id>" + entityId + "</id>");
            }
            // if (operation.equals("create")) {
            if (StringUtil.isEmpty(name)) {
                // TODO
                // System.err.println("Invalid Name");
                return null;
            }
            payload.append("<label xmlns=\"urn:ibm.com/td\" makeUnique=\"true\">" + name + "</label>");
            payload.append("<title>" + name + "</title>");

            if (!StringUtil.isEmpty(description) || !StringUtil.equalsIgnoreCase(description, null)) {
                payload.append("<summary type=\"text\">" + description + "</summary>");
            }
            if (StringUtil.isEmpty(shareWith) || StringUtil.equalsIgnoreCase(shareWith, "null")) {
                visibility = "private";
                shareWith = "";
            } else {
                visibility = "public";
                String parts[] = shareWith.split(",");
                if ((parts.length) != 3) {
                    // System.err.println("Invalid shareWith Argument");
                    return null;
                } else {
                    shareWithId = parts[0];
                    shareWithWhat = parts[1];
                    shareWithRole = parts[2];
                }
                shareWith = "<member ca:id=\""
                        + shareWithId
                        + "\" xmlns=\"http://www.ibm.com/xmlns/prod/composite-applications/v1.0\" ca:type=\""
                        + shareWithWhat
                        + "\" xmlns:ca=\"http://www.ibm.com/xmlns/prod/composite-applications/v1.0\" ca:role=\""
                        + shareWithRole + "\"></member>";
            }
            payload.append("<visibility xmlns=\"urn:ibm.com/td\">" + visibility
                    + "</visibility> <sharedWith xmlns=\"urn:ibm.com/td\">" + shareWith + "</sharedWith>");
        }
        // }
        payload.append("</entry>");
        return convertToXML(payload.toString());
    }

    /**
     * constructPayloadForComments
     * 
     * @param comment - comment for which a payload Document needs to be constructed.
     * @return Document - payload Document which is sent as part of the request body.
     */

    private Document constructPayloadForComments(String comment) {
        return this.constructPayloadForComments(null, comment);
    }

    /**
     * constructPayloadForComments
     * 
     * @param operation - used to determine whether we need the payload for adding / deleting / updating a
     *            comment.
     * @param commentToBeAdded - plaintext comment which needs to be added to the File.
     * @return Document - payload Document which is sent as part of the request body.
     */

    private Document constructPayloadForComments(String operation, String commentToBeAdded) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "constructPayloadForComments");
        }
        StringBuilder payload = new StringBuilder("<entry xmlns=\"http://www.w3.org/2005/Atom\">");
        payload.append("<category term=\"comment\"  label=\"comment\" scheme=\"tag:ibm.com,2006:td/type\"/>");

        if (!StringUtil.isEmpty(operation) && !operation.equals("delete")) {
            payload.append("<deleteWithRecord xmlns=\""
                    + NamespacesConnections.nameSpaceCtx.getNamespaceURI("td")
                    + "\">false</deleteWithRecord>");
        } else {
            payload.append("<content type=\"text/plain\">" + commentToBeAdded + "</content>");
        }
        payload.append("</entry>");
        return this.convertToXML(payload.toString());
    }

    /**
     * constructPayloadForFlagging
     * 
     * @param fileId
     * @param flagReason
     * @param flagWhat
     * @return
     */
    private Object constructPayloadForFlagging(String fileId, String content, String entity) {
        if (entity.equalsIgnoreCase("file")) {
            entity = "document";
        }
        StringBuilder payload = new StringBuilder("<entry" + this.constructEntry(fileId, content, entity)
                + "</entry> ");
        // System.err.println("payload in constructPayloadForFlagging " + payload.toString());
        return this.convertToXML(payload.toString());
    }

    private Object constructPayloadForModeration(String fileId, String action, String actionReason,
            String entity) {
        if (entity.equalsIgnoreCase("file")) {
            entity = "document";
        }
        StringBuilder payload = new StringBuilder("<?xml version=\"1.0\"?> <entry ");
        payload.append(this.constructEntry(fileId, actionReason, entity));
        payload.append("<snx:moderation action=\" " + action + "\" />");
        payload.append("</entry> ");
        // System.err.println("payload in constructPayloadForModeration " + payload.toString());
        return this.convertToXML(payload.toString());
    }

    private Document constructPayloadForMultipleEntries(List<String> listOfFileIds, String multipleEntryId) {
        return this.constructPayloadForMultipleEntries(listOfFileIds, multipleEntryId, null);
    }

    private Document constructPayloadForMultipleEntries(List<String> listOfIds, String multipleEntryId,
            String category) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "constructPayloadForMultipleEntries");
        }
        StringBuilder requestBody = new StringBuilder(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><feed xmlns=\"http://www.w3.org/2005/Atom\">");
        for (String fileId : listOfIds) {
            requestBody.append("<entry>");
            if (!StringUtil.isEmpty(category) && (StringUtil.equalsIgnoreCase(category, "community"))
                    || StringUtil.equalsIgnoreCase(category, "collection")) {
                requestBody.append("<category term=\"" + category + "\" label=\"" + category
                        + "\" scheme=\"tag:ibm.com,2006:td/type\"></category>");
            }
            requestBody.append("<" + multipleEntryId + " xmlns=\"urn:ibm.com/td\">" + fileId + "</"
                    + multipleEntryId + "></entry>");
        }
        requestBody.append("</feed>");
        // System.err.println("requestBody " + requestBody.toString());
        return this.convertToXML(requestBody.toString());
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
    @SuppressWarnings("static-access")
    private String constructUrl(String baseUrl, String accessType, String category, String view,
            String filter, SubFilters subFilters, String resultType) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "constructUrl");
        }
        // here we will set the value in API after constructing the url
        // if the user has set these values then ok. otherwise, we set the default to GetMyFiles :
        // /files/basic/api/myuserlibrary/feed
        StringBuilder url = new StringBuilder(baseUrl);
        url.append(SEPARATOR).append("basic");
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
            if (!StringUtil.isEmpty(subFilters.getCollectionId())) {
                url = url.append(subFilters.COLLECTION).append(SEPARATOR)
                        .append(subFilters.getCollectionId());
            }
            if (!StringUtil.isEmpty(subFilters.getUserId())) {
                url = url.append(subFilters.LIBRARY).append(SEPARATOR).append(subFilters.getUserId());
            }
            if (!StringUtil.isEmpty(subFilters.getFileId())) {
                url = url.append(subFilters.FILE).append(SEPARATOR).append(subFilters.getFileId());
            }
            if (!StringUtil.isEmpty(subFilters.getCommentId())) {
                url = url.append(subFilters.COMMENT).append(SEPARATOR).append(subFilters.getCommentId());
            }
            if (!StringUtil.isEmpty(subFilters.getRecycleBinDocumentId())) {
                url = url.append(subFilters.RECYCLEBIN).append(SEPARATOR)
                        .append(subFilters.getRecycleBinDocumentId());
            }
            if (!StringUtil.isEmpty(subFilters.getVersionId())) {
                url = url.append(subFilters.VERSION).append(SEPARATOR).append(subFilters.getVersionId());
            }
            if (!StringUtil.isEmpty(subFilters.getDocumentsId())) {
                url = url.append(subFilters.DOCUMENTS).append(SEPARATOR).append(subFilters.getDocumentsId());
            }
        }

        if (!StringUtil.isEmpty(resultType)) {
            url.append(resultType);
        }
        return url.toString();
    }

    /**
     * convertToXML
     * <p>
     * Utility method to construct XML payload
     * 
     * @param requestBody
     * @return Document
     */
    private Document convertToXML(String requestBody) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(requestBody.toString())));
        } catch (Exception e) {
            logger.log(Level.SEVERE, Messages.MessageGenericException + "convertToXML()", e);
            throw new RuntimeException(e);
        }
        return document;
    }

    private boolean filePayloadParamsContains(String key) {
        for (FileRequestPayload payloadEntry : FileRequestPayload.values()) {
            if (payloadEntry.toString().equalsIgnoreCase(key)) {
                return true;
            }
        }
        return false;
    }

    private boolean fileRequestParamsContains(String key) {
        for (FileRequestParams param : FileRequestParams.values()) {
            if (param.toString().equalsIgnoreCase(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * executePost
     * 
     * @param requestUri - api to be executed.
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @param headers - Map of Headers. See {@link Headers} for possible values.
     * @param payload - Document which is passed directly as requestBody to the execute request. This method
     *            is used to update the metadata/content of File in Connections.
     * @return Document
     * @throws FileServiceException public Document executePost(String requestUri, Map<String, String>
     *             parameters, Map<String, String> headers, Object payload) throws FileServiceException { if
     *             (logger.isLoggable(Level.FINEST)) { logger.entering(sourceClass, "executePost"); } Object
     *             result = null; try { result = endpoint.getClientService().post(requestUri, parameters,
     *             headers, payload, ClientService.FORMAT_XML); } catch (ClientServicesException exception) {
     *             logger.log(Level.SEVERE, Messages.FileServiceException_1 + "executePost()", exception);
     *             setStatus(); throw new FileServiceException(exception, exception.getMessage()); }
     *             parseResult(result); return (Document) result; }
     */

    private String getModerationUri(String contentId, String action, String content)
            throws FileServiceException {
        FilesModerationDocumentEntry fileModDocEntry = new FilesModerationDocumentEntry(null);
        if (FilesModerationDocumentEntry.data == null) {
            FilesModerationDocumentEntry.setData(this.getFilesModerationServiceDocument());
        }
        // get the request URI from the servide document.
        String requestUri = fileModDocEntry.get(ContentMapFiles.moderationMap.get("getFileUrl")); // TODO
        // If not obtained due to some reason, then construct the url in regular fashion.
        // URI : /files/basic/api/approval/documents
        if (StringUtil.isEmpty(requestUri)) {
            SubFilters subFilters = new SubFilters();
            String category = null;
            if (content.equalsIgnoreCase("file")) {
                subFilters.setDocumentsId("");
            } else if (content.equalsIgnoreCase("comment")) {
                subFilters.setCommentId("");
            }
            if (action.equalsIgnoreCase("review")) {
                category = Categories.REVIEW.getCategory();
            } else if (action.equalsIgnoreCase("approval")) {
                category = Categories.APPROVAL.getCategory();
            }
            requestUri = this.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(),
                    AccessType.AUTHENTICATED.getAccessType(),
                    category, null, null, subFilters, null);
        }
        FileEntryList resultantEntries;
        try {
            resultantEntries = (FileEntryList) this.getEntities(requestUri, null,
                    new FileEntryFeedHandler());
        } catch (Exception e) {
           throw new FileServiceException(e, Messages.MessageExceptionInReadingObject);
        } 
       
        String uri = null;
        for (FileEntry entry : resultantEntries) {
            if (entry.getFileId().equalsIgnoreCase(contentId)) {
                uri = entry.getAsString(FileEntryXPath.DeleteModeration);
            }
        }
        if (StringUtil.isEmpty(uri)) {
            logger.log(Level.SEVERE, Messages.Invalid_ContentId);
            return null;
        }
        return uri;
    }

    private void parseUpdationsMap(Map<String, String> updationsMap, Map<String, String> payloadMap,
            Map<String, String> paramsMap) {
        // here parse the entries in the updations map and create paramsMap, payloadMap appropriately.
        Iterator<Map.Entry<String, String>> entries = updationsMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> fieldMapPairs = entries.next();
            String key = fieldMapPairs.getKey();
            if (this.fileRequestParamsContains(key)) {
                paramsMap.put(key, fieldMapPairs.getValue());
            } else if (this.filePayloadParamsContains(key)) {
                payloadMap.put(key, fieldMapPairs.getValue());
            } else {
                // these parameters currently will get ignored. check this .. TODO
                logger.log(Level.ALL, Messages.InvalidArgument_Generic);
            }
        }
    }

    public void addFileToFolder(String fileId, String folderId) throws FileServiceException {
        List<String> c = Arrays.asList(new String[] { folderId });
        addFileToFolders(fileId, c);
    }

    public FileEntry newFileEntry(Node data)  {
        XmlDataHandler handler = new XmlDataHandler(data, ConnectionsConstants.nameSpaceCtx);
        FileEntry fileEntry = new FileEntry(this,handler);
        return fileEntry;
    }


    public CommentEntry newCommentEntry(Node data) {
        XmlDataHandler handler = new XmlDataHandler(data, ConnectionsConstants.nameSpaceCtx);
        CommentEntry commentEntry = new CommentEntry(this,handler);
        return commentEntry;
    }
    
    private class FileEntryFeedHandler implements IFeedHandler{
        @Override
        public FileEntry createEntity(Response dataHolder) {
            return newFileEntry((Node)dataHolder.getData());
        }

        @Override
        public FileEntry createEntityFromData(Object data) {
            return newFileEntry((Node)data);
        }

        @Override
        public FileEntryList createEntityList(Response dataHolder) {
            return new FileEntryList((Response) dataHolder, FileService.this);
        }

        @Override
        public FileService getService() {
            return FileService.this;
        }

    }
    private class CommentEntryFeedHandler implements IFeedHandler{
        @Override
        public CommentEntry createEntity(Response dataHolder) {
            return newCommentEntry((Node)dataHolder.getData());
        }

        @Override
        public CommentEntry createEntityFromData(Object data) {
            return newCommentEntry((Node)data);
        }

        @Override
        public CommentEntryList createEntityList(Response dataHolder) {
            return new CommentEntryList((Response) dataHolder, FileService.this);
        }

        @Override
        public FileService getService() {
            return FileService.this;
        }

    }

}
