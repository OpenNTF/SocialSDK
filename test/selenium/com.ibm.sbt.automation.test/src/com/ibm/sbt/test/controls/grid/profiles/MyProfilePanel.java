/**
 * 
 */
package com.ibm.sbt.test.controls.grid.profiles;

import static org.junit.Assert.assertTrue;
import junit.framework.Assert;

import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.automation.core.test.BaseTest;
import com.ibm.sbt.automation.core.test.pageobjects.PanelResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;
import com.ibm.sbt.automation.core.utils.Trace;

/**
 * @author mwallace
 *
 */
public class MyProfilePanel extends BaseTest {

	/**
	 * Constructor
	 */
	public MyProfilePanel() {
		setAuthType(AuthType.AUTO_DETECT);
	}
	
	@Override
	protected boolean isEnvironmentValid() {
		return super.isEnvironmentValid() && !environment.isLibrary("jquery") && !environment.isSmartCloud();
	}

    @Test
    public void testProfilePanel() {
        assertTrue("Expected the test to generate a grid", checkPanel("Social_Profiles_Controls_MyProfilePanel"));
    }																

    /**
     * Check the panel
     * 
     * @param snippetId
     * @return
     */
    protected boolean checkPanel(String snippetId) {
    	PanelResultPage resultPage = launchPanel(snippetId);
        
        return checkPanel(resultPage, snippetId);
    }
    
    /**
     * Launch the list snippet and return a PanelResultPage
     * 
     * @param snippetId
     * @return
     */
    protected PanelResultPage launchPanel(String snippetId) {
        ResultPage resultPage = super.launchSnippet(snippetId, authType);
        return new PanelResultPage(resultPage);
    }
    
    /**
     * Return true if a Panel was created on the page i.e. could find ul, multiple li's
     * @return
     */
    protected boolean checkPanel(PanelResultPage resultPage, String snippetId) {
       Trace.log("Panel result page: " + resultPage.getText());
       
       String photoUrl = resultPage.getPhotoUrl();
       Assert.assertFalse("Invalid photo url", StringUtil.isEmpty(photoUrl));
       
       String[] details = resultPage.getDetails();
       Assert.assertFalse("Invalid details", details == null || details.length == 0);
       Assert.assertEquals("Invalid details", 4, details.length);
       
       return true;
    }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.automation.core.test.BaseTest#getAuthenticatedMatch()
     */
    @Override
    public String getAuthenticatedMatch() {
    	return getProperty("sample.displayName1");
    }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.automation.core.test.BaseTest#getAuthenticatedCondition()
     */
    @Override
    public String getAuthenticatedCondition() {
    	return "linkText";
    }
    	
}
