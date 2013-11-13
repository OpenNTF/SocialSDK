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

import org.w3c.dom.Node;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.XResult;
import com.ibm.commons.xml.xpath.XPathException;
import com.ibm.commons.xml.xpath.XPathExpression;

/**
 * This class provides an implementation of the DataHandler abstract class to use data in XML format.
 * @author Carlos Manias
 *
 */
public class XmlDataHandler implements DataHandler<Node> {
	
	private final NamespaceContext nameSpaceCtx;
	private Node data;
	
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
    	this.data = xpathExpression == null? data : getEntry(data, xpathExpression);
    }
    
    /**
     * This method gets a list of Nodes for a particular field
     * 
     * @param path
     * @return 
     */
	@Override
	public List<Node> getEntries(FieldEntry path){
		return getEntries((XPathExpression)path.getPath());
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

	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.datahandlers.DataHandler#getAsArray(com.ibm.sbt.services.client.base.datahandlers.FieldEntry)
	 */
	@Override
	public String[] getAsArray(FieldEntry path) {
		return getAsArray((XPathExpression)path.getPath());
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.datahandlers.DataHandler#getAsArray(java.lang.String)
	 */
	@Override
	public String[] getAsArray(String path) {
		return getAsArray(getXPathQuery(path));
	}
	
	/*
	 * This method returns a list of nodes from an XPathExpression
	 */
	private String[] getAsArray(XPathExpression xpathExpression) {
		List<String> list = new ArrayList<String>();
		String[] results = null;
		if (data instanceof Node) {
			XResult xResult = null;
			try {
				xResult = getEntryResults(data, xpathExpression);
			} catch (XPathException e) {
			}
			results = xResult.getValues();
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
			XResult xResult = null;
			try {
				xResult = getEntryResults(data, xpathExpression);
			} catch (XPathException e) {
			}
			results = Arrays.asList(xResult.getNodes());
			for (Object result : results){
				entries.add((Node)result);
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
			node = (Node)(getEntryResults(data, xpathExpression).getSingleNode());
		} catch (XMLException e) {
		}
		return node;
	}
	
	/*
	 * This method returns an XResult object evaluating an XPathExpression against a Node 
	 * @return xresult
	 */
	private XResult getEntryResults(Node data, XPathExpression xpathExpression) throws XPathException{
		XResult xResult = null;
		xResult = xpathExpression.eval(data, nameSpaceCtx);
		return xResult;
	}
	
	/*
     * @return xpath query for specified field. Field names follow IBM Connections naming convention
	 * @throws XMLException 
     */
    private XPathExpression getXPathQuery(String path) {
    	XPathExpression xPath = null;
		try {
			xPath = DOMUtil.createXPath(path);
		} catch (XMLException e) {
		}
    	return xPath;
    }
	
    /**
     * This method returns the value of a field as a String
     * @param field
     * @return value 
     */
	@Override
	public String getAsString(FieldEntry field){
		XPathExpression xpQuery = (XPathExpression)field.getPath();
    	return getFieldUsingXPath(xpQuery);
	}
	
    /**
     * This method returns the value of a field as a String
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
     * This method returns the value of a field from an XPathExpression
     * @return Execute xpath query 
     */
    public String getFieldUsingXPath(XPathExpression xpathQuery) {
		try {
			XResult xResult = xpathQuery.eval(data, nameSpaceCtx);
			return xResult.getStringValue();
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
		this.data = (Node)data;
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
		} catch(NumberFormatException e) {
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
		} catch(NumberFormatException e) {
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
		} catch(NumberFormatException e) {
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
		} catch(NumberFormatException e) {
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
	public Date getAsDate(FieldEntry field, final Locale locale) throws DataHandlerException {
		String value = getAsString(field);
		Date date = null;
		DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		try {
			if(value != null) {
				date = format.parse(value);
			}
		} catch(ParseException e) {
			// Dates from search service work with below format
			// Example of such a date is : 2012-09-05T16:15:25+08:00
			format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX",locale);
			try {
				date = format.parse(value);
			} catch (ParseException e1) {
			}
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
		DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		try {
			date = format.parse(value);
		} catch(ParseException e) {
			// Dates from search service work with below format
			// Example of such a date is : 2012-09-05T16:15:25+08:00
			format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX",locale);
			try {
				date = format.parse(value);
			} catch (ParseException e1) {
			}
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
	public boolean getAsBoolean(FieldEntry fieldName){
		String value = getAsString(fieldName);
		return Boolean.parseBoolean(value);
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
		return getEntry(getData(), (XPathExpression)path.getPath());
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
		try {
			return Long.parseLong(getAsString(fieldName));
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
