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
define([ "../declare", "../lang", "../config", "../stringUtil", "./ProfileConstants", "../base/BaseService", "../base/BaseEntity", "../base/XmlDataHandler", "../base/VCardDataHandler", "../Cache" ], function(
        declare,lang,config,stringUtil,consts,BaseService,BaseEntity,XmlDataHandler, VCardDataHandler, Cache) {

	var updateProfileXmlTemplate = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns:app=\"http://www.w3.org/2007/app\" xmlns:thr=\"http://purl.org/syndication/thread/1.0\" xmlns:fh=\"http://purl.org/syndication/history/1.0\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\" xmlns=\"http://www.w3.org/2005/Atom\"><category term=\"profile\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category><content type=\"text\">\nBEGIN:VCARD\nVERSION:2.1\n${jobTitle}${address}${telephoneNumber}${building}${floor}END:VCARD\n</content></entry>";
    var updateProfileAttributeTemplate = "${attributeName}:${attributeValue}\n";
    var updateProfileAddressTemplate = "ADR;WORK:;;${streetAddress},${extendedAddress};${locality};${region};${postalCode};${countryName}\n";
    var createProfileTemplate = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns:app=\"http://www.w3.org/2007/app\" xmlns:thr=\"http://purl.org/syndication/thread/1.0\" xmlns:fh=\"http://purl.org/syndication/history/1.0\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\" xmlns=\"http://www.w3.org/2005/Atom\"><category term=\"profile\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category><content type=\"application/xml\"><person xmlns=\"http://ns.opensocial.org/2008/opensocial\"><com.ibm.snx_profiles.attrib>${guid}${email}${uid}${distinguishedName}${displayName}${givenNames}${surname}${userState}</com.ibm.snx_profiles.attrib></person></content></entry>";
    var createProfileAttributeTemplate = "<entry><key>${attributeName}</key><value><type>text</type><data>${attributeValue}</data></value></entry>";
   
    var OAuthString = "/oauth";
    var basicAuthString = "";
    var defaultAuthString = "";
    /**
     * Profile class.
     * 
     * @class Profile
     * @namespace sbt.connections
     */
    var Profile = declare(BaseEntity, {

        /**
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {            
        },

        /**
         * Get id of the profile
         * 
         * @method getUserid
         * @return {String} id of the profile
         * 
         */
        getUserid : function() {
            return this.getAsString("userid");
        },

        /**
         * Get name of the profile
         * 
         * @method getName
         * @return {String} name of the profile
         * 
         */
        getName : function() {
            return this.getAsString("name");
        },

        /**
         * Get email of the profile
         * 
         * @method getEmail
         * @return {String} email of the profile
         */
        getEmail : function() {
            return this.getAsString("email");
        },

        /**
         * Get groupware mail of the profile
         * 
         * @method getGroupwareMail
         * @return {String} groupware mail of the profile
         */
        getGroupwareMail : function() {
            return this.getAsString("groupwareMail");
        },

        /**
         * Get thumbnail URL of the profile
         * 
         * @method getThumbnailUrl
         * @return {String} thumbnail URL of the profile
         */
        getThumbnailUrl : function() {
            return this.getAsString("photoUrl");
        },

        /**
         * Get job title of the profile
         * 
         * @method getJobTitle
         * @return {String} job title of the profile
         */
        getJobTitle : function() {
            return this.getAsString("jobTitle");
        },

        /**
         * Get department of the profile
         * 
         * @method getDepartment
         * @return {String} department of the profile
         */
        getDepartment : function() {
            return this.getAsString("organizationUnit");
        },

        /**
         * Get address of the profile
         * 
         * @method getAddress
         * @return {Object} Address object of the profile
         */
        getAddress : function() {
            return this.getAsObject(consts.AddressFields);
        },
        /**
         * Get telephone number of the profile
         * 
         * @method getTelephoneNumber
         * @return {String} Phone number of the profile
         */
        getTelephoneNumber : function() {
            return this.getAsString("telephoneNumber");
        },

        /**
         * Get profile URL of the profile
         * 
         * @method getProfileUrl
         * @return {String} profile URL of the profile
         */
        getProfileUrl : function() {
            return this.getAsString("fnUrl");
        },
        /**
         * Get building name of the profile
         * 
         * @method getBuilding
         * @return {String} building name of the profile
         */
        getBuilding : function() {
            return this.getAsString("building");
        },
        /**
         * Get floor address of the profile
         * 
         * @method getFloor
         * @return {String} floor address of the profile
         */
        getFloor : function() {
            return this.getAsString("floor");
        },

        /**
         * Get Pronunciation URL of the profile
         * 
         * @method getPronunciationUrl
         * @return {String} Pronunciation URL of the profile
         */
        getPronunciationUrl : function() {
            return this.getAsString("soundUrl");
        },

        /**
         * Get summary of the profile
         * 
         * @method getSummary
         * @return {String} description of the profile
         */
        getSummary : function() {
            return this.getAsString("summary");
        },

        /**
         * Set work phone number of the profile in the field object
         * 
         * @method setTelephoneNumber
         * @param {String} telephoneNumber work phone number of the profile
         */
        setTelephoneNumber : function(telephoneNumber) {
            this.setAsString("telephoneNumber", telephoneNumber);
        },
        
        /**
         * Set building of the profile in the field object
         * 
         * @method setBuilding
         * @param {String} building building name of the profile
         */
        setBuilding : function(building) {
            this.setAsString("building", building);
        },
        
        /**
         * Set floor number of the profile in the field object
         * 
         * @method setFloor
         * @param {String} floor floor number of the profile
         */
        setFloor : function(floor) {
            this.setAsString("floor", floor);
        },

        /**
         * Set job title of the profile in the field object
         * 
         * @method setJobTitle
         * @param {String} title job title of the profile
         */
        setJobTitle : function(title) {
            this.setAsString("jobTitle", title);
        },

        /**
         * Set the location of the file input element in the markup for editing
         * profile photo in the field object
         * 
         * @method setPhotoLocation
         * @param {String} imgLoc location of the file input element
         */
        setPhotoLocation : function(imgLoc) {
            this.setAsString("imageLocation", imgLoc);
        },

        /**
         * Set the address of the profile in the field object
         * 
         * @method setAddress
         * @param {Object} address Address object of the profile.
         */
        setAddress : function(address) {
            this.setAsObject(address);
        },

        /**
         * Loads the profile object with the profile entry document associated
         * with the profile. By default, a network call is made to load the
         * profile entry document in the profile object.
         * 
         * @method load
         * @param {Object} [args] Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         * 
         */
        load : function(args) {        	
            var profileId = this.getUserid() || this.getEmail();
            var promise = this.service._validateProfileId(profileId);
            if (promise) {
                return promise;
            }

            var self = this;
            var callbacks = {
                createEntity : function(service,data,response) {
                    self.dataHandler = new XmlDataHandler({
                        data : data,
                        namespaces : consts.Namespaces,
                        xpath : consts.ProfileXPath
                    });
                    self.id = self.dataHandler.getEntityId();
                    return self;
                }
            };
            var requestArgs = {};
            if (this.service.isEmail(profileId)) {
            	requestArgs.email = profileId;
            } else {
            	requestArgs.userid = profileId;
            }            	
            lang.mixin(requestArgs, args || {});            
            var options = {
                handleAs : "text",
                query : requestArgs
            };
            
            var url = this.service.constructUrl(consts.AtomProfileDo, {}, {authType : this.service._getProfileAuthString()});
            return this.service.getEntity(url, options, profileId, callbacks, args);
        },

        /**
         * Updates the profile of a user.
         * 
         * @method update
         * @param {Object} [args] Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        update : function(args) {
        	return this.service.updateProfile(this, args);
        },
        /**
         * Get colleagues of the profile.
         * 
         * @method getColleagues
         * @param {Object} [args] Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        getColleagues : function(args){
        	return this.service.getColleagues(this, args);
        }
    });

    /**
     * Callbacks used when reading an entry that contains a Profile.
     */
    var ProfileCallbacks = {
        createEntity : function(service,data,response) {
            var entryHandler = null;
            if (response.args && response.args.format == "vcard") {
                entryHandler = new VCardDataHandler({
                    data : data,
                    namespaces : consts.Namespaces,
                    xpath : consts.ProfileVCardXPath
                });
            } else {
                entryHandler = new XmlDataHandler({
                    data : data,
                    namespaces : consts.Namespaces,
                    xpath : consts.ProfileXPath
                });
            }
            return new Profile({
                service : service,
                id : entryHandler.getEntityId(),
                dataHandler : entryHandler
            });
        }
    };
    
    /**
     * Callbacks used when reading a feed that contains Profile entries.
     */
    var ProfileFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ProfileFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = null;
            if (response.args && response.args.format == "vcard") {
                entryHandler = new VCardDataHandler({
                    data : data,
                    namespaces : consts.Namespaces,
                    xpath : consts.ProfileVCardXPath
                });
            } else {
                entryHandler = new XmlDataHandler({
                    data : data,
                    namespaces : consts.Namespaces,
                    xpath : consts.ProfileXPath
                });
            }
            return new Profile({
                service : service,
                id : entryHandler.getEntityId(),
                dataHandler : entryHandler
            });
        }
    };
    
    /**
     * ProfileService class.
     * 
     * @class ProfileService
     * @namespace sbt.connections
     */
    var ProfileService = declare(BaseService, {

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
        		if(config.Properties.ProfileCacheSize || consts.DefaultCacheSize){
        			this._cache = new Cache(config.Properties.ProfileCacheSize || consts.DefaultCacheSize);
        		}        		
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
        * Create a Profile object with the specified data.
        * 
        * @method newProfile
        * @param {Object} args Object containing the fields for the 
        *            new Profile 
        */
        
        newProfile : function(args) {
            return this._toProfile(args);
        },

        /**
         * Get the profile of a user.
         * 
         * @method getProfile
         * @param {String} userIdOrEmail Userid or email of the profile
         * @param {Object} [args] Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        getProfile : function(userIdOrEmail, args) {           
        	var profile = this._toProfile(userIdOrEmail);
            var promise = this._validateProfile(profile);
            if (promise) {
                return promise;
            }
            
            return profile.load(args);
        },
        
        /**
         * Update an existing profile
         * 
         * @method updateProfile
         * @param {Object} profileOrJson Profile object to be updated
         * @param {Object} [args] Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        updateProfile : function(profileOrJson,args) {
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
                    method : "PUT",
                    query : requestArgs,
                    headers : consts.AtomXmlHeaders,
                    data : this._constructProfilePostData(profile)
                };   
            var url = this.constructUrl(consts.AtomProfileEntry, {}, {authType : this._getProfileAuthString()});
            return this.updateEntity(url, options, callbacks, args);
            
        },      
        
        /**
         * Get the colleagues for the specified profile
         * 
         * @method getColleagues
         * @param {Object/String} profileOrId profile object representing the profile or user/email of the profile
         * @param {Object} args Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        getColleagues : function(profileOrId, args) {
            // detect a bad request by validating required arguments
            var idObject = this._toIdObject(profileOrId);
            var promise = this._validateIdObject(idObject);
            if (promise) {
                return promise;
            }
            
            var requestArgs = lang.mixin(idObject, {
                connectionType : "colleague",
                outputType : "profile"
            }, args || {});
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            var url = this.constructUrl(consts.AtomConnectionsDo, {}, {authType : this._getProfileAuthString()});
            return this.getEntities(url, options, this.getProfileFeedCallbacks(), args);
        },
        
        /*
         * Return callbacks for a profile feed
         */
        getProfileFeedCallbacks : function() {
            return ProfileFeedCallbacks;
        },

        /*
         * Return callbacks for a profile entry
         */
        getProfileCallbacks : function() {
            return ProfileCallbacks;
        },

        /*
         * 
         */
        _toIdObject : function(profileOrId) {
            var idObject = {};
            if (lang.isString(profileOrId)) {
                var userIdOrEmail = profileOrId;
                if (this.isEmail(userIdOrEmail)) {
                    idObject.email = userIdOrEmail;
                } else {
                    idObject.userid = userIdOrEmail;
                }
            } else if (profileOrId instanceof Profile) {
                if (profileOrId.getUserid()) {
                    idObject.userid = profileOrId.getUserid();
                }
                else if (profileOrId.getEmail()) {
                    idObject.email = profileOrId.getEmail();
                }
            }
            return idObject;
        },
        
        /*
         * Validate an ID object
         */
        _validateIdObject : function(idObject) {
            if (!idObject.userid && !idObject.email) {
                return this.createBadRequestPromise("Invalid argument, userid or email must be specified.");
            }
        },
        
        /*
         * Return a Profile instance from Profile or JSON or String. Throws
         * an error if the argument was neither.
         */
        _toProfile : function(profileOrJsonOrString,args) {
            if (profileOrJsonOrString instanceof Profile) {
                return profileOrJsonOrString;
            } else {
            	var profileJson = profileOrJsonOrString;
            	if (lang.isString(profileOrJsonOrString)) {
            		profileJson = {};
            		if(this.isEmail(profileOrJsonOrString)){
            			profileJson.email = profileOrJsonOrString;
            		}else{
            			profileJson.userid = profileOrJsonOrString;
            		}
                }else{ // handle the case when the profileJson has id attribute. id can take either userid or email.
                	if(profileJson && profileJson.id && !profileJson.userid && !profileJson.email){
                		this.isEmail(profileJson.id) ? profileJson.email = profileJson.id : profileJson.userid = profileJson.id;
                		delete profileJson.id;
                	}
                }
                return new Profile({
                    service : this,
                    _fields : lang.mixin({}, profileJson)
                });
            }
        },
        
        /**
         * Returns true if address is to be updated else false.
         */
        _AddressToBeUpdated : function(profile){
        	return (profile._fields["streetAddress"] || profile._fields["extendedAddress"] || profile._fields["locality"] || profile._fields["region"] || profile._fields["postalCode"] || profile._fields["countryName"]);
        },
        /**
         * Constructs update profile request body.
         */
        _constructProfilePostData : function(profile) {
            var transformer = function(value,key) {
                if (key == "address") {                	
                	value = profile.service._AddressToBeUpdated(profile) ? stringUtil.transform(updateProfileAddressTemplate, {"streetAddress" : profile._fields["streetAddress"], 
                	"extendedAddress" : profile._fields["extendedAddress"], "locality" : profile._fields["locality"], "region" : profile._fields["region"],
                	"postalCode" : profile._fields["postalCode"], "countryName" : profile._fields["countryName"]}) : null;
                } 
                else{                	
                	value = (profile._fields[key])? stringUtil.transform(updateProfileAttributeTemplate, {"attributeName" : consts.ProfileVCardXPath[key], "attributeValue" : profile._fields[key]}) : null;
                	
                }
                return value;
            };
            return stringUtil.transform(updateProfileXmlTemplate, profile, transformer, profile);
        },
        
        _constructProfilePutData : function(profile) {
            var transformer = function(value,key) {
            	if(profile._fields[key]){
	                value = stringUtil.transform(createProfileAttributeTemplate, {"attributeName" : consts.profileCreateAttributes[key], "attributeValue" : profile._fields[key]});
	                return value;
            	}
            };
            return stringUtil.transform(createProfileTemplate, profile, transformer, profile);
        },
        
        _validateProfile : function(profile) {
            if (!profile || (!profile.getUserid() && !profile.getEmail())) {
                return this.createBadRequestPromise("Invalid argument, profile with valid userid or email must be specified.");
            }            
        },
        
        _validateProfileId : function(profileId) {
            if (!profileId || profileId.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected userid or email");
            }
        },
        _getProfileAuthString : function(){
        	if (this.endpoint.authType == consts.AuthTypes.Basic){
        		return basicAuthString;
        	}else if(this.endpoint.authType == consts.AuthTypes.OAuth){
        		return OAuthString;
        	}else{
        		return defaultAuthString;
        	}
        }

    });
    return ProfileService;
});
