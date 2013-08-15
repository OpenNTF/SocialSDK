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
 * Social Business Toolkit SDK. Definition of constants for ProfileService.
 */
define([ "../lang" ], function(lang) {

    return lang.mixin( {} , {
        /**
         * Default size for the profile cache
         */
        DefaultCacheSize : 10,
        
		/**
         * Retrieve the profile entry of the logged in user.
         */
        GetProfile : "/lotuslive-shindig-server/social/rest/people/@me/@self",
        
        /**
         * Retrieve the logged in user's profile connections.
         */
        GetMyConnections : "/lotuslive-shindig-server/social/rest/people/@me/@friends",
        
        /**
         * Retrieve a profile's user Identity.
         */
        GetUserIdentity : "/manage/oauth/getUserIdentity",
        
        /**
         * Retrieve a Contact's Profile.
         */
        GetContactByGUID : "/lotuslive-shindig-server/social/rest/people/lotuslive:contact:{idToBeReplaced}/@self",
        
        /**
         * Retrieve a profiles entry using GUID.
         */
        GetProfileByGUID : "/lotuslive-shindig-server/social/rest/people/lotuslive:user:{idToBeReplaced}/@self",
        
        /**
         * Retrieve the logged in user's profile contacts.
         */
        GetMyContacts : "/lotuslive-shindig-server/social/rest/people/@me/@all",
        
        /**
         * JsonPath expressions to be used when reading a Profile Entry
         */
        ProfileJPath : { 
        	thumbnailUrl : "$..photo",
        	address : "$..address",
        	department : "$..name",
        	title : "$..jobtitle",
        	phoneNumbers : "$..telephone",
        	about : "$..aboutMe",
        	id : "$..id",
        	displayName : "$..displayName",
        	emailAddress : "$..emailAddress",
        	profileUrl : "$..profileUrl",
        	country : "$..country",
        	orgId : "$..orgId",
        	global : "$..",
        	firstElement : "$[0]",
        	totalResults : "totalResults",
        	startIndex : "startIndex",
        	itemsPerPage : "itemsPerPage"
        }
    });
});