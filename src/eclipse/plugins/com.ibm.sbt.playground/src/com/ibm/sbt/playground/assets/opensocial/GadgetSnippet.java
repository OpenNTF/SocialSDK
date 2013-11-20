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
package com.ibm.sbt.playground.assets.opensocial;

import com.ibm.sbt.playground.assets.Asset;



/**
 * Description of an OpenSocial Gadget snippet.
 */
public class GadgetSnippet extends Asset {

	private String title;

	private String gadgetXml;
	private String html;
    private String docHtml;
	private String js;
	private String css;
	private String json;
		
	public GadgetSnippet() {
	}

    public String getTheme() {
		return getProperty("theme");
    }
    public void setTheme(String theme) {
		setProperty("theme",theme);
    }

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getGadgetXml() {
		return gadgetXml;
	}
	public void setGadgetXml(String gadgetXml) {
		this.gadgetXml = gadgetXml;
	}

	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}

    public String getDocHtml() {
        return docHtml;
    }
    public void setDocHtml(String docHtml) {
        this.docHtml = docHtml;
    }

	public String getJs() {
		return js;
	}
	public void setJs(String js) {
		this.js = js;
	}

	public String getCss() {
		return css;
	}
	public void setCss(String css) {
		this.css = css;
	}

	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
}
