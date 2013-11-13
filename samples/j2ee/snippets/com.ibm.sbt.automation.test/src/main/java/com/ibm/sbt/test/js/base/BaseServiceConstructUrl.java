/*
 * © Copyright IBM Corp. 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at,
 * 
 * http,//www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.sbt.test.js.base;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author mwallace
 * 
 * @date 9 May 2013
 */
public class BaseServiceConstructUrl extends BaseApiTest {

    static final String SNIPPET_ID = "Toolkit_Base_BaseServiceConstructUrl";

    @Test
    public void testConstructUrl() {
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals("/communities/service/atom/communities/all", json.getString("a"));
        Assert.assertEquals("/communities/service/atom/communities/all?ps=5&since=2009-01-04T20%3A32%3A31.171Z&email=john%3F%40foo", json.getString("b"));
        Assert.assertEquals("/communities/service/atom/communities/all?page=1&ps=5&since=2009-01-04T20%3A32%3A31.171Z&email=john%3F%40foo", json.getString("c"));
        Assert.assertEquals("/communities/service/atom/communities/all?page=1&ps=5&since=2009-01-04T20%3A32%3A31.171Z&email=john%3F%40foo", json.getString("d"));
        Assert.assertEquals("/connections/opensocial/oauth/rest/activitystreams/", json.getString("e"));
        Assert.assertEquals("/connections/opensocial/oauth/rest/activitystreams/@me", json.getString("f"));
        Assert.assertEquals("/connections/opensocial/oauth/rest/activitystreams/@me/@following/@communities", json.getString("g"));
        Assert.assertEquals("/connections/opensocial/rest/activitystreams/", json.getString("h"));
    }
}
