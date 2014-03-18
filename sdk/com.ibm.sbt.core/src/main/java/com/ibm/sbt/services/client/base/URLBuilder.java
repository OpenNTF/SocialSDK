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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.ibm.commons.runtime.util.URLEncoding;
import com.ibm.commons.util.StringUtil;

/**
 * URLBuilder provides a unified way to generate urls
 * 
 * @author Carlos Manias
 */
public class URLBuilder {
	private final TreeMap<Version, VersionedUrl> urlVersions;
	
	public URLBuilder(VersionedUrl[] args) {
		urlVersions = new TreeMap<Version, VersionedUrl>();
		for (int i = 0; i < args.length; i++) {
			urlVersions.put(args[i].getKey(), args[i]);
		}
	}

	/**
	 * Returns URL pattern for the specified version
	 * @param version
	 * @return
	 */
	public VersionedUrl getPattern(Version version){
		//Returns an entry for a version of Connections less than or equal the version requested
		Entry<Version, VersionedUrl> entry = urlVersions.floorEntry(version);
		if (entry == null) {
			throw new IllegalArgumentException("No support found for Connections version "+version.toString());
		}
		return entry.getValue();
	}
	
	/**
	 * Returns the formatted URL for the specified version of Connections
	 * @param version
	 * @param args
	 * @return
	 */
	public String format(Version version, String... args) {
		VersionedUrl urlPattern = getPattern(version);
		int mutableParts = urlPattern.getMutableParts();
		if (args.length!=mutableParts) {
			throw new IllegalArgumentException("Wrong number of arguments, expected "+mutableParts+", got "+args.length);
		}
		return formatPattern(urlPattern.getCompiledUrl(), Arrays.asList(args));
	}
	
	/*
	 * Subtitutes the mutable parts of the url with the values supplied
	 */
	protected String formatPattern(String urlPattern, List<String> args) {
		List<String> encoded = new ArrayList<String>();
		for(String arg : args) {
			try {
				encoded.add(URLEncoding.encodeURIString(arg, "UTF-8", 0, false));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return sanitizeUrl(StringUtil.format(urlPattern, encoded.toArray()));
	}
	
	/*
	 * Removes double slashes in a url
	 */
	protected String sanitizeUrl(String url){
		return url.replaceAll("//", "/");
	}
}
