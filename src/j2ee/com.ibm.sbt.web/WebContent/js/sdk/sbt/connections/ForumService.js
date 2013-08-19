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
         * Construct a Result entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },

        /**
         * Return the value of id from result ATOM
         * entry document.
         * 
         * @method getId
         * @return {String} ID of the result
         */
        getId : function() {
            return this.getAsString("id");
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
         * Construct a Result entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },

        /**
         * Return the value of id from result ATOM
         * entry document.
         * 
         * @method getId
         * @return {String} ID of the result
         */
        getId : function() {
            return this.getAsString("id");
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
         * Construct a Result entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },

        /**
         * Return the value of id from result ATOM
         * entry document.
         * 
         * @method getId
         * @return {String} ID of the result
         */
        getId : function() {
            return this.getAsString("id");
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
         * Construct a Result entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },

        /**
         * Return the value of id from result ATOM
         * entry document.
         * 
         * @method getId
         * @return {String} ID of the result
         */
        getId : function() {
            return this.getAsString("id");
        }        
    });
    
    /*
     * Callbacks used when reading a feed that contains forums entries.
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
        }
    });
    return ForumService;
});
