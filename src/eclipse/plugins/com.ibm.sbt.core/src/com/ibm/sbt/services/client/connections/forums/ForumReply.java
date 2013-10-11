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
 * Reply model object
 *
 * @author Manish Kataria
 * @author Swati Singh
 */

public class ForumReply extends BaseForumEntity{

	/**
	 * To get Uuid of Topic to which the Reply is posted
	 *
	 * @method getTopicUuid
	 * @return String
	 */
	public String getTopicUuid(){
		String topicId = "";
		try {
			topicId = getAsString(ForumsXPath.inReplyTo);
		} catch (Exception e) {}
		if(StringUtil.isEmpty(topicId)){
			topicId = (String) fields.get("topicUuid");
		}
		return extractForumUuid(topicId);
	}

	/**
	 * Sets TopicUuid of the Reply
	 *
	 * @method getTopicUuid
	 * @return String
	 */
	public void setTopicUuid(String topicId) {
		fields.put("topicUuid", topicId);
	}
	
	/**
	 * Constructor
	 *  
	 * @param BaseService
	 * @param DataHandler
	 */
	public ForumReply(BaseService svc, DataHandler<?> handler) {
		super(svc, handler);
	}
	
	/**
	 * Constructor
	 *  
	 * @param ForumService
	 * @param id
	 */
	public ForumReply(ForumService forumsService, String id) {
		super(forumsService,id);
	}

	/**
	 * To get Uuid of Forum Reply
	 *
	 * @method getReplyUuid
	 * @return String
	 */
	public String getReplyUuid() throws ForumServiceException{
		return super.getUid();
	}

	/**
	 * Sets the Uuid of Forum Reply
	 *
	 * @method setReplyUuid
	 * @param {String} Uuid of forum
	 */
	public void setReplyUuid(String forumUuid) {
		setAsString(ForumsXPath.uid, forumUuid);
	}
	/**
	 * This method returns Recommendations for the IBM Connections forum Reply
	 *
	 * @return RecommendationList
	 * @throws ForumServiceException
	 */
	public RecommendationList getRecommendations() throws ForumServiceException
	{
		return getService().getRecommendations(getUid());
	}
	/**
	 * To get Url of Reply
	 *
	 * @method getReplyUrl
	 * @return String
	 */
	public String getReplyUrl() throws ForumServiceException{
		return getAsString(ForumsXPath.selfUrl);
	}
	/**
	 * to Like/Recommend a Reply, supported on Connections 4.5 or above
	 *
	 * @method like
	 * @return Recommendation
	 */
	public Recommendation like() throws ForumServiceException {
		return getService().createRecommendation(getReplyUuid());
	}
	/**
	 * to unLike/unRecommend a Reply, supported on Connections 4.5 or above
	 *
	 * @method unLike
	 * @return boolean
	 */
	public boolean unLike() throws ForumServiceException {
		return getService().deleteRecommendation(getReplyUuid());
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
	 * Return the permissions of the IBM Connections Forum Reply from
	 * forum ATOM entry document.
	 *
	 * @method getPermisisons
	 * @return {String} Permissions of the Forum Reply
	 */
	public String getPermisisons()throws ForumServiceException {
		return getAsString(ForumsXPath.permissions);
	}
	/**
	 * Set to true, indicates that the reply is an accepted answer.
	 *
	 * @method setAnswer
	 * @param answer
	 */
	public void setAnswer(boolean answer) {
		setAsBoolean(FlagType.ANSWER.getFlagType(), answer);
	}
	
	/**
	 * Check if Reply is accepted as answer
	 *
	 * @method isAnswer
	 * @return boolean
	 */
	public boolean isAnswer(){
		boolean answer = false;
		if(StringUtil.isNotEmpty(getAsString(ForumsXPath.flag))){
			List<String> flags = Arrays.asList(getDataHandler().getAsString(ForumsXPath.flag).split(" "));
			for (int i = 0; i < flags.size(); i++) {
				if(StringUtil.equalsIgnoreCase(flags.get(i), FlagType.ANSWER.getFlagType())){
					answer = true;
					break;
				}
			}
		}
		return answer;
	}

	/**
	 * This method updates the IBM Connections Forum Reply on the server
	 *
	 * @return ForumReply
	 * @throws ForumServiceException
	 */
	public ForumReply save() throws ForumServiceException{
		return save("");
	}
	/**
	 * This method creates/updates the IBM Connections Forum Topic on the server
	 *
	 * @return ForumReply
	 * @throws ForumServiceException
	 */
	public ForumReply save(String topicId) throws ForumServiceException{
		String topicUuid = (String) fields.get("topicUuid");
		if(StringUtil.isEmpty(getUid())){
			if(StringUtil.isEmpty(topicId) && StringUtil.isNotEmpty(topicUuid)){ // if a topicId was not provided but was set using setTopicUuid method
				topicId = topicUuid;
			}else if(StringUtil.isEmpty(topicId) && StringUtil.isEmpty(topicUuid)){ // Can not create reply without topicUuid
				throw new ForumServiceException(new Exception("No Parent Topic ID mentioned while creating Forum Reply"));
			}
			return getService().createForumReply(this,topicId);
		}else{
			//update scenario
			getService().updateForumReply(this);
			return getService().getForumReply(getUid());
		}
	}
	/**
	 * This method removes the forum reply
	 *
	 * @throws ForumServiceException
	 */
	public void remove() throws ForumServiceException {
		getService().removeForumReply(getUid());
	}

	/**
	 * This method loads the forum reply
	 *
	 * @return ForumReply
	 * @throws ForumServiceException
	 */
	public ForumReply load() throws ForumServiceException
	{
		return getService().getForumReply(getUid());
	}



}