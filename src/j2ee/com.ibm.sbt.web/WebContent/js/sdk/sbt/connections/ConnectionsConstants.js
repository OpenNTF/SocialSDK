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
define([], function() {

    return {

        /**
         * Error code used for a bad request
         */
        BadRequest : 400,

        /**
         * Namespaces to be used when reading the Connections ATOM field
         */
		Namespaces : {
			o : "http://ns.opensocial.org/2008/opensocial",
			app : "http://www.w3.org/2007/app",
			thr : "http://purl.org/syndication/thread/1.0",
			fh : "http://purl.org/syndication/history/1.0",
			snx : "http://www.ibm.com/xmlns/prod/sn",
			opensearch : "http://a9.com/-/spec/opensearch/1.1/",
			a : "http://www.w3.org/2005/Atom",
			h : "http://www.w3.org/1999/xhtml",
			td : "urn:ibm.com/td",
			relevance : "http://a9.com/-/opensearch/extensions/relevance/1.0/",
			ibmsc : "http://www.ibm.com/search/content/2010",
			xhtml : "http://www.w3.org/1999/xhtml"
		},

        /**
		 * XPath expressions used when parsing a Connections ATOM feed
		 */
        ConnectionsFeedXPath : {
            // used by getEntitiesDataArray
            entries : "/a:feed/a:entry",
            // used by getSummary
            totalResults : "/a:feed/opensearch:totalResults",
            startIndex : "/a:feed/opensearch:startIndex",
            itemsPerPage : "/a:feed/opensearch:itemsPerPage",
        },

        /**
         * 
         */
        AtomXmlHeaders : {
            "Content-Type" : "application/atom+xml"
        },
        
        /**
         * AuthType variable values for endpoint
         */
        AuthTypes : {
        	OAuth : "oauth",
        	Basic : "basic"
        }
        

    };
});