package com.ibm.sbt.services.client.connections.profiles;

import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.AtomEntityList;


public class ProfileList extends AtomEntityList<Profile> {

	public ProfileList(Response requestData, IFeedHandler<Profile> feedHandler) {
		super(requestData, feedHandler);
	}

}
