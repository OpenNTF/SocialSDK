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

package com.ibm.sbt.services.client.connections.activity;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.activity.feedHandler.DateFieldFeedHandler;
import com.ibm.sbt.services.client.connections.activity.feedHandler.FieldFeedHandler;
import com.ibm.sbt.services.client.connections.activity.feedHandler.FileFieldFeedHandler;
import com.ibm.sbt.services.client.connections.activity.feedHandler.PersonFieldFeedHandler;
import com.ibm.sbt.services.client.connections.activity.feedHandler.TextFieldFeedHandler;
import com.ibm.sbt.services.client.connections.activity.model.ActivityXPath;

/**
* @Represents Activity Field List
* @author Vimal Dhupar
*/
public class FieldList extends EntityList<Field> {

	public FieldList(Response requestData, FieldFeedHandler feedHandler) {
		super(requestData, feedHandler);
	}

	@Override
	public Document getData(){
		return (Document)super.getData();
	}

	@Override
	public ActivityService getService() {
		return (ActivityService)super.getService();
	}

	@Override
	public FieldFeedHandler getFeedHandler() {
		return (FieldFeedHandler)super.getFeedHandler();
	}

	@Override
	protected Field getEntity(Object data){
		return (Field)super.getEntity(data);
	}

	@Override
	protected ArrayList<Field> createEntities() {
		XmlDataHandler dataHandler = new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
		ArrayList<Field> fields = new ArrayList<Field>();
		FieldEntry path = ActivityXPath.field;
		if(getFeedHandler() instanceof TextFieldFeedHandler) { 
			path = ActivityXPath.textField;
		} else if (getFeedHandler() instanceof DateFieldFeedHandler) {
			path = ActivityXPath.dateField;
		} else if (getFeedHandler() instanceof FileFieldFeedHandler) {
			path = ActivityXPath.fileField;
		} else if (getFeedHandler() instanceof PersonFieldFeedHandler) {
			path = ActivityXPath.personField;
		} else {
			path = ActivityXPath.bookmarkField;
		}
			
		List<Node> entries = dataHandler.getEntries(path);
		for (Node node: entries) {
			Field field = getEntity(node);
			fields.add(field);
		}
		return fields;
	}

	@Override
	public int getTotalResults() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStartIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getItemsPerPage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCurrentPage() {
		// TODO Auto-generated method stub
		return 0;
	}

}