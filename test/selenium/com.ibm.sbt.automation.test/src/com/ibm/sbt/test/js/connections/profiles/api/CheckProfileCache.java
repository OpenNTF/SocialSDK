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
package com.ibm.sbt.test.js.connections.profiles.api;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseProfilesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;


/**
 * @author projsaha
 *  
 * @date 3 May 2013
 */
public class CheckProfileCache extends BaseProfilesTest {
    
    static final String SNIPPET_ID = "Social_Profiles_API_CheckProfileCache";     
	
    @Test
    public void testProfileCache() {     	
    	
    	JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
    	 List jsonList = previewPage.getJsonList();
    	 JsonJavaObject json = (JsonJavaObject)jsonList.get(0);         
        Assert.assertEquals(1, json.getInt("Cache count after first get operation"));   
        
    }       
}
