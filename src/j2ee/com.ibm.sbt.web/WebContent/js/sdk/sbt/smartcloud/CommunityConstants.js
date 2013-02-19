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
define(["sbt/smartcloud/core"],function(sbt) {
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
			"entry"				:"/a:entry",
			"communityUuid"		:"snx:communityUuid",
			"title"				:"a:title",
			"summary"			:"a:summary[@type='text']",
			"communityUrl"		:"a:link[@rel='self']/@href",
			"logoUrl"			:"a:link[@rel='http://www.ibm.com/xmlns/prod/sn/logo']/@href",
			"tags"				:"a:category/@term",
			"content"			:"a:content[@type='html']"			
		},
		xpath_feed_community : {
			"entry"				:"/a:feed/a:entry",			
		},
		xpath_member : {
				"entry"				:"/a:feed/a:entry",
				"name"				:"a:contributor/a:name"
		},
		xpath_feed_member : {
				"entry"				:"/a:feed/a:entry"				
		}
	};
});