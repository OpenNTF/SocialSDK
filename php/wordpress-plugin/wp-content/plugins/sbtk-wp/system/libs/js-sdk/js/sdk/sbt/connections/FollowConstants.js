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
         * Activities Source
         * 
         * @property ActivitiesSource
         * @type String
         * @for sbt.connections.FollowedResource
         */
        ActivitiesSource : "activities",
        
        /**
         * Activity Resource Type
         * 
         * @property ActivityResourceType
         * @type String
         * @for sbt.connections.FollowedResource
         */
        ActivityResourceType : "activity",
        
        /**
         * Blogs Source
         * 
         * @property BlogsSource
         * @type String
         * @for sbt.connections.FollowedResource
         */
        BlogsSource : "blogs",
        
        /**
         * Blog Resource Type
         * 
         * @property BlogResourceType
         * @type String
         * @for sbt.connections.FollowedResource
         */
        BlogResourceType : "blog",
        
        /**
         * Communities Source
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
        
        /**
         * Files Source
         * 
         * @property FilesSource
         * @type String
         * @for sbt.connections.FollowedResource
         */
        FilesSource : "files",

        /**
         * File Resource Type
         * 
         * @property FileResourceType
         * @type String
         * @for sbt.connections.FollowedResource
         */
        FileResourceType : "file",
        
        /**
         * FileFolder Resource Type
         * 
         * @property FileFolderResourceType
         * @type String
         * @for sbt.connections.FollowedResource
         */
        FileFolderResourceType : "file_folder",
        
        /**
         * Forums Source
         * 
         * @property ForumsSource
         * @type String
         * @for sbt.connections.FollowedResource
         */
        ForumsSource : "forums",

        /**
         * Forum Resource Type
         * 
         * @property ForumResourceType
         * @type String
         * @for sbt.connections.FollowedResource
         */
        ForumResourceType : "forum",
        
        /**
         * ForumTopic Resource Type
         * 
         * @property ForumTopicResourceType
         * @type String
         * @for sbt.connections.FollowedResource
         */
        ForumTopicResourceType : "forum_topic",
        
        /**
         * Profile Source
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
         * Wikis Source
         * 
         * @property WikisSource
         * @type String
         * @for sbt.connections.FollowedResource
         */
        WikisSource : "wikis",

        /**
         * Wiki Resource Type
         * 
         * @property WikiResourceType
         * @type String
         * @for sbt.connections.FollowedResource
         */
        WikiResourceType : "wiki",
        
        /**
         * WikiPage Resource Type
         * 
         * @property WikiPageResourceType
         * @type String
         * @for sbt.connections.FollowedResource
         */
        WikiPageResourceType : "wiki_page",
        
        /**
         * Tags Source
         * 
         * @property TagsSource
         * @type String
         * @for sbt.connections.FollowedResource
         */
        TagsSource : "tags",

        /**
         * Tag Resource Type
         * 
         * @property TagResourceType
         * @type String
         * @for sbt.connections.FollowedResource
         */
        TagResourceType : "tag",
        
        
        /**
		 * Get the followed resources feed
		 * 
		 * @method getFollowedResources
		 * @param {String} source String specifying the resource. Options are:
		 *
		 *	activities
		 *	blogs
		 *	communities
		 *	files
		 *	forums
		 *	profiles
		 *	wikis
		 *	tags
		 *
		 * @param {String} resourceType String representing the resource type. Options are:
		 * 
		 * If source=activities
		 * 	   activity
		 * 
		 * If source=blogs
		 *     blog
		 *     
		 * If source=communities
		 *     community
		 *     
		 * If source=files
		 *     file
		 *     file_folder
		 *      
		 * 
		 * If source=forums
		 *     forum
		 *     forum_topic
		 *      
		 * 
		 * If source=profiles
		 *     profile
		 *     
		 * If source=wikis
		 *     wiki
		 *     wiki_page
		 *      
		 * 
		 * If source=tags
		 *     tag
		 * 
		 * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of members of a
         * community. The parameters must be exactly as they are
         * supported by IBM Connections like ps, sortBy etc.
		 */
    	FollowedResourceFeedXPath : conn.ConnectionsFeedXPath,
    	
        /**
         * XPath expressions to be used when reading a followed resource entry
         */
    	FollowedResourceXPath : lang.mixin({
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