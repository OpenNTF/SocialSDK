package com.ibm.sbt.services.client.connections.profiles;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.ConnectionsFeedXpath;
import com.ibm.sbt.services.client.connections.profiles.feedhandler.ProfileFeedHandler;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

public class ProfileList extends EntityList<Profile> {

	public ProfileList(Response requestData, ProfileFeedHandler feedHandler) {
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
	public ProfileFeedHandler getFeedHandler() {
		return (ProfileFeedHandler)super.getFeedHandler();
	}
	
	@Override
	protected Profile getEntity(Object data){
		return (Profile)super.getEntity(data);
	}
	
	@Override
	protected ArrayList<Profile> createEntities() {
		XmlDataHandler dataHandler = new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
		ArrayList<Profile> profiles = new ArrayList<Profile>();
		List<Node> entries = dataHandler.getEntries(ConnectionsFeedXpath.Entry);
		for (Node node: entries) {
			Profile profile = getEntity(node);
			profiles.add(profile);
		}
		return profiles;
	}
	
	private XmlDataHandler getMetaDataHandler(){
		return new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
	}

	@Override
	public int getTotalResults() {
		return getMetaDataHandler().getAsInt(ConnectionsFeedXpath.TotalResults);
	}

	@Override
	public int getStartIndex() {
		return getMetaDataHandler().getAsInt(ConnectionsFeedXpath.StartIndex);
	}

	@Override
	public int getItemsPerPage() {
		return getMetaDataHandler().getAsInt(ConnectionsFeedXpath.ItemsPerPage);
	}

	@Override
	public int getCurrentPage() {
		return getMetaDataHandler().getAsInt(ConnectionsFeedXpath.CurrentPage);
	}
}
