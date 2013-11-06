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
package com.ibm.sbt.services.client.connections.communities.feedhandler;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityList;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.communities.model.CommunityXPath;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

/**
 * 
 * @author Carlos Manias
 *
 */
public class CommunityFeedHandler implements IFeedHandler {

	private final CommunityService service;
	
	/**
	 * Constructor
	 * 
	 * @param service
	 */
	public CommunityFeedHandler(CommunityService service){
		this.service = service;
	}
	
	/**
	 * @param requestData
	 * @return community
	 */
	@Override
	public Community createEntity(Response requestData) {
		Node data = (Node)requestData.getData();
		return createEntityFromData(data);
	}
	
	/**
	 * @param data object
	 * @return community
	 */
	@Override
	public Community createEntityFromData(Object data) {
		Node node = (Node)data;
		XPathExpression expr = (data instanceof Document) ? (XPathExpression)CommunityXPath.entry.getPath() : null;
		XmlDataHandler handler = new XmlDataHandler(node, ConnectionsConstants.nameSpaceCtx, expr);
		Community community = new Community(service, handler);
		return community;
	}

	/**
	 * @param data object
	 * @return Collection of communities
	 */
	@Override
	public CommunityList createEntityList(Response requestData) {
		return new CommunityList((Response)requestData, this);
	}

	/**
	 * @return CommunityService
	 */
	@Override
	public CommunityService getService() {
		return service;
	}

}
