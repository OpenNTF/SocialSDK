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

package com.ibm.sbt.services.client.connections.activities.serializers;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.ACTIVITY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.ACTIVITY_NODE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.APPLICATION_XML;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.CATEGORY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.CONTENT;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.LABEL;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.LABEL_ACTIVITYNODE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SCHEME;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TERM;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TYPE;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespace;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.connections.activities.ActivityNode;

/**
 * @author mwallace
 *
 */
public class ActivityNodeSerializer extends AtomEntitySerializer<ActivityNode> {

	public ActivityNodeSerializer(ActivityNode activityNode) {
		super(activityNode);
	}
	
	public String generateCreate() {
		Node entry = entry();
		
		appendChildren(entry,
				title(),
				activityNodeCategory(),
				activityUuid()
		);
		
		return serializeToString();
	}
	
	public String generateUpdate() {
		Node entry = genericAtomEntry();
		
		appendChildren(entry,
				title(),
				activityNodeCategory(),
				activityUuid()
		);
		
		return serializeToString();
	}
	
	protected Element activityNodeCategory() {
		return element(CATEGORY, 
				attribute(SCHEME, Namespace.TYPE.getUrl()), 
				attribute(TERM, ACTIVITY_NODE), 
				attribute(LABEL, LABEL_ACTIVITYNODE));
	}
	
	protected Element activityUuid() {
		return textElement(Namespace.SNX.getUrl(), ACTIVITY, entity.getActivityUuid());
	}
		
	@Override
	protected Element content() {
		return textElement(CONTENT, entity.getContent(), 
				attribute(TYPE, APPLICATION_XML));
	}
	
}
