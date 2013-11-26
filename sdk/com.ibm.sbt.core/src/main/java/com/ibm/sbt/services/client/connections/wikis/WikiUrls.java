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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ibm.commons.runtime.util.URLEncoding;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.util.AuthUtil;

/**
 * @author Mario Duarte
 *
 */
public enum WikiUrls {
	ALL_WIKIS(             "wikis/{0}/api/wikis/feed"),
	PUBLIC_WIKIS(          "wikis/{0}/api/wikis/public"),
	MY_WIKIS(              "wikis/{0}/api/mywikis/feed"),
	MOST_COMMENTED_WIKIS(  "wikis/{0}/anonymous/api/wikis/mostcommented"),
	MOST_RECOMMENDED_WIKIS("wikis/{0}/anonymous/api/wikis/mostrecommended"),
	MOST_VISITED_WIKIS(    "wikis/{0}/anonymous/api/wikis/mostvisited"),
	WIKI_MYPAGES(          "wikis/{0}/api/wiki/{1}/mypages"),
	WIKI_PAGES_TRASH(      "wikis/{0}/anonymous/api/wiki/{1}/recyclebin/feed"),
	WIKI_PAGES(            "wikis/{0}/anonymous/api/wiki/{1}/feed"),
	WIKI_PAGES_AUTH(       "wikis/{0}/api/wiki/{1}/feed"),
	WIKI(                  "wikis/{0}/anonymous/api/wiki/{1}/entry"),
	WIKI_AUTH(             "wikis/{0}/api/wiki/{1}/entry"),
	WIKI_PAGE(             "wikis/{0}/anonymous/api/wiki/{1}/page/{2}/entry"),
	WIKI_PAGE_AUTH(        "wikis/{0}/api/wiki/{1}/page/{2}/entry");
	
	private String urlPattern;
	
	private WikiUrls(String urlPattern) {
		this.urlPattern = urlPattern;
	}
	
	protected String format(String... args) {
		return formatPattern(Arrays.asList(args));
	}
	
	public String format(Endpoint endpoint, String... args) {
		List<String> list = new ArrayList<String>(Arrays.asList(args));
		list.add(0, getAuth(endpoint));
		return formatPattern(list);
	}
	
	private String formatPattern(List<String> args) {
		List<String> encoded = new ArrayList<String>();
		for(String arg : args) {
			try {
				encoded.add(URLEncoding.encodeURIString(arg, "UTF-8", 0, false));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return StringUtil.format(urlPattern, encoded.toArray());
	}
	
	private String getAuth(Endpoint endpoint) {
		 return AuthUtil.INSTANCE.getAuthValue(endpoint);
	}
}
