/*
 *  Copyright IBM Corp. 2013
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
package com.ibm.sbt.services.client.base.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.ConnectionsConstants;

/**
 * EntityUtil provides utility methods meant for consumption by any Service implementation
 * 
 * @author Manish Kataria
 */
public class EntityUtil {
	
	public static boolean isEmail(String userId) {
		return StringUtil.isEmpty(userId) || userId.matches("[^@]+@[^@]+\\.[^@]+");
	}

	public static String encodeURLParam(String param){
		String paramValue = "";
		try {
			paramValue = URLEncoder.encode(param, ConnectionsConstants.UTF8);
		} catch (UnsupportedEncodingException e) {
			
		}
		return paramValue;
	}
}
