package com.ibm.sbt.services.client.connections.wikis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ibm.sbt.services.client.ClientServicesException;

public class WikiCreateAndDeleteTest extends BaseWikiServiceTest {

	@Test
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
