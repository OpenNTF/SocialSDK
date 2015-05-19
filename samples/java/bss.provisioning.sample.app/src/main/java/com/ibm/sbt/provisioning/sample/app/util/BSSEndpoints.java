/*
 * © Copyright IBM Corp. 2014
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
package com.ibm.sbt.provisioning.sample.app.util;
/**
 * Each resource managed by the BSS API is represented by a REST endpoint through which that resource is manageble.
 * This <code>enum</code> is an abstraction for those endpoints
 * */
public enum BSSEndpoints {

	RES_ROOT("/", true),
	RES_CUSTOMER("/resource/customer", false),
	RES_SUBSCRIBER("/resource/subscriber", false), 
	RES_SUBSCRIPTION("/resource/subscription", false), 
	SER_AUTHENTICATION("/service/authentication", true), 
	SER_AUTHENTICATION_CHPWD("/service/authentication/changepassword", true), 
	SER_AUTHENTICATION_ROLELIST("/service/authentication/getrolelist", true);
	
	private String endpoint ;
  	private boolean sameWeight; // True if it all 3 methods have the same weight
	 
  	BSSEndpoints(String endpoint, boolean sameWeight){
		this.endpoint = endpoint ;
    	this.sameWeight = sameWeight;
	}
	
	/**
	 * This method return the String representation of the corresponding BSS REST endpoint
	 * <p>
	 * @return a String representing the corresponding BSS REST endpoint
	 */
	public String getEndpointString(){
		return endpoint ;
	}

	/**
   * This method returns true if all 3 methods have the same weight
   * <p>
   * 
   * @return a boolean value, true if all 3 methods have the same weight
   */
  public boolean hasSameWeight(){
    return this.sameWeight;
  }
}
