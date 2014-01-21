/*
 *  Copyright IBM Corp. 2012
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
package com.ibm.sbt.playground.assets;

import com.ibm.sbt.playground.vfs.VFSFile;


public abstract class AbstractNodeFactory implements NodeFactory {
	
	public AbstractNodeFactory() {
	}
	
	@Override
	public String getSnippetName(VFSFile s) {
		String ext = getExtension(s.getName(), getAssetExtensions());
		if(ext!=null) {
			String fileName = getNameWithoutExtension(s.getName(), ext);
			return fileName;
		}
		return null;
    }
	
    protected String getExtension(String name, String[] exts) {
        if (exts != null) {
            for (int i=0; i<exts.length; i++) {
                if (name.endsWith(exts[i])) {
                    return exts[i];
                }
            }
        } else {
            int pos = name.lastIndexOf('.');
            if(pos>=0) {
                return name.substring(pos+1);
            }
        }
        return null;
    }
    protected String getNameWithoutExtension(String name, String ext) {
        return name.substring(0, name.length() - (ext.length() + 1));
    }
}
