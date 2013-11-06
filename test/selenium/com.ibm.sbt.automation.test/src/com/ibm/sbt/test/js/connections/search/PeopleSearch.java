/*
 * © Copyright IBM Corp. 2012
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
package com.ibm.sbt.test.js.connections.search;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ibm.sbt.automation.core.test.BaseAuthServiceTest;
import com.ibm.sbt.automation.core.test.pageobjects.BaseResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;

/**
 * @author mwallace
 * 
 * @date 5 Mar 2013
 */
public class PeopleSearch extends BaseAuthServiceTest {
	
    @Test
    public void testPeopleSearch() {
    	PeopleSearchPage searchPage = launchSnippet();
        boolean success = searchPage.peopleSearch("Frank");
        Assert.assertTrue("No results when search for people", success);
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
        return "searchBtn";
    }

    // Internals

    private PeopleSearchPage launchSnippet() {
        ResultPage resultPage = launchSnippet("Social_Search_People_Search");
        return new PeopleSearchPage(resultPage);
    }

    /*
     * Page object for the
     * Social_Communities_Search_People_Search snippet
     */
    class PeopleSearchPage extends BaseResultPage {

        private ResultPage delegate;

        public PeopleSearchPage(ResultPage delegate) {
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

        public WebElement getError() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("error"));
        }

        public WebElement getTopicInput() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("topicInput"));
        }

        public WebElement getSearchBtn() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("searchBtn"));
        }

        public WebElement getPeopleTable() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("peopleTable"));
        }
        
        public void setTopic(String topic) {
            WebElement topicInput = getTopicInput();
            topicInput.clear();
            topicInput.sendKeys(topic);
        }

        public void clickSearch() {
        	getSearchBtn().click();
        }
        
        /**
         * Execute people search for specified topic and return the true if successful and
         * otherwise return false
         */
        public boolean peopleSearch(String topic) {
            setTopic(topic);
            clickSearch();

            WebElement webElement = waitForChildren("table", "tr[2]", 10);
            if (webElement != null) {
                String text = webElement.getText();
                return text != null && text.length() > 0;
            } else {
                webElement = getError();
                String text = webElement.getText();
                return text.startsWith("No people associated with topic:");
            }
        }



    }
    
}
