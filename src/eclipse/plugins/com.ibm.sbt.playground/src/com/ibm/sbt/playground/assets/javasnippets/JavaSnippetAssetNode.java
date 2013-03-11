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
package com.ibm.sbt.playground.assets.javasnippets;

import java.io.IOException;

import com.ibm.sbt.playground.assets.Asset;
import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.vfs.VFSFile;


/**
 * Definition of a Java code snippet.
 */
public class JavaSnippetAssetNode extends AssetNode {

	public JavaSnippetAssetNode(CategoryNode parent, String name) {
		super(parent,name);
	}

	public JavaSnippetAssetNode(CategoryNode parent, String name, String category, String unid, String jspUrl) {
		super(parent,name,category,unid,jspUrl);
	}

	@Override
	public JavaSnippet load(VFSFile root) throws IOException {
		return (JavaSnippet)super.load(root);
	}

	@Override
	public Asset createAsset(VFSFile root) throws IOException {
		VFSFile parent = getParentFile(root);
		String jsp = loadResource(parent,"jsp");
		JavaSnippet s = (JavaSnippet)new JavaSnippet();
		s.setJsp(jsp);
		s.setJspPath("/samples/java"+parent.getPath()+"/"+getName()+".jsp"); // TODO read the base part of the directory from the VFS
		return s;
	}
}
