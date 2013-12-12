/*
o * © Copyright IBM Corp. 2013
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

package com.ibm.sbt.services.client.connections.profiles;


import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.profiles.model.ColleagueConnectionXPath;

public class ColleagueConnection extends AtomEntity{
	
	private final String CONNECTIONID = "tag:profiles.ibm.com,2006:entry";
	/**
	 * Constructor
	 * @param BaseService
	 * @param handler
	 */
	public ColleagueConnection(BaseService svc, XmlDataHandler handler) {
		super(svc, handler);
	}
	/**
	 * Constructor
	 * @param BaseService
	 * @param Node
	 * @param NamespaceContext
	 * @param XPathExpression
	 */
	public ColleagueConnection(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}

	/**
	 * @return the Connection Id
	 */
	public String getConnectionId() {
	       String id = getId();
	        if(StringUtil.isNotEmpty(id)){
	                if(StringUtil.startsWithIgnoreCase(id, CONNECTIONID)){
	                        id = id.substring(CONNECTIONID.length());
	                }
	        }
	        return id;
	}
	/**
	 * sets the Connection Id
	 */
	public void setConnectionId(String connectionId) {
		super.setId(connectionId);
	}

	/**
	 * @return the Contributor Name
	 */
	@Deprecated
	public String getContributorName() {
		return getAsString(ColleagueConnectionXPath.contributorName);
	}

	/**
	 * @return the Contributor UserId
	 */
	@Deprecated
	public String getContributorUserId() {
		return getAsString(ColleagueConnectionXPath.contributorUserId);
	}

	/**
	 * @return the Contributor Email
	 */
	@Deprecated
	public String getContributorEmail() {
		return getAsString(ColleagueConnectionXPath.contributorEmail);
	}

	/**
	 * @return the Author Name
	 */
	@Deprecated
	public String getAuthorName() {
		return getAsString(ColleagueConnectionXPath.authorName);
	}

	/**
	 * @return the Author UserId
	 */
	@Deprecated
	public String getAuthorUserId() {
		return getAsString(ColleagueConnectionXPath.authorUserId);
	}

	/**
	 * @return the Author Email
	 */
	@Deprecated
	public String getAuthorEmail() {
		return getAsString(ColleagueConnectionXPath.authorEmail);
	}
	/**
	 * @return self Url of colleague connection entry
	 */
	@Deprecated
	public String getSelfLink(){
		return getAsString(ColleagueConnectionXPath.selfLinkFromEntry);
	}
	/**
	 * @return edit Url of colleague connection entry
	 */
	@Deprecated
	public String getEditLink(){
		return getAsString(ColleagueConnectionXPath.editLinkFromEntry);
	}



}
