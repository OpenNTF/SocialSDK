/*
 * © Copyright IBM Corp. 2012
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
package com.ibm.sbt.test.js.connections.communities;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ibm.sbt.automation.core.test.BaseServiceTest;

/**
 * @author mwallace
 * 
 * @since 5 Mar 2013
 */
public class GetPublicCommunities extends BaseServiceTest {

    @Test
    public void testNoError() {
        boolean result = checkNoError("Social_Communities_Get_Public_Communities");
        assertTrue(getNoErrorMsg(), result);
    }

}
