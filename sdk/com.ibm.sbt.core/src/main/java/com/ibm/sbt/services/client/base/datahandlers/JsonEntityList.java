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

package com.ibm.sbt.services.client.base.datahandlers;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.ConnectionsFeedXpath;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.JsonEntity;

/**
 * @author mwallace
 *
 */
public class JsonEntityList<T extends JsonEntity> extends EntityList<T> {
	
	private String entitiesPath;

	public JsonEntityList(Response requestData, IFeedHandler<T> feedHandler, String entitiesPath) {
		this.entitiesPath = entitiesPath;
        
		init(requestData, feedHandler);
	}
	
	@Override
	protected List<T> createEntities() {
		ArrayList<T> entries = new ArrayList<T>();
		List<JsonJavaObject> jsonObjects = getDataHandler().getEntries(entitiesPath);
		for (JsonJavaObject jsonObject: jsonObjects) {
			entries.add((T)super.getEntity(jsonObject));
		}
		return entries;
	}
	
	@Override
	public JsonJavaObject getData(){
		return (JsonJavaObject)super.getData();
	}

	protected JsonDataHandler getDataHandler() {
		return new JsonDataHandler(getData());
	}
	
	@Override
	public int getTotalResults() {
		return -1;
	}

	@Override
	public int getStartIndex() {
		return -1;
	}

	@Override
	public int getItemsPerPage() {
		return -1;
	}

	@Override
	public int getCurrentPage() {
		return -1;
	}
}
