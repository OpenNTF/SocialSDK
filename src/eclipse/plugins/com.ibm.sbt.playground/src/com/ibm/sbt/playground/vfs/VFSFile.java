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
package com.ibm.sbt.playground.vfs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.ibm.commons.util.StringUtil;


//
// Simple virtual file system
//
public abstract class VFSFile {
	
	public static final char SEPARATOR = '/';
	
	private VFS vfs;
	private VFSFile parent;
	private String name;
	private String path;
	
	public VFSFile(VFS vfs, VFSFile parent, String name) {
		this.vfs = vfs;
		this.parent = parent;
		this.name = name;
	}
	public VFS getVFS() {
		return vfs;
	}
	public VFSFile getParent() {
		return parent;
	}
	public String getName() {
		return name;
	}
	public String getPath() {
		if(path==null) {
			if(parent!=null) {
				path = parent.getPath() + SEPARATOR + getName();
			} else {
				path = "";
			}
		}
		return path;
	}
	public String getPathWithoutExtension() {
		String path = getPath();
		int pos = path.lastIndexOf('.');
		if(pos>=0) {
			return path.substring(0,pos);
		}
		return path;
	}
	public String getExtension() {
		int pos = name.lastIndexOf('.');
		if(pos>=0) {
			return name.substring(pos+1);
		}
		return null;
	}
	public String getNameWithoutExtension() {
		int pos = name.lastIndexOf('.');
		if(pos>=0) {
			return name.substring(0,pos);
		}
		return name;
	}
	public abstract boolean isFile() throws IOException;
	public abstract boolean isFolder() throws IOException;
	public abstract boolean exists() throws IOException;
	// File content
	public abstract InputStream getInputStream() throws IOException;
	public abstract OutputStream getOutputStream() throws IOException;
	//public abstract OutputStream getOutputStream(String path) throws IOException;
	public abstract void delete() throws IOException;
	// Children
	public abstract VFSFile getChild(String name) throws IOException;
	public abstract VFSFile[] getChildren() throws IOException;
	

	public VFSFile getFile(String path) throws IOException {
		String[] parts = StringUtil.splitString(path.trim(), VFS.SEPARATOR);
		VFSFile f = this;
		for(int i=0; i<parts.length; i++) {
			String s = parts[i];
			if(StringUtil.isNotEmpty(s)) {
				f = f.getChild(s);
				if(f==null) {
					return null;
				}
			}
		}
		return f;
	}
}
