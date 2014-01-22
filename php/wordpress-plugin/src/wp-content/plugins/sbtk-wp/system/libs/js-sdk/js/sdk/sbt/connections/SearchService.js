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
 * Use the Search API to perform searches across the installed Connections applications.
 * 
 * Returns a list of results with the specified text in the title, description, or content. Encode the strings. By default, spaces are treated as an AND operator. The following operators are supported:
 *
 *  AND or &&: Searches for items that contain both words. For example: query=red%20AND%20test returns items that contain both the word red and the word test. AND is the default operator.
 *  NOT or !: Excludes the word that follows the operator from the search. For example: query=test%20NOT%20red returns items that contain the word test, but not the word red.
 *  OR: Searches for items that contain either of the words. For example: query=test%20OR%20red
 *  To search for a phrase, enclose the phrase in quotation marks (" ").
 *  +: The plus sign indicates that the word must be present in the result. For example: query=+test%20red returns only items that contain the word test and many that also contain red, but none that contain only the word red.
 *  ?: Use a question mark to match individual characters. For example: query=te%3Ft returns items that contain the words test, text, tent, and others that begin with te.
 *  -: The dash prohibits the return of a given word. This operator is similar to NOT. For example: query=test%20-red returns items that contains the word test, but not the word red.
 *
 * Note: Wildcard searches are permitted, but wildcard only searches (*) are not.
 * For more details about supported operators, see Advanced search options in the Using section of the product documentation.
 * 
 * @module sbt.connections.SearchService
 */
define([ "../declare", "../config", "../lang", "../stringUtil", "../Promise", "../json", "./SearchConstants", 
         "../base/BaseService", "../base/BaseEntity", "../base/AtomEntity", "../base/XmlDataHandler" ], 
    function(declare,config,lang,stringUtil,Promise,json,consts,BaseService,BaseEntity,AtomEntity,XmlDataHandler) {

    /**
     * Scope class represents an entry for a scopes feed returned by the
     * Connections REST API.
     * 
     * @class Scope
     * @namespace sbt.connections
     */
    var Scope = declare(AtomEntity, {

        /**
         * Construct a Scope entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },
        
        /**
         * Return the scope search link.
         * 
         * @method getLink
         * @return {String} Scope link
         */
        getLink : function() {
        	return this.getAsString("link");
        }

    });
    
    /**
     * Result class represents an entry for a search feed returned by the
     * Connections REST API.
     * 
     * @class Result
     * @namespace sbt.connections
     */
    var Result = declare(AtomEntity, {

        /**
         * Construct a Scope entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },
        
        /**
         * Indicates a relative assessment of relevance for a particular search 
         * result with respect to the search query.
         * 
         * @method getRelevance
         * @return {String} Relative assessment of relevance
         */
        getRelevance : function() {
            return this.getAsNumber("relevance");
        }

    });
    
    /**
     * FacetValue class represents an entry for a search facet returned by the
     * Connections REST API.
     * 
     * @class FacetValue
     * @namespace sbt.connections
     */
    var FacetValue = declare(BaseEntity, {

        /**
         * Construct an FacetValue.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        	// create XML data handler
        	this.dataHandler = new XmlDataHandler({
                service : args.service,
                data : args.data,
                namespaces : lang.mixin(consts.Namespaces, args.namespaces || {}),
                xpath : lang.mixin(consts.FacetValueXPath, args.xpath || this.xpath || {})
            });
        	this.id = this.getAsString("uid");
        },
        
        /**
         * Return the value of id from facet entry.
         * 
         * @method getId
         * @return {String} ID of the facet entry
         */
        getId : function() {
            var id = this.getAsString("id");
            var parts = id.split("/");
            return (parts.length == 1) ? parts[0] : parts[1];
        },

        /**
         * Return the value of label from facet entry.
         * 
         * @method getLabel
         * @return {String} Facet entry label
         */
        getLabel : function() {
            return this.getAsString("label");
        },

        /**
         * Return the value of weigth from facet entry.
         * 
         * @method getWeight
         * @return {Number} Facet entry weight
         */
        getWeight : function() {
            return this.getAsNumber("weight");
        }

    });
    
    /*
     * Callbacks used when reading a feed that contains scope entries.
     */
    var ScopeFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                namespaces : consts.Namespaces,
                xpath : consts.SearchFeedXPath,
                service : service,
                data : data
            });
        },
        createEntity : function(service,data,response) {
            return new Scope({
            	namespaces : consts.Namespaces,
                service : service,
                data : data
            });
        }
    };

    /*
     * Callbacks used when reading a feed that contains search entries.
     */
    var ResultFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                namespaces : consts.Namespaces,
                xpath : consts.SearchFeedXPath,
                service : service,
                data : data
            });
        },
        createEntity : function(service,data,response) {
            return new Result({
            	namespaces : consts.Namespaces,
                xpath : consts.SearchXPath,
                service : service,
                data : data
            });
        }
    };
    
    /*
     * Callbacks used when reading a feed that contains search facets.
     */
    var FacetsCallbacks = {
        createEntities : function(service,data,response) {
        	var xpathExprs = lang.mixin({}, consts.SingleFacetXPath);
        	// facet param looks like this "{"id": "Person"}"
        	var facet = json.parse(response.options.query.facet);
        	xpathExprs.entries = xpathExprs.entries.replace("{facet.id}", facet.id);
            return new XmlDataHandler({
                namespaces : consts.Namespaces,
                xpath : xpathExprs,
                service : service,
                data : data
            });
        },
        createEntity : function(service,data,response) {
            return new FacetValue({
            	namespaces : consts.Namespaces,
                xpath : consts.FacetValueXPath,
                service : service,
                data : data
            });
        }
    };
    
    /**
     * SearchService class.
     * 
     * @class SearchService
     * @namespace sbt.connections
     */
    var SearchService = declare(BaseService, {
        
        contextRootMap: {
            search: "search"
        },

        /**
         * Constructor for SearchService
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
         * 
         * @method getDefaultEndpointName
         * @returns {String}
         */
        getDefaultEndpointName: function() {
            return "connections";
        },
        
        /**
         * Returns the set of supported values that can be passed to the "scope" parameter of the Search API. 
         * Scopes relating to Connections applications that have not been installed will not be returned.
         * 
         * @method getScopes
         * @param requestArgs
         */
        getScopes: function(requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs || {}
            };
            
            return this.getEntities(consts.AtomScopes, options, ScopeFeedCallbacks);
        },
        
        /**
         * Search Lotus Connection for public information.
         * 
         * @method getResults
         * @param query Text to search for
         * @param requestArgs
         */
        getResults: function(queryArg, requestArgs) {
        	requestArgs = this._stringifyRequestArgs(requestArgs);
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ query : queryArg } , requestArgs || {})
            };
            
            return this.getEntities(consts.AtomSearch, options, ResultFeedCallbacks);
        },
        
        /**
         * Search Lotus Connections for both public information and 
         * private information that you have access to. You must provide 
         * authentication information in the request to retrieve this 
         * resource.
         * 
         * @method getMyResults
         * @param query Text to search for
         * @param requestArgs
         */
        getMyResults: function(queryArg, requestArgs) {
        	requestArgs = this._stringifyRequestArgs(requestArgs);
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ query : queryArg } , requestArgs || {})
            };
            
            return this.getEntities(consts.AtomMySearch, options, ResultFeedCallbacks);
        },
        
        /**
         * Search IBM Connections for public information, tagged 
         * with the specified tags.
         * 
         * @method getTagged
         * @param tags tags to search for
         * @param requestArgs
         */
        getResultsByTag: function(tags, requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ 
                		constraint : this._createTagConstraint(tags)
                	} , 
                	requestArgs || {})
            };
                
            return this.getEntities(consts.AtomSearch, options, ResultFeedCallbacks);
        },
        
        /**
         * Search IBM Connections for both public information and private 
         * information that you have access to, tagged 
         * with the specified tags.
         * 
         * @method getMyResultsByTag
         * @param tags Tags to search for
         * @param requestArgs
         */
        getMyResultsByTag: function(tags, requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ 
            			constraint : this._createTagConstraint(tags)
                	} , 
                	requestArgs || {})
            };
                
            return this.getEntities(consts.AtomMySearch, options, ResultFeedCallbacks);
        },
        
        /**
         * Search IBM Connections Profiles for people using the specified 
         * query string and return public information.
         * 
         * @method getPeople
         * @param query Text to search for
         * @param requestArgs
         */
        getPeople: function(queryArg, requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ 
                		query : queryArg,
                		pageSize : "0",
                		facet : "{\"id\": \"Person\"}"
                	} , 
                	requestArgs || {})
            };
            
            return this.getEntities(consts.AtomSearch, options, FacetsCallbacks);
        },
        
        /**
         * Search IBM Connections Profiles for people using the specified 
         * query string and return public information.
         * 
         * @method getMyPeople
         * @param query Text to search for
         * @param requestArgs
         */
        getMyPeople: function(queryArg, requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ 
                		query : queryArg,
                		pageSize : "0",
                		facet : "{\"id\": \"Person\"}"
                	} , 
                	requestArgs || {})
            };
                
            return this.getEntities(consts.AtomMySearch, options, FacetsCallbacks);
        },
        
        //
        // Internals
        //
        
        /*
         * 
         */
        _stringifyRequestArgs: function(requestArgs) {
            if (!requestArgs) {
                return null;
            }
            var _requestArgs = {};
            for(var name in requestArgs){
                var value = requestArgs[name];
                if (lang.isObject(value)) {
                	_requestArgs[name] = json.stringify(value);
                } else {
                	_requestArgs[name] = value;
                }
            }
            return _requestArgs;
        },
        
        /*
         * Create a contraint JSON string for the specified tags
         */
        _createTagConstraint: function(tags) {
        	var jsonObj = { "type" : "category", "values" : new Array() };
        	if (lang.isArray(tags)) {
        		for (var i=0;i<tags.length;i++) {
        			jsonObj.values[i] = "Tag/" + tags[i];
        		}
        	} else {
        		jsonObj.values[0] = "Tag/" + tags;
        	}
        	return json.stringify(jsonObj);
        }
    });
    return SearchService;
});
