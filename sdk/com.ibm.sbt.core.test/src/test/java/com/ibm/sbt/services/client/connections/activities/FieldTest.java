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
package com.ibm.sbt.services.client.connections.activities;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.XResult;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

/**
 * @author mwallace
 *
 */
public class FieldTest extends BaseActivityServiceTest {

	@Test
	public void testFields() throws XMLException, IOException {
		//FIXME: Test is Broken
		/*Node doc = readXml("fields.xml");
				
		XmlDataHandler dataHandler = new XmlDataHandler(doc, nameSpaceCtx);
		List<Node> nodeFields = dataHandler.getEntries(ActivityXPath.entry_field);
		List<Field> fields = new ArrayList<Field>();
		for (Node node : nodeFields) {
			XPathExpression xpath = (node instanceof Document) ? (XPathExpression)ActivityXPath.field.getPath() : null;
			
			XPathExpression fieldType = ActivityXPath.field_type.getPath();
			XResult result = fieldType.eval(node, nameSpaceCtx);
			String type = result.getStringValue();
			if ("date".equals(type)) {
				fields.add(new DateField(activityService, node, nameSpaceCtx, xpath));
			} else if ("file".equals(type)) {
				fields.add(new FileField(activityService, node, nameSpaceCtx, xpath));
			} else if ("link".equals(type)) {
				fields.add(new LinkField(activityService, node, nameSpaceCtx, xpath));
			} else if ("person".equals(type)) {
				fields.add(new PersonField(activityService, node, nameSpaceCtx, xpath));
			} else if ("text".equals(type)) {
				fields.add(new TextField(activityService, node, nameSpaceCtx, xpath));
			}
		}
		Assert.assertEquals(5, fields.size());

		for (Field field : fields) {
			Assert.assertNotNull(field.getAsString("./@fid"));
			Assert.assertNotNull(field.getFid());
			Assert.assertNotNull(field.getName());
			Assert.assertNotNull(field.getType());
			Assert.assertNotNull(field.getPosition());
			
			String type = field.getType();
			if ("date".equals(type)) {
				DateField dateField = (DateField)field;
				Assert.assertNotNull(dateField.getDate());
			} else if ("file".equals(type)) {
				FileField fileField = (FileField)field;
				Assert.assertNotNull(fileField.getEditMediaLink());
				Assert.assertNotNull(fileField.getEditMediaLink().getHref());
				Assert.assertNotNull(fileField.getEditMediaLink().getType());
				Assert.assertNotNull(fileField.getEditMediaLink().getSize());
				Assert.assertNotNull(fileField.getEnclosureLink());
				Assert.assertNotNull(fileField.getEnclosureLink().getHref());
				Assert.assertNotNull(fileField.getEnclosureLink().getType());
				Assert.assertNotNull(fileField.getEnclosureLink().getSize());
			} else if ("link".equals(type)) {
				LinkField linkField = (LinkField)field;
				Assert.assertNotNull(linkField.getLink());
				Assert.assertNotNull(linkField.getLink().getTitle());
				Assert.assertNotNull(linkField.getLink().getHref());
			} else if ("person".equals(type)) {
				PersonField personField = (PersonField)field;
				Assert.assertNotNull(personField.getPerson());
				Assert.assertNotNull(personField.getPerson().getName());
				Assert.assertNotNull(personField.getPerson().getUserid());
				Assert.assertNotNull(personField.getPerson().getEmail());
			} else if ("text".equals(type)) {
				TextField textField = (TextField)field;
				Assert.assertNotNull(textField.getSummary());
			}
		}*/
	}

}
