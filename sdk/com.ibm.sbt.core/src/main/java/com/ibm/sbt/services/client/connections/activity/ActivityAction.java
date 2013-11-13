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
/**
 * Ativity Action class used in Constructing Url 
 * @author Vimal Dhupar
 */
public enum ActivityAction {
	
	SERVICE("service"),
	ACTIVITIES("activities"), 
	COMPLETED("completed"),
	EVERYTHING("everything"),
	TODOS("todos"),
	TAGS("tags"),
	//"entrytemplates?activityUuid=<uuid>"
	TRASH("trash"),
	ACTIVITY("activity"),
	ACTIVITYNODE("activitynode"),
	TRASHNODE("trashednode"),
	ACL("acl");
	
	String actionItem;
	
	private ActivityAction(String actionItem) {
		this.actionItem = actionItem;
	}
	
	/**
	 * Wrapper method to return action item
	 * <p>
	 */
	public String getActivityAction(){
		return actionItem;
	}

}
