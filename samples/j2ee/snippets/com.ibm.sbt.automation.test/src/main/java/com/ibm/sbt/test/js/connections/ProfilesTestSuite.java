/*
 * © Copyright IBM Corp. 2013
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

import com.ibm.sbt.test.js.connections.profiles.api.CheckProfileCache;
import com.ibm.sbt.test.js.connections.profiles.api.CreateProfile;
import com.ibm.sbt.test.js.connections.profiles.api.DeleteProfile;
import com.ibm.sbt.test.js.connections.profiles.api.GetCachedProfile;
import com.ibm.sbt.test.js.connections.profiles.api.GetColleagues;
import com.ibm.sbt.test.js.connections.profiles.api.GetPeopleManaged;
import com.ibm.sbt.test.js.connections.profiles.api.GetProfile;
import com.ibm.sbt.test.js.connections.profiles.api.GetProfileDemonstrationSnippet;
import com.ibm.sbt.test.js.connections.profiles.api.GetReportingChain;
import com.ibm.sbt.test.js.connections.profiles.api.ProfileEntryDataHandler;
import com.ibm.sbt.test.js.connections.profiles.api.ProfileEntryHCardFull;
import com.ibm.sbt.test.js.connections.profiles.api.ProfileEntryHCardLite;
import com.ibm.sbt.test.js.connections.profiles.api.ProfileEntryVCardFull;
import com.ibm.sbt.test.js.connections.profiles.api.ProfileEntryVCardLite;
import com.ibm.sbt.test.js.connections.profiles.api.ProfileFeedDataHandler;
import com.ibm.sbt.test.js.connections.profiles.api.Search;
import com.ibm.sbt.test.js.connections.profiles.api.UpdateProfile;
import com.ibm.sbt.test.js.connections.profiles.api.CreateAndDeleteProfile;
import com.ibm.sbt.test.js.connections.profiles.api.UpdateProfileDemonstrationSnippet;

/**
 * @author mwallace
 * 
 * @date 6 Mar 2013
 */
@RunWith(Suite.class)
@SuiteClasses({ 
    ProfileEntryDataHandler.class, 
    ProfileFeedDataHandler.class, 
    ProfileEntryVCardFull.class, 
    ProfileEntryVCardLite.class, 
    ProfileEntryHCardFull.class, 
    ProfileEntryHCardLite.class,
    GetProfile.class,
    GetCachedProfile.class,
    GetColleagues.class,
    GetPeopleManaged.class,
    GetReportingChain.class,
    CreateProfile.class,
    DeleteProfile.class,
    UpdateProfile.class,
    CreateAndDeleteProfile.class,
    Search.class,
    GetProfileDemonstrationSnippet.class,
    UpdateProfileDemonstrationSnippet.class,
    CheckProfileCache.class
    })
public class ProfilesTestSuite {
    @AfterClass
    public static void cleanup() {
    }
}
