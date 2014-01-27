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
package com.ibm.sbt.test.controls.view.files;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ibm.sbt.automation.core.test.connections.BaseFilesTest;
import com.ibm.sbt.automation.core.test.pageobjects.BaseResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;
import com.ibm.sbt.services.client.connections.files.File;

public class AddTagsWidget extends BaseFilesTest {
	
	static String SNIPPET_ID = "Social_Files_Views_Add_Tags_Widget";

	public AddTagsWidget() {
		setAuthType(AuthType.AUTO_DETECT);
	}
	
	@Override
    protected boolean isEnvironmentValid() {
        return super.isEnvironmentValid() && !environment.isLibrary("jquery");
    }
	
	@Test
	public void testTagFile() {
		createFile();
	
		addSnippetParam("fileCount", "1");
		WidgetResultPage resultPage = launchWidget(SNIPPET_ID);
		
		String tags = "tag1,tag2,tag3";
		String text = resultPage.addTags(tags);
		Assert.assertEquals(fileEntry.getLabel() + " was tagged with tag1,tag2,tag3.", text);
    }
	
	@Test
	public void testTagFiles() {
		File[] files = new File[3];
		for (int i=0; i<files.length; i++) {
			createFile();
			files[i] = fileEntry;
		}
		
		addSnippetParam("fileCount", "" + files.length);
		WidgetResultPage resultPage = launchWidget(SNIPPET_ID);
		
		String tags = "tag1,tag2,tag3";
		String text = resultPage.addTags(tags);
		Assert.assertEquals("3 files were tagged with tag1,tag2,tag3.", text);
		
		for (int i=0; i<files.length; i++) {
			deleteFile(files[i]);
		}
    }
	
	@Test
	public void testTagFileFail() {
		createFile();
	
		addSnippetParam("fileCount", "1");
		WidgetResultPage resultPage = launchWidget(SNIPPET_ID);
		
		deleteFile(fileEntry);
		
		String tags = "tag1,tag2,tag3";
		String text = resultPage.addTags(tags);
		Assert.assertEquals(fileEntry.getLabel() + " could not be tagged because the file has been deleted or is no longer visible.", text);
    }
	
	@Test
	public void testTagFilesFail() {
		File[] files = new File[3];
		for (int i=0; i<files.length; i++) {
			createFile();
			files[i] = fileEntry;
		}
		
		addSnippetParam("fileCount", "" + files.length);
		WidgetResultPage resultPage = launchWidget(SNIPPET_ID);
		
		String label1 = files[1].getLabel();
		deleteFile(files[1]);
		files[1] = null;
		
		String tags = "tag1,tag2,tag3";
		String text = resultPage.addTags(tags);
		Assert.assertEquals("2 files were tagged with tag1,tag2,tag3.\n" +
			label1 + " could not be tagged because the file has been deleted or is no longer visible.", text);
		
		for (int i=0; i<files.length; i++) {
			if (files[i] != null) {
				deleteFile(files[i]);
			}
		}
    }

	@Test
	public void testTagFilesFails() {
		File[] files = new File[5];
		for (int i=0; i<files.length; i++) {
			createFile();
			files[i] = fileEntry;
		}
		
		addSnippetParam("fileCount", "" + files.length);
		WidgetResultPage resultPage = launchWidget(SNIPPET_ID);
		
		
		String label1 = files[1].getLabel();
		deleteFile(files[1]);
		files[1] = null;
		String label2 = files[2].getLabel();
		deleteFile(files[2]);
		files[2] = null;
		
		String tags = "tag1,tag2,tag3";
		String text = resultPage.addTags(tags);
		Assert.assertEquals("3 files were tagged with tag1,tag2,tag3.\n" +
				label2 + " could not be tagged because the file has been deleted or is no longer visible.\n" +
				label1 + " could not be tagged because the file has been deleted or is no longer visible.", text);
		
		for (int i=0; i<files.length; i++) {
			if (files[i] != null) {
				deleteFile(files[i]);
			}
		}
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
        return "name";
    }

    /*
     * (non-Javadoc) @see com.ibm.sbt.automation.core.test.BaseTest#getAuthenticatedMatch()
     */
    @Override
    public String getAuthenticatedMatch() {
        return "tags";
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
	    
        public String getAlertText() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("alertDiv")).getText();
        }

        public WebElement getAlertDiv() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("alertDiv"));
        }

        public WebElement getWidgetDiv() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.id("widgetDiv"));
        }
	    
        public WebElement getTagsInput() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.name("tags"));
        }
	    
        public WebElement getAddTagsBtn() {
            WebElement resultEl = getWidgetDiv();
            return resultEl.findElements(By.tagName("button")).get(0);
        }
	    
        public WebElement getCancelBtn() {
            WebElement resultEl = getWidgetDiv();
            return resultEl.findElements(By.tagName("button")).get(1);
        }
        
        public void setTags(String tags) {
            WebElement tagsInput = getTagsInput();
            tagsInput.clear();
            tagsInput.sendKeys(tags);
        }

        public void clickAddTags() {
            getAddTagsBtn().click();
        }

        public void clickCancel() {
        	getCancelBtn().click();
        }
        
    	public String addTags(String tags) {
    		setTags(tags);
    		clickAddTags();
    		
            WebElement webElement = waitForText("alertDiv", "tagged", 20);
            return webElement.getText();
    	}
    	
	}
	
}
