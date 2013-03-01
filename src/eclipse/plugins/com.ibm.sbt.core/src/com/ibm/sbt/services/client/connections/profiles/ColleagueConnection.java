package com.ibm.sbt.services.client.connections.profiles;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.XMLException;

public class ColleagueConnection implements Serializable{
	 private static final long serialVersionUID = 2500126733627058535L;
	    static final Map<String, String> xpathMap;
	    private Map<String, String> fieldsMap = new HashMap<String, String>(); //this holds the values which dev sets while creating/ updating profile
	    static final String sourceClass = Profile.class.getName();
	    static final Logger logger = Logger.getLogger(sourceClass);

	    private Document data; 
	    private String connectionId;
	    private ProfileService profileService; // Service reference
	    
	    static {
		    xpathMap = new HashMap<String, String>();
		    String[][] pairs = {
		    		{"entry",				"/a:feed/a:entry"},
		    		{"id",					"/a:entry/a:id"},
		    		{"title",				"/a:entry/a:title"},
		    		{"content",				"/a:entry/a:content"}
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

			// Dummy implementation - not used!
			@Override
			public String getPrefix(String uri) {
				return null;
			}

			@Override
			public Iterator<String> getPrefixes() {
				return null;
			}
		};
	    
		public ColleagueConnection(ProfileService profileService, String connectionId) {
			this.profileService = profileService;
			this.connectionId = connectionId;
		}
		
		public Object getData() {
			return data;
		}

		public void setData(Document data) {
			this.data = data;
		}
		/**
	     * @return the title
	     */
	    public String getTitle() {
	    	String title = fieldsMap.get("title");
	    	if(title!=null)
	    		return title;
	    	else
	    		return get("title");
	    }
	   
	    
	    /**
	     * @return the name
	     */
	    public String getContent() {
	    	String content = fieldsMap.get("content");
	    	if(content!=null)
	    		return content;
	    	else
	    		return get("content");
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



}
