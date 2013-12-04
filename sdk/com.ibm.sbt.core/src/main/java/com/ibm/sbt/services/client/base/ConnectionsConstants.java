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

package com.ibm.sbt.services.client.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.NamespaceContextImpl;

public class ConnectionsConstants {
	
	public static final char 	SEPARATOR	= '/';
	public static final char 	INIT_URL_PARAM	= '?';
	public static final char 	URL_PARAM	= '&';
	public static final char 	EQUALS		= '=';
	public static final String	OAUTH		= "oauth";
	public static final String	UTF8		= "UTF-8";
	
	public static class Namespaces {
		public static final String ATOM       = "http://www.w3.org/2005/Atom";
		public static final String TD         = "urn:ibm.com/td";
		public static final String CA         = "http://www.ibm.com/xmlns/prod/composite-applications/v1.0";
		public static final String SNX        = "http://www.ibm.com/xmlns/prod/sn";
		public static final String THR        = "http://purl.org/syndication/thread/1.0";
		public static final String OPENSEARCH = "http://a9.com/-/spec/opensearch/1.1/";
		public static final String APP        = "http://www.w3.org/2007/app";
		public static final String RELEVANCE  = "http://a9.com/-/opensearch/extensions/relevance/1.0/";
		public static final String IBMSC      = "http://www.ibm.com/search/content/2010";
		public static final String XS         = "http://www.w3.org/2001/XMLSchema";
		public static final String XHTML      = "http://www.w3.org/1999/xhtml";
	}
	
	public static NamespaceContext	nameSpaceCtx = new NamespaceContextImpl();
	
	static{
		((NamespaceContextImpl)nameSpaceCtx).addNamespace("a", Namespaces.ATOM);
		((NamespaceContextImpl)nameSpaceCtx).addNamespace("td", Namespaces.TD);
		((NamespaceContextImpl)nameSpaceCtx).addNamespace("ca", Namespaces.CA);
		((NamespaceContextImpl)nameSpaceCtx).addNamespace("snx", Namespaces.SNX);
		((NamespaceContextImpl)nameSpaceCtx).addNamespace("thr", Namespaces.THR);
		((NamespaceContextImpl)nameSpaceCtx).addNamespace("opensearch", Namespaces.OPENSEARCH);
		((NamespaceContextImpl)nameSpaceCtx).addNamespace("app", Namespaces.APP);
		((NamespaceContextImpl)nameSpaceCtx).addNamespace("relevance", Namespaces.RELEVANCE);
		((NamespaceContextImpl)nameSpaceCtx).addNamespace("ibmsc", Namespaces.IBMSC);
		((NamespaceContextImpl)nameSpaceCtx).addNamespace("xs", Namespaces.XS);
		((NamespaceContextImpl)nameSpaceCtx).addNamespace("h", Namespaces.XHTML);
		
		
	}
	
}
