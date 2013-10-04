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

	private String topicUuid;

	public String getTopicUuid(){
		String topicId = "";
		try {
			topicId = getAsString(ForumsXPath.inReplyTo);
		} catch (Exception e) {}
		if(StringUtil.isEmpty(topicId)){
			topicId = topicUuid;
		}
		return extractForumUuid(topicId);
	}

	public void setTopicUuid(String topicId) {
		this.topicUuid = topicId;
	}

	public ForumReply(BaseService svc, DataHandler<?> handler) {
		super(svc, handler);
		// TODO Auto-generated constructor stub
	}

	public ForumReply(ForumService forumsService, String id) {
		super(forumsService,id);
	}

	/*
	 * This method returns uid of reply
	 */
	public String getReplyUuid() throws ForumServiceException{
		return super.getUid();
	}

	public void setReplyUuid(String forumUuid) {
		setAsString(ForumsXPath.uid, forumUuid);
	}


	public String getReplyUrl() throws ForumServiceException{
		return getAsString(ForumsXPath.selfUrl);
	}
	/**
	 * This works for Connections 4.5 or above
	 */
	public Recommendation like() throws ForumServiceException {
		return getService().createRecommendation(getReplyUuid());
	}
	/**
	 * This works for Connections 4.5 or above
	 */
	public boolean unLike() throws ForumServiceException {
		return getService().deleteRecommendation(getReplyUuid());
	}

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

	private String getTopicUidFromService(){
		return topicUuid;
	}

	public void acceptAnswer() {
		setAsString(ForumsXPath.flag, FlagType.ACCEPT_ANSWER.getFlagType());
	}

	public void declineAnswer() {
		setAsString(ForumsXPath.flag, FlagType.DECLINE_ANSWER.getFlagType());
	}

	public boolean isAnswer(){
		boolean answer = false;
		if(StringUtil.isNotEmpty(getAsString(ForumsXPath.flag))){
			List<String> flags =  Arrays.asList(getDataHandler().getAsString(ForumsXPath.flag).split(" "));
			for (int i = 0; i < flags.size(); i++) {
				if(StringUtil.equalsIgnoreCase(flags.get(i), FlagType.ACCEPT_ANSWER.getFlagType())){
					answer = true;
				}
			}
		}
		return answer;
	}

	/**
	 * This method updates the IBM Connections Forum Reply on the server
	 * 
	 * @return
	 * @throws ForumServiceException
	 */
	public ForumReply save() throws ForumServiceException{
		return save("");
	}
	/**
	 * This method creates/updates the IBM Connections Forum Topic on the server
	 * 
	 * @return
	 * @throws ForumServiceException
	 */
	public ForumReply save(String topicId) throws ForumServiceException{
		if(StringUtil.isEmpty(getUid())){
			if(StringUtil.isEmpty(topicId) && StringUtil.isNotEmpty(getTopicUidFromService())){ // if a topicId was not provided but was set using setTopicUuid method
				topicId = getTopicUidFromService();
			}else if(StringUtil.isEmpty(topicId) && StringUtil.isEmpty(getTopicUidFromService())){ // Can not create reply without topicUuid
				throw new ForumServiceException(new Exception("No Parent Topic ID mentioned while creating Forum Reply")); 
			}
			return getService().createForumReply(this,topicId);
		}else{
			//update scenario
			getService().updateForumReply(this);
			return getService().getForumReply(getUid());
		}
	}

	public void remove() throws ForumServiceException {
		getService().removeForumReply(getUid());
	}

	/**
	 * This method loads the forum reply
	 * 
	 * @return
	 * @throws ForumServiceException
	 */

	public ForumReply load() throws ForumServiceException
	{
		return getService().getForumReply(getUid());
	}



}
