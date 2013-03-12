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
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.sbt.services.client.BaseService;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.Handler;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.files.exception.FileServiceException;
import com.ibm.sbt.services.client.connections.files.model.CommentEntry;
import com.ibm.sbt.services.client.connections.files.model.FileEntry;
import com.ibm.sbt.services.client.connections.files.model.FileRequestParams;
import com.ibm.sbt.services.client.connections.files.model.FileRequestPayload;
import com.ibm.sbt.services.client.connections.files.model.Headers;
import com.ibm.sbt.services.client.connections.files.utils.Messages;
import com.ibm.sbt.services.client.connections.files.utils.NamespacesConnections;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.sbt.services.util.AuthUtil;

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

	Endpoint			endpoint;

	static final String	sourceClass	= FileService.class.getName();
	static final Logger	logger		= Logger.getLogger(sourceClass);

	private static char	SEPARATOR	= '/';
	/**
	 * Used to display the status of execution of the API methods.
	 */
	public String		FileStatus	= "Success";

	/**
	 * Default Constructor - 0 argument constructor Calls the Constructor of BaseService Class.
	 * <p>
	 * Creates FileService with Default Endpoint.
	 */
	public FileService() {
		super();
		this.endpoint = EndpointFactory.getEndpoint(DEFAULT_ENDPOINT_NAME);
	}

	/**
	 * Constructor
	 * 
	 * @param endpoint
	 *            Creates fileservice with specified endpoint.
	 */
	public FileService(String endpoint) {
		super(endpoint);
		this.endpoint = EndpointFactory.getEndpoint(endpoint);
	}

	/**
	 * update
	 * <p>
	 * This method is used to update the metadata/content of File in Connections. <br>
	 * Rest API used : /files/basic/api/myuserlibrary/document/{document-id}/entry. <br>
	 * User should get the specific file before calling this API, by using getFile method.
	 * 
	 * @param FileEntry
	 *            - pass the fileEntry object to be updated
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @param payloadMap
	 *            - Map of entries for which we will construct a Request Body. See {@link FileRequestPayload}
	 *            for possible values.
	 * @return FileEntry
	 * @throws FileServiceException
	 */
	public FileEntry update(FileEntry fileEntry, Map<String, String> params, Map<String, String> payloadMap)
			throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "update");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.MYLIBRARY.getCategory();
		SubFilters subFilters = new SubFilters();
		if (fileEntry == null) {
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}
		if (StringUtil.isEmpty(fileEntry.getFileId())) {
			throw new IllegalArgumentException(Messages.InvalidArgument_2);
		}
		subFilters.setDocumentId(fileEntry.getFileId());
		String resultType = ResultType.ENTRY.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, null, null,
				subFilters, resultType); // we pass null value for non applicable types.
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(Headers.ContentType, Headers.ATOM);
		Object updateFilePayload = constructPayload(fileEntry, payloadMap);
		Document result = executePut(requestUri, params, headers, updateFilePayload);
		return updateFileEntryWithResponseFeed(fileEntry, result);
	}

	/**
	 * update
	 * <p>
	 * This method is used to update the metadata/content of File in Connections. <br>
	 * Rest API used : /files/basic/api/myuserlibrary/document/{document-id}/entry. <br>
	 * User should get the specific file before calling this API, by using getFile method.
	 * 
	 * @param FileEntry
	 *            - pass the fileEntry object to be updated
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.possible values.
	 * @param requestBody
	 *            - Document which is passed directly as requestBody to the execute request. This method is
	 *            used to update the metadata/content of File in Connections.
	 * @return FileEntry
	 * @throws FileServiceException
	 */
	public FileEntry update(FileEntry fileEntry, Map<String, String> params, Document requestBody)
			throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "update");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.MYLIBRARY.getCategory();
		SubFilters subFilters = new SubFilters();
		if (fileEntry == null) {
			logger.log(Level.SEVERE, Messages.InvalidArgument_1);
			return new FileEntry();
		}
		if (StringUtil.isEmpty(fileEntry.getFileId())) {
			logger.log(Level.SEVERE, Messages.InvalidArgument_2);
			return new FileEntry();
		}
		subFilters.setDocumentId(fileEntry.getFileId());
		String resultType = ResultType.ENTRY.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, null, null,
				subFilters, resultType); // we pass null value for non applicable types.
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(Headers.ContentType, Headers.ATOM);
		Document result = executePut(requestUri, params, headers, requestBody);
		return updateFileEntryWithResponseFeed(fileEntry, result);
	}

	/**
	 * upload
	 * <p>
	 * Rest API Used : /files/basic/api/myuserlibrary/feed <br>
	 * 
	 * @param filePath
	 *            - the path of the file on server, to be uploaded.
	 * @return FileEntry
	 * @throws FileServiceException
	 */
	public FileEntry upload(String filePath) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "upload");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.MYLIBRARY.getCategory();
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, null, null, null,
				resultType); // no file id sent here bec we want to upload a new file
		java.io.File content = new java.io.File(filePath);
		Document result = executePost(requestUri, null, null, content); // no parameters so passing null.
		if (null == result) {
			return (FileEntry) result;
		}
		return (FileEntry) parseAndProcessResultFeed(result, FileEntry.class).get(0);
	}

	/**
	 * uploadFileWithMetadata
	 * <p>
	 * Rest API Used : /files/basic/api/myuserlibrary/feed <br>
	 * 
	 * @description A file consists of both the information about the file, which is also known as metadata,
	 *              and the binary data that the file contains. You can provide either of the following
	 *              inputs: - Binary data and no Atom entry document to define the metadata. Metadata is
	 *              created automatically and sets all values to the default values, except for the value of
	 *              the title element, which it takes from the SLUG header. - Atom entry document that defines
	 *              the metadata of the file and no binary data.
	 * @param filePath
	 *            - the path of the file on server, to be uploaded.
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @param requestBody
	 *            - Document which is passed directly as requestBody to the execute request. This method is
	 *            used to update the metadata/content of File in Connections.
	 * @param headers
	 *            - Map of Headers. See {@link Headers} for possible values.
	 * @return FileEntry
	 * @throws FileServiceException
	 */
	public FileEntry uploadFileWithMetadata(String filePath, Map<String, String> params,
			Map<String, String> headers, Document requestBody) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "uploadFileWithMetadata");
		}
		Object content = null;
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.MYLIBRARY.getCategory();
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, null, null, null,
				resultType);
		if (StringUtil.isEmpty(filePath)) {
			logger.log(Level.SEVERE, Messages.UploadINFO_1);
			if (requestBody == null) {
				throw new IllegalArgumentException(Messages.UploadINFO_2);
			}
			content = requestBody;
		}
		if (content == null) {
			content = new File(filePath);
		}
		Document result = executePost(requestUri, params, headers, content);
		if (null == result) {
			return (FileEntry) result;
		}
		return (FileEntry) parseAndProcessResultFeed(result, FileEntry.class).get(0);
	}

    /**
     * upload
     * <p>
     * Rest API Used : /files/basic/api/myuserlibrary/feed <br>
     * 
     * @description A file consists of both the information about the file, which is also known as metadata,
     *              and the binary data that the file contains. You can provide either of the following
     *              inputs: - Binary data and no Atom entry document to define the metadata. Metadata is
     *              created automatically and sets all values to the default values, except for the value of
     *              the title element, which it takes from the SLUG header. - Atom entry document that defines
     *              the metadata of the file and no binary data.
     * @param filePath
     *            - the path of the file on server, to be uploaded.
     * @param params
     *            - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @param headers
     *            - Map of Headers. See {@link Headers} for possible values.
     * @return FileEntry
     * @throws FileServiceException
     */
    public FileEntry upload(Object content, Map<String, String> params, Map<String, String> headers) throws FileServiceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "upload", new Object[] { content, params, headers });
        }
        
        String accessType = AccessType.AUTHENTICATED.getAccessType();
        String category = Categories.MYLIBRARY.getCategory();
        String resultType = ResultType.FEED.getResultType();
        String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, null, null, null, resultType);
        Document result = executePost(requestUri, params, headers, content);
        
        FileEntry fileEntry = null;
        if (null != result) {
            fileEntry = (FileEntry)parseAndProcessResultFeed(result, FileEntry.class).get(0);
        }
        
        if (logger.isLoggable(Level.FINEST)) {
            logger.exiting(sourceClass, "upload", fileEntry);
        }
        return fileEntry;
    }

	/**
	 * lock
	 * <p>
	 * This method can be used to set a lock on File. <br>
	 * Rest API used : /files/basic/api/document/{document-id}/lock <br>
	 * 
	 * @param fileId
	 *            - fileId of the file to be locked.
	 * @return FileEntry
	 * @throws FileServiceException
	 */
	public FileEntry lock(String fileId) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "lock");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		SubFilters subFilters = new SubFilters();
		if (StringUtil.isEmpty(fileId)) {
			throw new IllegalArgumentException(Messages.InvalidArgument_2);
		}
		subFilters.setDocumentId(fileId);
		String resultType = ResultType.LOCK.getResultType();
		Map<String, String> params = new HashMap<String, String>();
		params.put(FileRequestParams.LOCK, "HARD");
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, null, null,
				subFilters, resultType);
		Document result = executePost(requestUri, params, null, null);
		if (null == result) {
			return (FileEntry) result;
		}
		return (FileEntry) parseAndProcessResultFeed(result, FileEntry.class).get(0);
	}

	/**
	 * unlock
	 * <p>
	 * This method can be used to unlock a File. <br>
	 * Rest API used : /files/basic/api/document/{document-id}/lock <br>
	 * 
	 * @param fileId
	 *            - fileId of the file to be unlocked.
	 * @return FileEntry
	 * @throws FileServiceException
	 */
	public FileEntry unlock(String fileId) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "unlock");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		SubFilters subFilters = new SubFilters();
		if (StringUtil.isEmpty(fileId)) {
			throw new IllegalArgumentException(Messages.InvalidArgument_2);
		}
		subFilters.setDocumentId(fileId);
		String resultType = ResultType.LOCK.getResultType();
		Map<String, String> params = new HashMap<String, String>();
		params.put(FileRequestParams.LOCK, "NONE");
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, null, null,
				subFilters, resultType);
		Document result = executePost(requestUri, params, null, null);
		if (null == result) {
			return (FileEntry) result;
		}
		return (FileEntry) parseAndProcessResultFeed(result, FileEntry.class).get(0);
	}

	/**
	 * delete
	 * <p>
	 * Rest API used : /files/basic/api/myuserlibrary/document/{document-id}/entry <br>
	 * 
	 * @param fileId
	 *            - id of the file to be deleted
	 * @throws FileServiceException
	 */
	public void delete(String fileId) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "delete");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.MYLIBRARY.getCategory();
		SubFilters subFilters = new SubFilters();
		if (StringUtil.isEmpty(fileId)) {
			throw new IllegalArgumentException(Messages.InvalidArgument_2);
		}
		subFilters.setDocumentId(fileId);
		String resultType = ResultType.ENTRY.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, null, null,
				subFilters, resultType);
		executeDelete(requestUri);
	}

	/**
	 * addCommentToFile
	 * <p>
	 * Rest API used : /files/basic/api/userlibrary/{userid}/document/{document-id}/feed <br>
	 * 
	 * @param FileEntry
	 *            - pass the fileEntry object to be updated
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @param comment
	 *            - Comment to be added to the File
	 * @return FileEntry
	 * @throws FileServiceException
	 */
	public FileEntry addCommentToFile(FileEntry fileEntry, Map<String, String> params, String comment)
			throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "addCommentToFile");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		SubFilters subFilters = new SubFilters();
		if (fileEntry == null) {
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}
		if (StringUtil.isEmpty(fileEntry.getFileId())) {
			throw new IllegalArgumentException(Messages.InvalidArgument_2);
		}
		if (StringUtil.isEmpty(fileEntry.getAuthorEntry().getUserUuid())) {
			throw new IllegalArgumentException(Messages.InvalidArgument_3);
		}
		subFilters.setUserId(fileEntry.getAuthorEntry().getUserUuid());
		subFilters.setDocumentId(fileEntry.getFileId());
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, null, null,
				subFilters, resultType);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(Headers.ContentType, Headers.ATOM);
		Object payload = constructPayloadForComments(comment);
		Document result = executePost(requestUri, params, headers, payload);
		if (result == null) {
			return (FileEntry) result;
		}
		return (FileEntry) parseAndProcessResultFeed(result, FileEntry.class).get(0);
	}

	/**
	 * addCommentToMyFile
	 * <p>
	 * Rest API used : /files/basic/api/myuserlibrary/document/{document-id}/feed <br>
	 * 
	 * @param FileEntry
	 *            - pass the fileEntry object to be updated
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @param comment
	 *            - Comment to be added to the File
	 * @return FileEntry
	 * @throws FileServiceException
	 */
	public FileEntry addCommentToMyFile(FileEntry fileEntry, Map<String, String> params, String comment)
			throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "addCommentToMyFile");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.MYLIBRARY.getCategory();
		SubFilters subFilters = new SubFilters();
		if (fileEntry == null) {
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}
		if (StringUtil.isEmpty(fileEntry.getFileId())) {
			throw new IllegalArgumentException(Messages.InvalidArgument_2);
		}
		subFilters.setDocumentId(fileEntry.getFileId());
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, null, null,
				subFilters, resultType);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(Headers.ContentType, Headers.ATOM);
		Object payload = constructPayloadForComments(comment);
		Document result = executePost(requestUri, params, headers, payload);
		if (result == null) {
			return (FileEntry) result;
		}
		return (FileEntry) parseAndProcessResultFeed(result, FileEntry.class).get(0);
	}

	/**
	 * addFilesToFolder
	 * <p>
	 * Rest API used : /files/basic/api/collection/{collection-id}/feed <br>
	 * 
	 * @param collectionId
	 *            ID of the Collection / Folder to which File(s) need to be added.
	 * @param FileEntry
	 *            Specifies the file to be added to the collection.
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @return List<FileEntry>
	 * @throws FileServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<FileEntry> addFilesToFolder(String collectionId, List<FileEntry> fileEntry,
			Map<String, String> params) throws FileServiceException, UnsupportedEncodingException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "addFilesToFolder");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		if (StringUtil.isEmpty(collectionId)) {
			throw new IllegalArgumentException(Messages.InvalidArgument_4);
		}
		if (fileEntry == null) {
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}
		for (FileEntry entry : fileEntry) {
			if (StringUtil.isEmpty(entry.getFileId())) {
				throw new IllegalArgumentException(Messages.InvalidArgument_2);
			}
		}
		SubFilters subFilters = new SubFilters();
		subFilters.setCollectionId(collectionId);
		String resultType = ResultType.FEED.getResultType();
		if (null == params) {
			params = new HashMap<String, String>();
		}
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, null, null,
				subFilters, resultType);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(Headers.ContentType, Headers.ATOM);
		Object payload = constructPayloadForMultipleEntries(fileEntry, FileRequestParams.ITEMID);
		Document result = executePost(requestUri, params, headers, payload);
		if (result == null) {
			return (List<FileEntry>) result;
		}
		List<Object> resultantObjects = parseAndProcessResultFeed(result, FileEntry.class);
		List<FileEntry> resultantEntries = new ArrayList<FileEntry>();
		for (Object eachObject : resultantObjects) {
			resultantEntries.add((FileEntry) eachObject);
		}
		return resultantEntries;
	}

	/**
	 * retrieveFileComment
	 * <p>
	 * Rest API used : /files/basic/api/userlibrary/{userid}/document/{document-id}/comment/{comment-id}/entry
	 * 
	 * @param FileEntry
	 * @param commentId
	 * @param params
	 * @return CommentEntry
	 * @throws FileServiceException
	 */
	public CommentEntry retrieveFileComment(FileEntry fileEntry, String commentId, Map<String, String> params)
			throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "retrieveFileComment");
		}
		if (StringUtil.isEmpty(commentId)) {
			throw new IllegalArgumentException(Messages.InvalidArgument_7);
		}
		if (fileEntry == null) {
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}
		if (StringUtil.isEmpty(fileEntry.getFileId())) {
			throw new IllegalArgumentException(Messages.InvalidArgument_2);
		}
		if (StringUtil.isEmpty(fileEntry.getAuthorEntry().getUserUuid())) {
			throw new IllegalArgumentException(Messages.InvalidArgument_3);
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		SubFilters subFilters = new SubFilters();
		subFilters.setUserId(fileEntry.getAuthorEntry().getUserUuid());
		subFilters.setDocumentId(fileEntry.getFileId());
		subFilters.setCommentId(commentId);
		String resultType = ResultType.ENTRY.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, null, null,
				subFilters, resultType);
		return (CommentEntry) executeGet(requestUri, params, ClientService.FORMAT_XML, CommentEntry.class)
				.get(0);
	}

	/**
	 * retrieveMyFileComment
	 * <p>
	 * Rest API used : /files/basic/api/myuserlibrary/document/{document-id}/comment/{comment-id}/entry
	 * 
	 * @param FileEntry
	 * @param commentId
	 * @param params
	 * @return CommentEntry
	 * @throws FileServiceException
	 */
	public CommentEntry retrieveMyFileComment(FileEntry fileEntry, String commentId,
			Map<String, String> params) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "retrieveMyFileComment");
		}
		if (StringUtil.isEmpty(commentId)) {
			throw new IllegalArgumentException(Messages.InvalidArgument_7);
		}
		if (fileEntry == null) {
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}
		if (StringUtil.isEmpty(fileEntry.getFileId())) {
			throw new IllegalArgumentException(Messages.InvalidArgument_2);
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.MYLIBRARY.getCategory();
		SubFilters subFilters = new SubFilters();
		subFilters.setDocumentId(fileEntry.getFileId());
		subFilters.setCommentId(commentId);
		String resultType = ResultType.ENTRY.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, null, null,
				subFilters, resultType);
		return (CommentEntry) executeGet(requestUri, params, ClientService.FORMAT_XML, CommentEntry.class)
				.get(0);
	}

	/**
	 * deleteComment
	 * <p>
	 * Rest API used : /files/basic/api/userlibrary/{userid}/document/{document-id}/comment/{comment-id}/entry
	 * 
	 * @param FileEntry
	 *            specifies the file for which the comment needs to be deleted.
	 * @param commentId
	 *            Id of the comment to be deleted.
	 * @throws FileServiceException
	 */
	public void deleteComment(FileEntry fileEntry, String commentId) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "deleteComment");
		}
		if (fileEntry == null) {
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}
		if (StringUtil.isEmpty(fileEntry.getFileId())) {
			throw new IllegalArgumentException(Messages.InvalidArgument_2);
		}
		if (StringUtil.isEmpty(fileEntry.getAuthorEntry().getUserUuid())) {
			throw new IllegalArgumentException(Messages.InvalidArgument_3);
		}
		if (StringUtil.isEmpty(commentId)) {
			throw new IllegalArgumentException(Messages.InvalidArgument_7);
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		SubFilters subFilters = new SubFilters();
		subFilters.setUserId(fileEntry.getAuthorEntry().getUserUuid());
		subFilters.setDocumentId(fileEntry.getFileId());
		subFilters.setCommentId(commentId);
		String resultType = ResultType.ENTRY.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, null, null,
				subFilters, resultType);
		executeDelete(requestUri);
	}

	/**
	 * deleteComment
	 * <p>
	 * Rest API used : /files/basic/api/myuserlibrary/document/{document-id}/comment/{comment-id}/entry
	 * 
	 * @param fileId
	 * @param commentId
	 * @throws FileServiceException
	 */
	public void deleteMyComment(FileEntry fileEntry, String commentId) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "deleteMyComment");
		}
		if (fileEntry == null) {
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}
		if (StringUtil.isEmpty(fileEntry.getFileId())) {
			throw new IllegalArgumentException(Messages.InvalidArgument_2);
		}
		if (StringUtil.isEmpty(commentId)) {
			throw new IllegalArgumentException(Messages.InvalidArgument_7);
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.MYLIBRARY.getCategory();
		SubFilters subFilters = new SubFilters();
		subFilters.setDocumentId(fileEntry.getFileId());
		subFilters.setCommentId(commentId);
		String resultType = ResultType.ENTRY.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, null, null,
				subFilters, resultType);
		executeDelete(requestUri);
	}

	/**
	 * updateComment
	 * <p>
	 * Rest API used : /files/basic/api/userlibrary/{userid}/document/{document-id}/comment/{comment-id}/entry
	 * <br>
	 * Updates comment from someone else's file whose userid is specified
	 * 
	 * @param FileEntry
	 *            File for which the comment needs to be updated.
	 * @param commentId
	 *            Id of the comment to be updated.
	 * @param params
	 * @param comment
	 *            New comment String.
	 * @return CommentEntry
	 * @throws FileServiceException
	 */
	public CommentEntry updateComment(FileEntry fileEntry, String commentId, Map<String, String> params,
			String comment) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "updateComment");
		}
		if (fileEntry == null) {
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}
		if (StringUtil.isEmpty(fileEntry.getFileId())) {
			throw new IllegalArgumentException(Messages.InvalidArgument_2);
		}
		if (StringUtil.isEmpty(fileEntry.getAuthorEntry().getUserUuid())) {
			throw new IllegalArgumentException(Messages.InvalidArgument_3);
		}
		if (StringUtil.isEmpty(commentId)) {
			throw new IllegalArgumentException(Messages.InvalidArgument_7);
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		SubFilters subFilters = new SubFilters();
		subFilters.setUserId(fileEntry.getAuthorEntry().getUserUuid());
		subFilters.setDocumentId(fileEntry.getFileId());
		subFilters.setCommentId(commentId);
		String resultType = ResultType.ENTRY.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, null, null,
				subFilters, resultType);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(Headers.ContentType, Headers.ATOM);
		Object payload = constructPayloadForComments(comment);
		Document result = executePut(requestUri, params, headers, payload);
		if (result == null) {
			return (CommentEntry) result;
		}
		return (CommentEntry) parseAndProcessResultFeed(result, CommentEntry.class).get(0);
	}

	/**
	 * updateComment
	 * <p>
	 * Rest API used : /files/basic/api/myuserlibrary/document/{document-id}/comment/{comment-id}/entry <br>
	 * Updates comment in one of "my" library file
	 * 
	 * @param FileEntry
	 *            File for which the comment needs to be updated.
	 * @param commentId
	 *            Id of the comment to be updated.
	 * @param params
	 * @param comment
	 *            New comment String.
	 * @return CommentEntry
	 * @throws FileServiceException
	 */
	public CommentEntry updateMyComment(FileEntry fileEntry, String commentId, Map<String, String> params,
			String comment) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "updateMyComment");
		}
		if (fileEntry == null) {
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}
		if (StringUtil.isEmpty(fileEntry.getFileId())) {
			throw new IllegalArgumentException(Messages.InvalidArgument_2);
		}
		if (StringUtil.isEmpty(commentId)) {
			throw new IllegalArgumentException(Messages.InvalidArgument_7);
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.MYLIBRARY.getCategory();
		SubFilters subFilters = new SubFilters();
		subFilters.setDocumentId(fileEntry.getFileId());
		subFilters.setCommentId(commentId);
		String resultType = ResultType.ENTRY.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, null, null,
				subFilters, resultType);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(Headers.ContentType, Headers.ATOM);
		Object payload = constructPayloadForComments(comment);
		Document result = executePut(requestUri, params, headers, payload);
		if (result == null) {
			return (CommentEntry) result;
		}
		return (CommentEntry) parseAndProcessResultFeed(result, CommentEntry.class).get(0);
	}

	/**
	 * getNonce
	 * <p>
	 * Returns the Cryptographic Key - Nonce value obtained from Connections Server <br>
	 * Rest API used : /files/basic/api/nonce
	 * 
	 * @return String - nonce value
	 */
	public String getNonce() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getNonce");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String resultType = ResultType.NONCE.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, null, null, null,
				resultType);
		Object result = null;
		try {
			result = getClientService().get(requestUri, null, ClientService.FORMAT_TEXT);
		} catch (ClientServicesException e) {
			logger.log(Level.SEVERE, Messages.FileServiceException_1 + "getNonce()", e);
			setStatus();
		}
		if (result == null) {
			logger.log(Level.SEVERE, Messages.NonceInfo_1 + result);
		}
		setStatus("Success");
		return (String) result;
	}

	/**
	 * getMyFiles
	 * <p>
	 * calls getMyFiles(Map<String, String> params) internally with null parameters, if user has not specific
	 * any params
	 * 
	 * @return List<FileEntry>
	 * @throws FileServiceException
	 */

	public List<FileEntry> getMyFiles() throws FileServiceException {
		return getMyFiles(null);
	}

	/**
	 * getMyFiles
	 * <p>
	 * Rest API used : /files/basic/api/myuserlibrary/feed <br>
	 * This method is used to get Files of the person.
	 * 
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @return List<FileEntry>
	 * @throws FileServiceException
	 */

	public List<FileEntry> getMyFiles(Map<String, String> params) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMyFiles");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.MYLIBRARY.getCategory();
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, null, null, null,
				resultType);

		List<Object> resultantObjects = executeGet(requestUri, params, ClientService.FORMAT_XML,
				FileEntry.class);
		List<FileEntry> resultantEntries = new ArrayList<FileEntry>();
		for (Object eachObject : resultantObjects) {
			resultantEntries.add((FileEntry) eachObject);
		}
		return resultantEntries;
	}

	/**
	 * getFilesSharedWithMe
	 * <p>
	 * calls getFilesSharedWithMe(Map<String, String> params) with null params
	 * 
	 * @return List<FileEntry>
	 * @throws FileServiceException
	 */

	public List<FileEntry> getFilesSharedWithMe() throws FileServiceException {
		return getFilesSharedWithMe(null);
	}

	/**
	 * getFilesSharedWithMe
	 * <p>
	 * Rest API used : /files/basic/api/documents/shared/feed <br>
	 * This method is used to get Files Shared With the person.
	 * 
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @return List<FileEntry>
	 * @throws FileServiceException
	 */

	public List<FileEntry> getFilesSharedWithMe(Map<String, String> params) throws FileServiceException {
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
		params.put(FileRequestParams.DIRECTION, "inbound");
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, view, filter, null,
				resultType);

		List<Object> resultantObjects = executeGet(requestUri, params, ClientService.FORMAT_XML,
				FileEntry.class);
		List<FileEntry> resultantEntries = new ArrayList<FileEntry>();
		for (Object eachObject : resultantObjects) {
			resultantEntries.add((FileEntry) eachObject);
		}
		return resultantEntries;
	}

	/**
	 * getFilesSharedByMe
	 * <p>
	 * This method calls getFilesSharedByMe(Map<String, String> params) with null params
	 * 
	 * @return List<FileEntry>
	 * @throws FileServiceException
	 */
	public List<FileEntry> getFilesSharedByMe() throws FileServiceException {
		return getFilesSharedByMe(null);
	}

	/**
	 * getFilesSharedByMe
	 * <p>
	 * Rest API used : /files/basic/api/documents/shared/feed <br>
	 * This method is used to get Files Shared By the person.
	 * 
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @return List<FileEntry>
	 * @throws FileServiceException
	 */

	public List<FileEntry> getFilesSharedByMe(Map<String, String> params) throws FileServiceException {
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
		params.put(FileRequestParams.DIRECTION, "outbound");
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, view, filter, null,
				resultType);
		List<Object> resultantObjects = executeGet(requestUri, params, ClientService.FORMAT_XML,
				FileEntry.class);
		List<FileEntry> resultantEntries = new ArrayList<FileEntry>();
		for (Object eachObject : resultantObjects) {
			resultantEntries.add((FileEntry) eachObject);
		}
		return resultantEntries;
	}

	/**
	 * getPublicFiles
	 * <p>
	 * Rest API used : /files/basic/anonymous/api/documents/feed <br>
	 * This method returns a list of Public Files.
	 * 
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @return List<FileEntry>
	 * @throws FileServiceException
	 */

	public List<FileEntry> getPublicFiles(Map<String, String> params) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getPublicFiles");
		}
		String accessType = AccessType.PUBLIC.getAccessType();
		String view = Views.FILES.getViews();
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, view, null, null,
				resultType);
		if (params == null) {
			params = new HashMap<String, String>();
		}
		params.put(FileRequestParams.VISIBILITY, "public");
		List<Object> resultantObjects = executeGet(requestUri, params, ClientService.FORMAT_XML,
				FileEntry.class);
		List<FileEntry> resultantEntries = new ArrayList<FileEntry>();
		for (Object eachObject : resultantObjects) {
			resultantEntries.add((FileEntry) eachObject);
		}
		return resultantEntries;
	}

	/**
	 * getPinnedFiles
	 * <p>
	 * Rest API used : /files/basic/api/myfavorites/documents/feed
	 * 
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @return List<FileEntry>
	 * @throws FileServiceException
	 */

	public List<FileEntry> getPinnedFiles(Map<String, String> params) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getPinnedFiles");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.PINNED.getCategory();
		String view = Views.FILES.getViews();
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, view, null, null,
				resultType);
		List<Object> resultantObjects = executeGet(requestUri, params, ClientService.FORMAT_XML,
				FileEntry.class);
		List<FileEntry> resultantEntries = new ArrayList<FileEntry>();
		for (Object eachObject : resultantObjects) {
			resultantEntries.add((FileEntry) eachObject);
		}
		return resultantEntries;
	}

	/**
	 * getMyFolders
	 * <p>
	 * Rest API used : /files/basic/api/collections/feed Required Parameters : creator={snx:userid}
	 * 
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @return List<FileEntry>
	 * @throws FileServiceException
	 */

	public List<FileEntry> getMyFolders(Map<String, String> params) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMyFolders");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String view = Views.FOLDERS.getViews();
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, view, null, null,
				resultType);
		List<Object> resultantObjects = executeGet(requestUri, params, ClientService.FORMAT_XML,
				FileEntry.class);
		List<FileEntry> resultantEntries = new ArrayList<FileEntry>();
		for (Object eachObject : resultantObjects) {
			resultantEntries.add((FileEntry) eachObject);
		}
		return resultantEntries;
	}

	/**
	 * getMyPinnedFolders
	 * <p>
	 * Rest API used : /files/basic/api/myfavorites/collections/feed
	 * 
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @return List<FileEntry>
	 * @throws FileServiceException
	 */

	public List<FileEntry> getMyPinnedFolders(Map<String, String> params) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMyPinnedFolders");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.PINNED.getCategory();
		String view = Views.FOLDERS.getViews();
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, view, null, null,
				resultType);
		List<Object> resultantObjects = executeGet(requestUri, params, ClientService.FORMAT_XML,
				FileEntry.class);
		List<FileEntry> resultantEntries = new ArrayList<FileEntry>();
		for (Object eachObject : resultantObjects) {
			resultantEntries.add((FileEntry) eachObject);
		}
		return resultantEntries;
	}

	/**
	 * getFoldersWithRecentlyAddedFiles
	 * <p>
	 * Rest API used : /files/basic/api/collections/addedto/feed
	 * 
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @return List<FileEntry>
	 * @throws FileServiceException
	 */

	public List<FileEntry> getFoldersWithRecentlyAddedFiles(Map<String, String> params)
			throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getFoldersWithRecentlyAddedFiles");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String view = Views.FOLDERS.getViews();
		String filter = Filters.ADDEDTO.getFilters();
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, view, filter, null,
				resultType);
		List<Object> resultantObjects = executeGet(requestUri, params, ClientService.FORMAT_XML,
				FileEntry.class);
		List<FileEntry> resultantEntries = new ArrayList<FileEntry>();
		for (Object eachObject : resultantObjects) {
			resultantEntries.add((FileEntry) eachObject);
		}
		return resultantEntries;
	}

	/**
	 * getFilesInFolder
	 * <p>
	 * Rest API used : /files/basic/api/collection/{collection-id}/feed
	 * 
	 * @param collectionId
	 *            - uuid of the folder/collection.
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @return List<FileEntry>
	 * @throws FileServiceException
	 */

	public List<FileEntry> getFilesInFolder(String collectionId, Map<String, String> params)
			throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getFilesInFolder");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		SubFilters subFilters = new SubFilters();
		if (StringUtil.isEmpty(collectionId)) {
			throw new IllegalArgumentException(Messages.InvalidArgument_4);
		}
		subFilters.setCollectionId(collectionId);
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, null, null,
				subFilters, resultType);
		List<Object> resultantObjects = executeGet(requestUri, params, ClientService.FORMAT_XML,
				FileEntry.class);
		List<FileEntry> resultantEntries = new ArrayList<FileEntry>();
		for (Object eachObject : resultantObjects) {
			resultantEntries.add((FileEntry) eachObject);
		}
		return resultantEntries;
	}

	/**
	 * getPublicFileFolders
	 * <p>
	 * Rest API used : /files/basic/anonymous/api/collections/feed <br>
	 * Public method. No Auth required.
	 * 
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @return List<FileEntry>
	 * @throws FileServiceException
	 */

	public List<FileEntry> getPublicFileFolders(Map<String, String> params) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getPublicFileFolders");
		}
		String accessType = AccessType.PUBLIC.getAccessType();
		String view = Views.FOLDERS.getViews();
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, view, null, null,
				resultType);
		List<Object> resultantObjects = executeGet(requestUri, params, ClientService.FORMAT_XML,
				FileEntry.class);
		List<FileEntry> resultantEntries = new ArrayList<FileEntry>();
		for (Object eachObject : resultantObjects) {
			resultantEntries.add((FileEntry) eachObject);
		}
		return resultantEntries;
	}

	/**
	 * getPersonLibrary
	 * <p>
	 * Rest API used : /files/basic/anonymous/api/userlibrary/{userid}/feed <br>
	 * Public method. No Auth required.
	 * 
	 * @param userId
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @return List<FileEntry>
	 * @throws FileServiceException
	 */

	public List<FileEntry> getPersonLibrary(String userId, Map<String, String> params)
			throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getPersonLibrary");
		}
		String accessType = AccessType.PUBLIC.getAccessType();
		SubFilters subFilters = new SubFilters();
		if (StringUtil.isEmpty(userId)) {
			throw new IllegalArgumentException(Messages.InvalidArgument_3);
		}
		subFilters.setUserId(userId);
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, null, null,
				subFilters, resultType);
		List<Object> resultantObjects = executeGet(requestUri, params, ClientService.FORMAT_XML,
				FileEntry.class);
		List<FileEntry> resultantEntries = new ArrayList<FileEntry>();
		for (Object eachObject : resultantObjects) {
			resultantEntries.add((FileEntry) eachObject);
		}
		return resultantEntries;
	}

	/**
	 * getPublicFilesComments
	 * <p>
	 * Rest API used : /files/basic/anonymous/api/userlibrary/{userid}/document/{document-id}/feed <br>
	 * Public method. No Auth required.
	 * 
	 * @param FileEntry
	 *            - pass the fileEntry object to be updated
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @return List<CommentEntry>
	 * @throws FileServiceException
	 */

	public List<CommentEntry> getPublicFilesComments(FileEntry fileEntry, Map<String, String> params)
			throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getPublicFilesComments");
		}
		String accessType = AccessType.PUBLIC.getAccessType();
		SubFilters subFilters = new SubFilters();
		if (StringUtil.isEmpty(fileEntry.getAuthorEntry().getUserUuid())) {
			throw new IllegalArgumentException(Messages.InvalidArgument_3);
		}
		subFilters.setUserId(fileEntry.getAuthorEntry().getUserUuid());
		if (StringUtil.isEmpty(fileEntry.getFileId())) {
			throw new IllegalArgumentException(Messages.InvalidArgument_2);
		}
		subFilters.setDocumentId(fileEntry.getFileId());
		String resultType = ResultType.FEED.getResultType();
		if (params == null) {
			params = new HashMap<String, String>();
		}
		params.put(FileRequestParams.CATEGORY, "comment");
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, null, null,
				subFilters, resultType);
		List<Object> resultantObjects = executeGet(requestUri, params, ClientService.FORMAT_XML,
				CommentEntry.class);
		List<CommentEntry> resultantEntries = new ArrayList<CommentEntry>();
		for (Object eachObject : resultantObjects) {
			resultantEntries.add((CommentEntry) eachObject);
		}
		return resultantEntries;
	}

	/**
	 * getFilesComments
	 * <p>
	 * Rest API used : /files/basic/api/userlibrary/{userid}/document/{document-id}/feed
	 * 
	 * @param FileEntry
	 *            - pass the fileEntry object to be updated
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @return List<CommentEntry>
	 * @throws FileServiceException
	 */

	public List<CommentEntry> getFilesComments(FileEntry fileEntry, Map<String, String> params)
			throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getFilesComments");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		SubFilters subFilters = new SubFilters();
		if (fileEntry == null) {
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}
		if (StringUtil.isEmpty(fileEntry.getAuthorEntry().getUserUuid())) {
			throw new IllegalArgumentException(Messages.InvalidArgument_3);
		}
		subFilters.setUserId(fileEntry.getAuthorEntry().getUserUuid());
		if (StringUtil.isEmpty(fileEntry.getFileId())) {
			throw new IllegalArgumentException(Messages.InvalidArgument_2);
		}
		subFilters.setDocumentId(fileEntry.getFileId());
		String resultType = ResultType.FEED.getResultType();
		if (params == null) {
			params = new HashMap<String, String>();
		}
		params.put(FileRequestParams.CATEGORY, "comment");
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, null, null,
				subFilters, resultType);
		List<Object> resultantObjects = executeGet(requestUri, params, ClientService.FORMAT_XML,
				CommentEntry.class);
		List<CommentEntry> resultantEntries = new ArrayList<CommentEntry>();
		for (Object eachObject : resultantObjects) {
			resultantEntries.add((CommentEntry) eachObject);
		}
		return resultantEntries;
	}

	/**
	 * getMyFilesComments
	 * <p>
	 * Rest API used : /files/basic/api/myuserlibrary/document/{document-id}/feed
	 * 
	 * @param FileEntry
	 *            - pass the fileEntry object to be updated
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @return List<CommentEntry>
	 * @throws FileServiceException
	 */

	public List<CommentEntry> getMyFilesComments(FileEntry fileEntry, Map<String, String> params)
			throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMyFilesComments");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.MYLIBRARY.getCategory();
		SubFilters subFilters = new SubFilters();
		if (fileEntry == null) {
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}
		if (StringUtil.isEmpty(fileEntry.getFileId())) {
			throw new IllegalArgumentException(Messages.InvalidArgument_2);
		}
		subFilters.setDocumentId(fileEntry.getFileId());
		String resultType = ResultType.FEED.getResultType();
		if (params == null) {
			params = new HashMap<String, String>();
		}
		params.put(FileRequestParams.CATEGORY, "comment");
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, null, null,
				subFilters, resultType);
		List<Object> resultantObjects = executeGet(requestUri, params, ClientService.FORMAT_XML,
				CommentEntry.class);
		List<CommentEntry> resultantEntries = new ArrayList<CommentEntry>();
		for (Object eachObject : resultantObjects) {
			resultantEntries.add((CommentEntry) eachObject);
		}
		return resultantEntries;
	}

	/**
	 * getFilesInMyRecycleBin
	 * <p>
	 * Rest API used : /files/basic/api/myuserlibrary/view/recyclebin/feed
	 * 
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @return List<FileEntry>
	 * @throws FileServiceException
	 */

	public List<FileEntry> getFilesInMyRecycleBin(Map<String, String> params) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getFilesInMyRecycleBin");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.MYLIBRARY.getCategory();
		String view = Views.RECYCLEBIN.getViews();
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, view, null, null,
				resultType);
		List<Object> resultantObjects = executeGet(requestUri, params, ClientService.FORMAT_XML,
				FileEntry.class);
		List<FileEntry> resultantEntries = new ArrayList<FileEntry>();
		for (Object eachObject : resultantObjects) {
			resultantEntries.add((FileEntry) eachObject);
		}
		return resultantEntries;
	}

	/**
	 * constructUrl
	 * <p>
	 * This method is used to construct the URL for the API execution. The General Pattern of the URL is :: <br>
	 * baseUrl {@link BaseUrl} + authType(basic or oauth) + AccessType {@link AccessType} + Category
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
	public String constructUrl(String baseUrl, String accessType, String category, String view,
			String filter, SubFilters subFilters, String resultType) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "constructUrl");
		}
		// here we will set the value in API after constructing the url
		// if the user has set these values then ok. otherwise, we set the default to GetMyFiles :
		// /files/basic/api/myuserlibrary/feed
		StringBuilder url = new StringBuilder(baseUrl);
		url.append(SEPARATOR).append(AuthUtil.INSTANCE.getAuthValue(endpoint));
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
			if (!StringUtil.isEmpty(subFilters.getDocumentId())) {
				url = url.append(subFilters.DOCUMENT).append(SEPARATOR).append(subFilters.getDocumentId());
			}
			if (!StringUtil.isEmpty(subFilters.getCommentId())) {
				url = url.append(subFilters.COMMENT).append(SEPARATOR).append(subFilters.getCommentId());
			}
		}

		if (!StringUtil.isEmpty(resultType)) {
			url.append(resultType);
		}
		return url.toString();
	}

	/**
	 * constructPayloadForComments
	 * 
	 * @param comment
	 *            - comment for which a payload Document needs to be constructed.
	 * @return Document - payload Document which is sent as part of the request body.
	 */

	public Document constructPayloadForComments(String comment) {
		return constructPayloadForComments(null, comment);
	}

	/**
	 * constructPayloadForComments
	 * 
	 * @param operation
	 *            - used to determine whether we need the payload for adding / deleting / updating a comment.
	 * @param commentToBeAdded
	 *            - plaintext comment which needs to be added to the File.
	 * @return Document - payload Document which is sent as part of the request body.
	 */

	public Document constructPayloadForComments(String operation, String commentToBeAdded) {
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
		return convertToXML(payload.toString());
	}

	/**
	 * constructPayload
	 * <p>
	 * This method constructs the Atom entry document for the APIs requiring payload input. Currently this
	 * method constructs payload for updating Label, Summary, Visibility, Title of the file.
	 * 
	 * @param FileEntry
	 *            - pass the fileEntry object to be updated
	 * @param payloadMap
	 *            - Map of entries for which we will construct a Request Body. See {@link FileRequestPayload}
	 *            for possible values.
	 * @return Document
	 */

	public Document constructPayload(FileEntry fileEntry, Map<String, String> payloadMap) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "constructPayload");
		}
		if (payloadMap == null || payloadMap.isEmpty()) {
			logger.log(Level.ALL, Messages.PayloadInfo_1);
			return null;
			// throw new IllegalArgumentException(Messages.PayloadInfo_1);
		}
		StringBuilder requestBody = new StringBuilder("<entry xmlns=\"http://www.w3.org/2005/Atom\">");
		requestBody
				.append("<category term=\"document\" label=\"document\" scheme=\"tag:ibm.com,2006:td/type\"></category>");
		requestBody.append("<id>urn:lsid:ibm.com:td:" + fileEntry.getFileId() + "</id>");

		Iterator<Map.Entry<String, String>> entries = payloadMap.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, String> fieldMapPairs = entries.next();
			String key = fieldMapPairs.getKey();
			String value = fieldMapPairs.getValue();
			if (!StringUtil.isEmpty(key) && !StringUtil.isEmpty(value)) {
				// here we handle the cases of setting label/title/summary/visibility
				if (key.equals("label")) {
					requestBody.append("<label xmlns=\""
							+ NamespacesConnections.nameSpaceCtx.getNamespaceURI("td") + "\">" + value
							+ "</label>");
					requestBody.append("<title>" + value + "</title>");
				} else if (key.equals("summary")) {
					requestBody.append("<summary type=\"text\">" + value + "</summary>");
				} else if (key.equals("visibility")) {
					requestBody.append("<visibility xmlns=\""
							+ NamespacesConnections.nameSpaceCtx.getNamespaceURI("td") + "\">" + value
							+ "</visibility>");
				}
			}
			entries.remove();
		}
		requestBody.append("</entry>");
		return convertToXML(requestBody.toString());
	}

	public Document constructPayloadForMultipleEntries(List<FileEntry> fileEntries, String multipleEntryId) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "constructPayloadForMultipleEntries");
		}
		StringBuilder requestBody = new StringBuilder(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><feed xmlns=\"http://www.w3.org/2005/Atom\">");
		for (FileEntry entry : fileEntries) {
			requestBody.append("<entry><" + multipleEntryId + " xmlns=\"urn:ibm.com/td\">"
					+ entry.getFileId() + "</" + multipleEntryId + "></entry>");
		}
		requestBody.append("</feed>");
		System.err.println("requestBody " + requestBody.toString());
		return convertToXML(requestBody.toString());
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
		DocumentBuilder builder;
		Document document = null;
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(new InputSource(new StringReader(requestBody.toString())));
		} catch (Exception e) {
			logger.log(Level.SEVERE, Messages.FileServiceException_1 + "convertToXML()", e);
		}
		return document;
	}

	/**
	 * executeDelete
	 * 
	 * @param requestUri
	 * @throws FileServiceException
	 */

	public void executeDelete(String requestUri) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "executeDelete");
		}
		Object result = null;
		try {
			result = endpoint.getClientService().delete(requestUri);
		} catch (ClientServicesException e) {
			logger.log(Level.SEVERE, Messages.FileServiceException_1 + "executeDelete()", e);
			setStatus();
			throw new FileServiceException(e);
		}
		parseResult(result);
	}

	/**
	 * executePut
	 * 
	 * @param requestUri
	 *            - api to be executed.
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @param headers
	 *            - Map of Headers. See {@link Headers} for possible values.
	 * @param payload
	 *            - Document which is passed directly as requestBody to the execute request. This method is
	 *            used to update the metadata/content of File in Connections.
	 * @return Document
	 * @throws FileServiceException
	 */

	public Document executePut(String requestUri, Map<String, String> parameters,
			Map<String, String> headers, Object payload) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "executePut");
		}
		Object result = null;
		try {
			result = endpoint.getClientService().put(requestUri, parameters, headers, payload,
					ClientService.FORMAT_XML);
		} catch (ClientServicesException e) {
			logger.log(Level.SEVERE, Messages.FileServiceException_1 + "executePut()", e);
			setStatus();
			throw new FileServiceException(e);
		}
		parseResult(result);
		return (Document) result;
	}

	/**
	 * executePost
	 * 
	 * @param requestUri
	 *            - api to be executed.
	 * @param params
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @param headers
	 *            - Map of Headers. See {@link Headers} for possible values.
	 * @param payload
	 *            - Document which is passed directly as requestBody to the execute request. This method is
	 *            used to update the metadata/content of File in Connections.
	 * @return Document
	 * @throws FileServiceException
	 */

	public Document executePost(String requestUri, Map<String, String> parameters,
			Map<String, String> headers, Object payload) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "executePost");
		}
		Object result = null;
		try {
			result = endpoint.getClientService().post(requestUri, parameters, headers, payload,
					ClientService.FORMAT_XML);
		} catch (ClientServicesException exception) {
			logger.log(Level.SEVERE, Messages.FileServiceException_1 + "executePost()", exception);
			setStatus();
			throw new FileServiceException(exception, exception.getMessage());
		}
		parseResult(result);
		return (Document) result;
	}

	/**
	 * parseResult
	 * 
	 * @param result
	 * @return Object
	 */
	private Object parseResult(Object result) {
		if (result == null) {
			return null;
		}
		setStatus("Success");
		return result;
	}

	/**
	 * setStatus
	 */
	private void setStatus() {
		setStatus("Failure");
	}

	/**
	 * @param status
	 */
	private void setStatus(String status) {
		FileStatus = status;
	}

	/**
	 * getFile
	 * 
	 * @param fileId
	 *            - ID of the file to be fetched from the Connections Server
	 * @param load
	 *            - a flag to determine whether the network call should be made or an empty placeholder of the
	 *            fileEntry object should be returned. load - true : network call is made to fetch the file
	 *            load - false : an empty fileEntry object is returned, and then updations can be made on this
	 *            object.
	 * @return FileEntry
	 * @throws FileServiceException
	 */
	public FileEntry getFile(String fileId, boolean load) throws FileServiceException {
		return getFile(fileId, null, load);
	}

	/**
	 * getFile
	 * <p>
	 * Rest API for getting files :- /files/basic/api/myuserlibrary/document/{document-id}/entry
	 * 
	 * @param fileId
	 *            - ID of the file to be fetched from the Connections Server
	 * @param parameters
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @param load
	 *            - a flag to determine whether the network call should be made or an empty placeholder of the
	 *            fileEntry object should be returned. load - true : network call is made to fetch the file
	 *            load - false : an empty fileEntry object is returned, and then updations can be made on this
	 *            object.
	 * @return FileEntry
	 * @throws FileServiceException
	 */

	public FileEntry getFile(String fileId, Map<String, String> parameters, boolean load)
			throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getFile");
		}
		if (StringUtil.isEmpty(fileId)) {
			throw new IllegalArgumentException(Messages.InvalidArgument_2);
		}
		String requestUri = null;
		FileEntry file = new FileEntry(fileId);
		if (load) {
			SubFilters subFilters = new SubFilters();
			subFilters.setDocumentId(fileId);
			requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), AccessType.AUTHENTICATED.getAccessType(),
					Categories.MYLIBRARY.getCategory(), null, null, subFilters,
					ResultType.ENTRY.getResultType());

			List<Object> resultantObjects = executeGet(requestUri, parameters, ClientService.FORMAT_XML,
					FileEntry.class);
			List<FileEntry> resultantEntries = new ArrayList<FileEntry>();
			for (Object eachObject : resultantObjects) {
				resultantEntries.add((FileEntry) eachObject);
			}

			if (resultantEntries != null && !resultantEntries.isEmpty()) {
				file = resultantEntries.get(0);
			}
		}
		return file;
	}

	/**
	 * executeGet
	 * 
	 * @param requestUri
	 *            - api to be executed.
	 * @param parameters
	 *            - Map of Parameters. See {@link FileRequestParams} for possible values.
	 * @param format
	 * @return List<FileEntry>
	 * @throws FileServiceException
	 */

	public List<Object> executeGet(String requestUri, Map<String, String> parameters, Handler format,
			Class objectType) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "executeGet");
		}
		Object result = null;
		try {
			result = getClientService().get(requestUri, parameters, format);
		} catch (ClientServicesException e) {
			logger.log(Level.SEVERE, Messages.FileServiceException_1 + "executeGet()", e);
			setStatus();
			throw new FileServiceException(e);
		}
		if (result == null) {
			return null;
		}
		return parseAndProcessResultFeed((Document) result, objectType);
	}

	/**
	 * createFileEntriesFromResultFeed
	 * <p>
	 * Called from all getters to parse the result and return FileEntery objects.
	 * 
	 * @param result
	 *            - response from the network call.
	 * @return List<FileEntry>
	 * @throws FileServiceException
	 */
	private List<Object> parseAndProcessResultFeed(Document result, Class objectType)
			throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "createFileEntriesFromResultFeed");
		}
		List<Object> fileObjects = new ArrayList<Object>();
		try {
			if (null != result) {
				NodeList fileEntries = result.getElementsByTagName("entry");
				if (fileEntries != null && fileEntries.getLength() > 0) {
					for (int i = 0; i < fileEntries.getLength(); i++) {
						Node entry = fileEntries.item(i);
						Document doc = DOMUtil.createDocument();
						Node dup = doc.importNode(entry, true);
						doc.appendChild(dup);

						Object resultingEntry = FileEntry.createResultFileWithData(doc, objectType);
						fileObjects.add(resultingEntry);
					}
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, Messages.FileServiceException_1 + "createFileEntriesFromResultFeed()", e);
			throw new FileServiceException(e,
					"Exception occurred while parsing response feed to create FileEntries");
		}
		return fileObjects;
	}

	/**
	 * updateFileEntryWithResponseFeed
	 * <p>
	 * Called by API for which updations need to be made in the already existing fileEntry objects.
	 * 
	 * @param FileEntry
	 *            - pass the fileEntry object to be updated
	 * @param result
	 *            - response from the network call.
	 * @return FileEntry - returns the updated fileEntry object
	 */

	private FileEntry updateFileEntryWithResponseFeed(FileEntry fileEntry, Document result) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "updateFileEntryWithResponseFeed");
		}
		fileEntry.setData(result);
		return fileEntry;
	}

}
