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

import static com.ibm.sbt.services.client.base.CommonConstants.APPLICATION_ATOM_XML;
import static com.ibm.sbt.services.client.base.CommonConstants.CONTENT_TYPE;
import static com.ibm.sbt.services.client.base.CommonConstants.SLUG;
import static com.ibm.sbt.services.client.base.CommonConstants.TITLE;
import static com.ibm.sbt.services.client.base.CommonConstants.UPLOAD_METHOD_PHASES;
import static com.ibm.sbt.services.client.base.CommonConstants.X_IBM_UPLOAD_METHOD;
import static com.ibm.sbt.services.client.base.CommonConstants.X_IBM_UPLOAD_SIZE;
import static com.ibm.sbt.services.client.base.CommonConstants.X_IBM_UPLOAD_TOKEN;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.ContentPart;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomFeedHandler;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.CategoryFeedHandler;
import com.ibm.sbt.services.client.base.CommonConstants.HTTPCode;
import com.ibm.sbt.services.client.base.ConnectionsFeedXpath;
import com.ibm.sbt.services.client.base.ConnectionsService;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.Version;
import com.ibm.sbt.services.client.base.datahandlers.AtomEntityList;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.activities.serializers.ActivityNodeSerializer;
import com.ibm.sbt.services.client.connections.activities.serializers.ActivitySerializer;
import com.ibm.sbt.services.client.connections.common.Member;
import com.ibm.sbt.services.client.connections.common.Tag;
import com.ibm.sbt.services.client.connections.common.serializers.MemberSerializer;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * The Activities application of IBM©©© Connections enables a team to collect,
 * organize, share, and reuse work related to a project goal. The Activities API
 * allows application programs to create new activities, and to read and modify
 * existing activities.
 * 
 * Use the Atom subscription API to retrieve resources from the activities
 * hosted by the Activities application. Using the Atom Publishing Protocol,
 * also known as AtomPub, you can create and update activities that you own or
 * to which you have edit access.
 * 
 * @author mwallace
 * 
 */
public class ActivityService extends ConnectionsService {

	private static final long serialVersionUID = 1L;

	private static int LARGE_FILE_THRESHOLD = 30 * 1024 * 1024;

	/**
	 * Create ActivityService instance with default endpoint.
	 */
	public ActivityService() {
		this(DEFAULT_ENDPOINT_NAME);
	}

	/**
	 * Create ActivityService instance with specified endpoint.
	 * 
	 * @param endpoint
	 */
	public ActivityService(String endpoint) {
		super(endpoint);
	}

	/**
	 * Create ActivityService instance with specified endpoint.
	 * 
	 * @param endpoint
	 */
	public ActivityService(Endpoint endpoint) {
		super(endpoint);
	}

	@Override
	protected void initServiceMappingKeys() {
		serviceMappingKeys = new String[] { "activities" };
	}

	// ------------------------------------------------------------------------------------------------------------------
	// Getting Activities feeds
	// ------------------------------------------------------------------------------------------------------------------

	/**
	 * Get a feed of all active activities that match a specific criteria.
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Activity> getMyActivities() throws ClientServicesException {
		return getMyActivities(null);
	}

	/**
	 * Get a feed of all active activities that match a specific criteria.
	 * 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Activity> getMyActivities(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.MY_ACTIVITIES.format(this);
		return getActivityEntityList(requestUrl, parameters);
	}

	/**
	 * Search for a set of completed activities that match a specific criteria.
	 * 
	 * @return
	 */
	public EntityList<Activity> getCompletedActivities() throws ClientServicesException {
		return getCompletedActivities(null);
	}

	/**
	 * Search for a set of completed activities that match a specific criteria.
	 * 
	 * @param parameters
	 * @return
	 */
	public EntityList<Activity> getCompletedActivities(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.COMPLETED_ACTIVITIES.format(this);
		return getActivityEntityList(requestUrl, parameters);
	}

	/**
	 * Search for content in all of the activities, both completed and active,
	 * that matches a specific criteria.
	 * 
	 * @return
	 */
	public EntityList<Activity> getAllActivities() throws ClientServicesException {
		return getAllActivities(null);
	}

	/**
	 * Search for content in all of the activities, both completed and active,
	 * that matches a specific criteria.
	 * 
	 * @param parameters
	 * @return
	 */
	public EntityList<Activity> getAllActivities(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.ALL_ACTIVITIES.format(this);
		return getActivityEntityList(requestUrl, parameters);
	}

	/**
	 * Search for a set of to-do items that match a specific criteria.
	 * 
	 * @return
	 */
	public EntityList<ActivityNode> getToDos() throws ClientServicesException {
		return getToDos(null);
	}

	/**
	 * Search for a set of to-do items that match a specific criteria.
	 * 
	 * @param parameters
	 * @return
	 */
	public EntityList<ActivityNode> getToDos(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.TODO_ENTRIES.format(this);
		return getActivityNodeEntityList(requestUrl, parameters);
	}

	/**
	 * Get a category document that lists the tags that have been assigned to
	 * all of the activities hosted by the Activities application.
	 * 
	 * @return
	 */
	public EntityList<Tag> getActivityTags() throws ClientServicesException {
		return getActivityTags(null);
	}

	/**
	 * Get a category document that lists the tags that have been assigned to
	 * all of the activities hosted by the Activities application.
	 * 
	 * @param parameters
	 * @return
	 */
	public EntityList<Tag> getActivityTags(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.ACTIVITY_TAGS.format(this);
		return getTagEntityList(requestUrl, parameters);
	}

	/**
	 * 
	 * @return
	 */
	public EntityList<Category> getActivityCategories() throws ClientServicesException {
		return getActivityCategories(null);
	}

	/**
	 * 
	 * @param parameters
	 * @return
	 */
	public EntityList<Category> getActivityCategories(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.ACTIVITY_CATEGORIES.format(this);
		return getCategoryEntityList(requestUrl, parameters);
	}

	/**
	 * 
	 * @return
	 */
	public EntityList<ActivityNode> getActivityDescendants(String activityUuid) throws ClientServicesException {
		return getActivityDescendants(activityUuid, null);
	}

	/**
	 * 
	 * @param parameters
	 * @return
	 */
	public EntityList<ActivityNode> getActivityDescendants(String activityUuid, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.ACTIVITY_DESCENDANTS.format(this, ActivityUrls.activityPart(activityUuid));
		return getActivityNodeEntityList(requestUrl, parameters);
	}

	/**
	 * 
	 * @return
	 */
	public EntityList<ActivityNode> getActivityNodeChildren(String activityNodeUuid) throws ClientServicesException {
		return getActivityNodeChildren(activityNodeUuid, null);
	}

	/**
	 * 
	 * @param parameters
	 * @return
	 */
	public EntityList<ActivityNode> getActivityNodeChildren(String activityNodeUuid, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.ACTIVITY_NODECHILDREN.format(this, ActivityUrls.activityNodePart(activityNodeUuid));
		return getActivityNodeEntityList(requestUrl, parameters);
	}

	/**
	 * Get a feed of all active activities that the currently authenticated user
	 * has tuned out.
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Activity> getTunedOutActivities() throws ClientServicesException {
		return getTunedOutActivities(null);
	}

	/**
	 * Get a feed of all active activities that the currently authenticated user
	 * has tuned out.
	 * 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Activity> getTunedOutActivities(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.TUNED_OUT_ACTIVITIES.format(this);
		return getActivityEntityList(requestUrl, parameters);
	}

	/**
	 * Get a feed of all activities that are in the trash.
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Activity> getThrashedActivities() throws ClientServicesException {
		return getThrashedActivities(null);
	}

	/**
	 * Get a feed of all activities that are in the trash.
	 * 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Activity> getThrashedActivities(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.TRASHED_ACTIVITIES.format(this);
		return getActivityEntityList(requestUrl, parameters);
	}

	/**
	 * Get a feed of all activity nodes that are in the trash.
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<ActivityNode> getThrashedActivityNodes(String activityUuid) throws ClientServicesException {
		return getThrashedActivityNodes(null);
	}

	/**
	 * Get a feed of all activity nodes that are in the trash.
	 * 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<ActivityNode> getTrashedActivityNodes(String activityUuid, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.TRASHED_ACTIVITY_NODES.format(this, ActivityUrls.activityPart(activityUuid));
		return getActivityNodeEntityList(requestUrl, parameters);
	}

	/**
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Member> getMembers(String activityUuid) throws ClientServicesException {
		return getMembers(activityUuid, null);
	}

	/**
	 * 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Member> getMembers(String activityUuid, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.ACTIVITY_ACL.format(this, ActivityUrls.activityPart(activityUuid));
		return getMemberEntityList(requestUrl, parameters);
	}

	/**
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<ActivityUpdate> getHistory(String activityUuid) throws ClientServicesException {
		return getHistory(activityUuid, null);
	}

	/**
	 * 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<ActivityUpdate> getHistory(String activityUuid, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.ACTIVITY_HISTORY.format(this, ActivityUrls.activityPart(activityUuid));
		return getActivityUpdateEntityList(requestUrl, parameters);
	}

	// ------------------------------------------------------------------------------------------------------------------
	// Working with activities programmatically.
	// ------------------------------------------------------------------------------------------------------------------

	/**
	 * Create an activity by sending an Atom entry document containing the new
	 * activity to the user's My Activities feed.
	 * 
	 * @param activity
	 * @return
	 * @throws ClientServicesException
	 */
	public Activity createActivity(Activity activity) throws ClientServicesException {
		return createActivity(activity, null);
	}

	/**
	 * Create an activity by sending an Atom entry document containing the new
	 * activity to the user's My Activities feed.
	 * 
	 * @param activity
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public Activity createActivity(Activity activity, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.MY_ACTIVITIES.format(this);
		return createActivityEntity(requestUrl, activity, parameters);
	}

	/**
	 * To retrieve an activity, use the edit link found in the corresponding
	 * activity entry in the user's My Activities feed. This request returns the
	 * entire activity, not just a summary of the activity. You can use this
	 * operation to obtain activity information that you want to preserve prior
	 * to performing an update.
	 * 
	 * @param activityUuid
	 * @return
	 * @throws ClientServicesException
	 */
	public Activity getActivity(String activityUuid) throws ClientServicesException {
		return getActivity(activityUuid, null);
	}

	/**
	 * To retrieve an activity, use the edit link found in the corresponding
	 * activity entry in the user's My Activities feed. This request returns the
	 * entire activity, not just a summary of the activity. You can use this
	 * operation to obtain activity information that you want to preserve prior
	 * to performing an update.
	 * 
	 * @param activityUuid
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public Activity getActivity(String activityUuid, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.ACTIVITY_NODE.format(this, ActivityUrls.activityNodePart(activityUuid));
		return getActivityEntity(requestUrl, parameters);
	}

	/**
	 * To update an activity, send a replacement Atom Entry document containing
	 * the modified activity to the existing activity's edit URI. All existing
	 * activity information will be replaced with the new data. To avoid
	 * deleting all existing data, retrieve any data you want to retain first,
	 * and send it back with this request. For example, if you want to add a new
	 * tag to an activity, retrieve the existing tags and other data, and send
	 * it all back with the new tag in the update request.
	 * 
	 * @param activity
	 * @return
	 * @throws ClientServicesException
	 */
	public void updateActivity(Activity activity) throws ClientServicesException {
		updateActivity(activity, null);
	}

	/**
	 * To update an activity, send a replacement Atom Entry document containing
	 * the modified activity to the existing activity's edit URI. All existing
	 * activity information will be replaced with the new data. To avoid
	 * deleting all existing data, retrieve any data you want to retain first,
	 * and send it back with this request. For example, if you want to add a new
	 * tag to an activity, retrieve the existing tags and other data, and send
	 * it all back with the new tag in the update request.
	 * 
	 * @param activity
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public void updateActivity(Activity activity, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.ACTIVITY_NODE.format(this, ActivityUrls.activityNodePart(activity.getActivityUuid()));
		updateActivityEntity(requestUrl, activity, parameters, HTTPCode.OK);
	}

	/**
	 * To delete an existing activity, use the HTTP DELETE method. Deleted
	 * activities are moved to the trash collection and can be restored.
	 * 
	 * @param activity
	 * @return
	 * @throws ClientServicesException
	 */
	public String deleteActivity(Activity activity) throws ClientServicesException {
		return deleteActivity(activity.getActivityUuid(), null);
	}

	/**
	 * To delete an existing activity, use the HTTP DELETE method. Deleted
	 * activities are moved to the trash collection and can be restored.
	 * 
	 * @param activity
	 * @return
	 * @throws ClientServicesException
	 */
	public String deleteActivity(String activityUuid) throws ClientServicesException {
		return deleteActivity(activityUuid, null);
	}

	/**
	 * To delete an existing activity, use the HTTP DELETE method. Deleted
	 * activities are moved to the trash collection and can be restored.
	 * 
	 * @param activityUuid
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public String deleteActivity(String activityUuid, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.ACTIVITY_NODE.format(this, ActivityUrls.activityNodePart(activityUuid));
		deleteActivityEntity(requestUrl, activityUuid, parameters);
		return activityUuid;
	}

	/**
	 * To restore a deleted activity, use a HTTP PUT request. This moves the
	 * activity from the trash feed to the user's My Activities feed.
	 * 
	 * @param activity
	 * @return
	 * @throws ClientServicesException
	 */
	public void restoreActivity(Activity activity) throws ClientServicesException {
		restoreActivity(activity, null);
	}

	/**
	 * To restore a deleted activity, use a HTTP PUT request. This moves the
	 * activity from the trash feed to the user's My Activities feed.
	 * 
	 * @param activity
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public void restoreActivity(Activity activity, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.TRASHED_ACTIVITY_NODE.format(this, ActivityUrls.activityNodePart(activity.getActivityUuid()));
		updateActivityEntity(requestUrl, activity, parameters, HTTPCode.NO_CONTENT);
		activity.setDeleted(false);
	}

	/**
	 * 
	 * @param activity
	 * @param fileName
	 * @param fileContent
	 * @param mimeType
	 * @throws ClientServicesException
	 */
	public void uploadFile(ActivityNode activityNode, String fileName, InputStream fileContent, String mimeType) throws ClientServicesException {
		uploadFile(activityNode.getActivityNodeUuid(), fileName, fileContent, mimeType);
	}

	/**
	 * 
	 * @param activity
	 * @param fileName
	 * @param fileContent
	 * @param mimeType
	 * @throws ClientServicesException
	 */
	public void uploadFile(String activityNodeUuid, String fileName, InputStream fileContent, String mimeType) throws ClientServicesException {
		String requestUrl = ActivityUrls.ACTIVITY.format(this, ActivityUrls.activityPart(activityNodeUuid));
		createActivityNodeFile(requestUrl, fileName, fileContent, mimeType);
	}

	/**
	 * 
	 * @param fid
	 * @param fileName
	 * @param fileContent
	 * @param mimeType
	 * @throws ClientServicesException
	 */
	public void updateFile(String fid, String fileName, InputStream fileContent, String mimeType) throws ClientServicesException {
		String requestUrl = ActivityUrls.ACTIVITY_NODE.format(this, ActivityUrls.activityNodePart(fid));
		updateActivityNodeFile(requestUrl, fileName, fileContent, mimeType);
	}

	// ------------------------------------------------------------------------------------------------------------------
	// Working with activity nodes programmatically.
	// ------------------------------------------------------------------------------------------------------------------

	/**
	 * 
	 * @param activityNode
	 * @return
	 * @throws ClientServicesException
	 */
	public ActivityNode createActivityNode(ActivityNode activityNode) throws ClientServicesException {
		return createActivityNode(activityNode, null);
	}

	/**
	 * 
	 * @param activityNode
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public ActivityNode createActivityNode(ActivityNode activityNode, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.ACTIVITY.format(this, ActivityUrls.activityPart(activityNode.getActivityUuid()));
		return createActivityNodeEntity(requestUrl, activityNode, parameters);
	}

	/**
	 * 
	 * @param activityNodeUuid
	 * @return
	 * @throws ClientServicesException
	 */
	public ActivityNode getActivityNode(String activityNodeUuid) throws ClientServicesException {
		return getActivityNode(activityNodeUuid, null);
	}

	/**
	 * 
	 * @param activityNodeUuid
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public ActivityNode getActivityNode(String activityNodeUuid, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.ACTIVITY_NODE.format(this, ActivityUrls.activityNodePart(activityNodeUuid));
		return getActivityNodeEntity(requestUrl, parameters);
	}

	/**
	 * 
	 * @param activityNode
	 * @return
	 * @throws ClientServicesException
	 */
	public void updateActivityNode(ActivityNode activityNode) throws ClientServicesException {
		updateActivityNode(activityNode, null);
	}

	/**
	 * 
	 * @param activityNode
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public void updateActivityNode(ActivityNode activityNode, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.ACTIVITY_NODE.format(this, ActivityUrls.activityNodePart(activityNode.getActivityNodeUuid()));
		updateActivityNodeEntity(requestUrl, activityNode, parameters, HTTPCode.OK);
	}

	/**
	 * 
	 * @param activityNode
	 * @return
	 * @throws ClientServicesException
	 */
	public String deleteActivityNode(ActivityNode activityNode) throws ClientServicesException {
		return deleteActivityNode(activityNode.getActivityNodeUuid(), null);
	}

	/**
	 * 
	 * @param activityNode
	 * @return
	 * @throws ClientServicesException
	 */
	public String deleteActivityNode(String activityNodeUuid) throws ClientServicesException {
		return deleteActivityNode(activityNodeUuid, null);
	}

	/**
	 * 
	 * @param activityNodeUuid
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public String deleteActivityNode(String activityNodeUuid, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.ACTIVITY_NODE.format(this, ActivityUrls.activityNodePart(activityNodeUuid));
		deleteActivityNodeEntity(requestUrl, activityNodeUuid, parameters);
		return activityNodeUuid;
	}

	/**
	 * 
	 * @param activityNode
	 * @return
	 * @throws ClientServicesException
	 */
	public void restoreActivityNode(ActivityNode activityNode) throws ClientServicesException {
		restoreActivityNode(activityNode, null);
	}

	/**
	 * 
	 * @param activityNode
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public void restoreActivityNode(ActivityNode activityNode, Map<String, String> parameters) throws ClientServicesException {
		// TODO Remove the <category
		// scheme="http://www.ibm.com/xmlns/prod/sn/flags" term="deleted"/> flag
		// element from the entry before restoring it.
		String requestUrl = ActivityUrls.TRASHED_ACTIVITY_NODE.format(this, ActivityUrls.activityNodePart(activityNode.getActivityNodeUuid()));
		updateActivityNodeEntity(requestUrl, activityNode, parameters, HTTPCode.NO_CONTENT);
	}

	/*
	 * Create an entry with one file attachment, by using a multipart post
	 * method to post the file.
	 * 
	 * @param activityNode
	 * 
	 * @param fileName
	 * 
	 * @param fileContent
	 * 
	 * @throws ClientServicesException
	 */
	// public ActivityNode createActivityNode(ActivityNode activityNode, String
	// fileName, InputStream fileContent, String mimeType) throws
	// ClientServicesException {
	// return createActivityNode(activityNode, fileName, fileContent, mimeType,
	// null);
	// }

	/*
	 * Create an entry with one file attachment, by using a multipart post
	 * method to post the file.
	 * 
	 * @param activityNode
	 * 
	 * @param fileName
	 * 
	 * @param fileContent
	 * 
	 * @param parameters
	 * 
	 * @throws ClientServicesException
	 */
	// public ActivityNode createActivityNode(ActivityNode activityNode, String
	// fileName, InputStream fileContent, String mimeType, Map<String, String>
	// parameters) throws ClientServicesException {
	// // create file field if required
	// if (activityNode.getFieldByName(fileName) == null) {
	// FileField field = new FileField();
	// field.setName(fileName);
	// field.setPosition(1000);
	// activityNode.addField(field);
	// }
	//
	// String requestUrl = ActivityUrls.ACTIVITY.format(this,
	// ActivityUrls.activityPart(activityNode.getActivityUuid()));
	// return createActivityNodeEntity(requestUrl, activityNode, fileName,
	// fileContent, mimeType, parameters);
	// }

	// ------------------------------------------------------------------------------------------------------------------
	// Working with activity nodes programmatically.
	// ------------------------------------------------------------------------------------------------------------------

	/**
	 * Add an activity member.
	 * 
	 * @param activity
	 * @param member
	 * @return
	 */
	public Member addMember(Activity activity, Member member) throws ClientServicesException {
		return addMember(activity, member, null);
	}

	/**
	 * Add an activity member.
	 * 
	 * @param activityUuid
	 * @param member
	 * @return
	 */
	public Member addMember(String activityUuid, Member member) throws ClientServicesException {
		return addMember(activityUuid, member, null);
	}

	/**
	 * Add an activity member.
	 * 
	 * @param activity
	 * @param member
	 * @param parameters
	 * @return
	 */
	public Member addMember(Activity activity, Member member, Map<String, String> parameters) throws ClientServicesException {
		return addMember(activity.getActivityUuid(), member, parameters);
	}

	/**
	 * Add an activity member.
	 * 
	 * @param activityUuid
	 * @param member
	 * @param parameters
	 * @return
	 */
	public Member addMember(String activityUuid, Member member, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.ACTIVITY_ACL.format(this, ActivityUrls.activityPart(activityUuid));
		return createMemberEntity(requestUrl, member, parameters);
	}

	public void addMembers(Activity activity, List<Member> members) throws ClientServicesException {
		addMembers(activity.getActivityUuid(), members, null);
	}

	public void addMembers(Activity activity, List<Member> members, Map<String, String> parameters) throws ClientServicesException {
		addMembers(activity.getActivityUuid(), members, parameters);
	}

	public void addMembers(String activityUuid, List<Member> members) throws ClientServicesException {
		addMembers(activityUuid, members, null);
	}

	public void addMembers(String activityUuid, List<Member> members, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.ACTIVITY_ACL.format(this, ActivityUrls.activityPart(activityUuid));
		createMembers(requestUrl, members, parameters);
	}

	public void removeMembers(String activityUuid, List<Member> members, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.ACTIVITY_ACL.format(this, ActivityUrls.activityPart(activityUuid));
		deleteMembers(requestUrl, members, parameters);
	}

	/**
	 * Retrieve an activity member.
	 * 
	 * @param activity
	 * @return
	 */
	public Member getMember(Activity activity, String memberId) throws ClientServicesException {
		return getMember(activity, memberId, null);
	}

	/**
	 * Retrieve an activity member.
	 * 
	 * @param activity
	 * @return
	 */
	public Member getMember(String activityUuid, String memberId) throws ClientServicesException {
		return getMember(activityUuid, memberId, null);
	}

	/**
	 * Retrieve an activity member.
	 * 
	 * @param activity
	 * @param parameters
	 * @return
	 */
	public Member getMember(Activity activity, String memberId, Map<String, String> parameters) throws ClientServicesException {
		return getMember(activity.getActivityUuid(), memberId, parameters);
	}

	/**
	 * Retrieve an activity member.
	 * 
	 * @param activity
	 * @param parameters
	 * @return
	 */
	public Member getMember(String activityUuid, String memberId, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.ACTIVITY_MEMBER.format(this, ActivityUrls.activityPart(activityUuid), ActivityUrls.memberPart(memberId));
		return getMemberEntity(requestUrl, parameters);
	}

	/**
	 * Update an activity member.
	 * 
	 * @param activity
	 * @param member
	 * @return
	 */
	public Member updateMember(Activity activity, Member member) throws ClientServicesException {
		return updateMember(activity, member, null);
	}

	/**
	 * Update an activity member.
	 * 
	 * @param activityUuid
	 * @param member
	 * @return
	 */
	public Member updateMember(String activityUuid, Member member) throws ClientServicesException {
		return updateMember(activityUuid, member, null);
	}

	/**
	 * Update an activity member.
	 * 
	 * @param activity
	 * @param member
	 * @param parameters
	 * @return
	 */
	public Member updateMember(Activity activity, Member member, Map<String, String> parameters) throws ClientServicesException {
		return updateMember(activity.getActivityUuid(), member, parameters);
	}

	/**
	 * Update an activity member.
	 * 
	 * @param activityUuid
	 * @param member
	 * @param parameters
	 * @return
	 */
	public Member updateMember(String activityUuid, Member member, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.ACTIVITY_MEMBER.format(this, ActivityUrls.activityPart(activityUuid), ActivityUrls.memberPart(member.getId()));
		return updateMemberEntity(requestUrl, member, parameters);
	}

	/**
	 * Delete an activity member.
	 * 
	 * @param activity
	 * @param member
	 * @return
	 */
	public String deleteMember(Activity activity, Member member) throws ClientServicesException {
		return deleteMember(activity, member, null);
	}

	/**
	 * Delete an activity member.
	 * 
	 * @param activityUuid
	 * @param member
	 * @return
	 */
	public String deleteMember(String activityUuid, Member member) throws ClientServicesException {
		return deleteMember(activityUuid, member, null);
	}

	/**
	 * Delete an activity member.
	 * 
	 * @param activity
	 * @param member
	 * @param parameters
	 * @return
	 */
	public String deleteMember(Activity activity, Member member, Map<String, String> parameters) throws ClientServicesException {
		return deleteMember(activity.getActivityUuid(), member, parameters);
	}

	/**
	 * Delete an activity member.
	 * 
	 * @param activityUuid
	 * @param member
	 * @param parameters
	 * @return
	 */
	public String deleteMember(String activityUuid, Member member, Map<String, String> parameters) throws ClientServicesException {
		return deleteMember(activityUuid, member.getId(), parameters);
	}

	/**
	 * Delete an activity member.
	 * 
	 * @param activityUuid
	 * @param member
	 * @param parameters
	 * @return
	 */
	public String deleteMember(String activityUuid, String memberId, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.ACTIVITY_MEMBER.format(this, ActivityUrls.activityPart(activityUuid), ActivityUrls.memberPart(memberId));
		deleteMemberEntity(requestUrl, memberId, parameters);
		return memberId;
	}

	/**
	 * Change the priority for the specified activity for the currently
	 * authenticated user.
	 * 
	 * @param activity
	 * @throws ClientServicesException
	 */
	public void changePriority(Activity activity, int priority) throws ClientServicesException {
		changePriority(activity.getActivityUuid(), priority);
	}

	/**
	 * Change the priority for the specified activity for the currently
	 * authenticated user.
	 * 
	 * @param activityNodeUuid
	 * @throws ClientServicesException
	 */
	public void changePriority(String activityNodeUuid, int priority) throws ClientServicesException {
		String requestUrl = ActivityUrls.CHANGE_PRIORITY.format(this, ActivityUrls.activityNodePart(activityNodeUuid), ActivityUrls.priorityPart(priority));
		updateActivityEntity(requestUrl, null, null, HTTPCode.NO_CONTENT);
	}

	public ActivityNode moveFieldToEntry(String destinationUuid, String fieldUuid) throws ClientServicesException {
		return moveFieldToEntry(destinationUuid, fieldUuid, null);
	}

	public ActivityNode moveFieldToEntry(String destinationUuid, String fieldUuid, int position) throws ClientServicesException {
		Map<String, String> parameters = getParameters(null);
		parameters.put("position", Integer.toString(position));
		return moveFieldToEntry(destinationUuid, fieldUuid, parameters);
	}

	public ActivityNode moveFieldToEntry(String destinationUuid, String fieldUuid, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.MOVE_FIELD.format(this, ActivityUrls.destinationPart(destinationUuid), ActivityUrls.fieldPart(fieldUuid));
		Response response = putData(requestUrl, parameters, getAtomHeaders(), null, null);
		checkResponseCode(response, HTTPCode.OK);

		return getActivityNodeFeedHandler(false).createEntity(response);
	}

	public ActivityNode moveNode(String nodeUuid, String destinationUuid) throws ClientServicesException {
		return this.moveNode(nodeUuid, destinationUuid, null);
	}

	public ActivityNode moveNode(String nodeUuid, String destinationUuid, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.MOVE_NODE.format(this, ActivityUrls.activityNodePart(nodeUuid), ActivityUrls.destinationPart(destinationUuid));
		Response response = createData(requestUrl, parameters, getAtomHeaders(), null, null);
		checkResponseCode(response, HTTPCode.OK);

		return getActivityNodeFeedHandler(false).createEntity(response);
	}

	public ActivityNode copyNode(String nodeUuid, String destinationUuid) throws ClientServicesException {
		return this.copyNode(nodeUuid, destinationUuid, null);
	}

	public ActivityNode copyNode(String nodeUuid, String destinationUuid, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = ActivityUrls.COPY_NODE.format(this, ActivityUrls.activityNodePart(nodeUuid), ActivityUrls.destinationPart(destinationUuid));
		Response response = createData(requestUrl, parameters, getAtomHeaders(), null, null);
		checkResponseCode(response, HTTPCode.OK);

		return getActivityNodeFeedHandler(false).createEntity(response);
	}

	// ------------------------------------------------------------------------------------------------------------------
	// Internal implementations
	// ------------------------------------------------------------------------------------------------------------------

	protected Activity getActivityEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		try {
			return getEntity(requestUrl, parameters, getActivityFeedHandler(false));
		} catch (ClientServicesException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientServicesException(e);
		}
	}

	protected Activity createActivityEntity(String requestUrl, Activity activity, Map<String, String> parameters) throws ClientServicesException {
		try {
			ActivitySerializer serializer = new ActivitySerializer(activity);
			Map<String, String> headers = activity.hasAttachments() ? getMultipartHeaders() : getAtomHeaders();
			Response response = createData(requestUrl, parameters, headers, serializer.generateCreate());
			checkResponseCode(response, HTTPCode.CREATED);
			return updateActivityEntityData(activity, response);
		} catch (ClientServicesException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientServicesException(e);
		}
	}

	protected void createMembers(String requestUrl, List<Member> members, Map<String, String> parameters) throws ClientServicesException {
		try {
			MemberSerializer serializer = new MemberSerializer(members.get(0));

			Response response = createData(requestUrl, parameters, getAtomHeaders(), serializer.generateMemberFeed(members));
			//checkResponseCode(response, HTTPCode.CREATED);
		} catch (ClientServicesException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientServicesException(e);
		}

	}

	protected void deleteMembers(String requestUrl, List<Member> members, Map<String, String> parameters) throws ClientServicesException {
		try {
			MemberSerializer serializer = new MemberSerializer(members.get(0));
			Response response = delete(requestUrl, parameters, getAtomHeaders(), null, serializer.generateMemberFeed(members));
			checkResponseCode(response, HTTPCode.OK);
		} catch (ClientServicesException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientServicesException(e);
		}

	}

	protected void updateActivityEntity(String requestUrl, Activity activity, Map<String, String> parameters, HTTPCode expectedCode) throws ClientServicesException {
		try {
			String payload = null;
			if (activity != null) {
				ActivitySerializer serializer = new ActivitySerializer(activity);
				payload = serializer.generateUpdate();
			}
			Map<String, String> headers = activity.hasAttachments() ? getMultipartHeaders() : getAtomHeaders();
			String uniqueId = (activity == null) ? null : activity.getActivityUuid();
			Response response = putData(requestUrl, parameters, headers, payload, uniqueId);
			checkResponseCode(response, expectedCode);
		} catch (ClientServicesException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientServicesException(e);
		}
	}

	protected void deleteActivityEntity(String requestUrl, String activityUuid, Map<String, String> parameters) throws ClientServicesException {
		try {
			Response response = deleteData(requestUrl, parameters, activityUuid);
			checkResponseCode(response, HTTPCode.NO_CONTENT);
		} catch (ClientServicesException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientServicesException(e);
		}
	}

	protected void createActivityNodeFile(String requestUrl, String fileName, InputStream fileContent, String mimeType) throws ClientServicesException {
		try {
			Map<String, String> headers = new HashMap<String, String>();
			headers.put(SLUG, fileName);
			headers.put(CONTENT_TYPE, mimeType);
			headers.put(TITLE, fileName);
			if (isLargeFile(fileContent)) {
				Map<String, String> xheaders = new HashMap<String, String>();
				xheaders.put(X_IBM_UPLOAD_METHOD, UPLOAD_METHOD_PHASES);
				xheaders.put(X_IBM_UPLOAD_SIZE, "" + fileContent.available());
				Response xresponse = createData(requestUrl, null, xheaders, null, null);
				checkResponseCode(xresponse, HTTPCode.OK);
				String token = xresponse.getResponseHeader(X_IBM_UPLOAD_TOKEN);

				headers.put(X_IBM_UPLOAD_TOKEN, token);
				headers.put(X_IBM_UPLOAD_METHOD, UPLOAD_METHOD_PHASES);				
			}

			Response response = createData(requestUrl, null, headers, fileContent, null);
			checkResponseCode(response, HTTPCode.OK);
		} catch (ClientServicesException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientServicesException(e);
		}
	}

	protected void updateActivityNodeFile(String requestUrl, String fileName, InputStream fileContent, String mimeType) throws ClientServicesException {
		try {
			Map<String, String> xheaders = new HashMap<String, String>();
			xheaders.put(X_IBM_UPLOAD_METHOD, UPLOAD_METHOD_PHASES);
			xheaders.put(X_IBM_UPLOAD_SIZE, "" + fileContent.available());
			Response xresponse = putData(requestUrl, null, xheaders, null, null);
			checkResponseCode(xresponse, HTTPCode.OK);
			String token = xresponse.getResponseHeader(X_IBM_UPLOAD_TOKEN);

			Map<String, String> headers = new HashMap<String, String>();
			headers.put(SLUG, fileName);
			headers.put(CONTENT_TYPE, mimeType);
			headers.put(TITLE, fileName);
			headers.put(X_IBM_UPLOAD_TOKEN, token);
			headers.put(X_IBM_UPLOAD_METHOD, UPLOAD_METHOD_PHASES);

			Response response = putData(requestUrl, null, headers, fileContent, null);
			checkResponseCode(response, HTTPCode.OK);
		} catch (ClientServicesException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientServicesException(e);
		}
	}

	protected Activity updateActivityEntityData(Activity activity, Response response) {
		Node node = (Node) response.getData();
		XPathExpression xpath = (node instanceof Document) ? (XPathExpression) AtomXPath.singleEntry.getPath() : null;
		activity.setData(node, nameSpaceCtx, xpath);
		activity.setService(this);
		return activity;
	}

	protected ActivityNode getActivityNodeEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		try {
			return getEntity(requestUrl, parameters, getActivityNodeFeedHandler(false));
		} catch (ClientServicesException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientServicesException(e);
		}
	}

	protected ActivityNode createActivityNodeEntity(String requestUrl, ActivityNode activityNode, Map<String, String> parameters) throws ClientServicesException {
		try {
			ActivityNodeSerializer serializer = new ActivityNodeSerializer(activityNode);
			Map<String, String> headers = activityNode.hasAttachments() ? getMultipartHeaders() : getAtomHeaders();
			Response response = createData(requestUrl, parameters, headers, serializer.generateCreate());
			checkResponseCode(response, HTTPCode.CREATED);
			return updateActivityNodeEntityData(activityNode, response);
		} catch (ClientServicesException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientServicesException(e);
		}
	}

	protected ActivityNode createActivityNodeEntity(String requestUrl, ActivityNode activityNode, String fileName, InputStream fileContent, String mimeType, Map<String, String> parameters) throws ClientServicesException {
		try {
			ActivityNodeSerializer serializer = new ActivityNodeSerializer(activityNode);

			List<ClientService.ContentPart> contentParts = new ArrayList<ClientService.ContentPart>();
			contentParts.add(new ContentPart("activityNode", serializer.generateCreate(), APPLICATION_ATOM_XML));
			contentParts.add(new ContentPart(fileName, fileContent, fileName, mimeType));

			Response response = createData(requestUrl, parameters, getMultipartHeaders(), contentParts);
			checkResponseCode(response, HTTPCode.CREATED);
			return updateActivityNodeEntityData(activityNode, response);
		} catch (ClientServicesException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientServicesException(e);
		}
	}

	protected void updateActivityNodeEntity(String requestUrl, ActivityNode activityNode, Map<String, String> parameters, HTTPCode expectedCode) throws ClientServicesException {
		try {
			ActivityNodeSerializer serializer = new ActivityNodeSerializer(activityNode);
			Map<String, String> headers = activityNode.hasAttachments() ? getMultipartHeaders() : getAtomHeaders();
			Response response = putData(requestUrl, parameters, headers, serializer.generateUpdate(), activityNode.getActivityNodeUuid());
			checkResponseCode(response, expectedCode);
		} catch (ClientServicesException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientServicesException(e);
		}
	}

	protected void deleteActivityNodeEntity(String requestUrl, String activityNodeUuid, Map<String, String> parameters) throws ClientServicesException {
		try {
			Response response = deleteData(requestUrl, parameters, activityNodeUuid);
			checkResponseCode(response, HTTPCode.NO_CONTENT);
		} catch (ClientServicesException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientServicesException(e);
		}
	}

	protected ActivityNode updateActivityNodeEntityData(ActivityNode activityNode, Response response) {
		Node node = (Node) response.getData();
		XPathExpression xpath = (node instanceof Document) ? (XPathExpression) AtomXPath.singleEntry.getPath() : null;
		activityNode.setData(node, nameSpaceCtx, xpath);
		activityNode.setService(this);
		return activityNode;
	}

	protected Member getMemberEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		try {
			return getEntity(requestUrl, parameters, getMemberFeedHandler(false));
		} catch (ClientServicesException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientServicesException(e);
		}
	}

	protected Member createMemberEntity(String requestUrl, Member member, Map<String, String> parameters) throws ClientServicesException {
		try {
			MemberSerializer serializer = new MemberSerializer(member);
			Response response = createData(requestUrl, parameters, getAtomHeaders(), serializer.generateCreate());
			checkResponseCode(response, HTTPCode.CREATED);
			return updateMemberEntityData(member, response);
		} catch (ClientServicesException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientServicesException(e);
		}
	}

	protected Member updateMemberEntity(String requestUrl, Member member, Map<String, String> parameters) throws ClientServicesException {
		try {
			MemberSerializer serializer = new MemberSerializer(member);
			Response response = putData(requestUrl, parameters, getAtomHeaders(), serializer.generateUpdate(), null);
			checkResponseCode(response, HTTPCode.OK);
			return updateMemberEntityData(member, response);
		} catch (ClientServicesException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientServicesException(e);
		}
	}

	protected void deleteMemberEntity(String requestUrl, String memberId, Map<String, String> parameters) throws ClientServicesException {
		try {
			Response response = deleteData(requestUrl, parameters, memberId);
			checkResponseCode(response, HTTPCode.OK);
		} catch (ClientServicesException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientServicesException(e);
		}
	}

	protected Member updateMemberEntityData(Member member, Response response) {
		// Response does not contain a valid member entry
		Node node = null;
		Object data = response.getData();
		if (data instanceof Node) {
			node = (Node) data;
			XPathExpression xpath = (node instanceof Document) ? (XPathExpression) AtomXPath.singleEntry.getPath() : null;
			member.setData(node, nameSpaceCtx, xpath);
		}
		member.setService(this);
		return member;
	}

	protected EntityList<Activity> getActivityEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return (EntityList<Activity>) getEntities(requestUrl, getParameters(parameters), getActivityFeedHandler(true));
	}

	protected EntityList<ActivityNode> getActivityNodeEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return (EntityList<ActivityNode>) getEntities(requestUrl, getParameters(parameters), getActivityNodeFeedHandler(true));
	}

	protected EntityList<Tag> getTagEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return (EntityList<Tag>) getEntities(requestUrl, getParameters(parameters), getTagFeedHandler());
	}

	protected EntityList<Category> getCategoryEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return (EntityList<Category>) getEntities(requestUrl, getParameters(parameters), getCategoryFeedHandler());
	}

	protected EntityList<Member> getMemberEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return (EntityList<Member>) getEntities(requestUrl, getParameters(parameters), getMemberFeedHandler(true));
	}

	protected EntityList<ActivityUpdate> getActivityUpdateEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return (EntityList<ActivityUpdate>) getEntities(requestUrl, getParameters(parameters), getActivityUpdateFeedHandler(true));
	}

	protected IFeedHandler<Activity> getActivityFeedHandler(boolean isFeed) {
		return new AtomFeedHandler<Activity>(this, isFeed) {
			@Override
			protected Activity entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Activity(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	protected IFeedHandler<ActivityNode> getActivityNodeFeedHandler(boolean isFeed) {
		return new AtomFeedHandler<ActivityNode>(this, isFeed) {
			@Override
			protected ActivityNode entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new ActivityNode(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	protected IFeedHandler<Member> getMemberFeedHandler(boolean isFeed) {
		return new AtomFeedHandler<Member>(this, isFeed) {
			@Override
			protected Member entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Member(service, node, nameSpaceCtx, xpath);
			}
			/* (non-Javadoc)
			 * @see com.ibm.sbt.services.client.base.AtomFeedHandler#createEntityList(com.ibm.sbt.services.client.Response)
			 */
			@Override
			public EntityList<Member> createEntityList(Response dataHolder) {
				AtomEntityList<Member> entityList = (AtomEntityList<Member>)super.createEntityList(dataHolder);
				entityList.setTotalResults(ConnectionsFeedXpath.TotalMembers);
				return entityList;
			}
		};
	}

	protected IFeedHandler<ActivityUpdate> getActivityUpdateFeedHandler(boolean isFeed) {
		return new AtomFeedHandler<ActivityUpdate>(this, isFeed) {
			@Override
			protected ActivityUpdate entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new ActivityUpdate(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	protected IFeedHandler<Tag> getTagFeedHandler() {
		return new CategoryFeedHandler<Tag>(this) {
			@Override
			protected Tag newEntity(BaseService service, Node node) {
				XPathExpression xpath = (node instanceof Document) ? (XPathExpression) AtomXPath.singleCategory.getPath() : null;
				return new Tag(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	protected IFeedHandler<Category> getCategoryFeedHandler() {
		return new CategoryFeedHandler<Category>(this) {
			@Override
			protected Category newEntity(BaseService service, Node node) {
				XPathExpression xpath = (node instanceof Document) ? (XPathExpression) AtomXPath.singleCategory.getPath() : null;
				return new Category(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	protected Map<String, String> getParameters(Map<String, String> parameters) {
		if (parameters == null)
			return new HashMap<String, String>();
		else
			return parameters;
	}

	protected boolean isV5OrHigher() {
		Endpoint endpoint = getEndpoint();
		String apiVersion = endpoint.getApiVersion();
		if (StringUtil.isNotEmpty(apiVersion)) {
			Version version = Version.parse(apiVersion);
			return (version.getMajor() >= 5);
		}
		return false;
	}

	protected boolean isLargeFile(InputStream istream) throws IOException {
		return (istream.available() > LARGE_FILE_THRESHOLD);
	}

}
