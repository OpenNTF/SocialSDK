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
package com.ibm.sbt.services.client.connections.communities;

import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;

/**
 * This File represents Community Forum Topic

 * @author Swati Singh
 */

public class ForumTopic extends BaseEntity{
	
	

	public ForumTopic(CommunityService communityService, String id) {
		setService(communityService);
		setAsString(ForumTopicXPath.id, id);
	}
	
	public ForumTopic(CommunityService svc, DataHandler<?> handler)
	{
		super(svc,handler);
	}
	
	/**
	 * getId
	 * 
	 * @return id
	 */	
	public String getId() {
		return getAsString(ForumTopicXPath.id);
	}
	
	/**
	 * getTitle
	 * 
	 * @return title
	 */	
	public String getTitle() {
		return getAsString(ForumTopicXPath.title);
	}

	/**
	 * getSummary
	 * 
	 * @return summary
	 */	
	public String getSummary() {
		return getAsString(ForumTopicXPath.summary);
	}

	/**
	 * Method sets the bookmark title
	 */	
	public void setTitle(String title) {
		setAsString(ForumTopicXPath.title, title);
	}


}
