/*
 * © Copyright IBM Corp. 2014
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
package com.ibm.sbt.services.rest.atom;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Node;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

/**
 * @author mwallace
 *
 */
public class AtomFeed {
		
	private XmlDataHandler dataHandler;
	private List<AtomEntry> entries;

	/**
	 * Construct an AtomEntity instance.
	 * 
	 * @param service
	 * @param data
	 * @param namespaceCtx
	 * @param xpathExpression
	 */
	public AtomFeed(Node node) {
		dataHandler = new XmlDataHandler(node, nameSpaceCtx, (XPathExpression)AtomXPath.feed.getPath());
	}
	
    /**
     * Return the value of id from ATOM feed document.
     * 
     * @method getId
     * @return {String} ID of the ATOM feed
     */
    public String getId() {
        return dataHandler.getAsString(AtomXPath.id);
    }

    /**
     * Return the value of title from ATOM feed document.
     * 
     * @method getTitle
     * @return {String} ATOM feed title
     */
    public String getTitle() {
        return dataHandler.getAsString(AtomXPath.title);
    }

    /**
     * Return the value of summary from ATOM feed document.
     * 
     * @method getSummary
     * @return {String} ATOM feed summary
     */
    public String getSummary() {
        return dataHandler.getAsString(AtomXPath.summary);
    }

    /**
     * Return the value of subtitle from ATOM feed document.
     * 
     * @method getSubtitle
     * @return {String} ATOM feed subtitle
     */
    public String getSubtitle() {
        return dataHandler.getAsString(AtomXPath.subtitle);
    }

    /**
     * Return the content from ATOM feed document.
     * 
     * @method getContent
     * @return {Object} Content
     */
    public String getContent() {
        return dataHandler.getAsString(AtomXPath.content);
    }

//    /**
//     * Gets a author from ATOM feed document.
//     * 
//     * @method getAuthor
//     * @return {Person} author of the ATOM feed document
//     */
//    public Person getAuthor() {
//    	return new Person(null, new XmlDataHandler((Node)dataHandler.getData(), 
//	    			ConnectionsConstants.nameSpaceCtx, (XPathExpression)AtomXPath.author.getPath()));
//    }
//
//    /**
//     * Gets a contributor from ATOM feed document.
//     * 
//     * @method getContributor
//     * @return {Person} contributor of the ATOM feed document
//     */
//    public Person getContributor() {
//    	return new Person(null, new XmlDataHandler((Node)dataHandler.getData(), 
//	    			ConnectionsConstants.nameSpaceCtx, (XPathExpression)AtomXPath.contributor.getPath()));
//    }

    /**
     * Return the published date from ATOM feed document.
     * 
     * @method getPublished
     * @return {Date} Published date of the ATOM feed document
     */
    public Date getPublished() {
    	try {
    		return dataHandler.getAsDate(AtomXPath.published);
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	}
    }

    /**
     * Return the last updated date from ATOM feed document.
     * 
     * @method getUpdated
     * @return {Date} Last updated date of the ATOM feed document
     */
    public Date getUpdated() {
    	try {
    		return dataHandler.getAsDate(AtomXPath.updated);
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	}
    }
    
    /**
     * Return the alternate url of the ATOM feed document.
     * 
     * @method getAlternateUrl
     * @return {String} Alternate url
     */
    public String getAlternateUrl() {
        return dataHandler.getAsString(AtomXPath.alternateUrl);
    }
            
    /**
     * Return the self url of the ATOM feed document.
     * 
     * @method getSelfUrl
     * @return {String} Self url
     */
    public String getSelfUrl() {
        return dataHandler.getAsString(AtomXPath.selfUrl);
    }
    
    /**
     * Return the next url of the ATOM feed document.
     * 
     * @method getEditUrl
     * @return {String} Edit url
     */
    public String getNextUrl() {
        return dataHandler.getAsString(AtomXPath.nextUrl);
    }
    
    /**
     * Return a list of AtomEntry's of the ATOM feed document.
     * 
     * @return {List<AtomEntry>}
     */
    public List<AtomEntry> getEntries() {
    	if (entries == null) {
        	entries = new ArrayList<AtomEntry>();
    		List<Node> nodes = dataHandler.getEntries(AtomXPath.entry);
	    	for (Node node : nodes) {
	    		entries.add(new AtomEntry(node));
	    	}
    	}
    	return entries;
    }

	/**
	 * Return XML string for the Atom feed.
	 * 
	 * @return
	 * @throws XMLException
	 */
	public String toXmlString() throws XMLException {
		return DOMUtil.getXMLString(dataHandler.getData());
	}
	
}
