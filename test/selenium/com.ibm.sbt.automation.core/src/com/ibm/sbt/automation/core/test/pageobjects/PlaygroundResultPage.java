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
package com.ibm.sbt.automation.core.test.pageobjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 * @author mwallace 
 * 
 * @date 5 Mar 2013
 */
public class PlaygroundResultPage extends BaseResultPage {

    @FindBy(how = How.XPATH, using = "/html/body")
    private WebElement content;

    
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.automation.core.environment.pageobjects.ContentPage#getText()
     */
    @Override
    public String getText() {
        return content.getText();
    }

    /* (non-Javadoc)
     * @see com.ibm.sbt.automation.core.environment.pageobjects.ContentPage#getWebElement()
     */
    @Override
    public WebElement getWebElement() {
        return content;
    }

}
