package com.ibm.sbt.services.client.connections.profiles;

import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.AtomEntityList;


public class TagList extends AtomEntityList<Tag> {

	public TagList(Response requestData, IFeedHandler<Tag> feedHandler) {
		super(requestData, feedHandler);
	}

}
