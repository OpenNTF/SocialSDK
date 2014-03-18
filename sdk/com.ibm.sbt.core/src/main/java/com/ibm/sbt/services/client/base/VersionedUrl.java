/* * © Copyright IBM Corp. 2014 * 
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

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.commons.util.StringUtil;

/**
 * 
 * Immutable implementation of the Map.Entry interface
 * 
 * @author Carlos Manias
 *
 */
public class VersionedUrl extends SimpleImmutableEntry<Version, String> {
	private static final long serialVersionUID = 1998441770272116581L;
	private static final char LEFT_BRACE = '{';
	private static final char RIGHT_BRACE = '}';
	private static final String SLASH = "/";
	private static final char CSLASH = '/';
	private static final Pattern braces = Pattern.compile("\\{[^{}/]+\\}");
	private final int mutableParts;
	private final String compiledUrl;

	public VersionedUrl(Version version, String urlPattern) {
		super(version, urlPattern);
		this.mutableParts = countMutableParts(urlPattern);
		this.compiledUrl = compile(urlPattern);
	}
	
	public int getMutableParts(){
		return mutableParts;
	}
	
	public String getCompiledUrl(){
		return compiledUrl;
	}
	
	private int countMutableParts(String urlPattern){
		int count = 0;
		Matcher matcher = braces.matcher(urlPattern);
		while (matcher.find()) {
		    count++;
		}
		return count;
	}

	private String compile(String urlPattern){
		String[] parts = urlPattern.split(SLASH);
		int count = 0;
		for (int i = 0; i < parts.length; i++) {
			if (parts[i].charAt(0) == LEFT_BRACE) {
				StringBuilder sb = new StringBuilder();
				parts[i] = sb.append(LEFT_BRACE).append(count++).append(RIGHT_BRACE).toString();
			}
		}
		return StringUtil.concatStrings(parts, CSLASH, false);
	}
}
