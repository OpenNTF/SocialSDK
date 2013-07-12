package com.ibm.sbt.services.client.connections.profiles;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.ConnectionsFeedXpath;
import com.ibm.sbt.services.client.connections.profiles.feedhandlers.ConnectionEntryFeedHandler;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

public class ConnectionEntryList extends EntityList<ConnectionEntry> {
	
	public ConnectionEntryList(Response requestData, ConnectionEntryFeedHandler feedHandler) {
		super(requestData, feedHandler);
	}
	
	@Override
	public Document getData(){
		return (Document)super.getData();
	}
	
	@Override
	public ProfileService getService() {
		return (ProfileService)super.getService();
	}

	@Override
	public ConnectionEntryFeedHandler getFeedHandler() {
		return (ConnectionEntryFeedHandler)super.getFeedHandler();
	}
	
	@Override
	protected ConnectionEntry getEntity(Object data){
		return (ConnectionEntry)super.getEntity(data);
	}
	
	@Override
	protected ArrayList<ConnectionEntry> createEntities() {
		XmlDataHandler dataHandler = new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
		ArrayList<ConnectionEntry> connectionEntries = new ArrayList<ConnectionEntry>();
		List<Node> entries = dataHandler.getEntries(ConnectionsFeedXpath.Entry);
		for (Node node: entries) {
			ConnectionEntry connectionEntry = getEntity(node);
			connectionEntries.add(connectionEntry);
		}
		return connectionEntries;
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
