/*
 * � Copyright IBM Corp. 2013
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
package com.ibm.sbt.test.js.base;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.connections.communities.CommunityService;

/**
 * @author mwallace
 *  
 * @date 25 Mar 2013
 */
public class ConnectionsServices extends BaseApiTest {
	
	private List<String> errors = new ArrayList<String>();
    
    static final String SNIPPET_ID = "Toolkit_Base_ConnectionsServices";
    
    @Test
    public void testConnectionsServices() {
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        
        validate(CommunityService.class, getMethods(jsonList, "CommunityService"));
        
        StringBuilder msg = new StringBuilder();
        for (String error : errors) {
        	msg.append(error).append("\n");
        }
    	System.err.println(msg.toString());
        Assert.assertTrue(msg.toString(), errors.isEmpty());
    }
    
    
    protected void validate(Class clazz, String[] jsMethods) {
    	Method[] javaMethods = clazz.getMethods();
    	
    	for (String jsMethod : jsMethods) {
    		validateMethodExists(clazz.getSimpleName(), jsMethod, javaMethods);
    	}
		for (Method javaMethod : javaMethods) {
    		validateMethodExists(clazz.getSimpleName(), javaMethod, jsMethods);
		}
    }
    
	protected void validateMethodExists(String type, String jsMethod, Method[] javaMethods) {
		for (Method javaMethod : javaMethods) {
			if (jsMethod.equals(javaMethod.getName())) {
				// found method
				int modifiers = javaMethod.getModifiers();
				Class[] exceptionTypes = javaMethod.getExceptionTypes();
				return;
			}
		}
		
		String msg = MessageFormat.format("Unable to find Java method {0} in {1}", jsMethod, type);
		errors.add(msg);
	}

	protected void validateMethodExists(String type, Method javaMethod, String[] jsMethods) {
		for (String jsMethod : jsMethods) {
			if (jsMethod.equals(javaMethod.getName())) {
				// found method
				return;
			}
		}
		
		String msg = MessageFormat.format("Unable to find JavaScript method {0} in {1}", javaMethod.getName(), type);
		errors.add(msg);
	}


	protected String[] getMethods(List jsonList, String type) {
    	for (int i=0; i<jsonList.size(); i++) {
            JsonJavaObject json = (JsonJavaObject)jsonList.get(i);
            if (type.equals(json.getAsString("type"))) {
            	List methods = json.getAsList("methods");
            	return (String[])methods.toArray(new String[methods.size()]);
            }
        }
    	return new String[0];
    }

}
