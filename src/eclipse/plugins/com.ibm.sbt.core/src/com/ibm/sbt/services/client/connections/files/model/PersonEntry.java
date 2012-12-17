/*
 * © Copyright IBM Corp. 2012
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
package com.ibm.sbt.services.client.connections.files.model;

/**
 * Person Entry Class - representing a Person Entry of the File.
 * 
 * @author Vimal Dhupar
 */
import org.w3c.dom.Document;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.connections.files.utils.ContentMapFiles;
import com.ibm.sbt.services.client.connections.files.utils.NamespacesConnections;

public class PersonEntry {
	private String		name;
	private String		email;
	private String		userState;
	private String		userUuid;
	private Document	data;

	public Document getData() {
		return data;
	}

	public void setData(Document data) {
		this.data = data;
	}

	private String getUserUuid(String xPathIdentifier) {
		if (!StringUtil.isEmpty(userUuid)) {
			return userUuid;
		}
		return get(xPathIdentifier);
	}

	private void setUserUuid(String userUuId) {
		this.userUuid = userUuId;
	}

	private String getName(String xPathIdentifier) {
		if (!StringUtil.isEmpty(name)) {
			return name;
		}
		return get(xPathIdentifier);
	}

	private void setName(String name) {
		this.name = name;
	}

	private String getEmail(String xPathIdentifier) {
		if (!StringUtil.isEmpty(email)) {
			return email;
		}
		return get(xPathIdentifier);
	}

	private void setEmail(String email) {
		this.email = email;
	}

	private String getUserState(String xPathIdentifier) {
		if (!StringUtil.isEmpty(userState)) {
			return userState;
		}
		return get(xPathIdentifier);
	}

	private void setUserState(String userState) {
		this.userState = userState;
	}

	public PersonEntry getAuthorEntry() {
		setEmail(getEmail("emailFromEntry"));
		setName(getName("nameOfUserFromEntry"));
		setUserState(getUserState("userStateFromEntry"));
		setUserUuid(getUserUuid("userUuidFromEntry"));
		return this;
	}

	public PersonEntry getModifierEntry() {
		setEmail(getEmail("emailModifier"));
		setName(getName("nameModifier"));
		setUserState(getUserState("userStateModifier"));
		setUserUuid(getUserUuid("userUuidModifier"));
		return this;
	}

	public String getEmail() {
		return this.email;
	}

	public String getName() {
		return this.name;
	}

	public String getUserState() {
		return this.userState;
	}

	public String getUserUuid() {
		return this.userUuid;
	}

	/**
	 * get
	 * 
	 * @param fieldName
	 * @return
	 */
	private String get(String fieldName) {
		String xpQuery = getXPathQuery(fieldName);
		return getFieldUsingXPath(xpQuery);
	}

	/**
	 * getXPathQuery
	 * 
	 * @return xpath query for specified field. Field names follow IBM Connections naming convention
	 */
	private String getXPathQuery(String fieldName) {
		return ContentMapFiles.xpathMap.get(fieldName);
	}

	/**
	 * getFieldUsingXPath
	 * 
	 * @return Execute xpath query on Profile XML
	 */
	private String getFieldUsingXPath(String xpathQuery) {
		String result = null;
		try {
			result = DOMUtil.value(this.data, xpathQuery, NamespacesConnections.nameSpaceCtx);
		} catch (XMLException e) {
			// System.err.println("Error in getFieldUsingXPath .. xpathQuery is : " + xpathQuery);
		}
		return result;
	}
}
