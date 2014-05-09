/*
 * © Copyright IBM Corp. 2013
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

package com.ibm.sbt.services.client.base.serializers;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespace;
import com.ibm.sbt.services.client.connections.common.Person;

/**
 * 
 * @author Mario Duarte
 *
 */
public class PersonSerializer extends BaseEntitySerializer<Person> {
	private static final String NAME = "name";
	private static final String EMAIL = "email";
	private static final String USER_ID = "userid";
	private static final String USER_STATE = "userState";

	public PersonSerializer(Person entity) {
		super(entity);
	}
	
	   /**
     * Returns a full or partial xml representation of a Person,
     * depending on the fields with data
     * @param nodeName
     * @return
     */
    public Node xmlNode(String nodeName) {
        return xmlNode(nodeName, null);
    }
	
	/**
	 * Returns a full or partial xml representation of a Person,
	 * depending on the fields with data
	 * @param nodeName
	 * @param namespaceUri
	 * @return
	 */
	public Node xmlNode(String namespaceUri, String nodeName) {
		Element[] textElements = new Element[getNumFields()];
		int index = 0;
		if (StringUtil.isNotEmpty(entity.getName())) {
			textElements[index++] = textElement(NAME, entity.getName());
		}
		if (StringUtil.isNotEmpty(entity.getEmail())) {
			textElements[index++] = textElement(EMAIL, entity.getEmail());
		}
		if (StringUtil.isNotEmpty(entity.getId())) {
			textElements[index++] = textElement(Namespace.SNX.getUrl(), USER_ID, entity.getId());
		}
		if (StringUtil.isNotEmpty(entity.getUserState())) {
			textElements[index] = textElement(Namespace.SNX.getUrl(), USER_STATE, entity.getUserState());
		}
		Element e;
		if (namespaceUri==null)
		    e = element(nodeName);
		else 
		    e = element(namespaceUri, nodeName);
		return appendChildren(rootNode(e), textElements);
	}
	
	private int getNumFields(){
		int nodeCount = 0;
		if (StringUtil.isNotEmpty(entity.getName())) {
			nodeCount++;
		}
		if (StringUtil.isNotEmpty(entity.getEmail())) {
			nodeCount++;
		}
		if (StringUtil.isNotEmpty(entity.getId())) {
			nodeCount++;
		}
		if (StringUtil.isNotEmpty(entity.getUserState())) {
			nodeCount++;
		}
		return nodeCount;
	}
}
