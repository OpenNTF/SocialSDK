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


public abstract class AbstractImportExport {
	
	
	//
	// Node factory
	//
	public static interface NodeFactory {
		public abstract RootNode createRootNode();
		public abstract CategoryNode createCategoryNode(CategoryNode parent, String name);
		public abstract SnippetNode createSnippetNode(CategoryNode parent, String name);
	}
	public static class DefaultNodeFactory implements NodeFactory {
		@Override
		public RootNode createRootNode() {
			return new RootNode();
		}
		@Override
		public SnippetNode createSnippetNode(CategoryNode parent, String name) {
			return new SnippetNode(parent,name);
		}
		@Override
		public CategoryNode createCategoryNode(CategoryNode parent, String name) {
			return new CategoryNode(parent, name);
		}
	}
}
