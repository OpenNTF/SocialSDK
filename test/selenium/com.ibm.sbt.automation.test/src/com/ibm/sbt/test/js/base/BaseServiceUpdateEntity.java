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

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author mwallace
 *  
 * @date 25 Mar 2013
 */
public class BaseServiceUpdateEntity extends BaseApiTest {
    
    static final String SNIPPET_ID = "Toolkit_Base_BaseServiceUpdateEntity";
    
    public BaseServiceUpdateEntity() {
        setAuthType(AuthType.AUTO_DETECT);
    }
    
    @Test @Ignore
    public void testUpdateEntity() {
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        Assert.assertEquals(4, jsonList.size());
        
        JsonJavaObject json = (JsonJavaObject)jsonList.get(0);
        Assert.assertEquals("createEntity", json.getString("callback"));
        Assert.assertEquals(201, json.getJsonObject("response").getInt("status"));
        
        json = (JsonJavaObject)jsonList.get(1);
        Assert.assertEquals("updateEntity", json.getString("callback"));
        Assert.assertNotNull(json.getString("data"));
        
        json = (JsonJavaObject)jsonList.get(2);
        Assert.assertEquals("createEntity", json.getString("callback"));
        Assert.assertNotNull(json.getString("data"));
        Assert.assertEquals(200, json.getJsonObject("response").getInt("status"));
        
        json = (JsonJavaObject)jsonList.get(3);
        Assert.assertEquals("updateEntity", json.getString("callback"));
        Assert.assertNotNull(json.getString("data"));
        Assert.assertEquals(200, json.getJsonObject("response").getInt("status"));
    }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.automation.core.test.BaseTest#waitForResult(int)
     */
    @Override
    public WebElement waitForResult(int timeout) {
        return waitForJsonList(4, timeout);
    }

}
