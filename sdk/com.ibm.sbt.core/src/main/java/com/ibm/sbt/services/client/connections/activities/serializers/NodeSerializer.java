/*
 * � Copyright IBM Corp. 2013
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

package com.ibm.sbt.services.client.connections.activities.serializers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespaces;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.connections.activities.DateField;
import com.ibm.sbt.services.client.connections.activities.Field;
import com.ibm.sbt.services.client.connections.activities.FileField;
import com.ibm.sbt.services.client.connections.activities.LinkField;
import com.ibm.sbt.services.client.connections.activities.NodeEntity;
import com.ibm.sbt.services.client.connections.activities.PersonField;
import com.ibm.sbt.services.client.connections.activities.TextField;

/**
 * @author mwallace
 *
 */
class NodeSerializer extends AtomEntitySerializer<NodeEntity> {
	
	private static SimpleDateFormat dateFormat = 
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");	
	
	public NodeSerializer(NodeEntity nodeEntity) {
		super(nodeEntity);
	}
	
	protected List<Element> fields() {
		Field[] fields = entity.getFields();
		if (fields == null) {
			return null;
		}
		ArrayList<Element> elements = new ArrayList<Element>();
		for (Field field : fields) {
			Element element = element(Namespaces.SNX, "field", 
				attribute("fid", field.getFid()),
				attribute("name", field.getName()),
				attribute("type", field.getType()),
				attribute("position", field.getPosition()));
			if (field instanceof DateField) {
				DateField dateField = (DateField)field;
				addText(element, DateSerializer.toString(dateFormat, dateField.getDate()));
			} else if (field instanceof FileField) {
				FileField fileField = (FileField)field;
				if (fileField.getEditMediaLink() != null) {
					Element link = element(Namespaces.ATOM, "link",
							attribute("href", fileField.getEditMediaLink().getHref()),
							attribute("type", fileField.getEditMediaLink().getType()),
							attribute("size", fileField.getEditMediaLink().getSize()));
					appendChildren(element, link);					
				}
			} else if (field instanceof LinkField) {
				LinkField linkField = (LinkField)field;
				Element link = element(Namespaces.ATOM, "link",
						attribute("href", linkField.getLink().getHref()),
						attribute("title", linkField.getLink().getTitle()));
				appendChildren(element, link);
			} else if (field instanceof PersonField) {
				PersonField personField = (PersonField)field;
				Element name = addText(element(Namespaces.ATOM, "name"), personField.getPerson().getName());
				Element email = addText(element(Namespaces.ATOM, "email"), personField.getPerson().getEmail());
				Element userid = addText(element(Namespaces.SNX, "userid"), personField.getPerson().getUserid());
				appendChildren(element, name, email, userid);
			} else if (field instanceof TextField) {
				TextField textField = (TextField)field;
				Element summary = element(Namespaces.ATOM, "summary", 
						attribute("type", "text"));
				addText(summary, textField.getSummary());
				appendChildren(element, summary);
			}
			
			elements.add(element);
		}
		
		return elements;
	}
		
	protected Element activityNodeCategory() {
		return element("category", 
				attribute("scheme", "http://www.ibm.com/xmlns/prod/sn/type"), 
				attribute("term", "activity_node"), 
				attribute("label", "Activity Node"));
	}
	
	protected Element activityUuid() {
		return textElement(Namespaces.SNX, "activity", entity.getActivityUuid());
	}
		
	@Override
	protected Element content() {
		return textElement("content", entity.getContent(), 
				attribute("type", "application/atom+xml"));
	}
	
}
