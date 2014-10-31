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

import java.util.Date;

import org.w3c.dom.Document;
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
public class AtomEntry {
	
	private XmlDataHandler dataHandler;

	/**
	 * Construct an AtomEntity instance.
	 * 
	 * @param service
	 * @param data
	 * @param namespaceCtx
	 * @param xpathExpression
	 */
	public AtomEntry(Node node) {
		XPathExpression xpath = (node instanceof Document) ? (XPathExpression)AtomXPath.singleEntry.getPath() : null;
		dataHandler = new XmlDataHandler(node, nameSpaceCtx, xpath);
	}
	
	/**
	 * Return the String value from the ATOM entry document using the given xpath expression
	 * 
	 * @method getAsString
	 * @param xpath
	 * @return
	 */
	public String getAsString(String xpath){
		return dataHandler.getAsString(xpath);
	}

	/**
	 * Return the Long value from the ATOM entry document using the given xpath expression
	 * 
	 * @method getAsLong
	 * @param xpath
	 * @return
	 */
	public Long getAsLong(String xpath){
		return dataHandler.getAsLong(xpath);
	}

	/**
	 * Return the int value from the ATOM entry document using the given xpath expression
	 * 
	 * @method getAsInt
	 * @param xpath
	 * @return
	 */
	public int getAsInt(String xpath){
		return dataHandler.getAsInt(xpath);
	}

	/**
	 * Return the Float value from the ATOM entry document using the given xpath expression
	 * 
	 * @method getAsFloat
	 * @param xpath
	 * @return
	 */
	public Float getAsFloat(String xpath){
		return dataHandler.getAsFloat(xpath);
	}

	/**
	 * Return the String[] value from the ATOM entry document using the given xpath expression
	 * 
	 * @method getAsArray
	 * @param xpath
	 * @return
	 */
	public String[] getAsArray(String xpath){
		return dataHandler.getAsArray(xpath);
	}

	/**
	 * Return the Date value from the ATOM entry document using the given xpath expression
	 * 
	 * @method getAsDate
	 * @param xpath
	 * @return
	 */
	public Date getAsDate(String xpath){
		return dataHandler.getAsDate(xpath);
	}

	/**
	 * Return the boolean value from the ATOM entry document using the given xpath expression
	 * 
	 * @method getAsBoolean
	 * @param xpath
	 * @return
	 */
	public boolean getAsBoolean(String xpath){
		return dataHandler.getAsBoolean(xpath);
	}
	
    /**
     * Return the value of id from ATOM entry document.
     * 
     * @method getId
     * @return {String} ID of the ATOM entry
     */
    public String getId() {
        return dataHandler.getAsString(AtomXPath.id);
    }

    /**
     * Return the value of title from ATOM entry document.
     * 
     * @method getTitle
     * @return {String} ATOM entry title
     */
    public String getTitle() {
        return dataHandler.getAsString(AtomXPath.title);
    }

    /**
     * Return the value of summary from ATOM entry document.
     * 
     * @method getSummary
     * @return {String} ATOM entry summary
     */
    public String getSummary() {
        return dataHandler.getAsString(AtomXPath.summary);
    }

    /**
     * Return the value of subtitle from ATOM entry document.
     * 
     * @method getSubtitle
     * @return {String} ATOM entry subtitle
     */
    public String getSubtitle() {
        return dataHandler.getAsString(AtomXPath.subtitle);
    }

    /**
     * Return the content from ATOM entry document.
     * 
     * @method getContent
     * @return {Object} Content
     */
    public String getContent() {
        return dataHandler.getAsString(AtomXPath.content);
    }

//    /**
//     * Gets a author from ATOM entry document.
//     * 
//     * @method getAuthor
//     * @return {Person} author of the ATOM entry document
//     */
//    public Person getAuthor() {
//    	return new Person(null, new XmlDataHandler((Node)dataHandler.getData(), 
//	    			ConnectionsConstants.nameSpaceCtx, (XPathExpression)AtomXPath.author.getPath()));
//    }
//
//    /**
//     * Gets a contributor from ATOM entry document.
//     * 
//     * @method getContributor
//     * @return {Person} contributor of the ATOM entry document
//     */
//    public Person getContributor() {
//    	return new Person(null, new XmlDataHandler((Node)dataHandler.getData(), 
//	    			ConnectionsConstants.nameSpaceCtx, (XPathExpression)AtomXPath.contributor.getPath()));
//    }

    /**
     * Return the published date from ATOM entry document.
     * 
     * @method getPublished
     * @return {Date} Published date of the ATOM entry document
     */
    public Date getPublished() {
    	try {
    		return dataHandler.getAsDate(AtomXPath.published);
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	}
    }

    /**
     * Return the last updated date from ATOM entry document.
     * 
     * @method getUpdated
     * @return {Date} Last updated date of the ATOM entry document
     */
    public Date getUpdated() {
    	try {
    		return dataHandler.getAsDate(AtomXPath.updated);
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	}
    }
    
    /**
     * Return the alternate url of the ATOM entry document.
     * 
     * @method getAlternateUrl
     * @return {String} Alternate url
     */
    public String getAlternateUrl() {
        return dataHandler.getAsString(AtomXPath.alternateUrl);
    }
            
    /**
     * Return the self url of the ATOM entry document.
     * 
     * @method getSelfUrl
     * @return {String} Self url
     */
    public String getSelfUrl() {
        return dataHandler.getAsString(AtomXPath.selfUrl);
    }
    
    /**
     * Return the edit url of the ATOM entry document.
     * 
     * @method getEditUrl
     * @return {String} Edit url
     */
    public String getEditUrl() {
        return dataHandler.getAsString(AtomXPath.editUrl);
    }

	/**
	 * Return XML string for the Atom entity.
	 * 
	 * @return
	 * @throws XMLException
	 */
	public String toXmlString() throws XMLException {
		return DOMUtil.getXMLString(dataHandler.getData());
	}
	
}
