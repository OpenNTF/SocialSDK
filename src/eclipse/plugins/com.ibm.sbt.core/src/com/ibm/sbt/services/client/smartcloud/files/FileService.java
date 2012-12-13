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
import com.ibm.sbt.services.client.SBTServiceException;
import com.ibm.sbt.services.client.smartcloud.base.BaseEntity;

/**
 * Service class for all SmartCloud file operations
 * 
 * @author Lorenzo Boccaccia
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
				"/files/basic/cmis/repository/p!{subscriberId}/folderc/snx:files");

		private String	url;

		private FilesAPI(String url) {
			this.url = url;
		}

		String getUrl(String repositoryID) {
			if (repositoryID != null) {
				return url.replace("p!{subscriberId}", repositoryID);
			} else {
				return url;
			}
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
	 * Executes a service call to return all the user files
	 * 
	 * @return all the files for the current SmartCloud account
	 * @throws SBTServiceException
	 */
	public List<FileEntry> getMyFiles() throws SBTServiceException {
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
			throw new FileServiceException(e);
		}

		return entries;
	}

	/**
	 * Executes a service query to list all the user files
	 * 
	 * @return all the files for the current SmartCloud account
	 * @throws SBTServiceException
	 */
	public List<FileEntry> getMyFilesAlt() throws SBTServiceException {
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
			throw new FileServiceException(e);
		}
		return entries;
	}

	/**
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
	 * @throws SBTServiceException
	 */
	public List<FileEntry> getMyFiles(Integer maxItems, Integer skipCount, FieldFilter filter)
			throws SBTServiceException {

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
			throw new FileServiceException(e);
		}
		return entries;

	}

	/**
	 * Uploads a file to SmartCloud A file with the same name must not exists.
	 * 
	 * @param f
	 *            the file to upload
	 * @return the FileEntry created on SmartCloud for the file
	 * @throws SBTServiceException
	 */

	public FileEntry uploadFile(File f) throws SBTServiceException {
		return this.uploadFile(f, f.getName());
	}

	/**
	 * Uploads a file to SmartCloud A file with the same name must not exists. This version allow to upload
	 * the file under a different name
	 * 
	 * @param f
	 *            the file to upload
	 * @param name
	 *            The name to use for the newly uploaded file
	 * @return the FileEntry created on SmartCloud for the file
	 * @throws SBTServiceException
	 */
	public FileEntry uploadFile(File file, final String name) throws SBTServiceException {

		try {
			return this.uploadFile(new FileInputStream(file), name, file.length());
		} catch (FileNotFoundException e) {
			throw new FileServiceException(e);
		}
	}

	/**
	 * Upload a binary stream to SmartCloud as a file. The length of the stream must be know
	 * 
	 * @param stream
	 *            The stream to read from when uploading the file. Buffering will be added if necessary.
	 * @param name
	 *            The name for the new file to create on SmartCloud
	 * @param length
	 *            The number of characters to be read from the stream
	 * @return the FileEntry created on SmartCloud for the file
	 * @throws SBTServiceException
	 */
	public <DataFormat> FileEntry<DataFormat> uploadFile(java.io.InputStream stream, final String name,
			long length) throws SBTServiceException {
		// TODO: pass data to the content handler
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "uploading File");
		}
		if (length < 0) {
			throw new UnsupportedOperationException("Chunked Transfer Encoding Not Supported");
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
			if (e.getResponseStatusCode() == ClientServicesException.CONFLICT) {
				throw new DuplicateFileException(e, StringUtil.format(
						"A file with the name {0} already exists on the server for this user", name));
			}
			throw new FileServiceException(e);
		}
	}

	/**
	 * @return the repositoryInfo object containing the parameter required as entry point to every other
	 *         service call, the subscriber ID
	 * @throws SBTServiceException
	 */
	private RepositoryInfo getReposInfo() throws SBTServiceException {
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
				throw new FileServiceException(e);
			}
		}
		return reposInfo;
	}

	/**
	 * required as protected from FileEntry loading
	 * 
	 * @return the URL configured with the correct repository ID
	 * @throws SBTServiceException
	 */
	protected String getEntryLoadURL() throws SBTServiceException {

		return FilesAPI.GET_FILE_ENTRY.getUrl(getReposInfo().getRepositoryID());
	}

	/**
	 * Returns a fully loaded file entry.
	 * 
	 * @param id
	 *            The file entry object identifier
	 * @return
	 */
	public FileEntry getEntry(String id) throws SBTServiceException {
		return getEntry(id, true);
	}

	/**
	 * Returns a fully loaded file entry.
	 * 
	 * @param id
	 *            The file entry object identifier
	 * @return
	 * @throws SBTServiceException
	 */
	public FileEntry getEntry(String id, boolean loadIt) throws SBTServiceException {
		id = id.trim();
		FileEntry entry = new FileEntry(id, this);
		if (loadIt) {

			try {
				entry.load();
			} catch (ClientServicesException e) {
				throw new FileServiceException(e);
			}

		}
		return entry;
	}

	@Override
	public <DataFormat> BaseEntity<DataFormat> getEntityFromId(String entityName, String uuid)
			throws SBTServiceException {
		if (FileEntry.class.getSimpleName().equalsIgnoreCase(entityName)) {

			return new FileEntry<DataFormat>(uuid, this);

		} else if (RepositoryInfo.class.getSimpleName().equalsIgnoreCase(entityName)) {
			return new RepositoryInfo<DataFormat>(uuid, this);
		}
		throw new IllegalArgumentException("Entity decoding not supported" + entityName);
	}

	@Override
	public <DataFormat> BaseEntity<DataFormat> getEntityFromData(String entityName, DataFormat data)
			throws SBTServiceException {
		if (FileEntry.class.getSimpleName().equalsIgnoreCase(entityName)) {

			return new FileEntry<DataFormat>(data, this);

		} else if (RepositoryInfo.class.getSimpleName().equalsIgnoreCase(entityName)) {
			return new RepositoryInfo<DataFormat>(data, this);
		}
		throw new IllegalArgumentException("Entity decoding not supported" + entityName);
	}

}
