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

package com.ibm.sbt.services.client.connections.blogs.model;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.common.Person;


/**
 * Contributor class for persisting Contributor information from Blogs Entry 
 * @author Swati Singh
 */


@Deprecated
public class Contributor extends Person {

	public Contributor(BaseService svc, DataHandler<?> dataHandler) {
		super(svc, dataHandler);
	}
	
//	private String name;
//	private String uid;
//	private String email;
//	private String state;
//	DataHandler<?> dataHandler;
//	public Contributor(DataHandler<?> dataHandler) {
//		this.dataHandler = dataHandler;
//	}
//	
//	public String getUid() {
//		return dataHandler.getAsString(BlogXPath.contributorUserid);
//	}
//
//	
//	public String getName() {
//		return dataHandler.getAsString(BlogXPath.contributorName);
//	}
//	
//	public String getEmail() {
//		return dataHandler.getAsString(BlogXPath.contributorEmail);
//	}
//
//
//	public String getState() {
//		return dataHandler.getAsString(BlogXPath.contributorUserState);
//	}


}
