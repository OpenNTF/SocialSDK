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

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.ConnectionsFeedXpath;
import com.ibm.sbt.services.client.base.IFeedHandler;

/**
 * @author mwallace
 *
 */
public class CategoryEntityList<T extends BaseEntity> extends EntityList<T> {

	public CategoryEntityList(Response requestData, IFeedHandler<T> feedHandler) {
		super(requestData, feedHandler);
	}
	
	@Override
	protected List<T> createEntities() {
		ArrayList<T> entries = new ArrayList<T>();
		List<Node> nodeEntries = getDataHandler().getEntries(ConnectionsFeedXpath.Category);
		for (Node node: nodeEntries) {
			entries.add((T)super.getEntity(node));
		}
		return entries;
	}
	
	@Override
	public Document getData(){
		return (Document)super.getData();
	}

	protected XmlDataHandler getDataHandler() {
		return new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
	}
	
	@Override
	public int getTotalResults() {
		return getDataHandler().getAsInt(ConnectionsFeedXpath.TotalResults);
	}

	@Override
	public int getStartIndex() {
		return getDataHandler().getAsInt(ConnectionsFeedXpath.StartIndex);
	}

	@Override
	public int getItemsPerPage() {
		return getDataHandler().getAsInt(ConnectionsFeedXpath.ItemsPerPage);
	}

	@Override
	public int getCurrentPage() {
		return getDataHandler().getAsInt(ConnectionsFeedXpath.CurrentPage);
	}
}
