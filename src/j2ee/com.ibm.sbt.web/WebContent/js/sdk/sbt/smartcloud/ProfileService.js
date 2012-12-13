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
 * @author Vimal Dhupar
 * Helpers for accessing the Smartcloud Profiles services
 */
define(['sbt/_bridge/declare','sbt/config','sbt/lang','sbt/smartcloud/core','sbt/Cache','sbt/smartcloud/Subscriber','sbt/Jsonpath','sbt/Endpoint'],
		function(declare,cfg,lang,con,Cache,Subscriber,jsonPath, Endpoint) {
	/**
	Javascript APIs for IBM SmartCloud Profiles Service.
	@module sbt.smartcloud.ProfileService
	**/
	var requests = {};
	function notifyCb(id, param) {
  		var r = requests[id];
  		if(r) {
	  		delete requests[id];
	  		for(var i=0; i<r.length; i++) {
	  			r[i](param);
	  		}
  		}
	}
	
	/**
	Profile class associated with a profile. 
	@class Profile
	@namespace smartcloud
	@constructor
	@param {Object} profileService  profileService object
	@param {String} id id associated with the profile. It is the userId of the logged in user.
	**/
	
	var Profile = declare("sbt.smartcloud.Profile", null, {
		_service:	null,
		_id:		"",
		/**
		Profile entry document of the associated profile
		@property data 
		@type String
		**/
		data:		null,
		constructor: function(service,id) {
			this._service = service;
			this._id = id;
		},
		/**
		Loads the profile object with the profile entry document associated with the profile. By
		default, a network call is made to load the profile entry document in the profile object.

		@method load
		@param {Object} [args]  Argument object
			@param {Function} [args.error] Sometimes the load calls  fail. Often these are 404 errors 
			or server errors such as 500. The error parameter is another callback function
			that is only invoked when an error occurs. This allows to control what happens
		    when an error occurs without having to put a lot of logic into your load function
		    to check for error conditions. The parameter passed to the error function is a 
		    JavaScript Error object indicating what the failure was. From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
			@param {Function} [args.load] The function profile.load invokes when the profile is 
			loaded from the server. The function expects to receive one parameter, 
			the loaded profile object.
			@param {Function} [args.handle] This callback is called regardless of whether 
			the call to load the profile completes or fails. The  parameter passed to this callback
			is the profile object (or error object). From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
		**/
		load: function(args) {
			data = this._service._load(this,args);
		},
		/**
		Method used to evaluate the JsonQuery on Json Data(Profile Entry Document), 
		and return the value of the field.
		@method evalJson
		@param {String} jsonQuery, the Query String used to parse the profile entry document, 
		and fetch the required detail.
		@return {String} Value of the specific field from the response feed.	
		**/
		evalJson: function(jsonQuery){
			return jsonPath(this.data,jsonQuery);			
		},
		/**
		Method used to evaluate the JsonQuery on specific Json Data,  
		and return the value of the field.
		@method evalJsonWithData
		@param {String} jsonQuery, the Query String used to parse the Json response feed, 
		and fetch the required detail.
		@param {String} data to perform the parsing operation on.
		@return {String} Value of the specific field from the response feed.	
		**/
		evalJsonWithData: function(jsonQuery, data){
			return jsonPath(data,jsonQuery);			
		},
		/**
		Return the value of a field in profile entry document
		@method get
		@param {String} fieldName fieldname
		@return {String} value of the field in profile entry document
		**/
		get: function(fieldName){
			if(null == this.data) return "" ; 

			var result = null;
			switch (fieldName) {
			case "thumbnailUrl":
				var image = this.evalJson("$..photo");
				if(image)
					result = _endpoint.baseUrl+"/contacts/img/photos/"+ image;
				break;
			case "address":
				result = this.evalJson("$.."+fieldName);
				if(result) 
				{
					var address = this.evalJsonWithData("$[0]",result);
					if(address)
						result = address;
				}
				break;
			case "department":
				result = this.evalJson("$..name");
				break;
			case "title":
				result = this.evalJson("$..jobtitle");
				break;
			case "phoneNumbers":
				result = this.evalJson("$..telephone");
				break;
			case "about":
				result = this.evalJson("$..aboutMe");
				break;
			default:
				result = this.evalJson("$.."+fieldName);
				break;
			}
				
			return result;
		},
		/**
		Get id of the User
		@method getId
		@return {String} id of the User	
		**/
		getId: function () {
			return this.get("id");
		},
		/**
		Get display name of the User
		@method getDisplayName
		@return {String} display name of the User	
		**/
		getDisplayName: function () {
			return this.get("displayName");
		},
		/**
		Get email of the User
		@method getEmail
		@return {String} email of the User	
		**/
		getEmail: function () {
			return this.get("emailAddress");
		},
		/**
		Get thumbnail URL of the User
		@method getThumbnailUrl
		@return {String} thumbnail URL of the User
		**/
		getThumbnailUrl: function () {
			return this.get("thumbnailUrl");
		},
		/**
		Get address of the profile
		@method getAddress
		@return {String} Address object of the profile
		**/
		getAddress: function () { 
			return this.get("address"); 
		},
		/**
		Get department of the profile
		@method getDepartment
		@return {String} department of the profile
		**/
		getDepartment: function () {
			return this.get("department"); 
		},
		/**
		Get title of the profile
		@method getTitle
		@return {String} title of the profile
		**/
		getTitle: function () {
			return this.get("title");
		},
		/**
		Get profile URL of the profile
		@method getProfileUrl
		@return {String} profile URL of the profile
		**/
		getProfileUrl: function () {
			return this.get("profileUrl");
		},
		/**
		Get phone number of the profile
		@method getPhoneNumber
		@return {String} Phone number object of the profile
		**/
		getPhoneNumber: function () {
			return this.get("phoneNumbers"); 
		},
		/**
		Get Country of the profile
		@method getCountry
		@return {String} country of the profile
		**/
		getCountry: function () { 
			return this.get("country"); 
		},
		/**
		Get Organization Id of the profile
		@method getOrgId
		@return {String} Organization Id of the profile
		**/
		getOrgId: function () { 
			return this.get("orgId"); 
		},
		/**
		Get "About Me" / description of the profile
		@method getAbout
		@return {String} description of the profile
		**/
		getAbout: function () { 
			return this.get("about"); 
		}
	});
	
	/**
	Profile service class associated with a profile service of IBM Smartcloud.

	@class ProfileService
	@constructor
	@param {Object} options  Options object
		@param {String} [options.endpoint]  Endpoint to be used by ProfileService.
		@param {Integer} [options.cacheSize] Cache size to store profile entry documents.
		
	**/
	var ProfileService = declare("sbt.smartcloud.ProfileService", null, {
		_profiles: null,

		constructor: function(options) {
			options = options || {};
			_endpoint = Endpoint.find(options.endpoint||'smartcloud');
			var cs = options.cacheSize;
			if(cs && cs>0) {
				this._profiles = new Cache(cs);
			}
		},
		
		_notifyError: function(error, args){			
			if(args.error) {
				args.error(error);
			}
			if(args.handle) { 
				args.handle(error);
			}
		},
		/**
		Get the profile of a user. The fetched Profile is the Profile of the logged in user.
		
		@method getProfile
		@param {Object} args  Argument object
			@param {Function} [args.error] Sometimes the getProfile call fail with bad request such as 400  
			or server errors such as 500. The error parameter is another callback function
			that is only invoked when an error occurs. This allows to control what happens
		    when an error occurs without having to put a lot of logic into your load function
		    to check for error conditions. The parameter passed to the error function is a 
		    JavaScript Error object indicating what the failure was. From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
			@param {Function} [args.load] The function profile.getProfile invokes when the profile is 
			loaded from the server. The function expects to receive one parameter, 
			the loaded profile object.
			@param {Function} [args.handle] This callback is called regardless of whether 
			the call to get the profile completes or fails. The  parameter passed to this callback
			is the profile object (or error object). From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
			
		**/
		getProfile: function(args) {
			return this._getOne(args);
		},
		_getOne: function(args) { 		
			// here we first get the user's subscriber id from smartcloud using the Subscriber Helper.
			var _self = this;
			var subscriber = new Subscriber(_endpoint);
			subscriber.load(function(subscriber, response){
	            var subscriberId = null;
				if(subscriber) {
					subscriberId = subscriber.getSubscriberId(response);
				}
				var profile = new Profile(this, subscriberId);
				
				// only load if there is a valid subscriber id
				if (subscriberId) {
				    _self._load(profile,args);
				} else {
				    var error = new Error("Unable to retrieve subscriber id");
				    if (args.error) {
				        args.error(error);
				    } else if (args.handle) {
				        args.handle(error);
				    }
				}
				
				return profile;
			});
		},
		_load: function (profile, args) {
			var loadCb = null;
			var handleCb = null;
			if(args){
				 loadCb = args.load;
				 handleCb = args.handle;
			}
			var _self = this;
			var x = this._find(profile._id);
			if(x) {// the cached item is found. the call back function is called.
				profile.data = x;
				if(loadCb) loadCb(profile);
				if(handleCb)handleCb(profile);
			} else {// a network call is made to fetch the profile object and the cache is populated.
				if(requests[profile._id]) {
					// If there is a pending request for this id, then we simply add this callback to it
					if(loadCb){
						requests[profile._id].push(loadCb);
					}
					if(handleCb){
						requests[profile._id].push(handleCb);
					}
				} else {
					if(loadCb) {
						requests[profile._id] = [loadCb];
					}
					if(handleCb && requests[profile._id]) {
						requests[profile._id].push(handleCb);
					}else if (handleCb){
						requests[profile._id] = [handleCb];
					}
					var content = {format:	"json"};
					_endpoint.xhrGet({
						serviceUrl:	"/lotuslive-shindig-server/social/rest/people/lotuslive:user:" + profile._id + "/@self",
						handleAs:	"json",
						content:	content,
						load: function(response) {
				      		profile.data = response; 
				      		if(_self._profiles) {
				      			_self._profiles.put(profile._id,profile.data);
				      		}
							notifyCb(profile._id,profile);
						},
						error: function(error){
							_self._notifyError(error,args);
						}
					});
			} 
		}
	},
		_find: function(id) {
			if(null == id)
				return null;
			if(this._profiles) {
				return this._profiles.get(id);
			}
			return null;
		}
	});
	
	return ProfileService;
});
