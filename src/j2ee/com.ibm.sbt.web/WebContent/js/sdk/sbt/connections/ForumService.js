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
 * The Forums application of IBM® Connections enables a team to discuss issues that are pertinent to their work. 
 * The Forums API allows application programs to create new forums, and to read and modify existing forums.
 * 
 * @module sbt.connections.ForumsService
 */
define([ "../declare", "../config", "../lang", "../stringUtil", "../Promise", "./ForumConstants", "../base/BaseService",
         "../base/BaseEntity", "../base/XmlDataHandler" ], 
    function(declare,config,lang,stringUtil,Promise,consts,BaseService,BaseEntity,XmlDataHandler) {

    /**
     * Forum class represents an entry for a forums feed returned by the
     * Connections REST API.
     * 
     * @class Forum
     * @namespace sbt.connections
     */
    var Forum = declare(BaseEntity, {

        /**
         * Construct a Forum entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },

        /**
         * Return the value of id from Forum ATOM
         * entry document.
         * 
         * @method getId
         * @return {String} ID of the Forum
         */
        getId : function() {
            return this.getAsString("id");
        },

        /**
         * Return the value of id from Forum ATOM
         * entry document.
         * 
         * @method getId
         * @return {String} ID of the Forum
         */
        getForumUuid : function() {
            var uid = this.getAsString("uid");
            return extractForumUuid(uid);
        },

        /**
         * Return the value of IBM Connections forum title from forum
         * ATOM entry document.
         * 
         * @method getTitle
         * @return {String} forum title of the forum
         */
        getTitle : function() {
            return this.getAsString("title");
        },

        /**
         * Sets title of IBM Connections forum.
         * 
         * @method setTitle
         * @param {String} title Title of the forum
         */
        setTitle : function(title) {
            return this.setAsString("title", title);
        },
        
        /**
         * Return the value of IBM Connections forum description from
         * forum ATOM entry document.
         * 
         * @method getContent
         * @return {String} forum description of the forum
         */
        getContent : function() {
            return this.getAsString("content");
        },

        /**
         * Sets description of IBM Connections forum.
         * 
         * @method setContent
         * @param {String} content Description of the forum
         */
        setContent : function(content) {
            return this.setAsString("content", content);
        },

        /**
         * Gets an author of IBM Connections forum.
         * 
         * @method getAuthor
         * @return {Member} author Author of the forum
         */
        getAuthor : function() {
            return this.getAsObject([ "authorUserid", "authorName", "authorEmail" ]);
        },

        /**
         * Gets a contributor of IBM Connections forum.
         * 
         * @method getContributor
         * @return {Member} contributor Contributor of the forum
         */
        getContributor : function() {
            return this.getAsObject([ "contributorUserid", "contributorName", "contributorEmail" ]);
        },
        
        /**
         * Return the published date of the IBM Connections forum from
         * forum ATOM entry document.
         * 
         * @method getPublished
         * @return {Date} Published date of the forum
         */
        getPublished : function() {
            return this.getAsDate("published");
        },

        /**
         * Return the last updated date of the IBM Connections forum from
         * forum ATOM entry document.
         * 
         * @method getUpdated
         * @return {Date} Last updated date of the forum
         */
        getUpdated : function() {
            return this.getAsDate("updated");
        },
        
        /**
         * Return the moderation of the IBM Connections forum from
         * forum ATOM entry document.
         * 
         * @method getModeration
         * @return {String} Moderation of the forum
         */
        getModeration : function() {
            return this.getAsDate("moderation");
        },
        
        /**
         * Return the thread count of the IBM Connections forum from
         * forum ATOM entry document.
         * 
         * @method getThreadCount
         * @return {Number} Thread count of the forum
         */
        getThreadCount : function() {
            return this.getAsNumber("threadCount");
        },
        
        /**
         * Return the url of the IBM Connections forum from
         * forum ATOM entry document.
         * 
         * @method getForumUrl
         * @return {String} Url of the forum
         */
        getForumUrl : function() {
            return this.getAsString("forumUrl");
        },
                
        /**
         * Return the value of IBM Connections community ID from community ATOM
         * entry document.
         * 
         * @method getCommunityUuid
         * @return {String} Community ID of the community
         */
        getCommunityUuid : function() {
            return this.getAsString("communityUuid");
        },

        /**
         * Sets id of IBM Connections community.
         * 
         * @method setCommunityUuid
         * @param {String} communityUuid Id of the community
         */
        setCommunityUuid : function(communityUuid) {
            return this.setAsString("communityUuid", communityUuid);
        },

        /**
         * Get a list for forum topics that includes the topics in the specified forum.
         * 
         * @method getTopics
         * @param {Object} args
         */
        getTopics : function(args) {
        	return this.service.getForumTopics(this.getForumUuid(), args);
        }
        
       
    });
    
    /**
     * ForumTopic class represents an entry for a forums topic feed returned by the
     * Connections REST API.
     * 
     * @class ForumTopic
     * @namespace sbt.connections
     */
    var ForumTopic = declare(BaseEntity, {

        /**
         * Construct a ForumTopic entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },

        /**
         * Return the value of id from Forum Topic ATOM
         * entry document.
         * 
         * @method getId
         * @return {String} ID of the Forum Topic
         */
        getId : function() {
            return this.getAsString("id");
        },

        /**
         * Return the value of id from Forum Topic ATOM
         * entry document.
         * 
         * @method getTopicUuid
         * @return {String} ID of the Forum Topic
         */
        getTopicUuid : function() {
            var uid = this.getAsString("uid");
            return extractForumUuid(uid);
        },

        /**
         * Return the value of IBM Connections Forum Topic title from Forum Topic
         * ATOM entry document.
         * 
         * @method getTitle
         * @return {String} Forum Topic title
         */
        getTitle : function() {
            return this.getAsString("title");
        },

        /**
         * Sets title of IBM Connections Forum Topic.
         * 
         * @method setTitle
         * @param {String} title Title of the Forum Topic
         */
        setTitle : function(title) {
            return this.setAsString("title", title);
        },
        
        /**
         * Return the value of IBM Connections Forum Topic description from
         * Forum Topic ATOM entry document.
         * 
         * @method getContent
         * @return {String} Forum Topic description
         */
        getContent : function() {
            return this.getAsString("content");
        },

        /**
         * Sets description of IBM Connections Forum Topic.
         * 
         * @method setContent
         * @param {String} content Description
         */
        setContent : function(content) {
            return this.setAsString("content", content);
        },

        /**
         * Gets an author of IBM Connections Forum Topic.
         * 
         * @method getAuthor
         * @return {Member} author Author of the Forum Topic
         */
        getAuthor : function() {
            return this.getAsObject([ "authorUserid", "authorName", "authorEmail" ]);
        },

        /**
         * Return the published date of the IBM Connections Forum Topic from
         * Forum Topic ATOM entry document.
         * 
         * @method getPublished
         * @return {Date} Published date of the Forum Topic
         */
        getPublished : function() {
            return this.getAsDate("published");
        },

        /**
         * Return the last updated date of the IBM Connections Forum Topic from
         * Forum Topic ATOM entry document.
         * 
         * @method getUpdated
         * @return {Date} Last updated date of the Forum Topic
         */
        getUpdated : function() {
            return this.getAsDate("updated");
        },
        
        /**
         * Return the value of IBM Connections community ID from community ATOM
         * entry document.
         * 
         * @method getCommunityUuid
         * @return {String} Community ID of the community
         */
        getCommunityUuid : function() {
            return this.getAsString("communityUuid");
        },

        /**
         * Sets id of IBM Connections community.
         * 
         * @method setCommunityUuid
         * @param {String} communityUuid Id of the community
         */
        setCommunityUuid : function(communityUuid) {
            return this.setAsString("communityUuid", communityUuid);
        },

        /**
         * Return the url of the IBM Connections forum from
         * forum ATOM entry document.
         * 
         * @method getTopicUrl
         * @return {String} Url of the forum
         */
        getTopicUrl : function() {
            return this.getAsString("alternateUrl");
        },
                
        /**
         * Return the permissions of the IBM Connections forum topic from
         * forum ATOM entry document.
         * 
         * @method getPermisisons
         * @return {String} Permissions of the forum topic
         */
        getPermisisons : function() {
            return this.getAsString("permissions");
        },
                
        /**
         * Get a list for forum replies that includes the replies in this topic.
         * 
         * @method getReplies
         */
        getReplies : function(args) {
        	return this.service.getForumReplies(this.getTopicUuid(), args);
        }
    });
    
    /**
     * ForumReply class represents an entry for a forums reply feed returned by the
     * Connections REST API.
     * 
     * @class ForumReply
     * @namespace sbt.connections
     */
    var ForumReply = declare(BaseEntity, {

        /**
         * Construct a ForumReply entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },

        /**
         * Return the value of id from Forum Reply ATOM
         * entry document.
         * 
         * @method getId
         * @return {String} ID of the Forum Reply
         */
        getId : function() {
            return this.getAsString("id");
        },

        /**
         * Return the value of id from Forum Reply ATOM
         * entry document.
         * 
         * @method getReplyUuid
         * @return {String} ID of the Forum Reply
         */
        getReplyUuid : function() {
            var uid = this.getAsString("uid");
            return extractForumUuid(uid);
        },

        /**
         * Return the value of IBM Connections Forum Reply title from Forum Reply
         * ATOM entry document.
         * 
         * @method getTitle
         * @return {String} Forum Reply title
         */
        getTitle : function() {
            return this.getAsString("title");
        },

        /**
         * Sets title of IBM Connections Forum Reply.
         * 
         * @method setTitle
         * @param {String} title Title of the Forum Reply
         */
        setTitle : function(title) {
            return this.setAsString("title", title);
        },
        
        /**
         * Return the value of IBM Connections Forum Reply description from
         * Forum Reply ATOM entry document.
         * 
         * @method getContent
         * @return {String} Forum Reply description
         */
        getContent : function() {
            return this.getAsString("content");
        },

        /**
         * Sets description of IBM Connections Forum Reply.
         * 
         * @method setContent
         * @param {String} content Description
         */
        setContent : function(content) {
            return this.setAsString("content", content);
        },

        /**
         * Gets an author of IBM Connections Forum Reply.
         * 
         * @method getAuthor
         * @return {Member} author Author of the Forum Reply
         */
        getAuthor : function() {
            return this.getAsObject([ "authorUserid", "authorName", "authorEmail" ]);
        },

        /**
         * Return the published date of the IBM Connections Forum Reply from
         * Forum Reply ATOM entry document.
         * 
         * @method getPublished
         * @return {Date} Published date of the Forum Reply
         */
        getPublished : function() {
            return this.getAsDate("published");
        },

        /**
         * Return the last updated date of the IBM Connections Forum Reply from
         * Forum Reply ATOM entry document.
         * 
         * @method getUpdated
         * @return {Date} Last updated date of the Forum Reply
         */
        getUpdated : function() {
            return this.getAsDate("updated");
        },
        
        /**
         * Return the value of IBM Connections community ID from community ATOM
         * entry document.
         * 
         * @method getCommunityUuid
         * @return {String} Community ID of the community
         */
        getCommunityUuid : function() {
            return this.getAsString("communityUuid");
        },

        /**
         * Sets id of IBM Connections community.
         * 
         * @method setCommunityUuid
         * @param {String} communityUuid Id of the community
         */
        setCommunityUuid : function(communityUuid) {
            return this.setAsString("communityUuid", communityUuid);
        },

        /**
         * Return the url of the IBM Connections Forum Reply reply from
         * forum ATOM entry document.
         * 
         * @method getReplyUrl
         * @return {String} Url of the forum
         */
        getReplyUrl : function() {
            return this.getAsString("alternateUrl");
        },
                
        /**
         * Return the permissions of the IBM Connections Forum Reply from
         * forum ATOM entry document.
         * 
         * @method getPermisisons
         * @return {String} Permissions of the Forum Reply
         */
        getPermisisons : function() {
            return this.getAsString("permissions");
        }
    });
    
    /**
     * ForumTag class represents an entry for a forums tag feed returned by the
     * Connections REST API.
     * 
     * @class ForumTag
     * @namespace sbt.connections
     */
    var ForumTag = declare(BaseEntity, {

        /**
         * Construct a Forum Tag entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },

        /**
         * Return the value of id from Forum Tag ATOM
         * entry document.
         * 
         * @method getId
         * @return {String} ID of the Forum Tag
         */
        getId : function() {
            return this.getAsString("id");
        }        
    });
    
    /*
     * Method used to extract the forum uuid for an id string.
     */
    var extractForumUuid = function(uid) {
        if (uid && uid.indexOf("urn:lsid:ibm.com:forum:") == 0) {
            return uid.substring("urn:lsid:ibm.com:forum:".length);
        } else {
            return uid;
        }
    }; 
    
    /*
     * Callbacks used when reading a feed that contains forum entries.
     */
    var ForumFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service : service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ForumsFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = new XmlDataHandler({
                service : service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ForumXPath
            });
            return new Forum({
                service : service,
                dataHandler : entryHandler
            });
        }
    };

    /*
     * Callbacks used when reading a feed that contains forum topic entries.
     */
    var ForumTopicFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service : service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ForumsFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = new XmlDataHandler({
                service : service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ForumTopicXPath
            });
            return new ForumTopic({
                service : service,
                dataHandler : entryHandler
            });
        }
    };

    /*
     * Callbacks used when reading a feed that contains forum topic reply entries.
     */
    var ForumReplyFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service : service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ForumsFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = new XmlDataHandler({
                service : service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ForumReplyXPath
            });
            return new ForumReply({
                service : service,
                dataHandler : entryHandler
            });
        }
    };

    /**
     * ForumsService class.
     * 
     * @class ForumsService
     * @namespace sbt.connections
     */
    var ForumService = declare(BaseService, {

        /**
         * Constructor for ForumsService
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
            if (!this.endpoint) {
                this.endpoint = config.findEndpoint(this.getDefaultEndpointName());
            }
        },

        /**
         * Return the default endpoint name if client did not specify one.
         * @returns {String}
         */
        getDefaultEndpointName: function() {
            return "connections";
        },
        
        /**
         * Get a feed that includes forums created by the authenticated user or associated with communities to which the user belongs.
         * 
         * @method getMyForums
         * @param requestArgs
         */
        getMyForums: function(requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs || {}
            };
            
            return this.getEntities(consts.AtomForumsMy, options, ForumFeedCallbacks);
        },      
        
        /**
         * Get a feed that includes all stand-alone and community forums created in the enterprise.
         * 
         * @method getAllForums
         * @param requestArgs
         */
        getAllForums: function(requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs || {}
            };
            
            return this.getEntities(consts.AtomForums, options, ForumFeedCallbacks);
        },      
        
        /**
         * Get a feed that includes all stand-alone and community forums created in the enterprise.
         * 
         * @method getAllForums
         * @param requestArgs
         */
        getPublicForums: function(requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs || {}
            };
            
            return this.getEntities(consts.AtomForumsPublic, options, ForumFeedCallbacks);
        },
        
        /**
         * Get a list for forum topics that includes the topics in the specified forum.
         * 
         * @method getForumTopics
         * @param forumUuid
         * @param args
         * @returns
         */
        getForumTopics: function(forumUuid, args) {
            var promise = this._validateForumUuid(forumUuid);
            if (promise) {
                return promise;
            }
            
            var requestArgs = lang.mixin(
            	{ forumUuid : forumUuid }, args || {});
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            
            return this.getEntities(consts.AtomForumTopics, options, ForumTopicFeedCallbacks);
        },
        
        /**
         * Get a list for forum replies that includes the replies in the specified topic.
         * 
         * @method getForumReplies
         * @param topicUuid
         * @param args
         * @returns
         */
        getForumReplies: function(topicUuid, args) {
            var promise = this._validateTopicUuid(topicUuid);
            if (promise) {
                return promise;
            }
            
            var requestArgs = lang.mixin(
            	{ topicUuid : topicUuid }, args || {});
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            
            return this.getEntities(consts.AtomReplies, options, ForumReplyFeedCallbacks);
        },
        
        // Internals
        
        /*
         * Validate a forum UUID, and return a Promise if invalid.
         */
        _validateForumUuid : function(forumUuid) {
            if (!forumUuid || forumUuid.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected forumUuid.");
            }
        },
        
        /*
         * Validate a topic UUID, and return a Promise if invalid.
         */
        _validateTopicUuid : function(topicUuid) {
            if (!topicUuid || topicUuid.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected topicUuid.");
            }
        }
        
    });
    return ForumService;
});
