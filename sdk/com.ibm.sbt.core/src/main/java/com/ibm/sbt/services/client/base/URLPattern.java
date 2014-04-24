/* © Copyright IBM Corp. 2014 * 
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

import static com.ibm.sbt.services.client.base.CommonConstants.AMPERSAND;
import static com.ibm.sbt.services.client.base.CommonConstants.CH_LEFT_BRACE;
import static com.ibm.sbt.services.client.base.CommonConstants.CH_RIGHT_BRACE;
import static com.ibm.sbt.services.client.base.CommonConstants.DOUBLE_SLASH;
import static com.ibm.sbt.services.client.base.CommonConstants.EMPTY;
import static com.ibm.sbt.services.client.base.CommonConstants.EQUALS;
import static com.ibm.sbt.services.client.base.CommonConstants.INIT_URL_PARAM;
import static com.ibm.sbt.services.client.base.CommonConstants.RE_QUESTION_MARK;
import static com.ibm.sbt.services.client.base.CommonConstants.SLASH;
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
	private List<UrlPart> mutableParts;

	public URLPattern(String urlPattern){
		this.urlPattern = urlPattern;
		mutableParts = new ArrayList<UrlPart>();
		List<String> urlParts = new ArrayList<String>();
		List<String> urlParams = new ArrayList<String>();
		if (urlPattern.lastIndexOf(INIT_URL_PARAM) > -1){
			String[] urlSplit = urlPattern.split(RE_QUESTION_MARK);
			urlParts.addAll(Arrays.asList(urlSplit[0].split(SLASH)));
			urlParams.addAll(Arrays.asList(urlSplit[1].split(AMPERSAND)));
			compile(urlParts, false);
			compile(urlParams, true);
		} else {
			urlParts.addAll(Arrays.asList(urlPattern.split(SLASH)));
			compile(urlParts, false);
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
		StringBuilder sb = new StringBuilder();
		boolean inParams = false;
		for (Iterator<UrlPart> mutablePartIterator=mutableParts.iterator(); mutablePartIterator.hasNext(); ){
			UrlPart part = mutablePartIterator.next();
			if (inParams == false && part.isParam()) {
				sb.deleteCharAt(sb.length()-1);
				inParams = true;
				sb.append(INIT_URL_PARAM);
			}
			if (part.isMutable()){
				sb.append(part.getPartValue(namedParts));
			} else {
				sb.append(part.getName());
			}
			if (mutablePartIterator.hasNext()){
				sb.append(part.getSeparator());
			}
		}
		return sanitizeSlashes(sb.toString());
	}

	/*
	 * Removes double slashes on the url
	 */
	protected String sanitizeSlashes(String url){
		return url.replaceAll(DOUBLE_SLASH, SLASH);
	}
	
	private void compile(List<String> parts, boolean isParams){
		for (String part: parts){
			mutableParts.add(isParams?new UrlParam(part):new UrlPart(part));
		}
	}
	
	private class UrlPart {
		private String name;
		private boolean isMutable;
		private final boolean isParam;

		protected UrlPart(String part){
			this(part, false);
		}

		protected UrlPart(String part, boolean isParam){
			this.isParam = isParam;
			compile(part);
		}

		protected boolean isCurrentPart(String part){
			return name.equals(part);
		}
		
		protected String getName(){
			String value = encode(name);
			return isMutable()?addBraces(value):value;
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
		
		protected String getPartValue(List<NamedUrlPart> parts){
			String value = getName();;
			if (isMutable()){
				boolean partMatch = false;
				for (NamedUrlPart part : parts){
					if (isCurrentPart(part.getName())){
						value = part.getValue();
						partMatch = true;
						break;
					}
				}
				if (!partMatch){
					throw new IllegalArgumentException("Missing parameter "+getName());
				}
			}
			return encode(value);
		}
		
		protected String addBraces(String value){
			return new StringBuilder(CH_LEFT_BRACE).append(value).append(CH_RIGHT_BRACE).toString();
		}

		protected boolean isMutable(){
			return isMutable;
		}

		protected boolean isParam(){
			return isParam;
		}
		
		protected String getSeparator(){
			return SLASH;
		}
		
		protected void compile(String part){
			int indexLeft = part.indexOf(CH_LEFT_BRACE);
			if (indexLeft < 0) {
				this.name = part;
				this.isMutable = false;
			} else {
				int indexRight = part.indexOf(CH_RIGHT_BRACE);
				this.name = part.substring(indexLeft+1, indexRight);
				this.isMutable = true;
			}
		}
	}

	private class UrlParam extends UrlPart {
		private final UrlPart paramName;
		private final UrlPart paramValue;
		
		protected UrlParam (String param){
			super(param, true);
			if (param.lastIndexOf(EQUALS) > -1) { 
				String[] split = param.split(EQUALS);
				this.paramName = new UrlPart(split[0]);
				this.paramValue = new UrlPart(split[1]);
			} else {
				this.paramName = new UrlPart(param);
				this.paramValue = null;
			}
		}

		protected String getPartValue(List<NamedUrlPart> parts){
			StringBuilder sb = new StringBuilder();
			sb.append(paramName.getPartValue(parts));
			if (paramValue != null){
				sb.append(EQUALS).append(paramValue.getPartValue(parts));
			}
			return encode(sb.toString());
		}
		
		@Override
		protected String getName(){
			StringBuilder sb = new StringBuilder();
			sb.append(paramName.getName());
			if (paramValue != null){
				sb.append(EQUALS).append(paramValue.getName());
			}
			return encode(sb.toString());
		}

		@Override
		protected String getSeparator(){
			return AMPERSAND;
		}
	}
}
