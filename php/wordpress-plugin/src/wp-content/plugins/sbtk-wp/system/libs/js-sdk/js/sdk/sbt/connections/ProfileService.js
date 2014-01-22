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
define([ "../declare", "../lang", "../config", "../stringUtil", "./ProfileConstants", "../base/BaseService", "../base/BaseEntity", "../base/AtomEntity", "../base/XmlDataHandler", "../base/VCardDataHandler", "../Cache", "../util"  ], function(
        declare,lang,config,stringUtil,consts,BaseService,BaseEntity,AtomEntity,XmlDataHandler, VCardDataHandler, Cache, util) {
	
	var CategoryProfile = "<category term=\"profile\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category>";
	var updateProfileXmlTemplate = "\nBEGIN:VCARD\nVERSION:2.1\n${jobTitle}${address}${telephoneNumber}${building}${floor}END:VCARD\n";
	var updateProfileAttributeTemplate = "${attributeName}:${attributeValue}\n";
    var updateProfileAddressTemplate = "ADR;WORK:;;${streetAddress},${extendedAddress};${locality};${region};${postalCode};${countryName}\n";
    var ContentTmpl = "<content type=\"${contentType}\">${content}</content>";   
    var createProfileTemplate = "<person xmlns=\"http://ns.opensocial.org/2008/opensocial\"><com.ibm.snx_profiles.attrib>${guid}${email}${uid}${distinguishedName}${displayName}${givenNames}${surname}${userState}</com.ibm.snx_profiles.attrib></person>";
    var createProfileAttributeTemplate = "<entry><key>${attributeName}</key><value><type>text</type><data>${attributeValue}</data></value></entry>";
    
    var CategoryConnection = "<category term=\"connection\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\" />";
    var CategoryConnectionType = "<category term=\"${getConnectionType}\" scheme=\"http://www.ibm.com/xmlns/prod/sn/connection/type\" />";
    var CategoryStatus = "<category term=\"${getStatus}\" scheme=\"http://www.ibm.com/xmlns/prod/sn/status\" />";
   
    var OAuthString = "/oauth";
    var basicAuthString = "";
    var defaultAuthString = "";
    
    /**
     * Profile class.
     * 
     * @class Profile
     * @namespace sbt.connections
     */
    
    /*
     * ProfileDataHandler class.
     */
   
    
    
    var Profile = declare(AtomEntity, {

    	xpath : consts.ProfileXPath,
    	namespaces : consts.ProfileNamespaces,    	
    	categoryScheme : CategoryProfile,
    	_update : false,
    	
    	/**
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {            
        },
        
        /**
         * Data handler for profile object based on the query paramter - "output", used to get the profile.
         * If the value of output paramter is "vcard", the VCardDataHandler is associated with
         * the profile object else, by default, XmlDataHandler is associated with the object.
         *  
         *  @param {Object} service  ProfileService instance associated with the profile object
         *  @param {Object} data Profile document associated with the profile object
         *  @response {Object} response Response object returned after the operation for get/create/update 
         *  of the profile
         *  @namespaces {Object} namespace NameSpace object associated with the profile 
         *  @xpath {Object} xpath XPath object associated with the profile 
         */
        
        
        createDataHandler : function(service, data, response, namespaces, xpath) {            	
    	  if (response.options && response.options.query && response.options.query.output == "vcard") {
              return new VCardDataHandler({
            	  service: service,
                  data : data,
                  namespaces : namespaces,
                  xpath : consts.ProfileVCardXPath
              });
          } else {
              return new XmlDataHandler({
            	  service: service,
                  data : data,
                  namespaces : namespaces,
                  xpath : xpath
              });
          }
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
            	  self.setData(data, response);
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
        },
        /**
         * Get colleague connections of the profile.
         * 
         * @method getColleagueConnections
         * @param {Object} [args] Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        getColleagueConnections : function(args){
        	return this.service.getColleagueConnections(this, args);
        },
        
        /**
         * Return content element to be included in post data for the creation or updaton of new profile entry.
         * 
         * @method createContent
         * @returns {String}
         */
        createContent : function() { 
        	if(this._update){
        		this._update = false;
	        	var transformer = function(value,key) {
	                if (key == "address") {                	
	                	value = this.service._isAddressSet(this) ? stringUtil.transform(updateProfileAddressTemplate, {"streetAddress" : this._fields["streetAddress"], 
	                	"extendedAddress" : this._fields["extendedAddress"], "locality" : this._fields["locality"], "region" : this._fields["region"],
	                	"postalCode" : this._fields["postalCode"], "countryName" : this._fields["countryName"]}) : null;
	                } 
	                else{                	
	                	value = (this._fields[key])? stringUtil.transform(updateProfileAttributeTemplate, {"attributeName" : consts.ProfileVCardXPath[key], "attributeValue" : this._fields[key]}) : null;
	                	
	                }
	                return value;
	            };
	            var content = stringUtil.transform(updateProfileXmlTemplate, this, transformer, this);
	            if (content) {
	        		return stringUtil.transform(ContentTmpl, { "contentType" : "text", "content" : content });
	        	}
	        	return "";
        	}else{
        		var transformer = function(value,key) {
                	if(this._fields[key]){
    	                value = stringUtil.transform(createProfileAttributeTemplate, {"attributeName" : consts.profileCreateAttributes[key], "attributeValue" : this._fields[key]});
    	                return value;
                	}
                };
                var content = stringUtil.transform(createProfileTemplate, this, transformer, this);
                if(content){
                	return stringUtil.transform(ContentTmpl, { "contentType" : "application/xml", "content" : content });
                }
                return "";
        	}
        	
        },
        
        /**
         * Return tags elements to be included in post data for creation and updation of profile entry.
         * 
         * @method createTags
         * @returns {String}
         */
        createTags : function() {            
        	return "";
        }
        
    });
    
    /**
     * ColleagueConnection class.
     * 
     * @class ConnectionEntry
     * @namespace sbt.connections
     */
    var ColleagueConnection = declare(AtomEntity, {

        /**
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {            
        }
    });

    /**
     * ProfileTag class.
     * 
     * @class ProfileTag
     * @namespace sbt.connections
     */
    var ProfileTag = declare(BaseEntity, {

        /**
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {            
        },

        /**
         * Get term of the profile tag
         * 
         * @method getTerm
         * @return {String} term of the profile tag
         * 
         */
        getTerm : function() {
            return this.getAsString("term");
        },
        
        /**
         * Get frequency of the profile tag
         * 
         * @method getFrequency
         * @return {Number} frequency of the profile tag
         * 
         */
        getFrequency : function() {
            return this.getAsNumber("frequency");
        },
        
        /**
         * Get intensity of the profile tag
         * 
         * @method getIntensity
         * @return {Number} intensity of the profile tag
         * 
         */
        getIntensity : function() {
            return this.getAsNumber("intensity");
        },
        
        /**
         * Get visibility of the profile tag
         * 
         * @method getVisibility
         * @return {Number} visibility of the profile tag
         * 
         */
        getVisibility : function() {
            return this.getAsNumber("visibility");
        }
        
    });
    
    /**
     * Invite class.
     * 
     * @class Invite
     * @namespace sbt.connections
     */
    var Invite = declare(AtomEntity, {

    	xpath : consts.InviteXPath,
    	contentType : "html",
    	categoryScheme : CategoryConnection,
    	    	
        /**
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {            
        },
        
        /**
         * Return extra entry data to be included in post data for this entity.
         * 
         * @returns {String}
         */
        createEntryData : function() {
        	var entryData = "";
        	entryData += stringUtil.transform(CategoryConnectionType, this, function(v,k) { return v; }, this);
        	entryData += stringUtil.transform(CategoryStatus, this, function(v,k) { return v; }, this);
            return stringUtil.trim(entryData);
        },
        
        /**
         * Return tags elements to be included in post data for creation and updation of invite entry.
         * 
         * @method createTags
         * @returns {String}
         */
        createTags : function() {            
        	return "";
        },
        
        
        /**
         * Return the connection type associated with this invite. 
         * 
         * @method getConnectionType
         * @return {String} status
         */
        getConnectionType : function() {
        	var connectionType = this.getAsString("connectionType");
        	return connectionType || consts.TypeColleague;
        },
                
        /**
         * Set the connection type associated with this invite. 
         * 
         * @method setConnectionType
         * @param {String} status
         */
        setConnectionType : function(connectionType) {
        	return this.setAsString("connectionType", connectionType);
        },
                
        /**
         * Return the status associated with this invite. 
         * 
         * @method getStatus
         * @return {String} status
         */
        getStatus : function() {
        	var status = this.getAsString("status");
        	return status || consts.StatusPending;
        },
        
        /**
         * Set the status associated with this invite. 
         * 
         * @method setStatus
         * @param {String} status
         */
        setStatus : function(status) {
        	return this.setAsString("status", status);
        },
        
        /**
         * Return the connection id associated with this invite. 
         * 
         * @method getConnectionId
         * @return {String} connectionId
         */
        getConnectionId : function() {
        	return this.getAsString("connectionId");
        },
        
        /**
         * Set connection id associated with this invite. 
         * 
         * @method setConnectionId
         * @param connectionId
         * @return {Invite} 
         */
        setConnectionId : function(connectionId) {
        	return this.setAsString("connectionId", connectionId);
        }
    });
    
    /**
     * Callbacks used when reading an entry that contains a Profile.
     */
    var ProfileCallbacks = {
        createEntity : function(service,data,response) {
            return new Profile({
                service : service,
                data : data,
                response: response                
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
            return new Profile({
                service : service,
                data: data,
                response: response
            });
        }
    };
    
    /**
     * Callbacks used when reading a feed that contains ColleagueConnections
     */
    var ColleagueConnectionFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ProfileFeedXPath
            });
        },
        createEntity : function(service,data,response) {            
            return new ColleagueConnection({
                service : service,
                data: data,
                response: response
            });
        }
    };
    
    /**
     * Callbacks used when reading a feed that contains Profile Tag entries.
     */
    var ProfileTagFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ProfileTagsXPath
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = new XmlDataHandler({
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ProfileTagsXPath
            });
            return new ProfileTag({
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
        
        contextRootMap: {
            profiles: "profiles"
        },

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
         * Create a Invite object with the specified data.
         * 
         * @method newInvite
         * @param {Object} args Object containing the fields for the 
         *            new Invite 
         */
         newInvite : function(args) {
             return this._toInvite(args);
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
            profile._update = true;
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
                    data : profile.createPostData()
                };   
            var url = this.constructUrl(consts.AtomProfileEntryDo, {}, {authType : this._getProfileAuthString()});

            return this.updateEntity(url, options, callbacks, args);
        },      
        
        /**
         * Get the tags for the specified profile
         * 
         * @method getTags
         * @param {String} id userId/email of the profile
         * @param {Object} args Object representing various query parameters that can be passed. The parameters must 
         * be exactly as they are supported by IBM Connections.
         */
        getTags : function(id, args) {
            // detect a bad request by validating required arguments
            var idObject = this._toTargetObject(id);
            var promise = this._validateTargetObject(idObject);
            if (promise) {
                return promise;
            }
            
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin(idObject, args || {})
            };
            var url = this.constructUrl(consts.AtomTagsDo, {}, {authType : this._getProfileAuthString()});

            return this.getEntities(url, options, this.getProfileTagFeedCallbacks(), args);
        },
        
        /**
         * Get the colleagues for the specified profile
         * 
         * @method getColleagues
         * @param {String} id userId/email of the profile
         * @param {Object} args Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        getColleagues : function(id, args) {
            // detect a bad request by validating required arguments
            var idObject = this._toIdObject(id);
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
        
        /**
         * Get the colleagues for the specified profile as Collegue Connection entries
         * 
         * @method getColleagueConnections
         * @param {String} id userId/email of the profile
         * @param {Object} args Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        getColleagueConnections : function(id, args) {
            // detect a bad request by validating required arguments
            var idObject = this._toIdObject(id);
            var promise = this._validateIdObject(idObject);
            if (promise) {
                return promise;
            }
            
            var requestArgs = lang.mixin(idObject, {
                connectionType : "colleague",
				outputType : "connection"
            }, args || {});
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            var url = this.constructUrl(consts.AtomConnectionsDo, {}, {authType : this._getProfileAuthString()});
            return this.getEntities(url, options, this.getColleagueConnectionFeedCallbacks(), args);
        },
        
        /**
         * Get the reporting chain for the specified person.
         * 
         * @method getReportingChain
         * @param {String} id userId/email of the profile
         * @param {Object} args Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        getReportingChain : function(id, args) {
            // detect a bad request by validating required arguments
            var idObject = this._toIdObject(id);
            var promise = this._validateIdObject(idObject);
            if (promise) {
                return promise;
            }
            
            var requestArgs = lang.mixin(idObject, args || {});
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            var url = this.constructUrl(consts.AtomReportingChainDo, {}, {authType : this._getProfileAuthString()});
            return this.getEntities(url, options, this.getProfileFeedCallbacks(), args);
        },
        
        /**
         * Get the people managed for the specified person.
         * 
         * @method getPeopleManaged
         * @param {String} id userId/email of the profile
         * @param {Object} args Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        getPeopleManaged : function(id, args) {
            // detect a bad request by validating required arguments
            var idObject = this._toIdObject(id);
            var promise = this._validateIdObject(idObject);
            if (promise) {
                return promise;
            }
            
            var requestArgs = lang.mixin(idObject, args || {});
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            var url = this.constructUrl(consts.AtomPeopleManagedDo, {}, {authType : this._getProfileAuthString()});
            return this.getEntities(url, options, this.getProfileFeedCallbacks(), args);
        },
                
        /**
         * Search for a set of profiles that match a specific criteria and return them in a feed.
         * 
         * @method search
         * @param {Object} args Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        search : function(args) {
            // detect a bad request by validating required arguments
            if (!args) {
            	return this.createBadRequestPromise("Invalid arguments, one or more of the input parameters to narrow the search must be specified.");
            }
            
            var options = {
                method : "GET",
                handleAs : "text",
                query : args
            };
            var url = this.constructUrl(consts.AtomSearchDo, {}, {authType : this._getProfileAuthString()});
            return this.getEntities(url, options, this.getProfileFeedCallbacks(), args);
        },
        
        /**
		 * Updates the profile photo of a user.
		 * @method updateProfilePhoto
		 * @param {Object} fileControlOrId The Id of html control or the html control
		 * @param @param {String} id userId/email of the profile 
		 * @param {Object} [args] The additional parameters
		 */
		updateProfilePhoto: function (fileControlOrId, id, args) {
			var promise = this.validateField("File Control Or Id", fileControlOrId);
			if (promise) {
				return promise;
			}
			promose = this.validateHTML5FileSupport();
			if(promise){
				return promise;
			}			
			
			var idObject = this._toIdObject(id);
			var files = null;
			if (typeof fileControlOrId == "string") {
				var fileControl = document.getElementById(fileControlOrId);
				filePath = fileControl.value;
				files = fileControl.files;
			} else if (typeof fileControlOrId == "object") {
				filePath = fileControlOrId.value;
				files = fileControlOrId.files;
			} else {
				return this.createBadRequestPromise("File Control or ID is required");
			}

			if(files.length != 1){
				return this.createBadRequestPromise("Only one file needs to be provided to this API");
			}
			
			var file = files[0];
			var formData = new FormData();
			formData.append("file", file);
			var requestArgs = lang.mixin(idObject, args || {});		
			var url = this.constructUrl(config.Properties.serviceUrl + "/files/" + this.endpoint.proxyPath + "/" + "connections" + "/" +  "UpdateProfilePhoto" + "/" + encodeURIComponent(file.name),
					args && args.parameters ? args.parameters : {});
			var headers = {
				"Content-Type" : false,
				"Process-Data" : false // processData = false is reaquired by jquery 
			};			
			var options = {
				method : "PUT",
				headers : headers,
				query : requestArgs || {},
				data : formData
			};
			var callbacks = {
					createEntity : function(service, data, response) {
						return data; // Since this API does not return any response in case of success, returning empty data
					}
			};

			return this.updateEntity(url, options, callbacks);			
		},
        
        /**
         * Invite a person to become your colleague.
         * 
         * @method inviteColleague
         * @param id
         * @param inviteOrJson
         * @param args
         */
		createInvite : function(id, inviteOrJson, args) {
            // detect a bad request by validating required arguments
            var idObject = this._toIdObject(id);
            var promise = this._validateIdObject(idObject);
            if (promise) {
                return promise;
            }
            
            var invite = this._toInvite(inviteOrJson);

            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
                invite.setData(data);
                var connectionId = this.getLocationParameter(response, "connectionId");
                invite.setConnectionId(connectionId);
                return invite;
            };

            var requestArgs = lang.mixin(idObject, args || {});
            var options = {
                method : "POST",
                query : requestArgs,
                headers : consts.AtomXmlHeaders,
                data : invite.createPostData()
            };
            
            var url = this.constructUrl(consts.AtomConnectionsDo, {}, {authType : this._getProfileAuthString()});
            return this.updateEntity(url, options, callbacks, args);
        },

        //
        // Internals
        //
        
        /*
         * Return callbacks for a profile feed
         */
        getProfileFeedCallbacks : function() {
            return ProfileFeedCallbacks;
        },
        
        /*
         * Return callbacks for a ColleagueConnection feed
         */
        getColleagueConnectionFeedCallbacks : function() {
            return ColleagueConnectionFeedCallbacks;
        },

        /*
         * Return callbacks for a profile entry
         */
        getProfileCallbacks : function() {
            return ProfileCallbacks;
        },

        /*
         * Return callbacks for a profile tag feed
         */
        getProfileTagFeedCallbacks : function() {
            return ProfileTagFeedCallbacks;
        },
        
        /*
         * Convert profile or key to id object
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
         * Convert profile or key to target object
         */
        _toTargetObject : function(profileOrId) {
            var targetObject = {};
            if (lang.isString(profileOrId)) {
                var userIdOrEmail = profileOrId;
                if (this.isEmail(userIdOrEmail)) {
                	targetObject.targetEmail = userIdOrEmail;
                } else {
                	targetObject.targetKey = userIdOrEmail;
                }
            } else if (profileOrId instanceof Profile) {
                if (profileOrId.getUserid()) {
                	targetObject.targetKey = profileOrId.getUserid();
                }
                else if (profileOrId.getEmail()) {
                	targetObject.targetEmail = profileOrId.getEmail();
                }
            }
            return targetObject;
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
         * Validate an Target object
         */
        _validateTargetObject : function(idObject) {
            if (!idObject.targetKey && !idObject.targetEmail) {
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
        
        /*
         * Return a Invite instance from Invite or JSON or String. Throws
         * an error if the argument was neither.
         */
        _toInvite : function(inviteOrJsonOrString,args) {
            if (inviteOrJsonOrString instanceof Invite) {
                return inviteOrJsonOrString;
            } else {
            	if (lang.isString(inviteOrJsonOrString)) {
            		inviteOrJsonOrString = {
            			content : inviteOrJsonOrString
            		};
                } 
                return new Invite({
                    service : this,
                    _fields : lang.mixin({}, inviteOrJsonOrString)
                });
            }
        },
        
        /*
         * Returns true if an address field has been set.
         */
        _isAddressSet : function(profile){
        	return (profile._fields["streetAddress"] || profile._fields["extendedAddress"] || profile._fields["locality"] || profile._fields["region"] || profile._fields["postalCode"] || profile._fields["countryName"]);
        },
        
        /*
         * Validate a Profile object
         */
        _validateProfile : function(profile) {
            if (!profile || (!profile.getUserid() && !profile.getEmail())) {
                return this.createBadRequestPromise("Invalid argument, profile with valid userid or email must be specified.");
            }            
        },
        
        /*
         * Validate a Profile id
         */
        _validateProfileId : function(profileId) {
            if (!profileId || profileId.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected userid or email");
            }
        },
        
        _getProfileAuthString : function(){
        	if (this.endpoint.authType == consts.AuthTypes.Basic) {
        		return basicAuthString;
        	} else if (this.endpoint.authType == consts.AuthTypes.OAuth) {
        		return OAuthString;
        	} else {
        		return defaultAuthString;
        	}
        }

    });
    return ProfileService;
});
