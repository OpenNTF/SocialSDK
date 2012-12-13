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
package com.ibm.sbt.jasmine;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.playground.snippets.Snippet;

/**
 * @author mwallace
 *
 */
public class SnippetFactory {
	
	static final List<Snippet> EMPTY_LIST = new ArrayList<Snippet>();
	
	public static List<Snippet> getSnippets(ServletContext servletContext) {
		try {
			String snippetName = null;
			Context context = Context.getUnchecked();
			if (context != null) {
				Map<String, Object> requestParams = context.getRequestParameterMap();
				if (requestParams.containsKey("spec")) {
					return EMPTY_LIST;
				}
				snippetName = (String)requestParams.get("snippet");
			}
			
			return getSnippets("http://localhost:8080/sbt.sample.web/snippet?format=xml", snippetName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPTY_LIST;
	}
	
	/*
	 * Read all snippets available at specified location
	 */
	private static List<Snippet> getSnippets(String url, String snippetName) throws ClientProtocolException, IOException, XMLException {
		// read the snippets as an xml document
		HttpGet httpGet = new HttpGet(url);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity httpEntity = httpResponse.getEntity();
		InputStream inputStream = httpEntity.getContent();
		Document document = DOMUtil.createDocument(inputStream);
		//System.out.println(DOMUtil.getXMLString(document));
		
		Object[] nodes = DOMUtil.nodes(document.getDocumentElement(), "snippet");
		List<Snippet> snippets = new ArrayList<Snippet>();
		for (int i=0; i<nodes.length; i++) {
			Node snippetNode = (Node)nodes[i];
			if (StringUtil.isEmpty(snippetName)) {
				snippets.add(createSnippet((Element)snippetNode));
			} else if (snippetName.equals(DOMUtil.getAttributeValue((Element)snippetNode, "unid"))) {
				snippets.add(createSnippet((Element)snippetNode));
			}
		}
		return snippets;
	}

	/*
	 * Create a Snippet instance from a DOM node
	 */
	private static Snippet createSnippet(Element snippetNode) throws ClientProtocolException, IOException, XMLException {
		// read the snippet as an xml document
		String url = DOMUtil.getAttributeValue(snippetNode, "url");
		HttpGet httpGet = new HttpGet(url);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity httpEntity = httpResponse.getEntity();
		InputStream inputStream = httpEntity.getContent();
		Document document = DOMUtil.createDocument(inputStream);
		//System.out.println(DOMUtil.getXMLString(document));
		
		// create snippet
		Snippet snippet = new Snippet();
		snippet.setCss(DOMUtil.value(document.getDocumentElement(), "/snippet/css"));
		snippet.setDescription(DOMUtil.value(document.getDocumentElement(), "/snippet/description"));
		snippet.setHtml(DOMUtil.value(document.getDocumentElement(), "/snippet/html"));
		snippet.setJs(DOMUtil.value(document.getDocumentElement(), "/snippet/js"));
		snippet.setUnid(DOMUtil.value(document.getDocumentElement(), "/snippet/unid"));
		snippet.setTags(StringUtil.splitString(DOMUtil.value(document.getDocumentElement(), "/snippet/tags"),','));
		snippet.setLabels(StringUtil.splitString(DOMUtil.value(document.getDocumentElement(), "/snippet/unid"),','));
		
		return snippet;
	}

}
