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
 * Activity streams ASApplication class, allows user to choose Application type
 * @author Manish Kataria
 */
public enum ASApplication {
	// Possibile values : @communities,@tags,@people,@status
	
	COMMUNITIES("@communities"),
	TAGS("@tags"),
	PEOPLE("@people"),
	STATUS("@status"),
	ALL("@all"),
	NOAPP("NOAPP");
	
	String applicationType;
	ASApplication(String applicationType){
		this.applicationType = applicationType;
	}
	/**
	 * Wrapper method to return application type
	 * <p>
	 */
	public String getApplicationType(){return applicationType;}
}
