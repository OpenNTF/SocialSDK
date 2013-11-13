/*
 * � Copyright IBM Corp. 2013
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
package com.ibm.sbt.test.js.connections;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.test.js.connections.activitystreams.api.GetMyActionableView;
import com.ibm.sbt.test.js.connections.activitystreams.api.GetMySavedView;
import com.ibm.sbt.test.js.connections.activitystreams.api.GetMyStatusUpdates;
import com.ibm.sbt.test.js.connections.activitystreams.api.GetNotificationsForMe;
import com.ibm.sbt.test.js.connections.activitystreams.api.GetNotificationsFromMe;
import com.ibm.sbt.test.js.connections.activitystreams.api.GetPublicActivityStream;
import com.ibm.sbt.test.js.connections.activitystreams.api.GetResponsesToMyContent;
import com.ibm.sbt.test.js.connections.activitystreams.api.GetUpdatesFromACommunity;
import com.ibm.sbt.test.js.connections.activitystreams.api.GetUpdatesFromAUser;
import com.ibm.sbt.test.js.connections.activitystreams.api.GetUpdatesFromCommunitiesIFollow;
import com.ibm.sbt.test.js.connections.activitystreams.api.GetUpdatesFromMyNetwork;
import com.ibm.sbt.test.js.connections.activitystreams.api.GetUpdatesFromPeopleIFollow;
import com.ibm.sbt.test.js.connections.activitystreams.api.PostAStatusUpdate;
import com.ibm.sbt.test.js.connections.activitystreams.api.PostEntry;
import com.ibm.sbt.test.js.connections.activitystreams.api.PostEntryIntoACommunityStream;
import com.ibm.sbt.test.js.connections.activitystreams.api.PostEntryWithEmbeddedExperience;
import com.ibm.sbt.test.js.connections.activitystreams.api.SearchByFilters;
import com.ibm.sbt.test.js.connections.activitystreams.api.SearchByQuery;
import com.ibm.sbt.test.js.connections.activitystreams.api.SearchByTags;

/**
 * @author rajmeetbal
 * 
 * @date 6 Mar 2013
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	PostEntry.class,
	PostEntryIntoACommunityStream.class,
	PostAStatusUpdate.class,
	PostEntryWithEmbeddedExperience.class,
//	GetMyActionableView.class,
	GetMySavedView.class,
	GetMyStatusUpdates.class,
//	GetNotificationsForMe.class,
//	GetNotificationsFromMe.class,
	GetPublicActivityStream.class,
//	GetResponsesToMyContent.class,
	GetUpdatesFromACommunity.class,
	GetUpdatesFromAUser.class,
	GetUpdatesFromCommunitiesIFollow.class,
//	GetUpdatesFromMyNetwork.class,
	GetUpdatesFromPeopleIFollow.class,
//	SearchByFilters.class,
//	SearchByTags.class,
//	SearchByQuery.class
})
public class ActivitiesStreamsTestSuite {
    @AfterClass
    public static void cleanup() {
    }
}
