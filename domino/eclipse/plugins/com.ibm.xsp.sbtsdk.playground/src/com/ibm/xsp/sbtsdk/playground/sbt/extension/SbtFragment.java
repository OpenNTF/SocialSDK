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

package com.ibm.xsp.sbtsdk.playground.sbt.extension;

import nsf.playground.extension.PlaygroundFragment;


/**
 * 
 */
public class SbtFragment extends PlaygroundFragment {
    
	public static final SbtFragment instance = new SbtFragment();
	
    public SbtFragment() {
    }

    @Override
    public String[] getXspConfigFiles(String[] files) {
        return concat(files, new String[] {
        });
    }
    
    @Override
    public String[] getFacesConfigFiles(String[] files) {
        return concat(files, new String[] {
       		"/com/ibm/xsp/sbtsdk/playground/sbt/config/playground-faces-config.xml"
        });
    }
}
