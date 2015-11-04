/*
 * © Copyright IBM Corp. 2014
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
package com.ibm.sbt.services.client.connections.activities;

import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

/**
 * @author mwallace
 *
 */
public class ActivityNode extends NodeEntity {
	
	private InReplyTo inReplyTo;
	private AssignedTo assignedTo;
	
	/**
	 * Default constructor
	 */
	public ActivityNode() {
	}

	/**
	 * Construct ActivityNode associated with the specified service
	 * 
	 * @param service
	 */
	public ActivityNode(ActivityService service) {
		setService(service);
	}

	/**
	 * Construct ActivityNode associated with the specified service
	 * 
	 * @param service
	 * @param activityUuid
	 */
	public ActivityNode(ActivityService service, String activityUuid) {
		setService(service);
		setActivityUuid(activityUuid);
	}

	/**
	 * Construct Activity based on the specified node
	 * 
	 * @param service
	 * @param node
	 * @param namespaceCtx
	 * @param xpathExpression
	 */
	public ActivityNode(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}

	/**
	 * Returns the activity node ID.
	 * 
	 * @return {String} activityNodeUuid
	 */
	public String getActivityNodeUuid() {
		String id = getId();
		if (StringUtil.isNotEmpty(id) && id.startsWith(UUID_PREFIX)) {
			id = id.substring(UUID_PREFIX.length());
		}
		return id;
	}
	
	/**
	 * Returns the activity ID.
	 * 
	 * @return {String} activityUuid
	 */
	public String getActivityUuid() {
		return getAsString(ActivityXPath.activity);
	}
	
	/**
	 * Returns the details of the node this node is in reply to
	 * @return {InReplyTo}
	 */
	public InReplyTo getInReplyTo() {
		if (inReplyTo == null && getDataHandler() != null) {
			inReplyTo = createInReplyTo((Node)getDataHandler().getData(), ActivityXPath.in_reply_to);
		}
		return inReplyTo;
	}
	
	/**
	 * Sets the details of the node this node is in reply to
	 * 
	 * @param node
	 */
	public void setInReplyTo(ActivityNode node) {
		this.inReplyTo = new InReplyTo(node);
	}
	
	/**
	 * Sets the details of the node this node is in reply to
	 * 
	 * @param inReplyTo
	 */
	public void setInReplyTo(InReplyTo inReplyTo) {
		this.inReplyTo = inReplyTo;
	}
	
	/**
	 * Returns the details of the person this node is assigned to
	 * @return {AssignedTo}
	 */
	public AssignedTo getAssignedTo() {
		if (assignedTo == null) {
			assignedTo = createAssignedTo((Node)getDataHandler().getData(), ActivityXPath.in_reply_to);
		}
		return assignedTo;
	}
	
	/**
	 * Sets the details of the person this node is assigned to
	 * 
	 * @param assignedTo
	 */
	public void setAssignedTo(AssignedTo assignedTo) {
		this.assignedTo = assignedTo;
	}
	
	/**
	 * Create a InReplyTo to from the specified node
	 */
	protected AssignedTo createAssignedTo(Node node, FieldEntry fieldEntry) {
		return new AssignedTo(getService(), new XmlDataHandler(node, ConnectionsConstants.nameSpaceCtx, (XPathExpression)fieldEntry.getPath()));
	}

	/**
	 * Create a InReplyTo to from the specified node
	 */
	protected InReplyTo createInReplyTo(Node node, FieldEntry fieldEntry) {
		return new InReplyTo(getService(), new XmlDataHandler(node, ConnectionsConstants.nameSpaceCtx, (XPathExpression)fieldEntry.getPath()));
	}
	
	
	/**
	 * related to ActivityService.updateActivityNode  
	 * 
	 * @throws ClientServicesException 
	 * @throws {@link NullPointerException} If there is no service associated with this ActivityNode
	 */
	public void update() throws ClientServicesException {
		ActivityService service = getActivityService();
		service.updateActivityNode(this);
	}
	
}
