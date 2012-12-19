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
import com.ibm.sbt.util.DataNavigator;

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

	private List<ActivityStreamEntry>	replies;

	public List<ActivityStreamEntry> getReplies() {
		return replies;
	}

	public void setReplies(List<ActivityStreamEntry> replies) {
		this.replies = replies;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public String getPlainTitle() {
		return plainTitle;
	}

	public void setPlainTitle(String plainTitle) {
		this.plainTitle = plainTitle;
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	public Boolean isContainAttachment() {
		return containAttachment;
	}

	public void setContainAttachment(Boolean containAttachment) {
		if (null == containAttachment) {
			return;
		}
		this.containAttachment = containAttachment;
	}

	public Boolean isBroadcast() {
		return broadcast;
	}

	public void setBroadcast(Boolean broadcast) {
		if (null == broadcast) {
			return;
		}
		this.broadcast = broadcast;
	}

	public Boolean isPublic() {
		return isPublic;
	}

	public void setPublic(Boolean isPublic) {
		if (null == isPublic) {
			return;
		}
		this.isPublic = isPublic;
	}

	public Boolean isSaved() {
		return Saved;
	}

	public void setSaved(Boolean saved) {
		if (null == saved) {
			return;
		}
		Saved = saved;
	}

	public String getAtomUrl() {
		return atomUrl;
	}

	public void setAtomUrl(String atomUrl) {
		this.atomUrl = atomUrl;
	}

	public String getConnectionsContentUrl() {
		return connectionsContentUrl;
	}

	public void setConnectionsContentUrl(String connectionsContentUrl) {
		this.connectionsContentUrl = connectionsContentUrl;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

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

	public int getNumComments() {
		return numComments;
	}

	public void setNumComments(int numComments) {
		this.numComments = numComments;
	}

	public String getRollUpUrl() {
		return rollUpUrl;
	}

	public void setRollUpUrl(String rollUpUrl) {
		this.rollUpUrl = rollUpUrl;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getRollUpId() {
		return rollUpId;
	}

	public void setRollUpId(String rollUpId) {
		this.rollUpId = rollUpId;
	}

	public String getContainerId() {
		return containerId;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	public Boolean isFollowedResource() {
		return followedResource;
	}

	public void setFollowedResource(Boolean followedResource) {
		if (null == followedResource) {
			return;
		}
		this.followedResource = followedResource;
	}

	public String getVerb() {
		return verb;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}

	private String	summary;

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getContextId() {
		return contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getItemUrl() {
		return itemUrl;
	}

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

		// Parent ( Root )
		entryObj.setPublished(parent.stringValue("published"));
		entryObj.setUrl(parent.stringValue("url"));
		entryObj.setTitle(parent.stringValue("title"));
		entryObj.setVerb(parent.stringValue("verb"));
		entryObj.setUpdated(parent.stringValue("updated"));
		entryObj.setId(parent.stringValue("id"));
		try {
			DataNavigator target = parent.get("target");
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
		entryObj.setAttachment(attachment);
		if (entryObj.getNumComments() > 0) { // fetch comments
			entryObj.setReplies(fetchComments(object, entryObj.getNumComments()));
		}
		return entryObj;

	}

	public static Attachment fetchAttachment(DataNavigator object) {
		Attachment attachment = new Attachment();
		DataNavigator SUAttachments = object.get("attachments");
		DataNavigator SUAttachment = SUAttachments.get(0);
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

}
