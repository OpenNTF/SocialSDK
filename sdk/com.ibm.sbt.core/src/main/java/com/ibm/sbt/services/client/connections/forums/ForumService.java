/*
 * � Copyright IBM Corp. 2013
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

import static com.ibm.sbt.services.client.base.ConnectionsConstants.v4_5;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.StatusLine;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AuthType;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityServiceException;
import com.ibm.sbt.services.client.connections.forums.feedhandler.ForumsFeedHandler;
import com.ibm.sbt.services.client.connections.forums.feedhandler.RecommendationsFeedHandler;
import com.ibm.sbt.services.client.connections.forums.feedhandler.RepliesFeedHandler;
import com.ibm.sbt.services.client.connections.forums.feedhandler.TagFeedHandler;
import com.ibm.sbt.services.client.connections.forums.feedhandler.TopicsFeedHandler;
import com.ibm.sbt.services.client.connections.forums.transformers.BaseForumTransformer;
import com.ibm.sbt.services.endpoints.Endpoint;


/**
 * Forum model object
 * 
 * @author Manish Kataria 
 * @author Swati Singh
 * @author Carlos Manias
 */

public class ForumService extends BaseService {

	/**
	 * Used in constructing REST APIs
	 */
	private static final String FORUM_UNIQUE_IDENTIFIER = "forumUuid";
	private static final String TOPIC_UNIQUE_IDENTIFIER = "topicUuid";
	private static final String REPLY_UNIQUE_IDENTIFIER = "replyUuid";
	private static final String POST_UNIQUE_IDENTIFIER  = "postUuid"; 
	private static final String COMM_UNIQUE_IDENTIFIER	= "communityUuid";
	public static final String CREATE_OP 				= "create";
	
	private static final String[] serviceMappingKeys = new String[]{"forums"};
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

	/**
	 * Return mapping key for this service
	 */
	@Override
	public String[] getServiceMappingKeys() {
		return serviceMappingKeys;
	}

	@Override
	public NamedUrlPart getAuthType(){
		String auth = super.getAuthType().getValue();
		auth = AuthType.BASIC.get().equalsIgnoreCase(auth)?"":auth;
		return new NamedUrlPart("authType", auth);
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
			String allForumsUrl = ForumUrls.FORUMS.format(this);
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
			String publicForumsUrl = ForumUrls.FORUMS_PUBLIC.format(this);
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
		String myForumsUrl = ForumUrls.FORUMS_MY.format(this);
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
		String tagsUrl = ForumUrls.TAGS_FORUMS.format(this);
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
		String tagsUrl = ForumUrls.TAGS_TOPICS.format(this);
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
	
	protected void checkVersion() throws ForumServiceException{
		if (!getApiVersion().isAtLeast(v4_5)){
			UnsupportedOperationException ex = new UnsupportedOperationException("This API is only supported on connections 4.5 or above");
			throw new ForumServiceException(ex);
		}
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
		checkVersion();
		String recommendationsUrl = ForumUrls.RECOMMENDATION_ENTRIES.format(this);
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
		checkVersion();
		String recommendationsUrl = ForumUrls.RECOMMENDATION_ENTRIES.format(this);
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
		checkVersion();
		String recommendationsUrl = ForumUrls.RECOMMENDATION_ENTRIES.format(this);
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
		String url = ForumUrls.FORUM.format(this);
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

			String url = ForumUrls.FORUMS.format(this);
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
			if(forum.getFieldsMap().get(ForumsXPath.title)== null)
				forum.setTitle(forum.getTitle());
			if(forum.getFieldsMap().get(ForumsXPath.content)== null)
				forum.setContent(forum.getContent());
			if(!forum.getFieldsMap().toString().contains(ForumsXPath.tags.toString()))
				forum.setTags(forum.getTags());
			
			BaseForumTransformer transformer = new BaseForumTransformer(forum);
			Object payload = transformer.transform(forum.getFieldsMap());

			String url = ForumUrls.FORUM.format(this);

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
			String deleteForumUrl = ForumUrls.FORUM.format(this);

			Response response = super.deleteData(deleteForumUrl, parameters, FORUM_UNIQUE_IDENTIFIER);
			if (!isForumDeleted(response)){
				throw new ForumServiceException(new Exception(),"error deleting forum, received HTTP Status code "+response.getResponse().getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			throw new ForumServiceException(e,"error deleting forum");
		} 	

	}
	
	protected boolean isForumDeleted(Response response){
		StatusLine statusLine = response.getResponse().getStatusLine();
		return statusLine.getStatusCode() == 204;
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
		String myTopicsUrl = ForumUrls.TOPICS.format(this);
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
		String myTopicsUrl = ForumUrls.TOPICS_MY.format(this);
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
		String myTopicsUrl = ForumUrls.TOPICS.format(this);
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
		String myTopicsUrl = ForumUrls.TOPIC.format(this);
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

			String url = ForumUrls.TOPICS.format(this);
			result = createData(url, params, headers,payload);
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
			String postUrl = ForumUrls.TOPICS.format(this);
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
			String url = ForumUrls.TOPICS.format(this);
			if(topic.getFieldsMap().get(ForumsXPath.title)== null)
				topic.setTitle(topic.getTitle());
			if(topic.getFieldsMap().get(ForumsXPath.content)== null)
				topic.setContent(topic.getContent());
			if(!topic.getFieldsMap().toString().contains(ForumsXPath.tags.toString()))
				topic.setTags(topic.getTags());
			
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
			String deleteTopicUrl = ForumUrls.TOPIC.format(this);
			super.deleteData(deleteTopicUrl, parameters, TOPIC_UNIQUE_IDENTIFIER);
		} catch (Exception e) {
			throw new ForumServiceException(e,"error deleting forum");
		} 	
	}

	private ReplyList getReplies(Map<String, String> parameters) throws ForumServiceException {
		if (parameters != null){
			if(StringUtil.isEmpty(parameters.get(TOPIC_UNIQUE_IDENTIFIER)) && 
					StringUtil.isEmpty(parameters.get(REPLY_UNIQUE_IDENTIFIER))){
					throw new ForumServiceException(null, "null post Uuid");
			}
		}
		String myRepliesUrl = ForumUrls.REPLIES.format(this);
		ReplyList replies = null;
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			replies = (ReplyList) getEntities(myRepliesUrl, parameters, new RepliesFeedHandler(this));
		} catch (Exception e) {
			throw new ForumServiceException(e,"error getting forum replies");
		} 

		return replies;
	}

	 /**
     * Get a list for forum replies that includes the replies in the specified post.
     * The post uuid must be specified in the parametetrs as either:
     * topicUuid or replyUuid 
     * 
	 * @param parameters
	 * @return ReplyList
	 * @throws ForumServiceException
	 */
	public ReplyList getForumReplies(Map<String, String> parameters) throws ForumServiceException {
		return getReplies(parameters);
	}
	/**
     * Get a list for forum replies that includes the replies of a Forum Topic.
     * 
     * @param topicUuid
	 * @return ReplyList
	 * @throws ForumServiceException
	 */
	public ReplyList getForumTopicReplies(String topicUuid) throws ForumServiceException {
		return getForumTopicReplies(topicUuid, null);
	}
	 /**
     * Get a list for forum replies that includes the replies of a Forum Topic.
     * 
     * @param topicUuid
	 * @param parameters
	 * @return ReplyList
	 * @throws ForumServiceException
	 */
	public ReplyList getForumTopicReplies(String topicUuid, Map<String, String> parameters) throws ForumServiceException {
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		parameters.put(TOPIC_UNIQUE_IDENTIFIER, topicUuid);
		return getReplies(parameters);
		
	}
	/**
     * Get a list for forum replies that includes the replies of a Forum Reply.
     * 
     * @param topicUuid
	 * @return ReplyList
	 * @throws ForumServiceException
	 */
	public ReplyList getForumReplyReplies(String replyUuid) throws ForumServiceException {
		return getForumReplyReplies(replyUuid, null);
	}
	/**
     * Get a list for forum replies that includes the replies of a Forum Reply.
     * 
     * @param replyUuid
	 * @param parameters
	 * @return ReplyList
	 * @throws ForumServiceException
	 */
	public ReplyList getForumReplyReplies(String replyUuid, Map<String, String> parameters) throws ForumServiceException {
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		parameters.put(REPLY_UNIQUE_IDENTIFIER, replyUuid);
		return getReplies(parameters);
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
		String myRepliesUrl = ForumUrls.REPLY.format(this);
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
			String url = ForumUrls.REPLIES.format(this);
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
			
			if(reply.getFieldsMap().get(ForumsXPath.title)== null)
				reply.setTitle(reply.getTitle());
			if(reply.getFieldsMap().get(ForumsXPath.content)== null)
				reply.setContent(reply.getContent());
			if(!reply.getFieldsMap().toString().contains(ForumsXPath.tags.toString()))
				reply.setTags(reply.getTags());
			
			BaseForumTransformer transformer = new BaseForumTransformer(reply);
			Object 	payload = transformer.transform(reply.getFieldsMap());
			Map<String, String> params = new HashMap<String, String>();
			params.put(REPLY_UNIQUE_IDENTIFIER, reply.getUid());

			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			String url = ForumUrls.REPLY.format(this);
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
			String deleteReplyUrl = ForumUrls.REPLY.format(this);

			super.deleteData(deleteReplyUrl, parameters, REPLY_UNIQUE_IDENTIFIER);
		} catch (Exception e) {
			throw new ForumServiceException(e,"error deleting forum reply");
		} 	

	}
}
