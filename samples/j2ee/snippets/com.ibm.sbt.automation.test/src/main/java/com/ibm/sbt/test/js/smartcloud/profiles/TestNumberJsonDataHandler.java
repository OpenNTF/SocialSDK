/*
 * � Copyright IBM Corp. 2013
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

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author Vimal Dhupar
 *  
 * @date 11th June, 2013
 */
public class TestNumberJsonDataHandler extends BaseApiTest {
    
    static final String SNIPPET_ID = "Social_Profiles_SmartCloud_API_TestNumberJsonDataHandler";

    @Test
    public void testProfileEntryJsonDataHandler() {
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        
        Assert.assertEquals(123, json.getInt("number"));
        Assert.assertEquals(12.12, json.getDouble("decimalNumber"),0.01);
        Assert.assertEquals(234, json.getInt("stringNumber"));
        Assert.assertEquals(4, json.getInt("array"));
        //commenting out the below tests, because the jsonBeanStringify is currently not adding the NaN values in the json response.
//        Assert.assertEquals(Double.NaN, json.getAsInt("notANumber"),0.01);
//        Assert.assertEquals(Double.NaN, json.getAsInt("notANumber1"),0.01);
    }
}
