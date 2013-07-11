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

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;

/**
 * Actor class for persisting Attachment information from Activity Stream Entry 
 * @author Manish Kataria
 */

public class Attachment {
	
	private boolean isImage;
	private String summary;
	private String id;
	private String published;
	private String url;
	private String displayName;
	private Image image;
	private String AuthorName;
	private String AuthorId;
	private String AuthorType;
	private String AuthorUrl;
	
	public Attachment(){
		
	}
	
	public Attachment(DataHandler<?> dataHandler){
		JsonJavaObject attachmentObject = (JsonJavaObject) (dataHandler.getEntries(ASJsonPath.Attachments.getPath())).get(0);
		setSummary(attachmentObject.getString(ASJsonPath.AttachmentSummary.getPath()));
		setId(attachmentObject.getString(ASJsonPath.AttachmentId.getPath()));
		setDisplayName(attachmentObject.getString(ASJsonPath.AttachmentDisplayName.getPath()));
		setPublished(attachmentObject.getString(ASJsonPath.AttachmentPublished.getPath()));
		setUrl(attachmentObject.getString(ASJsonPath.AttachmentUrl.getPath()));
//		setAuthor(new Actor(dataHandler));
		JsonJavaObject imageObject = attachmentObject.getJsonObject(ASJsonPath.AttachmentImage.getPath());
		if(StringUtil.isNotEmpty(imageObject.getString(ASJsonPath.AttachmentImageUrl.getPath()))){
			setIsImage(true);
			setImage(new Image(dataHandler));
		}
		
		JsonJavaObject authorObject = attachmentObject.getJsonObject(ASJsonPath.AttachmentActor.getPath());
		setAuthorId(authorObject.getString(ASJsonPath.AttachmentActorId.getPath()));
		setAuthorName(authorObject.getString(ASJsonPath.AttachmentActorName.getPath()));
		setAuthorType(authorObject.getString(ASJsonPath.ReplyAuthorObjectType.getPath()));
		setAuthorUrl(authorObject.getString(ASJsonPath.AttachmentActorUrl.getPath()));
	}

	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public boolean isImage() {
		return isImage;
	}
	public void setIsImage(boolean isImage) {
		this.isImage = isImage;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
/*	public Actor getAuthor() {
		return author;
	}
	public void setAuthor(Actor author) {
		this.author = author;
	}*/
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getAuthorName() {
		return AuthorName;
	}

	public void setAuthorName(String authorName) {
		AuthorName = authorName;
	}

	public String getAuthorId() {
		return AuthorId;
	}

	public void setAuthorId(String authorId) {
		AuthorId = authorId;
	}

	public String getAuthorType() {
		return AuthorType;
	}

	public void setAuthorType(String authorType) {
		AuthorType = authorType;
	}
	public String getAuthorUrl() {
		return AuthorUrl;
	}

	public void setAuthorUrl(String authorUrl) {
		AuthorUrl = authorUrl;
	}
}
