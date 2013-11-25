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

import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;

/**
 * @author Mario Duarte
 *
 */
public class XmlSerializer {
	
	private Document doc;
	
	public XmlSerializer() {
		try {
			resetDocument();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected void resetDocument() throws Exception {
		this.doc = newDocument();
	}
	
	protected Document newDocument() throws Exception {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		docFactory.setNamespaceAware(true);
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		return docBuilder.newDocument();
	}
	
	public String serializeToString() {
		DOMImplementation impl = doc.getImplementation();
		DOMImplementationLS implLS = (DOMImplementationLS) impl.getFeature("LS", "3.0");
		LSSerializer lsSerializer = implLS.createLSSerializer();
		lsSerializer.getDomConfig().setParameter("format-pretty-print", true);
		 
		LSOutput lsOutput = implLS.createLSOutput();
		lsOutput.setEncoding("UTF-8");
		Writer stringWriter = new StringWriter();
		lsOutput.setCharacterStream(stringWriter);
		lsSerializer.write(doc, lsOutput);
		
		return stringWriter.toString();
	}
	
	public void serialize(Writer writer) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(writer);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
		} 
		catch (TransformerException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String serializeToString2() {
		StringWriter writer = new StringWriter();
		serialize2(writer);
		return writer.toString();
	}
	
	public void serialize2(Writer writer) {
		try {
			DOMUtil.serialize(writer, doc, false, true);
		} catch (XMLException e) {
			throw new RuntimeException(e); 
		}
	}
	
	protected <T extends Node> Node appendChilds(String rootElementName, List<T> childs) {
		Node element = element(rootElementName);
		return appendChilds(element, childs);
	}
	
	protected <T extends Node> Node appendChilds(String rootElementName, T... childs) {
		return appendChilds(rootElementName, childs);
	}
	
	protected <T extends Node> Node appendChilds(Node root, List<T> childs) {
		if(childs != null) {
			for(Node child : childs) {
				if(child != null) {
					try {
						root.appendChild(child);
					}
					catch(DOMException e) {
						root.appendChild(importNode(child));
					}
				}
			}
		}
		return root;
	}
	
	protected Node appendChilds(Node root, Node... childs) {
		return appendChilds(root, list(childs));
	}
	
	protected Node rootNode(Node root) {
		doc.appendChild(root);
		return root;
	}
	
	protected Element element(String tagName) {
		if(StringUtil.isEmpty(tagName)) 
			throw new NullPointerException("The name of an element may not be null or empty.");
		return doc.createElement(tagName);
	}
	
	protected Element element(String tagName, Attr... attributes) {
		Element element = element(tagName);
		return addAttributes(element, attributes);
	}
	
	protected Element element(String namespaceURI, String tagName) {
		if(StringUtil.isEmpty(tagName)) 
			throw new NullPointerException("The name of an element may not be null or empty.");
		if(StringUtil.isEmpty(namespaceURI)) 
			throw new NullPointerException("The namespaceURI of an element may not be null or empty.");
		
		return doc.createElementNS(namespaceURI, tagName);
	}
	
	protected Element element(String namespaceURI, String tagName, Attr... attributes) {
		Element element = element(namespaceURI, tagName);
		return addAttributes(element, attributes);
	}
	
	protected Element textElement(String tagName, String data) {
		if(data == null) return null;
		else {
			Element element = element(tagName);
			return addText(element, data);
		}
	}
	
	protected Element textElement(String tagName, String data, Attr... attributes) {
		Element element = textElement(tagName, data);
		return addAttributes(element, attributes);
	}
	
	protected Element textElement(String namespaceURI, String tagName, String data) {
		if(data == null) return null;
		else {
			Element element = element(namespaceURI, tagName);
			return addText(element, data);
		}
	}
	
	protected Element textElement(String namespaceURI, String tagName, String data, Attr... attributes) {
		Element element = textElement(namespaceURI, tagName, data);
		return addAttributes(element, attributes);
	}
	
	protected Attr attribute(String name, String value) {
		Attr attr = doc.createAttribute(name);
		attr.setValue(value);
		return attr;
	}
	
	protected Attr attribute(String namespaceURI, String name, String value) {
		Attr attr = doc.createAttributeNS(namespaceURI, name);
		attr.setValue(value);
		return attr;
	}
	
	protected Node importNode(Node node) {
		return doc.importNode(node, true);
	}
	
	protected List<Node> list(Node... nodes) {
		return Arrays.asList(nodes);
	}
	
	protected Element addText(Element element, String data) {
		if(element == null) return null;
		if(data == null) throw new NullPointerException("The data of a text element may not be null.");
		
		Text textNode = doc.createTextNode(data);
		element.appendChild(textNode);
		return element;
	}
	
	protected Element addAttributes(Element element, Attr... attributes) {
		if(element == null) return null;
		if(attributes == null) throw new NullPointerException("The attributes may not be null.");
		
		for(Attr attr : attributes) {
			element.setAttributeNode(attr);
		}
		return element;
	}
	
	public Document getDocument() {
		return doc;
	}
	
	public Node getRootNode() {
		return doc.getDocumentElement();
	}
}
