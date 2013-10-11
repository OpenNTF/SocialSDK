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
package com.ibm.sbt.services.client.connections.files.util;

/**
 * Class used to retrieve translatable message values from the associated properties file.
 * 
 * @author Vimal Dhupar
 */
public class Messages {

    public static String Invalid_FileEntry                         = "Invalid Input parameter : FileEntry";
    public static String Invalid_FileId                            = "Invalid Input parameter : FileId";
    public static String Invalid_UserId                            = "Input parameter UserId is Empty";
    public static String Invalid_CollectionId                      = "Invalid Input parameter : CollectionId";
    public static String Invalid_File                              = "Invalid Argument : File to be uploaded";
    public static String Invalid_CommentId                         = "Invalid Input parameter : CommentId";
    public static String Invalid_CommunityId                       = "Invalid Input parameter : CommunityId";
    public static String Invalid_CommunityLibraryId                = "Invalid Input parameter : CommunityLibraryId";
    public static String MyCommunityFilesException				   = "Problem occurred while fetching community files";
    public static String Invalid_ContentId                         = "Invalid Input parameter : ContentId";
    public static String Invalid_VersionId                         = "Invalid Input parameter : versionId.";
    public static String Message_RetrievalError                    = "Retrieving Atom Entry document of the original File.";
    public static String InvalidArgument_VersionLabel              = "Invalid Input parameter : versionLabel.";
    public static String InvalidArgument_FlagWhat                  = "Invalid Input parameter : flagWhat. Mention what needs to be flagged. Possible values : {file/comment}";
    public static String InvalidArgument_Generic                   = "Invalid Parameter";
    public static String MessageNoFileNameGiven                    = "No File name specified. Checking for request Body.";
    public static String MessageNoRequestBody                      = "No Request Body. Invalid call to upload.";
    public static String MessageGenericException                   = "Exception occurred in method";
    public static String MessageNonceValue                         = "Nonce returned from Server : ";
    public static String MessageEmptyPayload                       = "Empty Payload Map provided.";
    public static String Invalid_SubscriberId                      = "Invalid Value : SubscriberId";
    public static String MessageExceptionInReadingObject           = "Exception occurred in method while fetching object";
    public static String MessageNullProfileData                    = "Profile Object : Data is null";
    public static String MessageGenericError                       = "Error in method";
    public static String MessageCacheHit                           = "Cache Hit - Object found in Cache : ";
    public static String MessageCacheMiss                          = "Cache Miss - Object not found in Cache : ";
    public static String MessageCacheAdded                         = "Adding fetched Object to Cache";
    public static String MessagePutDataInObject                    = "Setting the fetched Json Data to the Entry Object";
    public static String MessageGetResult                          = "get method, returning the result : ";
    public static String MessageExceptionInUpload                  = "Error uploading the file";
    public static String MessageExceptionInUpdate	               = "Error updating the file";
    public static String Invalid_Name                              = "A null name was passed";
    public static String Invalid_Stream                            = "A null stream was passed";
    public static String MessageCannotReadFile                     = "Cannot open the file {0}";
    public static String MessageExceptionInRestoreFile             = "Error sending restore data";
    public static String MessageExceptionInPinningFolder           = "Error pinning the folder";
    public static String MessageExceptionInPinningFile             = "Error pinning the file";
    public static String MessageExceptionInLockingFile             = "Error locking the file";
    public static String MessageExceptionInGettingNonce            = "Error getting the nonce";
    public static String MessageExceptionInFetchingServiceDocument = "Error obtaining the service document";
    public static String MessageExceptionInFlaggingInappropriate   = "Error flagging the item as inappropriate";
    public static String MessageExceptionInDeleteFolder            = "Error deleting the folder";
    public static String MessageExceptionInDeletingFileShare       = "Error removing the sharing";
    public static String MessageExceptionInDeletingComment         = "Error deleting the comment";
    public static String MessageExceptionInDeletingFile            = "Error deleting the file";
    public static String MessageExceptionInCreatingFolder          = "Error creating the folder";
    public static String MessageExceptionInCreatingComment         = "Error creating the comment";
    public static String MesssageExceptionInFlaggingFile           = "Error flagging the file";
    public static String MessageExceptionInFlaggingComment         = "Error flagging the comment";
    public static String MessageExceptionInStatusChange            = "Error changing the approval status";

    private Messages() {
    }
}
