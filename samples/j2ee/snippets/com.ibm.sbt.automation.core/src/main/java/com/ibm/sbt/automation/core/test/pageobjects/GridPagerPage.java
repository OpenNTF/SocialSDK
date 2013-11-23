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

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ibm.commons.util.StringUtil;

/**
 * @author mwallace
 *  
 * @date 5 Mar 2013
 */
public class GridPagerPage {
    
    private GridResultPage gridPage;
    private WebElement pagerDiv;
    private WebElement pagerResultDiv;
    
    private int start;
    private int end;
    private int count;
    
    public String PagerXPath = "div/div[1]";
    public String PagerResultXPath = "div[2]";
   
    public String PagerNextXPath = "ul/li[2]/a";
    public String PagerPreviousXPath = "ul/li[1]/a";

    public GridPagerPage(GridResultPage gridPage) {
        this.gridPage = gridPage;
        
        pagerDiv = gridPage.getGridContainer().findElement(By.xpath(PagerXPath));        
        pagerResultDiv = pagerDiv.findElement(By.xpath(PagerResultXPath));
        
        int[] results = parsePagerResult();
        start = results[0];
        end = results[1];
        count = results[2];
    }
    
    /**
     * @return the start
     */
    public int getStart() {
        return start;
    }
    
    /**
     * @return the end
     */
    public int getEnd() {
        return end;
    }
    
    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * Return a WebElement for the pager div that was created on this page
     * 
     * @return
     */
    public WebElement getPagerDiv() {
        return pagerDiv;
    }

    /**
     * Return a WebElement for the pager result div that was created on this page
     * 
     * @return
     */
    public WebElement getPagerResultDiv() {
        return pagerResultDiv;
    }
    
    /**
     * Return true if you should be able to page forward
     * 
     * @return
     */
    public boolean canPageNext() {
        return (end < count);
    }
    
    /**
     * Return true if you should be able to page backward
     * 
     * @return
     */
    public boolean canPagePrevious() {
        return (count > 0);
    }
    
    /**
     * Click the next anchor
     */
    public void nextPage() {
        WebElement nextAnchor = pagerDiv.findElement(By.xpath(PagerNextXPath));
        nextAnchor.click();
    }
    
    /**
     * Click the next anchor
     */
    public void previousPage(GridResultPage gridPage) {
        pagerDiv = gridPage.getGridContainer().findElement(By.xpath(PagerXPath));
        WebElement previousAnchor = pagerDiv.findElement(By.xpath(PagerPreviousXPath));
        previousAnchor.click();

    }
    
    // Internals
    
    /*
     * This will be a string like this "0 - 5 of 29"
     * 
     * TODO need to handle localized versions of the pager results
     */
    private int[] parsePagerResult() {
        String pagerResult = pagerResultDiv.getText();

        String[] parts = StringUtil.splitString(pagerResult, ' ');
        int[] results = new int[3];
        results[0] = Integer.parseInt(parts[0]);
        results[1] = Integer.parseInt(parts[2]);
        results[2] = Integer.parseInt(parts[4]);
        return results;
    }

}
