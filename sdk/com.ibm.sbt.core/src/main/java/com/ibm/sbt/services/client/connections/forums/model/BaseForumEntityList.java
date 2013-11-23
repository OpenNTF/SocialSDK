package com.ibm.sbt.services.client.connections.forums.model;

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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.ConnectionsFeedXpath;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.forums.ForumService;
import com.ibm.sbt.services.client.connections.forums.feedhandler.ForumsFeedHandler;

/**
 * Base model list to be used with Forums, Topics and Replies
 * 
 * @author Manish Kataria 
 */

public class BaseForumEntityList extends EntityList<BaseForumEntity> {
	
	public BaseForumEntityList(Response requestData, BaseService service) {
		super(requestData, service);
	}
	
	public BaseForumEntityList(Response requestData, ForumsFeedHandler feedHandler) {
		super(requestData, feedHandler);
	}

	@Override
	public Document getData(){
		return (Document)super.getData();
	}
	
	@Override
	public ForumService getService() {
		return (ForumService)super.getService();
	}

	@Override
	public ForumsFeedHandler getFeedHandler() {
		return (ForumsFeedHandler)super.getFeedHandler();
	}
	
	@Override
	protected BaseForumEntity getEntity(Object data){
		return (BaseForumEntity)super.getEntity(data);
	}
	

	@Override
	protected ArrayList<BaseForumEntity> createEntities() {
		XmlDataHandler dataHandler = new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
		ArrayList<BaseForumEntity> forums = new ArrayList<BaseForumEntity>();
		List<Node> entries = dataHandler.getEntries(ConnectionsFeedXpath.Entry);
		for (Node node: entries) {
			BaseForumEntity forum =  getEntity(node);
			forums.add(forum);
		}
		return forums;
	}
	
	
	private XmlDataHandler getMetaDataHandler(){
		return new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
	}

	@Override
	public int getTotalResults() {
		return getMetaDataHandler().getAsInt(ConnectionsFeedXpath.TotalResults);
	}

	@Override
	public int getStartIndex() {
		return getMetaDataHandler().getAsInt(ConnectionsFeedXpath.StartIndex);
	}

	@Override
	public int getItemsPerPage() {
		return getMetaDataHandler().getAsInt(ConnectionsFeedXpath.ItemsPerPage);
	}

	@Override
	public int getCurrentPage() {
		return getMetaDataHandler().getAsInt(ConnectionsFeedXpath.CurrentPage);
	}

}
