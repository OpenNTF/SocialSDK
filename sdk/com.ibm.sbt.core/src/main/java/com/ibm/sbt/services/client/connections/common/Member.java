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
package com.ibm.sbt.services.client.connections.common;

import org.w3c.dom.Node;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.BaseService;

/**
 * Lists the elements in a member Atom entry.
 * You can create member entries in the Activities, Communities, and Forums applications. 
 * Most elements of a member are the same for all services.
 * The first table lists the common elements; the additional tables define extensions used by specific services.
 * 
 * @see
 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.5+API+Documentation#action=openDocument&res_title=Member_entry_content_ic45&content=pdcontent">
 *			Member entry content</a>
 * @author mwallace
 *
 */
public class Member extends AtomEntity {
	
	private String component;
	
	static public final String COMPONENT_ACTIVITIES = "http://www.ibm.com/xmlns/prod/sn/activities"; //$NON-NLS-1$
	static public final String COMPONENT_COMMUNITIES = "http://www.ibm.com/xmlns/prod/sn/communities"; //$NON-NLS-1$
	
	/**
	 * Specifies the member's role in the activity.
	 */
	static public final String ROLE_OWNER = "owner"; //$NON-NLS-1$
	static public final String ROLE_MEMBER = "member"; //$NON-NLS-1$
	static public final String ROLE_READER = "reader"; //$NON-NLS-1$

	/**
	 * Specifies the member type.
	 */
	static public final String TYPE_PERSON = "person"; //$NON-NLS-1$
	static public final String TYPE_GROUP = "group"; //$NON-NLS-1$
	static public final String TYPE_COMMUNITY = "community"; //$NON-NLS-1$
	static public final String TYPE_ORGANIZATION = "organization"; //$NON-NLS-1$

	/**
	 * Default constructor
	 */
	public Member() {
	}

	/**
	 * 
	 * @param type
	 * @param memberId
	 * @param role
	 */
	public Member(String type, String memberId, String role) {
		setComponent(COMPONENT_ACTIVITIES);
		setType(type);
		setContributor(memberId);
		setRole(role);
	}

	/**
	 * Construct Member based on the specified node
	 * 
	 * @param service
	 * @param node
	 * @param namespaceCtx
	 * @param xpathExpression
	 */
	public Member(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}
	
	@Override
	public String getId() {
		String id = super.getId();
		if (id != null) {
			if (id.lastIndexOf(':') != -1) {
				id = id.substring(id.lastIndexOf(':') + 1);
			}
			if (id.lastIndexOf("memberid=") != -1) {
				id = id.substring(id.lastIndexOf("memberid=") + 9);
			}
		}
		return id;
	}

	/**
	 * Returns the name of the member.
	 * 
	 * @return {String}
	 */
	public String getName() {
		return getTitle();
	}
	
	/**
	 * Sets the contributor from ATOM entry document.
	 * 
	 * @param id
	 */
	public void setContributor(String id) {
		Person person = new Person();
		if (isEmail(id)) {
			person.setEmail(id);
		} else {
			person.setUserid(id);
		}
		setContributor(person);
	}
	
	/**
	 * @param component the component to set
	 */
	public void setComponent(String component) {
		this.component = component;
	}
	
	/**
	 * @return {String} the component
	 */
	public String getComponent() {
		return component;
	}
	
	/**
	 * Specifies the member's role in the component.
	 * 
	 * @return {String} role
	 */
	public String getRole() {
		return getAsString(CommonXPath.role);
	}
	
	/**
	 * Specifies the member's role in the component.
	 * 
	 * @param role
	 */
	public void setRole(String role) {
		setAsString(CommonXPath.role, role);
	}
	
	/**
	 * Returns the member type.
	 * 
	 * @return {String}
	 */
	public String getType() {
		return getAsString(CommonXPath.type);
	}
	
	/**
	 * Specifies the member type.
	 * 
	 * @param type
	 */
	public void setType(String type) {
		setAsString(CommonXPath.type, type);
	}
	
	private boolean isEmail(String id) {
		return (id.indexOf('@') != -1);
	}
	 
}
