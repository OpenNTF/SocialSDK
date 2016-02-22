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

package com.ibm.sbt.services.client.connections.activitystreams;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.ConnectionsService;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.activitystreams.transformers.ActivityStreamTransformer;
import com.ibm.sbt.services.endpoints.Endpoint;


/**
 * ActivityStreamService can be used to perform operations related to ActivityStream.
 * <p>
 * Relies on values of User, Group and Application to construct URLs and perform Network operations. Constructs {@link ActivityStreamEntity} objects after parsing the JSON response from Connections server
 * 
 * @author Manish Kataria
 * @author Carlos Manias
 *         <pre>
 * Sample Usage
 *  {@code
 * 	ActivityStreamService _service = new ActivityStreamService();
 * 	ActivityStreamEntityList _entries = (List) _service.getUpdatesFromMyNetwork();
 * }
 * </pre>
 * @see <a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=IBM_Connections_Activity_Stream_API&content=pdcontent">Activity Stream API</a>
 */
public class ActivityStreamService extends ConnectionsService {
	private static final long serialVersionUID = -1169787598206507188L;
	private final ActivityStreamFeedHandler activityStreamFeedHandler = new ActivityStreamFeedHandler(this);
	// Typical url pattern /activitystream/user/group/application

	/**
	 * Constructor Creates ActivityStreamService Object with default endpoint
	 */
	public ActivityStreamService() {
		this(DEFAULT_ENDPOINT_NAME);
	}

	/**
	 * Constructor
	 * 
	 * @param endpoint
	 *            Creates ActivityStreamService Object with the specified endpoint
	 */
	public ActivityStreamService(String endpoint) {
		super(endpoint, DEFAULT_CACHE_SIZE);
	}
	
	/**
	 * Constructor
	 * 
	 * @param endpoint
	 *            Creates ActivityStreamService Object with the specified endpoint
	 */
	public ActivityStreamService(Endpoint endpoint) {
		super(endpoint, DEFAULT_CACHE_SIZE);
	}
	/**
	 * Returns updates from ActivityStream service
	 * <p>
	 * Assumes {@link ASUser} as PUBLIC, {@link ASGroup} as ALL and {@link ASApplication} as STATUS
	 * 
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */

	public ActivityStreamEntityList getStream() throws ActivityStreamServiceException {
		String url = ActivityStreamUrls.AS_PUBLIC_ALL_STATUS.format(this);
		return getActivityStreamEntities(url, null);
	}
	
	/**
	 * Returns updates from ActivityStream service
	 * 
	 * @param user
	 *            see {@link ASUser} for possible values
	 * @param group
	 *            see {@link ASGroup} for possible values
	 * @param app
	 *            see {@link ASApplication} for possible values
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getStream(String user, String group, String app,
			Map<String, String> params) throws ActivityStreamServiceException {

		// Set the parameters being passed in by user
		String url = ActivityStreamUrls.AS_USER_GROUP_APP.format(this, ASUser.getByName(user), ASGroup.getByName(group), ASApplication.getByName(app));
		return getActivityStreamEntities(url, params); 
	}

	/**
	 * Returns updates from ActivityStream service
	 * 
	 * @param user
	 *            see {@link ASUser} for possible values
	 * @param group
	 *            see {@link ASGroup} for possible values
	 * @param app
	 *            see {@link ASApplication} for possible values
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getStream(NamedUrlPart user, NamedUrlPart group, NamedUrlPart app,
			Map<String, String> params) throws ActivityStreamServiceException {

		// Set the parameters being passed in by user
		String url = ActivityStreamUrls.AS_USER_GROUP_APP.format(this, user, group, app);
		return getActivityStreamEntities(url, params); 
	}
	

	/**
	 * Wrapper method to get all updates from Activity Stream
	 * <p>
	 * Assumes {@link ASUser} as PUBLIC, {@link ASGroup} as ALL and {@link ASApplication} as ALL
	 * 
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getAllUpdates() throws ActivityStreamServiceException {
		return getAllUpdates(null);
	}

	/**
	 * Wrapper method to get all updates from Activity Stream
	 * <p>
	 * Assumes {@link ASUser} as PUBLIC, {@link ASGroup} as ALL and {@link ASApplication} as ALL
	 * 
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getAllUpdates(Map<String, String> params)
			throws ActivityStreamServiceException {
		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		params.put(ActivityStreamRequestParams.rollUp, "true");
		String url = ActivityStreamUrls.AS_PUBLIC_ALL_ALL.format(this);
		return getActivityStreamEntities(url, params);

	}
	
	/**
	 * Wrapper method to get all updates for user's network from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as ME, {@link ASGroup} as FRIENDS and {@link ASApplication} as ALL
	 * 
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getUpdatesFromMyNetwork() throws ActivityStreamServiceException {
		return getUpdatesFromMyNetwork(null);
	}

	/**
	 * Wrapper method to get all updates for user's network from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as ME, {@link ASGroup} as FRIENDS and {@link ASApplication} as ALL
	 * 
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getUpdatesFromMyNetwork(Map<String, String> params)
			throws ActivityStreamServiceException {
		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}

		params.put(ActivityStreamRequestParams.rollUp, "true");
		String url = ActivityStreamUrls.AS_ME_FRIENDS_ALL.format(this);
		return getActivityStreamEntities(url, params);
	}
	
	/**
	 * Wrapper method to get all status updates for user's network from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as ME, {@link ASGroup} as FRIENDS and {@link ASApplication} as STATUS
	 * 
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getStatusUpdatesFromMyNetwork() throws ActivityStreamServiceException {
		return getStatusUpdatesFromMyNetwork(null);
	}

	/**
	 * Wrapper method to get all status updates for user's network from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as ME, {@link ASGroup} as FRIENDS and {@link ASApplication} as STATUS
	 * 
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getStatusUpdatesFromMyNetwork(Map<String, String> params)
			throws ActivityStreamServiceException {
		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}

		params.put(ActivityStreamRequestParams.rollUp, "true");
		String url = ActivityStreamUrls.AS_ME_FRIENDS_STATUS.format(this);
		return getActivityStreamEntities(url, params);
	}
	
	/**
	 * Wrapper method to get all status updates for logged in user
	 * <p>
	 * Assumes {@link ASUser} as ME, {@link ASGroup} as ALL and {@link ASApplication} as STATUS
	 * 
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getMyStatusUpdates() throws ActivityStreamServiceException {
		return getMyStatusUpdates(null);
	}
	
	/**
	 * Wrapper method to get all status updates for logged in user
	 * <p>
	 * Assumes {@link ASUser} as ME, {@link ASGroup} as ALL and {@link ASApplication} as STATUS
	 * @param params
	 * 	Additional parameters used for constructing URL's
	 * 
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getMyStatusUpdates(Map<String, String> params) throws ActivityStreamServiceException {
		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		String url = ActivityStreamUrls.AS_ME_ALL_STATUS.format(this);
		return getActivityStreamEntities(url, params);
	}
	
	/**
	 * Wrapper method to get all updates for all user's logged in user follows from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as ME, {@link ASGroup} as FOLLOWING and {@link ASApplication} as STATUS
	 * 
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getUpdatesFromPeopleIFollow() throws ActivityStreamServiceException {
		return getUpdatesFromPeopleIFollow(null);
	}

	/**
	 * Wrapper method to get all updates for all user's logged in user follows from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as ME, {@link ASGroup} as FOLLOWING and {@link ASApplication} as STATUS
	 * 
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getUpdatesFromPeopleIFollow(Map<String, String> params)
			throws ActivityStreamServiceException {
		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}

		params.put(ActivityStreamRequestParams.rollUp, "true");
		String url = ActivityStreamUrls.AS_ME_FOLLOWING_STATUS.format(this);
		return getActivityStreamEntities(url, params);
	}
	
	
	
	/**
	 * Wrapper method to get Updates for Communities User Follows from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as ME, {@link ASGroup} as ALL and {@link ASApplication} as COMMUNITIES
	 * 
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getUpdatesFromCommunitiesIFollow() throws ActivityStreamServiceException {
		return getUpdatesFromCommunitiesIFollow(null);
	}

	/**
	 * Wrapper method to get Updates for Communities User Follows from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as ME, {@link ASGroup} as ALL and {@link ASApplication} as COMMUNITIES
	 * 
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getUpdatesFromCommunitiesIFollow(Map<String, String> params)
			throws ActivityStreamServiceException {
		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}

		params.put(ActivityStreamRequestParams.rollUp, "true");
		params.put(ActivityStreamRequestParams.broadcast, "true");
		String url = ActivityStreamUrls.AS_ME_ALL_COMMUNITIES.format(this);
		return getActivityStreamEntities(url, params);
	}
	
	/**
	 * Wrapper method to get Updates for a specific User from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as userid in parameter, {@link ASGroup} as INVOLVED and {@link ASApplication} as ALL
	 * 
	 * @param userId
	 *            Userid of the user whom updates are required
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 *             
	 */
	public ActivityStreamEntityList getUpdatesFromUser(String userId) throws ActivityStreamServiceException {
		return getUpdatesFromUser(userId, null);

	}

	/**
	 * Wrapper method to get Updates for a specific User from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as userid in parameter, {@link ASGroup} as INVOLVED and {@link ASApplication} as ALL
	 * 
	 * @param userId
	 *            Userid of the user whose updates are required
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getUpdatesFromUser(String userId, Map<String, String> params)
			throws ActivityStreamServiceException {
		String url = ActivityStreamUrls.AS_USER_INVOLVED_ALL.format(this, ASUser.get(userId), ActivityStreamUrls.getLang(getUserLanguage()));
		return getActivityStreamEntities(url, params);
	}
	
	
	/**
	 * Wrapper method to get Updates for a specific Community from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as communityId in parameter, {@link ASGroup} as ALL and {@link ASApplication} as NOAPP
	 * 
	 * @param communityId
	 *            Community of the community for which updates are required
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	// Retuns updates from a particular Community
	public ActivityStreamEntityList getUpdatesFromCommunity(String communityId) throws ActivityStreamServiceException {
		return getUpdatesFromCommunity(communityId, null);
	}

	/**
	 * Wrapper method to get Updates for a specific Community from Activity Streams
	 * <p>
	 * Assumes {@link ASUser} as communityId in parameter, {@link ASGroup} as ALL and {@link ASApplication} as NOAPP
	 * 
	 * @param communityId
	 *            Community of the community for which updates are required
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getUpdatesFromCommunity(String communityId, Map<String, String> params)
			throws ActivityStreamServiceException {
		if (StringUtil.isEmpty(communityId)) {
			throw new ActivityStreamServiceException(null,"communityid passed was null");
		}

		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		String url = ActivityStreamUrls.AS_COMMUNITY_ALL.format(this, ASUser.COMMUNITY.getWithValue(communityId));
		return getActivityStreamEntities(url, params);

	}
	
	/**
	 * Wrapper method to get Filtered view of a user's stream based on notification events
	 * <p>
	 * Assumes {@link ASUser} as ME in parameter, {@link ASGroup} as RESPONSES and {@link ASApplication} as NOAPP
	 * 
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getNotificationsForMe(Map<String, String> params)
			throws ActivityStreamServiceException {
		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		String url = ActivityStreamUrls.AS_ME_RESPONSES.format(this);
		return getActivityStreamEntities(url, params);
	}
	
	/**
	 * Wrapper method to get Filtered view of a user's stream based on notification events
	 * <p>
	 * Assumes {@link ASUser} as ME in parameter, {@link ASGroup} as NOTESFROMME and {@link ASApplication} as NOAPP
	 * 
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getNotificationsFromMe(Map<String, String> params)
			throws ActivityStreamServiceException {
		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		String url = ActivityStreamUrls.AS_ME_NOTESFROMME.format(this);
		return getActivityStreamEntities(url, params);
	}
	
	/**
	 * Wrapper method to get Filtered view of a user's stream based on notification events
	 * <p>
	 * Assumes {@link ASUser} as ME in parameter, {@link ASGroup} as RESPONSES and {@link ASApplication} as NOAPP
	 * 
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getResponsesToMyContent(Map<String, String> params)
			throws ActivityStreamServiceException {
		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		String url = ActivityStreamUrls.AS_ME_RESPONSES.format(this);
		return getActivityStreamEntities(url, params);
	}
	
	
	/**
	 * Wrapper method to get view of Actionable  events
	 * <p>
	 * Assumes {@link ASUser} as ME in parameter, {@link ASGroup} as ACTION and {@link ASApplication} as NOAPP
	 * 
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getMyActionableItems(Map<String, String> params)
			throws ActivityStreamServiceException {
		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		String url = ActivityStreamUrls.AS_ME_ACTION.format(this);
		return getActivityStreamEntities(url, params);
	}
	
	
	/**
	 * Wrapper method to get view of Actionable  events
	 * <p>
	 * Assumes {@link ASUser} as ME in parameter, {@link ASGroup} as ACTION
	 * 
	 * @param application
	 * 			  Application type ( blogs/wikis etc. ) 
	 * 			  @see <a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Support_for_Saved_and_Actionable_events&content=pdcontent">Saved and Actionable Events API</a>
	 *            
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getMyActionableItemsForApplication(String application, Map<String, String> params)
			throws ActivityStreamServiceException {
		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		String url = ActivityStreamUrls.AS_ME_ACTION_APP.format(this, ASApplication.get(application));
		return getActivityStreamEntities(url, params);
	}
	
	
	/**
	 * Wrapper method to get view of SAVED  events
	 * <p>
	 * Assumes {@link ASUser} as ME in parameter, {@link ASGroup} as SAVED and {@link ASApplication} as NOAPP
	 * 
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getMySavedItems(Map<String, String> params)
			throws ActivityStreamServiceException {
		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		String url = ActivityStreamUrls.AS_ME_SAVED.format(this);
		return getActivityStreamEntities(url, params);
	}
	
	
	
	 /** Wrapper method to get view of SAVED  events
	 * <p>
	 * Assumes {@link ASUser} as ME in parameter, {@link ASGroup} as SAVED
	 * 
	 * @param application
	 * 			  Application type ( blogs/wikis etc. ) 
	 * 			  @see <a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Support_for_Saved_and_Actionable_events&content=pdcontent">Saved and Actionable Content</a>
	 *            
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList getMySavedItemsForApplication(String application, Map<String, String> params)
			throws ActivityStreamServiceException {
		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		String url = ActivityStreamUrls.AS_ME_SAVED_APP.format(this,ASApplication.get(application));
		return getActivityStreamEntities(url, params);
	}
	
	
	/*
	 * Search wrappers
	 */
	
	/**
	 * Wrapper method to search in ActivityStreams
	 * <p>
	 * Assumes {@link ASUser} as PUBLIC , {@link ASGroup} as ALL and {@link ASApplication} as ALL
	 * 
	 * @param query
	 *            String to be searched for
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList searchByQuery(String query) throws ActivityStreamServiceException {
		return searchByQuery(query, null);
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
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList searchByQuery(String query, Map<String, String> params)
			throws ActivityStreamServiceException {

		if (StringUtil.isEmpty(query)) {
			throw new ActivityStreamServiceException(null,"query passed was null");
		}

		// /@public/@all/@all?rollup=true&query=manish
		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}
		params.put(ActivityStreamRequestParams.query, query);
		String url = ActivityStreamUrls.AS_PUBLIC_ALL_ALL.format(this);
		return getActivityStreamEntities(url, params);
	}
	
	
	/**
	 * Wrapper method to search by tags in ActivityStreams
	 * <p>
	 * Assumes {@link ASUser} as PUBLIC , {@link ASGroup} as ALL and {@link ASApplication} as ALL
	 * 
	 * @param tags
	 *            Tag to be searched for ( in case of multiple tags, provide comma separated String )
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList searchByTags(String tags) throws ActivityStreamServiceException {
		return searchByTags(tags, null);
	}

	/**
	 * Wrapper method to search by tags in ActivityStreams
	 * <p>
	 * Assumes {@link ASUser} as PUBLIC , {@link ASGroup} as ALL and {@link ASApplication} as ALL
	 * 
	 * @param tags
	 *            Tag to be searched for ( in case of multiple tags, provide comma separated String )
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList searchByTags(String tags, Map<String, String> params)
			throws ActivityStreamServiceException {

		if (StringUtil.isEmpty(tags)) {
			throw new ActivityStreamServiceException(null,"tags passed was null");
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
		String url = ActivityStreamUrls.AS_PUBLIC_ALL_ALL.format(this);
		return getActivityStreamEntities(url, params);
	}
	
	
	/**
	 * Wrapper method to search in ActivityStreams using Search filters
	 * <p>
	 * Assumes {@link ASUser} as PUBLIC , {@link ASGroup} as ALL and {@link ASApplication} as ALL
	 * 
	 * @param filterType the fitler type that is used
	 * @param query query to be used
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList searchByFilters(String filterType, String query)
			throws ActivityStreamServiceException {
		return searchByFilters(filterType, query, null);
	}

	/**
	 * Wrapper method to search in ActivityStreams using Search filters
	 * <p>
	 * Assumes {@link ASUser} as PUBLIC , {@link ASGroup} as ALL and {@link ASApplication} as ALL
	 * 
	 * @param filterType the fitler type that is used
	 * @param query
	 *            FilterType to be searched for
	 * @param params
	 *            Additional parameters used for constructing URL's
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList searchByFilters(String filterType, String query,
			Map<String, String> params) throws ActivityStreamServiceException {

		if (StringUtil.isEmpty(filterType)) {
			throw new ActivityStreamServiceException(null,"filterType passed was null");
		}

		if (StringUtil.isEmpty(query)) {
			throw new ActivityStreamServiceException(null,"query passed was null");
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
		String url = ActivityStreamUrls.AS_PUBLIC_ALL_ALL.format(this);
		return getActivityStreamEntities(url, params);
	}
	
	
	/**
	 * Wrapper method to search in ActivityStreams using Search pattern
	 * <p>
	 * Assumes {@link ASUser} as PUBLIC , {@link ASGroup} as ALL and {@link ASApplication} as ALL
	 * 
	 * @param searchpattern
	 *            search pattern, check Connections documentation for generating Search patterns
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	// Generic Search
	public ActivityStreamEntityList searchByPattern(String searchpattern) throws ActivityStreamServiceException {
		return searchByPattern(searchpattern, null);
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
	 * @return ActivityStreamEntityList
	 * @throws ActivityStreamServiceException
	 */
	public ActivityStreamEntityList searchByPattern(String searchpattern, Map<String, String> params)
			throws ActivityStreamServiceException {

		if (StringUtil.isEmpty(searchpattern)) {
			throw new ActivityStreamServiceException(null,"searchpattern passed was null");
		}


		// /@me/@all/@all?rollup=true&filters=[{'type':'tag','values':['iphone','android']}]&facetRequests=[{'people':5}]

		if (null == params) {
			params = new HashMap<String, String>();
			params.put(ActivityStreamRequestParams.lang, getUserLanguage());
		}

		params.put(ActivityStreamRequestParams.rollUp, "true");
		params.put(ActivityStreamRequestParams.custom, searchpattern);
		String url = ActivityStreamUrls.AS_PUBLIC_ALL_ALL.format(this);
		return getActivityStreamEntities(url, params);
	}
	
	
	/**
	 * postEntry Creates Activity Stream entry
	 * 
	 * @param user
	 * @param group
	 * @param application
	 * @param postPayload
	 * @return JsonJavaObject
	 * @throws ActivityStreamServiceException
	 */
	public String postEntry(String user, String group, String application, JsonJavaObject postPayload)
			throws ActivityStreamServiceException {

		if (null == postPayload) {
			throw new ActivityStreamServiceException(null,
					"postPayload passed was null");
		}

		String postUrl = ActivityStreamUrls.AS_USER_GROUP_APP.format(this, ASUser.get(user), ASGroup.get(group), ASApplication.NOAPP.get());
		return postEntry(postUrl, postPayload);

	}


	public String postEntry(NamedUrlPart user, NamedUrlPart group, NamedUrlPart application, JsonJavaObject postPayload) 
			throws ActivityStreamServiceException {
		String postUrl = ActivityStreamUrls.AS_USER_GROUP_APP.format(this, user, group, application);
		return postEntry(postUrl, postPayload);
	}
	
	/**
	 * postEntry Creates Activity Stream entry
	 * 
	 * @param postPayload
	 * @return {String} Id of the newly created entry
	 * @throws ActivityStreamServiceException
	 */

	/*
	 * This method allows user to set json payload and headers Rest of the parameters like user,group and app are assumed to be default
	 */
	public String postEntry(JsonJavaObject postPayload) throws ActivityStreamServiceException {

		if (null == postPayload) {
			throw new ActivityStreamServiceException(null,
					"postPayload passed was null");
		}

		String postUrl = ActivityStreamUrls.AS_ME_ALL_ALL.format(this);

		return postEntry(postUrl, postPayload);
	}
	
	
	/**
	 * postEntry Creates Activity Stream entry
	 * 
	 * @param populator
	 * @return String ( Id of the newly created entry )
	 * @throws ActivityStreamServiceException
	 */
	
	/*
	 * This method allows user to set ASDataPopulator, Rest of the parameters like user,group and app are assumed to be default
	 */
	public String postEntry(ASDataPopulator populator) throws ActivityStreamServiceException,TransformerException {

		if (null == populator) {
			throw new ActivityStreamServiceException(null,
					"populator passed was null");
		}
		
		return postEntry(ASUser.ME.get(), ASGroup.ALL.get(), ASApplication.ALL.get(), populator);
		
	}
	
	
	/**
	 * postEntry Creates Activity Stream entry
	 * @param user
	 * @param group
	 * @param application
	 * @param populator
	 * @return {String} Id of the newly created entry 
	 * @throws ActivityStreamServiceException
	 */
	
	/*
	 * This method allows user to set ASDataPopulator, Rest of the parameters like user,group and app are assumed to be default
	 */
	public String postEntry(NamedUrlPart user, NamedUrlPart group, NamedUrlPart application, ASDataPopulator populator)
			throws ActivityStreamServiceException, TransformerException {

			if (null == populator) {
				throw new ActivityStreamServiceException(null,
						"populator passed was null");
			}
			
			ActivityStreamTransformer transformer = new ActivityStreamTransformer();
			Map<String,Object> param = new HashMap<String, Object>();
			param.put(transformer.DATA_POPULATOR, populator);
			String jsonPayload = transformer.transform(param);
			String postUrl = ActivityStreamUrls.AS_USER_GROUP_APP.format(this, user, group, application);
			return postEntry(postUrl, jsonPayload);
	}

	/*
	 * Internal service methods
	 */
	public String postEntry(String url, Object payload) throws ActivityStreamServiceException {
		try {
			
			Map<String, String> header = new HashMap<String, String>();
			header.put("Content-Type", "application/json");
			Response serverResponse = createData(url, null, header, payload,ClientService.FORMAT_JSON);
			JsonJavaObject entityinfo = (JsonJavaObject) serverResponse.getData();
			return entityinfo.getJsonObject("entry").getString("id");
		} catch (Exception e) {
			throw new ActivityStreamServiceException(e, "postEntry failed");
		}
	}
	
	public void saveEntry(String entryId) throws ActivityStreamServiceException{
		JsonJavaObject payload = new JsonJavaObject();
		payload.putString("id", "");
		payload.putString("verb", "");
		
		JsonJavaObject actor = new JsonJavaObject();
		actor.putString("id", "");
		payload.putObject("actor", actor);
		
		JsonJavaObject connections = new JsonJavaObject();
		connections.putString("saved", "true");
		payload.putObject("connections", connections);
		
		JsonJavaObject object = new JsonJavaObject();
		object.putString("id", "");
		payload.putObject("object", object);
		
		String url = ActivityStreamUrls.AS_ME_ALL_ALL.format(this);
		postEntry(url + "/" + entryId + "?X-HTTP-Method-Override=PUT", payload);
	}
	
	/**
	 * postMBEntry Creates Microblog entry
	 * 
	 * @param user
	 * @param group
	 * @param application
	 * @param postPayload
	 * @return JsonJavaObject
	 * @throws ActivityStreamServiceException
	 */
	public String postMicroblogEntry(String user, String group,
			String application, JsonJavaObject postPayload)
			throws ActivityStreamServiceException {

		if (null == postPayload) {
			throw new ActivityStreamServiceException(null,
					"postPayload passed was null");
		}

		String postUrl = ActivityStreamUrls.UBLOG_USER_GROUP_APP.format(this, ASUser.get(user), ASGroup.get(group), ASApplication.get(application));
		return postEntry(postUrl, postPayload);

	}
	
	public ActivityStreamEntityList getActivityStreamEntities(String url, Map<String, String> params) throws ActivityStreamServiceException {
		ActivityStreamEntityList activityStreams = null;
		try {
			activityStreams = (ActivityStreamEntityList) getEntities(url, params, activityStreamFeedHandler);
		} catch (ClientServicesException e) {
			throw new ActivityStreamServiceException(e, "Problem occurred while creating list of activity streams");
		}
		return activityStreams;
	}
	
	private String getUserLanguage() {
		return "en";
	}

}
