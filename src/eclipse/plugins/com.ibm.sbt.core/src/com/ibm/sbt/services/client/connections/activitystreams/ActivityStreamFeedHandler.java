/*
 * � Copyright IBM Corp. 2013
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

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.JsonDataHandler;
import com.ibm.sbt.services.client.connections.activitystreams.model.ActivityStreamEntity;


/**
 * 
 * @author Carlos Manias
 *
 */
public class ActivityStreamFeedHandler implements IFeedHandler {

	private final ActivityStreamService service;
	
	/**
	 * Constructor
	 * 
	 * @param service
	 */
	public ActivityStreamFeedHandler(ActivityStreamService service){
		this.service = service;
	}
	
	/**
	 * @param requestData
	 * @return activityStream
	 */
	@Override
	public ActivityStreamEntity createEntity(Response requestData) {
		JsonJavaObject data = (JsonJavaObject)requestData.getData();
		return createEntityFromData(data);
	}
	
	/**
	 * @param data object
	 * @return activityStream
	 */
	@Override
	public ActivityStreamEntity createEntityFromData(Object data) {
		JsonDataHandler handler = new JsonDataHandler((JsonJavaObject)data);
		ActivityStreamEntity activityStream  = new ActivityStreamEntity(service, handler);
		return activityStream;
	}

	/**
	 * @param data object
	 * @return Collection of activityStreams
	 */
	@Override
	public ActivityStreamEntityList createEntityList(Response requestData) {
		return new ActivityStreamEntityList((Response)requestData, this);
	}

	/**
	 * @return activityStreamService
	 */
	@Override
	public ActivityStreamService getService() {
		return service;
	}

}
