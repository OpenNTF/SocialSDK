/* Copyright IBM Corp. 2014 * 
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

import static com.ibm.sbt.services.client.base.CommonConstants.EMPTY;
import static com.ibm.sbt.services.client.base.CommonConstants.UTF8;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.ibm.commons.runtime.util.URLEncoding;

/**
 * 
 * A url pattern
 * 
 * @author Carlos Manias
 *
 */
public class URLPattern {
	private final String urlPattern;
	private static final char LEFT_BRACE = '{';
	private static final String SLASH = "/";
	private static final String QUESTION_MARK = "?";
	private static final String RE_QUESTION_MARK = "\\?";
	private static final String AMPERSAND = "&";
	private static final String DOUBLE_SLASH = "//";
	private List<String> urlParts;
	private List<String> paramParts;
	private List<MutablePart> mutableParts;
	private List<MutablePart> mutableParamParts;

	public URLPattern(String urlPattern){
		this.urlPattern = urlPattern;
		mutableParts = new ArrayList<MutablePart>();
		mutableParamParts = new ArrayList<MutablePart>();
		if (urlPattern.lastIndexOf(QUESTION_MARK) > -1){
			String[] urlSplit = urlPattern.split(RE_QUESTION_MARK);
			urlParts = Arrays.asList(urlSplit[0].split(SLASH)); //parts before the ? (i.e. fus/roh/dah?ka=me&ha=me would be fus/roh/dah)
			paramParts = Arrays.asList(urlSplit[1].split(AMPERSAND)); //parts after the ? (i.e. fus/roh/dah?ka=me&ha=me would be ka=me&ha=me)
			int lastIndex = compile(urlParts, mutableParts, 0);
			compile(paramParts, mutableParamParts, lastIndex);
		} else {
			urlParts = Arrays.asList(urlPattern.split(SLASH)); //parts before the ? (i.e. fus/roh/dah?ka=me&ha=me would be fus/roh/dah)
			compile(urlParts, mutableParts, 0);
		}
	}

	public String getUrlPattern(){
		return urlPattern;
	}
	
	/**
	 * Formats the Url pattern contained on this object with the provided NamedUrlParts
	 * @param args
	 * @return
	 */
	public String format(NamedUrlPart... args){
		List<NamedUrlPart> namedParts = Arrays.asList(args);
		Iterator<MutablePart> mutablePartIterator=mutableParts.iterator();
		if (!mutablePartIterator.hasNext()) return urlPattern;
		StringBuilder sb = new StringBuilder();
		int urlPartIndex=0;
		String url = "";
		String baseUrl = computeUrl(urlParts.iterator(), namedParts, mutablePartIterator, urlPartIndex, SLASH);
		String paramUrl = "";
		if (paramParts != null && paramParts.size()>0){
			mutablePartIterator = mutableParamParts.iterator();
			paramUrl = computeUrl(paramParts.iterator(), namedParts, mutablePartIterator, urlParts.size(), AMPERSAND);
			url = sb.append(baseUrl).append(QUESTION_MARK).append(paramUrl).toString();
		} else {
			url = baseUrl;
		}

		return sanitizeSlashes(url);
	}
	
	protected String computeUrl(Iterator<String> partIterator, 
								List<NamedUrlPart> namedParts, 
								Iterator<MutablePart> mutablePartIterator,
								int urlPartIndex, String joiner){
		StringBuilder sb = new StringBuilder();
		MutablePart mutablePart = mutablePartIterator.next();
		while (partIterator.hasNext()){
			String part = partIterator.next();
			if (mutablePart.isCurrentIndex(urlPartIndex)){
				//This part is mutable, let's get the value
				boolean partMatch = false;
				for (NamedUrlPart namedPart : namedParts) {
					if (mutablePart.isCurrentPart(namedPart.getName())){
						sb.append(namedPart.getValue());
						partMatch = true;
						break;
					}
				}
				if (!partMatch){
					throw new IllegalArgumentException("Missing parameter "+mutablePart.getName());
				}
				if (mutablePartIterator.hasNext()){ mutablePart = mutablePartIterator.next(); }
			} else {
				//This part is immutable, let's add it to the url
				sb.append(encode(part));
			}
			if (partIterator.hasNext()) sb.append(joiner);
			urlPartIndex++;
		}
		return sb.toString();
	}

	/*
	 * URLencodes the url parts
	 */
	protected String encode(String part){
		try {
			return URLEncoding.encodeURIString((part==null)?EMPTY:part, UTF8, 0, false);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * Removes double slashes on the url
	 */
	protected String sanitizeSlashes(String url){
		return url.replaceAll(DOUBLE_SLASH, SLASH);
	}
	
	private int compile(List<String> parts, List<MutablePart> mutables, int partIndex){
		for (String part: parts){
			if (part.charAt(0) == LEFT_BRACE){
				String partName = part.substring(1, part.length()-1);
				mutables.add(new MutablePart(partName, partIndex));
			}
			partIndex++;
		}
		return partIndex;
	}
	
	private class UrlParam {
		private final String paramName;
		private final String paramValue;
		
		protected UrlParam (String param){
			String[] split = param.split("=");
			this.paramName = split[0];
			this.paramValue = split[1];
		}
		
	}
	
	private class MutablePart {
		private int index;
		private String name;
		protected MutablePart(String name, int index){
			this.index = index;
			this.name = name;
		}
		
		protected boolean isCurrentIndex(int ind){
			return index == ind;
		}
		
		protected boolean isCurrentPart(String part){
			return name.equals(part);
		}
		
		protected String getName(){
			return name;
		}
	}
}
