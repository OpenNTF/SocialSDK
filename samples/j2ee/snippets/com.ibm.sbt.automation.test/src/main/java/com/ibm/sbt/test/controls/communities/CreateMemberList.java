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
package com.ibm.sbt.test.controls.communities;


import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ibm.sbt.automation.core.test.BaseTest;
import com.ibm.sbt.automation.core.test.BaseTest.AuthType;
import com.ibm.sbt.automation.core.test.pageobjects.BaseResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;

/**
 * @author Benjamin Jakobus
 * 
 * @date Nov 15, 2013
 */
public class CreateMemberList extends BaseTest {

    public CreateMemberList() {
        setAuthType(AuthType.AUTO_DETECT);
    }
	
    @Test
    public void runTest() {
    	CreateMemberListPage page = launchSnippet();
    	page.createCommunity();
    }
    

    // Internals
    private CreateMemberListPage launchSnippet() {
        ResultPage resultPage = launchSnippet("Social_Communities_Controls_Create_Member_List");
        return new CreateMemberListPage(resultPage);
    }

    /*
     * Page object for the
     * Social_Communities_Create_Update_Delete_Community snippet
     */
    class CreateMemberListPage extends BaseResultPage {

        private ResultPage delegate;

        public CreateMemberListPage(ResultPage delegate) {
            this.delegate = delegate;

            setWebDriver(delegate.getWebDriver());
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.ibm.sbt.automation.core.test.pageobjects.ResultPage#getText()
         */
        @Override
        public String getText() {
            return delegate.getText();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.ibm.sbt.automation.core.test.pageobjects.ResultPage#getWebElement
         * ()
         */
        @Override
        public WebElement getWebElement() {
            return delegate.getWebElement();
        }

        public WebElement getLoadBtn() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("loadBtn"));
        }

        public WebElement getCreateBtn() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("btnCreateCommunity"));
        }

        public void clickCreate() {
            getCreateBtn().click();
        }

        /**
         * Create a new community and return the uuid if successful and
         * otherwise return null
         */
        public void createCommunity() {
            String title = "Test Automation Community - " + System.currentTimeMillis();
            String contents = "The community was created using the CreateUpdateDeleteCommunity unit test";
            String tags = "tagA,tagB,tagC";
            createCommunity(title, contents, tags);
        }
        
        public WebElement getCommunityTitle() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("titleTextField"));
        }

        public WebElement getCommunityContent() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("contentTextField"));
        }

        public WebElement getCommunityTags() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("tagsTextField"));
        }

        public void setCommunityTags(String tags) {
            WebElement communityTags = getCommunityTags();
            communityTags.clear();
            communityTags.sendKeys(tags);
        }
        
        public void setCommunityContent(String content) {
            WebElement communityContent = getCommunityContent();
            communityContent.clear();
            communityContent.sendKeys(content);
        }

        public void setCommunityTitle(String title) {
            WebElement communityTitle = getCommunityTitle();
            communityTitle.clear();
            communityTitle.sendKeys(title);
        }

        /**
         * Create a new community and return the uuid if successful and
         * otherwise return null
         */
        public void createCommunity(String title, String content, String tags) {
            setCommunityTitle(title);
            setCommunityContent(content);
            setCommunityTags(tags);

            clickCreate();
        }
    }

}
