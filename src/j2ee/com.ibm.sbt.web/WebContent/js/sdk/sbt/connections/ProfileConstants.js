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
 * Social Business Toolkit SDK. Definition of constants for CommunityService.
 */
define([ "../lang", "./ConnectionsConstants" ], function(lang,conn) {

    return lang.mixin(conn, {
        
        /**
         * Default size for the profile cache
         */
        DefaultCacheSize : 10,
        
        /**
         * Fields used to populate the Address object
         */
        AddressFields : [ "streetAddress", "extendedAddress", "locality", "region", "postalCode", "countryName" ],

        /**
         * XPath expressions used when parsing a Connections Profiles ATOM feed
         */
        ProfileFeedXPath : conn.ConnectionsFeedXPath,

        /**
         * XPath expressions to be used when reading a Profile Entry
         */
        ProfileXPath : {
            // used by getEntityData
            entry : "/a:feed/a:entry",
            // used by getEntityId
            uid : "a:contributor/snx:userid",
            // used by getters
            id : "a:id",
            userid : "a:contributor/snx:userid",
            name : "a:contributor/a:name",
            email : "a:contributor/a:email",
            title : "a:title",
            updated : "a:updated",
            altEmail : "a:content/h:div/h:span/h:div[@class='x-groupwareMail']", // TODO do we need this? it's a dupe of groupwareMail
            photoUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/image']/@href", 
            fnUrl : "a:content/h:div/h:span/h:div/h:a[@class='fn url']/@href",
            soundUrl : "a:content/h:div/h:span/h:div/h:a[@class='sound url']/@href",
            jobTitle : "a:content/h:div/h:span/h:div[@class='title']",
            organizationUnit : "a:content/h:div/h:span/h:div[@class='org']/h:span[@class='organization-unit']",
            telephoneNumber : "a:content/h:div/h:span/h:div[@class='tel']/h:span[@class='value']",
            building : "a:content/h:div/h:span/h:div/h:span[@class='x-building']",
            floor : "a:content/h:div/h:span/h:div/h:span[@class='x-floor']",
            officeNumber : "a:content/h:div/h:span/h:div/h:span[@class='x-office-number']",
            streetAddress : "a:content/h:div/h:span/h:div/h:div[@class='street-address']",
            extendedAddress : "a:content/h:div/h:span/h:div/h:div[@class='extended-address x-streetAddress2']",
            locality : "a:content/h:div/h:span/h:div/h:span[@class='locality']",
            postalCode : "a:content/h:div/h:span/h:div/h:span[@class='postal-code']",
            region : "a:content/h:div/h:span/h:div/h:span[@class='region']",
            countryName : "a:content/h:div/h:span/h:div/h:div[@class='country-name']",
            summary : "a:summary",
            groupwareMail : "a:content/h:div/h:span/h:div[@class='x-groupwareMail']",
            blogUrl : "a:content/h:div/h:span/h:div/h:a[@class='x-blog-url url']/@href",
            role : "a:content/h:div/h:span/h:div[@class='role']",
            managerUid : "a:content/h:div/h:span/h:div[@class='x-manager-uid']",
            isManager : "a:content/h:div/h:span/h:div[@class='x-is-manager']"
        },
        
        /**
         * XPath expressions to be used when reading a Community Entry with VCard content
         */
        ProfileVCardXPath : {
            // used by getEntityData
            entry : "/a:feed/a:entry",
            // used by getEntityId
            uid : "a:contributor/snx:userid",
            // used by parseVCard
            vcard : "a:content",
            // used by getters
            id : "a:id",
            userid : "a:contributor/snx:userid",
            name : "a:contributor/a:name",
            email : "a:contributor/a:email",
            title : "a:title",
            updated : "a:updated",
            altEmail : "EMAIL;X_GROUPWARE_MAIL", // TODO do we need this? it's a dupe of groupwareMail
            photoUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/image']/@href",
            fnUrl : "URL",
            soundUrl : "SOUND;VALUE=URL",
            jobTitle : "TITLE",
            organizationUnit : "a:content/h:div/h:span/h:div[@class='org']/h:span[@class='organization-unit']",
            telephoneNumber : "TEL;WORK",
            building : "X_BUILDING",
            floor : "X_FLOOR",
            officeNumber : "X_OFFICE_NUMBER",
            workLocation : "ADR;WORK",
            streetAddress : "a:content/h:div/h:span/h:div/h:div[@class='street-address']",
            extendedAddress : "a:content/h:div/h:span/h:div/h:div[@class='extended-address x-streetAddress2']",
            locality : "a:content/h:div/h:span/h:div/h:span[@class='locality']",
            postalCode : "a:content/h:div/h:span/h:div/h:span[@class='postal-code']",
            region : "a:content/h:div/h:span/h:div/h:span[@class='region']",
            countryName : "a:content/h:div/h:span/h:div/h:div[@class='country-name']",
            summary : "a:summary",
            groupwareMail : "EMAIL;X_GROUPWARE_MAIL"
        },
        
        /**
         * XML elements to be used when creating a Profile Entry
         *                    
         **/        
        profileCreateAttributes : {
			guid : "com.ibm.snx_profiles.base.guid",
			email : "com.ibm.snx_profiles.base.email",
			uid : "com.ibm.snx_profiles.base.uid",
			distinguishedName : "com.ibm.snx_profiles.base.distinguishedName",
			displayName : "com.ibm.snx_profiles.base.displayName",
			givenNames : "com.ibm.snx_profiles.base.givenNames",
			surname : "com.ibm.snx_profiles.base.surname",
			userState :"com.ibm.snx_profiles.base.userState"
		},
        
		/**
         * Retrieve a profile entry.
         */
        AtomProfileDo : "/profiles{authType}/atom/profile.do",
        
        /**
         * Update a profile entry.
         */
        AtomProfileEntry : "/profiles{authType}/atom/profileEntry.do",
        
        /**
         * Retrieve a profiles connections.
         */
        AtomConnectionsDo : "/profiles{authType}/atom/connections.do",
        
        /**
         * Admin API - create a new profile.
         */
        AdminAtomProfileDo : "/profiles/admin/atom/profiles.do",
        
        /**
         * Admin API - delete a  profile.
         */
        AdminAtomProfileEntryDo : "/profiles/admin/atom/profileEntry.do",

    });
});