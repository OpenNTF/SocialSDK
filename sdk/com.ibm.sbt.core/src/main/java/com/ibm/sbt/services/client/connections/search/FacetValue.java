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

package com.ibm.sbt.services.client.connections.search;

import org.w3c.dom.Node;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

/**
 * Facet model object
 * 
 * @author Mark Wallace 
 */
public class FacetValue extends AtomEntity{
	
	/**
	 * Constructor
	 *  
	 * @param searchService SearchService
	 * @param id FacetId
	 */
	public FacetValue(SearchService searchService, String id) {
		setService(searchService);
		setAsString(FacetValueXPath.uid, id);
	}

	public FacetValue(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}
	
	public FacetValue(){}

	public FacetValue(BaseService svc, XmlDataHandler handler) {
		super(svc,handler);
	}
	
	public String getId(){
		String id = getAsString(FacetValueXPath.id);
		int index = id.indexOf('/');
		return (index == -1) ? id : id.substring(index+1);
	}
	
	public String getLabel(){
		return getAsString(FacetValueXPath.label);
	}
	
	public float getWeight(){
		return getAsFloat(FacetValueXPath.weight);
	}

}