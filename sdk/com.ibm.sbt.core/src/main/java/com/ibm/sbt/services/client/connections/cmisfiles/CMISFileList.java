/*
 *  Copyright IBM Corp. 2013
 * 
 * Licensed under the Apache License Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing software 
 * distributed under the License is distributed on an "AS IS" BASIS 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.sbt.services.client.connections.cmisfiles;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.ConnectionsFeedXpath;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.cmisfiles.feedHandler.CMISFileFeedHandler;
/**
 * @Represents Connections CMIS File List
 * @author Vimal Dhupar
 */
public class CMISFileList extends EntityList<CMISFile> {

	public CMISFileList(Response requestData, CMISFileFeedHandler feedHandler) {
		super(requestData, feedHandler);
	}
	
	@Override
	public Document getData(){
		return (Document)super.getData();
	}
	
	@Override
	public CMISFileService getService() {
		return (CMISFileService)super.getService();
	}

	@Override
	public CMISFileFeedHandler getFeedHandler() {
		return (CMISFileFeedHandler)super.getFeedHandler();
	}
	
	@Override
	public CMISFile getEntity(Object data) {
		return (CMISFile)super.getEntity(data);
	}
	
	@Override
	protected ArrayList<CMISFile> createEntities() {
		XmlDataHandler dataHandler = new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
		ArrayList<CMISFile> fileEntries = new ArrayList<CMISFile>();
		List<Node> entries = dataHandler.getEntries(AtomXPath.entry);
		for (Node node: entries) {
			CMISFile fileEntry = getEntity(node);
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
