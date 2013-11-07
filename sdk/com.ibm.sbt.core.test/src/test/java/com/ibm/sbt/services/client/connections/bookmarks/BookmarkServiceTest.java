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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.endpoints.ConnectionsBasicEndpoint;

/**
 * @author mwallace
 *
 */
public class BookmarkServiceTest {

	@Test
	public void testGetAllBookmarks() {
		try {
			BookmarkService service = createBookmarkService();
			BookmarkList list = service.getAllBookmarks();
			assertValid(list);
			for (Bookmark bookmark : list) {
				assertValid(bookmark);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error calling BookmarkService.getAllBookmarks() caused by: "+e.getMessage());
		}
	}

	@Test
	public void testGetPrivateBookmarks() {
		try {
			BookmarkService service = createBookmarkService();
			Map<String, String> params = new HashMap<String, String>();
			params.put("access", "private");
			BookmarkList list = service.getAllBookmarks();
			assertNotNull("Expected non null BookmarkList", list);
			for (Bookmark bookmark : list) {
				assertValid(bookmark);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error calling BookmarkService.getAllBookmarks() caused by: "+e.getMessage());
		}
	}
	
	@Test
	public void testGetMyBookmarks() {
		try {
			BookmarkService service = createBookmarkService();
			Map<String, String> params = new HashMap<String, String>();
			params.put("email", "fadams@renovations.com");
			BookmarkList list = service.getAllBookmarks();
			assertNotNull("Expected non null BookmarkList", list);
			for (Bookmark bookmark : list) {
				assertValid(bookmark);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error calling BookmarkService.getAllBookmarks() caused by: "+e.getMessage());
		}
	}
	
	@Test
	public void testGetPopularBookmarks() {
		try {
			BookmarkService service = createBookmarkService();
			BookmarkList list = service.getPopularBookmarks();
			assertNotNull("Expected non null BookmarkList", list);
			for (Bookmark bookmark : list) {
				assertValid(bookmark);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error calling BookmarkService.getAllBookmarks() caused by: "+e.getMessage());
		}
	}
	
	@Test
	public void testGetMyNotifications() {
		try {
			BookmarkService service = createBookmarkService();
			BookmarkList list = service.getMyNotifications();
			assertNotNull("Expected non null BookmarkList", list);
			for (Bookmark bookmark : list) {
				assertValid(bookmark);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error calling BookmarkService.getAllBookmarks() caused by: "+e.getMessage());
		}
	}
	
	@Test
	public void testGetMySentNotifications() {
		try {
			BookmarkService service = createBookmarkService();
			BookmarkList list = service.getMySentNotifications();
			assertNotNull("Expected non null BookmarkList", list);
			for (Bookmark bookmark : list) {
				assertValid(bookmark);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error calling BookmarkService.getAllBookmarks() caused by: "+e.getMessage());
		}
	}
	
	private BookmarkService createBookmarkService() throws AuthenticationException {
		ConnectionsBasicEndpoint endpoint = new ConnectionsBasicEndpoint();
		endpoint.setUrl("https://qs.renovations.com:444");
		endpoint.setForceTrustSSLCertificate(true);
		endpoint.login("fadams", "passw0rd"); // TODO externalize these
		BookmarkService service = new BookmarkService(endpoint);
		return service;			
	}
	
	protected void assertValid(BookmarkList list) {
		assertNotNull("Expected non null BookmarkList", list);
		assertTrue("Invalid bookmark list total results", list.getTotalResults() != -1);
		assertTrue("Invalid bookmark list start index", list.getStartIndex() != -1);
		assertTrue("Invalid bookmark list items per page", list.getItemsPerPage() != -1);
		assertTrue("Invalid bookmark list current page", list.getCurrentPage() != -1);
	}
	
	protected void assertValid(BookmarkList list, long total, long start, long page, long current) {
		assertNotNull("Expected non null BookmarkList", list);
		assertEquals("Invalid bookmark list total results", total, list.getTotalResults());
		assertEquals("Invalid bookmark list start index", start, list.getStartIndex());
		assertEquals("Invalid bookmark list items per page", page, list.getItemsPerPage());
		assertEquals("Invalid bookmark list current page", current, list.getCurrentPage());
	}
	
	protected void assertValid(Bookmark bookmark) {
		assertNotNull("Invalid bookmark id", bookmark.getId());
		assertNotNull("Invalid bookmark title", bookmark.getTitle());
		assertNotNull("Invalid bookmark author", bookmark.getAuthor());
		assertNotNull("Invalid bookmark uuid", bookmark.getBookmarkUuid());
		assertTrue("Invalid bookmark click count", bookmark.getClickCount() != -1);
		assertTrue("Invalid bookmark link count", bookmark.getLinkCount() != -1);
	}
	
	protected void assertValid(Bookmark bookmark, String id, String title, String uuid, long clicks, long links) {
		assertEquals("Invalid bookmark id", id, bookmark.getId());
		assertEquals("Invalid bookmark title", title, bookmark.getTitle());
		assertEquals("Invalid bookmark uuid", uuid, bookmark.getBookmarkUuid());
		assertEquals("Invalid bookmark click count", clicks, bookmark.getClickCount());
		assertEquals("Invalid bookmark link count", links, bookmark.getLinkCount());
	}
	
}
