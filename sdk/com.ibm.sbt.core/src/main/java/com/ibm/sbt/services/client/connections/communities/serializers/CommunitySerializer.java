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

package com.ibm.sbt.services.client.connections.communities.serializers;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.CATEGORY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.CONTENT;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.ENTRY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.HTML;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TYPE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.HREF;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.LINK;
import static com.ibm.sbt.services.client.connections.communities.CommunityConstants.COMMUNITY;
import static com.ibm.sbt.services.client.connections.communities.CommunityConstants.COMMUNITY_TERM;
import static com.ibm.sbt.services.client.connections.communities.CommunityConstants.COMMUNITY_TYPE;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespace;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.connections.communities.Community;

public class CommunitySerializer extends AtomEntitySerializer<Community> {

	public CommunitySerializer(Community community) {
		super(community);
	}

	protected void generateCreatePayload() throws ClientServicesException {
		Node entry = entry();

		appendChildren(entry,
				title(),
				content(),
				category(),
				type(),
				id()
		);
		appendChildren(entry, tags());
	}
	
	protected void generateSubCommUpdatePayload() throws ClientServicesException {
		Node entry = entry();

		appendChildren(entry,
				title(),
				content(),
				category(),
				type(),
				id(),
				parentLink()
		);
		appendChildren(entry, tags());
	}

	public String createPayload() throws ClientServicesException {
		generateCreatePayload();
		return serializeToString();
	}
	
	public String subCommUpdatePayload() throws ClientServicesException {
		generateSubCommUpdatePayload();
		return serializeToString();
	}

	@Override
	protected Node entry() {
		Element element = element(Namespace.ATOM.getUrl(), ENTRY);
		element.setAttributeNS(Namespace.XMLNS.getUrl(), Namespace.APP.getNSPrefix(), Namespace.APP.getUrl());
		element.setAttributeNS(Namespace.XMLNS.getUrl(), Namespace.THR.getNSPrefix(), Namespace.THR.getUrl());
		element.setAttributeNS(Namespace.XMLNS.getUrl(), Namespace.SNX.getNSPrefix(), Namespace.SNX.getUrl());
		Node root = rootNode(element);
		return root;
	}

	@Override
	protected Element content() {
		String content = entity.getContent();
		if(content == null){
			content="";
		}
		return textElement(CONTENT, content, attribute(TYPE, HTML));
	}

	protected Element category() {
		return textElement(CATEGORY, "", attribute(COMMUNITY_TERM, COMMUNITY), attribute(Namespace.SCHEME.getPrefix(), Namespace.SCHEME.getUrl()));
	}

	protected Element type() {
		return textElement(COMMUNITY_TYPE, entity.getCommunityType());
	}
	
	protected Element parentLink(){
		return textElement(LINK, "", attribute(HREF, entity.getParentCommunityUrl()), attribute(Namespace.parentRel.getPrefix(), Namespace.parentRel.getUrl()), attribute(TYPE,"application/atom+xml") );
	}
}