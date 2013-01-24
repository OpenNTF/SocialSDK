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
import java.util.Set;

import javax.servlet.ServletContext;

import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;


/**
 * VFS to access files from the file system.
 * 
 * @author priand
 */
public class ServletVFS extends VFS {

	protected static class ServletVFSFile extends VFSFile {
		private ServletContext context;
		private String dir;
		private VFSFile[] children;
		public ServletVFSFile(VFSFile parent, String name, ServletContext context, String dir) {
			super(parent,name);
			this.context = context;
			this.dir = dir;
		}
		@Override
		public boolean isFile() throws IOException {
			return !dir.endsWith("/");
		}
		@Override
		public boolean isFolder() throws IOException {
			return dir.endsWith("/");
		}
		@Override
		public boolean exists() throws IOException {
			// Always exists...
			return true;
		}
		// File content
		@Override
		public InputStream getInputStream() throws IOException {
			return context.getResourceAsStream(dir);
		}
		@Override
		public OutputStream getOutputStream() throws IOException {
			throw new IOException("Cannot create an OutputStream in a servlet context");
		}
		@Override
		public void delete() throws IOException {
			throw new IOException("Cannot create a file in a servlet context");
		}
		// Children
		@Override
		public VFSFile getChild(String name) throws IOException {
			VFSFile[] children = getChildren();
			if(children!=null) {
				for(int i=0; i<children.length; i++) {
					if(StringUtil.equalsIgnoreCase(children[i].getName(), name)) {
						return children[i];
					}
				}
			}
			return null;
		}
		@Override
		@SuppressWarnings("unchecked")
		public VFSFile[] getChildren() throws IOException {
			if(children==null && isFolder()) {
				Set<String> children = context.getResourcePaths(dir);
				VFSFile[] files = new VFSFile[children.size()];
				int i=0;
				for(String s: children) {
					String name = null;
					if(s.endsWith("/")) {
						name = s.substring(dir.length(),s.length()-1);
					} else {
						name = s.substring(dir.length());
					}
					files[i++]= new ServletVFSFile(this, name, context, s);
				}
				return files;
			}
			return children;
		}
	}

	public static VFSFile createVFSFile(ServletContext context, String baseDir) {
		return new ServletVFSFile(null, "", context, baseDir);
	}

	private ServletVFSFile root;
	
	public ServletVFS(ServletContext context, String baseDir) {
		this.root = new ServletVFSFile(null, "", context, baseDir);
	}

	@Override
	public VFSFile getRoot() {
		return root;
	}
}
