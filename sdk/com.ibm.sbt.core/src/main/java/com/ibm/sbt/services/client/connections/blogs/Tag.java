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

package com.ibm.sbt.services.client.connections.blogs;

import org.w3c.dom.Node;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.profiles.model.ProfileXPath;

/**
 * tag Entry Class - representing a tag associated with a Profile.
 *
 * @author Swati Singh
 */
public class Tag extends AtomEntity {


	 /**
     * 
     * @param service
     * @param node
     * @param namespaceCtx
     * @param xpathExpression
     */
	public Tag(BaseService service, Node node, NamespaceContext namespaceCtx, 
			XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}

	public String getTerm() {
		return getAsString(ProfileXPath.term);
	}

	public void setTerm(String term) {
		setAsString(ProfileXPath.term,term);
	}

	public int getFrequency(){
		return getAsInt(ProfileXPath.frequency);
	}
	public int getIntensity() {
		return getAsInt(ProfileXPath.intensity);
	}

	public int getVisibility() {
		return getAsInt(ProfileXPath.visibility);
	}

}