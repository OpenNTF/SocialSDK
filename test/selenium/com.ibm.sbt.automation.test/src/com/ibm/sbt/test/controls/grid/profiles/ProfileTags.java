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
package com.ibm.sbt.test.controls.grid.profiles;

import static org.junit.Assert.assertTrue;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.ibm.sbt.automation.core.test.BaseTest;
import com.ibm.sbt.automation.core.test.pageobjects.ListResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;
import com.ibm.sbt.automation.core.utils.Trace;

/**
 * @author mwallace
 * @date 14 Aug 2013
 */
public class ProfileTags extends BaseTest {

	@Override
	protected boolean isEnvironmentValid() {
		return super.isEnvironmentValid() && !environment.isLibrary("jquery") && !environment.isSmartCloud();
	}

    @Test
    public void testTaggedBy() {
        assertTrue("Expected the test to generate a list", checkList("Social_Profiles_Controls_ProfileTaggedBy"));
    }																

    @Test
    public void testTagsFor() {
        assertTrue("Expected the test to generate a list", checkList("Social_Profiles_Controls_ProfileTagsFor"));
    }																

    /**
     * Check the list
     * 
     * @param snippetId
     * @return
     */
    protected boolean checkList(String snippetId) {
    	ListResultPage resultPage = launchList(snippetId);
        
        return checkList(resultPage, snippetId);
    }
    
    /**
     * Launch the list snippet and return a ListResultPage
     * 
     * @param snippetId
     * @return
     */
    protected ListResultPage launchList(String snippetId) {
        ResultPage resultPage = super.launchSnippet(snippetId, authType);
        return new ListResultPage(resultPage);
    }
    
    /**
     * Return true if a List was created on the page i.e. could find ul, multiple li's
     * @return
     */
    protected boolean checkList(ListResultPage resultPage, String snippetId) {
       Trace.log("List result page: " + resultPage.getText());
       if(resultPage.getText().contains("Empty")){
    	   return true;
       }
        
        WebElement ul = resultPage.getList();
        if (ul == null) {
        	Assert.fail("Unable to find list for snippet: " + snippetId);
        }
        
        List<WebElement> items = resultPage.getListItems();
        if (items == null || items.isEmpty()) {
        	Assert.fail("Unable to find items for snippet: " + snippetId);
        }
        
        return true;
    }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.automation.core.test.BaseTest#getAuthenticatedMatch()
     */
    @Override
    public String getAuthenticatedMatch() {
    	return "ul";
    }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.automation.core.test.BaseTest#getAuthenticatedCondition()
     */
    @Override
    public String getAuthenticatedCondition() {
    	return "tagName";
    }
    
}
