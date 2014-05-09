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
import static com.ibm.sbt.services.client.base.ConnectionsConstants.CONTRIBUTOR;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.ENTRY;
import static com.ibm.sbt.services.client.connections.communities.CommunityConstants.MEMBER_TERM;
import static com.ibm.sbt.services.client.connections.communities.CommunityConstants.MEMBER_TERM_VALUE;
import static com.ibm.sbt.services.client.connections.communities.CommunityConstants.SNX_ROLE;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespace;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.base.serializers.PersonSerializer;
import com.ibm.sbt.services.client.connections.communities.Member;


/**
 * @author Benjamin Jakobus
 * @author Dave Tobin
 */
public class CommunityMemberSerializer extends AtomEntitySerializer<Member> {

	public CommunityMemberSerializer(Member member) {
		super(member);
	}
	
	protected void generateCreatePayload() {
		Node entry = entry();
		appendChildren(entry,
				title(),
				contributor(),
				role(),
				category()
		);
	}
	
	protected void generateUpdatePayload() {
		Node entry = entry();
		appendChildren(entry,
				contributor(),
				role()
		);
	}
	
	@Override
	protected Node entry() {
		Element element = element(Namespace.ATOM.getUrl(), ENTRY);
		element.setAttributeNS(Namespace.XMLNS.getUrl(), Namespace.APP.getNSPrefix(), Namespace.APP.getUrl());
		element.setAttributeNS(Namespace.XMLNS.getUrl(), Namespace.OPENSEARCH.getNSPrefix(), Namespace.OPENSEARCH.getUrl());
		element.setAttributeNS(Namespace.XMLNS.getUrl(), Namespace.SNX.getNSPrefix(), Namespace.SNX.getUrl());
		Node root = rootNode(element);
		return root;
	}
	
	private Element category() {
		return element(CATEGORY, attribute(MEMBER_TERM, MEMBER_TERM_VALUE), attribute(Namespace.SCHEME.getPrefix(), Namespace.SCHEME.getUrl()));
	}
	
	private Element role() {
		return textElement(SNX_ROLE, entity.getRole(), attribute(Namespace.COMPONENT.getPrefix(), Namespace.COMPONENT.getUrl()));
	}
	
	@Override
	protected Node contributor() {
		return (new PersonSerializer(entity).xmlNode(CONTRIBUTOR));
	}
	
	public String createPayload() throws ClientServicesException {
		generateCreatePayload();
		return serializeToString();
	}

	public String updatePayload() throws ClientServicesException {
		generateUpdatePayload();
		System.out.println(serializeToString());
		return serializeToString();
	}
}
