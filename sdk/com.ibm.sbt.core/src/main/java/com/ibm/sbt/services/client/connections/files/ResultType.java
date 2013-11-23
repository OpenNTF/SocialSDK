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
package com.ibm.sbt.services.client.connections.files;

/**
 * Files Result Type<br>
 * Enumeration which determines the Result Type to be used for Executing FileService API
 * 
 * @author Vimal Dhupar
 */
public enum ResultType {
	FEED("/feed"), MEDIA("/media"), ENTRY("/entry"), REPORTS("/reports"), NONCE("/nonce"), LOCK("/lock"), NULL(
			""), INTROSPECTION("/introspection"), ATOMSVC("/atomsvc");

	String	resultType;

	private ResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getResultType() {
		return resultType;
	}
}
