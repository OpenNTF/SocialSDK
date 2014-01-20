/*
 *  Copyright IBM Corp. 2013
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

import com.ibm.sbt.services.client.base.datahandlers.DataHandler;


/**
 * Actor class for persisting Author information from Activity Stream Entry 
 * @author Manish Kataria
 */

public class Actor {
	private String name;
	private String uid;
	private String type;
	
	public Actor() {
		
	}
	
	public Actor(DataHandler<?> dataHandler) {
		setName(dataHandler.getAsString(ASJsonPath.ActorName));
		setType(dataHandler.getAsString(ASJsonPath.ActorType));
		setUid(dataHandler.getAsString(ASJsonPath.ActorUid));
	}
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
