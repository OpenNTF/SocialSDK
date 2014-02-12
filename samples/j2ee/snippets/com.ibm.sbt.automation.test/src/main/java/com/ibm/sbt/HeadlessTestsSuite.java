package com.ibm.sbt;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.test.js.ToolkitTestSuite;
import com.ibm.sbt.test.js.base.*;
import com.ibm.sbt.test.js.connections.communities.api.GetMyCommunities;
import com.ibm.sbt.test.js.sbt.EndpointDelete;
import com.ibm.sbt.test.js.sbt.EndpointGet;
import com.ibm.sbt.test.js.sbt.EndpointPost;
import com.ibm.sbt.test.js.sbt.EndpointPut;
import com.ibm.sbt.test.js.sbt.LangMixin;
import com.ibm.sbt.test.js.sbt.PromiseChain;
import com.ibm.sbt.test.js.sbt.RequireModules;
import com.ibm.sbt.test.js.sbt.ResponseHeaders;

@RunWith(Suite.class)
@SuiteClasses({ 
	AtomEntity.class, BaseEntity.class, BaseService.class, BaseServiceConstructUrl.class, 
	BaseServiceEndpoint.class, BaseServiceGetEntities.class, BaseServiceValidation.class,
	CommunityEntryDataHandler.class, CommunityFeedDataHandler.class, CommunityInvitesFeedDataHandler.class,
	ConnectionsServices.class, XmlDataHandlerDate.class, GetMyCommunities.class,
	EndpointDelete.class, 
	EndpointGet.class, 
	EndpointPut.class, 
	EndpointPost.class, 
	LangMixin.class, 
	ResponseHeaders.class,
	RequireModules.class,
	PromiseChain.class
})
public class HeadlessTestsSuite {

}
