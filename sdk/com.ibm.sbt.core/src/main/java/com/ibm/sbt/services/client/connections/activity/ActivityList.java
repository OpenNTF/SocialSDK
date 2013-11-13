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
package com.ibm.sbt.services.client.connections.activity;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.activity.feedHandler.ActivityFeedHandler;
import com.ibm.sbt.services.client.connections.activity.model.ActivityXPath;

/**
 * Activity List Class used for creating List of Activities
 * @author Vimal Dhupar
 *
 */
public class ActivityList extends EntityList<Activity> {

	public ActivityList(Response requestData, IFeedHandler feedHandler) {
		super(requestData, feedHandler);
	}

	@Override
	public Document getData(){
		return (Document)super.getData();
	}
	
	@Override
	public ActivityService getService() {
		return (ActivityService)super.getService();
	}

	@Override
	public ActivityFeedHandler getFeedHandler() {
		return (ActivityFeedHandler)super.getFeedHandler();
	}
	
	@Override
	public Activity getEntity(Object data) {
		return (Activity)super.getEntity(data);
	}
	
	@Override
	protected ArrayList<Activity> createEntities() {
		XmlDataHandler dataHandler = new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
		ArrayList<Activity> activityEntries = new ArrayList<Activity>();
		List<Node> entries = dataHandler.getEntries(ActivityXPath.Entry);
		for (Node node: entries) {
			Activity activityEntry = getEntity(node);
			activityEntries.add(activityEntry);
		}
		return activityEntries;
	}
	
	private XmlDataHandler getMetaDataHandler(){
		return new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
	}

	
	@Override
	public int getTotalResults() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStartIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getItemsPerPage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCurrentPage() {
		// TODO Auto-generated method stub
		return 0;
	}	
}
