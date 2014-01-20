/*
 *  Copyright IBM Corp. 2013
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

package com.ibm.sbt.services.client.connections.activitystreams;

public class ActivityStreamRequestParams {
	
	public static final String rollUp="rollup";
	public static final String broadcast="broadcast";
	public static final String lang="lang";
	public static final String filterBy="FilterBy";
	public static final String filterOp="filterOp";
	public static final String filterValue="filterValue";
	public static final String query="query";
	public static final String queryLanguage="queryLanguage";
	public static final String filters="filters";
	public static final String dateFilter="dateFilter";
	public static final String facetRequests="facetRequests";
	public static final String preferSearchIndex="preferSearchIndex";
	public static final String custom="custom"; //indicates user passed in the search string
	

}
