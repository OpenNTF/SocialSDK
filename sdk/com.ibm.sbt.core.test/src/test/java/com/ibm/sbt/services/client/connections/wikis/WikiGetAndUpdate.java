/*
 * �� Copyright IBM Corp. 2013
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mario Duarte
 *
 */
public class WikiGetAndUpdate extends BaseWikiServiceTest {
	protected Wiki wiki;
	protected Wiki wikiCreated;
	
	@Before
	public void createWikiInit() {
		wiki = newWiki();
		wikiCreated = createWiki(wiki);
	}
	
	@Test
	public void getWikiTest() throws Exception {
		Wiki wikiGot = wikiService.getWiki(wikiCreated.getLabel(), null);
		
		assertEquals(unRandomize(wiki.getTitle()), unRandomize(wikiGot.getTitle()));
		assertEquals(unRandomize(wikiCreated.getLabel()), unRandomize(wikiGot.getLabel()));
		assertEquals(wikiCreated.getPermissions(), wikiGot.getPermissions());
		assertEquals(unRandomize(wiki.getSummary()), unRandomize(wikiGot.getSummary()));
		assertEquals(wikiCreated.getTags(), wikiGot.getTags());
	}
	
	@Test
	public void updateWikiTest() throws Exception {
		Map<String,String> getParams = new HashMap<String, String>();
		getParams.put("includeTags", "true");
		getParams.put("acls", "true");
		
		Wiki wikiGot = wikiService.getWiki(wikiCreated.getLabel(), getParams);
		
		wikiGot.setTitle("New title " + System.currentTimeMillis());
		wikiGot.setSummary("New Summary " + System.currentTimeMillis());
		List<String> tags = new ArrayList<String>();
		tags.add("test_tag1"); tags.add("test_tag2");
		wikiGot.setTags(tags);
		
		Map<String,String> updateParams = null;
		wikiService.updateWiki(wikiGot, updateParams);
		
		Wiki wikiUpdated = wikiService.getWiki(wikiGot.getLabel(), getParams);
		
		assertEquals(unRandomize(wikiGot.getTitle()), unRandomize(wikiUpdated.getTitle()));
		assertEquals(unRandomize(wikiGot.getLabel()), unRandomize(wikiUpdated.getLabel()));
		assertEquals(unRandomize(wikiGot.getSummary()), unRandomize(wikiUpdated.getSummary()));
		assertEquals(wikiGot.getTags(), wikiUpdated.getTags());
	}
	
	@After
	public void deleteWiki() {
		deleteWikiSilently(wikiCreated);
	}
}
