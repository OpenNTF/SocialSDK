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

import static com.ibm.sbt.services.client.base.CommonConstants.APPLICATION_ATOM_XML;
import static com.ibm.sbt.services.client.base.CommonConstants.AUTH_TYPE;
import static com.ibm.sbt.services.client.base.CommonConstants.TRUE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;
import static com.ibm.sbt.services.client.connections.files.FileConstants.CATEGORY;
import static com.ibm.sbt.services.client.connections.files.FileConstants.CATEGORY_COLLECTION;
import static com.ibm.sbt.services.client.connections.files.FileConstants.CATEGORY_COMMENT;
import static com.ibm.sbt.services.client.connections.files.FileConstants.CATEGORY_COMMUNITY;
import static com.ibm.sbt.services.client.connections.files.FileConstants.DIRECTION_INBOUND;
import static com.ibm.sbt.services.client.connections.files.FileConstants.DIRECTION_OUTBOUND;
import static com.ibm.sbt.services.client.connections.files.FileConstants.LOCKTYPE_HARD;
import static com.ibm.sbt.services.client.connections.files.FileConstants.LOCKTYPE_NONE;
import static com.ibm.sbt.services.client.connections.files.FileConstants.VISIBILITY;
import static com.ibm.sbt.services.client.connections.files.FileConstants.VISIBILITY_PRIVATE;
import static com.ibm.sbt.services.client.connections.files.FileConstants.VISIBILITY_PUBLIC;
import static com.ibm.sbt.services.client.connections.files.FileConstants.X_UPDATE_NONCE;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.text.MessageFormat;
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
import com.ibm.sbt.services.client.base.ConnectionsService;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.communities.CommunityServiceException;
import com.ibm.sbt.services.client.connections.files.model.FileCommentParameterBuilder;
import com.ibm.sbt.services.client.connections.files.model.FileCommentsFeedParameterBuilder;
import com.ibm.sbt.services.client.connections.files.model.FileEntryXPath;
import com.ibm.sbt.services.client.connections.files.model.FileRequestParams;
import com.ibm.sbt.services.client.connections.files.model.FileRequestPayload;
import com.ibm.sbt.services.client.connections.files.model.Headers;
import com.ibm.sbt.services.client.connections.files.transformers.CommentTransformer;
import com.ibm.sbt.services.client.connections.files.transformers.FileTransformer;
import com.ibm.sbt.services.client.connections.files.transformers.FolderTransformer;
import com.ibm.sbt.services.client.connections.files.transformers.ModerationTransformer;
import com.ibm.sbt.services.client.connections.files.transformers.MultipleFileTransformer;
import com.ibm.sbt.services.client.connections.files.util.Messages;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * FileService can be used to perform File related operations.
 * <p>
 * Relies on the ID's provided by the user to perform the task.
 * 
 * @Represents Connections FileService
 * @author Vimal Dhupar
 * @author Carlos Manias
 * @see http 
 *      ://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation
 *      #action=openDocument&res_title=Files_API_ic40a&content=pdcontent
 */
public class FileService extends ConnectionsService {
	
	private static final long serialVersionUID = 6858863659072208180L;
	static final String sourceClass = FileService.class.getName();
	static final Logger logger = Logger.getLogger(sourceClass);
	private final HashMap<String, String> commentParams = new HashMap<String, String>();

    /**
     * Default Constructor - 0 argument constructor Calls the Constructor of BaseService Class.
     * <p>
     * Creates FileService with Default Endpoint.
     */
    public FileService() {
        this(getDefaultEndpoint());
    }

	/**
     * Constructor
     * 		Creates a service object with specified endpoint
     * 
     * @param endpoint
     */
    public FileService(String endpoint) {
        super(endpoint);
        commentParams.put(CATEGORY, CATEGORY_COMMENT);
    }
    
	/**
     * Constructor
     * 		Creates a service object with specified endpoint
     * 
     * @param endpoint
     */
    public FileService(Endpoint endpoint) {
        super(endpoint);
        commentParams.put(CATEGORY, CATEGORY_COMMENT);
    }

	/**
	 * Return mapping key for this service
	 */
	@Override
	public String getServiceMappingKey() {
		return "files";
	}
    
    private static String getDefaultEndpoint() {
		return "connections";
	}
    
    @Override
    public NamedUrlPart getAuthType(){
    	return new NamedUrlPart(AUTH_TYPE, AuthType.BASIC.get());
    }
    
    protected HashMap<String, String> getCommentParams(){
    	return commentParams;
    }

	/***************************************************************
	 * FeedHandlers for each entity type
	 ****************************************************************/

	/**
	 * 
	 * @return
	 */
	public IFeedHandler<File> getFileFeedHandler() {
		return new AtomFeedHandler<File>(this, false) {
			@Override
			protected File entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new File(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	/**
	 * 
	 * @return
	 */
	public IFeedHandler<Comment> getCommentFeedHandler() {
		return new AtomFeedHandler<Comment>(this, false) {
			@Override
			protected Comment entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Comment(service, node, nameSpaceCtx, xpath);
			}
		};
	}
    
    public void actOnCommentAwaitingApproval(String commentId, String action, String actionReason)
            throws ClientServicesException, TransformerException {
        // get thr uri from here ::
        // In the service document, locate the workspace with the <category term="comments-moderation" .../>
        // child element, and then find the collection with the <atom:category term="approval-action" .../>
        // child element, and make a note of the web address in its href attribute.

        FilesModerationDocumentEntry fileModDocEntry = getFilesModerationDocumentEntry();
        // get the request URI from the servide document.
        String requestUri = fileModDocEntry.get(ContentMapFiles.moderationMap.get("commentApprovalUrl")); // TODO
        // If not obtained due to some reason, then construct the url in regular fashion.
        // URI : /files/basic/api/approval/actions/comments
        if (StringUtil.isEmpty(requestUri)) {
        	String accessType = AccessType.AUTHENTICATED.getText();
        	action = Categories.APPROVAL.get();
        	String content = ModerationContentTypes.COMMENT.get();
            requestUri = FileUrls.MODERATION.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.action.get(action),  FileUrlParts.contentType.get(content));
        }
        Object payload = constructPayloadForModeration(commentId, action, actionReason, "comment");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        try {
            updateData(requestUri, null, headers, payload, commentId);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInStatusChange);
        }
    }

    public void actOnFileAwaitingApproval(String fileId, String action, String actionReason)
            throws ClientServicesException, TransformerException {
        // get the uri
        // In the service document, locate the workspace with the <category term="documents-moderation" .../>
        // child element, and then find the collection with the <atom:category term="approval-action" .../>
        // child element, and make a note of the web address in its href attribute.
        FilesModerationDocumentEntry fileModDocEntry = getFilesModerationDocumentEntry();
        // get the request URI from the servide document.
        String requestUri = fileModDocEntry.get(ContentMapFiles.moderationMap.get("fileApprovalUrl")); // TODO
        // If not obtained due to some reason, then construct the url in regular fashion.
        // URI : /files/basic/api/approval/actions/documents
        if (StringUtil.isEmpty(requestUri)) {
        	String accessType = AccessType.AUTHENTICATED.getText();
        	action = Categories.APPROVAL.get();
        	String content = ModerationContentTypes.DOCUMENTS.get();
            requestUri = FileUrls.MODERATION.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.action.get(action),  FileUrlParts.contentType.get(content));
        }
        Object payload = constructPayloadForModeration(fileId, action, actionReason, "file");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        try {
            updateData(requestUri, null,  headers, payload, fileId);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInStatusChange);
        }
    }

    public void actOnFlaggedComment(String commentId, String action, String actionReason)
            throws ClientServicesException, TransformerException {
        // get thr uri from here ::
        // In the service document, locate the workspace with the <category term="comments-moderation" .../>
        // child element, and then find the collection with the <atom:category term="approval-action" .../>
        // child element, and make a note of the web address in its href attribute.

        FilesModerationDocumentEntry fileModDocEntry = getFilesModerationDocumentEntry();
        // get the request URI from the servide document.
        String requestUri = fileModDocEntry.get(ContentMapFiles.moderationMap.get("commentReviewUrl")); // TODO
        // If not obtained due to some reason, then construct the url in regular fashion.
        // URI : /files/basic/api/review/actions/comments
        if (StringUtil.isEmpty(requestUri)) {
        	String accessType = AccessType.AUTHENTICATED.getText();
        	action = Categories.REVIEW.get();
        	String content = ModerationContentTypes.COMMENT.get();
            requestUri = FileUrls.MODERATION.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.action.get(action),  FileUrlParts.contentType.get(content));
        }
        Object payload = constructPayloadForModeration(commentId, action, actionReason, "comment");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        try {
            updateData(requestUri, null, headers, payload, commentId);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInFlaggingComment);
        }
    }

    public void actOnFlaggedFile(String fileId, String action, String actionReason)
            throws ClientServicesException, TransformerException {
        // get the uri
        // In the service document, locate the workspace with the <category term="documents-moderation" .../>
        // child element, and then find the collection with the <atom:category term="approval-action" .../>
        // child element, and make a note of the web address in its href attribute.
        FilesModerationDocumentEntry fileModDocEntry = getFilesModerationDocumentEntry();
        // get the request URI from the servide document.
        String requestUri = fileModDocEntry.get(ContentMapFiles.moderationMap.get("fileReviewUrl")); // TODO
        // If not obtained due to some reason, then construct the url in regular fashion.
        // URI : /files/basic/api/review/actions/documents
        if (StringUtil.isEmpty(requestUri)) {
        	String accessType = AccessType.AUTHENTICATED.getText();
        	action = Categories.REVIEW.get();
        	String content = ModerationContentTypes.DOCUMENTS.get();
            requestUri = FileUrls.MODERATION.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.action.get(action),  FileUrlParts.contentType.get(content));
        }
        Object payload = constructPayloadForModeration(fileId, action, actionReason, "file");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        try {
            updateData(requestUri, null, headers, payload, fileId);
       } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MesssageExceptionInFlaggingFile);
        }
    }

    /**
     * addCommentToFile
     * <p>
     * Rest API used : /files/basic/api/userlibrary/{userid}/document/{document-id}/feed <br>
     * 
     * @param fileId - ID of the file
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @param comment - Comment to be added to the File
     * @return Comment
     * @throws ClientServiceException
     * @throws TransformerException 
     */
    public Comment addCommentToFile(String fileId, String comment, Map<String, String> params) throws ClientServicesException, TransformerException {
    	return addCommentToFile(fileId, comment, null, params);
    }

    /**
     * addCommentToFile
     * <p>
     * Rest API used : /files/basic/api/userlibrary/{userid}/document/{document-id}/feed <br>
     * 
     * @param fileId - ID of the file
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @param comment - Comment to be added to the File
     * @param libraryId - Id of the library the file is present
     * @return Comment
     * @throws ClientServicesException
     * @throws TransformerException 
     */
    public Comment addCommentToFile(String fileId, String comment, String userId,
            Map<String, String> params) throws ClientServicesException, TransformerException {
    	//FIX: DUPLICATE METHOD see createComment()
        if (StringUtil.isEmpty(fileId)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri;
        if (StringUtil.isEmpty(userId)){
        	requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_FEED.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        } else {
        	requestUri = FileUrls.USERLIBRARY_DOCUMENT_FEED.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId), FileUrlParts.fileId.get(fileId));
        }
        Document payload = constructPayloadForComments(comment);
        try {
            Response result = (Response) createData(requestUri, null, new ClientService.ContentXml(payload, APPLICATION_ATOM_XML));
            return getCommentFeedHandler().createEntity(result);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInCreatingComment);
        }
    }

    /**
     * Method to add comments to a Community file
     * <p>
     * Rest API used : 
     * /files/basic/api/communitylibrary/<communityId>/document/<fileId>/feed
     * 
     * @param fileId
     * @param comment
     * @param communityId
     * @return Comment
     * @throws ClientServicesException
     * @throws TransformerException
     */
    public Comment addCommentToCommunityFile(String fileId, String comment, String communityId) throws ClientServicesException, TransformerException { 
    	return addCommentToCommunityFile(fileId, comment, communityId, null);
    }
    
    /**
     * Method to add comments to a Community file
     * <p>
     * Rest API used : 
     * /files/basic/api/communitylibrary/<communityId>/document/<fileId>/feed
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
        String requestUri = FileUrls.COMMUNITY_FILE_COMMENT.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.communityId.get(communityId), FileUrlParts.fileId.get(fileId));
        Document payload = constructPayloadForComments(comment);
        try {
            Response result = (Response) createData(requestUri, null, new ClientService.ContentXml(payload, APPLICATION_ATOM_XML));
            return getCommentFeedHandler().createEntity(result);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInCreatingComment);
        }
    }
    
    /**
     * Method to get a list of Community Files
     * @param communityId
     * @return
     * @throws ClientServicesException
     */
    public EntityList<File> getCommunityFiles(String communityId) throws ClientServicesException {
    	return getCommunityFiles(communityId, null);
    }
    
    /**
	 * Method to get a list of Community Files
	 * @param communityId
	 * @param params
	 * @return FileList
	 * @throws CommunityServiceException
	 */
	public EntityList<File> getCommunityFiles(String communityId, HashMap<String, String> params) throws ClientServicesException {
		String accessType = AccessType.AUTHENTICATED.getText();
        String requestUrl = FileUrls.COMMUNITYLIBRARY_FEED.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.communityId.get(communityId));
		params = (null == params)?new HashMap<String, String>():params;
		return getFileEntityList(requestUrl, params);
	}
	
	/**
	 * Method to get a Community File
	 * @param communityId
	 * @param fileId
	 * @return File
	 * @throws ClientServicesException
	 */
	public File getCommunityFile(String communityId, String fileId) throws ClientServicesException {
		return getCommunityFile(communityId, fileId, null);
	}
	
	/**
	 * Method to get a Community File
	 * @param communityId
	 * @param fileId
	 * @param params
	 * @return File
	 * @throws ClientServicesException
	 */
	public File getCommunityFile(String communityId, String fileId, HashMap<String, String> params) throws ClientServicesException {
		String accessType = AccessType.AUTHENTICATED.getText();
        String requestUrl = FileUrls.GET_COMMUNITY_FILE.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.communityId.get(communityId), FileUrlParts.fileId.get(fileId));
		params = (null == params)?new HashMap<String, String>():params;
		return getFileEntity(requestUrl, params);
	}
	
	/**
	 * Method to get a list of Files shared with the Community
	 * @param communityId
	 * @return FileList
	 * @throws ClientServicesException
	 */
	public EntityList<File> getCommunitySharedFiles(String communityId) throws ClientServicesException {
		return getCommunitySharedFiles(communityId, null); 
	}
	
	/**
	 * Method to get a list of Files shared with the Community
	 * @param communityId
	 * @param params
	 * @return FileList
	 * @throws ClientServicesException
	 */
	public EntityList<File> getCommunitySharedFiles(String communityId, HashMap<String, String> params) throws ClientServicesException {
		String accessType = AccessType.AUTHENTICATED.getText();
        String requestUrl = FileUrls.GET_COMMUNITY_COLLECTION.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.communityId.get(communityId));
		params = (null == params)?new HashMap<String, String>():params;
		return getFileEntityList(requestUrl, params);
	}
	
	/**
	 * Method to download a community file. A community file is a public file.
	 * @param ostream - output stream which contains the binary content of the file
	 * @param fileId
	 * @param libraryId - Library Id of which the file is a part. This value can be obtained by using File's getLibraryId method.
	 * @return
	 * @throws ClientServicesException
	 */
	public long downloadCommunityFile(OutputStream ostream, final String fileId, final String libraryId) throws ClientServicesException {
		return downloadCommunityFile(ostream, fileId, libraryId, null);
	}
	/**
	 * Method to download a community file. A community file is a public file. 
	 * @param ostream - output stream which contains the binary content of the file
	 * @param fileId
	 * @param libraryId - Library Id of which the file is a part. This value can be obtained by using File's getLibraryId method.
	 * @param params
	 * @return long 
	 * @throws ClientServicesException
	 */
	public long downloadCommunityFile(OutputStream ostream, final String fileId, final String libraryId, Map<String, String> params) throws ClientServicesException {
		return downloadFile(ostream, fileId, libraryId, params, true);
	}
	
	/**
	 * Method to download a File of logged in user 
	 * @param ostream - output stream which contains the binary content of the file
	 * @param fileId 
	 * @return long - no of bytes
	 * @throws ClientServicesException
	 */
	 public long downloadFile(OutputStream ostream, final String fileId) throws ClientServicesException {
		 return downloadFile(ostream, fileId, null, false);
	 }
    
	/**
	 * Method to download a File 
	 * @param ostream - output stream which contains the binary content of the file
	 * @param fileId
	 * @param libraryId - required in case of public files
	 * @param isPublic - flag to indicate public file
	 * @return long - no of bytes
	 * @throws ClientServicesException
	 */
	public long downloadFile(OutputStream ostream, final String fileId, final String libraryId, boolean isPublic) throws ClientServicesException {
		return downloadFile(ostream, fileId, libraryId, null, isPublic);
	}
	
	/**
	 * Method to download a File of logged in user
	 * @param ostream - output stream which contains the binary content of the file
	 * @param fileId
	 * @param libraryId - required in case of public file
	 * @param params
	 * @return long - no of bytes
	 * @throws ClientServicesException
	 */
	public long downloadFile(OutputStream ostream, final String fileId, Map<String, String> params) throws ClientServicesException {
		return downloadFile(ostream, fileId, null, params, false);
	}
	
	/**
	 * Method to download a File 
	 * @param ostream - output stream which contains the binary content of the file
	 * @param fileId
	 * @param libraryId - required in case of public file
	 * @param params
	 * @param isPublic - flag to indicate public file
	 * @return long - no of bytes
	 * @throws ClientServicesException
	 */
	public long downloadFile(OutputStream ostream, final String fileId, final String libraryId, Map<String, String> params, boolean isPublic) throws ClientServicesException {
		File file = !isPublic ? getFile(fileId) : getPublicFile(fileId, libraryId, null);
		// now we have the file.. we need to download it.. 
		String accessType = !isPublic ? AccessType.AUTHENTICATED.getText() : AccessType.PUBLIC.getText();
		String category = !isPublic ? Categories.MYUSERLIBRARY.get() : null;
		String libraryFilter = (libraryId != null)?"library":"";

        String requestUrl = FileUrls.DOWNLOAD_FILE.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.category.get(category), FileUrlParts.fileId.get(file.getFileId()), 
        		FileUrlParts.libraryFilter.get(libraryFilter), FileUrlParts.libraryId.get(libraryId));

		Map<String, String> headers = new HashMap<String, String>();
		headers.put(Headers.ContentType, Headers.BINARY);
		Response response = null;
		response = getClientService().get(requestUrl, params, headers, ClientService.FORMAT_INPUTSTREAM);
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
	 * Method to download the specified file
	 *  
	 * @param ostream - output stream which contains the binary content of the file
	 * @param file
	 * @param params
	 * 
	 * @return long - no of bytes
	 * @throws ClientServicesException
	 */
	public long downloadFile(OutputStream ostream, File file, Map<String, String> params) throws ClientServicesException {
		// file content url
		String requestUrl = file.getEditMediaUrl(); 
		
		// request headers
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(Headers.ContentType, Headers.BINARY);
		
		// trigger request to download the file
		Response response = null;
		response = getClientService().get(requestUrl, params, headers, ClientService.FORMAT_INPUTSTREAM);
		
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
	 * Method to update Community File's Metadata
	 * <p>
	 * Rest API used : 
	 * /files/basic/api/library/<communityLibraryId>/document/<fileId>/entry
	 * <p>
	 * @param fileEntry
	 * @param communityLibraryId
	 * @param params
	 * @return File
	 * @throws ClientServicesException
	 */
	public File updateCommunityFileMetadata(File fileEntry, String communityLibraryId, Map<String, String> params) throws ClientServicesException {
		if (fileEntry == null) {
            throw new ClientServicesException(null, Messages.Invalid_File);
        }
        if (StringUtil.isEmpty(fileEntry.getFileId())) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.COMMUNITY_FILE_METADATA.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.communityId.get(communityLibraryId), FileUrlParts.fileId.get(fileEntry.getFileId()));
        Document updateFilePayload = null;
        try {
        	updateFilePayload = constructPayload(fileEntry.getFileId(), fileEntry.getFieldsMap());
            Response result = (Response) updateData(requestUri, params, new ClientService.ContentXml(
            		updateFilePayload, "application/atom+xml"), null);
            return getFileFeedHandler().createEntity(result);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInUpdate);
        } catch (TransformerException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInUpdate);
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
     * @return FileList
     * @throws ClientServicesException
     * @throws TransformerException 
     */
    public FileList addFilesToFolder(String folderId, List<String> listOfFileIds,
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
		params = (null == params)?new HashMap<String, String>():params;
        String requestUri = FileUrls.COLLECTION_FEED.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.folderId.get(folderId));
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        Object payload = constructPayloadForMultipleEntries(listOfFileIds,
                FileRequestParams.ITEMID.getFileRequestParams());

        try {
            Response result;
            result = (Response) createData(requestUri, params, headers, payload);
            return (FileList) getFileFeedHandler().createEntityList(result);
            
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageGenericException);
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
     * @throws ClientServicesException
     * @throws TransformerException 
     */
    public void addFileToFolders(String fileId, List<String> folderIds) throws ClientServicesException, TransformerException {
        addFileToFolders(fileId, folderIds, null, null);
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
     * @throws ClientServicesException
     * @throws TransformerException 
     */
    public void addFileToFolders(String fileId, List<String> folderIds, Map<String, String> params)
            throws ClientServicesException, TransformerException {
        addFileToFolders(fileId, folderIds, null, params);
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
     * @throws ClientServicesException
     * @throws TransformerException 
     */
    public void addFileToFolders(String fileId, List<String> folderIds, String userId)
            throws ClientServicesException, TransformerException {
        addFileToFolders(fileId, folderIds, userId, null);
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
     * @throws ClientServicesException
     * @throws TransformerException 
     */
    public void addFileToFolders(String fileId, List<String> folderIds, String userId,
            Map<String, String> params) throws ClientServicesException, TransformerException {
        String accessType = AccessType.AUTHENTICATED.getText();

        String requestUri;
        if (StringUtil.isEmpty(userId)) {
        	requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_FEED.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        } else {
        	requestUri = FileUrls.USERLIBRARY_DOCUMENT_FEED.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId), FileUrlParts.fileId.get(fileId));
        }

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        headers.put(Headers.ContentLanguage, Headers.UTF);

		params = (null == params)?new HashMap<String, String>():params;
        Object payload = constructPayloadForMultipleEntries(folderIds,
                FileRequestParams.ITEMID.getFileRequestParams(), "collection");
        try {
            createData(requestUri, params, headers, payload);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageGenericException);
        }
    }

    public Comment createComment(String fileId, String comment) throws ClientServicesException, TransformerException {
        return createComment(fileId, comment, null, null);
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
        return createComment(fileId, comment, userId, null);
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
        if (StringUtil.isEmpty(userId)){
        	requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_FEED.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        } else {
        	requestUri = FileUrls.USERLIBRARY_DOCUMENT_FEED.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId), FileUrlParts.fileId.get(fileId));
        }
       
        Document payload = constructPayloadForComments(comment);
        Map<String, String> headers = new HashMap<String, String>();
      
        try {
            Response result = (Response) createData(requestUri, params, headers, new ClientService.ContentXml(
                    payload, APPLICATION_ATOM_XML));
            return getCommentFeedHandler().createEntity(result);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInCreatingComment);
        }
    }

    public File createFolder(String name) throws ClientServicesException, TransformerException {
        return createFolder(name, null, null);
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
     * @return File
     * @throws ClientServicesException
     * @throws TransformerException 
     */
    public File createFolder(String name, String description) throws ClientServicesException, TransformerException {
        return createFolder(name, description, null);
    }

    public File createFolder(String name, String description, String shareWith)
            throws ClientServicesException, TransformerException {
        String accessType = AccessType.AUTHENTICATED.getText();

        String requestUri = FileUrls.COLLECTIONS_FEED.format(this, FileUrlParts.accessType.get(accessType));
        Document payload = constructPayloadFolder(name, description, shareWith, "create");

        try {
            Response result = (Response) createData(requestUri, null,
                    new ClientService.ContentXml(
                            payload, APPLICATION_ATOM_XML));
            return getFileFeedHandler().createEntity(result);
        } catch (Exception e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInCreatingFolder);
        }

    }

    /**
     * delete
     * <p>
     * Rest API used : /files/basic/api/myuserlibrary/document/{document-id}/entry <br>
     * 
     * @param fileId - id of the file to be deleted
     * @throws ClientServicesException
     */
    public void deleteFile(String fileId) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));

        try {
            deleteData(requestUri, null, null);
        } catch (Exception e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInDeletingFile);
        }

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
        deleteAllFilesFromRecycleBin(null);
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
        if (StringUtil.isEmpty(userId)){
        	requestUri = FileUrls.MYUSERLIBRARY_RECYCLEBIN_FEED.format(this, FileUrlParts.accessType.get(accessType));
        } else {
        	requestUri = FileUrls.EMPTY_RECYCLE_BIN.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId));
        }
        try {
            deleteData(requestUri, null, null);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInDeletingFile);
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
     * @throws ClientServicesException
     */
    public void deleteAllVersionsOfFile(String fileId, String versionLabel, Map<String, String> params)
            throws ClientServicesException {
        if (StringUtil.isEmpty(versionLabel)) {
            throw new ClientServicesException(null, Messages.InvalidArgument_VersionLabel);
        }
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_FEED.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
		params = (null == params)?new HashMap<String, String>():params;
        params.put(FileRequestParams.CATEGORY.getFileRequestParams(), "version");
        params.put(FileRequestParams.DELETEFROM.getFileRequestParams(), versionLabel);

        try {
            deleteData(requestUri, params, null);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInDeletingFile);
        }

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
        deleteComment(fileId, commentId, "");
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
        if (StringUtil.isEmpty(userId)){
        	requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_COMMENT_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId), FileUrlParts.commentId.get(commentId));
        } else {
        	requestUri = FileUrls.USERLIBRARY_DOCUMENT_COMMENT_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId), FileUrlParts.fileId.get(fileId), FileUrlParts.commentId.get(commentId));
        }

        try {
            deleteData(requestUri, null, null);
        } catch (Exception e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInDeletingComment);
        }
    }

    public void deleteFileAwaitingApproval(String fileId) throws ClientServicesException {
        if (StringUtil.isEmpty(fileId)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        String requestUri = getModerationUri(fileId, Categories.APPROVAL.get(), ModerationContentTypes.DOCUMENTS.get());
        try {
            deleteData(requestUri, null, null);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInDeletingFile);
        }
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
        deleteFileFromRecycleBin(fileId, null);
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
        if (StringUtil.isEmpty(userId)){
        	requestUri = FileUrls.MYUSERLIBRARY_RECYCLEBIN_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        } else {
        	requestUri = FileUrls.USERLIBRARY_RECYCLEBIN_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId), FileUrlParts.fileId.get(fileId));
        }

        try {
            deleteData(requestUri, null, null);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInDeletingFile);
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
     * @throws ClientServicesException
     */
    public void deleteFileShare(String fileId) throws ClientServicesException {
        deleteFileShare(fileId, null);
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
        try {
            deleteData(requestUri, params, null);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInDeletingFileShare);
        }
    }

    public void deleteFlaggedComment(String commentId) throws ClientServicesException {
        if (StringUtil.isEmpty(commentId)) {
            throw new ClientServicesException(null, Messages.Invalid_CommentId);
        }
        String requestUri = getModerationUri(commentId, Categories.REVIEW.get(), ModerationContentTypes.COMMENT.get());
        if (StringUtil.isEmpty(requestUri)) {
            return;
        }
        try {
            deleteData(requestUri, null, null);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInDeletingComment);
        }
    }

    public void deleteFlaggedFiles(String fileId) throws ClientServicesException {
        if (StringUtil.isEmpty(fileId)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        String requestUri = getModerationUri(fileId, Categories.REVIEW.get(), ModerationContentTypes.DOCUMENTS.get());
        if (StringUtil.isEmpty(requestUri)) {
            return;
        }
        try {
            deleteData(requestUri, null, null);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInDeletingFile);
        }
    }

    /**
     * deleteFolder
     * <p>
     * Deletes the folder corresponding to the folderId mentioned as input. <br>
     * Rest API Used : /basic/api/collection/{collection-id}/entry
     * 
     * @param folderId
     * @throws ClientServicesException
     */
    public void deleteFolder(String folderId) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.COLLECTION_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.folderId.get(folderId));
        try {
            deleteData(requestUri, null, null);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInDeleteFolder);
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
     * @throws ClientServicesException
     * @throws TransformerException 
     */
    public void flagAsInappropriate(String id, String flagReason, String flagWhat)
            throws ClientServicesException, TransformerException {
        if (StringUtil.isEmpty(id)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        if (StringUtil.isEmpty(flagWhat)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.COLLECTION_ENTRY.format(this, FileUrlParts.accessType.get(accessType));
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);

        Object payload = constructPayloadForFlagging(id, flagReason, flagWhat);
        try {
            updateData(requestUri, null, headers, payload, id);
        } catch (Exception e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInFlaggingInappropriate);
        }
    }

    public EntityList<File> getAllUserFiles(String userId) throws ClientServicesException {
        return getAllUserFiles(userId, null);
    }

    /**
     * getAllUserFiles
     * <p>
     * Rest API used : /files/basic/anonymous/api/userlibrary/{userid}/feed <br>
     * Public method. No Auth required.
     * 
     * @param userId
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileList
     * @throws ClientServicesException
     */

    public EntityList<File> getAllUserFiles(String userId, Map<String, String> params)
            throws ClientServicesException {
        String accessType = AccessType.PUBLIC.getText();
        String requestUri = FileUrls.GET_ALL_USER_FILES.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId));
        return getFileEntityList(requestUri, params);
    }

    public EntityList<Comment> getCommentsAwaitingApproval(Map<String, String> params)
            throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.GET_COMMENTS_AWAITING_APPROVAL.format(this, FileUrlParts.accessType.get(accessType));
        return getCommentEntityList(requestUri, params, null);
    }

    /**
     * getFile
     * 
     * @param fileId - ID of the file to be fetched from the Connections Server
     * @return File
     * @throws ClientServicesException
     */
    public File getFile(String fileId) throws ClientServicesException {
        return getFile(fileId, true);
    }

    /**
     * getFile
     * 
     * @param fileId - ID of the file to be fetched from the Connections Server
     * @param load - a flag to determine whether the network call should be made or an empty placeholder of
     *            the File object should be returned. load - true : network call is made to fetch the
     *            file load - false : an empty File object is returned, and then updations can be made on
     *            this object.
     * @return File
     * @throws ClientServicesException
     */
    public File getFile(String fileId, boolean load) throws ClientServicesException {
        return getFile(fileId, null, load);
    }

    /**
     * getFile
     * <p>
     * Rest API for getting files :- /files/basic/api/myuserlibrary/document/{document-id}/entry
     * 
     * @param fileId - ID of the file to be fetched from the Connections Server
     * @param parameters - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @param load - a flag to determine whether the network call should be made or an empty placeholder of
     *            the File object should be returned. load - true : network call is made to fetch the
     *            file load - false : an empty File object is returned, and then updations can be made on
     *            this object.
     * @return File
     * @throws ClientServicesException
     */
    public File getFile(String fileId, Map<String, String> parameters, boolean load)
            throws ClientServicesException {
        File file = new File(fileId);
        if (load) {
            SubFilters subFilters = new SubFilters();
            subFilters.setFileId(fileId);
            String accessType = AccessType.AUTHENTICATED.getText();
            String requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
            return getFileEntity(requestUri, parameters);
        }
        return file;
    }
    
    /**
     * getFile
     * Read the specified file from the specified library
     * 
     * @param fileId - ID of the file to be fetched from the Connections Server
     * @param libraryId - ID of the library to which the public file belongs
     * @param parameters - Map of Parameters. See {@link FileRequestParams} for possible values.    
     * @return File
     * @throws ClientServicesException
     */
    public File getFile(String fileId, String libraryId, Map<String, String> parameters) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.libraryId.get(libraryId), FileUrlParts.fileId.get(fileId));
        return getFileEntity(requestUri, parameters);
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
        String requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.libraryId.get(libraryId), FileUrlParts.fileId.get(fileId));
        return getFileEntity(requestUri, parameters);
    }

    public File getFileAwaitingAction(String fileId) throws ClientServicesException {
        if (StringUtil.isEmpty(fileId)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        String requestUri = getModerationUri(fileId, Categories.APPROVAL.get(), ModerationContentTypes.DOCUMENTS.get());
        return getFileEntity(requestUri, null);
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
            
    public EntityList<Comment> getUserFileComment(String fileId, String userId, String commentId, boolean anonymousAccess, Map<String, String> parameters, Map<String, String> headers) throws ClientServicesException {
        String accessType = anonymousAccess?AccessType.PUBLIC.getText():AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.USERLIBRARY_DOCUMENT_COMMENT_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId), FileUrlParts.fileId.get(fileId), FileUrlParts.commentId.get(commentId));
        return getCommentEntityList(requestUri, parameters, headers);
    }
    
    /**
     * retrieve a single comment from a file of the authenticated user
     * @param fileId
     * @param commentId
     * @param parameters a map of paramters; can be generated using the {@link FileCommentsParameterBuilder}
     * @return
     * @throws ClientServicesException
     */
    public EntityList<Comment> getFileComment(String fileId, String commentId, Map<String, String> parameters, Map<String, String> headers) throws ClientServicesException {
        if (StringUtil.isEmpty(fileId)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        if (StringUtil.isEmpty(commentId)) {
            throw new ClientServicesException(null, Messages.Invalid_CommentId);
        }
        
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_COMMENT_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId), FileUrlParts.commentId.get(commentId));
        return getCommentEntityList(requestUri, parameters, headers);
    }
    /**
     * retrieve all comments from a file of any user
     * @param fileId
     * @param userId
     * @param anonymousAccess try anonymous access - will only work if the file visibility is public
     * @param parameters a map of paramters; can be generated using the {@link FileCommentsFeedParameterBuilder}
     * @return
     * @throws ClientServicesException
     */
    public EntityList<Comment> getAllUserFileComments(String fileId, String userId, boolean anonymousAccess, Map<String,String> parameters) throws ClientServicesException {
        String accessType = anonymousAccess?AccessType.PUBLIC.getText():AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.USERLIBRARY_DOCUMENT_FEED.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId), FileUrlParts.fileId.get(fileId));
        
        return getCommentEntityList(requestUri, parameters,null);
    }
    /**
     * retrieve all comments from a file of the authenticated user
     * @param fileId
     * @param parameters a map of paramters; can be generated using the {@link FileCommentsFeedParameterBuilder}
     * @return
     * @throws ClientServicesException
     */
    public EntityList<Comment> getAllFileComments(String fileId, Map<String, String> parameters) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_FEED.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        return getCommentEntityList(requestUri, parameters,null);
    }
    
    /**
     * Method to get All comments of a Community File
     * <p>
     * Rest API Used : 
     * /files/basic/api/communitylibrary/<communityId>/document/<fileId>/feed
     * <p>
     * @param fileId
     * @param communityId
     * @return CommentList
     * @throws ClientServicesException
     */
    public EntityList<Comment> getAllCommunityFileComments(String fileId, String communityId) throws ClientServicesException {
    	return getAllCommunityFileComments(fileId, communityId, null);
    }
    
    /**
     * Method to get All comments of a Community File
     * <p>
     * Rest API Used : 
     * /files/basic/api/communitylibrary/<communityId>/document/<fileId>/feed
     * <p>
     * @param fileId
     * @param communityId
     * @param parameters
     * @return CommentList
     * @throws ClientServicesException
     */
    public EntityList<Comment> getAllCommunityFileComments(String fileId, String communityId, Map<String, String> parameters) throws ClientServicesException {
        String accessType =AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.COMMUNITY_FILE_COMMENT.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.communityId.get(communityId), FileUrlParts.fileId.get(fileId));
        return getCommentEntityList(requestUri, parameters, null);
    }

    /**
     * getFileFromRecycleBin
     * <p>
     * 
     * @param fileId
     * @return
     * @throws ClientServicesException
     */
    public File getFileFromRecycleBin(String fileId) throws ClientServicesException {
        return getFileFromRecycleBin(fileId, null, null);
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
        return getFileFromRecycleBin(fileId, userId, null);
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
     * @return File
     * @throws ClientServicesException
     */
    public File getFileFromRecycleBin(String fileId, String userId, Map<String, String> params)
            throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri;
        if (StringUtil.isEmpty(userId)){
        	requestUri = FileUrls.MYUSERLIBRARY_RECYCLEBIN_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        } else {
        	requestUri = FileUrls.USERLIBRARY_RECYCLEBIN_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId), FileUrlParts.fileId.get(fileId));
        }
        return getFileEntity(requestUri, params);
    }

    // /files/basic/api/approval/documents
    /**
     * getFilesAwaitingApproval
     * 
     * @param params
     * @return
     * @throws ClientServicesException
     */
    public EntityList<File> getFilesAwaitingApproval(Map<String, String> params) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.GET_FILES_AWAITING_APPROVAL.format(this, FileUrlParts.accessType.get(accessType));
        return getFileEntityList(requestUri, params);
    }

    /**
     * getFileShares
     * <p>
     * Get a feed that lists the share entries. A share entry describes an instance in which access that has
     * been given to a file. <br>
     * Rest API used : /files/basic/api/documents/shared/feed
     * 
     * @return
     * @throws ClientServicesException
     */
    public EntityList<File> getFileShares() throws ClientServicesException {
        return getFileShares(null);
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
     * @throws ClientServicesException
     */
    public EntityList<File> getFileShares(Map<String, String> params) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.DOCUMENTS_SHARED_FEED.format(this, FileUrlParts.accessType.get(accessType));
        return getFileEntityList(requestUri, params);
    }

    public EntityList<File> getFilesInFolder(String folderId) throws ClientServicesException {
        return getFilesInFolder(folderId, null);
    }

    /**
     * getFilesInFolder
     * <p>
     * Rest API used : /files/basic/api/collection/{collection-id}/feed
     * 
     * @param folderId - uuid of the folder/collection.
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileList
     * @throws ClientServicesException
     */
    public EntityList<File> getFilesInFolder(String folderId, Map<String, String> params)
            throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.COLLECTION_FEED.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.folderId.get(folderId));
        return getFileEntityList(requestUri, params);
    }

    /**
     * getFilesInMyRecycleBin
     * 
     * @return
     * @throws ClientServicesException
     */
    public EntityList<File> getFilesInMyRecycleBin() throws ClientServicesException {
        return getFilesInMyRecycleBin(null);
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
        String requestUri = FileUrls.MYUSERLIBRARY_RECYCLEBIN_FEED.format(this, FileUrlParts.accessType.get(accessType));
        return getFileEntityList(requestUri, params);
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
        String requestUri = FileUrls.MODERATION_SERVICE_DOCUMENT.format(this, FileUrlParts.accessType.get(accessType));
        Response result = null;
        try {
            result = retrieveData(requestUri, null);
        } catch (IOException e) {
        	throw new ClientServicesException(e, Messages.MessageExceptionInFetchingServiceDocument);
        }
        return (Document) result.getData();
    }

    protected FilesModerationDocumentEntry getFilesModerationDocumentEntry() throws ClientServicesException {
    	return new FilesModerationDocumentEntry(getFilesModerationServiceDocument());
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
        result = getClientService().get(requestUri, null, ClientService.FORMAT_XML);
        return (Document) result;
    }

    /**
     * getFilesSharedByMe
     * <p>
     * This method calls getFilesSharedByMe(Map<String, String> params) with null params
     * 
     * @return FileList
     * @throws ClientServicesException
     */ public EntityList<File> getFilesSharedByMe() throws ClientServicesException {
        return getFilesSharedByMe(null);
    }

    /**
     * getFilesSharedByMe
     * <p>
     * Rest API used : /files/basic/api/documents/shared/feed <br>
     * This method is used to get Files Shared By the person.
     * 
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileList
     * @throws ClientServicesException
     */

    public EntityList<File> getFilesSharedByMe(Map<String, String> params) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.DOCUMENTS_SHARED_FEED.format(this, FileUrlParts.accessType.get(accessType));
        params = getParameters(params);
        params.put(FileRequestParams.DIRECTION.getFileRequestParams(), DIRECTION_OUTBOUND);
        return getFileEntityList(requestUri, params);
    }

    /**
     * getFilesSharedWithMe
     * <p>
     * calls getFilesSharedWithMe(Map<String, String> params) with null params
     * 
     * @return FileList
     * @throws ClientServicesException
     */

    public EntityList<File> getFilesSharedWithMe() throws ClientServicesException {
        return getFilesSharedWithMe(null);
    }

    /**
     * getFilesSharedWithMe
     * <p>
     * Rest API used : /files/basic/api/documents/shared/feed <br>
     * This method is used to get Files Shared With the person.
     * 
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileList
     * @throws ClientServicesException
     */

    public EntityList<File> getFilesSharedWithMe(Map<String, String> params) throws ClientServicesException {
        if (null == params) {
            params = new HashMap<String, String>();
        }
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.DOCUMENTS_SHARED_FEED.format(this, FileUrlParts.accessType.get(accessType));
        params.put(FileRequestParams.DIRECTION.getFileRequestParams(), DIRECTION_INBOUND);
        return getFileEntityList(requestUri, params);
    }

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
        return getFileWithGivenVersion(fileId, versionId, null, null);
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
     * @return File
     * @throws ClientServicesException
     */
    public File getFileWithGivenVersion(String fileId, String versionId, Map<String, String> params)
            throws ClientServicesException {
        return getFileWithGivenVersion(fileId, versionId, params, null);
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
     * @return File
     * @throws ClientServicesException
     */
    public File getFileWithGivenVersion(String fileId, String versionId, Map<String, String> params,
            Map<String, String> headers) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.GET_FILE_WITH_GIVEN_VERSION.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId), FileUrlParts.versionId.get(versionId));
        return getFileEntity(requestUri, params);
    }

    public Comment getFlaggedComment(String commentId) throws ClientServicesException {
        String requestUri = getModerationUri(commentId, Categories.REVIEW.get(), ModerationContentTypes.COMMENT.get());
        return getCommentEntity(requestUri, null);
    }

    // /files/basic/api/review/comments
    public EntityList<Comment> getFlaggedComments(Map<String, String> params) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.GET_FLAGGED_COMMENTS.format(this, FileUrlParts.accessType.get(accessType));
        return getCommentEntityList(requestUri, params, null);
    }

    public File getFlaggedFile(String fileId) throws ClientServicesException {
        if (StringUtil.isEmpty(fileId)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        String requestUri = getModerationUri(fileId, Categories.REVIEW.get(), ModerationContentTypes.DOCUMENTS.get());
        return getFileEntity(requestUri, null);
    }

    // /files/basic/api/review/actions/documents/{document-id}
    public Document getFlaggedFileHistory(String fileId, Map<String, String> params)
            throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.GET_FLAGGED_FILE_HISTORY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        try {
            return (Document) retrieveData(requestUri, params).getData();
        } catch (IOException e) {
           throw new ClientServicesException(e, Messages.MessageExceptionInReadingObject);
        }

    }

    // /files/basic/api/review/documents
    public EntityList<File> getFlaggedFiles(Map<String, String> params) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.GET_FLAGGED_FILES.format(this, FileUrlParts.accessType.get(accessType));
        return getFileEntityList(requestUri, params);
    }

    /**
     * getFolder
     * <p>
     * retrieves the atom entry document of the folder, whose folder id is mentioned as input. <br>
     * Rest API used : /basic/api/collection/{collection-id}/entry
     * 
     * @param folderId
     * @return
     * @throws ClientServicesException
     */
    public File getFolder(String folderId) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.COLLECTION_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.folderId.get(folderId));
        return getFileEntity(requestUri, null);
    }

    public EntityList<File> getFoldersWithRecentlyAddedFiles() throws ClientServicesException {
        return getFoldersWithRecentlyAddedFiles(null);
    }

    /**
     * getFoldersWithRecentlyAddedFiles
     * <p>
     * Rest API used : /files/basic/api/collections/addedto/feed
     * 
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileList
     * @throws ClientServicesException
     */
    public EntityList<File> getFoldersWithRecentlyAddedFiles(Map<String, String> params)
            throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.GET_FOLDERS_WITH_RECENT_FILES.format(this, FileUrlParts.accessType.get(accessType));
        return getFileEntityList(requestUri, params);
    }

    /**
     * getMyFiles
     * <p>
     * calls getMyFiles(Map<String, String> params) internally with null parameters, if user has not specific
     * any params
     * 
     * @return FileList
     * @throws ClientServicesException
     */

    public EntityList<File> getMyFiles() throws ClientServicesException {
        return getMyFiles(null);
    }

    /**
     * getMyFiles
     * <p>
     * Rest API used : /files/basic/api/myuserlibrary/feed <br>
     * This method is used to get Files of the person.
     * 
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileList
     * @throws ClientServicesException
     */

    public EntityList<File> getMyFiles(Map<String, String> params) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYUSERLIBRARY_FEED.format(this, FileUrlParts.accessType.get(accessType));
        return getFileEntityList(requestUri, params);
    }

    public EntityList<File> getMyFolders() throws ClientServicesException {
        return getMyFolders(null);
    }

    /**
     * getMyFolders
     * <p>
     * Rest API used : /files/basic/api/collections/feed Required Parameters : creator={snx:userid}
     * 
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileList
     * @throws ClientServicesException
     */
    public EntityList<File> getMyFolders(Map<String, String> params) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.COLLECTIONS_FEED.format(this, FileUrlParts.accessType.get(accessType));
        return getFileEntityList(requestUri, params);
    }

    /**
     * getNonce
     * <p>
     * Returns the Cryptographic Key - Nonce value obtained from Connections Server <br>
     * Rest API used : /files/basic/api/nonce
     * 
     * @return String - nonce value
     * @throws ClientServicesException
     */
    public String getNonce() throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.GET_NONCE.format(this, FileUrlParts.accessType.get(accessType));
        Object result = null;
        result = getClientService().get(requestUri, null, ClientService.FORMAT_TEXT);
        if (result == null) {
        	return null;
        }
        return (String)((Response) result).getData();
    }

    public EntityList<File> getPinnedFiles() throws ClientServicesException {
        return getPinnedFiles(null);
    }

    /**
     * getPinnedFiles
     * <p>
     * Rest API used : /files/basic/api/myfavorites/documents/feed
     * 
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileList
     * @throws ClientServicesException
     */
    public EntityList<File> getPinnedFiles(Map<String, String> params) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYFAVORITES_DOCUMENTS_FEED.format(this, FileUrlParts.accessType.get(accessType));
        return getFileEntityList(requestUri, params);
    }

    public EntityList<File> getPinnedFolders() throws ClientServicesException {
        return getPinnedFolders(null);
    }

    /**
     * getMyPinnedFolders
     * <p>
     * Rest API used : /files/basic/api/myfavorites/collections/feed
     * 
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileList
     * @throws ClientServicesException
     */
    public EntityList<File> getPinnedFolders(Map<String, String> params) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYFAVORITES_COLLECTIONS_FEED.format(this, FileUrlParts.accessType.get(accessType));
        return getFileEntityList(requestUri, params);

    }

    public EntityList<File> getPublicFiles() throws ClientServicesException {
        return getPublicFiles(null);
    }

    /**
     * getPublicFiles
     * <p>
     * Rest API used : /files/basic/anonymous/api/documents/feed <br>
     * This method returns a list of Public Files.
     * 
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileList
     * @throws ClientServicesException
     */
    public EntityList<File> getPublicFiles(Map<String, String> params) throws ClientServicesException {
        String accessType = AccessType.PUBLIC.getText();
        String requestUri = FileUrls.GET_PUBLIC_FILES.format(this, FileUrlParts.accessType.get(accessType));
		params = (null == params)?new HashMap<String, String>():params;
        params.put(FileRequestParams.VISIBILITY.getFileRequestParams(), VISIBILITY_PUBLIC);
        return getFileEntityList(requestUri, params);
    }

    public EntityList<File> getPublicFolders() throws ClientServicesException {
        return getPublicFolders(null);
    }

    /**
     * getPublicFileFolders
     * <p>
     * Rest API used : /files/basic/anonymous/api/collections/feed <br>
     * Public method. No Auth required.
     * 
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @return FileList
     * @throws ClientServicesException
     */

    public EntityList<File> getPublicFolders(Map<String, String> params) throws ClientServicesException {
        String accessType = AccessType.PUBLIC.getText();
        String requestUri = FileUrls.COLLECTIONS_FEED.format(this, FileUrlParts.accessType.get(accessType));
        return getFileEntityList(requestUri, params);
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
        String requestUri = FileUrls.LOCK_FILE.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.LOCK.getFileRequestParams(), LOCKTYPE_HARD);
        try {
            createData(requestUri, params, null);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInLockingFile);
        }
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
        String requestUri = FileUrls.MYFAVORITES_DOCUMENTS_FEED.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.ITEMID.getFileRequestParams(), fileId);
        try {
            createData(requestUri, params, null);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInPinningFile);
        }
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
        pinFolder(folderId, null);
    }

    /**
     * pinFolder
     * <p>
     * To pin a folder.<br>
     * Rest API Used : /files/basic/api/myfavorites/collections/feed
     * 
     * @param folderId
     * @param params filesAddedNotification - String. Indicates whether the collection should be added to
     *            notification at the same time. Options are on and off. The default value is on.
     * @throws ClientServicesException
     */
    public void pinFolder(String folderId, Map<String, String> params) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYFAVORITES_COLLECTIONS_FEED.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.folderId.get(folderId));
		params = (null == params)?new HashMap<String, String>():params;
        params.put(FileRequestParams.ITEMID.getFileRequestParams(), folderId);
        try {
            createData(requestUri, params,  null);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInPinningFolder);
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
     * @throws ClientServicesException
     */
    public void removeFileFromFolder(String folderId, String fileId) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.COLLECTION_FEED.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.folderId.get(folderId));
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.ITEMID.getFileRequestParams(), fileId);
        try {
            deleteData(requestUri, params, null);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInDeletingFile);
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
     * @throws ClientServicesException
     */
    public File restoreFileFromRecycleBin(String fileId) throws ClientServicesException {
        return restoreFileFromRecycleBin(fileId, null);
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
     * @throws ClientServicesException
     */
    public File restoreFileFromRecycleBin(String fileId, String userId) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri;
        if (StringUtil.isEmpty(userId)){
        	requestUri = FileUrls.MYUSERLIBRARY_RECYCLEBIN_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        } else {
        	requestUri = FileUrls.USERLIBRARY_RECYCLEBIN_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId), FileUrlParts.fileId.get(fileId));
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.UNDELETE.getFileRequestParams(), TRUE);
        Map<String, String> headers = new HashMap<String, String>();
        try {
            Response data = (Response) updateData(requestUri, params, headers, null, null);
            return getFileFeedHandler().createEntity(data);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInRestoreFile);
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
        String requestUri = FileUrls.MYUSERLIBRARY_RECYCLEBIN_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
		params = (null == params)?new HashMap<String, String>():params;
        Object payload = constructPayloadForMultipleEntries(communityIds,
                FileRequestParams.ITEMID.getFileRequestParams(), CATEGORY_COMMUNITY);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        headers.put(Headers.ContentLanguage, Headers.UTF);
        try {
            createData(requestUri, params, headers, payload);
        } catch (IOException e) {
            throw new ClientServicesException(e, "Error sharing the file");
        }
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
        String requestUri = FileUrls.LOCK_FILE.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.LOCK.getFileRequestParams(), LOCKTYPE_NONE);
        try {
            createData(requestUri, params, null);
        } catch (IOException e) {
            throw new ClientServicesException(e, "Error unlocking the file");
        }
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
        String requestUri = FileUrls.MYFAVORITES_DOCUMENTS_FEED.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.ITEMID.getFileRequestParams(), fileId);
        try {
            deleteData(requestUri, params ,null);
        } catch (IOException e) {
            throw new ClientServicesException(e, "Error unpinning the file");
        }
    } 

    public void unPinFolder(String folderId) throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYFAVORITES_COLLECTIONS_FEED.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.folderId.get(folderId));
        Map<String, String> params = new HashMap<String, String>();
        params.put(FileRequestParams.ITEMID.getFileRequestParams(), folderId);
        try {
            deleteData(requestUri, params, null);
        } catch (IOException e) {
            throw new ClientServicesException(e, "Error unpinning the folder");
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
     * @throws ClientServicesException
     * @throws TransformerException 
     */
    public Comment updateComment(String fileId, String commentId, Map<String, String> params,
            String comment) throws ClientServicesException, TransformerException {
        return updateComment(fileId, commentId, comment, "", params);
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
        return updateComment(fileId, commentId, comment, "", null);
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
        return updateComment(fileId, commentId, comment, userId, null);
    }

    /**
     * updateComment
     * <p>
     * Rest API used : /files/basic/api/userlibrary/{userid}/document/{document-id}/comment/{comment-id}/entry
     * <br>
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
        if (StringUtil.isEmpty(userId)){
        	requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_COMMENT_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId), FileUrlParts.commentId.get(commentId));
        } else {
        	requestUri = FileUrls.USERLIBRARY_DOCUMENT_COMMENT_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.userId.get(userId), FileUrlParts.fileId.get(fileId), FileUrlParts.commentId.get(commentId));
        }
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        Object payload = constructPayloadForComments(comment);


        try {
            Response result = (Response) updateData(requestUri, params, headers, payload, null);
            return getCommentFeedHandler().createEntity(result);
        } catch (Exception e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInUpdatingComment);
        }
       
    }

    /**
     * Method to updateFile. This method should be used to upload new version of a file, and to simultaneously update file metadata.
     * <p>
     * Supported Parameters : 
     * @see http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.5+API+Documentation
     * 		#action=openDocument&res_title=Updating_a_file_ic45&content=pdcontent
     * @param iStream
     * @param fileId
     * @param title
     * @param params
     * @return File
     * @throws ClientServicesException
     */
    public File updateFile(InputStream iStream, String fileId, String title, Map<String, String> params) throws ClientServicesException {
    	File newVersionFile = uploadNewVersionFile(iStream, fileId, title, params);
    	try {
			return updateFileMetadata(newVersionFile, params);
		} catch (TransformerException e) {
			 throw new ClientServicesException(e, Messages.MessageExceptionInUpdate);
		}
    }
    
    /**
     * Method to update Community File. This method should be used to upload new version of a community file, and to simultaneously update the file's metadata.
     * Supported Parameters : 
     * @see http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.5+API+Documentation
     * 		#action=openDocument&res_title=Updating_a_file_ic45&content=pdcontent
     * @param iStream
     * @param fileId
     * @param title
     * @param params
     * @return File
     * @throws ClientServicesException
     */
    public File updateCommunityFile(java.io.InputStream iStream, String fileId, String title, String communityLibraryId, Map<String, String> params) throws ClientServicesException {
    	File newVersionFile = uploadNewVersionCommunityFile(iStream, fileId, title, communityLibraryId, params);
    	return updateCommunityFileMetadata(newVersionFile, communityLibraryId, params);
    }
    
    /**
     * Method to Upload new version of a File 
     * <p>
     * Supported parameters : 
     * @see http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.5+API+Documentation
     * 		#action=openDocument&res_title=Updating_a_file_ic45&content=pdcontent
     * @param content
     * @param fileId
     * @param length
     * @param fileName
     * @param params
     * @return File
     * @throws ClientServicesException
     */
    public File uploadNewVersionFile(java.io.InputStream iStream, String fileId, String title, Map<String, String> params)
            throws ClientServicesException {
        if (iStream == null) {
            throw new ClientServicesException(null, Messages.Invalid_Stream);
        }
        if (StringUtil.isEmpty(title)) {
            throw new ClientServicesException(null, Messages.Invalid_Name);
        }
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));

        Content contentFile = getContentObject(title, iStream);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(X_UPDATE_NONCE, getNonce()); // It is not clearly documented which Content Type requires Nonce, thus adding nonce in header for all upload requests. 
        try {
            //TODO: check get data wrapping
            Response result = (Response) updateData(requestUri, params, headers, contentFile, null);
            return getFileFeedHandler().createEntity(result);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInUpdate);
        }
    }
    
    /**
     * Update the specified file.
     * 
     * @param inputStream
     * @param file
     * @param params
     * @return
     * @throws ClientServicesException
     */
    public File updateFile(java.io.InputStream inputStream, File file, Map<String, String> params) throws ClientServicesException {
		String requestUrl = file.getEditMediaUrl(); 
    	
        Content contentFile = getContentObject(file.getTitle(), inputStream);
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(X_UPDATE_NONCE, getNonce()); // It is not clearly documented which Content Type requires Nonce, thus adding nonce in header for all upload requests. 
        
        try {
            Response result = (Response) updateData(requestUrl, params, headers, contentFile, null);
            return getFileFeedHandler().createEntity(result);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInUpdate);
        }
    }
    
    /**
     * Method to Upload new version of a Community File 
     * <p>
     * Supported parameters : 
     * @see http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.5+API+Documentation
     * 		#action=openDocument&res_title=Updating_a_file_ic45&content=pdcontent
     * @param iStream
     * @param fileId
     * @param length
     * @param title
     * @param communityId
     * @param params
     * @return File
     * @throws ClientServicesException
     */
    public File uploadNewVersionCommunityFile(java.io.InputStream iStream, String fileId, String title, String communityId, Map<String, String> params)
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
        String requestUri = FileUrls.UPLOAD_NEW_VERSION_COMMUNITY_FILE.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.communityId.get(communityId), FileUrlParts.fileId.get(fileId));
		ContentStream contentFile = (ContentStream) getContentObject(title, iStream);
		Map<String, String> headers = new HashMap<String, String>();
        headers.put(X_UPDATE_NONCE, getNonce()); // It is not clearly documented which Content Type requires Nonce, thus adding nonce in header for all upload requests. 
        try {
            return uploadNewVersion(requestUri, contentFile, params, headers);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInUpload);
        }
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
    private File uploadNewVersion(String requestUri, ContentStream stream, Map<String, String> params, Map<String, String> headers) throws ClientServicesException, IOException {
    	//TODO: check get data wrapping
        Response result = (Response) updateData(requestUri, params, headers, stream, null);
        return getFileFeedHandler().createEntity(result);
    }
    
    /**
	 * Method to upload a File to Community
	 * @param iStream
	 * @param communityId
	 * @param title
	 * @param length
	 * @return File
	 * @throws ClientServicesException
	 */
	public File uploadCommunityFile(InputStream iStream, String communityId, final String title, long length) throws ClientServicesException {
		if (iStream == null) {
            throw new ClientServicesException(null, "null stream");
        }
        if (title == null) {
            throw new ClientServicesException(null, "null name");
        }
		String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.COMMUNITYLIBRARY_FEED.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.communityId.get(communityId));
        Content contentFile = getContentObject(title, iStream, length);
		Map<String, String> headers = new HashMap<String, String>();
        headers.put(X_UPDATE_NONCE, getNonce()); // It is not clearly documented which Content Type requires Nonce, thus adding nonce in header for all upload requests. 
	    try {
	    	Response data = (Response) createData(requestUri, null, headers, contentFile);
	    	return getFileFeedHandler().createEntity(data);
		} catch (IOException e) {
			throw new ClientServicesException(e, Messages.MessageExceptionInUpload);
		}
	}
    
    /**
     * updateFileMetadata
     * 
     * @param File
     * @param params
     * @param requestBody
     * @return File
     * @throws ClientServicesException
     */
    public File updateFileMetadata(File fileEntry, Map<String, String> params, Document requestBody) throws ClientServicesException {
        if (fileEntry == null) {
            throw new ClientServicesException(null, Messages.Invalid_File);
        }
        if (StringUtil.isEmpty(fileEntry.getFileId())) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        return updateFileMetadata(fileEntry.getFileId(), params, requestBody);
    }

    /**
     * updateFileMetadata
     * 
     * @param File
     * @param params
     * @param payloadMap
     * @return
     * @throws ClientServicesException
     * @throws TransformerException 
     */
    public File updateFileMetadata(File fileEntry, Map<String, String> params) throws ClientServicesException, TransformerException {
        if (fileEntry == null) {
            throw new ClientServicesException(null, Messages.Invalid_FileEntry);
        }
        if (StringUtil.isEmpty(fileEntry.getFileId())) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        return updateFileMetadata(fileEntry.getFileId(), params, fileEntry.getFieldsMap());
    }

    /**
     * updateFileMetadata
     * 
     * @param fileId
     * @param updationsMap a Map of updations which need to be done to the file.
     * @return
     * @throws ClientServicesException
     * @throws TransformerException 
     */
    public File updateFileMetadata(String fileId, Map<String, String> updationsMap)
            throws ClientServicesException, TransformerException {
        Map<String, Object> payloadMap = new HashMap<String, Object>();
        Map<String, String> paramsMap = new HashMap<String, String>();
        parseUpdationsMap(updationsMap, payloadMap, paramsMap);
        return updateFileMetadata(fileId, paramsMap, payloadMap);
    }

    /**
     * updateFileMetadata
     * <p>
     * This method is used to update the metadata/content of File in Connections. <br>
     * Rest API used : /files/basic/api/myuserlibrary/document/{document-id}/entry. <br>
     * User should get the specific file before calling this API, by using getFile method.
     * 
     * @param FileId - pass the fileID of the file to be updated
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.possible values.
     * @param requestBody - Document which is passed directly as requestBody to the execute request. This
     *            method is used to update the metadata/content of File in Connections.
     * @return File
     * @throws ClientServicesException
     */
    public File updateFileMetadata(String fileId, Map<String, String> params, Document requestBody)
            throws ClientServicesException {
        String accessType = AccessType.AUTHENTICATED.getText();
        if (StringUtil.isEmpty(fileId)) {
            return new File();
        }
        String requestUri = FileUrls.MYUSERLIBRARY_DOCUMENT_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.fileId.get(fileId));
        try {
            Response result = (Response) updateData(requestUri, params, new ClientService.ContentXml(
                    requestBody, APPLICATION_ATOM_XML), null);
            return getFileFeedHandler().createEntity(result);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInRestoreFile);
        }
    }

    /**
     * updateFileMetadata
     * <p>
     * This method is used to update the metadata/content of File in Connections. <br>
     * Rest API used : /files/basic/api/myuserlibrary/document/{document-id}/entry. <br>
     * User should get the specific file before calling this API, by using getFile method.
     * 
     * @param FileId - pass the fileID of the file to be updated
     * @param params - Map of Parameters. See {@link FileRequestParams} for possible values.
     * @param payloadMap - Map of entries for which we will construct a Request Body. See
     *            {@link FileRequestPayload} for possible values.
     * @return File
     * @throws ClientServicesException
     * @throws TransformerException 
     */
    public File updateFileMetadata(String fileId, Map<String, String> params,
            Map<String, Object> payloadMap) throws ClientServicesException, TransformerException {
        if (StringUtil.isEmpty(fileId)) {
            return new File();
        }
        Document updateFilePayload = null;
        updateFilePayload = constructPayload(fileId, payloadMap);
        return updateFileMetadata(fileId, params, updateFilePayload);
    }
    
    // Need to figure out what should be done with the label updation of comment. Connection Doc states that
    // comment updations here can be done on comment content and on label. But what is the label of the
    // comment ? Need to check this.
    public void updateFlaggedComment(String commentId, String updatedComment) throws ClientServicesException, TransformerException {
        if (StringUtil.isEmpty(commentId)) {
            throw new ClientServicesException(null, Messages.Invalid_CommentId);
        }
        String requestUri = getModerationUri(commentId, Categories.REVIEW.get(), ModerationContentTypes.COMMENT.get());
        // File File = (File) executeGet(requestUri, null, ClientService.FORMAT_XML,
        // null).get(0);

        // Map<String, String> payloadMap = new HashMap<String, String>();
        // Map<String, String> paramsMap = new HashMap<String, String>();
        Map<String, String> headers = new HashMap<String, String>();
        // parseUpdationsMap(updationsMap, payloadMap, paramsMap);
        // if (payloadMap != null && !payloadMap.isEmpty()) {
        headers.put(Headers.ContentType, Headers.ATOM);
        // }
        Document payload = constructPayloadForComments(updatedComment); // TODO
        try {
            updateData(requestUri, null, headers, payload, null);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInRestoreFile);
        }
    }

    public void updateFlaggedFile(String fileId, Map<String, String> updationsMap/* to title, tag and content */)
            throws ClientServicesException, TransformerException {
        if (StringUtil.isEmpty(fileId)) {
            throw new ClientServicesException(null, Messages.Invalid_FileId);
        }
        String requestUri = getModerationUri(fileId, Categories.REVIEW.get(), ModerationContentTypes.DOCUMENTS.get());
        // File File = (File) executeGet(requestUri, null, ClientService.FORMAT_XML,
        // null).get(0);

        Map<String, Object> payloadMap = new HashMap<String, Object>();
        Map<String, String> paramsMap = new HashMap<String, String>();
        Map<String, String> headers = new HashMap<String, String>();
        parseUpdationsMap(updationsMap, payloadMap, paramsMap);
        if (payloadMap != null && !payloadMap.isEmpty()) {
            headers.put(Headers.ContentType, Headers.ATOM);
        }
        Document payload = constructPayload(fileId, payloadMap);

        try {
            updateData(requestUri, null, headers, payload, null);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInRestoreFile);
        }
    }

    public File updateFolder(String folderId, String name, String description, String shareWith)
            throws ClientServicesException, TransformerException {
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.COLLECTION_ENTRY.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.folderId.get(folderId));
        Document payload = constructPayloadFolder(name, description, shareWith, "update");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Headers.ContentType, Headers.ATOM);
        
        try {
            Response result = (Response) updateData(requestUri, null, headers, payload, null);
            return getFileFeedHandler().createEntity(result);
        } catch (IOException e) {
            throw new ClientServicesException(e, Messages.MessageExceptionInRestoreFile);
        }
        
    }

    /**
     * Upload a new file; cannot be used to update an existing file
     * <p>
     * Rest API Used : /files/basic/api/myuserlibrary/feed <br>
     * 
     * @param file - a readable file on the server
     * @return File
     * @throws ClientServicesException
     */
    public File uploadFile(java.io.File file) throws ClientServicesException {
        return uploadFile(file, null);
    }

    /**
     * Upload a new file; cannot be used to update an existing file
     * <p>
     * Rest API Used : /files/basic/api/myuserlibrary/feed <br>
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
            return uploadFile(new FileInputStream(file), file.getName(), file.length(), parameters);
        } catch (FileNotFoundException e) {
            throw new ClientServicesException(null, Messages.MessageCannotReadFile,
                    file.getAbsolutePath());
        }
    }

    /**
     * Upload a new file; cannot be used to update an existing file
     * <p>
     * Rest API Used : /files/basic/api/myuserlibrary/feed <br>
     * @param stream
     * @param title
     * @param length
     * @return File
     * @throws ClientServicesException
     */
    public File uploadFile(java.io.InputStream stream, final String title, long length) throws ClientServicesException {
    	return uploadFile(stream, title, length, null);
    }
    
    /**
     * Upload a new file; cannot be used to update an existing file
     * <p>
     * Rest API Used : /files/basic/api/myuserlibrary/feed <br>
     * 
     * @param stream
     * @param title
     * @param length
     * @param p - parameters
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
        Content contentFile = getContentObject(title, stream, length);
        String accessType = AccessType.AUTHENTICATED.getText();
        String requestUri = FileUrls.MYUSERLIBRARY_FEED.format(this, FileUrlParts.accessType.get(accessType));
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(X_UPDATE_NONCE, getNonce()); // It is not clearly documented which Content Type requires Nonce, thus adding nonce in header for all upload requests. 
        try {
            Response data = (Response) createData(requestUri, p, headers, contentFile);
            if (logger.isLoggable(Level.FINEST)) {
    			logger.exiting(sourceClass, "uploadFile", data);
    		}
           
            return getFileFeedHandler().createEntity(data);
        } catch (IOException e) {
        	if (logger.isLoggable(Level.FINE)) {
        		String msg = MessageFormat.format("Error uploading file {0} length {1}", title, length);
        		logger.log(Level.FINE, msg, e);
        	}
            throw new ClientServicesException(e, Messages.MessageExceptionInUpload);
        }
    }

    /**
     * Method to get the MIME type of the file to be uploaded, from the extension of the file
     * @param title
     * @return String 
     */
    private String getMimeType(String title) {
    	return MIME.getMIMETypeFromExtension(MIME.getFileExtension(title));
    }
    
    /**
     * Method to create the content object based on the mime type. 
     * Content type is set using the getMimeType method. In case the mime type cannot be found from the extension, 
     * then default mime type is set by ClientService , which is "binary/octet-stream"
     * @param title
     * @param stream
     * @param length
     * @return Content
     */
    private Content getContentObject(String title, InputStream stream, long length) {
    	Content contentFile;
		if(StringUtil.isNotEmpty(getMimeType(title))) {
			contentFile = new ContentStream(title, stream, length, getMimeType(title));
		} else {
			contentFile = new ContentStream(stream, length, title);
		}
		return contentFile; 
    }
    
    /**
     * Method to create the content object based on the mime type. 
     * Content type is set using the getMimeType method. In case the mime type cannot be found from the extension, 
     * then default mime type is set by ClientService , which is "binary/octet-stream"
     * @param title
     * @param stream
     * @return Content
     */
    private Content getContentObject(String title, InputStream stream) {
    	return getContentObject(title, stream, -1);
    }
    
    /**
     * constructPayload
     * <p>
     * This method constructs the Atom entry document for the APIs requiring payload input. Currently this
     * method constructs payload for updating Label, Summary, Visibility, Title of the file.
     * 
     * @param File - pass the File object to be updated
     * @param payloadMap - Map of entries for which we will construct a Request Body. See
     *            {@link FileRequestPayload} for possible values.
     * @return Document
     * @throws TransformerException 
     */

    private Document constructPayload(String fileId, Map<String, Object> payloadMap) throws TransformerException {
    	payloadMap.put("category", "document");
    	payloadMap.put("id", fileId);
    	FileTransformer fileTransformer = new FileTransformer();
    	String requestBody = fileTransformer.transform(payloadMap);
    	return convertToXML(requestBody);
    }

    /**
     * constructPayloadFolder
     * 
     * @param name
     * @param description
     * @param shareWith
     * @param operation
     * @return
     * @throws TransformerException 
     */
    private Document constructPayloadFolder(String name, String description, String shareWith,
            String operation) throws TransformerException {
        return constructPayloadFolder(name, description, shareWith, operation, null);
    }

    private Document constructPayloadFolder(String name, String description, String shareWith,
            String operation, String entityId) throws TransformerException {
    	
    	Map<String, Object> fieldsMap = new HashMap<String, Object>();
    	fieldsMap.put(CATEGORY, CATEGORY_COLLECTION);
    	if (!StringUtil.isEmpty(operation)) {
    		if (operation.equals("update")) { 
    			fieldsMap.put("id", entityId);
    		}
    	}
    	fieldsMap.put("label", name);
    	fieldsMap.put("title", name);
    	fieldsMap.put("summary", description);
    	if (StringUtil.isEmpty(shareWith) || StringUtil.equalsIgnoreCase(shareWith, "null")) {
          fieldsMap.put(VISIBILITY, VISIBILITY_PRIVATE);
      } else {
    	  fieldsMap.put(VISIBILITY, VISIBILITY_PRIVATE);
          String parts[] = shareWith.split(",");
          if ((parts.length) != 3) {
              return null;
          } else {
        	  fieldsMap.put("shareWithId", parts[0]);
        	  fieldsMap.put("shareWithWhat", parts[1]);
        	  fieldsMap.put("shareWithRole", parts[2]);
          }
      }
    	FolderTransformer folderTransformer = new FolderTransformer();
    	String payload = folderTransformer.transform(fieldsMap); 
    	return convertToXML(payload.toString());    	
    }

    /**
     * constructPayloadForComments
     * 
     * @param comment - comment for which a payload Document needs to be constructed.
     * @return Document - payload Document which is sent as part of the request body.
     * @throws TransformerException 
     */

    private Document constructPayloadForComments(String comment) throws TransformerException {
        return constructPayloadForComments(null, comment);
    }

    /**
     * constructPayloadForComments
     * 
     * @param operation - used to determine whether we need the payload for adding / deleting / updating a
     *            comment.
     * @param commentToBeAdded - plaintext comment which needs to be added to the File.
     * @return Document - payload Document which is sent as part of the request body.
     * @throws TransformerException 
     */

    private Document constructPayloadForComments(String operation, String commentToBeAdded) throws TransformerException {
    	Map<String, Object> fieldsMap = new HashMap<String, Object>();
    	fieldsMap.put("category", CATEGORY_COMMENT);
    	if (!StringUtil.isEmpty(operation) && !operation.equals("delete")) {
          fieldsMap.put("deleteWithRecord", "false");
    	} else {
    		fieldsMap.put("content", commentToBeAdded);
    	}
    	CommentTransformer commentTransformer = new CommentTransformer();
    	String payload = commentTransformer.transform(fieldsMap);
    	return convertToXML(payload.toString());
    }

    /**
     * constructPayloadForFlagging
     * 
     * @param fileId
     * @param flagReason
     * @param flagWhat
     * @return
     * @throws TransformerException 
     */
    private Object constructPayloadForFlagging(String fileId, String content, String entity) throws TransformerException {
    	if (entity.equalsIgnoreCase("file")) {
            entity = "document";
        }
    	Map<String, Object> fieldsMap = new HashMap<String, Object>();
    	fieldsMap.put("entity", entity);
    	fieldsMap.put("fileId", fileId);
    	fieldsMap.put("content", content);
    	
    	ModerationTransformer moderationTransformer = new ModerationTransformer();
    	String payload = moderationTransformer.transform(fieldsMap);
        return convertToXML(payload.toString());
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
        return convertToXML(payload.toString());
    }

    private Document constructPayloadForMultipleEntries(List<String> listOfFileIds, String multipleEntryId) throws TransformerException {
        return constructPayloadForMultipleEntries(listOfFileIds, multipleEntryId, null);
    }

    private Document constructPayloadForMultipleEntries(List<String> listOfIds, String multipleEntryId,
            String category) throws TransformerException {
        
    	MultipleFileTransformer mfTransformer = new MultipleFileTransformer();
    	String payload = mfTransformer.transform(listOfIds, category);
    	return convertToXML(payload.toString());
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
  
    private String getModerationUri(String contentId, String action, String content) throws ClientServicesException {
        FilesModerationDocumentEntry fileModDocEntry = getFilesModerationDocumentEntry();
        // get the request URI from the service document.
        String requestUri = fileModDocEntry.get(ContentMapFiles.moderationMap.get("getFileUrl")); // TODO
        // If not obtained due to some reason, then construct the url in regular fashion.
        // URI : /files/basic/api/approval/documents
        if (StringUtil.isEmpty(requestUri)) {
            String accessType = AccessType.AUTHENTICATED.getText();
            requestUri = FileUrls.MODERATION.format(this, FileUrlParts.accessType.get(accessType), FileUrlParts.action.get(action), FileUrlParts.contentType.get(content));
        }
        FileList resultantEntries;
        try {
            resultantEntries = (FileList) getEntities(requestUri, null, getFileFeedHandler());
        } catch (IOException e) {
           throw new ClientServicesException(e, Messages.MessageExceptionInReadingObject);
        } 
       
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

    private void parseUpdationsMap(Map<String, String> updationsMap, Map<String, Object> payloadMap,
            Map<String, String> paramsMap) {
        // here parse the entries in the updations map and create paramsMap, payloadMap appropriately.
        Iterator<Map.Entry<String, String>> entries = updationsMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> fieldMapPairs = entries.next();
            String key = fieldMapPairs.getKey();
            if (fileRequestParamsContains(key)) {
                paramsMap.put(key, fieldMapPairs.getValue());
            } else if (filePayloadParamsContains(key)) {
                payloadMap.put(key, fieldMapPairs.getValue());
            } else {
                // these parameters currently will get ignored. check this .. TODO
            }
        }
    }

    public void addFileToFolder(String fileId, String folderId) throws ClientServicesException, TransformerException {
        List<String> c = Arrays.asList(new String[] { folderId });
        addFileToFolders(fileId, c);
    }

	/***************************************************************
	 * Factory methods
	 ****************************************************************/

	protected File getFileEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		try {
			return getEntity(requestUrl, parameters, getFileFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}

	protected EntityList<File> getFileEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		try {
			return getEntities(requestUrl, parameters, getFileFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}

	protected Comment getCommentEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		try {
            if (parameters == null)
                parameters = getCommentParams();
            else
                parameters.putAll(getCommentParams());
			return getEntity(requestUrl, parameters, getCommentFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}

	protected EntityList<Comment> getCommentEntityList(String requestUrl, Map<String, String> parameters, Map<String,String> headers) throws ClientServicesException {
		try {
			return getEntities(requestUrl, parameters, headers, getCommentFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
}
