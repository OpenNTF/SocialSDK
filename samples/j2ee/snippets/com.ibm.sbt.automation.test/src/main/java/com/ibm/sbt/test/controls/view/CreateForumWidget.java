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
package com.ibm.sbt.test.controls.view;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ibm.sbt.automation.core.test.connections.BaseForumsTest;
import com.ibm.sbt.automation.core.test.pageobjects.BaseResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;
import com.ibm.sbt.services.client.connections.forums.Forum;

public class CreateForumWidget extends BaseForumsTest {
	
	static String SNIPPET_ID = "ControlsEx_View_Forums_Create_Forum_Widget";

	public CreateForumWidget() {
		setAuthType(AuthType.AUTO_DETECT);
		
		createForum = false;
	}
	
	@Test
	public void testCreateForum() {
		WidgetResultPage resultPage = launchWidget(SNIPPET_ID);
		
		String name = createForumName();
		String forumUuid = resultPage.createForum(name, "tag1,tag2,tag3", name, true);
		Assert.assertNotNull("Error creating a forum: "+resultPage.getAlertText(), forumUuid);
		Forum forum = getForum(forumUuid);
		Assert.assertEquals("Forum title doesn't match", name, forum.getTitle());
		Assert.assertTrue("Forum tag1 not found", forum.getTags().contains("tag1"));
		Assert.assertTrue("Forum tag2 not found", forum.getTags().contains("tag2"));
		Assert.assertTrue("Forum tag3 not found", forum.getTags().contains("tag3"));
		Assert.assertEquals("Forum content doesn't match", name, forum.getContent());
		
		deleteForum(forumUuid);
    }
	
	@Test
	public void testCreateForumConflict() {
		WidgetResultPage resultPage = launchWidget(SNIPPET_ID);
		
		String forumUuid = resultPage.createForum("", "tag1,tag2,tag3", "", true);
		Assert.assertNull("Error created a forum"+resultPage.getAlertText(), forumUuid);
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
        return "name";
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
	    
        public WebElement getNameInput() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.name("name"));
        }
	    
        public WebElement getTagsInput() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.name("tags"));
        }
	    
        public WebElement getAutoFollowCheckbox() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.name("autoFollow"));
        }
	    
        public WebElement getDescriptionInput() {
            WebElement resultEl = getWebElement();
            return resultEl.findElement(By.name("description"));
        }
	    
        public WebElement getSaveBtn() {
            WebElement resultEl = getWidgetDiv();
            return resultEl.findElements(By.tagName("button")).get(0);
        }
	    
        public WebElement getCancelBtn() {
            WebElement resultEl = getWidgetDiv();
            return resultEl.findElements(By.tagName("button")).get(1);
        }
        
        public void setName(String name) {
            WebElement nameInput = getNameInput();
            nameInput.clear();
            nameInput.sendKeys(name);
        }

        public void setTags(String tags) {
            WebElement tagsInput = getTagsInput();
            tagsInput.clear();
            tagsInput.sendKeys(tags);
        }

        public void setDescription(String description) {
            WebElement descriptionInput = getDescriptionInput();
            descriptionInput.clear();
            descriptionInput.sendKeys(description);
        }
        
        public void setAutoFollow(boolean autoFollow) {
        	WebElement autoFollowCheckbox = getAutoFollowCheckbox();
        	if (autoFollow && !autoFollowCheckbox.isSelected()) {
        		autoFollowCheckbox.click();
        	} 
        	if (!autoFollow && autoFollowCheckbox.isSelected()) {
        		autoFollowCheckbox.click();
        	} 
        }

        public void clickSave() {
            getSaveBtn().click();
        }

        public void clickCancel() {
        	getCancelBtn().click();
        }
        
    	public String createForum(String name, String tags, String description, boolean autoFollow) {
    		setName(name);
    		setTags(tags);
    		setDescription(description);
    		setAutoFollow(autoFollow);
    		clickSave();
    		
            WebElement webElement = waitForText("alertDiv", "Successfully created", 20);
            String forumUuid = null;
            if (webElement != null) {
                String text = webElement.getText();
                if (text.startsWith("Successfully created")) {
                	int start = text.indexOf('[');
                	int end = text.indexOf(']');
                	return text.substring(start+1, end);
                }
            }
            return forumUuid;
    	}
    	
	}
	
}
