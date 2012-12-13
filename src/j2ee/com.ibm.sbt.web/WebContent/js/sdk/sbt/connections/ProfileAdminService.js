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
define(['sbt/_bridge/declare','sbt/config','sbt/lang','sbt/connections/core','sbt/xml','sbt/util','sbt/xpath','sbt/Cache','sbt/connections/ProfileService','sbt/connections/ProfileConstants'],
		function(declare,cfg,lang,con,xml,util,xpath,Cache,profileService,constants) {
	
	var ProfileAdminService = declare("sbt.connections.ProfileAdminService", sbt.connections.ProfileService, {

		createProfile: function (inputProfile, args) {
			if(!(util.checkObjectClass(inputProfile, "sbt.connections.Profile",constants.sbtErrorMessages.args_profile, args)) ||!(util.checkNullValue(inputProfile._id, constants.sbtErrorMessages.null_profileId, args))){
				return;
			}
			var headers = {"Content-Type" : "application/atom+xml"};
			var _self = this;
			var _id= inputProfile._id;
			var _param = (inputProfile._idType == "email" || inputProfile._idType == "id" && _self._isEmail(_id)) ? "email=" : "userid=";			
			this._endpoint.xhrPost({
				serviceUrl:	con.profileUrls["createProfile"]+"?"+_param + encodeURIComponent(_id),
				postData:_self._constructCreateRequest(inputProfile,inputProfile.fields),
				headers:headers,
				load:function(data){
					inputProfile.fields = {};
					_self._load(inputProfile, args);					
				},
				error: function(error){
					_self._notifyError(error,args);
				}
			});
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
			if(!(util.checkObjectClass(inputProfile, "sbt.connections.Profile",constants.sbtErrorMessages.args_profile, args)) ||!(util.checkNullValue(inputProfile._id, constants.sbtErrorMessages.null_profileId, args))){
				return;
			}
			var headers = {};
			var _self = this;
			var _id= inputProfile._id;
			var _param = (inputProfile._idType == "email" || inputProfile._idType == "id" && _self._isEmail(_id)) ? "email=" : "userid=";
			headers["Content-Type"] = "application/atom+xml";
			this._endpoint.xhrDelete({
				serviceUrl:	con.profileUrls["deleteProfile"] + "?"+_param + encodeURIComponent(_id) ,
				headers:headers,
				load:function(data){
					if(_self._profiles) {
						_self._deleteIdFromCache(_id);
		      		}
					if(args.load) args.load();
					if(args.handle)args.handle();
				},
				error: function(error){
					_self._notifyError(error,args);
				}
			});
		},
	});
	
	return ProfileAdminService;
});
