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
import com.ibm.sbt.services.client.SBTServiceException;
import com.ibm.sbt.services.client.connections.files.model.FileEntry;
import com.ibm.sbt.services.client.connections.files.model.FileRequestParams;
import com.ibm.sbt.services.client.connections.files.model.Headers;
import com.ibm.sbt.services.client.connections.files.utils.Messages;
import com.ibm.sbt.services.client.connections.files.utils.NamespacesConnections;
import com.ibm.sbt.services.client.smartcloud.base.BaseEntity;
import com.ibm.sbt.services.util.AuthUtil;

/**
 * @Represents Connections FileService
 * @author Vimal Dhupar
 */
public class FileService extends BaseService {

	static final String	sourceClass	= FileService.class.getName();
	static final Logger	logger		= Logger.getLogger(sourceClass);

	private static char	SEPARATOR	= '/';
	public String		FileStatus	= "Success";

	/**
	 * Default Constructor - 0 argument constructor Calls the Constructor of BaseService Class.
	 */
	public FileService() {
		this(DEFAULT_ENDPOINT_NAME, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor
	 * 
	 * @param endpoint
	 *            Creates fileservice with specified endpoint and a default CacheSize
	 */
	public FileService(String endpoint) {
		this(endpoint, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor
	 * 
	 * @param endpoint
	 * @param cacheSize
	 *            Creates FileService Object with specified values of endpoint and CacheSize
	 */
	public FileService(String endpoint, int cacheSize) {
		super(endpoint, cacheSize);
	}

	/**
	 * update
	 * 
	 * @param FileEntry
	 *            - pass the file to be updated
	 * @param params
	 *            - Map of Parameters
	 * @param payloadMap
	 *            - Map of entries for which we will construct a Request Body
	 * @return FileEntry This method is used to update the metadata/content of File in Connections. Rest API
	 *         used : /files/basic/api/myuserlibrary/document/{document-id}/entry User should get the specific
	 *         file before calling this API, by using getFile
	 */
	public FileEntry update(FileEntry fileEntry, Map<String, String> params, Map<String, String> payloadMap) {
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
		Object updateFilePayload = constructPayload(fileEntry, payloadMap);
		Document result = executePut(requestUri, params, headers, updateFilePayload);
		return updateFileEntryWithResponseFeed(fileEntry, result);
	}

	/**
	 * update
	 * 
	 * @param fileEntry
	 *            - pass the file to be updated
	 * @param params
	 *            - Map of Parameters
	 * @param requestBody
	 *            - Document which is passed directly as requestBody to the execute request This method is
	 *            used to update the metadata/content of File in Connections. Rest API used :
	 *            /files/basic/api/myuserlibrary/document/{document-id}/entry User should get the specific
	 *            file before calling this API, by using getFile
	 * @return FileEntry
	 */
	public FileEntry update(FileEntry fileEntry, Map<String, String> params, Document requestBody) {
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
	 * 
	 * @param filePath
	 *            Rest API Used : /files/basic/api/myuserlibrary/feed User should give the path of the file to
	 *            be uploaded.
	 * @return FileEntry
	 * @throws ClientServicesException
	 */
	public FileEntry upload(String filePath) {
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
		return createFileEntriesFromResultFeed(result).get(0);
	}

	/**
	 * uploadFileWithMetadata
	 * 
	 * @param filePath
	 * @param params
	 * @param headers
	 * @param requestBody
	 * @return FileEntry
	 * @description Rest API Used : /files/basic/api/myuserlibrary/feed
	 * @description A file consists of both the information about the file, which is also known as metadata,
	 *              and the binary data that the file contains. You can provide either of the following
	 *              inputs: - Binary data and no Atom entry document to define the metadata. Metadata is
	 *              created automatically and sets all values to the default values, except for the value of
	 *              the title element, which it takes from the SLUG header. - Atom entry document that defines
	 *              the metadata of the file and no binary data.
	 */
	public FileEntry uploadFileWithMetadata(String filePath, Map<String, String> params,
			Map<String, String> headers, Document requestBody) throws ClientServicesException {
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
				logger.log(Level.SEVERE, Messages.UploadINFO_2);
				return new FileEntry();
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
		return createFileEntriesFromResultFeed(result).get(0);
	}

	/**
	 * lock
	 * 
	 * @param fileId
	 *            We use update API to lock the file by modifying the lock tag in the entry Rest API used :
	 *            /files/basic/api/document/{document-id}/lock User should pass the fileId of the file to be
	 *            locked
	 * @return FileEntry
	 * @throws ClientServicesException
	 */
	public FileEntry lock(String fileId) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "lock");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		SubFilters subFilters = new SubFilters();
		if (StringUtil.isEmpty(fileId)) {
			logger.log(Level.SEVERE, Messages.InvalidArgument_2);
			return null;
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
		return createFileEntriesFromResultFeed(result).get(0);
	}

	/**
	 * unlock
	 * 
	 * @param fileId
	 *            We use update API to lock the file by modifying the lock tag in the entry Rest API used :
	 *            /files/basic/api/document/{document-id}/lock User should pass the fileId of the file to be
	 *            unlocked
	 * @return FileEntry
	 * @throws ClientServicesException
	 */
	public FileEntry unlock(String fileId) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "unlock");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		SubFilters subFilters = new SubFilters();
		if (StringUtil.isEmpty(fileId)) {
			logger.log(Level.SEVERE, Messages.InvalidArgument_2);
			return null;
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
		return createFileEntriesFromResultFeed(result).get(0);
	}

	/**
	 * delete a file
	 * 
	 * @param fileId
	 *            - id of the file to be deleted Rest API used :
	 *            /files/basic/api/myuserlibrary/document/{document-id}/entry
	 */
	public void delete(String fileId) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "delete");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.MYLIBRARY.getCategory();
		SubFilters subFilters = new SubFilters();
		if (StringUtil.isEmpty(fileId)) {
			logger.log(Level.SEVERE, Messages.InvalidArgument_2);
			return;
		}
		subFilters.setDocumentId(fileId);
		String resultType = ResultType.ENTRY.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, null, null,
				subFilters, resultType);
		executeDelete(requestUri);
	}

	/**
	 * addCommentToFile
	 * 
	 * @param fileEntry
	 * @param params
	 * @param comment
	 * @return FileEntry Rest API used : /files/basic/api/userlibrary/{userid}/document/{document-id}/feed
	 */
	public FileEntry addCommentToFile(FileEntry fileEntry, Map<String, String> params, String comment) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "addCommentToFile");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		SubFilters subFilters = new SubFilters();
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
		return createFileEntriesFromResultFeed(result).get(0);
	}

	/**
	 * addCommentToFile
	 * 
	 * @param fileEntry
	 * @param params
	 * @param comment
	 * @return FileEntry Rest API used : /files/basic/api/myuserlibrary/document/{document-id}/feed
	 */
	public FileEntry addCommentToMyFile(FileEntry fileEntry, Map<String, String> params, String comment) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "addCommentToMyFile");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.MYLIBRARY.getCategory();
		SubFilters subFilters = new SubFilters();
		subFilters.setDocumentId(fileEntry.getFileId());
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, null, null,
				subFilters, resultType);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(Headers.ContentType, Headers.ATOM);
		Object payload = constructPayloadForComments(comment);
		Document result = executePost(requestUri, params, headers, payload); // passing the comment here, as
																				// we are using the Plain Text
																				// method of adding comments.
		if (result == null) {
			return (FileEntry) result;
		}
		return createFileEntriesFromResultFeed(result).get(0);
	}

	/**
	 * addFilesToFolder
	 * 
	 * @param collectionId
	 * @param file
	 * @param params
	 * @return List<FileEntry> Rest API used : /files/basic/api/collection/{collection-id}/feed Parameters :
	 *         uuid of the file to be added. For adding more than one files, enter coma separated list of file
	 *         ids.
	 */
	@SuppressWarnings("unchecked")
	private List<FileEntry> addFilesToFolder(String collectionId, FileEntry fileEntry,
			Map<String, String> params) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "addFilesToFolder");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		SubFilters subFilters = new SubFilters();
		subFilters.setCollectionId(collectionId);
		String resultType = ResultType.FEED.getResultType();
		if (null == params) {
			params = new HashMap<String, String>();
		}
		params.put(FileRequestParams.ITEMID, fileEntry.getFileId());
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, null, null,
				subFilters, resultType);
		Document result = executePost(requestUri, params, null, null);
		if (result == null) {
			return (List<FileEntry>) result;
		}
		return createFileEntriesFromResultFeed(result);
	}

	/**
	 * retrieveFileComment
	 * 
	 * @param fileEntry
	 * @param commentId
	 * @param params
	 * @return List<FileEntry> Rest API used :
	 *         /files/basic/api/userlibrary/{userid}/document/{document-id}/comment/{comment-id}/entry
	 */
	private List<FileEntry> retrieveFileComment(FileEntry fileEntry, String commentId,
			Map<String, String> params) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "retrieveFileComment");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		SubFilters subFilters = new SubFilters();
		subFilters.setUserId(fileEntry.getAuthorEntry().getUserUuid());
		subFilters.setDocumentId(fileEntry.getFileId());
		subFilters.setCommentId(commentId);
		String resultType = ResultType.ENTRY.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, null, null,
				subFilters, resultType);
		return executeGet(requestUri, params, ClientService.FORMAT_XML);
	}

	/**
	 * retrieveFileComment
	 * 
	 * @param fileEntry
	 * @param commentId
	 * @param params
	 * @return List<FileEntry> Rest API used :
	 *         /files/basic/api/myuserlibrary/document/{document-id}/comment/{comment-id}/entry
	 */
	private List<FileEntry> retrieveMyFileComment(FileEntry fileEntry, String commentId,
			Map<String, String> params) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "retrieveMyFileComment");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.MYLIBRARY.getCategory();
		SubFilters subFilters = new SubFilters();
		subFilters.setDocumentId(fileEntry.getFileId());
		subFilters.setCommentId(commentId);
		String resultType = ResultType.ENTRY.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, null, null,
				subFilters, resultType);
		return executeGet(requestUri, params, ClientService.FORMAT_XML);
	}

	/**
	 * deleteComment
	 * 
	 * @param fileId
	 * @param commentId
	 *            Rest API used :
	 *            /files/basic/api/userlibrary/{userid}/document/{document-id}/comment/{comment-id}/entry
	 */
	private void deleteComment(FileEntry fileEntry, String commentId) // TODO test this..
	{
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "deleteComment");
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
	 * 
	 * @param fileId
	 * @param commentId
	 *            Rest API used :
	 *            /files/basic/api/myuserlibrary/document/{document-id}/comment/{comment-id}/entry
	 */
	private void deleteMyComment(String fileId, String commentId) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "deleteMyComment");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.MYLIBRARY.getCategory();
		SubFilters subFilters = new SubFilters();
		subFilters.setDocumentId(fileId);
		subFilters.setCommentId(commentId);
		String resultType = ResultType.ENTRY.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, null, null,
				subFilters, resultType);
		executeDelete(requestUri);
	}

	/**
	 * updateComment
	 * 
	 * @param fileId
	 * @param commentId
	 * @param params
	 * @param comment
	 *            Rest API used :
	 *            /files/basic/api/userlibrary/{userid}/document/{document-id}/comment/{comment-id}/entry
	 *            Updates comment from someone else's file whose userid is specified
	 * @return FileEntry
	 */
	private FileEntry updateComment(FileEntry fileEntry, String commentId, Map<String, String> params,
			String comment) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "updateComment");
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
			return (FileEntry) result;
		}
		return createFileEntriesFromResultFeed(result).get(0);
	}

	/**
	 * updateComment
	 * 
	 * @param fileId
	 * @param commentId
	 * @param params
	 * @param comment
	 *            Rest API used :
	 *            /files/basic/api/myuserlibrary/document/{document-id}/comment/{comment-id}/entry Updates
	 *            comment in one of "my" library file
	 * @return FileEntry
	 */
	private FileEntry updateMyComment(String fileId, String commentId, Map<String, String> params,
			String comment) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "updateMyComment");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.MYLIBRARY.getCategory();
		SubFilters subFilters = new SubFilters();
		subFilters.setDocumentId(fileId);
		subFilters.setCommentId(commentId);
		String resultType = ResultType.ENTRY.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, null, null,
				subFilters, resultType);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(Headers.ContentType, Headers.ATOM);
		Object payload = constructPayloadForComments(comment);
		Document result = executePut(requestUri, params, headers, payload);
		if (result == null) {
			return (FileEntry) result;
		}
		return createFileEntriesFromResultFeed(result).get(0);
	}

	/**
	 * getNonce
	 * 
	 * @return Rest API used : /files/basic/api/nonce
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
	 * 
	 * @return calls getMyFiles(Map<String, String> params) internally with null parameters, if user has not
	 *         specific any params
	 */
	public List<FileEntry> getMyFiles() {
		return getMyFiles(null);
	}

	/**
	 * getMyFiles
	 * 
	 * @return List<FileEntry>
	 * @param params
	 *            Rest API used : /files/basic/api/myuserlibrary/feed This method is used to get Files of the
	 *            person. this Single argument method, calls the get method with 3 arguments, passing true to
	 *            load the profile of the person and the API to make the call.
	 */
	public List<FileEntry> getMyFiles(Map<String, String> params) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMyFiles");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.MYLIBRARY.getCategory();
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, null, null, null,
				resultType);
		return executeGet(requestUri, params, ClientService.FORMAT_XML);
	}

	/**
	 * getFilesSharedWithMe
	 * 
	 * @return List<FileEntry> getFilesSharedWithMe(Map<String, String> params) with null params
	 */
	public List<FileEntry> getFilesSharedWithMe() {
		return getFilesSharedWithMe(null);
	}

	/**
	 * getFilesSharedWithMe
	 * 
	 * @param params
	 * @return List<FileEntry> Rest API used : /files/basic/api/documents/shared/feed This method is used to
	 *         get Files Shared With the person. this Single argument method, calls the get method with 3
	 *         arguments, passing true to load the profile of the person and the API to make the call.
	 */
	public List<FileEntry> getFilesSharedWithMe(Map<String, String> params) {
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
		return executeGet(requestUri, params, ClientService.FORMAT_XML);
	}

	/**
	 * getFilesSharedByMe
	 * 
	 * @return List<FileEntry> This method calls getFilesSharedByMe(Map<String, String> params) with null
	 *         params
	 */
	public List<FileEntry> getFilesSharedByMe() {
		return getFilesSharedByMe(null);
	}

	/**
	 * getFilesSharedByMe
	 * 
	 * @param params
	 * @return List<FileEntry> Rest API used : /files/basic/api/documents/shared/feed This method is used to
	 *         get Files Shared By the person. this Single argument method, calls the get method with 3
	 *         arguments, passing true to load the profile of the person and the API to make the call.
	 */
	public List<FileEntry> getFilesSharedByMe(Map<String, String> params) {
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
		return executeGet(requestUri, params, ClientService.FORMAT_XML);
	}

	/**
	 * getPublicFiles
	 * 
	 * @param params
	 * @return List<FileEntry> Rest API used : /files/basic/anonymous/api/documents/feed This method returns a
	 *         list of Public Files.
	 */
	public List<FileEntry> getPublicFiles(Map<String, String> params) {
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
		return executeGet(requestUri, params, ClientService.FORMAT_XML);
	}

	/**
	 * getPinnedFiles
	 * 
	 * @param params
	 * @return List<FileEntry> Rest API used : /files/basic/api/myfavorites/documents/feed
	 */
	public List<FileEntry> getPinnedFiles(Map<String, String> params) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getPinnedFiles");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.PINNED.getCategory();
		String view = Views.FILES.getViews();
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, view, null, null,
				resultType);
		return executeGet(requestUri, params, ClientService.FORMAT_XML);
	}

	/**
	 * getMyFolders
	 * 
	 * @param params
	 * @return List<FileEntry> Rest API used : /files/basic/api/collections/feed Required Parameters :
	 *         creator={snx:userid}
	 */
	public List<FileEntry> getMyFolders(Map<String, String> params) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMyFolders");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String view = Views.FOLDERS.getViews();
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, view, null, null,
				resultType);
		return executeGet(requestUri, params, ClientService.FORMAT_XML);
	}

	/**
	 * getMyPinnedFolders
	 * 
	 * @param params
	 * @return List<FileEntry> Rest API used : /files/basic/api/myfavorites/collections/feed
	 */
	public List<FileEntry> getMyPinnedFolders(Map<String, String> params) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMyPinnedFolders");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.PINNED.getCategory();
		String view = Views.FOLDERS.getViews();
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, view, null, null,
				resultType);
		return executeGet(requestUri, params, ClientService.FORMAT_XML);
	}

	/**
	 * getFoldersWithRecentlyAddedFiles
	 * 
	 * @param params
	 * @return List<FileEntry> Rest API used : /files/basic/api/collections/addedto/feed
	 */
	public List<FileEntry> getFoldersWithRecentlyAddedFiles(Map<String, String> params) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getFoldersWithRecentlyAddedFiles");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String view = Views.FOLDERS.getViews();
		String filter = Filters.ADDEDTO.getFilters();
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, view, filter, null,
				resultType);
		return executeGet(requestUri, params, ClientService.FORMAT_XML);
	}

	/**
	 * getFilesInFolder
	 * 
	 * @param collection
	 *            id
	 * @param params
	 * @return List<FileEntry> Rest API used : /files/basic/api/collection/{collection-id}/feed
	 */
	public List<FileEntry> getFilesInFolder(String collectionId, Map<String, String> params) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getFilesInFolder");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		SubFilters subFilters = new SubFilters();
		if (StringUtil.isEmpty(collectionId)) {
			logger.log(Level.SEVERE, Messages.InvalidArgument_4);
			return null;
		}
		subFilters.setCollectionId(collectionId);
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, null, null,
				subFilters, resultType);
		return executeGet(requestUri, params, ClientService.FORMAT_XML);
	}

	/**
	 * getPublicFileFolders
	 * 
	 * @param params
	 * @return List<FileEntry> Rest API used : /files/basic/anonymous/api/collections/feed Public method. No
	 *         Auth required.
	 */
	public List<FileEntry> getPublicFileFolders(Map<String, String> params) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getPublicFileFolders");
		}
		String accessType = AccessType.PUBLIC.getAccessType();
		String view = Views.FOLDERS.getViews();
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, view, null, null,
				resultType);
		return executeGet(requestUri, params, ClientService.FORMAT_XML);
	}

	/**
	 * getPersonLibrary
	 * 
	 * @param userId
	 * @param params
	 * @return List<FileEntry> Rest API used : /files/basic/anonymous/api/userlibrary/{userid}/feed Public
	 *         method. No Auth required.
	 */
	public List<FileEntry> getPersonLibrary(String userId, Map<String, String> params) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getPersonLibrary");
		}
		String accessType = AccessType.PUBLIC.getAccessType();
		SubFilters subFilters = new SubFilters();
		if (StringUtil.isEmpty(userId)) {
			logger.log(Level.SEVERE, Messages.InvalidArgument_3);
			return null;
		}
		subFilters.setUserId(userId);
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, null, null,
				subFilters, resultType);
		return executeGet(requestUri, params, ClientService.FORMAT_XML);
	}

	/**
	 * getPublicFilesComments
	 * 
	 * @param fileEntry
	 * @param params
	 * @return List<FileEntry> Rest API used :
	 *         /files/basic/anonymous/api/userlibrary/{userid}/document/{document-id}/feed Public method. No
	 *         Auth required.
	 */
	public List<FileEntry> getPublicFilesComments(FileEntry fileEntry, Map<String, String> params) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getPublicFilesComments");
		}
		String accessType = AccessType.PUBLIC.getAccessType();
		SubFilters subFilters = new SubFilters();
		if (StringUtil.isEmpty(fileEntry.getAuthorEntry().getUserUuid())) {
			logger.log(Level.SEVERE, Messages.InvalidArgument_3);
			return null;
		}
		subFilters.setUserId(fileEntry.getAuthorEntry().getUserUuid());
		if (StringUtil.isEmpty(fileEntry.getFileId())) {
			logger.log(Level.SEVERE, Messages.InvalidArgument_2);
			return null;
		}
		subFilters.setDocumentId(fileEntry.getFileId());
		String resultType = ResultType.FEED.getResultType();
		if (params == null) {
			params = new HashMap<String, String>();
		}
		params.put(FileRequestParams.CATEGORY, "comment");
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, null, null,
				subFilters, resultType);
		return executeGet(requestUri, params, ClientService.FORMAT_XML);
	}

	/**
	 * getFilesComments
	 * 
	 * @param fileEntry
	 * @param params
	 * @return List<FileEntry> Rest API used :
	 *         /files/basic/api/userlibrary/{userid}/document/{document-id}/feed
	 */
	public List<FileEntry> getFilesComments(FileEntry fileEntry, Map<String, String> params) // TODO test
																								// this..
	{
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getFilesComments");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		SubFilters subFilters = new SubFilters();
		if (fileEntry == null) {
			logger.log(Level.SEVERE, Messages.InvalidArgument_1);
			return null;
		}
		if (StringUtil.isEmpty(fileEntry.getAuthorEntry().getUserUuid())) {
			logger.log(Level.SEVERE, Messages.InvalidArgument_3);
			return null;
		}
		subFilters.setUserId(fileEntry.getAuthorEntry().getUserUuid());
		if (StringUtil.isEmpty(fileEntry.getFileId())) {
			logger.log(Level.SEVERE, Messages.InvalidArgument_2);
			return null;
		}
		subFilters.setDocumentId(fileEntry.getFileId());
		String resultType = ResultType.FEED.getResultType();
		if (params == null) {
			params = new HashMap<String, String>();
		}
		params.put(FileRequestParams.CATEGORY, "comment");
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, null, null, null,
				subFilters, resultType);
		return executeGet(requestUri, params, ClientService.FORMAT_XML);
	}

	/**
	 * getMyFilesComments
	 * 
	 * @param fileId
	 * @param params
	 * @return List<FileEntry> Rest API used : /files/basic/api/myuserlibrary/document/{document-id}/feed
	 */
	public List<FileEntry> getMyFilesComments(FileEntry fileEntry, Map<String, String> params) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMyFilesComments");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.MYLIBRARY.getCategory();
		SubFilters subFilters = new SubFilters();
		if (fileEntry == null) {
			logger.log(Level.SEVERE, Messages.InvalidArgument_1);
			return null;
		}
		if (StringUtil.isEmpty(fileEntry.getFileId())) {
			logger.log(Level.SEVERE, Messages.InvalidArgument_2);
			return null;
		}
		subFilters.setDocumentId(fileEntry.getFileId());
		String resultType = ResultType.FEED.getResultType();
		if (params == null) {
			params = new HashMap<String, String>();
		}
		params.put(FileRequestParams.CATEGORY, "comment");
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, null, null,
				subFilters, resultType);
		return executeGet(requestUri, params, ClientService.FORMAT_XML);
	}

	/**
	 * getFilesInMyRecycleBin
	 * 
	 * @param params
	 * @return List<FileEntry> Rest API used : /files/basic/api/myuserlibrary/view/recyclebin/feed
	 */
	public List<FileEntry> getFilesInMyRecycleBin(Map<String, String> params) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getFilesInMyRecycleBin");
		}
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		String category = Categories.MYLIBRARY.getCategory();
		String view = Views.RECYCLEBIN.getViews();
		String resultType = ResultType.FEED.getResultType();
		String requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), accessType, category, view, null, null,
				resultType);
		return executeGet(requestUri, params, ClientService.FORMAT_XML);
	}

	/**
	 * constructUrl
	 * 
	 * @param parameters
	 *            This method should be used if we are setting parameters in our Sample. If we wish to use the
	 *            default Parameters, then use constructUrl method with no arguments.
	 */
	@SuppressWarnings("static-access")
	public String constructUrl(String baseUrl, String accessType, String category, String view,
			String filter, SubFilters subFilters, String resultType) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "constructUrl");
		}
		// here we will set the value in API after constructing the url
		// the General Pattern of the URL is baseUrl + authType(basic or oauth) + AccessType + Category + View
		// + Filter + {SubFilterKey + SubFilters}* + resultType
		// if the user has set these values then ok. otherwise, we set the default to GetMyFiles :
		// /files/basic/api/myuserlibrary/feed
		StringBuilder url = new StringBuilder(baseUrl);
		url.append(SEPARATOR).append(AuthUtil.INSTANCE.getAuthValue(endpoint));
		// Parameters parameters = new Parameters();
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
	 * @return
	 */
	public Document constructPayloadForComments(String comment) {
		return constructPayloadForComments(null, comment);
	}

	/**
	 * constructPayloadForComments
	 * 
	 * @param id
	 * @return
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
	 * 
	 * @return This method constructs the Atom entry document for the APIs requiring input.
	 */
	public Document constructPayload(FileEntry fileEntry, Map<String, String> payloadMap) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "constructPayload");
		}
		if (payloadMap == null || payloadMap.isEmpty()) {
			logger.log(Level.SEVERE, Messages.PayloadInfo_1);
			return null;
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

	public Document convertToXML(String requestBody) {
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
	 * @param api
	 */
	public void executeDelete(String requestUri) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "executeDelete");
		}
		Object result = null;
		try {
			result = endpoint.getClientService().delete(requestUri);
		} catch (ClientServicesException e) {
			logger.log(Level.SEVERE, Messages.FileServiceException_1 + "executeDelete()", e);
			setStatus();
		}
		parseResult(result);
	}

	/**
	 * executePut
	 * 
	 * @param payload
	 * @return
	 */
	public Document executePut(String requestUri, Map<String, String> parameters,
			Map<String, String> headers, Object payload) {
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
		}
		parseResult(result);
		return (Document) result;
	}

	/**
	 * executePost
	 * 
	 * @param payload
	 * @return
	 */
	public Document executePost(String requestUri, Map<String, String> parameters,
			Map<String, String> headers, Object payload) {
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
		}
		parseResult(result);
		return (Document) result;
	}

	/**
	 * parseResult
	 * 
	 * @param result
	 * @return
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

	private void setStatus(String status) {
		FileStatus = status;
	}

	/**
	 * getFile
	 * 
	 * @param fileId
	 * @param load
	 * @return
	 */
	public FileEntry getFile(String fileId, boolean load) {
		return getFile(fileId, null, load);
	}

	/**
	 * getFile
	 * 
	 * @param fileId
	 * @param load
	 * @return Rest API for getting files :- /files/basic/api/myuserlibrary/document/{document-id}/entry
	 */
	public FileEntry getFile(String fileId, Map<String, String> parameters, boolean load) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getFile");
		}
		if (StringUtil.isEmpty(fileId)) {
			logger.log(Level.SEVERE, Messages.InvalidArgument_2);
			return null;
		}
		String requestUri = null;
		FileEntry file = new FileEntry(fileId);
		if (load) {
			SubFilters subFilters = new SubFilters();
			subFilters.setDocumentId(fileId);
			requestUri = constructUrl(BaseUrl.FILES.getBaseUrl(), AccessType.AUTHENTICATED.getAccessType(),
					Categories.MYLIBRARY.getCategory(), null, null, subFilters,
					ResultType.ENTRY.getResultType());
			List<FileEntry> files = executeGet(requestUri, parameters, ClientService.FORMAT_XML);
			if (files != null && !files.isEmpty()) {
				file = files.get(0);
			}
		}
		return file;
	}

	/**
	 * executeGet
	 * 
	 * @param userId
	 * @return
	 */
	public List<FileEntry> executeGet(String requestUri, Map<String, String> parameters, Handler format) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "executeGet");
		}
		Object result = null;
		try {
			result = getClientService().get(requestUri, parameters, format);
		} catch (ClientServicesException e) {
			logger.log(Level.SEVERE, Messages.FileServiceException_1 + "executeGet()", e);
			setStatus();
		}
		if (result == null) {
			return null;
		}
		return createFileEntriesFromResultFeed((Document) result);
	}

	/**
	 * createFileEntriesFromResultFeed
	 * 
	 * @param result
	 * @return Called from all getters to parse the result and return FileEntery objects.
	 */
	public List<FileEntry> createFileEntriesFromResultFeed(Document result) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "createFileEntriesFromResultFeed");
		}
		List<FileEntry> fileObjects = new ArrayList<FileEntry>();
		try {
			if (null != result) {
				NodeList fileEntries = result.getElementsByTagName("entry");
				if (fileEntries != null && fileEntries.getLength() > 0) {
					for (int i = 0; i < fileEntries.getLength(); i++) {
						Node entry = fileEntries.item(i);
						Document doc = DOMUtil.createDocument();
						Node dup = doc.importNode(entry, true);
						doc.appendChild(dup);

						FileEntry file = FileEntry.createResultFileWithData(doc);
						fileObjects.add(file);
					}
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, Messages.FileServiceException_1 + "createFileEntriesFromResultFeed()", e);
		}
		return fileObjects;
	}

	/**
	 * updateFileEntryWithResponseFeed
	 * 
	 * @param result
	 * @return
	 */
	private FileEntry updateFileEntryWithResponseFeed(FileEntry fileEntry, Document result) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "updateFileEntryWithResponseFeed");
		}
		fileEntry.setData(result);
		return fileEntry;
	}

	@Override
	public <DataFormat> BaseEntity<DataFormat> getEntityFromData(String entityName, DataFormat data)
			throws SBTServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <DataFormat> BaseEntity<DataFormat> getEntityFromId(String entityName, String uuid)
			throws SBTServiceException {
		// TODO Auto-generated method stub
		return null;
	}
}
