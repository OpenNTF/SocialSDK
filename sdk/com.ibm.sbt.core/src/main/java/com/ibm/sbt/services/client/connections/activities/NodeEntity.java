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
import java.util.Collections;
import java.util.Date;
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
import com.ibm.sbt.services.client.connections.common.Link;

/**
 * @author mwallace
 *
 */
public class NodeEntity extends AtomEntity {
	
	private List<Field> fields;
	private List<ActivityAttachment> attachments = new ArrayList<ActivityAttachment>();
	private Link enclosureLink;

	static final String UUID_PREFIX = "urn:lsid:ibm.com:oa:"; ////$NON-NLS-1$
	
	static final Field[] NO_FIELDS = new Field[0];
	
	/**
	 * Standard type values for an activity. 
	 */
	static public final String TYPE_ACTIVITY = "activity";
	static public final String TYPE_CHAT = "chat";
	static public final String TYPE_EMAIL = "email";
	static public final String TYPE_ENTRY = "entry";
	static public final String TYPE_ENTRY_TEMPLATE = "entrytemplate";
	static public final String TYPE_REPLY = "reply";
	static public final String TYPE_SECTION = "section";
	static public final String TYPE_TODO = "todo";
	
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
	 * @param namespaceCtx
	 * @param xpathExpression
	 */
	public NodeEntity(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.BaseEntity#clearFieldsMap()
	 */
	@Override
	public void clearFieldsMap() {
		fields = null;
		super.clearFieldsMap();
	}
	
	/**
	 * 
	 * @return {ActivityService}
	 */
	public ActivityService getActivityService() {
		return (ActivityService)getService();
	}

	/**
	 * Returns the activity ID.
	 * 
	 * @return {String} activityUuid
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
	 * Sets the type
	 * 
	 * @param type
	 */
	public void setType(String type) {
		setAsString(ActivityXPath.type, type);
	}
	
	/**
	 * Return the type
	 * 
	 * @return {String}
	 */
	public String getType() {
		return getAsString(ActivityXPath.type);
	}
	
	/**
	 * Sets the flags
	 * 
	 * @param flags
	 */
	public void setFlags(String flags) {
		setAsString(ActivityXPath.flags, flags);
	}
	
	/**
	 * Return the flags
	 * 
	 * @return {String}
	 */
	public String getFlags() {
		return getAsString(ActivityXPath.flags);
	}
	
	/**
	 * Returns permissions.
	 * 
	 * @return {String} permissions
	 */
	public String getPermissions() {
		return getAsString(ActivityXPath.permissions);
	}
	
	/**
	 * Returns the depth of the activity.
	 * 
	 * @return {int} depth
	 */
	public int getDepth() {
		return getAsInt(ActivityXPath.depth);
	}
	
	/**
	 * Returns the position of the activity.
	 * 
	 * @return {long} position
	 */
	public long getPosition() {
		return getAsLong(ActivityXPath.position);
	}
	
	/**
	 * Set the position of the activity.
	 * 
	 * @param position
	 */
	public void setPosition(long position) {
		setAsLong(ActivityXPath.position, position);
	}
	
	/**
	 * Returns the date on which the activity is due to be completed.
	 * 
	 * @return {Date} due date
	 */
	public Date getDuedate() {
		return getAsDate(ActivityXPath.duedate);
	}
	
	/**
	 * Set the date on which the activity is due to be completed.
	 * 
	 * @param duedate
	 */
	public void setDuedate(Date duedate) {
		setAsDate(ActivityXPath.duedate, duedate);
	}
		
	/**
	 * Returns the tags on the node.
	 * 
	 * @return {List<String>}
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
	 * Returns true if the node is a reply.
	 * 
	 * Flag that is only present on an node that is a reply.
	 * 
	 * @return {boolean}
	 */
	public boolean isReply() {
		return exists(ActivityXPath.reply);
	}
	
	/**
	 * Returns true if the node is deleted.
	 * 
	 * Flag that is only present on an node that is deleted, meaning it is in the Trash view and has not been removed from the system.
	 * 
	 * @return {boolean}
	 */
	public boolean isDeleted() {
		return exists(ActivityXPath.deleted);
	}
	
	/**
	 * Set deleted flag for the node.
	 * 
	 * @param deleted
	 */
	public void setDeleted(boolean deleted) {
		setAsBoolean(ActivityXPath.deleted, deleted);
	}
	
	/**
	 * 
	 * @return {Field[]}
	 */
	public Field[] getFields() {
		if (fields == null) {
			if (getDataHandler() != null) {
				XmlDataHandler xmlHandler = (XmlDataHandler)getDataHandler();
				List<Node> fieldNodes = xmlHandler.getEntries(ActivityXPath.field);
				if (fieldNodes != null && !fieldNodes.isEmpty()) {
					fields = new ArrayList<Field>();
					for (Node fieldNode : fieldNodes) {
						fields.add(createField(fieldNode));
					}
				}
			}
		}
		return (fields == null) ? NO_FIELDS : fields.toArray(new Field[fields.size()]);
	}

	/**
	 * Set the fields
	 * 
	 * @param fields
	 */
	public void setFields(List<Field> fields) {
		this.fields = new ArrayList<Field>();
		this.fields.addAll(fields);
	}
	
	/**
	 * Return the first field with the specified name.
	 * 
	 * @param name
	 * @return {Field}
	 */
	public Field getFieldByName(String name) {
	    getFields();
		if (fields != null) {
			for (Field field : fields) {
				if (field.getName().equals(name)) {
					return field;
				}
			}
		}
		return null;
	}
	
	/**
	 * Return the field with the specified id.
	 * 
	 * @param fid
	 * @return {Field}
	 */
	public Field getFieldById(String fid) {
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
		if (fields == null) {
			fields = new ArrayList<Field>();
		}
		fields.add(field);
	}
	
	/**
	 * 
	 * @param updatedField
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
	
	/**
	 * Add an attachment
	 * 
	 * @param attachment
	 */
	public void addAttachment(ActivityAttachment attachment) {
		attachments.add(attachment);
	}
	
	/**
	 * Return true if this node has attachments
	 */
	public boolean hasAttachments() {
		return !attachments.isEmpty();
	}
	
	/**
	 * Return copy of attachments
	 * 
	 * @return {List<ActivityAttachment>}
	 */
	public List<ActivityAttachment> getAttachments() {
		if (attachments.isEmpty()) {
			return Collections.emptyList();
		}
		List<ActivityAttachment> copy = new ArrayList<ActivityAttachment>();
		copy.addAll(attachments);
		return copy;
	}
	
	/**
	 * Return the link.
	 * 
	 * @return {Link} link
	 */
	public Link getEnclosureLink() {
		if (enclosureLink == null && getDataHandler() != null) {
			enclosureLink = createLink((Node)getDataHandler().getData(), ActivityXPath.enclsoure_link);
		}
		return enclosureLink;
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
