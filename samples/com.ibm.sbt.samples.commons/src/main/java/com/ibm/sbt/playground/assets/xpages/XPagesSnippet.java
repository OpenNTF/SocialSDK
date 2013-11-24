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
package com.ibm.sbt.playground.assets.xpages;

import com.ibm.sbt.playground.assets.Asset;


/**
 * Description of an XPages snippet.
 */
public class XPagesSnippet extends Asset {

	private String xsp;
    private String docHtml;
	
	public XPagesSnippet() {
	}

    public String getTheme() {
		return getProperty("theme");
    }
    public void setTheme(String theme) {
		setProperty("theme",theme);
    }
	
	public String getXsp() {
		return xsp;
	}
	public void setXsp(String xsp) {
		this.xsp = xsp;
	}

    public String getDocHtml() {
        return docHtml;
    }
    public void setDocHtml(String docHtml) {
        this.docHtml = docHtml;
    }
}
