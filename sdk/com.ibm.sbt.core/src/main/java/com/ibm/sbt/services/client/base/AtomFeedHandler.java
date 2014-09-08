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

package com.ibm.sbt.services.client.base;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.AtomEntityList;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;

/**
 * @author Mario Duarte
 * @author Carlos Manias
 *
 */
public abstract class AtomFeedHandler<T extends AtomEntity> implements IFeedHandler<T> {

	private BaseService service;
	private boolean isFeed;
	

	/**
	 * 
	 * @param service
	 */
	public AtomFeedHandler(BaseService service) {
		this(service, true);
	}

	/**
	 *
	 * @param service
	 * @param isFeed True by default. If false, it uses an xPath for a single entry. If true, it uses an xPath for a feed
	 */
	public AtomFeedHandler(BaseService service, boolean isFeed) {
		this.service = service;
		this.isFeed = isFeed;
	}
	
	@Override
	public T createEntity(Response response) {
		if (response == null) {
			return null;
		}
		Object data = response.getData();
		if (data instanceof Node) {
			return createEntityFromData((Node)data);
		} else {
			return null;
		}
	}

	@Override
	public T createEntityFromData(Object data) {
		return newEntity(service, (Node)data);
	}

	@Override
	public EntityList<T> createEntityList(Response dataHolder) {
		return new AtomEntityList<T>(dataHolder, this);
	}

	@Override
	public BaseService getService() {
		return service;
	}
	
	protected XPathExpression getEntityXPath(Node node){
		if (node instanceof Document){
			return (XPathExpression)(isFeed?AtomXPath.entry.getPath():AtomXPath.singleEntry.getPath());
		} else {
			 return null;
		}
	}

	protected T newEntity(BaseService service, Node node){
		XPathExpression xpath = getEntityXPath(node);
		return entityInstance(service, node, xpath);
	}

	 abstract protected T entityInstance(BaseService service, Node node, XPathExpression xpath);
}
