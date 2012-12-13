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
import com.ibm.sbt.playground.snippets.AbstractImportExport.VFSFile;


/**
 * Definition of a code snippet.
 */
public class SnippetNode extends AbstractNode {

	private String path;
	
	public SnippetNode(CategoryNode parent, String name, String path) {
		super(parent,name);
		this.path = path;
	}

	public SnippetNode(CategoryNode parent, String name, String category, String unid, String jspUrl) {
		super(parent,name,category,unid,jspUrl);
	}

	public String getPath() {
		return path;
	}
	
	public Snippet load(VFSFile root) throws IOException {
		String html = loadResource(root,"html");
		String js = loadResource(root,"js");
		String css = loadResource(root,"css");
		String jsp = loadResource(root,"jsp");
		Snippet s = new Snippet();
		s.setUnid(getUnid());
		s.setHtml(html);
		s.setJs(js);
		s.setCss(css);
		s.setJsp(jsp);

		String props = loadResource(root, "properties");
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
	
	private String loadResource(VFSFile root, String ext) throws IOException {
		String s = path + "." + ext;
		InputStream is = root.getInputStream(s);
		if(is!=null) {
			try {
				return StreamUtil.readString(is);
			} finally {
				StreamUtil.close(is);
			}
		}
		return null;
	}
}
