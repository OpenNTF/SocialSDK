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

import java.util.ArrayList;
import java.util.List;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.JsonDataHandler;
import com.ibm.sbt.services.client.smartcloud.profiles.feedhandler.ProfileFeedHandler;
import com.ibm.sbt.services.client.smartcloud.profiles.model.ProfileJsonPath;

public class ProfileList extends EntityList<Profile> {
	
	public ProfileList(Response requestData, ProfileFeedHandler feedHandler) {
		super(requestData, feedHandler);
	}

	@Override
	public JsonJavaObject getData(){
		return (JsonJavaObject)super.getData();
	}
	
	@Override
	public ProfileService getService() {
		return (ProfileService)super.getService();
	}

	@Override
	public ProfileFeedHandler getFeedHandler() {
		return (ProfileFeedHandler)super.getFeedHandler();
	}
	
	@Override
	protected Profile getEntity(Object data){
		return (Profile)super.getEntity(data);
	}
	
	@Override
	protected ArrayList<Profile> createEntities() {
		JsonDataHandler dataHandler = new JsonDataHandler(getData());
		ArrayList<Profile> profiles = new ArrayList<Profile>();
		List<JsonJavaObject> entries = dataHandler.getEntries(ProfileJsonPath.Entry);
		for (JsonJavaObject entry: entries) {
			Profile profile = getEntity(entry);
			profiles.add(profile);
		}
		return profiles;
	}
	
	@Override
	public int getTotalResults() {
		JsonDataHandler dataHandler = new JsonDataHandler(getData());
		return (int) dataHandler.getAsFloat(ProfileJsonPath.TotalResults);
	}

	@Override
	public int getStartIndex() {
		JsonDataHandler dataHandler = new JsonDataHandler(getData());
		return dataHandler.getAsInt(ProfileJsonPath.StartIndex);
	}

	@Override
	public int getItemsPerPage() {
		JsonDataHandler dataHandler = new JsonDataHandler(getData());
		return dataHandler.getAsInt(ProfileJsonPath.ItemsPerPage);
	}

	@Override
	public int getCurrentPage() {
		// TODO Auto-generated method stub
		return 0;
	}
}
