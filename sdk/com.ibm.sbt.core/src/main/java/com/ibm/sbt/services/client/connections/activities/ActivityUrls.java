/*
 * © Copyright IBM Corp. 2014
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
package com.ibm.sbt.services.client.connections.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ibm.commons.runtime.util.URLEncoding;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * @author mwallace
 *
 */
public enum ActivityUrls {

	ACTIVITIES_SERVICE("{0}/service/atom2/service"), // Retrieving the Activities service document
	MY_ACTIVITIES("{0}/service/atom2/activities"), //	Getting the My Activities feed and creating activities
	COMPLETED_ACTIVITIES("{0}/service/atom2/completed"), // Getting a feed of completed activities
	ALL_ACTIVITIES("{0}/service/atom2/everything"), //	Getting a feed of all activities
	TODO_ACTIVITIES("{0}/service/atom2/todos"), //	Getting a feed of entries in the to-do list
	ACTIVITY_TAGS("{0}/service/atom2/tags"), //	Getting a list of the tags assigned to all activities
	ACTIVITY_CATEGORIES("{0}/service/atom2/categories"), //	Getting a list of the categories assigned to this activity
	ACTIVITY_TEMPLATES("{0}/service/atom2/entrytemplates?activityUuid={1}"), //	Retrieves a feed of entry templates
	THRASHED_ACTIVITIES("{0}/service/atom2/trash"), //	Retrieves a feed of the activities and entries in the trash
	ACTIVITY("{0}/service/atom2/activity?activityUuid={1}"), // Retrieving an Activity entry and creating an activity node
	ACTIVITY_NODE("{0}/service/atom2/activitynode?activityNodeUuid={1}"), // Retrieve, update, deleting activity nodes programmatically
	THRASHED_ACTIVITY_NODE("{0}/service/atom2/trashednode?activityNodeUuid={1}"), // Restoring activity nodes programmatically
	ACTIVITY_ACL("{0}/service/atom2/acl?activityUuid={1}"), // Adding an activity member programmatically
	ACTIVITY_MEMBER("{0}/service/atom2/acl?activityUuid={1}&memberid={2}"), //	Retrieve, update and delete an activity member programmatically
	ACTIVITY_DESCENDANTS("{0}/service/atom2/descendants?nodeUuid={1}"), // Getting a feed of the Activity descendants
	;
	
	private String urlPattern;
	
	private ActivityUrls(String urlPattern) {
		this.urlPattern = urlPattern;
	}
	
	protected String format(String... args) {
		return formatPattern(Arrays.asList(args));
	}
	
	public String format(Endpoint endpoint, String... args) {
		List<String> list = new ArrayList<String>(Arrays.asList(args));
		list.add(0, "activities"); // TODO lookup this value
		return formatPattern(list);
	}
	
	private String formatPattern(List<String> args) {
		List<String> encoded = new ArrayList<String>();
		for(String arg : args) {
			try {
				encoded.add(URLEncoding.encodeURIString(arg, "UTF-8", 0, false));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return StringUtil.format(urlPattern, encoded.toArray());
	}
	
}
