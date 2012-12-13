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

package com.ibm.commons.util.profiler;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;



/**
 * Interface with the Java Profiler API.
 * @ibm-not-published 
 */
public class JVMPIInterface {

    private static Instrumentation instrumentation;
    
    static {
        try {
            // Look if a well known agent had been installed 
            Class<?> c = JVMPIInterface.class.getClassLoader().getSystemClassLoader().loadClass("com.ibm.commons.util.profiler.JVMPIAgent");  // $NON-NLS-1$
            
            // Then read its instrumentation
            Field f = c.getField("instrumentation"); // $NON-NLS-1$
            instrumentation = (Instrumentation)f.get(null);
        } catch(Throwable ex) {
            // Not available, or error...
            //ex.printStackTrace();
        }
    }
    
    public static Instrumentation getInstrumentation() {
        return instrumentation;
    }
}