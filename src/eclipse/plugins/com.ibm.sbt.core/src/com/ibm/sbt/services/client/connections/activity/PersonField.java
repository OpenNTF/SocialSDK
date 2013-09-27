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
 * Class representing the Person Field
 * @author Vimal Dhupar
 *
 */
public class PersonField extends Field{
	
	public PersonField() {
		super("person");
	}
	
	public PersonField(BaseService svc, DataHandler<?> handler) {
		super("person", svc, handler);
	}
	
	public PersonField(String userid, String email) {
		super("person");
		setPersonUserid(userid);
		setPersonEmail(email);
	}
	
	public PersonField(String userid, String email, String name) {
		super("person");
		setPersonUserid(userid);
		setPersonEmail(email);
		setPersonName(name);
	}

	public void setPersonUserid(String userid) {
		setAsString(ActivityXPath.fieldPersonUserId, userid);
	}
	
	public void setPersonEmail(String email) {
		setAsString(ActivityXPath.fieldPersonEmail, email);
	}
	
	public void setPersonName(String userid) {
		setAsString(ActivityXPath.fieldPersonName, userid);
	}
	
	public String getPersonUserid() {
		return getAsString(ActivityXPath.fieldPersonUserId);
	}
	
	public String getPersonEmail() {
		return getAsString(ActivityXPath.fieldPersonEmail);
	}
	
	public String getPersonName() {
		return getAsString(ActivityXPath.fieldPersonName);
	}
}
