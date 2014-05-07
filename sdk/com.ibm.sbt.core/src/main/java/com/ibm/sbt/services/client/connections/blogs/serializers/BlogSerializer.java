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

package com.ibm.sbt.services.client.connections.blogs.serializers;

import static com.ibm.sbt.services.client.base.CommonConstants.APPLICATION_ATOM_XML;
import static com.ibm.sbt.services.client.base.CommonConstants.COMMA;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.CATEGORY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.CONTENT;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.ENTRY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TAGS;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TERM;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TYPE;
import static com.ibm.sbt.services.client.connections.blogs.BlogConstants.BLOG_HANDLE;
import static com.ibm.sbt.services.client.connections.blogs.BlogConstants.TIMEZONE;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.CATEGORIES;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespace;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.connections.blogs.Blog;

/**
 * @author Benjamin Jakobus
 */
public class BlogSerializer extends AtomEntitySerializer<Blog> {

	public BlogSerializer(Blog blog) {
		super(blog);
	}
	
	protected void generateCreatePayload() {
		Node entry = entry();
		appendChildren(entry,
				title(),
				timeZone(),
				handle()
		);
		appendChildren(entry, tags());
	}
	
	@Override
	protected Node entry() {
		Element element = element(Namespace.ATOM.getUrl(), ENTRY);
		element.setAttributeNS(Namespace.XMLNS.getUrl(), Namespace.APP.getNSPrefix(), Namespace.APP.getUrl());
		element.setAttributeNS(Namespace.XMLNS.getUrl(), Namespace.THR.getNSPrefix(), Namespace.THR.getUrl());
		element.setAttributeNS(Namespace.XMLNS.getUrl(), Namespace.SNX.getNSPrefix(), Namespace.SNX.getUrl());
		Node root = rootNode(element);
		return root;
	}
	
	protected Element categoryTag(String tagTerm) {
		return element(Namespace.ATOM.getUrl(), CATEGORY, 
				attribute(TERM, tagTerm));
	}

	private Element timeZone() {
		return textElement(TIMEZONE, entity.getTimeZone());
	}
	
	public String tagsPayload(){
		generateTagsPayload();
		return serializeToString();
	}

	private Element handle() {
		return textElement(BLOG_HANDLE, entity.getHandle());
	}

	
	protected void generateTagsPayload() {
		String[] tags = entity.getAsString(TAGS).split(COMMA);
		rootNode(categories(tags));
	}
	
	protected Element categories(String[] tags) {
		Element element = element(Namespace.APP.getUrl(), CATEGORIES);
		element.setAttributeNS(Namespace.XMLNS.getUrl(), Namespace.ATOM.getNSPrefix(), Namespace.ATOM.getUrl());
		appendChildren(element, 
				tagElements(tags));
		return element;
	}
	
	protected Element[] tagElements(String[] tags){
		Element[] elements = new Element[tags.length];
		int i = 0;
		for (String tag : tags) {
			elements[i++] = categoryTag(tag);
		}
		return elements;
	}
	
	protected void generateUpdatePayload() {
		Node entry = genericAtomEntry();
		
		appendChildren(entry,
				title(),
				timeZone(),
				handle(),
				author()
		);
	}
	
	public String createPayload(){
		generateCreatePayload();
		return serializeToString();
	}

	public String updatePayload(){
		generateUpdatePayload();
		return serializeToString();
	}
	
	@Override
	protected Element content() {
		return textElement(CONTENT, entity.getContent(), 
				attribute(TYPE, APPLICATION_ATOM_XML));
	}
}
