/*
 * © Copyright IBM Corp. 2013
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

package com.ibm.sbt.services.client.base.datahandlers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Node;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.XResult;
import com.ibm.commons.xml.xpath.XPathException;
import com.ibm.commons.xml.xpath.XPathExpression;

/**
 * This class provides an implementation of the DataHandler abstract class to
 * use data in XML format.
 * 
 * @author Carlos Manias
 * 
 */
public class XmlDataHandler implements DataHandler<Node> {

	final static public String CONNECTIONS_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	final static public DateFormat dateFormat = new SimpleDateFormat(CONNECTIONS_DATE_FORMAT);
	static {
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	private NamespaceContext nameSpaceCtx;
	private Node data;
	@SuppressWarnings("unused")
	private String xml;

	private static final long serialVersionUID = 1L;

	private static String sourceClass = XmlDataHandler.class.getName();
	private static Logger logger = Logger.getLogger(sourceClass);
	
	/**
	 * Constructor
	 */
	public XmlDataHandler() {
	}

	/**
	 * Constructor
	 * 
	 * @param data
	 * @param nameSpaceCtx
	 */
	public XmlDataHandler(Node data, NamespaceContext nameSpaceCtx) {
		this(data, nameSpaceCtx, null);
	}

	/**
	 * Constructor
	 * 
	 * @param data
	 * @param nameSpaceCtx
	 * @param xpathExpression
	 */
	public XmlDataHandler(Node data, NamespaceContext nameSpaceCtx, XPathExpression xpathExpression) {
		this.nameSpaceCtx = nameSpaceCtx;
		this.data = (xpathExpression == null) ? data : getEntry(data, xpathExpression);
		
		if (logger.isLoggable(Level.FINE) && this.data != null) {
			try {
				this.xml = DOMUtil.getXMLString(this.data);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * This method gets a list of Nodes for a particular field
	 * 
	 * @param path
	 * @return
	 */
	@Override
	public List<Node> getEntries(FieldEntry path) {
		return getEntries((XPathExpression) path.getPath());
	}

	/**
	 * This method gets a list of Nodes for a particular field
	 * 
	 * @param path
	 * @return
	 */
	@Override
	public List<Node> getEntries(String path) {
		return getEntries(getXPathQuery(path));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibm.sbt.services.client.base.datahandlers.DataHandler#getAsArray(
	 * com.ibm.sbt.services.client.base.datahandlers.FieldEntry)
	 */
	@Override
	public String[] getAsArray(FieldEntry path) {
		return getAsArray((XPathExpression) path.getPath());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibm.sbt.services.client.base.datahandlers.DataHandler#getAsArray(
	 * java.lang.String)
	 */
	@Override
	public String[] getAsArray(String path) {
		return getAsArray(getXPathQuery(path));
	}

	/*
	 * This method returns a list of nodes from an XPathExpression
	 */
	private String[] getAsArray(XPathExpression xpathExpression) {
		String[] results = null;
		if (data instanceof Node) {
			XResult xresult = null;
			try {
				xresult = getEntryResults(data, xpathExpression);
			} catch (XPathException e) {
			}
			results = xresult.getValues();
		}
		return results;
	}

	/*
	 * This method returns a list of nodes from an XPathExpression
	 */
	private List<Node> getEntries(XPathExpression xpathExpression) {
		List<Node> entries = new ArrayList<Node>();
		List<Object> results = null;
		if (data instanceof Node) {
			XResult xresult = null;
			try {
				xresult = getEntryResults(data, xpathExpression);
			} catch (XPathException e) {
			}
			results = Arrays.asList(xresult.getNodes());
			for (Object result : results) {
				entries.add((Node) result);
			}
		}

		return entries;
	}

	/**
	 * This method gets Node for a particular field
	 * 
	 * @param path
	 * @return
	 * @throws DataHandlerException
	 */
	public Node getEntry(Node data, XPathExpression xpathExpression) {
		Node node = null;
		try {
			node = (Node) (getEntryResults(data, xpathExpression).getSingleNode());
		} catch (XMLException e) {
		}
		return node;
	}

	/*
	 * This method returns an XResult object evaluating an XPathExpression
	 * against a Node
	 * 
	 * @return xresult
	 */
	protected XResult getEntryResults(Node data, XPathExpression xpathExpression)
			throws XPathException {
		XResult xresult = null;
		xresult = xpathExpression.eval(data, nameSpaceCtx);
		return xresult;
	}

	/*
	 * @return xpath query for specified field. Field names follow IBM
	 * Connections naming convention
	 * 
	 * @throws XMLException
	 */
	protected XPathExpression getXPathQuery(String path) {
		XPathExpression xpath = null;
		try {
			xpath = DOMUtil.createXPath(path);
		} catch (XMLException e) {
		}
		return xpath;
	}

	/**
	 * This method returns the value of a field as a String
	 * 
	 * @param field
	 * @return value
	 */
	@Override
	public String getAsString(FieldEntry field) {
		XPathExpression xpQuery = (XPathExpression) field.getPath();
		return getFieldUsingXPath(xpQuery);
	}

	/**
	 * This method returns the value of a field as a String
	 * 
	 * @param fieldName
	 * @return value
	 */
	@Override
	public String getAsString(String fieldName) {
		XPathExpression xpQuery = null;
		xpQuery = getXPathQuery(fieldName);
		return getFieldUsingXPath(xpQuery);
	}

	/**
	 * This method returns the string value of a field from an XPathExpression
	 * 
	 * @return Execute xpath query
	 */
	public String getStringValue(XPathExpression xpathQuery) {
		try {
			XResult xresult = xpathQuery.eval(data, nameSpaceCtx);
			return xresult.getStringValue();
		} catch (XPathException e) {
		} catch (XMLException e) {
		}
		return null;
	}

	/**
	 * This method returns the date value of a field from an XPathExpression
	 * 
	 * @return Execute xpath query
	 */
	public Date getDateValue(XPathExpression xpathQuery) {
		try {
			XResult xresult = xpathQuery.eval(data, nameSpaceCtx);
			return xresult.getDateValue();
		} catch (XPathException e) {
		} catch (XMLException e) {
		}
		return null;
	}

	/**
	 * This method returns the boolean value of a field from an XPathExpression
	 * 
	 * @return Execute xpath query
	 */
	public boolean getBooleanValue(XPathExpression xpathQuery) {
		try {
			XResult xresult = xpathQuery.eval(data, nameSpaceCtx);
			return xresult.getBooleanValue();
		} catch (XPathException e) {
		} catch (XMLException e) {
		}
		return false;
	}

	/**
	 * This method returns the number value of a field from an XPathExpression
	 * 
	 * @return Execute xpath query
	 */
	public double getNumberValue(XPathExpression xpathQuery) {
		try {
			XResult xresult = xpathQuery.eval(data, nameSpaceCtx);
			return xresult.getNumberValue();
		} catch (XPathException e) {
		} catch (XMLException e) {
		}
		return 0;
	}

	/**
	 * This method returns the value of a field from an XPathExpression
	 * 
	 * @return Execute xpath query
	 */
	public String getFieldUsingXPath(XPathExpression xpathQuery) {
		try {
			XResult xresult = xpathQuery.eval(data, nameSpaceCtx);
			return xresult.getStringValue();
		} catch (XPathException e) {
			// TODO Add Logging
		} catch (XMLException e) {
			// TODO Add Logging
		}
		return null;
	}

	/**
	 * @set data
	 */
	@Override
	public void setData(final Object data) {
		this.data = (Node) data;
	}

	/**
	 * @return data
	 */
	@Override
	public Node getData() {
		return data;
	}

	/**
	 * @param field
	 * @return value as int
	 */
	@Override
	public int getAsInt(FieldEntry field) {
		String value = getAsString(field);
		int i = 0;
		try {
			i = Integer.parseInt(value);
		} catch (NumberFormatException e) {
		}
		return i;
	}

	/**
	 * @param fieldName
	 * @return value as int
	 */
	@Override
	public int getAsInt(String fieldName) {
		String value = getAsString(fieldName);
		int i = 0;
		try {
			i = Integer.parseInt(value);
		} catch (NumberFormatException e) {
		}
		return i;
	}

	/**
	 * @param field
	 * @return value as float
	 */
	@Override
	public float getAsFloat(FieldEntry field) {
		String value = getAsString(field);
		float f = 0;
		try {
			f = Float.parseFloat(value);
		} catch (NumberFormatException e) {
		}
		return f;
	}

	/**
	 * @param fieldName
	 * @return value as float
	 */
	@Override
	public float getAsFloat(String fieldName) {
		String value = getAsString(fieldName);
		float f = 0;
		try {
			f = Float.parseFloat(value);
		} catch (NumberFormatException e) {
		}
		return f;
	}

	/**
	 * @param field
	 * @param locale
	 * @return value as Date
	 * @throws DataHandlerException
	 */
	@Override
	public Date getAsDate(FieldEntry field, final Locale locale)
			throws DataHandlerException {
		String value = getAsString(field);
		Date date = null;
		try {
			if (value != null) {
				date = dateFormat.parse(value.trim());
			}
		} catch (ParseException e) {

		}
		return date;
	}

	/**
	 * @param fieldName
	 * @param locale
	 * @return value as Date
	 */
	@Override
	public Date getAsDate(String fieldName, final Locale locale) {
		String value = getAsString(fieldName);
		Date date = null;
		try {
			date = dateFormat.parse(value.trim());
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * @param field
	 * @return value as Date
	 * @throws DataHandlerException
	 */
	@Override
	public Date getAsDate(FieldEntry field) throws DataHandlerException {
		return getAsDate(field, Locale.getDefault());
	}

	/**
	 * @param fieldName
	 * @return value as Date
	 */
	@Override
	public Date getAsDate(String fieldName) {
		return getAsDate(fieldName, Locale.getDefault());
	}

	/**
	 * @param fieldName
	 * @return value as Date
	 */
	@Override
	public boolean getAsBoolean(FieldEntry field) {
		XPathExpression xpath = (XPathExpression) field.getPath();
		return getBooleanValue(xpath);
	}

	/**
	 * @param fieldName
	 * @return value as boolean
	 */
	@Override
	public boolean getAsBoolean(String fieldName) {
		String value = getAsString(fieldName);
		return Boolean.parseBoolean(value);
	}

	@Override
	public Node getEntry(FieldEntry path) {
		return getEntry(getData(), (XPathExpression) path.getPath());
	}

	@Override
	public Node getEntry(String path) {
		return getEntry(getData(), getXPathQuery(path));
	}

	/**
	 * @param fieldName
	 * @return value as long
	 */
	@Override
	public Long getAsLong(String fieldName) {
		String value = getAsString(fieldName);
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			return (long) 0;
		}
	}

	@Override
	public Long getAsLong(FieldEntry fieldName) {
		String value = getAsString(fieldName);
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			return (long) 0;
		}
	}
}
