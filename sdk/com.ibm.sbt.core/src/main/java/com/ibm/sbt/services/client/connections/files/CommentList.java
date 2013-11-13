package com.ibm.sbt.services.client.connections.files;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.ConnectionsFeedXpath;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.files.feedHandler.*;
import com.ibm.sbt.services.client.connections.files.model.FileEntryXPath;

public class CommentList extends EntityList<Comment> {

	public CommentList(Response requestData, CommentFeedHandler handler) {
		super(requestData, handler);
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
	public CommentFeedHandler getFeedHandler() {
		return (CommentFeedHandler)super.getFeedHandler();
	}
	
	@Override
	public Comment getEntity(Object data) {
		return (Comment)super.getEntity(data);
	}
	
	@Override
	protected ArrayList<Comment> createEntities() {
		XmlDataHandler dataHandler = new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
		ArrayList<Comment> commentEntries = new ArrayList<Comment>();
		List<Node> entries = dataHandler.getEntries(FileEntryXPath.Entry);
		for (Node node: entries) {
			Comment commentEntry = getEntity(node);
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
