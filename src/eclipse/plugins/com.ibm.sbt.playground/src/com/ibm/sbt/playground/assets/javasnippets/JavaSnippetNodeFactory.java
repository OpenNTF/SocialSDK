/*
 * � Copyright IBM Corp. 2012
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

import com.ibm.sbt.playground.assets.AbstractNodeFactory;
import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.assets.RootNode;


public class JavaSnippetNodeFactory extends AbstractNodeFactory {
	
	public static final JavaSnippetNodeFactory instance = new JavaSnippetNodeFactory();
	
	public static final String[] EXTENSIONS = new String[]{"jsp"};
	
	public JavaSnippetNodeFactory() {
	}
	
	@Override
	public String[] getAssetExtensions() {
		return EXTENSIONS;
	}
	
	@Override
	public RootNode createRootNode() {
		return new RootNode();
	}
	
	@Override
	public CategoryNode createCategoryNode(CategoryNode parent, String name) {
		return new CategoryNode(parent, name);
	}
	
	@Override
	public AssetNode createAssetNode(CategoryNode parent, String name) {
		return new JavaSnippetAssetNode(parent, name);
	}
}
