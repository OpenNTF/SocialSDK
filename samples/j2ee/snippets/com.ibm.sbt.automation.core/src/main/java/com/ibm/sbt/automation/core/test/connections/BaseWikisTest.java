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
package com.ibm.sbt.automation.core.test.connections;

import org.junit.After;
import org.junit.Before;

import junit.framework.Assert;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityService;

/**
 * @author mwallace
 *  
 * @date 15 Jun 2013
 */
public class BaseWikisTest extends BaseApiTest {
    
    protected boolean createWiki = true;
	
    public BaseWikisTest() {
        setAuthType(AuthType.AUTO_DETECT);
    }
    
    @Before
    public void createCommunity() {
        createContext();
        if (createWiki) {
        	String type = "public";
        	if (environment.isSmartCloud()) {
        		type = "private";
        	}
        	//String name = createWikiName();
            //wiki = createWiki(name, type, name, "tag1,tag2,tag3");
        }
    }
    
    @After
    public void deleteWikiAndQuit() {
    	//deleteWiki(wiki);
    	//wiki = null;
    	destroyContext();
    	
    	if (environment.isDebugTransport()) {
    		saveTestDataAndResults();
    	}
    }
    
    protected String createWikiLabel() {
    	return this.getClass().getName() + "#" + this.hashCode() + " Wiki - " + System.currentTimeMillis();
    }
    
    protected String createWikiPageLabel() {
    	return this.getClass().getName() + "#" + this.hashCode() + " WikiPage - " + System.currentTimeMillis();
    }
    
    protected void assertWikiValid(JsonJavaObject json) {
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        /*
        Assert.assertEquals(wiki.getUuid(), json.getString("getUuid"));
        Assert.assertEquals(wiki.getTitle(), json.getString("getTitle"));
        Assert.assertEquals(wiki.getSummary(), json.getString("getSummary"));
        Assert.assertEquals(wiki.getContent(), json.getString("getContent"));
        Assert.assertEquals(wiki.getLibrarySize(), json.getString("getLibrarySize"));
        Assert.assertEquals(wiki.getLibraryQuota(), json.getString("getLibraryQuota"));
        Assert.assertEquals(wiki.getTotalRemovedSize(), json.getInt("getTotalRemovedSize"));
        Assert.assertEquals(wiki.getAuthor().getName(), json.getJsonObject("getAuthor").getString("name"));
        Assert.assertEquals(wiki.getAuthor().getEmail(), json.getJsonObject("getAuthor").getString("email"));
        Assert.assertEquals(wiki.getAuthor().getUserid(), json.getJsonObject("getAuthor").getString("userid"));
        Assert.assertEquals(wiki.getContributor().getName(), json.getJsonObject("getContributor").getString("name"));
        Assert.assertEquals(wiki.getContributor().getEmail(), json.getJsonObject("getContributor").getString("email"));
        Assert.assertEquals(wiki.getContributor().getUserid(), json.getJsonObject("getContributor").getString("userid"));
        */
    }
        
}
