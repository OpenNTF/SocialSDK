/*
 *  Copyright IBM Corp. 2013
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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.activity.feedHandler.MemberFeedHandler;
import com.ibm.sbt.services.client.connections.activity.model.ActivityXPath;
import com.ibm.sbt.services.client.connections.activity.transformers.ActivityTransformer;


/**
 * Model class : Creates Business objects from Activity Service
 * @author Vimal Dhupar
 */


public class Activity extends AtomEntity {
	private Member		author;
	
	/**
	 * Default Constructor
	 */
	public Activity() {
	}
	
	/**
	 * Construct an Activity instance.
	 * @param service
	 * @param node
	 * @param namespaceCtx
	 * @param xpathExpression
	 */
	public Activity(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, new XmlDataHandler(node, namespaceCtx, xpathExpression));
	}
	
	/**
	 * Construct an Activity instance.
	 * @param service
	 * @param activityId
	 */
	public Activity(ActivityService service, String activityId) {
		this.setService(service);
		setId(activityId);
	}
	
	/**
	 * Construct an Activity instance.
	 * @param service
	 */
	public Activity(ActivityService service) {
		this.setService(service);
	}
	
	/**
	 * Construct an Activity instance.
	 * @param activityId
	 */
	public Activity(String activityId) {
		setId(activityId);
	}
	
	/**
	 * Construct an Activity instance.
	 * @param svc
	 * @param handler
	 */
	public Activity(BaseService svc, XmlDataHandler handler) {
		super(svc,handler);
	}
	
	/**
	 * Method used by Activity Service to construct the Payload for Activity Object
	 * @return Object
	 * @throws TransformerException
	 */
	public Object constructPayload() throws TransformerException {
		ActivityTransformer activityTransformer = new ActivityTransformer();
		if(!fields.containsKey("Category"))
			this.setEntryType("activity");
		return convertToXML(activityTransformer.transform(fields));
	}
	
	/**
	 * Method to save the Activity. Can be used to create an Activity or to save any modifications done to the Activity.
	 * @return Activity
	 * @throws ActivityServiceException
	 */
	public Activity save() throws ActivityServiceException {
		if(StringUtil.isEmpty(this.getId())){
			return this.getService().createActivity(this);
		}else{
			getService().updateActivity(this);
			return this.getService().getActivity(getId());
		}
	}
	
	/**
	 * Method to set the Id of the Activity.
	 * @param id
	 */
	public void setId(String id) {
		setAsString(ActivityXPath.Id, id);
	}

	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.AtomEntity#setTitle(java.lang.String)
	 */
	public void setTitle(String title){
		setAsString(ActivityXPath.Title, title);		
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.AtomEntity#setTags(java.util.List)
	 */
	public void setTags(List<String> tags) {
		if(!tags.isEmpty()){
			for (int i = 0; i < tags.size(); i++){
				   fields.put("tag" + i , tags.get(i));
			}
		}
	}
	
	/**
	 * Method to set the Flags on an Activity
	 * @param flags
	 */
	public void setFlags(List<String> flags) {
		if(!flags.isEmpty()){
			for (int i = 0; i < flags.size(); i++){
				   fields.put("flag" + i , flags.get(i));
			}
		}
	}
	
	/**
	 * Method to set the due date
	 * @param duedate
	 */
	public void setDueDate(Date duedate){
		if(duedate != null) {
			setAsDate(ActivityXPath.DueDate.getName(), duedate);
		}
	}
	
	/**
	 * Method to set the Goal 
	 * @param goal
	 */
	public void setGoal(String goal){
		setAsString(ActivityXPath.Content, goal);
	}
	
	/**
	 * Method to set the Content 
	 * @param content
	 */
	public void setContent(String content){
		setGoal(content);
	}
	
	/**
	 * Method allows you to set the Content Type while creating an Activity(Node)
	 * @param type
	 */
	public void setContentType(String type){
		if(type != null) {
			setAsString(ActivityXPath.ContentType, type);
		}
	}
	
	/**
	 * Method to set the Community Uuid
	 * @param communityUuid
	 */
	public void setCommunityUuid(String communityUuid){
		if(StringUtil.isNotEmpty(communityUuid)) {
			setAsString(ActivityXPath.CommunityUuid, communityUuid);
		}
	}
	
	/**
	 * Method to set the Community Link
	 * @param communityLink
	 */
	public void setCommunityLink(String communityLink){
		if(StringUtil.isNotEmpty(communityLink)) {
			setAsString(ActivityXPath.CommunityLink, communityLink);
		}
	}
	
	/**
	 * Method to set the Entry Type. This is required while creating an Activity and Activity Node.
	 * Entry Type can be Activity, Chat, Section, Todo etc.
	 * @param type
	 */
	public void setEntryType(String type){
		if(StringUtil.isNotEmpty(type)) {
			setAsString(ActivityXPath.Category, type); 
		}
	}
	
	/**
	 * returns the complete id of the activity. 
	 * @return
	 */
	public String getId() {
		// This method is intentionally overriden in Activity, 
		// due to the reason that the Id being fetched here is for Activity , 
		// and in other case of Activity Node, if the object represents an Activity Node. 
		// The Xpath given for AtomEntity representation of getId causes a regression and Null id is returned. 
		// Thus we are Using separate implementation here.
		return getAsString(ActivityXPath.Id); 
	}
	
	/**
	 * returns the extracted activity Id from the Id string
	 * @return
	 */
	public String getActivityId() {
		String id = getId();
		int startOfId = id.lastIndexOf(":");
		if(startOfId == -1)
			return id;
		return id.substring(startOfId+1);
	}
	
	@Override
	public String getTitle() {
		// This method is intentionally overriden in Activity, 
		// due to the reason that the Title being fetched here is for Activity , 
		// and in other case of Activity Node, if the object represents an Activity Node. 
		// The Xpath given for AtomEntity representation of getTitle causes a regression and Null id is returned. 
		// Thus we are Using separate implementation here.
		String title = getAsString(ActivityXPath.Title);
		if(StringUtil.isEmpty(title)) {
			title = getAsString(ActivityXPath.ActivityNodeTitle);
		}
		return title;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.AtomEntity#getUpdated()
	 */
	public Date getUpdated() {
		// Using overridden implementation as using AtomEntity representation causes a regression in these values. 
		// Null values get returned for some instances where Activity Node gets returned.
		return getAsDate(ActivityXPath.Updated);
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.AtomEntity#getPublished()
	 */
	public Date getPublished() {
		// Using overridden implementation as using AtomEntity representation causes a regression in these values. 
		// Null values get returned for some instances where Activity Node gets returned.
		return getAsDate(ActivityXPath.Published);
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.AtomEntity#getAuthor()
	 */
	public Member getAuthor() {
		MemberFeedHandler memHandler = new MemberFeedHandler(getService());
		Response response = new Response(getDataHandler().getData());
		return memHandler.createEntity(response);
	}
	
	/**
	 * Method to get the Owner Id of the activity
	 * @return
	 */
	public String getOwnerId() {
		return getAsString(ActivityXPath.OwnerId);
	}
	
	/**
	 * Method to fetch the Position
	 * @return
	 */
	public int getPosition() {
		return getAsInt(ActivityXPath.Position);
	}
	
	/**
	 * Method to fetch the Depth
	 * @return
	 */
	public String getDepth() {
		return getAsString(ActivityXPath.Depth);
	}
	
	/**
	 * Method to fetch the permissions
	 * @return
	 */
	public String getPermissions() {
		return getAsString(ActivityXPath.Permissions);
	}
	
	/**
	 * Method to fetch the Icon
	 * @return
	 */
	public String getIcon() {
		return getAsString(ActivityXPath.Icon);
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.AtomEntity#getContent()
	 */
	public String getContent() {
		// Using overridden implementation as using AtomEntity representation causes a regression in these values. 
		// Null values get returned for some instances where Activity Node gets returned.
		return getAsString(ActivityXPath.Content);
	}
	
	/**
	 * Method to fetch the Content Type
	 * @return
	 */
	public String getContentType() {
		return getAsString(ActivityXPath.ContentType);
	}
	
	/**
	 * Method to fetch the Entry Type of the Activity
	 * @return
	 */
	public String getEntryType() {
		return getAsString(ActivityXPath.Category);
	}
	
	/**
	 * Method to fetch the Category Flag for delete
	 * @return
	 */
	public String getCategoryFlagDelete() {
		return getAsString(ActivityXPath.CategoryFlagDelete);
	}
	
	/**
	 * Method to check if the Category flag delete is present or not.  
	 * @return
	 */
	public boolean hasCategoryFlagDelete() {
		if(StringUtil.isNotEmpty(this.getCategoryFlagDelete())) { 
			return true;
		}
		return false;
	}
	
	/**
	 * Method to fetch the Category Flag for Completed
	 * @return
	 */
	public String getCategoryFlagCompleted() {
		return getAsString(ActivityXPath.CategoryFlagCompleted);
	}
	
	/**
	 * Method to fetch the Category Flag for Template
	 * @return
	 */
	public String getCategoryFlagTemplate() {
		return getAsString(ActivityXPath.CategoryFlagTemplate);
	}
	
	/**
	 * MEthod to fetch the Community Uuid
	 * @return
	 */
	public String getCommunityUuid() {
		return getAsString(ActivityXPath.CommunityUuid);
	}
	
	/**
	 * Method to fetch the Community Link
	 * @return
	 */
	public String getCommunityLink() {
		return getAsString(ActivityXPath.CommunityLink);
	}
	
	/**
	 * Method to fetch the due date
	 * @return
	 */
	public Date getDueDate() {
//		if(StringUtil.isNotEmpty(getAsString(ActivityXPath.DueDate)))
//			return getAsDate(ActivityXPath.DueDate);
//		return null;
		return getAsDate(ActivityXPath.DueDate);
	}
	
	@Override
	public ActivityService getService() {
		return (ActivityService) super.getService();
	}
	
	/**
     * convertToXML
     * <p>
     * Utility method to construct XML payload
     * 
     * @param requestBody
     * @return Document
     */
    public Document convertToXML(String requestBody) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(requestBody.toString())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return document;
    }

	/**
	 * Utility Method to copy the Activity. This is done before updations and restore operations. 
	 * @param restoreActivity
	 * @return Activity
	 * @throws ActivityServiceException
	 */
	public Activity copyTo(Activity restoreActivity) throws ActivityServiceException {
		restoreActivity.setId(this.getActivityId());
		restoreActivity.setEntryType(this.getEntryType());
		restoreActivity.setGoal(this.getContent());
		restoreActivity.setContentType(this.getContentType());
		restoreActivity.setTitle(this.getTitle());
		restoreActivity.setCommunityUuid(this.getCommunityUuid());
		restoreActivity.setCommunityLink(this.getCommunityLink());
		
		TagList tags = this.getTags();
		List<String> tagList = new ArrayList<String>();
		for( Tag tag : tags) {
			tagList.add(tag.getTerm());
		}
		restoreActivity.setTags(tagList);
		List<String> flags = new ArrayList<String>();
		if(StringUtil.isNotEmpty(this.getCategoryFlagCompleted())) {
			flags.add(this.getCategoryFlagCompleted());
		}
		if(StringUtil.isNotEmpty(this.getCategoryFlagTemplate())) {
			flags.add(this.getCategoryFlagTemplate());
		}
		restoreActivity.setFlags(flags);
		restoreActivity.setDueDate(this.getDueDate());
		return restoreActivity;
	}

	public TagList getTags()  throws ActivityServiceException{
		return this.getService().getActivityTags(this.getActivityId());
	}
}

