/*
 * © Copyright IBM Corp. 2014
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
package com.ibm.sbt.services.client.connections.activities;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.URLBuilder;
import com.ibm.sbt.services.client.base.URLContainer;
import com.ibm.sbt.services.client.base.Version;
import com.ibm.sbt.services.client.base.VersionedUrl;

/**
 * @author mwallace
 *
 */
public enum ActivityUrls implements URLContainer {

	ACTIVITIES_SERVICE(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/service")), // Retrieving the Activities service document
	MY_ACTIVITIES(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/activities")), //	Getting the My Activities feed and creating activities
	COMPLETED_ACTIVITIES(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/completed")), // Getting a feed of completed activities
	ALL_ACTIVITIES(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/everything")), //	Getting a feed of all activities
	TODO_ENTRIES(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/todos")), //	Getting a feed of entries in the to-do list
	ACTIVITY_TAGS(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/tags")), //	Getting a list of the tags assigned to all activities
	ACTIVITY_CATEGORIES(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/categories")), //	Getting a list of the categories assigned to this activity
	TUNED_OUT_ACTIVITIES(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/tunedout")), // Getting a feed of the tuned out activities
	ACTIVITY_TEMPLATES(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/entrytemplates?activityUuid={activityUuid}")), //	Retrieves a feed of entry templates
	TRASHED_ACTIVITIES(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/trash")), // Retrieves a feed of the activities and entries in the trash
	TRASHED_ACTIVITY_NODES(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/trash?activityUuid={activityUuid}")), // Retrieves a feed of the activities and entries in the trash
	ACTIVITY(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/activity?activityUuid={activityUuid}")), // Retrieving an Activity entry and creating an activity node
	CHANGE_PRIORITY(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/activitynode?activityNodeUuid={activityNodeUuid}&priority={priority}")), // Change the priority of the specified activity
	ACTIVITY_NODE(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/activitynode?activityNodeUuid={activityNodeUuid}")), // Retrieve, update, deleting activity nodes programmatically
	TRASHED_ACTIVITY_NODE(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/trashednode?activityNodeUuid={activityNodeUuid}")), // Restoring activity nodes programmatically
	ACTIVITY_ACL(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/acl?activityUuid={activityUuid}")), // Adding an activity member programmatically
	ACTIVITY_MEMBER(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/acl?activityUuid={activityUuid}&memberid={memberId}")), // Retrieve, update and delete an activity member programmatically
	DESCENDANTS(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/descendants?activityNodeUuid={activityNodeUuid}")), // Getting a feed of the Activity descendants
	ACTIVITY_DESCENDANTS(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/activitydescendants?nodeUuid={activityNodeUuid}")), // Getting a feed of the Activity descendants
	ACTIVITY_NODECHILDREN(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/nodechildren?activityNodeUuid={activityNodeUuid}")), // Getting a feed of the Activity descendants
    MOVE_FIELD(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/moveField?destNodeUuid={destinationUuid}&fieldUuid={fieldUuid}")), // Move a field from an ActivityNode to another
    MOVE_NODE(new VersionedUrl(ConnectionsConstants.v4_0, "{activities}/service/atom2/moveEntry?activityNodeUuid={activityNodeUuid}&destNodeUuid={destinationUuid}")), // Move a field from an ActivityNode to another
	;
	
	private URLBuilder builder;
	
	static final public NamedUrlPart activityNodePart(String activityNodeUuid) {
		return new NamedUrlPart("activityNodeUuid", activityNodeUuid);
	}
	
	static final public NamedUrlPart priorityPart(int priority) {
		return new NamedUrlPart("priority", "" + priority);
	}
	
	static final public NamedUrlPart activityPart(String activityUuid) {
		return new NamedUrlPart("activityUuid", activityUuid);
	}
	
	static final public NamedUrlPart memberPart(String memberId) {
		return new NamedUrlPart("memberId", memberId);
	}
	
	static final public NamedUrlPart destinationPart(String destinationUuid) {
		return new NamedUrlPart("destinationUuid", destinationUuid);
	}

	static final public NamedUrlPart fieldPart(String fieldUuid) {
		return new NamedUrlPart("fieldUuid", fieldUuid);
	}

	private ActivityUrls(VersionedUrl... urlVersions) {
		builder = new URLBuilder(urlVersions);
	}
	
	public String format(BaseService service, NamedUrlPart... args) {
		return builder.format(service, args);
	}

	public String getPattern(Version version){
		return builder.getPattern(version).getUrlPattern();
	}

}
