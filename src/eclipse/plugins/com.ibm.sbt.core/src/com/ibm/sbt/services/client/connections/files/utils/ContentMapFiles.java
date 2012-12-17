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
package com.ibm.sbt.services.client.connections.files.utils;

/**
 * Files ContentMapFiles <br>
 * This Class contains XPath mappings which are used by the FIleService APIs
 *  
 * @author Vimal Dhupar
 */
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vimal Dhupar
 */
public class ContentMapFiles {

	public static final Map<String, String>	xpathMap;
	static {
		xpathMap = new HashMap<String, String>();
		String[][] pairs = { { "email", "/a:feed/a:author/a:email" },
			{ "emailFromEntry", "/a:entry/a:author/a:email" },
			{ "userUuidFromEntry", "/a:entry/a:author/snx:userid" },
			{ "nameOfUserFromEntry", "/a:entry/a:author/a:name" },
			{ "userStateFromEntry", "/a:entry/a:author/snx:userState" },

			{ "userStateModifier", "/a:entry/td:modifier/snx:userState" },
			{ "nameModifier", "/a:entry/td:modifier/a:name" },
			{ "userUuidModifier", "/a:entry/td:modifier/snx:userid" },
			{ "emailModifier", "/a:entry/td:modifier/a:email" },

			{ "fileName", "/a:feed/a:entry/a:title" }, { "fileUuid", "/a:feed/a:entry/td:uuid" },
			{ "downLinkFromEntry", "/a:entry/a:link[@rel=\"edit-media\"]/@href" },
			{ "commentFromEntry", "/a:entry/a:content" }, { "comment", "/a:feed/a:entry/a:content" },
			{ "entry", "/a:feed/a:entry" }, { "uuidFromEntry", "/a:entry/td:uuid" },
			{ "lockFromEntry", "/a:entry/td:lock/@type" }, { "labelFromEntry", "/a:entry/td:label" },
			{ "categoryFromEntry", "/a:entry/a:category/@label" },
			{ "modifiedFromEntry", "/a:entry/td:modified" },

			{ "visibilityFromEntry", "/a:entry/td:visibility" },
			{ "libraryTypeFromEntry", "/a:entry/td:libraryType" },
			{ "versionUuidFromEntry", "/a:entry/td:versionUuid" },
			{ "summaryFromEntry", "/a:entry/a:summary" },
			{ "restrictedVisibilityFromEntry", "/a:entry/td:restrictedVisibility" },
			{ "titleFromEntry", "/a:entry/a:title" }, { "totalResults", "/a:feed/opensearch:totalResults" },
			{ "publishedFromEntry", "/a:entry/a:published" }, { "updatedFromEntry", "/a:entry/a:updated" },
			{ "createdFromEntry", "/a:entry/td:created" }, { "modifiedFromEntry", "/a:entry/td:modified" },
			{ "lastAccessedFromEntry", "/a:entry/td:lastAccessed" },

			{ "visibilityFromEntry", "/a:entry/td:visibility" },
			{ "libraryIdFromEntry", "/a:entry/td:libraryId" },
			{ "libraryTypeFromEntry", "/a:entry/td:libraryType" },
			{ "versionLabelFromEntry", "/a:entry/td:versionLabel" },
			{ "propagationFromEntry", "/a:entry/td:propagation" },
			{ "totalMediaSizeFromEntry", "/a:entry/td:totalMediaSize" },
			{ "objectTypeIdFromEntry", "/a:entry/td:objectTypeId" },

		};

		for (String[] pair : pairs) {
			xpathMap.put(pair[0], pair[1]);
		}
	}
}
