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
package com.ibm.sbt.playground.assets.javasnippets;

import java.util.Properties;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.playground.assets.Asset;



/**
 * Description of a Java snippet.
 */
public class JavaSnippet extends Asset {

	private String theme;
	private String jsp;
	private String jspPath;
	
	public JavaSnippet() {
	}
	
	@Override
	public void init(Properties props) {
		super.init(props);
	}

    public String getTheme() {
        return theme;
    }
    public void setTheme(String theme) {
        this.theme = theme;
    }
	
	public String getJsp() {
		return jsp;
	}
	public void setJsp(String jsp) {
		this.jsp = jsp;
	}
	
	public String getJspPath() {
		return jspPath;
	}
	public void setJspPath(String jspPath) {
		this.jspPath = jspPath;
	}
	
	public String getJspForDisplay() {
		String s = getJsp();
		if(s!=null) {
			int start = StringUtil.indexOfIgnoreCase(s, "<body>");
			int end = StringUtil.indexOfIgnoreCase(s, "</body>");
			if(start>=0 && end>=start+6) {
				return s.substring(start+6,end).trim();
			}
		}
		return s;
	}
}
