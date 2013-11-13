
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
package com.ibm.sbt.automation.core.environment;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.automation.core.test.BaseTest;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.SbtWebResultPage;


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

/**
 * @author DavidRyan
 * 
 * @date 27 may 2013
 */
public class AcmeEnvironment extends TestEnvironment {
   
    private String baseUrl;
    
    public AcmeEnvironment() {
        baseUrl = System.getProperty(TestEnvironment.PROP_ACME_SAMPLE_URL);
        if (StringUtil.isEmpty(baseUrl)) {
            baseUrl = getProperty(TestEnvironment.PROP_ACME_SAMPLE_URL);
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.sbt.automation.core.environment.TestEnvironment#login()
     */
    @Override
    public boolean login() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.ibm.sbt.automation.core.environment.TestEnvironment#computeLaunchUrl(com.ibm.sbt.automation.core.test.BaseTest)
     */
    @Override
    public String computeLaunchUrl(BaseTest baseTest) {
        /*String url = null;
        if (baseTest.getSnippetType() == SnippetType.JAVASCRIPT) {
            url = baseUrl + "/javascriptPreview.jsp?snippet=" + baseTest.getSnippetId() + "&jsLibId=" + jsLib;
        } else {
            url = baseUrl + "/javaPreview.jsp?snippet=" + baseTest.getSnippetId() + "&jsLibId=" + jsLib;
        }
        
        url = addSnippetParams(baseTest, url);*/
        
        return "http://acmeairlines.com:8080/acme.social.sample.webapp/";
    }

    /* (non-Javadoc)
     * @see com.ibm.sbt.automation.core.environment.TestEnvironment#getPageObject(org.openqa.selenium.WebDriver, java.lang.String)
     */
    @Override
    public ResultPage getPageObject(WebDriver webDriver) {
        ResultPage resultPage = (ResultPage)PageFactory.initElements(webDriver, SbtWebResultPage.class);
        resultPage.setWebDriver(webDriver);
        return resultPage;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Test environment for Acme Airlines Sample Application";
    }

}
