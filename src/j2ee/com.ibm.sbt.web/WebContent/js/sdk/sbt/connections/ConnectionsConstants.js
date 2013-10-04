/*
 * © Copyright IBM Corp. 2012,2013
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
 * Social Business Toolkit SDK. Definition of constants for IBM Connections.
 * 
 * @module sbt.connections.ConnectionsConstants
 */
define([ "../lang", "../base/BaseConstants" ], function(lang, base) {

    return lang.mixin(base, {

        /**
         * Error code used for a bad request
         */
        BadRequest : 400,

        /**
		 * XPath expressions used when parsing a Connections ATOM feed
		 */
        ConnectionsFeedXPath : {
            // used by getEntitiesDataArray
            entries : "/a:feed/a:entry",
            // used by getSummary
            totalResults : "/a:feed/opensearch:totalResults",
            startIndex : "/a:feed/opensearch:startIndex",
            itemsPerPage : "/a:feed/opensearch:itemsPerPage"
        },

        /**
         * AuthType variable values for endpoint
         */
        AuthTypes : {
        	OAuth : "oauth",
        	Basic : "basic"
        }

    });
});