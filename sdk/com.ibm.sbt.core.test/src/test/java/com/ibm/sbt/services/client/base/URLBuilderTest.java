/* * �� Copyright IBM Corp. 2014 * 
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
package com.ibm.sbt.services.client.base;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.connections.activitystreams.ASApplication;
import com.ibm.sbt.services.client.connections.activitystreams.ASGroup;
import com.ibm.sbt.services.client.connections.activitystreams.ASUser;
import com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamService;
import com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamUrls;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.communities.CommunityUrls;
import com.ibm.sbt.services.client.connections.files.AccessType;
import com.ibm.sbt.services.client.connections.files.FileService;
import com.ibm.sbt.services.client.connections.files.FileUrlParts;
import com.ibm.sbt.services.client.connections.files.FileUrls;
import com.ibm.sbt.services.client.connections.wikis.WikiService;
import com.ibm.sbt.services.client.connections.wikis.WikiUrlParts;
import com.ibm.sbt.services.client.connections.wikis.WikiUrls;

/**
 * 
 * @author Carlos Manias
 *
 */
public class URLBuilderTest extends BaseUnitTest {
	@Rule 
	public ExpectedException thrown= ExpectedException.none();

	@Test
	public void testGenerateURLPredefinedParameters() {
		CommunityService service = new CommunityService();
		//URL pattern is "communities/service/atom/{authType}/{communityEntity}/{communityType}"
		String url = CommunityUrls.COMMUNITY_MEMBERS.format(service);
		assertEquals("communities/service/atom/community/members", url);
	}

	@Test
	public void testGenerateURLVariableParameters() {
		WikiService service = new WikiService();
		//URL pattern is "wikis/{authType}/api/wiki/{wikiLabel}/page/{wikiPage}/entry"
		String wikiLabel = "myWikiLabel";
		String pageLabel = "myPageLabel";
		String url = WikiUrls.WIKI_PAGE.format(service, WikiUrlParts.wikiLabel.get(wikiLabel), WikiUrlParts.wikiPage.get(pageLabel), WikiUrls.authenticated(true));
		assertEquals("wikis/basic/api/wiki/"+wikiLabel+"/page/"+pageLabel+"/entry", url);
	}

	@Test
	public void testGenerateURLUnsortedParameters() {
		WikiService service = new WikiService();
		//URL pattern is "wikis/{authType}/api/wiki/{wikiLabel}/page/{wikiPage}/entry"
		String wikiLabel = "myWikiLabel";
		String pageLabel = "myPageLabel";
		//This url has the correct parameters but in a different order. It works regardless. The first parameter needs to be the Version, though
		String url = WikiUrls.WIKI_PAGE.format(service, WikiUrlParts.wikiPage.get(pageLabel), WikiUrlParts.wikiLabel.get(wikiLabel), WikiUrls.authenticated(true));
		assertEquals("wikis/basic/api/wiki/"+wikiLabel+"/page/"+pageLabel+"/entry", url);
	}

	@Test
	public void testGenerateURLRigtNumberOfParametersButWrongParameter() {
		WikiService service = new WikiService();
		//URL pattern is "wikis/{authType}/api/wiki/{wikiLabel}/page/{wikiPage}/entry"
		String wikiLabel = "myWikiLabel";
		String pageLabel = "myPageLabel";
		//This url has the correct number of parameters but one of the parameters is not correct
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Missing parameter");
		String url = WikiUrls.WIKI_PAGE.format(service, WikiUrlParts.wikiLabel.get(wikiLabel), FileUrlParts.fileId.get("dummy"), WikiUrls.authenticated(true));
		assertEquals("wikis/basic/api/wiki/"+wikiLabel+"/page/"+pageLabel+"/entry", url);
	}

	@Test
	public void testGenerateURLVariableAndEmptyParameters() {
		WikiService service = new WikiService();
		//URL pattern is "wikis/{authType}/api/wiki/{wikiLabel}/page/{wikiPage}/entry
		//When a parameter is empty, it is simply removed from the url
		String url2 = WikiUrls.WIKI_PAGE.format(service, WikiUrlParts.wikiLabel.get(""), WikiUrlParts.wikiPage.get("myPageLabel"), WikiUrls.authenticated(true));
		assertEquals("wikis/basic/api/wiki/page/myPageLabel/entry", url2);
	}

	@Test
	public void testGenerateURLMissingParameters() {
		WikiService service = new WikiService();
		String userId = "abcde";
		String fileId = "1234";
		String commentId = "a1b2c3"; //unsed on this test
		//This would be the url with all the needed parameters
		//String url = FileUrls.SINGLE_COMMENT_USER_FILE.format(getApiVersion(), getAuthTypeBasic(), AccessType.PUBLIC.get(), FileUrlParts.userId.get(userId), FileUrlParts.fileId.get(fileId), FileUrlParts.commentId.get(commentId));

		//This url misses the last parameter, commentId
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Missing parameter");
		String url = FileUrls.USERLIBRARY_DOCUMENT_FEED.format(service, FileUrlParts.userId.get(userId), FileUrlParts.fileId.get(fileId));
	}

	@Test
	public void testGenerateURLExtraParameters() {
		FileService service = new FileService();
		String userId = "abcde";
		String fileId = "1234";
		String commentId = "a1b2c3";
		//This would be the url with all the needed parameters
		//String url = FileUrls.SINGLE_COMMENT_USER_FILE.format(getApiVersion(), getAuthTypeBasic(), AccessType.PUBLIC.get(), FileUrlParts.userId.get(userId), FileUrlParts.fileId.get(fileId), FileUrlParts.commentId.get(commentId));

		String url = FileUrls.USERLIBRARY_DOCUMENT_COMMENT_ENTRY.format(service, AccessType.PUBLIC.get(), FileUrlParts.userId.get(userId), FileUrlParts.fileId.get(fileId), FileUrlParts.commentId.get(commentId), 
				ASUser.ME.get(), WikiUrlParts.wikiLabel.get("value")); //The last 2 parameters are not needed, they are simply ignored
		String assertUrl = "files/basic/anonymous/api/userlibrary/"+userId+"/document/"+fileId+"/comment/"+commentId+"/entry";
		assertEquals(assertUrl, url);
	}

	@Test
	public void testGenerateURLVariableParameters2() {
		ActivityStreamService service = new ActivityStreamService();
		//URL pattern is "connections/opensocial/{authType}/rest/{service}/{user}/{group}/{application}"
		// Those variables are @me @all and @status URLEncoded
		String user = "%40me";
		String group = "%40all";
		String app = "%40status";

		String urlAssert = "connections/opensocial/basic/rest/activitystreams/"+user+"/"+group+"/"+app;
		String url = ActivityStreamUrls.AS_USER_GROUP_APP.format(service, ASUser.getByName("me"), ASGroup.getByName("all"), ASApplication.getByName("status"));
		assertEquals(urlAssert, url);

		String url2 = ActivityStreamUrls.AS_ME_ALL_STATUS.format(service);
		assertEquals(urlAssert, url2);

		String community = "urn:lsid:lconn.ibm.com:communities.community:a1b2c3d4c5g6";
		String urlAssert2 = "connections/opensocial/basic/rest/activitystreams/"+community+"/"+group;
		String url3 = ActivityStreamUrls.AS_COMMUNITY_ALL.format(service, ASUser.COMMUNITY.getWithValue("a1b2c3d4c5g6"));
		assertEquals(urlAssert2, url3);
	}
}
