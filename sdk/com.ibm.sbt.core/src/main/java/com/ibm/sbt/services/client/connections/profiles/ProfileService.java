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

package com.ibm.sbt.services.client.connections.profiles;

import static com.ibm.sbt.services.client.base.CommonConstants.AUTH_TYPE;
import static com.ibm.sbt.services.client.base.CommonConstants.CH_COLON;
import static com.ibm.sbt.services.client.base.CommonConstants.COMMA;
import static com.ibm.sbt.services.client.base.CommonConstants.CONTENT_TYPE;
import static com.ibm.sbt.services.client.base.CommonConstants.DOT;
import static com.ibm.sbt.services.client.base.CommonConstants.EMPTY;
import static com.ibm.sbt.services.client.base.CommonConstants.EQUALS;
import static com.ibm.sbt.services.client.base.CommonConstants.IMAGE_;
import static com.ibm.sbt.services.client.base.CommonConstants.IMAGE_JPG;
import static com.ibm.sbt.services.client.base.CommonConstants.JPG;
import static com.ibm.sbt.services.client.base.CommonConstants.LOCATION_HEADER;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.HREF;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.ACCEPTED;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.COLLEAGUE;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.CONNECTION;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.CONNECTION_TYPE;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.CONNECTION_UNIQUE_IDENTIFIER;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.FORMAT;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.FULL;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.OUTPUT;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.OUTPUT_TYPE;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.PROFILE;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.PROFILES;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.VCARD;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.conn.EofSensorInputStream;
import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomFeedHandler;
import com.ibm.sbt.services.client.base.AuthType;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.CommonConstants.HTTPCode;
import com.ibm.sbt.services.client.base.ConnectionsService;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.JsonDataHandler;
import com.ibm.sbt.services.client.connections.common.Tag;
import com.ibm.sbt.services.client.connections.profiles.model.ProfileXPath;
import com.ibm.sbt.services.client.connections.profiles.serializers.ColleagueConnectionSerializer;
import com.ibm.sbt.services.client.connections.profiles.serializers.ProfileSerializer;
import com.ibm.sbt.services.client.connections.profiles.utils.Messages;
import com.ibm.sbt.services.client.smartcloud.profiles.ProfileServiceException;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * The Profiles application of IBM® Connections is a directory of the people in your organization.
 * You can use it to find the information you need to form and encourage effective networks.
 * In addition to basic information, Profiles catalogs skills such as technical expertise, familiarity with foreign languages, and areas of interest.
 * 
 * @see
 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.5+API+Documentation#action=openDocument&res_title=Profiles_API_ic45&content=pdcontent">
 *			Profiles API</a>
 * 
 * @author Swati Singh
 * @author Carlos Manias
 */
public class ProfileService extends ConnectionsService {

	private static final long serialVersionUID = -598413531035038479L;

	/**
	 * Create ProfileService instance with default endpoint.
	 */
	public ProfileService() {
		this(DEFAULT_ENDPOINT_NAME, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Create ProfileService instance with specified endpoint.
	 * 
	 * @param endpoint
	 */
	public ProfileService(String endpoint) {
		this(endpoint, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Create WikiService instance with specified endpoint.
	 * 
	 * @param endpoint
	 */
	public ProfileService(Endpoint endpoint) {
		this(endpoint, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Create a ProfileService instance with specified endpoint and CacheSize
	 * 
	 * @param endpoint
	 * @param cacheSize
	 *            Creates ProfileService with specified endpoint and CacheSize
	 */
	public ProfileService(String endpoint, int cacheSize) {
		super(endpoint, cacheSize);
	}
	
	/**
	 * Create a ProfileService instance with specified endpoint and CacheSize
	 * 
	 * @param endpoint
	 * @param cacheSize
	 *            Creates ProfileService with specified endpoint and CacheSize
	 */
	public ProfileService(Endpoint endpoint, int cacheSize) {
		super(endpoint, cacheSize);
	}
	
	@Override
	public NamedUrlPart getAuthType(){
		String auth = super.getAuthType().getValue();
		auth = AuthType.BASIC.get().equalsIgnoreCase(auth)?EMPTY:auth;
		return new NamedUrlPart(AUTH_TYPE, auth);
	}

	/**
	 * Return mapping key for this service
	 */
	@Override
	public String getServiceMappingKey() {
		return PROFILES;
	}

	/***************************************************************
	 * Getting Profile Feeds
	 ****************************************************************/

	/**
	 * Search for a set of profiles that match a specific criteria and return them in a feed.<br>
	 * You can use this resource to discover someone's user ID. <br>
	 * When you search by name, the returned feed identifies each person's user ID, 
	 * which you can subsequently use in other types of searches to retrieve more detailed information about a person.<br>
	 * This method returns a feed of profiles instead of the Atom entry of a single profile.
	 * If you want to retrieve an Atom entry document, see Retrieving a profile entry. The content element of each returned entry includes the vCard information for the person being represented by the entry.
	 * 
	 * @param parameters 
	 * 				  list of query string parameters to pass to API
	 * @return EntityList&lt;Profile&gt;
	 * @throws ClientServicesException 
	 */
	public EntityList<Profile> searchProfiles(Map<String, String> parameters) throws ClientServicesException {
		String url = ProfileUrls.SEARCH.format(this);
		return getProfileEntityList(url, parameters);
	}
	
	/**
	 * This method returns a feed of profile as opposed to retrieving the Atom entry of the profile.<br>
	 * If you want to retrieve an Atom entry document, see Retrieving a profile entry.<br>
	 * The content element of each returned entry includes the vcard information for the person being represented by the entry.<br>
	 * In addition, it provides a list of the fully qualified URLs for each IBM® Connections application link displayed in the business card.
	 * 
	 * @param id
	 *             unique identifier of User , it can either be email or id 				
	 * @return Profile
	 * @throws ClientServicesException 
	 */
	public Profile getProfile(String id) throws ClientServicesException {
		return getProfile(id, null);
	}

	/**
	 * This method returns a feed of profile as opposed to retrieving the Atom entry of the profile.<br>
	 * If you want to retrieve an Atom entry document, see Retrieving a profile entry.<br>
	 * The content element of each returned entry includes the vcard information for the person being represented by the entry.<br>
	 * In addition, it provides a list of the fully qualified URLs for each IBM® Connections application link displayed in the business card.
	 * 
	 * @param id
	 *             unique identifier of User , it can either be email or id 				
	 * @param parameters 
	 * 				list of query string parameters to pass to API
	 * @return Profile
	 * @throws ClientServicesException 
	 */
	public Profile getProfile(String id, Map<String, String> parameters) throws ClientServicesException {
		// TODO: Do a cache lookup first. If cache miss, make a network call to get profile
		if (StringUtil.isEmpty(id)){
			throw new ClientServicesException(null, Messages.InvalidArgument_1);
		}
		String url = ProfileUrls.PROFILE.format(this, ProfileParams.userId.get(id));
		return getProfileEntity(url, parameters);
	}
	
	/**
	 * Retrieve the profiles of the people who comprise a specific user's report-to chain. 
	 * For example, the profile of the user's manager, that manager's manager, and so on.
	 * The content element of each returned entry includes the vcard information for the person being represented by the entry.
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose report to chain is required, it can be email or userID
	 * @return EntityList&lt;Profile&gt;
	 * @throws ClientServicesException 
	 */
	public EntityList<Profile> getReportingChain(String id) throws ClientServicesException {
		return getReportingChain(id,null);
	}

	/**
	 * Retrieve the profiles of the people who comprise a specific user's report-to chain. 
	 * For example, the profile of the user's manager, that manager's manager, and so on.
	 * The content element of each returned entry includes the vcard information for the person being represented by the entry.
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose report to chain is required, it can be email or userID
	 * @param parameters 
	 * 				list of query string parameters to pass to API
	 * @return EntityList&lt;Profile&gt;
	 * @throws ClientServicesException 
	 */
	public EntityList<Profile> getReportingChain (String id, Map<String, String> parameters)throws ClientServicesException {
		String url = ProfileUrls.REPORTING_CHAIN.format(this, ProfileParams.userId.get(id));
		return getProfileEntityList(url, parameters);
	}

	/**
	 * Retrieve the profiles of the people who report to a specific user.
	 * The content element of each returned entry includes the vcard information for the person being represented by the entry.
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose direct reports are required, it can be email or userID
	 * @return EntityList&lt;Profile&gt;
	 * @throws ClientServicesException 
	 */
	public EntityList<Profile> getPeopleManaged(String id) throws ClientServicesException {
		return getPeopleManaged(id,null);
	}

	/**
	 * Retrieve the profiles of the people who report to a specific user.
	 * The content element of each returned entry includes the vcard information for the person being represented by the entry.
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose direct reports are required, it can be email or userID
	 * @param parameters 
	 * 				list of query string parameters to pass to API
	 * @return EntityList&lt;Profile&gt;
	 * @throws ClientServicesException 
	 */
	public EntityList<Profile> getPeopleManaged(String id, Map<String, String> parameters)throws ClientServicesException {
		String url = ProfileUrls.PEOPLE_MANAGED.format(this, ProfileParams.userId.get(id));
		return getProfileEntityList(url, parameters);
	}

	/**
	 * Returns a feed that lists the contacts that a person has designated as colleagues. <br>
	 * You can use this resource to subscribe to a person's list of colleagues.
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose colleagues are required, it can be email or userID
	 * @return EntityList&lt;Profile&gt;
	 * @throws ClientServicesException 
	 */
	public EntityList<Profile> getColleagues(String id) throws ClientServicesException {
		return getColleagues(id,null);
	}

	/**
	 * Returns a feed that lists the contacts that a person has designated as colleagues. <br>
	 * You can use this resource to subscribe to a person's list of colleagues.
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose colleagues are required, it can be email or userID
	 * @param parameters 
	 * 				list of query string parameters to pass to API
	 * @return EntityList&lt;Profile&gt;
	 * @throws ClientServicesException 
	 */
	public EntityList<Profile> getColleagues(String id, Map<String, String> parameters) throws ClientServicesException {
		if (StringUtil.isEmpty(id)){
			throw new ClientServicesException(null, Messages.InvalidArgument_1);
		}

		parameters = getParameters(parameters);
		parameters.put(CONNECTION_TYPE, COLLEAGUE);
		parameters.put(OUTPUT_TYPE, PROFILE);

		String url = ProfileUrls.CONNECTIONS.format(this, ProfileParams.userId.get(id));
		return getProfileEntityList(url, parameters);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	//TODO: Get a list of your connections by status where status is accepted, pending and unconfirmed
	//////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns an entry of the colleague connection if one exists.
	 * 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Checking_whether_two_people_are_colleagues_ic40a&content=pdcontent">
	 *			Checking whether two people are colleagues</a>
	 * 
	 * @param sourceId 
	 * 				 userid or email of first user
	 * @param targetId 
	 * 				 userid or email of second user
	 * @return ColleagueConnection
	 * @throws ClientServicesException 
	 */
	public ColleagueConnection checkColleague(String sourceId, String targetId) throws ClientServicesException {
		return checkColleague(sourceId, targetId, null);
	}

	/**
	 * Returns an entry of the colleague connection if one exists.
	 * 
	 * @param sourceId 
	 * 				 userid or email of first user
	 * @param targetId 
	 * 				 userid or email of second user
	 * @param parameters 
	 * 				list of query string parameters to pass to API
	 * @return ColleagueConnection
	 * @throws ClientServicesException 
	 */
	public ColleagueConnection checkColleague(String sourceId, String targetId, Map<String, String> parameters) throws ClientServicesException {
		String url = ProfileUrls.CHECK_COLLEAGUE.format(this, ProfileParams.sourceId.get(sourceId), ProfileParams.targetId.get(targetId));
		return getColleagueConnectionEntity(url, parameters);
	}
	
	/**
	 * Retrieve the Colleagues In Common feed to find out what colleagues two people have in common.
	 * The feed contains entries that represent colleague connections. Each entry contains the following information:
	 * <li> A colleague that the two people share in common
	 * <li> The status of the connection
	 * The output is displayed as a profile entry.
	 *  
	 * @param sourceId 
	 * 				 userid or email of first user
	 * @param targetId 
	 * 				 userid or email of second user
	 * @return EntityList&lt;Profile&gt;
	 * @throws ClientServicesException 
	 */
	public EntityList<Profile> getCommonColleagues(String sourceId, String targetId) throws ClientServicesException {
		return getCommonColleagues(sourceId, targetId, null);
	}

	/**
	 * Retrieve the Colleagues In Common feed to find out what colleagues two people have in common.
	 * The feed contains entries that represent colleague connections. Each entry contains the following information:
	 * <li> A colleague that the two people share in common
	 * <li> The status of the connection
	 * The output is displayed as a profile entry.
	 *  
	 * @param sourceId 
	 * 				 userid or email of first user
	 * @param targetId 
	 * 				 userid or email of second user
	 * @param parameters 
	 * 				list of query string parameters to pass to API
	 * @return EntityList&lt;Profile&gt;
	 * @throws ClientServicesException 
	 */
	public EntityList<Profile> getCommonColleagues(String sourceId, String targetId,  Map<String, String> parameters) throws ClientServicesException {
		if (StringUtil.isEmpty(sourceId)) {
			throw new ClientServicesException(null, Messages.InvalidArgument_4);
		}
		if (StringUtil.isEmpty(targetId)) {
			throw new ClientServicesException(null, Messages.InvalidArgument_5);
		}
		StringBuilder value =  new StringBuilder(sourceId);
		value = value.append(COMMA).append(targetId);
		parameters = getParameters(parameters);
		parameters.put(CONNECTION_TYPE, COLLEAGUE);
		parameters.put(OUTPUT_TYPE, PROFILE);

		String url = ProfileUrls.CONNECTIONS_IN_COMMON.format(this, ProfileParams.userId.get(value.toString()));
		return getProfileEntityList(url, parameters);
	}

	/**
	 * Retrieve the Colleagues In Common feed to find out what colleagues two people have in common.
	 * The feed contains entries that represent colleague connections. Each entry contains the following information:
	 * <li> A colleague that the two people share in common
	 * <li> The status of the connection
	 * The output is displayed as a connection entry.
	 *  
	 * @param sourceId 
	 * 				 userid or email of first user
	 * @param targetId 
	 * 				 userid or email of second user
	 * @return EntityList&lt;ColleagueConnection&gt;
	 * @throws ClientServicesException 
	 */
	public EntityList<ColleagueConnection> getCommonColleagueConnections(String sourceId, String targetId) throws ClientServicesException {
		return getCommonColleagueConnections(sourceId, targetId, null);
	}
	
	/**
	 * Retrieve the Colleagues In Common feed to find out what colleagues two people have in common.
	 * The feed contains entries that represent colleague connections. Each entry contains the following information:
	 * <li> A colleague that the two people share in common
	 * <li> The status of the connection
	 * The output is displayed as a connection entry.
	 *  
	 * @param sourceId 
	 * 				 userid or email of first user
	 * @param targetId 
	 * 				 userid or email of second user
	 * @param parameters 
	 * 				list of query string parameters to pass to API
	 * @return EntityList&lt;ColleagueConnection&gt;
	 * @throws ClientServicesException 
	 */
	public EntityList<ColleagueConnection> getCommonColleagueConnections(String sourceId, String targetId,  Map<String, String> parameters) throws ClientServicesException {
		if (StringUtil.isEmpty(sourceId)) {
			throw new ClientServicesException(null, Messages.InvalidArgument_4);
		}
		if (StringUtil.isEmpty(targetId)) {
			throw new ClientServicesException(null, Messages.InvalidArgument_5);
		}
		parameters = getParameters(parameters);
		StringBuilder value =  new StringBuilder(sourceId);
		value = value.append(COMMA).append(targetId);
		parameters.put(CONNECTION_TYPE, COLLEAGUE);
		parameters.put(OUTPUT_TYPE, CONNECTION);

		String url = ProfileUrls.CONNECTIONS_IN_COMMON.format(this, ProfileParams.userId.get(value.toString()));
		return getColleagueConnectionEntityList(url, parameters);
	}

	////////////////////////////////////////////
	//TODO: Get a person's status update entries
	////////////////////////////////////////////
	
	//////////////////////////////////////////////////////
	//TODO: Get all the comments associated with a message
	//////////////////////////////////////////////////////
	
	///////////////////////////////////////////
	//TODO: Get the messages of your colleagues
	///////////////////////////////////////////

	///////////////////////////////
	//TODO: Get all status messages
	///////////////////////////////
	
	////////////////////////////////////////////////////
	//TODO: Get the status messages of a list of people
	////////////////////////////////////////////////////
	
	/***************************************************************
	 * Working with pronunciation files
	 ****************************************************************/

	////////////////////////////////////////////////////////////////////////////////
	//TODO: ALL WORKING PRONUNCIATION FILES
	////////////////////////////////////////////////////////////////////////////////
	
	/***************************************************************
	 * Working with profile extensions
	 ****************************************************************/

	////////////////////////////////////////////////////////////////////////////////
	//TODO: MOST WORKING PROFILE EXTENSIONS
	////////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns a mapping containing the extended attributes for the entry.<bt/>
	 * This method execute a xhr call to the back end for every attribute.
	 * 
	 * @param p the profile to use. has to be a full profile, to obtain all the extended attributes links
	 * @return a map containing the id of the attribute as key and the attribute value as value<br/>
	 * the map can contain object of type InputStream, Node, String, according to the return content type
	 * @throws ClientServicesException
	 */
	public Map<String,Object> getExtendedAttributes(Profile p) throws ClientServicesException {
		Map<String, Object> ret = new HashMap<String, Object>();
 		List<Node> attributes = (List<Node>) p.getDataHandler().getEntries(ProfileXPath.extendedAttributes);
		for (Node link :attributes) {
			String extUrl = link.getAttributes().getNamedItem(HREF).getTextContent();
			String extId = link.getAttributes().getNamedItem("snx:extensionId").getTextContent();
			Response resp;
			resp = getEndpoint().xhrGet(extUrl);

			Object extValue= resp.getData();
			if (resp.getData() instanceof EofSensorInputStream) {
				//a EofSensorInputStream leaves the connection open until it is read fully
				//so we read it before serving it to the client terminating the connection
				try {
					extValue = new ByteArrayInputStream(IOUtils.toByteArray((EofSensorInputStream)resp.getData()));
				} catch (Exception e) {
					throw new ClientServicesException(e,"Exception reading " + extUrl);
				}
			}
			ret.put(extId, extValue);
		}
		return ret;
	}

	/***************************************************************
	 * Working with profile photos
	 ****************************************************************/

	////////////////////////////////////////////////////
	//TODO: Adding a photo to a profile
	////////////////////////////////////////////////////

	////////////////////////////////////////////////////
	//TODO: Removing a photo from a profile
	////////////////////////////////////////////////////

	////////////////////////////////////////////////////
	//TODO: Retrieving a profile photo
	////////////////////////////////////////////////////

	/**
	 * To replace the image file that supplies the photo for a person's profile, 
	 * send a binary image file to the web address defined in the image file link returned by the Profile's entry document. <br>
	 * You can only replace a photo if you are the profile owner or an administrator.
	 * See Authenticating requests for information about how to authenticate the request. <br>
	 *  
	 * @param File
	 * 			image to be uploaded as profile photo
	 * @param userid
	 * @throws ClientServicesException
	 */
	public void updateProfilePhoto(File file, String userId) throws ClientServicesException {
        if (file == null || !file.canRead()) {
            throw new ClientServicesException(null, Messages.MessageCannotReadFile,
                    file.getAbsolutePath());
        }
		String name = file.getName();
		int dot = StringUtil.lastIndexOfIgnoreCase(name, DOT);
		String ext = "";
		if (dot > -1) {
			ext = name.substring(dot + 1); // add one for the dot!
		}
		if (!StringUtil.isEmpty(ext)) {
			Map<String, String> headers = new HashMap<String, String>();
			if (StringUtil.equalsIgnoreCase(ext, JPG)) {
				headers.put(CONTENT_TYPE, IMAGE_JPG);	// content-type should be image/jpeg for file extension - jpeg/jpg
			} else {
				headers.put(CONTENT_TYPE, IMAGE_ + ext);
			}
			String url = ProfileUrls.PHOTO.format(this, ProfileParams.userId.get(userId));
			Response response = getClientService().put(url, null, headers, file, ClientService.FORMAT_NULL);
			checkResponseCode(response, HTTPCode.OK);
		}
	}

	/***************************************************************
	 * Working with colleague connections
	 ****************************************************************/

	/**
	 * To invite a person to become your colleague, send an Atom entry document containing the connection resource to the person's colleague connections feed.<br>
	 * To find a person's colleague connections feed, you can search for the person by name. 
	 * From the returned feed, find the person and retrieve her associated user ID, and then use the user ID 
	 * to retrieve the person's full feed, which includes a link to her colleague connections feed. <br>
	 * After you send the invitation, the connection is added to your connections in an unconfirmed state.
	 * The connection is added to the connections of the person you invited in a pending state.
	 * See Accepting an invitation to become a colleague or Deleting connections for information about how to accept or decline an invitation. <br>
	 * See Authenticating requests for information about how to authenticate the request.<br>
	 * A default Invite message is used while sending the invite
	 *  
	 * @param id
	 * 		   unique identifier of the user to whom the invite is to be sent, it can be email or userID
	 * @return Id of the Connection
	 * @throws ProfileServiceException
	 */
	public String sendInvite(String id)throws ClientServicesException {
		String defaultInviteMsg = Messages.SendInviteMsg;
		return sendInvite(id, defaultInviteMsg);
	}

	/**
	 * To invite a person to become your colleague, send an Atom entry document containing the connection resource to the person's colleague connections feed.<br>
	 * To find a person's colleague connections feed, you can search for the person by name. 
	 * From the returned feed, find the person and retrieve her associated user ID, and then use the user ID 
	 * to retrieve the person's full feed, which includes a link to her colleague connections feed. <br>
	 * After you send the invitation, the connection is added to your connections in an unconfirmed state.
	 * The connection is added to the connections of the person you invited in a pending state.
	 * See Accepting an invitation to become a colleague or Deleting connections for information about how to accept or decline an invitation. <br>
	 * See Authenticating requests for information about how to authenticate the request.<br>
	 * A default Invite message is used while sending the invite
	 *  
	 * @param id
	 * 		   unique identifier of the user to whom the invite is to be sent, it can be email or userID
	 * @param inviteMsg 
	 * 				Invite message to the other user
	 * @return Id of the Connection
	 * @throws ProfileServiceException
	 */
	public String sendInvite(String id, String inviteMsg)throws ClientServicesException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(CONNECTION_TYPE, COLLEAGUE);
		String payload = constructSendInviteRequestBody(inviteMsg);
		String url = ProfileUrls.CONNECTIONS.format(this, ProfileParams.userId.get(id));
		Response response = createData(url, parameters, payload);
		checkResponseCode(response, HTTPCode.CREATED);
		return extractConnectionIdFromHeaders(response);
	}

	/**
	 * To remove an existing colleague relationship from your profile, 
	 * or to decline an invitation to become a colleague, use this method. <br>
	 * Only a participant in the colleague relationship or an invitee to join a colleague relationship 
	 * can delete the connection. Deleted connections cannot be restored.
	 * See Authenticating requests for information about how to authenticate the request.
	 *  
	 * @param connectionId 
	 * 					unique id of the connection
	 * @throws ClientServicesException 
	 */
	public void deleteInvite(String connectionId)throws ClientServicesException{
		if (StringUtil.isEmpty(connectionId)) {
			throw new ClientServicesException(null, Messages.InvalidArgument_2);
		}
		String url = ProfileUrls.CONNECTION.format(this, ProfileUrls.getConnectionId(connectionId));
		Response response = deleteData(url, null, connectionId);
		checkResponseCode(response, HTTPCode.OK);
	}

	/**
	 * To retrieve complete information about a colleague relationship, 
	 * use the edit link found in the connection entry in the colleague connections feed. <bR>
	 * You can use this operation to obtain connection information that you want to preserve prior to accepting an invitation.
	 * See Accepting an invitation to become a colleague for more information. <br>
	 * This method returns the Atom entry of a single connection entry as opposed to a feed of all the connection entries.
	 * If you want to retrieve a feed, see Searching for a person's colleagues. <br>
	 * See Authenticating requests for information about how to authenticate the request.
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose colleagues are required, it can be email or userID
	 * @return EntityList&lt;ColleagueConnection&gt;
	 * @throws ClientServicesException 
	 */
	public EntityList<ColleagueConnection> getColleagueConnections(String id) throws ClientServicesException {
		return getColleagueConnections(id,null);
	}
	
	/**
	 * To retrieve complete information about a colleague relationship, 
	 * use the edit link found in the connection entry in the colleague connections feed. <bR>
	 * You can use this operation to obtain connection information that you want to preserve prior to accepting an invitation.
	 * See Accepting an invitation to become a colleague for more information. <br>
	 * This method returns the Atom entry of a single connection entry as opposed to a feed of all the connection entries.
	 * If you want to retrieve a feed, see Searching for a person's colleagues. <br>
	 * See Authenticating requests for information about how to authenticate the request.
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose colleagues are required, it can be email or userID
	 * @param parameters 
	 * 				list of query string parameters to pass to API
	 * @return EntityList&lt;ColleagueConnection&gt;
	 * @throws ClientServicesException 
	 */
	public EntityList<ColleagueConnection> getColleagueConnections(String id, Map<String, String> parameters) throws ClientServicesException {
		parameters = getParameters(parameters);
		parameters.put(CONNECTION_TYPE, COLLEAGUE);
		parameters.put(OUTPUT_TYPE, CONNECTION);

		String url = ProfileUrls.CONNECTIONS.format(this, ProfileParams.userId.get(id));
		return getColleagueConnectionEntityList(url, parameters);
	}

	/**
	 * To accept an invitation to be a colleague, send an updated connection document
	 * in Atom format to the existing connection's edit web address.<br>
	 * After you accept a connection, the status of the connection in your connections 
	 * is changed from pending to accepted and the status of the connection in the invitee's list
	 * of connections is changed from unconfirmed to accepted.<br> 
	 * To decline the invitation, delete it by sending a DELETE HTTP request to the resource.
	 * All existing connection information will be replaced with the new data.<br>
	 * To avoid deleting all existing data, retrieve any data you want to retain first, 
	 * and send it back with this request. 
	 * See Inviting a person to become your colleague for more information.<br>
	 * See Authenticating requests for information about how to authenticate the request.
	 * 
	 * @param ColleagueConnection 
	 * @throws ClientServicesException 
	 * 
	 */
	public void acceptInvite(ColleagueConnection connection)throws ClientServicesException{
		if (connection == null) {
			throw new ClientServicesException(null, Messages.InvalidArgument_6);
		}
		String payload = constructAcceptInviteRequestBody(connection, ACCEPTED);
		String url = ProfileUrls.CONNECTION.format(this, ProfileUrls.getConnectionId(connection.getConnectionId()));
		Response response = updateData(url, null, payload, connection.getConnectionId());
		checkResponseCode(response, HTTPCode.OK);
	}
	
	/***************************************************************
	 * Working with update messages and comments
	 ****************************************************************/

	////////////////////////////////////////////////////////////////////////////////
	//TODO: ALL WORKING UPDATE MESSAGES AND COMMENTS
	////////////////////////////////////////////////////////////////////////////////

	/***************************************************************
	 * Working with status messages
	 ****************************************************************/

	////////////////////////////////////////////////////////////////////////////////
	//TODO: ALL WORKING STATUS MESSAGES
	////////////////////////////////////////////////////////////////////////////////

	/***************************************************************
	 * Working with profiles
	 ****************************************************************/

	////////////////////////////////////////////////////////////////////////////////
	//TODO: Retrieving a profile entry. Uses the url now only used in ProfileAdmin
	////////////////////////////////////////////////////////////////////////////////

	/**
	 * To update your profile entry, send an updated profile entry document 
	 * in Atom format to the existing profile's edit web address.<br>
	 * All existing profile entry information is replaced with the new data.
	 * To avoid deleting all existing data, retrieve any data you want to retain first,
	 * and send it back with this request. For example, 
	 * if you want to add a new tag to a profile entry, retrieve the existing tags,
	 * and send them all back with the new tag in the update request. 
	 * See Retrieving a profile entry for more information.<br>
	 * To find out which fields in a particular profile can be edited, 
	 * look for the list of <editableField> elements in the service document of the person 
	 * whose profile you want to edit. 
	 * You cannot programmatically change which fields can be edited; 
	 * that can only be done using wsadmin commands. 
	 * See Customizing the Profiles user interface for more details. 
	 * However, you can change the values stored in the editable fields for a profile 
	 * by specifying new values for the editable fields represented as corresponding vCard values.<br>
	 * In addition to the default set of fields available for a profile that can be configured as editable by the administrator,
	 * the administrator can also add custom fields to a profile.
	 * Custom fields are added using extensions; 
	 * see Adding custom extension attributes for Profiles for more details. 
	 * For information about how to edit the value of a custom field, see Working with profile extensions.<br>
	 * To get the rel="edit" link for a specific person's profile, 
	 * retrieve a feed of the profile and specify the output=vcard parameter on the request.
	 * See Searching a user's profile for more details.<br>
	 * Users can only update their own profiles. 
	 * See Authenticating requests for information about how to authenticate the request.<br>
	 * 
	 * @param Profile
	 * @throws ClientServicesException 
	 */
	public void updateProfile(Profile profile) throws ClientServicesException {
		if (profile == null) {
			throw new ClientServicesException(null, Messages.InvalidArgument_3);
		}
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(OUTPUT, VCARD);
		parameters.put(FORMAT, FULL);
		String id = profile.getUserid();
		String updateProfilePayload = constructUpdateRequestBody(profile);
		String updateUrl = ProfileUrls.PROFILE_ENTRY.format(this, ProfileParams.userId.get(id));
		Response response = updateData(updateUrl, parameters,updateProfilePayload, ProfileParams.userId.getParamName(profile.getAsString("uid")));
		checkResponseCode(response, HTTPCode.OK);
		profile.clearFieldsMap();
	}

	/***************************************************************
	 * Working with profile tags
	 ****************************************************************/

	/** 
	 * To retrieve the tags assigned to a profile from the Profiles tag collection,
	 *  use a HTTP GET request to retrieve the tags category document for that profile.<br>
	 *  You can retrieve tags created by any user for any user.
	 *  
	 * @param id 
	 *        unique identifier of the user whose tags are required, it can be email or userKey
	 * @return EntityList&lg;Tag&gt;
	 * @throws ClientServicesException 
	 */
	public EntityList<Tag> getTags(String id) throws ClientServicesException {
		return getTags(id,null);
	}

	/**
	 * To retrieve the tags assigned to a profile from the Profiles tag collection,
	 *  use a HTTP GET request to retrieve the tags category document for that profile.<br>
	 *  You can retrieve tags created by any user for any user.
	 *  
	 * @param id 
	 *        unique identifier of the user whose tags are required, it can be email or userKey
	 * @param parameters 
	 *         list of query string parameters to pass to API
	 * @return EntityList&lg;Tag&gt;
	 * @throws ClientServicesException 
	 */
	public EntityList<Tag> getTags(String id, Map<String, String> parameters) throws ClientServicesException {
	  String url = ProfileUrls.GET_TAGS.format(this, ProfileParams.targetId.get(id));
	  return getTagEntityList(url, parameters);
	}
	  
	/**
	 * Creating, updating and deleting tags is done with the same operation.<br>
	 * To update the tags that one user created and assigned to another user's profile,
	 * use a HTTP PUT request to update the tags listed in the tag creator's tags category document.<br>
	 * When you update profile tags, the existing tag information is replaced with the new data. 
	 * To avoid deleting all existing tags, retrieve the tags that you want to retain first,
	 * and send them back with this request. See Retrieving profile tags for more information.<br>
	 * You can add tags to anyone's collection. You can only replace tags created by the user whose credentials
	 * you provided to authenticate with the server. You can remove tags from the tag collection for your profile 
	 * by leaving them out of the updated set. <br>
	 * See Authenticating requests for information about how to authenticate the request. 
	 *  
	 * @param sourceId
	 *        unique identifier of the user who wants to add tags to a profile
	 * @param targetId
	 *        unique identifier of the user whose profile is going to be tagged
	 * @param profile
	 *        profile to be tagged, containing all the tags, both existing and new
	 * @param tags Comma-separated list of tags to add to the profile
	 * @throws ClientServicesException 
	 */
	public void addTags(String sourceId, String targetId, Profile profile) throws ClientServicesException{
		if (StringUtil.isEmpty(sourceId)){
			throw new ClientServicesException(null, Messages.InvalidArgument_4);
		}

		if (StringUtil.isEmpty(targetId)){
			throw new ClientServicesException(null, Messages.InvalidArgument_5);
		}
	    String serviceUrl = ProfileUrls.TAGS.format(this, 
	    			ProfileParams.sourceId.get(sourceId),
	    			ProfileParams.targetId.get(targetId));
	    
	    ProfileSerializer serializer = new ProfileSerializer(profile);
		Response response = updateData(serviceUrl, null, serializer.tagsPayload(), null);
		checkResponseCode(response, HTTPCode.OK);
	}


	/***************************************************************
	 * Working with profile types
	 ****************************************************************/

	////////////////////////////////////////////////////////////////////////////////
	//TODO: ALL WORKING WITH PROFILE TYPES
	////////////////////////////////////////////////////////////////////////////////

	
	/***************************************************************
	 * Miscellaneous methods
	 ****************************************************************/
	
	/**
	 * Returns the userid of the currently connected user
	 * 
	 * @return
	 * @throws ClientServicesException 
	 */
	public String getMyUserId() throws ClientServicesException{
		String id = "";
		String peopleApiUrl = ProfileUrls.MY_USER_ID.format(this);
		Response feed = getClientService().get(peopleApiUrl);
		JsonDataHandler dataHandler = new JsonDataHandler((JsonJavaObject)feed.getData());
		id = dataHandler.getAsString("entry/id");
		id = id.substring(id.lastIndexOf(CH_COLON)+1, id.length());
		return id;
	}

	/**
	 * Returns the profile of the currently connected user
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public Profile getMyProfile() throws ClientServicesException {
		return getProfile(getMyUserId(), null);
	}
	
	/***************************************************************
	 * FeedHandlers for each entity type
	 ****************************************************************/

	/**
	 * Factory method to instantiate a FeedHandler for Profiles
	 * @return IFeedHandler<Profile>
	 */
	public IFeedHandler<Profile> getProfileFeedHandler() {
		return new AtomFeedHandler<Profile>(this) {
			@Override
			protected Profile entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Profile(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	/**
	 * Factory method to instantiate a FeedHandler for ColleagueConnections
	 * @return IFeedHandler<ColleagueConnection>
	 */
	public IFeedHandler<ColleagueConnection> getColleagueFeedHandler() {
		return new AtomFeedHandler<ColleagueConnection>(this, false) {
			@Override
			protected ColleagueConnection entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new ColleagueConnection(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	/**
	 * Factory method to instantiate a FeedHandler for Tags
	 * @return IFeedHandler<Tag>
	 */
	public IFeedHandler<Tag> getTagFeedHandler() {
		return new AtomFeedHandler<Tag>(this) {
			@Override
			protected Tag entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Tag(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	/***************************************************************
	 * Factory methods
	 ****************************************************************/
	
	protected Profile getProfileEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntity(requestUrl, parameters, getProfileFeedHandler());
	}

	protected ColleagueConnection getColleagueConnectionEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntity(requestUrl, parameters, getColleagueFeedHandler());
	}

	protected EntityList<Profile> getProfileEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getProfileFeedHandler());
	}

	protected EntityList<ColleagueConnection> getColleagueConnectionEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getColleagueFeedHandler());
	}

	protected EntityList<Tag> getTagEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getTagFeedHandler());
	}


	/***************************************************************
	 * Utility methods
	 ****************************************************************/

	/*
	 * This method is used by ProfileService wrapper methods to construct request body for Add operations
	 * @return String
	 */
	protected String constructCreateRequestBody(Profile profile) {
		ProfileSerializer serializer = new ProfileSerializer(profile);
		return serializer.createPayload();
	}

	/*
	 * This method is used by ProfileService wrapper methods to construct request body for Update operations
	 * @return String
	 */
	protected String constructUpdateRequestBody(Profile profile) {
		ProfileSerializer serializer = new ProfileSerializer(profile);
		return serializer.updatePayload();
	}

	/*
	 * This method is used by ProfileService wrapper methods to construct request body for Add operations
	 * @return String
	 */
	protected String constructAcceptInviteRequestBody(ColleagueConnection connectionEntry, String action) {
		ColleagueConnectionSerializer serializer = new ColleagueConnectionSerializer(connectionEntry);
		return serializer.acceptInvitePayload();
	}

	/*
	 * This method is used by ProfileService wrapper methods to construct request body for Add operations
	 * @return String
	 * @throws ProfileServiceException 
	 */
	protected String constructSendInviteRequestBody(String inviteMsg) throws ClientServicesException {
		ColleagueConnection colleagueConnection = new ColleagueConnection(this, null);
		colleagueConnection.setContent(inviteMsg);
		ColleagueConnectionSerializer serializer = new ColleagueConnectionSerializer(colleagueConnection);
		return serializer.acceptInvitePayload();
	}

	protected String extractConnectionIdFromHeaders(Response requestData){
		Header header = requestData.getResponse().getFirstHeader(LOCATION_HEADER);
		String urlLocation = header!=null?header.getValue():EMPTY;
		return urlLocation.substring(urlLocation.indexOf(CONNECTION_UNIQUE_IDENTIFIER+EQUALS) + (CONNECTION_UNIQUE_IDENTIFIER+EQUALS).length());
	}
}
