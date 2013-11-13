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
package com.ibm.sbt.automation.core.test.pageobjects;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;

/**
 * @author mwallace
 *  
 * @date 5 Mar 2013
 */
public class JavaScriptPreviewPage extends BaseResultPage {
    
    private ResultPage delegate;
    
    public JavaScriptPreviewPage(ResultPage delegate) {
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
    
    /**
     * Return the contents of the content div
     */
    public String getContent() {
        WebElement webElement = getWebElement();
        return webElement.findElement(By.id("content")).getText();
    }
    
    /**
     * Return the contents of the json div
     */
    public String getJsonContent() {
        WebElement webElement = getWebElement();
        return webElement.findElement(By.id("json")).getText();
    }
    
    /**
     * Return the contents of the json div as a JSON object
     */
    public JsonJavaObject getJson() {
        WebElement webElement = getWebElement();
        String text = webElement.findElement(By.id("json")).getText();
        try {
            return (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, text);
        } catch (Throwable t) {
        	Assert.fail("Unable to parse JSON Object from: " + text);
            return null;
        }
    }

    /**
     * Return the contents of the json div as a list of JSON objects
     */
    public List getJsonList() {
        WebElement webElement = getWebElement();
        String text = webElement.findElement(By.id("json")).getText();
        try {
            return (List)JsonParser.fromJson(JsonJavaFactory.instanceEx, text);
        } catch (Throwable t) {
        	Assert.fail("Unable to parse JSON List from: " + text);
            return null;
        }
    }

}
