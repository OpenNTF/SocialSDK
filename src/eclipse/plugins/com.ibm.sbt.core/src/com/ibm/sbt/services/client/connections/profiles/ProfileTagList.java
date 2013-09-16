package com.ibm.sbt.services.client.connections.profiles;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.connections.profiles.feedhandlers.ProfileTagFeedHandler;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

public class ProfileTagList extends EntityList<ProfileTag> {

	public ProfileTagList(Response requestData, ProfileTagFeedHandler feedHandler) {
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
	public ProfileTagFeedHandler getFeedHandler() {
		return (ProfileTagFeedHandler)super.getFeedHandler();
	}
	
	@Override
	protected ProfileTag getEntity(Object data){
		return (ProfileTag)super.getEntity(data);
	}
	
	@Override
	protected ArrayList<ProfileTag> createEntities() {
		XmlDataHandler dataHandler = new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
		ArrayList<ProfileTag> tags = new ArrayList<ProfileTag>();
		List<Node> entries = dataHandler.getEntries(ProfileXPath.tagEntry);
		for (Node node: entries) {
			ProfileTag tag = getEntity(node);
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
