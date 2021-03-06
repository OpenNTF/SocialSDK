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
package com.ibm.sbt.test.controls.grid.profiles;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ibm.sbt.automation.core.test.BaseGridTest;

/**
 * @author sberrybyrne
 * @since 6 Mar 2013
 */
public class ReportingChain extends BaseGridTest {

	@Override
	protected boolean isEnvironmentValid() {
		return super.isEnvironmentValid() && !environment.isSmartCloud();
	}

    @Test
    public void testReportingChainGrid() {
    	addSnippetParam("sample.userId1" , getProperty("sample.id1"));
        assertTrue("Expected the test to generate a grid", checkGrid("Social_Profiles_Controls_Reporting_Chain",true));
    }																

}
