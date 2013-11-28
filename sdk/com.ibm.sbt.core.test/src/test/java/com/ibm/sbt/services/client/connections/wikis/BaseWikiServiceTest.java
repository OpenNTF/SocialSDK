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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;

/**
 * @author Mario Duarte
 *
 */
public class BaseWikiServiceTest extends BaseUnitTest {
	protected WikiService wikiService;
	
	@Before
	public void initWikiServiceTest() {
		wikiService = new WikiService();
	}
	
	protected Wiki newWiki() {
		Wiki wiki = new Wiki(wikiService);
		wiki.setTitle("Test wiki "+ System.currentTimeMillis());
		wiki.setSummary("Very basic summary "+System.currentTimeMillis());
		Set<String> permissions = new HashSet<String>();
		permissions.add("Edit"); permissions.add("View"); permissions.add("Delete"); 
		wiki.setPermissions(permissions);
		List<String> tags = new ArrayList<String>();
		tags.add("tag1"); tags.add("rag2"); tags.add("tag3"); 
		wiki.setTags(tags);
		return wiki;
	}
	
	protected Wiki createWiki() {
		return createWiki(newWiki());
	}
	
	protected Wiki createWiki(Wiki wiki) {
		try {
			return wikiService.createWiki(wiki, null);
		} catch (ClientServicesException e) {
			throw new RuntimeException("Failed to create a wiki.", e);
		}
	}
	
	protected WikiPage newWikiPage() {
		WikiPage wikiPage = new WikiPage(wikiService);
		wikiPage.setTitle("Test wiki page "+ System.currentTimeMillis());
		wikiPage.setSummary("Very basic summary "+System.currentTimeMillis());
		wikiPage.setContent("Content of wiki page "+System.currentTimeMillis());
		return wikiPage;
	}
	
	protected void deleteWikiSilently(Wiki wiki) {
		try {
			wikiService.deleteWiki(wiki.getLabel());
		} catch (ClientServicesException e) {}
	}
	
	protected static Response createResponseFromResource(String resourcePath) {
		InputStream in = BaseWikiServiceTest.class.getResourceAsStream(resourcePath);
		if(in == null) {
			Assert.fail("Could not find resource at: " + resourcePath);
			throw new RuntimeException();
		}
		else return createResponseFromInputStream(in);
	}
	
	protected static Response createResponseFromInputStream(InputStream inputSteam) {
		try {
			return new Response(DOMUtil.createDocument(inputSteam));
		} 
		catch (XMLException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected static String inputStreamToString(InputStream inputStream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		try {
			StringBuilder out = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				out.append(line);
			}
			return out.toString();
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
		finally {
			try {
				reader.close();
			}
			catch(Exception e) {}
		}
	}
	
	protected static <T> Set<T> toSet(Collection<T> col) {
		return new HashSet<T>(col);
	}
}
