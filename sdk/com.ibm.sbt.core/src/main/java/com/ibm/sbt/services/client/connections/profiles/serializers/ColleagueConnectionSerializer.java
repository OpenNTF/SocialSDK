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
package com.ibm.sbt.services.client.connections.profiles.serializers;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.CONTENT;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.ENTRY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.HTML;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.ID;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TYPE;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.ACCEPTED;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.COLLEAGUE;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.CONNECTION;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.PENDING;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespace;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.connections.profiles.ColleagueConnection;

/**
 * 
 * @author Carlos Manias
 *
 */
public class ColleagueConnectionSerializer extends AtomEntitySerializer<ColleagueConnection> {
	
	public ColleagueConnectionSerializer(ColleagueConnection entity) {
		super(entity);
	}

	public void generateSendInvitePayload() {
		invitePayload(PENDING);
	}

	public String sendInvitePayload(){
		generateSendInvitePayload();
		return serializeToString();
	}

	public void generateAcceptInvitePayload() {
		invitePayload(ACCEPTED);
	}

	public String acceptInvitePayload(){
		generateSendInvitePayload();
		return serializeToString();
	}

	protected Node invitePayload(String status) {
		Node entry = entry();
		
		appendChildren(entry,
				id(),
				title(),
				connection(),
				colleague(),
				status(status),
				content()
		);
		return entry;
	}

	@Override
	protected Node entry() {
		Element element = element(Namespace.ATOM.getUrl(), ENTRY);
		element.setAttributeNS(Namespace.XMLNS.getUrl(), Namespace.SNX.getNSPrefix(), Namespace.SNX.getUrl());
		return rootNode(element);
	}
	
	protected Element id(){
		return textElement(ID, new StringBuilder(Namespace.TAGENTRY.getUrl()).append(entity.getId()).toString());
	}

	protected Element connection() {
	 return category(Namespace.TYPE.getUrl(), CONNECTION);
	}

	protected Element colleague() {
	 return category(Namespace.CONNECTION.getUrl(), COLLEAGUE);
	}

	protected Element status(String status) {
	 return category(Namespace.STATUS.getUrl(), status);
	}

	@Override
	protected Element content() {
		return textElement(CONTENT, entity.getContent(), attribute(TYPE, HTML));
	}
}
