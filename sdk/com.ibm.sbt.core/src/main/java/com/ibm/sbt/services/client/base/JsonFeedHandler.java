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
package com.ibm.sbt.services.client.base;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.JsonEntityList;

/**
 * @author mwallace
 *
 */
public class JsonFeedHandler<T extends JsonEntity> implements IFeedHandler<T> {

	private BaseService service;
	private String entitiesPath;
	
	public JsonFeedHandler(BaseService service, String entitiesPath) {
		this.service = service;
		this.entitiesPath = entitiesPath;
	}
	
	@Override
	public T createEntity(Response dataHolder) {
		JsonJavaObject data = (JsonJavaObject)dataHolder.getData();
		return createEntityFromData(data);
	}

	@Override
	public T createEntityFromData(Object data) {
		return newEntity(service, (JsonJavaObject)data);
	}

	@Override
	public EntityList<T> createEntityList(Response dataHolder) {
		return new JsonEntityList<T>(dataHolder, this, this.entitiesPath);
	}

	@Override
	public BaseService getService() {
		return service;
	}
	
	@SuppressWarnings("unchecked")
	protected T newEntity(BaseService service, JsonJavaObject jsonObject) {
		return (T)new JsonEntity(service, jsonObject);
	}

}
