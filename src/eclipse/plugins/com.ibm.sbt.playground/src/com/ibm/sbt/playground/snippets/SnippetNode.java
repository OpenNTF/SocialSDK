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
import java.io.InputStream;
import java.io.StringReader;
import java.util.Properties;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.ReaderInputStream;
import com.ibm.commons.util.io.StreamUtil;
import com.ibm.sbt.playground.vfs.VFSFile;


/**
 * Definition of a code snippet.
 */
public class SnippetNode extends AbstractNode {

	public SnippetNode(CategoryNode parent, String name) {
		super(parent,name);
	}

	public SnippetNode(CategoryNode parent, String name, String category, String unid, String jspUrl) {
		super(parent,name,category,unid,jspUrl);
	}
	
	public Snippet load(VFSFile root) throws IOException {
		VFSFile parent = getParentFile(root);
		String html = loadResource(parent,"html");
		String js = loadResource(parent,"js");
		String css = loadResource(parent,"css");
		String jsp = loadResource(parent,"jsp");
		Snippet s = new Snippet();
		s.setUnid(getUnid());
		s.setHtml(html);
		s.setJs(js);
		s.setCss(css);
		s.setJsp(jsp);

		String props = loadResource(parent, "properties");
		if(StringUtil.isNotEmpty(props)) {
			Properties p = new Properties();
			ReaderInputStream is = new ReaderInputStream(new StringReader(props));
			try {
				p.load(is);
			} finally {
				StreamUtil.close(is);
			}
			s.init(p);
		}
		return s;
	}

	private VFSFile getParentFile(VFSFile root) throws IOException {
		if(getParent()!=null) {
			String parentPath = getParent().getPath();
			if(StringUtil.isNotEmpty(parentPath)) {
				return root.getFile(parentPath);
			}
		}
		return root;
	}
	
	private String loadResource(VFSFile parent, String ext) throws IOException {
		String s = getName() + "." + ext;
		VFSFile f = parent.getFile(s);
		if(f!=null) {
			InputStream is = f.getInputStream();
			if(is!=null) {
				try {
					return StreamUtil.readString(is);
				} finally {
					StreamUtil.close(is);
				}
			}
		}
		return null;
	}
}
