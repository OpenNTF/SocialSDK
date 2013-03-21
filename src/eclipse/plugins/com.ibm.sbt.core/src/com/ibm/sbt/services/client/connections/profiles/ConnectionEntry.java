package com.ibm.sbt.services.client.connections.profiles;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.connections.profiles.utils.Messages;

public class ConnectionEntry{
	  
	 private Document		data;											// Document object which stores the response feed obtained from Server.
	 private String			connectionId;									
	 private String			content;
	 private String			title;
	 private String			contributorName;
	 private String 		contributorUserId;
	 private String 		contributorEmail;
	 private String 		authorName;
	 private String 		authorUserId;
	 private String 		authorEmail;
	 private Profile 		profileEntry;
	 
	 static final String		sourceClass	= ConnectionEntry.class.getName();
	 static final Logger		logger		= Logger.getLogger(sourceClass);
	 static final Map<String, String> xpathMap;
	
	 public ConnectionEntry() {
			profileEntry = new Profile();
		}

		public ConnectionEntry(String connectionId) {
			this.connectionId = connectionId;
			profileEntry = new Profile();
		}

		static {
		    xpathMap = new HashMap<String, String>();
		    String[][] pairs = {
		    		{"entry",				"/a:feed/a:entry"},
		    		{"id",					"/a:feed/a:entry/a:id"},
		    		{"title",				"/a:feed/a:entry/a:title"},
		    		{"updated",				"/a:feed/a:entry/a:updated"},
		    		{"authorName",			"/a:feed/a:entry/snx:connection/a:contributor[@snx:rel=\"http://www.ibm.com/xmlns/prod/sn/connection/source\"]/a:name"},
		    		{"authorUserId",		"/a:feed/a:entry/snx:connection/a:contributor[@snx:rel=\"http://www.ibm.com/xmlns/prod/sn/connection/source\"]/snx:userid"},
		    		{"authorEmail",			"/a:feed/a:entry/snx:connection/a:contributor[@snx:rel=\"http://www.ibm.com/xmlns/prod/sn/connection/source\"]/a:email"},
		    		{"contributorName",		"/a:feed/a:entry/snx:connection/a:contributor[@snx:rel=\"http://www.ibm.com/xmlns/prod/sn/connection/target\"]/a:name"},
		    		{"contributorUserId",	"/a:feed/a:entry/snx:connection/a:contributor[@snx:rel=\"http://www.ibm.com/xmlns/prod/sn/connection/target\"]/snx:userid"},
		    		{"contributorEmail",	"/a:feed/a:entry/snx:connection/a:contributor[@snx:rel=\"http://www.ibm.com/xmlns/prod/sn/connection/target\"]/a:email"},
		    		{"selfLinkFromEntry", 	"/a:feed/a:entry/a:link/@href"},
		    		{"editLinkFromEntry", 	"/a:feed/a:entry/a:link[@rel=\"edit\"]/@href"},
		    		{"content",				"/a:feed/a:entry/a:content"}
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

			@Override
			public Iterator<String> getPrefixes(String val) {
				return null;
			}
			@Override
			public String getPrefix(String uri) {
				return null;
			}
			@Override
			public Iterator<String> getPrefixes() {
				return null;
			}
		};
	    
		/**
		 * createConnectionEntryWithData
		 * <p>
		 * method to create the ConnectionEntry object for the response feed
		 * 
		 * @param result
		 * @return FileEntry
		 */
		public static ConnectionEntry createConnectionEntryWithData(Document result, String type) {
			if (logger.isLoggable(Level.FINEST)) {
				logger.entering(sourceClass, "createResultFileWithData");
			}
			ConnectionEntry connection = new ConnectionEntry();
			connection.setData(result);
			if (type.equals("profile")) {
				connection.profileEntry.setData(result);
			}
			if (logger.isLoggable(Level.FINEST)) {
				logger.exiting(sourceClass, "createConnectionEntryWithData");
			}

			return connection;
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
	    public String getConnectionId() {
	    	
	    	if (!StringUtil.isEmpty(connectionId)) {
				return connectionId;
			}
	    	String selfLink = get("selfLinkFromEntry");
	    	String connectionId = selfLink.substring(selfLink.lastIndexOf("connectionId="),selfLink.lastIndexOf('&'));// check why the selfLink is not returned
			return connectionId;
	    }
	   
	    public void setConnectionId(String connectionId) {
			this.connectionId = connectionId;
		}
		/**
	     * @return the title
	     */
	    public String getTitle() {
	    	
	    	if (!StringUtil.isEmpty(title)) {
				return title;
			}
			return get("title");
	    }
	   
	    public void setTitle(String title) {
			this.title = title;
		}

	    
	    /**
	     * @return the content
	     */
	    public String getContent() {
	    	if (!StringUtil.isEmpty(content)) {
				return content;
			}
			return get("content");
	    }
	    
	    /**
	     * @return the Contributor Name
	     */
	    public String getContributorName() {
	    	if (!StringUtil.isEmpty(contributorName)) {
				return contributorName;
			}
			return get("contributorName");
	    }
	    
	    /**
	     * @return the Contributor UserId
	     */
	    public String getContributorUserId() {
	    	if (!StringUtil.isEmpty(contributorUserId)) {
				return contributorUserId;
			}
			return get("contributorUserId");
	    }
	    
	    /**
	     * @return the Contributor Email
	     */
	    public String getContributorEmail() {
	    	if (!StringUtil.isEmpty(contributorEmail)) {
				return contributorEmail;
			}
			return get("contributorEmail");
	    }
	    
	    /**
	     * @return the Author Name
	     */
	    public String getAuthorName() {
	    	if (!StringUtil.isEmpty(authorName)) {
				return authorName;
			}
			return get("authorName");
	    }
	    
	    /**
	     * @return the Author UserId
	     */
	    public String getAuthorUserId() {
	    	if (!StringUtil.isEmpty(authorUserId)) {
				return authorUserId;
			}
			return get("authorUserId");
	    }
	    
	    /**
	     * @return the Author Email
	     */
	    public String getAuthorEmail() {
	    	if (!StringUtil.isEmpty(authorEmail)) {
				return authorEmail;
			}
			return get("authorEmail");
	    }
	    public void setContent(String content) {
			this.content = content;
		}
	    
	    public Profile getProfileEntry() {
			return profileEntry.getProfileEntry();
		}

		public void setProfileEntry(Profile profileEntry) {
			this.profileEntry = profileEntry;
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
				logger.log(Level.SEVERE, Messages.ProfileError_2 + "getFieldUsingXPath");
				return null;
			}	
		}

		
		// ONLY GETTERS FOR SOME OF THE FIELDS IN RESPONSE FEED
	

		public String getUpdated() {
			return get("updatedFromEntry");
		}

		public String getSelfLink(){
			return get("selfLinkFromEntry");
		}
		
		public String getEditLink(){
			return get("editLinkFromEntry");
		}


}
