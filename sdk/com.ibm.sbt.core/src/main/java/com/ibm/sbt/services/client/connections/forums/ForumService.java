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

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.v4_5;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.StatusLine;
import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomFeedHandler;
import com.ibm.sbt.services.client.base.AuthType;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsService;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.forums.transformers.BaseForumTransformer;
import com.ibm.sbt.services.endpoints.Endpoint;


/**
 * The Forums application of IBM® Connections enables a team to discuss issues that are pertinent to their work. 
 * The Forums API allows application programs to create new forums, and to read and modify existing forums. 
 * 
 * @see
 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.5+API+Documentation#action=openDocument&res_title=Forums_API_ic45&content=pdcontent">
 *			Forums API</a>
 * 
 * @author Manish Kataria 
 * @author Swati Singh
 * @author Carlos Manias
 */

public class ForumService extends ConnectionsService {

	private static final long serialVersionUID = -4926901916081556236L;

	/**
	 * Used in constructing REST APIs
	 */
	private static final String FORUM_UNIQUE_IDENTIFIER = "forumUuid";
	private static final String TOPIC_UNIQUE_IDENTIFIER = "topicUuid";
	private static final String REPLY_UNIQUE_IDENTIFIER = "replyUuid";
	private static final String POST_UNIQUE_IDENTIFIER  = "postUuid"; 
	private static final String COMM_UNIQUE_IDENTIFIER	= "communityUuid";
	public static final String CREATE_OP 				= "create";

	/**
	 * Create ForumService instance with default endpoint.
	 */
	public ForumService() {
		this(DEFAULT_ENDPOINT_NAME);
	}

	/**
	 * Create ForumService instance with specified endpoint.
	 * 
	 * @param endpoint
	 */
	public ForumService(String endpoint) {
		super(endpoint, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Create ForumService instance with specified endpoint.
	 * 
	 * @param endpoint
	 */
	public ForumService(Endpoint endpoint) {
		super(endpoint, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Return mapping key for this service
	 */
	@Override
	public String getServiceMappingKey() {
		return "forums";
	}

	@Override
	public NamedUrlPart getAuthType(){
		String auth = super.getAuthType().getValue();
		auth = AuthType.BASIC.get().equalsIgnoreCase(auth)?"":auth;
		return new NamedUrlPart("authType", auth);
	}

	/***************************************************************
	 * Getting Forum feeds
	 ****************************************************************/

	/** 
	 * Get a feed that includes all stand-alone and community forums created in the enterprise.
	 * 
	 * @return EntityList&lt;Forum&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Forum> getAllForums() throws ClientServicesException{
		return getAllForums(null);

	}

	/**
	 * Get a feed that includes all stand-alone and community forums created in the enterprise.
	 * You can narrow down the forums that are returned by passing parameters to the request 
	 * that you use to retrieve the feed.
	 * 
	 * @param parameters
	 * @return EntityList&lt;Forum&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Forum> getAllForums(Map<String, String> parameters) throws ClientServicesException {
		String allForumsUrl = ForumUrls.FORUMS.format(this);
		return getForumEntityList(allForumsUrl, parameters);
	}

	/**
	 * Get a feed that includes all of the forums hosted by the Forums application.
	 * 
	 * @return EntityList&lt;Forum&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Forum> getPublicForums() throws ClientServicesException{
		return getPublicForums(null);

	}

	/**
	/**
	 * Get a feed that includes all of the forums hosted by the Forums application.
	 * You can narrow down the forums that are returned by passing parameters to the
	 * request that you use to retrieve the feed.
	 * 
	 * @param parameters
	 * @return EntityList&lt;Forum&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Forum> getPublicForums(Map<String, String> parameters) throws ClientServicesException {
		String publicForumsUrl = ForumUrls.FORUMS_PUBLIC.format(this);
		return getForumEntityList(publicForumsUrl, parameters);
	}


	/**
	 * Get a feed that includes forums created by the authenticated user 
	 * or associated with communities to which the user belongs.
	 * 
	 * @return EntityList&lt;Forum&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Forum> getMyForums() throws ClientServicesException {
		return getMyForums(null);
	}

	/**
	 * Get a feed that includes forums created by the authenticated user 
	 * or associated with communities to which the user belongs.
	 * You can narrow down the forums that are returned by passing parameters 
	 * to the request that you use to retrieve the feed.
	 * 
	 * @param parameters
	 * @return EntityList&lt;Forum&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Forum> getMyForums(Map<String, String> parameters) throws ClientServicesException {
		String myForumsUrl = ForumUrls.FORUMS_MY.format(this);
		return getForumEntityList(myForumsUrl, parameters);
	}
	
	/**
	 * Get a feed that includes the topics in a specific forum,
	 * whether it is a stand-alone or community forum.
	 * 
	 * @param forumUid
	 * @return EntityList&lt;ForumTopic&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<ForumTopic> getForumTopics(String forumUid) throws ClientServicesException {
		return getForumTopics(forumUid, null);
	}


	/**
	 * Get a feed that includes the topics in a specific forum,
	 * whether it is a stand-alone or community forum.
	 * You can narrow down the topics that are returned by passing parameters to the request 
	 * that you use to retrieve the feed.
	 * 
	 * @param forumUid
	 * @param parameters
	 * @return EntityList&lt;ForumTopic&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<ForumTopic> getForumTopics(String forumUid, Map<String, String> parameters) throws ClientServicesException {
		parameters = getParameters(parameters);
		parameters.put(FORUM_UNIQUE_IDENTIFIER, forumUid);
		String myTopicsUrl = ForumUrls.TOPICS.format(this);
		return getForumTopicEntityList(myTopicsUrl, parameters);
	}

	/**
	 * Get a feed that includes the topics in a specific forum,
	 * whether it is a stand-alone or community forum.
	 * 
	 * @return EntityList&lt;ForumTopic&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<ForumTopic> getPublicForumTopics() throws ClientServicesException {
		return getPublicForumTopics(null);
	}

	/**
	 * Get a feed that includes the topics in a specific forum,
	 * whether it is a stand-alone or community forum.
	 * You can narrow down the topics that are returned by passing parameters to the request 
	 * that you use to retrieve the feed.
	 * 
	 * @param parameters
	 * @return EntityList&lt;ForumTopic&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<ForumTopic> getPublicForumTopics(Map<String, String> parameters) throws ClientServicesException {
		String myTopicsUrl = ForumUrls.TOPICS.format(this);
		return getForumTopicEntityList(myTopicsUrl, parameters);
	}

	/**
	 * Get a feed that includes the topics that the authenticated user created in stand-alone forums
	 * and in forums associated with communities to which the user belongs.
	 * You can narrow down the topics that are returned by passing parameters 
	 * to the request that you use to retrieve the feed.
	 * 
	 * @return EntityList&lt;ForumTopic&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<ForumTopic> getMyForumTopics() throws ClientServicesException {
		return getMyForumTopics(null);
	}

	/**
	 * Get a feed that includes the topics that the authenticated user created in stand-alone forums
	 * and in forums associated with communities to which the user belongs.
	 * You can narrow down the topics that are returned by passing parameters 
	 * to the request that you use to retrieve the feed.
	 * 
	 * @param parameters
	 * @return EntityList&lt;ForumTopic&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<ForumTopic> getMyForumTopics(Map<String, String> parameters) throws ClientServicesException {
		String myTopicsUrl = ForumUrls.TOPICS_MY.format(this);
		return getForumTopicEntityList(myTopicsUrl, parameters);
	}

	/**
     * Get a feed that includes all of the replies for a specific forum topic.
     * You can narrow down the replies that are returned by passing parameters to the request
     * that you use to retrieve the feed.
	 * 
     * @param topicUuid
	 * @return EntityList&lt;ForumReply&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<ForumReply> getForumTopicReplies(String topicUuid) throws ClientServicesException {
		return getForumTopicReplies(topicUuid, null);
	}

	 /**
     * Get a feed that includes all of the replies for a specific forum topic.
     * You can narrow down the replies that are returned by passing parameters to the request
     * that you use to retrieve the feed.
	 * 
     * @param topicUuid
	 * @param parameters
	 * @return EntityList&lt;ForumReply&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<ForumReply> getForumTopicReplies(String topicUuid, Map<String, String> parameters) throws ClientServicesException {
		parameters = getParameters(parameters);
		parameters.put(TOPIC_UNIQUE_IDENTIFIER, topicUuid);
		return getReplies(parameters);
	}

	/**
	 * Get a category document that lists the tags that have been assigned to forums.
	 * 
	 * @return EntityList&lt;Tag&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Tag> getForumsTags() throws ClientServicesException {
		String tagsUrl = ForumUrls.TAGS_FORUMS.format(this);
		return getTagEntityList(tagsUrl, null);
	}

	/**
	 * This method returns the tags that have been assigned to forum topics
	 * 
	 * @param forumUuid
	 * @return EntityList&lt;Tag&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Tag> getForumTopicsTags(String forumUuid) throws ClientServicesException {
		return getForumTopicsTags(forumUuid, null);
	}

	/**
	 * This method returns the tags that have been assigned to forum topics
	 * 
	 * @param forumUuid
	 * @param parameters
	 * @return EntityList&lt;Tag&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Tag> getForumTopicsTags(String forumUuid, Map<String, String> parameters) throws ClientServicesException {
		parameters = getParameters(parameters);
		parameters.put(FORUM_UNIQUE_IDENTIFIER, forumUuid);
		String tagsUrl = ForumUrls.TAGS_TOPICS.format(this);
		return getTagEntityList(tagsUrl, null);
	}

	/**
	 * Get recommendations
	 * 
	 * @since 4.5
	 * @param postUuid
	 * @return RecommendationList
	 * @throws ClientServicesException
	 */
	public EntityList<Recommendation> getRecommendations(String postUuid) throws ClientServicesException{
		checkVersion();
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(POST_UNIQUE_IDENTIFIER, postUuid);
		String recommendationsUrl = ForumUrls.RECOMMENDATION_ENTRIES.format(this);
		return getRecommendationEntityList(recommendationsUrl, parameters);
	}
	
	////////////////////////////////////////////////////////////////////////////////
	//TODO: Getting a list of inappropriate content flag options in Forums
	////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////
	//TODO: Flagging a forum entry or reply as inappropriate
	////////////////////////////////////////////////////////////////////////////////
	
	/***************************************************************
	 * Working with stand-alone forums
	 ****************************************************************/

	/**
	 * To create a stand-alone forum, send an Atom entry document
	 * containing the forum entry document to the root collection resource.
	 * See Authenticating requests for information about how to authenticate the request.
	 * 
	 * @param Forum
	 * @return Forum
	 * @throws ClientServicesException
	 */
	public Forum createForum(Forum forum) throws ClientServicesException {
		if (null == forum){
			throw new ClientServicesException(null,"null forum");
		}
		Response result = null;
		try {
			BaseForumTransformer transformer = new BaseForumTransformer(forum);
			Object 	payload = transformer.transform(forum.getFieldsMap());

			String url = ForumUrls.FORUMS.format(this);
			result = createData(url, null, getAtomHeaders(), payload);
			forum = getForumFeedHandler().createEntity(result);

		} catch (Exception e) {
			throw new ClientServicesException(e, "error creating forum");
		}

		return forum;
	}

	/**
	 * To retrieve complete information about a forum, use the edit <link> found in the forum entry in the Forums feed.
	 * 
	 * @param forumUuid
	 *			   id of forum
	 * @return A Forum
	 * @throws ClientServicesException
	 */
	public Forum getForum(String forumUuid) throws ClientServicesException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(FORUM_UNIQUE_IDENTIFIER, forumUuid);
		String url = ForumUrls.FORUM.format(this);
		return getForumEntity(url, parameters);
	}

	/**
	 * To update a stand-alone forum, send an updated forum document
	 * in Atom format to the existing forum's edit web address.<br>
	 * All existing forum entry metadata will be replaced with the new data.
	 * To avoid deleting all existing data, retrieve any data you want to retain first,
	 * and send it back with this request. See Retrieving forums for more information. <br>
	 * See Authenticating requests for information about how to authenticate the request.
	 * 
	 * @param Forum
	 * @return Response
	 * @throws ClientServicesException
	 */
	public Response updateForum(Forum forum) throws ClientServicesException {
		if (null == forum){
			throw new ClientServicesException(null,"null forum");
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
			throw new ClientServicesException(e, "error updating forum");
		}

	}

	/**
	 * To delete a recommendation of a post(topic or reply) in a forum, 
	 * use the HTTP DELETE method.<br>
	 * Only the user who have already recommended the post
	 * can delete it's own recommendation.<br>
	 * See Authenticating requests for information about how to authenticate the request.
	 * 
	 * @param Forum
	 * 				forum which is to be deleted
	 * @throws CommunityServiceException
	 */
	public void removeForum(Forum forum) throws ClientServicesException {
		removeForum(forum.getUid());
	}

	/**
	 * To delete a recommendation of a post(topic or reply) in a forum, 
	 * use the HTTP DELETE method.<br>
	 * Only the user who have already recommended the post
	 * can delete it's own recommendation.<br>
	 * See Authenticating requests for information about how to authenticate the request.
	 * 
	 * @param String
	 * 				forumUuid which is to be deleted
	 * @throws ClientServicesException
	 */
	public void removeForum(String forumUuid) throws ClientServicesException {
		if (StringUtil.isEmpty(forumUuid)){
			throw new ClientServicesException(null, "null forum id");
		}

		try {
			Map<String, String> parameters = new HashMap<String, String>();

			parameters.put(FORUM_UNIQUE_IDENTIFIER, forumUuid);
			String deleteForumUrl = ForumUrls.FORUM.format(this);

			Response response = super.deleteData(deleteForumUrl, parameters, FORUM_UNIQUE_IDENTIFIER);
			if (!isForumDeleted(response)){
				throw new ClientServicesException(new Exception(),"error deleting forum, received HTTP Status code "+response.getResponse().getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			throw new ClientServicesException(e,"error deleting forum");
		} 	

	}
	
	/***************************************************************
	 * Working with stand-alone forum topics
	 ****************************************************************/

	/**
	 * To add a topic to a stand-alone forum, send an Atom entry document 
	 * containing the forum topic to the forum topics resource.<br>
	 * See Authenticating requests for information about how to authenticate the request.
	 * 
	 * @param topic
	 * 		the topic to be created
	 * @return ForumTopic
	 * @throws ClientServicesException
	 */
	public ForumTopic createForumTopic(ForumTopic topic) throws ClientServicesException {
		return createForumTopic(topic,topic.getForumUuid());
	}

	/**
	 * To add a topic to a stand-alone forum, send an Atom entry document 
	 * containing the forum topic to the forum topics resource.<br>
	 * See Authenticating requests for information about how to authenticate the request.
	 * 
	 * @param topic
	 * 		the topic to be created
	 * @param forumId
	 * 		the id of the forum where the topic will be added
	 * @return ForumTopic
	 * @throws ClientServicesException
	 */
	public ForumTopic createForumTopic(ForumTopic topic,String forumId) throws ClientServicesException {
		if (null == topic){
			throw new ClientServicesException(null,"Topic object passed was null");
		}
		Response result = null;
		try {
			BaseForumTransformer transformer = new BaseForumTransformer(topic);
			Object 	payload = transformer.transform(topic.getFieldsMap());

			Map<String, String> params = new HashMap<String, String>();
			params.put(FORUM_UNIQUE_IDENTIFIER, forumId);

			String url = ForumUrls.TOPICS.format(this);
			result = createData(url, params, getAtomHeaders(), payload);
			topic = getForumTopicFeedHandler().createEntity(result);

		} catch (Exception e) {
			throw new ClientServicesException(e, "error creating forum");
		}

		return topic;
	}
	
	/**
	 * To retrieve complete information about a forum topic,
	 * use the edit link found in the forum topic entry in the forum topics feed.
	 * 
	 * @param topicId
	 * 				The id of the forum topic to be retrieved
	 * @return ForumTopic
	 * @throws ClientServicesException
	 */
	public ForumTopic getForumTopic(String topicId) throws ClientServicesException {
		return getForumTopic(topicId, null);
	}

	/**
	 * To retrieve complete information about a forum topic,
	 * use the edit link found in the forum topic entry in the forum topics feed.
	 * 
	 * @param topicId
	 * 				The id of the forum topic to be retrieved
	 * @param parameters
	 * @return ForumTopic
	 * @throws ClientServicesException
	 */
	public ForumTopic getForumTopic(String topicId, Map<String, String> parameters) throws ClientServicesException {
		parameters = getParameters(parameters);
		parameters.put(TOPIC_UNIQUE_IDENTIFIER, topicId);
		String myTopicsUrl = ForumUrls.TOPIC.format(this);
		return getForumTopicEntity(myTopicsUrl, parameters);
	}

	/**
	 * To update a topic in a stand-alone forum, 
	 * send an updated forum topic document in Atom format 
	 * to the existing forum topic's edit web address.<br>
	 * All existing forum topic metadata will be replaced with the new data.
	 * To avoid deleting all existing data, retrieve any data 
	 * you want to retain first, and send it back with this request.
	 * See Retrieving forum topics for more information.<br>
	 * See Authenticating requests for information about how to authenticate the request.
	 * 
	 * @param ForumTopic
	 * @throws ClientServicesException
	 */
	public void updateForumTopic(ForumTopic topic) throws ClientServicesException {
		if (null == topic || StringUtil.isEmpty(topic.getUid())) {
			throw new ClientServicesException(null,"Topic object passed was null");
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

			getClientService().put(url, parameters, getAtomHeaders(), payload,ClientService.FORMAT_NULL);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ClientServicesException(e, "error updating topic");
		}
	}
	
	/**
	 * To delete a topic from a forum, use the HTTP DELETE method.<bR>
	 * Only the owner of a stand-alone forum can delete a topic from the forum.
	 * Deleted topics cannot be restored.<br>
	 * See Authenticating requests for information about how to authenticate the request.
	 * 
	 * @param ForumTopic
	 * 				forum topic which is to be deleted
	 * @throws ClientServicesException
	 */
	public void removeForumTopic(ForumTopic topic) throws ClientServicesException {
		removeForum(topic.getUid());
	}

	/**
	 * To delete a topic from a forum, use the HTTP DELETE method.<bR>
	 * Only the owner of a stand-alone forum can delete a topic from the forum.
	 * Deleted topics cannot be restored.<br>
	 * See Authenticating requests for information about how to authenticate the request.
	 * 
	 * @param String
	 * 				topicUuid which is to be deleted
	 * @throws ClientServicesException
	 */
	public void removeForumTopic(String topicUuid) throws ClientServicesException {
		if (StringUtil.isEmpty(topicUuid)){
			throw new ClientServicesException(null, "null topic id");
		}

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(TOPIC_UNIQUE_IDENTIFIER, topicUuid);
		String deleteTopicUrl = ForumUrls.TOPIC.format(this);
		super.deleteData(deleteTopicUrl, parameters, TOPIC_UNIQUE_IDENTIFIER);
	}

	////////////////////////////////////////////////////////////////////////////////
	//TODO: Moving a stand-alone forum topic programmatically
	////////////////////////////////////////////////////////////////////////////////

	/**
	 * To post a reply to a topic in a stand-alone forum, 
	 * send an Atom entry document containing the forum reply to the forum replies resource.<br>
	 * See Authenticating requests for information about how to authenticate the request.
	 * 
	 * @param ForumReply
	 * @return ForumReply
	 * @throws ClientServicesException
	 */
	public ForumReply createForumReply(ForumReply reply) throws ClientServicesException {
		return createForumReply(reply, reply.getTopicUuid());
	}

	/**
	 * To post a reply to a topic in a stand-alone forum, 
	 * send an Atom entry document containing the forum reply to the forum replies resource.<br>
	 * See Authenticating requests for information about how to authenticate the request.
	 * 
	 * @param ForumReply
	 * @param topicId
	 * @return ForumReply
	 * @throws ClientServicesException
	 */
	public ForumReply createForumReply(ForumReply reply, String topicId) throws ClientServicesException {
		if (null == reply || StringUtil.isEmpty(topicId)){
			throw new ClientServicesException(null,"Reply object passed was null");
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

			String url = ForumUrls.REPLIES.format(this);
			result = createData(url, params, getAtomHeaders(), payload);
			reply = getForumReplyFeedHandler().createEntity(result);

		} catch (Exception e) {
			throw new ClientServicesException(e, "error creating forum reply");
		}

		return reply;
	}

	/**
	 * To retrieve complete information about a reply to a topic in a stand-alone forum,
	 * use the edit link found in the forum reply entry in the forum replies feed.<br>
	 * You can use this operation to obtain forum reply information that you want to preserve
	 * prior to performing an update. See Updating forum replies for more information.<br>
	 * This method returns the Atom entry of a single reply entry as opposed to a feed of all the reply entries.
	 * See Authenticating requests for information about how to authenticate the request.
	 * 
	 * @param replyId
	 * @return ForumReply
	 * @throws ClientServicesException
	 */
	public ForumReply getForumReply(String replyId) throws ClientServicesException {
		return getForumReply(replyId, null);
	}

	/**
	 * To retrieve complete information about a reply to a topic in a stand-alone forum,
	 * use the edit link found in the forum reply entry in the forum replies feed.<br>
	 * You can use this operation to obtain forum reply information that you want to preserve
	 * prior to performing an update. See Updating forum replies for more information.<br>
	 * This method returns the Atom entry of a single reply entry as opposed to a feed of all the reply entries.
	 * See Authenticating requests for information about how to authenticate the request.
	 * 
	 * @param replyId
	 * @param parameters
	 * @return ForumReply
	 * @throws ClientServicesException
	 */
	public ForumReply getForumReply(String replyId, Map<String, String> parameters) throws ClientServicesException {
		parameters = getParameters(parameters);
		String myRepliesUrl = ForumUrls.REPLY.format(this);
		parameters.put(REPLY_UNIQUE_IDENTIFIER, replyId);
		return getForumReplyEntity(myRepliesUrl, parameters);
	}

	/**
	 * To update a reply to a topic in a stand-alone forum, 
	 * send an updated forum reply document in Atom format 
	 * to the existing reply's edit web address.<br>
	 * All existing forum reply entry metadata will be replaced with the new data.<br>
	 * To avoid deleting all existing data, retrieve any data you want to retain first,
	 * and send it back with this request. <br>
	 * See Retrieving forum replies for more information. 
	 * If you want to delete a reply and provide a customized message
	 * about why the reply was removed, send this request without 
	 * first retrieving the reply data and include a 
	 * <category scheme="http://www.ibm.com/xmlns/prod/sn/flags term="deleted"> element in the entry content.<br>
	 * See Deleting forum replies for more information.<br> 
	 * See Authenticating requests for information about how to authenticate the request.
	 * 
	 * @param ForumReply
	 * @return void
	 * @throws ClientServicesException
	 */
	public void updateForumReply(ForumReply reply) throws ClientServicesException {
		if (null == reply){
			throw new ClientServicesException(null,"Reply object passed was null");
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

			String url = ForumUrls.REPLY.format(this);
			updateData(url, params, getAtomHeaders(), payload, reply.getUid());

		} catch (Exception e) {
			throw new ClientServicesException(e, "error updating forum reply");
		}
	}

	/**
	 * Because replies often have child replies associated with them, when you delete a reply from a forum,
	 * the reply is not entirely removed. <br>
	 * Instead, if you use the HTTP DELETE command to delete the reply,
	 * the reply content is replaced with a standard message that states that the content has been removed.<br>
	 * If you want a custom message to be displayed, use the HTTP PUT command instead.<br>
	 * Only the owner of a stand-alone forum can delete a forum reply from it. Deleted replies cannot be restored. <br>
	 *  
	 * @param ForumReply
	 * 				reply which is to be deleted
	 * @throws CommunityServiceException
	 */
	public void removeForumReply(ForumReply reply) throws ClientServicesException {
		removeForum(reply.getUid());
	}

	/**
	 * Because replies often have child replies associated with them, when you delete a reply from a forum,
	 * the reply is not entirely removed. <br>
	 * Instead, if you use the HTTP DELETE command to delete the reply,
	 * the reply content is replaced with a standard message that states that the content has been removed.<br>
	 * If you want a custom message to be displayed, use the HTTP PUT command instead.<br>
	 * Only the owner of a stand-alone forum can delete a forum reply from it. Deleted replies cannot be restored. <br>
	 *  
	 * @param String
	 * 				replyUuid which is to be deleted
	 * @throws ClientServicesException
	 */
	public void removeForumReply(String replyUuid) throws ClientServicesException {
		if (StringUtil.isEmpty(replyUuid)){
			throw new ClientServicesException(null, "null reply id");
		}

		try {
			Map<String, String> parameters = new HashMap<String, String>();

			parameters.put(REPLY_UNIQUE_IDENTIFIER, replyUuid);
			String deleteReplyUrl = ForumUrls.REPLY.format(this);

			super.deleteData(deleteReplyUrl, parameters, REPLY_UNIQUE_IDENTIFIER);
		} catch (Exception e) {
			throw new ClientServicesException(e,"error deleting forum reply");
		} 	
	}

	////////////////////////////////////////////////////////////////////////////////
	//TODO: Splitting a stand-alone forum reply programmatically
	////////////////////////////////////////////////////////////////////////////////

	/***************************************************************
	 * Working with stand-alone forum members
	 ****************************************************************/

	////////////////////////////////////////////////////////////////////////////////
	//TODO: ALL WORKING WITH FORUM MEMBERS
	////////////////////////////////////////////////////////////////////////////////

	/***************************************************************
	 * Working with stand-alone forum recommendations
	 ****************************************************************/

	/**
	 * To like a post(topic/reply) in a stand-alone forum, 
	 * send an Atom entry document containing the forum recommendation 
	 * to the forum topic/reply resources.<br>
	 * See Authenticating requests for information about how to authenticate the request.
	 *  
	 * @since 4.5
	 * @param postUuid
	 * @return Recommendation
	 * @throws ClientServicesException
	 */
	public Recommendation createRecommendation(String postUuid) throws ClientServicesException{
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
			recommendation = getRecommendationFeedHandler().createEntity(result);

		} catch (Exception e) {
			throw new ClientServicesException(e);
		}
		return recommendation;
	}
	
	////////////////////////////////////////////////////////////////////////////////
	//TODO: Retrieving a stand-alone forum recommendation programatically
	////////////////////////////////////////////////////////////////////////////////

	/**
	 * To delete a recommendation of a post(topic or reply) 
	 * in a forum, use the HTTP DELETE method.<br>
	 * Only the user who have already recommended the post
	 * can delete it's own recommendation.<br>
	 * See Authenticating requests for information about how to authenticate the request.
	 *  
	 * @since 4.5
	 * @param postUuid
	 * @return boolean
	 * @throws ClientServicesException
	 */
	public boolean deleteRecommendation(String postUuid) throws ClientServicesException{
		checkVersion();
		String recommendationsUrl = ForumUrls.RECOMMENDATION_ENTRIES.format(this);
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(POST_UNIQUE_IDENTIFIER, postUuid);
		try {
			deleteData(recommendationsUrl, parameters, postUuid);
			return true;
		} catch (Exception e) {
			throw new ClientServicesException(e);
		}
	}

	//----------------------------------------------------------------------------------------------

	/**
	 * Wrapper method to create a Topic for default Forum of a Community
	 * <p>
	 * User should be authenticated to call this method
	 * @param ForumTopic
	 * @return Topic
	 * @throws ClientServicesException
	 */
	public ForumTopic createCommunityForumTopic(ForumTopic topic,String communityId) throws ClientServicesException {
		if (null == topic){
			throw new ClientServicesException(null,"Topic object passed was null");
		}
		Response result = null;
		try {
			BaseForumTransformer transformer = new BaseForumTransformer(topic);
			Object 	payload = transformer.transform(topic.getFieldsMap());

			Map<String, String> params = new HashMap<String, String>();
			params.put(COMM_UNIQUE_IDENTIFIER, communityId);

			String postUrl = ForumUrls.TOPICS.format(this);
			result = createData(postUrl, params, getAtomHeaders() ,payload);
			topic = getForumTopicFeedHandler().createEntity(result);

		} catch (Exception e) {
			throw new ClientServicesException(e, "error creating forum");
		}

		return topic;
	}


	 /**
     * Get a list for forum replies that includes the replies in the specified post.
     * The post uuid must be specified in the parametetrs as either:
     * topicUuid or replyUuid 
     * 
	 * @param parameters
	 * @return ReplyList
	 * @throws ClientServicesException
	 */
	public EntityList<ForumReply> getForumReplies(Map<String, String> parameters) throws ClientServicesException {
		return getReplies(parameters);
	}

	/**
     * Get a list for forum replies that includes the replies of a Forum Reply.
     * 
     * @param topicUuid
	 * @return ReplyList
	 * @throws ClientServicesException
	 */
	public EntityList<ForumReply> getForumReplyReplies(String replyUuid) throws ClientServicesException {
		return getForumReplyReplies(replyUuid, null);
	}
	/**
     * Get a list for forum replies that includes the replies of a Forum Reply.
     * 
     * @param replyUuid
	 * @param parameters
	 * @return ReplyList
	 * @throws ClientServicesException
	 */
	public EntityList<ForumReply> getForumReplyReplies(String replyUuid, Map<String, String> parameters) throws ClientServicesException {
		parameters = getParameters(parameters);
		parameters.put(REPLY_UNIQUE_IDENTIFIER, replyUuid);
		return getReplies(parameters);
	}

	/***************************************************************
	 * Handler factory methods
	 ****************************************************************/

	/**
	 * Returns a ForumFeedHandler
	 * @return
	 */
	public IFeedHandler<Forum> getForumFeedHandler() {
		return new AtomFeedHandler<Forum>(this) {
			@Override
			protected Forum entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Forum(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	/**
	 * Returns a ForumTopicFeedHandler
	 * @return
	 */
	public IFeedHandler<ForumTopic> getForumTopicFeedHandler() {
		return new AtomFeedHandler<ForumTopic>(this) {
			@Override
			protected ForumTopic entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new ForumTopic(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	/**
	 * Returns a ForumReplyFeedHandler
	 * @return
	 */
	public IFeedHandler<ForumReply> getForumReplyFeedHandler() {
		return new AtomFeedHandler<ForumReply>(this) {
			@Override
			protected ForumReply entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new ForumReply(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	/**
	 * Returns a RecommendationFeedHandler
	 * @return
	 */
	public IFeedHandler<Recommendation> getRecommendationFeedHandler() {
		return new AtomFeedHandler<Recommendation>(this) {
			@Override
			protected Recommendation entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Recommendation(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	/**
	 * Returns a TagFeedHandler
	 * @return
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

	protected Forum getForumEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return (Forum)getEntity(requestUrl, parameters, getForumFeedHandler());
	}

	protected ForumTopic getForumTopicEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return (ForumTopic)getEntity(requestUrl, parameters, getForumTopicFeedHandler());
	}

	protected ForumReply getForumReplyEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return (ForumReply)getEntity(requestUrl, parameters, getForumReplyFeedHandler());
	}

	protected Recommendation getRecommendationEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return (Recommendation)getEntity(requestUrl, parameters, getRecommendationFeedHandler());
	}

	protected EntityList<Forum> getForumEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getForumFeedHandler());
	}

	protected EntityList<ForumTopic> getForumTopicEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getForumTopicFeedHandler());
	}

	protected EntityList<ForumReply> getForumReplyEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getForumReplyFeedHandler());
	}

	protected EntityList<Recommendation> getRecommendationEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getRecommendationFeedHandler());
	}

	protected EntityList<Tag> getTagEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getTagFeedHandler());
	}

	/***************************************************************
	 * Utility methods
	 ****************************************************************/

	protected void checkVersion() throws ClientServicesException{
		if (!getApiVersion().isAtLeast(v4_5)){
			UnsupportedOperationException ex = new UnsupportedOperationException("This API is only supported on connections 4.5 or above");
			throw new ClientServicesException(ex);
		}
	}

	protected boolean isForumDeleted(Response response){
		StatusLine statusLine = response.getResponse().getStatusLine();
		return statusLine.getStatusCode() == 204;
	}

	private EntityList<ForumReply> getReplies(Map<String, String> parameters) throws ClientServicesException {
		if (parameters != null){
			if(StringUtil.isEmpty(parameters.get(TOPIC_UNIQUE_IDENTIFIER)) && 
					StringUtil.isEmpty(parameters.get(REPLY_UNIQUE_IDENTIFIER))){
					throw new ClientServicesException(null, "null post Uuid");
			}
		} else {
			parameters = new HashMap<String, String>();
		}
		String myRepliesUrl = ForumUrls.REPLIES.format(this);
		return getForumReplyEntityList(myRepliesUrl, parameters);
	}
}
