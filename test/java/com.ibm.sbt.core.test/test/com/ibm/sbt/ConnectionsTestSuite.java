package com.ibm.sbt;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.services.client.connections.communities.CommunityServiceTest;

@RunWith(Suite.class)
@SuiteClasses({CommunityServiceTest.class})
public class ConnectionsTestSuite {

}
