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
package com.ibm.sbt.services.client.connections.follow.serializers;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.CATEGORY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.ENTRY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SCHEME;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TERM;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespace;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.connections.follow.FollowedResource;

/**
 * 
 * @author Carlos Manias
 *
 */
public class FollowSerializer extends AtomEntitySerializer<FollowedResource> {

	public FollowSerializer(FollowedResource entity) {
		super(entity);
	}

	protected void generateStartFollowingPayload() {
		Node entry = entry();
		
		appendChildren(entry,
				source(),
				resourceType(),
				resourceId()
		);
	}

	public String startFollowingPayload(){
		generateStartFollowingPayload();
		return serializeToString();
	}

	@Override
	protected Node entry() {
		Element element = element(Namespace.ATOM.getUrl(), ENTRY);
		Node root = rootNode(element);
		return root;
	}

	protected Element source() {
		return element(Namespace.ATOM.getUrl(), CATEGORY, 
				attribute(SCHEME, Namespace.SOURCE.getUrl()),
				attribute(TERM, entity.getSource()));
	}

	protected Element resourceType() {
		return element(Namespace.ATOM.getUrl(), CATEGORY, 
				attribute(SCHEME, Namespace.RESOURCE_TYPE.getUrl()),
				attribute(TERM, entity.getResourceType()));
	}

	protected Element resourceId() {
		return element(Namespace.ATOM.getUrl(), CATEGORY, 
				attribute(SCHEME, Namespace.RESOURCE_ID.getUrl()),
				attribute(TERM, entity.getResourceId()));
	}

}
