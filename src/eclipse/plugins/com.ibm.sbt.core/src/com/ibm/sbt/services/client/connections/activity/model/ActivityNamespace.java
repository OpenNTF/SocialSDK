/*
 * © Copyright IBM Corp. 2013
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

package com.ibm.sbt.services.client.connections.activity.model;

import java.util.Iterator;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.NamespaceContext;

public class ActivityNamespace {

	public static NamespaceContext nameSpaceCtx = new NamespaceContext() {
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
			} else if (prefix.equals("app")) {
				uri = "http://www.w3.org/2007/app";
			} else if (prefix.equals("os")) {
				uri = "http://a9.com/-/spec/opensearch/1.1/";
			}else {
				uri = null;
			}
			return uri;
		}

		@Override
		public Iterator<String> getPrefixes(String val) {
			return null;
		}

		@Override
		public String getPrefix(String key) {
			if (StringUtil.equals(key, "totalResults")
					|| StringUtil.equals(key, "startIndex")) {
				return "os";
			} else if (StringUtil.equals(key, "userid") 
					|| StringUtil.equals(key, "userState")
					|| StringUtil.equals(key, "ldapid")
					|| StringUtil.equals(key, "activity")
					|| StringUtil.equals(key, "position")
					|| StringUtil.equals(key, "depth")
					|| StringUtil.equals(key, "permissions")
					|| StringUtil.equals(key, "duedate")
					|| StringUtil.equals(key, "icon")) {
				return "snx";
			}
			return null;
		}

		@Override
		public Iterator<String> getPrefixes() {
			// TODO Auto-generated method
			// stub
			return null;
		}
	};
}
