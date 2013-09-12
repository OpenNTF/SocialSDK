package com.ibm.sbt.services.client.connections.files;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.ConnectionsFeedXpath;
import com.ibm.sbt.services.client.connections.files.model.CommentEntry;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

public class CommentEntryList extends EntityList<CommentEntry> {

	public CommentEntryList(Response requestData, BaseService service) {
		super(requestData, service);
	}
	
	@Override
	public Document getData(){
		return (Document)super.getData();
	}
	
	@Override
	public FileService getService() {
		return (FileService)super.getService();
	}

	@Override
	protected ArrayList<CommentEntry> createEntities() {
		XmlDataHandler dataHandler = new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
		ArrayList<CommentEntry> commentEntries = new ArrayList<CommentEntry>();
		List<Node> entries = dataHandler.getEntries(FileEntryXPath.Entry);
		for (Node node: entries) {
			CommentEntry commentEntry = getService().newCommentEntry(node);
			commentEntries.add(commentEntry);
		}
		return commentEntries;
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
