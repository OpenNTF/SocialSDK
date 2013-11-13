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
package com.ibm.sbt.test.java.connections;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.automation.core.environment.TestEnvironment;
import com.ibm.sbt.test.java.connections.profiles.GetAbout;
import com.ibm.sbt.test.java.connections.profiles.GetDisplayName;
import com.ibm.sbt.test.java.connections.profiles.GetId;
import com.ibm.sbt.test.java.connections.profiles.GetPhoneNumber;
import com.ibm.sbt.test.java.connections.profiles.GetProfileUrl;
import com.ibm.sbt.test.java.connections.profiles.GetPronunciationUrl;
import com.ibm.sbt.test.java.connections.profiles.GetThumbnailUrl;
import com.ibm.sbt.test.java.connections.profiles.GetTitle;
import com.ibm.sbt.test.java.connections.profiles.UpdatePhoneNumber;

/**
 * @author mwallace
 * 
 * @date 6 Mar 2013
 */
@RunWith(Suite.class)
@SuiteClasses({ GetAbout.class, GetDisplayName.class, GetId.class, GetPhoneNumber.class, GetProfileUrl.class, GetPronunciationUrl.class,
        GetThumbnailUrl.class, GetTitle.class, UpdatePhoneNumber.class })
public class ProfilesTestSuite {
    @AfterClass
    public static void cleanup() {
        TestEnvironment.cleanup();
    }
}
