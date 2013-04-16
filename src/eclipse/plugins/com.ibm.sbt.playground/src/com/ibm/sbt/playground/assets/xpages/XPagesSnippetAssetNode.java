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
package com.ibm.sbt.playground.assets.xpages;

import java.io.IOException;

import com.ibm.sbt.playground.assets.Asset;
import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.vfs.VFSFile;


/**
 * Definition of an XPages code snippet.
 */
public class XPagesSnippetAssetNode extends AssetNode {

	public XPagesSnippetAssetNode(CategoryNode parent, String name) {
		super(parent,name);
	}

	public XPagesSnippetAssetNode(CategoryNode parent, String name, String category, String unid, String jspUrl) {
		super(parent,name,category,unid,jspUrl);
	}

	@Override
	public XPagesSnippet load(VFSFile root) throws IOException {
		return (XPagesSnippet)super.load(root);
	}

	@Override
	public Asset createAsset(VFSFile root) throws IOException {
		VFSFile parent = getParentFile(root);
		String xsp = loadResource(parent,"xsp");
		String docHtml = loadResource(parent,"doc.html");
		XPagesSnippet s = (XPagesSnippet)new XPagesSnippet();
		s.setXsp(xsp);
		s.setDocHtml(docHtml);
		return s;
	}
}
