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

	public static final Map<String, String>	moderationMap;

	static {
		moderationMap = new HashMap<String, String>();
		String[][] map = { { "fileApprovalUrl", "" }, { "commentApprovalUrl", "" }, { "deleteFileUrl", "" },
			{ "deleteCommentUrl", "" }, { "getFileUrl", "" }, { "fileReviewUrl", "" },
			{ "commentReviewUrl", "" } }; // add
		// the
		// values
		// here
		// ..

		for (String[] mapEntry : map) {
			moderationMap.put(mapEntry[0], mapEntry[1]);
		}

	}
	
}
