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

import java.util.Arrays;
import java.util.List;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.forums.model.BaseForumEntity;
import com.ibm.sbt.services.client.connections.forums.model.FlagType;

/**
 * Forum model object
 *
 * @author Manish Kataria
 */

public class ForumTopic extends BaseForumEntity{

	/**
	 * Constructor
	 *  
	 * @param BaseService
	 * @param DataHandler
	 */
	public ForumTopic(BaseService svc, DataHandler<?> handler) {
		super(svc, handler);
	}
	/**
	 * Constructor
	 *  
	 * @param ForumService
	 * @param id
	 */
	public ForumTopic(ForumService forumsService, String id) {
		super(forumsService,id);
	}
	/**
     * Constructor
     *
     * @param ForumService
     * @param id
     */
    public ForumTopic(ForumService forumsService) {
            super(forumsService);
    }
	/**
	 * This method returns Uuid of topic
	 * 
	 * @return {String} topicUuid
	 */
	public String getTopicUuid() {
		return super.getUid();
	}

	/**
	 * This method creates/updates the IBM Connections Forum Topic on the server
	 *
	 * @return ForumTopic
	 * @throws ForumServiceException
	 */
	public ForumTopic save(String forumId) throws ForumServiceException {
		if(StringUtil.isEmpty(getUid())){
			if(StringUtil.isEmpty(forumId) && StringUtil.isNotEmpty(getForumUuid())){ // if a forumId was not provided but was set using setForumUuid method
				forumId = getForumUuid();
			}else if(StringUtil.isEmpty(forumId) && StringUtil.isEmpty(getForumUuid())){ // Can not create topic without ForumUuid
				throw new ForumServiceException(new Exception("No Parent Forum ID mentioned while creating Forum Topic"));
			}
			return getService().createForumTopic(this,forumId);
		}else{
			//update scenario
			getService().updateForumTopic(this);
			return getService().getForumTopic(getUid());
		}
	}

	/**
	 * This method updates the IBM Connections Forum Topic on the server
	 *
	 * @return ForumTopic
	 * @throws ForumServiceException
	 */
	public ForumTopic save() throws ForumServiceException {
		return save("");
	}

	/**
	 * This method deletes the IBM Connections forum on the server
	 *
	 * @throws ForumServiceException
	 */

	public void remove() throws ForumServiceException {
		getService().removeForumTopic(getUid());
	}

	/**
	 * This method loads the IBM Connections Forum Topic
	 *
	 * @return ForumTopic
	 * @throws ForumServiceException
	 */

	public ForumTopic load() throws ForumServiceException
	{
		return getService().getForumTopic(getUid());
	}


	/**
	 * This method returns Replies for this IBM Connections forum Topic
	 *
	 * @return ReplyList
	 * @throws ForumServiceException
	 */

	public ReplyList getReplies() throws ForumServiceException
	{
		return getService().getForumTopicReplies(getUid());
	}

	/**
	 * This method returns Recommendations for the IBM Connections forum Topic
	 *
	 * @return RecommendationList
	 * @throws ForumServiceException
	 */
	public RecommendationList getRecommendations() throws ForumServiceException
	{
		return getService().getRecommendations(getUid());
	}
	/**
	 * Return the permissions of the IBM Connections forum topic from forum ATOM
	 * entry document.
	 *
	 * @return String
	 * @throws ForumServiceException
	 */

	public String getPermisisons() throws ForumServiceException
	{
		return super.getAsString(ForumsXPath.permissions);
	}

	/**
	 * Return the value of IBM Connections forum ID from forum ATOM
	 * entry document.
	 *
	 * @method getForumUuid
	 * @return {String} Forum ID of the forum
	 */
	public String getForumUuid(){
		String forumId = "";
		try {
			forumId = getAsString(ForumsXPath.inReplyTo);
		} catch (Exception e) {}

		if(StringUtil.isEmpty(forumId)){
			forumId = (String) fields.get("forumUuid");
		}
		return extractForumUuid(forumId);
	}

	/**
	 * Sets id of IBM Connections forum.
	 *
	 * @method setForumUuid
	 * @param {String} forumUuid Id of the forum
	 */
	public void setForumUuid(String forumUuid) {
		fields.put("forumUuid", forumUuid);
	}

	/**
	 * Return the url of the IBM Connections forum from
	 * forum ATOM entry document.
	 *
	 * @method getTopicUrl
	 * @return {String} Url of the forum
	 */
	public String getTopicUrl(){
		return getAsString(ForumsXPath.selfUrl);
	}
	/**
	 * Returns the total recommendation count for the thread, supported on Connections 4.5 or above
	 *
	 * @method getThreadRecommendationCount
	 * @return {String} total recommendation count
	 */
	public long getThreadRecommendationCount(){
		return getAsLong(ForumsXPath.ThreadRecommendationCount);
	}
	/**
	 * Returns the recommendation count for the topic, supported on Connections 4.5 or above
	 *
	 * @method getRecommendationCount
	 * @return {String} recommendation count
	 */
	public String getRecommendationCount(){
		return getAsString(ForumsXPath.RecommendationCount);
	}
	/**
	 * Returns the replied count for the topic
	 *
	 * @method getRepliesCount
	 * @return {String} replies count
	 */
	public String getRepliesCount(){
		return getAsString(ForumsXPath.threadCount);
	}
	/**
	 * Set to true if you want the topic to be added to the top of the forum thread.
	 * 
	 * @method setPinned
	 * @param pinned
	 */
	public void setPinned(boolean pinned) {
		setAsBoolean(FlagType.PIN.getFlagType(), pinned);
	}
	
	/**
	 * Set to true, indicates that the topic is locked. 
	 * 
	 * @method setLocked
	 * @param locked
	 */
	public void setLocked(boolean locked) {
		setAsBoolean(FlagType.LOCK.getFlagType(), locked);
	}
	
	/**
	 * to Like/Recommend a Topic, supported on Connections 4.5 or above
	 *
	 * @method like
	 * @return Recommendation
	 */
	public Recommendation like() throws ForumServiceException {
		return getService().createRecommendation(getTopicUuid());
	}
	/**
	 * to unLike/unRecommend a Topic, supported on Connections 4.5 or above
	 *
	 * @method unLike
	 * @return boolean
	 */
	public boolean unLike() throws ForumServiceException {
		return getService().deleteRecommendation(getTopicUuid());
	}
	/**
	 * Set to true, indicates that the topic is a question.
	 *
	 * @method setQuestion
	 * @param question
	 */
	public void setQuestion(boolean question) {
		setAsBoolean(FlagType.QUESTION.getFlagType(), question);
	}
	
	/**
	 * Reopen a topic as question
	 *
	 * @method ReopenQuestion
	 */
	public void ReopenQuestion() {
		setAsBoolean(FlagType.ANSWERED.getFlagType(), false);
	}
	
	/**
	 * Check if a Topic is pinned
	 *
	 * @method isPinned
	 * @return boolean
	 */
	public boolean isPinned(){
		boolean pin = false;
		if(StringUtil.isNotEmpty(getAsString(ForumsXPath.flag))){
			List<String> flags = Arrays.asList(getDataHandler().getAsString(ForumsXPath.flag).split(" "));
			for (int i = 0; i < flags.size(); i++) {
				if(StringUtil.equalsIgnoreCase(flags.get(i), FlagType.PIN.getFlagType())){
					pin = true;
					break;
				}
			}
		}
		return pin;

	}
	/**
	 * Check if a Topic is locked
	 *
	 * @method isLocked
	 * @return boolean
	 */
	public boolean isLocked(){
		boolean lock = false;
		if(StringUtil.isNotEmpty(getAsString(ForumsXPath.flag))){
			List<String> flags = Arrays.asList(getDataHandler().getAsString(ForumsXPath.flag).split(" "));
			for (int i = 0; i < flags.size(); i++) {
				if(StringUtil.equalsIgnoreCase(flags.get(i), FlagType.LOCK.getFlagType())){
					lock = true;
					break;
				}
			}
		}
		return lock;
	}
	/**
	 * Check if a Topic is a question
	 *
	 * @method isQuestion
	 * @return boolean
	 */
	public boolean isQuestion(){
		boolean question = false;
		if(StringUtil.isNotEmpty(getAsString(ForumsXPath.flag))){
			List<String> flags = Arrays.asList(getDataHandler().getAsString(ForumsXPath.flag).split(" "));
			for (int i = 0; i < flags.size(); i++) {
				if(StringUtil.equalsIgnoreCase(flags.get(i), FlagType.QUESTION.getFlagType())){
					question = true;
					break;
				}
			}
		}
		return question;
	}
	/**
	 * Check if a Topic is answered
	 *
	 * @method isAnswered
	 * @return boolean
	 */
	public boolean isAnswered(){
		boolean answered = false;
		if(StringUtil.isNotEmpty(getAsString(ForumsXPath.flag))){
			List<String> flags = Arrays.asList(getDataHandler().getAsString(ForumsXPath.flag).split(" "));
			for (int i = 0; i < flags.size(); i++) {
				if(StringUtil.equalsIgnoreCase(flags.get(i), FlagType.ANSWERED.getFlagType())){
					answered = true;
					break;
				}
			}
		}
		return answered;
	}


}