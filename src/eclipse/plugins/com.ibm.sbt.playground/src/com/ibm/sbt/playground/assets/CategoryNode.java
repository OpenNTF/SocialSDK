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
import java.util.ArrayList;
import java.util.List;

import com.ibm.commons.util.io.StreamUtil;
import com.ibm.sbt.playground.vfs.VFS;
import com.ibm.sbt.playground.vfs.VFSFile;


/**
 * Definition of a code category.
 */
public class CategoryNode extends Node {
	
	public static final String GLOBAL_PROPERTIES = "_global.properties";

	private List<Node> children = new ArrayList<Node>();
	private String properties; // cached...
	
	public CategoryNode(CategoryNode parent, String name) {
		super(parent,name);
	}

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}
	
	public VFSFile getFile(VFS vfs) throws IOException {
		return vfs.getFile(getPath());
	}

	public String readGlobalProperties(VFS vfs) throws IOException {
		if(properties==null) {
			// Look for a resource
			VFSFile f = getFile(vfs).getFile(GLOBAL_PROPERTIES);
			if(f!=null) {
				InputStream is = f.getInputStream();
				if(is!=null) {
					try {
						properties = StreamUtil.readString(is);
					} finally {
						StreamUtil.close(is);
					}
				}
			}
			// Mark it as read, but empty..
			if(properties==null) {
				properties = "";
			}
		}
		return properties;
	}
}
