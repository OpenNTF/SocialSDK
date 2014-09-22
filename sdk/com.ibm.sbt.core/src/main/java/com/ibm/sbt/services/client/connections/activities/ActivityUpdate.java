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

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.BaseService;

/**
 * @author mwallace
 *
 */
public class ActivityUpdate extends AtomEntity {

	static final String UUID_PREFIX = "urn:lsid:ibm.com:oa:"; ////$NON-NLS-1$
	
	/**
	 * Standard type values for an activity update. 
	 */
	static public final String TYPE_ACTIVITY_ENTRY_UPDATED = "activity.entry.updated";
	
	/**
	 * Default constructor
	 */
	public ActivityUpdate() {
	}

	/**
	 * Construct ActivityUpdate associated with the specified service
	 * 
	 * @param service
	 */
	public ActivityUpdate(ActivityService service) {
		setService(service);
	}

	/**
	 * Construct ActivityUpdate based on the specified node
	 * 
	 * @param service
	 * @param node
	 * @param nameSpaceCtx
	 * @param xpath
	 */
	public ActivityUpdate(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}
	
	/**
	 * 
	 * @return
	 */
	public ActivityService getActivityService() {
		return (ActivityService)getService();
	}

	/**
	 * Returns the activity ID.
	 * 
	 * @return activityUuid
	 */
	public String getActivityUuid() {
		return getAsString(ActivityXPath.activity);
	}
	
	/**
	 * Returns the activity ID.
	 * 
	 * @param activityUuid
	 */
	public void setActivityUuid(String activityUuid) {
		setAsString(ActivityXPath.activity, activityUuid);
	}
	
	/**
	 * Sets the type
	 * 
	 * @param type
	 */
	public void setType(String type) {
		setAsString(ActivityXPath.type, type);
	}
	
	/**
	 * Return the type
	 * 
	 * @return
	 */
	public String getType() {
		return getAsString(ActivityXPath.type);
	}
		
}
