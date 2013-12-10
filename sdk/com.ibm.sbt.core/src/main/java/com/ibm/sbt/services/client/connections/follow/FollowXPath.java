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

package com.ibm.sbt.services.client.connections.follow;

/**
 * Class used in resolving XPaths from Follow service
 * @author Manish Kataria
 */


import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;

public enum FollowXPath implements FieldEntry{
	
    Type("a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/type']/@term"),
    Source("a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/source']/@term"),
    ResourceType("a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/resource-type']/@term"),
    ResourceId("a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/resource-id']/@term"),
    RelatedUrl("a:link[@rel='related']/@href");
	
	private final XPathExpression path;
	
	private FollowXPath(String xpath) {
		XPathExpression xpathExpr = null;
		try {
			xpathExpr = DOMUtil.createXPath(xpath);
		} catch (XMLException e) {
			e.printStackTrace();
		}
		this.path = xpathExpr;
	}
	
	
	@Override
	public Object getPath() {
		return path;
	}

	@Override
	public String getName() {
		return this.name();
	}

}
