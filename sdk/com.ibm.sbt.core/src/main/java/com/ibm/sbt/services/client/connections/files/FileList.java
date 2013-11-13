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

public class FileList extends EntityList<File> {

	public FileList(Response requestData, FileFeedHandler feedHandler) {
		super(requestData, feedHandler);
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
	public FileFeedHandler getFeedHandler() {
		return (FileFeedHandler)super.getFeedHandler();
	}
	
	@Override
	public File getEntity(Object data) {
		return (File)super.getEntity(data);
	}
	
	@Override
	protected ArrayList<File> createEntities() {
		XmlDataHandler dataHandler = new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
		ArrayList<File> fileEntries = new ArrayList<File>();
		List<Node> entries = dataHandler.getEntries(FileEntryXPath.Entry);
		for (Node node: entries) {
			File fileEntry = getEntity(node);
			fileEntries.add(fileEntry);
		}
		return fileEntries;
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
