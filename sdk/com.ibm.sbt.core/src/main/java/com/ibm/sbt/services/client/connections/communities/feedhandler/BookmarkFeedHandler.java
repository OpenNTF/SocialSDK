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
package com.ibm.sbt.services.client.connections.communities.feedhandler;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.connections.communities.Bookmark;
import com.ibm.sbt.services.client.connections.communities.BookmarkList;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.communities.model.CommunityXPath;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

/**
 * 
 * @author Swati Singh
 *
 */
public class BookmarkFeedHandler implements IFeedHandler {

	private final CommunityService service;
	
	/**
	 * Constructor
	 * 
	 * @param service
	 */
	public BookmarkFeedHandler(CommunityService service){
		this.service = service;
	}
	
	/**
	 * @param requestData
	 * @return Bookmark
	 */
	@Override
	public Bookmark createEntity(Response requestData) {
		Node data = (Node)requestData.getData();
		return createEntityFromData(data);
	}
	
	/**
	 * @param data object
	 * @return Bookmark
	 */
	@Override
	public Bookmark createEntityFromData(Object data) {
		Node node = (Node)data;
		XPathExpression expr = (data instanceof Document) ? (XPathExpression)CommunityXPath.entry.getPath() : null;
		XmlDataHandler handler = new XmlDataHandler(node, ConnectionsConstants.nameSpaceCtx, expr);
		Bookmark bookmark = new Bookmark(service, handler);
		return bookmark;
	}

	/**
	 * @param data object
	 * @return Collection of communities
	 */
	@Override
	public BookmarkList createEntityList(Response requestData) {
		return new BookmarkList((Response)requestData, this);
	}

	/**
	 * @return CommunityService
	 */
	@Override
	public CommunityService getService() {
		return service;
	}

}
