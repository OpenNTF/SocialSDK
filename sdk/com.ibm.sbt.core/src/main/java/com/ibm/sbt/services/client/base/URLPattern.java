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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
	private List<String> urlParts;
	private List<MutablePart> mutableParts;

	public URLPattern(String urlPattern){
		this.urlPattern = urlPattern;
		urlParts = Arrays.asList(urlPattern.split(SLASH));
		compile();
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
		StringBuilder sb = new StringBuilder();
		int urlPartIndex=0;

		List<NamedUrlPart> namedParts = Arrays.asList(args);
		Iterator<MutablePart> mutablePartIterator=mutableParts.iterator();
		MutablePart mutablePart = mutablePartIterator.next();
		for (Iterator<String> partIterator=urlParts.iterator(); partIterator.hasNext(); ){
			String part = partIterator.next();
			if (mutablePart.isCurrentIndex(urlPartIndex)){
				//This part is mutable, let's get the value
				for (NamedUrlPart namedPart : namedParts) {
					if (mutablePart.isCurrentPart(namedPart.getName())){
						sb.append(namedPart.getValue());
						break;
					}
				}
				
				if (mutablePartIterator.hasNext()){ mutablePart = mutablePartIterator.next(); }
			} else {
				//This part is immutable, let's add it to the url
				sb.append(part);
			}
			if (partIterator.hasNext()) sb.append(SLASH);
			urlPartIndex++;
		}
		return sanitizeSlashes(sb.toString());
	}
	
	protected String sanitizeSlashes(String url){
		return url.replaceAll("//", "/");
		
	}
	
	private void compile(){
		mutableParts = new ArrayList<MutablePart>();
		int partIndex = 0;
		for (String part: urlParts){
			if (part.charAt(0) == LEFT_BRACE){
				String partName = part.substring(1, part.length()-1);
				mutableParts.add(new MutablePart(partName, partIndex));
			}
			partIndex++;
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
	}
}
