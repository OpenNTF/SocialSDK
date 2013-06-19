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
 *	Social Business Toolkit SDK.
 *	@author Vimal Dhupar
 *
 *	Javascript APIs for IBM SmartCloud Profiles Service.
 *	@module sbt.smartcloud.ProfileService
**/

define(["../declare","../lang", "../config","../stringUtil","../Cache","./Subscriber","../Jsonpath","../base/BaseService", "../base/JsonDataHandler", "./ProfileConstants", "../base/BaseEntity"],
		function(declare, lang, config, StringUtil, Cache, Subscriber, JsonPath, BaseService, JsonDataHandler, Consts, BaseEntity) {
	/**
     * Profile class representing the Smartcloud User Profile.
     * 
     * @class Profile
     * @namespace sbt.smartcloud
     */
	var Profile = declare(BaseEntity, {
		/**
		 * Profile Class Constructor
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },
		
        /**
         * Loads the profile object with the profile entry document associated
         * with the profile. By default, a network call is made to load the
         * profile entry document in the profile object.
         * 
         * @method load
         * @param {Object}
         *            [args] Argument object
         * 
         */
		load: function(args) {
			var profileId = this.getId();
            var promise = this.service._validateProfileId(profileId);
            if (promise) {
                return promise;
            }

            var self = this;
            var callbacks = {
                createEntity : function(service,data,response) {
                    return new JsonDataHandler({
                        data : data,
                        jsonpath : Consts.ProfileJPath
                    });
                }
            };
            var requestArgs = {};
            requestArgs.userid = profileId;
            lang.mixin(requestArgs, args || {format:"json"});            
            var options = {
                handleAs : "json",
                query : requestArgs
            };
            return this.service.getEntity(consts.GetProfile, options, profileId, callbacks, args);
		},
		
		/**
		Get data feed
		@method getData
		@return {Json} json Feed
		**/
		getData: function() {
			return this.dataHandler.getEntityData();
		},
		
		/**
		Returns the id of the User
		@method getId
		@return {String} id of the User	
		**/
		getId: function () {
			return this.getAsString("id");
		},
		/**
		Get display name of the User
		@method getDisplayName
		@return {String} display name of the User	
		**/
		getDisplayName: function () {
			return this.getAsString("displayName");
		},
		/**
		Get email of the User
		@method getEmail
		@return {String} email of the User	
		**/
		getEmail: function () {
			return this.getAsString("emailAddress");
		},
		/**
		Get thumbnail URL of the User
		@method getThumbnailUrl
		@return {String} thumbnail URL of the User
		**/
		getThumbnailUrl: function () {
			var image = this.getAsString("thumbnailUrl");
			if(image)
				image = this.service.endpoint.baseUrl+"/contacts/img/photos/"+ image;  // TODO : work in making this generic
			return image;
		},
		/**
		Get address of the profile
		@method getAddress
		@return {String} Address object of the profile
		**/
		getAddress: function () { 
			var address = this.getAsArray("address");
			address = this.dataHandler.extractFirstElement(address);
			return address;
		},
		/**
		Get department of the profile
		@method getDepartment
		@return {String} department of the profile
		**/
		getDepartment: function () {
			return this.getAsString("department"); 
		},
		/**
		Get title of the profile
		@method getTitle
		@return {String} title of the profile
		**/
		getTitle: function () {
			return this.getAsString("title");
		},
		/**
		Get profile URL of the profile
		@method getProfileUrl
		@return {String} profile URL of the profile
		**/
		getProfileUrl: function () {
			return this.getAsString("profileUrl");
		},
		/**
		Get phone number of the profile
		@method getPhoneNumber
		@return {String} Phone number object of the profile
		**/
		getPhoneNumber: function () {
			return this.getAsString("phoneNumbers"); 
		},
		/**
		Get Country of the profile
		@method getCountry
		@return {String} country of the profile
		**/
		getCountry: function () { 
			return this.getAsString("country"); 
		},
		/**
		Get Organization Id of the profile
		@method getOrgId
		@return {String} Organization Id of the profile
		**/
		getOrgId: function () { 
			return this.getAsString("orgId"); 
		},
		/**
		Get "About Me"/description of the profile
		@method getAbout
		@return {String} description of the profile
		**/
		getAbout: function () { 
			return this.getAsString("about"); 
		}
	});
	
	/**
     * Callbacks used when reading an entry that contains a Profile.
     */
    var ProfileCallbacks = {
        createEntity : function(service,data,response) {
        	var entryHandler = new JsonDataHandler({
                    data : data,
                    jsonpath : Consts.ProfileJPath
                });

            return new Profile({
                service : service,
                id : entryHandler.getEntityId(),
                dataHandler : entryHandler
            });
        }
    };
    
    /**
     * Callbacks used when reading a feed that contains multiple Profiles.
     */
    var ProfileFeedCallbacks = {
        createEntities : function(service,data,response) {
        	return new JsonDataHandler({
                    data : data,
                    jsonpath : Consts.ProfileJPath
                });
        }, 
        
        createEntity : function(service,data,response) {
        	var entryHandler = new JsonDataHandler({
                data : data,
                jsonpath : Consts.ProfileJPath
            });

        return new Profile({
            service : service,
            id : entryHandler.getEntityId(),
            dataHandler : entryHandler
        });
        }
    };
	
	/**
	 * 	Profile service class associated with a profile service of IBM SmartCloud.
     * 
     * @class ProfileService
     * @namespace sbt.smartcloud
     */
	var ProfileService = declare(BaseService, {		
		_profiles: null,

		 /**
         * 
         * @constructor
         * @param args
         */
		constructor : function(args) {
            if (!this.endpoint) {
                this.endpoint = config.findEndpoint(this.getDefaultEndpointName());
            }
            if(!this._cache){
        		if(config.Properties.ProfileCacheSize || Consts.DefaultCacheSize){
        			this._cache = new Cache(config.Properties.ProfileCacheSize || Consts.DefaultCacheSize);
        		}        		
        	}     
        },

        /**
         * Return the default endpoint name if client did not specify one.
         * @returns {String}
         */
        getDefaultEndpointName: function() {
            return "smartcloud";
        },
		
        /**
         * Get the profile of a user.
         * 
         * @method getProfileByGUID
         * @param {String}
         *            userId Userid of the profile
         * @param {Object}
         *            args Argument object
         */
        getProfileByGUID : function(userId, args) {
            var idObject = this._toIdObject(userId);
            var promise = this._validateIdObject(idObject);
            if (promise) {
                return promise;
            }

            var requestArgs = lang.mixin(idObject, args || {format:"json"});
            var options = {
                method : "GET",
                handleAs : "json", 
                query : requestArgs
            };
            var entityId = idObject.userid;
            var url = this.constructUrl(Consts.GetProfileByGUID, {}, {idToBeReplaced : entityId});
            return this.getEntity(url, options, entityId, this.getProfileCallbacks());
        },

        /**
         * Get the contact details of a user.
         * 
         * @method getContactByGUID
         * @param {String}
         *            userId Userid of the profile
         * @param {Object}
         *            args Argument object
         */
        getContactByGUID : function(userId, args) {
            var idObject = this._toIdObject(userId);
            var promise = this._validateIdObject(idObject);
            if (promise) {
                return promise;
            }

            var requestArgs = lang.mixin(idObject, args || {format:"json"});
            var options = {
                method : "GET",
                handleAs : "json", 
                query : requestArgs
            };
            var entityId = idObject.userid;
            var url = this.constructUrl(Consts.GetContactByGUID, {}, {idToBeReplaced : entityId});
            return this.getEntity(url, options, entityId, this.getProfileCallbacks());
        },
        
        /**
         * Get logged in user's Connections
         * 
         * @method getMyConnections
         * @param {Object}
         *            args Argument object
         */
        getMyConnections : function(args) {
        	var options = {
        		method : "GET",
                handleAs : "json",
                 query : args || {format:"json"}
        	};
            return this.getEntities(Consts.GetMyConnections, options, this.getProfileFeedCallbacks());
        },
        
        /**
         * Get logged in user's Contacts
         * 
         * @method getMyContacts
         * @param {Object}
         *            args Argument object
         */
        getMyContacts : function(args) {
        	var options = {
            		method : "GET",
                    handleAs : "json",
                    query : args || {format:"json"}
        	};
        	return this.getEntities(Consts.GetMyContacts, options, this.getProfileFeedCallbacks());
        },
        
        /**
         * Get logged in user's Contacts considering the startIndex and count as provided by the user
         * 
         * @method getMyContactsByIndex
         * @param startIndex
         * @param count
         * @param {Object}
         *            args Argument object
         */
        getMyContactsByIndex : function(startIndex, count, args) {
        	var requestArgs = { "startIndex" : startIndex, "count" : count };
        	var options = {
            		method : "GET",
                    handleAs : "json",
                    query : lang.mixin(requestArgs , args || {format:"json"}) 
        	};
        	return this.getEntities(Consts.GetMyContacts, options, this.getProfileFeedCallbacks());
        },
        
        /**
         * Return callbacks for a profile entry
        **/
        getProfileCallbacks : function() {
            return ProfileCallbacks;
        },
        
        /**
         * Return callbacks for a profile feed
        **/
        getProfileFeedCallbacks : function() {
            return ProfileFeedCallbacks;
        },
        
        _toIdObject : function(profileOrId) {
            var idObject = {};
            if (lang.isString(profileOrId)) {
            	idObject.userid = profileOrId;
            } else if (profileOrId instanceof Profile) {
            	idObject.userid = profileOrId.getUserid();
            }
            return idObject;
        },
       
        _validateIdObject : function(idObject) {
            if (!idObject.userid) {
                return this.createBadRequestPromise("Invalid argument, userid must be specified.");
            }
        },
        _validateProfileId : function(profileId) {
            if (!profileId || profileId.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected userid");
            }
        },
   	});
	return ProfileService;
});
