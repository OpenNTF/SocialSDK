/*
 * � Copyright IBM Corp. 2013
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

package com.ibm.sbt.services.client.connections.wikis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.sbt.services.client.ClientServicesException;

/**
 * @author Mario Duarte
 *
 */
public class WikiPageTest extends BaseWikiServiceTest {
	private Wiki wiki;
	
	@Before
	public void init() {
		wiki = createWiki();
	}
	
	@Test @Ignore
	public void createWikiPageTest() throws Exception {
		WikiPage wikiPage = newWikiPage();
		WikiPage wikiPageCreated = wikiService.createWikiPage(
				wiki.getLabel(), wikiPage , null);
		
		assertEquals(wikiPage.getTitle(), wikiPageCreated.getTitle());
		assertEquals(wikiPage.getSummary(), wikiPageCreated.getSummary());
		assertNotNull(wikiPageCreated.getLabel());
	}
	
	@Test @Ignore
	public void updateWikiPageTest() throws Exception {
		WikiPage wikiPage = wikiService.createWikiPage(
				wiki.getLabel(), newWikiPage() , null);
		
		wikiPage.setTitle("Test wiki page "+ System.currentTimeMillis());
		wikiPage.setSummary("Very basic summary "+System.currentTimeMillis());
		wikiPage.setContent("Content of wiki page "+System.currentTimeMillis());
		
		wikiService.updateWikiPage(wiki.getLabel(), wikiPage, null);
		
		WikiPage wikiPageGot = wikiService.getWikiPage(
				wiki.getLabel(), wikiPage.getLabel(), null);
		
		assertEquals(wikiPage.getLabel(), wikiPageGot.getLabel());
		assertEquals(wikiPage.getTitle(), wikiPageGot.getTitle());
		assertEquals(wikiPage.getSummary(), wikiPageGot.getSummary());
	}
	
	@Test
	public void getAndDeleteWikiPageTest() throws Exception {
		WikiPage wikiPageCreated = wikiService.createWikiPage(
				wiki.getLabel(), newWikiPage() , null);
		
		WikiPage wikiPageGot = wikiService.getWikiPage(
				wiki.getLabel(), wikiPageCreated.getLabel(), null);
		
		assertEquals(wikiPageCreated.getLabel(), wikiPageGot.getLabel());
		assertEquals(wikiPageCreated.getTitle(), wikiPageGot.getTitle());
		assertEquals(wikiPageCreated.getSummary(), wikiPageGot.getSummary());
		
		wikiService.deleteWikiPage(wiki.getLabel(), wikiPageCreated.getLabel());
		
		try {
			wikiService.getWikiPage(wiki.getLabel(), wikiPageCreated.getLabel(), null);
			fail("Getting a wiki page that does no longer exist should throw an exception.");
		}
		catch(ClientServicesException e) {
			assertEquals(404, e.getResponseStatusCode());
		}
	}
	
	@After
	public void cleanup() {
		deleteWikiSilently(wiki);
	}
}
