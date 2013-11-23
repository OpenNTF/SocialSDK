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
 * permissions and limitations under the License.*/

package com.ibm.sbt.automation.core.test.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class GridSorterPage {
	
	private GridResultPage gridPage;
    private WebElement sortingDiv;
    private WebElement sortAnchor;
    
    public String sortingXPath = "div/div[2]";
    public String sortAnchorXPath = "ul/li[2]/a";  
    
    public GridSorterPage(GridResultPage gridPage) {
        this.gridPage = gridPage;
        sortingDiv = gridPage.getGridContainer().findElement(By.xpath(sortingXPath));        
        sortAnchor = sortingDiv.findElement(By.xpath(sortAnchorXPath));        
    }
    
    
    public void SortByFirstSortAnchor(){
    	sortAnchor.click();
    }

}
