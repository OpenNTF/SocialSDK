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
package com.ibm.sbt.services.client.connections.activity.feedHandler;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.activity.ActivityService;
import com.ibm.sbt.services.client.connections.activity.Field;
import com.ibm.sbt.services.client.connections.activity.TextField;
import com.ibm.sbt.services.client.connections.activity.model.ActivityXPath;

/**
 * Feed Handler for Text Fields
 * @author Vimal Dhupar
 *
 */
public class TextFieldFeedHandler extends FieldFeedHandler implements IFeedHandler {

	/**
	 * Constructor
	 *
	 * @param service
	 */
	public TextFieldFeedHandler(ActivityService service){
		super(service);
	}

	/**
	 * @param data object
	 * @return Field
	 */
	@Override
	public Field createEntityFromData(Object data) {
		Node node = (Node)data;

		XPathExpression expr = (data instanceof Document) ? (XPathExpression)ActivityXPath.textField.getPath() : null;
		XmlDataHandler handler = new XmlDataHandler(node, ConnectionsConstants.nameSpaceCtx, expr);
		Field field = new TextField(service, handler);
		return field;
	}
}