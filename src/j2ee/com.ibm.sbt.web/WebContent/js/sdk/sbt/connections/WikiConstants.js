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
 * Definition of constants for WikiService.
 */
define([ "../lang", "./ConnectionsConstants" ], function(lang,conn) {

    return lang.mixin(conn, {
    	
        /**
         * XPath expressions used when parsing a Connections Wiki ATOM feed
         */
        WikiFeedXPath : conn.ConnectionsFeedXPath,

        /**
         * XPath expressions to be used when reading a search result
         */
        WikiXPath : lang.mixin({
        	wikiUuid : "a:id",
        	sharedWith : "a:sharedWith",
        	permissions : "td:permissions",
        	communityUuid : "snx:communityUuid",
        	member : "ca:member",
        	memberId : "ca:id",
        	memberType : "ca:type",
        	memberRole : "ca:role"
        }, conn.AtomEntryXPath),
        
		/**
         * This returns a feed of wikis to which the authenticated user has access. 
         */
        WikisAll : "/{wikis}/{authType}/api/wikis/feed",
        
		/**
         * This returns a feed of wikis to which everyone who can log into the Wikis application has access. 
         */
        WikisPublic : "/{wikis}/{authType}/api/wikis/public",
        
		/**
         * This returns a feed of wikis of which the authenticated user is a member. 
         */
        WikisMy : "/{wikis}/{authType}/api/mywikis/feed",
        
		/**
         * This returns a feed of wikis to which everyone who can log into the Wikis application has access.
         */
        WikisMostCommented : "/{wikis}/{authType}/anonymous/api/wikis/mostcommented",
        
		/**
         * This returns a feed of wikis to which everyone who can log into the Wikis application has access. 
         */
        WikisMostRecommended : "/{wikis}/{authType}/api/wikis/mostrecommended",
        
		/**
         * This returns a feed of wikis to which everyone who can log into the Wikis application has access. 
         */
        WikisMostVisited : "/{wikis}/{authType}/api/wikis/mostvisited",
        
		/**
         * This returns a feed of the pages in a given wiki. 
         */
        WikisGet : "/{wikis}/{authType}/anonymous/api/wiki/{wiki-label}/feed",
        
		/**
         * Get a feed that lists all of the pages in a specific wiki that have been added or edited by the authenticated user. 
         */
        WikisMyPages : "/{wikis}/{authType}/api/wiki/{wiki-label}/mypages",
        
		/**
         * This returns a feed that lists the pages that have been deleted from wikis and are currently stored in the trash.  
         */
        WikisRecycleBin : "/{wikis}/{authType}/anonymous/api/wiki/{wiki-label-or-ID}/recyclebin/feed"     
        
    });
});