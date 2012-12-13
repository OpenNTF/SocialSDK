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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.playground.snippets.AbstractImportExport.VFSFile;
import com.ibm.sbt.playground.snippets.dojo.JsonTreeRenderer;


/**
 * Definition of a code category.
 */
public class RootNode extends CategoryNode {

	public RootNode() {
		super(null,"");
	}
	
	public Snippet loadSnippet(VFSFile root, String unid) throws IOException {
		SnippetNode n = findSnippet(this, unid);
		if(n!=null) {
			return n.load(root);
		}
		return null;
	}
	
	public Snippet loadSnippetForJava(VFSFile root, String javaSnippetPath) throws IOException {
		SnippetNode n = findSnippetForJava(this, javaSnippetPath);
		if(n!=null) {
			return n.load(root);
		}
		return null;
	}
	
	private SnippetNode findSnippet(AbstractNode parent, String unid) {
		if(parent instanceof SnippetNode) {
			SnippetNode sn = (SnippetNode)parent;
			if(StringUtil.equals(sn.getUnid(), unid)) {
				return sn;
			}
		} else if(parent instanceof CategoryNode) {
			CategoryNode cn = (CategoryNode)parent;
			List<AbstractNode> children = cn.getChildren();
			for(int i=0; i<children.size(); i++) {
				SnippetNode s = findSnippet(children.get(i), unid);
				if(s!=null) {
					return s;
				}
			}
		}
		return null;
	}
	
	private SnippetNode findSnippetForJava(AbstractNode parent, String javaSnippetPath) {
		if(parent instanceof SnippetNode) {
			SnippetNode sn = (SnippetNode)parent;
			if(StringUtil.equals(sn.getJspUrl()+".jsp", javaSnippetPath)) {
				return sn;
			}
		} else if(parent instanceof CategoryNode) {
			CategoryNode cn = (CategoryNode)parent;
			List<AbstractNode> children = cn.getChildren();
			for(int i=0; i<children.size(); i++) {
				SnippetNode s = findSnippetForJava(children.get(i), javaSnippetPath);
				if(s!=null) {
					return s;
				}
			}
		}
		return null;
	}

	public List<AbstractNode> getAllChildrenFlat() {
		ArrayList<AbstractNode> nodes = new ArrayList<AbstractNode>();
		List<AbstractNode> children = getChildren();
		for(int i=0; i<children.size(); i++) {
			addNode(nodes,children.get(i));
		}
		return nodes;
	}
	private void addNode(List<AbstractNode> nodes, AbstractNode node) {
		nodes.add(node);
		if(node instanceof CategoryNode) {
			CategoryNode cn = (CategoryNode)node;
			List<AbstractNode> children = cn.getChildren();
			for(int i=0; i<children.size(); i++) {
				addNode(nodes,children.get(i));
			}
		}
	}
	
	public String getAsJson() {
		try {
			JsonTreeRenderer r = new JsonTreeRenderer();
			return r.generateAsStringHier(this, true);
		} catch (IOException e) {
			return "{}";
		}
	}
}
