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
 * permissions and limitations under the License.
 */
package com.ibm.sbt.test.js.authentication;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.ibm.sbt.automation.core.test.BaseServiceTest;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;

/**
 * @author mwallace
 * 
 * @date 6 Mar 2013
 */
public class Authentication extends BaseServiceTest {

    @Test
    public void testNoError() {
        setAuthType(AuthType.NONE,true);
    	ResultPage p = launchSnippet("Authentication_Authentication_Summary");
        
        WebDriver webDriver = p.getWebDriver();
		webDriver.findElement(By.id("logoutsmartcloudOA2"));
        webDriver.findElement(By.id("loginsmartcloudOA2"));
        webDriver.findElement(By.id("logoutconnections"));
        webDriver.findElement(By.id("loginconnections"));
        webDriver.findElement(By.id("logoutconnectionsOA2"));
        webDriver.findElement(By.id("loginconnectionsOA2"));
        webDriver.findElement(By.id("logoutsmartcloud"));
        webDriver.findElement(By.id("loginsmartcloud"));
        //Disabling the domino endpoint login/logout buttons.
        //webDriver.findElement(By.id("logoutdomino"));
       // webDriver.findElement(By.id("logindomino"));
        
    }
    @Override
    public String getAuthenticatedMatch() {
    	return "authenticationTable";
    }

}
