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
package com.ibm.sbt.jasmine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.playground.snippets.AbstractImportExport;
import com.ibm.sbt.playground.snippets.AbstractImportExport.NodeFactory;
import com.ibm.sbt.playground.snippets.AbstractImportExport.VFSFile;
import com.ibm.sbt.playground.snippets.AbstractNode;
import com.ibm.sbt.playground.snippets.CategoryNode;
import com.ibm.sbt.playground.snippets.Importer;
import com.ibm.sbt.playground.snippets.RootNode;
import com.ibm.sbt.playground.snippets.SnippetNode;



/**
 * Factory class to get the collection of Jasmine spec files available in the current project
 */
public class SpecFactory {

	static final List<AbstractNode> EMPTY_LIST = new ArrayList<AbstractNode>();
	
	public static List<AbstractNode> getSpecNodes(RootNode rootNode) {
		String specName = null;
		Context context = Context.getUnchecked();
		if (context != null) {
			Map<String, Object> requestParams = context.getRequestParameterMap();
			if (requestParams.containsKey("snippet")) {
				return EMPTY_LIST;
			}
			specName = (String)requestParams.get("spec");
		}
		
		List<AbstractNode> children = rootNode.getAllChildrenFlat();
		if (StringUtil.isEmpty(specName) || "*".equals(specName)) {
			return children;
		} else {
			List<AbstractNode> filteredChildren = new ArrayList<AbstractNode>();
			for (int i = 0; i < children.size(); i++) {
				AbstractNode node = children.get(i);
				if (node.isSnippet()) {
					if (specName.equals(node.getName())) {
						filteredChildren.add(node);
					}
				}
			}
			return filteredChildren;
		}
	}
	
	/**
	 * Return a RootNode contains all of the Jasmine spec files in this project
	 * @param context
	 * @return
	 */
	public static RootNode getSpecs(ServletContext servletContext) {
		try {
			return readSpecs(servletContext);
		} catch(IOException ex) {
		}
		return new RootNode();
	}

	public static VFSFile getRootFile(ServletContext context) {
		VFSFile file = AbstractImportExport.createVFSFile(context, "/spec/sbt/");
		return file;
	}
	
	private static RootNode readSpecs(ServletContext context) throws IOException {
		VFSFile file = getRootFile(context);
		NodeFactory factory = new AbstractImportExport.DefaultNodeFactory() {
			@Override
			public SnippetNode createSnippetNode(CategoryNode parent, String name, String path) {
				return new SpecNode(parent, name, path);
			}
			@Override
			public CategoryNode createCategoryNode(CategoryNode parent, String name, String path) {
				return new SpecCategory(parent, name, path);
			}
			
		};
		Importer imp = new Importer(file,factory,Importer.HTMLJS_EXTENSIONS);
		return imp.readSnippets();
	}

}
