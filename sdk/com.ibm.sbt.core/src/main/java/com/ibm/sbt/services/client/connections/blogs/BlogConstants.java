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
	public static final String BLOG_REF = "ref";
	public static final String BLOG_REF_VALUE = "urn:lsid:ibm.com:blogs:entry-";
}
