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
package com.ibm.sbt.services.client.connections.files;

/**
 * Files NamespacesConnections
 * 
 * @author Vimal Dhupar
 */
import java.util.Iterator;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.NamespaceContext;

public class NamespacesConnections {

	public static com.ibm.commons.xml.NamespaceContext nameSpaceCtx = new NamespaceContext() {
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
			} else {
				uri = null;
			}
			return uri;
		}

		// Dummy implementation - not
		// used!
		@Override
		public Iterator<String> getPrefixes(String val) {
			return null;
		}

		// Dummy implementation - not
		// used!
		@Override
		public String getPrefix(String key) {
			if (StringUtil.equals(key, "visibility")
					|| StringUtil.equals(key, "label")
					|| StringUtil.equals(key, "uuid")
					|| StringUtil.equals(key, "created")
					|| StringUtil.equals(key, "modified")
					|| StringUtil.equals(key, "lastAccessed")
					|| StringUtil.equals(key, "modifier")
					|| StringUtil.equals(key, "shared")
					|| StringUtil.equals(key, "libraryId")
					|| StringUtil.equals(key, "libraryType")
					|| StringUtil.equals(key, "versionUuid")
					|| StringUtil.equals(key, "versionLabel")
					|| StringUtil.equals(key, "propogation")
					|| StringUtil.equals(key, "totalMediaSize")
					|| StringUtil.equals(key, "restrictedVisibility")
					|| StringUtil.equals(key, "objectTypeId")
					|| StringUtil.equals(key, "lock")) {
				return "td";
			} else if (StringUtil.equals(key, "userid")
					|| StringUtil.equals(key, "userState")
					|| StringUtil.equals(key, "rank")) {
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
