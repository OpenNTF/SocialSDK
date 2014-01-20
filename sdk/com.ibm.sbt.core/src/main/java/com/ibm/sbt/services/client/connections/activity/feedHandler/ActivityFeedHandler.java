/*
 *  Copyright IBM Corp. 2013
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

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.activity.Activity;
import com.ibm.sbt.services.client.connections.activity.ActivityList;
import com.ibm.sbt.services.client.connections.activity.ActivityNode;
import com.ibm.sbt.services.client.connections.activity.ActivityService;


/**
 * Feed handler for Activity Service
 * 
 * @author Vimal Dhupar
 */

public class ActivityFeedHandler implements IFeedHandler  {
	
	private final ActivityService service;
	
	/**
	 * Constructor
	 * 
	 * @param service
	 */
	public ActivityFeedHandler(ActivityService service){
		this.service = service;
	}

	@Override
	public BaseEntity createEntity(Response response) {
		Node data = (Node)response.getData();
		return createEntityFromData(data);
	}

	@Override
	public Activity createEntityFromData(Object data) {
//		 XmlDataHandler handler = new XmlDataHandler((Node)data, ConnectionsConstants.nameSpaceCtx);
//       Activity activity = new Activity(service, handler);
//       return activity;
         XPathExpression xpath = (data instanceof Document) ? (XPathExpression)AtomXPath.singleEntry.getPath() : null;
         Activity activity = new Activity(service, (Node)data, ConnectionsConstants.nameSpaceCtx, xpath);
         return activity;
	}

	@Override
	public ActivityList createEntityList(Response requestData) {
		return new ActivityList((Response)requestData, this);
	}

	@Override
	public BaseService getService() {
		return service;
	}

}
