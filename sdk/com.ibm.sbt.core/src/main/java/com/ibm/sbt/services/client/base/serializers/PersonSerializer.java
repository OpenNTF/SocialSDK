package com.ibm.sbt.services.client.base.serializers;

import org.w3c.dom.Node;

import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespaces;
import com.ibm.sbt.services.client.connections.common.Person;

public class PersonSerializer extends BaseEntitySerializer<Person> {

	public PersonSerializer(Person entity) {
		super(entity);
	}
	
	public Node xmlNode(String nodeName) {
		return appendChilds(rootNode(element(nodeName)),
				textElement("name", entity.getName()),
				textElement("email", entity.getEmail()),
				textElement(Namespaces.SNX, "userid", entity.getId()),
				textElement(Namespaces.SNX, "userState", entity.getState())
		);
	}
}
