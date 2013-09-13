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
    	
    	BookmarksFeedXPath : conn.ConnectionsFeedXPath,
    	
        /**
         * XPath expressions
         * 
         * @property BookmarkXPath
         * @type Object
         * @for sbt.connections.CommunityService
         */
        BookmarkXPath : {
            title: "a:title",
            updated: "a:updated",
            url: "a:link[1]/@href",
            authorId:"a:author/snx:userid",
            authorName: "a:author/a:name",
            tags : "a:category[not(@scheme)]/@term",
        },

        /**
         * A feed of all bookmarks.
         *  
         * @property AtomBookmarkssAll
         * @type String
         * @for sbt.connections.BoomarkService
         */
        AtomBookmarksAll : "/${dogear}/atom",


    }, conn);
});