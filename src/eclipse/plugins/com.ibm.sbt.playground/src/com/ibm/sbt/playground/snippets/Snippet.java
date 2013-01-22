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
package com.ibm.sbt.playground.snippets;

import java.util.Properties;

import com.ibm.commons.util.StringUtil;



/**
 * Actual content of a snippet.
 */
public class Snippet {

	private String unid;
	private String description;
	private String[] tags;
	private String[] documentation;
	private String theme;
	private String html;
	private String js;
	private String css;
	private String jsp;
	private String[] labels;
	
	public Snippet() {
	}
	
	public void init(Properties props) {
		String desc = props.getProperty("description");
		if(StringUtil.isNotEmpty(desc)) {
			this.description = desc;
		}
		String tags = props.getProperty("tags");
		if(StringUtil.isNotEmpty(tags)) {
			this.tags = StringUtil.splitString(tags,',',true);
		}
		String documentation = props.getProperty("documentation");
		if(StringUtil.isNotEmpty(documentation)) {
			this.documentation = StringUtil.splitString(documentation,',',true);
		}
		String labels = props.getProperty("labels");
		if(StringUtil.isNotEmpty(tags)) {
			this.labels = StringUtil.splitString(labels,',',true);
		}
        String theme = props.getProperty("theme");
        if(StringUtil.isNotEmpty(theme)) {
            this.theme = theme;
        }
	}

    public String getTheme() {
        return theme;
    }
    public void setTheme(String theme) {
        this.theme = theme;
    }

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getTags() {
		return tags;
	}
	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public String[] getDocumentation() {
		return documentation;
	}
	public void setDocumentation(String[] documentation) {
		this.documentation = documentation;
	}

	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	
	public String getJsp() {
		return jsp;
	}
	public void setJsp(String jsp) {
		this.jsp = jsp;
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

	public void setLabels(String[] labels) {
		this.labels = labels;
	}

	public String[] getLabels() {
		return labels;
	}

	public void setUnid(String unid) {
		this.unid = unid;
	}
	
	public String getUnid() {
		return unid;
	}
}
