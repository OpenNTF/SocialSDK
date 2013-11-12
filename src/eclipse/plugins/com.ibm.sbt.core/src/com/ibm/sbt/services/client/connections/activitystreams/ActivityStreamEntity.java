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


package com.ibm.sbt.services.client.connections.activitystreams;


/**
 * Model class : Creates Business objects from Activity Stream Service
 * @author Manish Kataria
 */

import java.util.List;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.base.datahandlers.JsonDataHandler;
import com.ibm.sbt.services.client.connections.activitystreams.model.ASJsonPath;
import com.ibm.sbt.services.client.connections.activitystreams.model.Actor;
import com.ibm.sbt.services.client.connections.activitystreams.model.Attachment;
import com.ibm.sbt.services.client.connections.activitystreams.model.Community;
import com.ibm.sbt.services.client.connections.activitystreams.model.Reply;

public class ActivityStreamEntity extends BaseEntity{
	// ToDo add target ( right now being used with community )
	
	private Community					community;
	private Attachment					attachment;
	private Actor						actor;
	private List<Reply>	replies;
	

	public ActivityStreamEntity(BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
	}
	
	@Override
	public JsonDataHandler getDataHandler(){
		return (JsonDataHandler)dataHandler;
	}
	
	public String getAsString(ASJsonPath fieldType){
		return dataHandler.getAsString(fieldType);
	}
	
	public String getTitle() {
		return getAsString(ASJsonPath.Title);
	}

	public String getVerb() {
		return getAsString(ASJsonPath.Verb);
	}


	public String getShortTitle() {
		return getAsString(ASJsonPath.ShortTitle);
	}

	public Actor getActor() {
		if (actor == null) {
			actor = new Actor(dataHandler);
		}
		return actor;
	}

	/*
	public String getObjectType() {
		return getAsString(ASJsonHelper.ObjectType);
	}
	*/

	public String getPublished() {
		return getAsString(ASJsonPath.Published);
	}

	public String getUrl() {
		return getAsString(ASJsonPath.Url);
	}

	public String getUpdated() {
		return getAsString(ASJsonPath.Updated);
	}

	public String getId() {
		return getAsString(ASJsonPath.Id);
	}

	public Community getCommunity() {
		if(community==null){
			if(StringUtil.equalsIgnoreCase(getAsString(ASJsonPath.TargetObjectType),"community")){
				community = new Community(dataHandler);
			}
		}
		return community;
	}

	public Attachment getAttachment() {
		return attachment==null?new Attachment(dataHandler):attachment;
	}

	public boolean isContainAttachment() {
		boolean containAttachment;
		List attachmentList = dataHandler.getEntries(ASJsonPath.Attachments.getPath());
		if(attachmentList == null){
			return false;
		}
		JsonJavaObject attachmentObject = (JsonJavaObject) attachmentList.get(0);
		if(attachmentObject == null){
			return false;
		}
		
		String attachmentId = attachmentObject.getString(ASJsonPath.AttachmentId.getPath());
		if(StringUtil.isEmpty(attachmentId)){
			containAttachment = false;
		}else{
			containAttachment = true;
		}
		return containAttachment;
	}
	
	public boolean isBroadcast() {
		return getAsBoolean(ASJsonPath.Broadcast);
	}

	public boolean isPublic() {
		return getAsBoolean(ASJsonPath.IsPublic);
	}

	public boolean isSaved() {
		return getAsBoolean(ASJsonPath.Saved);
	}

	public String getAtomUrl() {
		return getAsString(ASJsonPath.AtomUrl);
	}

	public String getContainerId() {
		return getAsString(ASJsonPath.ContainerId);
	}

	public String getContainerName() {
		return getAsString(ASJsonPath.ContainerName);
	}

	public String getPlainTitle() {
		return getAsString(ASJsonPath.PlainTitle);
	}

	public boolean isFollowedResource() {
		return getAsBoolean(ASJsonPath.FollowedResource);
	}

	public String getRollUpId() {
		return getAsString(ASJsonPath.RollUpId);
	}

	public String getRollUpUrl() {
		return getAsString(ASJsonPath.RollUpUrl);
	}

	public String getConnectionsContentUrl() {
		return getAsString(ASJsonPath.ConnectionsContentUrl);
	}

	public String getEventType() {
		return getAsString(ASJsonPath.EventType);
	}

	public String getEventId() {
		return getAsString(ASJsonPath.EventId);
	}

	public String getIconUrl() {
		return getAsString(ASJsonPath.IconUrl);
	}

	public int getNumLikes() {
		return dataHandler.getAsInt(ASJsonPath.NumLikes);
	}

	public int getNumComments() {
		try {
			return getAsInt(ASJsonPath.NumComments);
		} catch (Exception e) {
			return 0;
		}
		
	}

	public String getContextId() {
		return getAsString(ASJsonPath.ContextId);
	}

	public String getEventTitle() {
		return getAsString(ASJsonPath.EventTitle);
	}

	public String getTags() {
		return getAsString(ASJsonPath.Tags);
	}

	public String getItemUrl() {
		return getAsString(ASJsonPath.ItemUrl);
	}


	public String getSummary() {
		return getAsString(ASJsonPath.Summary);
	}


	public List<Reply> getReplies() {
		if(getNumComments()>0){
			if(replies == null){
				replies = Reply.getReplies(getDataHandler());
			}
			return replies;
		}
		return null;				
	}
}
