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
package com.ibm.sbt.playground.git;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.ibm.commons.util.FastStringBuffer;
import com.ibm.commons.util.io.StreamUtil;
import com.ibm.sbt.playground.vfs.GitVFS;
import com.ibm.sbt.playground.vfs.VFSFile;


/**
 * Module to import files from GIT.
 * 
 * Uses: GET /repos/:owner/:repo/contents/:path
 * 	Ex: https://api.github.com/repos/OpenNTF/SocialSDK/contents/samples/j2ee/com.ibm.sbt.sample.web/WebContent/js/Utilities
 * 
 * @author priand
 */
public class GitImport {

	public static final String GITHUB_BASEURL	= "https://api.github.com";
	
	private String baseUrl;
	private String owner;
	private String repos;
	
	public GitImport(String baseUrl, String owner, String repos) {
		this.baseUrl = baseUrl;
		this.owner = owner;
		this.repos = repos;
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}

	public String getOwner() {
		return owner;
	}

	public String getRepos() {
		return repos;
	}

//	var gh = new com.ibm.sbt.playground.git.GitImport("https://api.github.com",null,"OpenNTF/SocialSDK")	
	public void browse(String path) throws Exception {
		GitVFS vfs = new GitVFS("https://api.github.com/repos/OpenNTF/SocialSDK/contents/samples/j2ee/com.ibm.sbt.sample.web/WebContent/js/","priand","gith7934");
		
		VFSFile f = vfs.getRoot().getChild("Smartcloud").getChild("Profiles").getChild("Get About.js");
		System.out.println("File:"+f.getName());
		InputStream is = f.getInputStream();
		try {
			FastStringBuffer fb = new FastStringBuffer();
			fb.append(new InputStreamReader(is));
			String s = fb.toString();
			System.out.println(s);
		} finally {
			StreamUtil.close(is);
		}
//		VFSFile f = vfs.getRoot();
//		browse(f,0);
	}
	public void browse(VFSFile file, int level) throws Exception {
		if(level>2) {
			return;
		}
		VFSFile[] files = file.getChildren();
		for(int i=0; i<files.length; i++) {
			VFSFile f = files[i];
			StringBuilder b = new StringBuilder();
			for(int ll=0; ll<level; ll++) {
				b.append("  ");
			}
			if(f.isFolder()) {
				b.append("[+]");
			}
			b.append(f.getName()+", "+f.getPath());
			System.out.println(b.toString());
			if(f.isFolder()) {
				browse(f, level+1);
			}
		}
	}
}
