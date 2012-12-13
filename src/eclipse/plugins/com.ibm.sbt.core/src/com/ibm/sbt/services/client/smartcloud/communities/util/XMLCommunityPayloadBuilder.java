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

package com.ibm.sbt.services.client.smartcloud.communities.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.sbt.services.client.smartcloud.communities.Community;
import com.ibm.sbt.util.XMLPayloadBuilder;

/**
 * @author Carlos Manias
 *
 */
public class XMLCommunityPayloadBuilder extends XMLPayloadBuilder {
	private static final String sourceClass = Community.class.getName();
    private static final Logger logger = Logger.getLogger(sourceClass);
    
	private static Map<String, String> namespaces;
    private static final String ENTRY = "entry";
    private static final String XMLNS_SNX = "xmlns:snx";
    private static final String XMLNS_APP = "xmlns:app";
    private static final String XMLNS = "xmlns";
    private static final String XMLNS_ATOM = "xmlns:atom";
    private static final String TYPE = "type";
    private static final String TEXT = "text";
    private static final String HTML = "html";
    private static final String TERM = "term";
    private static final String SCHEME = "scheme";
    private static final String TITLE = "title";
    private static final String CONTENT = "content";
    private static final String CATEGORY = "category";
    private static final String COMMUNITY = "community";
    private static final String SNX_COMMUNITYTYPE = "snx:communityType";
    private static final String CONTRIBUTOR = "contributor";
    private static final String SNX_USERID = "snx:userid";
    private static final String SNX_ROLE = "snx:role";
    private static final String COMPONENT = "component";
    
    public static XMLCommunityPayloadBuilder INSTANCE = new XMLCommunityPayloadBuilder();
    
    private XMLCommunityPayloadBuilder() { }
    
	static {
    	namespaces = new HashMap<String, String>();
    	
    	String[][] pairs = {
    			{XMLNS,				"http://www.w3.org/2007/xmlns/"},
	    		{XMLNS_ATOM,		"http://www.w3.org/2005/Atom"},
	    		{XMLNS_SNX,			"http://www.ibm.com/xmlns/prod/sn"},
	    		{XMLNS_APP,			"http://www.w3.org/2007/app"},
	    		{SCHEME,			"http://www.ibm.com/xmlns/prod/sn/type"},
	    		{COMPONENT,			"http://www.ibm.com/xmlns/prod/sn/communities"}
    	};
    	
    	for (String[] pair : pairs) {
    		namespaces.put(pair[0], pair[1]);
	    }
    }
	
	@Override
	public Document createPayload(){
		Document doc = super.createPayload();
			Element rootElement = doc.createElement(ENTRY);
				rootElement.setAttribute(XMLNS_SNX, namespaces.get(XMLNS_SNX));
				rootElement.setAttribute(XMLNS_APP, namespaces.get(XMLNS_APP));
				rootElement.setAttribute(XMLNS, namespaces.get(XMLNS_APP));
			doc.appendChild(rootElement);
		return doc;
	}
	
	public String dummyTestPayload(){
		/*String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" "+
				"xmlns:app=\"http://www.w3.org/2007/app\" xmlns=\"http://www.w3.org/2007/app\">"+
				"<title type=\"text\">My test community</title>"+
				"<content type=\"html\">This is a test community</content>"+
				"<category term=\"community\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category>"+
				"<snx:communityType xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">private</snx:communityType>"+
				"</entry>";*/
		String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<entry xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" xmlns:app=\"http://www.w3.org/2007/app\" xmlns=\"http://www.w3.org/2007/app\">\r\n" + 
				"	<title type=\"text\">My test community</title>\r\n" + 
				"	<content type=\"html\">This is a test community</content>\r\n" + 
				"	<category term=\"community\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category>\r\n" + 
				"	<snx:communityType xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">private</snx:communityType>\r\n" + 
				"</entry>";
//		System.out.println(payload);
		return payload;
	}
	
	/**
	 * @param info map containing title, content and snx:communityType in a Map
	 * @return
	 */
	public Document generateCreateCommunityPayload(Map<String, String> info){
		if (logger.isLoggable(Level.FINEST)) {
    		logger.entering(sourceClass, "generateCreateCommunityPayload", new Object[] {  });
        }
		
		Document doc = createPayload();
		Element rootElement = doc.getDocumentElement();
			Element title = doc.createElement(TITLE);
				title.setAttribute(TYPE, TEXT);
				title.appendChild(doc.createTextNode(info.get(TITLE)));
			rootElement.appendChild(title);
			Element content = doc.createElement(CONTENT);
				content.setAttribute(TYPE, HTML);
				content.appendChild(doc.createTextNode(info.get(CONTENT)));
			rootElement.appendChild(content);
			Element category = doc.createElement(CATEGORY);
				category.setAttribute(TERM, COMMUNITY);
				category.setAttribute(SCHEME, namespaces.get(SCHEME));
			rootElement.appendChild(category);
			Element snx_communityType = doc.createElement(SNX_COMMUNITYTYPE);
				snx_communityType.setAttribute(XMLNS_SNX, namespaces.get(XMLNS_SNX));
				snx_communityType.appendChild(doc.createTextNode(info.get(SNX_COMMUNITYTYPE)));
			rootElement.appendChild(snx_communityType);
		if (logger.isLoggable(Level.FINEST)) {
    		logger.exiting(sourceClass, "generateCreateCommunityPayload", new Object[] { doc });
        }
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			transformer.transform(new DOMSource(doc), new StreamResult(System.out));
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}
	
	/**
	 * 
	 * @param info map containing snx_userid and snx_role
	 * @return
	 */
	public Document generateAddMemberPayload(Map<String, String> info){
		if (logger.isLoggable(Level.FINEST)) {
    		logger.entering(sourceClass, "generateAddMemberPayload", new Object[] {  });
        }
		
		Document doc = createPayload();
		Element rootElement = doc.getDocumentElement();
			Element contributor = doc.createElement(CONTRIBUTOR);
				Element snx_userid = doc.createElement(SNX_USERID);
					snx_userid.setAttribute(XMLNS_SNX, namespaces.get(XMLNS_SNX));
					snx_userid.appendChild(doc.createTextNode(info.get(SNX_USERID)));
				contributor.appendChild(snx_userid);
			rootElement.appendChild(contributor);
			Element snx_role = doc.createElement(SNX_ROLE);
				snx_role.setAttribute(COMPONENT, namespaces.get(COMPONENT));
				snx_role.setAttribute(XMLNS_SNX, namespaces.get(XMLNS_SNX));
				snx_role.appendChild(doc.createTextNode(info.get(SNX_ROLE)));
			rootElement.appendChild(snx_role);
		if (logger.isLoggable(Level.FINEST)) {
    		logger.exiting(sourceClass, "generateAddMemberPayload", new Object[] { doc });
        }
		return doc;
	}
}
