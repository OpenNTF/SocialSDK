package com.ibm.sbt.services.client.connections.activity.feedHandler;

import org.w3c.dom.Node;

import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.activity.Activity;
import com.ibm.sbt.services.client.connections.activity.ActivityList;
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
		 XmlDataHandler handler = new XmlDataHandler((Node)data, ConnectionsConstants.nameSpaceCtx);
         Activity activity = new Activity(service, handler);
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
