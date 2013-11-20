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
package com.ibm.sbt.test.java.smartcloud;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import com.ibm.sbt.automation.core.environment.TestEnvironment;
import com.ibm.sbt.automation.core.environment.TestEnvironmentFactory;

/**
 * @author Vimal Dhupar
 * 
 * @date 15 Nov 2013
 */
@RunWith(Suite.class)
@SuiteClasses({ com.ibm.sbt.test.java.connections.ActivitiesTestSuite.class})
public class ActivitiesTestSuite {
    @BeforeClass
    public static void init() {
        TestEnvironment environment = TestEnvironmentFactory.getEnvironment();
        environment.enableSmartCloud();
    }
    
    @AfterClass
    public static void cleanup() {
        TestEnvironment environment = TestEnvironmentFactory.getEnvironment();
        environment.disableSmartCloud();
        TestEnvironment.cleanup();
    }
}
