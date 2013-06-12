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

package nsf.playground.library;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ibm.commons.extension.ExtensionManager;
import com.ibm.xsp.library.AbstractXspLibrary;


/**
 * Playground XPages Library
 */
public class PlaygroundLibrary extends AbstractXspLibrary {

	private List<PlaygroundFragment> fragments;
	
	public PlaygroundLibrary() {
	}

	public String getLibraryId() {
        return "com.ibm.xsp.sbtsdk.playground.library"; // $NON-NLS-1$
    }

    public boolean isDefault() {
		return false;
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
            };
        List<PlaygroundFragment> fragments = getPlaygroundFragments();
        for( PlaygroundFragment fragment: fragments) {
        	files = fragment.getXspConfigFiles(files);
        }
        return files;
    }
    
    @Override
	public String[] getFacesConfigFiles() {
        String[] files = new String[] {
            };
        List<PlaygroundFragment> fragments = getPlaygroundFragments();
        for( PlaygroundFragment fragment: fragments) {
        	files = fragment.getFacesConfigFiles(files);
        }
        return files;
    }
    
    private List<PlaygroundFragment> getPlaygroundFragments() {
    	if(fragments==null) {
            List<PlaygroundFragment> frags = ExtensionManager.findServices(null,
            		PlaygroundFragment.class.getClassLoader(),
                    PlaygroundFragment.EXTENSION_NAME, 
                    PlaygroundFragment.class);
            Collections.sort(frags, new Comparator<PlaygroundFragment>() {
                @Override
				public int compare(PlaygroundFragment o1, PlaygroundFragment o2) {
                    String className1 = null == o1? "null":o1.getClass().getName(); //$NON-NLS-1$
                    String className2 = null == o2? "null":o2.getClass().getName(); //$NON-NLS-1$
                    return className1.compareTo(className2);
                }
            });
            fragments = frags;
    	}
		return fragments;
	}
}
