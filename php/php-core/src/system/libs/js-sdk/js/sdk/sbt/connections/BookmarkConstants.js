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
 * Social Business Toolkit SDK. Definition of constants for BookmarkService.
 * 
 * @module sbt.connections.BookmarkConstants
 */
define([ "../lang", "./ConnectionsConstants" ], function(lang,conn) {

    return lang.mixin({
    	
    	BookmarkFeedXPath : conn.ConnectionsFeedXPath,
    	
        /**
         * XPath expressions
         * 
         * @property BookmarkXPath
         * @type Object
         * @for sbt.connections.CommunityService
         */
        BookmarkXPath : lang.mixin({}, conn.AtomEntryXPath, {
            BookmarkUuid: "a:id",
            privateFlag: "a:category[@term='private' and @scheme='http://www.ibm.com/xmlns/prod/sn/flags']",
            categoryType: "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/type']/@term",
            link: "a:link[not(@rel)]/@href",
            linkSame: "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/same']/@href",
            url: "a:link[1]/@href",
            authorId:"a:author/snx:userid",
            authorName: "a:author/a:name",
            authorEmail: "a:author/a:email",
            authorUri: "a:author/a:uri",
            tags : "a:category[not(@scheme)]/@term",
            clickcount: "snx:clickcount",
        }),
        
        /**
         * Namespaces to be used when reading the Bookmarks ATOM entry or feed
         */
        BookmarkNamespaces : {
			a : "http://www.w3.org/2005/Atom",
			snx : "http://www.ibm.com/xmlns/prod/sn"
		},

        /**
         * A feed of all bookmarks.
         *  
         * @property AtomBookmarkssAll
         * @type String
         * @for sbt.connections.BoomarkService
         */
        AtomBookmarksAll : "/${dogear}/atom",

        /**
         * A feed of popular bookmarks.
         *  
         * @property AtomBookmarkssAll
         * @type String
         * @for sbt.connections.BoomarkService
         */
        AtomBookmarksPopular : "/${dogear}/atom/popular",

        /**
         * A feed of bookmarks that others notified me about.
         *  
         * @property AtomBookmarkssAll
         * @type String
         * @for sbt.connections.BoomarkService
         */
        AtomBookmarksMyNotifications : "/${dogear}/atom/mynotifications",

        /**
         * A feed of bookmarks about which I notified others.
         *  
         * @property AtomBookmarkssAll
         * @type String
         * @for sbt.connections.BoomarkService
         */
        AtomBookmarksINotifiedMySentNotifications : "/${dogear}/atom/mysentnotifications",

        /**
         * A feed of all bookmark tags.
         *  
         * @property AtomBookmarksTags
         * @type String
         * @for sbt.connections.BoomarkService
         */
        AtomBookmarksTags : "/${dogear}/tags",

        /**
         * create delete or update a bookmark.
         *  
         * @property AtomBookmarkCreateUpdateDelete
         * @type String
         * @for sbt.connections.BoomarkService
         */
        AtomBookmarkCreateUpdateDelete : "/${dogear}/api/app"

    }, conn);
});