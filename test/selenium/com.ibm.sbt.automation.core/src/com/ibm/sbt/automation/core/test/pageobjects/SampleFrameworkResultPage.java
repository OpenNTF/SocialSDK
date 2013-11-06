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
package com.ibm.sbt.automation.core.test.pageobjects;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author Francis
 * @date 24 May 2013
 */
public class SampleFrameworkResultPage extends BaseResultPage {

    private ResultPage delegate;

    /*
     * ids
     */
    public static final String JSSNIPPETDIV = "jsContents";
    public static final String JSPSNIPPETDIV = "jspContents";
    public static final String HTMLSNIPPETDIV = "htmlContents";
    public static final String CSSSNIPPETDIV = "cssContents";
    public static final String DOCSNIPPETDIV = "docContents";
    public static final String TREE = "tree";
    public static final String EXPANDALL = "expandAll";
    public static final String COLLAPSEALL = "collapseAll";
    public static final String SNIPPETCONTAINER = "snippetContainer";
    public static final String RUNBUTTON = "runButton";
    public static final String DEBUGBUTTON = "debugButton";
    public static final String SHOWHTMLBUTTON = "showHtmlButton";
    public static final String SHOWHTMLPOPOUT = "showHtmlPopout";
    public static final String PREVIEWFRAME = "previewFrame";
    public static final String DEMOCONTAINER = "demoContainer";
    public static final String SMARTCLOUDNAVBAR = "nav_bar_include";
    /*
     * classes
     */
    public static final String LEAFNODECLASS = "leafNode";
    public static final String SNIPPETNAVBARCLASS = ".nav.nav-tabs";
    public static final String MAINCONTENTCLASS = ".row-fluid";
    
    public SampleFrameworkResultPage(ResultPage delegate) {
        this.delegate = delegate;
        setWebDriver(delegate.getWebDriver());
    }

    /*
     * (non-Javadoc)
     * @see com.ibm.sbt.automation.core.test.pageobjects.ResultPage#getText()
     */
    @Override
    public String getText() {
        return delegate.getText();
    }

    /*
     * (non-Javadoc)
     * @see com.ibm.sbt.automation.core.test.pageobjects.ResultPage#getWebElement()
     */
    @Override
    public WebElement getWebElement() {
        return delegate.getWebElement();
    }

    /**
     * Return the html span WebElement which must be there for a card to appear.
     * 
     * @return the WebElement
     */
    public WebElement getMainContent() {
        return getWebElement().findElement(By.cssSelector(MAINCONTENTCLASS));
    }

    /**
     * Return the tree of the sample framework page.
     * 
     * @return the WebElement
     */
    public WebElement getTree() {
        return getWebElement().findElement(By.id(TREE));
    }
    
    /**
     * Return the smartcloud navigation bar of the sample framework page.
     * 
     * @return the WebElement
     */
    public WebElement getSmartcloudNavBar() {
        return getWebElement().findElement(By.id(SMARTCLOUDNAVBAR));
    }
    
    public WebElement getExpandAllButton(){
        return getWebElement().findElement(By.id(EXPANDALL));
    }
    
    public WebElement getCollpaseAllButton(){
        return getWebElement().findElement(By.id(COLLAPSEALL));
    }
    
    /**
     * Return a leaf node. Useful for testing if a click on the leaf node works.
     * @return
     */
    public WebElement getTreeLeaf() {
        List<WebElement> leafNodes = getWebElement().findElements(By.className(LEAFNODECLASS));
        if (leafNodes.size() == 0){
            getExpandAllButton().click();
            leafNodes = getWebElement().findElements(By.className(LEAFNODECLASS));
        }
        
        return leafNodes.get(0);
    }
    
    /**
     * Return the div containing the code and documentation snippets.
     * 
     * @return the WebElement
     */
    public WebElement getSnippetContainer() {
        return getWebElement().findElement(By.id(SNIPPETCONTAINER));
    }
    
    /**
     * Return the div which contains the snippet of js code.
     * 
     * @return the WebElement
     */
    public WebElement getJsSnippetDiv() {
        return getWebElement().findElement(By.id(JSSNIPPETDIV));
    }
    
    /**
     * Return the div which contains the snippet of js code.
     * 
     * @return the WebElement
     */
    public WebElement getJspSnippetDiv() {
        return getWebElement().findElement(By.id(JSPSNIPPETDIV));
    }
    
    /**
     * Return the div which contains the snippet of html code.
     * 
     * @return the WebElement
     */
    public WebElement getHtmlSnippetDiv() {
        return getWebElement().findElement(By.id(HTMLSNIPPETDIV));
    }
    
    /**
     * Return the div which contains the snippet of css code.
     * 
     * @return the WebElement
     */
    public WebElement getCssSnippetDiv() {
        return getWebElement().findElement(By.id(CSSSNIPPETDIV));
    }
    
    /**
     * Return the div which contains the snippet of doc.
     * 
     * @return the WebElement
     */
    public WebElement getDocSnippetDiv() {
        return getWebElement().findElement(By.id(DOCSNIPPETDIV));
    }
    
    /**
     * Return the navbar which switches between code divs.
     * 
     * @return the WebElement
     */
    public WebElement getCodeNav() {
        return getWebElement().findElement(By.cssSelector(SNIPPETNAVBARCLASS));
        
    }

    /**
     * Return the div containing the run/debug/show html buttons and the demo iframe.
     * 
     * @return the WebElement
     */
    public WebElement getDemoContainer() {
        return getWebElement().findElement(By.id("DEMOCONTAINER"));
    }

    /**
     * Return the run button.
     * 
     * @return the WebElement
     */
    public WebElement getRunButton() {
        return getWebElement().findElement(By.id(RUNBUTTON));
    }

    /**
     * Return the debug button.
     * 
     * @return the WebElement
     */
    public WebElement getDebugButton() {
        return getWebElement().findElement(By.id(DEBUGBUTTON));
    }

    /**
     * Return the show html button.
     * 
     * @return the WebElement
     */
    public WebElement getShowHtmlButton() {
        return getWebElement().findElement(By.id(SHOWHTMLBUTTON));
    }

    /**
     * Return the iframe containing the snippet preview.
     * 
     * @return the WebElement
     */
    public WebElement getPreviewFrame() {
        return getWebElement().findElement(By.id(PREVIEWFRAME));
    }
    
    /**
     * Return the div which shows the generated html.
     * 
     * @return the WebElement
     */
    public WebElement getHtmlPopout() {
        return getWebElement().findElement(By.id(SHOWHTMLPOPOUT));
    }

}
