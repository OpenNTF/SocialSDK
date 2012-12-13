package com.ibm.sbt.services.util;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.InvalidInputException;

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

public class CommonUtils {

	/*
	 * Common function to check null and log out error information
	 */
	public static boolean checkForNull(String classname, String methodname, Map<String, Object> parameters)
			throws InvalidInputException {
		if (null != parameters) {
			Logger logger = Logger.getLogger(classname);
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				String argumentName = entry.getKey();
				Object argumentVal = entry.getValue();
				if (argumentVal instanceof String) {
					if (StringUtil.isEmpty((String) argumentVal)) { // Use the StringUtil class for checking String objects
						if (logger.isLoggable(Level.SEVERE)) {
							logger.severe(classname + " " + methodname + " Argument " + argumentName
									+ ", passed was null");
						}
						InvalidInputException invalidinputexception = new InvalidInputException(null,
								"Input parameter " + argumentName + " passed was null", classname, methodname);
						throw invalidinputexception;
					}
				} else if (null == argumentVal) {
					if (logger.isLoggable(Level.SEVERE)) {
						logger.severe(classname + " " + methodname + " Argument " + argumentName
								+ ", passed was null");
					}
					InvalidInputException invalidinputexception = new InvalidInputException(null,
							"Input parameter " + argumentName + " passed was null", classname, methodname);
					throw invalidinputexception;
				}
			}
		}
		return true;
	}
}
