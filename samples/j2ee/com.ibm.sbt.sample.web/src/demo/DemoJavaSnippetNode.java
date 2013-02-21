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
package demo;

import javax.servlet.http.HttpServletRequest;

import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.assets.javasnippets.JavaSnippetAssetNode;


/**
 * Definition of a Java code snippet.
 */
public class DemoJavaSnippetNode extends JavaSnippetAssetNode {

	public DemoJavaSnippetNode(CategoryNode parent, String name) {
		super(parent,name);
	}

	// url for JSP's
	public String getJSPUrl(HttpServletRequest request) {
		String pathToJSPSample = getUnid(); //getPath()+".jsp";	
		return UrlUtil.getRequestUrl(request,3)+"?snippet="+pathToJSPSample;
	}
}
