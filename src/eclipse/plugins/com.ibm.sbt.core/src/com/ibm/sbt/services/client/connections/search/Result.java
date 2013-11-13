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

/**
 * Result model object
 * 
 * @author Manish Kataria 
 */


public class Result extends AtomEntity{
	
	public Result(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}
	
	
    /**
     * Returns tags associated with search results
     * @return String
     */
	public String getTags(){
		return(getAsString(SearchXPath.tags));
	}
	
	
    /**
     * Relevance ranking of the search result. 
     * This is a relative score of an individual search result. 
	 * Values typically range between 0 and 1, but values greater than 1 can result and should be considered equivalent to a score of 1     
     * @return String
     */
	public String getScore(){
		return(getAsString(SearchXPath.score));
	}
	
	public String getRank(){
		return(getAsString(SearchXPath.rank));
	}
	
	/**
	 * Identifies the IBM® Connections application in which the result was found
	 * @return String
	 */
	public String getApplication(){
		return(getAsString(SearchXPath.application));
	}
}