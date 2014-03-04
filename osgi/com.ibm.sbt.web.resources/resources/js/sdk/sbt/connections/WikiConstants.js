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
 
dojo.provide("sbt.connections.WikiConstants");
/**
 * Social Business Toolkit SDK.
 * Definition of constants for WikiService.
 */
define([ "sbt/lang", "sbt/connections/ConnectionsConstants" ], function(lang,conn) {

    /**
     * XPath expressions to be used when reading a wiki or wiki page entry
     */
    var BaseWikiXPath = lang.mixin({
    	uuid : "td:uuid",
    	label : "td:label",
    	permissions : "td:permissions",
    	tags : "a:category[not(@scheme)]/@term",
        modifierName : "td:modifier/a:name",
        modifierEmail : "td:modifier/a:email",
        modifierUserid : "td:modifier/snx:userid",
        modifierUserState : "td:modifier/snx:userState",
        created : "td:created",
        modified : "td:modified",
        member : "ca:member",
        memberId : "ca:member/@ca:id",
        memberType : "ca:member/@ca:type",
        memberRole : "ca:member/@ca:role"
    }, conn.AtomEntryXPath);
	
    return lang.mixin(conn, {
    	
        /**
         * XPath expressions used when parsing a Connections Wiki ATOM feed
         */
        WikiFeedXPath : conn.ConnectionsFeedXPath,

        /**
         * XPath expressions to be used when reading a wiki entry
         */
        WikiXPath : lang.mixin({
            communityUuid : "snx:communityUuid",
            themeName : "td:themeName",
            librarySize : "td:librarySize",
            libraryQuota : "td:libraryQuota",
            totalRemovedSize : "td:totalRemovedSize"
        }, BaseWikiXPath, conn.AtomEntryXPath),
        
        /**
         * XPath expressions to be used when reading a wiki page entry
         */
        WikiPageXPath : lang.mixin({
            lastAccessed : "td:lastAccessed",
            versionUuid : "td:versionUuid",
            versionLabel : "td:versionLabel",
            propagation : "td:propagation",
            totalMediaSize : "td:totalMediaSize",
            recommendations : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/recommendations']",
            comment : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/comment']",
            hit : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/hit']",
            anonymous_hit : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/anonymous_hit']",
            share : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/share']",
            collections : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/collections']",
            attachments : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/attachments']",
            versions : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/versions']"            
        }, BaseWikiXPath, conn.AtomEntryXPath),
        
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
        WikiPages : "/{wikis}/{authType}/anonymous/api/wiki/{wikiLabel}/feed",
        
		/**
         * Get a feed that lists all of the pages in a specific wiki that have been added or edited by the authenticated user. 
         */
        WikiMyPages : "/{wikis}/{authType}/api/wiki/{wikiLabel}/mypages",
        
		/**
         * This returns a feed that lists the pages that have been deleted from wikis and are currently stored in the trash.  
         */
        WikiRecycleBin : "/{wikis}/{authType}/anonymous/api/wiki/{wikiLabelOrId}/recyclebin/feed",
        
        /**
         * Retrieve an Atom document of a wiki.
         */
        WikiEntry : "/{wikis}/{authType}/api/wiki/{wikiLabel}/entry",
        
        /**
         * Returns a wiki page after authenticating the request.
         */
        WikiPageEntry : "/{wikis}/{authType}/api/wiki/{wikiLabel}/page/{pageLabel}/entry",
        
        /**
         * Post to this feed to create a wiki page.
         */
        WikiFeed : "/{wikis}/{authType}/api/wiki/{wikiLabel}/feed"
        
    });
});