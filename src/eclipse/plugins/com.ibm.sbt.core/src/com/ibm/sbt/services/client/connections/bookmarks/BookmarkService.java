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

import java.util.HashMap;
import java.util.Map;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.connections.bookmarks.feedhandler.BookmarkFeedHandler;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * The Bookmarks application of IBM® Connections is a social bookmarking tool that you can use to save, organize, and share Internet and intranet bookmarks. 
 * The Bookmarks API allows application programs to read and write to the collections of bookmarks stored in the Bookmarks application.
 * 
 * @author mwallace
 *
 */
public class BookmarkService extends BaseService {
	
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
	}

	/**
	 * Construct an instance of the BookmarkService using the specified endpoint.
	 * 
	 * @param endpoint Endpoint to be associated with this service instance.
	 */
	public BookmarkService(Endpoint endpoint) {
		super(endpoint);
	}
	
	/**
	 * Return a list of bookmarks. 
	 * 
	 * @return List of bookmarks.
	 * @throws BookmarkServiceException
	 */
	public BookmarkList getAllBookmarks() throws BookmarkServiceException {
		return getAllBookmarks(null);
	}

	/**
	 * Return a list of bookmarks. You can search for a set of bookmarks that match a specific criteria by providing input parameters on the request.
	 * 
	 * @return List of bookmarks.
	 * @throws BookmarkServiceException
	 */
	public BookmarkList getAllBookmarks(Map<String, String> parameters) throws BookmarkServiceException {
		try {
			String url = BookmarkUrls.ALL.getUrl(this);
			return (BookmarkList)getEntities(url, parameters, new BookmarkFeedHandler(this));
		} catch (Exception e) {
			throw new BookmarkServiceException(e, "Error retrieving all bookmarks");
		} 
	}

	/**
	 * Return a list of specified users bookmarks. 
	 * 
	 * @param id
	 * @return List of specified users bookmarks.
	 * @throws BookmarkServiceException
	 */
	public BookmarkList getBookmarks(String id) throws BookmarkServiceException {
		return getBookmarks(id, null);
	}

	/**
	 * Return a list of specified users bookmarks. You can search for a set of bookmarks that match a specific criteria by providing input parameters on the request.
	 * 
	 * @return List of specified users bookmarks.
	 * @throws BookmarkServiceException
	 */
	public BookmarkList getBookmarks(String id, Map<String, String> parameters) throws BookmarkServiceException {
		if (StringUtil.isEmpty(id)) {
			throw new BookmarkServiceException(null, "Error retrieving bookmarks because invalid id was specified");
		}
		
		try {
			if (parameters == null) {
				parameters = new HashMap<String, String>();
			}
			parameters.put(isEmail(id) ? "email" : "userid", id);
			
			String url = BookmarkUrls.APP.getUrl(this);
			return (BookmarkList)getEntities(url, parameters, new BookmarkFeedHandler(this));
		} catch (Exception e) {
			throw new BookmarkServiceException(e, "Error retrieving bookmarks");
		} 
	}

	/**
	 * Retrieve a list of bookmarks ordered from the most popular to least popular. 
	 * 
	 * @return List of bookmarks.
	 * @throws BookmarkServiceException
	 */
	public BookmarkList getPopularBookmarks() throws BookmarkServiceException {
		return getPopularBookmarks(null);
	}

	/**
	 * Retrieve a list of bookmarks ordered from the most popular to least popular. 
	 * 
	 * @return List of bookmarks.
	 * @throws BookmarkServiceException
	 */
	public BookmarkList getPopularBookmarks(Map<String, String> parameters) throws BookmarkServiceException {
		try {
			String url = BookmarkUrls.POPULAR.getUrl(this);
			return (BookmarkList)getEntities(url, parameters, new BookmarkFeedHandler(this));
		} catch (Exception e) {
			throw new BookmarkServiceException(e, "Error retrieving popular bookmarks");
		} 
	}
	
	/**
	 * Retrieve a list of the bookmarks that other people have notified you about. 
	 * 
	 * @return List of bookmarks.
	 * @throws BookmarkServiceException
	 */
	public BookmarkList getMyNotifications() throws BookmarkServiceException {
		return getPopularBookmarks(null);
	}

	/**
	 * Retrieve a list of the bookmarks that other people have notified you about. 
	 * 
	 * @return List of bookmarks.
	 * @throws BookmarkServiceException
	 */
	public BookmarkList getMyNotifications(Map<String, String> parameters) throws BookmarkServiceException {
		try {
			String url = BookmarkUrls.MYNOTIFICATIONS.getUrl(this);
			return (BookmarkList)getEntities(url, parameters, new BookmarkFeedHandler(this));
		} catch (Exception e) {
			throw new BookmarkServiceException(e, "Error retrieving my notifications");
		} 
	}
	
	/**
	 * Retrieve a list of the bookmarks that you have notified other people about.
	 * 
	 * @return List of bookmarks.
	 * @throws BookmarkServiceException
	 */
	public BookmarkList getMySentNotifications() throws BookmarkServiceException {
		return getPopularBookmarks(null);
	}

	/**
	 * Retrieve a list of the bookmarks that you have notified other people about. 
	 * 
	 * @return List of bookmarks.
	 * @throws BookmarkServiceException
	 */
	public BookmarkList getMySentNotifications(Map<String, String> parameters) throws BookmarkServiceException {
		try {
			String url = BookmarkUrls.MYSENTNOTIFICATIONS.getUrl(this);
			return (BookmarkList)getEntities(url, parameters, new BookmarkFeedHandler(this));
		} catch (Exception e) {
			throw new BookmarkServiceException(e, "Error retrieving my sent notifications");
		} 
	}
	
//	/**
//	 * Create a bookmark.
//	 * 
//	 * @param bookmark
//	 * @return
//	 * @throws BookmarkServiceException
//	 */
//	public Bookmark createBookmark(Bookmark bookmark) throws BookmarkServiceException {
//		return null;
//	}
//	
//	/**
//	 * Get a bookmark.
//	 * 
//	 * @param bookmark
//	 * @return
//	 * @throws BookmarkServiceException
//	 */
//	public Bookmark getBookmark(String bookmarkUuid) throws BookmarkServiceException {
//		try {
//			String url = BookmarkUrls.???.getUrl(this);
//			return (Bookmark)getEntity(url, null, new BookmarkFeedHandler(this));
//		} catch (Exception e) {
//			throw new BookmarkServiceException(e, "Error retrieving bookmark: "+bookmarkUuid);
//		} 
//	}
//	
//	/**
//	 * Update a bookmark.
//	 * 
//	 * @param bookmark
//	 * @return
//	 * @throws BookmarkServiceException
//	 */
//	public Bookmark updateBookmark(Bookmark bookmark) throws BookmarkServiceException {
//		return null;
//	}
//	
//	/**
//	 * Delete a bookmark.
//	 * 
//	 * @param bookmark
//	 * @return
//	 * @throws BookmarkServiceException
//	 */
//	public Bookmark deleteBookmark(Bookmark bookmark) throws BookmarkServiceException {
//		return null;
//	}

}
