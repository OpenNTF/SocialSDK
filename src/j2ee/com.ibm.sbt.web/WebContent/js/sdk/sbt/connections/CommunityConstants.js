/*
 * © Copyright IBM Corp. 2012
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
 * Definition of constants for ActivityStreamService.
 */
define([],function() {
	return sbt.connections.communityConstants = {
		_methodName : {
			"createCommunity" : "createCommunity",
			"updateCommunity" : "updateCommunity",
			"deleteCommunity" : "deleteCommunity",
			"addMember"		  : "addMember",
			"removeMember"    : "removeMember",
			"getCommunity"    : "getCommunity",
			"getMember"		  : "getMember"
		},
		sbtErrorCodes:{
			badRequest         :400
		},
		sbtErrorMessages:{
			null_communityId        :"Undefined community Id",
			null_memberId        	:"Undefined member Id",
			null_community			:"Undefined community",
			args_object				:"Invalid argument",
			args_member				:"Invalid member",
			args_community			:"Invalid community"
			
		},
		_xpath : {
				"communityUuid"		:"/a:entry/snx:communityUuid",
				"title"				:"/a:entry/a:title",
				"summary"			:"/a:entry/a:summary[@type='text']",
				"communityUrl"		:"/a:entry/a:link[@rel='self']/@href",
				"logoUrl"			:"/a:entry/a:link[@rel='http://www.ibm.com/xmlns/prod/sn/logo']/@href",
				"tags"				:"/a:entry/a:category/@term",
				"content"			:"/a:entry/a:content[@type='html']"			
		},
		_xpath_member : {
				"name"				:"/a:entry/a:contributor/a:name",
				
				
		},
		 _xpath_communities_Feed : {
				"entry"				:"/a:feed/a:entry",
				"id"				:"snx:communityUuid",
				"title"				:"a:title",
		},
		 _xpath_community_Members_Feed : {
				"entry"				:"/a:feed/a:entry",
				"id"				:"a:contributor/snx:userid",
				"name"				:"a:contributor/a:name"
		},
		communityServiceEntity : {
				"communities"	:"/communities",
				"community"		:"/community",
		},		
		communitiesType : {
				"members"				:"/members",
				"subCommunities"		:"",
				"bookmarks"				:"",
				"invitees"				:"",
				"public"				:"/all",
				"my"					:"/my"
		}
	};
});