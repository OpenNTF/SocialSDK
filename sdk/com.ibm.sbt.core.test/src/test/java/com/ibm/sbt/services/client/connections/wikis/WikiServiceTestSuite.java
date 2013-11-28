package com.ibm.sbt.services.client.connections.wikis;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	WikiCreateAndDeleteTest.class, 
	WikiFeedHandlerTest.class,
	WikiGetAndUpdate.class
})
public class WikiServiceTestSuite {

}
