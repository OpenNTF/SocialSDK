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

package com.ibm.sbt.services.util.extractor.field;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Node;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.NamespaceContextImpl;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.XResult;
import com.ibm.commons.xml.xpath.XPathException;
import com.ibm.commons.xml.xpath.XPathExpression;

/**
 * @author Carlos Manias
 */
public class XMLFieldExtractor implements DataExtractor<Node> {
	private static final String					sourceClass						= XMLFieldExtractor.class
			.getName();
	private static final Logger					logger							= Logger.getLogger(sourceClass);
	private Map<String, XPathExpression>		xPathMap						= new HashMap<String, XPathExpression>();
	private static final NamespaceContextImpl	nameSpaceCtx					= new NamespaceContextImpl();
	public static final String					ENTITIES_EXTRACTING_NAMED_QUERY	= "_resultExtractorQuery";
	private static final String					DEFAULT_ENTITIES_XPATH			= "/sp_0:entry|/sp_0:feed/sp_0:entry";

	/**
	 * Constructor
	 * 
	 * @param map
	 */
	public XMLFieldExtractor(Map<String, XPathExpression> map) {
		this.xPathMap = map;
		if (!xPathMap.containsKey(ENTITIES_EXTRACTING_NAMED_QUERY)) {
			try {
				xPathMap.put(ENTITIES_EXTRACTING_NAMED_QUERY, DOMUtil.createXPath(DEFAULT_ENTITIES_XPATH));
			} catch (XMLException e) {}
		}
	}

	static {
		String[][] pairs = { { "app", "http://www.w3.org/2007/app" },
				{ "thr", "http://purl.org/syndication/thread/1.0" },
				{ "fh", "http://purl.org/syndication/history/1.0" },
				{ "snx", "http://www.ibm.com/xmlns/prod/sn" },
				{ "opensearch", "http://a9.com/-/spec/opensearch/1.1/" }, { "a", "http://www.w3.org/2005/Atom" },
				{ "h", "http://www.w3.org/1999/xhtml" }, { "td", "urn:ibm.com/td" },
				{ "atom", "http://www.w3.org/2005/Atom" },
				{ "cmis", "http://docs.oasis-open.org/ns/cmis/core/200908/" },
				{ "cmism", "http://docs.oasis-open.org/ns/cmis/messaging/200908/" },
				{ "cmisra", "http://docs.oasis-open.org/ns/cmis/restatom/200908/" },
				{ "lcmis", "http://www.ibm.com/xmlns/prod/sn/cmis" }, { "sp_0", "http://www.w3.org/2005/Atom" }, };
		for (String[] pair : pairs) {
			nameSpaceCtx.addNamespace(pair[0], pair[1]);
		}
	}

	private XPathExpression getXPathQuery(String fieldName) {
		return xPathMap.get(fieldName);
	}

	/**
	 * Returns the namespace context
	 * 
	 * @return namespaceCtx the Namespace context
	 */
	public NamespaceContext getNamespaceContext() {
		return nameSpaceCtx;
	}

	/**
	 * This method extracts the value of a fieldname from the data object
	 * 
	 * @param data
	 *            the data to process
	 * @param fieldName
	 *            the field to extract
	 * @return value the value of the field
	 */
	@Override
	public String get(Node data, String fieldName) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "get");
		}
		String result = "";
		try {
			XPathExpression xpathQuery = getXPathQuery(fieldName);
			XResult xResult = xpathQuery.eval(data, getNamespaceContext());
			result = xResult.getStringValue() != null ? xResult.getStringValue() : "";
		} catch (XPathException e) {
			logger.throwing(sourceClass, "get", e);
		} catch (XMLException e) {
			logger.throwing(sourceClass, "get", e);
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "get");
		}

		return result;
	}

	@Override
	public Set<String> getKnownFields() {
		return xPathMap.keySet();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Node> getNestedEntities(Node data, String namedQuery) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getEntities");
		}
		XPathExpression xpathQuery = getXPathQuery(namedQuery);

		try {
			XResult xResult = xpathQuery.eval(data, getNamespaceContext());
			// forcing erasure, as getNodes returns an array of objects
			List l = Arrays.asList(xResult.getNodes());;
			return l;
		} catch (XPathException e) {
			logger.throwing(sourceClass, "getNestedEntities", e);
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getEntities");
		}
		return null;
	}

	@Override
	public List<Node> getEntitiesFromServiceResult(Node data) {
		return getNestedEntities(data, ENTITIES_EXTRACTING_NAMED_QUERY);
	}

}
