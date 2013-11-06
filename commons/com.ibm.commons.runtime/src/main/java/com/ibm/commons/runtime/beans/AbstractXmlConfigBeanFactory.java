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

package com.ibm.commons.runtime.beans;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.DOMUtil;


/**
 * SBT ManagedBean Factory.
 *
 * This class creates the managed beans when necessary.
 *  
 * @author Philippe Riand
 */
public class AbstractXmlConfigBeanFactory extends AbstractBeanFactory {

	public AbstractXmlConfigBeanFactory() {
	}
	
	
	/**
	 * 
  		<managed-bean>
    		<managed-bean-name>aaaaa</managed-bean-name>
    		<managed-bean-class>bbbbb</managed-bean-class>
    		<managed-bean-scope>ccccc</managed-bean-scope>
			<managed-property>
				<property-name>xxxx</property-name>
				<value>yyyy</value>
			</managed-property>
  		</managed-bean>
  	 *	
	 * @param fileName
	 * @return
	 */
	public static Factory[] readFactories(InputStream is) {
		if(is==null) {
			return null;
		}
		try {
			Document doc = DOMUtil.createDocument(is);
			return readFactories(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Factory[] readFactories(Document doc) {
		List<Factory> factories = new ArrayList<Factory>();
		readFactories(factories, doc.getDocumentElement());
		return factories.toArray(new Factory[factories.size()]);
	}
	
	private static void readFactories(List<Factory> factories, Element root) {
		NodeList l = DOMUtil.getChildElementsByTagName(root, "managed-bean");
		if(l!=null) {
			for(int i=0; i<l.getLength(); i++) {
				Node n = l.item(i);
				if(n.getNodeType()==Node.ELEMENT_NODE) {
					Factory f = readFactory((Element)n);
					factories.add(f);
				}
			}
		}
	}
	
	private static Factory readFactory(Element root) {
		Element eName=null, eClassName=null, eScope=null;
		List<BeanProperty> properties = null;
		NodeList l = root.getChildNodes();
		if(l!=null) {
			for(int i=0; i<l.getLength(); i++) {
				Node n = l.item(i);
				if(n.getNodeType()==Node.ELEMENT_NODE) {
					if(n.getNodeName().equals("managed-bean-name")) {
						if(eName!=null) {
							throw new IllegalArgumentException("managed-bean-name is specified twice for the same bean");
						}
						eName = (Element)n;
					} else if(n.getNodeName().equals("managed-bean-class")) {
						if(eClassName!=null) {
							throw new IllegalArgumentException("managed-bean-class is specified twice for the same bean");
						}
						eClassName = (Element)n;
					} else if(n.getNodeName().equals("managed-bean-scope")) {
						if(eScope!=null) {
							throw new IllegalArgumentException("managed-bean-scope is specified twice for the same bean");
						}
						eScope = (Element)n;
					} else if(n.getNodeName().equals("managed-property")) {
						if(properties==null) {
							properties = new ArrayList<BeanProperty>();
						}
						BeanProperty p = readProperty((Element)n);
						properties.add(p);
					} else {
						throw new IllegalArgumentException(StringUtil.format("Invalid element {0} in bean definition",n.getNodeName()));
					}
				}
			}
		}

		String name = getText(eName);
		if(StringUtil.isEmpty(name)) {
			throw new IllegalArgumentException("Missing managed-bean-name in bean property definition");
		}
		String className = getText(eClassName);
		if(StringUtil.isEmpty(className)) {
			throw new IllegalArgumentException("Missing managed-bean-class in bean property definition");
		}
		String scope = getText(eScope);
		if(StringUtil.isEmpty(scope)) {
			throw new IllegalArgumentException("Missing managed-bean-scope in bean property definition");
		}
		int iscope = Context.SCOPE_NONE;
		if(scope.endsWith("global")) {
			iscope = Context.SCOPE_GLOBAL;
		} else if(scope.endsWith("application")) {
			iscope = Context.SCOPE_APPLICATION;
		} else if(scope.endsWith("session")) {
			iscope = Context.SCOPE_SESSION;
		} else if(scope.endsWith("request")) {
			iscope = Context.SCOPE_REQUEST;
		} else {
			throw new IllegalArgumentException("Invalid managed-bean-scope value in bean property definition");
		}
		
		BeanProperty[] p = properties!=null ? properties.toArray(new BeanProperty[properties.size()]) : null;
		return new Factory(iscope, name, className, p);
	}

	private static BeanProperty readProperty(Element root) {
		Element eName=null, eClassName=null, eValue=null;
		NodeList l = root.getChildNodes();
		if(l!=null) {
			for(int i=0; i<l.getLength(); i++) {
				Node n = l.item(i);
				if(n.getNodeType()==Node.ELEMENT_NODE) {
					if(n.getNodeName().equals("property-name")) {
						if(eName!=null) {
							throw new IllegalArgumentException("property-name is specified twice for the same bean property");
						}
						eName = (Element)n;
					} else if(n.getNodeName().equals("property-class")) {
						if(eClassName!=null) {
							throw new IllegalArgumentException("property-class is specified twice for the same bean property");
						}
						eClassName = (Element)n;
					} else if(n.getNodeName().equals("value")) {
						if(eValue!=null) {
							throw new IllegalArgumentException("value is specified twice for the same bean property");
						}
						eValue = (Element)n;
					} else {
						throw new IllegalArgumentException(StringUtil.format("Invalid element {0} in bean definition",n.getNodeName()));
					}
				}
			}
		}
		String name = getText(eName);
		if(StringUtil.isEmpty(name)) {
			throw new IllegalArgumentException("Missing property-name in bean property definition");
		}
		//String className = getText(eClassName); // We ignore this property for now..
		String value = getText(eValue);
		return new BeanProperty(name, value);
	}
	
	private static String getText(Element e) {
		if(e!=null) {
			NodeList l = e.getChildNodes();
			if(l!=null && l.getLength()==1) {
				Node child = l.item(0);
	            if(child.getNodeType()==Node.TEXT_NODE || child.getNodeType()==Node.CDATA_SECTION_NODE) {
	                return child.getNodeValue().trim();
	            }
			}
		}
		return null;
	}
}
