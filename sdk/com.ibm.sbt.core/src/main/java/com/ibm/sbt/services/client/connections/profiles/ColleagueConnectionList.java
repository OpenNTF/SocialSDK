package com.ibm.sbt.services.client.connections.profiles;

import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.AtomEntityList;


public class ColleagueConnectionList extends AtomEntityList<ColleagueConnection> {

	public ColleagueConnectionList(Response requestData, IFeedHandler<ColleagueConnection> feedHandler) {
		super(requestData, feedHandler);
	}

}
