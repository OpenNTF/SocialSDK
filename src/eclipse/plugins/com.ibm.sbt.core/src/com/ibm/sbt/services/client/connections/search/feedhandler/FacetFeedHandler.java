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

package com.ibm.sbt.services.client.connections.search.feedhandler;

import org.w3c.dom.Node;

import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.search.FacetList;
import com.ibm.sbt.services.client.connections.search.SearchService;

/**
 * Feed handler for Search Service / Facets
 * 
 * @author Manish Kataria
 */

public class FacetFeedHandler implements IFeedHandler{
	
	private final SearchService service;
	
	/**
	 * Constructor
	 * 
	 * @param service
	 */
	public FacetFeedHandler(SearchService service){
		this.service = service;
	}

	@Override
	public BaseEntity createEntity(Response response) {
		Node data = (Node)response.getData();
		return createEntityFromData(data);
	}


	@Override
	public EntityList<? extends BaseEntity>createEntityList(Response requestData) {
		return new FacetList((Response)requestData, this);
	}

	@Override
	public BaseService getService() {
		return service;
	}

	@Override
	public BaseEntity createEntityFromData(Object data) {
		return null;
	}

}
