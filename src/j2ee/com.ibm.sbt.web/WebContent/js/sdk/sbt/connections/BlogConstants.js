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
 * Social Business Toolkit SDK. Definition of constants for BlogService.
 * 
 * @module sbt.connections.BlogConstants
 */
define([ "../lang", "./ConnectionsConstants" ], function(lang,conn) {

    return lang.mixin({
    	
    	/**
         * XPath expressions used when parsing a Connections Blogs ATOM feed
         * 
         * @property BlogFeedXPath
         * @type Object
         * @for sbt.connections.BlogService
         */
        BlogFeedXPath : conn.ConnectionsFeedXPath,
        
        /**
         * XPath expressions to be used when reading a Blog
         * 
         * @property BlogXPath
         * @type Object
         * @for sbt.connections.BlogService
         */
        BlogXPath : {
            entry : "/a:entry",
            uid : "a:id",
            blogUuid : "a:id",
            title : "a:title",
            summary : "a:summary[@type='html']",
            blogUrl : "a:link[@rel='alternate']/@href",
            published : "a:published",
            updated : "a:updated",
            authorUserid : "a:author/snx:userid",
            authorName : "a:author/a:name",
            authorEmail : "a:author/a:email",
            handle : "snx:handle",
            timezone : "snx:timezone",
            rank : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/recommendations']",
            categoryupdates : "a:category[@term='updates']",
            categoryfaq : "a:category[@term='faq']",
            categorywith : "a:category[@term='with']",
            categoryshared : "a:category[@term='shared']"
        },
        
        /**
         * XPath expressions to be used when reading a Blog Post Entry
         * 
         * @property BlogEntryXPath
         * @type Object
         * @for sbt.connections.BlogService
         */
        BlogEntryXPath : {
            entry : "/a:entry",
            uid : "a:id",
            blogid : "a:id",
            title : "a:title",
            summary : "a:summary[@type='html']",
            blogUrlAlternate : "a:link[@rel='alternate']/@href",
            blogUrl : "a:link[@rel='self']/@href",
            replies : "a:link[@rel='replies']/@href",
            blogUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/recommendations']/@href",
            content : "a:content[@type='html']",
            published : "a:published",
            updated : "a:updated",
            authorUserid : "a:author/snx:userid",
            authorName : "a:author/a:name",
            authorEmail : "a:author/a:email",
            authorState : "a:author/a:state",
            rankRecommendations : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/recommendations']",
            rankComment : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/comment']",
            rankHit : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/hit']",
            sourceId : "a:source/a:id",
            sourceTitle  : "a:source/a:title ",
            sourceLink : "a:source/a:link[@rel='self']/@href",
            sourceLinkAlternate : "a:source/a:link[@rel='alternate']/@href",
            sourceUpdated : "a:source/a:updated",
            sourceCategory : "a:source/a:link[@scheme='http://www.ibm.com/xmlns/prod/sn/type']/@term"
        },

        /**
         * page  Page number. Specifies the page to be returned. The default value is 1, which returns the first page. 
         * ps  Page size. Specify the number of entries to return per page.
         * search  Well-formed full text search query. Performs a text search on community titles and descriptions.
         * since  Includes in the resulting feed all communities updated after a specified date. Specify the date using a date-time value that conforms to RFC3339. Use an upper case "T" to separate the date and time, and an uppercase "Z" in the absence of a numeric time zone offset. For example: 2009-01-04T20:32:31.171Z.
         * sortBy  Specifies what value to use as the basis for organizing the entries returned in the feed. The options are:
         *                                          modified – Sorts the results by last modified date.
         *                                          commented - Sorts the entries by the number of comments or replies an item has received.
         *                                          popularity - Sorts the entries by how popular the item is.
         *                                          recommended - Sorts the entries by the number of times the item was recommended.
		 *									        title - Sorts the entries alphabetically by title. The title used is the text that is displayed in the <title> element of each entry in the feed.
         * sortOrder  Specifies the order in which to sort the results. The options are:
         *                                          asc - Sorts the results in ascending order.
		 *                                          desc - Sorts the results in descending order.
		 * communityUuid  Returns community blog and community ideation blog in the specified community.
         * ps  Page size. Specify the number of entries to return per page.
         * blogType  Returns only specific Blogs type:
		 *											blog - regular blogs and community blogs
		 *	    									communityblog - community blogs only
		 *  										communityideationblog - community ideation blogs only
         * tags  Returns blog entries with the specified tags. Separate multiple tags with a plus sign (+).
         */
        
        /**
         * A feed of all blogs.
         *  
         * Get the Blogs feed to see a list of all blogs
         * 
         * Supports: page, ps, sortBy, sortOrder, search, since, communityUuid, blogType, sortField, tag
         * 
         * @property AtomBlogsAll
         * @type String
         * @for sbt.connections.BlogService
         */
        AtomBlogsAll : "/${blogs}/${blogHandle}/feed/blogs/atom",
        
        /**
         * A feed of my blogs.
         *  
         * Get the Blogs feed to see a list of Blogs created by logged in user
         * 
         * Supports: page, ps, sortBy, sortOrder, search, since, communityUuid, blogType, sortField, tag
         * 
         * @property AtomBlogsMy
         * @type String
         * @for sbt.connections.BlogService
         */
        AtomBlogsMy : "/${blogs}/${blogHandle}/api/blogs",
        
        /**
         * A feed of all blogs posts.
         *  
         * Get the Blogs posts feed to see a list of posts from all Blogs
         * 
         * Supports: page, ps, sortBy, sortOrder, search, since
         * 
         * @property AtomEntriesAll
         * @type String
         * @for sbt.connections.BlogService
         */
        AtomEntriesAll : "/${blogs}/${blogHandle}/feed/entries/atom",
        
        /**
         * A feed of a blog's posts.
         *  
         * Get the Blog posts feed to see a list of posts from a Blog
         * 
         * Supports: page, ps, sortBy, sortOrder, search, since
         * 
         * @property AtomBlogEnties
         * @type String
         * @for sbt.connections.BlogService
         */
        AtomBlogEnties : "/${blogs}/${blogHandle}/feed/entries/atom"

    }, conn);
});