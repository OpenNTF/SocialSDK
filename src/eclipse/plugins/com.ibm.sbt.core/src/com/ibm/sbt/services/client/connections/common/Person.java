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

package com.ibm.sbt.services.client.connections.common;

import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;

/**
 * @author Mario Duarte
 *
 */
public class Person extends BaseEntity {
	
	public Person(BaseService svc, DataHandler<?> dataHandler) {
		super(svc, dataHandler);
	}
	
	public void setUserId(String userId) {
		setAsString(AtomXPath.personUserid, userId);
	}
	
	public void setName(String name) {
		setAsString(AtomXPath.personName, name);
	}

	public void setEmail(String email) {
		setAsString(AtomXPath.personEmail, email);
	}

	public void setUserState(String userState) {
		setAsString(AtomXPath.personUserState, userState);
	}
	
	public String getEmail() {
		return getAsString(AtomXPath.personEmail);
	}

	public String getName() {
		return getAsString(AtomXPath.personName);
	}

	public String getState() {
		return getAsString(AtomXPath.personUserState);
	}

	public String getId() {
		return getAsString(AtomXPath.personUserid);
	}
}
