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
package com.ibm.sbt.services.client.connections.forums;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.util.EntityUtil;
import com.ibm.sbt.services.client.connections.forums.feedhandler.ForumsFeedHandler;
import com.ibm.sbt.services.client.connections.forums.feedhandler.RepliesFeedHandler;
import com.ibm.sbt.services.client.connections.forums.feedhandler.TagFeedHandler;
import com.ibm.sbt.services.client.connections.forums.feedhandler.TopicsFeedHandler;
import com.ibm.sbt.services.client.connections.forums.feedhandler.RecommendationsFeedHandler; 
import com.ibm.sbt.services.client.connections.forums.transformers.BaseForumTransformer;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.util.AuthUtil;


/**
 * Forum model object
 * 
 * @author Manish Kataria 
 * @author Swati Singh
 */

public class ForumService extends BaseService {

	/**
	 * Used in constructing REST APIs
	 */
	private static final String	_baseUrl				= "/forums/";
	private static final String _basicUrl				= "atom/";
	public static final String _oauthUrl				= "oauth/atom/";
	private static final String FORUM_UNIQUE_IDENTIFIER = "forumUuid";
	private static final String TOPIC_UNIQUE_IDENTIFIER = "topicUuid";
	private static final String REPLY_UNIQUE_IDENTIFIER = "replyUuid";
	private static final String POST_UNIQUE_IDENTIFIER  = "postUuid"; 
	private static final String COMM_UNIQUE_IDENTIFIER	= "communityUuid";
	public static final String CREATE_OP 				= "create";
	private static final double APIVERSION       		= 4.5; 
	/**
	 * Default Constructor
	 */

	public ForumService() {
		this(DEFAULT_ENDPOINT_NAME);
	}

	/**
	 * Constructor
	 * 
	 * @param endpoint
	 *            Creates ForumService Object with the specified endpoint
	 */
	public ForumService(String endpoint) {
		super(endpoint, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor
	 * 
	 * @param endpoint
	 *            Creates ForumService Object with the specified endpoint
	 */
	public ForumService(Endpoint endpoint) {
		super(endpoint, DEFAULT_CACHE_SIZE);
	}

	/** This method returns the all forums
	 * 
	 * @return
	 * @throws ForumServiceException
	 */
	public ForumList getAllForums() throws ForumServiceException{
		return getAllForums(null);

	}

	/**
	 * This method returns the all forums
	 * 
	 * @param parameters
	 * @return
	 * @throws ForumServiceException
	 */
	public ForumList getAllForums(Map<String, String> parameters) throws ForumServiceException {
		ForumList forums;

		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			String allForumsUrl = resolveUrl(ForumType.FORUMS, null);
			forums = (ForumList)getEntities(allForumsUrl, parameters, new ForumsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new ForumServiceException(e);
		} catch (IOException e) {
			throw new ForumServiceException(e);
		}
		return forums;
	}

	/**
	 * This method returns the public forums
	 * 
	 * @return
	 * @throws ForumServiceException
	 */
	public ForumList getPublicForums() throws ForumServiceException{
		return getPublicForums(null);

	}

	/**
	 * This method returns the public forums
	 * 
	 * @param parameters
	 * @return
	 * @throws ForumServiceException
	 */
	public ForumList getPublicForums(Map<String, String> parameters) throws ForumServiceException {
		ForumList forums;

		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			String publicForumsUrl = resolveUrl(ForumType.FORUMS, FilterType.PUBLIC);
			forums = (ForumList)getEntities(publicForumsUrl, parameters, new ForumsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new ForumServiceException(e);
		} catch (IOException e) {
			throw new ForumServiceException(e);
		}
		return forums;
	}


	/**
	 * This method returns the my forums
	 * 
	 * @return
	 * @throws ForumServiceException
	 */
	public ForumList getMyForums() throws ForumServiceException {
		return getMyForums(null);
	}

	/**
	 * This method returns the public forums
	 * 
	 * @param parameters
	 * @return
	 * @throws ForumServiceException
	 */
	public ForumList getMyForums(Map<String, String> parameters) throws ForumServiceException {
		String myForumsUrl = resolveUrl(ForumType.FORUMS, FilterType.MY);
		ForumList forums = null;
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			forums = (ForumList) getEntities(myForumsUrl, parameters, new ForumsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new ForumServiceException(e);
		} catch (IOException e) {
			throw new ForumServiceException(e);
		}

		return forums;
	}

	/**
	 * This method returns the tags that have been assigned to forums
	 * 
	 * @return TagList
	 * @throws ForumServiceException
	 */
	public TagList getForumsTags() throws ForumServiceException {
		String tagsUrl = resolveUrl(ForumType.TAGS, FilterType.FORUMS);
		TagList tags = null;
		try {
			tags = (TagList) getEntities(tagsUrl, null, new TagFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new ForumServiceException(e);
		} catch (IOException e) {
			throw new ForumServiceException(e);
		}

		return tags;
	}
	/**
	 * This method returns the tags that have been assigned to forum topics
	 * 
	 * @method getForumTopicsTags
	 * 
	 * @param forumUuid
	 * @return TagList
	 * @throws ForumServiceException
	 */
	public TagList getForumTopicsTags(String forumUuid) throws ForumServiceException {
		return getForumTopicsTags(forumUuid, null);
	}
	/**
	 * This method returns the tags that have been assigned to forum topics
	 * 
	 * @method getForumTopicsTags
	 *  
	 * @param forumUuid
	 * @param parameters
	 * @return TagList
	 * @throws ForumServiceException
	 */
	public TagList getForumTopicsTags(String forumUuid, Map<String, String> parameters) throws ForumServiceException {
		String tagsUrl = resolveUrl(ForumType.TAGS, FilterType.TOPICS);
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		parameters.put(FORUM_UNIQUE_IDENTIFIER, forumUuid);

		TagList tags = null;
		try {
			tags = (TagList) getEntities(tagsUrl, parameters, new TagFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new ForumServiceException(e);
		} catch (IOException e) {
			throw new ForumServiceException(e);
		}

		return tags;
	}

	/**
	 * Wrapper method to get list of recommendations for a ForumTopic or ForumReply
	 * API Supported on Connections 4.5 or above
	 * <p>
	 * User should be authenticated to call this method
	 * @param postUuid
	 * @return RecommendationList
	 * @throws ForumServiceException
	 */
	public RecommendationList getRecommendations(String postUuid) throws ForumServiceException{
		// check api version, if not 4.5 or above then throw unsupported operation exception
		if(StringUtil.isNotEmpty(this.endpoint.getApiVersion())){
			double apiVersion = Double.parseDouble(this.endpoint.getApiVersion());
			if(APIVERSION > apiVersion ){
				UnsupportedOperationException ex = new UnsupportedOperationException("This API is only supported on connections 4.5 or above");
				throw new ForumServiceException(ex);
			}
		}
		String recommendationsUrl = resolveUrl(ForumType.RECOMMENDATIONS, FilterType.ENTRIES);
		RecommendationList recommendations;
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(POST_UNIQUE_IDENTIFIER, postUuid);
		try {
			recommendations = (RecommendationList) getEntities(recommendationsUrl, parameters, new RecommendationsFeedHandler(this));
		} catch (Exception e) {
			throw new ForumServiceException(e);
		}

		return recommendations;

	}

	/**
	 * Wrapper method to create a recommendation, API Supported on Connections 4.5 or above
	 * <p>
	 * User should be authenticated to call this method
	 * @param postUuid
	 * @return Recommendation
	 * @throws ForumServiceException
	 */
	public Recommendation createRecommendation(String postUuid) throws ForumServiceException{
		// check api version, if not 4.5 or above then throw unsupported operation exception
		if(StringUtil.isNotEmpty(this.endpoint.getApiVersion())){
			double apiVersion = Double.parseDouble(this.endpoint.getApiVersion());
			if(APIVERSION > apiVersion ){
				throw new UnsupportedOperationException("This API is only supported on connections 4.5 or above");
			}
		}
		String recommendationsUrl = resolveUrl(ForumType.RECOMMENDATIONS, FilterType.ENTRIES);
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(POST_UNIQUE_IDENTIFIER, postUuid);
		Response result;
		Recommendation recommendation;
		// not using transformer, as the payload to be sent is constant
		String payload = "<entry xmlns='http://www.w3.org/2005/Atom'><category scheme='http://www.ibm.com/xmlns/prod/sn/type' term='recommendation'></category></entry>";
		try {
			result = createData(recommendationsUrl, parameters, null,payload);
			recommendation = (Recommendation) new RecommendationsFeedHandler(this).createEntity(result);

		} catch (Exception e) {
			throw new ForumServiceException(e);
		}
		return recommendation;

	}
	/**
	 * Wrapper method to delete a recommendation, API Supported on Connections 4.5 or above
	 * <p>
	 * User should be authenticated to call this method
	 * @param postUuid
	 * @return boolean
	 * @throws ForumServiceException
	 */
	public boolean deleteRecommendation(String postUuid) throws ForumServiceException{
		// check api version, if not 4.5 or above then throw unsupported operation exception
		if(StringUtil.isNotEmpty(this.endpoint.getApiVersion())){
			double apiVersion = Double.parseDouble(this.endpoint.getApiVersion());
			if(APIVERSION > apiVersion ){
				UnsupportedOperationException ex = new UnsupportedOperationException("This API is only supported on connections 4.5 or above");
				throw new ForumServiceException(ex);
		
			}
		}
		String recommendationsUrl = resolveUrl(ForumType.RECOMMENDATIONS, FilterType.ENTRIES);
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(POST_UNIQUE_IDENTIFIER, postUuid);
		try {
			deleteData(recommendationsUrl, parameters, postUuid);
			return true;
		} catch (Exception e) {
			throw new ForumServiceException(e);
		}
	}

	// todo : missing overloaded method accepting params for getForum

	/**
	 * Wrapper method to get a Stand-alone forum
	 * <p>
	 * fetches forum content from server and populates the data member of {@link Community} with the fetched content 
	 *
	 * @param forumUuid
	 *			   id of forum
	 * @return A Forum
	 * @throws ForumServiceException
	 */
	public Forum getForum(String forumUuid) throws ForumServiceException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(FORUM_UNIQUE_IDENTIFIER, forumUuid);
		String url = resolveUrl(ForumType.FORUM,null,parameters);
		Forum forum;
		try {
			forum = (Forum)getEntity(url, parameters, new ForumsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new ForumServiceException(e, forumUuid);
		} catch (Exception e) {
			throw new ForumServiceException(e, forumUuid);
		}

		return forum;
	}

	/**
	 * Wrapper method to create a forum
	 * <p>
	 * User should be authenticated to call this method
	 * 
	 * 
	 * @param Forum
	 * @return Forum
	 * @throws ForumServiceException
	 */
	public Forum createForum(Forum forum) throws ForumServiceException {
		if (null == forum){
			throw new ForumServiceException(null,"null forum");
		}
		Response result = null;
		try {
			BaseForumTransformer transformer = new BaseForumTransformer(forum);
			Object 	payload = transformer.transform(forum.getFieldsMap());

			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");

			String url = resolveUrl(ForumType.FORUMS,null,null);
			result = createData(url, null, headers, payload);
			forum = (Forum) new ForumsFeedHandler(this).createEntity(result);

		} catch (Exception e) {
			throw new ForumServiceException(e, "error creating forum");
		}

		return forum;
	}


	/**
	 * Wrapper method to update a forum
	 * <p>
	 * User should be authenticated as owner of the forum to call this method
	 * 
	 * @param Forum
	 * @return Response
	 * @throws ForumServiceException
	 */
	public Response updateForum(Forum forum) throws ForumServiceException {
		if (null == forum){
			throw new ForumServiceException(null,"null forum");
		}

		try {
			Map<String, String> parameters = new HashMap<String, String>();

			parameters.put(FORUM_UNIQUE_IDENTIFIER, forum.getUid());

			BaseForumTransformer transformer = new BaseForumTransformer(forum);
			Object payload = transformer.transform(forum.getFieldsMap());

			String url = resolveUrl(ForumType.FORUM,null,null);

			return updateData(url, parameters, payload, FORUM_UNIQUE_IDENTIFIER);
		} catch (Exception e) {
			throw new ForumServiceException(e, "error updating forum");
		}

	}


	/**
	 * Wrapper method to delete a forum
	 * <p>
	 * User should be logged in as a owner of the forum to call this method.
	 * 
	 * @param Forum
	 * 				forum which is to be deleted
	 * @throws CommunityServiceException
	 */
	public void removeForum(Forum forum) throws ForumServiceException {
		removeForum(forum.getUid());
	}

	/**
	 * Wrapper method to delete a forum
	 * <p>
	 * User should be logged in as a owner of the forum to call this method.
	 * 
	 * @param String
	 * 				forumUuid which is to be deleted
	 * @throws ForumServiceException
	 */
	public void removeForum(String forumUuid) throws ForumServiceException {
		if (StringUtil.isEmpty(forumUuid)){
			throw new ForumServiceException(null, "null forum id");
		}

		try {
			Map<String, String> parameters = new HashMap<String, String>();

			parameters.put(FORUM_UNIQUE_IDENTIFIER, forumUuid);
			String deleteForumUrl = resolveUrl(ForumType.FORUM,null,parameters);

			super.deleteData(deleteForumUrl, parameters, FORUM_UNIQUE_IDENTIFIER);
		} catch (Exception e) {
			throw new ForumServiceException(e,"error deleting forum");
		} 	

	}

	/**
	 * This method returns the public topics
	 * 
	 * @return TopicList
	 * @throws ForumServiceException
	 */
	public TopicList getPublicForumTopics() throws ForumServiceException {
		return getPublicForumTopics(null);
	}

	/**
	 * This method returns the public topics
	 * 
	 * @param parameters
	 * @return TopicList
	 * @throws ForumServiceException
	 */
	public TopicList getPublicForumTopics(Map<String, String> parameters) throws ForumServiceException {
		String myTopicsUrl = resolveUrl(ForumType.TOPICS, null);
		TopicList topics = null;
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			topics = (TopicList) getEntities(myTopicsUrl, parameters, new TopicsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new ForumServiceException(e);
		} catch (IOException e) {
			throw new ForumServiceException(e);
		}

		return topics;
	}




	/**
	 * This method returns the my topics
	 * 
	 * @return TopicList
	 * @throws ForumServiceException
	 */
	public TopicList getMyForumTopics() throws ForumServiceException {
		return getMyForumTopics(null);
	}

	/**
	 * This method returns the my topics
	 * 
	 * @param parameters
	 * @return TopicList
	 * @throws ForumServiceException
	 */
	public TopicList getMyForumTopics(Map<String, String> parameters) throws ForumServiceException {
		String myTopicsUrl = resolveUrl(ForumType.TOPICS, FilterType.MY);
		TopicList topics = null;
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			topics = (TopicList) getEntities(myTopicsUrl, parameters, new TopicsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new ForumServiceException(e);
		} catch (IOException e) {
			throw new ForumServiceException(e);
		}

		return topics;
	}




	/**
	 * This method returns the topics for a particular forum
	 * 
	 * @param parameters
	 * @return
	 * @throws ForumServiceException
	 */
	public TopicList getForumTopics(String forumUid) throws ForumServiceException {
		return getForumTopics(forumUid, null);
	}


	/**
	 * This method returns the topics for a particular forum
	 * 
	 * @param parameters
	 * @return TopicList
	 * @throws ForumServiceException
	 */
	public TopicList getForumTopics(String forumUid, Map<String, String> parameters) throws ForumServiceException {
		String myTopicsUrl = resolveUrl(ForumType.TOPICS, null);
		TopicList topics = null;
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}

		parameters.put(FORUM_UNIQUE_IDENTIFIER, forumUid);
		try {
			topics = (TopicList) getEntities(myTopicsUrl, parameters, new TopicsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new ForumServiceException(e);
		} catch (IOException e) {
			throw new ForumServiceException(e);
		}

		return topics;
	}

	/**
	 * This method returns topic
	 * 
	 * @param parameters
	 * @return TopicList
	 * @throws ForumServiceException
	 */
	public ForumTopic getForumTopic(String topicId) throws ForumServiceException {
		return getForumTopic(topicId, null);
	}



	/**
	 * This method returns topic
	 * 
	 * @param parameters
	 * @return
	 * @throws ForumServiceException
	 */
	public ForumTopic getForumTopic(String topicId, Map<String, String> parameters) throws ForumServiceException {
		String myTopicsUrl = resolveUrl(ForumType.TOPIC, null);
		ForumTopic topic = null;
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		parameters.put(TOPIC_UNIQUE_IDENTIFIER, topicId);
		try {
			topic = (ForumTopic)getEntity(myTopicsUrl, parameters, new TopicsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new ForumServiceException(e);
		} catch (IOException e) {
			throw new ForumServiceException(e);
		}

		return topic;
	}


	/**
	 * Wrapper method to create a Topic
	 * <p>
	 * User should be authenticated to call this method
	 * @param ForumTopic
	 * @return Topic
	 * @throws ForumServiceException
	 */
	public ForumTopic createForumTopic(ForumTopic topic) throws ForumServiceException {
		return createForumTopic(topic,topic.getForumUuid());
	}


	/**
	 * Wrapper method to create a Topic
	 * <p>
	 * User should be authenticated to call this method
	 * @param ForumTopic
	 * @return Topic
	 * @throws ForumServiceException
	 */
	public ForumTopic createForumTopic(ForumTopic topic,String forumId) throws ForumServiceException {
		if (null == topic){
			throw new ForumServiceException(null,"Topic object passed was null");
		}
		Response result = null;
		try {
			BaseForumTransformer transformer = new BaseForumTransformer(topic);
			Object 	payload = transformer.transform(topic.getFieldsMap());

			Map<String, String> params = new HashMap<String, String>();
			params.put(FORUM_UNIQUE_IDENTIFIER, forumId);

			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");

			String url = resolveUrl(ForumType.TOPICS,null,params);
			result = createData(url, null, headers,payload);
			topic = (ForumTopic) new TopicsFeedHandler(this).createEntity(result);

		} catch (Exception e) {
			throw new ForumServiceException(e, "error creating forum");
		}

		return topic;
	}



	/**
	 * Wrapper method to create a Topic for default Forum of a Community
	 * <p>
	 * User should be authenticated to call this method
	 * @param ForumTopic
	 * @return Topic
	 * @throws ForumServiceException
	 */
	public ForumTopic createCommunityForumTopic(ForumTopic topic,String communityId) throws ForumServiceException {
		if (null == topic){
			throw new ForumServiceException(null,"Topic object passed was null");
		}
		Response result = null;
		try {
			BaseForumTransformer transformer = new BaseForumTransformer(topic);
			Object 	payload = transformer.transform(topic.getFieldsMap());

			Map<String, String> params = new HashMap<String, String>();
			params.put(COMM_UNIQUE_IDENTIFIER, communityId);

			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			String postUrl = resolveUrl(ForumType.TOPICS,null,null);
			result = createData(postUrl, params, headers,payload);
			topic = (ForumTopic) new TopicsFeedHandler(this).createEntity(result);

		} catch (Exception e) {
			throw new ForumServiceException(e, "error creating forum");
		}

		return topic;
	}

	/**
	 * Wrapper method to update a topic
	 * <p>
	 * User should be authenticated as owner of the forum to call this method
	 * 
	 * @param ForumTopic
	 * @throws ForumServiceException
	 */
	public void updateForumTopic(ForumTopic topic) throws ForumServiceException {
		if (null == topic || StringUtil.isEmpty(topic.getUid())) {
			throw new ForumServiceException(null,"Topic object passed was null");
		}
		try {
			String url = resolveUrl(ForumType.TOPICS, null, null);
			BaseForumTransformer transformer = new BaseForumTransformer(topic);

			Object payload = transformer.transform(topic.getFieldsMap());

			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(TOPIC_UNIQUE_IDENTIFIER, topic.getUid());

			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");

			getClientService().put(url, parameters,headers, payload,ClientService.FORMAT_NULL);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ForumServiceException(e, "error updating topic");
		}

	}

	/**
	 * Wrapper method to delete a topic
	 * <p>
	 * User should be logged in as a owner of the forum to call this method.
	 * 
	 * @param Forum
	 * 				forum which is to be deleted
	 * @throws CommunityServiceException
	 */
	public void removeForumTopic(ForumTopic topic) throws ForumServiceException {
		removeForum(topic.getUid());
	}

	/**
	 * Wrapper method to delete a topic
	 * <p>
	 * User should be logged in as a owner of the forum to call this method.
	 * 
	 * @param String
	 * 				topicUuid which is to be deleted
	 * @throws ForumServiceException
	 */
	public void removeForumTopic(String topicUuid) throws ForumServiceException {
		if (StringUtil.isEmpty(topicUuid)){
			throw new ForumServiceException(null, "null topic id");
		}

		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(TOPIC_UNIQUE_IDENTIFIER, topicUuid);
			String deleteTopicUrl = resolveUrl(ForumType.TOPIC,null,parameters);
			super.deleteData(deleteTopicUrl, parameters, TOPIC_UNIQUE_IDENTIFIER);
		} catch (Exception e) {
			throw new ForumServiceException(e,"error deleting forum");
		} 	

	}


	/**
	 * This method returns all of the replies for a specific forum topic
	 * 
	 * @param topicUuid
	 * @return ReplyList
	 * @throws ForumServiceException
	 */
	public ReplyList getForumReplies(String topicUuid) throws ForumServiceException {
		return getForumReplies(topicUuid,null);
	}

	/**
	 * This method returns all of the replies for a specific forum topic
	 * 
	 * @param topicUuid
	 * @param parameters
	 * @return ReplyList
	 * @throws ForumServiceException
	 */
	public ReplyList getForumReplies(String topicUuid, Map<String, String> parameters) throws ForumServiceException {
		String myRepliesUrl = resolveUrl(ForumType.REPLIES, null);
		ReplyList replies = null;
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		parameters.put(TOPIC_UNIQUE_IDENTIFIER, topicUuid);
		try {
			replies = (ReplyList) getEntities(myRepliesUrl, parameters, new RepliesFeedHandler(this));
		} catch (Exception e) {
			throw new ForumServiceException(e,"error getting topic replies");
		} 

		return replies;
	}

	/**
	 * This method returns reply
	 * 
	 * @param replyId
	 * @return ForumReply
	 * @throws ForumServiceException
	 */
	public ForumReply getForumReply(String replyId) throws ForumServiceException {
		return getForumReply(replyId, null);
	}



	/**
	 * This method returns reply
	 * @param replyId
	 * @param parameters
	 * @return ForumReply
	 * @throws ForumServiceException
	 */
	public ForumReply getForumReply(String replyId, Map<String, String> parameters) throws ForumServiceException {
		String myRepliesUrl = resolveUrl(ForumType.REPLY, null);
		ForumReply reply = null;
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		parameters.put(REPLY_UNIQUE_IDENTIFIER, replyId);
		try {
			reply = (ForumReply)getEntity(myRepliesUrl, parameters, new RepliesFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new ForumServiceException(e);
		} catch (IOException e) {
			throw new ForumServiceException(e);
		}

		return reply;
	}


	/**
	 * Wrapper method to create a Reply
	 * <p>
	 * User should be authenticated to call this method
	 * @param ForumReply
	 * @return ForumReply
	 * @throws ForumServiceException
	 */
	public ForumReply createForumReply(ForumReply reply) throws ForumServiceException {
		return createForumReply(reply, reply.getTopicUuid());
	}


	/**
	 * Wrapper method to create a Reply
	 * <p>
	 * User should be authenticated to call this method
	 * @param ForumReply
	 * @param topicId
	 * @return ForumReply
	 * @throws ForumServiceException
	 */
	public ForumReply createForumReply(ForumReply reply,String topicId) throws ForumServiceException {
		if (null == reply || StringUtil.isEmpty(topicId)){
			throw new ForumServiceException(null,"Reply object passed was null");
		}
		Response result = null;
		try {
			if(StringUtil.isEmpty(reply.getTopicUuid())){
				reply.setTopicUuid(topicId);
			}
			BaseForumTransformer transformer = new BaseForumTransformer(reply, CREATE_OP);
			Object 	payload = transformer.transform(reply.getFieldsMap());
			Map<String, String> params = new HashMap<String, String>();

			params.put(TOPIC_UNIQUE_IDENTIFIER, topicId);

			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			String url = resolveUrl(ForumType.REPLIES,null,null);
			result = createData(url, params, headers,payload);
			reply = (ForumReply) new RepliesFeedHandler(this).createEntity(result);

		} catch (Exception e) {
			throw new ForumServiceException(e, "error creating forum reply");
		}

		return reply;
	}


	/**
	 * Wrapper method to update a Reply
	 * <p>
	 * User should be authenticated to call this method
	 * @param ForumReply
	 * @return void
	 * @throws ForumServiceException
	 */
	public void updateForumReply(ForumReply reply) throws ForumServiceException {
		if (null == reply){
			throw new ForumServiceException(null,"Reply object passed was null");
		}
		try {
			BaseForumTransformer transformer = new BaseForumTransformer(reply);
			Object 	payload = transformer.transform(reply.getFieldsMap());
			Map<String, String> params = new HashMap<String, String>();
			params.put(REPLY_UNIQUE_IDENTIFIER, reply.getUid());

			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			String url = resolveUrl(ForumType.REPLY,null,null);
			updateData(url, params, headers,payload, reply.getUid());

		} catch (Exception e) {
			throw new ForumServiceException(e, "error updating forum reply");
		}

	}

	/**
	 * Wrapper method to delete a forum reply
	 * <p>
	 * User should be logged in as a owner of the forum to call this method.
	 * 
	 * @param ForumReply
	 * 				reply which is to be deleted
	 * @throws CommunityServiceException
	 */
	public void removeForumReply(ForumReply reply) throws ForumServiceException {
		removeForum(reply.getUid());
	}

	/**
	 * Wrapper method to delete a forum reply
	 * <p>
	 * User should be logged in as a owner of the forum to call this method.
	 * 
	 * @param String
	 * 				replyUuid which is to be deleted
	 * @throws ForumServiceException
	 */
	public void removeForumReply(String replyUuid) throws ForumServiceException {
		if (StringUtil.isEmpty(replyUuid)){
			throw new ForumServiceException(null, "null reply id");
		}

		try {
			Map<String, String> parameters = new HashMap<String, String>();

			parameters.put(REPLY_UNIQUE_IDENTIFIER, replyUuid);
			String deleteReplyUrl = resolveUrl(ForumType.REPLY,null,null);

			super.deleteData(deleteReplyUrl, parameters, REPLY_UNIQUE_IDENTIFIER);
		} catch (Exception e) {
			throw new ForumServiceException(e,"error deleting forum reply");
		} 	

	}

	/*
	 * Util methods
	 */


	/*
	 * Method to generate appropriate REST URLs
	 * 
	 */
	protected String resolveUrl(ForumType forumType, FilterType filterType) {
		return resolveUrl(forumType, filterType, null);
	}

	/*
	 * Method to generate appropriate REST URLs
	 * 
	 */
	protected String resolveUrl(ForumType forumType, FilterType filterType, Map<String, String> params) {
		StringBuilder baseUrl = new StringBuilder(_baseUrl);

		if (AuthUtil.INSTANCE.getAuthValue(endpoint).equalsIgnoreCase(ConnectionsConstants.OAUTH)) {
			baseUrl.append(_oauthUrl);
		}else{
			baseUrl.append(_basicUrl);
		}

		// todo : Add oauth logic
		if(filterType != null){
			baseUrl.append(forumType.getForumType()).append(ConnectionsConstants.SEPARATOR).append(filterType.getFilterType());
		}else{
			baseUrl.append(forumType.getForumType());
		}

		// Add required parameters
		if (null != params && params.size() > 0) {
			baseUrl.append(ConnectionsConstants.INIT_URL_PARAM);
			boolean setSeparator = false;
			for (Map.Entry<String, String> param : params.entrySet()) {
				String key = param.getKey();
				if (StringUtil.isEmpty(key)) continue;
				String value = EntityUtil.encodeURLParam(param.getValue());
				if (StringUtil.isEmpty(value)) continue;
				if (setSeparator) {
					baseUrl.append(ConnectionsConstants.URL_PARAM);
				} else {
					setSeparator = true;
				}
				baseUrl.append(key).append(ConnectionsConstants.EQUALS).append(value);
			}
		}

		return baseUrl.toString();
	}

}
