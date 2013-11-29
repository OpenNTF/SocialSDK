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
package com.ibm.sbt.test.js.base;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author mwallace
 *  
 * @date 29 Nov 2013
 */
public class XmlDataHandlerDate extends BaseApiTest {
    
    static final String SNIPPET_ID = "Toolkit_Base_XmlDataHandlerDate";
    
    public XmlDataHandlerDate() {
        setAuthType(AuthType.NONE);
    }
    
    @Test
    public void testXmlDataHandlerDate() {
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        Assert.assertEquals("Expected two results", 2, jsonList.size());
        JsonJavaObject json0 = (JsonJavaObject)jsonList.get(0);
        JsonJavaObject json1 = (JsonJavaObject)jsonList.get(1);
        Assert.assertEquals("Error parsing: "+json0.getString("date1"),  "2003-12-13T18:30:02.000Z", json1.getString("date1"));
        Assert.assertEquals("Error parsing: "+json0.getString("date2"),  "2003-12-13T18:30:02.250Z", json1.getString("date2"));
        Assert.assertEquals("Error parsing: "+json0.getString("date3"),  "2003-12-13T17:30:02.000Z", json1.getString("date3"));
        Assert.assertEquals("Error parsing: "+json0.getString("date4"),  "2003-12-13T17:30:02.250Z", json1.getString("date4"));
    }

}
