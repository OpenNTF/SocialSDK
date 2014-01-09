/*
 * © Copyright IBM Corp. 2013
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

import java.io.IOException;
import java.util.Map;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.connections.cmisfiles.feedHandler.CMISFileFeedHandler;
import com.ibm.sbt.services.endpoints.ConnectionsBasicEndpoint;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.SmartCloudBasicEndpoint;

/**
 * CMISFileService can be used to expose file application data using the Content Management Interoperability Services (CMIS)
 * 
 * @Represents CMIS FileService
 * @author Vimal Dhupar
 * @see http://www-10.lotus.com/ldd/appdevwiki.nsf/xpAPIViewer.xsp?lookupName=API+Reference#action=openDocument&res_title=Files_CMIS_API_sbar&content=apicontent
 */
public class CMISFileService extends BaseService {
	
	String repositoryId; 

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
     * Method to get the Default endpoint to be used with the service
     * @return
     */
    private static String getDefaultEndpoint() {
		return "connections";
	}
    
    private String getRepositoryId() throws ClientServicesException {
    	if(StringUtil.isNotEmpty(repositoryId)){
    		return repositoryId;
		} else {
	    	if(!this.endpoint.getClientParams().containsKey("isSmartCloud")) { 
	    		// fetch the connections userid here 
	    		repositoryId = getRepositoryId(CMISFilesUrlBuilder.ATOM_GET_USER_ID.getUrl());
	    	} else if(this.endpoint.getClientParams().get("isSmartCloud").equals(Boolean.TRUE)) {  
	    		// fetch the smartcloud subscriber id here 
	    		repositoryId = getRepositoryId(CMISFilesUrlBuilder.ATOM_GET_SUBSCRIBER_ID.getUrl());
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
	
	/**
	 * Method to fetch the list of files.
	 * @return CMISFileList
	 * @throws CMISFileServiceException
	 */
	public CMISFileList getMyFiles() throws CMISFileServiceException {
		return getMyFiles(null);
	}
	
	/**
	 * Method to fetch the list of files.
	 * @param parameters
	 * @return CMISFileList
	 * @throws CMISFileServiceException
	 */
	public CMISFileList getMyFiles(Map<String, String> parameters) throws CMISFileServiceException {
		try {
			getRepositoryId();
		} catch(ClientServicesException cse) {
			throw new CMISFileServiceException(cse, "Error : Failed to fetch Repository ID");
		}
		String requestUrl = CMISFilesUrlBuilder.populateURL(CMISFilesUrlBuilder.GET_MY_FILES.getUrl(), repositoryId);
		try {
			return (CMISFileList) this.getEntities(requestUrl, parameters, new CMISFileFeedHandler(this));
		} catch (ClientServicesException cse) {
			throw new CMISFileServiceException(cse, "Error : Failed to fetch My Files List");
		} catch (IOException ioe) {
			throw new CMISFileServiceException(ioe, "Error : Failed to fetch My Files List");
		}
	}
	
	/**
	 * Method to fetch the list of files shared with the User.
	 * @return CMISFileList
	 * @throws CMISFileServiceException
	 */
	public CMISFileList getFilesSharedWithMe() throws CMISFileServiceException {
		return getFilesSharedWithMe(null);
	}
	
	/**
	 * Method to fetch the list of files shared with the User.
	 * @param parameters
	 * @return CMISFileList
	 * @throws CMISFileServiceException
	 */
	public CMISFileList getFilesSharedWithMe(Map<String, String> parameters) throws CMISFileServiceException {
		try {
			getRepositoryId();
		} catch(ClientServicesException cse) {
			throw new CMISFileServiceException(cse, "Error : Failed to fetch Repository ID");
		}
		String requestUrl = CMISFilesUrlBuilder.populateURL(CMISFilesUrlBuilder.GET_FILES_SHARED_WITH_ME.getUrl(), repositoryId);
		try {
			return (CMISFileList) this.getEntities(requestUrl, parameters, new CMISFileFeedHandler(this));
		} catch (ClientServicesException cse) {
			throw new CMISFileServiceException(cse, "Error : Failed to fetch list of Files shared with the user.");
		} catch (IOException ioe) {
			throw new CMISFileServiceException(ioe, "Error : Failed to fetch list of Files shared with the user.");
		}
	}
	
	/**
	 * Method to fetch the list of Collections.
	 * @return CMISFileList
	 * @throws CMISFileServiceException
	 */
	public CMISFileList getMyCollections() throws CMISFileServiceException {
		return getMyCollections(null);
	}
	
	/**
	 * Method to fetch the list of Collections.
	 * @param parameters
	 * @return CMISFileList
	 * @throws CMISFileServiceException
	 */
	public CMISFileList getMyCollections(Map<String, String> parameters) throws CMISFileServiceException {
		try {
			getRepositoryId();
		} catch(ClientServicesException cse) {
			throw new CMISFileServiceException(cse, "Error : Failed to fetch Repository ID");
		}
		String requestUrl = CMISFilesUrlBuilder.populateURL(CMISFilesUrlBuilder.GET_MY_COLLECTIONS.getUrl(), repositoryId);
		try {
			return (CMISFileList) this.getEntities(requestUrl, parameters, new CMISFileFeedHandler(this));
		} catch (ClientServicesException cse) {
			throw new CMISFileServiceException(cse, "Error : Failed to fetch list of Collections.");
		} catch (IOException ioe) {
			throw new CMISFileServiceException(ioe, "Error : Failed to fetch list of Collections.");
		}
	}
	
	/**
	 * Method to fetch the list of Collections shared with the User.
	 * @return CMISFileList
	 * @throws CMISFileServiceException
	 */
	public CMISFileList getCollectionsSharedWithMe() throws CMISFileServiceException {
		return getCollectionsSharedWithMe(null);
	}
	
	/**
	 * Method to fetch the list of Collections shared with the User.
	 * @param parameters
	 * @return CMISFileList
	 * @throws CMISFileServiceException
	 */
	public CMISFileList getCollectionsSharedWithMe(Map<String, String> parameters) throws CMISFileServiceException {
		try {
			getRepositoryId();
		} catch(ClientServicesException cse) {
			throw new CMISFileServiceException(cse, "Error : Failed to fetch Repository ID");
		}
		String requestUrl = CMISFilesUrlBuilder.populateURL(CMISFilesUrlBuilder.GET_COLLECTIONS_SHARED_WITH_ME.getUrl(), repositoryId);
		try {
			return (CMISFileList) this.getEntities(requestUrl, parameters, new CMISFileFeedHandler(this));
		} catch (ClientServicesException cse) {
			throw new CMISFileServiceException(cse, "Error : Failed to fetch list of Collections shared with the User.");
		} catch (IOException ioe) {
			throw new CMISFileServiceException(ioe, "Error : Failed to fetch list of Collections shared with the User.");
		}
	}
	
	/**
	 * Method to fetch the list of User's shares.
	 * @return CMISFileList
	 * @throws CMISFileServiceException
	 */
	public CMISFileList getMyShares() throws CMISFileServiceException {
		return getMyShares(null);
	}
	
	/**
	 * Method to fetch the list of User's shares.
	 * @param parameters
	 * @return CMISFileList
	 * @throws CMISFileServiceException
	 */
	public CMISFileList getMyShares(Map<String, String> parameters) throws CMISFileServiceException {
		try {
			getRepositoryId();
		} catch(ClientServicesException cse) {
			throw new CMISFileServiceException(cse, "Error : Failed to fetch Repository ID");
		}
		String requestUrl = CMISFilesUrlBuilder.populateURL(CMISFilesUrlBuilder.GET_MY_SHARES.getUrl(), repositoryId);
		try {
			return (CMISFileList) this.getEntities(requestUrl, parameters, new CMISFileFeedHandler(this));
		} catch (ClientServicesException cse) {
			throw new CMISFileServiceException(cse, "Error : Failed to fetch list of User's Shares.");
		} catch (IOException ioe) {
			throw new CMISFileServiceException(ioe, "Error : Failed to fetch list of User's Shares.");
		}
	}
}