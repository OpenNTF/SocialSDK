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
define(['sbt/_bridge/declare','sbt/config','sbt/lang','sbt/connections/core','sbt/xml','sbt/util','sbt/xpath','sbt/Cache','sbt/connections/ProfileService','sbt/connections/ProfileConstants','sbt/validate'],
		function(declare,cfg,lang,con,xml,util,xpath,Cache,profileService,constants,validate) {
	
	var ProfileAdminService = declare("sbt.connections.ProfileAdminService", sbt.connections.ProfileService, {
		
		constructor: function(_options) {
			this.inherited(arguments, [_options]);
		},
		
		createProfile: function (inputProfile, args) {
			if (!(validate._validateInputTypeAndNotify("ProfileAdminService", "createProfile", "Profile", inputProfile, "sbt.connections.Profile", args))) {
				return ;
			}
			if (!inputProfile._validate("ProfileAdminService", "createProfile", args, {
				isValidateId : true
			})) {
				return;
			}
			
			var headers = {"Content-Type" : "application/atom+xml"};
			var _self = this;
			var _id= inputProfile._id;
			var param = {};
			if(inputProfile._idType == "email"){
				param.email = _id;
			}else{
				param.userid = _id;
			}
			this._createEntity(args,{
				entityName: "profile", serviceEntity: "createProfile", entityType: "",
				entity: inputProfile,
				headers:headers,
				xmlPayload : _self._constructCreateRequest(inputProfile,inputProfile._fields),						
				urlParams : param				
			});		
		},
		_createEntityOnLoad : function (data, ioArgs, inputProfile, args){
			var param = {};
			if(inputProfile._idType == "email"){
				param.email = inputProfile._id;
			}else{
				param.userid = inputProfile._id;
			}
			inputProfile.fields = {};				
			this._load(inputProfile, args,{entityName: "profile", serviceEntity: "getProfile", entityType: "", _cachingEnabled : true, urlParams:param});	
			
		},
		
		deleteProfile: function (inputProfile, args) {			
			if(!(typeof inputProfile == "object")){
				var profile = new Profile(this, inputProfile);				
				profile._idType = "id";
				this._deleteProfile(profile, args);
			}else{
				this._deleteProfile(inputProfile, args);
			}			
		},
		_deleteProfile: function (inputProfile, args) {
			if (!(validate._validateInputTypeAndNotify("ProfileAdminService", "deleteProfile", "Profile", inputProfile, "sbt.connections.Profile", args))) {
				return ;
			}
			if (!inputProfile._validate("ProfileAdminService", "deleteProfile", args, {
				isValidateId : true
			})) {
				return;
			}
			var headers = {};
			headers["Content-Type"] = "application/atom+xml";
			var _self = this;
			var _id= inputProfile._id;
			var param = {};
			if(inputProfile._idType == "email"){
				param.email = _id;
			}else{
				param.userid = _id;
			}			
			this._deleteEntity(args,{
				entityName: "profile", serviceEntity: "deleteProfile", entityType: "",
				entity: inputProfile,
				headers:headers,									
				urlParams : param
			});		
		},
	});
	
	return ProfileAdminService;
});
