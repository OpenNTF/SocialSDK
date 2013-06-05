/*
 * © Copyright IBM Corp. 2012,2013
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
 * Social Business Toolkit SDK. Definition of constants for ActivityStreamService.
 */
define([ "../lang", "./ConnectionsConstants" ], function(lang,conn) {

    return lang.mixin({
    	
		ASUser: {
			ME : "@me",
			PUBLIC : "@public",
			COMMUNITY : "urn:lsid:lconn.ibm.com:communities.community:",//Suffix Community with it id wherever this constant is used.
			UBLOG : "ublog"
		},
		ASGroup: {
			ALL : "@all",
			FOLLOWING : "@following",
			FRIENDS : "@friends",
			SELF : "@self",
			INVOLVED : "@involved",
			NOTESFORME : "@notesforme",
			NOTESFROMME : "@notesfromme",
			RESPONSES : "@responses",
			ACTIONS : "@actions",
			SAVED : "@saved"
		},
		ASApplication: {
			ALL : "@all",
			COMMUNITIES : "@communities",
			TAGS : "@tags",
			PEOPLE : "@people",
			STATUS : "@status",
			NOTESFORME : "@notesforme",
			NOTESFROMME : "@notesfromme",
			RESPONSES : "@responses",
			COMMENTS : "comments"
		},
		Verb: {
			 ACCEPT : "accept",
			 ACCESS : "access",
			 ACKNOWLEDGE : "acknowledge",
			 ADD : "add",
			 AGREE : "agree",
			 APPEND : "append",
			 APPROVE : "approve",
			 ARCHIVE : "archive",
			 ASSIGN : "assign",
			 AT : "at",
			 ATTACH : "attach",
			 ATTEND : "attend",
			 AUTHOR : "author",
			 AUTHORIZE : "authorize",
			 
			 BORROW	: "borrow",
			 BUILD : "build",
			 
			 CANCEL	: "cancel",
			 CLOSE : "close",
			 COMMENT : "comment",
			 COMPLETE : "complete",
			 CONFIRM : "confirm",
			 CONSUME : "consume",
			 CHECKIN : "checkin",
			 CREATE : "create",
			 
			 DELETE : "delete",
			 DELIVER : "deliver",
			 DENY : "deny",
			 DISAGREE : "disagree",
			 DISLIKE : "dislike",
			 
			 EXPERIENCE	: "experience",
			 
			 FAVORITE : "favorite",
			 FIND : "find",
			 FLAG_AS_INAPPROPRIATE : "flag-as-inappropriate",
			 FOLLOW	: "follow",
			 
			 GIVE : "give",
			 
			 HOST : "host",
			 
			 IGNORE	: "ignore",
			 INSERT	: "insert",
			 INSTALL : "install",
			 INTERACT : "interact",
			 INVITE	: "invite",
			 
			 JOIN : "join",
			 
			 LEAVE : "leave",
			 LIKE : "like",
			 LISTEN	: "listen",
			 LOSE : "lose",
			 
			 MAKE_FRIEND : "make-friend",
			 
			 OPEN : "open",
			 
			 POST : "post",
			 PLAY : "play",
			 PRESENT : "present",
			 PURCHASE : "purchase",
			 
			 QUALIFY : "qualify",
			 
			 READ : "read",
			 RECEIVE : "receive",
			 REJECT : "reject",
			 REMOVE : "remove",
			 REMOVE_FRIEND : "remove-friend",
			 REPLACE : "replace",
			 REQUEST : "request",
			 REQUEST_FRIEND : "request-friend",
			 RESOLVE : "resolve",
			 RETURN : "return",
			 RETRACT : "retract",
			 RSVP_MAYBE	: "rsvp-maybe",
			 RSVP_NO : "rsvp-no",
			 RSVP_YES : "rsvp-yes",
			 
			 SATISFY : "satisfy",
			 SAVE : "save",
			 SCHEDULE : "schedule",
			 SEARCH	: "search",
			 SELL : "sell",
			 SEND : "send",
			 SHARE : "share",
			 SPONSOR : "sponsor",
			 START : "start",
			 STOP_FOLLOWING : "stop-following",
			 SUBMIT : "submit",
			 
			 TAG : "tag",
			 TERMINATE : "terminate",
			 TIE : "tie",
			 
			 UNFAVORITE	: "unfavorite",
			 UNLIKE	: "unlike",
			 UNSAVE	: "unsave",
			 UNSATISFY : "unsatisfy",
			 UNSHARE : "unshare",
			 UPDATE : "update",
			 USE : "use",
			 
			 WATCH : "watch",
			 WIN : "win"
		},
		ActivityStreamUrls: {
			activityStreamBaseUrl : "/connections/opensocial/",
			activityStreamRestUrl : "/rest/activitystreams/",
			activityStreamUBlogRestUrl : "/rest/ublog/"
		},
		errorMessages:{
			args_object	: "argument passed to get stream should be an Object",
			required_communityid : "Community ID is required"
		}
    }, conn);
});