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
package com.ibm.sbt.services.client.connections.bookmarks;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.ConnectionsFeedXpath;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

/**
 * @author mwallace
 *
 */
public class BookmarkList extends EntityList<Bookmark> {

	private XmlDataHandler metaHandler;
	
	/**
	 * Construct a BookmarkList instance.
	 * 
	 * @param response
	 * @param feedHandler
	 */
	public BookmarkList(Response response, IFeedHandler feedHandler) {
		super(response, feedHandler);
		
		metaHandler = new XmlDataHandler((Document)response.getData(), ConnectionsConstants.nameSpaceCtx);
    }

	/**
	 * Construct a BookmarkList instance.
	 * 
	 * @param response
	 * @param service
	 */
	public BookmarkList(Response response, BaseService service) {
		super(response, service);
		
		metaHandler = new XmlDataHandler((Document)response.getData(), ConnectionsConstants.nameSpaceCtx);
    }
	
	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.datahandlers.EntityList#createEntities()
	 */
	@Override
	protected ArrayList<Bookmark> createEntities() {
		XmlDataHandler dataHandler = new XmlDataHandler((Document)getData(), ConnectionsConstants.nameSpaceCtx);
		ArrayList<Bookmark> bookmarks = new ArrayList<Bookmark>();
		List<Node> nodes = dataHandler.getEntries(ConnectionsFeedXpath.Entry);
		for (Node node: nodes) {
			Bookmark bookmark = (Bookmark)getEntity(node);
			bookmarks.add(bookmark);
		}
		return bookmarks;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.datahandlers.ResponseProvider#getTotalResults()
	 */
	@Override
	public int getTotalResults() {
		return metaHandler.getAsInt(ConnectionsFeedXpath.TotalResults);
	}

	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.datahandlers.ResponseProvider#getStartIndex()
	 */
	@Override
	public int getStartIndex() {
		return metaHandler.getAsInt(ConnectionsFeedXpath.StartIndex);
	}

	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.datahandlers.ResponseProvider#getItemsPerPage()
	 */
	@Override
	public int getItemsPerPage() {
		return metaHandler.getAsInt(ConnectionsFeedXpath.ItemsPerPage);
	}

	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.datahandlers.ResponseProvider#getCurrentPage()
	 */
	@Override
	public int getCurrentPage() {
		return metaHandler.getAsInt(ConnectionsFeedXpath.CurrentPage);
	}
	
}
