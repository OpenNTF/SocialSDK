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
define(['sbt/_bridge/declare','sbt/config','sbt/lang','sbt/connections/core','sbt/xml','sbt/util','sbt/xpath','sbt/Cache','sbt/connections/ProfileConstants','sbt/Endpoint','sbt/validate','sbt/base/BaseService', 'sbt/base/BaseEntity', 'sbt/base/XmlHandler', 'sbt/connections/Profile'],
		function(declare,cfg,lang,con,xml,util,xpath,Cache,profileConstants,Endpoint,validate, BaseService, BaseEntity, XmlHandler, Profile) {
	
	/**
	Javascript APIs for IBM Connections Profiles Service.
	@module sbt.connections.ProfileService
	**/
	
	var handler = new XmlHandler({
	    xpath_map: profileConstants.xpath_profile, 
	    xpath_feed_map: profileConstants.xpath_feed_profile, 
	    nameSpaces:con.namespaces
	});
	
	
	
	/**
	Profile service class associated with a profile service of IBM Connections.

	@class ProfileService
	@constructor
	@param {Object} options  Options object
		@param {String} [options.endpoint=connections]  Endpoint to be used by ProfileService.
		@param {Integer} [options.cacheSize] Cache size to store profile entry documents of multiple
		profiles in one session.
	
	**/
	var ProfileService = declare(BaseService, {		
		_profiles: null,		

		constructor: function(_options) {
			var options = _options || {};			
			options = lang.mixin({endpoint: options.endpoint || "connections", Constants: profileConstants, con: con});
			this.inherited(arguments, [options]);			
		},
		/**
		Get the profile of a user.
		
		@method getProfile
		@param {Object} args  Argument object
			@param {String} args.userId UserId of the profile
			@param {String} args.email Email of the profile. If userId is provided, it will take 
			precedence over email
			@param {String} args.id Id of the profile. This can be either the userId or the email 
			of the profile. If userId or email parameters are provided, they would take precedence 
			over id.
			@param {Boolean} [args.loadIt=true] Loads the profile object with profile entry document.  If 
			an empty profile object associated with a profile (with no profile entry
			document), then the load method must be called with this parameter set to false. By default, this 
			parameter is true.
			@param {Function} [args.load] The function profile.getProfile invokes when the profile is 
			loaded from the server. The function expects to receive one parameter, 
			the loaded profile object.
			@param {Function} [args.error] Sometimes the getProfile call fail with bad request such as 400  
			or server errors such as 500. The error parameter is another callback function
			that is only invoked when an error occurs. This allows to control what happens
		    when an error occurs without having to put a lot of logic into your load function
		    to check for error conditions. The parameter passed to the error function is a 
		    JavaScript Error object indicating what the failure was. From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
			@param {Function} [args.handle] This callback is called regardless of whether 
			the call to get the profile completes or fails. The  parameter passed to this callback
			is the profile object (or error object). From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
		
		**/
		
		getProfile: function(args) {
			//return lang.isArray(args.id) ? this._getMultiple(args) : this._getOne(args);
			//Not handling the multiple profiles id use case now.
			
			return this._getOne(args, {entityName: "profile", serviceEntity: "getProfile", entityType: "",
				entity: Profile, cachingEnabled : true				
			});
		},
		
		_getOne: function(args, getArgs) {			
			if (!(validate._validateInputTypeAndNotify("ProfileService", "getProfile", "args", args, "object", args))) {
				return ;
			}
			if (!(validate._validateInputTypeAndNotify("ProfileService", "getProfile", "args.userId/args.email/args.id", args.userId || args.email || args.id, "string", args))) {
				return ;
			}			
			var content = {};
			var _args = lang.mixin({},args); 
			if(_args.userId){
				_args.id = args.userId;
				_args._idType = profileConstants._userIdentifiers.userId;
				content.userid = _args.id;
			}else if(_args.email){
				_args.id = args.email;
				_args._idType = profileConstants._userIdentifiers.email;
				content.email = _args.id;
			}else if(args.id){				
				_args._idType = profileConstants._userIdentifiers.id;
				content.userid = _args.id;
			}			
			getArgs.urlParams = content;			
			var profile = this.inherited(arguments, [_args, getArgs]);			
			return profile;
		},
		
		_createEntityObject : function (service, id){
			var profile = new Profile (service, id, handler);
			return profile;
		},
		
		_constructProfileUrl: function(methodName){
			var authType = "";
			if(this._endpoint.authType == "basic"){
				authType = "";
			}else if (this._endpoint.authType == "oauth"){
				authType = "/oauth";
			}else {
				authType = "";
			}
			
			return con.profileUrls["profileServiceBaseUrl"] + authType + con.profileUrls[methodName];
		},
		_constructCreateRequest: function (profile, fieldMap){
			var body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns:app=\"http://www.w3.org/2007/app\" xmlns:thr=\"http://purl.org/syndication/thread/1.0\" xmlns:fh=\"http://purl.org/syndication/history/1.0\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\" xmlns=\"http://www.w3.org/2005/Atom\"><category term=\"profile\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category><content type=\"application/xml\"><person xmlns=\"http://ns.opensocial.org/2008/opensocial\"><com.ibm.snx_profiles.attrib>";
			for(key in fieldMap){
				body += "<entry><key>" + profileConstants._createMapAttributes[key] + "</key><value><type>text</type><data>"+ xml.encodeXmlEntry(fieldMap[key]) + "</data></value></entry>";
			}			
			body += "</com.ibm.snx_profiles.attrib></person></content></entry>";
			return body;
		},
		
		_constructAddress: function(fieldMap){
			var addressBody = "";
			addressBody += profileConstants._updateMapAttributes["workLocation"] + ":" + ";;" ;
			addressBody += fieldMap["streetAddress"]? xml.encodeXmlEntry(fieldMap["streetAddress"]) + ";" : ";";
			addressBody += fieldMap["locality"]? xml.encodeXmlEntry(fieldMap["locality"]) + ";" : ";";	
			addressBody += fieldMap["region"]? xml.encodeXmlEntry(fieldMap["region"]) + ";" : ";";
			addressBody += fieldMap["postalCode"]? xml.encodeXmlEntry(fieldMap["postalCode"]) + ";" : ";";
			addressBody += fieldMap["countryName"]? xml.encodeXmlEntry(fieldMap["countryName"]) : "";
			addressBody += "\n";
			return addressBody;
		},
		_removeAddressKeys: function(fieldMap){
			if(fieldMap["streetAddress"]){
				delete fieldMap["streetAddress"];
			}
			if(fieldMap["region"]){
				delete fieldMap["region"];
			}
			if(fieldMap["postalCode"]){
				delete fieldMap["postalCode"];
			}
			if(fieldMap["locality"]){
				delete fieldMap["locality"];
			}
			if(fieldMap["countryName"]){
				delete fieldMap["countryName"];
			}
		},
		_constructUpdateRequestBody: function(profile, fieldMap){
			var body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns:app=\"http://www.w3.org/2007/app\" xmlns:thr=\"http://purl.org/syndication/thread/1.0\" xmlns:fh=\"http://purl.org/syndication/history/1.0\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\" xmlns=\"http://www.w3.org/2005/Atom\"><category term=\"profile\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category><content type=\"text\">\nBEGIN:VCARD\nVERSION:2.1\n";
			for(key in fieldMap){
				if(key == "countryName" || key == "locality" || key == "postalCode" || key == "region" || key == "streetAddress"){					
					body += this._constructAddress(fieldMap);
					this._removeAddressKeys(fieldMap);
				}
				else{
					body += profileConstants._updateMapAttributes[key] + ":"+ xml.encodeXmlEntry(fieldMap[key]) + "\n";					
					delete fieldMap[key];					
				}
			}
			body += "END:VCARD\n</content></entry>";
			return body;
		},
		/**
		Updates the profile of a user.

		@method updateProfile
		@param {Object} inputProfile  Profile object whose profile you want to update
		@param {Object} [args]  Argument object
			@param {Boolean} [args.reloadIt=true] Reloads the profile object with 
			the updated profile entry document. By default, this parameter is true.
			@param {Function} [args.load] The function profile.updateProfile will invoke when the 
			profile is updated. The function expects one parameter, the updated profile object.
			@param {Function} [args.error] Sometimes the update calls fails due to bad request (400 error).
		 	The error parameter is a callback function that is only invoked when an error occurs. This allows 
		 	to write logic when an error occurs. The parameter passed to the error function 
			is a JavaScript Error object indicating what the failure was. From the error object. 
			one can access the javascript library error object, the status code and the 
			error message.
			@param {Function} [args.handle] This callback function is called regardless of whether 
			the call to update the profile completes or fails. The  parameter passed to this callback
			is the profile object (or error object). From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
		
		**/
		updateProfile: function (inputProfile, args) {			
			if (!(validate._validateInputTypeAndNotify("ProfileService", "updateProfile", "Profile", inputProfile, "sbt.connections.Profile", args))) {
				return ;
			}
			if (!inputProfile._validate("ProfileService", "updateProfile", args, {
				isValidateType : true,
				isValidateId : true
			})) {
				return;
			}
			
			var _id = inputProfile._id;
			var _self = this;
			var headers = {"Content-Type" : "application/atom+xml"};
			var _param = {};
			if(_self._isEmail(_id)){
				_param.email = _id;
			}else{
				_param.userid = _id;
			}
			var _fields = lang.mixin({},inputProfile._fields);
			
			this._updateEntity(args,{
				entityName: "profile", serviceEntity: "updateProfile", entityType: "",
				entity: inputProfile,
				headers:headers,
				xmlPayload : this._constructUpdateRequestBody(inputProfile,_fields),						
				urlParams : _param,
				
			});			
		},
		
		_updateEntityOnLoad : function (data, ioArgs, inputProfile, args){
			this._deleteIdFromCache(inputProfile._id, inputProfile._idType);
			if(args && args.reloadIt != false){
				var _param = {};
				if(this._isEmail(inputProfile._id)){
					_param.email = inputProfile._id;
				}else{
					_param.userid = inputProfile._id;
				}
				this._load(inputProfile, args,{entityName: "profile", serviceEntity: "getProfile", entityType: "",
					entity: Profile, cachingEnabled : true, urlParams : _param	
				});	
			}
		},
			
		/**
		Updates the profile photo of a user.

		@method updateProfilePhoto
		@param {Object} inputProfile  Profile object whose profile photo one wants to update
		@param {Object} [args]  Argument object
			@param {Boolean} [args.reloadIt=true] Reloads the profile object with 
			the updated profile entry document. By default, this parameter is true.
			@param {Function} [args.load] The function profile.updateProfilePhoto will invoke when the 
			profile photo is updated. The function expects one parameter, the updated profile object.
			@param {Function} [args.error] Sometimes the update profile photo calls fails due to bad request (400 error).
		 	The error parameter is a callback function that is only invoked when an error occurs. This allows 
		 	to write logic when an error occurs. The parameter passed to the error function 
			is a JavaScript Error object indicating what the failure was. From the error object. 
			one can access the javascript library error object, the status code and the 
			error message.
			@param {Function} [args.handle] This callback function is called regardless of whether 
			the call to update the profile photo completes or fails. The  parameter passed to this callback
			is the profile object (or error object). From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
		
		**/	
		updateProfilePhoto: function (inputProfile,args) {			
			if (!(validate._validateInputTypeAndNotify("ProfileService", "updateProfilePhoto", "Profile", inputProfile, "sbt.connections.Profile", args))) {
				return ;
			}
			if (!inputProfile._validate("ProfileService", "updateProfilePhoto", args, {
				isValidateType : true,
				isValidateId : true
			})) {
				return;
			}
			
			var _id = inputProfile._id;
			var control = document.getElementById(inputProfile.fields.imageLocation);
		    var files = control.files;
		    var reader = new FileReader();
		    var _self = this;
		    reader.onload = function(event) {
		    	 	var binaryContent = event.target.result;									
					var headers = {"Content-Type" : "image/gif"};					
					var _param = _self._isEmail(_id) ? "email=" : "userid=";				
					_self._endpoint.xhrPut({
						serviceUrl:	_self._constructProfileUrl(profileConstants._methodName.updateProfilePhoto) + "?" + _param +encodeURIComponent(_id),
						putData:binaryContent,					
						headers:headers,
						load:function(data){
							_self._deleteIdFromCache(_id);
							if(args && args.reloadIt != false){
							_self._load(inputProfile, args);
							}
						},
						error: function(error){
							validate.notifyError(error,args);
						}
					});
		    };
		    reader.readAsArrayBuffer(files[0]);   
		},
		
		_validateProfileObject : function(profile, args){
			if (!(validate._validateInputTypeAndNotify("ProfileService", "updateProfile", "Profile", profile, "sbt.connections.Profile", args))) {
				return ;
			}
			if (!profile._validate("ProfileService", "updateProfile", args, {
				isValidateType : true,
				isValidateId : true
			})) {
				return;
			}
		},
		
		getColleagues : function(profile, args){			
			this._validateProfileObject(profile, args);			
			var params = {};
			if(args && args.parameters){
				params = args.parameters;
			}			
			if(this._isEmail(profile._id)) {
				params.email = profile._id; 
			} else {
				params.userid = profile._id; 
			}
			params.connectionType = "colleague";				
			params.outputType = "profile";
			
			var headers = {
					"Content-Type" : "application/atom+xml"
				};
			
			this._getEntities(args,
					{entityName: "profile", serviceEntity: "getColleagues", entityType: "",						
						entity: Profile, urlParams : params, headers : headers, dataHandler: handler
						
					});
		},
		_findIdForEmailInCache:function(emailAtr){
			if(this._cache) {				
				return this._cache.browse(function(k,v) {// see this code again
					var email = xpath.selectText(v,profileConstants.xpath_profile.email,con.namespaces);
					if(email==emailAtr) {
						return k;
					}
				});
			}
			return null;
		},
	});	
	return ProfileService;
});
