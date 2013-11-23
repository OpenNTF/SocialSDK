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
package com.ibm.sbt.test.js.utilities;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.automation.core.test.BaseAuthServiceTest;
import com.ibm.sbt.automation.core.test.BaseTest;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;

/**
 * @author projsaha
 */
public class XpathEngine extends BaseTest {

	public static final String SNIPPET_ID = "Utilities_XPath_Engine";
	
    @Test
    public void testNoError() {
    	ResultPage resultPage = launchSnippet(SNIPPET_ID, authType);
        String content = resultPage.getWebElement().findElement(By.id("content")).getText();
        Assert.assertNotNull(content);  
        Assert.assertEquals("I'm C", content);
    }   
}
