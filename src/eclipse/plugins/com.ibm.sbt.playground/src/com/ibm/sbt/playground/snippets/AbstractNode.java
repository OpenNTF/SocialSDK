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

import com.ibm.commons.util.StringUtil;


/**
 * Definition of a node.
 */
public abstract class AbstractNode {
	
	public static final char SEPARATOR = '_';
	public static final char SEPARATOR_SLASH = '/';
	
	private CategoryNode parent;
	private String category;
	private String name;
	private String unid;
	private String jspUrl;

	public AbstractNode(CategoryNode parent, String name, String category, String unid, String jspUrl) {
		this.parent = parent;
		this.name = name;
		this.category = category;
		this.unid = unid;
		this.jspUrl = jspUrl;
	}
	public AbstractNode(CategoryNode parent, String name) {
		this.parent = parent;
		this.name = name;
		// Create the category and the unid
		this.category = createName(new StringBuilder(),parent,'/').toString();
		this.unid = encodeUnid(createName(new StringBuilder(),this,SEPARATOR).toString());
		this.jspUrl = SEPARATOR_SLASH+createJspUrl(new StringBuilder(),this,SEPARATOR_SLASH).toString();
	}
	
	private StringBuilder createJspUrl(StringBuilder b, AbstractNode n, char sep) {
		if(n!=null) {
			if(n.getParent()!=null) {
				createJspUrl(b, n.getParent(),sep);
				if(b.length()>0) {
					b.append(sep);
				}
			}
			b.append(n.getName());
		}
		return b;
	}
	
	private StringBuilder createName(StringBuilder b, AbstractNode n, char sep) {
		if(n!=null) {
			if(n.getParent()!=null) {
				createName(b, n.getParent(),sep);
				if(b.length()>0) {
					b.append(sep);
				}
			}
			b.append(n.getName());
		}
		return b;
	}
	
	public CategoryNode getParent() {
		return parent;
	}

	public int getLevel() {
		if(parent!=null) {
			return parent.getLevel()+1;
		}
		return 0;
	}
	
	public String getCategory() {
		return category;
	}

	public String getName() {
		return name;
	}

	public String getUnid() {
		return unid;
	}
	
	public String getJspUrl() {
		return jspUrl;
	}
	
	public boolean isCategory() {
		return this instanceof CategoryNode;
	}
	
	public boolean isSnippet() {
		return this instanceof SnippetNode;
	}
	
	public static String encodeSnippet(String category, String name) {
		if(StringUtil.isNotEmpty(category)) {
			return encodeUnid(category+SEPARATOR+name);
		}
		return encodeUnid(name);
	}
	public static String encodeUnid(String s) {
		StringBuilder b = new StringBuilder();
		for(int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			if(Character.isLetterOrDigit(c)) {
				b.append(c);
			} else if(c=='_'||c=='-'||c=='.'||c=='@'||c=='$'||c=='('||c==')') {
				b.append(c);
			} else {
				b.append('_');
			}
		}
		return b.toString();
	}
}
