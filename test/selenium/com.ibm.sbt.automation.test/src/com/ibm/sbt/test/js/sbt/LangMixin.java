/*
 * � Copyright IBM Corp. 2013
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
package com.ibm.sbt.test.js.sbt;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author mwallace
 *  
 * @date 25 Mar 2013
 */
public class LangMixin extends BaseApiTest {
    
    static final String SNIPPET_ID = "Toolkit_LangMixin";
    
    @Test
    public void testLangMixin() {
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        Assert.assertEquals(6, jsonList.size());
        JsonJavaObject json = null;
        Assert.assertEquals("object1_one", ((JsonJavaObject)jsonList.get(0)).getString("one"));
        Assert.assertEquals("object2_one", ((JsonJavaObject)jsonList.get(1)).getString("one"));
        Assert.assertEquals("object2_two", ((JsonJavaObject)jsonList.get(1)).getString("two"));
        Assert.assertEquals("object3_one", ((JsonJavaObject)jsonList.get(2)).getString("one"));
        Assert.assertEquals("object3_two", ((JsonJavaObject)jsonList.get(2)).getString("two"));
        Assert.assertEquals("object3_three", ((JsonJavaObject)jsonList.get(2)).getString("three"));
        Assert.assertEquals("object1_one", ((JsonJavaObject)jsonList.get(3)).getString("one"));
        Assert.assertEquals("object2_two", ((JsonJavaObject)jsonList.get(3)).getString("two"));
        Assert.assertEquals("object3_three", ((JsonJavaObject)jsonList.get(3)).getString("three"));
        Assert.assertEquals("object1_one", ((JsonJavaObject)jsonList.get(4)).getString("one"));
        Assert.assertEquals("object3_two", ((JsonJavaObject)jsonList.get(4)).getString("two"));
        Assert.assertEquals("object3_three", ((JsonJavaObject)jsonList.get(4)).getString("three"));
        Assert.assertEquals("object1_one", ((JsonJavaObject)jsonList.get(5)).getString("one"));
        Assert.assertEquals("object2_two", ((JsonJavaObject)jsonList.get(5)).getString("two"));
    }

}
