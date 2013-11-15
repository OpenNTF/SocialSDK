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
	
	var CategoryWiki = "<category scheme=\"http://www.ibm.com/xmlns/prod/sn/type\" term=\"wiki\" label=\"wiki\"></category>";
	var CategoryWikiPage = "<category term=\"page\" scheme=\"tag:ibm.com,2006:td/type\" label=\"page\"/>";
	
	var LabelTmpl = "<td:label>${getLabel}</td:label>";
	var PermissionsTmpl = "<td:permissions>${getPermissions}</td:permissions>";
	var CategoryTmpl = "<category term=\"${tag}\" label=\"${tag}\"></category>";
	
    /**
     * Wiki class represents an entry for a Wiki or Wiki Page feed returned by the
     * Connections REST API.
     * 
     * @class BaseWikiEntity
     * @namespace sbt.connections
     */
    var BaseWikiEntity = declare(AtomEntity, {
    	
    	/**
    	 * Set to true to include the label in the post data when 
    	 * performing an update or create operation. By default the 
    	 * label is not sent which will keep it in synch with the
    	 * Wiki or WikiPage title. 
    	 */
    	includeLabelInPost : false,
    	
        /**
         * Construct a BaseWikiEntity entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },
        
        /**
         * Return extra entry data to be included in post data for this entity.
         * 
         * @returns {String}
         */
        createEntryData : function() {
        	var postData = "";
            var transformer = function(value,key) {
                return value;
            };
        	if (this.getLabel() && this.includeLabelInPost) {
                postData += stringUtil.transform(LabelTmpl, this, transformer, this);
        	}
        	if (this.getPermissions()) {
                postData += stringUtil.transform(PermissionsTmpl, this, transformer, this);
        	}
        	if (this.getTags()) {
        		var tags = this.getTags();
                for (var tag in tags) {
                	postData += stringUtil.transform(CategoryTmpl, {
                        "tag" : tags[tag]
                    });
                }
        	}
            return stringUtil.trim(postData);
        },
        
        /**
         * Return an array containing the tags for this wiki.
         * 
         * @method getTags
         * @return {Array}
         */
        getTags : function() {
        	var tags = this.getAsArray("tags");
        	return this.getAsArray("tags");
        },
        
        /**
         * Return an array containing the tags for this wiki.
         * 
         * @method setTags
         * @param {Array}
         */
        setTags : function(tags) {
        	return this.setAsArray("tags", tags);
        },
            
        /**
         * Return the value of id from Wiki ATOM
         * entry document.
         * 
         * @method getWikiUuid
         * @return {String} Id of the Wiki
         */
        getUuid : function() {
            var uid = this.getAsString("uuid");
            return extractWikiUuid(uid);
        },

        /**
         * Sets id of IBM Connections Wiki ATOM
         * entry document.
         * 
         * @method setWikiUuid
         * @param {String} Id of the Wiki
         */
        setUuid : function(uuid) {
            return this.setAsString("uuid", uuid);
        },

        /**
         * Return short text label used to identify the Wiki Entry in API operation resource addresses.
         * 
         * @method getLabel
         * @return {String} short text label used to identify the Wiki Entry in API operation resource addresses.
         */
        getLabel : function() {
            return this.getAsString("label");
        },

        /**
         * Set the short text label used to identify the Wiki Entry in API operation resource addresses.
         * 
         * @method setLabel
         * @param label short text label used to identify the Wiki Entry in API operation resource addresses.
         */
        setLabel : function(label) {
            return this.setAsString("label", label);
        },

        /**
         * Return the set of permissions available for the Wiki Entry.
         * 
         * @method getPermissions
         * @return {String} Permissions available for the Wiki Entry
         */
        getPermissions : function() {
            return this.getAsString("permissions");
        },
                
        /**
         * Set the permissions available for the Wiki Entry.
         * 
         * @method setPermissions
         * @param permissions Permissions available for the Wiki Entry
         */
        setPermissions : function(permissions) {
            return this.setAsString("permissions", permissions);
        },
        
        /**
         * Return modifier of the Wiki Entry.
         * 
         * @method getModifier
         * @return {Object} Modifier of the Wiki Entry 
         */
        getModifier : function() {
            return this.getAsObject(
            		[ "modifierUserid", "modifierName", "modifierEmail", "modifierUserState" ],
            		[ "userid", "name", "email", "userState" ]);
        },

        /**
         * Return the date the wiki was created.
         * 
         * @method getCreated
         * @return {Date} Date the wiki was created 
         */
        getCreated : function() {
            return this.getAsDate("created");
        },

        /**
         * Return the date the wiki was modified.
         * 
         * @method getModified
         * @return {Date} Date the wiki was modified 
         */
        getModified : function() {
            return this.getAsDate("modified");
        }

    });
    
    /**
     * Wiki class represents an entry for a Wiki feed returned by the
     * Connections REST API.
     * 
     * @class Wiki
     * @namespace sbt.connections
     */
    var Wiki = declare(BaseWikiEntity, {
    	
    	xpath : consts.WikiXPath,
    	contentType : "html",
    	categoryScheme : CategoryWiki,
    	
        /**
         * Construct a Wiki entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },
        
        /**
         * Return the community’s unique ID if this wiki belongs to a community.
         * 
         * @method getCommunityUuid
         * @return {String} Uuid of the Community
         */
        getCommunityUuid : function() {
            return this.getAsString("communityUuid");
        },

        /**
         * Set the community’s unique ID if this wiki belongs to a community.
         * 
         * @method setCommunityUuid
         * @param {String} communityUuid Community Uuid of the forum
         * @return {Wiki}
         */
        setCommunityUuid : function(communityUuid) {
            return this.setAsString("communityUuid", communityUuid);
        },
        
        /**
         * return the wiki theme name.
         * 
         * @method getThemeName
         * @return {String} Wiki theme name
         */
        getThemeName : function() {
            return this.getAsString("themeName");
        },

        /**
         * Return the library size.
         * 
         * @method getLibrarySize
         * @return {Number} the library size
         */
        getLibrarySize : function() {
            return this.getAsNumber("librarySize");
        },

        /**
         * Return the library quota.
         * 
         * @method getLibraryQuota
         * @return {Number} the library quota
         */
        getLibraryQuota : function() {
            return this.getAsNumber("libraryQuota");
        },

        /**
         * Return the total removed size.
         * 
         * @method getTotalRemovedSize
         * @return {Number} the total removed size 
         */
        getTotalRemovedSize : function() {
            return this.getAsNumber("totalRemovedSize");
        },
        
        /**
         * Get a list for wiki pages from this wiki.
         * 
         * @method getPages
         * @param {Object} args
         */
        getPages : function(args) {
        	return this.service.getWikiPages(this.getLabel(), args);
        },

        /**
         * Loads the wiki object with the atom entry associated with the
         * wiki. By default, a network call is made to load the atom entry
         * document in the wiki object.
         * 
         * @method load
         * @param {Object} [args] Argument object
         */
        load : function(args) {
            // detect a bad request by validating required arguments
            var label = this.getLabel();
            var promise = this.service._validateWikiLabel(label);
            if (promise) {
                return promise;
            }

            var self = this;
            var callbacks = {
                createEntity : function(service,data,response) {
                    self.setData(data);
                    return self;
                }
            };

            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            
            var url = this.service.constructUrl(consts.WikiEntry, null, { "wikiLabel" : encodeURIComponent(label) });
                
            return this.service.getEntity(url, options, label, callbacks);
        },

        /**
         * Remove this wiki
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        remove : function(args) {
            return this.service.deleteWiki(this.getLabel(), args);
        },

        /**
         * Update this wiki
         * 
         * @method update
         * @param {Object} [args] Argument object
         */
        update : function(args) {
            return this.service.updateWiki(this, args);
        },
        
        /**
         * Save this wiki
         * 
         * @method save
         * @param {Object} [args] Argument object
         */
        save : function(args) {
            if (this.getWikiUuid()) {
                return this.service.updateWiki(this, args);
            } else {
                return this.service.createWiki(this, args);
            }
        }        
        
    });
    
    /**
     * WikiPage class represents an entry for a Wiki Page feed returned by the
     * Connections REST API.
     * 
     * @class WikiPage
     * @namespace sbt.connections
     */
    var WikiPage = declare(BaseWikiEntity, {
    	
    	xpath : consts.WikiPageXPath,
    	contentType : "html",
    	categoryScheme : CategoryWikiPage,
    	wikiLabel : null,
    	
        /**
         * Construct a Wiki entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },
        
        /**
         * Return the wiki label associated with this Wiki Page.
         * 
         * @method getWikiLabel
         * @return {String} wiki label
         */
        getWikiLabel : function() {
        	if (this.wikiLabel) {
        		return this.wikiLabel;
        	} else {
        		return this.getAsString("label");
        	}
        },

        /**
         * Set the wiki label associated with this Wiki Page.
         * 
         * @method setWikiLabel
         * @param {String} wiki label
         * @return {WikiPage}
         */
        setWikiLabel : function(wikiLabel) {
        	this.wikiLabel = wikiLabel;
            return this;
        },
        
        /**
         * Return the last accessed date/time.
         * 
         * @method getLastAccessed
         * @return {Date} the last accessed date/time
         */
        getLastAccessed : function() {
            return this.getAsDate("lastAccessed");
        },

        /**
         * Return the version uuid.
         * 
         * @method getVersionUuid
         * @return {String} the version uuid
         */
        getVersionUuid : function() {
            return this.getAsString("versionUuid");
        },

        /**
         * Return the version label.
         * 
         * @method getVersionLabel
         * @return {String} the version label
         */
        getVersionLabel : function() {
            return this.getAsString("versionLabel");
        },

        /**
         * Return the propagation.
         * 
         * @method getPropagation
         * @return {Boolean} the propagation
         */
        getPropagation : function() {
            return this.getAsBoolean("propagation");
        },

        /**
         * Return the total media size.
         * 
         * @method getTotalMediaSize
         * @return {Number} the total media size
         */
        getTotalMediaSize : function() {
            return this.getAsNumber("totalMediaSize");
        },

        /**
         * Return the number of recommendations.
         * 
         * @method getRecommendations
         * @return {Number} the number of recommendations
         */
        getRecommendations : function() {
            return this.getAsNumber("recommendations");
        },

        /**
         * Return the number of comments.
         * 
         * @method getComments
         * @return {Number} the number of comments
         */
        getComments : function() {
            return this.getAsNumber("comment");
        },

        /**
         * Return the number of hits.
         * 
         * @method getHits
         * @return {Number} the number of hits
         */
        getHits : function() {
            return this.getAsNumber("hit");
        },

        /**
         * Return the number of anonymous hits.
         * 
         * @method getAnonymousHits
         * @return {Number} the number of anonymous hits
         */
        getAnonymousHits : function() {
            return this.getAsNumber("anonymous_hit");
        },

        /**
         * Return the number of shares.
         * 
         * @method getShare
         * @return {Number} the number of shares
         */
        getShare : function() {
            return this.getAsNumber("share");
        },

        /**
         * Return the number of collections.
         * 
         * @method getCollections
         * @return {Number} the number of collections
         */
        getCollections : function() {
            return this.getAsNumber("collections");
        },

        /**
         * Return the number of attachments.
         * 
         * @method getAttachments
         * @return {Number} the number of attachments 
         */
        getAttachments : function() {
            return this.getAsNumber("attachments");
        },

        /**
         * Return the number of versions.
         * 
         * @method getVersions
         * @return {Number} the number of versions
         */
        getVersions : function() {
            return this.getAsNumber("versions");
        },

        /**
         * Loads the Wiki Page object with the atom entry associated with the
         * wiki. By default, a network call is made to load the atom entry
         * document in the Wiki Page object.
         * 
         * @method load
         * @param {Object} [args] Argument object
         */
        load : function(args) {
            // detect a bad request by validating required arguments
            var label = this.getLabel();
            var promise = this.service._validatePageLabel(label);
            if (promise) {
                return promise;
            }
            var wikiLabel = this.getWikiLabel();
            promise = this.service._validateWikiLabel(wikiLabel);
            if (promise) {
                return promise;
            }

            var self = this;
            var callbacks = {
                createEntity : function(service,data,response) {
                    self.setData(data);
                    return self;
                }
            };

            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            
            var urlParams =  { "pageLabel" : encodeURIComponent(label), "wikiLabel" : encodeURIComponent(wikiLabel) };
            var url = this.service.constructUrl(consts.WikiPageEntry, null, urlParams);
                
            return this.service.getEntity(url, options, label, callbacks);
        },

        /**
         * Remove this Wiki Page
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        remove : function(args) {
            return this.service.deleteWikiPage(this.getLabel(), args);
        },

        /**
         * Update this Wiki Page
         * 
         * @method update
         * @param {Object} [args] Argument object
         */
        update : function(args) {
            return this.service.updateWikiPage(this, args);
        },
        
        /**
         * Save this Wiki Page
         * 
         * @method save
         * @param {Object} [args] Argument object
         */
        save : function(args) {
            if (this.getWikiUuid()) {
                return this.service.updateWikiPage(this, args);
            } else {
                return this.service.createWikiPage(this, args);
            }
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
    
    /*
     * Callbacks used when reading a feed that contains wiki page entries.
     */
    var WikiPageFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service : service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.WikiFeedXPath
            });
        }
    };
    
    /*
     * Callbacks used when reading an entry that contains wiki.
     */
    var WikiCallbacks = {
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
         * @param {Object} args Object containing the fields for the new Wiki 
         */
        newWiki : function(args) {
            return this._toWiki(args);
        },
        
        /**
         * Create a Wiki Page object with the specified data.
         * 
         * @method newWikiPage
         * @param {Object} args Object containing the fields for the new Wiki Page
         */
        newWikiPage : function(args) {
            return this._toWikiPage(args);
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
                
        /**
         * This retrieves a list all wikis sorted by wikis with the most comments first.
         * This returns a list of wikis to which everyone who can log into the Wikis application has access.  
         * 
         * @method getMostCommentedWikis
         * @param requestArgs
         */
        getMostCommentedWikis: function(requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs || {}
            };
            
            return this.getEntities(consts.WikisMostCommented, options, WikiFeedCallbacks);
        },
                
        /**
         * This retrieves a list all wikis sorted by wikis with the most recommendations first.
         * This returns a list of wikis to which everyone who can log into the Wikis application has access.  
         * 
         * @method getMostRecommendedWikis
         * @param requestArgs
         */
        getMostRecommendedWikis: function(requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs || {}
            };
            
            return this.getEntities(consts.WikisMostRecommended, options, WikiFeedCallbacks);
        },
                
        /**
         * This retrieves a list all wikis sorted by wikis with the most visits first.
         * This returns a list of wikis to which everyone who can log into the Wikis application has access.  
         * 
         * @method getMostVisitedWikis
         * @param requestArgs
         */
        getMostVisitedWikis: function(requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs || {}
            };
            
            return this.getEntities(consts.WikisMostVisited, options, WikiFeedCallbacks);
        },
                
        /**
         * This retrieves a list all of the pages in a specific wiki.
         * 
         * @method getWikiPages
         * @param wikiLabel Value of the <td:label> element of the wiki.
         * @param requestArgs
         */
        getWikiPages: function(wikiLabel, requestArgs) {
            var promise = this._validateWikiLabel(wikiLabel);
            if (promise) {
                return promise;
            }
        	
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs || {}
            };
            
            var callbacks = lang.mixin(WikiPageFeedCallbacks, {
                createEntity : function(service,data,response) {
                    return new WikiPage({
                        service : service,
                        wikiLabel : wikiLabel,
                        data : data
                    });
                }
            });
            
            var url = this.constructUrl(consts.WikiRecycleBin, null, { "wikiLabel" : encodeURIComponent(wikiLabel) });
            
            return this.getEntities(url, options, callbacks);
        },
                
        /**
         * This retrieves a list all of the pages in a specific wiki that have been added or edited by the authenticated user.
         * 
         * @method geMyWikiPages
         * @param wikiLabel Value of the <td:label> element of the wiki.
         * @param requestArgs
         */
        getMyWikiPages: function(wikiLabel, requestArgs) {
            var promise = this._validateWikiLabel(wikiLabel);
            if (promise) {
                return promise;
            }
        	
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs || {}
            };
            
            var callbacks = lang.mixin(WikiPageFeedCallbacks, {
                createEntity : function(service,data,response) {
                    return new WikiPage({
                        service : service,
                        wikiLabel : wikiLabel,
                        data : data
                    });
                }
            });
                
            var url = this.constructUrl(consts.WikiMyPages, null, { "wikiLabel" : encodeURIComponent(wikiLabel) });
            
            return this.getEntities(url, options, callbacks);
        },
                
        /**
         * This retrieves a list of the pages that have been deleted from wikis and are currently stored in the trash.
         * 
         * @method getRecycledPages
         * @param wikiLabel Value of the <td:label> element or the <id> element of the wiki.
         * @param requestArgs
         */
        getRecycledWikiPages: function(wikiLabelOrId, requestArgs) {
            var promise = this._validateWikiLabel(wikiLabelOrId);
            if (promise) {
                return promise;
            }
        	
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs || {}
            };
            
            var callbacks = lang.mixin(WikiPageFeedCallbacks, {
                createEntity : function(service,data,response) {
                    return new WikiPage({
                        service : service,
                        wikiLabelOrId : wikiLabelOrId,
                        data : data
                    });
                }
            });
                    
            var url = this.constructUrl(consts.WikiRecycleBin, null, { "wikiLabelOrId" : encodeURIComponent(wikiLabelOrId) });
            
            return this.getEntities(url, options, callbacks);
        },
        
        /**
         * Retrieve a wiki.
         * 
         * @method getWiki
         * @param wikiLabel Value of the <td:label> element or the <id> element of the wiki.
         * @param requestArgs
         */
        getWiki: function(wikiLabel, requestArgs) {
            var wiki = new Wiki({
                service : this,
                _fields : { label : wikiLabel }
            });
            return wiki.load(requestArgs);
        },
        
        /**
         * Create a wiki.
         * 
         * @method createWiki
         * @param wikiOrJson
         * @param requestArgs
         */
        createWiki: function(wikiOrJson, requestArgs) {
            var wiki = this._toWiki(wikiOrJson);
            var promise = this._validateWiki(wiki, false, requestArgs);
            if (promise) {
                return promise;
            }

            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
                wiki.setData(data);
                return wiki;
            };

            var options = {
                method : "POST",
                query : requestArgs || {},
                headers : consts.AtomXmlHeaders,
                data : wiki.createPostData()
            };
            
            return this.updateEntity(consts.WikisAll, options, callbacks, requestArgs);
        },
                
        /**
         * Update a wiki.
         * All existing wiki information will be replaced with the new data. To avoid deleting all existing data, 
         * retrieve any data you want to retain first, and send it back with this request. For example, if you want 
         * to add a new tag to a wiki definition entry, retrieve the existing tags, and send them all back with the 
         * new tag in the update request. 
         * 
         * @method updateWiki
         * @param wikiOrJson
         * @param requestArgs
         */
        updateWiki: function(wikiOrJson, requestArgs) {
            var wiki = this._toWiki(wikiOrJson);
            var promise = this._validateWiki(wiki, true, requestArgs);
            if (promise) {
                return promise;
            }
            
            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
                wiki.setData(data);
                return wiki;
            };

            var options = {
                method : "PUT",
                query : requestArgs || {},
                headers : consts.AtomXmlHeaders,
                data : wiki.createPostData()
            };
            
            var url = this.constructUrl(consts.WikiEntry, null, { "wikiLabel" : encodeURIComponent(wiki.getLabel()) });
            
            return this.updateEntity(url, options, callbacks, requestArgs);
        },
                
        /**
         * Delete a wiki.
         * 
         * @method deleteWiki
         * @param wikiLabel
         * @param requestArgs
         */
        deleteWiki: function(wikiLabel, requestArgs) {
            var promise = this._validateWikiLabel(wikiLabel);
            if (promise) {
                return promise;
            }            
           
            var options = {
                method : "DELETE",
                query : requestArgs || {},
                handleAs : "text"
            };
            
            var url = this.constructUrl(consts.WikiEntry, null, { "wikiLabel" : encodeURIComponent(wikiLabel) });
            
            return this.deleteEntity(url, options, wikiLabel);
        },
                
        /**
         * Retrieve a wiki page.
         * 
         * @method getWikiPage
         * @param wikiLabel Value of the <td:label> element or the <id> element of the wiki.
         * @param requestArgs
         */
        getWikiPage: function(wikiLabel, pageLabel, requestArgs) {
            var wikiPage = new WikiPage({
                service : this,
                wikiLabel : wikiLabel,
                _fields : { label : pageLabel }
            });
            return wikiPage.load(requestArgs);
        },
        
        /**
         * Create a wiki page.
         * 
         * @method createWikiPage
         * @param pageOrJson
         * @param requestArgs
         */
        createWikiPage: function(pageOrJson, requestArgs) {
            var wikiPage = this._toWikiPage(pageOrJson);
            var promise = this._validateWikiPage(wikiPage, false, requestArgs);
            if (promise) {
                return promise;
            }

            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
                wiki.setData(data);
                return wiki;
            };

            var options = {
                method : "POST",
                query : requestArgs || {},
                headers : consts.AtomXmlHeaders,
                data : wikiPage.createPostData()
            };
            
            var url = this.constructUrl(consts.WikiFeed, null, { "wikiLabel" : encodeURIComponent(wikiPage.getWikiLabel()) });
            
            return this.updateEntity(url, options, callbacks, requestArgs);
        },
                
        /**
         * Update a wiki page.
         * 
         * @method updateWikiPage
         * @param pageOrJson
         * @param requestArgs
         */
        updateWikiPage: function(pageOrJson, requestArgs) {
            var wikiPage = this._toWikiPage(pageOrJson);
            var promise = this._validateWikiPage(wikiPage, true, requestArgs);
            if (promise) {
                return promise;
            }
            
            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
            	wikiPage.setData(data);
                return wikiPage;
            };

            var options = {
                method : "PUT",
                query : requestArgs || {},
                headers : consts.AtomXmlHeaders,
                data : wikiPage.createPostData()
            };
            
            var urlParams =  { "pageLabel" : encodeURIComponent(wikiPage.getLabel()), "wikiLabel" : encodeURIComponent(wikiPage.getWikiLabel()) };
            var url = this.constructUrl(consts.WikiPageEntry, null, urlParams);
            
            return this.updateEntity(url, options, callbacks, requestArgs);
        },
                
        /**
         * Delete a wiki page.
         * 
         * @method deleteWikiPage
         * @param wikiLabel
         * @param pageLabel
         * @param requestArgs
         */
        deleteWikiPage: function(wikiLabel, pageLabel, requestArgs) {
            var promise = this._validateWikiLabel(wikiLabel);
            if (promise) {
                return promise;
            }            
            promise = this._validatePageLabel(pageLabel);
            if (promise) {
                return promise;
            }            
           
            var options = {
                method : "DELETE",
                query : requestArgs || {},
                handleAs : "text"
            };
            
            var urlParams =  { "pageLabel" : encodeURIComponent(label), "wikiLabel" : encodeURIComponent(wikiLabel) };
            var url = this.constructUrl(consts.WikiPageEntry, null, urlParams);
            
            return this.deleteEntity(url, options, wikiLabel);
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
         * Validate a Wiki and return a Promise if invalid.
         */
        _validateWiki : function(wiki,checkLabel) {
            if (!wiki || !wiki.getTitle()) {
                return this.createBadRequestPromise("Invalid argument, wiki with title must be specified.");
            }
            if (checkLabel && !wiki.getLabel()) {
                return this.createBadRequestPromise("Invalid argument, wiki with label must be specified.");
            }
        },
        
        /*
         * Validate a WikiPage and return a Promise if invalid.
         */
        _validateWikiPage : function(wikiPage,checkLabels) {
            if (!wikiPage || !wikiPage.getTitle()) {
                return this.createBadRequestPromise("Invalid argument, wiki page with title must be specified.");
            }
            if (checkLabels && !wikiPage.getLabel()) {
                return this.createBadRequestPromise("Invalid argument, wiki page with label must be specified.");
            }
            if (checkLabels && !wikiPage.getWikiLabel()) {
                return this.createBadRequestPromise("Invalid argument, wiki page with wiki label must be specified.");
            }
        },
        
        /*
         * Validate a wiki label, and return a Promise if invalid.
         */
        _validateWikiLabel : function(wikiLabel) {
            if (!wikiLabel || wikiLabel.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected wiki label.");
            }
        },
        
        /*
         * Validate a wiki page label, and return a Promise if invalid.
         */
        _validatePageLabel : function(pageLabel) {
            if (!pageLabel || pageLabel.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected wiki page label.");
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
                        handle : wikiOrJsonOrString
                    };
                }
                return new Wiki({
                    service : this,
                    _fields : lang.mixin({}, wikiOrJsonOrString)
                });
            }
        },
        
        /*
         * Return a WikiPage instance from WikiPage or JSON or String. Throws
         * an error if the argument was neither.
         */
        _toWikiPage : function(pageOrJsonOrString) {
            if (pageOrJsonOrString instanceof WikiPage) {
                return pageOrJsonOrString;
            } else {
                if (lang.isString(pageOrJsonOrString)) {
                	pageOrJsonOrString = {
                        handle : pageOrJsonOrString
                    };
                }
                return new WikiPage({
                    service : this,
                    _fields : lang.mixin({}, pageOrJsonOrString)
                });
            }
        }
        
    });
    return WikiService;
});
