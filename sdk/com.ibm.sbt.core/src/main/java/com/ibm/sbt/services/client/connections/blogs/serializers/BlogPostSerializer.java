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

import static com.ibm.sbt.services.client.base.ConnectionsConstants.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespace;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.connections.blogs.BlogPost;

/**
 * @author Benjamin Jakobus
 */
public class BlogPostSerializer extends AtomEntitySerializer<BlogPost> {

	public BlogPostSerializer(BlogPost blog) {
		super(blog);
	}
	
	protected void generateCreatePayload() {
		Node entry = entry();
		appendChildren(entry,
				title(),
				type(),
				summary(),
				content()
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
	

	private Element type() {
		return textElement(TYPE, entity.getType());
	}
	
	@Override
	protected Element summary() {
		return textElement(SUMMARY, entity.getSummary(), attribute(TYPE, HTML)); 
	}
	
	protected void generateUpdatePayload() {
		Node entry = genericAtomEntry();
		
		appendChildren(entry,
				title(),
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
				attribute(TYPE, HTML));
	}
}
