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

package com.ibm.sbt.services.client.base.serializers;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.AUTHOR;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.CATEGORY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.CONTENT;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.CONTRIBUTOR;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.ENTRY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.FEED;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.ID;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SNXUSERID;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.LABEL;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.PUBLISHED;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SCHEME;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SUBTITLE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SUMMARY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TERM;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TEXT;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TITLE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TYPE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.UPDATED;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.dateFormat;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespace;

/**
 * @author Mario Duarte
 * @author Carlos Manias
 *
 */
public class AtomEntitySerializer<T extends AtomEntity> extends BaseEntitySerializer<T> {

	public AtomEntitySerializer(T entity) {
		super(entity);
	}
	
	public Node genericAtomEntry() {
		Node entry = entry();
		
		appendChildren(entry, 
			title(),
			id(),
			summary(),
			content()
		);
		
		appendChildren(entry, tags());

		return entry;
	}

	/**
	 * 
	 * @return A Root node entry element
	 */
	protected Node entry() {
		return rootNode(element(Namespace.ATOM.getUrl(), ENTRY));
	}

	/**
	 * 
	 * @return An Entry element that is not the root element and can be used inside a Feed
	 */
	protected Node entryElement() {
		return element(ENTRY);
	}

	protected Node feed() {
		return rootNode(element(Namespace.ATOM.getUrl(), FEED));
	}
	
	protected Element title() {
		return textElement(TITLE, entity.getTitle(), attribute(TYPE, TEXT));
	}
	
	protected Element id() {
		return textElement(ID, entity.getId());
	}
	
	protected Element published() {
		return textElement(PUBLISHED, DateSerializer.toString(dateFormat, entity.getPublished()));
	}
	
	protected Element updated() {
		return textElement(UPDATED, DateSerializer.toString(dateFormat, entity.getUpdated()));
	}
	
	protected Element summary() {
		return textElement(SUMMARY, entity.getSummary(), attribute(TYPE, TEXT)); 
	}
	
	protected Element subtitle() {
		return textElement(SUBTITLE, entity.getSubtitle(), attribute(TYPE, TEXT)); 
	}
	
	protected Element content() {
		return textElement(CONTENT, entity.getContent(), attribute(TYPE, entity.getContentType()));
	}
	
	protected Node author() {
		return (new PersonSerializer(entity.getAuthor())).xmlNode(AUTHOR);
	}
	
	protected Node contributor() {
		return (new PersonSerializer(entity.getContributor())).xmlNode(CONTRIBUTOR);
	}
	
	protected Node snxUserID(){
		return element(SNXUSERID, entity.getId());
	}

	/**
	 * 
	 * @return A contributor element with no sub elements
	 */
	protected Node contributorElement(){
		return element(CONTRIBUTOR);
	}

	protected Element category(String scheme, String term) {
		return element(CATEGORY, 
				attribute(SCHEME, scheme), 
				attribute(TERM, term));
	}

	protected Element categoryType(String type) {
		return element(CATEGORY, 
				attribute(SCHEME, Namespace.TAG.getUrl()), 
				attribute(TERM, type), 
				attribute(LABEL, type));
	}
	
	protected List<Element> tags() {
		List<Element> elements = new ArrayList<Element>();
		if(entity.getBaseTags() != null) {
			List<String> list = entity.getBaseTags();
			for(String tag : list) {
				elements.add(element(CATEGORY, 
						attribute(TERM, tag),
						attribute(LABEL, tag)));
			}
		}
		return elements;
	}
}
