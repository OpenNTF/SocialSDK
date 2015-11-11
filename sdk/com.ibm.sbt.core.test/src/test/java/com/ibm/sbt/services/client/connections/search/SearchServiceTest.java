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

import org.junit.Assert;
import org.junit.Test;

import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.test.lib.TestEnvironment;

/**
 * Tests for the java connections Search API a test class provides its own
 * tests extending the test endpoint abstract class
 * 
 * @author Christian Gosch, inovex GmbH
 * @date Apr 24, 2015
 */

public class SearchServiceTest extends BaseSearchServiceTest {

    /**
     * Test is valid if the query runs through. All results including empty list are accepted!
     * @throws Exception
     */
    @Test
    public final void testGetResults() throws Exception {
    	//FIXME: Test is Broken
    	// lookup public content having "test" in its searched data fields
    	TestEnvironment.setRequiresAuthentication(false);
    	EntityList<Result> results = searchService.getResults("test");
    	Assert.assertTrue(results.size() >= 0);
    }

    /**
     * Test is valid if the query runs through. All results including empty list are accepted!
     * @throws Exception
     */
    @Test
    public final void testGetMyResults() throws Exception {
    	//FIXME: Test is Broken
    	// lookup "my" content having "test" in its searched data fields
    	TestEnvironment.setRequiresAuthentication(true);
    	EntityList<Result> results = searchService.getMyResults("test");
    	Assert.assertTrue(results.size() >= 0);
    }

    /**
     * Test is valid if the query runs through. All results including empty list are accepted!
     * @throws Exception
     */
    @Test
    public final void testGetResultsByTag() throws Exception {
    	//FIXME: Test is Broken
    	// lookup public content having "testtag" in its tags
    	// this in turn tests multiple methods in SearchService.
    	TestEnvironment.setRequiresAuthentication(false);
    	EntityList<Result> results = searchService.getResultsByTag("testtag");
    	Assert.assertTrue(results.size() >= 0);
    }

    /**
     * Test is valid if the query runs through. All results including empty list are accepted!
     * @throws Exception
     */
    @Test
    public final void testGetMyResultsByTag() throws Exception {
    	//FIXME: Test is Broken
    	// lookup "my" content having "testtag" in its tags
    	// this in turn tests multiple methods in SearchService.
    	TestEnvironment.setRequiresAuthentication(true);
    	EntityList<Result> results = searchService.getMyResultsByTag("testtag");
    	Assert.assertTrue(results.size() >= 0);
    }

}