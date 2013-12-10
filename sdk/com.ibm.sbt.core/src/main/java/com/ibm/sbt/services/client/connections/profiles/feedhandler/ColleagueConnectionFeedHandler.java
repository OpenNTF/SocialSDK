/*
 * � Copyright IBM Corp. 2013
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
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.connections.profiles.model.ColleagueConnectionXPath;
import com.ibm.sbt.services.client.connections.profiles.ColleagueConnection;
import com.ibm.sbt.services.client.connections.profiles.ColleagueConnectionList;
import com.ibm.sbt.services.client.connections.profiles.ProfileService;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

/**
 * 
 * @author Swati Singh
 *
 */
public class ColleagueConnectionFeedHandler implements IFeedHandler {

	private final ProfileService service;
	
	/**
	 * Constructor
	 * 
	 * @param service
	 */
	public ColleagueConnectionFeedHandler(ProfileService service){
		this.service = service;
	}
	
	/**
	 * @param requestData
	 * @return Profile
	 */
	@Override
	public ColleagueConnection createEntity(Response requestData) {
		Node data = (Node)requestData.getData();
		return createEntityFromData(data);
	}
	
	/**
	 * @param data object
	 * @return Profile
	 */
	@Override
	public ColleagueConnection createEntityFromData(Object data) {
		XPathExpression expr = (data instanceof Document) ? (XPathExpression)AtomXPath.singleEntry.getPath() : null;
		ColleagueConnection colleagueConnection = new ColleagueConnection(service, (Node)data, ConnectionsConstants.nameSpaceCtx, expr);
		return colleagueConnection;
	}

	/**
	 * @param data object
	 * @return list of Colleague Connections 
	 */
	@Override
	public ColleagueConnectionList createEntityList(Response requestData) {
		return new ColleagueConnectionList((Response)requestData, this);
	}

	/**
	 * @return CommunityService
	 */
	@Override
	public ProfileService getService() {
		return service;
	}

}
