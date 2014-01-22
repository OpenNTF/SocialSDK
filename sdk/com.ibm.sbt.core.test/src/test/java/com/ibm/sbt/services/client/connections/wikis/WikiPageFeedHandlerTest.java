/*
 *  Copyright IBM Corp. 2013
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

import org.junit.Test;

import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.serializers.BaseEntitySerializer.DateSerializer;

/**
 * 
 * @author Mario Duarte
 *
 */
public class WikiPageFeedHandlerTest extends BaseWikiServiceTest {
	
	@Test
	public void testWikiPage() {
		String wikiFilePath = "wikipage.xml";
		IFeedHandler<WikiPage> wikiPageFeedHandler = wikiService.getWikiPageFeedHandler();
		
		WikiPage wikiPage = wikiPageFeedHandler.createEntity(
				createResponseFromResource(wikiFilePath));
		
		assertEquals("Setup and configuration", wikiPage.getTitle());
		assertEquals("Setup and configuration", wikiPage.getLabel());
		assertEquals("urn:lsid:ibm.com:td:adb6e2dc-acac-467d-be88-ee1afe964ae1", wikiPage.getId());
		assertEquals("adb6e2dc-acac-467d-be88-ee1afe964ae1", wikiPage.getUuid());
		
		assertNotNull(wikiPage.getAuthor());
		assertEquals("Frank Adams", wikiPage.getAuthor().getName());
		assertEquals("0F19F8AD-37EA-6033-8525-7BBF005634B5", wikiPage.getAuthor().getId());
		assertEquals("fadams@renovations.com", wikiPage.getAuthor().getEmail());
		assertEquals("active", wikiPage.getAuthor().getState());
		
		assertNotNull(wikiPage.getModifier());
		assertEquals("Frank Adams", wikiPage.getModifier().getName());
		assertEquals("0F19F8AD-37EA-6033-8525-7BBF005634B5", wikiPage.getModifier().getId());
		assertEquals("fadams@renovations.com", wikiPage.getModifier().getEmail());
		assertEquals("active", wikiPage.getModifier().getState());
		
		assertEquals("2013-11-28T15:06:58.000Z", DateSerializer.toString(wikiPage.getPublished()));
		assertEquals("2013-11-28T15:07:50.000Z", DateSerializer.toString(wikiPage.getUpdated()));
		assertEquals("2013-11-28T15:06:58.000Z", DateSerializer.toString(wikiPage.getCreated()));
		assertEquals("2013-11-28T15:06:58.000Z", DateSerializer.toString(wikiPage.getModified()));
	
		assertEquals("1", wikiPage.getVersionLabel());
		assertEquals("a7852910-e40c-426d-8ed8-b68ccd1bfa2f", wikiPage.getVersionUuid());
		
		assertEquals(6, wikiPage.getNumberOfAnonymousViews());
		assertEquals(0, wikiPage.getNumberOfAttachments());
		assertEquals(3, wikiPage.getNumberOfComments());
		assertEquals(1, wikiPage.getNumberOfRecomendations());
		assertEquals(1, wikiPage.getNumberOfVersions());
		assertEquals(4, wikiPage.getNumberOfViews());
	}
}
