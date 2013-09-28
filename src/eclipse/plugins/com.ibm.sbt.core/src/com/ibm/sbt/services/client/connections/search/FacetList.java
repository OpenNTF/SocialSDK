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

import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.ConnectionsFeedXpath;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.search.feedhandler.FacetFeedHandler;

public class FacetList extends EntityList<Facet>{
	

	public FacetList(Response requestData, BaseService service) {
		super(requestData, service);
	}
	
	public FacetList(Response requestData, FacetFeedHandler feedHandler) {
		super(requestData, feedHandler);
	}
	
	@Override
	public Document getData(){
		return (Document)super.getData();
	}
	
	@Override
	protected ArrayList<Facet> createEntities() {
		XmlDataHandler dataHandler = new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
		ArrayList<Facet> results = new ArrayList<Facet>();
		List<Node> entries = dataHandler.getEntries(SearchXPath.facetEntry);
		for (Node node: entries) {
			XmlDataHandler facetDataHandler = new XmlDataHandler(node, ConnectionsConstants.nameSpaceCtx);
			List<Node> idNodes = (List<Node>) facetDataHandler.getEntries(SearchXPath.facetValueId);
			List<Node> labelNodes = (List<Node>) facetDataHandler.getEntries(SearchXPath.facetLabel);
			List<Node> weightNodes = (List<Node>) facetDataHandler.getEntries(SearchXPath.facetWeight);
			for(int i=0;i<idNodes.size();i++){
				Facet facet = new Facet();
				facet.setId(idNodes.get(i).getNodeValue());
				facet.setLabel(labelNodes.get(i).getNodeValue());
				facet.setWeight(weightNodes.get(i).getNodeValue());
				results.add(facet);
			}
		}
		return results;
	}
	
	private XmlDataHandler getMetaDataHandler(){
		return new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
	}

	@Override
	public int getTotalResults() {
		return getMetaDataHandler().getAsInt(ConnectionsFeedXpath.TotalResults);
	}

	@Override
	public int getStartIndex() {
		return getMetaDataHandler().getAsInt(ConnectionsFeedXpath.StartIndex);
	}

	@Override
	public int getItemsPerPage() {
		return getMetaDataHandler().getAsInt(ConnectionsFeedXpath.ItemsPerPage);
	}

	@Override
	public int getCurrentPage() {
		return getMetaDataHandler().getAsInt(ConnectionsFeedXpath.CurrentPage);
	}
	
	

}
