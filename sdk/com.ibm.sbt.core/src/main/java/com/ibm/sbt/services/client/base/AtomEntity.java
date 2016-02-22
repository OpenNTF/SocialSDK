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
package com.ibm.sbt.services.client.base;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Node;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.common.Link;
import com.ibm.sbt.services.client.connections.common.Person;

/**
 * @author mwallace
 *
 */
public class AtomEntity extends BaseEntity {
	
	private String contentType = "html";

	/**
	 * Construct an AtomEntity instance.
	 * 
	 * @param service
	 * @param node
	 * @param namespaceCtx
	 * @param xpathExpression
	 */
	public AtomEntity(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, new XmlDataHandler(node, namespaceCtx, xpathExpression));
	}
	
	/**
	 * Construct an AtomEntity instance.
	 * 
	 * @param service
	 * @param dataHandler
	 */
	public AtomEntity(BaseService service, XmlDataHandler dataHandler) {
		super(service, dataHandler);
	}
	
	/**
	 * Construct an AtomEntity instance.
	 * 
	 * @param service
	 */
	public AtomEntity(BaseService service) {
		super(service, null);
	}

	/**
	 * Construct an AtomEntity instance.
	 * 
	 * @param service
	 * @param id
	 */
	public AtomEntity(BaseService service, String id) {
		super(service, null);
		setAsString(AtomXPath.id, id);
	}
	
	/**
	 * Default Constructor.
	 */
	public AtomEntity() {
	}
	
	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Set the data for the entity from the specified response.
	 * 
	 * @param node
	 * @param namespaceCtx
	 * @param xpathExpression
	 */
	public void setData(Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		dataHandler = new XmlDataHandler(node, namespaceCtx, xpathExpression);
	}		
	
    /**
     * Return the value of id from ATOM entry document.
     * 
     * @method getId
     * @return {String} ID of the ATOM entry
     */
    public String getId() {
        return this.getAsString(AtomXPath.id);
    }

    /**
     * Sets Id of ATOM entry.
     * 
     * @method setId
     * @param id {String} ID of the ATOM entry
     */
    public void setId(String id) {
        setAsString(AtomXPath.id, id);
    }

    /**
     * Return the value of title from ATOM entry document.
     * 
     * @method getTitle
     * @return {String} ATOM entry title
     */
    public String getTitle() {
        return this.getAsString(AtomXPath.title);
    }

    /**
     * Sets title of ATOM entry.
     * 
     * @method setTitle
     * @param title {String} title ATOM entry title
     */
    public void setTitle(String title) {
        this.setAsString(AtomXPath.title, title);
    }
    
    /**
     * Return the value of summary from ATOM entry document.
     * 
     * @method getSummary
     * @return {String} ATOM entry summary
     */
    public String getSummary() {
        return this.getAsString(AtomXPath.summary);
    }

    /**
     * Sets summary of ATOM entry.
     * 
     * @method setSummary
     * @param summary title ATOM entry summary
     */
    public void setSummary(String summary) {
        this.setAsString(AtomXPath.summary, summary);
    }
    
    /**
     * Return the value of subtitle from ATOM entry document.
     * 
     * @method getSubtitle
     * @return {String} ATOM entry subtitle
     */
    public String getSubtitle() {
        return this.getAsString(AtomXPath.subtitle);
    }

    /**
     * Sets subtitle of ATOM entry.
     * 
     * @method setSummary
     * @param subtitle {String} title ATOM entry subtitle
     */
    public void setSubtitle(String subtitle) {
        this.setAsString(AtomXPath.subtitle, subtitle);
    }
    
    /**
     * Return the content from ATOM entry document.
     * 
     * @method getContent
     * @return {Object} Content
     */
    public String getContent() {
        return this.getAsString(AtomXPath.content);
    }

    /**
     * Sets content of ATOM entry.
     * 
     * @method setContent
     * @param content {String} content
     */
    public void setContent(String content) {
        this.setAsString(AtomXPath.content, content);
    }
    
    /**
     * Gets a author from ATOM entry document.
     * 
     * @method getAuthor
     * @return {Person} author of the ATOM entry document
     */
    public Person getAuthor() {
    	if (fields.containsKey(AtomXPath.author.getName())) {
			return (Person)fields.get(AtomXPath.author.getName());
		}
		if (dataHandler != null){
	    	return new Person(getService(), new XmlDataHandler((Node)this.getDataHandler().getData(), 
	    			ConnectionsConstants.nameSpaceCtx, (XPathExpression)AtomXPath.author.getPath()));
		}
		return null;
    }

    /**
     * Gets a contributor from ATOM entry document.
     * 
     * @method getContributor
     * @return {Person} contributor of the ATOM entry document
     */
    public Person getContributor() {
    	if (fields.containsKey(AtomXPath.contributor.getName())) {
			return (Person)fields.get(AtomXPath.contributor.getName());
		}
		if (dataHandler != null){
	    	return new Person(getService(), new XmlDataHandler((Node)this.getDataHandler().getData(), 
	    			ConnectionsConstants.nameSpaceCtx, (XPathExpression)AtomXPath.contributor.getPath()));
		}
		return null;
    }

	/**
	 * Sets the contributor from ATOM entry document.
	 * 
	 * @method setContributor
	 * @param person {Person} contributor of the ATOM entry document
	 */
	public void setContributor(Person person) {
		this.setAsObject(AtomXPath.contributor, person);
	}
    
    /**
     * Return the published date from ATOM entry document.
     * 
     * @method getPublished
     * @return {Date} Published date of the ATOM entry document
     */
    public Date getPublished() {
        return this.getAsDate(AtomXPath.published);
    }

    /**
     * Return the last updated date from ATOM entry document.
     * 
     * @method getUpdated
     * @return {Date} Last updated date of the ATOM entry document
     */
    public Date getUpdated() {
        return this.getAsDate(AtomXPath.updated);
    }
    
    /**
     * Return the alternate url of the ATOM entry document.
     * 
     * @method getAlternateUrl
     * @return {String} Alternate url
     */
    public String getAlternateUrl() {
        return this.getAsString(AtomXPath.alternateUrl);
    }
            
    /**
     * Return the self url of the ATOM entry document.
     * 
     * @method getSelfUrl
     * @return {String} Self url
     */
    public String getSelfUrl() {
        return this.getAsString(AtomXPath.selfUrl);
    }
    
    /**
     * Return the edit url of the ATOM entry document.
     * 
     * @method getEditUrl
     * @return {String} Edit url
     */
    public String getEditUrl() {
        return this.getAsString(AtomXPath.editUrl);
    }

    /**
     * Create ATOM entry XML
     * 
     * @return {String}
     */
    public String createPostData() {
        return "";
    }
    
    /**
     * Return extra entry data to be included in post data for this entity.
     * 
     * @return {String}
     */
    public String createEntryData() {
    	return "";
    }
    
    /**
     * Return the list of category terms that do not have a scheme attribute
     * @return {List<String>}
     */
	public List<String> getBaseTags() {
		return getAsList(AtomXPath.tags);
	}
	
	/**
	 * Set the tags
	 * @param tags
	 */
	public void setBaseTags(List<String> tags) {
		setAsList(AtomXPath.tags, tags);
	}

	/**
	 * 
	 * @return dataHandler
	 */
	@Override
	public XmlDataHandler getDataHandler(){
		return (XmlDataHandler)dataHandler;
	}
	
	/**
	 * Return XML string for the Atom entity.
	 * 
	 * @return {String}
	 * @throws XMLException
	 */
	public String toXmlString() throws XMLException {
		XmlDataHandler dataHandler = (XmlDataHandler)getDataHandler();
		if (dataHandler != null && dataHandler.getData() != null) {
			return DOMUtil.getXMLString(dataHandler.getData());
		}
		return null;
	}
	
	protected void setAsSet(FieldEntry field, Set<String> value) {
		fields.put(field.getName(), value);
	}
	
	protected void setAsList(FieldEntry field, List<String> value) {
		fields.put(field.getName(), value);
	}
	
	@SuppressWarnings("unchecked")
	protected Set<String> getAsSet(FieldEntry field) {
		if (fields.containsKey(field.getName())){
			return (Set<String>)fields.get(field.getName());
		}
		if (dataHandler != null) {
			return new HashSet<String>(Arrays.asList((dataHandler.getAsArray(field))));
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected List<String> getAsList(FieldEntry field) {
		if (fields.containsKey(field.getName())){
			return (List<String>)fields.get(field.getName());
		}
		if (dataHandler != null) {
			return Arrays.asList((dataHandler.getAsArray(field)));
		}
		return null;
	}
	
	/**
	 * Create a link to from the specified node
	 */
	protected Link createLink(Node node, FieldEntry fieldEntry) {
		XmlDataHandler dataHandler = new XmlDataHandler(node, ConnectionsConstants.nameSpaceCtx, (XPathExpression)fieldEntry.getPath());
		if (dataHandler.getData() != null) {
			return new Link(getService(), dataHandler);
		}
		return null;
	}
		
}
