/*
 * © Copyright IBM Corp. 2014
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

import static com.ibm.sbt.services.client.base.ConnectionsConstants.v4_0;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.URLBuilder;
import com.ibm.sbt.services.client.base.URLContainer;
import com.ibm.sbt.services.client.base.Version;
import com.ibm.sbt.services.client.base.VersionedUrl;

/**
 * This class handles and formats the URLS for the WikiService
 * 
 * @author Mario Duarte
 * @author Carlos Manias
 */
public enum WikiUrls implements URLContainer {
	ALL_WIKIS(             new VersionedUrl(v4_0, "{wikis}/{authType}/api/wikis/feed")),
	PUBLIC_WIKIS(          new VersionedUrl(v4_0, "{wikis}/{authType}/api/wikis/public")),
	MY_WIKIS(              new VersionedUrl(v4_0, "{wikis}/{authType}/api/mywikis/feed")),
	MOST_COMMENTED_WIKIS(  new VersionedUrl(v4_0, "{wikis}/{authType}/anonymous/api/wikis/mostcommented")),
	MOST_RECOMMENDED_WIKIS(new VersionedUrl(v4_0, "{wikis}/{authType}/anonymous/api/wikis/mostrecommended")),
	MOST_VISITED_WIKIS(    new VersionedUrl(v4_0, "{wikis}/{authType}/anonymous/api/wikis/mostvisited")),
	WIKI_MYPAGES(          new VersionedUrl(v4_0, "{wikis}/{authType}/api/wiki/{wikiLabel}/mypages")),
	WIKI_PAGES_TRASH(      new VersionedUrl(v4_0, "{wikis}/{authType}/anonymous/api/wiki/{wikiLabel}/recyclebin/feed")),
	WIKI_PAGES(            new VersionedUrl(v4_0, "{wikis}/{authType}/{authenticated}/api/wiki/{wikiLabel}/feed")),
	WIKI(                  new VersionedUrl(v4_0, "{wikis}/{authType}/{authenticated}/api/wiki/{wikiLabel}/entry")),
	WIKI_PAGE(             new VersionedUrl(v4_0, "{wikis}/{authType}/{authenticated}/api/wiki/{wikiLabel}/page/{wikiPage}/entry"));
	
	private URLBuilder builder;
	
	private WikiUrls(VersionedUrl... urlVersions) {
		builder = new URLBuilder(urlVersions);
	}
	
	public String format(BaseService service, NamedUrlPart... args) {
		return builder.format(service, args);
	}

	public String getPattern(Version version){
		return builder.getPattern(version).getUrlPattern();
	}
	
	public static NamedUrlPart getWikiPage(String value){
		return get("wikiPage", value);
	}

	public static NamedUrlPart getWikiLabel(String value){
		return get("wikiLabel", value);
	}

	private static NamedUrlPart get(String name, String value){
		return new NamedUrlPart(name, value);
	}
}
