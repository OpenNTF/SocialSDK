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
package com.ibm.sbt.test.java.connections.activitystreams;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ibm.sbt.automation.core.test.BaseAuthJavaServiceTest;

/**
 * @author mkataria
 * @date Jan 10, 2013
 */
public class SearchByTag extends BaseAuthJavaServiceTest {

    @Test
    public void runTest() {
        boolean result = checkNoError("Social_ActivityStreams_SearchByTag");
        assertTrue(getNoErrorMsg(), result);
    }

}
