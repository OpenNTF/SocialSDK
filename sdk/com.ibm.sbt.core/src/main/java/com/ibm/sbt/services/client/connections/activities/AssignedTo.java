/*
 * � Copyright IBM Corp. 2014
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

import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;

/**
 * @author mwallace
 *
 */
public class AssignedTo extends BaseEntity {

	/**
	 * Default constructor
	 */
	public AssignedTo() {
	}

	/**
	 * Default constructor
	 */
	public AssignedTo(String name, String userid) {
		setName(name);
		setUserid(userid);
	}

	/**
	 * Construct InReplyTo based on the specified node
	 * 
	 * @param service
	 * @param dataHandler
	 */
	public AssignedTo(BaseService svc, DataHandler<?> dataHandler) {
		super(svc, dataHandler);
	}
	
	/**
	 * 
	 * @return name
	 */
	public String getName() {
		return getAsString(ActivityXPath.attr_name);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		setAsString(ActivityXPath.attr_userid, name);
	}
	
	/**
	 * 
	 * @return userid
	 */
	public String getUserid() {
		return getAsString(ActivityXPath.attr_userid);
	}
	
	/**
	 * 
	 * @param userid
	 */
	public void setUserid(String userid) {
		setAsString(ActivityXPath.attr_userid, userid);
	}
	
}
