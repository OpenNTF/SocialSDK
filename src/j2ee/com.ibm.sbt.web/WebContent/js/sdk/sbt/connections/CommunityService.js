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
 * The Communities API allows application programs to retrieve community information, subscribe to community updates, and create or modify communities.
 * 
 * @module sbt.connections.CommunityService
 */
define([ "../declare", "../config", "../lang", "../stringUtil", "../Promise", "./CommunityConstants", "../base/BaseService",
         "../base/AtomEntity", "../base/XmlDataHandler", "./ForumService", "../pathUtil" ], 
    function(declare,config,lang,stringUtil,Promise,consts,BaseService,AtomEntity,XmlDataHandler,ForumService,pathUtil) {

	var CategoryCommunity = "<category term=\"community\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category>";
	var CategoryMember = "<category term=\"person\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category>";
	var CategoryInvite = "<category term=\"invite\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category>";
	var CategoryEvent = "<category term=\"event\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category>";
    
	var IsExternalTmpl = "<snx:isExternal>${isExternal}</snx:isExternal>";
    var CommunityTypeTmpl = "<snx:communityType>${getCommunityType}</snx:communityType>";
    var CommunityUuidTmpl = "<snx:communityUuid xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">${getCommunityUuid}</snx:communityUuid>";
    var CommunityThemeTmpl = "<snx:communityTheme xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" snx:uuid=\"default\">${getCommunityTheme}</snx:communityTheme>";
    var RoleTmpl = "<snx:role xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" component=\"http://www.ibm.com/xmlns/prod/sn/communities\">${getRole}</snx:role>";
    
    /*
     * CommunityDataHandler class.
     */
    var CommunityDataHandler = declare(XmlDataHandler, {
        /**
         * @method getEntityId
         * @returns
         */
        getEntityId : function() {
            var entityId = stringUtil.trim(this.getAsString("uid"));
            return extractCommunityUuid(this.service, entityId);
        }
    });
    
    /**
     * Community class represents an entry for a Community feed returned by the
     * Connections REST API.
     * 
     * @class Community
     * @namespace sbt.connections
     */
    var Community = declare(AtomEntity, {

    	xpath : consts.CommunityXPath,
    	namespaces : consts.CommunityNamespaces,
    	categoryScheme : CategoryCommunity,
    	    	
        /**
         * Construct a Community entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },

        /**
         * Create the DataHandler for this entity.
         * 
         * @method createDataHandler
         */
        createDataHandler : function(service, data, response, namespaces, xpath) {
        	return new CommunityDataHandler({
                service : service,
                data : data,
                namespaces : namespaces,
                xpath : xpath
            });
        },
        
        /**
         * Return extra entry data to be included in post data for this entity.
         * 
         * @returns {String}
         */
        createEntryData : function() {
        	var postData = "";
            var transformer = function(value,key) {
                return value;
            };
            postData += stringUtil.transform(CommunityTypeTmpl, this, transformer, this);
            postData += stringUtil.transform(IsExternalTmpl, this, transformer, this);
        	if (this.getCommunityUuid()) {
                postData += stringUtil.transform(CommunityUuidTmpl, this, transformer, this);
        	}
        	if (this.getCommunityTheme()) {
                postData += stringUtil.transform(CommunityThemeTmpl, this, transformer, this);
        	}
            return stringUtil.trim(postData);
        },
        
        /**
         * Return the value of IBM Connections community ID from community ATOM
         * entry document.
         * 
         * @method getCommunityUuid
         * @return {String} Community ID of the community
         */
        getCommunityUuid : function() {
            var communityUuid = this.getAsString("communityUuid");
            return extractCommunityUuid(this.service, communityUuid);
        },

        /**
         * Sets id of IBM Connections community.
         * 
         * @method setCommunityUuid
         * @param {String} communityUuid Id of the community
         */
        setCommunityUuid : function(communityUuid) {
            return this.setAsString("communityUuid", communityUuid);
        },

        /**
         * Return the community type of the IBM Connections community from
         * community ATOM entry document.
         * 
         * @method getCommunityType
         * @return {String} Type of the Community
         */
        getCommunityType : function() {
            var type = this.getAsString("communityType");
            if (!type) {
            	type = consts.Restricted;
            }
            return type;
        },

        /**
         * Set the community type of the IBM Connections community.
         * 
         * @method setCommunityType
         * @param {String} Type of the Community
         */
        setCommunityType : function(communityType) {
            return this.setAsString("communityType", communityType);
        },

        /**
         * Return the community theme of the IBM Connections community from
         * community ATOM entry document.
         * 
         * @method getCommunityTheme
         * @return {String} Theme of the Community
         */
        getCommunityTheme : function() {
            return this.getAsString("communityTheme");
        },

        /**
         * Set the community theme of the IBM Connections community.
         * 
         * @method setCommunityTheme
         * @param {String} Theme of the Community
         */
        setCommunityTheme : function(communityTheme) {
            return this.setAsString("communityTheme", communityTheme);
        },

        /**
         * Return the external of the IBM Connections community from
         * community ATOM entry document.
         * 
         * @method isExternal
         * @return {Boolean} External flag of the Community
         */
        isExternal : function() {
            return this.getAsBoolean("isExternal");
        },

        /**
         * Set the external flag of the IBM Connections community.
         * 
         * @method setExternal
         * @param {Boolean} External flag of the Community
         */
        setExternal : function(external) {
            return this.setAsBoolean("isExternal", external);
        },

        /**
         * Return tags of IBM Connections community from community ATOM entry
         * document.
         * 
         * @method getTags
         * @return {Object} Array of tags of the community
         */
        getTags : function() {
            return this.getAsArray("tags");
        },

        /**
         * Set new tags to be associated with this IBM Connections community.
         * 
         * @method setTags
         * @param {Object} Array of tags to be added to the community
         */

        setTags : function(tags) {
            return this.setAsArray("tags", tags);
        },

        /**
         * Return the value of IBM Connections community URL from community ATOM
         * entry document.
         * 
         * @method getCommunityUrl
         * @return {String} Community URL of the community
         * @deprecated Use getAlternateUrl instead
         */
        getCommunityUrl : function() {
            return this.getAlternateUrl();
        },

        /**
         * Return the value of IBM Connections community Logo URL from community
         * ATOM entry document.
         * 
         * @method getLogoUrl
         * @return {String} Community Logo URL of the community
         */
        getLogoUrl : function() {
            return this.getAsString("logoUrl");
        },

        /**
         * Return the member count of the IBM Connections community from
         * community ATOM entry document.
         * 
         * @method getMemberCount
         * @return {Number} Member count for the Community
         */
        getMemberCount : function() {
            return this.getAsNumber("memberCount");
        },

        /**
         * Get a list for forum topics that includes the topics in this community.
         * 
         * @method getForumTopics
         * @param {Object} args
         */
        getForumTopics : function(args) {
        	return this.service.getForumTopics(this.getCommunityUuid(), args);
        },
        
        /**
         * Create a forum topc by sending an Atom entry document containing the 
         * new forum to the forum replies resource.
         * 
         * @method createForumTopic
         * @param {Object} forumTopic Forum topic object which denotes the forum topic to be created.
         * @param {Object} [args] Argument object
         */
        createForumTopic : function(communityUuid, topicOrJson, args) {
        	return this.service.createForumTopic(this.getCommunityUuid(), topicOrJson, args);
        },
        
        /**
         * Get sub communities of a community.
         * 
         * @method getSubCommunities
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of members of a
         * community. The parameters must be exactly as they are
         * supported by IBM Connections like ps, sortBy etc.
         */
        getSubCommunities : function(args) {
            return this.service.getSubCommunities(this.getCommunityUuid(), args);
        },

        /**
         * Get members of this community.
         * 
         * @method getMembers
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of members of a
         * community. The parameters must be exactly as they are
         * supported by IBM Connections like ps, sortBy etc.
         */
        getMembers : function(args) {
            return this.service.getMembers(this.getCommunityUuid(), args);
        },

        /**
         * Add member to a community
         * 
         * @method addMember
         * @param {Object} [args] Argument object
         * @param {Object} [args.member] Object representing the member to be added
         * @param {String} [args.email] Object representing the email of the memeber to be added
         * @param {String} [args.id] String representing the id of the member to be added
         */
        addMember : function(member,args) {
            return this.service.addMember(this.getCommunityUuid(), member, args);
        },

        /**
         * Remove member of a community
         * 
         * @method removeMember
         * @param {String} Member id of the member 
         * @param {Object} [args] Argument object
         */
        removeMember : function(memberId,args) {
            return this.service.removeMember(this.getCommunityUuid(), memberId, args);
        },
        
        /**
         * Loads a member object with the atom entry associated with the
         * member of the community. By default, a network call is made to load the atom entry
         * document in the member object.
         * 
         * @method getMember
         * @param {String} member id of the member.
         * @param {Object} [args] Argument object
         */
        getMember : function(memberId, args) {
        	return this.service.getMember(this.getCommunityUuid(), memberId, args);
        },
        
        /**
         * Get a list of the outstanding community invitations for the specified community. 
         * The currently authenticated user must be an owner of the community.
         * 
         * @method getAllInvites
         * @param {Object} [args]
         */
        getAllInvites : function(args) {
        	return this.service.getAllInvites(this.getCommunityUuid(), args);
        },

        /**
         * Get the list of community forums.
         * 
         * @method getForums
         * @param {Object} [args] Argument object
         */
        getForums : function(args) {
        	var forumService = this.service.getForumService();
        	var requestArgs = lang.mixin(args || {}, { communityUuid : this.getCommunityUuid() });
        	return forumService.getForums(requestArgs);
        },
        
        /**
         * Loads the community object with the atom entry associated with the
         * community. By default, a network call is made to load the atom entry
         * document in the community object.
         * 
         * @method load
         * @param {Object} [args] Argument object
         */
        load : function(args) {
            // detect a bad request by validating required arguments
            var communityUuid = this.getCommunityUuid();
            var promise = this.service._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }

            var self = this;
            var callbacks = {
                createEntity : function(service, data, response) {
                	self.setData(data, response);
                    return self;
                }
            };

            var requestArgs = lang.mixin(
            	{ communityUuid : communityUuid }, 
            	args || {});
            var options = {
                handleAs : "text",
                query : requestArgs
            };
            
            return this.service.getEntity(consts.AtomCommunityInstance, options, communityUuid, callbacks);
        },

        /**
         * Remove this community
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        remove : function(args) {
            return this.service.deleteCommunity(this.getCommunityUuid(), args);
        },

        /**
         * Update this community
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        update : function(args) {
            return this.service.updateCommunity(this, args);
        },
        
        /**
         * Save this community
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        save : function(args) {
            if (this.getCommunityUuid()) {
                return this.service.updateCommunity(this, args);
            } else {
                return this.service.createCommunity(this, args);
            }
        }
        
    });

    /**
     * Member class represents an entry for a Member feed returned by the
     * Connections REST API.
     * 
     * @class Member
     * @namespace sbt.connections
     */
    var Member = declare(AtomEntity, {

    	xpath : consts.MemberXPath,
    	namespaces : consts.CommunityNamespaces,
    	categoryScheme : CategoryMember,
    	    	
        /**
         * The UUID of the community associated with this Member
         */
        communityUuid : null,

        /**
         * Constructor for Member entity
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
            if (!this.getRole()) {
                this.setRole(consts.Member);
            }
        },

        /**
         * Return extra entry data to be included in post data for this entity.
         * 
         * @returns {String}
         */
        createEntryData : function() {
        	var postData = "";
            var transformer = function(value,key) {
                return value;
            };
            postData += stringUtil.transform(RoleTmpl, this, transformer, this);
            return stringUtil.trim(postData);
        },
        
        /**
         * Return the community UUID.
         * 
         * @method getCommunityUuid
         * @return {String} communityUuid
         */
        getCommunityUuid : function() {
            return this.communityUuid;
        },

        /**
         * Return the community member name.
         * 
         * @method getName
         * @return {String} Community member name
         */

        getName : function() {
            return this.getAsString("contributorName");
        },

        /**
         * Set the community member name.
         * 
         * @method setName
         * @param {String} Community member name
         */

        setName : function(name) {
            return this.setAsString("contributorName", name);
        },

        /**
         * Return the community member userId.
         * 
         * @method getUserid
         * @return {String} Community member userId
         */
        getUserid : function() {
            return this.getAsString("contributorUserid");
        },

        /**
         * Set the community member userId.
         * 
         * @method getId
         * @return {String} Community member userId
         */
        setUserid : function(userid) {
            return this.setAsString("contributorUserid", userid);
        },

        /**
         * Return the community member email.
         * 
         * @method getName
         * @return {String} Community member email
         */
        getEmail : function() {
            return this.getAsString("contributorEmail");
        },

        /**
         * Return the community member email.
         * 
         * @method getName
         * @return {String} Community member email
         */

        setEmail : function(email) {
            return this.setAsString("contributorEmail", email);
        },

        /**
         * Return the value of community member role from community member ATOM
         * entry document.
         * 
         * @method getRole
         * @return {String} Community member role
         */
        getRole : function() {
            return this.getAsString("role");
        },

        /**
         * Sets role of a community member
         * 
         * @method setRole
         * @param {String} role Role of the community member.
         */
        setRole : function(role) {
            return this.setAsString("role", role);
        },
        
        /**
         * Loads the member object with the atom entry associated with the
         * member. By default, a network call is made to load the atom entry
         * document in the member object.
         * 
         * @method load
         * @param {Object} [args] Argument object
         */
        load : function(args) {
            // detect a bad request by validating required arguments
            var communityUuid = this.communityUuid;
            var promise = this.service._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }

            var self = this;
            var callbacks = {
                createEntity : function(service, data, response) {
                    self.setData(data, response);
                    return self;
                }
            };

            var requestArgs = {
                communityUuid : communityUuid
            };
            var memberId = null;
            if (this.getUserid()) {
                memberId = requestArgs.userid = this.getUserid();
            } else {
                memberId = requestArgs.email = this.getEmail();
            }
            requestArgs = lang.mixin(requestArgs, args || {});
            var options = {
                handleAs : "text", 
                query : requestArgs
            };

            return this.service.getEntity(consts.AtomCommunityMembers, options, memberId, callbacks);
        },
        
        /**
         * Remove this member from the community.
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        remove : function(args) {
        	var memberId = this.getUserid() || this.getEmail();
            return this.service.removeMember(this.getCommunityUuid(), memberId, args);
        }

    });

    /**
     * Invite class represents an entry for a Invite feed returned by the
     * Connections REST API.
     * 
     * @class Invite
     * @namespace sbt.connections
     */
    var Invite = declare(AtomEntity, {

    	xpath : consts.InviteXPath,
    	namespaces : consts.CommunityNamespaces,
    	categoryScheme : CategoryInvite,
    	    	
        /**
         * The UUID of the community associated with this Invite
         */
        communityUuid : null,

        /**
         * The UUID if the invitee associated with this Invite
         */
        inviteeUuid : null,

        /**
         * Constructor for Invite
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
            this.inherited(arguments, [ args ]);
        },

        /**
         * Return the value of IBM Connections invite ID from invite ATOM
         * entry document.
         * 
         * @method getInviteUuid
         * @return {String} Invite ID of the invite
         */
        getInviteUuid : function() {
            var inviteUuid = this.getAsString("inviteUuid");
            return extractInviteUuid(inviteUuid);
        },

        /**
         * Sets id of IBM Connections invite.
         * 
         * @method setInviteUuid
         * @param {String} inviteUuid Id of the invite
         */
        setInviteUuid : function(inviteUuid) {
            return this.setAsString("inviteUuid", inviteUuid);
        },

        /**
         * Set the community UUID.
         * 
         * @method setCommunityUuid
         * @return {String} communityUuid
         */
        setCommunityUuid : function(communityUuid) {
			this.communityUuid = communityUuid;
			return this;
        },
        
        /**
         * Return the community UUID.
         * 
         * @method getCommunityUuid
         * @return {String} communityUuid
         */
        getCommunityUuid : function() {
        	if (!this.communityUuid) {
				this.communityUuid = this.service.getUrlParameter(this.getAsString("communityUrl"), "communityUuid");
			} 
			return this.communityUuid;
        },
        
        /**
         * Set the invitee UUID.
         * 
         * @method setInviteeUuid
         * @return {String} inviteeUuid
         */
        setInviteeUuid : function(inviteeUuid) {
			this.inviteeUuid = inviteeUuid;
			return this;
        },
        
        /**
         * Return the value of IBM Connections invitee ID from invite ATOM
         * entry document.
         * 
         * @method getInviteeUuid
         * @return {String} Invitee ID of the invite
         */
        getInviteeUuid : function() {
        	if (!this.inviteeUuid) {
            	var inviteUuid = this.getInviteUuid();
            	this.inviteeUuid = extractInviteeUuid(inviteUuid, this.getCommunityUuid());
        	}
        	return this.inviteeUuid;
        },

        /**
         * Set the user id of the invitee.
         * 
         * @method setUserid
         * @return {String} userid
         */
        setUserid : function(userid) {
        	return this.setAsString("contributorUserid", userid);
        },
        
        /**
         * Return the user id of the invitee.
         * 
         * @method getUserid
         * @return {String} userid
         */
        getUserid : function() {
        	return this.getAsString("contributorUserid");
        },
        
        /**
         * Set the email of the invitee.
         * 
         * @method setEmail
         * @return {String} email
         */
        setEmail : function(email) {
        	return this.setAsString("contributorEmail", email);
        },
        
        /**
         * Return the email of the invitee.
         * 
         * @method getEmail
         * @return {String} email
         */
        getEmail : function() {
        	return this.getAsString("contributorEmail");
        },
        
        /**
         * Loads the invite object with the atom entry associated with the
         * invite. By default, a network call is made to load the atom entry
         * document in the invite object.
         * 
         * @method load
         * @param {Object} [args] Argument object
         */
        load : function(args) {
            // detect a bad request by validating required arguments
            var communityUuid = this.getCommunityUuid();
            var promise = this.service._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }
            var userid = this.getInviteeUuid();
            promise = this.service._validateUserid(userid);
            if (promise) {
                return promise;
            }

            var self = this;
            var callbacks = {
                createEntity : function(service, data, response) {
                    self.setData(data, response);
                    return self;
                }
            };

            var requestArgs = lang.mixin({
                communityUuid : communityUuid,
                userid : userid
            }, args || {});
            var options = {
                handleAs : "text",
                query : requestArgs
            };
            
            return this.service.getEntity(consts.AtomCommunityInvites, options, communityUuid + "-" + userid, callbacks);
        },

        /**
         * Remove this invite
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        remove : function(args) {
            return this.service.removeInvite(this, args);
        },

        /**
         * Update this invite
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        update : function(args) {
            return this.service.updateInvite(this, args);
        },
        
        /**
         * Save this invite
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        save : function(args) {
            if (this.getInviteUuid()) {
                return this.service.updateInvite(this, args);
            } else {
                return this.service.createInvite(this, args);
            }
        }        

    });
    
    
    /**
     * Event class represents an entry for an Events feed returned by the Connections REST API.
     * 
     * @class Event
     * @namespace sbt.connections
     */
    var Event = declare(AtomEntity, {

    	xpath : consts.EventXPath,
    	namespaces : consts.CommunityNamespaces,
    	categoryScheme : CategoryEvent,
    	    	
        /**
         * Constructor for Event.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },

        /**
         * Return the community UUID.
         * 
         * @method getCommunityUuid
         * @return {String} communityUuid
         */
        getCommunityUuid : function() {
            return this.getAsString("communityUuid");
        },
        
        /**
         * The Uuid of the event. This is per event rather than per event instance. 
         * 
         * e.g. if an event spans multiple days it will have multiple instances, yet each even will have the same Uuid.
         * @method getEventUuid
         * @return {String} Uuid of the event.
         */
        getEventUuid : function(){
            return this.getAsString("eventUuid");
        },
        
        /**
         * The event instance uuid. This is per event instance, rather than per event. 
         * e.g. if an event spans multiple days each day will have its own eventInstUuid.
         * 
         * Can be used with the{{#crossLink "CommunityService/getEvent:method"}}{{/crossLink}} method to retrieve event instances.
         * @method getEventInstUuid
         * @return {String} Uuid of the event instance.
         */
        getEventInstUuid : function(){
            return this.getAsString("eventInstUuid");
        },

        /**
         * return the atom event url.
         * 
         * @returns
         */
        getEventAtomUrl : function(){
            return this.getAsString("eventAtomUrl");
        },
        
        /**
         * 
         * @returns
         */
        getLocation : function(){
            return this.getAsString("location");
        },

        /**
         * Gets the recurrence information of the event.
         * 
         * Recurrence information object consists of:
         * frequency - 'daily' or 'weekly'
         * interval - Week interval. Value is int between 1 and 5.
         * until - The end date of the repeating event.
         * allDay - 1 if an all day event, 0 otherwise.
         * startDate - Start time of the event
         * endDate - End time of the event
         * byDay - Days of the week this event occurs, possible values are: SU,MO,TU,WE,TH,FR,SA
         * 
         * @method getRecurrence
         * @return {Object} An object containing the above recurrence information of the community event.
         */
        getRecurrence : function() {
            return this.getAsObject(
            		[ "frequency", "interval", "until", "allDay", "startDate", "endDate", "byDay" ],
            		[ "frequency", "interval", "until", "allDay", "startDate", "endDate", "byDay" ]);
        },

        /**
         * Get the full event description, with content.
         * @deprecated Use CommunityEvent.load
         */
        getFullEvent : function() {
            return load();
        },
        
        /**
         * Load the content for this community event instance.
         * 
         * @method load
         */
        load : function(args) {
            // detect a bad request by validating required arguments
            var eventInstUuid = this.getEventInstUuid();
            var promise = this.service._validateEventInstUuid(eventInstUuid);
            if (promise) {
                return promise;
            }

            var self = this;
            var callbacks = {
                createEntity : function(service,data,response) {
                    self.setData(data);
                    return self;
                }
            };

            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ eventInstUuid : eventInstUuid }, args || {})
            };
            
            return this.service.getEntity(consts.AtomCommunityEvent, options, eventInstUuid, callbacks);
        }
        
    });

    /*
     * Method used to extract the community uuid for an id url.
     */
    var extractCommunityUuid = function(service, uid) {
        if (uid && uid.indexOf("http") == 0) {
            return service.getUrlParameter(uid, "communityUuid");
        } else {
            return uid;
        }
    };
    
    /*
     * Method used to extract the invite uuid for an id url.
     */
    var extractInviteUuid = function(uid) {
    	if (uid && uid.indexOf("urn:lsid:ibm.com:communities:invite-") == 0) {
            return uid.substring("urn:lsid:ibm.com:communities:invite-".length);
        } else {
            return uid;
        }
    };
    
    /*
     * Method used to extract the invitee uuid for an id url.
     */
    var extractInviteeUuid = function(uid, communityUuid) {
    	if (uid && uid.indexOf(communityUuid) == 0) {
            return uid.substring(communityUuid.length + 1);
        } else {
            return uid;
        }
    };
    
    /*
     * Callbacks used when reading a feed that contains Community entries.
     */
    var CommunityFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new CommunityDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.CommunityFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            return new Community({
                service : service,
                data : data,
                response: response
            });
        }
    };

    /*
     * Callbacks used when reading a feed that contains Member entries.
     */
    var MemberFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.CommunityFeedXPath
            });
        }
    };

    /*
     * Callbacks used when reading a feed that contains Invite entries.
     */
    var InviteFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.CommunityFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            return new Invite({
                service : service,
                data : data,
                response: response
            });
        }
    };
    
    /*
     * Callbacks used when reading a feed that contains Event entries.
     */
    var EventFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.CommunityFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            return new Event({
                service : service,
                data : data,
                response: response
            });
        }
    };
    
    /*
     * Callbacks used when reading an entry that contains a Community.
     */
    var CommunityCallbacks = {
        createEntity : function(service,data,response) {
            return new Community({
                service : service,
                data : data,
                response: response
            });
        }
    };
    
    /*
     * Callbacks used when reading an feed that contains community forum topics.
     */
    var ForumTopicFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.CommunityFeedXPath
            });
        },
        createEntity : function(service, data, response) {
        	var forumService = service.getForumService();
        	var forumTopic = forumService.newForumTopic({});
        	forumTopic.setData(data, response);
            return forumTopic;
        }
    };

    /**
     * CommunityService class.
     * 
     * @class CommunityService
     * @namespace sbt.connections
     */
    var CommunityService = declare(BaseService, {
    	
    	forumService : null,
    	
    	contextRootMap: {
            communities: "communities"
        },

        /**
         * Constructor for CommunityService
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        	if (!this.endpoint) {
                this.endpoint = config.findEndpoint(this.getDefaultEndpointName());
            }
        },

        /**
         * Return the default endpoint name if client did not specify one.
         * @returns {String}
         */
        getDefaultEndpointName : function() {
            return "connections";
        },
        
        /**
         * Return a ForumService instance
         * @returns {ForumService}
         */
        getForumService : function() {
        	if (!this.forumService) {
        		this.forumService = new ForumService();
        		this.forumService.endpoint = this.endpoint;
        	}
        	return this.forumService;
        },
        
        /**
         * Get the All Communities feed to see a list of all public communities to which the 
         * authenticated user has access or pass in parameters to search for communities that 
         * match a specific criteria.
         * 
         * @method getPublicCommunities
         * 
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of my communities. The
         * parameters must be exactly as they are supported by IBM
         * Connections like ps, sortBy etc.
         */
        getPublicCommunities : function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            
            return this.getEntities(consts.AtomCommunitiesAll, options, CommunityFeedCallbacks);
        },

        /**
         * Get the My Communities feed to see a list of the communities to which the 
         * authenticated user is a member or pass in parameters to search for a subset 
         * of those communities that match a specific criteria.
         * 
         * @method getMyCommunities
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of my communities. The
         * parameters must be exactly as they are supported by IBM
         * Connections like ps, sortBy etc.
         */
        getMyCommunities : function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            
            return this.getEntities(consts.AtomCommunitiesMy, options, CommunityFeedCallbacks);
        },

        /**
         * Retrieve the members feed to view a list of the members who belong 
         * to a given community.
         * 
         * @method getMembers
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of members of a
         * community. The parameters must be exactly as they are
         * supported by IBM Connections like ps, sortBy etc.
         */
        getMembers : function(communityUuid,args) {
            var promise = this._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }
            
            var requestArgs = lang.mixin({
                communityUuid : communityUuid
            }, args || {});
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            
            var callbacks = lang.mixin({
                createEntity : function(service,data,response) {
                    return new Member({
                        service : service,
                        communityUuid : communityUuid,
                        data : data,
                        response: response
                    });
                }
            }, MemberFeedCallbacks);
            
            return this.getEntities(consts.AtomCommunityMembers, options, callbacks);
        },

        /**
         * Retrieve the member entry to view a member who belongs to a given community.
         * 
         * @method getMembers
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of members of a
         * community. The parameters must be exactly as they are
         * supported by IBM Connections like ps, sortBy etc.
         */
        getMember : function(communityUuid,memberId,args) {
            var member = new Member({
                service : this,
                communityUuid : communityUuid
            });
            
            if (this.isEmail(memberId)) {
                member.setEmail(memberId);
            } else {
                member.setUserid(memberId);
            }
            
            return member.load(args);
        },
        
        /**
         * Get the Events for a community. See {{#crossLink "CommunityConstants/AtomCommunityEvents:attribute"}}{{/crossLink}} for a complete listing of parameters.
         * 
         * These results do not include all details of the event, such as content. However summaries are available.
         * 
         * @param communityId The uuid of the Community.
         * @param startDate Include events that end after this date.
         * @param endDate Include events that end before this date.
         * @param args url parameters.
         * 
         * @returns
         */
        getCommunityEvents : function(communityUuid, startDate, endDate, args){
        	var promise = this._validateCommunityUuid(communityUuid) || this._validateDateTimes(startDate, endDate);
            if (promise) {
                return promise;
            }
            var requiredArgs = {
                calendarUuid : communityUuid
            };
            if(startDate){
                lang.mixin(requiredArgs, {
                    startDate : startDate
                });
            } 
            if(endDate){
                lang.mixin(requiredArgs, {
                    endDate : endDate
                });
            }
            
            args = lang.mixin(args, requiredArgs);
            
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
                
            return this.getEntities(consts.AtomCommunityEvents, options, EventFeedCallbacks);
        },
        
        /**
         * @deprecated Use CommunityService.getCommunityEvent instead
         */
        getEvent : function(eventInstUuid) {
        	return this.getCommunityEvent(eventInstUuid);
        },
        
        /**
         * Used to get the event with the given eventInstUuid, this will include all details of the event, 
         * including its content. 
         * 
         * @method getCommunityEvent
         * @param eventInstUuid id of the event to be returned
         * @returns
         */
        getCommunityEvent : function(eventInstUuid, requestArgs) {
            var event = new Event({
                service : this,
                _fields : { eventInstUuid : eventInstUuid }
            });
            return event.load(requestArgs);
        },

        /**
         * Get a list of the outstanding community invitations of the currently authenticated 
         * user or provide parameters to search for a subset of those invitations.
         * 
         * @method getMyInvites
         * @param {Object} [args] 
         */
        getMyInvites : function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            
            return this.getEntities(consts.AtomCommunityInvitesMy, options, InviteFeedCallbacks);
        },      

        /**
         * Get a list of the outstanding community invitations for the specified community. 
         * The currently authenticated user must be an owner of the community.
         * 
         * @method getAllInvites
         * @param communityUuid
         * @param {Object} [args]
         */
        getAllInvites : function(communityUuid, args) {
            var promise = this._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }

            var requestArgs = lang.mixin({
                communityUuid : communityUuid
            }, args || {});
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            
            return this.getEntities(consts.AtomCommunityInvites, options, InviteFeedCallbacks);
        },      

        /**
         * Get a list of subcommunities associated with a community.
         * 
         * @method getSubCommunities
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of members of a
         * community. The parameters must be exactly as they are
         * supported by IBM Connections like ps, sortBy etc.
         */
        getSubCommunities : function(communityUuid,args) {
            var promise = this._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }

            var requestArgs = lang.mixin({
                communityUuid : communityUuid
            }, args || {});
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            
            return this.getEntities(consts.AtomCommunitySubCommunities, options, CommunityFeedCallbacks);
        },

        /**
         * Get a list for forum topics for th specified community.
         * 
         * @method getForumTopics
         * @param communityUuid
         * @param args
         * @returns
         */
        getForumTopics: function(communityUuid, args) {
            var promise = this._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }

            var requestArgs = lang.mixin(
            	{ communityUuid : communityUuid }, args || {});
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            
            return this.getEntities(consts.AtomCommunityForumTopics, options, ForumTopicFeedCallbacks);
        },
        
        /**
         * Create a Community object with the specified data.
         * 
         * @method newCommunity
         * @param {Object} args Object containing the fields for the 
         * new Community 
         */
        newCommunity : function(args) {
            return this._toCommunity(args);
        },
        
        /**
         * Create a Member object with the specified data.
         * 
         * @method newMember
         * @param {Object} args Object containing the fields for the 
         * new Member 
         */
        newMember : function(args) {
            return this._toMember(args);
        },
        
        /**
         * Create a Invite object with the specified data.
         * 
         * @method newInvite
         * @param {String} communityUuid
         * @param {Object} args Object containing the fields for the 
         * new Invite 
         */
        newInvite : function(communityUuid,args) {
            return this._toInvite(communityUuid,args);
        },
        
        /**
         * Retrieve a community entry, use the edit link for the community entry 
         * which can be found in the my communities feed.
         * 
         * @method getCommunity
         * @param {String } communityUuid
         * @param {Object} args Object containing the query arguments to be 
         * sent (defined in IBM Connections Communities REST API) 
         */
        getCommunity : function(communityUuid, args) {
            var community = new Community({
                service : this,
                _fields : { communityUuid : communityUuid }
            });
            return community.load(args);
        },

        /**
         * Create a community by sending an Atom entry document containing the 
         * new community to the My Communities resource.
         * 
         * @method createCommunity
         * @param {Object} community Community object which denotes the community to be created.
         * @param {Object} [args] Argument object
         */
        createCommunity : function(communityOrJson,args) {
            var community = this._toCommunity(communityOrJson);
            var promise = this._validateCommunity(community, false, args);
            if (promise) {
                return promise;
            }

            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
                var communityUuid = this.getLocationParameter(response, "communityUuid");
                community.setCommunityUuid(communityUuid);
                return community;
            };

            var options = {
                method : "POST",
                query : args || {},
                headers : consts.AtomXmlHeaders,
                data : community.createPostData()
            };
            
            return this.updateEntity(consts.AtomCommunitiesMy, options, callbacks, args);
        },

        /**
         * Update a community by sending a replacement community entry document in Atom format 
         * to the existing community's edit web address.
         * All existing community entry information will be replaced with the new data. To avoid 
         * deleting all existing data, retrieve any data you want to retain first, and send it back 
         * with this request. For example, if you want to add a new tag to a community entry, retrieve 
         * the existing tags, and send them all back with the new tag in the update request.
         * 
         * @method updateCommunity
         * @param {Object} community Community object
         * @param {Object} [args] Argument object
         */
        updateCommunity : function(communityOrJson,args) {
            var community = this._toCommunity(communityOrJson);
            var promise = this._validateCommunity(community, true, args);
            if (promise) {
                return promise;
            }
            
            var callbacks = {};
            callbacks.createEntity = function(service, data, response) {
            	// preserve the communityUuid
            	var communityUuid = community.getCommunityUuid();
            	if (data) {
                	community.setData(data, response);
            	}
            	community.setCommunityUuid(communityUuid);
                return community;
            };

            var requestArgs = lang.mixin({
                communityUuid : community.getCommunityUuid()
            }, args || {});
            
            var options = {
                method : "PUT",
                query : requestArgs,
                headers : consts.AtomXmlHeaders,
                data : community.createPostData()
            };
            
            return this.updateEntity(consts.AtomCommunityInstance, options, callbacks, args);
        },

        /**
         * Delete a community, use the HTTP DELETE method.
         * Only the owner of a community can delete it. Deleted communities cannot be restored
         * 
         * @method deleteCommunity
         * @param {String/Object} community id of the community or the community object (of the community to be deleted)
         * @param {Object} [args] Argument object
         */
        deleteCommunity : function(communityUuid,args) {
            var promise = this._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }            
           
            var requestArgs = lang.mixin({
                communityUuid : communityUuid
            }, args || {});
            
            var options = {
                method : "DELETE",
                query : requestArgs,
                handleAs : "text"
            };
            
            return this.deleteEntity(consts.AtomCommunityInstance, options, communityUuid);
        },

        /**
         * Add member to a community
         * 
         * @method addMember
         * @param {String/Object} community id of the community or the community object.
         * @param {Object} member member object representing the member of the community to be added
         * @param {Object} [args] Argument object
         */
        addMember : function(communityUuid,memberOrId,args) {
            var promise = this._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }
            var member = this._toMember(memberOrId);
            promise = this._validateMember(member);
            if (promise) {
                return promise;
            }

            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
                var userid = this.getLocationParameter(response, "userid");
                member.setUserid(userid);
                member.communityUuid = communityUuid;
                return member;
            };

            var requestArgs = lang.mixin({
                communityUuid : communityUuid
            }, args || {});
            var options = {
                method : "POST",
                query : requestArgs,
                headers : consts.AtomXmlHeaders,
                data : member.createPostData()
            };
            
            return this.updateEntity(consts.AtomCommunityMembers, options, callbacks, args);
        },
        
		/**
		 * Updates a member in the access control list for an application, sends a replacement member entry document in Atom format to the existing ACL
		 * node's edit web address.
		 * @method updateMember
		 * @param {String} activityUuid
		 * @param {Object} memberOrJson
		 */
		updateMember : function(communityUuid, memberOrJson, args) {
			var promise = this.validateField("communityUuid", communityUuid);
			if (promise) {
				return promise;
			}
			var member = this._toMember(memberOrJson);
			promise = this._validateMember(member, true, true);
			if (promise) {
				return promise;
			}

			var requestArgs = {
	                communityUuid : communityUuid
	        };
	        var key = member.getEmail() ? "email" : "userid";
	        var value = member.getEmail() ? member.getEmail() : member.getUserid();
	        requestArgs[key] = value;
	        requestArgs = lang.mixin(requestArgs, args || {});

			var options = {
				method : "PUT",
				headers : consts.AtomXmlHeaders,
				query : requestArgs,
				data : member.createPostData()
			};

			var callbacks = {
				createEntity : function(service, data, response) {
					return response;
				}
			};

			return this.updateEntity(consts.AtomCommunityMembers, options, callbacks);

		},

        /**
         * Remove member of a community
         * 
         * @method
         * @param {String/Object} community id of the community or the community object.
         * @param {String} memberId id of the member
         * @param {Object} [args] Argument object
         */
        removeMember : function(communityUuid,memberId,args) {
            var promise = this._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }
            var member = this._toMember(memberId);
            promise = this._validateMember(member);
            if (promise) {
                return promise;
            }

            var requestArgs = {
                communityUuid : communityUuid
            };
            var key = member.getEmail() ? "email" : "userid";
            var value = member.getEmail() ? member.getEmail() : member.getUserid();
            requestArgs[key] = value;
            requestArgs = lang.mixin(requestArgs, args || {});
            var options = {
                method : "DELETE",
                query : requestArgs,
                handleAs : "text"
            };
            
            return this.deleteEntity(consts.AtomCommunityMembers, options, value);
        },
        
        /**
         * Retrieve a community invite.
         * 
         * @method getInvite
         * @param {String} communityUuid
         * @param (String} userid
         */
        getInvite : function(communityUuid, userid) {
            var invite = new Invite({
                service : this,
                _fields : { communityUuid : communityUuid, userid : userid }
            });
            return invite.load();
        },

        /**
         * Create an invite to be a member of a community.
         * 
         * @method createInvite
         * @param {Object} inviteOrJson
         * @param {Object} [args] Argument object
         */
        createInvite: function(inviteOrJson, args) {
            var invite = this._toInvite(inviteOrJson);
            var promise = this._validateInvite(invite, true);
            if (promise) {
                return promise;
            }

            var callbacks = {};
            callbacks.createEntity = function(service, data, response) {
                invite.setData(data, response);
                return invite;
            };

            var requestArgs = lang.mixin({
                communityUuid : invite.getCommunityUuid()
            }, args || {});
            
            var options = {
                method : "POST",
                query : requestArgs,
                headers : consts.AtomXmlHeaders,
                data : invite.createPostData()
            };
            
            return this.updateEntity(consts.AtomCommunityInvites, options, callbacks, args);
        },
           
        /**
         * Decline or revoke an invite to be a member of a community
         * 
         * @method removeInvite
         * @param {Object} inviteOrJson
         * @param {Object} [args] Argument object
         */
        removeInvite: function(inviteOrJson, args) {
            var invite = this._toInvite(inviteOrJson);
            var promise = this._validateInvite(invite, true);
            if (promise) {
                return promise;
            }

            var requestArgs = lang.mixin({
                communityUuid : invite.getCommunityUuid(),
                userid : invite.getInviteeUuid()
            }, args || {});
            
            var options = {
                method : "DELETE",
                query : requestArgs,
                handleAs : "text"
            };
            var entityId = invite.getCommunityUuid() + "-" + invite.getInviteeUuid();
            return this.deleteEntity(consts.AtomCommunityInvites, options, entityId);
        },
        
        /**
         * Accept an invite to be a member of a community.
         * 
         * @method acceptInvite
         * @param {Object} inviteOrJson.
         * @param {Object} [args] Argument object
         */
        acceptInvite: function(inviteOrJson, args) {
            var invite = this._toInvite(inviteOrJson);
            var promise = this._validateInvite(invite, true);
            if (promise) {
                return promise;
            }

            var requestArgs = lang.mixin({
                communityUuid : invite.getCommunityUuid()
            }, args || {});
            
            var options = {
            	method : "POST",
        		query : requestArgs,
                headers : consts.AtomXmlHeaders,
                data : invite.createPostData()
            };
            
            // return the community id for the community whose invite is accepted in the argument of the success promise.
            var callbacks = {}; 
            callbacks.createEntity = function(service, data, response) { 
            	invite.setData(data, response);
                return invite;
            };
            
            return this.updateEntity(consts.AtomCommunityMembers, options, callbacks, args);
        },
        
        /**
         * Create a forum topc by sending an Atom entry document containing the 
         * new forum to the forum replies resource.
         * 
         * @method createForumTopic
         * @param {String} communityUuid Community UUID of the community for this forum topic
         * @param {Object} forumTopic Forum topic object which denotes the forum topic to be created.
         * @param {Object} [args] Argument object
         */
        createForumTopic : function(communityUuid, topicOrJson, args) {
            var promise = this._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }
        	
        	var forumService = this.getForumService();
            var forumTopic = forumService.newForumTopic(topicOrJson);
            var promise = forumService._validateForumTopic(forumTopic, false, args);
            if (promise) {
                return promise;
            }

            var callbacks = {};
            callbacks.createEntity = function(service, data, response) {
                var topicUuid = this.getLocationParameter(response, "topicUuid");
                forumTopic.setTopicUuid(topicUuid);
                forumTopic.setData(data, response);
                return forumTopic;
            };

            var requestArgs = lang.mixin(
                	{ communityUuid : communityUuid }, args || {});
            
            var options = {
                method : "POST",
                query : requestArgs,
                headers : consts.AtomXmlHeaders,
                data : forumTopic.createPostData()
            };
            
            return this.updateEntity(consts.AtomCommunityForumTopics, options, callbacks, args);
        },
                
        /**
		 * Updates the Logo picture of a community
		 * @method updateCommunityLogo
		 * @param {Object} fileControlOrId The Id of html control or the html control
		 * @param {String} communityUuid the Uuid of community
		 */
		updateCommunityLogo : function(fileControlOrId, communityUuid) {
			var promise = this.validateField("File Control Or Id", fileControlOrId);
			if (promise) {
				return promise;
			}
			promise = this.validateHTML5FileSupport();
			if (promise) {
				return promise;
			}
			promise = this.validateField("CommunityUuid", communityUuid);
			if (promise) {
				return promise;
			}

			var files = null;
			var fileControl = this.getFileControl(fileControlOrId);
			if(!fileControl){
				return this.createBadRequestPromise("File Control or ID is required");
			}
			filePath = fileControl.value;
			files = fileControl.files;

			if (files.length != 1) {
				return this.createBadRequestPromise("Only one file needs to be provided to this API");
			}

			var file = files[0];
			var formData = new FormData();
			formData.append("file", file);
			var requestArgs = {
				"communityUuid" : communityUuid
			};
			var url = this.constructUrl(consts.AtomUpdateCommunityLogo, null, {
				endpointName : this.endpoint.proxyPath,
				fileName : encodeURIComponent(file.name)
			});
			if (this.endpoint.proxy) {
                url = config.Properties.serviceUrl + url;
            } else {
            	return this.createBadRequestPromise("File Proxy is required to run this API");
            }
					
			var headers = {
				"Content-Type" : false,
				"Process-Data" : false //processData = false is reaquired by jquery
			};
			var options = {
				method : "PUT",
				headers : headers,
				query : requestArgs,
				data : formData
			};
			var callbacks = {
				createEntity : function(service, data, response) {
					return data; // Since this API does not return any response in case of success, returning empty data
				}
			};

			return this.updateEntity(url, options, callbacks);
		},

		//
		// Internals
		//
       
        /*
         * Return a Community instance from Community or JSON or String. Throws
         * an error if the argument was neither.
         */
        _toCommunity : function(communityOrJsonOrString) {
            if (communityOrJsonOrString instanceof Community) {
                return communityOrJsonOrString;
            } else {
                if (lang.isString(communityOrJsonOrString)) {
                    communityOrJsonOrString = {
                        communityUuid : communityOrJsonOrString
                    };
                }
                return new Community({
                    service : this,
                    _fields : lang.mixin({}, communityOrJsonOrString)
                });
            }
        },

        /*
         * Return as Invite instance from Invite or JSON or String. Throws
         * an error if the argument was neither.
         */
        _toInvite : function(inviteOrJsonOrString, args){
            if (inviteOrJsonOrString instanceof Invite) {
                return inviteOrJsonOrString;
            } else {
                if (lang.isString(inviteOrJsonOrString)) {
                	inviteOrJsonOrString = {
                        communityUuid : inviteOrJsonOrString
                    };
                }
                return new Invite({
                    service : this,
                    _fields : lang.mixin({}, inviteOrJsonOrString)
                });
            }
        },

        /*
         * Return a Community UUID from Community or communityUuid. Throws an
         * error if the argument was neither or is invalid.
         */
        _toCommunityUuid : function(communityOrUuid) {
            var communityUuid = null;
            if (communityOrUuid) {
                if (lang.isString(communityOrUuid)) {
                    communityUuid = communityOrUuid;
                } else if (communityOrUuid instanceof Community) {
                    communityUuid = communityOrUuid.getCommunityUuid();
                }
            }

            return communityUuid;
        },
        
        /*
         * Return a Community Member from Member or memberId. Throws an error if
         * the argument was neither or is invalid.
         */
        _toMember : function(idOrJson) {
            if (idOrJson) {
                if (idOrJson instanceof Member) {
                    return idOrJson;
                }
                var member = new Member({
                    service : this
                });
                if (lang.isString(idOrJson)) {
                    if (this.isEmail(idOrJson)) {
                        member.setEmail(idOrJson);
                    } else {
                        member.setUserid(idOrJson);
                    }
                } else {
                	if(idOrJson.id && !idOrJson.userid && !idOrJson.email){
                		this.isEmail(idOrJson.id) ? idOrJson.email = idOrJson.id : idOrJson.userid = idOrJson.id;
                		delete idOrJson.id;
                	}
                    member._fields = lang.mixin({}, idOrJson);
                }
                return member;
            }
        },

        /*
         * Validate a community UUID, and return a Promise if invalid.
         */
        _validateCommunityUuid : function(communityUuid) {
            if (!communityUuid || communityUuid.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected communityUuid.");
            }
        },
        
        /*
         * Validate a userid, and return a Promise if invalid.
         */
        _validateUserid : function(userid) {
            if (!userid || userid.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected userid.");
            }
        },
        
        /*
         * Validate that the date-time is not empty, return a promise if invalid
         */
        _validateDateTimes : function(startDate, endDate){
            if ((!startDate || startDate.length === 0) && (!endDate || endDate.length === 0)) {
                return this.createBadRequestPromise("Invalid date arguments, expected either a startDate, endDate or both as parameters.");
            }
        },

        /*
         * Validate contributor id
         */
        _validateContributorId : function(contributorId) {
        	if (!contributorId || contributorId.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected contributorId.");
            }
        },

        /*
         * Validate a community, and return a Promise if invalid.
         */
        _validateCommunity : function(community,checkUuid) {
            if (!community || !community.getTitle()) {
                return this.createBadRequestPromise("Invalid argument, community with title must be specified.");
            }
            if (checkUuid && !community.getCommunityUuid()) {
                return this.createBadRequestPromise("Invalid argument, community with UUID must be specified.");
            }
        },

        /*
         * Validate an invite, and return a Promise if invalid.
         */
        _validateInvite : function(invite, checkCommunityUuid) {
            if (!invite || (!invite.getEmail() && !invite.getUserid() && !invite.getInviteeUuid())) {
                return this.createBadRequestPromise("Invalid argument, invite with email or userid or invitee must be specified.");
            }
            if (checkCommunityUuid && !invite.getCommunityUuid()) {
                return this.createBadRequestPromise("Invalid argument, invite with community UUID must be specified.");
            }
        },

        /*
         * Validate a member, and return a Promise if invalid.
         */
        _validateMember : function(member) {
            if (!member || (!member.getUserid() && !member.getEmail())) {
                return this.createBadRequestPromise("Invalid argument, member with userid or email must be specified.");
            }
        },
        
        /*
         * Validate an event inst uuid, and return a Promise if invalid.
         */
        _validateEventInstUuid : function(eventInstUuid) {
            if (!eventInstUuid || eventInstUuid.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected event inst uuid.");
            }
        }

    });
    return CommunityService;
});