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
import com.ibm.sbt.services.client.base.CommonConstants.HTTPCode;
import com.ibm.sbt.services.client.base.ConnectionsService;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.common.Tag;
import com.ibm.sbt.services.client.connections.forums.serializers.ForumSerializer;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * The Forums application of IBM® Connections enables a team to discuss issues
 * that are pertinent to their work. The Forums API allows application programs
 * to create new forums, and to read and modify existing forums.
 * 
 * @see <a
 *      href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.5+API+Documentation#action=openDocument&res_title=Forums_API_ic45&content=pdcontent">
 *      Forums API</a>
 * 
 * @author Manish Kataria
 * @author Swati Singh
 * @author Carlos Manias
 * @author Paul Bastide <pbastide@us.ibm.com>
 */

public class ForumService extends ConnectionsService {

	private static final long serialVersionUID = -4926901916081556236L;

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

	@Override
	protected void initServiceMappingKeys() {
		serviceMappingKeys = new String[] { "forums" };
	}

	@Override
	public NamedUrlPart getAuthType() {
		String auth = super.getAuthType().getValue();
		auth = AuthType.BASIC.get().equalsIgnoreCase(auth) ? "" : auth;
		return new NamedUrlPart("authType", auth);
	}

	/***************************************************************
	 * Getting Forum feeds
	 ****************************************************************/

	/**
	 * Get a feed that includes all stand-alone and community forums created in
	 * the enterprise.
	 * 
	 * @return EntityList&lt;Forum&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Forum> getAllForums() throws ClientServicesException {
		return getAllForums(null);

	}

	/**
	 * Get a feed that includes all stand-alone and community forums created in
	 * the enterprise. You can narrow down the forums that are returned by
	 * passing parameters to the request that you use to retrieve the feed.
	 * 
	 * @param parameters
	 * @return EntityList&lt;Forum&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Forum> getAllForums(Map<String, String> parameters)
			throws ClientServicesException {
		String allForumsUrl = ForumUrls.FORUMS.format(this);
		return getForumEntityList(allForumsUrl, parameters);
	}

	/**
	 * Get a feed that includes all of the forums hosted by the Forums
	 * application.
	 * 
	 * @return EntityList&lt;Forum&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Forum> getPublicForums() throws ClientServicesException {
		return getPublicForums(null);

	}

	/**
	 * /** Get a feed that includes all of the forums hosted by the Forums
	 * application. You can narrow down the forums that are returned by passing
	 * parameters to the request that you use to retrieve the feed.
	 * 
	 * @param parameters
	 * @return EntityList&lt;Forum&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Forum> getPublicForums(Map<String, String> parameters)
			throws ClientServicesException {
		String publicForumsUrl = ForumUrls.FORUMS_PUBLIC.format(this);
		return getForumEntityList(publicForumsUrl, parameters);
	}

	/**
	 * Get a feed that includes forums created by the authenticated user or
	 * associated with communities to which the user belongs.
	 * 
	 * @return EntityList&lt;Forum&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Forum> getMyForums() throws ClientServicesException {
		return getMyForums(null);
	}

	/**
	 * Get a feed that includes forums created by the authenticated user or
	 * associated with communities to which the user belongs. You can narrow
	 * down the forums that are returned by passing parameters to the request
	 * that you use to retrieve the feed.
	 * 
	 * @param parameters
	 * @return EntityList&lt;Forum&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Forum> getMyForums(Map<String, String> parameters)
			throws ClientServicesException {
		String myForumsUrl = ForumUrls.FORUMS_MY.format(this);
		return getForumEntityList(myForumsUrl, parameters);
	}

	/**
	 * Get a feed that includes the topics in a specific stand-alone forum.
	 * 
	 * @param forumUuid
	 * @return EntityList&lt;ForumTopic&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<ForumTopic> getForumTopics(String forumUid)
			throws ClientServicesException {
		return getForumTopics(forumUid, null);
	}

	/**
	 * Get a feed that includes the topics in a specific stand-alone forum. You
	 * can narrow down the topics that are returned by passing parameters to the
	 * request that you use to retrieve the feed.
	 * 
	 * @param forumuuid
	 * @param parameters
	 * @return EntityList&lt;ForumTopic&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<ForumTopic> getForumTopics(String forumUuid,
			Map<String, String> parameters) throws ClientServicesException {
		String myForumTopicsUrl = ForumUrls.TOPIC.format(this,
				ForumUrls.forumPart(forumUuid));
		return getForumTopicEntityList(myForumTopicsUrl, parameters);
	}

	/**
	 * Get a feed that includes the topics in a specific community forum.
	 * 
	 * @param communityUuid
	 * @return EntityList&lt;ForumTopic&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<ForumTopic> getCommunityTopics(String communityUuid)
			throws ClientServicesException {
		return getCommunityTopics(communityUuid, null);
	}

	/**
	 * Get a feed that includes the topics in a specific community forum. You
	 * can narrow down the topics that are returned by passing parameters to the
	 * request that you use to retrieve the feed.
	 * 
	 * @param communityUuid
	 * @param parameters
	 * @return EntityList&lt;ForumTopic&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<ForumTopic> getCommunityTopics(String communityUuid,
			Map<String, String> parameters) throws ClientServicesException {
		String myTopicsUrl = ForumUrls.COMMUNITY_TOPICS.format(this,
				ForumUrls.communityPart(communityUuid));
		return getForumTopicEntityList(myTopicsUrl, parameters);
	}

	/**
	 * Get a feed that includes the topics in a specific forum, whether it is a
	 * stand-alone or community forum.
	 * 
	 * @return EntityList&lt;ForumTopic&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<ForumTopic> getPublicForumTopics()
			throws ClientServicesException {
		return getPublicForumTopics(null);
	}

	/**
	 * Get a feed that includes the topics in a specific forum, whether it is a
	 * stand-alone or community forum. You can narrow down the topics that are
	 * returned by passing parameters to the request that you use to retrieve
	 * the feed.
	 * 
	 * @param parameters
	 * @return EntityList&lt;ForumTopic&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<ForumTopic> getPublicForumTopics(
			Map<String, String> parameters) throws ClientServicesException {
		String myTopicsUrl = ForumUrls.PUBLIC_TOPICS.format(this);
		return getForumTopicEntityList(myTopicsUrl, parameters);
	}

	/**
	 * Get a feed that includes the topics that the authenticated user created
	 * in stand-alone forums and in forums associated with communities to which
	 * the user belongs. You can narrow down the topics that are returned by
	 * passing parameters to the request that you use to retrieve the feed.
	 * 
	 * @return EntityList&lt;ForumTopic&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<ForumTopic> getMyForumTopics()
			throws ClientServicesException {
		return getMyForumTopics(null);
	}

	/**
	 * Get a feed that includes the topics that the authenticated user created
	 * in stand-alone forums and in forums associated with communities to which
	 * the user belongs. You can narrow down the topics that are returned by
	 * passing parameters to the request that you use to retrieve the feed.
	 * 
	 * @param parameters
	 * @return EntityList&lt;ForumTopic&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<ForumTopic> getMyForumTopics(
			Map<String, String> parameters) throws ClientServicesException {
		String myTopicsUrl = ForumUrls.TOPICS_MY.format(this);
		return getForumTopicEntityList(myTopicsUrl, parameters);
	}

	/**
	 * Get a feed that includes all of the replies for a specific forum topic.
	 * You can narrow down the replies that are returned by passing parameters
	 * to the request that you use to retrieve the feed.
	 * 
	 * @param topicUuid
	 * @return EntityList&lt;ForumReply&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<ForumReply> getForumTopicReplies(String topicUuid)
			throws ClientServicesException {
		return getForumTopicReplies(topicUuid, null);
	}

	/**
	 * Get a feed that includes all of the replies for a specific forum topic.
	 * You can narrow down the replies that are returned by passing parameters
	 * to the request that you use to retrieve the feed.
	 * 
	 * @param topicUuid
	 * @param parameters
	 * @return EntityList&lt;ForumReply&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<ForumReply> getForumTopicReplies(String topicUuid,
			Map<String, String> parameters) throws ClientServicesException {
		String url = ForumUrls.TOPIC_REPLIES.format(this,
				ForumUrls.topicPart(topicUuid));
		return getReplies(url, parameters);
	}

	/**
	 * Get a category document that lists the tags that have been assigned to
	 * forums.
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
	public EntityList<Tag> getForumTopicsTags(String forumUuid)
			throws ClientServicesException {
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
	public EntityList<Tag> getForumTopicsTags(String forumUuid,
			Map<String, String> parameters) throws ClientServicesException {
		String tagsUrl = ForumUrls.TAGS_TOPICS.format(this,
				ForumUrls.forumPart(forumUuid));
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
	public EntityList<Recommendation> getRecommendations(String postUuid)
			throws ClientServicesException {
		checkVersion();
		String recommendationsUrl = ForumUrls.RECOMMENDATION_ENTRIES.format(
				this, ForumUrls.postPart(postUuid));
		return getRecommendationEntityList(recommendationsUrl, null);
	}

	// //////////////////////////////////////////////////////////////////////////////
	// TODO: Getting a list of inappropriate content flag options in Forums
	// //////////////////////////////////////////////////////////////////////////////

	// //////////////////////////////////////////////////////////////////////////////
	// TODO: Flagging a forum entry or reply as inappropriate
	// //////////////////////////////////////////////////////////////////////////////

	/***************************************************************
	 * Working with stand-alone forums
	 ****************************************************************/

	/**
	 * To create a stand-alone forum, send an Atom entry document containing the
	 * forum entry document to the root collection resource. See Authenticating
	 * requests for information about how to authenticate the request.
	 * 
	 * @param Forum
	 * @return Forum
	 * @throws ClientServicesException
	 */
	public Forum createForum(Forum forum) throws ClientServicesException {
		if (null == forum) {
			throw new ClientServicesException(null, "null forum");
		}
		ForumSerializer serializer = new ForumSerializer(forum);

		String url = ForumUrls.FORUMS.format(this);
		Response response = createData(url, null, getAtomHeaders(),
				serializer.generateCreate());
		checkResponseCode(response, HTTPCode.CREATED);
		forum = getForumFeedHandler(false).createEntity(response);

		return forum;
	}

	/**
	 * To retrieve complete information about a forum, use the edit <link> found
	 * in the forum entry in the Forums feed.
	 * 
	 * @param forumUuid
	 *            id of forum
	 * @return A Forum
	 * @throws ClientServicesException
	 */
	public Forum getForum(String forumUuid) throws ClientServicesException {
		String url = ForumUrls.FORUM.format(this,
				ForumUrls.forumPart(forumUuid));
		return getForumEntity(url, null);
	}

	/**
	 * To update a stand-alone forum, send an updated forum document in Atom
	 * format to the existing forum's edit web address.<br>
	 * All existing forum entry metadata will be replaced with the new data. To
	 * avoid deleting all existing data, retrieve any data you want to retain
	 * first, and send it back with this request. See Retrieving forums for more
	 * information. <br>
	 * See Authenticating requests for information about how to authenticate the
	 * request.
	 * 
	 * @param Forum
	 * @return Response
	 * @throws ClientServicesException
	 */
	public void updateForum(Forum forum) throws ClientServicesException {
		if (null == forum) {
			throw new ClientServicesException(null, "null forum");
		}

		ForumSerializer serializer = new ForumSerializer(forum);
		String url = ForumUrls.FORUM.format(this,
				ForumUrls.forumPart(forum.getForumUuid()));

		Response response = updateData(url, null, serializer.generateUpdate(),
				null);
		checkResponseCode(response, HTTPCode.OK);
	}

	/**
	 * To delete a recommendation of a post(topic or reply) in a forum, use the
	 * HTTP DELETE method.<br>
	 * Only the user who have already recommended the post can delete it's own
	 * recommendation.<br>
	 * See Authenticating requests for information about how to authenticate the
	 * request.
	 * 
	 * @param Forum
	 *            forum which is to be deleted
	 * @throws CommunityServiceException
	 */
	public void deleteForum(Forum forum) throws ClientServicesException {
		deleteForum(forum.getUid());
	}

	/**
	 * To delete a recommendation of a post(topic or reply) in a forum, use the
	 * HTTP DELETE method.<br>
	 * Only the user who have already recommended the post can delete it's own
	 * recommendation.<br>
	 * See Authenticating requests for information about how to authenticate the
	 * request.
	 * 
	 * @param String
	 *            forumUuid which is to be deleted
	 * @throws ClientServicesException
	 */
	public void deleteForum(String forumUuid) throws ClientServicesException {
		String deleteForumUrl = ForumUrls.FORUM.format(this,
				ForumUrls.forumPart(forumUuid));
		Response response = deleteData(deleteForumUrl, null, null);
		checkResponseCode(response, HTTPCode.NO_CONTENT);
	}

	/***************************************************************
	 * Working with stand-alone forum topics
	 ****************************************************************/

	/**
	 * To add a topic to a stand-alone forum, send an Atom entry document
	 * containing the forum topic to the forum topics resource.<br>
	 * See Authenticating requests for information about how to authenticate the
	 * request.
	 * 
	 * @param topic
	 *            the topic to be created
	 * @return ForumTopic
	 * @throws ClientServicesException
	 */
	public ForumTopic createForumTopic(ForumTopic topic)
			throws ClientServicesException {
		return createForumTopic(topic, topic.getForumUuid());
	}

	/**
	 * To add a topic to a stand-alone forum, send an Atom entry document
	 * containing the forum topic to the forum topics resource.<br>
	 * See Authenticating requests for information about how to authenticate the
	 * request.
	 * 
	 * @param topic
	 *            the topic to be created
	 * @param forumUuid
	 *            the id of the forum where the topic will be added
	 * @return ForumTopic
	 * @throws ClientServicesException
	 */
	public ForumTopic createForumTopic(ForumTopic topic, String forumUuid)
			throws ClientServicesException {
		if (null == topic) {
			throw new ClientServicesException(null,
					"Topic object passed was null");
		}
		ForumSerializer serializer = new ForumSerializer(topic);

		String url = ForumUrls.FORUM_TOPICS.format(this,
				ForumUrls.forumPart(forumUuid));
		Response response = createData(url, null, getAtomHeaders(),
				serializer.generateCreate());
		checkResponseCode(response, HTTPCode.CREATED);
		topic = getForumTopicFeedHandler(false).createEntity(response);

		return topic;
	}

	/**
	 * To retrieve complete information about a forum topic, use the edit link
	 * found in the forum topic entry in the forum topics feed.
	 * 
	 * @param topicUuid
	 *            The id of the forum topic to be retrieved
	 * @return ForumTopic
	 * @throws ClientServicesException
	 */
	public ForumTopic getForumTopic(String topicUuid)
			throws ClientServicesException {
		return getForumTopic(topicUuid, null);
	}

	/**
	 * To retrieve complete information about a forum topic, use the edit link
	 * found in the forum topic entry in the forum topics feed.
	 * 
	 * @param topicUuid
	 *            The id of the forum topic to be retrieved
	 * @param parameters
	 * @return ForumTopic
	 * @throws ClientServicesException
	 */
	public ForumTopic getForumTopic(String topicUuid,
			Map<String, String> parameters) throws ClientServicesException {
		String myTopicsUrl = ForumUrls.TOPIC.format(this,
				ForumUrls.topicPart(topicUuid));
		return getForumTopicEntity(myTopicsUrl, parameters);
	}

	/**
	 * To update a topic in a stand-alone forum, send an updated forum topic
	 * document in Atom format to the existing forum topic's edit web address.<br>
	 * All existing forum topic metadata will be replaced with the new data. To
	 * avoid deleting all existing data, retrieve any data you want to retain
	 * first, and send it back with this request. See Retrieving forum topics
	 * for more information.<br>
	 * See Authenticating requests for information about how to authenticate the
	 * request.
	 * 
	 * @param ForumTopic
	 * @throws ClientServicesException
	 */
	public void updateForumTopic(ForumTopic topic)
			throws ClientServicesException {
		if (null == topic || StringUtil.isEmpty(topic.getUid())) {
			throw new ClientServicesException(null,
					"Topic object passed was null");
		}
		String url = ForumUrls.TOPIC.format(this,
				ForumUrls.topicPart(topic.getUid()));

		ForumSerializer serializer = new ForumSerializer(topic);
		Response response = getClientService().put(url, null, getAtomHeaders(),
				serializer.generateUpdate(), ClientService.FORMAT_NULL);
		checkResponseCode(response, HTTPCode.OK);
	}

	/**
	 * To delete a topic from a forum, use the HTTP DELETE method.<bR>
	 * Only the owner of a stand-alone forum can delete a topic from the forum.
	 * Deleted topics cannot be restored.<br>
	 * See Authenticating requests for information about how to authenticate the
	 * request.
	 * 
	 * @param ForumTopic
	 *            forum topic which is to be deleted
	 * @throws ClientServicesException
	 */
	public void deleteForumTopic(ForumTopic topic)
			throws ClientServicesException {
		deleteForum(topic.getUid());
	}

	/**
	 * To delete a topic from a forum, use the HTTP DELETE method.<bR>
	 * Only the owner of a stand-alone forum can delete a topic from the forum.
	 * Deleted topics cannot be restored.<br>
	 * See Authenticating requests for information about how to authenticate the
	 * request.
	 * 
	 * @param String
	 *            topicUuid which is to be deleted
	 * @throws ClientServicesException
	 */
	public void deleteForumTopic(String topicUuid)
			throws ClientServicesException {
		String deleteTopicUrl = ForumUrls.TOPIC.format(this,
				ForumUrls.topicPart(topicUuid));
		Response response = deleteData(deleteTopicUrl, null, null);
		checkResponseCode(response, HTTPCode.NO_CONTENT);
	}

	// //////////////////////////////////////////////////////////////////////////////
	// TODO: Moving a stand-alone forum topic programmatically
	// //////////////////////////////////////////////////////////////////////////////

	/**
	 * To post a reply to a topic in a stand-alone forum, send an Atom entry
	 * document containing the forum reply to the forum replies resource.<br>
	 * See Authenticating requests for information about how to authenticate the
	 * request.
	 * 
	 * @param ForumReply
	 * @return ForumReply
	 * @throws ClientServicesException
	 */
	public ForumReply createForumReply(ForumReply reply)
			throws ClientServicesException {
		return createForumReply(reply.getTopicUuid(), reply);
	}

	/**
	 * To post a reply to a topic in a stand-alone forum, send an Atom entry
	 * document containing the forum reply to the forum replies resource.<br>
	 * See Authenticating requests for information about how to authenticate the
	 * request.
	 * 
	 * @param ForumReply
	 * @param topicUuid
	 * @return ForumReply
	 * @throws ClientServicesException
	 */
	public ForumReply createForumReply(String topicUuid, ForumReply reply)
			throws ClientServicesException {
		if (StringUtil.isEmpty(reply.getTopicUuid())) {
			reply.setTopicUuid(topicUuid);
		}
		ForumSerializer serializer = new ForumSerializer(reply);
		String url = ForumUrls.TOPIC_REPLIES.format(this,
				ForumUrls.topicPart(topicUuid));
		Response response = createData(url, null, getAtomHeaders(),
				serializer.generateCreate());
		checkResponseCode(response, HTTPCode.CREATED);
		reply = getForumReplyFeedHandler(false).createEntity(response);

		return reply;
	}

	/**
	 * To retrieve complete information about a reply to a topic in a
	 * stand-alone forum, use the edit link found in the forum reply entry in
	 * the forum replies feed.<br>
	 * You can use this operation to obtain forum reply information that you
	 * want to preserve prior to performing an update. See Updating forum
	 * replies for more information.<br>
	 * This method returns the Atom entry of a single reply entry as opposed to
	 * a feed of all the reply entries. See Authenticating requests for
	 * information about how to authenticate the request.
	 * 
	 * @param replyUuid
	 * @return ForumReply
	 * @throws ClientServicesException
	 */
	public ForumReply getForumReply(String replyUuid)
			throws ClientServicesException {
		return getForumReply(replyUuid, null);
	}

	/**
	 * To retrieve complete information about a reply to a topic in a
	 * stand-alone forum, use the edit link found in the forum reply entry in
	 * the forum replies feed.<br>
	 * You can use this operation to obtain forum reply information that you
	 * want to preserve prior to performing an update. See Updating forum
	 * replies for more information.<br>
	 * This method returns the Atom entry of a single reply entry as opposed to
	 * a feed of all the reply entries. See Authenticating requests for
	 * information about how to authenticate the request.
	 * 
	 * @param replyUuid
	 * @param parameters
	 * @return ForumReply
	 * @throws ClientServicesException
	 */
	public ForumReply getForumReply(String replyUuid,
			Map<String, String> parameters) throws ClientServicesException {
		String myRepliesUrl = ForumUrls.REPLY.format(this,
				ForumUrls.replyPart(replyUuid));
		return getForumReplyEntity(myRepliesUrl, parameters);
	}

	/**
	 * To update a reply to a topic in a stand-alone forum, send an updated
	 * forum reply document in Atom format to the existing reply's edit web
	 * address.<br>
	 * All existing forum reply entry metadata will be replaced with the new
	 * data.<br>
	 * To avoid deleting all existing data, retrieve any data you want to retain
	 * first, and send it back with this request. <br>
	 * See Retrieving forum replies for more information. If you want to delete
	 * a reply and provide a customized message about why the reply was removed,
	 * send this request without first retrieving the reply data and include a
	 * <category scheme="http://www.ibm.com/xmlns/prod/sn/flags term="deleted">
	 * element in the entry content.<br>
	 * See Deleting forum replies for more information.<br>
	 * See Authenticating requests for information about how to authenticate the
	 * request.
	 * 
	 * @param ForumReply
	 * @return void
	 * @throws ClientServicesException
	 */
	public void updateForumReply(ForumReply reply)
			throws ClientServicesException {
		if (null == reply) {
			throw new ClientServicesException(null,
					"Reply object passed was null");
		}

		ForumSerializer serializer = new ForumSerializer(reply);
		String url = ForumUrls.REPLY.format(this,
				ForumUrls.replyPart(reply.getReplyUuid()));
		Response response = updateData(url, null, getAtomHeaders(),
				serializer.generateUpdate(), null);
		checkResponseCode(response, HTTPCode.OK);
	}

	/**
	 * Because replies often have child replies associated with them, when you
	 * delete a reply from a forum, the reply is not entirely removed. <br>
	 * Instead, if you use the HTTP DELETE command to delete the reply, the
	 * reply content is replaced with a standard message that states that the
	 * content has been removed.<br>
	 * If you want a custom message to be displayed, use the HTTP PUT command
	 * instead.<br>
	 * Only the owner of a stand-alone forum can delete a forum reply from it.
	 * Deleted replies cannot be restored. <br>
	 * 
	 * @param ForumReply
	 *            reply which is to be deleted
	 * @throws CommunityServiceException
	 */
	public void deleteForumReply(ForumReply reply)
			throws ClientServicesException {
		deleteForum(reply.getUid());
	}

	/**
	 * Because replies often have child replies associated with them, when you
	 * delete a reply from a forum, the reply is not entirely removed. <br>
	 * Instead, if you use the HTTP DELETE command to delete the reply, the
	 * reply content is replaced with a standard message that states that the
	 * content has been removed.<br>
	 * If you want a custom message to be displayed, use the HTTP PUT command
	 * instead.<br>
	 * Only the owner of a stand-alone forum can delete a forum reply from it.
	 * Deleted replies cannot be restored. <br>
	 * 
	 * @param String
	 *            replyUuid which is to be deleted
	 * @throws ClientServicesException
	 */
	public void deleteForumReply(String replyUuid)
			throws ClientServicesException {
		String deleteReplyUrl = ForumUrls.REPLY.format(this,
				ForumUrls.replyPart(replyUuid));
		Response response = deleteData(deleteReplyUrl, null, null);
		checkResponseCode(response, HTTPCode.NO_CONTENT);
	}

	// //////////////////////////////////////////////////////////////////////////////
	// TODO: Splitting a stand-alone forum reply programmatically
	// //////////////////////////////////////////////////////////////////////////////

	/***************************************************************
	 * Working with stand-alone forum members
	 ****************************************************************/

	// //////////////////////////////////////////////////////////////////////////////
	// TODO: ALL WORKING WITH FORUM MEMBERS
	// //////////////////////////////////////////////////////////////////////////////

	/***************************************************************
	 * Working with stand-alone forum recommendations
	 ****************************************************************/

	/**
	 * To like a post(topic/reply) in a stand-alone forum, send an Atom entry
	 * document containing the forum recommendation to the forum topic/reply
	 * resources.<br>
	 * See Authenticating requests for information about how to authenticate the
	 * request.
	 * 
	 * @since 4.5
	 * @param postUuid
	 * @return Recommendation
	 * @throws ClientServicesException
	 */
	public Recommendation createRecommendation(String postUuid)
			throws ClientServicesException {
		checkVersion();
		String recommendationsUrl = ForumUrls.RECOMMENDATION_ENTRIES.format(
				this, ForumUrls.postPart(postUuid));
		Recommendation recommendation;
		// not using transformer, as the payload to be sent is constant
		String payload = "<entry xmlns='http://www.w3.org/2005/Atom'><category scheme='http://www.ibm.com/xmlns/prod/sn/type' term='recommendation'></category></entry>";
		Response response = createData(recommendationsUrl, null, null, payload);
		checkResponseCode(response, HTTPCode.CREATED);
		recommendation = getRecommendationFeedHandler().createEntity(response);

		return recommendation;
	}

	// //////////////////////////////////////////////////////////////////////////////
	// TODO: Retrieving a stand-alone forum recommendation programatically
	// //////////////////////////////////////////////////////////////////////////////

	/**
	 * To delete a recommendation of a post(topic or reply) in a forum, use the
	 * HTTP DELETE method.<br>
	 * Only the user who have already recommended the post can delete it's own
	 * recommendation.<br>
	 * See Authenticating requests for information about how to authenticate the
	 * request.
	 * 
	 * @since 4.5
	 * @param postUuid
	 * @return boolean
	 * @throws ClientServicesException
	 */
	public void deleteRecommendation(String postUuid)
			throws ClientServicesException {
		checkVersion();
		String recommendationsUrl = ForumUrls.RECOMMENDATION_ENTRIES.format(
				this, ForumUrls.postPart(postUuid));
		Response response = deleteData(recommendationsUrl, null, null);
		checkResponseCode(response, HTTPCode.NO_CONTENT);
	}

	// ----------------------------------------------------------------------------------------------

	/**
	 * Wrapper method to create a Topic for default Forum of a Community
	 * <p>
	 * User should be authenticated to call this method
	 * 
	 * @param ForumTopic
	 * @param communityUuid
	 * @return Topic
	 * @throws ClientServicesException
	 */
	public ForumTopic createCommunityForumTopic(ForumTopic topic,
			String communityUuid) throws ClientServicesException {
		if (null == topic) {
			throw new ClientServicesException(null,
					"Topic object passed was null");
		}
		ForumSerializer serializer = new ForumSerializer(topic);

		String postUrl = ForumUrls.COMMUNITY_TOPICS.format(this,
				ForumUrls.communityPart(communityUuid));
		Response response = createData(postUrl, null, getAtomHeaders(),
				serializer.generateCreate());
		checkResponseCode(response, HTTPCode.CREATED);
		topic = getForumTopicFeedHandler().createEntity(response);

		return topic;
	}

	/**
	 * Get a list for forum replies that includes the replies in the specified
	 * post. The post uuid must be specified in the parametetrs as either:
	 * topicUuid or replyUuid
	 * 
	 * @param topicUuid
	 * @return EntityList<ForumReply>
	 * @throws ClientServicesException
	 */
	public EntityList<ForumReply> getForumReplies(String topicUuid)
			throws ClientServicesException {
		return getForumReplies(topicUuid, null);
	}

	/**
	 * Get a list for forum replies that includes the replies in the specified
	 * post. The post uuid must be specified in the parametetrs as either:
	 * topicUuid or replyUuid
	 * 
	 * @param topicUuid
	 * @param parameters
	 * @return EntityList<ForumReply>
	 * @throws ClientServicesException
	 */
	public EntityList<ForumReply> getForumReplies(String topicUuid,
			Map<String, String> parameters) throws ClientServicesException {
		String url = ForumUrls.FORUM_REPLIES.format(this,
				ForumUrls.topicPart(topicUuid));
		return getReplies(url, parameters);
	}

	/**
	 * Get a list for forum replies that includes the replies of a Forum Reply.
	 * 
	 * @param replyUuid
	 * @return EntityList<ForumReply>
	 * @throws ClientServicesException
	 */
	public EntityList<ForumReply> getForumReplyReplies(String replyUuid)
			throws ClientServicesException {
		return getForumReplyReplies(replyUuid, null);
	}

	/**
	 * Get a list for forum replies that includes the replies of a Forum Reply.
	 * 
	 * @param replyUuid
	 * @param parameters
	 * @return EntityList<ForumReply>
	 * @throws ClientServicesException
	 */
	public EntityList<ForumReply> getForumReplyReplies(String replyUuid,
			Map<String, String> parameters) throws ClientServicesException {
		String url = ForumUrls.REPLY_REPLIES.format(this,
				ForumUrls.replyPart(replyUuid));
		return getReplies(url, parameters);
	}

	/***************************************************************
	 * Handler factory methods
	 ****************************************************************/

	/**
	 * Returns a ForumHandler
	 */
	public IFeedHandler<Forum> getForumFeedHandler() {
		return getForumFeedHandler(true);
	}

	/**
	 * Returns a ForumFeedHandler
	 * 
	 * @param isFeed
	 *            true if the response is a feed, false if the response contains
	 *            one entry.
	 * @return
	 */
	public IFeedHandler<Forum> getForumFeedHandler(boolean isFeed) {
		return new AtomFeedHandler<Forum>(this, isFeed) {
			@Override
			protected Forum entityInstance(BaseService service, Node node,
					XPathExpression xpath) {
				return new Forum(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	/**
	 * Returns a ForumTopicFeedHandler
	 * 
	 * @return
	 */
	public IFeedHandler<ForumTopic> getForumTopicFeedHandler() {
		return getForumTopicFeedHandler(true);
	}

	/**
	 * Returns a ForumTopicFeedHandler
	 * 
	 * @param isFeed
	 *            true if the response is a feed, false if the response contains
	 *            one entry.
	 * @return
	 */
	public IFeedHandler<ForumTopic> getForumTopicFeedHandler(boolean isFeed) {
		return new AtomFeedHandler<ForumTopic>(this, isFeed) {
			@Override
			protected ForumTopic entityInstance(BaseService service, Node node,
					XPathExpression xpath) {
				return new ForumTopic(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	/**
	 * Returns a ForumReplyFeedHandler
	 * 
	 * @return
	 */
	public IFeedHandler<ForumReply> getForumReplyFeedHandler() {
		return getForumReplyFeedHandler(true);
	}

	/**
	 * Returns a ForumReplyFeedHandler
	 * 
	 * @param isFeed
	 *            true if the response is a feed, false if the response contains
	 *            one entry.
	 * @return
	 */
	public IFeedHandler<ForumReply> getForumReplyFeedHandler(boolean isFeed) {
		return new AtomFeedHandler<ForumReply>(this, isFeed) {
			@Override
			protected ForumReply entityInstance(BaseService service, Node node,
					XPathExpression xpath) {
				return new ForumReply(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	/**
	 * Returns a RecommendationFeedHandler
	 * 
	 * @return
	 */
	public IFeedHandler<Recommendation> getRecommendationFeedHandler() {
		return new AtomFeedHandler<Recommendation>(this) {
			@Override
			protected Recommendation entityInstance(BaseService service,
					Node node, XPathExpression xpath) {
				return new Recommendation(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	/**
	 * Returns a TagFeedHandler
	 * 
	 * @return
	 */
	public IFeedHandler<Tag> getTagFeedHandler() {
		return new AtomFeedHandler<Tag>(this) {
			@Override
			protected Tag entityInstance(BaseService service, Node node,
					XPathExpression xpath) {
				return new Tag(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	/***************************************************************
	 * Factory methods
	 ****************************************************************/

	protected Forum getForumEntity(String requestUrl,
			Map<String, String> parameters) throws ClientServicesException {
		return (Forum) getEntity(requestUrl, parameters,
				getForumFeedHandler(false));
	}

	protected ForumTopic getForumTopicEntity(String requestUrl,
			Map<String, String> parameters) throws ClientServicesException {
		return (ForumTopic) getEntity(requestUrl, parameters,
				getForumTopicFeedHandler(false));
	}

	protected ForumReply getForumReplyEntity(String requestUrl,
			Map<String, String> parameters) throws ClientServicesException {
		return (ForumReply) getEntity(requestUrl, parameters,
				getForumReplyFeedHandler(false));
	}

	protected Recommendation getRecommendationEntity(String requestUrl,
			Map<String, String> parameters) throws ClientServicesException {
		return (Recommendation) getEntity(requestUrl, parameters,
				getRecommendationFeedHandler());
	}

	protected EntityList<Forum> getForumEntityList(String requestUrl,
			Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getForumFeedHandler());
	}

	protected EntityList<ForumTopic> getForumTopicEntityList(String requestUrl,
			Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getForumTopicFeedHandler());
	}

	protected EntityList<ForumReply> getForumReplyEntityList(String requestUrl,
			Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getForumReplyFeedHandler());
	}

	protected EntityList<Recommendation> getRecommendationEntityList(
			String requestUrl, Map<String, String> parameters)
			throws ClientServicesException {
		return getEntities(requestUrl, parameters,
				getRecommendationFeedHandler());
	}

	protected EntityList<Tag> getTagEntityList(String requestUrl,
			Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getTagFeedHandler());
	}

	/***************************************************************
	 * Utility methods
	 ****************************************************************/

	protected void checkVersion() throws ClientServicesException {
		if (!getApiVersion().isAtLeast(v4_5)) {
			UnsupportedOperationException ex = new UnsupportedOperationException(
					"This API is only supported on connections 4.5 or above");
			throw new ClientServicesException(ex);
		}
	}

	protected boolean isForumDeleted(Response response) {
		StatusLine statusLine = response.getResponse().getStatusLine();
		return statusLine.getStatusCode() == 204;
	}

	private EntityList<ForumReply> getReplies(String url,
			Map<String, String> parameters) throws ClientServicesException {
		return getForumReplyEntityList(url, null);
	}
}
