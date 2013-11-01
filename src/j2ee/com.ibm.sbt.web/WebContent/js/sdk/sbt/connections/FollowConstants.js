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
 * Social Business Toolkit SDK.
 * Definition of constants for FollowService.
 */
define([ "../lang", "./ConnectionsConstants" ], function(lang,conn) {

    return lang.mixin(conn, {
    	
    	/**
         * Profile resource
         * 
         * @property ProfilesSource
         * @type String
         * @for sbt.connections.FollowedResource
         */
        ProfilesSource : "profiles",

        /**
         * Profile Resource Type
         * 
         * @property ProfilesResourceType
         * @type String
         * @for sbt.connections.FollowedResource
         */
        ProfilesResourceType : "profile",
        
        /**
         * Community resource
         * 
         * @property CommunitiesSource
         * @type String
         * @for sbt.connections.FollowedResource
         */
        CommunitiesSource : "communities",

        /**
         * Community Resource Type
         * 
         * @property CommunitiesResourceType
         * @type String
         * @for sbt.connections.FollowedResource
         */
        CommunitiesResourceType : "community",
    	
    	FollowedResourceFeedXPath : conn.ConnectionsFeedXPath,
    	
    	FollowedResourceXPath : lang.mixin({
            id : "a:id",
            followedResourceUuid : "a:id",
            categoryType : "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/type']/@term",
            source : "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/source']/@term",
            resourceType : "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/resource-type']/@term",
            resourceId : "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/resource-id']/@term",
            relatedUrl : "a:link[@rel='related']/@href"
        }, conn.AtomEntryXPath),
        
        /**
         * Get, follow or stop following a resource. 
         */
        AtomFollowAPI : "/{service}/follow/atom/resources",
        
        /**
         * Get, follow or stop following a resource. 
         */
        AtomStopFollowAPI : "/{service}/follow/atom/resources/{resourceId}"
    });
});