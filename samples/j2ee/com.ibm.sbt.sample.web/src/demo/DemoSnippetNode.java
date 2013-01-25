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

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.sbt.playground.snippets.CategoryNode;
import com.ibm.sbt.playground.snippets.SnippetNode;


/**
 * Definition of a code snippet.
 */
public class DemoSnippetNode extends SnippetNode {

	public DemoSnippetNode(CategoryNode parent, String name) {
		super(parent,name);
	}

	public String getUrl(HttpServletRequest request) {
		String unid = getUnid();
		return UrlUtil.getRequestUrl(request,false)+"?snippet="+URLEncoder.encode(unid);
	}
	
	// url for JSP's
	public String getJSPUrl(HttpServletRequest request) {
		String pathToJSPSample = getPath()+".jsp";	
		return UrlUtil.getRequestUrl(request,false)+"?javaSamplePath="+pathToJSPSample;
	}
}
