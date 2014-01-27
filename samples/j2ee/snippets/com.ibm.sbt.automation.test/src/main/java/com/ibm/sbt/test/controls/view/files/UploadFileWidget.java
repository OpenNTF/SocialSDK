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

import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.automation.core.test.connections.BaseFilesTest;
import com.ibm.sbt.automation.core.test.pageobjects.BaseResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;

public class UploadFileWidget extends BaseFilesTest {
	
	static String SNIPPET_ID = "Social_Files_Views_Upload_File_Widget";

	public UploadFileWidget() {
		setAuthType(AuthType.AUTO_DETECT);
	}
	
	@Override
    protected boolean isEnvironmentValid() {
        return super.isEnvironmentValid() && !environment.isLibrary("jquery");
    }
	
	@Test
	public void testUploadFile() {
		WidgetResultPage resultPage = launchWidget(SNIPPET_ID);
		
		java.io.File file = createLocalFile();
		String tags = "tag1,tag2,tag3";
		String visibility = "public";
		boolean shareFilePropagate = true;
		String text = resultPage.uploadFile(file.getAbsolutePath(), tags, visibility, shareFilePropagate);
		Assert.assertEquals("Sucessfully uploaded "+file.getName(), text);
		
		file.delete();
    }
	
	@Test
	public void testUploadFileFail() {
		WidgetResultPage resultPage = launchWidget(SNIPPET_ID);
		
		java.io.File file = createLocalFile();
		boolean deleted = file.delete();
		System.out.println("deleted:"+deleted);
		System.out.println(file.getAbsolutePath());
		java.io.File tempFile1 = new java.io.File(file.getAbsolutePath());
		System.out.println("exists: "+tempFile1.exists());
		String tags = "tag1,tag2,tag3";
		String visibility = "public";
		boolean shareFilePropagate = true;
		String text = resultPage.uploadFile(file.getAbsolutePath(), tags, visibility, shareFilePropagate);
		Assert.assertEquals("The file could not be uploaded. Please try again later.", text);
    }
	
	@Test
	public void testUploadFileExists() {
		java.io.File file = createLocalFile();
		createFile(file.getName());
		
		WidgetResultPage resultPage = launchWidget(SNIPPET_ID);
		
		String[] parts = StringUtil.splitString(fileEntry.getTitle(), '.');
		String tags = "tag1,tag2,tag3";
		String visibility = "public";
		boolean shareFilePropagate = true;
		String text = resultPage.uploadFile(file.getAbsolutePath(), tags, visibility, shareFilePropagate);
		Assert.assertEquals("A file with this name already exists. Continue to upload as a new version or rename the file.", text);
    }
	
	@Test
	public void testUploadFileNewVersion() {
		// TODO
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
        return "_tags";
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
            return resultEl.findElement(By.name("_tags"));
        }
	    
        public WebElement getFileInput() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.name("_file"));
        }
	    
        public List<WebElement> getVisibilityRadioBtns() {
            WebElement resultEl = getWebElement();
            return resultEl.findElements(By.name("_visibility"));
        }
	            
        public WebElement getShareFilePropagateCheckbox() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.name("_shareFilePropagate"));
        }
	            
        public WebElement getUploadBtn() {
            WebElement resultEl = getWidgetDiv();
            return resultEl.findElements(By.tagName("button")).get(0);
        }
	    
        public WebElement getCancelBtn() {
            WebElement resultEl = getWidgetDiv();
            return resultEl.findElements(By.tagName("button")).get(1);
        }
        
        public void setFile(String file) {
            WebElement fileInput = getFileInput();
            fileInput.sendKeys(file);
        }

        public void setTags(String tags) {
            WebElement tagsInput = getTagsInput();
            tagsInput.clear();
            tagsInput.sendKeys(tags);
        }

        public void setShareFilePropagate(boolean shareFilePropagate) {
        	WebElement shareFilePropagateCheckbox = getShareFilePropagateCheckbox();
        	if (shareFilePropagate && !shareFilePropagateCheckbox.isSelected()) {
        		shareFilePropagateCheckbox.click();
        	} 
        	if (!shareFilePropagate && shareFilePropagateCheckbox.isSelected()) {
        		shareFilePropagateCheckbox.click();
        	} 
        }
        
        public void setVisibility(String visibility) {
        	List<WebElement> radioBtns = getVisibilityRadioBtns();
        	for (Iterator<WebElement> iter = radioBtns.iterator(); iter.hasNext(); ) {
        		WebElement radioBtn = iter.next();
        		if (visibility.equals(radioBtn.getAttribute("value"))) {
        			radioBtn.click();
        			return;
        		}
        	}
        }
        
        public void clickUpload() {
            getUploadBtn().click();
        }

        public void clickCancel() {
        	getCancelBtn().click();
        }
        
    	public String uploadFile(String file, String tags, String visibility, boolean shareFilePropagate) {
    		setFile(file);
    		setTags(tags);
    		setVisibility(visibility);
    		setShareFilePropagate(shareFilePropagate);
    		clickUpload();
    		
            WebElement webElement = waitForText("alertDiv", "*", 20);
            return webElement.getText();
    	}
    	
	}
	
}
