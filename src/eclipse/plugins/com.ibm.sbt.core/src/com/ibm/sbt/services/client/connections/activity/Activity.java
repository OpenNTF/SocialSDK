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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.activity.feedHandler.MemberFeedHandler;
import com.ibm.sbt.services.client.connections.activity.model.ActivityXPath;
import com.ibm.sbt.services.client.connections.activity.transformers.ActivityTransformer;


/**
 * Model class : Creates Business objects from Activity Service
 * @author Vimal Dhupar
 */


public class Activity extends BaseEntity {
	private Member		author;
	private String id;
	
	public Activity() {
		// TODO Auto-generated constructor stub
	}
	
	public Activity(ActivityService service, String activityId) {
		this.setService(service);
		setId(activityId);
	}
	
	public Activity(String activityId) {
		setId(activityId);
	}
	
	public Activity(BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
	}
	
	@Override
	public XmlDataHandler getDataHandler(){
		return (XmlDataHandler)dataHandler;
	}
	
	public String getAsString(ActivityXPath fieldType){
		return dataHandler.getAsString(fieldType);
	}
	
	public Object constructPayload() throws TransformerException {
		ActivityTransformer activityTransformer = new ActivityTransformer();
		if(!fields.containsKey("Category"))
			this.setEntryType("activity");
		return convertToXML(activityTransformer.transform(fields));
	}
	
	public Activity save() throws ActivityServiceException {
		if(StringUtil.isEmpty(this.getId())){
			return this.getService().createActivity(this);
		}else{
			getService().updateActivity(this);
			return this.getService().getActivity(getId());
		}
	}
	
	public void setId(String id) {
		this.id = id;
		setAsString(ActivityXPath.Id, id);
	}

	public void setTitle(String title){
		setAsString(ActivityXPath.Title, title);		
	}
	
	public void setTags(List<String> tags) {
		if(!tags.isEmpty()){
			for (int i = 0; i < tags.size(); i++){
				   fields.put("tag" + i , tags.get(i));
			}
		}
	}
	
	public void setFlags(List<String> flags) {
		if(!flags.isEmpty()){
			for (int i = 0; i < flags.size(); i++){
				   fields.put("flag" + i , flags.get(i));
			}
		}
	}
	
	public void setDueDate(Date duedate){
		if(duedate != null) {
			setAsDate(ActivityXPath.DueDate.getName(), duedate);
		}
	}
	
	public void setGoal(String goal){
		setAsString(ActivityXPath.Content, goal);
	}
	
	/**
	 * Method fetches the Content Type
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
	
	public void setCommunityUuid(String communityUuid){
		if(StringUtil.isNotEmpty(communityUuid)) {
			setAsString(ActivityXPath.CommunityUuid, communityUuid);
		}
	}
	
	public void setCommunityLink(String communityLink){
		if(StringUtil.isNotEmpty(communityLink)) {
			setAsString(ActivityXPath.CommunityLink, communityLink);
		}
	}
	
	public void setEntryType(String type){
		if(StringUtil.isNotEmpty(type)) {
			setAsString(ActivityXPath.Category, type); 
		}
	}
	
	/**
	 * returns the complete id of the activity
	 * @return
	 */
	public String getId() {
		if(!StringUtil.isEmpty(this.id)) {
			return id;
		}
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
	
	public String getTitle() {
		String title = getAsString(ActivityXPath.Title);
		if(StringUtil.isEmpty(title)) {
			title = getAsString(ActivityXPath.ActivityNodeTitle);
		}
		return title;
	}
	
	public String getUpdated() {
		return getAsString(ActivityXPath.Updated);
	}
	
	public String getPublished() {
		return getAsString(ActivityXPath.Published);
	}
	
	public Member getAuthor() {
		MemberFeedHandler memHandler = new MemberFeedHandler(getService());
		Response response = new Response(getDataHandler().getData());
		return memHandler.createEntity(response);
	}
	
	public String getOwnerId() {
		return getAsString(ActivityXPath.OwnerId);
	}
	
	public int getPosition() {
		return getAsInt(ActivityXPath.Position);
	}
	
	public String getDepth() {
		return getAsString(ActivityXPath.Depth);
	}
	
	public String getPermissions() {
		return getAsString(ActivityXPath.Permissions);
	}
	
	public String getIcon() {
		return getAsString(ActivityXPath.Icon);
	}
	
	public String getContent() {
		return getAsString(ActivityXPath.Content);
	}
	
	public String getContentType() {
		return getAsString(ActivityXPath.ContentType);
	}
	
	public String getEntryType() {
		return getAsString(ActivityXPath.Category);
	}
	
	public String getCategoryFlagDelete() {
		return getAsString(ActivityXPath.CategoryFlagDelete);
	}
	
	public boolean hasCategoryFlagDelete() {
		if(StringUtil.isNotEmpty(this.getCategoryFlagDelete())) { 
			return true;
		}
		return false;
	}
	
	public String getCategoryFlagCompleted() {
		return getAsString(ActivityXPath.CategoryFlagCompleted);
	}
	
	public String getCategoryFlagTemplate() {
		return getAsString(ActivityXPath.CategoryFlagTemplate);
	}
	
	public String getCommunityUuid() {
		return getAsString(ActivityXPath.CommunityUuid);
	}
	
	public String getCommunityLink() {
		return getAsString(ActivityXPath.CommunityLink);
	}
	
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

	public TagList getTags() throws ActivityServiceException {
		return this.getService().getActivityTags(this.getActivityId());
	}
}

