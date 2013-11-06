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
package com.ibm.sbt.automation.core.test.connections;

import junit.framework.Assert;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseApiTest;

/**
 * @author mwallace
 *  
 * @date 15 Jun 2013
 */
public class BaseSearchTest extends BaseApiTest {
    
    public BaseSearchTest() {
        setAuthType(AuthType.AUTO_DETECT);
    }
    
    protected void assertResultValid(JsonJavaObject json, boolean hasRelevance) {
    	assertResultValid(json, hasRelevance, false);
    }
    
    protected void assertResultValid(JsonJavaObject json) {
    	assertResultValid(json, false, false);
    }
    
    protected void assertResultValid(JsonJavaObject json, boolean hasRelevance, boolean hasContent) {
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        Assert.assertNotNull(json.getString("getTitle"));
        if (hasRelevance) {
        	Assert.assertNotNull(json.getString("getRelevance"));
        } 
        Assert.assertNotNull(json.getString("getUpdated"));
        if (hasContent) {
        	Assert.assertNotNull(json.getString("getContent"));
        }
    }

    protected void assertFacetValueValid(JsonJavaObject json) {
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        Assert.assertNotNull(json.getString("getId"));
        Assert.assertNotNull(json.getString("getLabel"));
        Assert.assertNotNull(json.getString("getWeight"));
    }

}
