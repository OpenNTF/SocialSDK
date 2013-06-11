/*
 * © Copyright IBM Corp. 2010
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

package com.ibm.xsp.sbtsdk.library;

import com.ibm.xsp.library.AbstractXspLibrary;


/**
 * SBT SDK XPages Library
 */
public class SbtSdkLibrary extends AbstractXspLibrary {

	public SbtSdkLibrary() {
	}

	public String getLibraryId() {
        return "com.ibm.xsp.sbtsdk.library"; // $NON-NLS-1$
    }

    public boolean isDefault() {
		return true;
	}

    @Override
	public String getPluginId() {
        return "com.ibm.xsp.sbtsdk"; // $NON-NLS-1$
    }
    
    @Override
	public String[] getDependencies() {
        return new String[] {
            "com.ibm.xsp.core.library",     // $NON-NLS-1$
            "com.ibm.xsp.extsn.library",    // $NON-NLS-1$
            "com.ibm.xsp.domino.library",   // $NON-NLS-1$
        };
    }
    
    @Override
	public String[] getXspConfigFiles() {
        String[] files = new String[] {
                "com/ibm/xsp/sbtsdk/config/sbtsdk.xsp-config", // $NON-NLS-1$
            };
        return files;
    }
    
    @Override
	public String[] getFacesConfigFiles() {
        String[] files = new String[] {
                "com/ibm/xsp/sbtsdk/config/sbtsdk-faces-config.xml", // $NON-NLS-1$
            };
        return files;
    }
}
