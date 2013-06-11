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


/**
 * Playground library fragment.
 * 
 * @author priand
 */
public abstract class PlaygroundFragment {
	
    public static final String EXTENSION_NAME = "com.ibm.xsp.sbtsdk.playground.fragment"; // $NON-NLS-1$
	
    
	public String[] getXspConfigFiles(String[] files) {
		return files;
	}
	
	public String[] getFacesConfigFiles(String[] files) {
		return files;
	}
	
    public static String[] concat(String[] s1, String[] s2) {
    	String[] s = new String[s1.length+s2.length];
    	System.arraycopy(s1, 0, s, 0, s1.length);
    	System.arraycopy(s2, 0, s, s1.length, s2.length);
    	return s;
    }
	
}
