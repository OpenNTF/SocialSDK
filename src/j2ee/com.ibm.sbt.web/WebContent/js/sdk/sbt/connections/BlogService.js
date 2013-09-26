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
 * The Blogs API allows application programs to retrieve blog information, subscribe to blog updates, and create or modify blogs.
 * 
 * @module sbt.connections.BlogService
 */
define([ "../declare", "../config", "../lang", "../stringUtil", "../Promise", "./BlogConstants", "../base/BaseService",
         "../base/BaseEntity", "../base/XmlDataHandler" ], 
    function(declare,config,lang,stringUtil,Promise,consts,BaseService,BaseEntity,XmlDataHandler) {
	
    /*
     * BlogDataHandler class.
     */
    var BlogDataHandler = declare(XmlDataHandler, {
        /**
         * @method getEntityId
         * @returns
         */
        getEntityId : function() {
            var entityId = stringUtil.trim(this.getAsString("uid"));
            return extractBlogUuid(this.service, entityId);
        }
    });
    
    /**
     * Blog class represents an entry for a Blogs feed returned by the
     * Connections REST API.
     * 
     * @class Blog
     * @namespace sbt.connections
     */
    var Blog = declare(BaseEntity, {

        /**
         * Construct a Blog entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },

        /**
         * Return the value of IBM Connections blog handle from blog ATOM
         * entry document.
         * 
         * @method getHandle
         * @return {String} handle of the blog
         */
        getHandle : function() {
            return this.getAsString("handle");
        },

        /**
         * Return the value of IBM Connections blog ID from blog ATOM
         * entry document.
         * 
         * @method getBlogUuid
         * @return {String} ID of the blog
         */
        getBlogUuid : function() {
            return this.getAsString("blogUuid");
        },

        /**
         * Sets id of IBM Connections blog.
         * 
         * @method setBlogUuid
         * @param {String} blogUuid of the blog
         */
        setBlogUuid : function(blogUuid) {
            return this.setAsString("blogUuid", blogUuid);
        },

        /**
         * Return the value of IBM Connections blog title from blog
         * ATOM entry document.
         * 
         * @method getTitle
         * @return {String} Blog title of the blog
         */
        getTitle : function() {
            return this.getAsString("title");
        },

        /**
         * Sets title of IBM Connections blog.
         * 
         * @method setTitle
         * @param {String} title Title of the blog
         */
        setTitle : function(title) {
            return this.setAsString("title", title);
        },

        /**
         * Gets an author of IBM Connections Blog.
         * 
         * @method getAuthor
         * @return {Member} Author of the blog
         */
        getAuthor : function() {
            return this.getAsObject([ "authorUserid", "authorName", "authorEmail" ]);
        },

        /**
         * Return the value of IBM Connections blog description summary
         * from blog ATOM entry document.
         * 
         * @method getSummary
         * @return {String} Blog description summary of the blog
         */
        getSummary : function() {
            return this.getAsString("summary");
        },

        /**
         * Return the value of IBM Connections blog URL from blog ATOM
         * entry document.
         * 
         * @method getBlogUrl
         * @return {String} Blog URL of the blog
         */
        getBlogUrl : function() {
            return this.getAsString("blogUrl");
        },

        /**
         * Return the published date of the IBM Connections blog from
         * blog ATOM entry document.
         * 
         * @method getPublished
         * @return {Date} Published date of the Blog
         */
        getPublished : function() {
            return this.getAsDate("published");
        },

        /**
         * Return the last updated date of the IBM Connections blog from
         * blog ATOM entry document.
         * 
         * @method getUpdated
         * @return {Date} Last updated date of the Blog
         */
        getUpdated : function() {
            return this.getAsDate("updated");
        }

    });

    /**
     * Post class represents a post for a Blogs feed returned by the
     * Connections REST API.
     * 
     * @class Post
     * @namespace sbt.connections
     */
    var Post = declare(BaseEntity, {

        /**
         * Construct a Blog Post.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },

        /**
         * Return the value of IBM Connections blog post ID
         * entry document.
         * 
         * @method getPostUuid
         * @return {String} ID of the blog post
         */
        getPostUuid : function() {
            return this.getAsString("uid");
        },

        /**
         * Sets id of IBM Connections blog post Id.
         * 
         * @method setPostUuid
         * @param {String} PostUuid of the blog post
         */
        setPostUuid : function(blogUuid) {
            return this.setAsString("blogUuid", blogUuid);
        },

        /**
         * Return the value of IBM Connections blog title from blog post
         * ATOM entry document.
         * 
         * @method getTitle
         * @return {String} Blog title of the blog post
         */
        getTitle : function() {
            return this.getAsString("title");
        },

        /**
         * Sets title of IBM Connections blog post.
         * 
         * @method setTitle
         * @param {String} title Title of the blog post
         */
        setTitle : function(title) {
            return this.setAsString("title", title);
        },

        /**
         * Gets an author of IBM Connections Blog post.
         * 
         * @method getAuthor
         * @return {Object} Author of the blog post
         */
        getAuthor : function() {
            return this.getAsObject([ "authorUserid", "authorName", "authorEmail", "authorState" ]);
        },

        /**
         * Return the value of IBM Connections blog post description summary
         * from blog ATOM entry document.
         * 
         * @method getSummary
         * @return {String} Blog post description summary of the blog
         */
        getSummary : function() {
            return this.getAsString("summary");
        },

        /**
         * Return the value of IBM Connections blog post URL from blog ATOM
         * entry document.
         * 
         * @method getSelfUrl
         * @return {String} Blog URL of the blog
         */
        getSelfUrl : function() {
            return this.getAsString("blogEntryUrl");
        },
        
        /**
         * Return the value of IBM Connections blog post alternate URL from blog ATOM
         * entry document.
         * 
         * @method getAlternateUrl
         * @return {String} Blog URL of the blog
         */
        getAlternateUrl : function() {
            return this.getAsString("blogEntryUrlAlternate");
        },
        
        /**
         * Return the value of IBM Connections blog post replies URL from blog ATOM
         * entry document.
         * 
         * @method getRepliesUrl
         * @return {String} Blog replies URL for the blog post
         */
        getRepliesUrl : function() {
            return this.getAsString("replies");
        },

        /**
         * Return the published date of the IBM Connections blog post from
         * blog ATOM entry document.
         * 
         * @method getPublished
         * @return {Date} Published date of the Blog post
         */
        getPublished : function() {
            return this.getAsDate("published");
        },

        /**
         * Return the last updated date of the IBM Connections blog post from
         * blog ATOM entry document.
         * 
         * @method getUpdated
         * @return {Date} Last updated date of the Blog post
         */
        getUpdated : function() {
            return this.getAsDate("updated");
        },
        
        /**
         * Return the content of the IBM Connections blog post from
         * blog ATOM entry document.
         * 
         * @method getContent
         * @return {String} content of the Blog
         */
        getContent : function() {
            return this.getAsDate("content");
        },
        
        /**
         * Return the last updated dateRecomendations URL of the IBM Connections blog post from
         * blog ATOM entry document.
         * 
         * @method getRecomendationsURL
         * @return {String} Recomendations URL of the Blog Post
         */
        getRecomendationsURL : function() {
            return this.getAsDate("recomendationsUrl");
        },
        
        /**
         * Return the Recomendations count of the IBM Connections blog post from
         * blog ATOM entry document.
         * 
         * @method getRecomendationsCount
         * @return {String} Last updated date of the Blog post
         */
        getRecomendationsCount : function() {
            return this.getAsDate("rankRecommendations");
        },
        
        /**
         * Return the comment count of the IBM Connections blog post from
         * blog ATOM entry document.
         * 
         * @method getCommentCount
         * @return {String} Last updated date of the Blog post
         */
        getCommentCount : function() {
            return this.getAsDate("rankComment");
        },
        
        /**
         * Return the hit count of the IBM Connections blog post from
         * blog ATOM entry document.
         * 
         * @method getHitCount
         * @return {String} Last updated date of the Blog post
         */
        getHitCount : function() {
            return this.getAsDate("rankHit");
        },

        /**
         * Gets an source of IBM Connections Blog post.
         * 
         * @method getSource
         * @return {Object} Source of the blog post
         */
        getSource : function() {
            return this.getAsObject([ "sourceId", "sourceTitle", "sourceLink", "sourceLinkAlternate", "sourceUpdated", "sourceCategory" ]);
        }
    });
    
    /**
     * Comment class represents a comment on a Blogs post returned by the
     * Connections REST API.
     * 
     * @class Comment
     * @namespace sbt.connections
     */
    var Comment = declare(BaseEntity, {

        /**
         * Construct a Blog Post Comment.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },

        /**
         * Return the value of IBM Connections blog post comment
         * entry document.
         * 
         * @method getCommentUuid
         * @return {String} comment of the blog post
         */
        getCommentUuid : function() {
            return this.getAsString("uid");
        },

        /**
         * Sets id of IBM Connections blog post comment.
         * 
         * @method setCommentUuid
         * @param {String} CommentUuid of the blog post
         */
        setCommentUuid : function(CommentUuid) {
            return this.setAsString("CommentUuid", CommentUuid);
        },

        /**
         * Return the value of IBM Connections blog title from blog post comment
         * ATOM entry document.
         * 
         * @method getTitle
         * @return {String} Blog title of the blog post comment
         */
        getTitle : function() {
            return this.getAsString("title");
        },

        /**
         * Sets title of IBM Connections blog post comment.
         * 
         * @method setTitle
         * @param {String} title Title of the blog post comment
         */
        setTitle : function(title) {
            return this.setAsString("title", title);
        },

        /**
         * Gets an author of IBM Connections Blog post comment.
         * 
         * @method getAuthor
         * @return {Object} Author of the blog post comment
         */
        getAuthor : function() {
            return this.getAsObject([ "authorUserid", "authorName", "authorEmail", "authorState" ]);
        },

        /**
         * Return the value of IBM Connections blog post comment description summary
         * from blog ATOM entry document.
         * 
         * @method getSummary
         * @return {String} Blog post comment description summary of the blog
         */
        getSummary : function() {
            return this.getAsString("summary");
        },

        /**
         * Return the value of IBM Connections blog post comment URL from blog ATOM
         * entry document.
         * 
         * @method getSelfUrl
         * @return {String} Blog URL of the blog post comment
         */
        getSelfUrl : function() {
            return this.getAsString("commentUrl");
        },
        
        /**
         * Return the value of IBM Connections blog post comment alternate URL from blog ATOM
         * entry document.
         * 
         * @method getAlternateUrl
         * @return {String} Blog URL of the blog post comment
         */
        getAlternateUrl : function() {
            return this.getAsString("commentUrlAlternate");
        },

        /**
         * Return the published date of the IBM Connections blog post comment from
         * blog ATOM entry document.
         * 
         * @method getPublished
         * @return {Date} Published date of the Blog post comment
         */
        getPublished : function() {
            return this.getAsDate("published");
        },

        /**
         * Return the last updated date of the IBM Connections blog post comment from
         * blog ATOM entry document.
         * 
         * @method getUpdated
         * @return {Date} Last updated date of the Blog post comment
         */
        getUpdated : function() {
            return this.getAsDate("updated");
        },
        
        /**
         * Return the content of the IBM Connections blog post comment from
         * blog ATOM entry document.
         * 
         * @method getContent
         * @return {String} content of the Blog post comment
         */
        getContent : function() {
            return this.getAsDate("content");
        },
        
        /**
         * Return the last updated dateRecomendations URL of the IBM Connections blog post comment from
         * blog ATOM entry document.
         * 
         * @method getRecomendationsURL
         * @return {String} Recomendations URL of the Blog Post comment
         */
        getRecomendationsURL : function() {
            return this.getAsDate("recomendationsUrl");
        },
        
        /**
         * Return the Recomendations count of the IBM Connections blog post comment from
         * blog ATOM entry document.
         * 
         * @method getRecomendationsCount
         * @return {String} Last updated date of the Blog post comment
         */
        getRecomendationsCount : function() {
            return this.getAsDate("rankRecommendations");
        },
        
        /**
         * Return the get-reply-to of the IBM Connections blog post comment from
         * blog ATOM entry document.
         * 
         * @method getReplyTo
         * @return {String} Last updated date of the Blog post comment
         */
        getReplyTo : function() {
            return this.getAsDate("replyTo");
        },
        
        /**
         * Return the getTrackbackTitle of the IBM Connections blog post comment from
         * blog ATOM entry document.
         * 
         * @method getTrackbackTitle
         * @return {String} Last updated date of the Blog post comment
         */
        getTrackbackTitle : function() {
            return this.getAsDate("trackbacktitle");
        },

        /**
         * Gets an source of IBM Connections Blog post.
         * 
         * @method getSource
         * @return {Object} Source of the blog post
         */
        getSource : function() {
            return this.getAsObject([ "sourceId", "sourceTitle", "sourceLink", "sourceLinkAlternate" ]);
        }
    });

    /*
     * Method used to extract the blog uuid.
     */
    var extractBlogUuid = function(service, uid) {
        if (uid && uid.indexOf("http") == 0) {
            return service.getUrlParameter(uid, "blogUuid");
        } else {
            return uid;
        }
    };
    
    /*
     * Callbacks used when reading a feed that contains blog entries.
     */
    var ConnectionsBlogFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new BlogDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.BlogFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = new BlogDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.BlogXPath
            });
            return new Blog({
                service : service,
                dataHandler : entryHandler
            });
        }
    };
    
    /*
     * Callbacks used when reading a feed that contains blog entries.
     */
    var ConnectionsBlogPostsCallbacks = {
        createEntities : function(service,data,response) {
            return new BlogDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.BlogFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = new BlogDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.PostXPath
            });
            return new Post({
                service : service,
                dataHandler : entryHandler
            });
        }
    };

    /*
     * Callbacks used when reading a feed that contains blog entries.
     */
    var ConnectionsBlogPostCommentsCallbacks = {
        createEntities : function(service,data,response) {
            return new BlogDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.BlogFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = new BlogDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.CommentXPath
            });
            return new Comment({
                service : service,
                dataHandler : entryHandler
            });
        }
    };
    /**
     * BlogService class.
     * 
     * @class BlogService
     * @namespace sbt.connections
     */
    var BlogService = declare(BaseService, {
    	
    	contextRootMap: {
        	blogs : "blogs"
        },
    	
        /**
         * Constructor for BlogService
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        	
            if (!this.endpoint) {
                this.endpoint = config.findEndpoint(this.getDefaultEndpointName());
                if(!this.endpoint.serviceMappings.blogHomepageHandle){
                	this.contextRootMap.blogHomepageHandle = this.getDefaultHandle();
                }
            }
        },

        /**
         * Return the default endpoint name if client did not specify one.
         * @returns {String}
         */
        getDefaultEndpointName : function() {
            return "connections";
        },

        /**
         * Return the default blog homepage handle name if there is not one specified in sbt.properties.
         * @returns {String}
         */
        getDefaultHandle : function() {
            return "homepage";
        },
        
        /**
         * Get the All Blogs feed to see a list of all blogs to which the 
         * authenticated user has access.
         * 
         * @method getBlogs
         * 
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of all blogs. The
         * parameters must be exactly as they are supported by IBM
         * Connections like ps, sortBy etc.
         */
        getBlogs : function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            
            return this.getEntities(consts.AtomBlogsAll, options, this.getBlogFeedCallbacks());
        },

        /**
         * Get the My Blogs feed to see a list of the blogs to which the 
         * authenticated user is a member.
         * 
         * @method getMyBlogs
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of my blogs. The
         * parameters must be exactly as they are supported by IBM
         * Connections like ps, sortBy etc.
         */
        getMyBlogs : function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            
            return this.getEntities(consts.AtomBlogsMy, options, this.getBlogFeedCallbacks());
        },

        /**
         * Get the featured posts feed to find the blog posts that have had the most activity across 
         * all of the blogs hosted by the Blogs application within the past two weeks
         * 
         * @method getFeaturedBlogs
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of my blogs. The
         * parameters must be exactly as they are supported by IBM
         * Connections like ps, sortBy etc.
         */
        getFeaturedBlogs : function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            
            return this.getEntities(consts.AtomBlogsFeatured, options, this.getBlogFeedCallbacks());
        },

        /**
         * Get the featured posts feed to find the blog posts that have had the most activity across 
         * all of the blogs hosted by the Blogs application within the past two weeks
         * 
         * @method getFeaturedPosts
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of my blogs. The
         * parameters must be exactly as they are supported by IBM
         * Connections like ps, sortBy etc.
         */
        getFeaturedPosts : function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            
            return this.getEntities(consts.AtomBlogsPostsFeatured, options, this.getBlogPostsCallbacks());
        },

        /**
         * Get a feed that includes all of the recommended blog posts 
         * in all of the blogs hosted by the Blogs application.
         * 
         * @method getRecommendedPosts
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of my blogs. The
         * parameters must be exactly as they are supported by IBM
         * Connections like ps, sortBy etc.
         */
        getRecommendedPosts : function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            
            return this.getEntities(consts.AtomBlogsPostsRecommended, options, this.getBlogPostsCallbacks());
        },
        
        /**
         * Get the blog posts feed to see a list of posts for all blog .
         * 
         * @method getBlogsPosts
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of all blogs posts. The
         * parameters must be exactly as they are supported by IBM
         * Connections like ps, sortBy etc.
         */
        getBlogsPosts : function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            
            return this.getEntities(consts.AtomEntriesAll, options, this.getBlogPostsCallbacks());
        },
        
        /**
         * Get the blog posts feed to see a list of posts for a blog .
         * 
         * @method getBlogPosts
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of posts of a blog . The
         * parameters must be exactly as they are supported by IBM
         * Connections like ps, sortBy etc.
         */
        getBlogPosts : function(blogHandle, args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            lang.mixin(this.contextRootMap, {blogHandle:blogHandle});
            return this.getEntities(consts.AtomBlogEnties, options, this.getBlogPostsCallbacks());
        },
        
        /**
         * Get the blog comments feed to see a list of comments for all blogs .
         * 
         * @method getBlogsComments
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of posts of a blog . The
         * parameters must be exactly as they are supported by IBM
         * Connections like ps, sortBy etc.
         */
        getBlogsComments : function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            return this.getEntities(consts.AtomBlogCommentsAll, options, this.getBlogPostCommentsCallbacks());
        },
        
        /**
         * Get the blog comments feed to see a list of comments for a blog post .
         * 
         * @method getBlogPostComments
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of posts of a blog . The
         * parameters must be exactly as they are supported by IBM
         * Connections like ps, sortBy etc.
         */
        getBlogComments : function(blogHandle, args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            lang.mixin(this.contextRootMap, {blogHandle:blogHandle});
            return this.getEntities(consts.AtomBlogComments, options, this.getBlogPostCommentsCallbacks());
        },
        
        /*
         * Callbacks used when reading a feed that contains Blog entries.
         */
        getBlogFeedCallbacks: function() {
            return ConnectionsBlogFeedCallbacks;
        },
        
        /*
         * Callbacks used when reading a feed that contains Blog entries.
         */
        getBlogPostsCallbacks: function() {
            return ConnectionsBlogPostsCallbacks;
        },
        
        /*
         * Callbacks used when reading a feed that contains Blog post comments.
         */
        getBlogPostCommentsCallbacks: function() {
            return ConnectionsBlogPostCommentsCallbacks;
        },

        /*
         * Validate a blog UUID, and return a Promise if invalid.
         */
        _validateBlogUuid : function(blogUuid) {
            if (!blogUuid || blogUuid.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected blogUuid.");
            }
        },

        /*
         * Validate a blog, and return a Promise if invalid.
         */
        _validateBlog : function(blog,checkUuid) {
            if (!blog || !blog.getTitle()) {
                return this.createBadRequestPromise("Invalid argument, blog with title must be specified.");
            }
            if (checkUuid && !blog.getBlogUuid()) {
                return this.createBadRequestPromise("Invalid argument, blog with UUID must be specified.");
            }
        }
    });
    return BlogService;
});
