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
         * XPath expressions used when parsing a Connections Search ATOM feed
         */
        SearchFeedXPath : conn.ConnectionsFeedXPath,

        /**
         * XPath expressions to be used when reading a Profile Entry
         */
        SearchXPath : {
            // used by getEntityData
            entry : "/a:feed/a:entry",
            // used by getEntityId
            uid : "a:contributor/snx:userid",
            // used by getters
            id : "a:id",
            userid : "a:contributor/snx:userid",
            name : "a:contributor/a:name",
            email : "a:contributor/a:email",
            title : "a:title"
        },
                
		/**
         * Search IBM Connections for public information.
         */
        publicSearch : "/search/atom/search/results",
        
        /**
         * Search IBM Connections for both public information and private information that you have access to.
         */
        mySearch : "/search/atom/mysearch/results",

		/**
         * Search IBM Connection for public information, and then return the people associated with the results.
         */
        peopleSearch : "/search/atom/search/facets/people",
        
        /**
         * Search Lotus Connections for both public information and private information that you have access to, and then return the people associated with the results.
         */
        myPeopleSearch : "/search/atom/mysearch/facets/people",

		/**
         * Search IBM Connection for public information, and then return the tags associated with the results.
         */
        tagSearch : "/search/atom/search/facets/tag",
        
        /**
         * Search Lotus Connections for both public information and private information that you have access to, and then return the tags associated with the results.
         */
        myTagSearch : "/search/atom/mysearch/facets/tag",

		/**
         * Search IBM Connection for public information, and then return the dates associated with the results.
         */
        dateSearch : "/search/atom/search/facets/date",
        
        /**
         * Search Lotus Connections for both public information and private information that you have access to, and then return the dates associated with the results.
         */
        myDateSearch : "/search/atom/mysearch/facets/date",

		/**
         * Search IBM Connection for public information, and then return the applications associated with the results.
         */
        sourceSearch : "/search/atom/search/facets/source",
        
        /**
         * Search Lotus Connections for both public information and private information that you have access to, and then return the applications associated with the results.
         */
        mySourceSearch : "/search/atom/mysearch/facets/source"

    });
});