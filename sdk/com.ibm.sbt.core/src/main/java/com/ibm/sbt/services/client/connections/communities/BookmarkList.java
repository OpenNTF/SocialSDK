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
package com.ibm.sbt.services.client.connections.communities;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.ConnectionsFeedXpath;
import com.ibm.sbt.services.client.connections.communities.feedhandler.BookmarkFeedHandler;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

/**
 * 
 * @author Swati Singh
 *
 */
public class BookmarkList extends EntityList<Bookmark> {

	public BookmarkList(Response requestData, BookmarkFeedHandler feedHandler) {
		super(requestData, feedHandler);
	}
	
	@Override
	public Document getData(){
		return (Document)super.getData();
	}
	
	@Override
	public CommunityService getService() {
		return (CommunityService)super.getService();
	}

	@Override
	public BookmarkFeedHandler getFeedHandler() {
		return (BookmarkFeedHandler)super.getFeedHandler();
	}
	
	@Override
	protected Bookmark getEntity(Object data){
		return (Bookmark)super.getEntity(data);
	}
	
	@Override
	protected ArrayList<Bookmark> createEntities() {
		XmlDataHandler dataHandler = new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
		ArrayList<Bookmark> bookmarks = new ArrayList<Bookmark>();
		List<Node> entries = dataHandler.getEntries(ConnectionsFeedXpath.Entry);
		for (Node node: entries) {
			Bookmark bookmark = getEntity(node);
			bookmarks.add(bookmark);
		}
		return bookmarks;
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
