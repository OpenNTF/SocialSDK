package com.ibm.sbt.test.controls.view.files;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.test.pageobjects.BaseResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;

public class FilesView extends BaseApiTest {

	static String SNIPPET_ID = "Social_Files_Views_FilesView";

	public FilesView() {
		setAuthType(AuthType.AUTO_DETECT);
	}
	
	@Test
	public void testFilesView() {
		WidgetResultPage resultPage = launchWidget(SNIPPET_ID);
		
		Assert.assertTrue(resultPage.getText().startsWith("Files"));
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
        return "id";
    }

    /*
     * (non-Javadoc) @see com.ibm.sbt.automation.core.test.BaseTest#getAuthenticatedMatch()
     */
    @Override
    public String getAuthenticatedMatch() {
        return "uniqName_9_0";
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
	        	
	}
	
}
