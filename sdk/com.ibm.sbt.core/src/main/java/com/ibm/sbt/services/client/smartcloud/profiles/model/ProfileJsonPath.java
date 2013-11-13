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
package com.ibm.sbt.services.client.smartcloud.profiles.model;

import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;

public enum ProfileJsonPath implements FieldEntry {
	Entry("entry"),
	Id("id"),
	ObjectId("objectId"),
	DisplayName("displayName"),
	EmailAddress("emailAddress"),
	ThumbnailUrl("photo"),
	Title("jobtitle"),
	OrgId("orgId"),
	PhoneNumbers("telephone"),			
	Address("address"),
	Country("country"),
	Department("org/name"),
	About("aboutMe"),
	ProfileUrl("profileUrl"),
	TotalResults("totalResults"),
	StartIndex("startIndex"),
	ItemsPerPage("itemsPerPage");
	
	private final String jsonPath;
	private ProfileJsonPath(final String path) {
		this.jsonPath = path;
	}
	
	@Override
	public String getPath(){
		return jsonPath;
	}
	
	@Override
	public String getName(){
		return this.name();
	}
}
