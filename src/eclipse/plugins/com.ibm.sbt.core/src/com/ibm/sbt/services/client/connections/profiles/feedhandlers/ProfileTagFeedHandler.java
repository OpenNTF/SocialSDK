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
package com.ibm.sbt.services.client.connections.profiles.feedhandlers;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.connections.profiles.ProfileService;
import com.ibm.sbt.services.client.connections.profiles.ProfileTag;
import com.ibm.sbt.services.client.connections.profiles.ProfileTagList;
import com.ibm.sbt.services.client.connections.profiles.ProfileXPath;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

/**
 * 
 * @author Swati Singh
 *
 */
public class ProfileTagFeedHandler implements IFeedHandler {

	private final ProfileService service;
	
	/**
	 * Constructor
	 * 
	 * @param service
	 */
	public ProfileTagFeedHandler(ProfileService service){
		this.service = service;
	}
	
	/**
	 * @param requestData
	 * @return Profile
	 */
	@Override
	public ProfileTag createEntity(Response requestData) {
		Node data = (Node)requestData.getData();
		return createEntityFromData(data);
	}
	
	/**
	 * @param data object
	 * @return Profile
	 */
	@Override
	public ProfileTag createEntityFromData(Object data) {
		Node node = (Node)data;
	
		XPathExpression expr = (data instanceof Document) ? (XPathExpression)ProfileXPath.tagEntry.getPath() : null;
		XmlDataHandler handler = new XmlDataHandler(node, ConnectionsConstants.nameSpaceCtx, expr);
		ProfileTag profileTag = new ProfileTag(service, handler);
		return profileTag;
	}

	/**
	 * @param data object
	 * @return Collection of profiles
	 */
	@Override
	public ProfileTagList createEntityList(Response requestData) {
		  
		return new ProfileTagList((Response)requestData, this);
	}

	/**
	 * @return CommunityService
	 */
	@Override
	public ProfileService getService() {
		return service;
	}

}
