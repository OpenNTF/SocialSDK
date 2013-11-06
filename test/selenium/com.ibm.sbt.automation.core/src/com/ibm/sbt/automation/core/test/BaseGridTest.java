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
package com.ibm.sbt.automation.core.test;

import java.util.List;

import junit.framework.Assert;

import org.openqa.selenium.WebElement;

import com.ibm.sbt.automation.core.test.pageobjects.GridPagerPage;
import com.ibm.sbt.automation.core.test.pageobjects.GridResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.GridSorterPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;
import com.ibm.sbt.automation.core.utils.Trace;


/**
 * @author mwallace
 *  
 * @date 5 Mar 2013
 */
public class BaseGridTest extends BaseTest {
	
	public BaseGridTest() {
		setAuthType(AuthType.AUTO_DETECT);
	}
    
    /**
     * Return true if a Grid was created on the page i.e. could find table, tbody and multiple tr's
     * @return
     */
    protected boolean checkGrid(String snippetId) {
        return checkGrid(snippetId, false, false);
    }
    
    @Override
    protected boolean isEnvironmentValid() {
    	return super.isEnvironmentValid() && !environment.isLibrary("jquery");
    }
    /**
     * Return true if a Grid was created on the page i.e. could find table, tbody and multiple tr's
     * @return
     */
    protected boolean checkGrid(String snippetId, boolean hasPager) {
        return checkGrid(snippetId, hasPager, false);
    }
    
    protected boolean checkGrid(String snippetId, boolean hasPager, boolean hasSorter){
        GridResultPage resultPage = launchGrid(snippetId);
        
        return checkGrid(resultPage, hasPager, hasSorter, snippetId);
    }
    
    /**
     * Return true if a Grid was created on the page i.e. could find table, tbody and multiple tr's
     * @return
     */
    protected boolean checkGrid(GridResultPage resultPage, boolean hasPager, boolean hasSorter, String snippetId) {

       if(resultPage.getText().contains("Empty")){
    	   return true;
       }
        
        WebElement table = resultPage.getTable();
        if (table == null) {
        	Assert.fail("Unable to find table for snippet: " + snippetId);
        }
        
        WebElement tbody = resultPage.getTableBody();
        if (tbody == null) {
        	Assert.fail("Unable to find tbody for snippet: " + snippetId);
        }
        
        List<WebElement> rows = resultPage.getTableRows();
        if (rows == null || rows.isEmpty()) {
        	Assert.fail("Unable to find rows for snippet: " + snippetId);
        }
        
        boolean pagerOk = true;
        /*if (hasPager) {
        	//creating a string array to hold the text value of each row
        	//because when the next page link is clicked rows.get(i).getText, will not work because
        	//the element is no longer attached to the dom
        	String[] tableRowText = new String[rows.size()];
        	for(int i=0;i<tableRowText.length;i++){
        		tableRowText[i] = rows.get(i).getText();
        	}
        	
        	//check that when next is clicked a new set of results is displayed,
        	//by comparing with results from the previous page
            pagerOk = checkPager(resultPage,tableRowText);
            
            if (!pagerOk) {
            	Assert.fail("Unable to page for snippet: " + snippetId);
            }            
        }*/
        
        boolean sorterOk = true;
       /* if (hasSorter) {
        	
        	List<WebElement> rowsafterPaging = resultPage.getTableRows();
        	String[] tableRowText = new String[rowsafterPaging.size()];
        	for(int i=0;i<tableRowText.length;i++){
        		tableRowText[i] = rowsafterPaging.get(i).getText();
        	}
        	
            sorterOk = checkSorter(resultPage,tableRowText);
            
            if (!sorterOk) {
            	Assert.fail("Unable to sort for snippet: " + snippetId);
            }            
        }*/
        return (table != null) && (tbody != null) && (rows != null && !rows.isEmpty()) && pagerOk && sorterOk;
    }
        
    /**
     * @param resultPage
     * @return
     */
    protected boolean checkPager(GridResultPage gridPage,String[] rows) {
        GridPagerPage gridPager = gridPage.getGridPager();
        
        //If paging is available
        if (gridPager.canPageNext()){
        	gridPager.nextPage();
        	//wait for the page number to change
        	waitForText("/html/body/div[3]/div/div/div[2]", 5, "5 - 10");
        	// wait for the rows of the grid to load after changing page
        	waitForChildren("table", "tbody/tr[5]", 10);
        } else {
        	return true;
        }
        
        //Check that the results of the grid have changed
        boolean nextPageLinkIsWorking = true;
        List<WebElement> rowsAfterNextPageWasClicked = gridPage.getTableRows();
        for(int i=0;i<rowsAfterNextPageWasClicked.size();i++){
        	//if the set of rows in page 2 is different than the set of rows in page 1
        	//then the pager is working. - if the rows do not change, then pager is not working
        	
        	//System.out.println(rows[i]+" == "+rowsAfterNextPageWasClicked.get(i).getText());
        	if(rows[i].equals(rowsAfterNextPageWasClicked.get(i).getText())){
               	Assert.fail("Same row after paging next " + rows[i] + " == " + rowsAfterNextPageWasClicked.get(i).getText());
        		nextPageLinkIsWorking = false; 
        	}
        }
   
        //If we can move back a page
        if (gridPager.canPagePrevious()){
        	//move back to the first page
        	gridPager.previousPage(gridPage);
           	//wait to go back to page 1
        	waitForText("/html/body/div[3]/div/div/div[2]", 5, "0 - 5");
        	//wait for the rows of the grid to load after changing page
        	waitForChildren("table", "tbody/tr[5]", 5);
        } else {
        	//we should be able to go back after clicking next, if we can't return false;
           	Assert.fail("Unable to page to previous: " + gridPage.getGridPager().getPagerDiv().getText());
        	return false;
        }
        
        //check that the results of clicking previous are the same as what we has originally
        boolean previousPageLinkIsWorking = true;
        List<WebElement> rowsAfterPrevWasClicked = gridPage.getTableRows();
        for(int i=0;i<rowsAfterPrevWasClicked.size();i++){
        	//if the set of rows in page 2 is different than the set of rows in page 1
        	//then the sorter is working. - if the rows do not change, then pager is not working
        	if(!rows[i].equals(rowsAfterPrevWasClicked.get(i).getText())){
               	Assert.fail("Same row after paging previous " + rows[i] + " == " + rowsAfterPrevWasClicked.get(i).getText());
        		previousPageLinkIsWorking = false;
        	}
        }
         return previousPageLinkIsWorking && nextPageLinkIsWorking;
    }

    /**
     * @param resultPage
     * @return
     */
    protected boolean checkSorter(GridResultPage resultPage, String[] rows) {
    	boolean sortingIsOkay = true;
    	
    	GridSorterPage sortPage = resultPage.getSortingPager();

    	sortPage.SortByFirstSortAnchor();
    	
    	//wait for the rows of the grid to load after changing page
    	waitForChildren("table", "tbody/tr[5]", 5);
    	
    	List<WebElement> rowsAfterSorting = resultPage.getTableRows();
    	
    	//Compare the the table rows before the sort to after the sort, they should have changed
    	for(int i=0;i<rows.length;i++){
    		
    		//if no change in results, sorting has done nothing
    		if(rows[i] == rowsAfterSorting.get(i).getText()){
    			sortingIsOkay = false;
    		}
    	}
    	

        return sortingIsOkay;
    }

    /**
     * Launch the grid snippet and return a GridResultPage
     * 
     * @param snippetId
     * @return
     */
    protected GridResultPage launchGrid(String snippetId) {
        ResultPage resultPage = super.launchSnippet(snippetId, authType);
        return wrapResultPage(resultPage);
    }
    
    /**
     * Wrap the environment result page in a GridResultPage
     * 
     * @param resultPage
     * @return
     */
    protected GridResultPage wrapResultPage(ResultPage resultPage) {
        return new GridResultPage(resultPage);
    }

    /**
     * most grid samples use this div and not the content div to inject themselves;
     * further extend this on specific tests if they fill another div after being
     * authenticated
     */
    @Override
    public String getAuthenticatedMatch() {
    	return "table";
    }
    
    @Override
    public String getAuthenticatedCondition() {
    	// TODO Auto-generated method stub
    	return "tagName";
    }
}
