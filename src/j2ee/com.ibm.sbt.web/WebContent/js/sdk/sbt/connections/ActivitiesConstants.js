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
/**
 * Social Business Toolkit SDK.
 * Definition of constants for SearchService.
 */
define([ "../lang", "./ConnectionsConstants" ], function(lang,conn) {

    return lang.mixin(conn, {
    	
        /**
         * XPath expressions used when parsing a Connections Activities ATOM feed
         */
    	ActivitiesFeedXPath : conn.ConnectionsFeedXPath,

        /**
         * XPath expressions to be used when reading an activity entry
         */
        ActivityXPath : {
            // used by getEntityData
            entry : "/a:entry",
            // used by getEntityId
            uid : "a:id",
            // used by getters
            id : "a:id",
            title : "a:title",
            updated : "a:updated",
            published : "a:published",
            authorUserid : "a:author/snx:userid",
            authorName : "a:author/a:name",
            authorEmail : "a:author/a:email",
            contributorUserid : "a:contributor/snx:userid",
            contributorName : "a:contributor/a:name",
            contributorEmail : "a:contributor/a:email",
    		content : "a:content[@type='text']",
            tags : "a:category[not(@scheme)]/@term",
            activity : "snx:activity",
    		position : "snx:position",
    		depth : "snx:depth",
    		permissions : "snx:permissions",
    		icon : "snx:icon"
        },
        
		/**
         * Search for content in all of the activities, both completed and active, that matches a specific criteria.
         */
        allActivities : "/activities/service/atom2/everything",
        
        /**
         * Get a feed of all active activities that match a specific criteria.
         */
        myActivities : "activities/service/atom2/activities",

        /**
         * Search for a set of completed activities that match a specific criteria.
         */
        completedActivities : "activities/service/atom2/completed",

    });
});