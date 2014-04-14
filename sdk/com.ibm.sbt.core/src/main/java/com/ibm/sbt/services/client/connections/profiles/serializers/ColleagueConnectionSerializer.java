package com.ibm.sbt.services.client.connections.profiles.serializers;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespace;
import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespaces;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.connections.profiles.ColleagueConnection;

public class ColleagueConnectionSerializer extends AtomEntitySerializer<ColleagueConnection> {
	
	protected static final String CONNECTION = "connection";
	protected static final String COLLEAGUE = "colleague";
	protected static final String PENDING = "pending";
	protected static final String ACCEPTED = "accepted";

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
		Element element = element(Namespaces.ATOM, ENTRY);
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
