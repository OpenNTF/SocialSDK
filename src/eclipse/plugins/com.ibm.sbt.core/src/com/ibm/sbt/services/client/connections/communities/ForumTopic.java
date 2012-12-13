package com.ibm.sbt.services.client.connections.communities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.w3c.dom.Document;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.util.XmlNavigator;

public class ForumTopic {
	
	static final Map<String, String> xpathMap;
	private HashMap<String, String> fieldsMap = new HashMap<String, String>(); //this holds the values which dev sets while creating/ updating profile
	private CommunityService communityService; // Service reference
	static {
		xpathMap = new HashMap<String, String>();
		String[][] pairs = {
				{"id",		"/a:entry/a:id"},
				{"title",				"/a:entry/a:title"},
				{"summary",				"/a:entry/a:summary[@type='text']"}
		};
		for (String[] pair : pairs) {
			xpathMap.put(pair[0], pair[1]);
		}
	}

	static com.ibm.commons.xml.NamespaceContext nameSpaceCtx = new NamespaceContext() {

		@Override
		public String getNamespaceURI(String prefix) {
			String uri;
			if(prefix.equals("h"))
				uri = "http://www.w3.org/1999/xhtml";
			else if(prefix.equals("a"))
				uri = "http://www.w3.org/2005/Atom";
			else if(prefix.equals("snx"))
				uri = "http://www.ibm.com/xmlns/prod/sn";

			else 
				uri = null;
			return uri;
		}

		// Dummy implementation - not used!
		@Override
		public Iterator<String> getPrefixes(String val) {
			return null;
		}

		// Dummy implemenation - not used!
		@Override
		public String getPrefix(String uri) {
			return null;
		}

		@Override
		public Iterator<String> getPrefixes() {
			// TODO Auto-generated method stub
			return null;
		}
	};

	private Document data; 
	private String id; 

	public ForumTopic(CommunityService communityService, String id) {
		this.communityService = communityService;
		this.id = id;
	}


	public Object getData() {
		return data;
	}

	public void setData(Document data) {
		this.data = data;
	}

	public static com.ibm.commons.xml.NamespaceContext getNameSpaceCtx() {
		return nameSpaceCtx;
	}

	public void setNameSpaceCtx(com.ibm.commons.xml.NamespaceContext nameSpaceCtx) {
		this.nameSpaceCtx = nameSpaceCtx;
	}

	public HashMap<String, String> getFieldsMap() {
		return fieldsMap;
	}

	public String getid() {
		return id;
	}

	public String getTitle() {
		return get("title");
	}

	public void setTitle(String title) {
		fieldsMap.put("title",title);
	}

	/**
	 * @return value for specified field. Field names follow IBM Connections naming convention
	 */
	public String get(String fieldName)
	{
		String xpQuery = getXPathQuery(fieldName);
		return getFieldUsingXPath(xpQuery);
	}

	/**
	 * @return xpath query for specified field. Field names follow IBM Connections naming convention
	 */
	public String getXPathQuery(String fieldName)
	{	
		return xpathMap.get(fieldName);
	}

	/**
	 * @return Execute xpath query on Profile XML
	 */
	public String getFieldUsingXPath(String xpathQuery)
	{
		try {

			return DOMUtil.value(this.data, xpathQuery, nameSpaceCtx);
		} catch (XMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}	
	}

	@Override
	public String toString()
	{
		return (new XmlNavigator((Document)data)).toString();

	}
}
