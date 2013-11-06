/*
 * © Copyright IBM Corp. 2012
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

package com.ibm.sbt.services.client.connections.profiles;

public enum ProfileType {

	/*
	 *  Scope for Url Construction for Profiles
	 */

	GETPROFILE("atom/profile.do"),
	DELETEPROFILE("atom/profileEntry.do")  ,
	ADDPROFILE("atom/profiles.do")  ,
	UPDATEPROFILE("atom/profileEntry.do"),
	UPDATEPROFILEPHOTO("photo.do"),
	CONNECTIONS("atom/connections.do"),
	CONNECTION("atom/connection.do"),
	REPORTINGCHAIN("atom/reportingChain.do"),
	DIRECTREPORTS("atom/peopleManaged.do"),
	CONNECTIONS_IN_COMMON("atom/connectionsInCommon.do"),
	SEARCH("atom/search.do"),
	MESSAGES_ALL("atom/mv/theboard/entries/all.do"),
	MESSAGE_BOARD_ENTRIES("atom/mv/theboard/entries.do"),
	MESSAGE_BOARD_COMMENTS("atom/mv/theboard/comments.do"),
	TAGS("atom/profileTags.do"),
	MESSAGES_COLLEAGUES("atom/mv/theboard/entries/related.do");
	
	private String proType;
	private ProfileType(String proType) {
		this.proType = proType;
	}
	public String getProfileType(){return proType;}

}
