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
