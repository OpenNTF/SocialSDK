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


/**
 * Base VFS file holding an hierarchy of files in memory.
 * 
 * This is used to access a remote file system, so it is browsed once and the the files are
 * kept in memory.
 * 
 * @author priand
 */
public abstract class RemoteVFS extends VFS {

	protected static class BaseFile extends VFSFile {
		private boolean file;
		private BaseFile[] children;

		protected BaseFile(VFS vfs, VFSFile parent, String name, boolean file) {
			super(vfs,parent,name);
			this.file = file;
		}
		protected void refresh() throws IOException {
			this.children = loadChildren();
		}
		
		@Override
		public boolean isFile() throws IOException {
			return file;
		}
		@Override
		public boolean isFolder() throws IOException {
			return !file;
		}
		@Override
		public boolean exists() throws IOException {
			// Always exists...
			return true;
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
		public VFSFile[] getChildren() throws IOException {
			if(isFolder()) {
				if(children==null) {
					refresh();
				}
			}
			return children;
		}

		// File functions
		@Override
		public InputStream getInputStream() throws IOException {
			throw new IOException("Input stream is not supported");
		}
		@Override
		public OutputStream getOutputStream() throws IOException {
			throw new IOException("Output stream is not supported");
		}
		@Override
		public void delete() throws IOException {
			throw new IOException("delete() is not supported");
		}
		
		// Directory functions
		protected BaseFile[] loadChildren() throws IOException {
			return new BaseFile[0];
		}
	}
	
	public RemoteVFS() {
	}
}
