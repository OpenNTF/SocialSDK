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
package com.ibm.sbt.services.client.smartcloud.profiles.feedhandler;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.JsonDataHandler;
import com.ibm.sbt.services.client.smartcloud.profiles.Profile;
import com.ibm.sbt.services.client.smartcloud.profiles.ProfileList;
import com.ibm.sbt.services.client.smartcloud.profiles.ProfileService;
import com.ibm.sbt.services.client.smartcloud.profiles.model.ProfileJsonPath;

public class ProfileFeedHandler implements IFeedHandler{
	private final ProfileService service;

	/**
	 * Constructor
	 * 
	 * @param service
	 */
	public ProfileFeedHandler(ProfileService service){
		this.service = service;
	}
	
	/**
	 * @param requestData
	 * @return Profile
	 */
	@Override
	public Profile createEntity(Response requestData) {
		JsonJavaObject data = (JsonJavaObject)requestData.getData();
		JsonDataHandler handler = new JsonDataHandler(data);
		data = handler.getEntry(ProfileJsonPath.Entry.getPath());
		return createEntityFromData(data);
	}
	
	/**
	 * @param data object
	 * @return Profile
	 */
	@Override
	public Profile createEntityFromData(Object data) {
		JsonDataHandler handler = new JsonDataHandler((JsonJavaObject)data);
		Profile profile  = new Profile(service, handler);
		return profile;
	}

	/**
	 * @param data object
	 * @return Collection of profiles
	 */
	@Override
	public ProfileList createEntityList(Response requestData) {
		return new ProfileList((Response)requestData, this);
	}

	/**
	 * @return profileService
	 */
	@Override
	public ProfileService getService() {
		return service;
	}
}
