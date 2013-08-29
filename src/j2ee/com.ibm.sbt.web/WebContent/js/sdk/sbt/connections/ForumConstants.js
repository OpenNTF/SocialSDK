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
 * Definition of constants for ForumService.
 */
define([ "../lang", "./ConnectionsConstants" ], function(lang,conn) {

    return lang.mixin(conn, {
    	
        /**
         * XPath expressions used when parsing a Connections Forums ATOM feed
         */
    	ForumsFeedXPath : conn.ConnectionsFeedXPath,

        /**
         * XPath expressions to be used when reading an forum entry
         */
        ForumXPath : {
        	forumUuid : "a:id",
            moderation : "snx:moderation/@status",
            threadCount: "a:link[@rel='replies']/@thr:count",	
            communityUuid : "snx:communityUuid"
        },
        
        /**
         * XPath expressions to be used when reading an forum topic entry
         */
        ForumTopicXPath : {
        	topicUuid : "a:id",
        	forumUuid : "thr:in-reply-to/@ref",
            permissions : "snx:permissions",
            communityUuid : "snx:communityUuid"
        },
        
        /**
         * XPath expressions to be used when reading an forum reply entry
         */
        ForumReplyXPath : {
        	replyUuid : "a:id",
        	forumUuid : "thr:in-reply-to/@ref",
            permissions : "snx:permissions",
            communityUuid : "snx:communityUuid"
        },
        
        /**
		 * Edit link for a forum entry.  
         */
        AtomForum : "forums/atom/forum",
        
		/**
		 * Edit link for a forum topic entry.  
         */
        AtomTopic : "/forums/atom/topic",
        
		/**
		 * Edit link for a forum reply entry.  
         */
        AtomReply : "/forums/atom/reply",
        
		/**
		 * Get a feed that includes all stand-alone and community forums created in the enterprise. 
         */
        AtomForums : "/forums/atom/forums",
        
		/**
		 * Get a feed that includes all of the forums hosted by the Forums application. 
         */
        AtomForumsPublic : "/forums/atom/forums/public",
        
		/**
		 * Get a feed that includes forums created by the authenticated user or associated with communities to which the user belongs.  
         */
        AtomForumsMy : "/forums/atom/forums/my",
        
		/**
		 * Get a feed that includes the topics in a specific stand-alone forum.  
         */
        AtomTopics : "/forums/atom/topics",
        
        /**
         * Get a feed that includes the topics that the authenticated user created in stand-alone forums and in forums associated 
         * with communities to which the user belongs. 
         */
        AtomTopicsMy : "/forums/atom/topics/my",
        
        /**
         * Get a feed that includes all of the replies for a specific forum topic. 
         */
        AtomReplies : "/forums/atom/replies",
        
        /**
         * Get a category document that lists the tags that have been assigned to forums. 
         */
        AtomTagsForum : "/atom/tags/forums",
        
        /**
         * Get a category document that lists the tags that have been assigned to forum topics. 
         */
        AtomTagsTopics : "/atom/tags/topics"

    });
});