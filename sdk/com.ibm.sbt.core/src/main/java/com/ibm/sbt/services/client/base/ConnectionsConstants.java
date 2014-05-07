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

package com.ibm.sbt.services.client.base;

import java.text.SimpleDateFormat;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.NamespaceContextImpl;

/**
 * This class contains all the constants used in the Connections based services.
 * 
 * @author Carlos Manias
 *
 */
public final class ConnectionsConstants {
	
	/**
	 * Reference  versions of Connections
	 */
	public static final Version v4_5 = new Version(4,5);
	public static final Version v4_0 = new Version(4,0);

	/**
	 * Data type
	 */
	public static final String TEXT = "text";
	public static final String HTML = "html";
	
	/**
	 * HTML parts
	 */
	public static final String REL = "rel";
	public static final String HREF = "href";
	
	/**
	 * General ATOM
	 */
	public static final String ENTRY = "entry";
	public static final String TAGS = "tags";
	public static final String CATEGORY = "category";
	public static final String SCHEME = "scheme";
	public static final String TERM = "term";
	public static final String CONTENT = "content";
	public static final String TYPE = "type";
	public static final String KEY = "key";
	public static final String VALUE = "value";
	public static final String DATA = "data";
	public static final String TITLE = "title";
	public static final String ID = "id";
	public static final String PUBLISHED = "published";
	public static final String UPDATED = "updated";
	public static final String SUMMARY = "summary";
	public static final String AUTHOR = "author";
	public static final String CONTRIBUTOR = "contributor";
	public static final String LABEL = "label";
	public static final String COMPLETED = "completed";
	public static final String PERSON = "person";

	/**
	 * Activity ATOM
	 */
	public static final String ACTIVITY = "activity";
	public static final String ACTIVITY_NODE = "activity_node";
	public static final String LABEL_ACTIVITYNODE = "Activity Node";
	public static final String FIELD = "field";
	public static final String FID = "fid";
	public static final String NAME = "name";
	public static final String POSITION = "position";
	public static final String DATE = "date";
	public static final String FILE = "file";
	public static final String LINK = "link";
	public static final String COMMUNITY_ACTIVITY = "community_activity";
	public static final String LABEL_COMMUNITYACTIVITY = "Community Activity";
	public static final String EXTERNAL = "external";
	public static final String LABEL_EXTERNAL = "External";
	public static final String TEMPLATE = "template";
	public static final String SNX_DUEDATE = "snx:duedate";
	public static final String REF = "ref";
	public static final String SOURCE = "source";
	public static final String IN_REPLY_TO = "in-reply-to";
	public static final String ASSIGNEDTO = "assignedto";
	public static final String USERID = "userid";

	/**
	 * Specifies the starting page of a template. The term attribute identifies the default view to use. 
	 */
	public static final String RECENT = "recent"; //$NON-NLS-1$
	public static final String OUTLINE = "outline"; //$NON-NLS-1$
	public static final String TODO = "todo"; //$NON-NLS-1$
	
	/**
	 * Wiki ATOM
	 */
	public static final String WIKI = "wiki";
	public static final String COMMUNITYUUID = "communityUuid";
	public static final String PERMISSIONS = "permissions";
	public static final String PAGE = "page";

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	/**
	 * Enum containing the namespaces and prefixes used in ATOM construction (payloads) and parsing (xpath)
	 * 
	 * @author Carlos Manias
	 *
	 */
	public enum Namespace {
		ATOM("a", "http://www.w3.org/2005/Atom"),
		TD("td", "urn:ibm.com/td"),
		CA("ca", "http://www.ibm.com/xmlns/prod/composite-applications/v1.0"),
		SNX("snx", "http://www.ibm.com/xmlns/prod/sn"),
		TYPE("type", "http://www.ibm.com/xmlns/prod/sn/type"), 
		SOURCE("source", "http://www.ibm.com/xmlns/prod/sn/source"), 
		RESOURCE_TYPE("source", "http://www.ibm.com/xmlns/prod/sn/resource-type"), 
		RESOURCE_ID("source", "http://www.ibm.com/xmlns/prod/sn/resource-id"), 
		CONTAINER("container","http://www.ibm.com/xmlns/prod/sn/container"),
		PRIORITY("priority","http://www.ibm.com/xmlns/prod/sn/priority"),
		FLAGS("flags","http://www.ibm.com/xmlns/prod/sn/flags"),
		CONNECTION("connection", "http://www.ibm.com/xmlns/prod/sn/connection/type"),
		STATUS("status", "http://www.ibm.com/xmlns/prod/sn/status"),
		THR("thr", "http://purl.org/syndication/thread/1.0"),
		FH("fh", "http://purl.org/syndication/history/1.0"),
		OPENSEARCH("opensearch", "http://a9.com/-/spec/opensearch/1.1/"),
		APP("app", "http://www.w3.org/2007/app"),
		RELEVANCE("relevance", "http://a9.com/-/opensearch/extensions/relevance/1.0/"),
		IBMSC("ibmsc", "http://www.ibm.com/search/content/2010"),
		XS("xs", "http://www.w3.org/2001/XMLSchema"),
		XHTML("xhtml", "http://www.w3.org/1999/xhtml"),
		H("h", "http://www.w3.org/1999/xhtml"),
		CMISRA("cmisra", "http://docs.oasis-open.org/ns/cmis/restatom/200908/"),
		CMISM("cmism", "http://docs.oasis-open.org/ns/cmis/messaging/200908/"), 
		LCMIS("lcmis", "http://www.ibm.com/xmlns/prod/sn/cmis"),
		CMIS("cmis", "http://docs.oasis-open.org/ns/cmis/core/200908/"), 
		OPENSOCIAL("opensocial", "http://ns.opensocial.org/2008/opensocial"), 
		TAG("tag", "tag:ibm.com,2006:td/type"),
		TAGENTRY("tag","tag:profiles.ibm.com,2006:entry"),
		XMLNS("xmlns", "http://www.w3.org/2000/xmlns/");
		
		private final String prefix;
		private final String url;
		
		Namespace(String prefix, String url){
			this.prefix = prefix;
			this.url = url;
		}

		public String getPrefix(){
			return prefix;
		}
		
		public String getNSPrefix(){
			return XMLNS.getPrefix()+":"+prefix;
		}
		
		public String getUrl(){
			return url;
		}
	}
	
	public static NamespaceContext	nameSpaceCtx = new NamespaceContextImpl();
	
	static{
		for (Namespace namespace : Namespace.values()){
			((NamespaceContextImpl)nameSpaceCtx).addNamespace(namespace.getPrefix(), namespace.getUrl());
		}
	}
	
}
