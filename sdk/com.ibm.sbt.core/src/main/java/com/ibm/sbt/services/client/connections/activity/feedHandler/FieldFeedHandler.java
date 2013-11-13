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
package com.ibm.sbt.services.client.connections.activity.feedHandler;

import org.w3c.dom.Node;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.connections.activity.ActivityService;
import com.ibm.sbt.services.client.connections.activity.Field;
import com.ibm.sbt.services.client.connections.activity.FieldList;

/**
 * Feed Handler for Fields
 * @author Vimal Dhupar
 *
 */
public class FieldFeedHandler implements IFeedHandler {

	protected final ActivityService service;

	/**
	 * Constructor
	 *
	 * @param service
	 */
	public FieldFeedHandler(ActivityService service){
		this.service = service;
	}

	/**
	 * @param requestData
	 * @return Field
	 */
	@Override
	public Field createEntity(Response requestData) {
		Node data = (Node)requestData.getData();
		return createEntityFromData(data);
	}

	/**
	 * @param data object
	 * @return Field
	 */
	@Override
	public Field createEntityFromData(Object data) {
		Field field = new Field() {	};
		return field;
	}

	/**
	 * @param data object
	 * @return FieldList
	 */
	@Override
	public FieldList createEntityList(Response requestData) {
		return new FieldList((Response)requestData, this);
	}

	/**
	 * @return ActivityService
	 */
	@Override
	public ActivityService getService() {
		return service;
	}

}