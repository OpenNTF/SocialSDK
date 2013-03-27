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
define(['sbt/_bridge/declare','sbt/config','sbt/lang','sbt/connections/core','sbt/xml','sbt/util','sbt/xpath','sbt/Cache','sbt/connections/ProfileConstants','sbt/Endpoint','sbt/validate','sbt/base/BaseService', 'sbt/base/XmlHandler'],
		function(declare,cfg,lang,con,xml,util,xpath,Cache,profileConstants,Endpoint,validate, BaseService, XmlHandler) {
	
	/**
	Javascript APIs for IBM Connections Profiles Service.
	@module sbt.connections.ProfileService
	**/
	
	var handler = new XmlHandler({xpath_map: profileConstants.xpath_profile, xpath_feed_map: profileConstants.xpath_feed_profile,nameSpaces:con.namespaces});
	
	/**
	Profile class associated with a profile. 
	@class Profile
	@namespace connections
	**/
	
	var Profile = declare("sbt.connections.Profile", sbt.base.BaseEntity, {
		
		_idType:	"",// can take value - userId / email / id	
		_email:		"",
		_userId:	"",
		_name:		"",	
		
		constructor: function(svc,id,name,email) {
			
			var args = { entityName : "profile", Constants: profileConstants, con: con, dataHandler: handler};
			this.inherited(arguments, [svc, id, args]);
			if(this._service._isEmail(id)){
				this._email = id;
			}else{
				this._userId = id;
			}
			this._name = name;
			this._email = email;
			
		},
		/**
		Loads the profile object with the profile entry document associated with the profile. By
		default, a network call is made to load the profile entry document in the profile object.

		@method load
		@param {Object} [args]  Argument object
			@param {Boolean} [args.loadIt=true] Loads the profile object with profile entry document. To 
			instantiate an empty profile object associated with a profile (with no profile entry
			document), the load method must be called with this parameter set to false. By default, this 
			parameter is true.
			@param {Function} [args.load] The function profile.load invokes when the profile is 
			loaded from the server. The function expects to receive one parameter, 
			the loaded profile object.
			@param {Function} [args.error] Sometimes the load calls  fail. Often these are 404 errors 
			or server errors such as 500. The error parameter is another callback function
			that is only invoked when an error occurs. This allows to control what happens
		    when an error occurs without having to put a lot of logic into your load function
		    to check for error conditions. The parameter passed to the error function is a 
		    JavaScript Error object indicating what the failure was. From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
			@param {Function} [args.handle] This callback is called regardless of whether 
			the call to load the profile completes or fails. The  parameter passed to this callback
			is the profile object (or error object). From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
		
		**/
		
		load: function(args) {
			this._data = this._service._load(this, args);			
		},
		
		/**
		Updates the profile of a user.

		@method update
		@param {Object} args  Argument object
			@param {Boolean} [args.reloadIt=true] Reloads the profile object with 
			the updated profile entry document. By default, this parameter is true.
			@param {Function} [args.load] The function profile.update invokes when the 
			profile is updated. The function expects to receive one parameter, 
			the updated profile object.
			@param {Function} [args.error] Sometimes the update calls fails. The error parameter is 
			a callback function that is only invoked when an error occurs. This allows to 
			control what happens when an error occurs. The parameter passed to the error function 
			is a JavaScript Error object indicating what the failure was. From the error object. 
			you can get access to the javascript library error object, the status code and the 
			error message.
			@param {Function} [args.handle] This callback is called regardless of whether 
			the call to update the profile completes or fails. The  parameter passed to this callback
			is the profile object (or error object). From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
		
		**/
		
		update: function(args) {
			this._service.updateProfile(this, args);
			
		},
		
		/**
		Get id of the profile
		@method getId
		@return {String} id of the profile	
		**/
		getId: function () {
			if(!this._userId){
				this._userId = this.get("uid");
			}
			return this._userId;
		},
		/**
		Get display name of the profile
		@method getDisplayName
		@return {String} display name of the profile	
		**/
		getDisplayName: function () {	
			if(!this._name){
				this._name = this.get("name");
			}
			return this._name;
			
		},	
		/**
		Get groupware mail of the profile
		@method getGroupwareMail
		@return {String} email groupware mail of the profile	
		**/
		getGroupwareMail: function () {
			return this.get("groupwareMail");
		},
		/**
		Get email of the profile
		@method getEmail
		@return {String} email of the profile	
		**/
		getEmail: function () {
			if(!this._email){
				this._email = this.get("email");
			}
			return this._email;
		},
		/**
		Get thumbnail URL of the profile
		@method getThumbnailUrl
		@return {String} thumbnail URL of the profile
		**/
		getThumbnailUrl: function () {
			return this.get("photo");
		},
		/**
		Get title of the profile
		@method getTitle
		@return {String} title of the profile
		**/
		getTitle: function() {
			return this.get("title");
		},
		/**
		Get department of the profile
		@method getDepartment
		@return {String} department of the profile
		**/
		getDepartment: function() {
			return this.get("organizationUnit");
		},	
		/**
		Get address of the profile
		@method getAddress
		@return {Object} Address object of the profile
		**/
		getAddress: function() {			
			var address = {};
			if(this.get("countryName")){
				address.country = this.get("countryName");
			}
			if(this.get("locality")){
				address.locality = this.get("locality");
			}
			if(this.get("postalCode")){
				address.postalCode = this.get("postalCode");
			}
			if(this.get("region")){
				address.region = this.get("region");
			}
			if(this.get("streetAddress")){
				address.streetAddress = this.get("streetAddress");
			}
			if(this.get("extendedAddress")){
				address.extendedAddress = this.get("extendedAddress");
			}
			if(this.get("bldgId")){
				address.building = this.get("bldgId");
			}
			if(this.get("floor")){
				address.floor = this.get("floor");
			}
			return address;
		},	
		/**
		Get phone number of the profile
		@method getPhoneNumber
		@return {Object} Phone number object of the profile
		**/
		getPhoneNumber: function() {			
			return this.get("telephoneNumber");
		},	
		/**
		Get profile URL of the profile
		@method getProfileUrl
		@return {String} profile URL of the profile
		**/
		getProfileUrl: function() {
			return this.get("fnUrl");
		},
		/**
		Get Pronunciation URL of the profile
		@method getPronunciationUrl
		@return {String} Pronunciation URL of the profile
		**/
		getPronunciationUrl: function() {
			return this.get("soundUrl");
		},	
		/**
		Get "About" / description of the profile
		@method getAbout
		@return {String} description of the profile
		**/
		getAbout: function() {
			return this.get("summary");
		},
		/**
		Set work phone number of the profile in the field object
		@method setPhoneNumber
		@param {String} work phone number of the profile
		**/
		setPhoneNumber: function(newPhonenumber){
			this.set("telephoneNumber",newPhonenumber);
		},
		/**
		Set title of the profile in the field object
		@method setTitle
		@param {String} title of the profile
		**/
		setTitle: function(title){
			this.set("title",title);
		},
		/**
		Set the location of the file input element in the markup for editing profile photo
		in the field object
		@method setPhotoLocation
		@param {String} location of the file input element
		**/
		setPhotoLocation: function(imgLoc){
			this.set("imageLocation",imgLoc);
		},
		/**
		Set the address of the profile in the field object
		@method setPhotoLocation
		@param {Object} Address object of the profile.
		**/
		setAddress: function(address) {
			var countryName = address.country;
			var locality = address.locality;
			var postalCode = address.postalCode;
			var region = address.region;
			var streetAddress = address.streetAddress;
			var bldgId = address.building;
			var floor = address.floor;	
			if(countryName) {
				this.set("countryName",countryName);
			}
			if(locality) {
				this.set("locality",locality);
			}
			if(postalCode) {
				this.set("postalCode",postalCode);
			}
			if(region) {
				this.set("region",region);
			}
			if(streetAddress) {
				this.set("streetAddress",streetAddress);
			}
			if(bldgId) {
				this.set("bldgId",bldgId);
			}
			if(floor) {
				this.set("floor",floor);
			}
		},
		_validate : function(className, methodName, args, validateMap) {
			
			if (validateMap.isValidateType && !(validate._validateInputTypeAndNotify(className, methodName, "Profile", this, "sbt.connections.Profile", args))) {
				return false;
			}
			if (validateMap.isValidateId && !(validate._validateInputTypeAndNotify(className, methodName, "Profile Id", this._id, "string", args))) {
				return false;
			}			
			return true;
		}
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
	var ProfileService = declare("sbt.connections.ProfileService", sbt.base.BaseService, {		
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
			var profile = new Profile (service, id);
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
