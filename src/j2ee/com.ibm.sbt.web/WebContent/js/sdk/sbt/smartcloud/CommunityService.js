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
 * Helpers for accessing the SmartCloud Community services
 * @module sbt.CommunityService
 */
define(['sbt/declare','sbt/config','sbt/lang','sbt/smartcloud/core','sbt/xml','sbt/xpath','sbt/Cache',
        'sbt/Endpoint','sbt/smartcloud/CommunityConstants',
        'sbt/base/BaseService', 'sbt/base/BaseEntity', 'sbt/log','sbt/base/XmlHandler'],
		function(declare,cfg,lang,con,xml,xpath,Cache,
				Endpoint, communityConstants,
				BaseService, BaseEntity, log, XmlHandler) {
	
	var communityHandler = new XmlHandler({xpath_map: communityConstants.xpath_community, xpath_feed_map: communityConstants.xpath_feed_community,nameSpaces:con.namespaces});
	var memberHandler = new XmlHandler({xpath_map: communityConstants.xpath_member, xpath_feed_map: communityConstants.xpath_feed_member,nameSpaces:con.namespaces});
	
	
	/**
	 * Community class associated with a community. 
	 * @namespace smartcloud
	 * @class Community
	 * @constructor
	 * @param {Object} CommunityService  communityService object
	 * @param {String} id community id associated with the community.
	 */		
	var Community = declare("sbt.smartcloud.Community", BaseEntity, {
		
		constructor: function(svc,id) {
			var args = { entityName : "community", Constants: communityConstants, con: con, dataHandler: communityHandler};
			this.inherited(arguments, [svc, id, args]);
		},
		
		validate: function(className, methodName, args, validateMap) {
			if (validateMap.isValidateId && !(validate._validateInputTypeAndNotify(className, methodName, "Community Id", this._id, "string", args))) {
				return false;
			}
			return true;
		},
		
		/**
		Updates the community object.

		@method update
		@param {Object} [args]  Argument object			
			@param {Function} [args.load] The function community.load invokes when the community is 
			loaded from the server. The function expects to receive one parameter, 
			the loaded community object.
			@param {Function} [args.error] Sometimes the load calls  fail. Often these are due to bad request 
			like http error code 400 or server errors like http error code 500. The error parameter is another callback function
			that is only invoked when an error occurs. This allows to control what happens
		    when an error occurs without having to put a lot of logic into your load function
		    to check for error conditions. The parameter passed to the error function is a 
		    JavaScript Error object indicating what the failure was. From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
			@param {Function} [args.handle] This callback is called regardless of whether 
			the call to load the community completes or fails. The  parameter passed to this callback
			is the community object (or error object). From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
		
		**/
		update: function(args) {			
			this._service.updateCommunity(this,args);			
		},
		
		getCommunityUuid: function () {
			return this.get("communityUuid");
		},
		getTitle: function () {
			return this.get("title");
		},
		getSummary: function () {
			return this.get("summary");
		},
		getContent: function () {
			return this.get("content");
		},
		getCommunityUrl: function () {
			return this.get("communityUrl");
		},
		getLogoUrl: function () {
			return this.get("logoUrl");
		},
		getTags: function (){
			return this.get("tags");
		},
		setTitle: function(title){
			this.set("title",title);
		},
		setContent: function(content){
			this.set("content",content);
		},
		setAddedTags: function(addedTags){
			this.set("addedTags",addedTags);
		},
		setDeletedTags: function(deletedTags){
			this.set("deletedTags",deletedTags);
		}
	});
	
	/**
	 * Member class associated with a community.
	 * @namespace smartcloud 
	 * @class Member
	 * @constructor
	 * @param {Object} CommunityService  communityService object
	 * @param {String} id id of the member. This can be userid or email of the member.
	 */
	var Member = declare("sbt.smartcloud.Member", BaseEntity, {
		
		constructor: function(svc,id) {
			var args = { entityName : "member", Constants: communityConstants, dataHandler: memberHandler};
			this.inherited(arguments, [svc, id, args]);
		},
		
		validate: function(className, methodName, args, validateMap) {
			if (validateMap.isValidateId && !(validate._validateInputTypeAndNotify(className, methodName, "Community Member Id", this._id, "string", args))) {
				return false;
			}
			return true;
		},
		
		getName: function () {
			return this.get("name");
		},
		setName: function(name){
			this.set("name",name);
		},
		getRole: function () {
			return this.get("role");
		},
		setRole: function(role){
			this.set("role",role);
		}
	});
	/**
	 * 	Community service class associated with the community service of IBM SmartCloud.
	 *
	 * @namespace smartcloud
	 * @class CommunityService
	 * @constructor
	 * @param {Object} options  Options object
	 * @param {String} [options.endpoint=smartcloud]  Endpoint to be used by CommunityService.
	 * 		
	 */
	var CommunityService = declare("sbt.smartcloud.CommunityService", BaseService, {
		
		constructor: function(_options) {
			var options = _options || {};			
			options = lang.mixin({endpoint: options.endpoint || "smartcloud", Constants: communityConstants, con: con});
			this.inherited(arguments, [options]);
		},
		
		_parseCommunityId: function(url) {
			return url.substring(url.indexOf("communityUuid=")+"communityUuid=".length);
		},
		
		/**
		Get member entry document of a member of a community
		
		@method getMember
		@param {Object} args  Argument object
			@param {String} args.id Id of the member. This can be userId or email of the member.
			@param {String/Object} args.community This can be either a string representing community id or a community object 
			with its id initialized.
			@param {Boolean} [args.loadIt=true] Loads the members object with member entry document.  If 
			an empty member object associated with a community (with no member entry
			document) is needed, then the load method must be called with this parameter set to false. By default, this 
			parameter is true.
			@param {Function} [args.load] This function is invoked when the member is 
			loaded from the server. The function expects to receive one parameter, 
			the loaded member object.
			@param {Function} [args.error] Sometimes the getMember call fails with bad request such as 400  
			or server errors such as 500. The error parameter is another callback function
			that is only invoked when an error occurs. This allows to control what happens
		    when an error occurs without having to put a lot of logic into your load function
		    to check for error conditions. The parameter passed to the error function is a 
		    JavaScript Error object indicating what the failure was. From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
			@param {Function} [args.handle] This callback is called regardless of whether 
			the call to get the member completes or fails. The  parameter passed to this callback
			is the member object (or error object). From the error object. one can get access to the 
			javascript library error object, the status code and the error message.
		
		**/
		getMember: function(args) {
			if (!args || !args.community || !args.community._id || !args.id) {
				log.error("Need to pass communityId and userid of the member.");
			}
			var content = {communityUuid : args.community._id};
			if(this._isEmail(args.id)){
				content.email = args.id; 
			}else{
				content.userid = args.id; 
			}
			return this._getOne(args,
				{entityName: "community", serviceEntity: "community", entityType: "members",
					entity: Member,
					content : content
				});
		},
		
		/**
		Get community object associated with a community
		
		@method getCommunity
		@param {Object} args  Argument object
			@param {String} args.id id of the community
			@param {Boolean} [args.loadIt=true] 
			@param {Function} [args.load] 
			@param {Function} [args.error] 
			@param {Function} [args.handle] 
		
		**/
		getCommunity: function(args) {
			if (!args || !args.id) {
				log.error("Need to pass communityId.");
			}
			return this._getOne(args,
				{entityName: "community", serviceEntity: "community", entityType: "instance",
					entity: Community,
					urlParams : { communityUuid : args.id },
					headers : {"Content-Type" : "application/atom+xml"}
				});
		},
		
		/**
		Create a new community
		
		@method createCommunity
		@param {Object} community  Community object
		@param {Object} [args]  Argument object
			@param {Boolean} [args.loadIt=true] 
			@param {Function} [args.load] 
			@param {Function} [args.error] 
			@param {Function} [args.handle] 
		
		**/
		createCommunity: function (community, args) {
			if (!args || !community) {
				log.error("Need to pass community and arguments.");
			}
			var xmlPayloadTemplate = 
				["<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
				"<entry xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" xmlns:app=\"http://www.w3.org/2007/app\" xmlns=\"http://www.w3.org/2005/Atom\">",
					"<title type=\"text\">{title}</title>",
					"<content type=\"html\">{content}</content>",
					"<category term=\"community\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category>",
					"<snx:communityType xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">{communityType}</snx:communityType>",
				"</entry>"].join("");
			var xmlData = { title: community.getTitle(), content: community.getContent(), communityType: "private" };
			var headers = {
					"Content-Type" : "application/atom+xml"
				};
			this._createEntity(args,
					{entityName: "community", serviceEntity: "communities", entityType: "my",
						entity: community,
						headers: headers,
						xmlPayloadTemplate : xmlPayloadTemplate,
						xmlData : xmlData
						
					});
		},
		
		_createEntityOnLoad : function (data, ioArgs, entity, args, postArgs){
			var newCommunityUrl = ioArgs.xhr.getResponseHeader("Location");
			var communityId = newCommunityUrl.substring(newCommunityUrl.indexOf("communityUuid=") + "communityUuid=".length);
			entity._id = communityId;
			entity._fields = {};
			if (args && args.loadIt != false) {									
				this._load(entity, args,{entityName: "community", serviceEntity: "community", entityType: "instance",
					entity: Community, urlParams : { communityUuid : entity._id },
					headers : {"Content-Type" : "application/atom+xml"}
				});
			}else{
				this._notifyResponse(args,entity);
			}
		},						
			
			
		
		
		/**
		Update an existing community
		
		@method updateCommunity
		@param {Object} community  Community object
		@param {Object} [args]  Argument object			
			@param {Function} [args.load] 
			@param {Function} [args.error] 
			@param {Function} [args.handle] 
		
		**/
		updateCommunity: function (community, args) {
			if (!args || !community) {
				log.error("Need to pass community and arguments.");
			}
			community.updateData();
			//this.updateXmlField(community._data, "title", community.getTitle());
			//this.updateXmlField(community._data, "content", community.getContent());
			var xmlPayloadTemplate = community.getUpdatePayload();
			//var xmlData = { title: community.getTitle(), content: community.getContent(), communityType: "private" };
			this._updateEntity(args,
					{entityName: "community", serviceEntity: "community", entityType: "instance",
						entity: community,
						xmlPayloadTemplate : xmlPayloadTemplate,
						//xmlData : xmlData,
						urlParams : { communityUuid: community.getId()}
					});
		},
			
		/**
		Delete an existing community
		
		@method deleteCommunity
		@param {String/Object} community  id of the community or the community object.
		@param {Object} [args]  Argument object			
			@param {Function} [args.load] 
			@param {Function} [args.error] 
			@param {Function} [args.handle] 
		
		**/
		deleteCommunity: function (inputCommunity, args) {
			if (!args || !inputCommunity) {
				log.error("Need to pass community and arguments.");
			}
			var communityId = this.getIdFromIdOrObject(inputCommunity);
			this._deleteEntity(args,{entityName: "community", serviceEntity: "community", entityType: "instance",
				urlParams : { communityUuid: communityId }
			});
		},
		
		/**
		Add member to a community
		
		@method addMember
		@param {String/Object} community  id of the community or the community object.
		@param {Object} member  member object representing the member of the community
		@param {Object} [args]  Argument object			
			@param {Function} [args.load] 
			@param {Function} [args.error] 
			@param {Function} [args.handle] 
		
		**/	
		addMember: function (inputCommunity, inputMember, args) {
			if (!args || !inputCommunity || !inputMember) {
				log.error("Need to pass community, member and arguments.");
			}
			var xmlPayloadTemplate = 
				["<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
				 "<entry xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" xmlns:app=\"http://www.w3.org/2007/app\" xmlns=\"http://www.w3.org/2005/Atom\">",
				 	"<contributor>",
				 		"<snx:userid xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">{userId}</snx:userid>",
				 	"</contributor>",
				 	"<snx:role xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" component=\"http://www.ibm.com/xmlns/prod/sn/communities\">{role}</snx:role>",
				 "</entry>"].join("");
			var xmlData = { userId: inputMember, role: "member" };
			var communityId = this.getIdFromIdOrObject(inputCommunity);
			this._createEntity(args,
					{entityName: "community", serviceEntity: "community", entityType: "members",
						entity: Member,
						xmlPayloadTemplate : xmlPayloadTemplate,
						xmlData : xmlData,
						urlParams : { communityUuid: communityId }
					});
		},
		
		/**
		Remove member of a community
		
		@method removeMember
		@param {String/Object} community  id of the community or the community object.
		@param {String/Object} member  id of the member or member object
		@param {Object} [args]  Argument object			
			@param {Function} [args.load] 
			@param {Function} [args.error] 
			@param {Function} [args.handle] 
		
		**/	
		removeMember: function (inputCommunity, inputMember, args) {
			if (!args || !inputCommunity || !inputMember) {
				log.error("Need to pass community, member and arguments.");
			}
			var communityId = this.getIdFromIdOrObject(inputCommunity);
			var memberId = this.getIdFromIdOrObject(inputMember);
			var params = { communityUuid: communityId };
			if (_self._isEmail(memberId)) {
				lang.mixin(params, { email: memberId });
			} else {
				lang.mixin(params, { userid: memberId });
			}
			this._deleteEntity(args,{entityName: "community", serviceEntity: "community", entityType: "members",
				urlParams : params
			});
		},
		
		/**
		Get public communities from IBM SmartCloud
		
		@method getPublicCommunities
		@param {Object} [args]  Argument object			
			@param {Function} [args.load] 
			@param {Function} [args.error] 
			@param {Function} [args.handle] 
			@param {Object} [args.parameters] Object representing various parameters that can be passed to get a feed of public communities.
			The parameters must be exactly as they are supported by IBM SmartCloud like ps, sortBy etc.
		
		**/	
		getPublicCommunities: function (args){
			if (!args) {
				log.error("Need to pass arguments.");
			}
			this._getEntities(args,
					{entityName: "community", serviceEntity: "communities", entityType: "public",
						parseId: this._parseCommunityId,
						entity: Community,
						headers : {"Content-Type" : "application/atom+xml"},
						dataHandler : communityHandler
					});
		},
		
		/**
		Get my communities from IBM SmartCloud
		
		@method getMyCommunities
		@param {Object} [args]  Argument object			
			@param {Function} [args.load] 
			@param {Function} [args.error] 
			@param {Function} [args.handle] 
			@param {Object} [args.parameters] Object representing various parameters that can be passed to get a feed of my communities.
			The parameters must be exactly as they are supported by IBM SmartCloud like ps, sortBy etc.
		
		**/	
		
		getMyCommunities: function (args){
			if (!args) {
				log.error("Need to pass arguments.");
			}
			this._getEntities(args,
					{entityName: "community", serviceEntity: "communities", entityType: "my",
						parseId: this._parseCommunityId,
						entity: Community,
						headers : {"Content-Type" : "application/atom+xml"},
						dataHandler : communityHandler
					});
		},
		
		/**
		Get members of a community.
		
		@method getMembers
		@param {Object} [args]  Argument object			
			@param {Function} [args.load] 
			@param {Function} [args.error] 
			@param {Function} [args.handle] 
			@param {Object} [args.parameters] Object representing various parameters that can be passed to get a feed of members of a community.
			The parameters must be exactly as they are supported by IBM SmartCloud like ps, sortBy etc.
		
		**/	
		getMembers: function (communityArg, args){
			if (!args || !communityArg) {
				log.error("Need to pass community/communityId and arguments.");
			}
			var community = (typeof communityArg == "object")? communityArg : new Community(this, communityArg);
			this._getEntities(args,
					{community: community,
						entityName: "member", serviceEntity: "community", entityType: "members",
						entity: Member,
						urlParams : { communityUuid : community._id },
						dataHandler : memberHandler
					});
			
		},
		_createEntityObject : function (service, id, requestArgs, args){
			if(requestArgs.entityName == "community"){
				var community = new Community (service, id);
				return community;
			}else if (requestArgs.entityName == "member"){
				var member = new Member (service, id);
				return member;
			}
		},
	});
	return CommunityService;
});
