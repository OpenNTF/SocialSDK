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

	public ActivityNode(ActivityService service, String activityId) {
		super.setService(service);
		super.setId(activityId);
	}
	
	public ActivityNode(ActivityService service, XmlDataHandler handler) {
		super(service, handler);
	}
	
	public void setPosition(int position){
		String positionStr = String.valueOf(position);
		setAsString(ActivityXPath.Position, positionStr);
	}
	
	public void setAssignedTo(String assignedToName, String assignedToId){
		if(StringUtil.isNotEmpty(assignedToId)) {
			setAsString(ActivityXPath.assignedToId, assignedToId); 
		}
		if(StringUtil.isNotEmpty(assignedToName)) {
			setAsString(ActivityXPath.assignedToName, assignedToName);
		}
	}
	
	public void setInReplyTo(String sectionId, String sectionUrl){
		if(StringUtil.isNotEmpty(sectionId)) {
			setAsString(ActivityXPath.inReplyToId, sectionId); 
		}
		if(StringUtil.isNotEmpty(sectionUrl)) {
			setAsString(ActivityXPath.inReplyToUrl, sectionUrl); 
		}
		setAsString(ActivityXPath.inReplyToActivityId, getId());
	}
	
	public void setIcon(String icon) {
		setAsString(ActivityXPath.Icon, icon);
	}
	
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
	
	private int noOfFields(Map<String, Object> flds) {
		int count = 0; 
		for(Map.Entry<String, Object> xmlEntry : flds.entrySet()){
			if(xmlEntry.getKey().contains("field")) {
				count++;
			}
		}
		return count;
	}

	public void setField(Field singleField){ 
		List<Field> list = new ArrayList<Field>();
		list.add(singleField);
		setFields(list);
	}
	
	public String getAssignedToName() throws ActivityServiceException{
		return getAsString(ActivityXPath.assignedToName);
	}
	
	public String getAssignedToId() throws ActivityServiceException{
		return getAsString(ActivityXPath.assignedToId);
	}
	
	public String getInReplyToId(){
		return getAsString(ActivityXPath.inReplyToId);
	}
	
	public String getInReplyToUrl(){
		return getAsString(ActivityXPath.inReplyToUrl);
	}
	
	public String getNodeUrl() {
		return getAsString(ActivityXPath.nodeUrl);
	}
	
	private FieldList getFields(){
		return fieldElements;
	}
	
	public FieldList getTextFields(){
		TextFieldFeedHandler txtHandler = new TextFieldFeedHandler(getService());
		Response response = new Response(getDataHandler().getData());
		return txtHandler.createEntityList(response);
	}
	
	public FieldList getDateFields(){
		DateFieldFeedHandler dateHandler = new DateFieldFeedHandler(getService());
		Response response = new Response(getDataHandler().getData());
		return dateHandler.createEntityList(response);
	}
	
	public FieldList getPersonFields(){
		PersonFieldFeedHandler personHandler = new PersonFieldFeedHandler(getService());
		Response response = new Response(getDataHandler().getData());
		return personHandler.createEntityList(response);
	}
	
	public FieldList getBookmarkFields(){
		BookmarkFieldFeedHandler bookmarkHandler = new BookmarkFieldFeedHandler(getService());
		Response response = new Response(getDataHandler().getData());
		return bookmarkHandler.createEntityList(response);
	}
	
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

