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

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.ibm.sbt.services.client.ClientServicesException;

/**
 * @author Mario Duarte
 *
 */
public class WikiCreateAndDeleteTest extends BaseWikiServiceTest {

	@Test @Ignore
	public void createWikiTest() throws Exception {
		Wiki wiki = newWiki();
		
		Map<String,String> params = new HashMap<String, String>();
		Wiki wikiReturned = wikiService.createWiki(wiki, params);
		
		assertNotNull(wikiReturned.getLabel());
		assertEquals(wiki.getTitle(), wikiReturned.getTitle());
		
		deleteWikiSilently(wikiReturned);
	}
	
	@Test
	public void deleteWikiTest() throws Exception {
		Wiki createdWiki = createWiki();
		
		Wiki wikiGot = wikiService.getWiki(createdWiki.getLabel(), null);
		assertEquals(createdWiki.getLabel(), wikiGot.getLabel());
		
		wikiService.deleteWiki(createdWiki.getLabel());
		
		try {
			wikiService.getWiki(createdWiki.getLabel(), null);
			fail("Getting a wiki that does no longer exist should throw an exception.");
		}
		catch(ClientServicesException e) {
			assertEquals(404, e.getResponseStatusCode());
		}
	}
}
