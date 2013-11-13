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
package com.ibm.sbt.test.js.connections.communities;

import junit.framework.Assert;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ibm.sbt.automation.core.test.BaseTest;
import com.ibm.sbt.automation.core.test.pageobjects.BaseResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;

/**
 * @author mwallace
 * 
 * @date 7 Mar 2013
 */
public class AddRemoveCommunityMembers extends BaseTest {
    
	public AddRemoveCommunityMembers() {
        setAuthType(AuthType.AUTO_DETECT);
    }
    
    @Test
    public void testLoadCommunity() {
        AddRemoveMembersPage crudPage = launchSnippet();
        String uuid = crudPage.getLoadedCommunityId();
        Assert.assertNotNull("Unable to load community", uuid);
    }
    
    @Override
    public WebElement waitForResult(int timeout) {
    	return waitForText("success", "Sucessfully loaded community members for:", timeout);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.sbt.automation.core.test.BaseTest#getAuthenticatedMatch()
     */
    @Override
    public String getAuthenticatedMatch() {
        return "success";
    }

    // Internals

    private AddRemoveMembersPage launchSnippet() {
        ResultPage resultPage = launchSnippet("Social_Communities_Add_Remove_Community_Members");

        waitForText("success", "Sucessfully loaded community members for:", 20);

        return new AddRemoveMembersPage(resultPage);
    }

    /*
     * Page object for the Social_Communities_Add_Remove_Community_Members
     * snippet
     */
    class AddRemoveMembersPage extends BaseResultPage {

        private ResultPage delegate;

        public AddRemoveMembersPage(ResultPage delegate) {
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

        public WebElement getSuccess() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("success"));
        }

        public WebElement getError() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("error"));
        }

        public WebElement getCommunityId() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("communityId"));
        }

        public WebElement getCommunityTitle() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("communityTitle"));
        }

        public WebElement getCommunityMembers() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("membersList"));
        }

        public WebElement getMemberEmail() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("memberEmail"));
        }

        public WebElement getRemoveBtn() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("removeBtn"));
        }

        public WebElement getRefreshBtn() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("refreshBtn"));
        }

        public WebElement getAddBtn() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("addBtn"));
        }

        /**
         * @param tags
         */
        public void setMemberEmail(String email) {
            WebElement communityTags = getMemberEmail();
            communityTags.clear();
            communityTags.sendKeys(email);
        }

        public void clickRemove() {
            getRemoveBtn().click();
        }

        public void clickrefresh() {
            getRefreshBtn().click();
        }

        public void clickAdd() {
            getAddBtn().click();
        }

        /**
         * Add a new member to the current community
         */
        public boolean addMember() {
            String email = getProperty("sample.email2");
            return addMember(email);
        }

        /**
         * Add a new member to the current community
         */
        public boolean addMember(String email) {
            setMemberEmail(email);

            clickAdd();

            waitForText("success", "Sucessfully loaded community members for:", 30);

            String members = getCommunityMembers().getText();
            
            Assert.assertNotNull("Failed to load community members", members);

            return members.contains(email);
        }

        /**
         * Return the community id of the community which was loaded
         */
        public String getLoadedCommunityId() {
            waitForText("success", "Sucessfully loaded community members for:", 30);

            String text = getSuccess().getText();

            Assert.assertNotNull("Failed to load community members", text);

            return text.substring("Sucessfully loaded community members for: ".length());
        }

    }

}
