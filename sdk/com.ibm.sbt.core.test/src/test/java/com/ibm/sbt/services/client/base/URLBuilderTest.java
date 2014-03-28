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

import com.ibm.sbt.services.client.connections.activitystreams.ASApplication;
import com.ibm.sbt.services.client.connections.activitystreams.ASGroup;
import com.ibm.sbt.services.client.connections.activitystreams.ASService;
import com.ibm.sbt.services.client.connections.activitystreams.ASUser;
import com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamUrls;
import com.ibm.sbt.services.client.connections.communities.CommunityEntity;
import com.ibm.sbt.services.client.connections.communities.CommunityType;
import com.ibm.sbt.services.client.connections.communities.CommunityUrls;
import com.ibm.sbt.services.client.connections.files.AccessType;
import com.ibm.sbt.services.client.connections.files.FileUrlParts;
import com.ibm.sbt.services.client.connections.files.FileUrls;
import com.ibm.sbt.services.client.connections.wikis.WikiUrlParts;
import com.ibm.sbt.services.client.connections.wikis.WikiUrls;

/**
 * 
 * @author Carlos Manias
 *
 */
public class URLBuilderTest {
	@Rule 
	public ExpectedException thrown= ExpectedException.none();

	@Test
	public void testGenerateURLPredefinedParameters() {
		//URL pattern is "communities/service/atom/{authType}/{communityEntity}/{communityType}"
		String url = CommunityUrls.COMMUNITY_URL.format(getApiVersion(), getAuthTypeBasic(), CommunityEntity.COMMUNITY.get(),CommunityType.MEMBERS.get());
		assertEquals("communities/service/atom/basic/community/members", url);
	}

	@Test
	public void testGenerateURLVariableParameters() {
		//URL pattern is "wikis/{authType}/api/wiki/{wikiLabel}/page/{wikiPage}/entry"
		String wikiLabel = "myWikiLabel";
		String pageLabel = "myPageLabel";
		String url = WikiUrls.WIKI_PAGE_AUTH.format(getApiVersion(), getAuthTypeOAuth(), WikiUrlParts.wikiLabel.get(wikiLabel), WikiUrlParts.wikiPage.get(pageLabel));
		assertEquals("wikis/oauth/api/wiki/"+wikiLabel+"/page/"+pageLabel+"/entry", url);
	}

	@Test
	public void testGenerateURLUnsortedParameters() {
		//URL pattern is "wikis/{authType}/api/wiki/{wikiLabel}/page/{wikiPage}/entry"
		String wikiLabel = "myWikiLabel";
		String pageLabel = "myPageLabel";
		//This url has the correct parameters but in a different order. It works regardless. The first parameter needs to be the Version, though
		String url = WikiUrls.WIKI_PAGE_AUTH.format(getApiVersion(), WikiUrlParts.wikiPage.get(pageLabel), getAuthTypeOAuth(), WikiUrlParts.wikiLabel.get(wikiLabel));
		assertEquals("wikis/oauth/api/wiki/"+wikiLabel+"/page/"+pageLabel+"/entry", url);
	}

	@Test
	public void testGenerateURLRigtNumberOfParametersButWrongParameter() {
		//URL pattern is "wikis/{authType}/api/wiki/{wikiLabel}/page/{wikiPage}/entry"
		String wikiLabel = "myWikiLabel";
		String pageLabel = "myPageLabel";
		//This url has the correct number of parameters but one of the parameters is not correct
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Missing parameter");
		String url = WikiUrls.WIKI_PAGE_AUTH.format(getApiVersion(), getAuthTypeOAuth(), WikiUrlParts.wikiLabel.get(wikiLabel), CommunityType.MEMBERS.get());
		assertEquals("wikis/oauth/api/wiki/"+wikiLabel+"/page/"+pageLabel+"/entry", url);
	}

	@Test
	public void testGenerateURLVariableAndEmptyParameters() {
		//URL pattern is "wikis/{authType}/api/wiki/{wikiLabel}/page/{wikiPage}/entry
		//When a parameter is empty, it is simply removed from the url
		String url2 = WikiUrls.WIKI_PAGE_AUTH.format(getApiVersion(), getAuthTypeOAuth(), WikiUrlParts.wikiLabel.get(""), WikiUrlParts.wikiPage.get("myPageLabel"));
		assertEquals("wikis/oauth/api/wiki/page/myPageLabel/entry", url2);
	}

	@Test
	public void testGenerateURLMissingParameters() {
		String userId = "abcde";
		String fileId = "1234";
		String commentId = "a1b2c3";
		//This would be the url with all the needed parameters
		//String url = FileUrls.SINGLE_COMMENT_USER_FILE.format(getApiVersion(), getAuthTypeBasic(), AccessType.PUBLIC.get(), FileUrlParts.userId.get(userId), FileUrlParts.fileId.get(fileId), FileUrlParts.commentId.get(commentId));

		//This url misses the last parameter, commentId
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Missing parameter");
		String url = FileUrls.SINGLE_COMMENT_USER_FILE.format(getApiVersion(), getAuthTypeBasic(), AccessType.PUBLIC.get(), FileUrlParts.userId.get(userId), FileUrlParts.fileId.get(fileId));
	}

	@Test
	public void testGenerateURLExtraParameters() {
		String userId = "abcde";
		String fileId = "1234";
		String commentId = "a1b2c3";
		//This would be the url with all the needed parameters
		//String url = FileUrls.SINGLE_COMMENT_USER_FILE.format(getApiVersion(), getAuthTypeBasic(), AccessType.PUBLIC.get(), FileUrlParts.userId.get(userId), FileUrlParts.fileId.get(fileId), FileUrlParts.commentId.get(commentId));

		String url = FileUrls.SINGLE_COMMENT_USER_FILE.format(getApiVersion(), getAuthTypeBasic(), AccessType.PUBLIC.get(), FileUrlParts.userId.get(userId), FileUrlParts.fileId.get(fileId), FileUrlParts.commentId.get(commentId), 
				CommunityEntity.COMMUNITY.get(), ASUser.ME.get(), WikiUrlParts.wikiLabel.get("value")); //The last 3 parameters are not needed, they are simply ignored
		String assertUrl = "files/basic/anonymous/api/userlibrary/"+userId+"/document/"+fileId+"/comment/"+commentId+"/entry";
		assertEquals(assertUrl, url);
	}

	@Test
	public void testGenerateURLVariableParameters2() {
		//URL pattern is "connections/opensocial/{authType}/rest/{service}/{user}/{group}/{application}"
		// Those variables are @me @all and @status URLEncoded
		String user = "%40me";
		String group = "%40all";
		String app = "%40status";

		String urlAssert = "connections/opensocial/rest/activitystream/"+user+"/"+group+"/"+app;
		String url = ActivityStreamUrls.ACTIVITYSTREAM_URL.format(getApiVersion(), getAuthTypeBasicEmpty(), ASService.ACTIVITYSTREAM.get(), ASUser.getByName("me"), ASGroup.getByName("all"), ASApplication.getByName("status"));
		assertEquals(urlAssert, url);

		String url2 = ActivityStreamUrls.ACTIVITYSTREAM_URL.format(getApiVersion(), getAuthTypeBasicEmpty(), ASService.ACTIVITYSTREAM.get(), ASUser.ME.get(), ASGroup.ALL.get(), ASApplication.STATUS.get());
		assertEquals(urlAssert, url2);

		String community = "urn:lsid:lconn.ibm.com:communities.community:a1b2c3d4c5g6";
		String urlAssert2 = "connections/opensocial/rest/activitystream/"+community+"/"+group+"/";
		String url3 = ActivityStreamUrls.ACTIVITYSTREAM_URL.format(getApiVersion(), getAuthTypeBasicEmpty(), ASService.ACTIVITYSTREAM.get(), ASUser.COMMUNITY.getWithValue("a1b2c3d4c5g6"), ASGroup.ALL.get(), ASApplication.NOAPP.get());
		assertEquals(urlAssert2, url3);
	}
	
	protected Version getApiVersion(){
		return ConnectionsConstants.v4_0;
	}
	
	protected NamedUrlPart getAuthTypeBasic(){
		return AuthType.BASIC.getPart();
	}

	protected NamedUrlPart getAuthTypeOAuth(){
		return AuthType.OAUTH.getPart();
	}

	protected NamedUrlPart getAuthTypeBasicEmpty(){
		return new NamedUrlPart("authType", "");
	}
}
