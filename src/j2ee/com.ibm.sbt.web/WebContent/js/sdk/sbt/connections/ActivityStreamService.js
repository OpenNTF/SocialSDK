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
 * JavaScript API for IBM Connections Activity Stream Service.
 * 
 * @module sbt.connections.ActivityStreamService
 */
define([ "../declare", "../lang", "../stringUtil", "../Endpoint", "../Promise", "./ActivityStreamConstants", "../base/BaseService","../base/BaseEntity",  "../base/DataHandler", 'sbt/json'], 
		function(declare,lang,stringUtil,Endpoint,Promise,consts,BaseService, BaseEntity,DataHandler, json) {

	/**
     * JsonDataHandler class
     * 
     * @class JsonDataHandler
     * @namespace sbt.base
     */   
	var ActivityStreamsDataHandler = declare(DataHandler, {	
        
		constructor : function(args) {
            lang.mixin(this, args);
            this.data = args.data;
		},
        
        getSummary : function() {
            if (!this._summary && this.data.totalResults) {
                this._summary = {
                    totalResults : this.data.totalResults,
                    startIndex : this.data.startIndex,
                    itemsPerPage : this.data.itemsPerPage
                };
            }
            return this._summary;
        },
        
        getEntitiesDataArray : function() {
        	return this.data.list;
        }
	
	});
	
	
    /**
     * ActivityStreamEntry class.
     * 
     * @class ActivityStreamEntry
     * @namespace sbt.connections
     */
    var ActivityStreamEntry = declare(BaseEntity, {

    	data:		null, 
    	
    	/**
		Get published date of activity stream entry
		@method getPublishedDate
		@return {String} published date of activity stream entry
		**/
    	getPublishedDate: function(){
			return this.dataHandler.data.published;
		},
    	
		/**
		Get plain title of the activity stream entry. this represents the action that was done by actor.
		@method getPlainTitle
		@return {String} plain title of the activity stream entry
		**/
    	getPlainTitle: function(){
			return this.dataHandler.data.connections.plainTitle;
		},
    	
		/**
		Get actor name of the activity stream entry
		@method getActorDisplayName
		@return {String} display name of the activity stream actor
		**/
    	getActorDisplayName: function(){
			return this.dataHandler.data.actor.displayName;
		},
		
		/**
		Get full actor object of the activity stream entry. Object holds all properties of actor.
		Here is an example of an actor object:
		{
			connections:{
				state:"active"
			},
			objectType:"person",
			id:"urn:lsid:lconn.ibm.com:profiles.person:0EE5A7FA-3434-9A59-4825-7A7000278DAA",
			displayName:"Frank Adams"
		},
		@method getActor
		@return {String} actor object
		**/
    	getActor: function(){
			return this.dataHandler.data.actor;
		},
		
		/**
		Get verb of activity stream entry, verb represents the type of action that was done
		@method getVerb
		@return {String} verb of activity stream entry
		**/
    	getVerb: function(){
			return this.dataHandler.data.verb;
		},
		
		/**
		Get id of activity stream entry
		@method getId
		@return {String} id of activity stream entry
		**/
    	getId: function(){
			return this.dataHandler.data.id;
		},
    	
      
    });
    
    /**
     * Callbacks used when reading a feed that contains ActivityStream entries.
     */
    var getActivityStreamServiceCallbacks = {
        createEntities : function(service,data,response) {
            return new ActivityStreamsDataHandler({
                data : data
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = new ActivityStreamsDataHandler({
                data : data
            });
            return new ActivityStreamEntry({
                service : service,
                dataHandler : entryHandler
            });
        }
    };

    /**
     * ActivityStreamService class.
     * 
     * @class ActivityStreamService
     * @namespace sbt.connections
     */
    var ActivityStreamService = declare(BaseService, {
        /**
         * Constructor for ActivityStreamService
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
            if (!this.endpoint) {
                this.endpoint = Endpoint.find(this.getDefaultEndpointName());
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
         * Callbacks used when reading a feed that contains Activity Stream entries.
         */
        getActivityStreamCallbacks: function() {
            return getActivityStreamServiceCallbacks;
        },

        /**
         * Get activity stream for given user, group and application type
         * 
         * @method getStream
         * * @param {String}
         * 			  userType user type for which activity stream is to be obtained.
         * 			  If null is passed for userType, then '@public' will be used as 
         * 			  default 
         * * @param {String}
         * 			  groupType group type for which activity stream is to be obtained
         * 			  If null is passed for userType, then '@all' will be used as 
         * 			  default 
         * * @param {String}
         * 			  application type for which activity stream is to be obtained
         *            If null is passed for userType, then '@all' will be used as 
         * 			  default 
         * @param {Object}
         *            [args] Object representing various parameters
         *            that can be passed to get an activity stream. 
         *            The parameters must be exactly as they are
         *            supported by IBM Connections like count, query etc.
         */
        getStream : function(userType, groupType, applicationType, args) {
        	var _userType = userType || consts.ASUser.PUBLIC; //Default is public updates
			var _groupType = groupType || consts.ASGroup.ALL; // Default is all groups
			var _applicationType = applicationType || consts.ASApplication.ALL; // Default is all Applications
//			var url = consts.ActivityStreamUrls.activityStreamBaseUrl+this.endpoint.authType+consts.ActivityStreamUrls.activityStreamRestUrl+_userType+"/"+_groupType+"/"+_applicationType;
            var url = this.constructUrl(consts.ActivityStreamUrls.activityStreamBaseUrl+"{authType}"+consts.ActivityStreamUrls.activityStreamRestUrl+"{userType}/{groupType}/{appType}", 
                    {}, 
                    { authType : this.endpoint.authType, userType : _userType, groupType : _groupType, appType : _applicationType });
            var options = {
                method : "GET",
                handleAs : "json",
                query : args || {}
            };
            return this.getEntities(url, options, this.getActivityStreamCallbacks(), args);
        },
        
		/**
		Get My Status Updates.
		
		@method getMyStatusUpdates
		@param {Object} [args] Object representing various parameters
         *            that can be passed to get an activity stream. 
         *            The parameters must be exactly as they are
         *            supported by IBM Connections like count, query etc.
		**/
        getMyStatusUpdates : function(args) {
			return this.getStream(consts.ASUser.ME, consts.ASGroup.ALL, consts.ASApplication.STATUS, args);
        },
        
		/**
		Get Updates From My Network.
		
		@method getUpdatesFromMyNetwork
		@param {Object} [args] Object representing various parameters
         *            that can be passed to get an activity stream. 
         *            The parameters must be exactly as they are
         *            supported by IBM Connections like count, query etc.
		**/
        getUpdatesFromMyNetwork : function(args) {
			return this.getStream(consts.ASUser.ME, consts.ASGroup.FRIENDS, consts.ASApplication.ALL, args);
        },
        
		/**
		Get Updates From People I Follow.
		
		@method getUpdatesFromPeopleIFollow
		@param {Object} [args] Object representing various parameters
         *            that can be passed to get an activity stream. 
         *            The parameters must be exactly as they are
         *            supported by IBM Connections like count, query etc.
		**/
        getUpdatesFromPeopleIFollow : function(args) {
			return this.getStream(consts.ASUser.ME, consts.ASGroup.FOLLOWING, consts.ASApplication.PEOPLE, args);
        },
        
		/**
		Get Updates From Communities I Follow.
		
		@method getUpdatesFromCommunitiesIFollow
		@param {Object} [args] Object representing various parameters
         *            that can be passed to get an activity stream. 
         *            The parameters must be exactly as they are
         *            supported by IBM Connections like count, query etc.
		**/
        getUpdatesFromCommunitiesIFollow : function(args) {
			return this.getStream(consts.ASUser.ME, consts.ASGroup.ALL, consts.ASApplication.COMMUNITIES, args);
        },
        
		/**
		Get Updates From A Community.
		
		@method getUpdatesFromACommunity
		
		@param {String} communityID Community id for which activity Stream
					  is to be obtained
		@param {Object} [args] Object representing various parameters
         *            that can be passed to get an activity stream. 
         *            The parameters must be exactly as they are
         *            supported by IBM Connections like count, query etc.
		**/
        getUpdatesFromACommunity : function(communityID, args) {
        	var promise = this._validateCommunityUuid(communityID);
            if (promise) {
                return promise;
            }
			return this.getStream(consts.ASUser.COMMUNITY+communityID, consts.ASGroup.ALL, consts.ASApplication.ALL, args);
        },
        
		/**
		Get Updates from a single user
		
		@method getUpdatesFromUser
		
		@param {String} user id for which activity Stream
			  		  is to be obtained
		@param {Object} [args] Object representing various parameters
         *            that can be passed to get an activity stream. 
         *            The parameters must be exactly as they are
         *            supported by IBM Connections like count, query etc.
		**/
        getUpdatesFromAUser : function(userId, args) {
        	var promise = this._validateUserId(userId);
            if (promise) {
                return promise;
            }
			return this.getStream(userId, consts.ASGroup.INVOLVED, consts.ASApplication.ALL, args);
        },
        
		/**
		Get Notifications For Me.
		
		@method getNotificationsForMe
		
		@param {Object} [args] Object representing various parameters
         *            that can be passed to get an activity stream. 
         *            The parameters must be exactly as they are
         *            supported by IBM Connections like count, query etc.
		**/
        getNotificationsForMe : function(args) {
			return this.getStream(consts.ASUser.ME, consts.ASGroup.RESPONSES, consts.ASApplication.ALL, args);
        },
        
		/**
		Get Notifications From Me.
		
		@method getNotificationsFromMe
		@param {Object} [args] Object representing various parameters
         *            that can be passed to get an activity stream. 
         *            The parameters must be exactly as they are
         *            supported by IBM Connections like count, query etc.
		**/
        getNotificationsFromMe : function(args) {
			return this.getStream(consts.ASUser.ME, consts.ASGroup.NOTESFROMME, consts.ASApplication.ALL, args);
        },
        
		/**
		Get Responses To My Content.
		
		@method getResponsesToMyContent
		@param {Object} [args] Object representing various parameters
         *            that can be passed to get an activity stream. 
         *            The parameters must be exactly as they are
         *            supported by IBM Connections like count, query etc.
		**/
        getResponsesToMyContent : function(args) {
			return this.getStream(consts.ASUser.ME, consts.ASGroup.RESPONSES, consts.ASApplication.ALL, args);
        },
        
		/**
		Get My Actionable View.
		
		@method getMyActionableView
		@param {Object} [args] Object representing various parameters
         *            that can be passed to get an activity stream. 
         *            The parameters must be exactly as they are
         *            supported by IBM Connections like count, query etc.
		**/
        getMyActionableView : function(args) {
			return this.getStream(consts.ASUser.ME, consts.ASGroup.ACTIONS, consts.ASApplication.ALL, args);
        },
        
		/**
		Get My Saved View.
		
		@method getMySavedView
		@param {Object} [args] Object representing various parameters
         *            that can be passed to get an activity stream. 
         *            The parameters must be exactly as they are
         *            supported by IBM Connections like count, query etc.
		**/
        getMySavedView : function(args) {
			return this.getStream(consts.ASUser.ME, consts.ASGroup.SAVED, consts.ASApplication.ALL, args);
        },
        
		/**
		search activity stream by query.
		
		@method searchByQuery
		
		 * * @param {String}
         * 			  query string for which activity stream search is to be done.
         * 			  If null is passed for userType, then '@public' will be used as 
         * 			  default 
		 * * @param {String}
         * 			  userType user type for which activity stream search is to be done.
         * 			  If null is passed for userType, then '@public' will be used as 
         * 			  default 
         * * @param {String}
         * 			  groupType group type for which activity stream search is to be done
         * 			  If null is passed for userType, then '@all' will be used as 
         * 			  default 
         * * @param {String}
         * 			  application type for which activity stream search is to be done
         *            If null is passed for userType, then '@all' will be used as 
         * 			  default 
		 * * @param {Object} [args] Object representing various parameters
         *            that can be passed to get an activity stream. 
         *            The parameters must be exactly as they are
         *            supported by IBM Connections like count, query etc.
		**/
        searchByQuery : function(searchString) {
        	var promise = this._validateSearchQuery(searchString);
            if (promise) {
                return promise;
            }
        	var args = {
        			query : searchString
        	};
			return this.getStream(null, null, null, args);
        },
        
		 /**
		 search activity stream by filters.
		 
		 @method searchByFilters
		 
		 * * @param {String} filters array of filter objects for which activity stream search is to be done. here is a sample array of two filters:
		 *         "[{'type':'tag','values':['"+tags+"']},{'type':'tag','values':['test',"mobile"]}]"
		**/
        searchByFilters : function(filters) {
        	var promise = this._validateSearchFilters(filters);
            if (promise) {
                return promise;
            }
        	var args = {};
        	args.filters = filters;
			return this.getStream(null, null, null, args);
        },
        
		/**
		search activity stream by tags.
		
		@method searchByTags
		
		 * * @param {String}
         * 			  tags string containing tags separated by commas for which activity 
         * 			  stream search is to be done.
         * 			  If null is passed for userType, then '@public' will be used as 
         * 			  default 
		**/
        searchByTags : function(tags) {
        	var promise = this._validateSearchTags(tags);
            if (promise) {
                return promise;
            }
        	var args = {};
        	args.filters = "[{'type':'tag','values':['"+tags+"']}]";
			return this.getStream(null, null, null, args);
        },
        
        /**
         * post an Activity Stream entry
         * 
         * @method postEntry
         * * @param {String}
         * 			  userType user type for which activity stream is to be posted
         *            If null is passed for userType, then '@public' will be used as 
         * 			  default 
         * * @param {String}
         * 			  groupType group type for which activity stream is to be posted
         *            If null is passed for userType, then '@all' will be used as 
         * 			  default 
         * * @param {String}
         * 			  application type for which activity stream is to be posted
         *            If null is passed for userType, then '@all' will be used as 
         * 			  default 
         * @param {Object}
         *            [args]Object representing various parameters
         *            that can be passed to post an activity stream. 
         *            The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        postEntry : function(postData, userType, groupType, applicationType, args) {
        	var _userType = userType || consts.ASUser.ME; //Default is public updates
			var _groupType = groupType || consts.ASGroup.ALL; // Default is all groups
			var _applicationType = applicationType || consts.ASApplication.ALL; // Default is all Applications
			var url = consts.ActivityStreamUrls.activityStreamBaseUrl+this.endpoint.authType+consts.ActivityStreamUrls.activityStreamRestUrl+_userType+"/"+_groupType+"/"+_applicationType;
			var headers = {"Content-Type" : "application/json"};
	   	    var options = {
	            method : "POST",
	            query : args || {},
	            handleAs : "json",
	            headers : headers,
	            data : json.stringify(postData)
	        };
            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
                return data;
            };	                
            return this.updateEntity(url, options, callbacks, args);
        },
        
        /*
         * Validate a community UUID, and return a Promise if invalid.
         */
        _validateCommunityUuid : function(communityUuid) {
            if (!communityUuid || communityUuid.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected communityUuid.");
            }
        },
        
        /*
         * Validate a search query, and return a Promise if invalid.
         */
        _validateSearchQuery : function(searchQuery) {
            if (!searchQuery || searchQuery.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected communityUuid.");
            }
        },
        
        /*
         * Validate search tags, and return a Promise if invalid.
         */
        _validateSearchTags : function(searchTags) {
            if (!searchTags || searchTags.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected communityUuid.");
            }
        },
        
        /*
         * Validate search filters, and return a Promise if invalid.
         */
        _validateSearchFilters : function(searchFilters) {
            if (!searchFilters || searchFilters.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected communityUuid.");
            }
        },
        
        /*
         * Validate a user ID, and return a Promise if invalid.
         */
        _validateUserId : function(userId) {
            if (!userId || userId.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected userId.");
            }
        },

    });
    return ActivityStreamService;
});
