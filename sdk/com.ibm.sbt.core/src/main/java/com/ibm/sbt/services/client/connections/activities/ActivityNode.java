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

import java.util.List;

import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.BaseService;

/**
 * @author mwallace
 *
 */
public class ActivityNode extends AtomEntity {

	static final String UUID_PREFIX = "urn:lsid:ibm.com:oa:"; ////$NON-NLS-1$
	
	/**
	 * Default constructor
	 */
	public ActivityNode() {
	}

	/**
	 * Construct Activity based on the specified node
	 * 
	 * @param service
	 * @param node
	 * @param nameSpaceCtx
	 * @param xpath
	 */
	public ActivityNode(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}

	/**
	 * Return the associated ActivityService.
	 * 
	 * @return
	 */
	public ActivityService getActivityService() {
		return (ActivityService)getService();
	}
	
	/**
	 * Returns the activity node ID.
	 * 
	 * @return activityNodeUuid
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
	 * Returns permissions.
	 * 
	 * @return permissions
	 */
	public String getPermissions() {
		return getAsString(ActivityXPath.permissions);
	}
	
	/**
	 * Returns the depth of the activity.
	 * 
	 * @return depth
	 */
	public int getDepth() {
		return getAsInt(ActivityXPath.depth);
	}
	
	/**
	 * Returns the position of the activity.
	 * 
	 * @return position
	 */
	public long getPosition() {
		return getAsLong(ActivityXPath.position);
	}
	
	/**
	 * Returns the tags on the wiki page.
	 * 
	 * @return
	 */
	public List<String> getTags() {
		return super.getBaseTags();
	}

	/**
	 * Set the tags on the wiki page.
	 * 
	 * @param tags
	 */
	public void setTags(List<String> tags) {
		super.setBaseTags(tags);
	}
	
}
