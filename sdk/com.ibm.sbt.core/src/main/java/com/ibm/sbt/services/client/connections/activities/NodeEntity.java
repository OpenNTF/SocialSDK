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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

/**
 * @author mwallace
 *
 */
public class NodeEntity extends AtomEntity {
	
	private List<Field> fields;

	static final String UUID_PREFIX = "urn:lsid:ibm.com:oa:"; ////$NON-NLS-1$
	
	/**
	 * Default constructor
	 */
	public NodeEntity() {
	}

	/**
	 * Construct ActivityNode associated with the specified service
	 * 
	 * @param service
	 */
	public NodeEntity(ActivityService service) {
		setService(service);
	}

	/**
	 * Construct Activity based on the specified node
	 * 
	 * @param service
	 * @param node
	 * @param nameSpaceCtx
	 * @param xpath
	 */
	public NodeEntity(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}
	
	/**
	 * 
	 * @return
	 */
	public ActivityService getActivityService() {
		return (ActivityService)getService();
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
	 * Returns the tags on the node.
	 * 
	 * @return
	 */
	public List<String> getTags() {
		return super.getBaseTags();
	}

	/**
	 * Set the tags on the node.
	 * 
	 * @param tags
	 */
	public void setTags(List<String> tags) {
		super.setBaseTags(tags);
	}

	/**
	 * 
	 * @return
	 */
	public Field[] getFields() {
		if (fields == null) {
			fields = new ArrayList<Field>();
			if (getDataHandler() != null) {
				XmlDataHandler xmlHandler = (XmlDataHandler)getDataHandler();
				List<Node> fieldNodes = xmlHandler.getEntries(ActivityXPath.field);
				for (Node fieldNode : fieldNodes) {
					fields.add(createField(fieldNode));
				}
			}
		}
		return fields.toArray(new Field[fields.size()]);
	}
	
	/**
	 * 
	 * @param fid
	 * @return
	 */
	public Field getField(String fid) {
		if (fields != null) {
			for (Field field : fields) {
				if (field.getFid().equals(fid)) {
					return field;
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param field
	 */
	public void addField(Field field) {
		if (fields == null) {
			getFields();
		}
		fields.add(field);
	}
	
	/**
	 * 
	 * @param field
	 */
	public void updateField(Field updatedField) {
		for (Field field : fields) {
			if (field.getFid().equals(updatedField.getFid())) {
				fields.remove(field);
				fields.add(updatedField);
				return;
			}
		}
		throw new IllegalArgumentException(MessageFormat.format("Node does contain field with id: {0}", updatedField.getFid()));
	}
	
	/**
	 * 
	 * @param field
	 */
	public void deleteField(Field field) {
		deleteField(field.getFid());
	}
	
	/**
	 * 
	 * @param fid
	 */
	public void deleteField(String fid) {
		for (Field field : fields) {
			if (field.getFid().equals(fid)) {
				fields.remove(field);
				return;
			}
		}
		throw new IllegalArgumentException(MessageFormat.format("Node does contain field with id: {0}", fid));
	}
	
	//
	// Internals
	// 
	
	protected Field createField(Node node) {
		Field field = null;
		String type = DOMUtil.getAttributeValue((Element)node, "type");
		BaseService service = getService();
		if ("date".equals(type)) {
			field = new DateField(service, node, ConnectionsConstants.nameSpaceCtx, null);
		} else if ("file".equals(type)) {
			field = new FileField(service, node, ConnectionsConstants.nameSpaceCtx, null);
		} else if ("link".equals(type)) {
			field = new LinkField(service, node, ConnectionsConstants.nameSpaceCtx, null);
		} else if ("person".equals(type)) {
			field = new PersonField(service, node, ConnectionsConstants.nameSpaceCtx, null);
		} else if ("text".equals(type)) {
			field = new TextField(service, node, ConnectionsConstants.nameSpaceCtx, null);
		}
		return field;
	}
	
}
