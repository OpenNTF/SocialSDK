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
 * JavaScript API for IBM Connections Profile Service.
 * 
 * @module sbt.connections.ProfileService
 */
define([ "../declare", "../lang", "../config", "../stringUtil", "./ProfileConstants", "../base/BaseService", "../base/XmlDataHandler", "./ProfileService" ], function(
        declare,lang,config,stringUtil,consts,BaseService,XmlDataHandler, ProfileService) { 
    
    /**
     * ProfileAdminService class.
     * 
     * @class ProfileAdminService
     * @namespace sbt.connections
     */
    var ProfileAdminService = declare(ProfileService , {
    	
        /**
         * Create a new profile
         * 
         * @method createProfile
         * @param {Object} profileOrJson Profile object or json representing the profile to be created.
         * @param {Object} [args] Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        createProfile : function(profileOrJson,args) {
            var profile = this._toProfile(profileOrJson);
            var promise = this._validateProfile(profile);
            if (promise) {
                return promise;
            }

            var requestArgs = {};
            profile.getUserid() ? requestArgs.userid = profile.getUserid() : requestArgs.email = profile.getEmail();
            lang.mixin(requestArgs, args || {});
            
            var callbacks = {};            
            callbacks.createEntity = function(service,data,response) {                
                return profile;
            };        
            
            var options = {
                    method : "POST",
                    query : requestArgs,
                    headers : consts.AtomXmlHeaders,
                    data : this._constructProfilePutData(profile)
                };   
            
            return this.updateEntity(consts.AdminAtomProfileDo, options, callbacks, args);
            
        },
        
        /**
         * Delete an existing profile
         * 
         * @method deleteProfile
         * @param {Object} profileId userid or email of the profile
         * @param {Object}[args] Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        deleteProfile : function(profileId,args) {
        	var promise = this._validateProfileId(profileId);
            if (promise) {
                return promise;
            }

            var requestArgs = {};
            this.isEmail(profileId) ? requestArgs.email = profileId : requestArgs.userid = profileId;
            lang.mixin(requestArgs, args || {});
            
            var options = {
                method : "DELETE",
                query : requestArgs,
                handleAs : "text"
            };
            
            return this.deleteEntity(consts.AdminAtomProfileEntryDo, options, profileId, args);
            
        }        
    });
    return ProfileAdminService;
});
