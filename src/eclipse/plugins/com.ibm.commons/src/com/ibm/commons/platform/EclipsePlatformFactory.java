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


package com.ibm.commons.platform;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;

import com.ibm.commons.Platform;



/**
 * Eclipse Platform Factory.
 * @ibm-not-published
 */
public class EclipsePlatformFactory implements IPlatformFactory {
  
	public EclipsePlatformFactory() {
	}
	
	public Platform createPlatform() {
		// Look if someone implemented an extension point to retrive the platform
        IExtensionRegistry reg = org.eclipse.core.runtime.Platform.getExtensionRegistry();
        if (reg != null) {
            IConfigurationElement[] elt = reg.getConfigurationElementsFor("com.ibm.commons.PlatformFactory"); // $NON-NLS-1$
            for( int i=0; i<elt.length; i++ ) {
                if( "platformFactory".equalsIgnoreCase(elt[i].getName()) ) { // $NON-NLS-1$
                    try {
                        Object o = elt[i].createExecutableExtension("class"); // $NON-NLS-1$
                        IPlatformFactory factory = (IPlatformFactory)o;
                        return factory.createPlatform();
                    } catch(Throwable ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
		
		// Return the default Eclipse platform
		return new GenericEclipsePlatform();
	}
}
