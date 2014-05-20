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

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import java.util.Map;

import org.w3c.dom.Node;

import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.AtomFeedHandler;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsService;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * The Bookmarks application of IBM Connections is a social bookmarking tool that you can use to save, organize, and share Internet and intranet bookmarks. 
 * The Bookmarks API allows application programs to read and write to the collections of bookmarks stored in the Bookmarks application.
 * 
 * @author mwallace
 * @author Carlos Manias
 *
 */
public class BookmarkService extends ConnectionsService {

	private static final long serialVersionUID = -363260957215796869L;

	/**
	 * Construct an instance of the BookmarkService using the default "connections" endpoint.
	 */
	public BookmarkService() {
		super(DEFAULT_ENDPOINT_NAME);
	}

	/**
	 * Construct an instance of the BookmarkService using the specified endpoint.
	 * 
	 * @param endpointName Name of the endpoint to be associated with this service instance.
	 */
	public BookmarkService(String endpointName) {
		super(endpointName);
		serviceMappingKeys = new String[]{"dogear"};
	}

	/**
	 * Construct an instance of the BookmarkService using the specified endpoint.
	 * 
	 * @param endpoint Endpoint to be associated with this service instance.
	 */
	public BookmarkService(Endpoint endpoint) {
		super(endpoint);
		serviceMappingKeys = new String[]{"dogear"};
	}

	/***************************************************************
	 * Working with Bookmarks
	 ****************************************************************/
	
	/**
	 * Return a list of all bookmarks.
	 * 
	 * @return A List Of Bookmarks
	 * @throws ClientServicesException
	 */
	public EntityList<Bookmark> getAllBookmarks() throws ClientServicesException {
		return getAllBookmarks(null);
	}
	
	/**
	 * Return a list of bookmarks. You can search for a set of bookmarks that match a specific criteria by providing input parameters on the request.
	 * 
	 * @return List of bookmarks.
	 * @throws BookmarkServiceException
	 */
	public EntityList<Bookmark> getAllBookmarks(Map<String, String> parameters) throws ClientServicesException {
		String url = BookmarkUrls.ALL.format(this);
		return getBookmarkEntityList(url, parameters);
	}

	/**
	 * Return a list of specified users bookmarks. 
	 * 
	 * @param id
	 * @return List of specified users bookmarks.
	 * @throws BookmarkServiceException
	 */
	public EntityList<Bookmark> getBookmarks(String id) throws ClientServicesException {
		return getBookmarks(id, null);
	}

	/**
	 * Return a list of specified users bookmarks. You can search for a set of bookmarks that match a specific criteria by providing input parameters on the request.
	 * 
	 * @return List of specified users bookmarks.
	 * @throws ClientServicesException
	 */
	public EntityList<Bookmark> getBookmarks(String id, Map<String, String> parameters) throws ClientServicesException {
		String url = BookmarkUrls.APP.format(this,BookmarkUrls.getUserId(id));
		return getBookmarkEntityList(url, parameters);
		
	}

	/**
	 * Retrieve a list of bookmarks ordered from the most popular to least popular. 
	 * 
	 * @return List of bookmarks.
	 * @throws ClientServicesException
	 */
	public EntityList<Bookmark> getPopularBookmarks() throws ClientServicesException {
		return getPopularBookmarks(null);
	}

	/**
	 * Retrieve a list of bookmarks ordered from the most popular to least popular. 
	 * 
	 * @return List of bookmarks.
	 * @throws ClientServicesException
	 */
	public EntityList<Bookmark> getPopularBookmarks(Map<String, String> parameters) throws ClientServicesException {		
		String url = BookmarkUrls.POPULAR.format(this);
		return getBookmarkEntityList(url, parameters);
		
	}
	
	/**
	 * Retrieve a list of the bookmarks that other people have notified you about. 
	 * 
	 * @return List of bookmarks.
	 * @throws ClientServicesException
	 */
	public EntityList<Bookmark> getMyNotifications() throws ClientServicesException {
		return getMyNotifications(null);
	}

	/**
	 * Retrieve a list of the bookmarks that other people have notified you about. 
	 * 
	 * @return List of bookmarks.
	 * @throws ClientServicesException
	 */
	public EntityList<Bookmark> getMyNotifications(Map<String, String> parameters) throws ClientServicesException {
		String url = BookmarkUrls.MYNOTIFICATIONS.format(this);
		return getBookmarkEntityList(url, parameters);
 
	}
	
	/**
	 * Retrieve a list of the bookmarks that you have notified other people about.
	 * 
	 * @return List of bookmarks.
	 * @throws ClientServicesException
	 */
	public EntityList<Bookmark> getMySentNotifications() throws ClientServicesException {
		return getMySentNotifications(null);
	}

	/**
	 * Retrieve a list of the bookmarks that you have notified other people about. 
	 * 
	 * @return List of bookmarks.
	 * @throws ClientServicesException
	 */
	public EntityList<Bookmark> getMySentNotifications(Map<String, String> parameters) throws ClientServicesException {
		String url = BookmarkUrls.MYSENTNOTIFICATIONS.format(this);
		return getBookmarkEntityList(url, parameters);

	}
	
	/***************************************************************
	 * FeedHandlers for each entity type
	 ****************************************************************/
	
	/**
	 * Factory method to instantiate a FeedHandler for Bookmarks
	 * @return IFeedHandler<Bookmark>
	 */
	protected IFeedHandler<Bookmark> getBookmarkFeedHandler(){
		return new AtomFeedHandler<Bookmark>(this) {
			@Override
			protected Bookmark entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Bookmark(service, node, nameSpaceCtx, xpath);
			}
		};
	};
	
	/***************************************************************
	 * Factory methods
	 ****************************************************************/
	
	protected EntityList<Bookmark> getBookmarkEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getBookmarkFeedHandler());
	}

}
