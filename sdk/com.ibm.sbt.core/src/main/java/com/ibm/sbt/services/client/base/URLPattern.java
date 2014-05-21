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

import static com.ibm.sbt.services.client.base.CommonConstants.DOUBLE_SLASH;
import static com.ibm.sbt.services.client.base.CommonConstants.SLASH;
import static com.ibm.sbt.services.client.base.CommonConstants.CH_LEFT_BRACE;
import static com.ibm.sbt.services.client.base.CommonConstants.CH_RIGHT_BRACE;

import java.util.Arrays;
import java.util.List;

import com.ibm.commons.util.StringUtil;

/**
 * 
 * A url pattern
 * 
 * @author Carlos Manias
 *
 */
public class URLPattern {
	
	private final String urlPattern;

	public URLPattern(String urlPattern){
		this.urlPattern = urlPattern;
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
		String url = this.urlPattern;
		for (NamedUrlPart namedPart : namedParts) {
			url = StringUtil.replace(url, "{" + namedPart.getName() + "}", namedPart.getValue());
		}
		checkNoMissingParameters(url);
		return url.replaceAll(DOUBLE_SLASH, SLASH);
	}
	
	protected void checkNoMissingParameters(String url){
		int indexStart = url.indexOf(CH_LEFT_BRACE);
		if (indexStart >= 0){
			int indexEnd = url.indexOf(CH_RIGHT_BRACE, indexStart);
			String partName = url.substring(indexStart + 1, indexEnd-1);
			throw new IllegalArgumentException("Missing parameter: "+partName);
		}
	}
}
