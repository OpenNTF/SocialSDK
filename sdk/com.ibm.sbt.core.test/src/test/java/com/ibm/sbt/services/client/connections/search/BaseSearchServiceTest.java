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

package com.ibm.sbt.services.client.connections.search;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import com.ibm.sbt.services.BaseUnitTest;

/**
 * Tests for the java connections Search API a test class provides its own
 * tests extending the test endpoint abstract class
 * 
 * @author Christian Gosch, inovex GmbH
 * @date Apr 24, 2015
 */

public class BaseSearchServiceTest extends BaseUnitTest {

	protected Result result;
	protected SearchService searchService;
	@Rule public ExpectedException thrown= ExpectedException.none();

	@Before
	public void createTestData() throws Exception {
		//FIXME: Needs to be fixed to address issues with mock files
		if (searchService==null) {
			searchService = new SearchService();
		}
	}
	
}