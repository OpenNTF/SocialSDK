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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ibm.commons.runtime.util.URLEncoding;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.base64.Base64;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.RestClient;


/**
 * Module to import files from GIT.
 * 
 * Uses: GET /repos/:owner/:repo/contents/:path
 * 	Ex: https://api.github.com/repos/OpenNTF/SocialSDK/contents/samples/j2ee/com.ibm.sbt.sample.web/WebContent/js/Utilities
 * 
 * @author priand
 */
public class GitVFS extends RemoteVFS {

	public static final String GITHUB_BASEURL	= "https://api.github.com";

    private static class StringInputStream extends java.io.InputStream {
    	private String str;
    	private int ptr;
    	StringInputStream(String str) {
    		this.str = str;
    	}
        @Override
		public int read() throws IOException {
        	if(ptr<str.length()) {
        		return str.charAt(ptr++);
        	}
        	return -1;
        }
    }
	
	public class GitFile extends RemoteVFS.BaseFile {
		
		private String path;
		
		public GitFile(GitVFS vfs, GitFile parent, String name, String path, boolean file) {
			super(vfs,parent,name,file);
			this.path = path;
		}
		
		@Override
		public GitVFS getVFS() {
			return (GitVFS)super.getVFS();
		}
		
		public String getUrl() {
			return path;
		}
	
		// File functions
		@Override
		public InputStream getInputStream() throws IOException {
			RestClient c = createGitClient(getUserName(),getPassword());
			String path = URLEncoding.encodeURIString(getPath(),"utf-8",0,false);
			try {
				Map<String,Object> json = (Map<String,Object>)c.get(path,new RestClient.HandlerJson(JsonJavaFactory.instance));
				String content = (String)json.get("content");
				if(content!=null) {
					String encoding = (String)json.get("encoding");
					if(StringUtil.equals(encoding, "base64")) {
						return new Base64.InputStream(new StringInputStream(content));
					}
					throw new IOException(StringUtil.format("Unknown content encoding {0}", encoding));
				}
				return null;
			} catch (ClientServicesException e) {
				throw new IOException(e);
			}
		}

		@Override
		public OutputStream getOutputStream() throws IOException {
			throw new IOException("Output stream is not supported");
		}
		@Override
		public void delete() throws IOException {
			throw new IOException("delete() is not supported");
		}
		
		/*
		 [
		  {
		    "_links": {
		      "html": "https://github.com/OpenNTF/SocialSDK/blob/master/samples/j2ee/com.ibm.sbt.sample.web/WebContent/js/Utilities/Email.html",
		      "self": "https://api.github.com/repos/OpenNTF/SocialSDK/contents/samples/j2ee/com.ibm.sbt.sample.web/WebContent/js/Utilities/Email.html",
		      "git": "https://api.github.com/repos/OpenNTF/SocialSDK/git/blobs/ab4df12cc4fb3a5bbd097f226aadad16a36710fb"
		    },
		    "type": "file",
		    "path": "samples/j2ee/com.ibm.sbt.sample.web/WebContent/js/Utilities/Email.html",
		    "sha": "ab4df12cc4fb3a5bbd097f226aadad16a36710fb",
		    "git_url": "https://api.github.com/repos/OpenNTF/SocialSDK/git/blobs/ab4df12cc4fb3a5bbd097f226aadad16a36710fb",
		    "html_url": "https://github.com/OpenNTF/SocialSDK/blob/master/samples/j2ee/com.ibm.sbt.sample.web/WebContent/js/Utilities/Email.html",
		    "url": "https://api.github.com/repos/OpenNTF/SocialSDK/contents/samples/j2ee/com.ibm.sbt.sample.web/WebContent/js/Utilities/Email.html",
		    "size": 2425,
		    "name": "Email.html"
		  },	
		 */
		
		// Directory functions
		@Override
		protected BaseFile[] loadChildren() throws IOException {
			try {
				ArrayList<BaseFile> files = new ArrayList<BaseFile>();
				RestClient c = createGitClient(getUserName(),getPassword());
				String path = URLEncoding.encodeURIString(getPath(),"utf-8",0,false);
				Object json = c.get(path,new RestClient.HandlerJson(JsonJavaFactory.instance));
				//System.out.println("Emit request for: "+path);				
				if(json instanceof List) {
					for(Object entry: (List)json) {
						if(entry instanceof Map) {
							Map m = (Map)entry;
							String eType = (String)m.get("type");
							String eName = (String)m.get("name");
							//String ePath = (String)m.get("path");
							String eUrl = (String)m.get("url");
							GitFile f = new GitFile(getVFS(),this, eName, eUrl, StringUtil.equals(eType,"file"));
							files.add(f);
							//System.out.println(StringUtil.format("Type:{0}, Name:{1}, Path:{2}, Url:{3}",eType,eName,ePath,eUrl));
						}
					}
				}
				return files.toArray(new BaseFile[files.size()]);
			} catch(ClientServicesException ex) {
				throw new IOException(ex);
			}
		}
	}

	private String baseUrl;

	private String userName;
	private String password;
	
	private GitFile root;
	
	public GitVFS(String baseUrl, String userName, String password) {
		this.baseUrl = baseUrl;
		this.root = new GitFile(this,null, "", baseUrl, false);
		this.userName = userName;
		this.password = password;
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}

	@Override
	public VFSFile getRoot() {
		return root;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	protected GitClient createGitClient(String userName, String password) {
		String url = baseUrl;
//		url = PathUtil.concat(url, "repos", '/');
//		if(StringUtil.isNotEmpty(getOwner())) {
//			url = PathUtil.concat(url, getOwner(), '/');
//		}
//		if(StringUtil.isNotEmpty(getRepos())) {
//			url = PathUtil.concat(url, getRepos(), '/');
//		}
//		url = PathUtil.concat(url, "contents", '/');
//		if(StringUtil.isNotEmpty(getBasePath())) {
//			url = PathUtil.concat(url, getBasePath(), '/');
//		}
		return new GitClient(url,userName,password);
	}
	
	public static class GitClient extends RestClient {
		public GitClient(String baseUrl, String userName, String password) {
			super(baseUrl);
			setAuthenticator(new BasicAuthenticator(userName,password));
		}
		// Allow HTTPS, regardless of the certificates
		@Override
		protected boolean isForceTrustSSLCertificate() throws ClientServicesException {
			return true;
		}
		
	}
}
