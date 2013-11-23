/*
 * � Copyright IBM Corp. 2012
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
package com.ibm.sbt.test.js.connections.profiles.rest;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ibm.sbt.automation.core.test.BaseServiceTest;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;

/**
 * @author mwallace
 * 
 * @date 5 Mar 2013
 */
public class ReadProfilePhoto extends BaseServiceTest {

    @Test
    public void testNoError() {
        ResultPage resultPage = launchSnippet("Social_Profiles_REST_Read_Profile_Photo", AuthType.NONE);
        WebElement contentEl = resultPage.getWebElement().findElement(By.id("content"));
        WebElement imgEl = contentEl.findElement(By.tagName("img"));
        assertNotNull("Unable to find image tag", imgEl);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ibm.sbt.automation.core.test.BaseTest#getAuthenticatedCondition()
     */
    @Override
    public String getAuthenticatedCondition() {
        return "idWithChild";
    }

}
