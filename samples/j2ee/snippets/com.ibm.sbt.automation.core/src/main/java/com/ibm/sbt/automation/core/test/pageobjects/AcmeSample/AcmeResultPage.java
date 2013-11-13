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
package com.ibm.sbt.automation.core.test.pageobjects.AcmeSample;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.ibm.sbt.automation.core.test.pageobjects.BaseResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;

/**
 * @author David Ryan
 * 
 * @date 27 Mar 2013
 */
public class AcmeResultPage extends BaseResultPage {

    @FindBy(how = How.XPATH, using = "/html/body")
    
    private final String homePageMainContentId = "mainContainer";
    
    private WebElement content;
    private ResultPage delegate;
    
    public AcmeResultPage(ResultPage delegate) {
        this.delegate = delegate;
        
        setWebDriver(delegate.getWebDriver());
    }
    
    public WebElement getMainContent(){
    	return getWebElement().findElement(By.id(homePageMainContentId));
    }
    

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
        return delegate.getWebElement();
    }

}
