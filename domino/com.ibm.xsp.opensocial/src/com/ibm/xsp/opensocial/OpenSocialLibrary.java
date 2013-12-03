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

package com.ibm.xsp.opensocial;

import com.ibm.xsp.library.AbstractXspLibrary;


/**
 * OpenSocial XPages Library
 */
public class OpenSocialLibrary extends AbstractXspLibrary {

	public OpenSocialLibrary() {
	}

	@Override
	public String getLibraryId() {
        return "com.ibm.xsp.opensocial.library"; // $NON-NLS-1$
    }

    @Override
	public String getPluginId() {
        return "com.ibm.xsp.opensocial"; // $NON-NLS-1$
    }
    
    @Override
	public String[] getDependencies() {
        return new String[] {
            "com.ibm.xsp.core.library",     // $NON-NLS-1$
            "com.ibm.xsp.extsn.library",    // $NON-NLS-1$
            "com.ibm.xsp.domino.library",   // $NON-NLS-1$
            "com.ibm.xsp.extlib.library"    // $NON-NLS-1$
        };
    }
    
    @Override
	public String[] getXspConfigFiles() {
        return null;
    }
    
    @Override
	public String[] getFacesConfigFiles() {
        return null;
    }
}
