package com.ibm.sbt.services.client.connections.profiles;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.connections.profiles.feedhandlers.TagFeedHandler;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

public class TagList extends EntityList<Tag> {

	public TagList(Response requestData, TagFeedHandler feedHandler) {
		super(requestData, feedHandler);
	}

	@Override
	public Document getData(){
		return (Document)super.getData();
	}

	@Override
	public ProfileService getService() {
		return (ProfileService)super.getService();
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
		List<Node> entries = dataHandler.getEntries(ProfileXPath.tagEntry);
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