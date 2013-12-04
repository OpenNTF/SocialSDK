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
		 * XPath expressions used when parsing a Connections service document ATOM feeds
		 */
        ConnectionsServiceDocsFeedXPath : {
            // used by getEntitiesDataArray
            entries : "/a:feed/a:entry",
            // used by getSummary
    		emailConfig : "/a:feed/a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/configuration']/@term",
    		language : "/a:feed/a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/language']/@term",       
    		languageLabels : "/a:feed/a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/language']/@label"
        },

        /**
         * AuthType variable values for endpoint
         */
        AuthTypes : {
        	OAuth : "oauth",
        	Basic : "basic"
        },
        
        /**
         * XPath expressions to be used when reading a Connections entity
         * 
         * @property TagsXPath
         * @type Object
         * @for sbt.connections.Tag
         */
        TagsXPath : {
        	entries : "app:categories/a:category",
			term : "@term",
			frequency : "@snx:frequency",
			uid : "@term"
		},
		
		/**
         * XPath expressions to be used when reading a Blog
         * 
         * @property BlogXPath
         * @type Object
         * @for sbt.connections.BlogService
         */
        ServiceConfigXPath : lang.mixin({}, base.AtomEntryXPath, {
        	alternateSSLUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/alternate-ssl']/@href"
        }),
        
        /**
         * XPath expressions to be used when reading a Community Member Entry
         * 
         * @property MemberXPath
         * @type Object
         * @for sbt.connections.CommunityService
         */
        MemberXPath : lang.mixin({}, base.AtomEntryXPath, {
            role : "snx:role"
        }),
        
        /**
         * XPath expressions to be used when reading a Community Member Entry
         * 
         * @property MemberXPath
         * @type Object
         * @for sbt.connections.CommunityService
         */
        ReportEntryXPath : lang.mixin({}, base.AtomEntryXPath, {
            categoryIssue : "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/issue']/@term",
            reportItemLink : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/report-item']/@href",
            relatedLink : "a:link[@rel='related']/@href",
            inRefTo : "snx:in-ref-to[@rel='http://www.ibm.com/xmlns/prod/sn/report-item']/@ref"	
        }),
        
        /**
         * XPath expressions to be used when reading a Community Member Entry
         * 
         * @property MemberXPath
         * @type Object
         * @for sbt.connections.CommunityService
         */
        ModerationActionEntryXPath : lang.mixin({}, base.AtomEntryXPath, {
            moderationAction : "snx:moderation/action",
            relatedLink : "a:link[@rel='related']/@href",
            inRefTo : "snx:in-ref-to[@rel='http://www.ibm.com/xmlns/prod/sn/report-item']/@ref"	
        }),

        /**
         * Get service configs
         */
//        ServiceConfigs : "/${service}/serviceconfigs",

		/**
	     * Get service configs
	     */
	    ServiceConfigs : "/{service}/serviceconfigs"
	
	    });
});