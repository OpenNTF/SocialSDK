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
package com.ibm.sbt.provisioning.sample.app.weightedBSSCall;
/**
 * Each call to the BSS API has to be associated with a key needed for retrieving that call's weight 
 * from the {@link com.ibm.sbt.provisioning.sample.app.WeightManager#getWeightPerBSSCall()} <code>Map</code>.
 * The key is going to be a String made up by the endpoint as specified in the 
 * {@link com.ibm.sbt.provisioning.sample.app.util.BSSEndpoints} <code>enum</code>,
 * a column and the HTTP method being used ( in upper case letters ).
 * This interface is needed for giving the possibility to the implementing classes to specify that key.
 * */
public interface BSSCall {
	/**
	 * Each call to the BSS API has to be associated with a key needed for retrieving that call's weight 
	 * from the {@link com.ibm.sbt.provisioning.sample.app.WeightManager#getWeightPerBSSCall()} <code>Map</code>.
	 * The key is going to be a String made up by the endpoint as specified in the 
	 * {@link com.ibm.sbt.provisioning.sample.app.util.BSSEndpoints} <code>enum</code>,
	 * a column and the HTTP method being used ( in upper case letters ).
	 * This method gives the possibility to the implementing classes to specify that key.
	 * <p>
	 * @return a String representing the key associated with the BSS API call
	 */
	public String getKey();
}
