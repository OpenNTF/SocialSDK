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
package com.ibm.sbt.services.client.connections.forums.feedhandler;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.forums.ForumService;
import com.ibm.sbt.services.client.connections.forums.ForumsXPath;
import com.ibm.sbt.services.client.connections.forums.Recommendation;
import com.ibm.sbt.services.client.connections.forums.RecommendationList;

/**
 * Feed handler for Forums Service
 *
 * @author Swati Singh
 *
 */
public class RecommendationsFeedHandler implements IFeedHandler {

	private final ForumService service;

	/**
	 * Constructor
	 *
	 * @param service
	 */
	public RecommendationsFeedHandler(ForumService service){
		this.service = service;
	}

	/**
	 * @param requestData
	 * @return Recommendation
	 */
	@Override
	public Recommendation createEntity(Response requestData) {
		Node data = (Node)requestData.getData();
		return createEntityFromData(data);
	}

	/**
	 * @param data object
	 * @return Recommendation
	 */
	@Override
	public Recommendation createEntityFromData(Object data) {
		Node node = (Node)data;

		XPathExpression expr = (data instanceof Document) ? (XPathExpression)ForumsXPath.entry.getPath() : null;
		XmlDataHandler handler = new XmlDataHandler(node, ConnectionsConstants.nameSpaceCtx, expr);
		Recommendation recommendation = new Recommendation(service, handler);
		return recommendation;
	}

	/**
	 * @param data object
	 * @return RecommendationList
	 */
	@Override
	public RecommendationList createEntityList(Response requestData) {

		return new RecommendationList((Response)requestData, this);
	}

	/**
	 * @return ForumService
	 */
	@Override
	public ForumService getService() {
		return service;
	}

}