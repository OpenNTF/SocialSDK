/*
 * © Copyright IBM Corp. 2013
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
package com.ibm.sbt.test.controls.view.files;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.ibm.sbt.automation.core.test.BaseViewTest;
import com.ibm.sbt.automation.core.test.pageobjects.BaseResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;

public class FileActions extends BaseViewTest {

	static String SNIPPET_ID = "Social_Files_Views_File_Actions";

	public FileActions() {
		setAuthType(AuthType.AUTO_DETECT);
	}
	
	@Test
	public void testFileActions() {
		WidgetResultPage resultPage = launchWidget(SNIPPET_ID);
		
		Assert.assertEquals("Upload File\nNew Folder", resultPage.getText());
    }
	
	protected WidgetResultPage launchWidget(String snippetId) {
		ResultPage resultPage = launchSnippet(snippetId);
		return new WidgetResultPage(resultPage);
	}
	
    /*
     * (non-Javadoc) @see com.ibm.sbt.automation.core.test.BaseTest#getAuthenticatedCondition()
     */
    @Override
    public String getAuthenticatedCondition() {
        return "id";
    }

    /*
     * (non-Javadoc) @see com.ibm.sbt.automation.core.test.BaseTest#getAuthenticatedMatch()
     */
    @Override
    public String getAuthenticatedMatch() {
        return "uniqName_1_0";
    }
	
	public class WidgetResultPage extends BaseResultPage {
	    
	    private ResultPage delegate;
	   
	    public WidgetResultPage(ResultPage delegate) {
	        this.delegate = delegate;
	        
	        setWebDriver(delegate.getWebDriver());
	    }

	    /* (non-Javadoc)
	     * @see com.ibm.sbt.automation.core.test.pageobjects.ResultPage#getText()
	     */
	    @Override
	    public String getText() {
	        return delegate.getText();
	    }

	    /* (non-Javadoc)
	     * @see com.ibm.sbt.automation.core.test.pageobjects.ResultPage#getWebElement()
	     */
	    @Override
	    public WebElement getWebElement() {
	    	return delegate.getWebElement();
	    }
	        	
	}
	
}
