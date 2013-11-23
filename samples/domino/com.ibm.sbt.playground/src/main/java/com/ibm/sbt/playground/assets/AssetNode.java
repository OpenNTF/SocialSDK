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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Properties;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.ReaderInputStream;
import com.ibm.commons.util.io.StreamUtil;
import com.ibm.sbt.playground.vfs.VFS;
import com.ibm.sbt.playground.vfs.VFSFile;


/**
 * Definition of a code snippet.
 */
public abstract class AssetNode extends Node {
	
	public static final String GLOBAL_PROPERTIES = "_global.properties";

	public AssetNode(CategoryNode parent, String name) {
		super(parent,name);
	}

	public AssetNode(CategoryNode parent, String name, String category, String unid, String jspUrl) {
		super(parent,name,category,unid,jspUrl);
	}
	
	public VFSFile getFile(VFS vfs, String ext) throws IOException {
		return vfs.getFile(getPath()+"."+ext);
	}

	/**
	 * Load the assert from the VFSFile
	 * @param root
	 * @return
	 * @throws IOException
	 */
	public Asset load(VFSFile root) throws IOException {
		Asset s = createAsset(root);
		if(s!=null) {
			s.setUnid(getUnid());
	
			// Read the properties, starting from the most global ones
			Properties p = new Properties();
			readProperties(root.getVFS(), this, p);
			s.init(p);
		}
		return s;
	}
	protected void readProperties(VFS vfs, Node node, Properties p) throws IOException {
		// Read the parent global first
		CategoryNode parent =node.getParent();
		if(parent!=null) {
			readProperties(vfs, parent, p);
		}
		// Then the current file
		if(node.isCategory()) {
			String props = ((CategoryNode)node).readGlobalProperties(vfs);
			addProperties(p, props);
		} else if(node.isAsset()) {
			String props = loadResource(parent.getFile(vfs), "properties");
			addProperties(p, props);
		}
	}
	protected void addProperties(Properties p, String value) throws IOException {
		if(StringUtil.isNotEmpty(value)) {
			ReaderInputStream is = new ReaderInputStream(new StringReader(value));
			try {
				p.load(is);
			} finally {
				StreamUtil.close(is);
			}
		}
	}
	
	public abstract Asset createAsset(VFSFile root) throws IOException;
	
	protected VFSFile getParentFile(VFSFile root) throws IOException {
		if(getParent()!=null) {
			String parentPath = getParent().getPath();
			if(StringUtil.isNotEmpty(parentPath)) {
				return root.getFile(parentPath);
			}
		}
		return root;
	}
	
	protected String loadResource(VFSFile parent, String ext) throws IOException {
		String s = getName() + "." + ext;
		return loadFile(parent, s);
	}

	protected String loadFile(VFSFile parent, String fileName) throws IOException {
		VFSFile f = parent.getFile(fileName);
		return loadFile(f);
	}

	protected String loadFile(VFSFile file) throws IOException {
		if(file!=null) {
			InputStream is = file.getInputStream();
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
