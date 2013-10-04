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
 * The Wikis application of IBM® Connections enables teams to create a shared repository of information. 
 * The Wikis API allows application programs to create new wikis, and to read and modify existing wikis.
 * 
 * @module sbt.connections.WikiService
 */
define([ "../declare", "../config", "../lang", "../stringUtil", "../Promise", "./WikiConstants", "../base/BaseService",
         "../base/AtomEntity", "../base/XmlDataHandler" ], 
    function(declare,config,lang,stringUtil,Promise,consts,BaseService,AtomEntity,XmlDataHandler) {
	
    /**
     * Wiki class represents an entry for a Wiki feed returned by the
     * Connections REST API.
     * 
     * @class Wiki
     * @namespace sbt.connections
     */
    var Wiki = declare(AtomEntity, {
    	
    	xpath : consts.WikiXPath,
    	contentType : "html",
    	
        /**
         * Construct a Wiki entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },
        
        /**
         * Return the value of id from Wiki ATOM
         * entry document.
         * 
         * @method getWikiUuid
         * @return {String} Id of the Wiki
         */
        getWikiUuid : function() {
            var uid = this.getAsString("wikiUuid");
            return extractWikiUuid(uid);
        },

        /**
         * Sets id of IBM Connections Wiki.
         * 
         * @method setWikiUuid
         * @param {String} Id of the Wiki
         */
        setWikiUuid : function(forumUuid) {
            return this.setAsString("wikiUuid", forumUuid);
        },

        /**
         * Identifies the set of permissions available for the wiki.
         */
        getPermissions : function() {
        	return this.getAsString("permissions");
        },

        /**
         * Identifies the wiki as a community wiki and identifies the community to 
         * which it belongs by the community’s unique ID.
         */
        getCommunityUuid : function() {
        	return this.getAsString("communityUuid");
        },

        /**
         * Return extra entry data to be included in post data for this entity.
         * 
         * @returns {String}
         */
        createEntryData : function() {
        }       
    });
    
    /*
     * Method used to extract the wiki uuid for an id string.
     */
    var extractWikiUuid = function(uid) {
        if (uid && uid.indexOf("urn:lsid:ibm.com:td:") == 0) {
            return uid.substring("urn:lsid:ibm.com:td:".length);
        } else {
            return uid;
        }
    }; 
        
    /*
     * Callbacks used when reading a feed that contains wiki entries.
     */
    var WikiFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service : service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.WikiFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            return new Wiki({
                service : service,
                data : data
            });
        }
    };
    
    /**
     * WikisService class.
     * 
     * @class WikisService
     * @namespace sbt.connections
     */
    var WikiService = declare(BaseService, {

        contextRootMap: {
            wikis: "wikis"
        },
        
        /**
         * Constructor for WikisService
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
         * Create a Wiki object with the specified data.
         * 
         * @method newWiki
         * @param {Object} args Object containing the fields for the 
         * new Wiki 
         */
        newWiki : function(args) {
            return this._toWiki(args);
        },
        
        /**
         * This retrieves a list of wikis to which the authenticated user has access.
         * 
         * @method getAllWikis
         * @param requestArgs
         */
        getAllWikis: function(requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs || {}
            };
            
            return this.getEntities(consts.WikisAll, options, WikiFeedCallbacks);
        },
                
        /**
         * This retrieves a list of wikis to which everyone who can log into the Wikis application has access. 
         * 
         * @method getPublicWikis
         * @param requestArgs
         */
        getPublicWikis: function(requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs || {}
            };
            
            return this.getEntities(consts.WikisPublic, options, WikiFeedCallbacks);
        },
                
        /**
         * This retrieves a list of wikis of which the authenticated user is a member. 
         * 
         * @method getMyWikis
         * @param requestArgs
         */
        getMyWikis: function(requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs || {}
            };
            
            return this.getEntities(consts.WikisMy, options, WikiFeedCallbacks);
        },
                
        //
        // Internals
        //

        /**
         * Move this to BaseService
         */
        constructUrl : function(url,params,urlParams) {
            if (!url) {
                throw new Error("BaseService.constructUrl: Invalid argument, url is undefined or null.");
            }
            
            var _urlParams = lang.mixin(
            		{ authType: this.endpoint.authType }, 
            		this.contextRootMap, 
            		this.endpoint.serviceMappings,
            		urlParams || {});
            url = stringUtil.replace(url, _urlParams);

            if (params) {
                for (param in params) {
                    if (url.indexOf("?") == -1) {
                        url += "?";
                    } else if (url.indexOf("&") != (url.length - 1)) {
                        url += "&";
                    }
                    var value = encodeURIComponent(params[param]);
                    if (value) {
                        url += param + "=" + value;
                    }
                }
            }
            return url;
        },
        
        /*
         * Validate a wiki and return a Promise if invalid.
         */
        _validateWiki : function(wiki,checkUuid) {
            if (!wiki || !wiki.getTitle()) {
                return this.createBadRequestPromise("Invalid argument, wiki with title must be specified.");
            }
            if (checkUuid && !wiki.getWikiUuid()) {
                return this.createBadRequestPromise("Invalid argument, wiki with UUID must be specified.");
            }
        },
        
        /*
         * Validate a wiki UUID, and return a Promise if invalid.
         */
        _validateWikiUuid : function(wikiUuid) {
            if (!wikiUuid || wikiUuid.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected wikiUuid.");
            }
        },
        
        /*
         * Return a Wiki instance from Wiki or JSON or String. Throws
         * an error if the argument was neither.
         */
        _toWiki : function(wikiOrJsonOrString) {
            if (wikiOrJsonOrString instanceof Wiki) {
                return wikiOrJsonOrString;
            } else {
                if (lang.isString(wikiOrJsonOrString)) {
                    wikiOrJsonOrString = {
                        wikiUuid : wikiOrJsonOrString
                    };
                }
                return new Wiki({
                    service : this,
                    _fields : lang.mixin({}, wikiOrJsonOrString)
                });
            }
        }
        
    });
    return WikiService;
});
