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

package com.ibm.sbt.services.client.activitystreams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.sbt.services.client.BaseService;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.SBTServiceException;
import com.ibm.sbt.services.client.activitystreams.model.ActivityStreamEntry;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.sbt.services.util.AuthUtil;
import com.ibm.sbt.util.DataNavigator;

/**
 * ActivityStreamService can be used to perform operations related to ActivityStream.
 * <p>
 * Relies on values of User, Group and Application to construct URLs and perform Network operations. Constructs {@link ActivityStreamEntry} objects after parsing the JSON response from Connections server
 * 
 * @author Manish Kataria
 * 
 *         <pre>
 * Sample Usage
 *  {@code
 * 	ActivityStreamService _service = new ActivityStreamService();
 * 	List<ActivityStreamEntry> _entries = (List) _service.getUpdatesFromMyNetwork();
 * }
 * </pre>
 * @see http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=IBM_Connections_Activity_Stream_API&content=pdcontent
 */

public class ActivityStreamService extends BaseService {
	Endpoint					endpoint;

	// Typical url pattern /activitystream/user/group/application

	static final String			sourceClass	= ActivityStreamService.class.getName();
	static final Logger			logger		= Logger.getLogger(sourceClass);
	private static final String	seperator	= "/";

	/**
	 * Used in constructing REST APIs
	 */
	public static final String	baseUrl		= "/connections/opensocial/";
	/**
	 * Used in constructing REST APIs
	 */
	public static final String	restUrl		= "/rest";
	/**
	 * Used in constructing REST APIs for Activity Stream
	 */
	public static final String  activityStreamsUrl = "/activitystreams";
	/**
	 * Used in constructing REST APIs for Microblogging
	 */
	public static final String  boardUrl = "/ublog";

	/**
	 * Constructor Creates ActivityStreamService Object with default endpoint
	 */
	public ActivityStreamService() {
		super();
		this.endpoint = EndpointFactory.getEndpoint(ActivityStreamService.getDefaultEndpoint());
	}

	/**
	 * Constructor
	 * 
	 * @param endpoint
	 *            Creates ActivityStreamService Object with specified values of endpoint
	 */
	public ActivityStreamService(String endpoint) {
		super(endpoint);
		this.endpoint = EndpointFactory.getEndpoint(endpoint);;
	}

	/**
	 * Returns updates from ActivityStream service
	 * <p>
	 * Assumes {@link ASUser} as PUBLIC, {@link ASGroup} as ALL and {@link ASApplication} as STATUS
	 * 
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 */

	public List<ActivityStreamEntry> getActivityStream() throws SBTServiceException {
		return getActivityStreamEntries("", "", "", null);
	}

	/**
	 * Returns updates from ActivityStream service
	 * 
	 * @param User
	 *            see {@link ASUser} for possible values
	 * @param Group
	 *            see {@link ASGroup} for possible values
	 * @param User
	 *            see {@link ASApplication} for possible values
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 */

	public List<ActivityStreamEntry> getActivityStream(String user, String group, String app,
			Map<String, String> params) throws SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getActivityStream");
		}
		// Set the parameters being passed in by user
		return getActivityStreamEntries(user, group, app, params);
	}

	/**
	 * Wrapper method to get all updates from Activity Stream
	 * <p>
	 * Assumes {@link ASUser} as PUBLIC, {@link ASGroup} as ALL and {@link ASApplication} as ALL
	 * 
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 */
	public List<ActivityStreamEntry> getAllUpdatesStream() throws SBTServiceException {
		return getAllUpdatesStream(null);
	}

	/**
	 * Wrapper method to get all updates from Activity Stream
	 * <p>
	 * Assumes {@link ASUser} as PUBLIC, {@link ASGroup} as ALL and {@link ASApplication} as ALL
	 * 
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 */
	public List<ActivityStreamEntry> getAllUpdatesStream(Map<String, String> params)
			throws SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getAllUpdatesStream");
		}
		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		params.put(ActivityStreamRequestParams.rollUp, "true");
		return getActivityStreamEntries(ASUser.PUBLIC.getUserType(), ASGroup.ALL.getGroupType(),
				ASApplication.ALL.getApplicationType(), params);

	}

	/**
	 * Wrapper method to get all updates for user's network from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as ME, {@link ASGroup} as FRIENDS and {@link ASApplication} as ALL
	 * 
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 */

	public List<ActivityStreamEntry> getUpdatesFromMyNetwork() throws SBTServiceException {
		return getUpdatesFromMyNetwork(null);
	}

	/**
	 * Wrapper method to get all updates for user's network from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as ME, {@link ASGroup} as FRIENDS and {@link ASApplication} as ALL
	 * 
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 */
	public List<ActivityStreamEntry> getUpdatesFromMyNetwork(Map<String, String> params)
			throws SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getUpdatesFromMyNetwork");
		}

		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}

		params.put(ActivityStreamRequestParams.rollUp, "true");
		return getActivityStreamEntries(ASUser.ME.getUserType(), ASGroup.FRIENDS.getGroupType(),
				ASApplication.ALL.getApplicationType(), params);
	}

	/**
	 * Wrapper method to get all status updates for user's network from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as ME, {@link ASGroup} as FRIENDS and {@link ASApplication} as STATUS
	 * 
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 */
	public List<ActivityStreamEntry> getStatusUpdatesFromMyNetwork() throws SBTServiceException {
		return getStatusUpdatesFromMyNetwork(null);
	}

	/**
	 * Wrapper method to get all status updates for user's network from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as ME, {@link ASGroup} as FRIENDS and {@link ASApplication} as STATUS
	 * 
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 */
	public List<ActivityStreamEntry> getStatusUpdatesFromMyNetwork(Map<String, String> params)
			throws SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getUpdatesFromMyNetwork");
		}

		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}

		params.put(ActivityStreamRequestParams.rollUp, "true");
		return getActivityStreamEntries(ASUser.ME.getUserType(), ASGroup.FRIENDS.getGroupType(),
				ASApplication.STATUS.getApplicationType(), params);
	}

	/**
	 * Wrapper method to get all updates for all user's logged in user follows from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as ME, {@link ASGroup} as FOLLOWING and {@link ASApplication} as STATUS
	 * 
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 */
	public List<ActivityStreamEntry> getUpdatesFromPeopleIFollow() throws SBTServiceException {
		return getUpdatesFromPeopleIFollow(null);
	}

	/**
	 * Wrapper method to get all updates for all user's logged in user follows from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as ME, {@link ASGroup} as FOLLOWING and {@link ASApplication} as STATUS
	 * 
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 */
	public List<ActivityStreamEntry> getUpdatesFromPeopleIFollow(Map<String, String> params)
			throws SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getUpdatesFromPeopleIFollow");
		}
		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}

		params.put(ActivityStreamRequestParams.rollUp, "true");
		return getActivityStreamEntries(ASUser.ME.getUserType(), ASGroup.FOLLOWING.getGroupType(),
				ASApplication.STATUS.getApplicationType(), params);
	}

	/**
	 * Wrapper method to get Updates for Communities User Follows from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as ME, {@link ASGroup} as ALL and {@link ASApplication} as COMMUNITIES
	 * 
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 */
	public List<ActivityStreamEntry> getUpdatesFromCommunitiesIFollow() throws SBTServiceException {
		return getUpdatesFromCommunitiesIFollow(null);
	}

	/**
	 * Wrapper method to get Updates for Communities User Follows from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as ME, {@link ASGroup} as ALL and {@link ASApplication} as COMMUNITIES
	 * 
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 */
	public List<ActivityStreamEntry> getUpdatesFromCommunitiesIFollow(Map<String, String> params)
			throws SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getUpdatesFromCommunitiesIFollow");
		}

		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}

		params.put(ActivityStreamRequestParams.rollUp, "true");
		params.put(ActivityStreamRequestParams.broadcast, "true");
		return getActivityStreamEntries(ASUser.ME.getUserType(), ASGroup.ALL.getGroupType(),
				ASApplication.COMMUNITIES.getApplicationType(), params);
	}

	/**
	 * Wrapper method to get Updates for a specific User from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as userid in parameter, {@link ASGroup} as INVOLVED and {@link ASApplication} as ALL
	 * 
	 * @param id
	 *            Userid of the user whom updates are required
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	public List<ActivityStreamEntry> getUpdatesFromUser(String userId) throws SBTServiceException {
		return getUpdatesFromUser(userId, null);

	}

	/**
	 * Wrapper method to get Updates for a specific User from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as userid in parameter, {@link ASGroup} as INVOLVED and {@link ASApplication} as ALL
	 * 
	 * @param id
	 *            Userid of the user whose updates are required
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	public List<ActivityStreamEntry> getUpdatesFromUser(String userId, Map<String, String> params)
			throws SBTServiceException {

		if (StringUtil.isEmpty(userId)) {
			throw new IllegalArgumentException("userid passed was null");
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getUpdatesFromUser" + userId);
		}

		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}

		return getActivityStreamEntries(userId, ASGroup.INVOLVED.getGroupType(),
				ASApplication.ALL.getApplicationType(), params);

	}

	/**
	 * Wrapper method to get Updates for a specific Community from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as communityId in parameter, {@link ASGroup} as ALL and {@link ASApplication} as NOAPP
	 * 
	 * @param id
	 *            Community of the community for which updates are required
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	// Retuns updates from a particular Community
	public List<ActivityStreamEntry> getUpdatesFromCommunity(String communityId) throws SBTServiceException {
		return getUpdatesFromCommunity(communityId, null);

	}

	/**
	 * Wrapper method to get Updates for a specific Community from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as communityId in parameter, {@link ASGroup} as ALL and {@link ASApplication} as NOAPP
	 * 
	 * @param id
	 *            Community of the community for which updates are required
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	public List<ActivityStreamEntry> getUpdatesFromCommunity(String communityId, Map<String, String> params)
			throws SBTServiceException {

		if (StringUtil.isEmpty(communityId)) {
			throw new IllegalArgumentException("communityid passed was null");
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getUpdatesFromUser" + communityId);
		}

		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		return getActivityStreamEntries(ASUser.COMMUNITY.getUserType() + communityId,
				ASGroup.ALL.getGroupType(), ASApplication.NOAPP.getApplicationType(), params);

	}
	
	/**
	 * Wrapper method to get Filtered view of a user's stream based on notification events
	 * <p>
	 * Assumes {@link ASUser} as ME in parameter, {@link ASGroup} as NOTESFORME and {@link ASApplication} as NOAPP
	 * 
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	public List<ActivityStreamEntry> getNotificationsForMe(Map<String, String> params)
			throws SBTServiceException {

		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getNotificationsForMe");
		}

		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		return getActivityStreamEntries(ASUser.ME.getUserType(),ASGroup.NOTESFORME.getGroupType(), ASApplication.NOAPP.getApplicationType(), params);

	}
	
	
	/**
	 * Wrapper method to get Filtered view of a user's stream based on notification events
	 * <p>
	 * Assumes {@link ASUser} as ME in parameter, {@link ASGroup} as NOTESFROMME and {@link ASApplication} as NOAPP
	 * 
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	public List<ActivityStreamEntry> getNotificationsFromMe(Map<String, String> params)
			throws SBTServiceException {

		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getNotificationsFromMe");
		}

		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		return getActivityStreamEntries(ASUser.ME.getUserType(),ASGroup.NOTESFROMME.getGroupType(), ASApplication.NOAPP.getApplicationType(), params);

	}
	
	
	/**
	 * Wrapper method to get Filtered view of a user's stream based on notification events
	 * <p>
	 * Assumes {@link ASUser} as ME in parameter, {@link ASGroup} as RESPONSES and {@link ASApplication} as NOAPP
	 * 
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	public List<ActivityStreamEntry> getResponsesToMyContent(Map<String, String> params)
			throws SBTServiceException {

		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getResponsesToMyContent");
		}

		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		return getActivityStreamEntries(ASUser.ME.getUserType(),ASGroup.RESPONSES.getGroupType(), ASApplication.NOAPP.getApplicationType(), params);

	}
	
	
	/**
	 * Wrapper method to get view of Actionable  events
	 * <p>
	 * Assumes {@link ASUser} as ME in parameter, {@link ASGroup} as ACTION and {@link ASApplication} as NOAPP
	 * 
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	public List<ActivityStreamEntry> getMyActionableView(Map<String, String> params)
			throws SBTServiceException {

		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMyActionableView");
		}

		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		return getActivityStreamEntries(ASUser.ME.getUserType(),ASGroup.ACTION.getGroupType(), ASApplication.NOAPP.getApplicationType(), params);

	}
	
	
	/**
	 * Wrapper method to get view of Actionable  events
	 * <p>
	 * Assumes {@link ASUser} as ME in parameter, {@link ASGroup} as ACTION
	 * 
	 * @param id
	 * 			  Application type ( blogs/wikis etc. ) 
	 * 			  {@see http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Support_for_Saved_and_Actionable_events&content=pdcontent}
	 *            
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	public List<ActivityStreamEntry> getMyActionableViewForApplication(String application, Map<String, String> params)
			throws SBTServiceException {

		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMyActionableView");
		}

		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		return getActivityStreamEntries(ASUser.ME.getUserType(),ASGroup.ACTION.getGroupType(), application, params);

	}
	
	
	/**
	 * Wrapper method to get view of SAVED  events
	 * <p>
	 * Assumes {@link ASUser} as ME in parameter, {@link ASGroup} as SAVED and {@link ASApplication} as NOAPP
	 * 
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	public List<ActivityStreamEntry> getMySavedView(Map<String, String> params)
			throws SBTServiceException {

		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMySavedView");
		}

		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		return getActivityStreamEntries(ASUser.ME.getUserType(),ASGroup.SAVED.getGroupType(), ASApplication.NOAPP.getApplicationType(), params);

	}
	
	
	 /** Wrapper method to get view of SAVED  events
	 * <p>
	 * Assumes {@link ASUser} as ME in parameter, {@link ASGroup} as SAVED
	 * 
	 * @param id
	 * 			  Application type ( blogs/wikis etc. ) 
	 * 			  {@see http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Support_for_Saved_and_Actionable_events&content=pdcontent}
	 *            
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	public List<ActivityStreamEntry> getMySavedViewForApplication(String application, Map<String, String> params)
			throws SBTServiceException {

		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMyActionableView");
		}

		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		return getActivityStreamEntries(ASUser.ME.getUserType(),ASGroup.SAVED.getGroupType(), application, params);

	}
	
	// Microblogging apis
	

	/**
	 * Wrapper method to fetch updates from logged in user board
	 * <p>
	 * Assumes {@link ASUser} as ME , {@link ASGroup} as ALL and {@link ASApplication} as NOAPP
	 * @param params
	 * 		Additional parameters used for constructing URL's
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	public List<ActivityStreamEntry> getUpdatesFromMyBoard() throws SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getUpdatesFromMyBoard");
		}
		return getUpdatesFromMyBoard(null);
	}
	
	
	/**
	 * Wrapper method to fetch updates from logged in user board
	 * <p>
	 * Assumes {@link ASUser} as ME , {@link ASGroup} as ALL and {@link ASApplication} as NOAPP
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	public List<ActivityStreamEntry> getUpdatesFromMyBoard(Map<String, String> params)throws SBTServiceException{
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getUpdatesFromMyBoard");
		}
		
		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		
		return getMicroblogEntries(ASUser.ME.getUserType(),ASGroup.ALL.getGroupType(), ASApplication.NOAPP.getApplicationType(), params);
		
	}
	
	
	

	/**
	 * Wrapper method to search in ActivityStreams
	 * <p>
	 * Assumes {@link ASUser} as PUBLIC , {@link ASGroup} as ALL and {@link ASApplication} as ALL
	 * 
	 * @param query
	 *            String to be searched for
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	public List<ActivityStreamEntry> searchForQuery(String query) throws SBTServiceException {
		return searchForQuery(query, null);
	}

	/**
	 * Wrapper method to search in ActivityStreams
	 * <p>
	 * Assumes {@link ASUser} as PUBLIC , {@link ASGroup} as ALL and {@link ASApplication} as ALL
	 * 
	 * @param query
	 *            String to be searched for
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	public List<ActivityStreamEntry> searchForQuery(String query, Map<String, String> params)
			throws SBTServiceException {

		if (StringUtil.isEmpty(query)) {
			throw new IllegalArgumentException("query passed was null");
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "searchFor" + query);
		}

		// /@public/@all/@all?rollup=true&query=manish
		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		params.put(ActivityStreamRequestParams.query, query);
		return getActivityStreamEntries(ASUser.PUBLIC.getUserType(), ASGroup.ALL.getGroupType(),
				ASApplication.ALL.getApplicationType(), params);
	}

	/**
	 * Wrapper method to search by tags in ActivityStreams
	 * <p>
	 * Assumes {@link ASUser} as PUBLIC , {@link ASGroup} as ALL and {@link ASApplication} as ALL
	 * 
	 * @param query
	 *            Tag to be searched for ( in case of multiple tags, provide comma separated String )
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	public List<ActivityStreamEntry> searchForTags(String tags) throws SBTServiceException {
		return searchForTags(tags, null);
	}

	/**
	 * Wrapper method to search by tags in ActivityStreams
	 * <p>
	 * Assumes {@link ASUser} as PUBLIC , {@link ASGroup} as ALL and {@link ASApplication} as ALL
	 * 
	 * @param query
	 *            Tag to be searched for ( in case of multiple tags, provide comma separated String )
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	public List<ActivityStreamEntry> searchForTags(String tags, Map<String, String> params)
			throws SBTServiceException {

		if (StringUtil.isEmpty(tags)) {
			throw new IllegalArgumentException("tags passed was null");
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "searchForTags" + tags);
		}
		// /@me/@all/@all?rollup=true&filters=[{'type':'tag','values':['iphone','android']}]&facetRequests=[{'people':5}]

		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}

		params.put(ActivityStreamRequestParams.rollUp, "true");
		if (!(tags.contains(","))) {
			params.put(ActivityStreamRequestParams.filters, "[{'type':'tag','values':['" + tags + "']}]");
		} else {
			StringBuffer modifiedQuery = new StringBuffer();
			StringTokenizer tokenizer = new StringTokenizer(tags, ",");
			boolean addseperator = false;
			while (tokenizer.hasMoreElements()) {
				if (addseperator) {
					modifiedQuery.append(",");
				}
				modifiedQuery.append("'" + tokenizer.nextElement().toString() + "'");
				addseperator = true;
			}
			params.put(ActivityStreamRequestParams.filters, "[{'type':'tag','values':[" + modifiedQuery
					+ "]}]");
		}
		return getActivityStreamEntries(ASUser.PUBLIC.getUserType(), ASGroup.ALL.getGroupType(),
				ASApplication.ALL.getApplicationType(), params);
	}

	/**
	 * Wrapper method to search in ActivityStreams using Search filters
	 * <p>
	 * Assumes {@link ASUser} as PUBLIC , {@link ASGroup} as ALL and {@link ASApplication} as ALL
	 * 
	 * @param query
	 *            FilterType to be searched for
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	public List<ActivityStreamEntry> searchbyFilters(String filterType, String query)
			throws SBTServiceException {
		return searchbyFilters(filterType, query, null);
	}

	/**
	 * Wrapper method to search in ActivityStreams using Search filters
	 * <p>
	 * Assumes {@link ASUser} as PUBLIC , {@link ASGroup} as ALL and {@link ASApplication} as ALL
	 * 
	 * @param query
	 *            FilterType to be searched for
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	public List<ActivityStreamEntry> searchbyFilters(String filterType, String query,
			Map<String, String> params) throws SBTServiceException {

		if (StringUtil.isEmpty(filterType)) {
			throw new IllegalArgumentException("filterType passed was null");
		}

		if (StringUtil.isEmpty(query)) {
			throw new IllegalArgumentException("query passed was null");
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "searchbyFilters" + query);
		}
		// /@me/@all/@all?rollup=true&filters=[{'type':'tag','values':['iphone','android']}]&facetRequests=[{'people':5}]

		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}

		params.put(ActivityStreamRequestParams.rollUp, "true");
		if (!(query.contains(","))) {
			params.put(ActivityStreamRequestParams.filters, "[{'type':'" + filterType + "','values':['"
					+ query + "']}]");
		} else {
			StringBuffer modifiedQuery = new StringBuffer();
			StringTokenizer tokenizer = new StringTokenizer(query, ",");
			boolean addseperator = false;
			while (tokenizer.hasMoreElements()) {
				if (addseperator) {
					modifiedQuery.append(",");
				}
				modifiedQuery.append("'" + tokenizer.nextElement().toString() + "'");
				addseperator = true;
			}
			params.put(ActivityStreamRequestParams.filters, "[{'type':'" + filterType + "','values':["
					+ modifiedQuery + "]}]");
		}
		return getActivityStreamEntries(ASUser.PUBLIC.getUserType(), ASGroup.ALL.getGroupType(),
				ASApplication.ALL.getApplicationType(), params);
	}

	/**
	 * Wrapper method to search in ActivityStreams using Search pattern
	 * <p>
	 * Assumes {@link ASUser} as PUBLIC , {@link ASGroup} as ALL and {@link ASApplication} as ALL
	 * 
	 * @param Complete
	 *            search pattern, check Connections documentation for generating Search patterns
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	// Generic Search
	public List<ActivityStreamEntry> search(String searchpattern) throws SBTServiceException {
		return search(searchpattern, null);
	}

	/**
	 * Wrapper method to search in ActivityStreams using Search pattern
	 * <p>
	 * Assumes {@link ASUser} as PUBLIC , {@link ASGroup} as ALL and {@link ASApplication} as ALL
	 * 
	 * @param searchpattern
	 *            Complete search pattern, check Connections documentation for generating Search patterns
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	public List<ActivityStreamEntry> search(String searchpattern, Map<String, String> params)
			throws SBTServiceException {

		if (StringUtil.isEmpty(searchpattern)) {
			throw new IllegalArgumentException("searchpattern passed was null");
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "search" + searchpattern);
		}
		// /@me/@all/@all?rollup=true&filters=[{'type':'tag','values':['iphone','android']}]&facetRequests=[{'people':5}]

		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}

		params.put(ActivityStreamRequestParams.rollUp, "true");
		params.put(ActivityStreamRequestParams.custom, searchpattern);
		return getActivityStreamEntries(ASUser.PUBLIC.getUserType(), ASGroup.ALL.getGroupType(),
				ASApplication.ALL.getApplicationType(), params);
	}
	
	
	
	/**
	 * Wrapper method to fetch updates from Activity Stream
	 * Constructs the api using the inputs provided in parameters, makes the network call and returns the ActivityStreamEntry list.
	 * <p>
	 * @param User see {@link ASUser} for possible values
	 * @param Group see {@link ASGroup} for possible values
	 * @param User see {@link ASApplication} for possible values
	 * @param params Additional parameters used for constructing URL's
	 * @throws SBTServiceException
	 */

	public List<ActivityStreamEntry> getActivityStreamEntries(String user, String group, String app,
			Map<String, String> params) throws SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getActivityStreamEntries");
		}

		Map<String, String> parameters = new HashMap<String, String>();
		try {
			JsonObject listOfActivityStreamEnteries = (JsonObject) endpoint.xhrGet(
					resolveUrlForFetchingAS(user, group, app, params), parameters, ClientService.FORMAT_JSON);
			return populateActivityEntryData(listOfActivityStreamEnteries);

		} catch (ClientServicesException e) {
			Object[] args = new Object[] { user, group, app };
			String message = MessageFormat.format(
					"Error retrieving activity stream entries for user:{0} group:{1} app:{2}", args);
			logger.log(Level.SEVERE, message, e);
			throw new ActivityStreamServiceException(e);
		}

	}
	
	
	/**
	 * Returns the updates from User Boards. 
	 * <p>
	 * @param User see {@link ASUser} for possible values
	 * @param Group see {@link ASGroup} for possible values
	 * @param User see {@link ASApplication} for possible values
	 * @param params Additional parameters used for constructing URL's
	 * @throws SBTServiceException
	 */

	public List<ActivityStreamEntry> getMicroblogEntries(String user, String group, String app,
			Map<String, String> params) throws SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getActivityStreamEntries");
		}

		Map<String, String> parameters = new HashMap<String, String>();
		try {
			JsonObject listOfMicroBlogEnteries = (JsonObject) endpoint.xhrGet(
					resolveUrlForFetchingMicroblog(user, group, app, params), parameters, ClientService.FORMAT_JSON);
			return populateMicroBlogEntryData(listOfMicroBlogEnteries);

		} catch (ClientServicesException e) {
			Object[] args = new Object[] { user, group, app };
			String message = MessageFormat.format(
					"Error retrieving activity stream entries for user:{0} group:{1} app:{2}", args);
			logger.log(Level.SEVERE, message, e);
			throw new ActivityStreamServiceException(e);
		}

	}

	/*
	 * populateActivityEntryData converts the JsonObject into List of ActivityStreamEntry.
	 */
	private List<ActivityStreamEntry> populateActivityEntryData(JsonObject listOfActivityStremEnteries) {

		List<ActivityStreamEntry> _activityStreamEntry = new ArrayList<ActivityStreamEntry>();

		try {
			DataNavigator.Json nav = new DataNavigator.Json(listOfActivityStremEnteries); // this.data has the response feed.
			DataNavigator entry = nav.get("list");
			int numberOfStreamUpdates = entry.getCount();
			for (int i = 0; i < numberOfStreamUpdates; i++) {

				// Create and populate the ActivityStreamEntryObject
				ActivityStreamEntry entryObject = ActivityStreamEntry.createActivityStreamEntryObject(entry
						.get(i));
				_activityStreamEntry.add(entryObject);
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, "populateActivityEntryData caused exception", e);
		}

		return _activityStreamEntry;
	}
	
	
	/*
	 * populateActivityEntryData converts the JsonObject into List of ActivityStreamEntry.
	 */
	private List<ActivityStreamEntry> populateMicroBlogEntryData(JsonObject listOfActivityStremEnteries) {

		List<ActivityStreamEntry> microBlogEntryList = new ArrayList<ActivityStreamEntry>();

		try {
			DataNavigator.Json nav = new DataNavigator.Json(listOfActivityStremEnteries); // this.data has the response feed.
			DataNavigator entry = nav.get("list");
			int numberOfStreamUpdates = entry.getCount();
			for (int i = 0; i < numberOfStreamUpdates; i++) {

				// Create and populate the ActivityStreamEntryObject
				ActivityStreamEntry entryObject = ActivityStreamEntry.createMicroBlogEntryObject(entry
						.get(i));
				microBlogEntryList.add(entryObject);
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, "populateActivityEntryData caused exception", e);
		}

		return microBlogEntryList;
	}

	/*
	 * Method responsible for generating appropriate REST URLs
	 */
	private String resolveUrlForFetchingAS(String user, String group, String application,
			Map<String, String> params) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "resolveUrlForFetchingAS");
		}

		StringBuilder streamUrl = new StringBuilder(baseUrl);
		streamUrl.append(AuthUtil.INSTANCE.getAuthValue(endpoint)).append(restUrl); // Basic or Oauth supported for now
		
		streamUrl.append(activityStreamsUrl); // include a condition here to switch this to board url

		return resolveUrlForFetch(streamUrl,user,group,application,params);

	}
	
	private String resolveUrlForFetchingMicroblog(String user, String group, String application,
			Map<String, String> params){
		
		StringBuilder streamUrl = new StringBuilder(baseUrl);
		streamUrl.append(AuthUtil.INSTANCE.getAuthValue(endpoint)).append(restUrl); // Basic or Oauth supported for now
		
		streamUrl.append(boardUrl); // include a condition here to switch this to board url
		return resolveUrlForFetch(streamUrl,user,group,application,params);
		
	}
	
	private String resolveUrlForFetch(StringBuilder baseurl, String user, String group, String application,
			Map<String, String> params){
		
		if (StringUtil.isEmpty(group)) {
			group = ASGroup.ALL.getGroupType();
		}

		if (StringUtil.isEmpty(user)) {
			user = ASUser.PUBLIC.getUserType();
		}

		if (StringUtil.isEmpty(application)) {
			application = ASApplication.STATUS.getApplicationType();
		}

		baseurl.append(seperator).append(user).append(seperator).append(group);
		if (!(application.equals(ASApplication.NOAPP.getApplicationType()))) {
			baseurl.append(seperator + application);
		}

		// Add required parameters
		if (null != params) {
			if (params.size() > 0) {
				baseurl.append("?");
				boolean setSeperator = false;
				for (Map.Entry<String, String> param : params.entrySet()) {
					if (setSeperator) {
						baseurl.append("&");
					}
					String paramvalue = "";
					try {
						paramvalue = URLEncoder.encode(param.getValue(), "UTF-8");
					} catch (UnsupportedEncodingException e) {}
					if (param.getKey().equals(ActivityStreamRequestParams.custom)) { // this indicates user passed in custom value, we do not add key
						baseurl.append(paramvalue);
					} else {
						baseurl.append(param.getKey() + "=" + paramvalue);
					}
					setSeperator = true;
				}
			}
		} else { // Parameters were null, default to english language
			baseurl.append("?" + ActivityStreamRequestParams.lang + "=" + getUserLanguage());
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.log(Level.FINEST, "resolved URL :" + baseurl.toString());
		}
		return baseurl.toString();
		
	}

	/*
	 * Method responsible for generating appropriate REST URLs for POST functionality
	 */
	private String resolveUrlForPostingAS(String user, String group, String application) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "resolveUrlForPostingAS");
		}

		StringBuilder streamUrl = new StringBuilder(baseUrl);
		streamUrl.append(AuthUtil.INSTANCE.getAuthValue(endpoint)).append(restUrl); // Basic or Oauth
		
		streamUrl.append(activityStreamsUrl); // include a condition here to switch this to board url

		if (StringUtil.isEmpty(user)) {
			user = ASUser.PUBLIC.getUserType();
		}

		if (StringUtil.isEmpty(group)) {
			group = ASGroup.ALL.getGroupType();
		}

		streamUrl.append(seperator + user).append(seperator + group);
		if (!(StringUtil.isEmpty(application))) {
			streamUrl.append(seperator + application);
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.log(Level.FINEST, "resolved URL for POST:" + streamUrl.toString());
		}
		return streamUrl.toString();
	}

	/**
	 * postEntry Creates Activity Stream entry
	 * 
	 * @param user
	 * @param group
	 * @param application
	 * @param jsondata
	 * @param header
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */
	public void postEntry(String user, String group, String application, JsonJavaObject postPayload,
			Map<String, String> header) throws SBTServiceException {

		if (null == postPayload) {
			throw new IllegalArgumentException("postPayload passed was null");
		}

		try {

			ClientService svc = endpoint.getClientService();
			svc.post(resolveUrlForPostingAS(user, group, ""), null, header, postPayload,
					ClientService.FORMAT_JSON);

		} catch (ClientServicesException e) {
			logger.log(Level.SEVERE, "postEvent caused exception", e);
			throw new ActivityStreamServiceException(e);
		}
	}

	/**
	 * postEntry Creates Activity Stream entry
	 * 
	 * @param jsondata
	 * @param header
	 * @return List<ActivityStreamEntry>
	 * @throws SBTServiceException
	 *             ,IllegalArgumentException
	 */

	/*
	 * This method allows user to set json payload and headers Rest of the parameters like user,group and app are assumed to be default
	 */
	public JsonJavaObject postEntry(JsonJavaObject postPayload, Map<String, String> header) throws SBTServiceException {

		if (null == postPayload) {
			throw new IllegalArgumentException("postPayload passed was null");
		}

		try {
			ClientService svc = endpoint.getClientService();
			return (JsonJavaObject)svc.post(
					resolveUrlForPostingAS(ASUser.ME.getUserType(), ASGroup.ALL.getGroupType(),
							ASApplication.ALL.getApplicationType()), null, header, postPayload,
					ClientService.FORMAT_JSON);			
		} catch (ClientServicesException e) {
			logger.log(Level.SEVERE, "postEvent caused exception", e);
			throw new ActivityStreamServiceException(e);
		}
	}

	private String getUserLanguage() {
		return "en";

	}

}
