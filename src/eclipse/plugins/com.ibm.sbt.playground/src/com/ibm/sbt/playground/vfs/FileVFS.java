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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * VFS to access files from the file system.
 * 
 * @author priand
 */
public class FileVFS extends VFS {

	protected static class FileVFSFile extends VFSFile {
		private File file;
		public FileVFSFile(VFS vfs, VFSFile parent, String name, File file) {
			super(vfs,parent,name);
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
		@Override
		public boolean exists() throws IOException {
			return file.exists();
		}
		// File content
		@Override
		public InputStream getInputStream() throws IOException {
			return new FileInputStream(file);
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
			if(c.exists()) {
				return new FileVFSFile(getVFS(),this, c.getName(), c);
			}
			return null;
		}
		@Override
		public VFSFile[] getChildren() throws IOException {
			File[] children = file.listFiles();
			VFSFile[] files = new VFSFile[children.length];
			for(int i=0; i<children.length; i++) {
				File c = children[i];
				files[i] = new FileVFSFile(getVFS(),this, c.getName(), c);
			}
			return files;
		}
	}

	private FileVFSFile root;
	
	public FileVFS(File file) {
		this.root = new FileVFSFile(this,null, "", file);
	}

	@Override
	public VFSFile getRoot() {
		return root;
	}
}
