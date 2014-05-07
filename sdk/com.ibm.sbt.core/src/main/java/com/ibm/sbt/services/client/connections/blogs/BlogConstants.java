/*
 * © Copyright IBM Corp. 2014
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

package com.ibm.sbt.services.client.connections.blogs;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.NamespaceContextImpl;
/**
 * Blog constants
 * 
 * @author Benjamin Jakobus
 */


public final class BlogConstants{
	
	/**
	 * Blog ATOM
	 */
	public static final String BLOG_HANDLE = "snx:handle";
	public static final String TIMEZONE = "snx:timezone";
	public static final String BLOG_REPLY_TO = "thr:in-reply-to";
	public static final String FLAG = "category";
	
	

	/**
	 * Enum containing the namespaces and prefixes used in ATOM construction (payloads) and parsing (xpath)
	 * 
	 * @author Benjamin jakobus
	 *
	 */
	public enum BlogNamespace {
		BLOG_REF("ref","urn:lsid:ibm.com:blogs:entry-");
		
		private final String prefix;
		private final String url;
		
		BlogNamespace(String prefix, String url){
			this.prefix = prefix;
			this.url = url;
		}

		public String getPrefix(){
			return prefix;
		}
		
		public String getUrl(){
			return url;
		}
	}
	
	public static NamespaceContext	nameSpaceCtx = new NamespaceContextImpl();
	
	static{
		for (BlogNamespace namespace : BlogNamespace.values()){
			((NamespaceContextImpl)nameSpaceCtx).addNamespace(namespace.getPrefix(), namespace.getUrl());
		}
	}
}
