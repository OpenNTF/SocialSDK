/*
 * © Copyright IBM Corp. 2012, 2013
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
 * 
 * @module sbt.connections.BlogPost
 * @author Rajmeet Bal
 */
define(["../declare", "../base/AtomEntity", "./BlogConstants" ], 
    function(declare, AtomEntity, consts) {

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
         * @deprecated Use getPostUuid instead.
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
         * @deprecated Use setPostUuid instead
         * @method setBlogPostUuid
         * @param {String} BlogPostUuid of the blog post
         */
        setBlogPostUuid : function(postUuid) {
            return this.setAsString("postUuid", postUuid);
        },
        
        /**
         * Return the value of IBM Connections blog post ID.
         * 
         * @method getPostUuid
         * @return {String} Unique id of the blog post
         */
        getPostUuid : function() {
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
         * @method setPostUuid
         * @param {String} Unique id of the blog post
         */
        setPostUuid : function(postUuid) {
            return this.setAsString("postUuid", postUuid);
        },
        
        /**
         * Return the entry anchor for this blog post.
         * 
         * @method getEntryAnchor
         * @return {String} Entry anchor for this blog post
         */
        getEntryAnchor : function() {
        	var entry = this.dataHandler.getData();
        	var base = entry.getAttribute("xml:base");
        	if (base) {
        		var n = base.lastIndexOf("/"); 
        		return base.substring(n+1);
        	}
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
         * Return the replies url of the ATOM entry document.
         * 
         * @method getRepliesUrl
         * @return {String} Replies url
         */
        getRepliesUrl : function() {
            return this.getAsString("repliesUrl");
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
            return this.getAsNumber("rankRecommendations");
        },
        
        /**
         * Return the comment count of the IBM Connections blog post from
         * blog ATOM entry document.
         * 
         * @method getCommentCount
         * @return {String} Last updated date of the Blog post
         */
        getCommentCount : function() {
            return this.getAsNumber("rankComment");
        },
        
        /**
         * Return the hit count of the IBM Connections blog post from
         * blog ATOM entry document.
         * 
         * @method getHitCount
         * @return {String} Last updated date of the Blog post
         */
        getHitCount : function() {
            return this.getAsNumber("rankHit");
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
            	blogHomepageHandle : this.handle,
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
        },
        
        /**
         * Return comments associated with this blog post.
         * 
         * @method getComments
         * @param {Object} [args] Argument object
         */
        getComments : function(args) {
        	var blogHandle = this.getBlogHandle();
        	var entryAnchor = this.getEntryAnchor();
        	return this.service.getEntryComments(blogHandle, entryAnchor, args);
        }
    });
    return BlogPost;
});
