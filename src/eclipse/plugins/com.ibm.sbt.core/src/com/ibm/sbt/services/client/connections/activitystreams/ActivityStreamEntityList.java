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

import java.util.ArrayList;
import java.util.List;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.JsonDataHandler;
import com.ibm.sbt.services.client.connections.activitystreams.model.ASJsonPath;


/**
 * @author Manish Kataria
 *
 */
public class ActivityStreamEntityList extends EntityList<ActivityStreamEntity> {
	
	public ActivityStreamEntityList(Response requestData, ActivityStreamFeedHandler feedHandler) {
		super(requestData, feedHandler);
	}

	@Override
	public JsonJavaObject getData(){
		return (JsonJavaObject)super.getData();
	}
	
	@Override
	public ActivityStreamService getService() {
		return (ActivityStreamService)super.getService();
	}

	@Override
	public ActivityStreamFeedHandler getFeedHandler() {
		return (ActivityStreamFeedHandler)super.getFeedHandler();
	}
	
	@Override
	protected ActivityStreamEntity getEntity(Object data){
		return (ActivityStreamEntity)super.getEntity(data);
	}
	
	@Override
	protected ArrayList<ActivityStreamEntity> createEntities() {
		JsonDataHandler dataHandler = new JsonDataHandler(getData());
		ArrayList<ActivityStreamEntity> activityStreams = new ArrayList<ActivityStreamEntity>();
		List<JsonJavaObject> entries = dataHandler.getEntries(ASJsonPath.Entry);
		for (JsonJavaObject entry: entries) {
			ActivityStreamEntity activityStream = getEntity(entry);
			activityStreams.add(activityStream);
		}
		return activityStreams;
	}
	
	@Override
	public int getTotalResults() {
		JsonDataHandler dataHandler = new JsonDataHandler(getData());
		return dataHandler.getAsInt("totalResults");
	}

	@Override
	public int getStartIndex() {
		JsonDataHandler dataHandler = new JsonDataHandler(getData());
		return dataHandler.getAsInt("startIndex");
	}

	@Override
	public int getItemsPerPage() {
		JsonDataHandler dataHandler = new JsonDataHandler(getData());
		return dataHandler.getAsInt("itemsPerPage");
	}

	@Override
	public int getCurrentPage() {
		// TODO Auto-generated method stub
		return 0;
	}
}
