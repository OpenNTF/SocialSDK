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
package com.ibm.sbt.playground.assets;

import com.ibm.commons.util.StringUtil;


/**
 * Definition of a node.
 */
public abstract class Node {
	
	public static final char SEPARATOR = '_';
	public static final char SEPARATOR_SLASH = '/';
	
	private CategoryNode parent;
	private String category;
	private String name;
	private String path;
	private String tooltip;
	private String unid;
	private String jspUrl;

	public Node(CategoryNode parent, String name, String category, String unid, String jspUrl) {
		this.parent = parent;
		this.name = name;
		this.category = category;
		this.unid = unid;
		this.jspUrl = jspUrl;
	}
	public Node(CategoryNode parent, String name) {
		this.parent = parent;
		this.name = name;
		// Create the category and the unid
		this.category = createName(new StringBuilder(),parent,'/').toString();
		this.unid = encodeUnid(createName(new StringBuilder(),this,SEPARATOR).toString());
		this.jspUrl = SEPARATOR_SLASH+createJspUrl(new StringBuilder(),this,SEPARATOR_SLASH).toString();
	}

	public String getPath() {
		if(path==null) {
			if(parent!=null) {
				path = parent.getPath()+'/'+getName();
			} else {
				path = getName();
			}
		}
		return path;
	}
			
	protected StringBuilder createJspUrl(StringBuilder b, Node n, char sep) {
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
	
	protected StringBuilder createName(StringBuilder b, Node n, char sep) {
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

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
	
	public String getJspUrl() {
		return jspUrl;
	}
	
	public boolean isCategory() {
		return this instanceof CategoryNode;
	}
	
	public boolean isAsset() {
		return this instanceof AssetNode;
	}
	
	public static String encodeSnippet(String category, String name) {
		if(StringUtil.isNotEmpty(category)) {
			return encodeUnid(category+SEPARATOR+name);
		}
		return encodeUnid(name);
	}
	public static String encodeUnid(String s) {
		if(StringUtil.isNotEmpty(s)) {
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
		return s;
	}
}
