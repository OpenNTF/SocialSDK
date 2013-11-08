/*
 * � Copyright IBM Corp. 2012,2013
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
 * Social Business Toolkit SDK. Definition of constants for CommunityService.
 * 
 * @module sbt.connections.CommunityConstants
 */
define([ "../lang", "./ConnectionsConstants" ], function(lang,conn) {

    return lang.mixin({
        
        /**
         * Public community
         * 
         * @property Public
         * @type String
         * @for sbt.connections.Community
         */
        Public : "public",

        /**
         * Moderated community
         * 
         * @property Moderated
         * @type String
         * @for sbt.connections.Community
         */
        Moderated : "publicInviteOnly",

        /**
         * Restricted community
         * 
         * @property Restricted
         * @type String
         * @for sbt.connections.Community
         */
        Restricted : "private",

        /**
         * Community owner
         * 
         * @property Owner
         * @type String
         * @for sbt.connections.Member
         */
        Owner : "owner",
        
        /**
         * Community member
         * 
         * @property Member
         * @type String
         * @for sbt.connections.Member
         */
        Member : "member",
        
        /**
         * Namespaces to be used when reading the Communities ATOM entry or feed
         */
        CommunityNamespaces : {
			a : "http://www.w3.org/2005/Atom",
			app : "http://www.w3.org/2007/app",
			snx : "http://www.ibm.com/xmlns/prod/sn"
		},
        
        /**
         * XPath expressions used when parsing a Connections Communities ATOM feed
         * 
         * @property CommunityFeedXPath
         * @type Object
         * @for sbt.connections.CommunityService
         */
        CommunityFeedXPath : conn.ConnectionsFeedXPath,

        /**
         * XPath expressions to be used when reading a Community Entry
         * 
         * @property CommunityXPath
         * @type Object
         * @for sbt.connections.CommunityService
         */
        CommunityXPath : lang.mixin({}, conn.AtomEntryXPath, {
            communityUuid : "a:id",
            communityTheme : "snx:communityTheme",
            logoUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/logo']/@href",
            tags : "a:category[not(@scheme)]/@term",
            memberCount : "snx:membercount",
            communityType : "snx:communityType",
            isExternal : "snx:isExternal"
        }),
        
        /**
         * XPath expressions to be used when reading a Community Member Entry
         * 
         * @property MemberXPath
         * @type Object
         * @for sbt.connections.CommunityService
         */
        MemberXPath : lang.mixin({}, conn.AtomEntryXPath, {
        	id : "a:contributor/snx:userid",
            uid : "a:contributor/snx:userid",
            role : "snx:role"
        }),

        /**
         * XPath expressions to be used when reading a Community Invite Entry
         * 
         * @property InviteXPath
         * @type Object
         * @for sbt.connections.CommunityService
         */
        InviteXPath : lang.mixin({}, conn.AtomEntryXPath, {
            inviteUuid : "a:id",
            communityUuid : "snx:communityUuid",
            communityUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/community']/@href"
        }),
        
        /**
         * XPath expressions to be used when reading a Community Event Entry
         * 
         * @property EventXPath
         * @type Object
         * @for sbt.connections.CommunityService
         */
        EventXPath : lang.mixin({}, conn.AtomEntryXPath, {
            eventUuid : "snx:eventUuid",
            eventInstUuid : "snx:eventInstUuid",
            location : "snx:location",
            communityLink : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/container']/@href",
            eventAtomInstances : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/calendar/event/instances']/@href",
            eventAtomAttendees : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/calendar/event/attend']/@href",
            eventAtomFollowers : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/calendar/event/follow']/@href",
            frequency : "snx:recurrence/@frequency",
            interval : "snx:recurrence/@interval",
            until : "snx:recurrence/snx:until",
            allDay : "snx:recurrence/snx:allday",
            startDate : "snx:recurrence/snx:startDate",
            endDate : "snx:recurrence/snx:endDate",
            byDay : "snx:recurrence/snx:byDay"
        }),

        /**
         * A feed of all public communities.
         *  
         * Get the All Communities feed to see a list of all public communities to which the authenticated user has access or pass in parameters to search for communities that match a specific criteria.
         * 
         * Supports: asc, email, ps, search, since, sortField, tag, userid
         * 
         * @property AtomCommunitiesAll
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunitiesAll : "/${communities}/service/atom/communities/all",

        /**
         * A feed of communities of which the authenticated user is a member.
         * 
         * Get the My Communities feed to see a list of the communities to which the authenticated user is a member or pass in parameters to search for a subset of those communities that match a specific criteria.
         * 
         * Supports: asc, email, ps, search, since, sortField, tag, userid
         * 
         * @property AtomCommunitiesMy
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunitiesMy : "/${communities}/service/atom/communities/my",
        
        /**
         * A feed of invitations.
         * 
         * Get a list of the outstanding community invitations of the currently authenticated user or provide parameters to search for a subset of those invitations.
         * 
         * Supports: asc, ps, search, since, sortField
         * 
         * @property AtomCommunityInvitesMy
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunityInvitesMy : "/${communities}/service/atom/community/invites/my",
        
        /**
         * URL to delete/create Community Invites
         * 
         * @property AtomCommunityInvites
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunityInvites : "${communities}/service/atom/community/invites",
        
        /**
         * A feed of subcommunities.
         * 
         * Get a list of subcommunities associated with a community.
         * 
         * Supports: asc, page, ps, since, sortBy, sortOrder, sortField
         * 
         * @property AtomCommunitySubCommunities
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunitySubCommunities : "${communities}/service/atom/community/subcommunities",
            
        /**
         * A feed of members.
         * 
         * Retrieve the members feed to view a list of the members who belong to a given community.
         * 
         * Supports: asc, email, page, ps, role, since, sortField, userid
         * 
         * @property AtomCommunityMembers
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunityMembers : "${communities}/service/atom/community/members",
        
        /**
         * A community entry.
         * 
         * @property AtomCommunityInstance
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunityInstance : "${communities}/service/atom/community/instance",
        
        /**
         * Get a feed that includes the topics in a specific community forum.
         * 
         * @property AtomCommunityForumTopics
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunityForumTopics : "/${communities}/service/atom/community/forum/topics",
        
        /**
         * Get a feed of a Community's bookmarks. Requires a url parameter of the form communityUuid=xxx
         * @property AtomCommunityBookmarks
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunityBookmarks : "/${communities}/service/atom/community/bookmarks",
        
        /**
         * Get a feed of a Community's events.
         * 
         * Required url parameters: 
         *   calendarUuid - The uuid of the community to get events from.
         *   
         *   startDate and/or endDate. At least one of these must be present. Format is any date-time that conforms to rfc3339. 
         *   startDate - Include events that end after this date.
         *   endDate - Include events that end before this date.
         *   
         * Optional Url parameters
         *   page - Page number, specifies the page to be returned. Default value is page 1.
         *   ps - Page size. Number of entries to return per page. Defaule value is 10, max is 100.
         *   tags - Tag filter, only return events with these tags. Multiple tags are separated using +, e.g. tag1+tag2
         *   
         * @property AtomCommunityEvents
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunityEvents : "/${communities}/calendar/atom/calendar/event",
        
        /**
         * Get full atom event.
         * 
         * Required url parameters: 
         *   eventInstUuid - The uuid of the event, gotten from the AtomCommunityEvents feed.
         *   
         * @property AtomCommunityEvent
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunityEvent : "/${communities}/calendar/atom/calendar/event",
        
        /**
         * Obtain a full representation of the invitations as an Atom entry document.
         * 
         * @property AtomCommunityInvites
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunityInvites : "/${communities}/service/atom/community/invites"
        
    }, conn);
});