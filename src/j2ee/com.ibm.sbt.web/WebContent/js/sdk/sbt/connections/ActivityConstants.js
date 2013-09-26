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
 * Definition of constants for ActivityService.
 */
define([ "../lang", "./ConnectionsConstants" ], function(lang,conn) {

    return lang.mixin(conn, {
    	
    	ActivityNodeTypes : {
    		Activity : "activity",
    		Chat : "chat",
    		Email : "email",
    		Entry : "entry",
    		EntryTemplate : "entrytemplate",
    		Reply : "reply",
    		Section : "section",
    		ToDo : "todo"    		
    	},
    	
    	ActivityNodeUrl : "/${activities}/service/atom2/activitynode?activityNodeUuid={activityNodeId}",
    	
        /**
         * XPath expressions used when parsing a Connections Activities ATOM feed
         */
    	ActivitiesFeedXPath : conn.ConnectionsFeedXPath,
    	
    	PersonFieldXPath : {
    		name : "a:name",
    		userId : "snx:userid",
    		email : "a:email"
    		
    	},
    	
    	FileFieldXPath : {    		
    		url : "a:link[@rel='enclosure']/@href",
    		type : "a:link[@rel='enclosure']/@type",
    		size : "a:link[@rel='enclosure']/@size",
    		length : "a:link/@length",
    	},
    	
    	LinkFieldXPath : {
    		url : "a:link/@href",
    		title : "a:link/@title"
    	},
    	 
    	TextFieldXPath : {
    		summary : "a:summary",
    	},    	   
    	
    	
    	/**
         * XPath expressions to be used when reading an activity Node entry
         */
        ActivityNodeXPath : {   
        	entry : "/a:entry",
        	uid : "a:id",
        	activityId : "snx:activity",
        	title : "a:title",
        	updated : "a:updated",
            published : "a:published",
            categoryFlagCompleted : "a:category[@term='completed']/@label",
            categoryFlagTemplate : "a:category[@term='template']/@label",
            categoryFlagDelete : "a:category[@term='deleted']/@label",
            
            authorName : "a:author/a:name",			
			authorUserId : "a:author/snx:userid",
			authorEmail : "a:author/a:email",
			authorUserState : "a:author/snx:userState",
			authorLdapid : "a:author/snx:ldapid",
			
			contributorName : "a:contributor/a:name",			
			contributorUserId : "a:contributor/snx:userid",
			contributorEmail : "a:contributor/a:email",
			contributorUserState : "a:contributor/snx:userState",
			contributorLdapid : "a:contributor/snx:ldapid",
			
			type : "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/type']/@label",
			priority : "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/priority']/@label",
			
			coummunityId : "snx:communityUuid",
			communityUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/container']/@href",					    	
			
			dueDate : "snx:duedate",
			membersUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/member-list']/@href",
			historyUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/history']/@href",
			templatesUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/templates']/@href",
			
			editUrl : "a:link[@rel='edit']/@href",
			selfUrl : "a:link[@rel='self']/@href",
			recentUpdatesUrl : "a:link[@rel='alternate']/@href",
			
			position : "snx:position",
			depth : "snx:depth",
			permissions : "snx:permissions",
			iconUrl : "snx:icon",
			content : "a:content",
			tags : "a:category[not(@scheme)]/@term",
			
			inReplyToId : "thr:in-reply-to/@ref",
			inReplyToUrl : "thr:in-reply-to/@href",
			inReplyToActivity : "thr:in-reply-to/snx:activity",
			
			assignedToName : "snx:assignedto/@name", 
			assignedToUserId : "snx:assignedto/@userid",
			assignedToEmail : "snx:assignedto",
			
			textFields : "snx:field[@type='text']",
			linkFields : "snx:field[@type='link']",
			personFields : "snx:field[@type='person']",
			dateFields : "snx:field[@type='date']",
			fileFields : "snx:field[@type='file']"	
					
        },        
        
        
		/**
         * Search for content in all of the activities, both completed and active, that matches a specific criteria.
         */
        AtomActivitiesEverything : "/${activities}/service/atom2/everything",
        
        /**
         * Get a feed of all active activities that match a specific criteria.
         */
        AtomActivitiesMy : "${activities}/service/atom2/activities",

        /**
         * Search for a set of completed activities that match a specific criteria.
         */
        AtomActivitiesCompleted : "${activities}/service/atom2/completed",
        
        AtomActivityNode :  "${activities}/service/atom2/activitynode",
        
        AtomActivityNodeTrash : "${activities}/service/atom2/trashednode",
        
        AtomCreateActivityNode : "${activities}/service/atom2/activity"

    }, conn);
});