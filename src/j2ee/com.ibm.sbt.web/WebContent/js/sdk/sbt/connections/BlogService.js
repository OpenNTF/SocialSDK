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
        	blogs : "blogs"
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

    /**
     * BlogService class.
     * 
     * @class BlogService
     * @namespace sbt.connections
     */
    var BlogService = declare(BaseService, {
    	
    	contextRootMap: {},
    	
        /**
         * Constructor for BlogService
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        	
            if (!this.endpoint) {
                this.endpoint = config.findEndpoint(this.getDefaultEndpointName());
                if(!this.endpoint.serviceMappings.blogHandle){
                	this.contextRootMap.blogHandle = this.getDefaultHandle();
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
        
        /*
         * Callbacks used when reading a feed that contains Blog entries.
         */
        getBlogFeedCallbacks: function() {
            return ConnectionsBlogFeedCallbacks;
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
