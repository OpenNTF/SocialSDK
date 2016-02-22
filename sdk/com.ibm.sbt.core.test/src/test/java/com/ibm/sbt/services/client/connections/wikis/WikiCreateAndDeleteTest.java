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

import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.test.lib.TestEnvironment;

/**
 * @author Mario Duarte
 *
 */
public class WikiCreateAndDeleteTest extends BaseWikiServiceTest {
	@Rule public ExpectedException thrown= ExpectedException.none();

	@Test
	public void createWikiTest() throws Exception {
		//FIXME: Describe what the test is doing
		Wiki wiki = newWiki();
		
		Map<String,String> params = new HashMap<String, String>();
		Wiki wikiReturned = wikiService.createWiki(wiki, params);
		
		assertNotNull(wikiReturned.getLabel());
		assertEquals(unRandomize(wiki.getTitle()), unRandomize(wikiReturned.getTitle()));
		
		deleteWikiSilently(wikiReturned);
	}
	
	@Test
	public void deleteWikiTest() throws Exception {
		//FIXME: Describe what the test is doing
		if (TestEnvironment.isSmartCloudEnvironment()) return;
		Wiki createdWiki = createWiki();
		
		Wiki wikiGot = wikiService.getWiki(createdWiki.getLabel(), null);
		assertEquals(createdWiki.getLabel(), wikiGot.getLabel());
		
		//Checks when the Label for the Wiki Doesn't exist
		thrown.expect(ClientServicesException.class);
		thrown.expectMessage("Wiki Not Found");
		wikiService.deleteWiki(createdWiki.getLabel()+"INVALID");
		
		// Checks when the Label for the Wiki Doesn't Exist 
		thrown.expect(ClientServicesException.class);
		thrown.expectMessage("Wiki Not Found");
		wikiService.getWiki(createdWiki.getLabel()+"INVALID", null);
	}
}
