package com.ibm.sbt.automation.core.environment;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.automation.core.test.BaseTest;
import com.ibm.sbt.automation.core.test.BaseTest.AuthType;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;
import com.ibm.sbt.automation.core.utils.Trace;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;

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
 * @author mkataria
 * @author mwallace
 * 
 * @date Jan 10, 2013
 */
public abstract class TestEnvironment {

    private WebDriver webDriver;
    private boolean quitDriver = true;
    private int loginTimeout = 20;
    private Properties properties;
    private Map<String, String> snippetParams = new HashMap<String, String>();
    private boolean smartCloud;

    protected String jsLib;

    // Need to create property bundles for localizable values
    protected String BasicLoginTitle = "Authentication";
    protected String OAuth10LoginTitle = "Log In";
    protected String OAuth20LoginTitle = "Log In to IBM Connections";
    protected String OAuth20AuthTitle = "Authorize access to IBM Connections";
	private boolean takeScreenshot;
	private String screenshotsPath;

    static final public String PROP_ENVIRONMENT                 = "environment"; //$NON-NLS-1$
    static final public String PROP_JAVASCRIPT_LIB              = "jslib"; //$NON-NLS-1$
    static final public String PROP_USERNAME                    = "username"; //$NON-NLS-1$
    static final public String PROP_PASSWORD                    = "password"; //$NON-NLS-1$
    static final public String PROP_RESTART_BROWSER             = "restart.browser"; //$NON-NLS-1$
    static final public String PROP_LOGIN_TIMEOUT               = "login.timeout"; //$NON-NLS-1$
    static final public String PROP_BROWSER                     = "browser"; //$NON-NLS-1$
    static final public String PROP_WEBDRIVER_IE_DRIVER         = "webdriver.ie.driver"; //$NON-NLS-1$
    static final public String PROP_WEBDRIVER_CHROMER_DRIVER    = "webdriver.chrome.driver"; //$NON-NLS-1$
    static final public String PROP_CHROME_BINARY               = "chrome.binary"; //$NON-NLS-1$
    static final public String PROP_USER_DIR                    = "user.dir"; //$NON-NLS-1$
    static final public String PROP_SBT_SAMPLE_WEB_URL          = "sbt.sample.web.url"; //$NON-NLS-1$
    static final public String PROP_SBT_PLAYGROUND_URL          = "playground.web.url"; //$NON-NLS-1$
    static final public String PROP_ACME_SAMPLE_URL				= "acme.url";
    static final public String PROP_BASIC_LOGINFORMID           = "basic.loginFormId"; //$NON-NLS-1$
    static final public String PROP_BASIC_USERNAMEID            = "basic.usernameId"; //$NON-NLS-1$
    static final public String PROP_BASIC_PASSWORDID            = "basic.passwordId"; //$NON-NLS-1$
    static final public String PROP_BASIC_SUBMITID              = "basic.submitId"; //$NON-NLS-1$
    static final public String PROP_BASIC_USERNAME              = "basic.username"; //$NON-NLS-1$
    static final public String PROP_BASIC_PASSWORD              = "basic.password"; //$NON-NLS-1$
    static final public String PROP_OAUTH10_LOGINFORMID         = "oauth10.loginFormId"; //$NON-NLS-1$
    static final public String PROP_OAUTH10_USERNAMEID          = "oauth10.usernameId"; //$NON-NLS-1$
    static final public String PROP_OAUTH10_PASSWORDID          = "oauth10.passwordId"; //$NON-NLS-1$
    static final public String PROP_OAUTH10_SUBMITID            = "oauth10.submitId"; //$NON-NLS-1$
    static final public String PROP_OAUTH10_USERNAME            = "oauth10.username"; //$NON-NLS-1$
    static final public String PROP_OAUTH10_PASSWORD            = "oauth10.password"; //$NON-NLS-1$
    static final public String PROP_OAUTH20_LOGINFORMXPATH      = "oauth20.loginFormXPath"; //$NON-NLS-1$
    static final public String PROP_OAUTH20_USERNAMEXPATH       = "oauth20.usernameXPath"; //$NON-NLS-1$
    static final public String PROP_OAUTH20_PASSWORDXPATH       = "oauth20.passwordXPath"; //$NON-NLS-1$
    static final public String PROP_OAUTH20_SUBMITXPATH         = "oauth20.submitXPath"; //$NON-NLS-1$
    static final public String PROP_OAUTH20_GRANTXPATH          = "oauth20.grantXPath"; //$NON-NLS-1$
    static final public String PROP_OAUTH20_USERNAME            = "oauth20.username"; //$NON-NLS-1$
    static final public String PROP_OAUTH20_PASSWORD            = "oauth20.password"; //$NON-NLS-1$
    static final public String PROP_GENERATE_SCREENSHOTS		= "screenshots.enabled"; //$NON-NLS-1$
    static final public String PROP_SCREENSHOTS_BASE_PATH		= "screenshots.base"; //$NON-NLS-1$
    static final public String PROP_ENABLE_SMARTCLOUD           = "enable.smartcloud"; //$NON-NLS-1$
    static final public String PROP_ENABLED_TRACE				= "enable.trace"; //$NON-NLS-1$
    static final public String PROP_FIREBUG_ENABLED             = "firebug.enabled"; //$NON-NLS-1$
    static final public String PROP_ENABLE_MOCKTRANSPORT        = "enable.mocktransport"; //$NON-NLS-1$
    static final public String PROP_ENABLE_DEBUGTRANSPORT       = "enable.debugtransport"; //$NON-NLS-1$
    static final public String PROP_ENABLE_OVERWRITETESTDATA    = "enabled.overwritetestdata"; //$NON-NLS-1$

    private static final String PROP_OVERRIDE_CONNECTIONS_BE    = "connections.override.url"; //$NON-NLS-1$
    private static final String PROP_OVERRIDE_SMARTCLOUD_BE     = "smartcloud.override.url"; //$NON-NLS-1$

    static final String sourceClass = TestEnvironment.class.getName();
    static final Logger logger = Logger.getLogger(sourceClass);

    /**
     * Perform cleanup
     */
    public static void cleanup() {
        TestEnvironment environment = TestEnvironmentFactory.getEnvironment();
        if (environment != null) {
            environment.snippetParams.clear();
            environment.quitDriver();
        }
    }
    
    /**
     * Clean the browser state. 
     * The browser cookies are removed and 
     * it is returned to the default empty page
     */
    public void cleanBrowserState() {
        if (webDriver!=null){
            //because the session cookie is HttpOnly, selenium can't clear it.
            //browser cleanup happens by cosing it and letting the next getWebDriver() 
            //call create a new browser from scratch
            webDriver.quit();
            webDriver=null;
        }
    }
    /**
     * Default constructor
     */
    public TestEnvironment() {
        properties = loadProperties();

        // JS toolkit to test with
        jsLib = properties.getProperty(PROP_JAVASCRIPT_LIB);

        String restart = properties.getProperty(PROP_RESTART_BROWSER, "false");
        quitDriver = "true".equalsIgnoreCase(restart);

        String timeout = properties.getProperty(PROP_LOGIN_TIMEOUT, "30");
        loginTimeout = Integer.parseInt(timeout);
        
        String ts = properties.getProperty(PROP_GENERATE_SCREENSHOTS, "false");
        takeScreenshot = "true".equalsIgnoreCase(ts);

        // Where screenshot go
        screenshotsPath = properties.getProperty(PROP_SCREENSHOTS_BASE_PATH);
        if (takeScreenshot && StringUtil.isEmpty(screenshotsPath)) {
        	logger.severe("no screenshot path defined, please define system property " +PROP_SCREENSHOTS_BASE_PATH);
        	takeScreenshot = false;
        }
    
        // Enable SmartCloud
        String enableSmartCloud = properties.getProperty(PROP_ENABLE_SMARTCLOUD, "false");
        if ("true".equalsIgnoreCase(enableSmartCloud)) {
            enableSmartCloud();
        }
    }

    /**
     * Return the specified test property
     * 
     * @param name
     * @return
     */
    public String getProperty(String name) {
        return properties.getProperty(name);
    }
    
    /**
     * Enable SmartCloud environment
     */
    public void enableSmartCloud() {
        smartCloud = true;
        logger.config("Enabling Smartcloud");
        addSnippetParam("env", getProperty("smartcloud.env"));
    }
    
    /**
     * Enable SmartCloud environment
     */
    public void disableSmartCloud() {
        smartCloud = false;
        removeSnippetParam("env");
    }
    
    /**
     * Return true if SmartCloud environment is enabled.
     * 
     * @return
     */
    public boolean isSmartCloud() {
        return smartCloud;
    }
    
    /**
     * Return true if mock transport is being used.
     * @return
     */
    public boolean isMockTransport() {
    	return properties.getProperty(PROP_ENABLE_MOCKTRANSPORT, "false").equals("true");
    }

    /**
     * Return true if debug transport is being used.
     * @return
     */
    public boolean isDebugTransport() {
    	return properties.getProperty(PROP_ENABLE_DEBUGTRANSPORT, "false").equals("true");
    }

    /**
     * Return true if test data should be overwritten.
     * @return
     */
    public boolean isOverwriteTestdata() {
    	return properties.getProperty(PROP_ENABLE_OVERWRITETESTDATA, "false").equals("true");
    }

    /**
     * Remove a snippet param
     * @param key
     */
    public void removeSnippetParam(String key) {
        snippetParams.remove(key);
    }
    
    /**
     * Add a snippet param which will be passed to the snippet when it is invoked
     * @param key
     * @param value
     */
    public void addSnippetParam(String key, String value) {
        snippetParams.put(key, value);
    }
    
    /**
     * Add a snippet param which will be passed to the snippet when it is invoked
     * @param key
     * @param value
     */
    public void addSnippetParam(String key, String[] values) {
        snippetParams.put(key, StringUtil.concatStrings(values, ',', true));
    }
        
    /**
     * Return the WebDriver
     * 
     * @return
     */
    public WebDriver getWebDriver() {
        if (webDriver == null) {
            // browser to test with
            String browserName = System.getProperty(PROP_BROWSER);
            if ("ie".equals(browserName)) {
                initInternetExplorerDriver();
            } else if ("chrome".equals(browserName)) {
                initChromeDriver();
            } else if ("safari".equals(browserName)) {
                webDriver = new SafariDriver();
            }  else if ("headless".equals(browserName)) {
                HtmlUnitDriver driver = new HtmlUnitDriver(true);
                webDriver = driver;
            } else {
                webDriver = new FirefoxDriver();
            }
        }
        return webDriver;
    }

    /**
     * @return true to quit the driver after each test
     */
    public boolean isQuitDriver() {
        return quitDriver;
    }

    /**
     * Close the web driver
     */
    public void closeDriver() {
        if (webDriver != null) {
            webDriver.close();
        }
    }

    /**
     * Quit the web driver
     */
    public void quitDriver() {
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
    }

    /**
     * Launch the specified snippet
     * 
     * @param snippetId
     * 
     * @return true if the snippet could be launched
     */
    public ResultPage launchSnippet(BaseTest baseTest) {
        if (logger.isLoggable(Level.FINE)) {
            logger.entering(sourceClass, "launchSnippet", new Object[] { baseTest });
        }
        
        if (properties.getProperty(PROP_FIREBUG_ENABLED, "false").equals("true")) {
            addSnippetParam("debug", "true");
        }
        if (properties.getProperty(PROP_ENABLE_MOCKTRANSPORT, "false").equals("true")) {
            addSnippetParam("mockTransport", "true");
        }
        if (properties.getProperty(PROP_ENABLE_DEBUGTRANSPORT, "false").equals("true")) {
            addSnippetParam("debugTransport", "true");
        }

        String launchUrl = computeLaunchUrl(baseTest);
        Trace.log(launchUrl);
        if (logger.isLoggable(Level.FINE)) {
            logger.exiting(sourceClass, "computeLaunchUrl", new Object[] { baseTest, launchUrl });
        }
        if (StringUtil.isEmpty(launchUrl)) {
            return null;
        }
        //TODO call this init
        webDriver = getWebDriver();
        String windowHandle = webDriver.getWindowHandle();

        webDriver.get(launchUrl);
        WebElement webElement = authenticate(baseTest, null, windowHandle);

        if (baseTest.getAuthType() != AuthType.NONE && baseTest.getAuthType() != AuthType.AUTO_DETECT ) {
            if (!baseTest.isResultsReady()) { //try waiting some more for the page to refresh
                baseTest.waitForResult(1);
            }
            if (!baseTest.isResultsReady()) { //let it go if the page is ready according to the test's condition
                assertNotNull("Unable to confirm authentication for: " + baseTest.getSnippetId(), webElement);
            }
        }

        return getPageObject();
    }

    // Protected stuff

    /**
     * Authenticate and return the WebElement for the specified match/condition
     * 
     * If authType == NONE then null is returned
     */
    protected WebElement authenticate(BaseTest baseTest, AuthType authType, String originalHandle) {
        if (logger.isLoggable(Level.FINE)) {
            logger.entering(sourceClass, "authenticate", new Object[] { baseTest });
        }
        

        if (authType == null) {
            authType = baseTest.getAuthType();
        }

        switch (authType) {
        case NONE:
            break;
        case AUTO_DETECT:
            return handleAutoDetect(baseTest, originalHandle);
        case BASIC:
            handleBasicLogin(baseTest);
            break;
        case OAUTH10:
            handleOAuth10(baseTest);
            break;
        case OAUTH20:
            handleOAuth20(baseTest);
            break;
        }
        // restore window handle
        restoreWindowHandle(webDriver, originalHandle);

        // wait to confirm result page has displayed
        WebElement webElement = baseTest.waitForResult(loginTimeout);
        if (logger.isLoggable(Level.FINE)) {
            logger.exiting(sourceClass, "authenticate", webElement);
        }

        return webElement;
    }

    /*
     * Handle auto detection of the authentication mechanism
     */
    protected WebElement handleAutoDetect(BaseTest baseTest, String originalHandle) {
        WebElement form = searchAnyAuthenticationForm(baseTest, loginTimeout);
        if (baseTest.isResultsReady()) {
        	// results for this test are ready so return them here
        	return form;
        }
        
        AuthType authType = detectAuthType(baseTest, form);
        if (authType != AuthType.NONE) {
        	Trace.log("handleAutoDetect: " + form.getText() + " - " + authType);
        }

        return authenticate(baseTest, authType, originalHandle);
    }

    /*
     * Handle basic authentication login
     */
    protected void handleBasicLogin(BaseTest baseTest) {
        String loginFormId = baseTest.getProperty(PROP_BASIC_LOGINFORMID);
        String usernameId = baseTest.getProperty(PROP_BASIC_USERNAMEID);
        String passwordId = baseTest.getProperty(PROP_BASIC_PASSWORDID);
        String submitId = baseTest.getProperty(PROP_BASIC_SUBMITID);
        
        String username = null;
        String password = null;
        if (isSmartCloud()) {
        	username = baseTest.getProperty(PROP_OAUTH10_USERNAME);
            password = baseTest.getProperty(PROP_OAUTH10_PASSWORD);
        } else {
        	username = baseTest.getProperty(PROP_BASIC_USERNAME);
            password = baseTest.getProperty(PROP_BASIC_PASSWORD);
        }

        WebElement loginForm = waitForLoginForm(loginTimeout, loginFormId, null, BasicLoginTitle, baseTest);
        if (baseTest.isResultsReady()) return;
        if (loginForm != null) {
            WebElement usernameEl = loginForm.findElement(By.name(usernameId));
            WebElement passwordEl = loginForm.findElement(By.name(passwordId));
            WebElement submitEl = loginForm.findElement(By.name(submitId));
            usernameEl.sendKeys(username);
            passwordEl.sendKeys(password);
            submitEl.click();
        } else {
            //check if page was authenticated before
            if (baseTest.waitForResult(0)!=null) return;
            fail("Unable to locate basic login form");
        }
    }

    /*
     * Handle OAuth1.0 authentication
     */
    protected void handleOAuth10(BaseTest baseTest) {
        String loginFormId = baseTest.getProperty(PROP_OAUTH10_LOGINFORMID);
        String usernameId = baseTest.getProperty(PROP_OAUTH10_USERNAMEID);
        String passwordId = baseTest.getProperty(PROP_OAUTH10_PASSWORDID);
        String submitId = baseTest.getProperty(PROP_OAUTH10_SUBMITID);
        String username = baseTest.getProperty(PROP_OAUTH10_USERNAME);
        String password = baseTest.getProperty(PROP_OAUTH10_PASSWORD);

        WebElement loginForm = waitForLoginForm(loginTimeout, loginFormId, null, OAuth10LoginTitle, baseTest);
        if (baseTest.isResultsReady()) return;
        if (loginForm != null) {
            WebElement usernameEl = loginForm.findElement(By.name(usernameId));
            WebElement passwordEl = loginForm.findElement(By.name(passwordId));
            WebElement submitEl = loginForm.findElements(By.id(submitId)).get(0);
            usernameEl.sendKeys(username);
            passwordEl.sendKeys(password);
            submitEl.click();
        } else {
            fail("Unable to locate OAuth1.0 login form");
        }
    }

    /*
     * Handle OAuth2.0 authentication
     */
    protected void handleOAuth20(BaseTest baseTest) {
        String loginFormXPath = baseTest.getProperty(PROP_OAUTH20_LOGINFORMXPATH);
        String usernameXPath = baseTest.getProperty(PROP_OAUTH20_USERNAMEXPATH);
        String passwordXPath = baseTest.getProperty(PROP_OAUTH20_PASSWORDXPATH);
        String submitXPath = baseTest.getProperty(PROP_OAUTH20_SUBMITXPATH);
        String grantXPath = baseTest.getProperty(PROP_OAUTH20_GRANTXPATH);
        String username = baseTest.getProperty(PROP_OAUTH20_USERNAME);
        String password = baseTest.getProperty(PROP_OAUTH20_PASSWORD);

        WebElement loginForm = waitForLoginForm(loginTimeout, null, loginFormXPath, OAuth20LoginTitle, baseTest);
        if (baseTest.isResultsReady()) return;

        if (loginForm != null) {
            WebElement usernameEl = loginForm.findElement(By.xpath(usernameXPath));
            WebElement passwordEl = loginForm.findElement(By.xpath(passwordXPath));
            WebElement submitEl = loginForm.findElements(By.xpath(submitXPath)).get(0);
            usernameEl.sendKeys(username);
            passwordEl.sendKeys(password);
            submitEl.click();

            // wait for authorization popop
            WebElement authPage = waitForPopup(loginTimeout, OAuth20AuthTitle);
            if (authPage != null) {
                WebElement grantEl = authPage.findElement(By.xpath(grantXPath));
                grantEl.click();
            } else {
                fail("Unable to locate OAuth2.0 authorization page");
            }
        } else {
            fail("Unable to locate OAuth2.0 login form");
        }
    }

    /**
     * Detect what type of authentication is being used
     * @param form 
     * 
     * @return
     */
    public AuthType detectAuthType(BaseTest baseTest, WebElement loginForm) {
    	// if results are available then no need to check for authentication
        if (baseTest.isResultsReady()) {
        	return AuthType.NONE;
        }
    	
        // look for all variations of login form
        String basicLoginFormId = baseTest.getProperty(PROP_BASIC_LOGINFORMID);
        String oauth10LoginFormId = baseTest.getProperty(PROP_OAUTH10_LOGINFORMID);
        String[] loginFormIds = { basicLoginFormId, oauth10LoginFormId };
        String oauth20LoginFormXPath = baseTest.getProperty(PROP_OAUTH20_LOGINFORMXPATH);
        String[] loginFormXPathExprs = { oauth20LoginFormXPath };
        if (loginForm != null) {
            String loginFormId = loginForm.getAttribute("id");
            if (basicLoginFormId.equals(loginFormId)) {
                return AuthType.BASIC;
            } else if (oauth10LoginFormId.equals(loginFormId)) {
                return AuthType.OAUTH10;
            } else {
                // TODO handle multiple login form xpath exprs
                return AuthType.OAUTH20;
            }
        }
        return AuthType.NONE;
    }

    /**
     * Wait the specified interval for the one of the authentication screens to
     * appear
     */
    private WebElement searchAnyAuthenticationForm(final BaseTest baseTest, final int secs) {
        String basicLoginFormId = baseTest.getProperty(PROP_BASIC_LOGINFORMID);
        String oauth10LoginFormId = baseTest.getProperty(PROP_OAUTH10_LOGINFORMID);
        String[] loginFormIds = { basicLoginFormId, oauth10LoginFormId };
        String oauth20LoginFormXPath = baseTest.getProperty(PROP_OAUTH20_LOGINFORMXPATH);
        String[] loginFormXPathExprs = { oauth20LoginFormXPath };
        String[] loginTitles = { BasicLoginTitle, OAuth10LoginTitle, OAuth20LoginTitle };
        return waitForLoginForm(secs, loginFormIds, loginFormXPathExprs, loginTitles, baseTest);
    }
    
    /**
     * Return true if the WebElement represents a login form
     */
    public boolean isLoginForm(BaseTest baseTest, WebElement webElement) {
        String basicLoginFormId = baseTest.getProperty(PROP_BASIC_LOGINFORMID);
        String oauth10LoginFormId = baseTest.getProperty(PROP_OAUTH10_LOGINFORMID);
        String[] loginFormIds = { basicLoginFormId, oauth10LoginFormId };
        String oauth20LoginFormXPath = baseTest.getProperty(PROP_OAUTH20_LOGINFORMXPATH);
        String[] loginFormXPathExprs = { oauth20LoginFormXPath };
    	
        for (int i = 0; i < loginFormIds.length; i++) {
            if (StringUtil.isNotEmpty(loginFormIds[i])) {
                try {
                    if (loginFormIds[i] != null) {
                        WebElement loginForm = webElement.findElement(By.id(loginFormIds[i]));
                        if (loginForm != null) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        for (int i = 0; i < loginFormXPathExprs.length; i++) {
            if (StringUtil.isNotEmpty(loginFormXPathExprs[i])) {
                try {
                    if (loginFormXPathExprs[i] != null) {
                        WebElement loginForm = webElement.findElement(By.xpath(loginFormXPathExprs[i]));
                        if (loginForm != null) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        return false;
    }

    /**
     * Wait the specified interval for the popup with the specified title to
     * appear
     */
    public WebElement waitForPopup(final int secs, final String title) {
        return waitForPopups(secs, new String[] { title });
    }
    /**
     * Wait the specified interval for any popup with the specified titles to
     * appear
     */
    public WebElement waitForPopups(final int secs, final String[] titles) {
        try {
            return (new WebDriverWait(getPageObject().getWebDriver(), secs)).until(new ExpectedCondition<WebElement>() {
                @Override
                public WebElement apply(WebDriver webDriver) {
                    failIfPageCrashed(webDriver);
                    WebDriver popup = findPopup(titles);
                    if (popup != null) {
                        return popup.findElement(By.tagName("body"));
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Wait the specified interval for the login form to appear
     */
    public WebElement waitForLoginForm(int secs, String id, String xpathExpr, String title, final BaseTest baseTest) {
        return waitForLoginForm(secs, new String[] { id }, new String[] { xpathExpr }, new String[] { title }, baseTest);
    }

    /**
     * Wait the specified interval for the login form to appear
     */
    public WebElement waitForLoginForm(final int secs, final String[] ids, final String[] xpathExprs, final String[] titles, final BaseTest baseTest) {
        try {
            return (new WebDriverWait(getPageObject().getWebDriver(), secs)).pollingEvery(1, TimeUnit.SECONDS)
                    .until(new ExpectedCondition<WebElement>() {
                
                boolean lookForResult = false;
                
                @Override
                public WebElement apply(WebDriver webDriver) {
                    failIfPageCrashed(webDriver);
                    
                    
                    WebElement loginForm = findLoginForm(getPageObject(webDriver).getWebDriver(), ids, xpathExprs, titles);
                    
                    if (loginForm == null && lookForResult) {
                        WebElement result = baseTest.waitForResult(0);
                            if (result!=null) {
                            // wait for result may not flag ready if overridden
                            baseTest.setResultsReady();
                            // returning the result not a login form
                            return result;
                        }
                    } 
                    lookForResult = true;
                    return loginForm;
                }

            });
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Wait the specified interval for the specified web element to be available
     */
    public WebElement waitForElement(final String match, final int secs, final String condition) {
        try {
            return (new WebDriverWait(getPageObject().getWebDriver(), secs)).until(new ExpectedCondition<WebElement>() {
                @Override
                public WebElement apply(WebDriver webDriver) {
                    failIfPageCrashed(webDriver);
                    webDriver = getPageObject(webDriver).getWebDriver();
                    if (condition.equalsIgnoreCase("id")) {
                        return webDriver.findElement(By.id(match));
                    } else if (condition.equalsIgnoreCase("linkText")) {
                        return webDriver.findElement(By.linkText(match));
                    } else if (condition.equalsIgnoreCase("tagName")) {
                        return webDriver.findElement(By.tagName(match));
                    } else if (condition.equalsIgnoreCase("name")) {
                        return webDriver.findElement(By.name(match));
                    } else if (condition.equalsIgnoreCase("idWithText")) {
                        WebElement element = webDriver.findElement(By.id(match));
                        String text = element.getText();
                        if (StringUtil.isNotEmpty(text)) {
                            return element;
                        }
                        String value = element.getAttribute("value");
                        if (StringUtil.isNotEmpty(value)) {
                            return element;
                        }
                        return null;
                    } else if (condition.equalsIgnoreCase("idWithChild")) {
                        WebElement element = webDriver.findElement(By.id(match));
                        List<WebElement> children = element.findElements(By.xpath("*"));
                        if (!children.isEmpty()) {
                            return element;
                        }
                        return null;
                    } else {
                        return webDriver.findElement(By.name(match));
                    }
                }
            });
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Wait the specified interval for the specified web element to be available
     * and for it to have non empty content
     */
    public WebElement waitForText(final String id, final int secs) {
        try {
            return (new WebDriverWait(getPageObject().getWebDriver(), secs)).until(new ExpectedCondition<WebElement>() {
                @Override
                public WebElement apply(WebDriver webDriver) {
                    failIfPageCrashed(webDriver);
                    webDriver = getPageObject(webDriver).getWebDriver();
                    WebElement element = webDriver.findElement(By.id(id));
                    String text = element.getText();
                    if (StringUtil.isNotEmpty(text)) {
                        return element;
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Wait the specified interval for the specified web element to be available
     * and for it to have non empty content
     * @param xPath This is the xPath expression used to find the element 
     * @param secs This is the amount of seconds to wait before timing out
     * @param expectedText This is the text that you expect the element to have
     */
    public WebElement waitForText(final String xPath, final int secs, final String expectedText) {
        try {
            return (new WebDriverWait(getPageObject().getWebDriver(), secs)).until(new ExpectedCondition<WebElement>() {
                @Override
                public WebElement apply(WebDriver webDriver) {
                    failIfPageCrashed(webDriver);
                    webDriver = getPageObject(webDriver).getWebDriver();
                    WebElement element = webDriver.findElement(By.xpath(xPath));
                    String text = element.getText();
                    if (text!=null && text.contains(expectedText)) {
                        return element;
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Wait the specified interval for the specified web element to be available with the specified children
     */
    public WebElement waitForChildren(final String tagName, final String xpath, final int secs) {
        try {
            return (new WebDriverWait(getPageObject().getWebDriver(), secs)).until(new ExpectedCondition<WebElement>() {
                @Override
                public WebElement apply(WebDriver webDriver) {
                	failIfPageCrashed(getPageObject(webDriver).getWebDriver());
                    WebElement tableElement = getPageObject(webDriver).getWebDriver().findElement(By.tagName(tagName));
                    WebElement element = tableElement.findElement(By.xpath(xpath));  
                    return element;
                }
            });
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Wait the specified interval for the specified web element to be available
     */
    public WebElement waitForText(final String id, final String match, final int secs) {
        try {
            return (new WebDriverWait(getPageObject().getWebDriver(), secs)).until(new ExpectedCondition<WebElement>() {
                @Override
                public WebElement apply(WebDriver webDriver) {
                    failIfPageCrashed(getPageObject(webDriver).getWebDriver());
                    WebElement element = getPageObject(webDriver).getWebDriver().findElement(By.id(id));
                    String text = element.getText();
                    if (text != null && text.contains(match)) {
                        return element;
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Dump the specified result page to the trace log
     */
    public void dumpWebElement(WebElement webElement) {
        Trace.log(webElement + " tagName:" + webElement.getTagName() + " text:" + webElement.getText() + " id:" + webElement.getAttribute("id") + " displayed:"
                + webElement.isDisplayed());

        List<WebElement> webElements = webElement.findElements(By.xpath("*"));
        if (webElements.size() > 0) {
            Trace.log("Children size: " + webElements.size());
            for (int i = 0; i < webElements.size(); i++) {
                WebElement nextElement = webElements.get(i);
                Trace.log("[" + i + "]" + nextElement + " tagName:" + webElement.getTagName() + " text:" + nextElement.getText() + " id:"
                        + nextElement.getAttribute("id") + " displayed:" + nextElement.isDisplayed());
            }
        }
    }

    /**
     * Dump the page source to the trace log
     */
    public void dumpPageSource(WebDriver webDriver) {
        String pageSource = webDriver.getPageSource();
        Trace.log("Page source: " + pageSource);
    }

    /**
     * Find the login form and optionally include popups
     */
    protected WebElement findLoginForm(SearchContext webDriver, String[] ids, String[] xpathExprs, String[] titles) {
        WebElement loginForm = findLoginForm(webDriver, ids, xpathExprs);
        if (loginForm != null) {
            return loginForm;
        }

        if (titles != null) {
            // look for authentication popup
            WebDriver popup = findPopup(titles);
            if (popup != null) {
                loginForm = findLoginForm(popup, ids, xpathExprs);
                if (loginForm != null) {
                    return loginForm;
                }
            }
        }

        return null;
    }

    /**
     * Find the basic authentication login form
     */
    protected WebElement findLoginForm(SearchContext wd, String[] ids, String[] xpathExprs) {
        for (int i = 0; i < ids.length; i++) {
            if (StringUtil.isNotEmpty(ids[i])) {
                try {
                    if (ids[i] != null) {
                        WebElement loginForm = wd.findElement(By.id(ids[i]));
                        if (loginForm != null) {
                            return loginForm;
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        for (int i = 0; i < xpathExprs.length; i++) {
            if (StringUtil.isNotEmpty(xpathExprs[i])) {
                try {
                    if (xpathExprs[i] != null) {
                        WebElement loginForm = wd.findElement(By.xpath(xpathExprs[i]));
                        if (loginForm != null) {
                            return loginForm;
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        return null;
    }

    /**
     * Return a WebDriver for the window with the specified title
     */
    public WebDriver findPopup(String title) {
        return findPopup(new String[] { title });
    }

    /**
     * Return a WebDriver for the window with one of the specified titles
     */
    public WebDriver findPopup(String[] titles) {
        if (titles == null || titles.length == 0) return null;
        WebDriver webDriver = getWebDriver();
        WebDriver popup = null;
        Set<String> windowHandles = webDriver.getWindowHandles();
        Iterator<String> windowIterator = windowHandles.iterator();
        while (windowIterator.hasNext()) {
            String windowHandle = windowIterator.next();
            popup = webDriver.switchTo().window(windowHandle);
            String title = popup.getTitle();
            for (int i = 0; i < titles.length; i++) {
                if (title != null && title.contains(titles[i])) {
                    return popup;
                }
            }
        }
        return null;
    }

    /**
     * tests whether the current environment is using the specified library
     * @param lib a library identifier, like dojo or jquery
     * @return true if the environment is using the specified library
     */
    public boolean isLibrary(String lib) {
        if (jsLib==null|| lib == null)return false;
        if (jsLib.startsWith(lib)) return true;
        return false;
    }
    
    /**
     * tests the version identifier of the current library
     * @param lib a library version, using non dotted format 
     * (i.e. 182 for jquery 1.8.2, 143 for dojo 1.4.3)
     * @return true if the environment is using the specified library
     */
    public boolean isLibraryVersion(String version) {
        if (jsLib==null|| version == null)return false;
        if (!version.matches("[\\D]"))return false;
        return jsLib.replaceAll("[\\D]", "").equals(version);
    }
    /**
     * tests the version identifier of the current library
     * @param lib a library version, using non dotted format 
     * (i.e. 182 for jquery 1.8.2, 143 for dojo 1.4.3)
     * @return
     */
    public boolean isLibraryVersionGreatherThan(String version) {
        if (jsLib==null|| version == null)return false;
        if (version.matches("[\\D]"))return false;
        return Integer.valueOf(jsLib.replaceAll("[\\D]", ""))> Integer.valueOf(version);
    }

    /**
     * Return the WebDriver being in use without creating it on demand
     * @return the current WebDriver
     */
    public WebDriver getCurrentDriver() {
        return webDriver;
    }

    /**
     * Return true if screenshots should be taken.
     * @return
     */
    public boolean isTakeScreenshots() {
        return takeScreenshot;
    }

    /**
     * Return the path where screenshots should be stored.
     * 
     * @return
     */
    public String getScreenshotsPath() {
        return screenshotsPath;
    }

    /**
     * @param webDriver
     * @param windowHandle
     */
    protected void restoreWindowHandle(WebDriver webDriver, String windowHandle) {
        Set<String> windowHandles = webDriver.getWindowHandles();
        if (!windowHandles.contains(windowHandle)) {
            // expected window handle doesn't exist
            Iterator<String> windowIterator = windowHandles.iterator();
            if (windowIterator.hasNext()) {
                windowHandle = windowIterator.next();
            }
        }
        webDriver.switchTo().window(windowHandle);
    }
    
    // Protected stuff

    /**
     * Load the test properties
     */
    protected Properties loadProperties() {
        Properties properties = new Properties();
        try {
            ClassLoader loader = getClass().getClassLoader();
            InputStream in = loader.getResourceAsStream("com/ibm/sbt/automation/core/environment/TestEnvironment.properties");
            if (in != null) {
                properties.load(in);
                in.close();
            }
            for (Object key : properties.keySet()) {
                if (key==null) continue;
                if(StringUtil.isNotEmpty(System.getProperty(key.toString()))) {
                    properties.put(key, System.getProperty(key.toString()));
                }
            }
        } catch (IOException ioe) {
        }
        return properties;
    }

    /**
     * @param baseTest
     * @param url
     * @return
     */
    protected String addSnippetParams(BaseTest baseTest, String url) {
        Map<String, String> params = baseTest.getSnippetParams();
        Iterator<Entry<String, String>> entries = params.entrySet().iterator();
        while (entries.hasNext()) {
            Entry<String, String> entry = entries.next();
            if(url.indexOf("?") != -1)
                url += "&" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue());
            else
                url += "?" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue());
        }
        
        // add params from the environment but do not override the same
        // param from the test
        entries = snippetParams.entrySet().iterator();
        while (entries.hasNext()) {
            Entry<String, String> entry = entries.next();
            if (!params.containsKey(entry.getKey())) {
                if(url.indexOf("?") != -1)
                    url += "&" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue());
                else
                    url += "?" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue());
            }
        }
        
        return url;
    }
    
    // Private stuff

    private void initInternetExplorerDriver() {
        String ieDriver = System.getProperty(PROP_WEBDRIVER_IE_DRIVER);
        if (StringUtil.isEmpty(ieDriver)) {
            String userDir = System.getProperty(PROP_USER_DIR);
            // TODO check the bitness of the OS and move this to test.properties
            String driverPath = userDir + "/../../../tools/com.ibm.sbtx.ci/selenium/iew32/IEDriverServer.exe";
            System.setProperty(PROP_WEBDRIVER_IE_DRIVER, driverPath);
        }
        webDriver = new InternetExplorerDriver() {
            @Override
            public void get(String url) {
                super.get(url);
                // FIX for self signed certificates
                String t = super.getCurrentUrl();
                if (t.contains("res://ieframe.dll/invalidcert.htm")) {
                    super.navigate().to("javascript:document.getElementById('overridelink').click()");
                }
            }
        };
    }

    private void initChromeDriver() {
        String chromeDriver = System.getProperty(PROP_WEBDRIVER_CHROMER_DRIVER);
        if (StringUtil.isEmpty(chromeDriver)) {
            String userDir = System.getProperty(PROP_USER_DIR);
            String driverPath = userDir + "/../../../tools/com.ibm.sbtx.ci/selenium/Chrome/chromedriver.exe";
            System.setProperty(PROP_WEBDRIVER_CHROMER_DRIVER, driverPath);
        }
        if (!StringUtil.isEmpty(System.getProperty(PROP_CHROME_BINARY))) {
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability("chrome.binary", System.getProperty(PROP_CHROME_BINARY));
            webDriver = new ChromeDriver(capabilities);
        } else {
            webDriver = new ChromeDriver();
        }
    }

    /**
     * this method tests for error pages from which the test cannot recover so
     * that instead of waiting the full timeout while testing for a until() to
     * happen we can fail the test early
     */
    private void failIfPageCrashed(WebDriver webDriver) {
        // TODO: populate with more conditions as we find them
        String text = getPageObject(webDriver).getText();
        if (webDriver.getTitle().contains("Apache Tomcat") && webDriver.getTitle().contains("Error report")) {
            fail(text);
        }
        if (text.startsWith("Error, unable to load snippet: ")) {
            fail(text);
        }
        if (text.contains("Unrecognized SSL message, plaintext connection?")) {
            fail("Cannot reach the quickstart image, probably firewall issues\n"+text);
        }        
        if (text.contains("oauth_consumer_missing_subscription")) {
            fail("Missing OAuth configuration -> 'oauth_consumer_missing_subscription'\n"+text);
        }        
        if (text.contains("Your account has been expired or suspended.")) {
            fail("Smartcloud credential probably expired\n"+text);
        }
    }

    // Abstract stuff

    /**
     * Perform a login
     * 
     * @return true if the log in operation succeeded
     */
    abstract public boolean login();

    /**
     * Compute the launch URL for the specified test
     * 
     * @param baseTest
     * @return
     */
    abstract public String computeLaunchUrl(BaseTest baseTest);

    /**
     * Return the result page for the current web driver
     * 
     * @return
     */
    public ResultPage getPageObject() {
        return getPageObject(getCurrentDriver());
    }
    
    
    /**
     * Return the result page for the specified web driver
     * 
     * @param webDriver
     * @return
     */
    abstract public ResultPage getPageObject(WebDriver webDriver);

    public void decorateContext(Context context) {
        try {
            if (!StringUtil.isEmpty(getProperty(PROP_OVERRIDE_CONNECTIONS_BE))) {
                BasicEndpoint connections = (BasicEndpoint) EndpointFactory.getEndpoint("connections");
                connections.setUrl(getProperty(PROP_OVERRIDE_CONNECTIONS_BE));
                context.getSessionMap().put("connections", connections);
            }
            
            if (!StringUtil.isEmpty(getProperty(PROP_OVERRIDE_SMARTCLOUD_BE))) {
                BasicEndpoint smartcloud = (BasicEndpoint) EndpointFactory.getEndpoint("smartcloud");
                smartcloud.setUrl(getProperty(PROP_OVERRIDE_SMARTCLOUD_BE));
                context.getSessionMap().put("smartcloud", smartcloud);
            }
        } catch (Throwable e) {
            logger.severe(e.getMessage());
        }
    }
}
