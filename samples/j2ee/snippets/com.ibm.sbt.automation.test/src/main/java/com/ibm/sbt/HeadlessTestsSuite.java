package com.ibm.sbt;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.test.js.base.*;
import com.ibm.sbt.test.js.connections.communities.api.GetMyCommunities;

@RunWith(Suite.class)
@SuiteClasses({ 
	AtomEntity.class, BaseEntity.class, BaseService.class, BaseServiceConstructUrl.class, 
	BaseServiceEndpoint.class, BaseServiceGetEntities.class, BaseServiceValidation.class,
	CommunityEntryDataHandler.class, CommunityFeedDataHandler.class, CommunityInvitesFeedDataHandler.class,
	ConnectionsServices.class, XmlDataHandlerDate.class, GetMyCommunities.class
})
public class HeadlessTestsSuite {

}
