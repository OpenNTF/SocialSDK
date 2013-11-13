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

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.activity.model.ActivityXPath;
/**
 * Class representing the File Field
 * @author Vimal Dhupar
 *
 */
public class FileField extends Field{
	
	public FileField() {
		super("file");
	}
	
	public FileField(BaseService svc, DataHandler<?> handler) {
		super("file",svc, handler);
	}
	
	public FileField(String fileUrl, String fileType, int fileSize) {
		super("file");
		setFieldFileSize(fileSize);
		setFieldFileType(fileType);
		setFieldFileUrl(fileUrl);		
	}
	
	public String getFieldFileUrl() {
		return getAsString(ActivityXPath.fieldFileUrl);
	}
	
	public int getFieldFileSize() {
		return getAsInt(ActivityXPath.fieldFileSize);
	}
	
	public String getFieldFileType() {
		return getAsString(ActivityXPath.fieldFileType);
	}
	
	public void setFieldFileUrl(String fileUrl) {
		setAsString(ActivityXPath.fieldFileUrl, fileUrl);
	}
	
	public void setFieldFileSize(int size) {
		setAsString(ActivityXPath.fieldFileSize, "\"" + size + "\"");
	}
	
	public void setFieldFileType(String fileType) {
		setAsString(ActivityXPath.fieldFileType, fileType);
	}
}
