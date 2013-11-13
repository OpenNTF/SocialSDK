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
package com.ibm.sbt.services.client.connections.profiles.feedhandler;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.connections.profiles.ProfileService;
import com.ibm.sbt.services.client.connections.profiles.Tag;
import com.ibm.sbt.services.client.connections.profiles.TagList;
import com.ibm.sbt.services.client.connections.profiles.model.ProfileXPath;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

/**
 *
 * @author Swati Singh
 *
 */
public class TagFeedHandler implements IFeedHandler {

	private final ProfileService service;

	/**
	 * Constructor
	 *
	 * @param service
	 */
	public TagFeedHandler(ProfileService service){
		this.service = service;
	}

	/**
	 * @param requestData
	 * @return Tag
	 */
	@Override
	public Tag createEntity(Response requestData) {
		Node data = (Node)requestData.getData();
		return createEntityFromData(data);
	}

	/**
	 * @param data object
	 * @return Tag
	 */
	@Override
	public Tag createEntityFromData(Object data) {
		Node node = (Node)data;

		XPathExpression expr = (data instanceof Document) ? (XPathExpression)ProfileXPath.tagEntry.getPath() : null;
		XmlDataHandler handler = new XmlDataHandler(node, ConnectionsConstants.nameSpaceCtx, expr);
		Tag profileTag = new Tag(service, handler);
		return profileTag;
	}

	/**
	 * @param data object
	 * @return TagList
	 */
	@Override
	public TagList createEntityList(Response requestData) {

		return new TagList((Response)requestData, this);
	}

	/**
	 * @return ProfileService
	 */
	@Override
	public ProfileService getService() {
		return service;
	}

}