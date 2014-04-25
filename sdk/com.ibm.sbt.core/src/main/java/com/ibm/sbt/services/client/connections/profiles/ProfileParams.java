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

package com.ibm.sbt.services.client.connections.profiles;

import static com.ibm.sbt.services.client.base.CommonConstants.AT;

import com.ibm.sbt.services.client.base.NamedUrlPart;

/**
 * 
 * @author Carlos Manias
 *
 */
public enum ProfileParams {
	sourceId("sourceEmail", "sourceKey"), targetId("targetEmail", "targetKey"), userId("email", "userid"); 

	private final String keyParam;
	private final String emailParam;
	
	private ProfileParams(String emailParam, String keyParam){
		this.emailParam = emailParam;
		this.keyParam = keyParam;
	}
	
	public NamedUrlPart get(String id){
		String paramName = isEmail(id)?emailParam:keyParam;
		return new NamedUrlPart(name(), paramName+"="+id);
	}

	public String getParamName(String id){
		return isEmail(id)?emailParam:keyParam;
	}
	
	private static boolean isEmail(String id) {
		return (id == null) ? false : id.contains(AT);
	}
}
