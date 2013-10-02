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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.search.feedhandler.FacetsHandler;


/**
 * Class used in representing List of FacetValue objects of Search service
 * @author Mark Wallace
 */
public class FacetValueList extends EntityList<FacetValue> {

	public FacetValueList(Response requestData, BaseService service) {
		super(requestData, service);
	}
	
	public FacetValueList(Response requestData, FacetsHandler feedHandler) {
		super(requestData, feedHandler);
	}
	
	private XmlDataHandler getMetaDataHandler(){
		return new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
	}

	@Override
	public int getTotalResults() {
		return 0; // TODO
	}

	@Override
	public int getStartIndex() {
		return 0; // TODO
	}

	@Override
	public int getItemsPerPage() {
		return 0; // TODO
	}

	@Override
	public int getCurrentPage() {
		return 0; // TODO
	}
	
	@Override
	public Document getData(){
		return (Document)super.getData();
	}
	
	@Override
	protected FacetValue getEntity(Object data){
		return (FacetValue)super.getEntity(data);
	}

	@Override
	protected ArrayList<FacetValue> createEntities() {
		XmlDataHandler dataHandler = new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
		ArrayList<FacetValue> results = new ArrayList<FacetValue>();
		List<Node> entries = dataHandler.getEntries(createXPath());
		for (Node node: entries) {
			FacetValue facetValue =  getEntity(node);
			results.add(facetValue);
		}
		return results;
	}
	
	private String createXPath() {
		FacetsHandler facetsHandler = (FacetsHandler)getFeedHandler();
		return StringUtil.replace("/a:feed/ibmsc:facets/ibmsc:facet[@id='{facet.id}']/ibmsc:facetValue", "{facet.id}", facetsHandler.getFacetId());
	}
	
}
