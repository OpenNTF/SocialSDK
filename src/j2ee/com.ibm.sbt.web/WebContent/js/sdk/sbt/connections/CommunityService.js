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
 * Javascript APIs for IBM Connections Community Service.
 * 
 * @module sbt.connections.CommunityService
 */
define(['sbt/_bridge/declare', 'sbt/config', 'sbt/lang', 'sbt/connections/core', 'sbt/xml', 'sbt/util', 'sbt/xpath', 'sbt/Cache', 'sbt/Endpoint',
				'sbt/connections/CommunityConstants','sbt/validate', 'sbt/base/BaseService', 'sbt/base/XmlHandler', 'sbt/base/BaseEntity' ],
		function(declare, cfg, lang, con, xml, util, xpath, Cache, Endpoint, communityConstants, validate, BaseService, XmlHandler, BaseEntity) {

			/**
			 * Community class associated with a community.
			 * 
			 * @class Community
			 * @namespace connections
			 */
			var communityHandler = new XmlHandler({xpath_map: communityConstants.xpath_community, xpath_feed_map: communityConstants.xpath_feed_community,nameSpaces:con.namespaces});
			var memberHandler = new XmlHandler({xpath_map: communityConstants.xpath_member, xpath_feed_map: communityConstants.xpath_feed_member,nameSpaces:con.namespaces});
			
			var Community = declare(BaseEntity , {
				_service : null,
				_id : "",
				_author : null,
				_contributor : null,
				data : null,
				fields : {},

				constructor : function(svc, id) {					
					var args = { entityName : "community", Constants: communityConstants, con: con, dataHandler: communityHandler};
					this.inherited(arguments, [svc, id, args]);
					this._service = svc;
					this._id = id;
				},
				/**
				 * Loads the community object with the atom entry associated with the community. By default, a network call is made to load the atom entry
				 * document in the community object.
				 * 
				 * @method load
				 * @param {Object} [args] Argument object
				 * @param {Boolean} [args.loadIt=true] Loads the community object with atom entry document of the community. To instantiate an empty community object
				 *            associated with a community (with no atom entry document), the load method must be called with this parameter set to false. By
				 *            default, this parameter is true.
				 * @param {Function} [args.load] The function community.load invokes when the community is loaded from the server. The function expects to receive one
				 *            parameter, the loaded community object.
				 * @param {Function} [args.error] Sometimes the load calls fail. Often these are 404 errors or server errors such as 500. The error parameter is
				 *            another callback function that is only invoked when an error occurs. This allows to control what happens when an error occurs
				 *            without having to put a lot of logic into your load function to check for error conditions. The parameter passed to the error
				 *            function is a JavaScript Error object indicating what the failure was. From the error object. one can get access to the javascript
				 *            library error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback is called regardless of whether the call to load the community completes or fails. The parameter
				 *            passed to this callback is the community object (or error object). From the error object. one can get access to the javascript
				 *            library error object, the status code and the error message.
				 */
				load : function(args) {					
					this.data = this._service._load(this, args);					
				},
				/**
				 * Updates the community object.
				 * 
				 * @method update
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The function community.load invokes when the community is loaded from the server. The function expects to receive one
				 *            parameter, the loaded community object.
				 * @param {Function} [args.error] Sometimes the load calls fail. Often these are due to bad request like http error code 400 or server errors like http
				 *            error code 500. The error parameter is another callback function that is only invoked when an error occurs. This allows to control
				 *            what happens when an error occurs without having to put a lot of logic into your load function to check for error conditions. The
				 *            parameter passed to the error function is a JavaScript Error object indicating what the failure was. From the error object. one
				 *            can get access to the javascript library error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback is called regardless of whether the call to load the community completes or fails. The parameter
				 *            passed to this callback is the community object (or error object). From the error object. one can get access to the javascript
				 *            library error object, the status code and the error message.
				 */
				update : function(args) {
					this._service.updateCommunity(this, args);
				},

				
				
				/**
				 * Return the value of IBM Connections community attribute from community ATOM entry document.
				 * 
				 * @method get
				 * @param {String} fieldName Name of the field representing community attribute in IBM Connections.
				 * @return {String/Object} The value of the field from the community ATOM entry document.
				 */
				get : function(fieldName) {
					if (fieldName == "tags") {
						return this._getTags(fieldName);
					}
					if (fieldName == "published" || fieldName == "updated") {
                        return this._getDate(fieldName);
                    }
					return this.fields[fieldName] || this.xpath(this.fieldXPathForEntry(fieldName));
				},
				
				/**
				 * Sets the value of IBM Connections community attribute.
				 * 
				 * @method set
				 * @param {String} fieldName Name of the field representing community attribute in IBM Connections.
				 * @param {String} value Value of the field representing community attribute in IBM Connections.				 
				 */
				
				set : function(fieldName, value) {
					this.fields[fieldName] = value;
				},

				remove : function(fieldName) {
					delete this.fields[fieldName];
				},

				_getTags : function(fieldName) {					
					var nodeArray = this.xpathArray(this.fieldXPathForEntry(fieldName));					
					var tags = [];					
					for(var count = 1;count < nodeArray.length; count ++){
						var node = nodeArray[count];
						var tag = node.text || node.textContent;
						tags.push(tag);
					}
					return tags;
				},
				
                _getDate : function(fieldName) {                    
                    var dateStr = this.fields[fieldName] || this.xpath(this.fieldXPathForEntry(fieldName)) || this.xpath(this.fieldXPathForFeed(fieldName));
                    if (dateStr) {
                        return new Date(dateStr);
                    }
                    return null;
                },
                
				/**
				 * Return the value of IBM Connections community ID from community ATOM entry document.
				 * 
				 * @method getCommunityUuid
				 * @return {String} Community ID of the community
				 */
				
				getCommunityUuid : function() {
					return this.get("communityUuid");
				},
				
				/**
				 * Return the value of IBM Connections community title from community ATOM entry document.
				 * 
				 * @method getTitle
				 * @return {String} Community title of the community
				 */
				
				getTitle : function() {
					return this.get("title");
				},
				
				/**
				 * Return the value of IBM Connections community description summary from community ATOM entry document.
				 * 
				 * @method getSummary
				 * @return {String} Community description summary of the community
				 */
				getSummary : function() {
					return this.get("summary");
				},
				
				/**
				 * Return the value of IBM Connections community description from community ATOM entry document.
				 * 
				 * @method getContent
				 * @return {String} Community description of the community
				 */
				getContent : function() {
					return this.get("content");
				},
				
				/**
				 * Return the value of IBM Connections community URL  from community ATOM entry document.
				 * 
				 * @method getCommunityUrl
				 * @return {String} Community URL of the community
				 */
				getCommunityUrl : function() {
					return this.get("communityUrl");
				},
				
				/**
				 * Return the value of IBM Connections community Logo URL  from community ATOM entry document.
				 * 
				 * @method getLogoUrl
				 * @return {String} Community Logo URL of the community
				 */
				getLogoUrl : function() {
					return this.get("logoUrl");
				},
				
				/**
				 * Return tags of IBM Connections community from community ATOM entry document.
				 * 
				 * @method getTags
				 * @return {Object} Array of tags of the community
				 */
				getTags : function() {
					return this.get("tags");
				},
				
                /**
                 * Return the member count of the IBM Connections community from community ATOM entry document.
                 * 
                 * @method getMemberCount
                 * @return {Number} Member count for the Community
                 */
                getMemberCount : function() {
                    return this.get("memberCount");
                },
                
                /**
                 * Return the community type of the IBM Connections community from community ATOM entry document.
                 * 
                 * @method getCommunityType
                 * @return {String} Type of the Community
                 */
                getCommunityType : function() {
                    var type = this.get("communityType");
                    if (!type) {
                        type = "public";
                    }
                    return type;
                },
                
                /**
                 * Return the published date of the IBM Connections community from community ATOM entry document.
                 * 
                 * @method getPublished
                 * @return {Date} Published date of the Community
                 */
                getPublished : function() {
                    return this.get("published");
                },
                
                /**
                 * Return the last updated date of the IBM Connections community from community ATOM entry document.
                 * 
                 * @method getUpdated
                 * @return {Date} Last updated date of the Community
                 */
                getUpdated : function() {
                    return this.get("updated");
                },                
                /**
				 * Gets an author of IBM Connections community.
				 * 
				 * @method getAuthor
				 * @return {Member} author Author of the community
				 */
				getAuthor : function(){
					if (!this._author){
						this._author = new Member({
						    community: this,
						    id: this.get("authorUid"), 
						    name: this.get("authorName"), 
						    email: this.get("authorEmail")
						});						
					}
                    return this._author;
				},
				/**
				 * Gets a contributor of IBM Connections community.
				 * 
				 * @method getContributor
				 * @return {Member} contributor Contributor of the community
				 */
				getContributor : function() {
                    if (!this._contributor){
                        this._contributor = new Member({
                            community: this,
                            id: this.get("contributorUid"), 
                            name: this.get("contributorName"), 
                            email: this.get("contributorEmail")
                        });                     
                    }
                    return this._contributor;
				},
				/**
                 * Get members of this community.
                 * 
                 * @method getMembers
                 * @param {Object} [args] Argument object
                 * @param {Function} [args.load] This function is invoked when the call to get the members of a community completes. The function expects to receive one
                 *            parameter, the members object - an array of members of the community.
                 * @param {Function} [args.error] Sometimes the getMembers call fails with bad request such as 400 or server errors such as 500. The error
                 *            parameter is another callback function that is only invoked when an error occurs. This allows to control what happens when
                 *            an error occurs without having to put a lot of logic into your load function to check for error conditions. The parameter
                 *            passed to the error function is a JavaScript Error object indicating what the failure was. From the error object. one can
                 *            get access to the javascript library error object, the status code and the error message.
                 * @param {Function} [args.handle] This callback is called regardless of whether the call to get the members of the community completes or fails. The parameter
                 *            passed to this callback is the members object (or error object). From the error object. one can get access to the
                 *            javascript library error object, the status code and the error message.
                 * @param {Object}  [args.parameters] Object representing various parameters that can be passed to get a feed of members of a community. The
                 *            parameters must be exactly as they are supported by IBM Connections like ps, sortBy etc.
				 */
				getMembers : function(args) {
				    this._service.getMembers(this, args);
				},
				/**
				 * Sets title of IBM Connections community.
				 * 
				 * @method setTitle
				 * @param {String} title Title of the community
				 */
				setTitle : function(title) {
					this.set("title", title);
				},
				
				/**
				 * Sets description of IBM Connections community.
				 * 
				 * @method setContent
				 * @param {String} content Description of the community
				 */
				setContent : function(content) {
					this.set("content", content);
				},
				
                /**
                 * Set new tags to be associated with this IBM Connections community.
                 * 
                 * @method setTags
                 * @param {Object} Array of tags to be added to the community
                 */
                
                setTags : function(tags) {
                    if (lang.isArray(tags)) {
                        this.set("tags", tags);
                    } else if (lang.isString(tags)) {
                        this.set("tags", tags.split(/[ ,]+/));
                    } else {
                        throw new Error("Invalid argument for tags: "+tags);
                    }
                },
                
				/**
				 * Set new tags to be added to IBM Connections community.
				 * 
				 * @method setAddedTags
				 * @param {Object} Array of tags to be added to the community
				 */
				
				setAddedTags : function(addedTags) {
					this.set("addedTags", addedTags);
				},
				
				/**
				 * Set tags to be deleted from IBM Connections community.
				 * 
				 * @method setDeletedTags
				 * @param {Object} Array of tags to be deleted from the community
				 */
				setDeletedTags : function(deletedTags) {
					this.set("deletedTags", deletedTags);
				},
				
                /**
                 * Set the community type of the IBM Connections community.
                 * 
                 * @method setCommunityType
                 * @param {String} Type of the Community
                 */
                setCommunityType : function(communityType) {
                    this.set("communityType", communityType);
                },
                
                /**
                 * Sets the author of this IBM Connections community.
                 * 
                 * @method setAuthor
                 */
                setAuthor : function(member) {
                	if(member.declaredClass){
						if (!(validate._validateInputTypeAndNotify("CommunityService", "setContributor", "Member", member, "sbt.connections.Member"))) {
							return ;
						}
						this._author = member;
					}else{
						if (!(validate._validateInputTypeAndNotify("CommunityService", "setContributor", "Member", member, "object"))) {
							return ;
						}
						var _args = lang.mixin({ community: this }, member);
	                    this._author = new Member(_args);
					}
                },
                /**
                 * Sets the contributor of this IBM Connections community.
                 * 
                 * @method setContributor
                 */
                setContributor : function(member) {
                	if(member.declaredClass){
						if (!(validate._validateInputTypeAndNotify("CommunityService", "setContributor", "Member", member, "sbt.connections.Member"))) {
							return ;
						}
						this._contributor = member;
					}else{
						if (!(validate._validateInputTypeAndNotify("CommunityService", "setContributor", "Member", member, "object"))) {
							return ;
						}
						var _args = lang.mixin({ community: this }, member);
	                    this._contributor = new Member(_args);
					}
                    
                },

                /**
                 * Add member to a community
                 * 
                 * @method addMember
                 * @param {Object} [args] Argument object
                 * @param {Object} [args.member] Object representing the member to be added
                 * @param {String} [args.email] Object representing the email of the memeber to be added
                 * @param {String} [args.id] String representing the id of the member to be added
                 * @param {Function} [args.load] This function is invoked when the member is added to the community. The function expects to receive one
                 *            parameter, the loaded member object.
                 * @param {Function} [args.error] Sometimes the addMember call fails with bad request such as 400 or server errors such as 500. The error
                 *            parameter is another callback function that is only invoked when an error occurs. This allows to control what happens when
                 *            an error occurs without having to put a lot of logic into your load function to check for error conditions. The parameter
                 *            passed to the error function is a JavaScript Error object indicating what the failure was. From the error object. one can
                 *            get access to the javascript library error object, the status code and the error message.
                 * @param {Function} [args.handle] This callback is called regardless of whether the call to add member to the community completes or fails. The parameter
                 *            passed to this callback is the member object (or error object). From the error object. one can get access to the
                 *            javascript library error object, the status code and the error message.
                 */
				addMember : function(member, args) {				   
				    this._service.addMember(this, member, args);
				},
				
                /**
                 * Remove member of a community
                 * 
                 * @method removeMember
                 * @param {String/Object} member id of the member or member object (of the member to be removed)
                 * @param {Object} [args] Argument object
                 * @param {Function} [args.load] This function is invoked when the member is removed from the community. 
                 * @param {Function} [args.error] Sometimes the removeMember call fails with bad request such as 400 or server errors such as 500. The error
                 *            parameter is another callback function that is only invoked when an error occurs. This allows to control what happens when
                 *            an error occurs without having to put a lot of logic into your load function to check for error conditions. The parameter
                 *            passed to the error function is a JavaScript Error object indicating what the failure was. From the error object. one can
                 *            get access to the javascript library error object, the status code and the error message.
                 * @param {Function} [args.handle] This callback is called regardless of whether the call to remove a member from the community completes or fails. The parameter
                 *            passed to this callback is the error object (in case of error). From the error object. one can get access to the
                 *            javascript library error object, the status code and the error message.
                 */
                removeMember : function(member, args) {
                    this._service.removeMember(this, member, args);
                },
				
                /**
                 * Remove this community
                 * 
                 * @method remove
                 * @param {Object} [args] Argument object
                 * @param {Function} [args.load] This function is invoked when the community is deleted from the server.
                 * @param {Function} [args.error] Sometimes the deleteCommunity call fails with bad request such as 400 or server errors such as 500. The error
                 *            parameter is another callback function that is only invoked when an error occurs. This allows to control what happens when
                 *            an error occurs without having to put a lot of logic into your load function to check for error conditions. The parameter
                 *            passed to the error function is a JavaScript Error object indicating what the failure was. From the error object. one can
                 *            get access to the javascript library error object, the status code and the error message.
                 * @param {Function} [args.handle] This callback is called regardless of whether the call to delete the community completes or fails. The parameter
                 *            passed to this callback is the error object (in case of an error). From the error object. one can get access to the
                 *            javascript library error object, the status code and the error message.
                 */
                remove : function(args) {
                    this._service.deleteCommunity(this, args);
                },

				_validate : function(className, methodName, args, validateMap) {
					
					if (validateMap.isValidateType && !(validate._validateInputTypeAndNotify(className, methodName, "Community", this, "sbt.connections.Community", args))) {
						return false;
					}
					if (validateMap.isValidateId && !(validate._validateInputTypeAndNotify(className, methodName, "Community Id", this._id, "string", args))) {
						return false;
					}			
					return true;
				}

			});

			/**
			 * Member class associated with a member of a community.
			 * 
			 * @class Member
			 * @namespace connections
			 */
			var Member = declare(BaseEntity, {
				_community : null,
				_userid : null,
				_name: null,
				_email: null,
				data: null,
				memberFields: {},

				constructor : function(args) {
					
					
				    if (args.member) {
                        this._community = args.member.community;
                        this._name = args.member.name;
                        if(args.member.id){
	                        if(this._isEmail(args.member.id)){
	                        	this._email = args.member.id;
	                        }else{
	                        	if (args.member.id.indexOf("urn:lsid:lconn.ibm.com:profiles.person:") != -1) {
	                                this._userid = args.member.id.substring("urn:lsid:lconn.ibm.com:profiles.person:".length);
	                            } else {
	                                this._userid = args.member.id;
	                            }
	                        }
                        }
                        if(args.member.email){
                        	this._email = args.member.email;
                        }
                        if(args.member.userid){
                        	this._userid = args.member.userid;
                        }
				    } else {
    					this._community = args.community;
                        this._name = args.name || args.displayName || null;
                        if (args.id) {
                            if (this._isEmail(args.id)) {
                                this._email = args.id;
                            } else {
                                if (args.id.indexOf("urn:lsid:lconn.ibm.com:profiles.person:") != -1) {
                                    this._userid = args.id.substring("urn:lsid:lconn.ibm.com:profiles.person:".length);
                                } else {
                                    this._userid = args.id;
                                }
                            }
                        }
                        if (args.emails) {
                            this._email = args.emails[0].value;
                        } 
                        if(args.email){
                            this._email = args.email || null;
                        }
                        if(args.userid){
                            this._userid = args.userid || null;
                        }
				    }
				    var _args = { entityName : "member", Constants: communityConstants, con: con, dataHandler: memberHandler};
					this.inherited(arguments, [null, args.id, _args]);
				}
				,
				/**
				 * Loads the member object with the atom entry associated with the member of the community. By default, a network call is made to load the atom entry
				 * document in the member object.
				 * 
				 * @method load
				 * @param {Object} [args] Argument object
				 * @param {Boolean} [args.loadIt=true] Loads the member object with atom entry document of the member of the community. To instantiate an empty member object
				 *            associated with a community (with no atom entry document), the load method must be called with this parameter set to false. By
				 *            default, this parameter is true.
				 * @param {Function} [args.load] The function member.load invokes when the member is loaded from the server. The function expects to receive one
				 *            parameter, the loaded member object.
				 * @param {Function} [args.error] Sometimes the load calls fail. Often these are 404 errors due to bad request or server errors such as 500. The error parameter is
				 *            another callback function that is only invoked when an error occurs. This allows to control what happens when an error occurs
				 *            without having to put a lot of logic into your load function to check for error conditions. The parameter passed to the error
				 *            function is a JavaScript Error object indicating what the failure was. From the error object. one can get access to the javascript
				 *            library error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback is called regardless of whether the call to load the community completes or fails. The parameter
				 *            passed to this callback is the member object in case of successful network call (or error object in case of error from the network call). From the error object. one can get access to the javascript
				 *            library error object, the status code and the error message.
				 */
				load : function(args) {					
					this.data = this._service._loadMember(this, args);					
				},
				
				
                /**
                 * Return the community id of the community associated with this member.
                 * 
                 * @method getCommunity
                 * @return {String} Community Id of the member
                 */
                
                getCommunity : function() {
                    return this._community;
                },  
                
				/**
				 * Return the value of community member name.
				 * 
				 * @method getName
				 * @return {String} Community member name
				 */
				
				getName : function() {
					if(!this._name){
						this._name = this.get("name");
					}
					return this._name;
				},	
				
				/**
				 * Return the value of community member email.
				 * 
				 * @method getName
				 * @return {String} Community member name
				 */
				
				getEmail : function() {
					if(!this._email){
						this._email = this.get("email");
					}
					return this._email;					
				},	
				
				/**
				 * Return the value of community member role from community member ATOM entry document.
				 * 
				 * @method getRole
				 * @return {String} Community member role
				 */
				getRole : function() {
					return this.get("role");
				},
				/**
				 * Return the value of community member userId from community member ATOM entry document.
				 * 
				 * @method getId
				 * @return {String} Community member userId
				 */
				getId : function(){
					if(!this._userid){
						this._userid = this.get("uid");
					}
					return this._userid;					
				},
				
				/**
				 * Sets role of a community member
				 * 
				 * @method setRole
				 * @param {String} role Role of the community member.
				 */
				setRole : function(role) {
					this.set("role", role);
				},
				
				toXml : function() {
				  return "<name>" + this.getName() + 
				      "</name><email>" + this.getEmail() +
				      "</email><snx:userid>" + this.getId() +
				      "</snx:userid><snx:userState>active</snx:userState>";  
				},
				
                _isEmail : function(id) {
                    return id && id.indexOf('@') >= 0;
                },
				
				_validate : function(className, methodName, args, validateMap) {
					
					if (validateMap.isValidateType && !(validate._validateInputTypeAndNotify(className, methodName, "Member", this, "sbt.connections.Member", args))) {
						return false;
					}
					if (validateMap.isValidateId && !(validate._validateInputTypeAndNotify(className, methodName, "Member Id or EMail", this._userid || this._email, "string", args))) {
						return false;
					}			
					return true;
				},
				
			});
			/**
			 * Community service class associated with the community service of IBM Connections.
			 * 
			 * @class CommunityService
			 * @namespace connections
			 * @constructor
			 * @param {Object} options Options object
			 * @param {String} [options.endpoint=connections] Endpoint to be used by CommunityService.
			 */
			var CommunityService = declare(
					"sbt.connections.CommunityService",
					sbt.base.BaseService ,
					{
						_endpoint : null,
						community : null,
						constructor : function(_options) {
							var options = _options || {};							
							options = lang.mixin({endpoint: options.endpoint || "connections", Constants: communityConstants, con: con});
							this.inherited(arguments, [options]);							
						},
						
						/**
						 * Get member entry document of a member of a community
						 * 
						 * @method getMember
						 * @param {Object} args Argument object
						 * @param {String} args.id Id of the member. This can be userId or email of the member.
						 * @param {String/Object} args.community This can be either a string representing community id or a community object with its id initialized.
						 * @param {Boolean} [args.loadIt=true] Loads the members object with member entry document. If an empty member object associated with a
						 *            community (with no member entry document) is needed, then the load method must be called with this parameter set to false.
						 *            By default, this parameter is true.
						 * @param {Function} [args.load] This function is invoked when the member is loaded from the server. The function expects to receive one
						 *            parameter, the loaded member object.
						 * @param {Function} [args.error] Sometimes the getMember call fails with bad request such as 400 or server errors such as 500. The error
						 *            parameter is another callback function that is only invoked when an error occurs. This allows to control what happens when
						 *            an error occurs without having to put a lot of logic into your load function to check for error conditions. The parameter
						 *            passed to the error function is a JavaScript Error object indicating what the failure was. From the error object. one can
						 *            get access to the javascript library error object, the status code and the error message.
						 * @param {Function} [args.handle] This callback is called regardless of whether the call to get the member completes or fails. The parameter
						 *            passed to this callback is the member object (or error object). From the error object. one can get access to the
						 *            javascript library error object, the status code and the error message.
						 */
						getMember : function(args) {
							return this._getOneMember(args);
						},

						/**
						 * Get community object associated with a community
						 * 
						 * @method getCommunity
						 * @param {Object} args Argument object
						 * @param {String} args.id id of the community
						 * @param {Boolean} [args.loadIt=true] Loads the community object with community entry document. If an empty community object 
						 * 		(with no community entry document) is needed, then the load method must be called with this parameter set to false.
						 *      By default, this parameter is true.
						 * @param {Function} [args.load] This function is invoked when the community is loaded from the server. The function expects to receive one
						 *            parameter, the loaded community object.
						 * @param {Function} [args.error] Sometimes the getCommunity call fails with bad request such as 400 or server errors such as 500. The error
						 *            parameter is another callback function that is only invoked when an error occurs. This allows to control what happens when
						 *            an error occurs without having to put a lot of logic into your load function to check for error conditions. The parameter
						 *            passed to the error function is a JavaScript Error object indicating what the failure was. From the error object. one can
						 *            get access to the javascript library error object, the status code and the error message.
						 * @param {Function} [args.handle] This callback is called regardless of whether the call to get the community completes or fails. The parameter
						 *            passed to this callback is the community object (or error object). From the error object. one can get access to the
						 *            javascript library error object, the status code and the error message.
						 */
						getCommunity : function(args) {
							// return lang.isArray(id) ? this._getMultiple(id,
							// cb,options) : this._getOne(id,load, cb,options);
							return this._getOneCommunity(args);
						},

						_getOneCommunity : function(args) {
							if(args){
								if (!(validate._validateInputTypeAndNotify("CommunityService", "getCommunity", "args", args, "object", args))) {
									return ;
								}
							}
							return this._getOne(args,
									{entityName: "community", serviceEntity: "community", entityType: "instance",
										entity: Community,
										urlParams : { communityUuid : args.id },
										headers : {"Content-Type" : "application/atom+xml"}
									});						
						},

						_getOneMember : function(args) {
							if (!(validate._validateInputTypeAndNotify("CommunityService", "getMember", "args", args, "object", args))) {
								return ;
							}
							if (!(validate._validateInputTypeAndNotify("CommunityService", "getMember", "args.userid/args.email/args.id", args.id || args.email || args.userid, "string", args))) {
								return ;
							}
							
							var communityId;							
							if(this._checkCommunityPresence(args, args)){
								if (!(typeof args.community == "object")) {
									communityId = args.community ;								
								} else {
									if (!(validate._validateInputTypeAndNotify("CommunityService", "getMember", "args.community", args.community, "sbt.connections.Community", args))) {
										return ;
									}
									if (!(validate._validateInputTypeAndNotify("CommunityService", "getMember", "args.community.id", args.community._id, "string", args))) {
										return ;
									}
									communityId = args.community._id;
								}	
							}
							var content = {
									communityUuid : communityId
								};
							var _args = lang.mixin({},args); 
							if(_args.userId){
								_args.id = args.userId;								
								content.userid = _args.id;
							}else if(_args.email){
								_args.id = args.email;								
								content.email = _args.id;
							}else if(args.id){							
								content.userid = _args.id;
							}
							
							return this._getOne(_args,
									{entityName: "member", serviceEntity: "community", entityType: "members",
										entity: Member,
										urlParams : content
									});
						},

						/*
						 * _getMultiple: function(ids,cb,options) { // For now. Should later use a single call for multiple entries var a = []; for(var i=0; i<ids.length;
						 * i++) { a[i] = this._getOne(ids[i],cb,options); } return a; },
						 */
					
						_constructAddMemberRequestBody : function(member) {
							var body = "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\" xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">";
							body += "<contributor>";
							if (member._userid) {
								body += "<snx:userid xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">" + xml.encodeXmlEntry(member._userid) + "</snx:userid>";
							} else if (member._email) {
							    body += "<email>" + xml.encodeXmlEntry(member._email) + "</email>";
							}
							body += "</contributor>";
							if (member.memberFields.role) {
								body += "<snx:role xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" component=\"http://www.ibm.com/xmlns/prod/sn/communities\">"
										+ xml.encodeXmlEntry(member.memberFields.role) + "</snx:role>";
							}
							body += "</entry>";
							return body;
						},

						_constructCommunityRequestBody : function(community) {
							var body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">";

							// community title and content are mandatory fields in the request body
							if (!community.fields.title) {
								if (community.getTitle()) {
									body += "<title type=\"text\">" + xml.encodeXmlEntry(community.getTitle()) + "</title>";
								}
							}
							if (!community.fields.content) {
								if (community.getContent()) {
									body += "<content type=\"html\">" + xml.encodeXmlEntry(community.getContent()) + "</content>";
								}else{
									body += "<content type=\"html\"></content>";
								}
							}
                            if (!community.fields.communityType) {
                                body += "<snx:communityType xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">" + community.getCommunityType() + "</snx:communityType>";
                            }

                            // TODO is this needed?
                            //if (community.getCommunityUuid() == null) {
                            //    body += "<author>" + community.getAuthor().toXml() + "</author>";
                            //    body += "<contributor>" + community.getContributor().toXml() + "</contributor>";;
							//}
							
                            for (key in community.fields) {
							    var value = xml.encodeXmlEntry(community.fields[key]);
								if (key == "title") {
									body += "<title type=\"text\">" + value + "</title>";
								}
								else if (key == "content") {
									body += "<content type=\"html\">" + value + "</content>";
								}
                                else if (key == "communityType") {
                                    body += "<snx:communityType xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">" + value + "</snx:communityType>";
                                }
                                else if (key == "tags") {
                                    var tags = community.fields.tags;
                                    for (var i=0; i<tags.length; i++) {
                                        body += "<category term=\"" + xml.encodeXmlEntry(tags[i]) + "\"/>";
                                    }
                                }
								else if (key == "addedTags") {
									var _concatTags = community.getTags();
									var _addedTags = this._getUniqueElements(community.fields.addedTags);
									for ( var count = 0; count < _addedTags.length; count++) {
										if (_concatTags.indexOf(_addedTags[count]) == -1) {
											_concatTags.push(_addedTags[count]);
										}
									}
									while (_concatTags.length > 0) {
										body += "<category term=\"" + xml.encodeXmlEntry(_concatTags.shift()) + "\"/>";
									}
								}
								else if (key == "deletedTags") {
									var _originalTags = community.getTags();
									for ( var len = 0; len < community.fields.deletedTags.length; len++) {
										if (_originalTags.indexOf(community.fields.deletedTags[len]) != -1) {
											_originalTags.splice(_originalTags.indexOf(community.fields.deletedTags[len]), 1);
										}
									}
									while (_originalTags.length > 0) {
										body += "<category term=\"" + xml.encodeXmlEntry(_originalTags.shift()) + "\"/>";
									}
								}
							}
							body += "<category term=\"community\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category></entry>";
							return body;
						},					

						/**
						 * Create a new community
						 * 
						 * @method createCommunity
						 * @param {Object} community Community object which denotes the community to be created.
						 * @param {Object} [args] Argument object
						 * @param {Boolean} [args.loadIt=true] Loads the community object with community entry document. If an empty community object 
						 * 		(with no community entry document) is needed, then the load method must be called with this parameter set to false.
						 *      By default, this parameter is true.
						 * @param {Function} [args.load] This function is invoked when the community is created. The function expects to receive one
						 *            parameter, the loaded community object.
						 * @param {Function} [args.error] Sometimes the createCommunity call fails with bad request such as 400 or server errors such as 500. The error
						 *            parameter is another callback function that is only invoked when an error occurs. This allows to control what happens when
						 *            an error occurs without having to put a lot of logic into your load function to check for error conditions. The parameter
						 *            passed to the error function is a JavaScript Error object indicating what the failure was. From the error object. one can
						 *            get access to the javascript library error object, the status code and the error message.
						 * @param {Function} [args.handle] This callback is called regardless of whether the call to create the community completes or fails. The parameter
						 *            passed to this callback is the community object (or error object). From the error object. one can get access to the
						 *            javascript library error object, the status code and the error message.
						 */
						createCommunity : function(communityArgs, args) {
							var community = null;
							if(!communityArgs.declaredClass){
								if (!(validate._validateInputTypeAndNotify("CommunityService", "createCommunity", "Community", communityArgs, "object", args))) {
									return ;
								}
							community = new Community(this);
							community.fields = communityArgs;
							}
								else {
									if (!(validate._validateInputTypeAndNotify("CommunityService", "createCommunity", "Community", communityArgs, "sbt.connections.Community", args))) {
										return ;
									}
									community = communityArgs;
							}
							var headers = {
								"Content-Type" : "application/atom+xml"
							};
							var _self = this;						
							var headers = {
									"Content-Type" : "application/atom+xml"
								};
							this._createEntity(args,
									{entityName: "community", serviceEntity: "communities", entityType: "my",
										methodName : "createCommunity",
										entity: community,
										headers: headers,
										xmlPayload : _self._constructCommunityRequestBody(community)										
									});
						},
						
						_createEntityOnLoad : function (data, ioArgs, entity, args, postArgs){
							if(postArgs.methodName == "createCommunity"){
								var newCommunityUrl = ioArgs.headers["Location"];
								var communityId = newCommunityUrl.substring(newCommunityUrl.indexOf("communityUuid=") + "communityUuid=".length);
								entity._id = communityId;
								entity._fields = {};
								if (args && args.loadIt != false) {									
									this._load(entity, args,{entityName: "community", serviceEntity: "community", entityType: "instance",
										entity: entity, urlParams : { communityUuid : entity._id },
										headers : {"Content-Type" : "application/atom+xml"}
									});
								}else{
									this._notifyResponse(args,entity);
								}
							}else if(postArgs.methodName == "addMember"){
								var content = {communityUuid : postArgs.urlParams.communityUuid};
								if(this._isEmail(entity._id)){																
									content.email = entity._id;
								}else{																
									content.userid = entity._id;
								}								
								this._load(entity, args,{entityName: "member", serviceEntity: "community", entityType: "members",
									entity: entity, urlParams : content,
									headers : {"Content-Type" : "application/atom+xml"}
								});
							}							
							else{
								this.inherited(arguments, [data, ioArgs, entity, args, postArgs]);
							}
						},
						/**
						 * Update an existing community
						 * 
						 * @method updateCommunity
						 * @param {Object} community Community object
						 * @param {Object} [args] Argument object
						 * @param {Function} [args.load] This function is invoked when the community is updated. The function expects to receive one
						 *            parameter, the loaded community object.
						 * @param {Function} [args.error] Sometimes the updateCommunity call fails with bad request such as 400 or server errors such as 500. The error
						 *            parameter is another callback function that is only invoked when an error occurs. This allows to control what happens when
						 *            an error occurs without having to put a lot of logic into your load function to check for error conditions. The parameter
						 *            passed to the error function is a JavaScript Error object indicating what the failure was. From the error object. one can
						 *            get access to the javascript library error object, the status code and the error message.
						 * @param {Function} [args.handle] This callback is called regardless of whether the call to update the community completes or fails. The parameter
						 *            passed to this callback is the community object (or error object). From the error object. one can get access to the
						 *            javascript library error object, the status code and the error message.
						 */
						updateCommunity : function(community, args) {
							if (!(validate._validateInputTypeAndNotify("CommunityService", "updateCommunity", "Community", community, "sbt.connections.Community", args))) {
								return ;
							}
							if (!community._validate("CommunityService", "updateCommunity", args, {
								isValidateId : true
							})) {
								return;
							}							
							var _self = this;
							var headers = {
								"Content-Type" : "application/atom+xml"
							};							
							this._updateEntity(args,
									{entityName: "community", serviceEntity: "community", entityType: "instance",
										entity: community,
										xmlPayload : _self._constructCommunityRequestBody(community),
										headers: headers,
										urlParams : { communityUuid: community._id}
									});
						},

						/**
						 * Delete an existing community
						 * 
						 * @method deleteCommunity
						 * @param {String/Object} community id of the community or the community object (of the community to be deleted)
						 * @param {Object} [args] Argument object
						 * @param {Function} [args.load] This function is invoked when the community is deleted from the server.
						 * @param {Function} [args.error] Sometimes the deleteCommunity call fails with bad request such as 400 or server errors such as 500. The error
						 *            parameter is another callback function that is only invoked when an error occurs. This allows to control what happens when
						 *            an error occurs without having to put a lot of logic into your load function to check for error conditions. The parameter
						 *            passed to the error function is a JavaScript Error object indicating what the failure was. From the error object. one can
						 *            get access to the javascript library error object, the status code and the error message.
						 * @param {Function} [args.handle] This callback is called regardless of whether the call to delete the community completes or fails. The parameter
						 *            passed to this callback is the error object (in case of an error). From the error object. one can get access to the
						 *            javascript library error object, the status code and the error message.
						 */
						deleteCommunity : function(inputCommunity, args) {
							if (!(typeof inputCommunity == "object")) {
								var community = new Community(this, inputCommunity);
								this._deleteCommunity(community, args);
							} else {
								this._deleteCommunity(inputCommunity, args);
							}
						},
						_deleteCommunity : function(community, args) {
							if (!(validate._validateInputTypeAndNotify("CommunityService", "deleteCommunity", "Community", community, "sbt.connections.Community", args))) {
								return ;
							}
							if (!community._validate("CommunityService", "deleteCommunity", args, {
								isValidateId : true
							})) {
								return;
							}
							var headers = {
								"Content-Type" : "application/atom+xml"
							};					
							var params = { communityUuid: community._id };							
							this._deleteEntity(args,{entityName: "community", serviceEntity: "community", entityType: "instance",
								urlParams : params, headers : headers
							});
						},
						/**
						 * Add member to a community
						 * 
						 * @method addMember
						 * @param {String/Object} community id of the community or the community object.
						 * @param {Object} member member object representing the member of the community to be added
						 * @param {Object} [args] Argument object
						 * @param {Function} [args.load] This function is invoked when the member is added to the community. The function expects to receive one
						 *            parameter, the loaded member object.
						 * @param {Function} [args.error] Sometimes the addMember call fails with bad request such as 400 or server errors such as 500. The error
						 *            parameter is another callback function that is only invoked when an error occurs. This allows to control what happens when
						 *            an error occurs without having to put a lot of logic into your load function to check for error conditions. The parameter
						 *            passed to the error function is a JavaScript Error object indicating what the failure was. From the error object. one can
						 *            get access to the javascript library error object, the status code and the error message.
						 * @param {Function} [args.handle] This callback is called regardless of whether the call to add member to the community completes or fails. The parameter
						 *            passed to this callback is the member object (or error object). From the error object. one can get access to the
						 *            javascript library error object, the status code and the error message.
						 */
						addMember : function(inputCommunity, inputMember, args) {
							var community = null;
							var member = inputMember;
							if (!(typeof inputCommunity == "object")) {
								community = new Community(this, inputCommunity);
							} else {
								community = inputCommunity;
							}
							if (!(validate._validateInputTypeAndNotify("CommunityService", "addMember", "Community", community, "sbt.connections.Community", args))) {
								return ;
							}
							if (!community._validate("CommunityService", "addMember", args, {
								isValidateId : true
							})) {
								return;
							}
							if(inputMember.declaredClass){
								if (!(validate._validateInputTypeAndNotify("CommunityService", "addMember", "Member", inputMember, "sbt.connections.Member", args))) {
									return ;
								}
							}else{
								if (!(validate._validateInputTypeAndNotify("CommunityService", "addMember", "Member", inputMember, "object", args))) {
									return ;
								}
								member = new Member(inputMember);
								member.memberFields = inputMember;
							}
							if (!member._validate("CommunityService", "addMember", args, {
								isValidateId : true
							})) {
								return;
							}
							var _self = this;
							var communityId = community._id;
							var headers = {
								"Content-Type" : "application/atom+xml"
							};							
							this._createEntity(args,
									{entityName: "community", serviceEntity: "community", entityType: "members",
										methodName: "addMember",
										entity: member,
										xmlPayload : _self._constructAddMemberRequestBody(member),										
										headers : headers,
										urlParams : { communityUuid: communityId }
									});
						},
						/**
						 * Remove member of a community
						 * 
						 * @method 
						 * @param {String/Object} community id of the community or the community object.
						 * @param {String/Object} member id of the member or member object (of the member to be removed)
						 * @param {Object} [args] Argument object
						 * @param {Function} [args.load] This function is invoked when the member is removed from the community. 
						 * @param {Function} [args.error] Sometimes the removeMember call fails with bad request such as 400 or server errors such as 500. The error
						 *            parameter is another callback function that is only invoked when an error occurs. This allows to control what happens when
						 *            an error occurs without having to put a lot of logic into your load function to check for error conditions. The parameter
						 *            passed to the error function is a JavaScript Error object indicating what the failure was. From the error object. one can
						 *            get access to the javascript library error object, the status code and the error message.
						 * @param {Function} [args.handle] This callback is called regardless of whether the call to remove a member from the community completes or fails. The parameter
						 *            passed to this callback is the error object (in case of error). From the error object. one can get access to the
						 *            javascript library error object, the status code and the error message.
						 */
						removeMember : function(inputCommunity, inputMember, args) {
							var community = null;
							var member = null;
							if (!(typeof inputCommunity == "object")) {
								community = new Community(this, inputCommunity);
							} else {
								community = inputCommunity;
							}
							if (!(typeof inputMember == "object")) {
								member = new Member({ community: community, id: inputMember });
							} else {
								member = inputMember;
							}
							if (!(validate._validateInputTypeAndNotify("CommunityService", "removeMember", "Community", community, "sbt.connections.Community", args))) {
								return ;
							}
							if (!community._validate("CommunityService", "removeMember", args, {
								isValidateId : true
							})) {
								return;
							}
							if (!(validate._validateInputTypeAndNotify("CommunityService", "removeMember", "Member", member, "sbt.connections.Member", args))) {
								return ;
							}
							if (!member._validate("CommunityService", "removeMember", args, {
								isValidateId : true
							})) {
								return;
							}
							var _self = this;
							var headers = {
								"Content-Type" : "application/atom+xml"
							};							
							var params = { communityUuid: community._id };
							if (_self._isEmail(member._id)) {
								lang.mixin(params, { email: member._id });
							} else {
								lang.mixin(params, { userid: member._id });
							}
							this._deleteEntity(args,{entityName: "community", serviceEntity: "community", entityType: "members",
								urlParams : params, headers : headers
							});
						},
						/**
						 * Get public communities from IBM Connections
						 * 
						 * @method getPublicCommunities
						 * @param {Object} [args] Argument object
						 * @param {Function} [args.load] This function is invoked when the call to get public communities completes. The function expects to receive one
						 *            parameter, the communities object - an array of public communities.
						 * @param {Function} [args.error] Sometimes the getPublicCommunities call fails with bad request such as 400 or server errors such as 500. The error
						 *            parameter is another callback function that is only invoked when an error occurs. This allows to control what happens when
						 *            an error occurs without having to put a lot of logic into your load function to check for error conditions. The parameter
						 *            passed to the error function is a JavaScript Error object indicating what the failure was. From the error object. one can
						 *            get access to the javascript library error object, the status code and the error message.
						 * @param {Function} [args.handle] This callback is called regardless of whether the call to get public communities completes or fails. The parameter
						 *            passed to this callback is the communities object (or error object). From the error object. one can get access to the
						 *            javascript library error object, the status code and the error message.
						 * @param {Object} [args.parameters] Object representing various parameters that can be passed to get a feed of public communities. The
						 *            parameters must be exactly as they are supported by IBM Connections like ps, sortBy etc.
						 */
						getPublicCommunities : function(args) {
							this._getEntities(args,
									{entityName: "community", serviceEntity: "communities", entityType: "public",
										parseId: this._parseCommunityId,
										entity: Community,
										headers : {"Content-Type" : "application/atom+xml"},
										dataHandler : communityHandler
									});
						},

						/**
						 * Get my communities from IBM Connections
						 * 
						 * @method getMyCommunities
						 * @param {Object} [args] Argument object
						 * @param {Function} [args.load] This function is invoked when the call to get my communities completes. The function expects to receive one
						 *            parameter, the communities object - an array of my communities.
						 * @param {Function} [args.error] Sometimes the getMyCommunities call fails with bad request such as 400 or server errors such as 500. The error
						 *            parameter is another callback function that is only invoked when an error occurs. This allows to control what happens when
						 *            an error occurs without having to put a lot of logic into your load function to check for error conditions. The parameter
						 *            passed to the error function is a JavaScript Error object indicating what the failure was. From the error object. one can
						 *            get access to the javascript library error object, the status code and the error message.
						 * @param {Function} [args.handle] This callback is called regardless of whether the call to get my communities completes or fails. The parameter
						 *            passed to this callback is the communities object (or error object). From the error object. one can get access to the
						 *            javascript library error object, the status code and the error message.
						 * @param {Object} [args.parameters] Object representing various parameters that can be passed to get a feed of my communities. The
						 *            parameters must be exactly as they are supported by IBM Connections like ps, sortBy etc.
						 */

						getMyCommunities : function(args) {
							this._getEntities(args,
									{entityName: "community", serviceEntity: "communities", entityType: "my",
										parseId: this._parseCommunityId,
										entity: Community,
										headers : {"Content-Type" : "application/atom+xml"},
										dataHandler : communityHandler
									});
						},
						/**
						 * Get members of a community.
						 * 
						 * @method getMembers
						 * @param {Object} [args] Argument object
						 * @param {Function} [args.load] This function is invoked when the call to get the members of a community completes. The function expects to receive one
						 *            parameter, the members object - an array of members of the community.
						 * @param {Function} [args.error] Sometimes the getMembers call fails with bad request such as 400 or server errors such as 500. The error
						 *            parameter is another callback function that is only invoked when an error occurs. This allows to control what happens when
						 *            an error occurs without having to put a lot of logic into your load function to check for error conditions. The parameter
						 *            passed to the error function is a JavaScript Error object indicating what the failure was. From the error object. one can
						 *            get access to the javascript library error object, the status code and the error message.
						 * @param {Function} [args.handle] This callback is called regardless of whether the call to get the members of the community completes or fails. The parameter
						 *            passed to this callback is the members object (or error object). From the error object. one can get access to the
						 *            javascript library error object, the status code and the error message.
						 * @param {Object}  [args.parameters] Object representing various parameters that can be passed to get a feed of members of a community. The
						 *            parameters must be exactly as they are supported by IBM Connections like ps, sortBy etc.
						 */
						getMembers : function(communityArg, args) {
							if (!(typeof communityArg == "object")) {
								var community = new Community(this, communityArg);
								this._getEntities(args,
										{community: community,
											entityName: "member", serviceEntity: "community", entityType: "members",
											entity: Member,
											urlParams : { communityUuid : community._id },
											dataHandler : memberHandler
										});
								
							} else {
								this._getEntities(args,
										{community: communityArg,
											entityName: "member", serviceEntity: "community", entityType: "members",
											entity: Member,
											urlParams : { communityUuid : communityArg._id },
											dataHandler : memberHandler
										});
								
							}

						},						
						_isEmail : function(id) {
							return id && id.indexOf('@') >= 0;
						},
						_getUniqueElements: function(arr){
							var _arr = [];
							for(var count = 0; count < arr.length; count ++){
								if(count == arr.indexOf(arr[count])){
									_arr.push(arr[count]);
								}
							}
							return _arr;
						},
						
						_checkCommunityPresence : function(obj, args){
							if (!(obj.community)) {
								validate.notifyError({
									code : communityConstants.sbtErrorCodes.badRequest,
									message : communityConstants.sbtErrorMessages.null_community
								}, args);
								return false;
							} else {
								return true;
							}
						},
						_createEntityObject : function (service, id, requestArgs, args){
							if(requestArgs.entityName == "community"){
								var community = new Community (service, id);
								return community;
							}else if (requestArgs.entityName == "member"){
								var member = new Member ({id : id, community : requestArgs.urlParams.communityUuid});
								return member;
							}
						}
					});
			return CommunityService;
		});
