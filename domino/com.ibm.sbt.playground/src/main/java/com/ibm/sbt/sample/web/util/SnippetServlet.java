/*
 * � Copyright IBM Corp. 2012
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
package com.ibm.sbt.sample.web.util;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.runtime.servlet.BaseHttpServlet;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.playground.assets.Node;
import com.ibm.sbt.playground.assets.RootNode;
import com.ibm.sbt.playground.assets.jssnippets.JSSnippet;
import com.ibm.sbt.playground.assets.jssnippets.JSSnippetAssetNode;
import com.ibm.sbt.playground.vfs.VFSFile;

/**
 * @author mwallace
 */
public class SnippetServlet extends BaseHttpServlet {

	private static final long	serialVersionUID		= 1L;

	public static final String	PARAM_FORMAT			= "format";						//$NON-NLS-1$
	public static final String	PARAM_UNID				= "unid";							//$NON-NLS-1$
	public static final String	PARAM_SNIPPET			= "snippet";						//$NON-NLS-1$

	public static final String	FORMAT_JSON				= "json";							//$NON-NLS-1$
	public static final String	FORMAT_XML				= "xml";							//$NON-NLS-1$
	public static final String	FORMAT_GADGETS_JSON		= "gadgets_json";					//$NON-NLS-1$

	public static final String	APPLICATION_JAVASCRIPT	= "application/javascript";		//$NON-NLS-1$
	public static final String	APPLICATION_XML			= "application/xml";				//$NON-NLS-1$
	public static final String	UTF8					= "utf-8";							//$NON-NLS-1$

	static final String			sourceClass				= SnippetServlet.class.getName();
	static final Logger			logger					= Logger.getLogger(sourceClass);

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		try {
			String unid = request.getParameter(PARAM_UNID);
			if (unid == null || unid.length() == 0) {
				unid = request.getParameter(PARAM_SNIPPET);
			}
			String format = getFormat(request);

			String str = null;
			RootNode rootNode = SnippetFactory.getJsSnippets(getServletContext(), request);
			if (unid != null && unid.length() > 0) {
				VFSFile rootFile = SnippetFactory.getJsRootFile(getServletContext());
				JSSnippet snippet = (JSSnippet) rootNode.loadAsset(rootFile, unid);
				if (snippet == null) {
					service400(request, response, "Invalid unid: {0}", unid);
					return;
				}
				if (FORMAT_JSON.equals(format)) {
					str = toJson(request, snippet);
				} else {
					str = toXml(request, snippet);
				}
			} else {
				List<Node> children = rootNode.getAllChildrenFlat();
				if (FORMAT_JSON.equals(format)) {
					str = rootNode.getAsJson();
				} else if (FORMAT_GADGETS_JSON.equals(format)) {
					str = toGadgetsJson(request, children);
				} else {
					str = toXml(request, children);
				}
			}

			// write response
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), UTF8));
			try {
				// response is of type application/javascript
				response.setStatus(HttpServletResponse.SC_OK);
				boolean isJson = FORMAT_JSON.equals(format) || FORMAT_GADGETS_JSON.equals(format);
				response.setContentType(isJson ? APPLICATION_JAVASCRIPT : APPLICATION_XML);
				writer.write(str);
			} finally {
				writer.flush();
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error generating json for sample snippets", e);
		}
	}

	/*
	 * Return format
	 */
	private String getFormat(HttpServletRequest request) {
		String val = request.getParameter(PARAM_FORMAT);
		if (val != null && val.length() > 0) {
			if (FORMAT_GADGETS_JSON.equalsIgnoreCase(val)) {
				return FORMAT_GADGETS_JSON;
			}
			if (FORMAT_JSON.equalsIgnoreCase(val)) {
				return FORMAT_JSON;
			}
			if (FORMAT_XML.equalsIgnoreCase(val)) {
				return FORMAT_XML;
			}
		}
		return FORMAT_XML;
	}

	/*
	 * Return list of children in json notation
	 */
	private String toJson(HttpServletRequest request, List<Node> children) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		for (int i = 0; i < children.size(); i++) {
			Node node = children.get(i);
			if (node.isAsset()) {
				JSSnippetNode snippetNode = (JSSnippetNode) node;
				sb.append("\"").append(snippetNode.getUnid()).append("\": {\n");
				sb.append("  \"level\": \"").append(snippetNode.getLevel()).append("\",\n");
				sb.append("  \"path\": \"").append(snippetNode.getPath()).append("\",\n");
				sb.append("  \"unid\": \"").append(snippetNode.getUnid()).append("\"\n");
				sb.append("  \"url\": \"").append(snippetNode.getUrl(request)).append("\"\n");
				sb.append((i + 1 < children.size()) ? "},\n" : "}\n");
			}
		}
		sb.append("}\n");
		return sb.toString();
	}

	/*
	 * Return list of children in gadgets json notation
	 */
	private String toGadgetsJson(HttpServletRequest request, List<Node> children) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"collections\": [\n");
		for (int i = 0; i < children.size(); i++) {
			Node node = children.get(i);
			if (node.isAsset()) {
				JSSnippetAssetNode snippetNode = (JSSnippetAssetNode) node;
				String unid = snippetNode.getUnid();
				String gadgetUrl = UrlUtil.getBaseUrl(request) + "/gadget/sampleRunner.jsp?snippet="
						+ URLEncoder.encode(unid);
				sb.append("{\n");
				sb.append("\"name\": \"").append(snippetNode.getName()).append("\",\n");
				sb.append("\"Description\": \"").append(snippetNode.getName()).append(" from ")
						.append(snippetNode.getCategory()).append("\",\n");
				sb.append("\"apps\" : [\n");
				sb.append("{\"name\": \"").append(snippetNode.getName()).append("\", \"url\": \"")
						.append(gadgetUrl).append("\"}\n");
				sb.append("]\n");
				sb.append((i + 1 < children.size()) ? "},\n" : "}\n");
			}
		}
		sb.append("] }\n");
		return sb.toString();
	}

	/*
	 * Return list of children in xml notation
	 */
	private String toXml(HttpServletRequest request, List<Node> children) {
		StringBuilder sb = new StringBuilder();
		sb.append("<snippets>\n");
		for (int i = 0; i < children.size(); i++) {
			Node node = children.get(i);
			if (node.isAsset()) {
				sb.append("  <snippet name=\"").append(node.getName()).append("\"\n");
				sb.append("           level=\"").append(node.getLevel()).append("\"\n");
				sb.append("           path=\"").append(node.getPath()).append("\"\n");
				sb.append("           unid=\"").append(node.getUnid()).append("\"\n");
				sb.append("           url=\"").append(node.getJspUrl()).append("\"/>\n");
			}
		}
		sb.append("</snippets>\n");
		return sb.toString();
	}

	/*
	 * Return snippet in json notation
	 */
	private String toJson(HttpServletRequest request, JSSnippet snippet) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		toJson(sb, "unid", snippet.getUnid());
		toJson(sb, "js", snippet.getJs());
		toJson(sb, "html", snippet.getHtml());
		toJson(sb, "docHtml", snippet.getDocHtml());
		toJson(sb, "css", snippet.getCss());
		toJson(sb, "description", snippet.getDescription());
		toJson(sb, "tags", snippet.getTags());
		toJson(sb, "labels", snippet.getLabels());
		sb.append("}\n");
		return sb.toString();
	}

	/**
	 * @param snippet
	 * @param sb
	 */
	private void toJson(StringBuilder sb, String name, String[] values) {
		if (values != null) {
			toJson(sb, name, StringUtil.concatStrings(values, ',', true));
		}
	}

	/**
	 * @param snippet
	 * @param sb
	 */
	private void toJson(StringBuilder sb, String name, String value) {
		sb.append("  \"").append(name).append("\": \"");
		encodeJson(sb, value, true);
		sb.append("\",\n");
	}

	/** Minimum printable ASCII character */
	private static final int	ASCII_MIN	= 32;
	/** Maximum printable ASCII character */
	private static final int	ASCII_MAX	= 126;

	/*
	 * Encode for use as JSON value
	 */
	private void encodeJson(StringBuilder b, String s, boolean preventBackslash) {
		if (s == null || s.length() == 0) {
			return;
		}
		int length = s.length();
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			switch (c) {
				case '\b':
					b.append("\\b");break; //$NON-NLS-1$
				case '\t':
					b.append("\\t");break; //$NON-NLS-1$
				case '\n':
					b.append("\\n");break; //$NON-NLS-1$
				case '\f':
					b.append("\\f");break; //$NON-NLS-1$
				case '\r':
					b.append("\\r");break; //$NON-NLS-1$
				case '\'':
					b.append("\\'");break; //$NON-NLS-1$
				case '\"':
					b.append("\\\"");break; //$NON-NLS-1$
				case '\\':
					if (!preventBackslash) {
						b.append("\\\\");}break; //$NON-NLS-1$
				default: {
					if ((c < ASCII_MIN) || (c > ASCII_MAX)) {
						b.append("\\u"); //$NON-NLS-1$
						b.append(StringUtil.toUnsignedHex(c, 4));
					} else {
						b.append(c);
					}
				}
			}
		}
	}

	/*
	 * Return snippet in xml notation
	 */
	private String toXml(HttpServletRequest request, JSSnippet snippet) {
		StringBuilder sb = new StringBuilder();
		sb.append("<snippet>\n");
		addCDataElement(sb, "unid", snippet.getUnid());
		addCDataElement(sb, "js", snippet.getJs());
		addCDataElement(sb, "html", snippet.getHtml());
		addCDataElement(sb, "docHtml", snippet.getDocHtml());
		addCDataElement(sb, "css", snippet.getCss());
		addCDataElement(sb, "theme", snippet.getTheme());
		addCDataElement(sb, "description", snippet.getDescription());

		if (snippet.getTags() != null) {
			addCDataElement(sb, "tags", StringUtil.concatStrings(snippet.getTags(), ',', true));
		}
		if (snippet.getLabels() != null) {
			addCDataElement(sb, "labels", StringUtil.concatStrings(snippet.getLabels(), ',', true));
		}
		sb.append("</snippet>\n");
		return sb.toString();
	}

	/*
	 * Create element with cdata section containing value
	 */
	private void addCDataElement(StringBuilder sb, String name, String value) {
		sb.append("<").append(name).append(">");
		if (value != null && value.length() != 0) {
			sb.append("<![CDATA[").append(value).append("]]>\n");
		}
		sb.append("</").append(name).append(">\n");
	}

}
