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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.activity.feedHandler.BookmarkFieldFeedHandler;
import com.ibm.sbt.services.client.connections.activity.feedHandler.DateFieldFeedHandler;
import com.ibm.sbt.services.client.connections.activity.feedHandler.FileFieldFeedHandler;
import com.ibm.sbt.services.client.connections.activity.feedHandler.PersonFieldFeedHandler;
import com.ibm.sbt.services.client.connections.activity.feedHandler.TextFieldFeedHandler;
import com.ibm.sbt.services.client.connections.activity.model.ActivityXPath;
import com.ibm.sbt.services.client.connections.activity.transformers.ActivityTransformer;

/**
 * Activity Node Model Class representing an Activity Node Object
 * @author Vimal Dhupar
 */


public class ActivityNode extends Activity {
	
	public FieldList fieldElements; 
	
	public ActivityNode() {
	}
	
	/**
	 * Constructor 
	 * @param service
	 * @param activityId
	 */
	public ActivityNode(ActivityService service, String activityId) {
		super.setService(service);
		super.setId(activityId);
	}
	
	/**
	 * Constructor 
	 * @param service
	 * @param handler
	 */
	public ActivityNode(ActivityService service, XmlDataHandler handler) {
		super(service, handler);
	}
	
	/**
	 * Method to specify the Position of the section within the activity.
	 * @see http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+
	 * 		Documentation#action=openDocument&res_title=Section_content_ic40a&content=pdcontent
	 * @param position
	 */
	public void setPosition(int position){
		String positionStr = String.valueOf(position);
		setAsString(ActivityXPath.Position, positionStr);
	}
	
	/**
	 * Method to set the "assigned to" parameter, to assign the entry to a person.
	 * @param assignedToName
	 * @param assignedToId
	 */
	public void setAssignedTo(String assignedToName, String assignedToId){
		if(StringUtil.isNotEmpty(assignedToId)) {
			setAsString(ActivityXPath.assignedToId, assignedToId); 
		}
		if(StringUtil.isNotEmpty(assignedToName)) {
			setAsString(ActivityXPath.assignedToName, assignedToName);
		}
	}
	
	/**
	 * Method to set the "in reply to" parameter, to set the parent entry.
	 * @param sectionId
	 * @param sectionUrl
	 */
	public void setInReplyTo(String sectionId, String sectionUrl){
		if(StringUtil.isNotEmpty(sectionId)) {
			setAsString(ActivityXPath.inReplyToId, sectionId); 
		}
		if(StringUtil.isNotEmpty(sectionUrl)) {
			setAsString(ActivityXPath.inReplyToUrl, sectionUrl); 
		}
		setAsString(ActivityXPath.inReplyToActivityId, getId());
	}
	
	/**
	 * Method to set the Icon
	 * @param icon
	 */
	public void setIcon(String icon) {
		setAsString(ActivityXPath.Icon, icon);
	}
	
	/**
	 * Method to set Fields. This method takes a list of Fields. 
	 * {@link Field}
	 * @param fieldList
	 */
	public void setFields(List<Field> fieldList){
		// before setting new fields, we need to count how many fields exist in the fieldmap.
		// then we need to add new fields, instead of overwriting them.
		int count = noOfFields(this.fields);
		if(!fieldList.isEmpty()){
			for(int i=0; i<fieldList.size(); i++){
				   fields.put("field" + (count+i) , fieldList.get(i));
			}
		}
	}
	
	/**
	 * @param flds
	 * @return
	 */
	private int noOfFields(Map<String, Object> flds) {
		int count = 0; 
		for(Map.Entry<String, Object> xmlEntry : flds.entrySet()){
			if(xmlEntry.getKey().contains("field")) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Method to set Fields. This method takes a single Field. 
	 * {@link Field}
	 * @param singleField
	 */
	public void setField(Field singleField){ 
		List<Field> list = new ArrayList<Field>();
		list.add(singleField);
		setFields(list);
	}
	
	/**
	 * Method returns the Name of the person to whom the Entry is assigned.
	 * @return String 
	 * @throws ActivityServiceException
	 */
	public String getAssignedToName() throws ActivityServiceException{
		return getAsString(ActivityXPath.assignedToName);
	}
	
	/**
	 * Method returns the Id of the person to whom the Entry is assigned.
	 * @return String
	 * @throws ActivityServiceException
	 */
	public String getAssignedToId() throws ActivityServiceException{
		return getAsString(ActivityXPath.assignedToId);
	}
	
	/**
	 * Method returns the Id of the parent entry.
	 * @return String
	 */
	public String getInReplyToId(){
		return getAsString(ActivityXPath.inReplyToId);
	}
	
	/**
	 * Method returns the Url of the parent entry.
	 * @return String 
	 */
	public String getInReplyToUrl(){
		return getAsString(ActivityXPath.inReplyToUrl);
	}
	
	/**
	 * Method returns the activity node's url.
	 * @return
	 */
	public String getNodeUrl() {
		return getAsString(ActivityXPath.nodeUrl);
	}
	
	private FieldList getFields(){
		return fieldElements;
	}
	
	/**
	 * Method returns a List of Text Fields.
	 * @return FieldList
	 */
	public FieldList getTextFields(){
		TextFieldFeedHandler txtHandler = new TextFieldFeedHandler(getService());
		Response response = new Response(getDataHandler().getData());
		return txtHandler.createEntityList(response);
	}
	
	/**
	 * Method returns a List of Date Fields.
	 * @return FieldList
	 */
	public FieldList getDateFields(){
		DateFieldFeedHandler dateHandler = new DateFieldFeedHandler(getService());
		Response response = new Response(getDataHandler().getData());
		return dateHandler.createEntityList(response);
	}
	
	/**
	 * Method returns a List of Person Fields.
	 * @return FieldList
	 */
	public FieldList getPersonFields(){
		PersonFieldFeedHandler personHandler = new PersonFieldFeedHandler(getService());
		Response response = new Response(getDataHandler().getData());
		return personHandler.createEntityList(response);
	}
	
	/**
	 * Method returns a List of Bookmark Fields.
	 * @return FieldList
	 */
	public FieldList getBookmarkFields(){
		BookmarkFieldFeedHandler bookmarkHandler = new BookmarkFieldFeedHandler(getService());
		Response response = new Response(getDataHandler().getData());
		return bookmarkHandler.createEntityList(response);
	}
	
	/**
	 * Method returns a List of File Fields.
	 * @return FieldList
	 */
	private FieldList getFileFields(){
		FileFieldFeedHandler fileHandler = new FileFieldFeedHandler(getService());
		Response response = new Response(getDataHandler().getData());
		return fileHandler.createEntityList(response);
	}
	
	@Override
	public Object constructPayload() throws TransformerException {
		ActivityTransformer activityTransformer = new ActivityTransformer();
//		if(!fields.containsKey("Category")) {
//			if(StringUtil.isEmpty(this.getCategory())) {
//				this.setCategory("DefaultActivity"); 
//			} else {
//				this.setCategory(this.getCategory());
//			}
//		}
//		if(!fields.containsKey("Content")) {
//			if(StringUtil.isEmpty(this.getContent())) {
//				this.setContent("DefaultActivity");
//			}  else {
//				this.setContent(this.getContent());
//			}
//		}
//		if(!fields.containsKey("Title")) {
//			if(StringUtil.isEmpty(this.getTitle())) {
//				this.setTitle("DefaultActivity"); 
//			} else {
//				this.setTitle(this.getTitle());
//			}
//		}
		return convertToXML(activityTransformer.transform(fields));
	} 
	
	/**
	 * @param restoreNode
	 * @return ActivityNode
	 * @throws ActivityServiceException
	 */
	public ActivityNode copyTo(ActivityNode restoreNode)
			throws ActivityServiceException {
		super.copyTo(restoreNode);
		restoreNode.setPosition(this.getPosition());
		restoreNode.setInReplyTo(this.getInReplyToId(), this.getInReplyToUrl());
		restoreNode.setAssignedTo(this.getAssignedToName(), this.getAssignedToId());
		
		FieldList textFields = this.getTextFields();
		FieldList dateFields = this.getDateFields();
		FieldList personFields = this.getPersonFields();
		FieldList bookmarkFields = this.getBookmarkFields();
		FieldList fileFields = this.getFileFields();
 		List<Field> listOfFields = new ArrayList<Field>();
 		for (Field fd : textFields) {
 			listOfFields.add(fd);
		}
 		for (Field fd : dateFields) {
 			listOfFields.add(fd);
		}
 		for (Field fd : personFields) {
 			listOfFields.add(fd);
		}
 		for (Field fd : bookmarkFields) {
 			listOfFields.add(fd);
		}
 		for (Field fd : fileFields) {
 			listOfFields.add(fd);
		}
 		restoreNode.setFields(listOfFields); 		
		return restoreNode;
	}

	/**
	 * @param updatedFields
	 */
	public void addToFieldMap(Map<String, Object> updatedFields) {
		Map<String, Object> newUpdatedFields = new HashMap<String, Object>();
		for(Map.Entry<String, Object> updatedEntry : updatedFields.entrySet()){
			String key = updatedEntry.getKey();
			Object value = updatedEntry.getValue();
			if(fields.containsKey(key)) {
				if(key.contains("field") || key.contains("tag") || key.contains("flag")) {
					newUpdatedFields.put(key + "1", value);
					continue;
				}
				else { 
					newUpdatedFields.put(key, value);
					fields.remove(key); 
				}
			} else {
				newUpdatedFields.put(key, value);
			}
		}
		fields.putAll(newUpdatedFields);
	}
	
	@Override
	public TagList getTags() throws ActivityServiceException {
		return this.getService().getActivityNodeTags(this.getActivityId());
	}
	
}

