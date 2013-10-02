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
package com.ibm.sbt.services.client.connections.bookmarks.feedhandler;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.blogs.Blog;
import com.ibm.sbt.services.client.connections.blogs.BlogList;
import com.ibm.sbt.services.client.connections.blogs.model.BaseBlogEntity;
import com.ibm.sbt.services.client.connections.blogs.model.BlogXPath;
import com.ibm.sbt.services.client.connections.bookmarks.Bookmark;
import com.ibm.sbt.services.client.connections.bookmarks.BookmarkList;
import com.ibm.sbt.services.client.connections.bookmarks.BookmarkService;

/**
 * @author mwallace
 *
 */
public class BookmarkFeedHandler implements IFeedHandler {
	
	private BookmarkService service;
	
	public BookmarkFeedHandler(BookmarkService service) {
		this.service = service;
	}

	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.IFeedHandler#createEntity(com.ibm.sbt.services.client.Response)
	 */
	@Override
	public BaseEntity createEntity(Response response) {
		Node data = (Node)response.getData();
		return createEntityFromData(data);
	}

	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.IFeedHandler#createEntityFromData(java.lang.Object)
	 */
	@Override
	public BaseEntity createEntityFromData(Object data) {
		XPathExpression xpath = (data instanceof Document) ? (XPathExpression)AtomXPath.singleEntry.getPath() : null;
		Bookmark bookmark = new Bookmark(service, (Node)data, ConnectionsConstants.nameSpaceCtx, xpath);
		return bookmark;
	}

	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.IFeedHandler#createEntityList(com.ibm.sbt.services.client.Response)
	 */
	@Override
	public EntityList<? extends BaseEntity> createEntityList(Response response) {
		BookmarkList bookmarkList = new BookmarkList(response, this);
		return bookmarkList;
	}

	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.IFeedHandler#getService()
	 */
	@Override
	public BaseService getService() {
		return service;
	}

}
