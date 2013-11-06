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

package com.ibm.sbt.services.client.connections.forums;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.connections.forums.feedhandler.TagFeedHandler;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

/**
* @Represents Forum Tags
* @author Swati Singh
*/
public class TagList extends EntityList<Tag> {

	public TagList(Response requestData, TagFeedHandler feedHandler) {
		super(requestData, feedHandler);
	}

	@Override
	public Document getData(){
		return (Document)super.getData();
	}

	@Override
	public ForumService getService() {
		return (ForumService)super.getService();
	}

	@Override
	public TagFeedHandler getFeedHandler() {
		return (TagFeedHandler)super.getFeedHandler();
	}

	@Override
	protected Tag getEntity(Object data){
		return (Tag)super.getEntity(data);
	}

	@Override
	protected ArrayList<Tag> createEntities() {
		XmlDataHandler dataHandler = new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
		ArrayList<Tag> tags = new ArrayList<Tag>();
		List<Node> entries = dataHandler.getEntries(ForumsXPath.tagEntry);
		for (Node node: entries) {
			Tag tag = getEntity(node);
			tags.add(tag);
		}
		return tags;
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