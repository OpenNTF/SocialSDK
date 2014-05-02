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

package com.ibm.sbt.services.client.connections.cmisfiles;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import java.util.Map;

import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomFeedHandler;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsService;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * CMISFileService can be used to expose file application data using the Content Management Interoperability Services (CMIS)
 * 
 * @Represents CMIS FileService
 * @author Vimal Dhupar
 * @see http://www-10.lotus.com/ldd/appdevwiki.nsf/xpAPIViewer.xsp?lookupName=API+Reference#action=openDocument&res_title=Files_CMIS_API_sbar&content=apicontent
 */
public class CMISFileService extends ConnectionsService {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -337041873140937439L;
	private String repositoryId;

    /**
     * Constructs CMISFileService Object
     */
    public CMISFileService() {
        this(getDefaultEndpoint());
    }

	/**
     * Constructs CMIS File service object with specified endpoint name
     * @param endpoint
     */
    public CMISFileService(String endpoint) {
        super(endpoint);
    }
    
	/**
     * Constructs CMIS File service object with specified endpoint
     * @param endpoint
     */
    public CMISFileService(Endpoint endpoint) {
        super(endpoint);
    }

	/**
	 * Return mapping key for this service
	 */
	@Override
	public String getServiceMappingKey() {
		return "files";
	}
	
	//------------------------------------------------------------------------------------------------------------------
	// Getting CMIS File feeds
	//------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Gets a feed that lists the files in your library.
	 * 
	 * @return EntityList<CMISFile>
	 * @throws ClientServicesException
	 */
	public EntityList<CMISFile> getMyFiles() throws ClientServicesException {
		return getMyFiles(null);
	}
	
	/**
	 * Gets a feed that lists the files in your library.
	 * 
	 * @param parameters
	 * @return EntityList<CMISFile>
	 * @throws ClientServicesException
	 */
	public EntityList<CMISFile> getMyFiles(Map<String, String> parameters) throws ClientServicesException {
		getRepositoryId();
	    String url = CMISFilesUrls.GET_MY_FILES.format(this, CMISFilesUrlParts.repositoryId.get(repositoryId));
	    return getFileEntityList(url, parameters);
	}
	
	/**
	 * Retrieves an Atom document representation of a file shared with the user.
	 * 
	 * @return EntityList<CMISFile>
	 * @throws ClientServicesException
	 */
	public EntityList<CMISFile> getFilesSharedWithMe() throws ClientServicesException {
		return getFilesSharedWithMe(null);
	}
	
	/**
	 * Retrieves an Atom document representation of a file shared with the user.
	 * 
	 * @param parameters
	 * @return EntityList<CMISFile>
	 * @throws ClientServicesException
	 */
	public EntityList<CMISFile> getFilesSharedWithMe(Map<String, String> parameters) throws ClientServicesException {
		getRepositoryId();
	    String url = CMISFilesUrls.GET_FILES_SHARED_WITH_ME.format(this, CMISFilesUrlParts.repositoryId.get(repositoryId));
	    return getFileEntityList(url, parameters);
	}
	
	/**
	 * Retrieves an Atom document representation of the user's file collection.
	 * 
	 * @return EntityList<CMISFile>
	 * @throws ClientServicesException
	 */
	public EntityList<CMISFile> getMyCollections() throws ClientServicesException {
		return getMyCollections(null);
	}
	
	/**
	 * Retrieves an Atom document representation of the user's file collection.
	 * 
	 * @param parameters
	 * @return EntityList<CMISFile>
	 * @throws ClientServicesException
	 */
	public EntityList<CMISFile> getMyCollections(Map<String, String> parameters) throws ClientServicesException {
		getRepositoryId();
	    String url = CMISFilesUrls.GET_MY_COLLECTIONS.format(this, CMISFilesUrlParts.repositoryId.get(repositoryId));
	    return getFileEntityList(url, parameters);
	}
	
	/**
	 * Retrieves an Atom document representation of a file collection shared with the user.
	 * 
	 * @return EntityList<CMISFile>
	 * @throws CMISFileServiceException
	 */
	public EntityList<CMISFile> getCollectionsSharedWithMe() throws ClientServicesException {
		return getCollectionsSharedWithMe(null);
	}
	
	/**
	 * Retrieves an Atom document representation of a file collection shared with the user.
	 * 
	 * @param parameters
	 * @return EntityList<CMISFile>
	 * @throws ClientServicesException
	 */
	public EntityList<CMISFile> getCollectionsSharedWithMe(Map<String, String> parameters) throws ClientServicesException {
		getRepositoryId();
	    String url = CMISFilesUrls.GET_COLLECTIONS_SHARED_WITH_ME.format(this, CMISFilesUrlParts.repositoryId.get(repositoryId));
	    return getFileEntityList(url, parameters);
	}
	
	/**
	 * Retrieves an Atom document representation of a file shared by the user.
	 * 
	 * @return EntityList<CMISFile>
	 * @throws CMISFileServiceException
	 */
	public EntityList<CMISFile> getMyShares() throws ClientServicesException {
		return getMyShares(null);
	}
	
	/**
	 * Retrieves an Atom document representation of a file shared by the user.
	 * 
	 * @param parameters
	 * @return EntityList<CMISFile>
	 * @throws ClientServicesException
	 */
	public EntityList<CMISFile> getMyShares(Map<String, String> parameters) throws ClientServicesException {
		getRepositoryId();
		String url = CMISFilesUrls.GET_MY_SHARES.format(this, CMISFilesUrlParts.repositoryId.get(repositoryId));
		return getFileEntityList(url, parameters);
	}
	
	/***************************************************************
	 * FeedHandlers for each entity type
	 ****************************************************************/

	/**
	 * Factory method to instantiate a FeedHandler for Profiles
	 * @return IFeedHandler<Profile>
	 */
	public IFeedHandler<CMISFile> getFileFeedHandler() {
		return new AtomFeedHandler<CMISFile>(this, false) {
			@Override
			protected CMISFile entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new CMISFile(service, node, nameSpaceCtx, xpath);
			}
		};
	}
	
	/***************************************************************
	 * Factory methods
	 ****************************************************************/
	protected EntityList<CMISFile> getFileEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getFileFeedHandler());
	}
	
    /**
     * Method to get the Default endpoint to be used with the service
     * @return
     */
    protected static String getDefaultEndpoint() {
		return "connections";
	}
    
    private String getRepositoryId() throws ClientServicesException {
    	if(StringUtil.isNotEmpty(repositoryId)){
    		return repositoryId;
    	} else {
		    if(!this.endpoint.getClientParams().containsKey("isSmartCloud")) { 
		    	// fetch the connections userid here 
		    	repositoryId = getRepositoryId(CMISFilesUrls.ATOM_GET_USER_ID.format(this));
		    } else if(this.endpoint.getClientParams().get("isSmartCloud").equals(Boolean.TRUE)) {  
		    	// fetch the smartcloud subscriber id here 
		    	repositoryId = getRepositoryId(CMISFilesUrls.ATOM_GET_SUBSCRIBER_ID.format(this));
		    }
    	}
    	return repositoryId;
    }
    
	private String getRepositoryId(String repositoryUrl) throws ClientServicesException {
		Response response = endpoint.getClientService().get(repositoryUrl, ClientService.FORMAT_JSON);
		JsonObject result = (JsonObject) response.getData() ;
		JsonObject entry = (JsonObject) result.getJsonProperty("entry");
		if(entry != null) {
			String repId = (String) ((JsonObject)result.getJsonProperty("entry")).getJsonProperty("id");
			if(StringUtil.isNotEmpty(repId)) {
				repId = "p!" + repId.substring("urn:lsid:lconn.ibm.com:profiles.person:".length());
			}
			return repId;
		}
		return null;
	}
}