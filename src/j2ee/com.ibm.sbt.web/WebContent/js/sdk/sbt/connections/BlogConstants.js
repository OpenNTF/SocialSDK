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
        
        RecommendersFeedXpath : {
            entries : "/a:feed/a:entry",
            totalResults : "/a:feed/os:totalResults"
        },
        
        /**
         * Namespaces to be used when reading the Blogs ATOM entry or feed
         */
        BlogNamespaces : {
			a : "http://www.w3.org/2005/Atom",
			app : "http://www.w3.org/2007/app",
			snx : "http://www.ibm.com/xmlns/prod/sn"
		},
        
        /**
         * XPath expressions to be used when reading a Blog
         * 
         * @property BlogXPath
         * @type Object
         * @for sbt.connections.BlogService
         */
        BlogXPath : lang.mixin({}, conn.AtomEntryXPath, {
            blogUuid : "a:id",
            handle : "snx:handle",
            timezone : "snx:timezone",
            rank : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/recommendations']",
            categoryupdates : "a:category[@term='updates']",
            categoryfaq : "a:category[@term='faq']",
            categorywith : "a:category[@term='with']",
            categoryshared : "a:category[@term='shared']"
        }),
        
        /**
         * XPath expressions to be used when reading a Blog Post
         * 
         * @property BlogPostXPath
         * @type Object
         * @for sbt.connections.BlogService
         */
        BlogPostXPath : lang.mixin({}, conn.AtomEntryXPath, {
            postUuid : "a:id",
            replies : "a:link[@rel='replies']/@href",
            recomendationsUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/recommendations']/@href",
            rankRecommendations : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/recommendations']",
            rankComment : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/comment']",
            rankHit : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/hit']",
            sourceId : "a:source/a:id",
            sourceTitle  : "a:source/a:title ",
            sourceLink : "a:source/a:link[@rel='self']/@href",
            sourceLinkAlternate : "a:source/a:link[@rel='alternate']/@href",
            sourceUpdated : "a:source/a:updated",
            sourceCategory : "a:source/a:link[@scheme='http://www.ibm.com/xmlns/prod/sn/type']/@term",
            blogHandle : "blogHandle"
        }),
        
        /**
         * XPath expressions to be used when reading a Blog Post Comment
         * 
         * @property CommentXPath
         * @type Object
         * @for sbt.connections.BlogService
         */
        CommentXPath : lang.mixin({}, conn.AtomEntryXPath, {
            commentUuid : "a:id",
            commentUrl : "a:link[@rel='self']/@href",
            recomendationsUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/recommendations']/@href",
            trackbacktitle : "snx:trackbacktitle",
            replyTo : "thr:in-reply-to/@ref",
            replyToSource : "thr:in-reply-to/@source",
            rankRecommendations : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/recommendations']",
            sourceId : "a:source/a:id",
            sourceTitle  : "a:source/a:title ",
            sourceLink : "a:source/a:link[@rel='self']/@href",
            sourceLinkAlternate : "a:source/a:link[@rel='alternate']/@href",
            blogHandle : "blogHandle",
            blogPostUuid : "blogPostUuid"
        }),
        
        /**
         * XPath expressions to be used when reading a Blog Post Recommenders feed
         * 
         * @property RecommendersXPath
         * @type Object
         * @for sbt.connections.BlogService
         */
        RecommendersXPath : lang.mixin({}, conn.AtomEntryXPath, {
            recommenderUuid : "a:id",
            category : "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/type']/@term"            
        }),

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
        AtomBlogsAll : "/${blogs}/${blogHomepageHandle}/feed/blogs/atom",
        
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
        AtomBlogsMy : "/${blogs}/${blogHomepageHandle}/api/blogs",
        
        /**
         * A blog instance.
         * 
         * @property AtomBlogInstance
         * @type String
         * @for sbt.connections.BlogService
         */
        AtomBlogInstance : "/${blogs}/${blogHomepageHandle}/api/blogs/{blogUuid}",
        
        /**
         * A blog post entry.
         * 
         * @property AtomBlogPostInstance
         * @type String
         * @for sbt.connections.BlogService
         */
        AtomBlogPostInstance : "/${blogs}/${blogHomepageHandle}/api/entries/{postUuid}",
        
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
        AtomEntriesAll : "/${blogs}/${blogHomepageHandle}/feed/entries/atom",
        
        /**
         * A feed of a blog's posts.
         *  
         * Get the Blog posts feed to see a list of posts from a Blog
         * 
         * Supports: page, ps, sortBy, sortOrder, search, since
         * 
         * @property AtomBlogEntries
         * @type String
         * @for sbt.connections.BlogService
         */
        AtomBlogEntries : "/${blogs}/{blogHandle}/feed/entries/atom",
        	
        /**
         * A feed of a blog's comments.
         *  
         * Get the Blog Comments feed to see a list of comments from a blog post
         * 
         * Supports: page, ps, sortBy, sortOrder, search, since
         * 
         * @property AtomBlogComments
         * @type String
         * @for sbt.connections.BlogService
         */
        AtomBlogComments : "/${blogs}/{blogHandle}/feed/comments/atom",

        /**
         * A feed of a blog's comments.
         *  
         * Get the Blog Comments feed to see a list of comments from all blog post
         * 
         * Supports: page, ps, sortBy, sortOrder, search, since
         * 
         * @property AtomBlogCommentsAll
         * @type String
         * @for sbt.connections.BlogService
         */
        AtomBlogCommentsAll : "/${blogs}/${blogHomepageHandle}/feed/comments/atom",
        
        /**
         * A feed of featured blogs.
         *  
         * Get the featured blogs feed to find the blogs that have had the most activity across
         * all of the blogs hosted by the Blogs application in the past two weeks. 
         * 
         * Supports: page, ps, sortBy, sortOrder, search, since
         * 
         * @property AtomBlogsFeatured
         * @type String
         * @for sbt.connections.BlogService
         */
        AtomBlogsFeatured : "/${blogs}/${blogHomepageHandle}/feed/featuredblogs/atom",
        	
        /**
         * A feed of featured blogs posts.
         *  
         * Get the featured posts feed to find the blog posts that have had the most activity across 
         * all of the blogs hosted by the Blogs application within the past two weeks
         * 
         * Supports: page, ps, sortBy, sortOrder, search, since
         * 
         * @property AtomBlogsPostsFeatured
         * @type String
         * @for sbt.connections.BlogService
         */
        AtomBlogsPostsFeatured : "/${blogs}/${blogHomepageHandle}/feed/featured/atom",
        
        /**
         * A feed of featured blogs posts.
         *  
         * Get a feed that includes all of the recommended blog posts 
         * in all of the blogs hosted by the Blogs application. 
         * 
         * Supports: page, ps, sortBy, sortOrder, search, since
         * 
         * @property AtomBlogsPostsRecommended
         * @type String
         * @for sbt.connections.BlogService
         */
        AtomBlogsPostsRecommended : "/${blogs}/${blogHomepageHandle}/feed/recommended/atom",
        
        /**
         * A feed of blogs tags.
         *  
         * Get a feed that includes all of the tags 
         * in all of the blogs hosted by the Blogs application. 
         * 
         * @property AtomBlogsTags
         * @type String
         * @for sbt.connections.BlogService
         */
        AtomBlogsTags : "/${blogs}/${blogHomepageHandle}/feed/tags/atom",

        /**
         * A feed of blog tags.
         *  
         * Get a feed that includes all of the tags 
         * in a perticular Blog. 
         * 
         * @property AtomBlogTags
         * @type String
         * @for sbt.connections.BlogService
         */
        AtomBlogTags : "/${blogs}/{blogHandle}/feed/tags/atom",
        
        /**
         * Create a Blog. 
         * 
         * @property AtomBlogCreate
         * @type String
         * @for sbt.connections.BlogService
         */
        AtomBlogCreate : "/${blogs}/${blogHomepageHandle}/api/blogs",
        
        /**
         * Edit or remove a Blog. 
         * 
         * @property AtomBlogEditDelete
         * @type String
         * @for sbt.connections.BlogService
         */
        AtomBlogEditDelete : "/${blogs}/${blogHomepageHandle}/api/blogs/{blogUuid}",
        /**
         * Create, Edit or remove a Blog Post. 
         * 
         * @property AtomBlogPostCreate
         * @type String
         * @for sbt.connections.BlogService
         */
        AtomBlogPostCreate : "/${blogs}/{blogHandle}/api/entries",
        
        /**
         * Edit or remove a Blog Post. 
         * 
         * @property AtomBlogPostEditDelete
         * @type String
         * @for sbt.connections.BlogService
         */
        AtomBlogPostEditDelete : "/${blogs}/{blogHandle}/api/entries/{postUuid}",
        /**
         * Create, Edit or remove a Blog Comment. 
         * 
         * @property AtomBlogCommentCreate
         * @type String
         * @for sbt.connections.BlogService
         */
        AtomBlogCommentCreate : "/${blogs}/{blogHandle}/api/entrycomments/{postUuid}",
        
        /**
         * Edit or remove a Blog Comment. 
         * 
         * @property AtomBlogCommentEditRemove
         * @type String
         * @for sbt.connections.BlogService
         */        
        AtomBlogCommentEditRemove : "/${blogs}/{blogHandle}/api/comments/{commentUuid}",
        
        /**
         * Recommend or Unrecommend a Blog Post. 
         * 
         * @property AtomRecommendBlogPost
         * @type String
         * @for sbt.connections.BlogService
         */ 
        AtomRecommendBlogPost : "/${blogs}/{blogHandle}/api/recommend/entries/{postUuid}",
    }, conn);
});