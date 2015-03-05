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

package com.ibm.sbt.services.client.connections.wikis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.test.lib.TestEnvironment;

/**
 * @author Mario Duarte
 *
 */
public class WikiPageTest extends BaseWikiServiceTest {
	@Rule public ExpectedException thrown= ExpectedException.none();
	private Wiki wiki;
	
	@Before
	public void init() {
		wiki = createWiki();
	}
	
	@Test
	public void createWikiPageTest() throws Exception {
		WikiPage wikiPage = newWikiPage();
		WikiPage wikiPageCreated = wikiService.createWikiPage(wiki.getLabel(), wikiPage , null);
		
		assertEquals(unRandomize(wikiPage.getTitle()), unRandomize(wikiPageCreated.getTitle()));
		assertEquals(unRandomize(wikiPage.getSummary()), unRandomize(wikiPageCreated.getSummary()));
		assertNotNull(wikiPageCreated.getLabel());
	}
	
	@Test
	public void updateWikiPageTest() throws Exception {
		if (TestEnvironment.isSmartCloudEnvironment()) return;
		WikiPage wikiPage = wikiService.createWikiPage(wiki.getLabel(), newWikiPage() , null);
		
		wikiPage.setTitle("WikiPagelabel"+ System.currentTimeMillis());
		wikiPage.setSummary("Very basic summary "+System.currentTimeMillis());
		wikiPage.setContent("Content of wiki page "+System.currentTimeMillis());
		
		wikiService.updateWikiPage(wiki.getLabel(), wikiPage, null);
		
		WikiPage wikiPageGot = wikiService.getWikiPage(
				wiki.getLabel(), wikiPage.getLabel(), null);
		
		assertEquals(unRandomize(wikiPage.getLabel()), unRandomize(wikiPageGot.getLabel()));
		assertEquals(unRandomize(wikiPage.getTitle()), unRandomize(wikiPageGot.getTitle()));
		assertEquals(unRandomize(wikiPage.getSummary()), unRandomize(wikiPageGot.getSummary()));
	}
	
	@Test
	public void getAndDeleteWikiPageTest() throws Exception {
		WikiPage wikiPageCreated = wikiService.createWikiPage(wiki.getLabel(), newWikiPage() , null);
		
		WikiPage wikiPageGot = wikiService.getWikiPage(wiki.getLabel(), wikiPageCreated.getLabel(), null);
		
		assertEquals(wikiPageCreated.getLabel(), wikiPageGot.getLabel());
		assertEquals(wikiPageCreated.getTitle(), wikiPageGot.getTitle());
		assertEquals(wikiPageCreated.getSummary(), wikiPageGot.getSummary());
		
		wikiService.deleteWikiPage(wiki.getLabel(), wikiPageCreated.getLabel());
		thrown.expect(ClientServicesException.class);
		thrown.expectMessage("404:Not Found");
		wikiService.getWikiPage(wiki.getLabel(), wikiPageCreated.getLabel(), null);
	}
	
	@After
	public void cleanup() {
		deleteWikiSilently(wiki);
	}
}
