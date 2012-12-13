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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import javax.servlet.ServletContext;

import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;

public abstract class AbstractImportExport {
	
	//
	// Simple virtual file system
	//
	public static abstract class VFSFile {
		public static final char SEPARATOR = '/';
		private VFSFile parent;
		private String name;
		private String path;
		public VFSFile(VFSFile parent, String name) {
			this.parent = parent;
			this.name = name;
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
		// File content
		public abstract InputStream getInputStream() throws IOException;
		public abstract InputStream getInputStream(String path) throws IOException;
		public abstract OutputStream getOutputStream() throws IOException;
		//public abstract OutputStream getOutputStream(String path) throws IOException;
		public abstract void delete() throws IOException;
		// Children
		public abstract VFSFile getChild(String name) throws IOException;
		public abstract VFSFile[] getChildren() throws IOException;
	}

	protected static class FileVFSFile extends VFSFile {
		private File file;
		public FileVFSFile(VFSFile parent, String name, File file) {
			super(parent,name);
			this.file = file;
		}
		@Override
		public boolean isFile() throws IOException {
			return file.isFile();
		}
		@Override
		public boolean isFolder() throws IOException {
			return file.isDirectory();
		}
		// File content
		@Override
		public InputStream getInputStream() throws IOException {
			return new FileInputStream(file);
		}
		@Override
		public InputStream getInputStream(String path) throws IOException {
			try {
				File f = new File(file,StringUtil.replace(path, SEPARATOR, File.separatorChar));
				return new FileInputStream(f);
			} catch(FileNotFoundException ex) {
				// Return an empty stream...
				return null;
			}
		}
		@Override
		public OutputStream getOutputStream() throws IOException {
			return new FileOutputStream(file);
		}
		@Override
		public void delete() throws IOException {
			file.delete();
		}
		// Children
		@Override
		public VFSFile getChild(String name) throws IOException {
			File c = new File(file,name);
			return new FileVFSFile(this, c.getName(), c);
		}
		@Override
		public VFSFile[] getChildren() throws IOException {
			File[] children = file.listFiles();
			VFSFile[] files = new VFSFile[children.length];
			for(int i=0; i<children.length; i++) {
				File c = children[i];
				files[i] = new FileVFSFile(this, c.getName(), c);
			}
			return files;
		}
	}

	protected static class ServletVFSFile extends VFSFile {
		private ServletContext context;
		private String dir;
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
		// File content
		@Override
		public InputStream getInputStream() throws IOException {
			return context.getResourceAsStream(dir);
		}
		@Override
		public InputStream getInputStream(String path) throws IOException {
			String s = PathUtil.concat(dir, path, SEPARATOR);
			return context.getResourceAsStream(s);
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
			// Should find out if it is file or a dir..
			throw new IOException("Operation not available in a servlet context");
		}
		@Override
		public VFSFile[] getChildren() throws IOException {
			@SuppressWarnings("unchecked")
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
	}

	public static VFSFile createVFSFile(File file) {
		return new FileVFSFile(null, "", file);
	}

	public static VFSFile createVFSFile(ServletContext context, String baseDir) {
		return new ServletVFSFile(null, "", context, baseDir);
	}

	
	//
	// Node factory
	//
	public static interface NodeFactory {
		public abstract RootNode createRootNode();
		public abstract CategoryNode createCategoryNode(CategoryNode parent, String name, String path);
		public abstract SnippetNode createSnippetNode(CategoryNode parent, String name, String path);
	}
	public static class DefaultNodeFactory implements NodeFactory {
		@Override
		public RootNode createRootNode() {
			return new RootNode();
		}
		@Override
		public SnippetNode createSnippetNode(CategoryNode parent, String name, String path) {
			return new SnippetNode(parent,name,path);
		}
		@Override
		public CategoryNode createCategoryNode(CategoryNode parent, String name, String path) {
			return new CategoryNode(parent, name);
		}
	}
}
