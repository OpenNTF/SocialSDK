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
package com.ibm.sbt.test.js.smartcloud.profiles;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author Vimal Dhupar
 *  
 * @date 10th June, 2013
 */
public class ProfileFeedJsonDataHandler extends BaseApiTest {
    
    static final String SNIPPET_ID = "Social_Profiles_SmartCloud_API_ProfileFeedJsonDataHandler";

    @Test
    public void testProfileFeedJsonDataHandler() {
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        Assert.assertEquals(2, jsonList.size());
    	JsonJavaObject json0 = (JsonJavaObject)jsonList.get(0);
    	JsonJavaObject json1 = (JsonJavaObject)jsonList.get(1);
    	
    	Assert.assertEquals("na.collabserv.com:contact:966797", json0.getString("entityId"));
    	Assert.assertEquals("na.collabserv.com:contact:966800", json1.getString("entityId"));
    	
        Assert.assertEquals("Technical Sales Manager, WorldWide Team.", json0.getString("title")); 
        Assert.assertEquals("https://apps.na.collabserv.com/contacts/contacts/view/966797", json0.getString("profileUrl"));
        Assert.assertEquals("https://apps.na.collabserv.com/contacts/contacts/view/966800", json1.getString("profileUrl"));
        
        Assert.assertEquals("Alan Goodwin", json0.getString("name"));
        Assert.assertEquals("Philippe Riand", json1.getString("name"));
        
        Assert.assertEquals(false, json0.getBoolean("thumbnailUrlB"));
        Assert.assertEquals(true, json0.getBoolean("emailB"));
        Assert.assertEquals(true, json0.getBoolean("addressB"));
        Assert.assertEquals(false, json1.getBoolean("thumbnailUrlB"));
        Assert.assertEquals(true, json1.getBoolean("emailB"));
        Assert.assertEquals(false, json1.getBoolean("addressB"));
        
        List jArray = new ArrayList();
        jArray.add("IBM Test - SDK Renovations");
        Assert.assertEquals(jArray, (List)json0.get("departmentA"));
        Assert.assertEquals(jArray, (List)json1.get("departmentA"));
        jArray.clear();
    }
}