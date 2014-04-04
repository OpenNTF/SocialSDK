/* * ?? Copyright IBM Corp. 2014 * 
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

import java.io.IOException;

import com.ibm.commons.runtime.util.URLEncoding;

/**
 * 
 * Named Url Part
 * 
 * @author Carlos Manias
 *
 */
public class NamedUrlPart {

	private final String name;
	private final String value;

	public NamedUrlPart(String name, String value) {
		try {
			this.name = name;
			this.value = URLEncoding.encodeURIString((value==null)?"":value, "UTF-8", 0, false);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
}
