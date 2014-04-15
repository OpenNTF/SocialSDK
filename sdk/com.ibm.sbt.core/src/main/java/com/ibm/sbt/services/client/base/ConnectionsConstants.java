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
	 * Common String characters
	 */
	public static final String NL = "\n";
	public static final String EMPTY = "";
	public static final String COMMA = ",";
	public static final String SEMICOLON = ";";
	public static final String COLON = ":";
	public static final String DOT = ".";
	public static final String AT = "@";
	public static final char 	SEPARATOR	= '/';
	public static final char 	CH_COLON	= ':';
	
	/**
	 * URL parameter construction
	 */
	public static final char 	INIT_URL_PARAM	= '?';
	public static final char 	URL_PARAM	= '&';
	public static final char 	EQUALS		= '=';

	/**
	 * Headers parameters
	 */
	public static final String	IMAGE_		= "image/";
	public static final String 	IMAGE_JPG	= "image/jpg";

	/**
	 * File extensions
	 */
	public static final String 	JPG	= "jpg";

	/**
	 * Authentication
	 */
	public static final String	OAUTH		= "oauth";
	public static final String	AUTH_TYPE	= "authType";
	
	/**
	 * Encoding
	 */
	public static final String	UTF8		= "UTF-8";

	/**
	 * Content Type
	 */
	public static final String APPLICATION_XML = "application/xml";
	public static final String APPLICATION_ATOM_XML = "application/atom+xml";
	public static final String CONTENT_TYPE = "Content-Type";

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
	 * General Services
	 */
	public static final String LOCATION_HEADER = "Location";
	
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

	/**
	 * Profile ATOM
	 */
	public static final String PROFILE = "profile";
	public static final String PERSON = "person";
	public static final String CATEGORIES = "categories";
	public static final String SNX_ATTRIB = "com.ibm.snx_profiles.attrib";
	public static final String SNX_GUID = "com.ibm.snx_profiles.base.guid";
	public static final String SNX_EMAIL = "com.ibm.snx_profiles.base.email";
	public static final String SNX_UID = "com.ibm.snx_profiles.base.uid";
	public static final String SNX_DISTINGUISHEDNAME = "com.ibm.snx_profiles.base.distinguishedName";
	public static final String SNX_DISPLAYNAME = "com.ibm.snx_profiles.base.displayName";
	public static final String SNX_GIVENNAMES = "com.ibm.snx_profiles.base.givenNames";
	public static final String SNX_SURNAME = "com.ibm.snx_profiles.base.surname";
	public static final String SNX_USERSTATE = "com.ibm.snx_profiles.base.userState";
	public static final String GUID = "guid";
	public static final String DISTINGUISHEDNAME = "distinguishedName";
	public static final String DISPLAYNAME = "displayName";
	public static final String GIVENNAMES = "givenNames";
	public static final String SURNAME = "surname";
	public static final String USERSTATE = "userState";
	public static final String BEGIN_VCARD = "BEGIN:VCARD";
	public static final String END_VCARD = "END:VCARD";
	public static final String VCARD_V21 = "VERSION:2.1";
	public static final String JOBTITLE = "jobTitle";
	public static final String ADDRESS = "address";
	public static final String TELEPHONENUMBER = "telephoneNumber";
	public static final String BUILDING = "building";
	public static final String FLOOR = "floor";
	public static final String STREETADRESS = "streetAddress";
	public static final String EXTENDEDADDRESS = "extendedAddress";
	public static final String LOCALITY = "locality";
	public static final String REGION = "region";
	public static final String POSTALCODE = "postalCode";
	public static final String COUNTRYNAME = "countryName";
	public static final String VCARD_ADDR = "ADR;WORK:;;";

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
