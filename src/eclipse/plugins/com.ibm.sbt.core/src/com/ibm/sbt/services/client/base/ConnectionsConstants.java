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

import java.util.Iterator;
import com.ibm.commons.xml.NamespaceContext;

public class ConnectionsConstants {
	
	public static final char 	SEPARATOR	= '/';
	public static final char 	INIT_URL_PARAM	= '?';
	public static final char 	URL_PARAM	= '&';
	public static final char 	EQUALS		= '=';
	public static final String	OAUTH		= "oauth";
	public static final String	UTF8		= "UTF-8";
	
	//TODO: Perhaps we could use a proper implementation, or extend NamespaceContextImpl?
	public static NamespaceContext	nameSpaceCtx = new NamespaceContext() {

		@Override
		public String getNamespaceURI(String prefix) {
			String uri;
			if (prefix.equals("h")) {
				uri = "http://www.w3.org/1999/xhtml";
			} else if (prefix.equals("a")) {
				uri = "http://www.w3.org/2005/Atom";
            } else if (prefix.equals("snx")) {
                uri = "http://www.ibm.com/xmlns/prod/sn";
            } else if (prefix.equals("td")) {
                uri = "urn:ibm.com/td";
            } else if (prefix.equals("opensearch")) {
                uri = "http://a9.com/-/spec/opensearch/1.1/";
            } else if (prefix.equals("thr")){
            	uri = "http://purl.org/syndication/thread/1.0";
            } else if (prefix.equals("app")){
            	uri = "http://www.w3.org/2007/app";
            } else if(prefix.equals("relevance")){
            	uri = "http://a9.com/-/opensearch/extensions/relevance/1.0/";
            } else if (prefix.equals("ibmsc")){
            	uri = "http://www.ibm.com/search/content/2010";
            } else {
				uri = null;
			}
			return uri;
		}

		// Dummy implementation - not used!
		@Override
		public Iterator<String> getPrefixes(String val) {
			return null;
		}

		// Dummy implemenation - not used!
		@Override
		public String getPrefix(String uri) {
			return null;
		}

		@Override
		public Iterator<String> getPrefixes() {
			// TODO Auto-generated method stub
			return null;
		}
	};
}
