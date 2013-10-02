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
	
	var BlogTmpl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\"  xmlns:thr=\"http://purl.org/syndication/thread/1.0\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\"><title type=\"text\">${getTitle}</title><snx:timezone>${getTimezone}</snx:timezone><snx:handle>${getHandle}</snx:handle><summary type=\"html\">${getSummary}</summary>${getTags}</entry>";
	var BlogPostTmpl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\"  xmlns:thr=\"http://purl.org/syndication/thread/1.0\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\"><title type=\"text\">${getTitle}</title><content type=\"html\">${getContent}</content>${getTags}</entry>";
	var BlogCommentTmpl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\"  xmlns:thr=\"http://purl.org/syndication/thread/1.0\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\"><content type=\"html\">${getContent}</content></entry>";
	CategoryTmpl = "<category term=\"${tag}\"></category>";
    
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
         * Sets Handle of IBM Connections blog.
         * 
         * @method setHandle
         * @param {String} setHandle of the blog
         */
        setHandle : function(handle) {
            return this.setAsString("handle", handle);
        },

        /**
         * Return the value of IBM Connections blog ID from blog ATOM
         * entry document.
         * 
         * @method getBlogUuid
         * @return {String} ID of the blog
         */
        getBlogUuid : function() {
        	var blogIdPrefix = "urn:lsid:ibm.com:blogs:blog-";
        	var blogId = this.getAsString("blogUuid");
        	if(blogId && blogId.indexOf(blogIdPrefix) != -1){
            	blogId = blogId.substring(blogIdPrefix.length, blogId.length);
        	}
            return blogId;
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
         * Sets description of IBM Connections blog.
         * 
         * @method setSummary
         * @param {String} title Summary of the blog
         */
        setSummary : function(title) {
            return this.setAsString("summary", summary);
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
        },

        /**
         * Loads the blog object with the atom entry associated with the
         * blog. By default, a network call is made to load the atom entry
         * document in the blog object.
         * 
         * @method load
         * @param {Object} [args] Argument object
         */
        load : function(args) {
            // detect a bad request by validating required arguments
            var blogUuid = this.getBlogUuid();
            var promise = this.service._validateBlogUuid(blogUuid);
            if (promise) {
                return promise;
            }

            var self = this;
            var callbacks = {
                createEntity : function(service,data,response) {
                    self.setDataHandler(new XmlDataHandler({
                        service :  service,
                        data : data,
                        namespaces : consts.Namespaces,
                        xpath : consts.BlogXPath
                    }));
                    return self;
                }
            };

            var requestArgs = lang.mixin({
                blogUuid : blogUuid
            }, args || {});
            var options = {
                handleAs : "text",
                query : requestArgs
            };
            
            return this.service.getEntity(consts.AtomBlogEnties, options, blogUuid, callbacks);
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
        	var postIdPrefix = "urn:lsid:ibm.com:blogs:entry-";
        	var postId = this.getAsString("postUuid");
        	if(postId && postId.indexOf(postIdPrefix) != -1){
        		postId = postId.substring(postIdPrefix.length, postId.length);
        	}
            return postId;
        },

        /**
         * Sets id of IBM Connections blog post Id.
         * 
         * @method setPostUuid
         * @param {String} PostUuid of the blog post
         */
        setPostUuid : function(postUuid) {
            return this.setAsString("postUuid", postUuid);
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
         * Sets content of IBM Connections blog post.
         * 
         * @method setContent
         * @param {String} content Content of the blog post
         */
        setContent : function(content) {
            return this.setAsString("content", content);
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
        	var commentIdPrefix = "urn:lsid:ibm.com:blogs:comment-";
        	var commentId = this.getAsString("commentUuid");
        	if(commentId && commentId.indexOf(commentIdPrefix) != -1){
        		commentId = commentId.substring(commentIdPrefix.length, commentId.length);
        	}
            return commentId;
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
         * Sets content of IBM Connections blog post comment.
         * 
         * @method setContent
         * @param {String} content Content of the blog post comment
         */
        setContent : function(content) {
            return this.setAsString("content", content);
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
        },

        /**
         * Loads the blog comment object associated with the
         * blog. By default, a network call is made to load the atom entry
         * document in the comment object.
         * 
         * @method load
         * @param {Object} [args] Argument object
         */
        load : function(blogHandle, commentId, args) {
        	try{
            var promise = this.service._validateUuid(commentId);
            if (promise) {
                return promise;
            }

            var self = this;
            var callbacks = {
                createEntity : function(service,data,response) {
                    self.setDataHandler(new XmlDataHandler({
                        service :  service,
                        data : data,
                        namespaces : consts.Namespaces,
                        xpath : consts.CommentXPath
                    }));
                    return self;
                }
            };

            var requestArgs = lang.mixin({}, args || {});
            var options = {
                handleAs : "text",
                query : requestArgs
            };
            var url = null;
			url = this.service.constructUrl(consts.AtomBlogCommentEditRemove, null, {
				blogHandle : blogHandle,
				commentId : commentId
			});
        	}catch(e){
        		console.log("Excp: "+e);
        	}
            return this.service.getEntity(url, options, commentId, callbacks);
        }

    });
    
    /*
     * Callbacks used when reading a feed that contains blog entries.
     */
    var ConnectionsBlogFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.BlogFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = new XmlDataHandler({
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
            return new XmlDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.BlogFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = new XmlDataHandler({
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
            return new XmlDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.BlogFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = new XmlDataHandler({
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
                	this.endpoint.serviceMappings.blogHomepageHandle = this.getDefaultHandle();
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
         * @param {String} blogHandle Handle of the blog of which posts are to be get.
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
            var url = null;
            url = this.constructUrl(consts.AtomBlogEnties, null, {
				blogHandle : blogHandle
			});
            return this.getEntities(url, options, this.getBlogPostsCallbacks());
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
         * @param {blogHandle} Handle of the blog of which Comments are to be get
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
            var url = null;
            url = this.constructUrl(consts.AtomBlogComments, null, {
				blogHandle : blogHandle
			});
            return this.getEntities(url, options, this.getBlogPostCommentsCallbacks());
        },

        /**
         * Create a blog by sending an Atom entry document containing the 
         * new blog.
         * 
         * @method createBlog
         * @param {Object} blog Blog object which denotes the blog to be created.
         * @param {Object} [args] Argument object
         */
        createBlog : function(blogOrJson,args) {
            var blog = this._toBlog(blogOrJson);
            var promise = this._validateBlog(blog, false, args);
            if (promise) {
                return promise;
            }

            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
        		if (data) {
            		var dataHandler = new XmlDataHandler({
                        service :  service,
                        data : data,
                        namespaces : consts.Namespaces,
                        xpath : consts.BlogXPath
                    });
                	blog.setDataHandler(dataHandler);
            	}
            	blog.setData(data);
                return blog;
            };

            var options = {
                method : "POST",
                query : args || {},
                headers : consts.AtomXmlHeaders,
                data : this._constructBlogPostData(blog)
            };
            
            return this.updateEntity(consts.AtomBlogCreate, options, callbacks, args);
        },

        /**
         * Update a blog by sending a replacement blog entry document 
         * to the existing blog's edit web address.
         * All existing blog entry information will be replaced with the new data. To avoid 
         * deleting all existing data, retrieve any data you want to retain first, and send it back 
         * with this request. For example, if you want to add a new tag to a blog entry, retrieve 
         * the existing tags, and send them all back with the new tag in the update request.
         * 
         * @method updateBlog
         * @param {Object} blog Blog object
         * @param {Object} [args] Argument object
         */
        updateBlog : function(blogOrJson,args) {
            var blog = this._toBlog(blogOrJson);
            var promise = this._validateBlog(blog, true, args);
            if (promise) {
                return promise;
            }
            
            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
            	var blogUuid = blog.getBlogUuid();
            	if (data) {
            		var dataHandler = new XmlDataHandler({
                        service :  service,
                        data : data,
                        namespaces : consts.Namespaces,
                        xpath : consts.BlogXPath
                    });
            		blog.setDataHandler(dataHandler);
            	}
            	blog.setBlogUuid(blogUuid);
                return blog;
            };

            var requestArgs = lang.mixin({}, args || {});
            
            var options = {
                method : "PUT",
                query : requestArgs,
                headers : consts.AtomXmlHeaders,
                data : this._constructBlogPostData(blog)
            };
            var blogId = blog.getBlogUuid();
            var url = null;
			url = this.constructUrl(consts.AtomBlogEditDelete, null, {
				blogId : blogId
			});
            return this.updateEntity(url, options, callbacks, args);
        },

        /**
         * Delete a blog, use the HTTP DELETE method.
         * 
         * @method deleteBlog
         * @param {String/Object} blog id of the blog or the blog object (of the blog to be deleted)
         * @param {Object} [args] Argument object
         */
        deleteBlog : function(blogUuid,args) {
            var promise = this._validateBlogUuid(blogUuid);
            if (promise) {
                return promise;
            }            
           
            var requestArgs = lang.mixin({}, args || {});
            
            var options = {
                method : "DELETE",
                query : requestArgs,
                handleAs : "text"
            };
            var url = null;
			url = this.constructUrl(consts.AtomBlogEditDelete, null, {
				blogId : blogUuid
			});
            return this.deleteEntity(url, options, blogUuid);
        },

        /**
         * Create a blog post by sending an Atom entry document containing the 
         * new blog post.
         * 
         * @method createPost
         * @param {Object} postOrJson Blog post object which denotes the blog post to be created.
         * @param {Object} [args] Argument object
         */
        createPost : function(postOrJson, blogHandle, args) {
            var post = this._toPost(postOrJson);
            var promise = this._validateBlog(post, false, args);
            if (promise) {
                return promise;
            }

            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
        		if (data) {
            		var dataHandler = new XmlDataHandler({
                        service :  service,
                        data : data,
                        namespaces : consts.Namespaces,
                        xpath : consts.PostXPath
                    });
            		post.setDataHandler(dataHandler);
            	}
        		post.setData(data);
                return post;
            };

            var options = {
                method : "POST",
                query : args || {},
                headers : consts.AtomXmlHeaders,
                data : this._constructPostPostData(post)
            };
            var url = null;
            url = this.constructUrl(consts.AtomBlogPostCreate, null, {
				blogHandle : blogHandle
			});
            return this.updateEntity(url, options, callbacks, args);
        },

        /**
         * Update a post by sending a replacement post entry document 
         * to the existing post's edit web address.
         * All existing post entry information will be replaced with the new data. To avoid 
         * deleting all existing data, retrieve any data you want to retain first, and send it back 
         * with this request. For example, if you want to add a new tag to a post entry, retrieve 
         * the existing tags, and send them all back with the new tag in the update request.
         * 
         * @method updateBost
         * @param {Object} post Post object
         * @param {Object} [args] Argument object
         */
        updatePost : function(postOrJson, blogHandle, args) {
            var post = this._toPost(postOrJson);
            var promise = this._validatePost(post, true, args);
            if (promise) {
                return promise;
            }
            
            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
            	var postUuid = post.getPostUuid();
            	if (data) {
            		var dataHandler = new XmlDataHandler({
                        service :  service,
                        data : data,
                        namespaces : consts.Namespaces,
                        xpath : consts.PostXPath
                    });
            		post.setDataHandler(dataHandler);
            	}
            	post.setPostUuid(postUuid);
                return post;
            };

            var requestArgs = lang.mixin({}, args || {});
            
            var options = {
                method : "PUT",
                query : requestArgs,
                headers : consts.AtomXmlHeaders,
                data : this._constructPostPostData(post)
            };
            var postId = post.getPostUuid();
            var url = null;
			url = this.constructUrl(consts.AtomBlogPostEditDelete, null, {
				postId : post.getPostUuid(),
				blogHandle : blogHandle
			});
            return this.updateEntity(url, options, callbacks, args);
        },

        /**
         * Delete a blog post, use the HTTP DELETE method.
         * 
         * @method deletePost
         * @param {String/Object} blogHandle handle of the blog(of which the post is to be deleted)
         * @param {String/Object} postId post id of the blog post(of the blog post to be deleted)
         * @param {Object} [args] Argument object
         */
        deletePost : function(blogHandle, postId, args) {
            var promise = this._validateUuid(postId);
            if (promise) {
                return promise;
            }            
           
            var requestArgs = lang.mixin({}, args || {});
            
            var options = {
                method : "DELETE",
                query : requestArgs,
                handleAs : "text"
            };
            var url = null;
			url = this.constructUrl(consts.AtomBlogPostEditDelete, null, {
				blogHandle : blogHandle,
				postId : postId
			});
            return this.deleteEntity(url, options, postId);
        },

        /**
         * Delete a blog comment, use the HTTP DELETE method.
         * 
         * @method deleteComment
         * @param {String/Object} blogHandle handle of the blog(of which the post is to be deleted)
         * @param {String/Object} comment id of the blog comment(of the blog comment to be deleted)
         * @param {Object} [args] Argument object
         */
        deleteComment : function(blogHandle, commentId, args) {
            var promise = this._validateUuid(commentId);
            if (promise) {
                return promise;
            }            
           
            var requestArgs = lang.mixin({}, args || {});
            
            var options = {
                method : "DELETE",
                query : requestArgs,
                handleAs : "text"
            };
            var url = null;
			url = this.constructUrl(consts.AtomBlogCommentEditRemove, null, {
				blogHandle : blogHandle,
				commentId : commentId
			});
            return this.deleteEntity(url, options, commentId);
        },
        
        /**
         * Create a comment post by sending an Atom entry document containing the 
         * new blog comment.
         * 
         * @method createComment
         * @param {Object} commentOrJson Blog comment object which denotes the comment to be created.
         * @param {Object} [args] Argument object
         */
        createComment : function(commentOrJson, blogHandle, entryId, args) {
            var comment = this._toComment(commentOrJson);
            var promise = this._validateComment(comment);
            if (promise) {
                return promise;
            }

            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
        		if (data) {
            		var dataHandler = new XmlDataHandler({
                        service :  service,
                        data : data,
                        namespaces : consts.Namespaces,
                        xpath : consts.CommentXPath
                    });
            		comment.setDataHandler(dataHandler);
            	}
        		comment.setData(data);
                return comment;
            };

            var options = {
                method : "POST",
                query : args || {},
                headers : consts.AtomXmlHeaders,
                data : this._constructCommentPostData(comment)
            };
            var url = null;
			url = this.constructUrl(consts.AtomBlogCommentCreate, null, {
				blogHandle : blogHandle,
				postId : entryId
			});
            return this.updateEntity(url, options, callbacks, args);
        },
        
        /**
         * Retrieve a blog comment, use the edit link for the blog entry 
         * which can be found in the my blogs feed.
         * 
         * @method getComment
         * @param {String } blogHandle
         * @param {String } commentUuid
         * @param {Object} args Object containing the query arguments to be 
         * sent (defined in IBM Connections Communities REST API) 
         */
        getComment : function(blogHandle, commentUuid, args) {
            var comment = new Comment({
                service : this,
                _fields : { commentUuid : commentUuid }
            });
            return comment.load(blogHandle, commentUuid, args);
        },
        
        /**
         * Create a Blog object with the specified data.
         * 
         * @method newBlog
         * @param {Object} args Object containing the fields for the 
         * new blog 
         */
        newBlog : function(args) {
            return this._toBlog(args);
        },
        
        /**
         * Create a Blog Post object with the specified data.
         * 
         * @method newPost
         * @param {Object} args Object containing the fields for the 
         * new post 
         */
        newPost : function(args) {
            return this._toPost(args);
        },
        
        /**
         * Create a Blog Post comment object with the specified data.
         * 
         * @method newComment
         * @param {Object} args Object containing the fields for the 
         * new comment 
         */
        newComment : function(args) {
            return this._toComment(args);
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
         * Return a Blog instance from Blog or JSON or String. Throws
         * an error if the argument was neither.
         */
        _toBlog : function(blogOrJsonOrString) {
            if (blogOrJsonOrString instanceof Blog) {
                return blogOrJsonOrString;
            } else {
                if (lang.isString(blogOrJsonOrString)) {
                    blogOrJsonOrString = {
                        blogUuid : blogOrJsonOrString
                    };
                }
                return new Blog({
                    service : this,
                    _fields : lang.mixin({}, blogOrJsonOrString)
                });
            }
        },
        
        /*
         * Return a Post instance from Post or JSON or String. Throws
         * an error if the argument was neither.
         */
        _toPost : function(postOrJsonOrString) {
            if (postOrJsonOrString instanceof Post) {
                return postOrJsonOrString;
            } else {
                if (lang.isString(postOrJsonOrString)) {
                    postOrJsonOrString = {
                        postUuid : postOrJsonOrString
                    };
                }
                return new Post({
                    service : this,
                    _fields : lang.mixin({}, postOrJsonOrString)
                });
            }
        },
        
        /*
         * Return a Comment instance from Comment or JSON or String. Throws
         * an error if the argument was neither.
         */
        _toComment : function(commentOrJsonOrString) {
            if (commentOrJsonOrString instanceof Comment) {
                return commentOrJsonOrString;
            } else {
                if (lang.isString(commentOrJsonOrString)) {
                	commentOrJsonOrString = {
                        commentUuid : commentOrJsonOrString
                    };
                }
                return new Comment({
                    service : this,
                    _fields : lang.mixin({}, commentOrJsonOrString)
                });
            }
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
         * Validate a post, and return a Promise if invalid.
         */
        _validateUuid : function(postId) {
            if (!postId) {
                return this.createBadRequestPromise("Invalid argument, blog post id must be specified.");
            }
        },
        
        /*
         * Validate a post, and return a Promise if invalid.
         */
        _validatePost : function(post,checkUuid) {
            if (!post || !post.getTitle()) {
                return this.createBadRequestPromise("Invalid argument, blog post with title must be specified.");
            }
            if (checkUuid && !post.getPostUuid()) {
                return this.createBadRequestPromise("Invalid argument, blog post with UUID must be specified.");
            }
        },
        
        /*
         * Validate a comment, and return a Promise if invalid.
         */
        _validateComment : function(comment,checkUuid) {
            if (!comment || !comment.getContent()) {
                return this.createBadRequestPromise("Invalid argument, blog comment with content must be specified.");
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
        },
        
        /*
         * Construct a post data for a Blog
         */
        _constructBlogPostData : function(blog) {
            var transformer = function(value,key) {
                if (key == "getTags") {
                    var tags = value;
                    value = "";
                    for (var tag in tags) {
                        value += stringUtil.transform(CategoryTmpl, {
                            "tag" : tags[tag]
                        });
                    }
                }
                return value;
            };
            
            var postData = stringUtil.transform(BlogTmpl, blog, transformer, blog);
            return stringUtil.trim(postData);
        },
        
        /*
         * Construct a post data for a Blog Post
         */
        _constructPostPostData : function(post) {
            var transformer = function(value,key) {
                if (key == "getTags") {
                    var tags = value;
                    value = "";
                    for (var tag in tags) {
                        value += stringUtil.transform(CategoryTmpl, {
                            "tag" : tags[tag]
                        });
                    }
                }
                return value;
            };
            
            var postData = stringUtil.transform(BlogPostTmpl, post, transformer, post);
            return stringUtil.trim(postData);
        },
        
        /*
         * Construct a post data for a Blog Post
         */
        _constructCommentPostData : function(comment) {
            var transformer = function(value,key) {
                return value;
            };
            
            var postData = stringUtil.transform(BlogCommentTmpl, comment, transformer, comment);
            return stringUtil.trim(postData);
        }
        
    });
    return BlogService;
});
