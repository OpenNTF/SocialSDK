/*
 * © Copyright IBM Corp. 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at,
 * 
 * http,//www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.sbt.test.js.sbt;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.test.BaseServiceTest;
import com.ibm.sbt.automation.core.test.BaseTest;
import com.ibm.sbt.automation.core.test.connections.BaseCommunitiesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;

/**
 * @author mwallace
 *  
 * @date 25 Mar 2013
 */
public class RequireModules extends BaseServiceTest {
    
    static final String SNIPPET_ID = "Toolkit_RequireModules";
    
    @Test
    public void testResponseHeaders() {
        boolean result = checkNoError(SNIPPET_ID);
        assertTrue(getNoErrorMsg(), result);
    }

}
