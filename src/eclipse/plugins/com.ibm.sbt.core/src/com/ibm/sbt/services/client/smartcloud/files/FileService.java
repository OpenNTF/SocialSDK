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

package com.ibm.sbt.services.client.smartcloud.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Node;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.BaseService;
import com.ibm.sbt.services.client.ClientService.ContentStream;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.smartcloud.base.BaseEntity;
import com.ibm.sbt.services.client.smartcloud.files.FileServiceException.Reason;

/**
 * FileService can be used to perform File related operations.
 * <p>
 * Relies on the ID's provided by the user to perform the task.
 * 
 * @author Lorenzo Boccaccia
 * @Represents Smartcloud FileService
 * @see http 
 *      ://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=API+Reference#action=openDocument
 *      &res_title=Files_CMIS_API_sbar&content=pdcontent
 */

public class FileService extends BaseService {

	public static class FieldFilter extends HashSet<String> {

		private static final long	serialVersionUID	= 1L;

		public String getForURLQuery() {
			// adding mandatory fields as per documentation
			this.add("cmis:objectId");
			this.add("cmis:objectTypeId");
			StringBuilder concatenation = new StringBuilder();
			boolean first = true;
			for (String e : this) {
				if (!first) {
					concatenation.append(',');
				} else {
					first = false;
				}
				concatenation.append(e);
			}

			// NOTE: not encoded because it will be encoded after being passed to the base service
			return concatenation.toString();

		}
	}

	private static final String	sourceClass				= FileService.class.getName();
	private static final Logger	logger					= Logger.getLogger(sourceClass);
	private static String		DEFAULT_HANDLER_NAME	= "XML";
	private static int			DEFAULT_CACHE_SIZE		= 0;
	private static String		DEFAULT_ENDPOINT_NAME	= "smartcloud";

	private RepositoryInfo		reposInfo;

	protected static enum FilesAPI {
		GET_REPOSITORY_INFO("/files/basic/cmis/my/servicedoc"), GET_FILE_ENTRY(
				"/files/basic/cmis/repository/p!{subscriberId}/object/snx:file!{fileId}"), POST_FILE_UPLOAD(
				"/files/basic/cmis/repository/p!{subscriberId}/folderc/snx:files"), GET_MY_FILES(
				"/files/basic/cmis/repository/p!{subscriberId}/folderc/snx:files"), GET_MY_FILES_ALT(
				"/files/basic/cmis/repository/p!{subscriberId}/folderc/snx:virtual!.!filesownedby"), GET_MY_FILES_WITH_FILTER(
				"/files/basic/cmis/repository/p!{subscriberId}/folderc/snx:files"), GET_FILES_SHARED_WITH_ME(
				"/files/basic/cmis/repository/p!{subscriberId}/folderc/snx:virtual!.!filessharedwith"), GET_MY_COLLECTION(
				"/files/basic/cmis/repository/p!{subscriberId}/folderc/snx:collections"), GET_COLLECTIONS_SHARED_WITH_ME(
				"/files/basic/cmis/repository/p!{subscriberId}/folderc/snx:virtual!.!collectionssharedwith"), GET_FILES_INSIDE_FOLDER(
				"/files/basic/cmis/repository/p!{subscriberId}/folderc/snx:collection!{IdToBeReplaced}"), GET_FILE_COMMENTS(
				"/files/basic/cmis/repository/p!{subscriberId}/folderc/snx:comments!{IdToBeReplaced}");

		private String	url;

		private FilesAPI(String url) {
			this.url = url;
		}

		String getUrl(String repositoryId) {
			if (repositoryId != null) {
				return url.replace("p!{subscriberId}", repositoryId);
			} else {
				return url;
			}
		}

		String getUrl(String repositoryId, String idToBeReplaced) {
			if (repositoryId != null) {
				url = url.replace("p!{subscriberId}", repositoryId);
			}
			if (idToBeReplaced != null) {}
			url = url.replace("{IdToBeReplaced}", idToBeReplaced);

			return url;
		}

		public String getUrl() {
			return getUrl(null);
		}
	}

	/**
	 * Default Constructor - 0 argument constructor Calls the Constructor of BaseService Class.
	 */
	public FileService() {
		this(DEFAULT_ENDPOINT_NAME, DEFAULT_CACHE_SIZE, DEFAULT_HANDLER_NAME);
	}

	/**
	 * Constructor - 1 argument constructor
	 * 
	 * @param endpoint
	 *            Creates ProfileService with specified endpoint and a default CacheSize
	 */
	public FileService(String endpoint) {
		this(endpoint, DEFAULT_CACHE_SIZE, DEFAULT_HANDLER_NAME);
	}

	/**
	 * Constructor - 2 argument constructor
	 * 
	 * @param endpoint
	 * @param cacheSize
	 *            Creates ProfileService with specified values of endpoint and CacheSize
	 * @throws ClientServicesException
	 */
	public FileService(String endpoint, int cacheSize) throws ClientServicesException {
		super(endpoint, cacheSize, "XML");
	}

	public FileService(String endpoint, String format) {
		this(endpoint, DEFAULT_CACHE_SIZE, format);
	}

	public FileService(String endpoint, int cacheSize, String format) {
		super(endpoint, cacheSize, format);
	}

	/**
	 * getMyFiles
	 * <p>
	 * Executes a service call to return all the user files
	 * 
	 * @return all the files for the current SmartCloud account
	 * @throws FileServiceException
	 */
	public List<FileEntry> getMyFiles() throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMyFiles");
		}

		new HashMap<String, String>();

		@SuppressWarnings("unchecked")
		List<FileEntry> entries;

		try {
			entries = super.getMultipleEntities(
					FilesAPI.GET_MY_FILES.getUrl(getReposInfo().getRepositoryID()), null, FileEntry.class);
		} catch (ClientServicesException e) {
			Reason reason = Reason.SERVER_ERROR;
			if (e.isClientError()) {
				reason = reason.CLIENT_ERROR;
			}
			// TODO: agree on level of client exception. Any exception should be severe, but client exception
			// aren't really exceptional behaviour and sometime expected as part of the API
			if (logger.isLoggable(Level.WARNING)) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			throw new FileServiceException(e, StringUtil.format("Error reading files"), reason);
		}

		return entries;
	}

	/**
	 * getFilesSharedWithMe
	 * <p>
	 * Executes a service call to return all files shared with the user
	 * 
	 * @return all the files for the current SmartCloud account
	 * @throws FileServiceException
	 */
	public List<FileEntry> getFilesSharedWithMe() throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getFilesSharedWithMe");
		}

		new HashMap<String, String>();

		@SuppressWarnings("unchecked")
		List<FileEntry> entries;

		try {
			entries = super.getMultipleEntities(
					FilesAPI.GET_FILES_SHARED_WITH_ME.getUrl(getReposInfo().getRepositoryID()), null,
					FileEntry.class);
		} catch (ClientServicesException e) {
			Reason reason = Reason.SERVER_ERROR;
			if (e.isClientError()) {
				reason = reason.CLIENT_ERROR;
			}
			// TODO: agree on level of client exception. Any exception should be severe, but client exception
			// aren't really exceptional behaviour and sometime expected as part of the API
			if (logger.isLoggable(Level.WARNING)) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			throw new FileServiceException(e, StringUtil.format("Error reading files"), reason);
		}

		return entries;
	}

	/**
	 * getMyCollections
	 * <p>
	 * Executes a service call to return User's Collections
	 * 
	 * @return all the collections for the current SmartCloud account
	 * @throws FileServiceException
	 */
	public List<FileEntry> getMyCollections() throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMyCollections");
		}

		new HashMap<String, String>();

		@SuppressWarnings("unchecked")
		List<FileEntry> entries;

		try {
			entries = super.getMultipleEntities(
					FilesAPI.GET_MY_COLLECTION.getUrl(getReposInfo().getRepositoryID()), null,
					FileEntry.class);
		} catch (ClientServicesException e) {
			Reason reason = Reason.SERVER_ERROR;
			if (e.isClientError()) {
				reason = reason.CLIENT_ERROR;
			}
			// TODO: agree on level of client exception. Any exception should be severe, but client exception
			// aren't really exceptional behaviour and sometime expected as part of the API
			if (logger.isLoggable(Level.WARNING)) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			throw new FileServiceException(e, StringUtil.format("Error reading files"), reason);
		}

		return entries;
	}

	/**
	 * getCollectionsSharedWithMe
	 * <p>
	 * Executes a service call to return Collections shared with the current SmartCloud User
	 * 
	 * @return all the collections shared with the current SmartCloud account
	 * @throws FileServiceException
	 */
	public List<FileEntry> getCollectionsSharedWithMe() throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getCollectionsSharedWithMe");
		}

		new HashMap<String, String>();

		@SuppressWarnings("unchecked")
		List<FileEntry> entries;

		try {
			entries = super.getMultipleEntities(
					FilesAPI.GET_COLLECTIONS_SHARED_WITH_ME.getUrl(getReposInfo().getRepositoryID()), null,
					FileEntry.class);
		} catch (ClientServicesException e) {
			Reason reason = Reason.SERVER_ERROR;
			if (e.isClientError()) {
				reason = reason.CLIENT_ERROR;
			}
			// TODO: agree on level of client exception. Any exception should be severe, but client exception
			// aren't really exceptional behaviour and sometime expected as part of the API
			if (logger.isLoggable(Level.WARNING)) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			throw new FileServiceException(e, StringUtil.format("Error reading files"), reason);
		}

		return entries;
	}

	/**
	 * getFilesInsideFolder
	 * <p>
	 * Executes a service call to return Files inside the Folder for which the Id is given
	 * 
	 * @return all the files inside the given Folder
	 * @throws FileServiceException
	 */
	public List<FileEntry> getFilesInsideFolder(String folderId) throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getFilesInsideFolder");
		}

		new HashMap<String, String>();

		@SuppressWarnings("unchecked")
		List<FileEntry> entries;
		try {
			entries = super.getMultipleEntities(
					FilesAPI.GET_FILES_INSIDE_FOLDER.getUrl(getReposInfo().getRepositoryID(), folderId),
					null, FileEntry.class);
		} catch (ClientServicesException e) {
			Reason reason = Reason.SERVER_ERROR;
			if (e.isClientError()) {
				reason = reason.CLIENT_ERROR;
			}
			// TODO: agree on level of client exception. Any exception should be severe, but client exception
			// aren't really exceptional behaviour and sometime expected as part of the API
			if (logger.isLoggable(Level.WARNING)) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			throw new FileServiceException(e, StringUtil.format("Error reading files"), reason);
		}

		return entries;
	}

	/**
	 * getMyFilesAlt
	 * <p>
	 * Executes a service query to list all the user files
	 * 
	 * @return all the files for the current SmartCloud account
	 * @throws FileServiceException
	 */
	public List<FileEntry> getMyFilesAlt() throws FileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMyFiles");
		}

		new HashMap<String, String>();

		@SuppressWarnings("unchecked")
		List<FileEntry> entries;
		try {
			entries = super
					.getMultipleEntities(FilesAPI.GET_MY_FILES_ALT.getUrl(getReposInfo().getRepositoryID()),
							null, FileEntry.class);
		} catch (ClientServicesException e) {
			Reason reason = Reason.SERVER_ERROR;
			if (e.isClientError()) {
				reason = reason.CLIENT_ERROR;
			}
			// TODO: agree on level of client exception. Any exception should be severe, but client exception
			// aren't really exceptional behaviour and sometime expected as part of the API
			if (logger.isLoggable(Level.WARNING)) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			throw new FileServiceException(e, StringUtil.format("Error reading files"), reason);
		}
		return entries;
	}

	/**
	 * getMyFiles
	 * <p>
	 * This API allows to read a list of file entries limiting both the size of the returned list and the size
	 * of the returned entries
	 * 
	 * @param maxItems
	 *            The maximum entries to be returned by the query; leave null for no limits
	 * @param skipCount
	 *            The entries to be skipped (allows pagination when used togheter with maxItems); leave null
	 *            to not skip entries
	 * @param filter
	 *            The field that will be populated by the service (accessing other fields will reeturn null);
	 *            leave null for no filtering
	 * @return A list of entries optionally limited in size, in fields and with the first skipCount entries
	 *         optionally skipped
	 * @throws FileServiceException
	 */
	public List<FileEntry> getMyFiles(Integer maxItems, Integer skipCount, FieldFilter filter)
			throws FileServiceException {

		Map parameters = new HashMap();
		if (maxItems != null) {
			parameters.put("maxItems", maxItems.toString());
		}
		if (skipCount != null) {
			parameters.put("skipCount", skipCount.toString());
		}
		if (filter != null) {
			parameters.put("filter", filter.getForURLQuery());
		}

		List<FileEntry> entries;
		try {
			entries = super.getMultipleEntities(
					FilesAPI.GET_MY_FILES_WITH_FILTER.getUrl(getReposInfo().getRepositoryID()), parameters,
					FileEntry.class);
		} catch (ClientServicesException e) {
			Reason reason = Reason.SERVER_ERROR;
			if (e.isClientError()) {
				reason = Reason.CLIENT_ERROR;
			}
			// TODO: agree on level of client exception. Any exception should be severe, but client exception
			// aren't really exceptional behaviour and sometime expected as part of the API
			if (logger.isLoggable(Level.WARNING)) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			throw new FileServiceException(e, StringUtil.format(
					"Error reading files, using filters: {0}, limited at {1} entries starting from {2} ",
					filter, maxItems, skipCount), reason);
		}
		return entries;

	}

	/**
	 * uploadFile
	 * <p>
	 * Uploads a file to SmartCloud A file with the same name must not exists.
	 * 
	 * @param f
	 *            the file to upload
	 * @return the FileEntry created on SmartCloud for the file
	 * @throws FileServiceException
	 */

	public FileEntry uploadFile(File f) throws FileServiceException {
		return this.uploadFile(f, f.getName());
	}

	/**
	 * uploadFile
	 * <p>
	 * Uploads a file to SmartCloud A file with the same name must not exists. This version allow to upload
	 * the file under a different name
	 * 
	 * @param f
	 *            the file to upload
	 * @param name
	 *            The name to use for the newly uploaded file
	 * @return the FileEntry created on SmartCloud for the file
	 * @throws FileServiceException
	 */
	public FileEntry uploadFile(File file, final String name) throws FileServiceException {
		if (file == null) {
			throw new IllegalArgumentException(StringUtil.format("A null file was passed"));
		}
		if (name == null) {
			throw new IllegalArgumentException(StringUtil.format("A null file name was passed"));
		}
		try {
			return this.uploadFile(new FileInputStream(file), name, file.length());
		} catch (FileNotFoundException e) {
			if (logger.isLoggable(Level.SEVERE)) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
			// check null for safety as the previous check may get removed/moved to other classes
			throw new FileServiceException(e, StringUtil.format("File {0} not found", file == null ? "null"
					: file.getAbsolutePath()), Reason.INVALID_INPUT);
		}
	}

	/**
	 * uploadFile
	 * <p>
	 * Upload a binary stream to SmartCloud as a file. The length of the stream must be know
	 * 
	 * @param stream
	 *            The stream to read from when uploading the file. Buffering will be added if necessary.
	 * @param name
	 *            The name for the new file to create on SmartCloud
	 * @param length
	 *            The number of characters to be read from the stream
	 * @return the FileEntry created on SmartCloud for the file
	 * @throws FileServiceException
	 */
	public <DataFormat> FileEntry<DataFormat> uploadFile(java.io.InputStream stream, final String name,
			long length) throws FileServiceException {
		// TODO: pass data to the content handler
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "uploading File");
		}
		if (stream == null) {
			throw new IllegalArgumentException(StringUtil.format("A null stream was passed"));
		}
		if (name == null) {
			throw new IllegalArgumentException(StringUtil.format("A null name was passed"));
		}
		ContentStream contentFile = new ContentStream(stream, length, name);

		try {
			Node result = (Node) super.createData(
					FilesAPI.POST_FILE_UPLOAD.getUrl(getReposInfo().getRepositoryID()), null, contentFile);
			List<Node> atomEntries = FileEntry.getNodeExtractorFromData(result).getEntitiesFromServiceResult(
					result);
			// TODO: should we raise an error if length != 1?
			Node payload = atomEntries.get(0);
			return (FileEntry<DataFormat>) getEntityFromData(FileEntry.class.getSimpleName(), payload);

		} catch (ClientServicesException e) {
			// TODO: agree on level of client exception. Any exception should be severe, but client exception
			// aren't really exceptional behaviour and sometime expected as part of the API
			if (logger.isLoggable(Level.WARNING)) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			Reason reason = Reason.SERVER_ERROR;
			if (e.getResponseStatusCode() == ClientServicesException.CONFLICT) {
				reason = Reason.DUPLICATE_FILE;
				throw new FileServiceException(e, StringUtil.format(
						"A file with the name {0} already exists on the server for this user", name), reason);
			}
			if (e.getResponseStatusCode() == ClientServicesException.LENGTH_REQUIRED) {
				reason = Reason.INVALID_INPUT;
				throw new FileServiceException(
						e,
						StringUtil
								.format("Server doesn't support chunked transfer encoding; please pass a valid length; input length was {0} ",
										length), reason);
			}

			if (e.isClientError()) {
				reason = Reason.CLIENT_ERROR;
			}

			throw new FileServiceException(e, StringUtil.format("Error uploading the file"), reason);
		}
	}

	/**
	 * @return the repositoryInfo object containing the parameter required as entry point to every other
	 *         service call, the subscriber ID
	 * @throws FileServiceException
	 */
	private RepositoryInfo getReposInfo() throws FileServiceException {
		if (reposInfo != null) {
			return reposInfo;
		}
		synchronized (this) {
			if (reposInfo != null) {
				return reposInfo;
			}
			try {
				this.reposInfo = super.getSingleEntry(FilesAPI.GET_REPOSITORY_INFO.getUrl(), true,
						RepositoryInfo.class);
			} catch (ClientServicesException e) {
				// TODO: agree on level of client exception. Any exception should be severe, but client
				// exception aren't really exceptional behaviour and sometime expected as part of the API
				if (logger.isLoggable(Level.WARNING)) {
					logger.log(Level.WARNING, e.getMessage(), e);
				}
				Reason reason = Reason.SERVER_ERROR;
				if (e.isClientError()) {
					reason = Reason.CLIENT_ERROR;
				}

				throw new FileServiceException(e,
						StringUtil.format("Error reading the file service API index"), reason);
			}
		}
		return reposInfo;
	}

	/**
	 * required as protected from FileEntry loading
	 * 
	 * @return the URL configured with the correct repository ID
	 * @throws FileServiceException
	 */
	protected String getEntryLoadURL() throws FileServiceException {

		return FilesAPI.GET_FILE_ENTRY.getUrl(getReposInfo().getRepositoryID());
	}

	/**
	 * getEntry
	 * <p>
	 * Returns a fully loaded file entry.
	 * 
	 * @param id
	 *            The file entry object identifier
	 * @return
	 */
	public FileEntry getEntry(String id) throws FileServiceException {
		return getEntry(id, true);
	}

	/**
	 * getEntry
	 * <p>
	 * Returns a fully loaded file entry.
	 * 
	 * @param id
	 *            The file entry object identifier
	 * @return
	 * @throws FileServiceException
	 */
	public FileEntry getEntry(String id, boolean loadIt) throws FileServiceException {
		if (id == null) {
			throw new IllegalArgumentException(StringUtil.format("A null entry identifier  was passed"));
		}
		FileEntry entry = new FileEntry(id, this);

		if (loadIt) {

			try {
				entry.load();
			} catch (ClientServicesException e) {
				// TODO: agree on level of client exception. Any exception should be severe, but client
				// exception aren't really exceptional behaviour and sometime expected as part of the API
				if (logger.isLoggable(Level.WARNING)) {
					logger.log(Level.WARNING, e.getMessage(), e);
				}
				Reason reason = Reason.SERVER_ERROR;
				if (e.isClientError()) {
					reason = Reason.CLIENT_ERROR;
				}

				throw new FileServiceException(e, StringUtil.format(
						"Error reading the file entry with id {0}", id), reason);
			}

		}
		return entry;
	}

	/**
	 * getEntry
	 * <p>
	 * Returns a partially loaded file entry, using the FieldFilter to retrieve only specific fields from the
	 * entry.
	 * 
	 * @param id
	 *            The file entry object identifier
	 * @param includeACL
	 *            Boolean that determines if to retrieve or not the file entry ACL
	 * @param filter
	 *            The field filter, @see {@link FieldFilter}
	 * @return
	 * @throws FileServiceException
	 */
	public FileEntry getEntry(String id, Boolean includeACL, FieldFilter filter) throws FileServiceException {
		if (id == null) {
			throw new IllegalArgumentException(StringUtil.format("A null entry identifier  was passed"));
		}
		FileEntry entry = new FileEntry(id, this);

		Map<String, String> parameters = new HashMap<String, String>();

		if (filter != null) {
			parameters.put("filter", filter.getForURLQuery());
		}
		if (includeACL != null) {
			parameters.put("includeACL", includeACL.toString());
		}

		try {
			entry.load(parameters);
		} catch (ClientServicesException e) {
			// TODO: agree on level of client exception. Any exception should be severe, but client
			// exception aren't really exceptional behaviour and sometime expected as part of the API
			if (logger.isLoggable(Level.WARNING)) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			Reason reason = Reason.SERVER_ERROR;
			if (e.isClientError()) {
				reason = Reason.CLIENT_ERROR;
			}

			throw new FileServiceException(e, StringUtil.format("Error reading the file entry with id {0}",
					id), reason);
		}

		return entry;
	}

	@Override
	public <DataFormat> BaseEntity<DataFormat> getEntityFromId(String entityName, String uuid)
			throws ClientServicesException {
		if (FileEntry.class.getSimpleName().equalsIgnoreCase(entityName)) {

			try {
				return new FileEntry<DataFormat>(uuid, this);
			} catch (FileServiceException e) {
				// This exception handling applies only to the file service API where creating an entity ref
				// requires to fetch the server file API to build the retrieval URL
				throw new ClientServicesException(e);
			}

		} else if (RepositoryInfo.class.getSimpleName().equalsIgnoreCase(entityName)) {
			return new RepositoryInfo<DataFormat>(uuid, this);
		}
		throw new IllegalArgumentException("Entity decoding not supported" + entityName);
	}

	@Override
	public <DataFormat> BaseEntity<DataFormat> getEntityFromData(String entityName, DataFormat data)
			throws ClientServicesException {
		if (FileEntry.class.getSimpleName().equalsIgnoreCase(entityName)) {

			try {
				return new FileEntry<DataFormat>(data, this);
			} catch (FileServiceException e) {
				// not to be taken as example, only applies to smartcloud file service API, other entities
				// shouldn't fail construction
				throw new ClientServicesException(e);
			}

		} else if (RepositoryInfo.class.getSimpleName().equalsIgnoreCase(entityName)) {
			return new RepositoryInfo<DataFormat>(data, this);
		}
		throw new IllegalArgumentException("Entity decoding not supported" + entityName);
	}
}
