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
package com.ibm.sbt;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import com.ibm.sbt.test.AcmeTestSuite;
import com.ibm.sbt.test.js.SmartCloudTestSuite;

/**
 * @author Lorenzo Boccaccia 
 * @since May 31, 2013
 */
@RunWith(Suite.class)
@SuiteClasses({ CheckinTestSuite.class, SbtTestSuite.class, SbtxTestSuite.class, AcmeTestSuite.class, SmartCloudTestSuite.class })
public class AllTests {

}
