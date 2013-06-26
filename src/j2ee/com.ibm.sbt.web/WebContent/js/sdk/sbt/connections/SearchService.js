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
 * The Communities API allows application programs to retrieve community information, subscribe to community updates, and create or modify communities.
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
define([ "../declare", "../config", "../lang", "../stringUtil", "../Promise", "./SearchConstants", "../base/BaseService",
         "../base/BaseEntity", "../base/XmlDataHandler" ], 
    function(declare,config,lang,stringUtil,Promise,consts,BaseService,BaseEntity,XmlDataHandler) {

    /**
     * Result class represents an entry for a result feed returned by the
     * Connections REST API.
     * 
     * @class Result
     * @namespace sbt.connections
     */
    var Result = declare(BaseEntity, {

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
         * Return the value of IBM Connections community title from community
         * ATOM entry document.
         * 
         * @method getTitle
         * @return {String} Community title of the community
         */
        getTitle : function() {
            return this.getAsString("title");
        },

        /**
         * Sets title of IBM Connections community.
         * 
         * @method setTitle
         * @param {String} title Title of the community
         */
        setTitle : function(title) {
            return this.setAsString("title", title);
        },

        /**
         * Return tags of IBM Connections community from community ATOM entry
         * document.
         * 
         * @method getTags
         * @return {Object} Array of tags of the community
         */
        getTags : function() {
            return this.getAsArray("tags");
        },

        /**
         * Set new tags to be associated with this IBM Connections community.
         * 
         * @method setTags
         * @param {Object} Array of tags to be added to the community
         */

        setTags : function(tags) {
            return this.setAsArray("tags", tags);
        },

        /**
         * Gets an author of IBM Connections community.
         * 
         * @method getAuthor
         * @return {Member} author Author of the community
         */
        getAuthor : function() {
            return this.getAsObject([ "authorUserid", "authorName", "authorEmail" ]);
        },

        /**
         * Return the value of IBM Connections community description summary
         * from community ATOM entry document.
         * 
         * @method getSummary
         * @return {String} Community description summary of the community
         */
        getSummary : function() {
            return this.getAsString("summary");
        },

        /**
         * Return the relevance score from result ATOM entry document.
         * 
         * @method getMemberCount
         * @return {Number} Member count for the Community
         */
        getScore : function() {
            return this.getAsNumber("relevance");
        },

        /**
         * Return the relevance rank from result ATOM entry document.
         * 
         * @method getMemberCount
         * @return {Number} Member count for the Community
         */
        getRank : function() {
            return this.getAsNumber("rank");
        },

        /**
         * Return the last updated date of the IBM Connections community from
         * community ATOM entry document.
         * 
         * @method getUpdated
         * @return {Date} Last updated date of the Community
         */
        getUpdated : function() {
            return this.getAsDate("updated");
        }

    });
    
    /*
     * Callbacks used when reading a feed that contains result entries.
     */
    var ResultFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service : service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.SearchFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = new XmlDataHandler({
                service : service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.SearchXPath
            });
            return new Result({
                service : service,
                dataHandler : entryHandler
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
         * @returns {String}
         */
        getDefaultEndpointName: function() {
            return "connections";
        },
        
        /**
         * Search Lotus Connection for public information.
         * 
         * @param query Text to search for
         * @param requestArgs
         */
        getResults: function(query, requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ 'query' : query } , requestArgs || {})
            };
            
            return this.getEntities(consts.publicSearch, options, ResultFeedCallbacks);
        },
        
        /**
         * Search Lotus Connections for both public information and 
         * private information that you have access to. You must provide 
         * authentication information in the request to retrieve this 
         * resource.
         * 
         * @param query Text to search for
         * @param requestArgs
         */
        getMyResults: function(query, requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ 'query' : query } , requestArgs || {})
            };
            
            return this.getEntities(consts.mySearch, options, ResultFeedCallbacks);
        },
        
        /**
         * Search Lotus Connection for public information, and then return 
         * the people associated with the results.
         * 
         * @param query Text to search for
         * @param requestArgs
         */
        getPeople: function(query, requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ 'query' : query } , requestArgs || {})
            };
            
            return this.getEntities(consts.peopleSearch, options, ResultFeedCallbacks);
        },
        
        /**
         * Search Lotus Connections for both public information and private 
         * information that you have access to, and then return the people 
         * associated with the results. You must provide authentication 
         * information in the request to retrieve this resource.
         * 
         * @param query Text to search for
         * @param requestArgs
         */
        getMyPeople: function(query, requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ 'query' : query } , requestArgs || {})
            };
            
            return this.getEntities(consts.myPeopleSearch, options, ResultFeedCallbacks);
        },
        
        /**
         * Search Lotus Connection for public information, and then 
         * return the tags associated with the results.
         * 
         * @param query Text to search for
         * @param requestArgs
         */
        getTags: function(query, requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ 'query' : query } , requestArgs || {})
            };
            
            return this.getEntities(consts.tagsSearch, options, ResultFeedCallbacks);
        },
        
        /**
         * Search Lotus Connections for both public information and private 
         * information that you have access to, and then return the tags associated 
         * with the results. You must provide authentication information in the 
         * request to retrieve this resource.
         * 
         * @param query Text to search for
         * @param requestArgs
         */
        getMyTags: function(query, requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ 'query' : query } , requestArgs || {})
            };
            
            return this.getEntities(consts.myTagsSearch, options, ResultFeedCallbacks);
        },
        
        /**
         * Search Lotus Connection for public information, and then return the 
         * applications associated with the results and identify how many results 
         * were found per application.
         * 
         * @param query Text to search for
         * @param requestArgs
         */
        getApplications: function(query, requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ 'query' : query } , requestArgs || {})
            };
            
            return this.getEntities(consts.sourceSearch, options, ResultFeedCallbacks);
        },
        
        /**
         * Search Lotus Connections for both public information and private 
         * information that you have access to, and then return the applications 
         * associated with the results and identify how many results were found 
         * per application. You must provide authentication information in the 
         * request to retrieve this resource.
         * 
         * @param query Text to search for
         * @param requestArgs
         */
        getMyApplications: function(query, requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ 'query' : query } , requestArgs || {})
            };
            
            return this.getEntities(consts.mySourceSearch, options, ResultFeedCallbacks);
        },
        
        /**
         * Search Lotus Connection for public information, and then 
         * return the dates associated with the results.
         * 
         * @param query Text to search for
         * @param requestArgs
         */
        getDates: function(query, requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ 'query' : query } , requestArgs || {})
            };
            
            return this.getEntities(consts.dateSearch, options, ResultFeedCallbacks);
        },
        
        /**
         * Search Lotus Connections for both public information and private 
         * information that you have access to, and then return the dates 
         * associated with the results. You must provide authentication 
         * information in the request to retrieve this resource.
         * 
         * @param query Text to search for
         * @param requestArgs
         */
        getMyDates: function(query, requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ 'query' : query } , requestArgs || {})
            };
            
            return this.getEntities(consts.myDateSearch, options, ResultFeedCallbacks);
        }
        
        
    });
    return SearchService;
});
