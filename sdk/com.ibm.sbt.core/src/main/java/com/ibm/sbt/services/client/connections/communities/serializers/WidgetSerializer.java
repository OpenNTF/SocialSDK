/**
 * 
 */
package com.ibm.sbt.services.client.connections.communities.serializers;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.CATEGORY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.ENTRY;
import static com.ibm.sbt.services.client.connections.communities.CommunityConstants.COMMUNITY_TERM;
import static com.ibm.sbt.services.client.connections.communities.WidgetConstants.WIDGET;
import static com.ibm.sbt.services.client.connections.communities.WidgetConstants.WIDGET_DEF_ID;
import static com.ibm.sbt.services.client.connections.communities.WidgetConstants.WIDGET_HIDDEN;
import static com.ibm.sbt.services.client.connections.communities.WidgetConstants.WIDGET_INST_ID;
import static com.ibm.sbt.services.client.connections.communities.WidgetConstants.WIDGET_LOCATION;
import static com.ibm.sbt.services.client.connections.communities.WidgetConstants.WIDGET_PREV_INST_ID;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespace;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.connections.communities.Widget;
import com.ibm.sbt.services.client.connections.communities.model.WidgetXPath;

/**
 * @author Christian Gosch, inovex GmbH
 * 
 * @see Widget
 */
public class WidgetSerializer extends AtomEntitySerializer<Widget> {

	public WidgetSerializer(Widget entity) {
		super(entity);
	}

	protected void generateCreatePayload() throws ClientServicesException {
		Node entry = entry();

		appendChildren(entry,
				title(),
				category(),
				defId(),
				instanceId(),
				hidden(),
				location(),
				prevInstId()
		);
	}
	
	public String createPayload() throws ClientServicesException {
		generateCreatePayload();
		return serializeToString();
	}
	
	@Override
	protected Node entry() {
		Element element = element(Namespace.ATOM.getUrl(), ENTRY);
		element.setAttributeNS(Namespace.XMLNS.getUrl(), Namespace.SNX.getNSPrefix(), Namespace.SNX.getUrl());
		Node root = rootNode(element);
		return root;
	}

	protected Element category() {
		return textElement(CATEGORY, "", attribute(COMMUNITY_TERM, WIDGET), attribute(Namespace.SCHEME.getPrefix(), Namespace.SCHEME.getUrl()));
	}

	protected Element defId() {
		Element elem = null;
		if (entity.exists(WidgetXPath.widgetDefId)) {
			elem = textElement(WIDGET_DEF_ID, entity.getWidgetDefId());
		}
		return elem;
	}
	
	protected Element instanceId() {
		Element elem = null;
		if (entity.exists(WidgetXPath.widgetInstanceId)) {
			elem = textElement(WIDGET_INST_ID, entity.getWidgetInstanceId());
		}
		return elem;
	}
	
	protected Element hidden() {
		Element elem = null;
		if (entity.exists(WidgetXPath.widgetHidden)) {
			elem = textElement(WIDGET_HIDDEN, String.valueOf(entity.getWidgetHidden()));
		}
		return elem;
	}
	
	protected Element location() {
		Element elem = null;
		if (entity.exists(WidgetXPath.widgetLocation)) {
			elem = textElement(WIDGET_LOCATION, entity.getWidgetLocation());
		}
		return elem;
	}
	
	protected Element prevInstId() {
		Element elem = null;
		if (entity.exists(WidgetXPath.previousWidgetInstanceId)) {
			elem = textElement(WIDGET_PREV_INST_ID, entity.getPreviousWidgetInstanceId());
		}
		return elem;
	}
	
}
