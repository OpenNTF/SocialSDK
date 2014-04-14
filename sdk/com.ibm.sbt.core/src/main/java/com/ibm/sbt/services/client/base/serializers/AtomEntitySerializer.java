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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespace;
import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespaces;

/**
 * @author Mario Duarte
 * @author Carlos Manias
 *
 */
public class AtomEntitySerializer<T extends AtomEntity> extends BaseEntitySerializer<T> {

	protected static final String COMMA = ",";
	protected static final String SEMICOLON = ";";
	protected static final String COLON = ":";
	protected static final String ENTRY = "entry";
	protected static final String TAGS = "tags";
	protected static final String CATEGORY = "category";
	protected static final String SCHEME = "scheme";
	protected static final String TERM = "term";
	protected static final String CONTENT = "content";
	protected static final String TYPE = "type";
	protected static final String APPLICATION_XML = "application/xml";
	protected static final String TEXT = "text";
	protected static final String HTML = "html";
	protected static final String KEY = "key";
	protected static final String VALUE = "value";
	protected static final String DATA = "data";
	protected static final String TITLE = "title";
	protected static final String ID = "id";
	protected static final String PUBLISHED = "published";
	protected static final String UPDATED = "updated";
	protected static final String SUMMARY = "summary";
	protected static final String AUTHOR = "author";
	protected static final String CONTRIBUTOR = "contributor";
	protected static final String LABEL = "label";

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

	protected Node entry() {
		return rootNode(element(Namespaces.ATOM, ENTRY));
	}
	
	protected Element title() {
		return textElement(TITLE, entity.getTitle(), attribute(TYPE, TEXT));
	}
	
	protected Element id() {
		return textElement(ID, entity.getId());
	}
	
	protected Element published() {
		return textElement(PUBLISHED, DateSerializer.toString(entity.getPublished()));
	}
	
	protected Element updated() {
		return textElement(UPDATED, DateSerializer.toString(entity.getUpdated()));
	}
	
	protected Element summary() {
		return textElement(SUMMARY, entity.getSummary(), attribute(TYPE, TEXT)); }
	
	protected Element content() {
		return textElement(CONTENT, entity.getContent(), attribute(TYPE, TEXT));
	}
	
	protected Node author() {
		return (new PersonSerializer(entity.getAuthor())).xmlNode(AUTHOR);
	}
	
	protected Node contributor() {
		return (new PersonSerializer(entity.getContributor())).xmlNode(CONTRIBUTOR);
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
