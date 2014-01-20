/*
 *  Copyright IBM Corp. 2012
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
package com.ibm.sbt.services.client.connections.files.model;

/**
 * FileRequestPayload represents the Payload which can be passed as parameters and the Request Body would be ,
 * hence, generated by the API itself. For the Updations which are not covered under this FileRequestPayload
 * File, User needs to provide the Document Request Body.
 * 
 * @author Vimal Dhupar
 */
public enum FileRequestPayload {
	VISIBILITY("visibility"), SUMMARY("summary"), LABEL("label"), COMMENT("comment");

	String	fileRequestPayload;

	private FileRequestPayload(String payload) {
		this.fileRequestPayload = payload;
	}

	public String getFileRequestPayload() {
		return fileRequestPayload;
	}
}
