/*
 * � Copyright IBM Corp. 2012,2013
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
 * Social Business Toolkit SDK. Definition of constants for CommunityService.
 * 
 * @module sbt.connections.CommunityConstants
 */
define([ "../lang", "../smartcloud/SmartcloudConstants" ], function(lang,conn) {

    return lang.mixin({
  
        /**
         * Namespaces to be used when reading the Communities ATOM entry or feed
         */
        CommunityNamespaces : {
			a : "http://www.w3.org/2005/Atom",
			app : "http://www.w3.org/2007/app",
			snx : "http://www.ibm.com/xmlns/prod/sn",
			opensearch: "http://a9.com/-/spec/opensearch/1.1/"	
		},
        
        /**
         * XPath expressions used when parsing a Connections Communities ATOM feed
         * 
         * @property CommunityFeedXPath
         * @type Object
         * @for sbt.connections.CommunityService
         */
        CommunityFeedXPath : conn.ConnectionsFeedXPath,

        /**
         * XPath expressions to be used when reading a Community Entry
         * 
         * @property CommunityXPath
         * @type Object
         * @for sbt.connections.CommunityService
         */
        CommunityXPath : lang.mixin({}, conn.AtomEntryXPath, {
            communityUuid : "a:id",
            communityTheme : "snx:communityTheme",
            logoUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/logo']/@href",
            tags : "a:category[not(@scheme)]/@term",
            memberCount : "snx:membercount",
            communityType : "snx:communityType",
            isExternal : "snx:isExternal"
        }),
        
        /**
         * XPath expressions to be used when reading a Community Member Entry
         * 
         * @property MemberXPath
         * @type Object
         * @for sbt.connections.CommunityService
         */
        MemberXPath : lang.mixin({}, conn.AtomEntryXPath, {
        	id : "a:contributor/snx:userid",
            uid : "a:contributor/snx:userid",
            role : "snx:role"
        }),
                    
        /**
         * A feed of members.
         * 
         * Retrieve the members feed to view a list of the members who belong to a given community.
         * 
         * Supports: asc, email, page, ps, role, since, sortField, userid
         * 
         * @property AtomCommunityMembers
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunityMembers : "${communities}/service/atom/community/members",

        
    }, conn);
});