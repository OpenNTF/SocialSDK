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
define(['sbt/_bridge/declare','sbt/config','sbt/lang','sbt/connections/core','sbt/xml','sbt/util','sbt/xpath','sbt/Cache','sbt/connections/ProfileConstants','sbt/Endpoint'],
		function(declare,cfg,lang,con,xml,util,xpath,Cache,constants,Endpoint) {
	
	/**
	Javascript APIs for IBM Connections Profiles Service.
	@module sbt.connections.ProfileService
	**/
	var requests = {};
	function _notifyCb(id,param) {
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
	@namespace connections
	**/
	
	var Profile = declare("sbt.connections.Profile", null, {
		_service:	null,
		_id:		"",
		_idType:	"",// can take value - userId / email / id		
		/**
		Profile entry document of the associated profile
		@private
		@property data 
		@type String
		**/
		data:		null,
		/**
		Field object associated with the profile. It holds the values of the profile attributes that
		are used during updation of a profile and creation of a new profile.
		@private
		@property fields 
		@type Object
		**/
		fields:		{},		
		
		constructor: function(svc,id) {
			this._service = svc;
			this._id = id;
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
			this.data = this._service._load(this, args);			
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
		Return the xpath expression for a field in the profile entry document.
		@method fieldXPath
		@param {String} fieldName Xml element name in profile entry document.
		@return {String} xpath for the element in profile entry document.	
		**/
		fieldXPath: function(fieldName) {
			return constants.xpath_profile[fieldName];
		},
		/**
		Return the value of a field in profile entry document using xpath expression
		@method xpath
		@param {String} path xpath expression
		@return {String} value of a field in profile entry document using the xpath expression
		**/
		xpath: function(path) {
			return this.data && path ? xpath.selectText(this.data,path,con.namespaces) : null;
		},
		/**
		Return the value of a field in profile entry document
		@method get
		@param {String} fieldName fieldname
		@return {String} value of the field in profile entry document
		**/
		get: function(fieldName) {
			return this.fields[fieldName] || this.xpath(this.fieldXPath(fieldName));
		},
		/**
		Set the value of a field in the field object
		@method set
		@param {String} fieldName fieldname
		@param {String} value value of the field
		**/
		set: function(fieldName,value) {
			this.fields[fieldName] = value;
		},
		/**
		Remove a field from the field object
		@method remove
		@param {String} fieldName fieldname		
		**/
		remove: function(fieldName) {
			delete this.fields[fieldName];
		},
		/**
		Get id of the profile
		@method getId
		@return {String} id of the profile	
		**/
		getId: function () {
			return this.get("uid");
		},
		/**
		Get display name of the profile
		@method getDisplayName
		@return {String} display name of the profile	
		**/
		getDisplayName: function () {			
			return this.get("name");
		},	
		/**
		Get email of the profile
		@method getEmail
		@return {String} email of the profile	
		**/
		getEmail: function () {
			return this.get("email");
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
	var ProfileService = declare("sbt.connections.ProfileService", null, {
		_endpoint: null,
		_profiles: null,		

		constructor: function(options) {
			options = options || {};			
			this._endpoint = Endpoint.find(options.endpoint ||'connections');			
			var cs = options.cacheSize;
			if(cs && cs>0) {
				this._profiles = new Cache(cs);
			}
		},
		
		_notifyError: function(error, args){			
				if(args.error)args.error(error);
				if(args.handle)args.handle(error);	
				else{
					util.log("Error received. Error Code = %d. Error Message = %s" , error.code, error.message);
				}
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
			return this._getOne(args);
		},
		
		_getOne: function(args) {
			
			if(typeof args != "object"){
				util.log(constants.sbtErrorMessages.args_object);
				return;
			}
			if(!(args.userId || args.email || args.id)){
				util.notifyError({code:constants.sbtErrorCodes.badRequest,message:constants.sbtErrorMessages.null_id},args);
				return;
			}
			var profile = null;
			if(args.userId){
				 profile = new Profile(this,args.userId);
				 profile._idType = constants._userIdentifiers.userId;
			}else if(args.email){
				profile = new Profile(this,args.email);
				profile._idType = constants._userIdentifiers.email;
			}else if(args.id){
				profile = new Profile(this,args.id);
				profile._idType = constants._userIdentifiers.id;
			}			
			if(args.loadIt == false){
				if(args.load){
					args.load(profile);
				}
				if(args.handle){
					args.handle(profile);
				}
			}
			else{
				this._load(profile,args);
			}
			return profile;
		},
		
		/*_getMultiple: function(ids,cb,options) {
			// For now. Should later use a single call for multiple entries
			var a = [];
			for(var i=0; i<ids.length; i++) {
				a[i] = this._getOne(ids[i],cb,options);
			}
			return a;
		},*/
		
		_load: function (profile,args) {			
			if(!(util.checkNullValue(profile._id, constants.sbtErrorMessages.null_profileId, args))){
				return;
			}
			var loadCb = null;
			var handleCb = null;
			if(args){
				 loadCb = args.load;
				 handleCb = args.handle;
			}
			var _self = this;
			var x = this._find(profile._id,profile._idType);
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
					var content = {};
					if((profile._idType == constants._userIdentifiers.email) || (profile._idType == constants._userIdentifiers.id && this._isEmail(profile._id))) {
						content.email = profile._id; 
					} else {
						content.userid = profile._id; 
					}
					this._endpoint.xhrGet({
						serviceUrl:	this._constructProfileUrl(constants._methodName.getProfile),
						handleAs:	"text",
						content:	content,
						load: function(data, ioArgs) {
							profile.data = xml.parse(data);							
							profile.fields = {};
							if(_self._profiles) {
				      			_self._profiles.put(xpath.selectText(profile.data,constants.xpath_profile.uid,con.namespaces),profile.data);
				      		}
							_notifyCb(profile._id,profile);
						},
						error: function(error){
							_self._notifyError(error,args);
							
						}
					});
				}
			}
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
			if(methodName == constants._methodName.getProfile){				
				return con.profileUrls["profileServiceBaseUrl"] + authType + con.profileUrls["getProfile"];
			}
			if(methodName == constants._methodName.updateProfile){				
				return con.profileUrls["profileServiceBaseUrl"] + authType + con.profileUrls["updateProfile"];
			}
			if(methodName == constants._methodName.updateProfilePhoto){				
				return con.profileUrls["profileServiceBaseUrl"] + authType + con.profileUrls["updateProfilePhoto"];
			}			
		},
		_constructCreateRequest: function (profile, fieldMap){
			var body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns:app=\"http://www.w3.org/2007/app\" xmlns:thr=\"http://purl.org/syndication/thread/1.0\" xmlns:fh=\"http://purl.org/syndication/history/1.0\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\" xmlns=\"http://www.w3.org/2005/Atom\"><category term=\"profile\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category><content type=\"application/xml\"><person xmlns=\"http://ns.opensocial.org/2008/opensocial\"><com.ibm.snx_profiles.attrib>";
			for(key in fieldMap){
				body += "<entry><key>" + constants._createMapAttributes[key] + "</key><value><type>text</type><data>"+ xml.encodeXmlEntry(fieldMap[key]) + "</data></value></entry>";
			}			
			body += "</com.ibm.snx_profiles.attrib></person></content></entry>";
			return body;
		},
		
		_constructAddress: function(fieldMap){
			var addressBody = "";
			addressBody += constants._updateMapAttributes["workLocation"] + ":" + ";;" ;
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
					body += constants._updateMapAttributes[key] + ":"+ xml.encodeXmlEntry(fieldMap[key]) + "\n";					
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
			if(!(util.checkObjectClass(inputProfile, "sbt.connections.Profile",constants.sbtErrorMessages.args_profile, args)) ||!(util.checkNullValue(inputProfile._id, constants.sbtErrorMessages.null_profileId, args))){
				return;
			}
			var _id = inputProfile._id;
			var _self = this;
			var headers = {"Content-Type" : "application/atom+xml"};
			var _param = _self._isEmail(_id) ? "email=" : "userid=";
			var _fields = lang.mixin({},inputProfile.fields);
			this._endpoint.xhrPut({
				serviceUrl:	this._constructProfileUrl(constants._methodName.updateProfile)+ "?"+ _param +encodeURIComponent(_id)+"&output=vcard&format=full",
				putData:this._constructUpdateRequestBody(inputProfile,_fields),
				headers:headers,
				load:function(data){					
					_self._deleteIdFromCache(_id, inputProfile._idType);
					if(args && args.reloadIt != false){
						_self._load(inputProfile, args);	
					}
				},
				error: function(error){
					_self._notifyError(error,args);
				}
			});
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
			if(!(util.checkObjectClass(inputProfile, "sbt.connections.Profile",constants.sbtErrorMessages.args_profile, args)) ||!(util.checkNullValue(inputProfile._id, constants.sbtErrorMessages.null_profileId, args))){
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
						serviceUrl:	_self._constructProfileUrl(constants._methodName.updateProfilePhoto) + "?" + _param +encodeURIComponent(_id),
						putData:binaryContent,					
						headers:headers,
						load:function(data){
							_self._deleteIdFromCache(_id);
							if(args && args.reloadIt != false){
							_self._load(inputProfile, args);
							}
						},
						error: function(error){
							_self._notifyError(error,args);
						}
					});
		    };
		    reader.readAsArrayBuffer(files[0]);   
		},
		
		_find: function(id, idType) {
			// Case of an email: find it in the cache
			if(this._profiles) {
				if((idType == constants._userIdentifiers.email) || (idType == constants._userIdentifiers.id && this._isEmail(id))) {
					var _self = this;
					return this._profiles.browse(function(k,v) {
						var email = xpath.selectText(v,constants.xpath_profile.email,con.namespaces);
						if(email==id) {
							return _self._profiles.get(k);
						}else{
							return null;
						}
					});
				}else{
					return this._profiles.get(id);
				}
			}else{			
				return null;
			}
		},
		
		_isEmail: function(id) {
			return id && id.indexOf('@')>=0;
		},
		
		_findIdForEmailInCache:function(emailAtr){
			if(this._profiles) {
				//var _self = this;
				return this._profiles.browse(function(k,v) {
					var email = xpath.selectText(v,constants.xpath_profile.email,con.namespaces);
					if(email==emailAtr) {
						return k;
					}
				});
			}
			return null;
		},
		
		_deleteIdFromCache:function(_id, idType){
			if(this._profiles) {
				if(!((idType == constants._userIdentifiers.email) || (idType == constants._userIdentifiers.id && this._isEmail(id)))){
					this._profiles.remove(_id);
				}else{
					this._profiles.remove(this._findIdForEmailInCache(_id));
				}
			}
		}
	});	
	return ProfileService;
});
