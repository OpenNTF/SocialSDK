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
package com.ibm.sbt.playground.snippets.dojo;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.playground.snippets.AbstractNode;
import com.ibm.sbt.playground.snippets.CategoryNode;
import com.ibm.sbt.playground.snippets.RootNode;
import com.ibm.sbt.playground.snippets.SnippetNode;



/**
 * Helper class that generates a JSON data set out of the snippet list, to populate a Dojo tree.
 */
public class JsonTreeRenderer {

	public JsonTreeRenderer() {
	}

	//
	// Flat generation - Dojo 1.8
	//
	public String generateAsStringFlat(CategoryNode node) throws IOException {
		return generateAsStringFlat(node,true);
	}

	public String generateAsStringFlat(CategoryNode node, boolean compact) throws IOException {
		StringWriter sw = new StringWriter();
		JsonWriter jw = new JsonWriter(sw, compact);
		generateFlat(jw, node);
		jw.flush();
		return sw.toString();
	}

	public void generateFlat(JsonWriter jw, CategoryNode node) throws IOException {
		jw.startArray();
			generateNodeFlat(jw, node);
		jw.endArray();
	}

	protected void generateNodeFlat(JsonWriter jw, AbstractNode node) throws IOException {
		jw.startArrayItem();
			generateNodeEntryFlat(jw, node);
		jw.endArrayItem();
		if(node instanceof CategoryNode) {
			CategoryNode cn = (CategoryNode)node;
			List<AbstractNode> children = cn.getChildren();
			for(AbstractNode c: children) {
				generateNodeFlat(jw, c);
			}
		}
	}
	
	protected void generateNodeEntryFlat(JsonWriter jw, AbstractNode node) throws IOException {
		jw.startObject();
			jw.startProperty("id");
				String id = (node instanceof RootNode) ? "_root" : node.getUnid();
				jw.outStringLiteral(id);
			jw.endProperty();
			jw.startProperty("name");
				String name = (node instanceof RootNode) ? "_root" : node.getName();
				jw.outStringLiteral(name);
			jw.endProperty();
			if(node instanceof SnippetNode) {
				jw.startProperty("url");
					jw.outStringLiteral(((SnippetNode)node).getName());
				jw.endProperty();
			}
			AbstractNode parent = node.getParent();
			if(parent!=null) {
				jw.startProperty("parent");
					jw.outStringLiteral(parent.getUnid());
				jw.endProperty();
			}
		jw.endObject();
	}
	
	
	//
	// Hierarchical generation - Dojo 1.6/1.7
	//
	public String generateAsStringHier(CategoryNode node) throws IOException {
		return generateAsStringHier(node,true);
	}

	public String generateAsStringHier(CategoryNode node, boolean compact) throws IOException {
		StringWriter sw = new StringWriter();
		JsonWriter jw = new JsonWriter(sw, compact);
		generateHier(jw, node);
		jw.flush();
		return sw.toString();
	}

	public void generateHier(JsonWriter jw, CategoryNode node) throws IOException {
		jw.startArray();
			generateNodeHier(jw, node);
		jw.endArray();
	}

	protected void generateNodeHier(JsonWriter jw, AbstractNode node) throws IOException {
		jw.startArrayItem();
			generateNodeEntryHier(jw, node);
		jw.endArrayItem();
	}
	
	protected void generateNodeEntryHier(JsonWriter jw, AbstractNode node) throws IOException {
		jw.startObject();
			jw.startProperty("id");
				String id = (node instanceof RootNode) ? "_root" : node.getUnid();
				jw.outStringLiteral(id);
			jw.endProperty();
			jw.startProperty("name");
				String name = (node instanceof RootNode) ? "_root" : node.getName();
				jw.outStringLiteral(name);
			jw.endProperty();
			if(node instanceof SnippetNode) {
				jw.startProperty("url");
					jw.outStringLiteral(((SnippetNode)node).getUnid());
				jw.endProperty();
			}
			if(StringUtil.isNotEmpty(node.getJspUrl())) {
				if(node instanceof SnippetNode) {
					jw.startProperty("jspUrl");
						jw.outStringLiteral((node).getJspUrl());
					jw.endProperty();
				}
			}
			if(node instanceof CategoryNode) {
				CategoryNode cn = (CategoryNode)node;
				List<AbstractNode> children = cn.getChildren();
				if(!children.isEmpty()) {
					jw.startProperty("children");
						jw.startArray();
							for(AbstractNode c: children) {
								generateNodeHier(jw, c);
							}
						jw.endArray();
					jw.endProperty();
				}
			}
		jw.endObject();
	}	
}
