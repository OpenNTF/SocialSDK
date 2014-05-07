/*
 * © Copyright IBM Corp. 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.ibm.sbt.services.client.connections;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.services.client.connections.activities.ActivityCrudrTest;
import com.ibm.sbt.services.client.connections.activities.ActivityDescendantsSinceTest;
import com.ibm.sbt.services.client.connections.activities.ActivityFeedTest;
import com.ibm.sbt.services.client.connections.activities.ActivityFieldCrudrTest;
import com.ibm.sbt.services.client.connections.activities.ActivityMemberArudTest;
import com.ibm.sbt.services.client.connections.activities.ActivityNodeCrudrTest;
import com.ibm.sbt.services.client.connections.activities.ActivityPriorityTest;
import com.ibm.sbt.services.client.connections.activities.ActivityTest;
import com.ibm.sbt.services.client.connections.activities.ActivityUrlsTest;
import com.ibm.sbt.services.client.connections.activities.CreateActivitiesTest;
import com.ibm.sbt.services.client.connections.activities.GetActivityDescendantsTest;
import com.ibm.sbt.services.client.connections.activities.GetMyActivitiesTest;
import com.ibm.sbt.services.client.connections.activities.PublicActivityTest;

/**
 * @author mwallace
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	ActivityUrlsTest.class,
	ActivityCrudrTest.class,
	ActivityDescendantsSinceTest.class,
	ActivityFeedTest.class,
	ActivityFieldCrudrTest.class,
	ActivityNodeCrudrTest.class,
	ActivityMemberArudTest.class,
	ActivityPriorityTest.class,
	ActivityTest.class,
	CreateActivitiesTest.class,
	GetMyActivitiesTest.class,
	PublicActivityTest.class,
	GetActivityDescendantsTest.class
})
public class ActivitiesTestSuite {
}
