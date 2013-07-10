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

package com.ibm.sbt.services.client.connections.activitystreams.model;

import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;


public enum ASJsonPath implements FieldEntry {
		Entry("list"),
		Title("title"),
		Verb("verb"),
		TargetObjectType("target/objectType"),
		TargetId("target/id"),
		TargetDisplayName("target/displayName"),
		TargetUrl("target/url"),
		Published("published"),
		Url("url"),
		Updated("updated"),
		Id("id"),
		Broadcast("connections/broadcast"),
		IsPublic("connections/isPublic"),
		Saved("connections/saved"),
		AtomUrl("connections/atomUrl"),
		ContainerId("connections/containerId"),
		ContainerName("connections/containerName"),
		PlainTitle("connections/plainTitle"),
		FollowedResource("connections/followedResource"),
		RollUpId("connections/rollupid"),
		RollUpUrl("connections/rollupUrl"),
		ShortTitle("connections/shortTitle"),
		Summary("openSocial/embed/context/summary"),
		ConnectionsContentUrl("openSocial/embed/context/connectionsContentUrl"),
		EventType("openSocial/embed/context/eventType"),
		EventId("openSocial/embed/context/eventId"),
		IconUrl("openSocial/embed/context/iconUrl"),
		NumLikes("openSocial/embed/context/numLikes"),
		NumComments("openSocial/embed/context/numComments"),
		ContextId("openSocial/embed/context/id"),
		EventTitle("openSocial/embed/context/eventTitle"),
		Tags("openSocial/embed/context/tags"),
		ItemUrl("openSocial/embed/context/itemUrl"),
		ActorName("actor/displayName"),
		ActorUid("actor/id"),
		ActorType("actor/objectType"),
		Attachments("object/attachments"),
		AttachmentSummary("summary"),
		AttachmentId("id"),
		AttachmentDisplayName("displayName"),
		AttachmentPublished("published"),
		AttachmentUrl("url"),
		AttachmentImage("image"),
		AttachmentImageUrl("url"),
		AttachmentActor("author"),
		AttachmentActorObjectType("objectType"),
		AttachmentActorId("id"),
		AttachmentActorName("displayName"),
		AttachmentActorUrl("url"),
		
		//Reply objects are meant to be used in conjunction with Replies ( DataNavigator object )
		Comments("target/replies/items"),
		ObjectComments("object/replies/items"),
		ReplyContent("content"),
		ReplyId("id"),
		ReplyUpdated("updated"),
		ReplyAuthor("author"),
		ReplyAuthorName("displayName"),
		ReplyAuthorId("id"),
		ReplyAuthorObjectType("objectType");
	
	private final String path;
	private ASJsonPath(final String path) {
		this.path = path;
	}
	
	@Override
	public String getPath(){
		return path;
	}
	
	@Override
	public String getName(){
		return this.name();
	}
}
