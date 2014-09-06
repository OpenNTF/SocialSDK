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
package com.ibm.sbt.services.client.connections.forums;

import org.w3c.dom.Node;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.common.Link;
import com.ibm.sbt.services.client.connections.common.Person;

/**
 * @author mwallace
 *
 */
public class Field extends BaseEntity {

	/*
	 * Defines the field type. The following values are supported:
	 */
	static final public String DATE = "date"; //$NON-NLS-1$
	static final public String FILE = "file"; //$NON-NLS-1$
	static final public String LINK = "link"; //$NON-NLS-1$
	static final public String PERSON = "person"; //$NON-NLS-1$
	static final public String TEXT = "text"; //$NON-NLS-1$
	
	/**
	 * Default constructor
	 */
	public Field() {
	}

	/**
	 * Construct Field based on the specified node
	 * 
	 * @param service
	 * @param node
	 * @param namespaceCtx
	 * @param xpathExpression
	 */
	public Field(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, new XmlDataHandler(node, namespaceCtx, xpathExpression));
	}
	
	/**
	 * Unique identifier of the field. 
	 * This identifier is assigned by Activities. You do not need to provide a value for this attribute 
	 * if you are creating a field, but you must specify a value for this field if you are updating, 
	 * replacing, or deleting a field.
	 * Required when updating, not posting.
	 * 
	 * @return fid
	 */
	public String getFid() {
		return getAsString(ForumsXPath.field_fid);
	}
	
	/**
	 * Return true if the field is hidden.
	 * 
	 * @return hidden
	 */
	public boolean isHidden() {
		if (fields.containsKey(ForumsXPath.field_hidden.getName())){
			return (Boolean)fields.get(ForumsXPath.field_hidden.getName());
		}
		return exists(ForumsXPath.field_hidden);
	}
	
	/**
	 * Set the hidden state for this field.
	 * 
	 * @param hidden
	 */
	public void setHidden(boolean hidden) {
		setAsBoolean(ForumsXPath.field_hidden, hidden);
	}
	
	/**
	 * This name is displayed in the entry as the field label, similar to the Title or Description field 
	 * labels that are included in the entry by default. Limited to 64 bytes.
	 * 
	 * @return name
	 */
	public String getName() {
		return getAsString(ForumsXPath.field_name);
	}
	
	/**
	 * Set the name for this field.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		setAsString(ForumsXPath.field_name, name);
	}
	
	/**
	 * Position of the field in the containing Activity entry's array of fields. 
	 * Use numbers separated by large increments to allow for repositioning. 
	 * For example, use 1000, 2000, 3000, and so on.
	 * 
	 * @return
	 */
	public long getPosition() {
		return getAsLong(ForumsXPath.field_position);
	}
	
	/**
	 * Set the position for this field.
	 * 
	 * @param position
	 */
	public void setPosition(long position) {
		setAsLong(ForumsXPath.field_position, position);
	}
	
	/**
	 * Defines the field type. The following values are supported:
	 *    date
	 *    file
	 *    link
	 *    person
	 *    text
	 * 
	 * @return
	 */
	public String getType() {
		return getAsString(ForumsXPath.field_type);
	}

	/**
	 * Create a person from the specified node
	 */
	public Person createPerson(Node node, FieldEntry fieldEntry) {
		return new Person(getService(), new XmlDataHandler(node, ConnectionsConstants.nameSpaceCtx, (XPathExpression)fieldEntry.getPath()));
	}
		
	/**
	 * Create a link to from the specified node
	 */
	protected Link createLink(Node node, FieldEntry fieldEntry) {
		return new Link(getService(), new XmlDataHandler(node, ConnectionsConstants.nameSpaceCtx, (XPathExpression)fieldEntry.getPath()));
	}
	
}
