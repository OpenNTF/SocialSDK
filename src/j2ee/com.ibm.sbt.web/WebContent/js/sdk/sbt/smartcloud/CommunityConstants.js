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
 * Definition of constants for CommunitiesService.
 */
define([],function() {
	return sbt.smartcloud.communityConstants = {
		entityServiceBaseUrl	: "/communities/service/atom",
		serviceEntity : {
				"communities"	:"/communities",
				"community"		:"/community",
		},		
		entityType : {
				"members"				:"/members",
				"subCommunities"		:"",
				"bookmarks"				:"",
				"invitees"				:"",
				"public"				:"/all",
				"my"					:"/my",
				"instance"				:"/instance"
		},
		sbtErrorMessages:{
			null_communityId        :"Null community Id",
			null_memberId        	:"Null member Id",
		},
		xpath_community : {
			"communityUuid"		:"/a:entry/snx:communityUuid",
			"title"				:"/a:entry/a:title",
			"summary"			:"/a:entry/a:summary[@type='text']",
			"communityUrl"		:"/a:entry/a:link[@rel='self']/@href",
			"logoUrl"			:"/a:entry/a:link[@rel='http://www.ibm.com/xmlns/prod/sn/logo']/@href",
			"tags"				:"/a:entry/a:category/@term",
			"content"			:"/a:entry/a:content[@type='html']"			
		},
		xpath_feed_community : {
			"entry"				:"/a:feed/a:entry",
			"id"				:"a:id",
			"title"				:"a:title",
		},
		xpath_member : {
				"name"				:"/a:entry/a:contributor/a:name"
		},
		xpath_feed_member : {
				"entry"				:"/a:feed/a:entry",
				"id"				:"a:contributor/snx:userid",
				"name"				:"a:contributor/a:name"
		}
	};
});