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
package com.ibm.sbt.services.client.connections.communities.util;

public class Messages {
	
	public static String	InvalidArgument_1						= "Invalid Input : Community passed is null";
	public static String	CommunityServiceException_1				= "Exception occurred in method";
	public static String	PublicCommunitiesException				= "Problem occurred while fetching public communities";
	public static String	CommunityException						= "Error getting community with id: {0}";
	public static String	CommunityMembersException				= "Problem occurred while fetching members of a community with id: {0}";
	public static String	MyCommunitiesException					= "Problem occurred while fetching My communities";
	public static String	MyCommunityFilesException				= "Problem occurred while fetching community files";
	public static String	UploadCommunitiesException				= "Problem occurred while uploading file to communities";
	public static String	DownloadCommunitiesException			= "Problem occurred while downloading file from communities";
	public static String	SubCommunitiesException					= "Problem occurred while fetching Sub communities of Community with id : {0}";
	public static String	CommunityBookmarksException				= "Problem occurred while fetching bookmarks of Community with id : {0}";
	public static String	CommunityForumTopicsException			= "Problem occurred while fetching forum topics of Community with id : {0}";
	public static String	CreateCommunityForumTopicException		= "Problem occurred while creating forum topic for Community with id : {0}";
	public static String	CommunityInvitationsException			= "Problem occurred while fetching invitations";
	public static String	CreateInvitationException				= "Problem occurred while creating invitation";
	public static String	AcceptInvitationException				= "Problem occurred while accepting invitation";
	public static String	DeclineInvitationException				= "Problem occurred while declining invitation";
	public static String	CreateCommunityException				= "Error creating community";
	public static String	CreateCommunityPayloadException			= "Error creating community Payload";
	public static String	UpdateCommunityException				= "Error updating community";
	public static String	NullCommunityIdUserIdOrRoleException	= "Community Id or userid or role was null";
	public static String	NullFileId								= "File Id was null";
	public static String	NullCommunityIdOrUserIdException		= "Community Id or userid was null";
	public static String	GetMemberException						= "Error geting member {0} in community with Id {1}";
	public static String	AddMemberException						= "Error adding member {0} to community {1}";
	public static String	UpdateMemberException					= "Error updating member {0} to community {1}";
	public static String	RemoveMemberException					= "Error removing member {0} from community {1}";
	public static String	NullCommunityIdException				= "No CommunityId Passed";	
	public static String	DeleteCommunityException				= "Error Deleting Community {0}";	
	public static String	NullCommunityObjectException			= "Community Object was null";	
	public static String	CommunityInfo_2							= "resolved Community URL :";
	public static String	CommunityInfo_3							= "Error encountered in deleting Community";
	public static String	CommunityInfo_4							= "Error encountered in removing member";
	public static String	CommunityInfo_5							= "Error encountered in adding member";
	public static String	CommunityInfo_6							= "Error encountered in updating community";
	public static String	CommunityInfo_7							= "Error encountered in creating community";
	public static String	CommunityInfo_8							= "A community already exists with this title {0}";
	public static String	CommunityInfo_9							= "empty response from server";
	public static String	CommunityInfo_10						= "returning requested community";
	public static String	CommunityInfo_11						= "Exception occurred while parsing response feed to create Member Entries";
	public static String	CommunityInfo_12						= "Exception occurred while parsing response feed to create Community Entries";
	public static String	CommunityInfo_13						= "Exception occurred while parsing response feed to create Community Bookmarks";
	public static String	CommunityInfo_14						= "Exception occurred while parsing response feed to create Community Forum Topics";

}
