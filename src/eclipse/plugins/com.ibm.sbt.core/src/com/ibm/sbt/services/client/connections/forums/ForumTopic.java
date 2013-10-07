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

	public ForumTopic(BaseService svc, DataHandler<?> handler) {
		super(svc, handler);
	}

	public ForumTopic(ForumService forumsService, String id) {
		super(forumsService,id);
	}

	/**
	 * This method returns Uuid of topic
	 */
	public String getTopicUuid() throws ForumServiceException{
		return super.getUid();
	}

	/**
	 * This method creates/updates the IBM Connections Forum Topic on the server
	 * 
	 * @return
	 * @throws ForumServiceException
	 */
	public ForumTopic save(String forumId) throws ForumServiceException{
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
	 * @return
	 * @throws ForumServiceException
	 */
	public ForumTopic save() throws ForumServiceException{
		return save("");
	}

	/**
	 * This method deletes the IBM Connections forum on the server
	 * 
	 * @return
	 * @throws ForumServiceException
	 */

	public void remove() throws ForumServiceException {
		getService().removeForumTopic(getUid());
	}

	/**
	 * This method loads the IBM Connections Forum Topic
	 * 
	 * @return
	 * @throws ForumServiceException
	 */

	public ForumTopic load() throws ForumServiceException
	{
		return getService().getForumTopic(getUid());
	}


	/**
	 * This method returns Replies for this IBM Connections forum Topic
	 * 
	 * @return
	 * @throws ForumServiceException
	 */

	public ReplyList getReplies() throws ForumServiceException
	{
		return getService().getForumReplies(getUid());
	}


	/**
	 * Return the permissions of the IBM Connections forum topic from forum ATOM
	 * entry document.
	 * 
	 * @return
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
	public String getThreadRecommendationCount(){
		return getAsString(ForumsXPath.ThreadRecommendationCount);
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
	 * Pins a topic
	 * @method pin
	 */
	public void pin() {
		setAsString(ForumsXPath.flag, FlagType.PIN.getFlagType());
	}
	/**
	 * un-pin a topic
	 * @method unPin
	 */
	public void unPin() {
		setAsString(ForumsXPath.flag, FlagType.UNPIN.getFlagType());
	}
	/**
	 * locks a topic
	 * @method lock
	 */
	public void lock() {
		setAsString(ForumsXPath.flag, FlagType.LOCK.getFlagType());
	}
	/**
	 * unlock a topic
	 * @method unLock
	 */
	public void unLock() {
		setAsString(ForumsXPath.flag, FlagType.UNLOCK.getFlagType());
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
	 * Mark a Topic as question
	 * 
	 * @method markAsQuestion
	 */
	public void markAsQuestion() {
		setAsString(ForumsXPath.flag, FlagType.QUESTION.getFlagType());
	}
	/**
	 * Reopen a topic as question
	 * 
	 * @method ReopenQuestion
	 */
	public void ReopenQuestion() {
		setAsString(ForumsXPath.flag, FlagType.REOPENQUESTION.getFlagType());
	}
	/**
	 * Mark a Topic as Normal Topic
	 * 
	 * @method markAsNormal
	 */
	public void markAsNormal() {
		setAsString(ForumsXPath.flag, FlagType.NORMAL.getFlagType());
	}
	/**
	 * Check if a Topic is pinned
	 * 
	 * @method isPinned
	 */
	public boolean isPinned(){
		boolean pin = false;
		if(StringUtil.isNotEmpty(getAsString(ForumsXPath.flag))){
			List<String> flags =  Arrays.asList(getDataHandler().getAsString(ForumsXPath.flag).split(" "));
			for (int i = 0; i < flags.size(); i++) {
				if(StringUtil.equalsIgnoreCase(flags.get(i), FlagType.PIN.getFlagType())){
					pin = true;
				}
			}
		}
		return pin;

	}
	/**
	 * Check if a Topic is locked
	 * 
	 * @method isLocked
	 */
	public boolean isLocked(){
		boolean lock = false;
		if(StringUtil.isNotEmpty(getAsString(ForumsXPath.flag))){
			List<String> flags =  Arrays.asList(getDataHandler().getAsString(ForumsXPath.flag).split(" "));
			for (int i = 0; i < flags.size(); i++) {
				if(StringUtil.equalsIgnoreCase(flags.get(i), FlagType.LOCK.getFlagType())){
					lock = true;
				}
			}
		}
		return lock;
	}
	/**
	 * Check if a Topic is a question
	 * 
	 * @method isQuestion
	 */
	public boolean isQuestion(){
		boolean question = false;
		if(StringUtil.isNotEmpty(getAsString(ForumsXPath.flag))){
			List<String> flags =  Arrays.asList(getDataHandler().getAsString(ForumsXPath.flag).split(" "));
			for (int i = 0; i < flags.size(); i++) {
				if(StringUtil.equalsIgnoreCase(flags.get(i), FlagType.QUESTION.getFlagType())){
					question = true;
				}
			}
		}
		return question;
	}
	/**
	 * Check if a Topic is answered
	 * 
	 * @method isAnswered
	 */
	public boolean isAnswered(){
		boolean answered = false;
		if(StringUtil.isNotEmpty(getAsString(ForumsXPath.flag))){
			List<String> flags =  Arrays.asList(getDataHandler().getAsString(ForumsXPath.flag).split(" "));
			for (int i = 0; i < flags.size(); i++) {
				if(StringUtil.equalsIgnoreCase(flags.get(i), FlagType.ANSWERED.getFlagType())){
					answered = true;
				}
			}
		}
		return answered;
	}


}
