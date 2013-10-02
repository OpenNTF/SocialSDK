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
import java.util.Properties;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
	
	private void assertValid(Bookmark bookmark) {
		assertNotNull("Invalid bookmark id", bookmark.getId());
		assertNotNull("Invalid bookmark title", bookmark.getTitle());
		assertNotNull("Invalid bookmark author", bookmark.getAuthor());
	}
	
}
