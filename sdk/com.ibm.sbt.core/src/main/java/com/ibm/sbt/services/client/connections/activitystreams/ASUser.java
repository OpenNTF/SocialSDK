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


package com.ibm.sbt.services.client.connections.activitystreams;
/**
 * Activity streams ASUser class, allows user to choose User type
 * @author Manish Kataria
 */
public enum ASUser {
	ME("@me"),
	PUBLIC("@public"),
	COMMUNITY("urn:lsid:lconn.ibm.com:communities.community:");
	
	String userType;
	ASUser(String userType){
		this.userType = userType;
	}
	
	/**
	 * Wrapper method to return user type
	 * <p>
	 */
	public String getUserType(){return userType;}

}
