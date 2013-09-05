package com.ibm.sbt.automation.core.test;

import org.openqa.selenium.WebElement;
import com.ibm.sbt.automation.core.test.pageobjects.WrapperResultPage;
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

/**
 * @author Francis 
 * @date 26 Mar 2013
 */
public class BaseActivityStreamTest extends BaseWrapperTest{

	/**
     * Default constructor
     */
    public BaseActivityStreamTest() {
        setAuthType(AuthType.AUTO_DETECT);
    }
	
	/**
	 * Check if the ActivityStream is displayed on the page
	 * @param snippetId
	 * @return true if displayed
	 */
	protected boolean checkActivityStream(String snippetId) {
		WrapperResultPage resultPage = launchSnippet(snippetId);
		
		WebElement activityStreamFrame = resultPage.getActivityStreamFrame();
		
		// switch context into the iframe.
		switchContextToIframe(resultPage, activityStreamFrame);
		
	    WebElement activityStream = resultPage.getActivityStream();
		
		WebElement newsFeedNode = resultPage.getNewsFeedNode();
		
		return (activityStream!=null) && (newsFeedNode!=null);
	}
	
	@Override
    protected boolean isEnvironmentValid() {
	    // don't run the test in jquery
        return super.isEnvironmentValid() && !environment.isLibrary("jquery");
    }
}
