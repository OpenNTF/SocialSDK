/*
 * © Copyright IBM Corp. 2012
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
 * Social Business Toolkit SDK.
 *  
 * Helpers for accessing the Connections Profiles services
 */
define(['sbt/_bridge/declare','sbt/config','sbt/connections/core','sbt/connections/ActivityStreamConstants','sbt/Jsonpath','sbt/Endpoint','sbt/lang','sbt/json','sbt/log', 'sbt/validate'],
		function(declare, config, core,ASConstants,jsonPath, Endpoint, lang, json, log, validate) {
	/**
	Javascript APIs for IBM Connections Activity Stream Service.
	@module sbt.connections.ActivityStreamService
	**/
	/**
	ActivityStream class associated with an activity stream. 
	@class ActivityStream
	**/
	
	var ActivityStream = declare("sbt.connections.ActivityStream", null, {
		data:		null, 
		constructor: function() {
		},
		/**
		Get array of activity stream objects
		@method getEntries
		@return {ActivityStream[]} array of activity stream objects
		**/
		getEntries: function(){
			return this.data.list;
		},
		/**
		Get items per page
		@method getItemsPerPage
		@return {int} items per page of the activity stream
		**/
		getItemsPerPage: function(){
			return this.data.itemsPerPage;
		},
		/**
		Get information whether entries are sorted or not
		@method isSorted
		@return {boolean} whether entries are sorted or not
		**/
		isSorted: function(){
			return this.data.sorted;
		},
		/**
		Get information whether entries are filtered or not
		@method isFiltered
		@return {boolean} whether entries are filtered or not
		**/
		isFiltered: function(){
			return this.data.filtered;
		},
		/**
		Get information whether current user is administrator or not
		@method isAdmin
		@return {String} "true" if user is an administrator, "false" if user is not an administrator
		**/
		isAdmin: function(){
			return this.data.connections.isAdmin;
		},
		/**
		Get total results for activity stream
		@method getTotalResults
		@return {int} total results for activity stream
		**/
		getTotalResults: function(){
			return this.data.totalResults;
		},
		/**
		Get start index
		@method getStartIndex
		@return {String} start index
		**/
		getStartIndex: function(){
			return this.data.startIndex;
		},
		/**
		Get title of the activity stream
		@method getTitle
		@return {String} title of the activity stream
		**/
		getTitle: function(){
			return this.data.title;
		}
	});

	/**
	ActivityStreamService class associated with a activity stream service of IBM Connections.

	@class ActivityStreamService
	@constructor
	@param {Object} options  Options object
		@param {String} [options.endpoint=connections]  Endpoint to be used by ProfileService.
	**/
	var ActivityStreamService = declare("sbt.connections.ActivityStreamService", null, {
		_endpoint			:null,
		_userType			:null,
		_groupType			:null,
		_applicationType	:null,
		
		constructor: function(options) {
			options = options || {};
			this._endpoint = Endpoint.find(options.endpoint || "connections");
		},
		/**
		Get activity stream based on userType, groupType and applicationType.
		
		@method getStream
		@param {Object} [args]  Argument object
			@param {String} [args.userType] User type for which activity stream is to be obtained. default value is "@public".
			@param {String} [args.groupType] Group type for which activity stream is to be obtained. default value is "@all".
			@param {String} [args.applicationType] Application type for which activity stream is to be obtained. default value is "@all".
			@param {Object} [args.parameters] Object containing all query parameters as properties to be passed with get call.
			@param {Function} [args.load] The function getStream invokes when the activity stream is 
			loaded from the server. The function expects to receive one parameter, 
			the loaded ActivityStream object.
			@param {Function} [args.error] Sometimes the getStream call fail with bad request such as 400  
			or server errors such as 500. The error parameter is another callback function
			that is only invoked when an error occurs. This allows to control what happens
		    when an error occurs without having to put a lot of logic into your load function
		    to check for error conditions. The parameter passed to the error function is a 
		    JavaScript Error object indicating what the failure was. From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
			@param {Function} [args.handle] This callback is called regardless of whether 
			the call to get the activity stream completes or fails. The  parameter passed to this callback
			is the ActivityStream object (or error object). From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
		**/
		getStream: function(args) {
			if (!(validate._validateInputTypeAndNotify("ActivityStreamService", "getStream", "args", args, "object", args))) {
				return ;
			}
			this._userType = args.userType || ASConstants.ASUser.PUBLIC; //Default is public updates
			this._groupType = args.groupType || ASConstants.ASGroup.ALL; // Default is all groups
			this._applicationType = args.applicationType || ASConstants.ASApplication.ALL; // Default is all Apps
			var as = new ActivityStream();
			if(args.load || args.handle) {
				this._load(as,args);
			}
			return as;
		},
		/**
		Post an entry .
		
		@method postEntry
		@param {Object} [args]  Argument object
			@param {String} [args.userType] User type of activity stream to which entry is to be posted. default value is "@public".
			@param {String} [args.groupType] Group type of activity stream to which entry is to be posted. default value is "@all".
			@param {String} [args.applicationType] Application type of activity stream to which entry is to be posted. default value is "@all".
			@param {Function} [args.load] The function postEntry invokes when the activity stream is 
			loaded from the server. The function expects to receive one parameter, 
			the response containing newly created ActivityStream id.
			@param {Function} [args.error] Sometimes the postEntry call fail with bad request such as 400  
			or server errors such as 500. The error parameter is another callback function
			that is only invoked when an error occurs. This allows to control what happens
		    when an error occurs without having to put a lot of logic into your load function
		    to check for error conditions. The parameter passed to the error function is a 
		    JavaScript Error object indicating what the failure was. From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
			@param {Function} [args.handle] This callback is called regardless of whether 
			the call to get the activity stream completes or fails. The  parameter passed to this callback
			is the ActivityStream object (or error object). From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
		**/
		postEntry: function(args) {
			if (!(validate._validateInputTypeAndNotify("ActivityStreamService", "postEntry", "args", args, "object", args))) {
				return ;
			}
			this._userType = args.userType || ASConstants.ASUser.PUBLIC; //Default is public updates
			this._groupType = args.groupType || ASConstants.ASGroup.ALL; // Default is all groups
			this._applicationType = args.applicationType || ASConstants.ASApplication.ALL; // Default is all Apps
			var as = new ActivityStream();
			this._post(as,args);
			return as;
		},
		/**
		Get Updates from user's network.
		
		@method getUpdatesFromMyNetwork
		@param {Object} [args]  Argument object
			@param {Object} [args.parameters] Object containing all query parameters as properties to be passed with get call.
			@param {Function} [args.load] The function getUpdatesFromMyNetwork invokes when the activity stream is 
			loaded from the server. The function expects to receive one parameter, 
			the loaded ActivityStream object.
			@param {Function} [args.error] Sometimes the getUpdatesFromMyNetwork call fail with bad request such as 400  
			or server errors such as 500. The error parameter is another callback function
			that is only invoked when an error occurs. This allows to control what happens
		    when an error occurs without having to put a lot of logic into your load function
		    to check for error conditions. The parameter passed to the error function is a 
		    JavaScript Error object indicating what the failure was. From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
			@param {Function} [args.handle] This callback is called regardless of whether 
			the call to get the activity stream completes or fails. The  parameter passed to this callback
			is the ActivityStream object (or error object). From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
		**/
		getUpdatesFromMyNetwork: function(args) {
			if (!(validate._validateInputTypeAndNotify("ActivityStreamService", "getUpdatesFromMyNetwork", "args", args, "object", args))) {
				return ;
			}
			this._userType = ASConstants.ASUser.ME;
			this._groupType = ASConstants.ASGroup.FRIENDS;
			this._applicationType = ASConstants.ASApplication.ALL;
			var as = new ActivityStream();
			if(args.load || args.handle) {
				this._load(as,args);
			}
			return as;
		},
		/**
		Get Status Updates for user.
		
		@method getMyStatusUpdates
		@param {Object} [args]  Argument object
			@param {Object} [args.parameters] Object containing all query parameters as properties to be passed with get call.
			@param {Function} [args.load] The function getMyStatusUpdates invokes when the activity stream is 
			loaded from the server. The function expects to receive one parameter, 
			the loaded ActivityStream object.
			@param {Function} [args.error] Sometimes the getMyStatusUpdates call fail with bad request such as 400  
			or server errors such as 500. The error parameter is another callback function
			that is only invoked when an error occurs. This allows to control what happens
		    when an error occurs without having to put a lot of logic into your load function
		    to check for error conditions. The parameter passed to the error function is a 
		    JavaScript Error object indicating what the failure was. From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
			@param {Function} [args.handle] This callback is called regardless of whether 
			the call to get the activity stream completes or fails. The  parameter passed to this callback
			is the ActivityStream object (or error object). From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
		**/
		getMyStatusUpdates: function(args) {
			if (!(validate._validateInputTypeAndNotify("ActivityStreamService", "getMyStatusUpdates", "args", args, "object", args))) {
				return ;
			}
			this._userType = ASConstants.ASUser.ME;
			this._groupType = ASConstants.ASGroup.ALL;
			this._applicationType = ASConstants.ASApplication.STATUS;
			var as = new ActivityStream();
			if(args.load || args.handle) {
				this._load(as,args);
			}
			return as;
		},
		/**
		Get Updates from people followed by user .
		
		@method getUpdatesFromPeopleIFollow
		@param {Object} [args]  Argument object
			@param {Object} [args.parameters] Object containing all query parameters as properties to be passed with get call.
			@param {Function} [args.load] The function getUpdatesFromPeopleIFollow invokes when the activity stream is 
			loaded from the server. The function expects to receive one parameter, 
			the loaded ActivityStream object.
			@param {Function} [args.error] Sometimes the getUpdatesFromPeopleIFollow call fail with bad request such as 400  
			or server errors such as 500. The error parameter is another callback function
			that is only invoked when an error occurs. This allows to control what happens
		    when an error occurs without having to put a lot of logic into your load function
		    to check for error conditions. The parameter passed to the error function is a 
		    JavaScript Error object indicating what the failure was. From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
			@param {Function} [args.handle] This callback is called regardless of whether 
			the call to get the activity stream completes or fails. The  parameter passed to this callback
			is the ActivityStream object (or error object). From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
		**/
		getUpdatesFromPeopleIFollow: function(args) {
			if (!(validate._validateInputTypeAndNotify("ActivityStreamService", "getUpdatesFromPeopleIFollow", "args", args, "object", args))) {
				return ;
			}
			this._userType = ASConstants.ASUser.ME;
			this._groupType = ASConstants.ASGroup.FOLLOWING;
			this._applicationType = ASConstants.ASApplication.PEOPLE;
			var as = new ActivityStream();
			if(args.load || args.handle) {
				this._load(as,args);
			}
			return as;
		},
		/**
		Get Updates from communities followed by user .
		
		@method getUpdatesFromCommunitiesIFollow
		@param {Object} [args]  Argument object
			@param {Object} [args.parameters] Object containing all query parameters as properties to be passed with get call.
			@param {Function} [args.load] The function getUpdatesFromCommunitiesIFollow invokes when the activity stream is 
			loaded from the server. The function expects to receive one parameter, 
			the loaded ActivityStream object.
			@param {Function} [args.error] Sometimes the getUpdatesFromCommunitiesIFollow call fail with bad request such as 400  
			or server errors such as 500. The error parameter is another callback function
			that is only invoked when an error occurs. This allows to control what happens
		    when an error occurs without having to put a lot of logic into your load function
		    to check for error conditions. The parameter passed to the error function is a 
		    JavaScript Error object indicating what the failure was. From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
			@param {Function} [args.handle] This callback is called regardless of whether 
			the call to get the activity stream completes or fails. The  parameter passed to this callback
			is the ActivityStream object (or error object). From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
		**/
		getUpdatesFromCommunitiesIFollow: function(args) {
			if (!(validate._validateInputTypeAndNotify("ActivityStreamService", "getUpdatesFromCommunitiesIFollow", "args", args, "object", args))) {
				return ;
			}
			this._userType = ASConstants.ASUser.ME;
			this._groupType = ASConstants.ASGroup.ALL;
			this._applicationType = ASConstants.ASApplication.COMMUNITIES;
			var as = new ActivityStream();
			if(args.load || args.handle) {
				this._load(as,args);
			}
			return as;
		},
		/**
		Get Updates from a single community .
		
		@method getUpdatesFromACommunity
		@param {Object} [args]  Argument object
			@param {String} [communityID] Community ID for which activity stream is to be obtained..
			@param {Object} [args.parameters] Object containing all query parameters as properties to be passed with get call.
			@param {Function} [args.load] The function getUpdatesFromCommunitiesIFollow invokes when the activity stream is 
			loaded from the server. The function expects to receive one parameter, 
			the loaded ActivityStream object.
			@param {Function} [args.error] Sometimes the getUpdatesFromCommunitiesIFollow call fail with bad request such as 400  
			or server errors such as 500. The error parameter is another callback function
			that is only invoked when an error occurs. This allows to control what happens
		    when an error occurs without having to put a lot of logic into your load function
		    to check for error conditions. The parameter passed to the error function is a 
		    JavaScript Error object indicating what the failure was. From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
			@param {Function} [args.handle] This callback is called regardless of whether 
			the call to get the activity stream completes or fails. The  parameter passed to this callback
			is the ActivityStream object (or error object). From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
		**/
		getUpdatesFromACommunity: function(args) {
			if (!(validate._validateInputTypeAndNotify("ActivityStreamService", "getUpdatesFromACommunity", "args", args, "object", args))) {
				return ;
			}
			if (!(validate._validateInputTypeAndNotify("ActivityStreamService", "getUpdatesFromACommunity", "args.communityID", args.communityID, "string", args))) {
				return ;
			}
			this._userType = ASConstants.ASUser.COMMUNITY+args.communityID;
			this._groupType = ASConstants.ASGroup.ALL;
			this._applicationType = ASConstants.ASApplication.ALL;
			var as = new ActivityStream();
			if(args.load || args.handle) {
				this._load(as,args);
			}
			return as;
		},
		
		getUpdatesFromUser: function(args) {
			if (!(validate._validateInputTypeAndNotify("ActivityStreamService", "getUpdatesFromUser", "args", args, "object", args))) {
				return ;
			}
			if (!(validate._validateInputTypeAndNotify("ActivityStreamService", "getUpdatesFromUser", "args.userID", args.userID, "string", args))) {
				return ;
			}
			this._userType = args.userID;
			this._groupType = ASConstants.ASGroup.INVOLVED;
			this._applicationType = ASConstants.ASApplication.ALL;
			var as = new ActivityStream();
			if(args.load || args.handle) {
				this._load(as,args);
			}
			return as;
		},
		
		getNotificationsForMe: function(args) {
			if (!(validate._validateInputTypeAndNotify("ActivityStreamService", "getNotificationsForMe", "args", args, "object", args))) {
				return ;
			}
			this._userType = ASConstants.ASUser.ME;
			this._groupType = ASConstants.ASGroup.NOTESFORME;
			this._applicationType = ASConstants.ASApplication.ALL;
			var as = new ActivityStream();
			if(args.load || args.handle) {
				this._load(as,args);
			}
			return as;
		},
		
		getNotificationsFromMe: function(args) {
			if (!(validate._validateInputTypeAndNotify("ActivityStreamService", "getNotificationsFromMe", "args", args, "object", args))) {
				return ;
			}
			this._userType = ASConstants.ASUser.ME;
			this._groupType = ASConstants.ASGroup.NOTESFROMME;
			this._applicationType = ASConstants.ASApplication.ALL;
			var as = new ActivityStream();
			if(args.load || args.handle) {
				this._load(as,args);
			}
			return as;
		},
		
		getResponsesToMyContent: function(args) {
			if (!(validate._validateInputTypeAndNotify("ActivityStreamService", "getResponsesToMyContent", "args", args, "object", args))) {
				return ;
			}
			this._userType = ASConstants.ASUser.ME;
			this._groupType = ASConstants.ASGroup.RESPONSES;
			this._applicationType = ASConstants.ASApplication.ALL;
			var as = new ActivityStream();
			if(args.load || args.handle) {
				this._load(as,args);
			}
			return as;
		},
		
		getMyActionableView: function(args) {
			if (!(validate._validateInputTypeAndNotify("ActivityStreamService", "getMyActionableView", "args", args, "object", args))) {
				return ;
			}
			if (args.application && !(validate._validateInputTypeAndNotify("ActivityStreamService", "getMyActionableView", "args.application", args.application, "string", args))) {
				return ;
			}
			this._userType = ASConstants.ASUser.ME;
			this._groupType = ASConstants.ASGroup.ACTIONS;
			this._applicationType = args.application || ASConstants.ASApplication.ALL;
			var as = new ActivityStream();
			if(args.load || args.handle) {
				this._load(as,args);
			}
			return as;
		},
		
		getMySavedView: function(args) {
			if (!(validate._validateInputTypeAndNotify("ActivityStreamService", "getMySavedView", "args", args, "object", args))) {
				return ;
			}
			if (args.application && !(validate._validateInputTypeAndNotify("ActivityStreamService", "getMySavedView", "args.application", args.application, "string", args))) {
				return ;
			}
			this._userType = ASConstants.ASUser.ME;
			this._groupType = ASConstants.ASGroup.SAVED;
			this._applicationType = args.application || ASConstants.ASApplication.ALL;
			var as = new ActivityStream();
			if(args.load || args.handle) {
				this._load(as,args);
			}
			return as;
		},
		
		_createPostUrl: function (_self) {
			return core.ActivityStreamUrls.activityStreamBaseUrl+_self._endpoint.authType+core.ActivityStreamUrls.activityStreamRestUrl+_self._userType+"/"+_self._groupType+"/"+_self._applicationType;
		},
		
		_notifyError: function(error, errorCallBack, handleCallBack){
			if(errorCallBack || handleCallBack){
				if(errorCallBack)errorCallBack(error);
				if(handleCallBack)handleCallBack(error);
			}else{
				log.error("Errror received. Error Code = {0}. Error Message = {1}.", error.code, error.message);
			}
		},
		_load: function (as,args) {
			var _self = this;
			var loadCb = args.load;
			var handleCb = args.handle;
			var content = {format :	"json"};
			if(args.parameters){
				lang.mixin(content, args.parameters);
			}
			var url = core.ActivityStreamUrls.activityStreamBaseUrl+_self._endpoint.authType+core.ActivityStreamUrls.activityStreamRestUrl+_self._userType+"/"+_self._groupType+"/"+_self._applicationType;// encodeURI
			_self._endpoint.xhrGet({
				serviceUrl:	url,
				handleAs  :	"json",
				content   :	content,
				load: function(response) {
		      		as.data = response;
		      		if(loadCb)loadCb(as);
		      		if(handleCb)handleCb(as);
				},
				error: function(error){
					_self._notifyError(error,args.error, args.handle);
				}
			});
		},
		_post: function (as,args) {
			var _self = this;
			var headers = {"Content-Type" : "application/json"};
			var loadCb = args.load;
			var handleCb = args.handle;
			var content = {format :	"json"};
			var url = _self._createPostUrl(_self);
			_self._endpoint.xhrPost({
				serviceUrl : url,
				handleAs   : "json",
				content    : content,
				postData   : json.stringify(args.postData),
				headers    : headers,
				load       : function(response) {
					      		 as.data = response;
					      		 if(loadCb)loadCb(as);
					      		 if(handleCb)handleCb(as);
							 },
				error      : function(error){									
							 	_self._notifyError(error,args.error, args.handle);
							 }
			});
		}
		
	});
	
	return ActivityStreamService;
});
