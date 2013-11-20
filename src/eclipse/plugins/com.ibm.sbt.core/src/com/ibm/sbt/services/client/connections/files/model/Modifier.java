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
package com.ibm.sbt.services.client.connections.files.model;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;

@Deprecated
public class Modifier extends Person {

	public Modifier(BaseService svc, DataHandler<?> dataHandler) {
		super(svc, dataHandler);
	}

//	public Modifier(BaseService svc, DataHandler<?> dataHandler) {
//		super(svc, dataHandler);
//	}
//	
//	public void setUserUuid(String userUuId) {
//		setAsString(FileEntryXPath.ModifierUserUuid, userUuId);
//	}
//	
//	public void setName(String name) {
//		setAsString(FileEntryXPath.ModifierName, name);
//	}
//
//	public void setEmail(String email) {
//		setAsString(FileEntryXPath.ModifierEmail, email);
//	}
//
//	public void setUserState(String userState) {
//		setAsString(FileEntryXPath.ModifierUserState, userState);
//	}
//	
//	public String getEmail() {
//		return dataHandler.getAsString(FileEntryXPath.ModifierEmail);
//	}
//
//	public String getName() {
//		return dataHandler.getAsString(FileEntryXPath.ModifierName);
//	}
//
//	public String getUserState() {
//		return dataHandler.getAsString(FileEntryXPath.ModifierUserState);
//	}
//
//	public String getUserUuid() {
//		return dataHandler.getAsString(FileEntryXPath.ModifierUserUuid);
//	}
}
