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

package com.ibm.sbt.services.client.connections.activity;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.base.util.EntityUtil;
import com.ibm.sbt.services.client.connections.activity.feedHandler.ActivityFeedHandler;
import com.ibm.sbt.services.client.connections.activity.feedHandler.ActivityNodeFeedHandler;
import com.ibm.sbt.services.client.connections.activity.feedHandler.MemberFeedHandler;
import com.ibm.sbt.services.client.connections.activity.feedHandler.TagFeedHandler;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * ActivityService can be used to perform operations related to Activities.
 * <p>
 * 
 * @author Vimal Dhupar
 * @see http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Activities_API_ic40a&content=pdcontent
 */

public class ActivityService extends BaseService {
	
	/**
	 * Used in constructing REST APIs
	 */
	public static final String	baseUrl		= "/activities/service/atom2/";
	
	/**
	 * Constructor Creates ActivityService Object with default endpoint
	 */
	public ActivityService() {
		this(DEFAULT_ENDPOINT_NAME);
	}

	/**
	 * Constructor
	 * 
	 * @param endpoint
	 *            Creates ActivityService Object with the specified endpoint
	 */
	public ActivityService(String endpoint) {
		super(endpoint);
	}
	
	/**
	 * Constructor
	 * 
	 * @param endpoint
	 *            Creates ActivityService Object with the specified endpoint
	 */
	public ActivityService(Endpoint endpoint) {
		super(endpoint);
	}
	
    /**
     * Method returns Activities of the logged in user
     * 
     * @return ActivityList
     * @throws ActivityServiceException
     */
    public ActivityList getMyActivities() throws ActivityServiceException {
    	return getMyActivities(null);
    }
    
    /**
     * Method returns Activities of the logged in user
     * 
     * @param params
     * @return ActivityList
     * @throws ActivityServiceException
     */
    public ActivityList getMyActivities(Map<String, String> params) throws ActivityServiceException {
        String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.ACTIVITIES.getActivityAction());
        try {
        	return (ActivityList) getEntities(requestUri, params, new ActivityFeedHandler(this));
        } catch (Exception e) {
            throw new ActivityServiceException(e);
        }
    }
    
    /**
     * Method returns Completed Activities of the logged in user
     * 
     * @return ActivityList
     * @throws ActivityServiceException
     */
    public ActivityList getCompletedActivities() throws ActivityServiceException {
    	return getCompletedActivities(null);
    }
    
    /**
     * Method returns Completed Activities of the logged in user
     * 
     * @param params
     * @return ActivityList
     * @throws ActivityServiceException
     */
    public ActivityList getCompletedActivities(Map<String, String> params) throws ActivityServiceException {
        String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.COMPLETED.getActivityAction());
        try {
        	return (ActivityList) getEntities(requestUri, params, new ActivityFeedHandler(this));
        } catch (Exception e) {
            throw new ActivityServiceException(e);
        }
    }
    
    /**
     * Method returns All Activities of the logged in user
     * 
     * @return ActivityList
     * @throws ActivityServiceException
     */
    public ActivityList getAllActivities() throws ActivityServiceException {
    	return getAllActivities(null);
    }
    
    /**
     * Method returns All Activities of the logged in user
     * 
     * @param params
     * @return ActivityList
     * @throws ActivityServiceException
     */
    public ActivityList getAllActivities(Map<String, String> params) throws ActivityServiceException {
        String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.EVERYTHING.getActivityAction());
        try {
        	return (ActivityList) getEntities(requestUri, params, new ActivityFeedHandler(this));
        } catch (Exception e) {
            throw new ActivityServiceException(e);
        }
    }
    
    /**
     * Method returns Todos of the logged in user
     * 
     * @return ActivityList
     * @throws ActivityServiceException
     */
    public ActivityList getAllTodos() throws ActivityServiceException {
    	return getAllTodos(null);
    }
    
    /**
     * Method returns Todos of the logged in user
     * 
     * @param params
     * @return ActivityList
     * @throws ActivityServiceException
     */
    public ActivityList getAllTodos(Map<String, String> params) throws ActivityServiceException {
        String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.TODOS.getActivityAction());
        try {
        	return (ActivityList) getEntities(requestUri, params, new ActivityFeedHandler(this));
        } catch (Exception e) {
            throw new ActivityServiceException(e);
        }
    }
    
    /**
     * Method returns Tags of the logged in user
     * @return TagList
     * @throws ActivityServiceException
     */
    public TagList getAllTags() throws ActivityServiceException {
    	return getAllTags(null);
    }
    
    /**
     * Method returns Tags of the logged in user
     * 
     * @param params
     * @return TagList
     * @throws ActivityServiceException
     */
    public TagList getAllTags(Map<String, String> params) throws ActivityServiceException {
        String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.TAGS.getActivityAction());
        try {
        	return (TagList) getEntities(requestUri, params, new TagFeedHandler(this));
        } catch (Exception e) {
            throw new ActivityServiceException(e);
        }
    }
    
    /**
     * Method returns Activities from Trash 
     * 
     * @return ActivityList
     * @throws ActivityServiceException
     */
    public ActivityList getActivitiesInTrash() throws ActivityServiceException {
    	return getActivitiesInTrash(null);
    }
    
    /**
     * Method returns Activities from Trash 
     * 
     * @param params
     * @return ActivityList
     * @throws ActivityServiceException
     */
    public ActivityList getActivitiesInTrash(Map<String, String> params) throws ActivityServiceException {
        String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.TRASH.getActivityAction());
        try {
        	return (ActivityList) getEntities(requestUri, params, new ActivityFeedHandler(this));
        } catch (Exception e) {
            throw new ActivityServiceException(e);
        }
    }
    
    /**
     * Method to create an Activity
     * 
     * @param activity
     * @return Activity
     * @throws ActivityServiceException
     */
    public Activity createActivity(Activity activity) throws ActivityServiceException {
		if (null == activity){
			throw new ActivityServiceException(null, "Null activity");
		}
		String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.ACTIVITIES.getActivityAction());
		try {
			Object activityPayload;
			try {
				activityPayload =  activity.constructPayload();
			} catch (TransformerException e) {
				throw new ActivityServiceException(e);
			}
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			Response requestData = createData(requestUri, null, headers, activityPayload);
			activity.clearFieldsMap();
			return (Activity) new ActivityFeedHandler(this).createEntity(requestData);
		} catch (Exception e) {
			throw new ActivityServiceException(e);
		} 
	}

    /**
     * Method to get an Activity
     * 
     * @param activityId
     * @return Activity
     * @throws ActivityServiceException
     */
    public Activity getActivity(String activityId) throws ActivityServiceException {
		if (null == activityId){
			throw new ActivityServiceException(null, "Null activityId");
		}
		String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.ACTIVITYNODE.getActivityAction());
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("activityNodeUuid", activityId);
        	return (Activity) getEntity(requestUri, params, new ActivityFeedHandler(this));
		} catch (Exception e) {
			throw new ActivityServiceException(e);
		} 
	}
    
	/**
	 * Method to update an Activity
	 * 
	 * @param activity
	 * @throws ActivityServiceException
	 */
	public void updateActivity(Activity activity) throws ActivityServiceException {
		if (null == activity){
			throw new ActivityServiceException(null, "Null activity");
		}
		if (null == activity.getId()){
			throw new ActivityServiceException(null, "Null activityId");
		}
		String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.ACTIVITYNODE.getActivityAction());
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("activityNodeUuid", activity.getActivityId());
			Object activityPayload = null;
			try {
				activityPayload =  activity.constructPayload();
			} catch (TransformerException e) {
				throw new ActivityServiceException(e);
			}
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			
			updateData(requestUri, params, headers, activityPayload, null);
			activity.clearFieldsMap();
		} catch (Exception e) {
			throw new ActivityServiceException(e);
		} 
	}
	
	/**
	 * Method to delete an Activity
	 * 
	 * @param activityId
	 * @throws ActivityServiceException
	 */
	public void deleteActivity(String activityId) throws ActivityServiceException {
		if (null == activityId){
			throw new ActivityServiceException(null, "Null activityId");
		}
		String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.ACTIVITYNODE.getActivityAction());
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("activityNodeUuid", activityId);
        	deleteData(requestUri, params, null);
		} catch (Exception e) {
			throw new ActivityServiceException(e);
		} 
	}
	
	/**
	 * Method to get Activity from Trash
	 * 
	 * @param activityId
	 * @return Activity
	 * @throws ActivityServiceException
	 */
	public Activity getActivityFromTrash(String activityId) throws ActivityServiceException {
		if (null == activityId){
			throw new ActivityServiceException(null, "Null activityId");
		}
		String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.TRASHNODE.getActivityAction());
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("activityNodeUuid", activityId);
        	return (Activity) getEntity(requestUri, params, new ActivityFeedHandler(this));
		} catch (Exception e) {
			throw new ActivityServiceException(e);
		} 
	}
	
	/**
	 * Method to restore an Activity
	 * 
	 * @param activityId
	 * @throws ActivityServiceException
	 */
	public void restoreActivity(String activityId) throws ActivityServiceException {
		if (StringUtil.isEmpty(activityId)){
			throw new ActivityServiceException(null, "Null activityId");
		}
		Activity givenActivity = getActivityFromTrash(activityId);
		Activity restoreActivity = new Activity();
		Object activityPayload = null;
		if(givenActivity.hasCategoryFlagDelete()) { 
			restoreActivity = givenActivity.copyTo(restoreActivity);
			try {
				activityPayload =  restoreActivity.constructPayload();
			} catch (TransformerException e) {
				throw new ActivityServiceException(e);
			}
		}
		String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.TRASHNODE.getActivityAction());
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("activityNodeUuid", activityId);
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			updateData(requestUri, params, headers, activityPayload, null);
			restoreActivity.clearFieldsMap();
		} catch (Exception e) {
			throw new ActivityServiceException(e);
		} 
	}

	/**
	 * Method to add member to Activity
	 * 
	 * @param activityId
	 * @param member
	 * @return Member
	 * @throws ActivityServiceException
	 */
	public Member addMember(String activityId, Member member) throws ActivityServiceException {
		if (StringUtil.isEmpty(activityId)){
			throw new ActivityServiceException(null, "Null activityId");
		}
//		Activity activity = getActivity(activityId);
//		if(!StringUtil.equalsIgnoreCase(activity.getCategory(), "explicit_membership_community_activity")) {
//			throw new ActivityServiceException(null, "Activity is not a \"Explicit Membership Community Activity\". Addition of Members is not supported.");
//		}
		String memberId = member.getUserid();
		if(StringUtil.isEmpty(memberId)){
			if(StringUtil.isEmpty(member.getEmail())) {
				throw new ActivityServiceException(null, "Null memberId / memberEmail"); 
			} else {
				memberId = member.getEmail(); 
			}
		}
		try {
			if(StringUtil.isEmpty(member.getRole())) {
				member.setRole("member");
			}
		} catch (Exception e) {	
			member.setRole("member"); 
		}
		String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.ACL.getActivityAction());
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityUuid", activityId);
		try {
			Object memberPayload = null;
			try {
				memberPayload =  member.constructPayload();
			} catch (TransformerException e) {
				throw new ActivityServiceException(e);
			}
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			Response requestData = createData(requestUri, params, headers, memberPayload);
			member.clearFieldsMap();
			if(requestData.getData() instanceof InputStream && requestData.getResponse().getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
				return null;
			}
			return (Member) new MemberFeedHandler(this).createEntity(requestData);
		} catch (Exception e) {
			throw new ActivityServiceException(e);
		} 
	}
	
	/**
	 * Method to update member in Activity
	 * 
	 * @param activityId
	 * @param member
	 * @throws ActivityServiceException
	 */
	public void updateMember(String activityId, Member member) throws ActivityServiceException {
		if (null == activityId){
			throw new ActivityServiceException(null, "Null activityId");
		}
		String memberId = member.getMemberId();
		if(StringUtil.isEmpty(memberId)){
			throw new ActivityServiceException(null, "Null memberId / memberEmail"); 
		}
		member.setUserid(member.getUserid());
		// We fetch all values and set them in the new member here. Reason, as per connections documentation  : 
		// All existing member information will be replaced with the new data. 
		// To avoid deleting all existing data, retrieve any data you want to retain first, 
		// and send it back with this request.
		if(!member.getFieldsMap().containsKey("role")) {
			if(StringUtil.isNotEmpty(member.getRole())) {
				member.setRole(member.getRole());
			}
		} 
		if(!member.getFieldsMap().containsKey("category")) {
			if(StringUtil.isNotEmpty(member.getCategory())) {
				member.setCategory(member.getCategory());
			}
		} 
		String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.ACL.getActivityAction());
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("activityUuid", activityId);
			params.put("memberid", memberId);
			
			Object memberPayload = null;
			try {
				memberPayload =  member.constructPayload();
			} catch (TransformerException e) {
				throw new ActivityServiceException(e);
			}
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			
			updateData(requestUri, params, headers, memberPayload, null);
			member.clearFieldsMap();
		} catch (Exception e) {
			throw new ActivityServiceException(e);
		} 
	}
	
	/**
	 * Method to get members 
	 * 
	 * @param activityId
	 * @return MemberList
	 * @throws ActivityServiceException
	 */
	public MemberList getMembers(String activityId) throws ActivityServiceException {
		if (StringUtil.isEmpty(activityId)){
			throw new ActivityServiceException(null, "Null activityId");
		}
		String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.ACL.getActivityAction());
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("activityUuid", activityId);
			
        	return (MemberList) getEntities(requestUri, params, new MemberFeedHandler(this));
		} catch (Exception e) {
			throw new ActivityServiceException(e);
		}
	}
	
	/**
	 * Method to get member
	 * 
	 * @param activityId
	 * @param memberId
	 * @return Member
	 * @throws ActivityServiceException
	 */
	public Member getMember(String activityId, String memberId) throws ActivityServiceException {
		if (StringUtil.isEmpty(activityId)||StringUtil.isEmpty(memberId)){
			throw new ActivityServiceException(null, "Null activityId / memberId");
		}
		String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.ACL.getActivityAction());
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("activityUuid", activityId);
			if(EntityUtil.isEmail(memberId)){
				params.put("email", memberId);
			}else{
				params.put("userid", memberId);
			}
			
        	return (Member) getEntity(requestUri, params, new MemberFeedHandler(this));
		} catch (Exception e) {
			throw new ActivityServiceException(e);
		} 
	}

	/**
	 * Method to delete member from Activity
	 * 
	 * @param activityId
	 * @param memberId
	 * @throws ActivityServiceException
	 */
	public void deleteMember(String activityId, String memberId) throws ActivityServiceException {
		if (null == activityId){
			throw new ActivityServiceException(null, "Null activityId");
		}
		if (null == memberId){
			throw new ActivityServiceException(null, "Null memberId");
		}
		String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.ACL.getActivityAction());
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("activityUuid", activityId);
			params.put("memberid", memberId);
        	deleteData(requestUri, params, null);
		} catch (Exception e) {
			throw new ActivityServiceException(e);
		} 
	}

	/**
	 * Method to create Activity node
	 * 
	 * @param activityNode
	 * @return ActivityNode
	 * @throws ActivityServiceException
	 */
	public ActivityNode createActivityNode(ActivityNode activityNode) throws ActivityServiceException {
		if (null == activityNode){
			throw new ActivityServiceException(null, "Null activity node");
		}
		if (null == activityNode.getId()){
			throw new ActivityServiceException(null, "Null activity id");
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityUuid", activityNode.getActivityId());
		String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.ACTIVITY.getActivityAction());
		try {
			Object activityNodePayload = null;
			try {
				activityNodePayload =  activityNode.constructPayload();
			} catch (TransformerException e) {
				throw new ActivityServiceException(e);
			}
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			Response requestData = createData(requestUri, params, headers, activityNodePayload);
			activityNode.clearFieldsMap();
			return (ActivityNode) new ActivityNodeFeedHandler(this).createEntity(requestData);
		} catch (Exception e) {
			throw new ActivityServiceException(e);
		}
	}

	/**
	 * Method to get Activity node
	 * 
	 * @param activityNodeId
	 * @return ActivityNode
	 * @throws ActivityServiceException
	 */
	public ActivityNode getActivityNode(String activityNodeId) throws ActivityServiceException {
		if (null == activityNodeId){
			throw new ActivityServiceException(null, "Null activityNodeId");
		}
		String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.ACTIVITYNODE.getActivityAction());
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("activityNodeUuid", activityNodeId);
        	return (ActivityNode) getEntity(requestUri, params, new ActivityNodeFeedHandler(this));
		} catch (Exception e) {
			throw new ActivityServiceException(e);
		} 
	}

	/**
	 * Method to update Activity node
	 * 
	 * @param activityNode
	 * @throws ActivityServiceException
	 */
	public void updateActivityNode(ActivityNode activityNode) throws ActivityServiceException {
		if (null == activityNode){
			throw new ActivityServiceException(null, "Null activity node");
		}
		if (null == activityNode.getId()){
			throw new ActivityServiceException(null, "Null activity id");
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityNodeUuid", activityNode.getActivityId());
		String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.ACTIVITYNODE.getActivityAction());
		try {
			Object updateNodePayload = null;
			try {
				Map<String, Object> updatedFields = new HashMap<String, Object>();
				updatedFields.putAll(activityNode.getFieldsMap());
				activityNode.clearFieldsMap();
				ActivityNode updatedNode = new ActivityNode();
				activityNode.copyTo(updatedNode);
				updatedNode.addToFieldMap(updatedFields);
				updateNodePayload =  updatedNode.constructPayload();
			} catch (TransformerException e) {
				throw new ActivityServiceException(e);
			}
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			updateData(requestUri, params, headers, updateNodePayload, null);
			activityNode.clearFieldsMap();
		} catch (Exception e) {
			throw new ActivityServiceException(e);
		}
	}
	
	/**
	 * Method to restore Activity node
	 * 
	 * @param activityNodeId
	 * @throws ActivityServiceException
	 */
	public void restoreActivityNode(String activityNodeId) throws ActivityServiceException {
		if (StringUtil.isEmpty(activityNodeId)){
			throw new ActivityServiceException(null, "Null activityNodeId");
		}
		ActivityNode givenNode = getActivityNodeFromTrash(activityNodeId);
		ActivityNode restoreNode = new ActivityNode();
		Object nodePayload = null;
		if(givenNode.hasCategoryFlagDelete()) { 
			restoreNode = givenNode.copyTo(restoreNode);
			try {
				nodePayload =  restoreNode.constructPayload();
			} catch (TransformerException e) {
				throw new ActivityServiceException(e);
			}
		}
		String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.TRASHNODE.getActivityAction());
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("activityNodeUuid", activityNodeId);
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			updateData(requestUri, params, headers, nodePayload, null);
			restoreNode.clearFieldsMap();
		} catch (Exception e) {
			throw new ActivityServiceException(e);
		} 
	}
	
	/**
	 * Method to get Activity node from Trash
	 * 
	 * @param activityNodeId
	 * @return ActivityNode
	 * @throws ActivityServiceException
	 */
	public ActivityNode getActivityNodeFromTrash(String activityNodeId) throws ActivityServiceException {
		if (null == activityNodeId){
			throw new ActivityServiceException(null, "Null activityNodeId");
		}
		String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.TRASHNODE.getActivityAction());
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("activityNodeUuid", activityNodeId);
        	return (ActivityNode) getEntity(requestUri, params, new ActivityNodeFeedHandler(this));
		} catch (Exception e) {
			throw new ActivityServiceException(e);
		} 
	}

	/**
	 * Method to get Activity nodes from Trash
	 * 
	 * @param activityId
	 * @return ActivityNodeList
	 * @throws ActivityServiceException
	 */
	public ActivityNodeList getActivityNodesInTrash(String activityId) throws ActivityServiceException {
		if (null == activityId){
			throw new ActivityServiceException(null, "Null activityId");
		}
		String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.TRASH.getActivityAction());
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("activityUuid", activityId);
        	return (ActivityNodeList) getEntities(requestUri, params, new ActivityNodeFeedHandler(this));
		} catch (Exception e) {
			throw new ActivityServiceException(e);
		} 
	}

	/**
	 * Method to delete Activity node
	 * 
	 * @param activityNodeId
	 * @throws ActivityServiceException
	 */
	public void deleteActivityNode(String activityNodeId) throws ActivityServiceException {
		if (null == activityNodeId){
			throw new ActivityServiceException(null, "Null activityNodeId");
		}
		String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.ACTIVITYNODE.getActivityAction());
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("activityNodeUuid", activityNodeId);
        	deleteData(requestUri, params, null);
		} catch (Exception e) {
			throw new ActivityServiceException(e);
		} 
	}

	/**
	 * Method to get Tags of Activity
	 * 
	 * @param activityId
	 * @return TagList
	 * @throws ActivityServiceException
	 */
	public TagList getActivityTags(String activityId) throws ActivityServiceException {
		if (null == activityId){
			throw new ActivityServiceException(null, "Null activityId");
		}
		String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.TAGS.getActivityAction());
        try {
        	Map<String, String> params = new HashMap<String, String>();
        	params.put("activityUuid", activityId);
        	return (TagList) getEntities(requestUri, params, new TagFeedHandler(this));
        } catch (Exception e) {
            throw new ActivityServiceException(e);
        }
	}
	
	/**
	 * Method to get Tags of Activity Nodes
	 * 
	 * @param activityNodeId
	 * @return TagList
	 * @throws ActivityServiceException
	 */
	public TagList getActivityNodeTags(String activityNodeId) throws ActivityServiceException {
		if (null == activityNodeId){
			throw new ActivityServiceException(null, "Null activityNodeId");
		}
		String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.TAGS.getActivityAction());
        try {
        	Map<String, String> params = new HashMap<String, String>();
        	params.put("activityNodeUuid", activityNodeId);
        	return (TagList) getEntities(requestUri, params, new TagFeedHandler(this));
        } catch (Exception e) {
            throw new ActivityServiceException(e);
        }
	}

	/**
	 * Method to move an existing Activity Node to a Section
	 * 
	 * @param activityNodeId
	 * @param activityId
	 * @param sectionId
	 * @throws ActivityServiceException
	 */
	public void moveEntryToSection(String activityNodeId, String sectionId) throws ActivityServiceException {
		moveEntryToSection(activityNodeId, sectionId, null);
	}
	
	/**
	 * Method to move an existing Activity Node to a Section
	 * 
	 * @param activityNodeId
	 * @param activityId
	 * @param sectionId
	 * @param title
	 * @throws ActivityServiceException 
	 */
	public void moveEntryToSection(String activityNodeId, String sectionId, String title) throws ActivityServiceException {
		if (null == activityNodeId){
			throw new ActivityServiceException(null, "Null activityNodeId");
		}
		if (null == sectionId){
			throw new ActivityServiceException(null, "Null sectionId");
		}
		ActivityNode actNode = getActivityNode(activityNodeId);
		actNode.setInReplyTo(sectionId, actNode.getNodeUrl());
		if(StringUtil.isEmpty(title)) {
			title = actNode.getTitle();
		}
		actNode.setTitle(title);
		updateActivityNode(actNode);
	}
	
	/**
	 * Method to change the type of Activity Node
	 * 
	 * @param activityNodeId
	 * @param newType
	 * @param activityNode
	 * @throws ActivityServiceException
	 */
	public void changeEntryType(String activityNodeId, String newType, ActivityNode activityNode) throws ActivityServiceException {
		if (null == activityNodeId){
			throw new ActivityServiceException(null, "Null activityNodeId");
		}
		ActivityNode actNode = getActivityNode(activityNodeId);
		actNode.setEntryType(newType);
		updateActivityNode(actNode);
	}
	
	/**
	 * Method to change the type of Activity Node
	 * @param activityNodeId
	 * @param newType
	 * @param activityNodeOrJson
	 * @throws ActivityServiceException 
	 */
	public void changeEntryType(String activityNodeId, String newType) throws ActivityServiceException {
		changeEntryType(activityNodeId, newType, getActivityNode(activityNodeId));
	}
	
	/**
	 * Method to get all activity nodes of an activity
	 * @param activityId
	 * @return ActivityNodeList
	 * @throws ActivityServiceException
	 */
	public ActivityNodeList getActivityNodes(String activityId) throws ActivityServiceException {
		if (null == activityId){
			throw new ActivityServiceException(null, "Null activityId");
		}
		String requestUri = ActivityServiceUrlBuilder.populateURL(ActivityAction.ACTIVITY.getActivityAction());
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("activityUuid", activityId);
        	return (ActivityNodeList) getEntities(requestUri, params, new ActivityNodeFeedHandler(this));
		} catch (Exception e) {
			throw new ActivityServiceException(e);
		} 
	}
}
