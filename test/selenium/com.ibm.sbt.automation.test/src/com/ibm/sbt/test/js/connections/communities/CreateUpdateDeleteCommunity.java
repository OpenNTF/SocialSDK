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
public class CreateUpdateDeleteCommunity extends BaseTest {

    public CreateUpdateDeleteCommunity() {
        setAuthType(AuthType.AUTO_DETECT);
    }
    
    @Test
    public void testLoadCommunity() {
        CreateUpdateDeletePage crudPage = launchSnippet();
        String uuid = crudPage.getLoadedCommunityId();
        Assert.assertNotNull("Unable to load community", uuid);
    }

    @Test
    public void testCreateCommunity() {
        CreateUpdateDeletePage crudPage = launchSnippet();
        String uuid = crudPage.createCommunity();
        Assert.assertNotNull("Unable to create new community", uuid);
        boolean deleted = crudPage.deleteCommunity();
        Assert.assertTrue("Unable to delete a community", deleted);
    }

    @Test
    public void testUpdateCommunity() {
        CreateUpdateDeletePage crudPage = launchSnippet();
        String uuid = crudPage.createCommunity();
        Assert.assertNotNull("Unable to create new community", uuid);
        boolean updated = crudPage.updateCommunity();
        Assert.assertTrue("Unable to update a community", updated);
        boolean deleted = crudPage.deleteCommunity();
        Assert.assertTrue("Unable to delete a community", deleted);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ibm.sbt.automation.core.test.BaseTest#getAuthenticatedCondition()
     */
    @Override
    public String getAuthenticatedCondition() {
        return "idWithText";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.sbt.automation.core.test.BaseTest#getAuthenticatedMatch()
     */
    @Override
    public String getAuthenticatedMatch() {
        return "communityId";
    }

    // Internals

    private CreateUpdateDeletePage launchSnippet() {
        ResultPage resultPage = launchSnippet("Social_Communities_Create_Update_Delete_Community");

        waitForText("success", "Successfully loaded community:", 20);

        return new CreateUpdateDeletePage(resultPage);
    }

    /*
     * Page object for the
     * Social_Communities_Create_Update_Delete_Community snippet
     */
    class CreateUpdateDeletePage extends BaseResultPage {

        private ResultPage delegate;

        public CreateUpdateDeletePage(ResultPage delegate) {
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

        public WebElement getCommunityContent() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("communityContent"));
        }

        public WebElement getCommunityTags() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("communityTags"));
        }

        public WebElement getLoadBtn() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("loadBtn"));
        }

        public WebElement getCreateBtn() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("createBtn"));
        }

        public WebElement getUpdateBtn() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("updateBtn"));
        }

        public WebElement getDeleteBtn() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("deleteBtn"));
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

        public void clickCreate() {
            getCreateBtn().click();
        }

        public void clickUpdate() {
            getUpdateBtn().click();
        }

        public void clickDelete() {
            getDeleteBtn().click();
        }

        /**
         * Create a new community and return the uuid if successful and
         * otherwise return null
         */
        public String createCommunity() {
            String title = "Test Automation Community - " + System.currentTimeMillis();
            String contents = "The community was created using the CreateUpdateDeleteCommunity unit test";
            String tags = "tagA,tagB,tagC";
            return createCommunity(title, contents, tags);
        }

        /**
         * Create a new community and return the uuid if successful and
         * otherwise return null
         */
        public String createCommunity(String title, String content, String tags) {
            setCommunityTitle(title);
            setCommunityContent(content);
            setCommunityTags(tags);

            clickCreate();

            return getCreatedCommunityId();
        }

        /**
         * Update the current community and return the true if successful and
         * otherwise return false
         */
        public boolean updateCommunity() {
            String title = "Updated Test Automation Community - " + System.currentTimeMillis();
            String contents = "The community was updated using the CreateUpdateDeleteCommunity unit test";
            String tags = "tagA,tagB,tagC,tagD";
            return updateCommunity(title, contents, tags);
        }

        /**
         * Update the current community and return the true if successful and
         * otherwise return false
         */
        public boolean updateCommunity(String title, String content, String tags) {
            setCommunityTitle(title);
            setCommunityContent(content);
            setCommunityTags(tags);

            clickUpdate();

            WebElement webElement = waitForText("success", "Successfully updated community:", 20);

            String text = webElement.getText();

            return text.startsWith("Successfully updated community:");
        }

        /**
         * Delete the current community and return the true if successful and
         * otherwise return false
         */
        public boolean deleteCommunity() {
            clickDelete();

            WebElement webElement = waitForText("success", "Successfully deleted community:", 20);

            String text = webElement.getText();

            return text.startsWith("Successfully deleted community:");
        }

        /**
         * Return the community id of the community that was last created
         */
        public String getCreatedCommunityId() {
            WebElement webElement = waitForText("success", "Successfully created community:", 20);

            String text = webElement.getText();

            if (text.startsWith("Successfully created community:")) {
                return text.substring("Successfully created community: ".length());
            } else {
                return null;
            }
        }

        /**
         * Return the community id of the community that was last loaded
         */
        public String getLoadedCommunityId() {
            WebElement webElement = waitForText("success", "Successfully loaded community:", 20);

            String text = webElement.getText();

            if (text.startsWith("Successfully loaded community:")) {
                return text.substring("Successfully loaded community: ".length());
            } else {
                return null;
            }
        }
    }

}
