/*
 * © Copyright IBM Corp. 2012-2013
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

package com.ibm.commons.platform;

import java.io.PrintStream;

import com.ibm.commons.Platform;
import com.ibm.commons.log.LogMgrFactory;
import com.ibm.commons.log.jdk.JdkLogMgrFactory;


/**
 * Base implementation of the platform object.
 * 
 * @ibm-not-published 
 */
public class GenericPlatform extends Platform {

    public static final String GENERIC_PLATFORM = "WebAppServer"; // $NON-NLS-1$

    
    public GenericPlatform() {
    }
    protected void initialize() {
    }

    public String getName() {
        return "Generic"; // $NLS-GenericPlatform.Generic-1$
    }
    
    public boolean isPlatform(String name) {
        if(GENERIC_PLATFORM.equals(name)) {
            return true;
        }
        return super.isPlatform(name);
    }
    
    public final boolean isEclipseBased() {
        return false;
    }
    
    public PrintStream getOutputStream() {
        return java.lang.System.out;
    }
    
    public PrintStream getErrorStream() {
        return java.lang.System.err;
    }
    
    protected LogMgrFactory createLogMgrFactory() {
        return new JdkLogMgrFactory(); 
    }

    public boolean isFeatureEnabled (String featureId){
        return false;
    }
}
