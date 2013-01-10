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
define(
		[ 'sbt/_bridge/declare', 'sbt/config', 'sbt/lang', 'sbt/connections/core', 'sbt/xml', 'sbt/util', 'sbt/xpath', 'sbt/Cache', 'sbt/Endpoint',
				'sbt/connections/CommunityConstants','sbt/validate' ],
		function(declare, cfg, lang, con, xml, util, xpath, Cache, Endpoint, Constants, validate) {

			/**
			 * Community class associated with a community.
			 * 
			 * @class Community
			 * @namespace connections
			 */
			var Community = declare("sbt.connections.Community", null, {
				_service : null,
				_id : "",
				data : null,
				fields : {},

				constructor : function(svc, id) {
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
				 * Return the xpath expression for a field in the atom entry document of the community.
				 * 
				 * @method fieldXPathForEntry
				 * @param {String} fieldName Xml element name in atom entry document of the community.
				 * @return {String} xpath for the element in atom entry document of the community.
				 */
				fieldXPathForEntry : function(fieldName) {
					return Constants._xpath[fieldName];
				},
				/**
				 * Return the xpath expression for a field in the atom entry of the community within a feed of communities.
				 * 
				 * @method fieldXPathForFeed
				 * @param {String} fieldName Xml element name in entry of the community.
				 * @return {String} xpath for the element in entry of the community.
				 */
				fieldXPathForFeed : function(fieldName) {
					return Constants._xpath_communities_Feed[fieldName];
				},
				/**
				 * Return the value of a field in the community entry using xpath expression
				 * 
				 * @method xpath
				 * @param {String} path xpath expression
				 * @return {String} value of a field in community entry using the xpath expression
				 */
				xpath : function(path) {
					return this.data && path ? xpath.selectText(this.data, path, con.namespaces) : null;
				},
				/**
				 * Return an array of nodes from a community entry using xpath expression
				 * 
				 * @method xpathArray
				 * @param {String} path xpath expression
				 * @return {Object} an array of nodes from a community entry using xpath expression
				 */
				xpathArray : function(path) {
					return this.data && path ? xpath.selectNodes(this.data, path, con.namespaces) : null;
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
					return this.fields[fieldName] || this.xpath(this.fieldXPathForEntry(fieldName)) || this.xpath(this.fieldXPathForFeed(fieldName));
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
					if (nodeArray.length == 0) {
					    nodeArray = this.xpathArray(this.fieldXPathForFeed(fieldName));
					}
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
                    return this.get("communityType");
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
			var Member = declare("sbt.connections.Member", null, {
				_service : null,
				_id : "",
				data : null,
				memberFields : {},

				constructor : function(svc, id) {
					this._service = svc;
					this._id = id;
				},
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
				 * Return the xpath expression for a field in the atom entry of the community member within a feed of community members.
				 * 
				 * @method fieldXPathForFeed
				 * @param {String} fieldName Field name in entry of the community member.
				 * @return {String} xpath for the element in entry of the community member.
				 */
				fieldXPathForFeed : function(fieldName) {
					return Constants._xpath_member[fieldName];
				},
				
				/**
				 * Return the xpath expression for a field in the atom entry of a community member.
				 * 
				 * @method fieldXPathForEntry
				 * @param {String} fieldName Field name in entry of the community member.
				 * @return {String} xpath for the element in entry of the community member.
				 */
				
				fieldXPathForEntry : function(fieldName) {
					return Constants._xpath_community_Members_Feed[fieldName];
				},
				
				/**
				 * Return the value of a field in the community member entry using xpath expression
				 * 
				 * @method xpath
				 * @param {String} path xpath expression for the field
				 * @return {String} Value of a field in community entry using the xpath expression
				 */
				xpath : function(path) {
					return this.data && path ? xpath.selectText(this.data, path, con.namespaces) : null;
				},
				
				/**
				 * Return the value of IBM Connections member attribute from community member ATOM entry document.
				 * 
				 * @method get
				 * @param {String} fieldName Name of the field representing community member attribute in IBM Connections.
				 * @return {String/Object} The value of the field from the community member ATOM entry document.
				 */
				get : function(fieldName) {
					return this.memberFields[fieldName] || this.xpath(this.fieldXPathForEntry(fieldName)) || this.xpath(this.fieldXPathForFeed(fieldName));
				},
				
				/**
				 * Sets the value of IBM Connections community member attribute.
				 * 
				 * @method set
				 * @param {String} fieldName Name of the field representing community member attribute in IBM Connections.
				 * @param {String} value Value of the field representing community member attribute in IBM Connections.				 
				 */
				set : function(fieldName, value) {
					this.memberFields[fieldName] = value;
				},
				remove : function(fieldName) {
					delete this.memberFields[fieldName];
				},
				
				/**
				 * Return the value of community member name from community member ATOM entry document.
				 * 
				 * @method getName
				 * @return {String} Community member name
				 */
				
				getName : function() {
					return this.get("name");
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
				 * Sets role of a community member
				 * 
				 * @method setRole
				 * @param {String} role Role of the community member.
				 */
				setRole : function(role) {
					this.set("role", role);
				},
				
				_validate : function(className, methodName, args, validateMap) {
					
					if (validateMap.isValidateType && !(validate._validateInputTypeAndNotify(className, methodName, "Member", this, "sbt.connections.Member", args))) {
						return false;
					}
					if (validateMap.isValidateId && !(validate._validateInputTypeAndNotify(className, methodName, "Member Id", this._id, "string", args))) {
						return false;
					}			
					return true;
				}
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
					null,
					{
						_endpoint : null,

						constructor : function(options) {
							options = options || {};
							this._endpoint = Endpoint.find(options.endpoint || 'connections');
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
							// return lang.isArray(id) ? this._getMultiple(id,
							// cb,options) : this._getOne(id,load, cb,options);
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
							return this._getOne(args);
						},

						_getOne : function(args) {
							if(args){
								if (!(validate._validateInputTypeAndNotify("CommunityService", "getCommunity", "args", args, "object", args))) {
									return ;
								}
							}
							var community = null;
							if(args && args.id){
								community = new Community(this, args.id);
							}else{
								community = new Community(this);
							}
							if (args && (args.loadIt == false)) {
								if (args.load) {
									args.load(community);
								}
								if (args.handle) {
									args.handle(community);
								}
							} else {
								this._load(community, args);
							}
							return community;
						},

						_getOneMember : function(args) {
							if (!(validate._validateInputTypeAndNotify("CommunityService", "getMember", "args", args, "object", args))) {
								return ;
							}
							var member = new Member(this, args.id);
							if (args.loadIt == false) {
								if (args.load) {
									args.load(member);
								}
								if (args.handle) {
									args.handle(member);
								}
							} else {
								this._loadMember(member, args);
							}
							return member;
						},

						/*
						 * _getMultiple: function(ids,cb,options) { // For now. Should later use a single call for multiple entries var a = []; for(var i=0; i<ids.length;
						 * i++) { a[i] = this._getOne(ids[i],cb,options); } return a; },
						 */

						_load : function(community, args) {
							if (!community._validate("Community", "load", args, {
								isValidateType : true,
								isValidateId : true
							})) {
								return;
							}
							var _self = this;
							var content = {};
							content.communityUuid = community._id;
							this._endpoint.xhrGet({
								serviceUrl : this._constructCommunityUrl(Constants._methodName.getCommunity),
								handleAs : "text",
								content : content,
								load : function(data) {
									community.data = xml.parse(data);
									if (args.load) {
										args.load(community);
									}
									if (args.handle) {
										args.handle(community);
									}
								},
								error : function(error) {
									validate.notifyError(error, args);

								}
							});

						},

						_loadMember : function(member, args) {
							if (!member._validate("Member", "load", args, {
								isValidateType : true,
								isValidateId : true
							})) {
								return;
							}
							var communityId;
							if(!(this._checkCommunityPresence(args, args))){
								return;
							}
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
							var _self = this;
							var content = {
								communityUuid : communityId
							};
							if (this._isEmail(member._id)) {
								content.email = member._id;
							} else {
								content.userid = member._id;
							}
							this._endpoint.xhrGet({
								serviceUrl : this._constructCommunityUrl(Constants._methodName.getMember),
								handleAs : "text",
								content : content,
								load : function(data) {
									member.data = xml.parse(data);
									if (args.load) {
										args.load(member);
									}
									if (args.handle) {
										args.handle(member);
									}
								},
								error : function(error) {
									validate.notifyError(error, args);

								}
							});
						},

						_constructAddMemberRequestBody : function(member) {
							var body = "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\" xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">";
							body += "<contributor>";
							if (member._id) {
								if (this._isEmail(member._id)) {
									body += "<email>" + xml.encodeXmlEntry(member._id) + "</email>";
								} else {
									body += "<snx:userid xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">" + xml.encodeXmlEntry(member._id) + "</snx:userid>";
								}
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
							// community title and content are mandatory fields
							// in the request body
							for (key in community.fields) {
								if (key == "title") {
									body += "<title type=\"text\">" + xml.encodeXmlEntry(community.fields.title) + "</title>";
								}
								if (key == "content") {
									body += "<content type=\"html\">" + xml.encodeXmlEntry(community.fields.content) + "</content>";
								}
								if (key == "addedTags") {
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
								if (key == "deletedTags") {
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
							body += "<category term=\"community\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category><snx:communityType xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">public</snx:communityType></entry>";
							return body;
						},

						_constructCommunityUrl : function(methodName) {
							var authType = "";
							if (this._endpoint.authType == "basic") {
								authType = "";
							} else if (this._endpoint.authType == "oauth") {
								authType = "/oauth";
							} else {
								authType = "";
							}
							if (methodName == Constants._methodName.createCommunity) {
								return con.communitiesUrls["communitiesServiceBaseUrl"] + authType + con.communitiesUrls["createCommunity"];
							}
							if (methodName == Constants._methodName.updateCommunity) {
								return con.communitiesUrls["communitiesServiceBaseUrl"] + authType + con.communitiesUrls["updateCommunity"];
							}
							if (methodName == Constants._methodName.deleteCommunity) {
								return con.communitiesUrls["communitiesServiceBaseUrl"] + authType + con.communitiesUrls["deleteCommunity"];
							}
							if (methodName == Constants._methodName.addMember) {
								return con.communitiesUrls["communitiesServiceBaseUrl"] + authType + con.communitiesUrls["addCommunityMember"];
							}
							if (methodName == Constants._methodName.removeMember) {
								return con.communitiesUrls["communitiesServiceBaseUrl"] + authType + con.communitiesUrls["removeCommunityMember"];
							}
							if (methodName == Constants._methodName.getCommunity) {
								return con.communitiesUrls["communitiesServiceBaseUrl"] + authType + con.communitiesUrls["getCommunity"];
							}
							if (methodName == Constants._methodName.getMember) {
								return con.communitiesUrls["communitiesServiceBaseUrl"] + authType + con.communitiesUrls["getCommunityMember"];
							}

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
						createCommunity : function(community, args) {
							if (!(validate._validateInputTypeAndNotify("CommunityService", "createCommunity", "Community", community, "sbt.connections.Community", args))) {
								return ;
							}
							var headers = {
								"Content-Type" : "application/atom+xml"
							};
							var _self = this;
							this._endpoint.xhrPost({
								serviceUrl : this._constructCommunityUrl(Constants._methodName.createCommunity),
								postData : this._constructCommunityRequestBody(community),
								headers : headers,
								load : function(data, ioArgs) {
									var newCommunityUrl = ioArgs.xhr.getResponseHeader("Location");
									var communityId = newCommunityUrl.substring(newCommunityUrl.indexOf("communityUuid=") + "communityUuid=".length);
									community._id = communityId;
									community.fields = {};
									if (args && args.loadIt != false) {									
										_self._load(community, args);
									} else {
										if (args.load)
											args.load(community);
										if (args.handle)
											args.handle(community);
									}
								},
								error : function(error) {
									validate.notifyError(error, args);
								}
							});
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
							var _id = community._id;
							var _self = this;
							var headers = {
								"Content-Type" : "application/atom+xml"
							};
							this._endpoint.xhrPut({
								serviceUrl : this._constructCommunityUrl(Constants._methodName.updateCommunity) + "?communityUuid=" + encodeURIComponent(_id),
								putData : this._constructCommunityRequestBody(community),
								headers : headers,
								load : function(data) {
									community.data = xml.parse(data);
									if (args.load)
										args.load(community);
									if (args.handle)
										args.handle(community);
								},
								error : function(error, ioargs) {
									validate.notifyError(error, args);
								}
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
							var _self = this;
							var _id = community._id;
							this._endpoint.xhrDelete({
								serviceUrl : this._constructCommunityUrl(Constants._methodName.deleteCommunity) + "?communityUuid=" + encodeURIComponent(_id),
								headers : headers,
								load : function(data) {
									if (args.load)
										args.load();
									if (args.handle)
										args.handle();
								},
								error : function(error, ioargs) {
									validate.notifyError(error, args);
								}
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
							if (!(validate._validateInputTypeAndNotify("CommunityService", "addMember", "Member", inputMember, "sbt.connections.Member", args))) {
								return ;
							}
							if (!inputMember._validate("CommunityService", "addMember", args, {
								isValidateId : true
							})) {
								return;
							}
							var _self = this;
							var communityId = community._id;
							var headers = {
								"Content-Type" : "application/atom+xml"
							};
							this._endpoint
									.xhrPost({
										serviceUrl : this._constructCommunityUrl(Constants._methodName.addMember) + "?communityUuid="
												+ encodeURIComponent(communityId),
										postData : this._constructAddMemberRequestBody(member),
										headers : headers,
										load : function(data) {
											var _args = lang.mixin({
												"community" : community
											}, args);
											_self._loadMember(inputMember, _args);
										},
										error : function(error) {
											validate.notifyError(error, args);
										}
									});
						},
						/**
						 * Remove member of a community
						 * 
						 * @method removeMember
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
								member = new Member(this, inputMember);
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
							var _param = _self._isEmail(member._id) ? "&email=" : "&userid=";
							this._endpoint.xhrDelete({
								serviceUrl : this._constructCommunityUrl(Constants._methodName.removeMember) + "?communityUuid="
										+ encodeURIComponent(community._id) + _param + encodeURIComponent(member._id),
								headers : headers,
								load : function(data) {
									if (args.load)
										args.load();
									if (args.handle)
										args.handle();
								},
								error : function(error) {
									validate.notifyError(error, args);
								}
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
							this._getEntities(args, {
								communityServiceEntity : "communities",
								communitiesType : "public",
								xpath : Constants._xpath_communities_Feed
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
							this._getEntities(args, {
								communityServiceEntity : "communities",
								communitiesType : "my",
								xpath : Constants._xpath_communities_Feed
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
								this._getEntities(args, {
									community : community,
									communityServiceEntity : "community",
									communitiesType : "members",
									xpath : Constants._xpath_community_Members_Feed
								});
							} else {
								this._getEntities(args, {
									community : communityArg,
									communityServiceEntity : "community",
									communitiesType : "members",
									xpath : Constants._xpath_community_Members_Feed
								});
							}

						},
						_constructServiceUrl : function(getArgs) {
							var authType = "";
							if (this._endpoint.authType == "basic") {
								authType = "";
							} else if (this._endpoint.authType == "oauth") {
								authType = "/oauth";
							} else {
								authType = "";
							}
							return con.communitiesUrls["communitiesServiceBaseUrl"] + authType
									+ Constants.communityServiceEntity[getArgs.communityServiceEntity] + Constants.communitiesType[getArgs.communitiesType];
						},
						_constructQueryObj : function(args, getArgs) {
							var params = args.parameters;
							if (getArgs.communitiesType == "members") {
								params = lang.mixin(params, {
									"communityUuid" : getArgs.community._id
								});
							}
							return params;
						},
						_getEntities : function(args, getArgs) {
							var _self = this;
							var headers = {
								"Content-Type" : "application/atom+xml"
							};
							this._endpoint.xhrGet({
								serviceUrl : this._constructServiceUrl(getArgs),
								headers : headers,
								content : this._constructQueryObj(args, getArgs),
								load : function(data) {
									var entities = [];
									var entry = xpath.selectNodes(xml.parse(data), getArgs.xpath.entry, con.namespaces);									
									for(var count = 0; count < entry.length; count ++){	
										var node = entry[count];
										if (getArgs.communityServiceEntity == "communities") {
											var community = new Community(this, xpath.selectText(node, getArgs.xpath.id, con.namespaces));
											community.data = node;
											entities.push(community);
										} else if (getArgs.communityServiceEntity == "community" && getArgs.communitiesType == "members") {
											var member = new Member(this, xpath.selectText(node, getArgs.xpath.id, con.namespaces));
											member.data = node;
											entities.push(member);
										}
									}
									if (args.load)
										args.load(entities);
									if (args.handle)
										args.handle(entities);
								},
								error : function(error) {
									validate.notifyError(error, args);
								}
							});
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
									code : Constants.sbtErrorCodes.badRequest,
									message : Constants.sbtErrorMessages.null_community
								}, args);
								return false;
							} else {
								return true;
							}
						},
					});
			return CommunityService;
		});
