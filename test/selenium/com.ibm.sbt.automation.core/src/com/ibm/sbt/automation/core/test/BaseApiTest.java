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

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.junit.Rule;
import org.junit.After;
import org.junit.rules.TestName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeFactory;
import com.ibm.commons.runtime.impl.app.RuntimeFactoryStandalone;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.sbt.automation.core.environment.TestEnvironment;
import com.ibm.sbt.automation.core.environment.TestEnvironmentFactory;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;
import com.ibm.sbt.automation.core.utils.Trace;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.security.authentication.password.PasswordException;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;

/**
 * @author mwallace
 *  
 * @date 5 Mar 2013
 */
public class BaseApiTest extends BaseTest {
	
	@Rule public TestName name = new TestName();

    private Application application;
    private Context context;
    private String snippetId;
    
    public BaseApiTest() {
        RuntimeFactory runtimeFactory = new RuntimeFactoryStandalone() {
            @Override
            public Context initContext(Application application, Object request, Object response) {
                Context context = super.initContext(application, request, response);
                TestEnvironmentFactory.getEnvironment().decorateContext(context);
                return context;
            }
        };
        application = runtimeFactory.initApplication(null);
        createContext();
    }

    /**
     * Return the endpoint name to use
     */
    public String getEndpointName() {
        if (environment.isSmartCloud()) {
            return "smartcloud";
        } else {
            return "connections";
        }
    }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.automation.core.test.BaseTest#getAuthenticatedMatch()
     */
    @Override
    public String getAuthenticatedMatch() {
        return "json";
    }

    /**
     * Create the context (if needed)
     */
    protected Context createContext() {
        if (context == null) {
            context = Context.init(application, null, null);
        }
        return context;
    }
    
    /**
     * Destroy the context
     */
    @After
    public void destroyContext() {
        if (context != null) {
            Context.destroy(context);
            context = null;
        }
    }
    
    /**
     * @throws PasswordException
     */
    protected void loginConnections() throws AuthenticationException {
        BasicEndpoint endpoint = (BasicEndpoint)EndpointFactory.getEndpoint(getEndpointName());
        String username = null;
        String password = null;            
        if (environment.isSmartCloud()) {
            username = getProperty(TestEnvironment.PROP_OAUTH10_USERNAME);
            password = getProperty(TestEnvironment.PROP_OAUTH10_PASSWORD);            
        } else {
            username = getProperty(TestEnvironment.PROP_BASIC_USERNAME);
            password = getProperty(TestEnvironment.PROP_BASIC_PASSWORD);            
        }
        endpoint.login(username, password, true);
    }
    
    /**
     * @param snippetId
     * @return
     */
    protected JavaScriptPreviewPage executeSnippet(String snippetId) {
        return executeSnippet(snippetId, 0);
    }
    
    /**
     * @param snippetId
     * @return
     */
    protected JavaScriptPreviewPage executeSnippet(String snippetId, long delay) {
    	this.snippetId = snippetId;
        ResultPage resultPage = launchSnippet(snippetId, authType, delay);
        JavaScriptPreviewPage previewPage = new JavaScriptPreviewPage(resultPage);
        Trace.log(previewPage.getText());
        return previewPage;
    }
    
    protected void saveTestDataAndResults() {
    	String fileBase = getSnippetFile();
    	File testData = getTestDataDir();
    	
    	WebDriver webDriver = environment.getWebDriver();
    	
    	WebElement jsonElement = webDriver.findElement(By.id("json"));
        String jsonText = jsonElement.getText();
        writeFile(testData, fileBase + ".result", jsonText);
        
    	WebElement mockDataElement = webDriver.findElement(By.id("mockData"));
        String mockDataText = mockDataElement.getText();
        writeFile(testData, fileBase + ".mock", mockDataText);
    }
    
	protected void writeFile(File folder, String fileName, String contents) {
		try {
			if (StringUtil.isEmpty(contents)) {
				return;
			}
			
			File file = (new File(folder, fileName)).getAbsoluteFile();
			if (file.exists()) {
				if (!environment.isOverwriteTestdata()) {
					return;
				} else {
					file.delete();
				}
			}
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(contents.getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected File getTestDataDir() {
    	try {
    		String currentDir = System.getProperty("user.dir");
    		String samplesJsBase = environment.getProperty("samples.js.base");
    		String snippetPath = getSnippetPath();
    		return new File(currentDir + File.separatorChar + samplesJsBase + File.separatorChar + snippetPath);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
	
	protected String getSnippetPath() {
		int index = snippetId.indexOf("API_");
		String snippetPath = snippetId.substring(0, index+3);
		return snippetPath.replace('_', File.separatorChar);
	}

	protected String getSnippetFile() {
		int index = snippetId.indexOf("API_");
		String snippetFile = snippetId.substring(index+4);
		return snippetFile.replace('_', ' ');
	}

    protected String getTestFileBase() {
    	return this.getClass().getCanonicalName() + "." + name.getMethodName();
    }
    
    /**
     * Wait the specified interval for a json list with the specified number of entries
     */
    public WebElement waitForJsonList(final int count, final int secs) {
        return waitForJsonList(count, null, secs);
    }
    
    /**
     * Wait the specified interval for a json list with the specified number of entries
     */
    public WebElement waitForJsonList(final int count, final JsonValidator jsonValidator, final int secs) {
        try {
            return (new WebDriverWait(environment.getWebDriver(), secs)).until(new ExpectedCondition<WebElement>() {
                @Override
                public WebElement apply(WebDriver webDriver) {
                    WebElement element = webDriver.findElement(By.id("json"));
                    String text = element.getText();
                    if (StringUtil.isNotEmpty(text)) {
                        try {
                            List jsonList = (List)JsonParser.fromJson(JsonJavaFactory.instanceEx, text);
                            if (jsonList.size() >= count) {
                                if (jsonValidator != null) {
                                    for (int i=0; i<jsonList.size(); i++) {
                                        Object object = jsonList.get(i);
                                        if (object instanceof JsonJavaObject) {
                                            if (!jsonValidator.isValid(i, (JsonJavaObject)object)) {
                                                return null;
                                            }
                                        }
                                    }
                                }
                                return element;
                            }
                        } catch (JsonException je) {}
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Interface used to check the content of a JSON object
     */
    public interface JsonValidator {
        
        /**
         * Return true if the JSON object is in valid state for test validation
         */
        boolean isValid(int index, JsonJavaObject json);
        
    }
    
}
