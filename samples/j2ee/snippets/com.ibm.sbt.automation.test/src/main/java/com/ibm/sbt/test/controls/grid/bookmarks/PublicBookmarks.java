/*
 * � Copyright IBM Corp. 2012
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
package com.ibm.sbt.test.controls.grid.bookmarks;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ibm.sbt.automation.core.test.BaseGridTest;

/**
 * @author David Ryan
 * 
 * @date 15 November 2013
 */
public class PublicBookmarks extends BaseGridTest {

	@Override
	protected boolean isEnvironmentValid() {
		return super.isEnvironmentValid() && !environment.isSmartCloud();
	}

    @Test
    public void testGrid() {
        assertTrue("Expected the test to generate a grid", checkGrid("Social_Bookmarks_Controls_Public_Bookmarks",true,true));
    }
    
}