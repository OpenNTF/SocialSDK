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

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

/**
 * 
 * @author mwallace
 */
public class ActivityAttachment {

	private String name;
	private String fileName;
	private String mimeType;
	private Object content;
	
	/**
	 * Default constructor
	 */
	public ActivityAttachment() {
	}
	
	/**
	 * 
	 * @param name
	 * @param content
	 * @param mimeType
	 */
	public ActivityAttachment(String name, Object content, String mimeType) {
		this(name, name, content, mimeType);
	}
	
	/**
	 * 
	 * @param name
	 * @param fileName
	 * @param content
	 * @param mimeType
	 */
	public ActivityAttachment(String name, String fileName, Object content, String mimeType) {
		this.name = name;
		this.fileName = fileName;
		this.content = content;
		this.mimeType = mimeType;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * @return the content
	 */
	public Object getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(Object content) {
		this.content = content;
	}

	public MimeBodyPart toMimeBodyPart() throws MessagingException {
		MimeBodyPart bodyPart = new MimeBodyPart();
		bodyPart.setFileName(fileName);
		bodyPart.setContent(content, mimeType);
		bodyPart.setHeader("Content-Disposition", getDisposition());
		bodyPart.setHeader("Content-Type", mimeType);
		return bodyPart;
	}
	
	private String getDisposition() {
		return "attachment; filename=" + fileName + "; name=" + name;
	}

}
