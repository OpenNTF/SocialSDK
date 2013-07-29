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

package com.ibm.sbt.services.client.smartcloud.profiles;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.base.datahandlers.JsonDataHandler;

/**
 * This Class creates a Profile Object from Profile Service
 * @Represents SmartCloud Profile
 * @author Vimal Dhupar
 */
public class Profile extends BaseEntity {

	public Profile(BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
	}
	
	@Override
	public JsonDataHandler getDataHandler(){
		return (JsonDataHandler)dataHandler;
	}
	
	/**
	 * getThumbnailUrl
	 * 
	 * @return ThumbnailUrl
	 */
	public String getThumbnailUrl() {
		String thumbnailUrl = getAsString(ProfilesJsonPath.ThumbnailUrl);
		if(StringUtil.isNotEmpty(thumbnailUrl)) {
			thumbnailUrl = super.getService().getEndpoint().getUrl() +"/contacts/img/photos/"+ thumbnailUrl; 
		}
		return thumbnailUrl;
	}

	/**
	 * getDisplayName
	 * 
	 * @return name
	 */
	public String getDisplayName() {
		return getAsString(ProfilesJsonPath.DisplayName);
	}

	/**
	 * getId
	 * 
	 * @return id
	 */
	public String getId() {
		return getAsString(ProfilesJsonPath.Id);
	}

	/**
	 * getEmail
	 * 
	 * @return email
	 */
	public String getEmail() {
		return getAsString(ProfilesJsonPath.EmailAddress);
	}

	/**
	 * getAddress
	 * 
	 * @return
	 */
	public String getAddress() {
		return getAsString(ProfilesJsonPath.Address);
	}

	/**
	 * getDepartment
	 * 
	 * @return
	 */
	public String getDepartment() {
		return getAsString(ProfilesJsonPath.Department);
	}

	/**
	 * getTitle
	 * 
	 * @return
	 */
	public String getTitle() {
		return getAsString(ProfilesJsonPath.Title);
	}

	/**
	 * getProfileUrl
	 * 
	 * @return
	 */
	public String getProfileUrl() {
		return getAsString(ProfilesJsonPath.ProfileUrl);
	}

	/**
	 * getPhoneNumber
	 * 
	 * @return
	 */
	public String getPhoneNumber() {
		return getAsString(ProfilesJsonPath.PhoneNumbers);
	}
	
	/**
	 * getOrgId
	 * 
	 * @return
	 */
	public String getOrgId() { 
		return this.getAsString(ProfilesJsonPath.OrgId); 
	}
	
	/**
	 * getCountry
	 * 
	 * @return
	 */
	public String getCountry() {
		return getAsString(ProfilesJsonPath.Country);
	}

	/**
	 * getAboutMe
	 * 
	 * @return
	 */
	public String getAboutMe() {
		return getAsString(ProfilesJsonPath.About);
	}
}