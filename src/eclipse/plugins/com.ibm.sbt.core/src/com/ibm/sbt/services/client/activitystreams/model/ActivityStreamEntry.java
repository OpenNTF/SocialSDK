/*
 * © Copyright IBM Corp. 2012
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

package com.ibm.sbt.services.client.activitystreams.model;

import java.util.ArrayList;
import java.util.List;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.SBTServiceException;
import com.ibm.sbt.services.client.activitystreams.ASApplication;
import com.ibm.sbt.services.client.activitystreams.ASGroup;
import com.ibm.sbt.services.client.activitystreams.ASUser;
import com.ibm.sbt.util.DataNavigator;
import com.ibm.commons.util.StringUtil;

/**
 * ActivityStreamEntry, model class for persisting individual Entries from Activity Stream
 * 
 * @author Manish Kataria
 */

public class ActivityStreamEntry {

	private String						title;
	private String						verb;
	private String						shortTitle;
	private Actor						actor;
	private String						objectType;
	private String						published;
	private String						url;
	private String						updated;
	private String						id;
	private Community					community;
	private Attachment					attachment;
	private boolean						containAttachment;

	// Connections
	private boolean						broadcast;
	private boolean						isPublic;
	private boolean						Saved;
	private String						atomUrl;
	private String						containerId;
	private String						containerName;
	private String						plainTitle;
	private boolean						followedResource	= false;
	private String						rollUpId;
	private String						rollUpUrl;

	// OpenSocial/Context/embed
	private String						connectionsContentUrl;
	private String						eventType;
	private String						eventId;
	private String						iconUrl;
	private int							numLikes;
	private int							numComments;
	private String						contextId;
	private String						eventTitle;
	private String						tags;
	private String						itemUrl;
	
	// Microblogs
	private String content;
	private String repliesUrl;
	private String likesUrl;
	
	/**
	 * Wrapper method to return content from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Wrapper method to set Content
	 * <p>
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * Wrapper method to return url for replies from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getRepliesUrl() {
		return repliesUrl;
	}

	/**
	 * Wrapper method to set replies Url
	 * <p>
	 * @param repliesUrl
	 */
	public void setRepliesUrl(String repliesUrl) {
		this.repliesUrl = repliesUrl;
	}

	/**
	 * Wrapper method to return url for likes from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getLikesUrl() {
		return likesUrl;
	}

	/**
	 * Wrapper method to set likes Url
	 * <p>
	 * @param likesUrl
	 */
	public void setLikesUrl(String likesUrl) {
		this.likesUrl = likesUrl;
	}

	private List<ActivityStreamEntry>	replies;

	/**
	 * Wrapper method to return Replies from ActivityStreamEntry object
	 * <p>
	 * @return List<ActivityStreamEntry>
	 */
	public List<ActivityStreamEntry> getReplies() {
		return replies;
	}

	/**
	 * Wrapper method to set replies
	 * <p>
	 * @param replies
	 * 		List of replies
	 */
	public void setReplies(List<ActivityStreamEntry> replies) {
		this.replies = replies;
	}

	/**
	 * Wrapper method to return Title from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Wrapper method to set title
	 * <p>
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Wrapper method to return Short Title from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getShortTitle() {
		return shortTitle;
	}

	/**
	 * Wrapper method to set short title
	 * <p>
	 * @param short title
	 */
	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	/**
	 * Wrapper method to return Plain Title from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getPlainTitle() {
		return plainTitle;
	}

	/**
	 * Wrapper method to set plain title
	 * <p>
	 * @param plain title
	 */
	public void setPlainTitle(String plainTitle) {
		this.plainTitle = plainTitle;
	}

	/**
	 * Wrapper method to return Actor object from ActivityStreamEntry object
	 * <p>
	 * @return Actor
	 */
	public Actor getActor() {
		return actor;
	}

	/**
	 * Wrapper method to set actor
	 * <p>
	 * @param Actor
	 */
	public void setActor(Actor actor) {
		this.actor = actor;
	}

	/**
	 * Wrapper method to return Object Type from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getObjectType() {
		return objectType;
	}

	/**
	 * Wrapper method to set object type
	 * <p>
	 * @param objectType
	 */
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	/**
	 * Wrapper method to return Published from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getPublished() {
		return published;
	}

	/**
	 * Wrapper method to set published
	 * <p>
	 * @param published
	 * 		published date in String format
	 */
	public void setPublished(String published) {
		this.published = published;
	}

	/**
	 * Wrapper method to return URL from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Wrapper method to set url
	 * <p>
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Wrapper method to return Updated from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getUpdated() {
		return updated;
	}

	/**
	 * Wrapper method to set updated
	 * <p>
	 * @param updated
	 * 		updated date in String format
	 */
	public void setUpdated(String updated) {
		this.updated = updated;
	}

	/**
	 * Wrapper method to return ID from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getId() {
		return id;
	}

	/**
	 * Wrapper method to set id
	 * <p>
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Wrapper method to return Community from ActivityStreamEntry object
	 * <p>
	 * @return Community
	 */
	public Community getCommunity() {
		return community;
	}

	/**
	 * Wrapper method to set community
	 * <p>
	 * @param Community
	 */
	public void setCommunity(Community community) {
		this.community = community;
	}

	/**
	 * Wrapper method to return Attachment from ActivityStreamEntry object
	 * <p>
	 * @return Attachment
	 */
	public Attachment getAttachment() {
		return attachment;
	}

	/**
	 * Wrapper method to set Attachment
	 * <p>
	 * @param Attachment
	 */
	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	/**
	 * Wrapper method to returns true/false if update contains attachment from ActivityStreamEntry object
	 * <p>
	 * @return Boolean
	 */
	public Boolean isContainAttachment() {
		return containAttachment;
	}

	/**
	 * Wrapper method to set contain Attachment
	 * <p>
	 * @param containAttachment
	 * 		true if update contains an attachment
	 * 		false if update does not contain an attachment
	 */
	public void setContainAttachment(Boolean containAttachment) {
		if (null == containAttachment) {
			return;
		}
		this.containAttachment = containAttachment;
	}

	/**
	 * Wrapper method to return isBroadcast from ActivityStreamEntry object
	 * <p>
	 * @return Boolean
	 */
	public Boolean isBroadcast() {
		return broadcast;
	}

	/**
	 * Wrapper method to set Broadcast
	 * <p>
	 * @param setBroadcast
	 */
	public void setBroadcast(Boolean broadcast) {
		if (null == broadcast) {
			return;
		}
		this.broadcast = broadcast;
	}

	/**
	 * Wrapper method to return isPublic from ActivityStreamEntry object
	 * <p>
	 * @return Boolean
	 */
	public Boolean isPublic() {
		return isPublic;
	}

	/**
	 * Wrapper method to set visibility of update
	 * <p>
	 * @param isPublic
	 */
	public void setPublic(Boolean isPublic) {
		if (null == isPublic) {
			return;
		}
		this.isPublic = isPublic;
	}

	/**
	 * Wrapper method to return isSaved from ActivityStreamEntry object
	 * <p>
	 * @return Boolean
	 */
	public Boolean isSaved() {
		return Saved;
	}

	/**
	 * Wrapper method to set saved
	 * <p>
	 * @param Saved
	 */
	public void setSaved(Boolean saved) {
		if (null == saved) {
			return;
		}
		Saved = saved;
	}

	/**
	 * Wrapper method to return Atom URL from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getAtomUrl() {
		return atomUrl;
	}

	/**
	 * Wrapper method to set atom url
	 * <p>
	 * @param atomUrl
	 */
	public void setAtomUrl(String atomUrl) {
		this.atomUrl = atomUrl;
	}

	/**
	 * Wrapper method to return connectionsContentUrl from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getConnectionsContentUrl() {
		return connectionsContentUrl;
	}

	/**
	 * Wrapper method to set connectionsContentUrl
	 * <p>
	 * @param connectionsContentUrl
	 */
	public void setConnectionsContentUrl(String connectionsContentUrl) {
		this.connectionsContentUrl = connectionsContentUrl;
	}

	
	/**
	 * Wrapper method to return eventType from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getEventType() {
		return eventType;
	}

	/**
	 * Wrapper method to set eventType
	 * <p>
	 * @param eventType
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	/**
	 * Wrapper method to return eventId from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getEventId() {
		return eventId;
	}

	/**
	 * Wrapper method to set eventId
	 * <p>
	 * @param eventId
	 */
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	/**
	 * Wrapper method to return number of Likes from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public int getNumLikes() {
		return numLikes;
	}

	public void setNumLikes(int numLikes) {
		try {
			this.numLikes = numLikes;
		} catch (Exception e) {
			this.numLikes = 0;
		}

	}

	/**
	 * Wrapper method to return number of Comments from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public int getNumComments() {
		return numComments;
	}

	
	/**
	 * Wrapper method to set numOfComments
	 * <p>
	 * @param numOfComments
	 */
	public void setNumComments(int numComments) {
		this.numComments = numComments;
	}

	/**
	 * Wrapper method to return RollUpUrl from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getRollUpUrl() {
		return rollUpUrl;
	}

	/**
	 * Wrapper method to set RollUpUrl
	 * <p>
	 * @param RollUpUrl
	 */
	public void setRollUpUrl(String rollUpUrl) {
		this.rollUpUrl = rollUpUrl;
	}

	/**
	 * Wrapper method to return Event Title from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getEventTitle() {
		return eventTitle;
	}

	/**
	 * Wrapper method to set  Event Title 
	 * <p>
	 * @param  EventTitle 
	 */
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	/**
	 * Wrapper method to return RollUpId from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getRollUpId() {
		return rollUpId;
	}

	/**
	 * Wrapper method to set  RollUpId
	 * <p>
	 * @param  RollUpId 
	 */
	public void setRollUpId(String rollUpId) {
		this.rollUpId = rollUpId;
	}

	/**
	 * Wrapper method to return ContainerId from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getContainerId() {
		return containerId;
	}

	/**
	 * Wrapper method to set  ContainerId
	 * <p>
	 * @param  ContainerId 
	 */
	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	/**
	 * Wrapper method to return containerName from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getContainerName() {
		return containerName;
	}

	/**
	 * Wrapper method to set  ContainerName
	 * <p>
	 * @param  ContainerName 
	 */
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	/**
	 * Wrapper method to return isFollowedResource from ActivityStreamEntry object
	 * <p>
	 * @return Boolean
	 */
	public Boolean isFollowedResource() {
		return followedResource;
	}

	/**
	 * Wrapper method to set  followedResource
	 * <p>
	 * @param  followedResource 
	 */
	public void setFollowedResource(Boolean followedResource) {
		if (null == followedResource) {
			return;
		}
		this.followedResource = followedResource;
	}

	
	/**
	 * Wrapper method to return getVerb from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getVerb() {
		return verb;
	}

	/**
	 * Wrapper method to set  verb
	 * <p>
	 * @param  verb 
	 */
	public void setVerb(String verb) {
		this.verb = verb;
	}

	private String	summary;

	/**
	 * Wrapper method to return summary from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * Wrapper method to set  summary
	 * <p>
	 * @param  summary 
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * Wrapper method to return iconUrl from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getIconUrl() {
		return iconUrl;
	}

	/**
	 * Wrapper method to set iconurl
	 * <p>
	 * @param  iconurl 
	 */
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	/**
	 * Wrapper method to return contextId from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getContextId() {
		return contextId;
	}
	/**
	 * Wrapper method to set contextId
	 * <p>
	 * @param  contextId 
	 */
	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	/**
	 * Wrapper method to return tags from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getTags() {
		return tags;
	}

	/**
	 * Wrapper method to set tags
	 * <p>
	 * @param  tags 
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}

	/**
	 * Wrapper method to return itemUrl from ActivityStreamEntry object
	 * <p>
	 * @return String
	 */
	public String getItemUrl() {
		return itemUrl;
	}

	/**
	 * Wrapper method to set item url
	 * <p>
	 * @param  itemUrl 
	 */
	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}

	/**
	 * Converts a json entry to ActivityStreamEntry Java object
	 * 
	 * @param parent
	 * @return ActivityStreamEntry
	 */
	public static ActivityStreamEntry createActivityStreamEntryObject(DataNavigator parent) {

		ActivityStreamEntry entryObj = new ActivityStreamEntry();

		Actor actor = new Actor();

		DataNavigator actornav = parent.get("actor");
		DataNavigator connections = parent.get("connections");
		DataNavigator opensocial = parent.get("openSocial");
		DataNavigator embed = opensocial.get("embed");
		DataNavigator context = embed.get("context");
		DataNavigator object = parent.get("object");
		DataNavigator target = parent.get("target");
		// Parent ( Root )
		entryObj.setPublished(parent.stringValue("published"));
		entryObj.setUrl(parent.stringValue("url"));
		entryObj.setTitle(parent.stringValue("title"));
		entryObj.setVerb(parent.stringValue("verb"));
		entryObj.setUpdated(parent.stringValue("updated"));
		entryObj.setId(parent.stringValue("id"));
		try {
			if (null != target) {
				String objecttype = target.stringValue("objectType");
				if (null != objecttype && objecttype.equalsIgnoreCase("community")) {
					Community comm = fetchCommunity(target);
					entryObj.setCommunity(comm);
				}
			}
		} catch (Exception e) {}

		// actor
		actor.setName(actornav.stringValue("displayName"));
		actor.setUid(actornav.stringValue("id"));
		actor.setType(actornav.stringValue("objectType"));
		entryObj.setActor(actor);

		// connections
		entryObj.setBroadcast(connections.booleanValue("broadcast"));
		entryObj.setRollUpId(connections.stringValue("rollupid"));
		entryObj.setPublic(connections.booleanValue("isPublic"));
		entryObj.setSaved(connections.booleanValue("saved"));
		entryObj.setRollUpUrl(connections.stringValue("rollupUrl"));
		entryObj.setShortTitle(connections.stringValue("shortTitle"));
		entryObj.setContainerId(connections.stringValue("containerId"));
		entryObj.setContainerName(connections.stringValue("containerName"));
		entryObj.setPlainTitle(connections.stringValue("plainTitle"));
		entryObj.setAtomUrl(connections.stringValue("atomUrl"));
		entryObj.setFollowedResource(connections.booleanValue("followedResource"));

		// opensocial
		entryObj.setSummary(context.stringValue("summary"));
		entryObj.setConnectionsContentUrl(context.stringValue("connectionsContentUrl"));
		entryObj.setEventType(context.stringValue("eventType"));
		entryObj.setEventId(context.stringValue("eventId"));
		entryObj.setIconUrl(context.stringValue("iconUrl"));
		try {
			entryObj.setNumLikes(context.intValue("numLikes"));
		} catch (Exception e) {
			entryObj.setNumLikes(0);
		}

		try {
			entryObj.setNumComments(context.intValue("numComments"));
		} catch (Exception e) {
			entryObj.setNumComments(0);
		}

		entryObj.setContextId(context.stringValue("id"));
		entryObj.setEventTitle(context.stringValue("eventTitle"));
		entryObj.setTags(context.stringValue("tags"));
		entryObj.setItemUrl(context.stringValue("itemUrl"));
		Attachment attachment = fetchAttachment(object); // fetch attachments
		
		if(attachment!=null){
			entryObj.setContainAttachment(true);
			entryObj.setAttachment(attachment); 
		}
		
		
		if (entryObj.getNumComments() > 0) { // fetch comments
			entryObj.setReplies(fetchComments(target, entryObj.getNumComments()));
		}
		return entryObj;

	}

	public static Attachment fetchAttachment(DataNavigator object) {
		Attachment attachment = new Attachment();
		DataNavigator SUAttachments = object.get("attachments");
		DataNavigator SUAttachment = SUAttachments.get(0); 
		// Fix for defect parsing attachments
		if(StringUtil.isEmpty(SUAttachment.stringValue("id"))){
			return null;
		}
		attachment.setSummary(SUAttachment.stringValue("summary"));
		attachment.setId(SUAttachment.stringValue("id"));
		attachment.setDisplayName(SUAttachment.stringValue("displayName"));
		attachment.setPublished(SUAttachment.stringValue("published"));
		attachment.setUrl(SUAttachment.stringValue("url"));

		if (SUAttachment.get("image") != null) {
			Image img = fetchImage(SUAttachment.get("image"));
			if (img != null) {
				attachment.setImage(img);
				attachment.setIsImage(true);
			} else {
				attachment.setIsImage(false);
			}
		} else {
			attachment.setIsImage(false);
		}
		return attachment;

	}

	public static Image fetchImage(DataNavigator image) {
		Image attachmentimage = new Image();
		if (null != image.stringValue("url")) {
			attachmentimage.setUrl(image.stringValue("url"));
		} else {
			attachmentimage = null;
		}
		return attachmentimage;

	}

	public static List<ActivityStreamEntry> fetchComments(DataNavigator object, int numberOfComments) {
		List<ActivityStreamEntry> comments = new ArrayList<ActivityStreamEntry>();
		DataNavigator replies = object.get("replies").get("items");
		for (int i = 0; i < numberOfComments; i++) {
			ActivityStreamEntry commentEntry = new ActivityStreamEntry();
			Actor actor = new Actor();
			DataNavigator reply = replies.get(i);
			if(StringUtil.isEmpty(reply.stringValue("id"))){
				break;
			}
			String comment = reply.stringValue("content");
			String updated = reply.stringValue("updated");
			String id = reply.stringValue("id");

			DataNavigator actorav = replies.get(i).get("author");
			String type = actorav.stringValue("objectType");
			String actorid = actorav.stringValue("id");
			String displayName = actorav.stringValue("displayName");

			actor.setName(displayName);
			actor.setType(type);
			actor.setUid(actorid);

			commentEntry.setTitle(comment);
			commentEntry.setUpdated(updated);
			commentEntry.setId(id);
			commentEntry.setActor(actor);
			comments.add(commentEntry);
		}
		return comments;
	}

	public static Community fetchCommunity(DataNavigator target) {
		String communitypattern = "urn:lsid:lconn.ibm.com:communities.community:";
		Community community = new Community();
		String communityId = target.stringValue("id");
		if (communityId.startsWith(communitypattern)) {
			communityId = communityId.substring(communitypattern.length(), communityId.length());
		}
		String communityName = target.stringValue("displayName");
		community.setCommunityId(communityId);
		community.setCommunityName(communityName);
		return community;
	}
	
	
	
	/*
	 * Parsing for Microblogs 
	 */
	
	/**
	 * Converts a json entry to ActivityStreamEntry Java object
	 * 
	 * @param parent
	 * @return ActivityStreamEntry
	 */
	public static ActivityStreamEntry createMicroBlogEntryObject(DataNavigator parent) {

		ActivityStreamEntry entryObj = new ActivityStreamEntry();

		Actor actor = new Actor();

		DataNavigator actornav = parent.get("author");

		// Parent ( Root )
		entryObj.setPublished(parent.stringValue("published"));
		entryObj.setUrl(parent.stringValue("url"));
		entryObj.setSummary(parent.stringValue("summary"));
		entryObj.setContent(parent.stringValue("content"));
		entryObj.setObjectType(parent.stringValue("objectType"));
		entryObj.setId(parent.stringValue("id"));
		

		// actor
		actor.setName(actornav.stringValue("displayName"));
		actor.setUid(actornav.stringValue("id"));
		actor.setType(actornav.stringValue("objectType"));
		entryObj.setActor(actor);
		
		
		DataNavigator replies = parent.get("replies");
		entryObj.setRepliesUrl(replies.stringValue("replies"));
		entryObj.setNumComments(replies.intValue("totalItems"));
		
		
		DataNavigator likes = parent.get("likes");
		entryObj.setLikesUrl(likes.stringValue("url"));
		entryObj.setNumLikes(likes.intValue("totalItems"));
		
		Attachment attachment = fetchAttachment(parent); // fetch attachments
		entryObj.setAttachment(attachment);

		return entryObj;
	}

}
