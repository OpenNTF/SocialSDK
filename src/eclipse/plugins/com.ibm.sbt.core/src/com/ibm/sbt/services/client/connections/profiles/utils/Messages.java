package com.ibm.sbt.services.client.connections.profiles.utils;

/**
 * Class used to retrieve translatable message values from the associated properties file.
 * 
 * @author Swati Singh
 */
public class Messages {

	public static String	InvalidArgument_1		= "Required input parameter is missing : id";
	public static String	InvalidArgument_2		= "Required input parameter is missing : connectionId";
	public static String	InvalidArgument_3		= "Invalid Input  : Null profile was passed";
	public static String	InvalidArgument_4		= "Required input parameter is missing  : source id is missing";
	public static String	InvalidArgument_5		= "Required input parameter is missing  : target id is missing";
	public static String	GetProfileInfo_1		= "returning  requested profile";
	public static String	GetProfileInfo_2		= "empty response from server for requested profile";
	public static String	GetProfileInfo_3		= "Error while loading a profile";
	public static String	GetProfileInfo_4		= "Error encountered while updating profile";
	public static String	ProfileServiceException_1= "Exception occurred in method";
	public static String	UpdateProfileInfo_1		= "Extension of file being uploaded is may be null";
	public static String	PayloadInfo_1			= "Empty Payload Map provided.";
	public static String	ProfilesException_1		= "Exception occurred in method while fetching User's Profile";
	public static String	ProfileError_1			= "Profile Object : Data is null";
	public static String	ProfileError_2			= "Error in method";
	public static String	ProfileInfo_1			= "Cache Hit - Object found in Cache : ";
	public static String	ProfileInfo_2			= "Cache Miss - Object not found in Cache : ";
	public static String	ProfileInfo_4			= "Adding fetched Profile to Cache";
	public static String	ProfileInfo_5			= "Error executing xpath query";
	public static String	ProfileInfo_6			= "get method, returning the result : ";
	public static String	ProfileInfo_7			= "resolved Profiles URL :";
	public static String	ProfileInfo_8 			= "Error converting xml to String";
	public static String	ProfileInfo_9 			= "Error encountered while updating photo";
	public static String 	SendInviteMsg 			= "Please accept this invitation to be in my network of Connections colleagues.";
	public static String    DeleteInviteError		= "Error encountered in deleting invite";
	public static String    AcceptInviteError       = "Error encountered in accepting invite";
	

	private Messages() {
	}
}
