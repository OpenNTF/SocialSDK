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
 * Social Business Toolkit SDK. Definition of constants for ForumService.
 */
define([ "../lang", "./ConnectionsConstants" ], function(lang,conn) {

    return lang.mixin(conn, {
    	
    	url: {
        	my: "forums/atom/forums/my",
        	publicForums: "forums/atom/forums/public"
        },
        
        /**
         * XPath expressions used when parsing a Connections Profiles ATOM feed
         */
        ForumFeedXPath : conn.ConnectionsFeedXPath,
        
        ForumXPath:{
        	
        	title: "a:title",
        	id: "a:id",
        	lastUpdated: "a:updated",
        	authorName: "a:author/a:name",
        	authorId: "a:author/snx:userid",
        	authorEmail: "a:author/a:email",
        	forumURL: "a:link[@rel='alternate']/@href",
        	threadCount: "a:link[@rel='replies']/@thr:count"
        },
    	
    }); 
});