/*
 * © Copyright IBM Corp. 2012
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
package com.ibm.sbt.sample.app;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.wikis.WikiPage;
import com.ibm.sbt.services.client.connections.wikis.WikiService;
import com.ibm.sbt.services.endpoints.BasicEndpoint;

/**
 * @author mwallace
 * @date 14 January 2014
 */
public class ExportWiki {

	private BasicEndpoint endpoint;
	private WikiService wikiService;

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
	/**
	 * 
	 * @param url
	 * @param user
	 * @param password
	 * @throws AuthenticationException
	 */
	public ExportWiki(String url, String user, String password)
			throws AuthenticationException {
		BasicEndpoint basicEndpoint = new BasicEndpoint();
		basicEndpoint.setUrl(url);
		basicEndpoint.setForceTrustSSLCertificate(true);
		basicEndpoint.setUser(user);
		basicEndpoint.setPassword(password);

		this.wikiService = new WikiService();
		this.setEndpoint(basicEndpoint);
	}

	/**
	 * 
	 * @param endpoint
	 *            The endpoint you want this class to use.
	 * @throws AuthenticationException
	 */
	public void setEndpoint(BasicEndpoint endpoint)
			throws AuthenticationException {
		this.endpoint = endpoint;
		this.wikiService.setEndpoint(this.endpoint);
	}

	public void export(String wikiLabel, String outputFolder) throws FileNotFoundException, ClientServicesException {
		long start = System.currentTimeMillis();
		
		int page = 0;
		int pageSize = 50;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("page", StringUtil.toString(page));
		params.put("ps", StringUtil.toString(pageSize));
		params.put("sortBy", "modified");
		params.put("sortOrder", "desc");

		EntityList<WikiPage> wikiList = wikiService.getMyWikiPages(wikiLabel, params);
		int totalResults = wikiList.getTotalResults();
		int totalPosts = wikiList.size();
		
		while (true) {
			System.out.println("Read "+totalPosts+" of "+totalResults+" wikis posts.");

			for (Iterator<WikiPage> iter = wikiList.iterator(); iter.hasNext(); ) {
				WikiPage wikiPage = iter.next();
			}
			
			params.put("page", StringUtil.toString(++page));
			wikiList = wikiService.getWikiPages(wikiLabel, params);
			
			if (wikiList.isEmpty()) {
				break;
			}
			
			totalResults = wikiList.getTotalResults();
			totalPosts += wikiList.size();
		}
		/*
		File file = new File(csvFile);
		PrintWriter printWriter = new PrintWriter(file);
		
		List<WikiPostEntry> entries = new ArrayList<ExportWiki.WikiPostEntry>();
		
		int page = 0;
		int pageSize = 50;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("page", StringUtil.toString(page));
		params.put("ps", StringUtil.toString(pageSize));
		params.put("sortBy", "modified");
		params.put("sortOrder", "desc");
		
		WikiPostList postList = wikiService.getWikiPosts(wikiHandle, params);
		
		int totalResults = postList.getTotalResults();
		int totalPosts = postList.size();
		
		while (true) {
			System.out.println("Read "+totalPosts+" of "+totalResults+" wikis posts.");

			for (Iterator<BaseWikiEntity> iter = postList.iterator(); iter.hasNext(); ) {
				entries.add(new WikiPostEntry((WikiPost)iter.next()));
			}
			
			params.put("page", StringUtil.toString(++page));
			postList = wikiService.getWikiPosts(wikiHandle, params);
			
			if (postList.isEmpty()) {
				break;
			}
			
			totalResults = postList.getTotalResults();
			totalPosts += postList.size();
		}

		printWriter.print("\"Title\",\"Comment\",\"Author\",\"Published\",\"Contributor\",\"Updated\",\"Content\",\"Comments\",\"Recommendations\",\"Hits\",\"Tags\"\n");
		for (Iterator<WikiPostEntry> iter = entries.iterator(); iter.hasNext(); ) {
			iter.next().write(printWriter);
		}
		
		printWriter.close();
		*/
		
		long duration = (System.currentTimeMillis() - start) / 1000;
		System.out.println("Export took: "+duration+"(secs)");
	}
	
	/**
	 * Demo.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("Usage: java com.ibm.sbt.sample.app.ExplortWiki <connections_url> <wiki_label> <output_folder>");
			return;
		}
		
		String url = args[0];
		String wikiLabel = args[1];
		String outputFolder = args[2];
		String user = args[3];
		String password = args[4];
		
		ExportWiki exportWiki = null;
		try {
			exportWiki = new ExportWiki(url, user, password);
			exportWiki.export(wikiLabel, outputFolder);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
}