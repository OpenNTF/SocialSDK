package com.ibm.sbt.services.client.connections.profiles.utils;

/**
 * Class used to retrieve translatable message values from the associated properties file.
 * 
 * @author Swati Singh
 */
public class Messages {

	public static String	InvalidArgument_1				= "Required input parameter is missing : id";
	public static String	InvalidArgument_2				= "Required input parameter is missing : connectionId";
	public static String	InvalidArgument_3				= "Invalid Input  : Profile passed is null";
	public static String	InvalidArgument_4				= "Required input parameter is missing  : source id is missing";
	public static String	InvalidArgument_5				= "Required input parameter is missing  : target id is missing";
	public static String	InvalidArgument_6				= "Invalid Input  : Connection passed is null";
	
	public static String	ProfileServiceException_1		= "Exception occurred in method";
	public static String	ProfileException				= "Error getting profile with identifier : {0}";
	public static String	SearchException					= "Problem occurred while searching profiles";
	public static String	ColleaguesException				= "Problem occurred while getting colleagues of user with identifier : {0}";
	public static String	CheckColleaguesException		= "Problem occurred in checking if two users are colleagues";
	public static String	CommonColleaguesException		= "Problem occurred in getting common colleagues of users {0} and {1}";
	public static String	ReportingChainException			= "Problem occurred in getting report chain of user with identifier : {0}";
	public static String	DirectReportsException			= "Problem occurred in getting direct reports of user with identifier : {0}";
	public static String	ConnectionsByStatusException	= "Problem occurred while getting connections by status for user with identifier : {0}"; 
	public static String	SendInviteException				= "Problem occurred in sending Invite to user with identifier : {0}, please check if there is already a pending invite for this user"; 
	public static String	SendInvitePayloadException		= "Error creating Send Invite Payload";
	public static String 	SendInviteMsg 					= "Please accept this invitation to be in my network of Connections colleagues.";
	public static String	AcceptInviteException			= "Problem occurred in accepting Invite with connection Id : {0}"; 
	public static String	AcceptInvitePayloadException	= "Error creating Accept Invite Payload";
	public static String	DeleteInviteException			= "Problem occurred in deleting Invite with connection Id : {0}";
	public static String	UpdateProfilePhotoException		= "Problem occurred in Updating Profile Photo";
	public static String	UpdateProfileException			= "Problem occurred in Updating Profile ";
	public static String	DeleteProfileException			= "Problem occurred in deleting Profile of user with identifier : {0}";
	public static String	CreateProfileException			= "Problem occurred while creating Profile. Please check if profile already exists. Please check if logged in user has admin rights.";
	public static String	CreateProfilePayloadException	= "Error in create Profile Payload";
	public static String  TagsException          = "Problem occurred while getting tags of user with identifier : {0}";
	
}
