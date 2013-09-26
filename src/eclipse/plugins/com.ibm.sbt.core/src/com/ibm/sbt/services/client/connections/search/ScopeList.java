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
import com.ibm.sbt.services.client.connections.search.feedhandler.ScopeFeedHandler;

/**
 * Class used in representing List of Result objects of Search service
 * @author Manish Kataria
 */

public class ScopeList extends EntityList<Scope>{
	
	public ScopeList(Response requestData, BaseService service) {
		super(requestData, service);
	}
	
	public ScopeList(Response requestData, ScopeFeedHandler feedHandler) {
		super(requestData, feedHandler);
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
	
	@Override
	public Document getData(){
		return (Document)super.getData();
	}

	@Override
	protected Scope getEntity(Object data){
		return (Scope)super.getEntity(data);
	}

	@Override
	protected ArrayList<Scope> createEntities() {
		XmlDataHandler dataHandler = new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
		ArrayList<Scope> results = new ArrayList<Scope>();
		List<Node> entries = dataHandler.getEntries(ConnectionsFeedXpath.Entry);
		for (Node node: entries) {
			Scope searchResult =  getEntity(node);
			results.add(searchResult);
		}
		return results;
	}

}
