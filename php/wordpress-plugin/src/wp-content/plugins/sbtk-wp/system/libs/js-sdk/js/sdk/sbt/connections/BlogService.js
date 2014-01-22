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
         "../base/AtomEntity", "../base/XmlDataHandler",  "./Tag"], 
    function(declare,config,lang,stringUtil,Promise,consts,BaseService,AtomEntity,XmlDataHandler, Tag) {
	
	var BlogTmpl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\"  xmlns:thr=\"http://purl.org/syndication/thread/1.0\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\"><title type=\"text\">${getTitle}</title><snx:timezone>${getTimezone}</snx:timezone><snx:handle>${getHandle}</snx:handle><summary type=\"html\">${getSummary}</summary>${getTags}</entry>";
	var BlogPostTmpl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\"  xmlns:thr=\"http://purl.org/syndication/thread/1.0\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\"><title type=\"text\">${getTitle}</title><content type=\"html\">${getContent}</content>${getTags}</entry>";
	var BlogCommentTmpl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\"  xmlns:thr=\"http://purl.org/syndication/thread/1.0\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\"><content type=\"html\">${getContent}</content></entry>";
	CategoryTmpl = "<category term=\"${tag}\"></category>";
	var CategoryBlog = "<category term=\"blog\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category>";
	var CategoryPerson = "<category term=\"person\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category>";
    
    /**
     * Blog class represents an entry for a Blogs feed returned by the
     * Connections REST API.
     * 
     * @class Blog
     * @namespace sbt.connections
     */
    var Blog = declare(AtomEntity, {

    xpath : consts.BlogXPath,
    namespaces : consts.BlogNamespaces,
    categoryScheme : CategoryBlog,
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
        	var blogUuidPrefix = "urn:lsid:ibm.com:blogs:blog-";
        	var blogUuid = this.getAsString("blogUuid");
        	if(blogUuid && blogUuid.indexOf(blogUuidPrefix) != -1){
        		blogUuid = blogUuid.substring(blogUuidPrefix.length, blogUuid.length);
        	}
            return blogUuid;
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
         * Return the value of IBM Connections blog URL from blog ATOM
         * entry document.
         * 
         * @method getBlogUrl
         * @return {String} Blog URL of the blog
         */
        getBlogUrl : function() {
            return this.getAsString("alternateUrl");
        },

        /**
         * Return tags of IBM Connections blog
         * document.
         * 
         * @method getTags
         * @return {Object} Array of tags of the blog
         */
        getTags : function() {
            return this.getAsArray("tags");
        },

        /**
         * Set new tags to be associated with this IBM Connections blog.
         * 
         * @method setTags
         * @param {Object} Array of tags to be added to the blog
         */

        setTags : function(tags) {
            return this.setAsArray("tags", tags);
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
            var requestArgs = lang.mixin({}, args || {});
            var options = {
                handleAs : "text",
                query : requestArgs
            };
            var url = null;
            
            url = this.service.constructUrl(consts.AtomBlogInstance, null, {
            	blogUuid : blogUuid
			});
            return this.service.getEntity(url, options, blogUuid, callbacks);
        },

        /**
         * Remove this blog
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        remove : function(args) {
            return this.service.deleteBlog(this.getBlogUuid(), args);
        },

        /**
         * Update this blog
         * 
         * @method update
         * @param {Object} [args] Argument object
         */
        update : function(args) {
            return this.service.updateBlog(this, args);
        },
        
        /**
         * Save this blog
         * 
         * @method save
         * @param {Object} [args] Argument object
         */
        save : function(args) {
            if (this.getBlogUuid()) {
                return this.service.updateBlog(this, args);
            } else {
                return this.service.createBlog(this, args);
            }
        }

    });

    /**
     * BlogPost class represents a post for a Blogs feed returned by the
     * Connections REST API.
     * 
     * @class BlogPost
     * @namespace sbt.connections
     */
    var BlogPost = declare(AtomEntity, {

    	xpath : consts.BlogPostXPath,
    	namespaces : consts.BlogPostNamespaces,
    	
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
         * @method getBlogPostUuid
         * @return {String} ID of the blog post
         */
        getBlogPostUuid : function() {
        	var postUuidPrefix = "urn:lsid:ibm.com:blogs:entry-";
        	var postUuid = this.getAsString("postUuid");
        	if(postUuid && postUuid.indexOf(postUuidPrefix) != -1){
        		postUuid = postUuid.substring(postUuidPrefix.length, postUuid.length);
        	}
            return postUuid;
        },

        /**
         * Sets id of IBM Connections blog post Id.
         * 
         * @method setBlogPostUuid
         * @param {String} BlogPostUuid of the blog post
         */
        setBlogPostUuid : function(postUuid) {
            return this.setAsString("postUuid", postUuid);
        },
        
        /**
         * Return the bloghandle of the blog post.
         * 
         * @method getBlogHandle
         * @return {String} Blog handle of the blog post
         */
        getBlogHandle : function() {
        	var blogHandle = this.getAsString("blogHandle");
        	if(blogHandle){
        		return blogHandle;
        	}
        	var blogEntryUrlAlternate = this.getAsString("alternateUrl");
        	blogHandle = this.service._extractBlogHandle(blogEntryUrlAlternate);
            return blogHandle;
        },

        /**
         * Sets blog handle of IBM Connections blog post.
         * 
         * @method setBlogHandle
         * @param {String} blogHandle of the blog post's blog
         */
        setBlogHandle : function(blogHandle) {
            return this.setAsString("blogHandle", blogHandle);
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
         * Return tags of IBM Connections blog post
         * document.
         * 
         * @method getTags
         * @return {Object} Array of tags of the blog post
         */
        getTags : function() {
            return this.getAsArray("tags");
        },

        /**
         * Set new tags to be associated with this IBM Connections blog post.
         * 
         * @method setTags
         * @param {Object} Array of tags to be added to the blog post
         */

        setTags : function(tags) {
            return this.setAsArray("tags", tags);
        },
        
        /**
         * Return the last updated dateRecomendations URL of the IBM Connections blog post from
         * blog ATOM entry document.
         * 
         * @method getRecomendationsURL
         * @return {String} Recomendations URL of the Blog Post
         */
        getRecomendationsURL : function() {
            return this.getAsString("recomendationsUrl");
        },
        
        /**
         * Return the Recomendations count of the IBM Connections blog post from
         * blog ATOM entry document.
         * 
         * @method getRecomendationsCount
         * @return {String} Last updated date of the Blog post
         */
        getRecomendationsCount : function() {
            return this.getAsString("rankRecommendations");
        },
        
        /**
         * Return the comment count of the IBM Connections blog post from
         * blog ATOM entry document.
         * 
         * @method getCommentCount
         * @return {String} Last updated date of the Blog post
         */
        getCommentCount : function() {
            return this.getAsString("rankComment");
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
        },

        /**
         * Loads the blog post object with the atom entry associated with the
         * blog post. By default, a network call is made to load the atom entry
         * document in the blog post object.
         * 
         * @method load
         * @param {Object} [args] Argument object
         */
        load : function(args) {
            // detect a bad request by validating required arguments
            var blogPostUuid = this.getBlogPostUuid();
            var promise = this.service._validateBlogUuid(blogPostUuid);
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
                        xpath : consts.BlogPostXPath
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
            url = this.service.constructUrl(consts.AtomBlogPostInstance, null, {
            	postUuid : blogPostUuid
			});
            return this.service.getEntity(url, options, blogPostUuid, callbacks);
        },

        /**
         * Remove this blog post
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        remove : function(args) {
            return this.service.deletePost(this.getBlogPostUuid(), args);
        },

        /**
         * Update this blog post
         * 
         * @method update
         * @param {Object} [args] Argument object
         */
        update : function(args) {
            return this.service.updatePost(this, args);
        },
        
        /**
         * Save this blog post
         * 
         * @method save
         * @param {Object} [args] Argument object
         */
        save : function(args) {
            if (this.getBlogPostUuid()) {
                return this.service.updatePost(this, args);
            } else {
                return this.service.createPost(this, args);
            }
        }
    });
    
    /**
     * Comment class represents a comment on a Blogs post returned by the
     * Connections REST API.
     * 
     * @class Comment
     * @namespace sbt.connections
     */
    var Comment = declare(AtomEntity, {
    	
    	xpath : consts.CommentXPath,
    	namespaces : consts.BlogNamespaces,

        /**
         * Construct a Blog Post Comment.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },

        /**
         * Return the value of IBM Connections blog post comment id
         * entry document.
         * 
         * @method getCommentUuid
         * @return {String} id of the blog post comment
         */
        getCommentUuid : function() {
        	var commentUuidPrefix = "urn:lsid:ibm.com:blogs:comment-";
        	var commentUuid = this.getAsString("commentUuid");
        	if(commentUuid && commentUuid.indexOf(commentUuidPrefix) != -1){
        		commentUuid = commentUuid.substring(commentUuidPrefix.length, commentUuid.length);
        	}
            return commentUuid;
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
         * Return the last updated dateRecomendations URL of the IBM Connections blog post comment from
         * blog ATOM entry document.
         * 
         * @method getRecomendationsURL
         * @return {String} Recomendations URL of the Blog Post comment
         */
        getRecomendationsURL : function() {
            return this.getAsString("recomendationsUrl");
        },
        
        /**
         * Return the Recomendations count of the IBM Connections blog post comment from
         * blog ATOM entry document.
         * 
         * @method getRecomendationsCount
         * @return {String} Number of recommendations for the Blog post comment
         */
        getRecomendationsCount : function() {
            return this.getAsString("rankRecommendations");
        },
        
        /**
         * Return the get-reply-to of the IBM Connections blog post comment from
         * blog ATOM entry document.
         * 
         * @method getReplyTo
         * @return {String} Entity Id of the entity in reply to which comment was created
         */
        getReplyTo : function() {
            return this.getAsString("replyTo");
        },
        
        /**
         * Return the postUuid of the blog post on which comment was created.
         * 
         * @method getPostUuid
         * @return {String} Blog post Id of the entity in reply to which comment was created
         */
        getPostUuid : function() {
        	var postUuid = this.getAsString("blogPostUuid");
        	if(postUuid){
        		return postUuid;
        	}
        	var postUuidPrefix = "urn:lsid:ibm.com:blogs:entry-";
        	postUuid = this.getAsString("replyTo");
        	if(postUuid && postUuid.indexOf(postUuidPrefix) != -1){
        		postUuid = postUuid.substring(postUuidPrefix.length, postUuid.length);
        	}
            return postUuid;
        },

        /**
         * Sets blog post id of IBM Connections blog post comment.
         * 
         * @method setBlogPostUuid
         * @param {String} blogPostUuid of the comment's blog post
         */
        setBlogPostUuid : function(blogPostUuid) {
            return this.setAsString("blogPostUuid", blogPostUuid);
        },
        
        /**
         * Return the bloghandle of the blog post on which comment was created.
         * 
         * @method getBlogHandle
         * @return {String} Blog handle of the entity in reply to which comment was created
         */
        getBlogHandle : function() {
        	var blogHandle = this.getAsString("blogHandle");
        	if(blogHandle){
        		return blogHandle;
        	}
        	var commentUrlAlternate = this.getAsString("alternateUrl");
        	var blogHandle = this.service._extractBlogHandle(commentUrlAlternate);
            return blogHandle;
        },

        /**
         * Sets blog handle of IBM Connections blog post comment.
         * 
         * @method setBlogHandle
         * @param {String} blogHandle of the comments's blog
         */
        setBlogHandle : function(blogHandle) {
        	this.setAsString("blogHandle", blogHandle);
            return this.setAsString("blogHandle", blogHandle);
        },
        
        /**
         * Return the Trackback Title of the IBM Connections blog post comment from
         * blog ATOM entry document.
         * 
         * @method getTrackbackTitle
         * @return {String} TrackbackTitle of the Blog post comment
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
        load : function(blogHandle, commentUuid, args) {
            var promise = this.service._validateUuid(commentUuid);
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
				commentUuid : commentUuid
			});
            return this.service.getEntity(url, options, commentUuid, callbacks);
        },

        /**
         * Remove this blog post comment
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        remove : function(args) {
            return this.service.deleteComment(this.getCommentUuid(), args);
        },
        
        /**
         * Save this blog post comment
         * 
         * @method save
         * @param {Object} [args] Argument object
         */
        save : function(args) {
            return this.service.createPost(this, args);
        }

    });
    
    /**
     * Recommender class represents a recommender of a Blogs post returned by the
     * Connections REST API.
     * 
     * @class Recommender
     * @namespace sbt.connections
     */
    var Recommender = declare(AtomEntity, {

    	xpath : consts.RecommendersXPath,
    	categoryScheme : CategoryPerson,
    	namespaces : consts.BlogNamespaces,
    	
        /**
         * Construct a Blog Post Recommender.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },

        /**
         * Return the value of IBM Connections blog post recommender id
         * 
         * @method getRecommenderUuid
         * @return {String} id of the blog post recommender
         */
        getRecommenderUuid : function() {
        	var recommenderUuidPrefix = "urn:lsid:ibm.com:blogs:person-";
        	var recommenderUuid = this.getAsString("recommenderUuid");
        	if(recommenderUuid && recommenderUuid.indexOf(recommenderUuidPrefix) != -1){
        		recommenderUuid = recommenderUuid.substring(recommenderUuidPrefix.length, recommenderUuid.length);
        	}
            return recommenderUuid;
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
            return new Blog({
                service : service,
                data : data
            });
       }
    };

    /*
     * Callbacks used when reading a feed that contains forum recommendation entries.
     */
    var RecommendBlogPostCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service : service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ForumsFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            return new BlogPost({
                service : service,
                data : data
            });
       }
    };
    
    /*
     * Callbacks used when reading a feed that contains blog entries.
     */
    var ConnectionsTagsFeedCallbacks = {
		createEntities : function(service,data,response) {
            return new XmlDataHandler({
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.TagsXPath
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = new XmlDataHandler({
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.TagsXPath
            });
            return new Tag({
                service : service,
                id : entryHandler.getEntityId(),
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
            return new BlogPost({
                service : service,
                data : data
            });
       }
    };
    
    /*
     * Callbacks used when reading a feed that contains blog entries.
     */
    var ConnectionsBlogPostRecommendersCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
//                xpath : consts.RecommendersFeedXpath
                xpath : consts.BlogFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            return new Recommender({
                service : service,
                data : data
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
            return new Comment({
                service : service,
                data : data
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
            url = this.constructUrl(consts.AtomBlogEntries, null, {
				blogHandle : blogHandle
			});
            return this.getEntities(url, options, this.getBlogPostsCallbacks());
        },
        
        /**
         * Get the blog posts recommenders feed to see a list of recommenders of a blog post.
         * 
         * @method getBlogPostRecommenders
         * @param {Object} object representing a blog post.
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of posts of a blog . The
         * parameters must be exactly as they are supported by IBM
         * Connections like ps, sortBy etc.
         */
        getBlogPostRecommenders : function(blogPost, args) {
        	var post = this._toBlogPost(blogPost);
            var promise = this._validateBlogPost(post);
            if (promise) {
                return promise;
            }
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            var url = null;
            url = this.constructUrl(consts.AtomRecommendBlogPost, null, {
            	postUuid : post.getBlogPostUuid(),
				blogHandle : post.getBlogHandle()
			});
            return this.getEntities(url, options, this.getBlogPostRecommendersCallbacks());
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
         * Retrieve a blog instance.
         * 
         * @method getBlog
         * @param {String } blogUuid
         * @param {Object} args Object containing the query arguments to be 
         * sent (defined in IBM Connections Blogs REST API) 
         */
        getBlog : function(blogUuid, args) {
            var blog = new Blog({
                service : this,
                _fields : { blogUuid : blogUuid }
            });
            return blog.load(args);
        },

        /**
         * Create a blog by sending an Atom entry document containing the 
         * new blog.
         * 
         * @method createBlog
         * @param {String/Object} blogOrJson Blog object which denotes the blog to be created.
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
         * @param {String/Object} blogOrJson Blog object
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
            var blogUuid = blog.getBlogUuid();
            var url = null;
			url = this.constructUrl(consts.AtomBlogEditDelete, null, {
				blogUuid : blogUuid
			});
            return this.updateEntity(url, options, callbacks, args);
        },

        /**
         * Delete a blog, use the HTTP DELETE method.
         * 
         * @method deleteBlog
         * @param {String} blogUuid blog id of the blog or the blog object (of the blog to be deleted)
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
				blogUuid : blogUuid
			});
            return this.deleteEntity(url, options, blogUuid);
        },
        
        /**
         * Retrieve a blog post instance.
         * 
         * @method getBlogPost
         * @param {String} blogPostUuid blog post id
         * @param {Object} args Object containing the query arguments to be 
         * sent (defined in IBM Connections Blogs REST API) 
         */
        getBlogPost : function(blogPostUuid, args) {
            var blogPost = new BlogPost({
                service : this,
                _fields : { postUuid : blogPostUuid }
            });
            return blogPost.load(args);
        },

        /**
         * Create a blog post by sending an Atom entry document containing the 
         * new blog post.
         * 
         * @method createPost
         * @param {String/Object} postOrJson Blog post object which denotes the blog post to be created.
         * @param {Object} [args] Argument object
         */
        createPost : function(postOrJson, args) {
            var post = this._toBlogPost(postOrJson);
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
                        xpath : consts.BlogPostXPath
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
                data : this._constructBlogPostPostData(post)
            };
            var url = null;
            url = this.constructUrl(consts.AtomBlogPostCreate, null, {
				blogHandle : postOrJson.getBlogHandle()
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
         * @param {String/Object} BlogPost object
         * @param {Object} [args] Argument object
         */
        updatePost : function(postOrJson, args) {
            var post = this._toBlogPost(postOrJson);
            var promise = this._validateBlogPost(post, true, args);
            if (promise) {
                return promise;
            }
            
            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
            	var postUuid = post.getBlogPostUuid();
            	if (data) {
            		var dataHandler = new XmlDataHandler({
                        service :  service,
                        data : data,
                        namespaces : consts.Namespaces,
                        xpath : consts.BlogPostXPath
                    });
            		post.setDataHandler(dataHandler);
            	}
            	post.setBlogPostUuid(postUuid);
                return post;
            };

            var requestArgs = lang.mixin({}, args || {});
            
            var options = {
                method : "PUT",
                query : requestArgs,
                headers : consts.AtomXmlHeaders,
                data : this._constructBlogPostPostData(post)
            };
            var url = null;
			url = this.constructUrl(consts.AtomBlogPostEditDelete, null, {
				postUuid : post.getBlogPostUuid(),
				blogHandle : post.getBlogHandle()
			});
            return this.updateEntity(url, options, callbacks, args);
        },

        /**
         * Recommend a post
         * 
         * @method recommendPost
         * @param {String/Object} BlogPost object
         * @param {String} blogHandle Id of post
         */
        recommendPost : function(blogPost) {
        	var post = this._toBlogPost(blogPost);
            var promise = this._validateBlogPost(post);
            if (promise) {
                return promise;
            }
            var options = {
                method : "POST",
                headers : consts.AtomXmlHeaders
            };
            var url = null;
			url = this.constructUrl(consts.AtomRecommendBlogPost, null, {
				postUuid : post.getBlogPostUuid(),
				blogHandle : post.getBlogHandle()
			});
			
            return this.updateEntity(url, options, this.getRecommendBlogPostCallbacks());
        },

        /**
         * Unrecommend a post
         * 
         * @method unRecommendPost
         * @param {String/Object} blogPost object
         * @param {String} blogHandle Id of post
         */
        unrecommendPost : function(blogPost) {
        	var post = this._toBlogPost(blogPost);
            var promise = this._validateBlogPost(post);
            if (promise) {
                return promise;
            }
            var options = {
                method : "DELETE",
                headers : consts.AtomXmlHeaders
            };
            var url = null;
			url = this.constructUrl(consts.AtomRecommendBlogPost, null, {
				postUuid : post.getBlogPostUuid(),
				blogHandle : post.getBlogHandle()
			});
            return this.deleteEntity(url, options, blogPost.getBlogPostUuid());
        },

        /**
         * Delete a blog post, use the HTTP DELETE method.
         * 
         * @method deletePost
         * @param {String/Object} blogHandle handle of the blog(of which the post is to be deleted)
         * @param {String/Object} postUuid post id of the blog post(of the blog post to be deleted)
         * @param {Object} [args] Argument object
         */
        deletePost : function(blogHandle, postUuid, args) {
            var promise = this._validateUuid(postUuid);
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
				postUuid : postUuid
			});
            return this.deleteEntity(url, options, postUuid);
        },

        /**
         * Delete a blog comment, use the HTTP DELETE method.
         * 
         * @method deleteComment
         * @param {String/Object} blogHandle handle of the blog(of which the post is to be deleted)
         * @param {String/Object} comment id of the blog comment(of the blog comment to be deleted)
         * @param {Object} [args] Argument object
         */
        deleteComment : function(blogHandle, commentUuid, args) {
            var promise = this._validateUuid(commentUuid);
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
				commentUuid : commentUuid
			});
            return this.deleteEntity(url, options, commentUuid);
        },
        
        /**
         * Create a comment post by sending an Atom entry document containing the 
         * new blog comment.
         * 
         * @method createComment
         * @param {String/Object} commentOrJson Blog comment object.
         * @param {Object} [args] Argument object
         */
        createComment : function(commentOrJson, args) {
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
                data : this._constructCommentBlogPostData(comment)
            };
            var url = null;
			url = this.constructUrl(consts.AtomBlogCommentCreate, null, {
				blogHandle : commentOrJson.getBlogHandle(),
				postUuid : commentOrJson.getPostUuid()
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
         * sent (defined in IBM Connections Blogs REST API) 
         */
        getComment : function(blogHandle, commentUuid, args) {
            var comment = new Comment({
                service : this,
                _fields : { commentUuid : commentUuid }
            });
            return comment.load(blogHandle, commentUuid, args);
        },

        /**
         * Get the tags feed to see a list of the tags for all blogs.
         * 
         * @method getBlogsTags
         * @param {Object} [args] Object representing various parameters. 
         * The parameters must be exactly as they are supported by IBM
         * Connections.
         */
        getBlogsTags : function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            
            return this.getEntities(consts.AtomBlogsTags, options, this.getTagsFeedCallbacks(), args);
        },

        /**
         * Get the tags feed to see a list of the tags for a perticular blog.
         * 
         * @method getBlogTags
         * @param {String} blogHandle handle of the blog
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of blog tags. The
         * parameters must be exactly as they are supported by IBM
         * Connections.
         */
        getBlogTags : function(blogHandle, args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            var url = null;
			url = this.constructUrl(consts.AtomBlogTags, null, {
				blogHandle : blogHandle
			});
            return this.getEntities(url, options, this.getTagsFeedCallbacks(), args);
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
         * @method newBlogPost
         * @param {Object} args Object containing the fields for the 
         * new post 
         */
        newBlogPost : function(args) {
            return this._toBlogPost(args);
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
        getTagsFeedCallbacks: function() {
            return ConnectionsTagsFeedCallbacks;
        },
        
        /*
         * Callbacks used when reading a feed that contains Blog entries.
         */
        getRecommendBlogPostCallbacks: function() {
            return RecommendBlogPostCallbacks;
        },
        
        /*
         * Callbacks used when reading a feed that contains Blog entries.
         */
        getBlogPostsCallbacks: function() {
            return ConnectionsBlogPostsCallbacks;
        },
        
        /*
         * Callbacks used when reading a feed that contains Blog entries.
         */
        getBlogPostRecommendersCallbacks: function() {
            return ConnectionsBlogPostRecommendersCallbacks;
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
         * Return a BlogPost instance from BlogPost or JSON or String. Throws
         * an error if the argument was neither.
         */
        _toBlogPost : function(postOrJsonOrString) {
            if (postOrJsonOrString instanceof BlogPost) {
                return postOrJsonOrString;
            } else {
                if (lang.isString(postOrJsonOrString)) {
                    postOrJsonOrString = {
                        postUuid : postOrJsonOrString
                    };
                }
                return new BlogPost({
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
        _validateUuid : function(postUuid) {
            if (!postUuid) {
                return this.createBadRequestPromise("Invalid argument, blog post id must be specified.");
            }
        },
        
        /*
         * Validate a post, and return a Promise if invalid.
         */
        _validateBlogPost : function(post) {
            if (!post || !post.getTitle()) {
                return this.createBadRequestPromise("Invalid argument, blog post with title must be specified.");
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
        _constructBlogPostPostData : function(post) {
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
        _constructCommentBlogPostData : function(comment) {
            var transformer = function(value,key) {
                return value;
            };
            
            var postData = stringUtil.transform(BlogCommentTmpl, comment, transformer, comment);
            return stringUtil.trim(postData);
        },
        
        /*
         * Extract Blog handle from comment source url
         */
        _extractBlogHandle : function(source) {
        	var urlSuffix = "/entry/";
        	source = source.substring(0,source.indexOf(urlSuffix));
        	var bloghandle = source.substring(source.lastIndexOf("/")+1,source.length);
        	return bloghandle;
        	
        }
        
    });
    return BlogService;
});
