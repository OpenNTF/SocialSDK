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

package com.ibm.sbt.services.client.connections.activities.serializers;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespaces;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.connections.activities.ActivityNode;

/**
 * @author mwallace
 *
 */
public class ActivityNodeSerializer extends AtomEntitySerializer<ActivityNode> {
	
	private NodeSerializer nodeSerializer;
	
	public ActivityNodeSerializer(ActivityNode activityNode) {
		super(activityNode);
		
		nodeSerializer = new NodeSerializer(activityNode);
	}
	
	public String generateCreate() {
		return generateUpdate();
	}
	
	public String generateUpdate() {
		Node entry = genericAtomEntry();
		
		appendChildren(entry,
				title(),
				activityNodeCategory(),
				activityUuid()
		);
		
		appendChildren(entry, tags());
		appendChildren(entry, nodeSerializer.fields());
		
		return serializeToString();
	}
	
	protected Element activityNodeCategory() {
		return element("category", 
				attribute("scheme", "http://www.ibm.com/xmlns/prod/sn/type"), 
				attribute("term", "activity_node"), 
				attribute("label", "Activity Node"));
	}
	
	protected Element activityUuid() {
		return textElement(Namespaces.SNX, "activity", entity.getActivityUuid());
	}
		
	@Override
	protected Element content() {
		return textElement("content", entity.getContent(), 
				attribute("type", "application/atom+xml"));
	}
	
}
