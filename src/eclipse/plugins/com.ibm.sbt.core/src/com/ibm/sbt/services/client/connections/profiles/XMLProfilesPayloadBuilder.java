package com.ibm.sbt.services.client.connections.profiles;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.util.XMLPayloadBuilder;

/**
 * @author Swati Singh
 *
 */
public class XMLProfilesPayloadBuilder extends XMLPayloadBuilder{
	
	private static final String sourceClass = Profile.class.getName();
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
    private static final String SCHEME_COLLEAGUE = "scheme_collleague";
    private static final String SCHEME_STATUS = "scheme_status";
    private static final String TITLE = "title";
    private static final String CONTENT = "content";
    private static final String CATEGORY = "category";
    private static final String CONNECTION = "connection";
    private static final String COLLEAGUE = "colleague";
    private static final String PENDING = "pending";
    
    public static XMLProfilesPayloadBuilder INSTANCE = new XMLProfilesPayloadBuilder();
    
    private XMLProfilesPayloadBuilder() { }
    
	static {
    	namespaces = new HashMap<String, String>();
    	
    	String[][] pairs = {
    			{XMLNS,				"http://www.w3.org/2007/xmlns/"},
	    		{XMLNS_ATOM,		"http://www.w3.org/2005/Atom"},
	    		{XMLNS_SNX,			"http://www.ibm.com/xmlns/prod/sn"},
	    		{XMLNS_APP,			"http://www.w3.org/2007/app"},
	    		{SCHEME,			"http://www.ibm.com/xmlns/prod/sn/type"},
	    		{SCHEME_COLLEAGUE,	"http://www.ibm.com/xmlns/prod/sn/connection/type"},
	    		{SCHEME_STATUS,		"http://www.ibm.com/xmlns/prod/sn/status"}
    	};
    	
    	for (String[] pair : pairs) {
    		namespaces.put(pair[0], pair[1]);
	    }
    }
	
	@Override
	public Document createPayload(){
		Document doc = super.createPayload();
		Element rootElement = doc.createElement(ENTRY);
		rootElement.setAttribute(XMLNS, namespaces.get(XMLNS_ATOM));
		rootElement.setAttribute(XMLNS_SNX, namespaces.get(XMLNS_SNX));
		doc.appendChild(rootElement);
		return doc;
	}
	
	/**
	 * 
	 * @param info map containing Invite message
	 * @return
	 */
	public Document generateInviteRequestPayload(String inviteMessage){
		if (logger.isLoggable(Level.FINEST)) {
    		logger.entering(sourceClass, "generateInviteRequestPayload", new Object[] {  });
        }
		
		Document doc = createPayload();
		Element rootElement = doc.getDocumentElement();
		
		Element category = doc.createElement(CATEGORY);
		category.setAttribute(SCHEME, namespaces.get(SCHEME));
		category.setAttribute(TERM, CONNECTION);
		
		rootElement.appendChild(category);
		
		category = doc.createElement(CATEGORY);
		category.setAttribute(SCHEME, namespaces.get(SCHEME_COLLEAGUE));
		category.setAttribute(TERM, COLLEAGUE);
		rootElement.appendChild(category);
		
		category = doc.createElement(CATEGORY);
		category.setAttribute(SCHEME, namespaces.get(SCHEME_STATUS));

		category.setAttribute(TERM, PENDING);
		rootElement.appendChild(category);
		
		Element content = doc.createElement(CONTENT);
		content.setAttribute(TYPE, HTML);
		content.appendChild(doc.createTextNode(inviteMessage));// have to check this
		rootElement.appendChild(content);

		if (logger.isLoggable(Level.FINEST)) {
    		logger.exiting(sourceClass, "generateInviteRequestPayload", new Object[] { doc });
        }
		return doc;
	}

}
