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
	
	private String forumUuid;

	public ForumTopic(BaseService svc, DataHandler<?> handler) {
		super(svc, handler);
		// TODO Auto-generated constructor stub
	}
	
	public ForumTopic(ForumService forumsService, String id) {
		super(forumsService,id);
	}
	
	/*
	 * This method returns uid of topic
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
    		forumId = forumUuid;
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
		this.forumUuid = forumUuid;
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
	
    public void pin() {
    	
		setAsString(ForumsXPath.flag, FlagType.PIN.getFlagType());
	}
    
    public void unPin() {
		setAsString(ForumsXPath.flag, FlagType.UNPIN.getFlagType());
	}
    
    public void lock() {
		setAsString(ForumsXPath.flag, FlagType.LOCK.getFlagType());
	}
    
    public void unLock() {
		setAsString(ForumsXPath.flag, FlagType.UNLOCK.getFlagType());
	}
    
    public void markAsQuestion() {
		setAsString(ForumsXPath.flag, FlagType.QUESTION.getFlagType());
	}
    
    public void markAsNormal() {
		setAsString(ForumsXPath.flag, FlagType.NORMAL.getFlagType());
	}
    
    public boolean isPinned(){
    	boolean pin = false;
	    if(StringUtil.isNotEmpty(getAsString(ForumsXPath.flag))){
	    	if(StringUtil.equalsIgnoreCase(getAsString(ForumsXPath.flag), FlagType.PIN.getFlagType())){
	        	pin = true;
	    	}
	        else{
	        	pin = false;
	        }
        }
    	return pin;
    	
    }
    
    public boolean isLocked(){
    	boolean lock = false;
	    if(StringUtil.isNotEmpty(getAsString(ForumsXPath.flag))){
	    	if(StringUtil.equalsIgnoreCase(getAsString(ForumsXPath.flag), FlagType.LOCK.getFlagType())){
	    		lock = true;
	    	}
	        else{
	        	lock = false;
	        }
        }
    	return lock;
    }
    
    public boolean isQuestion(){
    	boolean question = false;
	    if(StringUtil.isNotEmpty(getAsString(ForumsXPath.flag))){
	    	if(StringUtil.equalsIgnoreCase(getAsString(ForumsXPath.flag), FlagType.QUESTION.getFlagType())){
	    		question = true;
	    	}
	        else{
	        	question = false;
	        }
        }
    	return question;
    }
    
}
