/*
 * © Copyright IBM Corp. 2014
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.ibm.commons.runtime.mime.MIME;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.StreamUtil;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.Content;
import com.ibm.sbt.services.client.ClientService.ContentStream;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomFeedHandler;
import com.ibm.sbt.services.client.base.AuthType;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.CommonConstants;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.ConnectionsService;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.files.FileConstants.FlagType;
import com.ibm.sbt.services.client.connections.files.model.FileCommentParameterBuilder;
import com.ibm.sbt.services.client.connections.files.model.FileEntryXPath;
import com.ibm.sbt.services.client.connections.files.model.FileRequestParams;
import com.ibm.sbt.services.client.connections.files.model.Headers;
import com.ibm.sbt.services.client.connections.files.serializer.CommentSerializer;
import com.ibm.sbt.services.client.connections.files.serializer.EntityIdSerializer;
import com.ibm.sbt.services.client.connections.files.serializer.FileSerializer;
import com.ibm.sbt.services.client.connections.files.serializer.FlagSerializer;
import com.ibm.sbt.services.client.connections.files.transformers.CommentTransformer;
import com.ibm.sbt.services.client.connections.files.transformers.ModerationTransformer;
import com.ibm.sbt.services.client.connections.files.util.Messages;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * The Files application of IBM® Connections enables teams to create a shared repository of files. The Files API allows application programs to add files to a collection and to read and modify existing files.
 * 
 * @see <href a="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.5+API+Documentation#action=openDocument&res_title=Files_API_ic45&content=pdcontent"/>
 * 
 * @Represents Connections FileService
 * @author Vimal Dhupar
 * @author Carlos Manias
 */
public class FileService extends ConnectionsService {

    private static final long serialVersionUID = 685886362342208180L;
    static final String       sourceClass      = FileService.class.getName();
    static final Logger       logger           = Logger.getLogger(FileService.sourceClass);

    private static String getDefaultEndpoint() {
        return "connections";
    }

    /**
     * Creates FileService with default endpoint.
     */
    public FileService() {
        this(FileService.getDefaultEndpoint());
    }

    /**
     * Create FileService instance with specified endpoint.
     * 
     * @param endpoint the endpoint object to use for authentication
     */
    public FileService(Endpoint endpoint) {
        super(endpoint);
    }

    /**
     * Create FileService instance with specified endpoint.
     * 
     * @param endpoint the endpoint name to use to retrieve the endpoint from the Context
     */
    public FileService(String endpoint) {
        super(endpoint);
    }
    
    /**
     * Return mapping key for this service
     */
    @Override
    public String getServiceMappingKey() {
        return "files";
    }
    
    /*****************************************************************
     * Getting Files feeds
     ****************************************************************/
    
    /**
     * Get a feed that lists all public files. 
     * 
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<File> getPublicFiles() throws ClientServicesException {
        return this.getPublicFiles(null);
    }

    /**
     * Get a feed that lists all public files. 
     * 
     * @param parameters 
     *                list of query string parameters to pass to API
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<File> getPublicFiles(Map<String, String> parameters) throws ClientServicesException {
        String accessType = AccessType.PUBLIC.getText();
        String requestUri = FileUrls.GET_PUBLIC_FILES.format(this, FileUrlParts.accessType.get(accessType));
        return this.getFileEntityList(requestUri, parameters);
    }
    
    /**
     * Get a feed that lists the files that you have pinned. 
     * 
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<File> getPinnedFiles() throws ClientServicesException {
        return this.getPinnedFiles(null);
    }

    /**
     * Get a feed that lists the files that you have pinned. 
     * 
     * @param parameters 
     *                list of query string parameters to pass to API
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<File> getPinnedFiles(Map<String, String> parameters) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYFAVORITES_DOCUMENTS_FEED.format(this,
                FileUrlParts.accessType.get(accessType));
        return this.getFileEntityList(requestUri, parameters);
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //TODO: Get My Folder requires to fetch the link from the service document
    //////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Get a feed that lists file folders that you have pinned. 
     * 
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<File> getPinnedFolders() throws ClientServicesException {
        return this.getPinnedFolders(null);
    }
    
    /**
     * Get a feed that lists file folders that you have pinned. 
     * 
     * @param parameters 
     *                list of query string parameters to pass to API
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<File> getPinnedFolders(Map<String, String> parameters) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYFAVORITES_COLLECTIONS_FEED.format(this,
                FileUrlParts.accessType.get(accessType));
        return this.getFileEntityList(requestUri, parameters);
    }
    
    /**
     * Get a feed that lists the folders that you added files to recently. 
     * 
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<File> getFoldersWithRecentlyAddedFiles() throws ClientServicesException {
        return this.getFoldersWithRecentlyAddedFiles(null);
    }

    /**
     * Get a feed that lists the folders that you added files to recently. 
     * 
     * @param parameters 
     *                list of query string parameters to pass to API
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<File> getFoldersWithRecentlyAddedFiles(Map<String, String> parameters)
            throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.GET_FOLDERS_WITH_RECENT_FILES.format(this,
                FileUrlParts.accessType.get(accessType));
        return this.getFileEntityList(requestUri, parameters);
    }
    
    /**
     * Get a feed that lists the files associated with a file folder. 
     * 
     * @param folderId - uuid of the folder/collection.
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<File> getFilesInFolder(String folderId) throws ClientServicesException {
        return this.getFilesInFolder(folderId, null);
    }

    /**
     * Get a feed that lists the files associated with a file folder. 
     * 
     * @param folderId - uuid of the folder/collection.
     * @param parameters 
     *                list of query string parameters to pass to API
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<File> getFilesInFolder(String folderId, Map<String, String> parameters)
            throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.COLLECTION_FEED.format(this, FileUrlParts.accessType.get(accessType),
                FileUrlParts.folderId.get(folderId));
        return this.getFileEntityList(requestUri, parameters);
    }
    
    public EntityList<File> getPublicFolders() throws ClientServicesException {
        return this.getPublicFolders(null);
    }

    /**
     * Get a feed that lists public file folders. 
     * 
     * 
     * @param parameters 
     *                list of query string parameters to pass to API
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<File> getPublicFolders(Map<String, String> params) throws ClientServicesException {
        String accessType = AccessType.PUBLIC.getText();
        String requestUri = FileUrls.COLLECTIONS_FEED.format(this, FileUrlParts.accessType.get(accessType));
        return this.getFileEntityList(requestUri, params);
    }
    
    /**
     * Get a feed that lists the files in a person's library.<br/> 
     *
     * @param userId
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<File> getPublicUserFiles(String userId) throws ClientServicesException {
        return this.getPublicUserFiles(userId, null);
    }

    /**
     * Get a feed that lists the files in a person's library.<br/> 
     *
     * @param userId
     * @param parameters 
     *                list of query string parameters to pass to API
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<File> getPublicUserFiles(String userId, Map<String, String> parameters)
            throws ClientServicesException {
        String accessType = AccessType.PUBLIC.getText();
        String requestUri = FileUrls.GET_ALL_USER_FILES.format(this, FileUrlParts.accessType.get(accessType),
                FileUrlParts.userId.get(userId));
        return this.getFileEntityList(requestUri, parameters);
    }
    
    /**
     * Get a feed that lists the files in a person's library you have access to.<br/> 
     * This feed includes any files in the library that have been shared with you and all user public files.
     *  
     * @param userId
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<File> getSharedUserFiles(String userId) throws ClientServicesException {
        return this.getSharedUserFiles(userId, null);
    }

    /**
     * Get a feed that lists the files in a person's library you have access to.<br/> 
     * This feed includes any files in the library that have been shared with you and all user public files.
     *
     * @param userId
     * @param parameters 
     *                list of query string parameters to pass to API
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<File> getSharedUserFiles(String userId, Map<String, String> parameters)
            throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.GET_ALL_USER_FILES.format(this, FileUrlParts.accessType.get(accessType),
                FileUrlParts.userId.get(userId));
        return this.getFileEntityList(requestUri, parameters);
    }
    
    /**
     * Get a feed that lists the files in your library. 
     * 
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<File> getMyFiles() throws ClientServicesException {
        return this.getMyFiles(null);
    }

    /**
     * Get a feed that lists the files in your library. 
     * 
     * @param parameters 
     *                list of query string parameters to pass to API
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<File> getMyFiles(Map<String, String> parameters) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYUSERLIBRARY_FEED.format(this, FileUrlParts.accessType.get(accessType));
        return this.getFileEntityList(requestUri, parameters);
    }
    
    
    
    /**
     * Get a feed that lists the share entries. A share entry describes an instance in which access that has been given to a file.
     * This returns a feed of shares to which the authenticated user has access. 
     * 
     * @param parameters 
     *                list of query string parameters to pass to API
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<File> getFilesSharedWithMe() throws ClientServicesException {
        return this.getFilesSharedWithMe(null);
    }

    /**
     * Get a feed that lists the share entries. A share entry describes an instance in which access that has been given to a file.
     * This returns a feed of shares to which the authenticated user has access. 
     * 
     * @param parameters 
     *                list of query string parameters to pass to API
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<File> getFilesSharedWithMe(Map<String, String> parameters) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.DOCUMENTS_SHARED_FEED.format(this,
                FileUrlParts.accessType.get(accessType));
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.DIRECTION.getFileRequestParams(), FileConstants.DIRECTION_INBOUND);
        if (parameters!=null) params.putAll(parameters);
        return this.getFileEntityList(requestUri, params);
    }
    
    
    /**
     * Get a feed that lists the share entries. A share entry describes an instance in which access that has been given to a file.
     * This returns a feed of shares to which the authenticated user has access. 
     * 
     * @param parameters 
     *                list of query string parameters to pass to API
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<File> getFilesSharedByMe() throws ClientServicesException {
        return this.getFilesSharedByMe(null);
    }

    /**
     * Get a feed that lists the share entries. A share entry describes an instance in which access that has been given to a file.
     * This returns a feed of shares to which the authenticated user has access. 
     * 
     * @param parameters 
     *                list of query string parameters to pass to API
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<File> getFilesSharedByMe(Map<String, String> params) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.DOCUMENTS_SHARED_FEED.format(this,
                FileUrlParts.accessType.get(accessType));
        params = this.getParameters(params);
        params.put(FileRequestParams.DIRECTION.getFileRequestParams(), FileConstants.DIRECTION_INBOUND);
        return this.getFileEntityList(requestUri, params);
    }
    
    
    
    
    /**
     * Get a feed that lists all of the comments associated with a file. 
     * 
     * @return EntityList&lt;Comment&gt;
     * @throws ClientServicesException 
     */
    public EntityList<Comment> getPublicUserFileComments(String fileId, String userId) throws ClientServicesException {
        return getPublicUserFileComments(fileId, userId, null);
    }
    
    /**
     * Get a feed that lists all of the comments associated with a file. 
     * 
     * @param parameters 
     *                list of query string parameters to pass to API
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException 
     */
    public EntityList<Comment> getPublicUserFileComments(String fileId, String userId, Map<String, String> parameters) throws ClientServicesException {
        String accessType = AccessType.PUBLIC.getText();
        String requestUri = FileUrls.USERLIBRARY_DOCUMENT_FEED.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId),
                FileUrlParts.fileId.get(fileId));
        return this.getCommentEntityList(requestUri, parameters, null);
    }
    
    /**
     * Get a feed that lists all of the comments associated with a file. 
     * 
     * @return EntityList&lt;Comment&gt;
     * @throws ClientServicesException 
     */
    public EntityList<Comment> getAllUserFileComments(String fileId, String userId) throws ClientServicesException {
        return getAllUserFileComments(fileId, userId, null);
    }
    
    /**
     * Get a feed that lists all of the comments associated with a file. 
     * 
     * @param parameters 
     *                list of query string parameters to pass to API
     * @return EntityList&lt;Comment&gt;
     * @throws ClientServicesException 
     */
    public EntityList<Comment> getAllUserFileComments(String fileId, String userId, Map<String, String> parameters) throws ClientServicesException {
        String accessType = AccessType.PUBLIC.getText();
        String requestUri = FileUrls.USERLIBRARY_DOCUMENT_FEED.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId),
                FileUrlParts.fileId.get(fileId));
        return this.getCommentEntityList(requestUri, parameters, null);
    }
    
    /**
     * Get a feed that lists all of the comments associated with one of your files. 
     * 
     * @return EntityList&lt;Comment&gt;
     * @throws ClientServicesException 
     */
    public EntityList<Comment> getMyFileComments(String fileId) throws ClientServicesException{
        return getMyFileComments(fileId, null);
    }
    
    /**
     * Get a feed that lists all of the comments associated with one of your files. 
     * 
     * @param parameters 
     *                list of query string parameters to pass to API
     * @return EntityList&lt;Comment&gt;
     * @throws ClientServicesException 
     */
    public EntityList<Comment> getMyFileComments(String fileId, Map<String, String> parameters)
            throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_FEED.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        return this.getCommentEntityList(requestUri, parameters, null);
    }

    /**
     * Get a feed that lists all of the files in your recycle bin. 
     * 
     * @param fileId
     * @return
     * @throws ClientServicesException
     */
    public File getFileFromRecycleBin(String fileId) throws ClientServicesException {
        return this.getFileFromRecycleBin(fileId, null, null);
    }
    
    
    /**
     * Using the Atom Publishing Protocol, also known as AtomPub, you can flag content as inappropriate so that the site administrator can take care of it.
     * 
     * @param objectId id of the file/comment which needs to be flagged as inappropriate.
     * @param flagReason why the file/comment is being flagged as inappropriate.
     * @param flagWhat whether it is a file or a comment, from the FlagType enum.
     * @throws ClientServicesException
     */
    public void flagAsInappropriate(String objectId, String flagReason, FlagType flagWhat)
            throws ClientServicesException {
        if (StringUtil.isEmpty(objectId)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        if (flagWhat == null) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.COLLECTION_ENTRY.format(this, FileUrlParts.accessType.get(accessType));
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        
        Object payload = new FlagSerializer(objectId, flagReason, flagWhat).flagPayload();
        this.updateData(requestUri, null, headers, payload, objectId);
    }
    
    /*****************************************************************
     * Working with files
     ****************************************************************/
    
    /**
     * A cryptographic nonce (number used once) key is a server-specified data string that is generated each time a 401 response is made. The server returns the data string to the client, and the client then passes that string unchanged back to the server with its subsequent request. Cryptographic keys prevent unauthorized access to data and protect against replay attacks. See RFC 2617 for more information about these keys. 
     * 
     * @return String - nonce value
     * @throws ClientServicesException
     */
    public String getNonce() throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.GET_NONCE.format(this, FileUrlParts.accessType.get(accessType));
        Object result = null;
        result = this.getClientService().get(requestUri, null, ClientService.FORMAT_TEXT);
        if (result == null) {
            return null;
        }
        return (String) ((Response) result).getData();
    }
    
    /**
     * Add a file to your library programmatically.
     * 
     * @param file - a readable file on the server
     * @return File
     * @throws ClientServicesException
     */
    public File uploadFile(java.io.File file) throws ClientServicesException {
        return this.uploadFile(file, null);
    }

    /**
     * Add a file to your library programmatically.
     * 
     * @param file - a readable file on the server
     * @param parameters - file creation parameters, can be null
     * @return File
     * @throws ClientServicesException
     */
    public File uploadFile(java.io.File file, Map<String, String> parameters) throws ClientServicesException {
        if (file == null) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        if (!file.canRead()) {
            throw new ClientServicesException(null, Messages.MessageCannotReadFile,
                    file.getAbsolutePath());
        }

        try {
            return this.uploadFile(new FileInputStream(file), file.getName(), file.length(), parameters);
        } catch (FileNotFoundException e) {
            throw new ClientServicesException(null, Messages.MessageCannotReadFile,
                    file.getAbsolutePath());
        }
    }

    /**
     * Add a file to your library programmatically.
     * 
     * @param stream the content to be uploaded
     * @param title the title to be used for uploading the file
     * @param length the length of the file
     * @return File
     * @throws ClientServicesException
     */
    public File uploadFile(java.io.InputStream stream, final String title, long length)
            throws ClientServicesException {
        return this.uploadFile(stream, title, length, null);
    }

    /**
     * Add a file to your library programmatically.
     * 
     * @param stream the content to be uploaded
     * @param title the title to be used for uploading the file
     * @param length the length of the file
     * @param p parameters
     * @return File
     * @throws ClientServicesException
     */
    public File uploadFile(java.io.InputStream stream, final String title, long length,
            Map<String, String> p) throws ClientServicesException {
        if (stream == null) {
            throw new ClientServicesException(null, Messages.Invalid_Stream);
        }
        if (title == null) {
            throw new ClientServicesException(null, Messages.Invalid_Name);
        }
        Content contentFile = this.getContentObject(title, stream, length);
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYUSERLIBRARY_FEED.format(this, FileUrlParts.accessType.get(accessType));
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(FileConstants.X_UPDATE_NONCE, this.getNonce()); // It is not clearly documented which Content Type requires Nonce, thus adding nonce in header for all upload requests. 
        Response data = this.createData(requestUri, p, headers, contentFile);
        if (FileService.logger.isLoggable(Level.FINEST)) {
            FileService.logger.exiting(FileService.sourceClass, "uploadFile", data);
        }

        return this.getFileFeedHandler().createEntity(data);
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //TODO: add file using multipart POST
    //////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Add a file or files to a folder programmatically.
     * You cannot add a file from your local directory to a folder; the file must already have been uploaded to the Files application. To add a file to a folder you must be an editor of the folder.
     * 
     * @param folderId ID of the Collection / Folder to which File(s) need to be added.
     * @param listOfFileIds A list of file ids, which need to be added to the collection.
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException
     * @throws TransformerException
     */
    public EntityList<File> addFilesToFolder(String folderId, List<String> listOfFileIds,
            Map<String, String> params) throws ClientServicesException, TransformerException {
        if (StringUtil.isEmpty(folderId)) {
            throw new ClientServicesException(null, Messages.Invalid_CollectionId);
        }
        for (String fileId : listOfFileIds) {
            if (StringUtil.isEmpty(fileId)) {
                throw new ClientServicesException(null, Messages.Invalid_FileId);
            }
        }
        String accessType = AccessType.AUTHENTICATED.getText();
        params = (null == params) ? new HashMap<String, String>() : params;
        String requestUri = FileUrls.COLLECTION_FEED.format(this, FileUrlParts.accessType.get(accessType),
                FileUrlParts.folderId.get(folderId));
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        Object payload = new EntityIdSerializer(listOfFileIds).fileIdListPayload();
        Response result;
        result = this.createData(requestUri, params, headers, payload);
        return this.getFileFeedHandler().createEntityList(result);
    }
    
    /**
     * Add a file to a folder programmatically.
     * You cannot add a file from your local directory to a folder; the file must already have been uploaded to the Files application. To add a file to a folder you must be an editor of the folder.
     * 
     * @param folderId ID of the Collection / Folder to which File(s) need to be added.
     * @param listOfFileIds A list of file ids, which need to be added to the collection.
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return EntityList&lt;File&gt;
     * @throws ClientServicesException
     * @throws TransformerException
     */
    public void addFileToFolder(String fileId, String folderId) throws ClientServicesException,
            TransformerException {
        List<String> c = Arrays.asList(new String[] { folderId });
        this.addFileToFolders(fileId, c);
    }

    /**
     * Add a file to multiple folders programmatically.
     * You cannot add a file from your local directory to a folder; the file must already have been uploaded to the Files application. To add a file to a folder you must be an editor of the folder.
     * 
     * @param fileId
     * @param folderIds - list of folder Ids to which the file needs to be added.
     * @throws ClientServicesException
     * @throws TransformerException
     */
    public void addFileToFolders(String fileId, List<String> folderIds) throws ClientServicesException,
            TransformerException {
        this.addFileToFolders(fileId, folderIds, null, null);
    }

    /**
     * Add a file to multiple folders programmatically.
     * You cannot add a file from your local directory to a folder; the file must already have been uploaded to the Files application. To add a file to a folder you must be an editor of the folder.
     * 
     * @param fileId
     * @param folderIds - list of folder Ids to which the file needs to be added.
     * @param params
     * @throws ClientServicesException
     * @throws TransformerException
     */
    public void addFileToFolders(String fileId, List<String> folderIds, Map<String, String> params)
            throws ClientServicesException, TransformerException {
        this.addFileToFolders(fileId, folderIds, null, params);
    }

    /**
     * Add a file to multiple folders programmatically.
     * You cannot add a file from your local directory to a folder; the file must already have been uploaded to the Files application. To add a file to a folder you must be an editor of the folder.
     * 
     * @param fileId
     * @param folderIds - list of folder Ids to which the file needs to be added.
     * @param userId
     * @throws ClientServicesException
     * @throws TransformerException
     */
    public void addFileToFolders(String fileId, List<String> folderIds, String userId)
            throws ClientServicesException, TransformerException {
        this.addFileToFolders(fileId, folderIds, userId, null);
    }

    /**
     * Add a file to multiple folders programmatically.
     * You cannot add a file from your local directory to a folder; the file must already have been uploaded to the Files application. To add a file to a folder you must be an editor of the folder.
     * 
     * @param fileId
     * @param folderIds - list of folder Ids to which the file needs to be added.
     * @param userId
     * @param params
     * @throws ClientServicesException
     * @throws TransformerException
     */
    public void addFileToFolders(String fileId, List<String> folderIds, String userId,
            Map<String, String> params) throws ClientServicesException, TransformerException {
        String accessType = AccessType.AUTHENTICATED.getText();

        String requestUri;
        if (StringUtil.isEmpty(userId)) {
            requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_FEED.format(this,
                    FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        } else {
            requestUri = FileUrls.USERLIBRARY_DOCUMENT_FEED.format(this,
                    FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId),
                    FileUrlParts.fileId.get(fileId));
        }

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        headers.put(Headers.ContentLanguage, Headers.UTF);

        params = (null == params) ? new HashMap<String, String>() : params;
        Object payload = new EntityIdSerializer(folderIds,FileConstants.CATEGORY_COLLECTION).fileIdListPayload();
        this.createData(requestUri, params, headers, payload);
    }
    
    /**
     * Share a file with a community or multiple communities programmatically.
     * 
     * @param fileId Id of the file to be shared
     * @param communityIds Id/Ids of the communities with which the file needs to be shared
     * @param params
     * @return
     * @throws ClientServicesException
     * @throws TransformerException
     */
    public void shareFileWithCommunities(String fileId, List<String> communityIds, Map<String, String> params)
            throws ClientServicesException, TransformerException {
        if (StringUtil.isEmpty(fileId)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        for (String communityId : communityIds) {
            if (StringUtil.isEmpty(communityId)) {
                throw new ClientServicesException(null, Messages.Invalid_CommunityId);
            }
        }
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYUSERLIBRARY_RECYCLEBIN_ENTRY.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        params = (null == params) ? new HashMap<String, String>() : params;
        
        Object payload =  new EntityIdSerializer(communityIds,FileConstants.CATEGORY_COMMUNITY).fileIdListPayload();
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        headers.put(Headers.ContentLanguage, Headers.UTF);
        this.createData(requestUri, params, headers, payload);
    }
    
    /**
     * Method to download the specified file
     * 
     * @param ostream - output stream which contains the binary content of the file
     * @param file
     * @param params
     * 
     * @return long - no of bytes
     * @throws ClientServicesException
     */
    public long downloadFile(OutputStream ostream, File file, Map<String, String> params)
            throws ClientServicesException {
        // file content url
        String requestUrl = file.getEditMediaUrl();

        // request headers
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.BINARY);

        // trigger request to download the file
        Response response = null;
        response = this.getClientService().get(requestUrl, params, headers, ClientService.FORMAT_INPUTSTREAM);

        // read the file data
        InputStream istream = (InputStream) response.getData();
        long noOfBytes = 0;
        try {
            if (istream != null) {
                noOfBytes = StreamUtil.copyStream(istream, ostream);
                ostream.flush();
            }
        } catch (IllegalStateException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInDownloadingFile);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInDownloadingFile);
        }
        return noOfBytes;
    }

    /**
     * Method to download a File of logged in user
     * 
     * @param ostream - output stream which contains the binary content of the file
     * @param fileId
     * @return long - no of bytes
     * @throws ClientServicesException
     */
    public long downloadFile(OutputStream ostream, final String fileId) throws ClientServicesException {
        return this.downloadFile(ostream, fileId, null, false);
    }

    /**
     * Method to download a File of logged in user
     * 
     * @param ostream - output stream which contains the binary content of the file
     * @param fileId
     * @param libraryId - required in case of public file
     * @param params
     * @return long - no of bytes
     * @throws ClientServicesException
     */
    public long downloadFile(OutputStream ostream, final String fileId, Map<String, String> params)
            throws ClientServicesException {
        return this.downloadFile(ostream, fileId, null, params, false);
    }

    /**
     * Method to download a File
     * 
     * @param ostream - output stream which contains the binary content of the file
     * @param fileId
     * @param libraryId - required in case of public files
     * @param isPublic - flag to indicate public file
     * @return long - no of bytes
     * @throws ClientServicesException
     */
    public long downloadFile(OutputStream ostream, final String fileId, final String libraryId,
            boolean isPublic) throws ClientServicesException {
        return this.downloadFile(ostream, fileId, libraryId, null, isPublic);
    }

    /**
     * Method to download a File
     * 
     * @param ostream - output stream which contains the binary content of the file
     * @param fileId
     * @param libraryId - required in case of public file
     * @param params
     * @param isPublic - flag to indicate public file
     * @return long - no of bytes
     * @throws ClientServicesException
     */
    public long downloadFile(OutputStream ostream, final String fileId, final String libraryId,
            Map<String, String> params, boolean isPublic) throws ClientServicesException {
        File file = !isPublic ? this.getFile(fileId) : this.getPublicFile(fileId, libraryId, null);
        // now we have the file.. we need to download it.. 
        String accessType = !isPublic ? AccessType.AUTHENTICATED.getText() : AccessType.PUBLIC.getText();
        String category = !isPublic ? Categories.MYUSERLIBRARY.get() : null;
        String libraryFilter = (libraryId != null) ? "library" : "";

        String requestUrl = FileUrls.DOWNLOAD_FILE.format(this, FileUrlParts.accessType.get(accessType),
                FileUrlParts.category.get(category), FileUrlParts.fileId.get(file.getFileId()),
                FileUrlParts.libraryFilter.get(libraryFilter), FileUrlParts.libraryId.get(libraryId));

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.BINARY);
        Response response = null;
        response = this.getClientService().get(requestUrl, params, headers, ClientService.FORMAT_INPUTSTREAM);
        InputStream istream = (InputStream) response.getData();
        long noOfBytes = 0;
        try {
            if (istream != null) {
                noOfBytes = StreamUtil.copyStream(istream, ostream);
                ostream.flush();
            }
        } catch (IllegalStateException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInDownloadingFile);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInDownloadingFile);
        }
        return noOfBytes;
    }

    
    /**
     * Method to download a community file. A community file is a public file.
     * 
     * @param ostream - output stream which contains the binary content of the file
     * @param fileId
     * @param libraryId - Library Id of which the file is a part. This value can be obtained by using File's getLibraryId method.
     * @return
     * @throws ClientServicesException
     */
    public long downloadCommunityFile(OutputStream ostream, final String fileId, final String libraryId)
            throws ClientServicesException {
        return this.downloadCommunityFile(ostream, fileId, libraryId, null);
    }
    
    /**
     * Retrieve an Atom document representation of the metadata for a file from your library. This method returns the Atom document containing the metadata associated with a file in a library. If you want to retrieve the file itself, see Retrieving a file
     * 
     * @param fileId - ID of the file to be fetched from the Connections Server
     * @param parameters - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @param load - a flag to determine whether the network call should be made or an empty placeholder of
     *        the File object should be returned. load - true : network call is made to fetch the
     *        file load - false : an empty File object is returned, and then updations can be made on
     *        this object.
     * @return File
     * @throws ClientServicesException
     */
    public File getFile(String fileId, Map<String, String> parameters, boolean load)
            throws ClientServicesException {
        File file = new File(fileId);
        file.setService(this);
        if (load) {
            SubFilters subFilters = new SubFilters();
            subFilters.setFileId(fileId);
            String accessType = AccessType.AUTHENTICATED.getText();
            String requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_ENTRY.format(this,
                    FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
            return this.getFileEntity(requestUri, parameters);
        }
        return file;
    }

    /**
     * Retrieve an Atom document representation of the metadata for a file from your library. This method returns the Atom document containing the metadata associated with a file in a library. If you want to retrieve the file itself, see Retrieving a file
     * 
     * @param fileId - ID of the file to be fetched from the Connections Server
     * @param libraryId - ID of the library to which the public file belongs
     * @param parameters - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return File
     * @throws ClientServicesException
     */
    public File getFile(String fileId, String libraryId, Map<String, String> parameters)
            throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_ENTRY.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.libraryId.get(libraryId),
                FileUrlParts.fileId.get(fileId));
        return this.getFileEntity(requestUri, parameters);
    }
    
    /**
     * Delete a file and the Atom document representation of its associated metadata from your collection. 
     * Only the owner of a collection can delete a file from the collection. 
     * 
     * @param fileId - id of the file to be deleted
     * @throws ClientServicesException
     */
    public void deleteFile(String fileId) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_ENTRY.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        this.deleteData(requestUri, null, null);
    }
    
    /**
     * Removes a file from a folder. 
     * This action does not delete the file entirely; it only removes its association with the folder. 
     * The currently authenticated user must be the owner of the folder, an administrator, 
     * the owner of the item in the folder or have been given delete access to the folder. 
     * Delete access is granted through the manager role of a folder.
     * 
     * @param folderId ID of the Collection / Folder from which the File needs to be removed.
     * @param fileId file Id of the file which need to be removed from the collection.
     * @throws ClientServicesException
     */
    public void removeFileFromFolder(String folderId, String fileId) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.COLLECTION_FEED.format(this, FileUrlParts.accessType.get(accessType),
                FileUrlParts.folderId.get(folderId));
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.ITEMID.getFileRequestParams(), fileId);
        this.deleteData(requestUri, params, null);
    }


    /**
     * Update a file in your file collection programmatically.
     * To update the metadata associated with the file, pass new values for the document elements using input parameters.
     * 
     * @param iStream
     * @param fileId
     * @param title
     * @param params
     * @return File
     * @throws ClientServicesException
     */
    public File updateFile(java.io.InputStream inputStream, File file, Map<String, String> params)
            throws ClientServicesException {
        String requestUrl = file.getEditMediaUrl();

        Content contentFile = this.getContentObject(file.getTitle(), inputStream);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(FileConstants.X_UPDATE_NONCE, this.getNonce()); // It is not clearly documented which Content Type requires Nonce, thus adding nonce in header for all upload requests. 

        Response result = this.updateData(requestUrl, params, headers, contentFile, null);
        return this.getFileFeedHandler().createEntity(result);
    }

    /**
     * Update the Atom document representation of the metadata for a file from your library.
     * 
     * @param FileId - pass the fileID of the file to be updated
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.possible values.
     * @param requestBody - Document which is passed directly as requestBody to the execute request. This
     *        method is used to update the metadata/content of File in Connections.
     * @return File
     * @throws ClientServicesException
     */
    public File updateFileMetadata(File file, Map<String, String> params)
            throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        if (file==null) {
            throw new ClientServicesException(null, Messages.Invalid_File);
        }
        String payload = new FileSerializer(file).generateFileUpdatePayload();
        String requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_ENTRY.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(file.getFileId()));
        Response result = this.updateData(requestUri, params, new ClientService.ContentString(
                payload, CommonConstants.APPLICATION_ATOM_XML), null);
        return this.getFileFeedHandler().createEntity(result);
    }

    
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //TODO: add file using multipart POST
    //////////////////////////////////////////////////////////////////////////////////////////////////
    

    /*****************************************************************
     * Working with folders
     ****************************************************************/

    /**
     * Create a file folder programmatically.
     * 
     * @param name name of the folder to be created
     * @return File
     * @throws ClientServicesException
     * @throws TransformerException
     */
    public File createFolder(String name) throws ClientServicesException, TransformerException {
        return this.createFolder(name, null);
    }

    /**
     * Create a file folder programmatically.
     * 
     * @param name name of the folder to be created
     * @param summary description of the folder
     * @return File
     * @throws ClientServicesException
     * @throws TransformerException
     */
    public File createFolder(String name, String summary) throws ClientServicesException {
        File f  = new File(this,null);
        f.setTitle(name);
        f.setLabel(name);
        f.setSummary(summary);
        return createFolder(f);
        
    }
    /**
     * Create a file folder programmatically.
     * 
     * @param folder the folder objct to be created
     * @return File
     * @throws ClientServicesException
     * @throws TransformerException
     */
    public File createFolder(File folder)
            throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();

        String requestUri = FileUrls.COLLECTIONS_FEED.format(this, FileUrlParts.accessType.get(accessType));
        String payload = new FileSerializer(folder).generateFileUpdatePayload();

        Response result = this.createData(requestUri, null, new ClientService.ContentString(payload,
                CommonConstants.APPLICATION_ATOM_XML));
        File r = this.getFileFeedHandler().createEntity(result);
        folder.clearFieldsMap();
        folder.setDataHandler(r.getDataHandler());
        return folder;
    }

    /**
     * Retrieve an Atom document representation of a file folder.
     * 
     * @param folderId
     * @return
     * @throws ClientServicesException
     */
    public File getFolder(String folderId) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.COLLECTION_ENTRY.format(this, FileUrlParts.accessType.get(accessType),
                FileUrlParts.folderId.get(folderId));
        return this.getFileEntity(requestUri, null);
    }
    
    /**
     * Delete a file folder using the HTTP DELETE request. Deleting a folder does not delete the files in the folder. Only a manager of a folder can delete the folder
     * 
     * @param folderId
     * @throws ClientServicesException
     */
    public void deleteFolder(String folderId) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.COLLECTION_ENTRY.format(this, FileUrlParts.accessType.get(accessType),
                FileUrlParts.folderId.get(folderId));
        this.deleteData(requestUri, null, null);
    }
    
    /**
     * Update the Atom document representation of the file folder.
     * 
     * @param folder
     * @return
     * @throws ClientServicesException
     */
    public File updateFolder(File folder) throws ClientServicesException{
            String accessType = AccessType.AUTHENTICATED.getText();
            String requestUri = FileUrls.COLLECTION_ENTRY.format(this, FileUrlParts.accessType.get(accessType),
                    FileUrlParts.folderId.get(folder.getFileId()));
            String payload = new FileSerializer(folder).generateFileUpdatePayload();
            Map<String, String> headers = new HashMap<String, String>();
            headers.put(Headers.ContentType, Headers.ATOM);

            Response result = this.updateData(requestUri, null, headers, payload, null);
            File r = this.getFileFeedHandler().createEntity(result);
            folder.clearFieldsMap();
            folder.setDataHandler(r.getDataHandler());
            return folder;
    }
    
    /**
     * Update the Atom document representation of the file folder.
     * 
     * @param folderId
     * @param title
     * @param label
     * @param summary
     * @return
     * @throws ClientServicesException
     */
    public File updateFolder(String folderId, String title, String label, String summary) throws ClientServicesException{
         File f = new File(this,null);
        f.setFileId(folderId);
        f.setLabel(label);
        f.setTitle(title);
        f.setSummary(summary);
        return updateFolder(f);
    }
    
    /*****************************************************************
     * Working with shares
     ****************************************************************/
    

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //TODO: add creating share api
    //////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //TODO: add retrieving share api
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Delete a file share programmatically.
     * Only the owner of a file can delete shares from that file.
     * 
     * @param fileId - sharedWhat : This is a required parameter. Document uuid. Delete a set of share
     *        resources for the specified document.<br>
     * @throws ClientServicesException
     */
    public void deleteFileShare(String fileId) throws ClientServicesException {
        this.deleteFileShare(fileId, null);
    }

    /**
     * Delete a file share programmatically.
     * Only the owner of a file can delete shares from that file.
     * 
     * @param fileId - sharedWhat : This is a required parameter. Document uuid. Delete a set of share
     *        resources for the specified document.<br>
     *        - sharedWith : User ID of the user with whom the document has been shared, but you would
     *        like to prevent from having access to it. You can specify more than one person. Separate
     *        multiple user IDs with a comma. Any share resources for the document that have the specified
     *        users as targets of the share will be deleted. The default is to delete the document's
     *        shares with all users.
     * @param userId
     * @throws ClientServicesException
     */
    public void deleteFileShare(String fileId, String userId) throws ClientServicesException {
        if (StringUtil.isEmpty(fileId)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.DELETE_FILE_SHARE.format(this, FileUrlParts.accessType.get(accessType));

        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.SHAREDWHAT.getFileRequestParams(), fileId);
        if (!StringUtil.isEmpty(userId)) {
            params.put(FileRequestParams.SHAREDWITH.getFileRequestParams(), userId);
        }
        this.deleteData(requestUri, params, null);
    }
    
    
    /*****************************************************************
     * Working with comments
     ****************************************************************/
    
    /**
     * Create a comment programmatically.
     * 
     * @param fileId - ID of the file
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @param comment - Comment to be added to the File
     * @return Comment
     * @throws ClientServiceException
     * @throws TransformerException
     */
    public Comment addCommentToFile(String fileId, String comment, Map<String, String> params)
            throws ClientServicesException, TransformerException {
        Comment c = new Comment(this,null);
        c.setContent(comment);
        return this.addCommentToFile(fileId, c, null, params);
    }
    /**
     * Create a comment programmatically.
     * 
     * @param fileId - ID of the file
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @param comment - Comment to be added to the File
     * @param userId - the user owning the file
     * @return Comment
     * @throws ClientServiceException
     * @throws TransformerException
     */
    public Comment addCommentToFile(String fileId, String comment, String userId, Map<String, String> params)
            throws ClientServicesException, TransformerException {
        Comment c = new Comment(this,null);
        c.setContent(comment);
        return this.addCommentToFile(fileId, c, userId, params);
    }
    /**
     * Create a comment programmatically.
     * 
     * @param fileId - ID of the file
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @param comment - Comment to be added to the File
     * @param libraryId - Id of the library the file is present
     * @return Comment the comment to create
     * @throws ClientServicesException
     * @throws TransformerException
     */
    public Comment addCommentToFile(String fileId, Comment comment, String userId,
            Map<String, String> params) throws ClientServicesException, TransformerException {
        //FIX: DUPLICATE METHOD see createComment()
        if (StringUtil.isEmpty(fileId)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri;
        if (StringUtil.isEmpty(userId)) {
            requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_FEED.format(this,
                    FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        } else {
            requestUri = FileUrls.USERLIBRARY_DOCUMENT_FEED.format(this,
                    FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId),
                    FileUrlParts.fileId.get(fileId));
        }
        String payload = new CommentSerializer(comment).generateCommentUpdatePayload();
        Response result = this.createData(requestUri, null, new ClientService.ContentString(payload,
                CommonConstants.APPLICATION_ATOM_XML));
        Comment ret = this.getCommentFeedHandler().createEntity(result);
        comment.clearFieldsMap();
        comment.setDataHandler(ret.getDataHandler());
        return comment;
    }


    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*****************************************************************
     * Working with files in the trash
     ****************************************************************/
    
    /*****************************************************************
     * Working with versions
     ****************************************************************/
    
    /*****************************************************************
     * Working with file attachments programmatically
     ****************************************************************/
    
    /*****************************************************************
     * Working with pinned files
     ****************************************************************/
    
    
    /*****************************************************************
     * Working with pinned folders
     ****************************************************************/
    
    /*****************************************************************
     * Moderating community files and comments programmatically
     ****************************************************************/
    
    
    
    
    
    
    
    
    
    
    
    public void actOnCommentAwaitingApproval(String commentId, String action, String actionReason)
            throws ClientServicesException, TransformerException {
        // get thr uri from here ::
        // In the service document, locate the workspace with the <category term="comments-moderation" .../>
        // child element, and then find the collection with the <atom:category term="approval-action" .../>
        // child element, and make a note of the web address in its href attribute.

        FilesModerationDocumentEntry fileModDocEntry = this.getFilesModerationDocumentEntry();
        // get the request URI from the servide document.
        String requestUri = fileModDocEntry.get(ContentMapFiles.moderationMap.get("commentApprovalUrl")); // TODO
        // If not obtained due to some reason, then construct the url in regular fashion.
        // URI : /files/basic/api/approval/actions/comments
        if (StringUtil.isEmpty(requestUri)) {
            String accessType = AccessType.AUTHENTICATED.getText();
            action = Categories.APPROVAL.get();
            String content = ModerationContentTypes.COMMENT.get();
            requestUri = FileUrls.MODERATION.format(this, FileUrlParts.accessType.get(accessType),
                    FileUrlParts.action.get(action), FileUrlParts.contentType.get(content));
        }
        Object payload = this.constructPayloadForModeration(commentId, action, actionReason, "comment");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        this.updateData(requestUri, null, headers, payload, commentId);
    }

    public void actOnFileAwaitingApproval(String fileId, String action, String actionReason)
            throws ClientServicesException, TransformerException {
        // get the uri
        // In the service document, locate the workspace with the <category term="documents-moderation" .../>
        // child element, and then find the collection with the <atom:category term="approval-action" .../>
        // child element, and make a note of the web address in its href attribute.
        FilesModerationDocumentEntry fileModDocEntry = this.getFilesModerationDocumentEntry();
        // get the request URI from the servide document.
        String requestUri = fileModDocEntry.get(ContentMapFiles.moderationMap.get("fileApprovalUrl")); // TODO
        // If not obtained due to some reason, then construct the url in regular fashion.
        // URI : /files/basic/api/approval/actions/documents
        if (StringUtil.isEmpty(requestUri)) {
            String accessType = AccessType.AUTHENTICATED.getText();
            action = Categories.APPROVAL.get();
            String content = ModerationContentTypes.DOCUMENTS.get();
            requestUri = FileUrls.MODERATION.format(this, FileUrlParts.accessType.get(accessType),
                    FileUrlParts.action.get(action), FileUrlParts.contentType.get(content));
        }
        Object payload = this.constructPayloadForModeration(fileId, action, actionReason, "file");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        this.updateData(requestUri, null, headers, payload, fileId);
    }

    public void actOnFlaggedComment(String commentId, String action, String actionReason)
            throws ClientServicesException, TransformerException {
        // get thr uri from here ::
        // In the service document, locate the workspace with the <category term="comments-moderation" .../>
        // child element, and then find the collection with the <atom:category term="approval-action" .../>
        // child element, and make a note of the web address in its href attribute.

        FilesModerationDocumentEntry fileModDocEntry = this.getFilesModerationDocumentEntry();
        // get the request URI from the servide document.
        String requestUri = fileModDocEntry.get(ContentMapFiles.moderationMap.get("commentReviewUrl")); // TODO
        // If not obtained due to some reason, then construct the url in regular fashion.
        // URI : /files/basic/api/review/actions/comments
        if (StringUtil.isEmpty(requestUri)) {
            String accessType = AccessType.AUTHENTICATED.getText();
            action = Categories.REVIEW.get();
            String content = ModerationContentTypes.COMMENT.get();
            requestUri = FileUrls.MODERATION.format(this, FileUrlParts.accessType.get(accessType),
                    FileUrlParts.action.get(action), FileUrlParts.contentType.get(content));
        }
        Object payload = this.constructPayloadForModeration(commentId, action, actionReason, "comment");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        this.updateData(requestUri, null, headers, payload, commentId);
    }

    public void actOnFlaggedFile(String fileId, String action, String actionReason)
            throws ClientServicesException, TransformerException {
        // get the uri
        // In the service document, locate the workspace with the <category term="documents-moderation" .../>
        // child element, and then find the collection with the <atom:category term="approval-action" .../>
        // child element, and make a note of the web address in its href attribute.
        FilesModerationDocumentEntry fileModDocEntry = this.getFilesModerationDocumentEntry();
        // get the request URI from the servide document.
        String requestUri = fileModDocEntry.get(ContentMapFiles.moderationMap.get("fileReviewUrl")); // TODO
        // If not obtained due to some reason, then construct the url in regular fashion.
        // URI : /files/basic/api/review/actions/documents
        if (StringUtil.isEmpty(requestUri)) {
            String accessType = AccessType.AUTHENTICATED.getText();
            action = Categories.REVIEW.get();
            String content = ModerationContentTypes.DOCUMENTS.get();
            requestUri = FileUrls.MODERATION.format(this, FileUrlParts.accessType.get(accessType),
                    FileUrlParts.action.get(action), FileUrlParts.contentType.get(content));
        }
        Object payload = this.constructPayloadForModeration(fileId, action, actionReason, "file");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        this.updateData(requestUri, null, headers, payload, fileId);
    }

    /**
     * Method to add comments to a Community file
     * <p>
     * Rest API used : /files/basic/api/communitylibrary/<communityId>/document/<fileId>/feed
     * 
     * @param fileId
     * @param comment
     * @param communityId
     * @return Comment
     * @throws ClientServicesException
     * @throws TransformerException
     */
    public Comment addCommentToCommunityFile(String fileId, String comment, String communityId)
            throws ClientServicesException, TransformerException {
        return this.addCommentToCommunityFile(fileId, comment, communityId, null);
    }

    /**
     * Method to add comments to a Community file
     * <p>
     * Rest API used : /files/basic/api/communitylibrary/<communityId>/document/<fileId>/feed
     * 
     * @param fileId
     * @param comment
     * @param communityId
     * @param params
     * @return Comment
     * @throws ClientServicesException
     * @throws TransformerException
     */
    public Comment addCommentToCommunityFile(String fileId, String comment, String communityId,
            Map<String, String> params) throws ClientServicesException, TransformerException {

        if (StringUtil.isEmpty(fileId)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        if (StringUtil.isEmpty(communityId) || StringUtil.equalsIgnoreCase(communityId, null)) {
            throw new ClientServicesException(null, Messages.Invalid_CommunityId);
        }

        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.COMMUNITY_FILE_COMMENT.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.communityId.get(communityId),
                FileUrlParts.fileId.get(fileId));
        Comment c = new Comment(this, null);
        c.setContent(comment);
        String payload = new CommentSerializer(c).generateCommentUpdatePayload();
        Response result = this.createData(requestUri, null, new ClientService.ContentString(payload,
                CommonConstants.APPLICATION_ATOM_XML));
        return this.getCommentFeedHandler().createEntity(result);
    }



    public Comment createComment(String fileId, String comment) throws ClientServicesException,
            TransformerException {
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
     * @throws ClientServicesException
     * @throws TransformerException
     */
    public Comment createComment(String fileId, String comment, String userId)
            throws ClientServicesException, TransformerException {
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
     * @return Comment
     * @throws ClientServicesException
     * @throws TransformerException
     */
    public Comment createComment(String fileId, String comment, String userId, Map<String, String> params)
            throws ClientServicesException, TransformerException {
        //FIX: DUPLICATE METHOD see addCommentToFile()
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri;
        if (StringUtil.isEmpty(userId)) {
            requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_FEED.format(this,
                    FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        } else {
            requestUri = FileUrls.USERLIBRARY_DOCUMENT_FEED.format(this,
                    FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId),
                    FileUrlParts.fileId.get(fileId));
        }

        Comment c = new Comment(this, null);
        c.setContent(comment);
        String payload = new CommentSerializer(c).generateCommentUpdatePayload();
        Map<String, String> headers = new HashMap<String, String>();
        Response result = this.createData(requestUri, params, headers, new ClientService.ContentString(
                payload, CommonConstants.APPLICATION_ATOM_XML));
        return this.getCommentFeedHandler().createEntity(result);
    }

    /**
     * deleteFileFromRecycleBin
     * <p>
     * Permanently deletes a file from the logged in person's recycle bin. <br>
     * Rest API Used : /files/basic/api/myuserlibrary/view/recyclebin/{document-id}/entry
     * 
     * @param fileId
     * @throws ClientServicesException
     */
    public void deleteAllFilesFromRecycleBin() throws ClientServicesException {
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
     * @throws ClientServicesException
     */
    public void deleteAllFilesFromRecycleBin(String userId) throws ClientServicesException {
        String requestUri;
        String accessType = AccessType.AUTHENTICATED.getText();
        if (StringUtil.isEmpty(userId)) {
            requestUri = FileUrls.MYUSERLIBRARY_RECYCLEBIN_FEED.format(this,
                    FileUrlParts.accessType.get(accessType));
        } else {
            requestUri = FileUrls.EMPTY_RECYCLE_BIN.format(this, FileUrlParts.accessType.get(accessType),
                    FileUrlParts.userId.get(userId));
        }
        this.deleteData(requestUri, null, null);
    }

    /**
     * deleteAllVersionsOfFile
     * <p>
     * Removes one or more versions of a file from your library. You cannot delete the current (most recent) version of a file using this request. <br>
     * Rest API Used : /files/basic/api/myuserlibrary/document/{document-id}/feed
     * 
     * @param fileId
     * @param versionLabel - Specifies the latest version to delete. This version and all earlier versions are
     *        deleted. This should not be the current version of the file.
     * @param params > identifier : Indicates how the document is identified in the {document-id} variable
     *        segment of the web address. By default, the lookup operation is performed with the
     *        expectation that the URL contains the value from the <td:uuid>element of a file Atom entry, so the value uuid is used. Specify label if the URL instead contains the value from the <td:label>
     *        element of the file Atom entry.
     * @throws ClientServicesException
     */
    public void deleteAllVersionsOfFile(String fileId, String versionLabel, Map<String, String> params)
            throws ClientServicesException {
        if (StringUtil.isEmpty(versionLabel)) {
            throw new ClientServicesException(null, Messages.InvalidArgument_VersionLabel);
        }
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_FEED.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        params = (null == params) ? new HashMap<String, String>() : params;
        params.put(FileRequestParams.CATEGORY.getFileRequestParams(), "version");
        params.put(FileRequestParams.DELETEFROM.getFileRequestParams(), versionLabel);
        this.deleteData(requestUri, params, null);
    }

    /**
     * deleteComment
     * <p>
     * 
     * @param fileId
     * @param commentId
     * @return
     * @throws ClientServicesException
     */
    public void deleteComment(String fileId, String commentId) throws ClientServicesException {
        this.deleteComment(fileId, commentId, "");
    }

    /**
     * deleteComment
     * <p>
     * Rest API used : /files/basic/api/userlibrary/{userid}/document/{document-id}/comment/{comment-id}/entry
     * 
     * @param File specifies the file for which the comment needs to be deleted.
     * @param commentId Id of the comment to be deleted.
     * @throws ClientServicesException
     */
    public void deleteComment(String fileId, String commentId, String userId) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();

        String requestUri;
        if (StringUtil.isEmpty(userId)) {
            requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_COMMENT_ENTRY.format(this,
                    FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId),
                    FileUrlParts.commentId.get(commentId));
        } else {
            requestUri = FileUrls.USERLIBRARY_DOCUMENT_COMMENT_ENTRY.format(this,
                    FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId),
                    FileUrlParts.fileId.get(fileId), FileUrlParts.commentId.get(commentId));
        }

        this.deleteData(requestUri, null, null);
    }



    public void deleteFileAwaitingApproval(String fileId) throws ClientServicesException {
        if (StringUtil.isEmpty(fileId)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        String requestUri = this.getModerationUri(fileId, Categories.APPROVAL.get(),
                ModerationContentTypes.DOCUMENTS.get());
        this.deleteData(requestUri, null, null);
    }

    /**
     * deleteFileFromRecycleBin
     * <p>
     * Permanently deletes a file from the logged in person's recycle bin. <br>
     * Rest API Used : /files/basic/api/myuserlibrary/view/recyclebin/{document-id}/entry
     * 
     * @param fileId
     * @throws ClientServicesException
     */
    public void deleteFileFromRecycleBin(String fileId) throws ClientServicesException {
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
     * @throws ClientServicesException
     */
    public void deleteFileFromRecycleBin(String fileId, String userId) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri;
        if (StringUtil.isEmpty(userId)) {
            requestUri = FileUrls.MYUSERLIBRARY_RECYCLEBIN_ENTRY.format(this,
                    FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        } else {
            requestUri = FileUrls.USERLIBRARY_RECYCLEBIN_ENTRY.format(this,
                    FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId),
                    FileUrlParts.fileId.get(fileId));
        }

        this.deleteData(requestUri, null, null);
    }


    public void deleteFlaggedComment(String commentId) throws ClientServicesException {
        if (StringUtil.isEmpty(commentId)) {
            throw new ClientServicesException(null, Messages.Invalid_CommentId);
        }
        String requestUri = this.getModerationUri(commentId, Categories.REVIEW.get(),
                ModerationContentTypes.COMMENT.get());
        if (StringUtil.isEmpty(requestUri)) {
            return;
        }
        this.deleteData(requestUri, null, null);
    }

    public void deleteFlaggedFiles(String fileId) throws ClientServicesException {
        if (StringUtil.isEmpty(fileId)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        String requestUri = this.getModerationUri(fileId, Categories.REVIEW.get(),
                ModerationContentTypes.DOCUMENTS.get());
        if (StringUtil.isEmpty(requestUri)) {
            return;
        }
        this.deleteData(requestUri, null, null);
    }





    /**
     * Method to download a community file. A community file is a public file.
     * 
     * @param ostream - output stream which contains the binary content of the file
     * @param fileId
     * @param libraryId - Library Id of which the file is a part. This value can be obtained by using File's getLibraryId method.
     * @param params
     * @return long
     * @throws ClientServicesException
     */
    public long downloadCommunityFile(OutputStream ostream, final String fileId, final String libraryId,
            Map<String, String> params) throws ClientServicesException {
        return this.downloadFile(ostream, fileId, libraryId, params, true);
    }




    /**
     * Method to get All comments of a Community File
     * <p>
     * Rest API Used : /files/basic/api/communitylibrary/<communityId>/document/<fileId>/feed
     * <p>
     * 
     * @param fileId
     * @param communityId
     * @return CommentList
     * @throws ClientServicesException
     */
    public EntityList<Comment> getAllCommunityFileComments(String fileId, String communityId)
            throws ClientServicesException {
        return this.getAllCommunityFileComments(fileId, communityId, null);
    }

    /**
     * Method to get All comments of a Community File
     * <p>
     * Rest API Used : /files/basic/api/communitylibrary/<communityId>/document/<fileId>/feed
     * <p>
     * 
     * @param fileId
     * @param communityId
     * @param parameters
     * @return CommentList
     * @throws ClientServicesException
     */
    public EntityList<Comment> getAllCommunityFileComments(String fileId, String communityId,
            Map<String, String> parameters) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.COMMUNITY_FILE_COMMENT.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.communityId.get(communityId),
                FileUrlParts.fileId.get(fileId));
        return this.getCommentEntityList(requestUri, parameters, null);
    }





    @Override
    public NamedUrlPart getAuthType() {
        return new NamedUrlPart(CommonConstants.AUTH_TYPE, AuthType.BASIC.get());
    }

    /**
     * 
     * @return
     */
    public IFeedHandler<Comment> getCommentFeedHandler() {
        return new AtomFeedHandler<Comment>(this, false) {
            @Override
            protected Comment entityInstance(BaseService service, Node node, XPathExpression xpath) {
                return new Comment(service, node, ConnectionsConstants.nameSpaceCtx, xpath);
            }
        };
    }

    public EntityList<Comment> getCommentsAwaitingApproval(Map<String, String> params)
            throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.GET_COMMENTS_AWAITING_APPROVAL.format(this,
                FileUrlParts.accessType.get(accessType));
        return this.getCommentEntityList(requestUri, params, null);
    }

    /**
     * Method to get a Community File
     * 
     * @param communityId
     * @param fileId
     * @return File
     * @throws ClientServicesException
     */
    public File getCommunityFile(String communityId, String fileId) throws ClientServicesException {
        return this.getCommunityFile(communityId, fileId, null);
    }

    /**
     * Method to get a Community File
     * 
     * @param communityId
     * @param fileId
     * @param params
     * @return File
     * @throws ClientServicesException
     */
    public File getCommunityFile(String communityId, String fileId, HashMap<String, String> params)
            throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUrl = FileUrls.GET_COMMUNITY_FILE.format(this, FileUrlParts.accessType.get(accessType),
                FileUrlParts.communityId.get(communityId), FileUrlParts.fileId.get(fileId));
        params = (null == params) ? new HashMap<String, String>() : params;
        return this.getFileEntity(requestUrl, params);
    }

    /**
     * Method to get a list of Community Files
     * 
     * @param communityId
     * @return
     * @throws ClientServicesException
     */
    public EntityList<File> getCommunityFiles(String communityId) throws ClientServicesException {
        return this.getCommunityFiles(communityId, null);
    }

    /**
     * Method to get a list of Community Files
     * 
     * @param communityId
     * @param params
     * @return FileList
     * @throws CommunityServiceException
     */
    public EntityList<File> getCommunityFiles(String communityId, HashMap<String, String> params)
            throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUrl = FileUrls.COMMUNITYLIBRARY_FEED.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.communityId.get(communityId));
        params = (null == params) ? new HashMap<String, String>() : params;
        return this.getFileEntityList(requestUrl, params);
    }

    /**
     * Method to get a list of Files shared with the Community
     * 
     * @param communityId
     * @return FileList
     * @throws ClientServicesException
     */
    public EntityList<File> getCommunitySharedFiles(String communityId) throws ClientServicesException {
        return this.getCommunitySharedFiles(communityId, null);
    }

    /**
     * Method to get a list of Files shared with the Community
     * 
     * @param communityId
     * @param params
     * @return FileList
     * @throws ClientServicesException
     */
    public EntityList<File> getCommunitySharedFiles(String communityId, HashMap<String, String> params)
            throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUrl = FileUrls.GET_COMMUNITY_COLLECTION.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.communityId.get(communityId));
        params = (null == params) ? new HashMap<String, String>() : params;
        return this.getFileEntityList(requestUrl, params);
    }

    /**
     * getFile
     * 
     * @param fileId - ID of the file to be fetched from the Connections Server
     * @return File
     * @throws ClientServicesException
     */
    public File getFile(String fileId) throws ClientServicesException {
        return this.getFile(fileId, true);
    }

    /**
     * getFile
     * 
     * @param fileId - ID of the file to be fetched from the Connections Server
     * @param load - a flag to determine whether the network call should be made or an empty placeholder of
     *        the File object should be returned. load - true : network call is made to fetch the
     *        file load - false : an empty File object is returned, and then updations can be made on
     *        this object.
     * @return File
     * @throws ClientServicesException
     */
    public File getFile(String fileId, boolean load) throws ClientServicesException {
        return this.getFile(fileId, null, load);
    }



    public File getFileAwaitingAction(String fileId) throws ClientServicesException {
        if (StringUtil.isEmpty(fileId)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        String requestUri = this.getModerationUri(fileId, Categories.APPROVAL.get(),
                ModerationContentTypes.DOCUMENTS.get());
        return this.getFileEntity(requestUri, null);
    }

    /**
     * retrieve a single comment from a file of the authenticated user
     * 
     * @param fileId
     * @param commentId
     * @param parameters a map of paramters; can be generated using the {@link FileCommentsParameterBuilder}
     * @return
     * @throws ClientServicesException
     */
    public EntityList<Comment> getFileComment(String fileId, String commentId,
            Map<String, String> parameters, Map<String, String> headers) throws ClientServicesException {
        if (StringUtil.isEmpty(fileId)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        if (StringUtil.isEmpty(commentId)) {
            throw new ClientServicesException(null, Messages.Invalid_CommentId);
        }

        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_COMMENT_ENTRY.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId),
                FileUrlParts.commentId.get(commentId));
        return this.getCommentEntityList(requestUri, parameters, headers);
    }

    /**
     * 
     * @return
     */
    public IFeedHandler<File> getFileFeedHandler() {
        return new AtomFeedHandler<File>(this, false) {
            @Override
            protected File entityInstance(BaseService service, Node node, XPathExpression xpath) {
                return new File(service, node, ConnectionsConstants.nameSpaceCtx, xpath);
            }
        };
    }



    /**
     * getFileFromRecycleBin
     * <p>
     * 
     * @param fileId
     * @param userId
     * @return
     * @throws ClientServicesException
     */
    public File getFileFromRecycleBin(String fileId, String userId) throws ClientServicesException {
        return this.getFileFromRecycleBin(fileId, userId, null);
    }


    private File getFileFromRecycleBin(String fileId, String userId, Map<String, String> params)
            throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri;
        if (StringUtil.isEmpty(userId)) {
            requestUri = FileUrls.MYUSERLIBRARY_RECYCLEBIN_ENTRY.format(this,
                    FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        } else {
            requestUri = FileUrls.USERLIBRARY_RECYCLEBIN_ENTRY.format(this,
                    FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId),
                    FileUrlParts.fileId.get(fileId));
        }
        return this.getFileEntity(requestUri, params);
    }

    // /files/basic/api/approval/documents
    /**
     * getFilesAwaitingApproval
     * 
     * @param params
     * @return
     * @throws ClientServicesException
     */
    public EntityList<File> getFilesAwaitingApproval(Map<String, String> params)
            throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.GET_FILES_AWAITING_APPROVAL.format(this,
                FileUrlParts.accessType.get(accessType));
        return this.getFileEntityList(requestUri, params);
    }

    /**
     * getFileShares
     * <p>
     * Get a feed that lists the share entries. A share entry describes an instance in which access that has been given to a file. <br>
     * Rest API used : /files/basic/api/documents/shared/feed
     * 
     * @return
     * @throws ClientServicesException
     */
    public EntityList<File> getFileShares() throws ClientServicesException {
        return this.getFileShares(null);
    }

    /**
     * getFileShares
     * <p>
     * Get a feed that lists the share entries. A share entry describes an instance in which access that has been given to a file. <br>
     * Rest API used : /files/basic/api/documents/shared/feed
     * 
     * @param params
     * @return
     * @throws ClientServicesException
     */
    public EntityList<File> getFileShares(Map<String, String> params) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.DOCUMENTS_SHARED_FEED.format(this,
                FileUrlParts.accessType.get(accessType));
        return this.getFileEntityList(requestUri, params);
    }



    /**
     * getFilesInMyRecycleBin
     * 
     * @return
     * @throws ClientServicesException
     */
    public EntityList<File> getFilesInMyRecycleBin() throws ClientServicesException {
        return this.getFilesInMyRecycleBin(null);
    }

    /**
     * getFilesInMyRecycleBin
     * <p>
     * Rest API used : /files/basic/api/myuserlibrary/view/recyclebin/feed
     * 
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileList
     * @throws ClientServicesException
     */

    public EntityList<File> getFilesInMyRecycleBin(Map<String, String> params) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYUSERLIBRARY_RECYCLEBIN_FEED.format(this,
                FileUrlParts.accessType.get(accessType));
        return this.getFileEntityList(requestUri, params);
    }

    /**
     * getFilesServiceDocument
     * <p>
     * Rest API Used : /files/basic/api/introspection
     * 
     * @return
     * @throws ClientServicesException
     */
    public Document getFilesServiceDocument() throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.SERVICE_DOCUMENT.format(this, FileUrlParts.accessType.get(accessType));
        Object result = null;
        result = this.getClientService().get(requestUri, null, ClientService.FORMAT_XML);
        return (Document) result;
    }
    
    /**
     * getFilesSharedWithMe
     * <p>
     * calls getFilesSharedWithMe(Map<String, String> params) with null params
     * 
     * @return FileList
     * @throws ClientServicesException
     */



    /**
     * getFileWithGivenVersion
     * <p>
     * Retrieve an Atom document representation of a version of a file from your library.<br>
     * Rest API Used : /files/basic/api/myuserlibrary/document/{document-id}/version/{version-id}/entry
     * 
     * @param fileId
     * @param versionId
     * @return File
     * @throws ClientServicesException
     */
    public File getFileWithGivenVersion(String fileId, String versionId) throws ClientServicesException {
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
     *        entry. Options are true or false. The default value is false.<br>
     *        > identifier : Indicates how the document is identified in the {document-id} variable
     *        segment of the web address. By default, the lookup operation is performed with the
     *        expectation that the URL contains the value from the <td:uuid>element of a document Atom entry, so the value uuid is used. Specify label if the URL instead contains the value from the <td:label>element of a document Atom entry.<br>
     *        > inline : Specifies whether the version content should be included in the content element of the returned Atom document. Options are true or false. The default value is false.
     * @return File
     * @throws ClientServicesException
     */
    public File getFileWithGivenVersion(String fileId, String versionId, Map<String, String> params)
            throws ClientServicesException {
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
     *        entry. Options are true or false. The default value is false.<br>
     *        > identifier : Indicates how the document is identified in the {document-id} variable
     *        segment of the web address. By default, the lookup operation is performed with the
     *        expectation that the URL contains the value from the <td:uuid>element of a document Atom entry, so the value uuid is used. Specify label if the URL instead contains the value from the <td:label>element of a document Atom entry.<br>
     *        > inline : Specifies whether the version content should be included in the content element of the returned Atom document. Options are true or false. The default value is false.
     * @param headers {@link FileReuestHeaders}<br>
     *        > If-Modified-Since : Used to validate the local cache of the feed and entry documents retrieved previously. If the feed or entry has not been modified since the specified date, HTTP response code 304 (Not Modified) is returned. <br>
     *        > If-None-Match : Contains an ETag response header sent by the server in a previous request to the same URL. If the ETag is still valid for the specified resource, HTTP response code 304 (Not Modified) is returned.
     * @return File
     * @throws ClientServicesException
     */
    public File getFileWithGivenVersion(String fileId, String versionId, Map<String, String> params,
            Map<String, String> headers) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.GET_FILE_WITH_GIVEN_VERSION.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId),
                FileUrlParts.versionId.get(versionId));
        return this.getFileEntity(requestUri, params);
    }

    public Comment getFlaggedComment(String commentId) throws ClientServicesException {
        String requestUri = this.getModerationUri(commentId, Categories.REVIEW.get(),
                ModerationContentTypes.COMMENT.get());
        return this.getCommentEntity(requestUri, null);
    }

    // /files/basic/api/review/comments
    public EntityList<Comment> getFlaggedComments(Map<String, String> params) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.GET_FLAGGED_COMMENTS.format(this,
                FileUrlParts.accessType.get(accessType));
        return this.getCommentEntityList(requestUri, params, null);
    }

    public File getFlaggedFile(String fileId) throws ClientServicesException {
        if (StringUtil.isEmpty(fileId)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        String requestUri = this.getModerationUri(fileId, Categories.REVIEW.get(),
                ModerationContentTypes.DOCUMENTS.get());
        return this.getFileEntity(requestUri, null);
    }

    // /files/basic/api/review/actions/documents/{document-id}
    public Document getFlaggedFileHistory(String fileId, Map<String, String> params)
            throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.GET_FLAGGED_FILE_HISTORY.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        return (Document) this.retrieveData(requestUri, params).getData();
    }

    // /files/basic/api/review/documents
    public EntityList<File> getFlaggedFiles(Map<String, String> params) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.GET_FLAGGED_FILES.format(this, FileUrlParts.accessType.get(accessType));
        return this.getFileEntityList(requestUri, params);
    }













    /**
     * getPublicFile
     * <p>
     * Rest API for getting files :- /files/basic/api/myuserlibrary/document/{document-id}/entry
     * 
     * @param fileId - ID of the file to be fetched from the Connections Server
     * @param libraryId - ID of the library to which the public file belongs
     * @param parameters - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return File
     * @throws ClientServicesException
     */
    public File getPublicFile(String fileId, String libraryId, Map<String, String> parameters)
            throws ClientServicesException {
        String accessType = AccessType.PUBLIC.getText();
        String requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_ENTRY.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.libraryId.get(libraryId),
                FileUrlParts.fileId.get(fileId));
        return this.getFileEntity(requestUri, parameters);
    }









    /**
     * 
     * @param fileId
     * @param userId
     * @param commentId
     * @param anonymousAccess
     * @param parameters a map of paramters; can be generated using the {@link FileCommentParameterBuilder}
     * @return
     * @throws ClientServicesException
     */

    public EntityList<Comment> getUserFileComment(String fileId, String userId, String commentId,
            boolean anonymousAccess, Map<String, String> parameters, Map<String, String> headers)
            throws ClientServicesException {
        String accessType = anonymousAccess ? AccessType.PUBLIC.getText() : AccessType.AUTHENTICATED
                .getText();
        String requestUri = FileUrls.USERLIBRARY_DOCUMENT_COMMENT_ENTRY.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId),
                FileUrlParts.fileId.get(fileId), FileUrlParts.commentId.get(commentId));
        return this.getCommentEntityList(requestUri, parameters, headers);
    }

    /**
     * lock
     * <p>
     * This method can be used to set a lock on File. <br>
     * Rest API used : /files/basic/api/document/{document-id}/lock <br>
     * 
     * @param fileId - fileId of the file to be locked.
     * @throws ClientServicesException
     */
    public void lock(String fileId) throws ClientServicesException {
        if (StringUtil.isEmpty(fileId)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.LOCK_FILE.format(this, FileUrlParts.accessType.get(accessType),
                FileUrlParts.fileId.get(fileId));
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.LOCK.getFileRequestParams(), FileConstants.LOCKTYPE_HARD);
        this.createData(requestUri, params, null);
    }

    /**
     * pinFile
     * <p>
     * Pin a file. <br>
     * Rest API Used : /files/basic/api/myfavorites/documents/feed
     * 
     * @param fileId
     * @throws ClientServicesException
     */
    public void pinFile(String fileId) throws ClientServicesException {
        if (StringUtil.isEmpty(fileId)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYFAVORITES_DOCUMENTS_FEED.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.ITEMID.getFileRequestParams(), fileId);
        this.createData(requestUri, params, null);
    }

    /**
     * * pinFolder
     * <p>
     * To pin a folder.<br>
     * Rest API Used : /files/basic/api/myfavorites/collections/feed
     * 
     * @param folderId
     * @throws ClientServicesException
     */
    public void pinFolder(String folderId) throws ClientServicesException {
        this.pinFolder(folderId, null);
    }

    /**
     * pinFolder
     * <p>
     * To pin a folder.<br>
     * Rest API Used : /files/basic/api/myfavorites/collections/feed
     * 
     * @param folderId
     * @param params filesAddedNotification - String. Indicates whether the collection should be added to
     *        notification at the same time. Options are on and off. The default value is on.
     * @throws ClientServicesException
     */
    public void pinFolder(String folderId, Map<String, String> params) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYFAVORITES_COLLECTIONS_FEED.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.folderId.get(folderId));
        params = (null == params) ? new HashMap<String, String>() : params;
        params.put(FileRequestParams.ITEMID.getFileRequestParams(), folderId);
        this.createData(requestUri, params, null);
    }


    /**
     * restoreFileFromRecycleBin
     * <p>
     * Restore a file to a document library from the trash. Restore a file from the logged in person's recycle bin. <br>
     * Rest API Used : /files/basic/api/myuserlibrary/view/recyclebin/{document-id}/entry
     * 
     * @param fileId
     * @return
     * @throws ClientServicesException
     */
    public File restoreFileFromRecycleBin(String fileId) throws ClientServicesException {
        return this.restoreFileFromRecycleBin(fileId, null);
    }

    /**
     * restoreFileFromRecycleBin
     * <p>
     * Restore a file to a document library from the trash. Restore a file from the specified person's recycle bin. <br>
     * Rest API Used : /files/basic/api/userlibrary/{userid}/view/recyclebin/{document-id}/entry
     * 
     * @param fileId
     * @param userId
     * @return
     * @throws ClientServicesException
     */
    public File restoreFileFromRecycleBin(String fileId, String userId) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri;
        if (StringUtil.isEmpty(userId)) {
            requestUri = FileUrls.MYUSERLIBRARY_RECYCLEBIN_ENTRY.format(this,
                    FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        } else {
            requestUri = FileUrls.USERLIBRARY_RECYCLEBIN_ENTRY.format(this,
                    FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId),
                    FileUrlParts.fileId.get(fileId));
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.UNDELETE.getFileRequestParams(), CommonConstants.TRUE);
        Map<String, String> headers = new HashMap<String, String>();
        Response data = this.updateData(requestUri, params, headers, null, null);
        return this.getFileFeedHandler().createEntity(data);
    }



    /**
     * unlock
     * <p>
     * This method can be used to unlock a File. <br>
     * Rest API used : /files/basic/api/document/{document-id}/lock <br>
     * 
     * @param fileId - fileId of the file to be unlocked.
     * @throws ClientServicesException
     */
    public void unlock(String fileId) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.LOCK_FILE.format(this, FileUrlParts.accessType.get(accessType),
                FileUrlParts.fileId.get(fileId));
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.LOCK.getFileRequestParams(), FileConstants.LOCKTYPE_NONE);
        this.createData(requestUri, params, null);
    }

    /**
     * unPinFile
     * <p>
     * Removes the file from the myfavorites feed. <br>
     * Rest API Used : /files/basic/api/myfavorites/documents/feed
     * 
     * @param fileId
     * @throws ClientServicesException
     */
    public void unPinFile(String fileId) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYFAVORITES_DOCUMENTS_FEED.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.ITEMID.getFileRequestParams(), fileId);
        this.deleteData(requestUri, params, null);
    }

    public void unPinFolder(String folderId) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYFAVORITES_COLLECTIONS_FEED.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.folderId.get(folderId));
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.ITEMID.getFileRequestParams(), folderId);
        this.deleteData(requestUri, params, null);
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
     * @throws ClientServicesException
     * @throws TransformerException
     */
    public Comment updateComment(String fileId, String commentId, Map<String, String> params,
            String comment) throws ClientServicesException, TransformerException {
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
     * @throws ClientServicesException
     * @throws TransformerException
     */
    public Comment updateComment(String fileId, String commentId, String comment)
            throws ClientServicesException, TransformerException {
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
     * @throws ClientServicesException
     * @throws TransformerException
     */
    public Comment updateComment(String fileId, String commentId, String comment, String userId)
            throws ClientServicesException, TransformerException {
        return this.updateComment(fileId, commentId, comment, userId, null);
    }

    /**
     * updateComment
     * <p>
     * Rest API used : /files/basic/api/userlibrary/{userid}/document/{document-id}/comment/{comment-id}/entry <br>
     * Updates comment from someone else's file whose userid is specified
     * 
     * @param File File for which the comment needs to be updated.
     * @param commentId Id of the comment to be updated.
     * @param params
     * @param comment New comment String.
     * @return Comment
     * @throws ClientServicesException
     * @throws TransformerException
     */
    public Comment updateComment(String fileId, String commentId, String comment, String userId,
            Map<String, String> params) throws ClientServicesException, TransformerException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri;
        if (StringUtil.isEmpty(userId)) {
            requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_COMMENT_ENTRY.format(this,
                    FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId),
                    FileUrlParts.commentId.get(commentId));
        } else {
            requestUri = FileUrls.USERLIBRARY_DOCUMENT_COMMENT_ENTRY.format(this,
                    FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId),
                    FileUrlParts.fileId.get(fileId), FileUrlParts.commentId.get(commentId));
        }
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        Comment c = new Comment(this, null);
        c.setContent(comment);
        c.setId(commentId);
        String payload = new CommentSerializer(c).generateCommentUpdatePayload();
        Response result = this.updateData(requestUri, params, headers, payload, null);
        return this.getCommentFeedHandler().createEntity(result);
    }

    /**
     * Method to update Community File. This method should be used to upload new version of a community file, and to simultaneously update the file's metadata.
     * Supported Parameters :
     * 
     * @see http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.5+API+Documentation
     *      #action=openDocument&res_title=Updating_a_file_ic45&content=pdcontent
     * @param iStream
     * @param fileId
     * @param title
     * @param params
     * @return File
     * @throws ClientServicesException
     */
    public File updateCommunityFile(java.io.InputStream iStream, String fileId, String title,
            String communityLibraryId, Map<String, String> params) throws ClientServicesException {
        File newVersionFile = this.uploadNewVersionCommunityFile(iStream, fileId, title, communityLibraryId,
                params);
        return this.updateCommunityFileMetadata(newVersionFile, communityLibraryId, params);
    }

    /**
     * Method to update Community File's Metadata
     * <p>
     * Rest API used : /files/basic/api/library/<communityLibraryId>/document/<fileId>/entry
     * <p>
     * 
     * @param fileEntry
     * @param communityLibraryId
     * @param params
     * @return File
     * @throws ClientServicesException
     */
    public File updateCommunityFileMetadata(File fileEntry, String communityLibraryId,
            Map<String, String> params) throws ClientServicesException {
        if (fileEntry == null) {
            throw new ClientServicesException(null, Messages.Invalid_File);
        }
        if (StringUtil.isEmpty(fileEntry.getFileId())) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.COMMUNITY_FILE_METADATA.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.communityId.get(communityLibraryId),
                FileUrlParts.fileId.get(fileEntry.getFileId()));
        String updateFilePayload = new FileSerializer(fileEntry).generateFileUpdatePayload();
        
        Response result = this.updateData(requestUri, params, new ClientService.ContentString(
                updateFilePayload, "application/atom+xml"), null);
        File r = this.getFileFeedHandler().createEntity(result);
        fileEntry.clearFieldsMap();
        fileEntry.setDataHandler(r.getDataHandler());
        return fileEntry;
    }

    // Need to figure out what should be done with the label updation of comment. Connection Doc states that
    // comment updations here can be done on comment content and on label. But what is the label of the
    // comment ? Need to check this.
    public void updateFlaggedComment(String commentId, String updatedComment) throws ClientServicesException,
            TransformerException {
        if (StringUtil.isEmpty(commentId)) {
            throw new ClientServicesException(null, Messages.Invalid_CommentId);
        }
        String requestUri = this.getModerationUri(commentId, Categories.REVIEW.get(),
                ModerationContentTypes.COMMENT.get());
        // File File = (File) executeGet(requestUri, null, ClientService.FORMAT_XML,
        // null).get(0);

        // Map<String, String> payloadMap = new HashMap<String, String>();
        // Map<String, String> paramsMap = new HashMap<String, String>();
        Map<String, String> headers = new HashMap<String, String>();
        // parseUpdationsMap(updationsMap, payloadMap, paramsMap);
        // if (payloadMap != null && !payloadMap.isEmpty()) {
        headers.put(Headers.ContentType, Headers.ATOM);
        // }
        
        Comment c = new Comment(this, null);
        c.setId(commentId);
        c.setContent(updatedComment);
        String payload = new CommentSerializer(c).generateCommentUpdatePayload();
        this.updateData(requestUri, null, headers, payload, null);
    }

    public void updateFlaggedFile(File file)
            throws ClientServicesException, TransformerException {
        if (file == null) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        String requestUri = this.getModerationUri(file.getFileId(), Categories.REVIEW.get(),
                ModerationContentTypes.DOCUMENTS.get());

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        String payload = new FileSerializer(file).generateFileUpdatePayload();
        this.updateData(requestUri, null, headers, payload, null);
    }



    /**
     * Method to upload a File to Community
     * 
     * @param iStream
     * @param communityId
     * @param title
     * @param length
     * @return File
     * @throws ClientServicesException
     */
    public File uploadCommunityFile(InputStream iStream, String communityId, final String title, long length)
            throws ClientServicesException {
        if (iStream == null) {
            throw new ClientServicesException(null, "null stream");
        }
        if (title == null) {
            throw new ClientServicesException(null, "null name");
        }
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.COMMUNITYLIBRARY_FEED.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.communityId.get(communityId));
        Content contentFile = this.getContentObject(title, iStream, length);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(FileConstants.X_UPDATE_NONCE, this.getNonce()); // It is not clearly documented which Content Type requires Nonce, thus adding nonce in header for all upload requests. 
        Response data = this.createData(requestUri, null, headers, contentFile);
        return this.getFileFeedHandler().createEntity(data);
    }


    /**
     * Method to Upload new version of a Community File
     * <p>
     * Supported parameters :
     * 
     * @see http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.5+API+Documentation
     *      #action=openDocument&res_title=Updating_a_file_ic45&content=pdcontent
     * @param iStream
     * @param fileId
     * @param length
     * @param title
     * @param communityId
     * @param params
     * @return File
     * @throws ClientServicesException
     */
    public File uploadNewVersionCommunityFile(java.io.InputStream iStream, String fileId, String title,
            String communityId, Map<String, String> params)
            throws ClientServicesException {
        if (StringUtil.isEmpty(fileId)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        if (iStream == null) {
            throw new ClientServicesException(null, Messages.Invalid_Stream);
        }
        if (title == null) {
            throw new ClientServicesException(null, Messages.Invalid_Name);
        }
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.UPLOAD_NEW_VERSION_COMMUNITY_FILE.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.communityId.get(communityId),
                FileUrlParts.fileId.get(fileId));
        ContentStream contentFile = (ContentStream) this.getContentObject(title, iStream);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(FileConstants.X_UPDATE_NONCE, this.getNonce()); // It is not clearly documented which Content Type requires Nonce, thus adding nonce in header for all upload requests. 
        return this.uploadNewVersion(requestUri, contentFile, params, headers);
    }

    /**
     * Method to Upload new version of a File
     * <p>
     * Supported parameters :
     * 
     * @see http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.5+API+Documentation
     *      #action=openDocument&res_title=Updating_a_file_ic45&content=pdcontent
     * @param content
     * @param fileId
     * @param length
     * @param fileName
     * @param params
     * @return File
     * @throws ClientServicesException
     */
    public File uploadNewVersionFile(java.io.InputStream iStream, String fileId, String title,
            Map<String, String> params)
            throws ClientServicesException {
        if (iStream == null) {
            throw new ClientServicesException(null, Messages.Invalid_Stream);
        }
        if (StringUtil.isEmpty(title)) {
            throw new ClientServicesException(null, Messages.Invalid_Name);
        }
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_ENTRY.format(this,
                FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));

        Content contentFile = this.getContentObject(title, iStream);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(FileConstants.X_UPDATE_NONCE, this.getNonce()); // It is not clearly documented which Content Type requires Nonce, thus adding nonce in header for all upload requests. 
        //TODO: check get data wrapping
        Response result = this.updateData(requestUri, params, headers, contentFile, null);
        return this.getFileFeedHandler().createEntity(result);
    }

    protected Comment getCommentEntity(String requestUrl, Map<String, String> parameters)
            throws ClientServicesException {
        if (parameters == null) {
            //TODO parameters = getCommentParams();
        }
        else {
            //TODO parameters.putAll(getCommentParams());
        }
        return this.getEntity(requestUrl, parameters, this.getCommentFeedHandler());
    }

    protected EntityList<Comment> getCommentEntityList(String requestUrl, Map<String, String> parameters,
            Map<String, String> headers) throws ClientServicesException {
        return this.getEntities(requestUrl, parameters, headers, this.getCommentFeedHandler());
    }

    /***************************************************************
     * Factory methods
     ****************************************************************/

    protected File getFileEntity(String requestUrl, Map<String, String> parameters)
            throws ClientServicesException {
        return this.getEntity(requestUrl, parameters, this.getFileFeedHandler());
    }

    protected EntityList<File> getFileEntityList(String requestUrl, Map<String, String> parameters)
            throws ClientServicesException {
        return this.getEntities(requestUrl, parameters, this.getFileFeedHandler());
    }

    protected FilesModerationDocumentEntry getFilesModerationDocumentEntry() throws ClientServicesException {
        return new FilesModerationDocumentEntry(this.getFilesModerationServiceDocument());
    }

    /**
     * getFilesModerationServiceDocument
     * <p>
     * Rest API Used : /files/basic/api/moderation/atomsvc
     * 
     * @return
     * @throws ClientServicesException
     */
    protected Document getFilesModerationServiceDocument() throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MODERATION_SERVICE_DOCUMENT.format(this,
                FileUrlParts.accessType.get(accessType));
        Response result = null;
        result = this.retrieveData(requestUri, null);
        return (Document) result.getData();
    }

    private Object constructPayloadForModeration(String fileId, String action, String actionReason,
            String entity) throws TransformerException {
        if (entity.equalsIgnoreCase("file")) {
            entity = "document";
        }
        Map<String, Object> fieldsMap = new HashMap<String, Object>();
        fieldsMap.put("entity", entity);
        fieldsMap.put("fileId", fileId);
        fieldsMap.put("content", actionReason);
        fieldsMap.put("action", action);
        ModerationTransformer moderationTransformer = new ModerationTransformer();
        String payload = moderationTransformer.transform(fieldsMap);
        return this.convertToXML(payload.toString());
    }


    /**
     * convertToXML
     * <p>
     * Utility method to construct XML payload
     * 
     * @param requestBody
     * @return Document
     */
    private Document convertToXML(String requestBody) throws TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(requestBody.toString())));
        } catch (ParserConfigurationException e) {
            throw new TransformerException(e, e.getMessage());
        } catch (SAXException e) {
            throw new TransformerException(e, e.getMessage());
        } catch (IOException e) {
            throw new TransformerException(e, e.getMessage());
        }
        return document;
    }

    /**
     * Method to create the content object based on the mime type.
     * Content type is set using the getMimeType method. In case the mime type cannot be found from the extension,
     * then default mime type is set by ClientService , which is "binary/octet-stream"
     * 
     * @param title
     * @param stream
     * @return Content
     */
    private Content getContentObject(String title, InputStream stream) {
        return this.getContentObject(title, stream, -1);
    }

    /**
     * Method to create the content object based on the mime type.
     * Content type is set using the getMimeType method. In case the mime type cannot be found from the extension,
     * then default mime type is set by ClientService , which is "binary/octet-stream"
     * 
     * @param title
     * @param stream
     * @param length
     * @return Content
     */
    private Content getContentObject(String title, InputStream stream, long length) {
        Content contentFile;
        if (StringUtil.isNotEmpty(this.getMimeType(title))) {
            contentFile = new ContentStream(title, stream, length, this.getMimeType(title));
        } else {
            contentFile = new ContentStream(stream, length, title);
        }
        return contentFile;
    }

    /**
     * Method to get the MIME type of the file to be uploaded, from the extension of the file
     * 
     * @param title
     * @return String
     */
    private String getMimeType(String title) {
        return MIME.getMIMETypeFromExtension(MIME.getFileExtension(title));
    }

    private String getModerationUri(String contentId, String action, String content)
            throws ClientServicesException {
        FilesModerationDocumentEntry fileModDocEntry = this.getFilesModerationDocumentEntry();
        // get the request URI from the service document.
        String requestUri = fileModDocEntry.get(ContentMapFiles.moderationMap.get("getFileUrl")); // TODO
        // If not obtained due to some reason, then construct the url in regular fashion.
        // URI : /files/basic/api/approval/documents
        if (StringUtil.isEmpty(requestUri)) {
            String accessType = AccessType.AUTHENTICATED.getText();
            requestUri = FileUrls.MODERATION.format(this, FileUrlParts.accessType.get(accessType),
                    FileUrlParts.action.get(action), FileUrlParts.contentType.get(content));
        }
        EntityList<File> resultantEntries;
        resultantEntries = this.getEntities(requestUri, null, this.getFileFeedHandler());

        String uri = null;
        for (File entry : resultantEntries) {
            if (entry.getFileId().equalsIgnoreCase(contentId)) {
                uri = entry.getAsString(FileEntryXPath.DeleteModeration);
            }
        }
        if (StringUtil.isEmpty(uri)) {
            return null;
        }
        return uri;
    }

    /**
     * @param requestUri
     * @param stream
     * @param params
     * @param headers
     * @return File
     * @throws ClientServicesException
     * @throws IOException
     */
    private File uploadNewVersion(String requestUri, ContentStream stream, Map<String, String> params,
            Map<String, String> headers) throws ClientServicesException {
        //TODO: check get data wrapping
        Response result = this.updateData(requestUri, params, headers, stream, null);
        return this.getFileFeedHandler().createEntity(result);
    }
}
